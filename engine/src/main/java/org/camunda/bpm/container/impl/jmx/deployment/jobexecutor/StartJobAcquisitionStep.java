/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.container.impl.jmx.deployment.jobexecutor;

import static org.camunda.bpm.container.impl.jmx.deployment.Attachments.PROCESS_APPLICATION;

import org.camunda.bpm.application.AbstractProcessApplication;
import org.camunda.bpm.container.impl.jmx.JmxRuntimeContainerDelegate.ServiceTypes;
import org.camunda.bpm.container.impl.jmx.kernel.MBeanDeploymentOperation;
import org.camunda.bpm.container.impl.jmx.kernel.MBeanDeploymentOperationStep;
import org.camunda.bpm.container.impl.jmx.kernel.MBeanServiceContainer;
import org.camunda.bpm.container.impl.jmx.services.JmxManagedJobExecutor;
import org.camunda.bpm.container.impl.metadata.spi.JobAcquisitionXml;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.impl.jobexecutor.JobExecutor;
import org.camunda.bpm.engine.impl.jobexecutor.RuntimeContainerJobExecutor;

/**
 * <p>Deployment operation step responsible for starting a JobEexecutor</p> 
 * 
 * @author Daniel Meyer
 *
 */
public class StartJobAcquisitionStep extends MBeanDeploymentOperationStep {
  
  protected final JobAcquisitionXml jobAcquisitionXml;

  public StartJobAcquisitionStep(JobAcquisitionXml jobAcquisitionXml) {
    this.jobAcquisitionXml = jobAcquisitionXml;
    
  }

  public String getName() {
    return "Start job acquisition '"+jobAcquisitionXml.getName()+"'";
  }

  public void performOperationStep(MBeanDeploymentOperation operationContext) {
    
    final MBeanServiceContainer serviceContainer = operationContext.getServiceContainer();
    final AbstractProcessApplication processApplication = operationContext.getAttachment(PROCESS_APPLICATION);
        
    ClassLoader configurationClassloader = null;
    
    if(processApplication != null) {
      configurationClassloader = processApplication.getProcessApplicationClassloader();      
    } else {
      configurationClassloader = ProcessEngineConfiguration.class.getClassLoader();      
    }
    
    String configurationClassName = jobAcquisitionXml.getJobExecutorClassName();
    
    if(configurationClassName == null || configurationClassName.isEmpty()) {
      configurationClassName = RuntimeContainerJobExecutor.class.getName();
    }
    
    // create & instantiate the job executor class    
    Class<? extends JobExecutor> jobExecutorClass = loadJobExecutorClass(configurationClassloader, configurationClassName);
    JobExecutor jobExecutor = instantiateJobExecutor(jobExecutorClass);
            
    // construct service for job executor
    JmxManagedJobExecutor jmxManagedJobExecutor = new JmxManagedJobExecutor(jobExecutor);
    
    // deploy the job executor service into the container
    serviceContainer.startService(ServiceTypes.JOB_EXECUTOR, jobAcquisitionXml.getName(), jmxManagedJobExecutor);
  }
  

  protected JobExecutor instantiateJobExecutor(Class<? extends JobExecutor> configurationClass) {
    try {
      return configurationClass.newInstance();
      
    } catch (InstantiationException e) {
      throw new ProcessEngineException("Could not instantiate job executor class", e);
    } catch (IllegalAccessException e) {
      throw new ProcessEngineException("IllegalAccessException while instantiating job executor class", e);
    }
  }

  @SuppressWarnings("unchecked")
  protected Class<? extends JobExecutor> loadJobExecutorClass(ClassLoader processApplicationClassloader, String jobExecutorClassname) {
    try {
      return (Class<? extends JobExecutor>) processApplicationClassloader.loadClass(jobExecutorClassname);
    } catch (ClassNotFoundException e) {
      throw new ProcessEngineException("Could not load job executor class",e);
    }
  }

}

package org.camunda.bpm.engine.impl.form.generic;

import java.util.concurrent.Callable;
import org.camunda.bpm.application.ProcessApplicationReference;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.impl.application.ProcessApplicationManager;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;

/**
 *
 * @author Michael Siebers <info@msiebers.de>
 */
public class ApplicationSwitchUtil {

    public static Object getValue(final Expression defaultValue, final ProcessDefinitionEntity processDefinition, final VariableScope variableScope) {
        if (defaultValue == null) {
            return null;
        }
        String deploymentId = processDefinition.getProcessDefinition().getDeploymentId();

        ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        ProcessApplicationManager processApplicationManager = processEngineConfiguration.getProcessApplicationManager();

        ProcessApplicationReference pa = processApplicationManager.getProcessApplicationForDeployment(deploymentId);

        Object value = null;

        if (pa != null && !pa.getName().equals(Context.getCurrentProcessApplication())) {
            value = Context.executeWithinProcessApplication(new Callable<Object>() {
                public Object call() throws Exception {
                    return defaultValue.getValue(variableScope);
                }
            }, pa);
        } else {
            value = defaultValue.getValue(variableScope);
        }

        return value;
    }
}

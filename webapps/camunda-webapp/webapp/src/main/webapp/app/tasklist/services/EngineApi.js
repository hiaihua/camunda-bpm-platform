ngDefine('tasklist.services', [
  'angular'
], function(module, angular) {

  var EngineApiFactory = function($resource, Uri) {

    function EngineApi() {

      this.taskList = $resource(Uri.appUri("engine://engine/:engine/task/:id/:operation/:suboperation"), { id: "@id" } , {
        claim : { method: 'POST', params : { operation: "claim" }},
        unclaim : { method: 'POST', params : { operation: "unclaim" }},
        delegate : { method: 'POST', params : { operation: "delegate" }},
        resolve : { method: 'POST', params : { operation: "resolve" }},
        complete : { method: 'POST', params : { operation: "complete" }},
        completeGenericForm : { method: 'POST', params : { operation: "form", suboperation: "complete" }},
        resolveGenericForm : { method: 'POST', params : { operation: "form", suboperation: "resolve" }}
      });

      var forms = $resource(Uri.appUri("engine://engine/:engine/:context/:id/:action"), { id: "@id" } , {
        startForm : { method: 'GET', params : { context: "process-definition", action: 'startForm' }},
        taskForm : { method: 'GET', params : { context: "task", action: 'form' }},
        genericTaskForm : { method: 'GET', params : { context: "task", action: 'form'}}
      });

      this.taskList.getForm = function(data, fn) {
          var response = forms.taskForm(data, fn);
          console.log('getForm', response, data, fn);
        return response;
      };

      this.taskList.getGenericForm = function(data, fn) {
          var response = forms.genericTaskForm(data, fn);
          console.log('getGenericForm', response, data, fn);
        return response;
      };

      this.taskCount = $resource(Uri.appUri("engine://engine/:engine/task/count"));
      this.processDefinitions = $resource(Uri.appUri("engine://engine/:engine/process-definition/:id/:operation"), { id: "@id" }, {
          xml : { method: 'GET', params : { operation: "xml" }}
      });

      this.processDefinitions.getStartForm = function(data, fn) {
        return forms.startForm(data, fn);
      };

      this.processDefinitions.startInstance = function(data, fn) {
        data = angular.extend(data, { operation : "start" });

        return this.save(data, fn);
      };

      this.groups = $resource(Uri.appUri("engine://engine/:engine/identity/groups"));

      this.processInstance = $resource(Uri.appUri("engine://engine/:engine/process-instance/:id/:operation"), { id: "@id" } , {
        variables : { method: 'GET', params : { operation: "variables" }}
      });

    };

    EngineApi.prototype.getProcessDefinitions = function() {
      return this.processDefinitions;
    };

    EngineApi.prototype.getTaskList = function () {
      return this.taskList;
    };

    EngineApi.prototype.getTaskCount = function () {
      return this.taskCount;
    };

    EngineApi.prototype.getColleagueCount = function (userId) {
      return this.taskCount.get({ assignee: userId });
    };

    EngineApi.prototype.getGroupTaskCount = function(groupId) {
      return this.taskCount.get({ candidateGroup: groupId });
    };

    EngineApi.prototype.getGroups = function(userId) {
      return this.groups.get({ userId: userId });
    };

    EngineApi.prototype.getProcessInstance = function() {
      return this.processInstance;
    };

    return new EngineApi();
  };

  EngineApiFactory.$inject = ["$resource", "Uri"];

  module.factory("EngineApi", EngineApiFactory);
});
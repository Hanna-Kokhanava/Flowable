import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 31.01.2019
 */
public class FlowableManager {
    private ProcessEngine processEngine;
    private RepositoryService repositoryService;
    private RuntimeService runtimeService;

    private ProcessDefinition processDefinition;

    public void initEngineAndCreateServices() {
        processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault().buildProcessEngine();
        repositoryService = processEngine.getRepositoryService();
        runtimeService = processEngine.getRuntimeService();
    }

    public Deployment createDeployment() {
        return repositoryService.createDeployment()
                .addClasspathResource("flowable-example.bpmn20.xml")
                .deploy();
    }

    public void createProcessDefinition(Deployment deployment) {
        processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();
        System.out.println("Found Process definition : " + processDefinition.getName());
    }

    public ProcessInstance createProcessInstance() {
        //TODO actually come from form as process variables
        Map<String, Object> vars = new HashMap<>();
        vars.put("employee", "Hanna Kokhanava");
        //TODO go through the User Task
        return runtimeService.startProcessInstanceByKey(processDefinition.getKey(), vars);
    }

    public void completeActualListOfTasksForManager() {
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("managers").list();
        System.out.println("You have " + tasks.size() + " tasks:");

        Map<String, Object> variables = new HashMap<>();
        variables.put("approved", true);

        for (Task task : tasks) {
            System.out.println("Task name : " + task.getName());
            System.out.println("Employee name : " + taskService.getVariable(task.getId(), "employee"));
            //TODO it triggers next steps, in this case - Service Task
            taskService.complete(task.getId(), variables);
        }
    }

    public void printDeployInfo() {
        List<Deployment> deployments = repositoryService.createDeploymentQuery().list();
        for (Deployment deployment : deployments) {
            System.out.println("Category : " + deployment.getCategory());
            System.out.println("Name : " + deployment.getName());
            System.out.println("ID : " + deployment.getId());
            System.out.println("Key : " + deployment.getKey());
        }
    }
}

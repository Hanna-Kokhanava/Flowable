import com.google.inject.Guice;
import com.google.inject.Inject;
import org.ModulesInjection;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.repository.Deployment;
import org.managers.FlowableManager;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Created on 31.01.2019
 */
public class FlowableTest {
    @Inject
    private FlowableManager manager;

    {
        Guice.createInjector(new ModulesInjection())
                .injectMembers(this);
    }

    @BeforeTest(description = "Init Flowable Engine and create necessary services")
    public void init() {
        manager.initEngineAndCreateServices();
    }

    @Test(description = "Deploy process definition")
    public void deployAndCreateProcessDefinition() {
        Deployment deployment = manager.createDeployment();
        manager.createProcessDefinition(deployment);
    }

    @Test(description = "Flowable example test", dependsOnMethods = {"deployAndCreateProcessDefinition"})
    public void startProcessAndCompleteTasks() {
        manager.startProcessInstance();
        manager.completeActualTasksForGroup("managers");
    }

    @Test(description = "Print Model information on screen", dependsOnMethods = {"startProcessAndCompleteTasks"})
    public void printBpmnCurrentModelInfo() {
        for (Process process : manager.getModel().getProcesses()) {
            System.out.println("Process Name : " + process.getName());
            for (FlowElement element : process.getFlowElements()) {
                System.out.println("Type of flow element : " + element.getClass());
            }
        }
    }
}

import org.flowable.engine.repository.Deployment;
import org.testng.annotations.Test;

/**
 * Created on 31.01.2019
 */
public class FlowableTest {

    @Test(description = "Flowable example test")
    public void testFlowable() {
        FlowableManager manager = new FlowableManager();
        manager.initEngineAndCreateServices();

        Deployment deployment = manager.createDeployment();
        manager.createProcessDefinition(deployment);

        manager.createProcessInstance();

        manager.completeActualListOfTasksForManager();


    }
}

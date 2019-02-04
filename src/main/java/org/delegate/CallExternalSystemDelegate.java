package org.delegate;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

/**
 * Created on 01.02.2019
 */
public class CallExternalSystemDelegate implements JavaDelegate {

    //TODO just to model ServiceTask execution
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("Calling the external system for employee : "
                + execution.getVariable("employee"));
    }
}

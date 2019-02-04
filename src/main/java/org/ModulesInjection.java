package org;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.managers.FlowableManager;

/**
 * Created on 04.02.2019
 */
public class ModulesInjection extends AbstractModule {

    @Override
    protected void configure() {
        bind(FlowableManager.class).in(Scopes.SINGLETON);
    }
}

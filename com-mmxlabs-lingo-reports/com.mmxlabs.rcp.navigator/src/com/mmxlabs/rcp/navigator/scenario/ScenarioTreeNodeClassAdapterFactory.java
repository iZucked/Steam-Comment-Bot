package com.mmxlabs.rcp.navigator.scenario;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;

import scenario.Scenario;

/**
 * Needed to make Eclipse Core Expressions resolve the adapt method for arbitary objects
 * @author sg
 *
 */
public class ScenarioTreeNodeClassAdapterFactory implements IAdapterFactory {

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {

		
		if (adaptableObject instanceof ScenarioTreeNodeClass) {
			ScenarioTreeNodeClass node = (ScenarioTreeNodeClass) adaptableObject;
			if (Scenario.class.isAssignableFrom(adapterType)) {
				return node.getScenario();
			} else if (IContainer.class.isAssignableFrom(adapterType)) {
				return node.getContainer();
			} else if (IResource.class.isAssignableFrom(adapterType)) {
				return node.getResource();
			}
		}

		return null;
	}

	@Override
	public Class[] getAdapterList() {
		return new Class[] { Scenario.class, IContainer.class, IResource.class };
	}

}

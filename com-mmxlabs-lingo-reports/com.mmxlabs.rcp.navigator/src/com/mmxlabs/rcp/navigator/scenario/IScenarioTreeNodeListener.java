package com.mmxlabs.rcp.navigator.scenario;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;

import scenario.Scenario;

import com.mmxlabs.jobcontroller.core.IManagedJob;

/**
 * Interface used to notify viewers of events on a {@link ScenarioTreeNodeClass}
 * 
 * @author Simon Goodall
 * 
 */
public interface IScenarioTreeNodeListener {

	/**
	 * Send notification of a {@link IResource} change of instance
	 * 
	 * @param node
	 */
	void resourceChanged(ScenarioTreeNodeClass node, IResource oldResource,
			IResource newResource);

	/**
	 * Send notification of a {@link IContainer} change of instance
	 * 
	 * @param node
	 */
	void containerChanged(ScenarioTreeNodeClass node, IContainer oldContainer,
			IContainer newContainer);
	
	/**
	 * Send notification of a {@link Scenario} change of instance
	 * 
	 * @param node
	 */
	void scenarioChanged(ScenarioTreeNodeClass node, Scenario oldScenario,
			Scenario newScenario);

	/**
	 * Send notification of a {@link IManagedJob} change of instance
	 * 
	 * @param node
	 */
	void jobChanged(ScenarioTreeNodeClass node, IManagedJob oldJob,
			IManagedJob newJob);
}

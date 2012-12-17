/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports;

import java.util.Collection;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public interface IScenarioViewerSynchronizerOutput {
	/**
	 * Returns a collection of objects which are of interest to 
	 * a particular report, for all scenarios which have been changed.
	 * Each collected element is going to be a row in a tabular report.
	 *  
	 * @return
	 */
	public Collection<Object> getCollectedElements();
	
	/**
	 * Tells you which scenario instance an object from the getCollectedElements()
	 * method came from.
	 * 
	 * @param object
	 * @return
	 */
	public ScenarioInstance getScenarioInstance(Object object);
	
	/**
	 * Is the object from the "pinned" scenario (used as a reference scenario)?
	 * 
	 * @param object
	 * @return
	 */
	public boolean isPinned(Object object);
	
	/**
	 * Gets the MMX root object for a particular object from the 
	 * getCollectedElements list.
	 * 
	 * @param object
	 * @return
	 */
	public MMXRootObject getRootObject(Object object);
	
	/**
	 * Returns the MMX root objects for all the currently selected scenarios
	 * regardless of whether or not there are getCollectedElements objects coming
	 * from them. 
	 *  
	 * @return
	 */
	public Collection<MMXRootObject> getRootObjects();
}
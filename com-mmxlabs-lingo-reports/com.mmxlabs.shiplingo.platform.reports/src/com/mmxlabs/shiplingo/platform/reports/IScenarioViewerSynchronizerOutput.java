/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports;

import java.util.Collection;

import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public interface IScenarioViewerSynchronizerOutput {
	/**
	 * Returns a collection of objects which are of interest to a particular report, for all scenarios which have been changed. Each collected element is going to be a row in a tabular report.
	 * 
	 * @return
	 */
	public Collection<Object> getCollectedElements();

	/**
	 * Tells you which scenario instance an object from the {@link #getCollectedElements()} method came from.
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
	 * Gets the {@link LNGScenarioModel} object for a particular object from the {@link #getCollectedElements()} list.
	 * 
	 * @param object
	 * @return
	 * @since 4.0
	 */
	public LNGScenarioModel getLNGScenarioModel(Object object);

	/**
	 * Gets the {@link LNGPortfolioModel} object for a particular object from the {@link #getCollectedElements()} list.
	 * 
	 * @param object
	 * @return
	 * @since 4.0
	 */
	public LNGPortfolioModel getLNGPortfolioModel(Object object);

	/**
	 * Returns the {@link LNGScenarioModel} objects for all the currently selected scenarios regardless of whether or not there are getCollectedElements objects coming from them.
	 * 
	 * @return
	 * @since 4.0
	 */
	public Collection<LNGScenarioModel> getLNGScenarioModels();

	/**
	 * @since 4.0
	 */
	public Collection<LNGPortfolioModel> getLNGPortfolioModels();
}
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.export;

import scenario.Scenario;
import scenario.schedule.Schedule;

import com.mmxlabs.lngscheduler.emf.extras.ModelEntityMap;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;

/**
 * Extension interface for exporting to EMF.
 * 
 * @author hinton
 * 
 */
public interface IExporterExtension {
	/**
	 * Called before any export stuff happens
	 * 
	 * @param inputScenario
	 * @param output
	 * @param entities
	 * @param annotatedSolution
	 */
	void startExporting(final Scenario inputScenario, Schedule output, final ModelEntityMap entities, final IAnnotatedSolution annotatedSolution);

	/**
	 * Called after the main export has happened, and after any other prior export steps have happened.
	 */
	void finishExporting();
}

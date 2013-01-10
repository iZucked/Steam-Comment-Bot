/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.mmxcore.MMXRootObject;
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
	void startExporting(final MMXRootObject inputScenario, Schedule output, final ModelEntityMap entities, final IAnnotatedSolution annotatedSolution);

	/**
	 * Called after the main export has happened, and after any other prior export steps have happened.
	 */
	void finishExporting();
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
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
	 * @param output
	 * @param modelEntityMap
	 * @param annotatedSolution
	 */
	void startExporting(@NonNull Schedule output, @NonNull ModelEntityMap modelEntityMap, @NonNull IAnnotatedSolution annotatedSolution);

	/**
	 * Called after the main export has happened, and after any other prior export steps have happened.
	 */
	void finishExporting();
}

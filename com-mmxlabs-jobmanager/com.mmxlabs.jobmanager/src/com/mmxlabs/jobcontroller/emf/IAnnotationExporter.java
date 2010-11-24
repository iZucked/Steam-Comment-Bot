/**
 *
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 */
package com.mmxlabs.jobcontroller.emf;

import scenario.Scenario;
import scenario.schedule.Schedule;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;

/**
 * An interface for classes which export the information contained
 * in a particular annotation into the output EMF.
 * 
 * @author hinton
 *
 */
public interface IAnnotationExporter {

	/**
	 * @param annotatedSolution
	 */
	void setAnnotatedSolution(
			IAnnotatedSolution<ISequenceElement> annotatedSolution);

	/**
	 * @param output
	 */
	void setOutput(Schedule output);

	/**
	 * @param inputScenario
	 */
	void setScenario(Scenario inputScenario);

	/**
	 * @param entities
	 */
	void setModelEntityMap(ModelEntityMap entities);

	void init();
	
	/**
	 * @param annotation
	 */
	void exportAnnotation(ISequenceElement element, Object annotation);
	
}

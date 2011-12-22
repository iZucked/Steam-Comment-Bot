/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.export;

import java.util.Map;

import scenario.Scenario;
import scenario.schedule.Schedule;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.fleetallocation.AllocatedVessel;

import com.mmxlabs.lngscheduler.emf.extras.ModelEntityMap;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * An interface for classes which export the information contained in a particular annotation into the output EMF.
 * 
 * @author hinton
 * 
 */
public interface IAnnotationExporter {

	/**
	 * @param annotatedSolution
	 */
	void setAnnotatedSolution(IAnnotatedSolution annotatedSolution);

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
	 * Create a scheduled event from the given annotation.
	 * 
	 * Returns null if there is no scheduled event.
	 * 
	 * @param element
	 * @param annotation
	 * @param key
	 * @return
	 */
	ScheduledEvent export(final ISequenceElement element, final Map<String, Object> annotations, final AllocatedVessel vessel);
}

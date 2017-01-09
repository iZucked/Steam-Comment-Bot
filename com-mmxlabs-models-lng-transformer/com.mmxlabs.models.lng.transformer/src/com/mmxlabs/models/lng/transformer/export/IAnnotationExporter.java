/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import java.util.Map;

import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IElementAnnotation;
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
	 * @param entities
	 */
	void setModelEntityMap(ModelEntityMap modelEntityMap);

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
	Event export(final ISequenceElement element, final Map<String, IElementAnnotation> annotations);
}

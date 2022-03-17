/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;

/**
 * Convenience class for holding some useful variables.
 * 
 * @author hinton
 * 
 */
public abstract class BaseAnnotationExporter implements IAnnotationExporter {
	protected IAnnotatedSolution annotatedSolution;
	protected Schedule output;
	protected ModelEntityMap modelEntityMap;

	protected final ScheduleFactory scheduleFactory = SchedulePackage.eINSTANCE.getScheduleFactory();

	protected final ScheduleFactory factory = SchedulePackage.eINSTANCE.getScheduleFactory();

	@Override
	public void init() {
		// Sub-classes can override
	}

	@Override
	public void setAnnotatedSolution(final IAnnotatedSolution annotatedSolution) {
		this.annotatedSolution = annotatedSolution;
	}

	@Override
	public void setOutput(final Schedule output) {
		this.output = output;
	}

	@Override
	public void setModelEntityMap(final ModelEntityMap modelEntityMap) {
		this.modelEntityMap = modelEntityMap;
	}

}

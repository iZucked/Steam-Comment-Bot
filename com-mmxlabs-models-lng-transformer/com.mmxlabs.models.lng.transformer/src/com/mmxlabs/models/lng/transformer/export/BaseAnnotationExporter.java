/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

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

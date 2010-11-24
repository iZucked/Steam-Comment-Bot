/**
 *
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 */
package com.mmxlabs.jobcontroller.emf;

import java.util.HashMap;
import java.util.Map;

import scenario.Scenario;
import scenario.schedule.FuelQuantity;
import scenario.schedule.FuelType;
import scenario.schedule.Schedule;
import scenario.schedule.ScheduleFactory;
import scenario.schedule.SchedulePackage;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

/**
 * Convenience class for holding some useful variables.
 * 
 * @author hinton
 * 
 */
public abstract class BaseAnnotationExporter implements IAnnotationExporter {
	protected IAnnotatedSolution<ISequenceElement> annotatedSolution;
	protected Scenario inputScenario;
	protected Schedule output;
	protected ModelEntityMap entities;
	protected final ScheduleFactory factory = SchedulePackage.eINSTANCE
			.getScheduleFactory();
	private static final Map<FuelUnit, scenario.schedule.FuelUnit> fuelUnits =
		new HashMap<FuelUnit, scenario.schedule.FuelUnit>();
	
	private static final Map<FuelComponent, FuelType> fuelTypes = 
		new HashMap<FuelComponent, FuelType>();
	
	static {
		fuelTypes.put(FuelComponent.Base, FuelType.BASE_FUEL);
		fuelTypes.put(FuelComponent.Base_Supplemental, FuelType.BASE_FUEL);
		fuelTypes.put(FuelComponent.IdleBase, FuelType.BASE_FUEL);
		fuelTypes.put(FuelComponent.NBO, FuelType.NBO);
		fuelTypes.put(FuelComponent.IdleNBO, FuelType.NBO);
		fuelTypes.put(FuelComponent.FBO, FuelType.FBO);
		fuelUnits.put(FuelUnit.M3, scenario.schedule.FuelUnit.M3);
		fuelUnits.put(FuelUnit.MT, scenario.schedule.FuelUnit.MT);
		fuelUnits.put(FuelUnit.MMBTu, scenario.schedule.FuelUnit.MMB_TU);
	}

	@Override
	public void setAnnotatedSolution(
			IAnnotatedSolution<ISequenceElement> annotatedSolution) {
		this.annotatedSolution = annotatedSolution;
	}

	@Override
	public void setOutput(Schedule output) {
		this.output = output;
	}

	@Override
	public void setScenario(Scenario inputScenario) {
		this.inputScenario = inputScenario;
	}

	@Override
	public void setModelEntityMap(ModelEntityMap entities) {
		this.entities = entities;
	}

	/**
	 * Create a fuel quantity
	 * 
	 * @param fc
	 * @param consumption
	 * @param cost
	 * @return
	 */
	protected FuelQuantity createFuelQuantity(final FuelComponent fc, final long consumption,
			final long cost) {
		final FuelQuantity fq = factory.createFuelQuantity();
		
		fq.setQuantity(consumption);
		fq.setTotalPrice(cost);
		fq.setUnitPrice(cost/consumption); // TODO float?
		
		fq.setFuelType(fuelTypes.get(fc));
		fq.setFuelUnit(fuelUnits .get(fc.getDefaultFuelUnit()));
		
		return fq;
	}
}

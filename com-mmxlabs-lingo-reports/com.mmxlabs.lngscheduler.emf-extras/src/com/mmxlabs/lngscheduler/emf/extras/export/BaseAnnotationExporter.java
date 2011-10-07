/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.export;

import java.util.HashMap;
import java.util.Map;

import scenario.Scenario;
import scenario.schedule.Schedule;
import scenario.schedule.ScheduleFactory;
import scenario.schedule.SchedulePackage;
import scenario.schedule.events.EventsFactory;
import scenario.schedule.events.EventsPackage;
import scenario.schedule.events.FuelMixture;
import scenario.schedule.events.FuelQuantity;
import scenario.schedule.events.FuelType;

import com.mmxlabs.lngscheduler.emf.extras.ModelEntityMap;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.Calculator;
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
	protected final ScheduleFactory scheduleFactory = SchedulePackage.eINSTANCE.getScheduleFactory();

	protected final EventsFactory factory = EventsPackage.eINSTANCE.getEventsFactory();

	private static final Map<FuelUnit, scenario.schedule.events.FuelUnit> fuelUnits = new HashMap<FuelUnit, scenario.schedule.events.FuelUnit>();

	private static final Map<FuelComponent, FuelType> fuelTypes = new HashMap<FuelComponent, FuelType>();

	static {
		fuelTypes.put(FuelComponent.Base, FuelType.BASE_FUEL);
		fuelTypes.put(FuelComponent.Base_Supplemental, FuelType.BASE_FUEL);
		fuelTypes.put(FuelComponent.IdleBase, FuelType.BASE_FUEL);

		fuelTypes.put(FuelComponent.PilotLight, FuelType.PILOT_LIGHT);
		fuelTypes.put(FuelComponent.IdlePilotLight, FuelType.PILOT_LIGHT);

		fuelTypes.put(FuelComponent.NBO, FuelType.NBO);
		fuelTypes.put(FuelComponent.IdleNBO, FuelType.NBO);

		fuelTypes.put(FuelComponent.FBO, FuelType.FBO);

		fuelTypes.put(FuelComponent.Cooldown, FuelType.COOLDOWN);

		fuelUnits.put(FuelUnit.M3, scenario.schedule.events.FuelUnit.M3);
		fuelUnits.put(FuelUnit.MT, scenario.schedule.events.FuelUnit.MT);
		fuelUnits.put(FuelUnit.MMBTu, scenario.schedule.events.FuelUnit.MMB_TU);
	}

	@Override
	public void setAnnotatedSolution(IAnnotatedSolution<ISequenceElement> annotatedSolution) {
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
	 * TODO some units need scaling.
	 * 
	 * @param fc
	 * @param consumption
	 * @param cost
	 * @return
	 */
	private FuelQuantity createFuelQuantity(final FuelComponent fc, final long consumption, final long cost) {
		final FuelQuantity fq = factory.createFuelQuantity();

		fq.setQuantity(consumption);
		fq.setTotalPrice(cost);
		// TODO float?
		fq.setUnitPrice(consumption == 0 ? 0 : cost / consumption);

		fq.setFuelType(fuelTypes.get(fc));
		fq.setFuelUnit(fuelUnits.get(fc.getDefaultFuelUnit()));

		return fq;
	}

	protected void addFuelQuantity(final FuelMixture mixture, final FuelComponent component, final long consumption, final long cost) {
		final FuelType fuelType = fuelTypes.get(component);

		for (final FuelQuantity fq : mixture.getFuelUsage()) {
			if (fq.getFuelType().equals(fuelType)) {
				// add to existing
				// TODO this will accumulate rounding error worse than batch
				// adding with a final division.
				final long newQuantity = fq.getQuantity() + consumption;
				final long newTotalPrice = fq.getTotalPrice() + cost;
				fq.setQuantity(newQuantity);
				fq.setTotalPrice(newTotalPrice);
				fq.setUnitPrice(newQuantity == 0 ? 0 : newTotalPrice / newQuantity);
				return;
			}
		}

		mixture.getFuelUsage().add(createFuelQuantity(component, consumption, cost));
	}

	protected void scaleFuelQuantities(final FuelMixture mixture) {
		for (final FuelQuantity fq : mixture.getFuelUsage()) {
			fq.setQuantity(fq.getQuantity() / Calculator.ScaleFactor);
			fq.setTotalPrice(fq.getTotalPrice() / Calculator.ScaleFactor);
		}
	}
}

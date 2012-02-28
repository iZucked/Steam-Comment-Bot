/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.Calculator;
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
	protected MMXRootObject inputScenario;
	protected Schedule output;
	protected ModelEntityMap entities;
	protected final ScheduleFactory scheduleFactory = SchedulePackage.eINSTANCE.getScheduleFactory();

	protected final ScheduleFactory factory = SchedulePackage.eINSTANCE.getEventsFactory();

	private static final Map<FuelUnit, scenario.schedule.events.FuelUnit> fuelUnits = new HashMap<FuelUnit, scenario.schedule.events.FuelUnit>();

	private static final Map<FuelComponent, FuelType> fuelTypes = new HashMap<FuelComponent, FuelType>();

	private static final Map<FuelComponent, FuelPurpose> fuelPurposes = new HashMap<FuelComponent, FuelPurpose>();

	static {
		fuelTypes.put(FuelComponent.Base, FuelType.BASE_FUEL);
		fuelTypes.put(FuelComponent.Base_Supplemental, FuelType.BASE_FUEL);
		fuelTypes.put(FuelComponent.IdleBase, FuelType.BASE_FUEL);

		fuelTypes.put(FuelComponent.PilotLight, FuelType.BASE_FUEL);
		fuelTypes.put(FuelComponent.IdlePilotLight, FuelType.BASE_FUEL);

		fuelTypes.put(FuelComponent.NBO, FuelType.NBO);
		fuelTypes.put(FuelComponent.IdleNBO, FuelType.NBO);

		fuelTypes.put(FuelComponent.FBO, FuelType.FBO);

		fuelTypes.put(FuelComponent.Cooldown, FuelType.COOLDOWN);

		fuelUnits.put(FuelUnit.M3, scenario.schedule.events.FuelUnit.M3);
		fuelUnits.put(FuelUnit.MT, scenario.schedule.events.FuelUnit.MT);
		fuelUnits.put(FuelUnit.MMBTu, scenario.schedule.events.FuelUnit.MMB_TU);

		fuelPurposes.put(FuelComponent.Base, FuelPurpose.TRAVEL);
		fuelPurposes.put(FuelComponent.Base_Supplemental, FuelPurpose.TRAVEL);
		fuelPurposes.put(FuelComponent.NBO, FuelPurpose.TRAVEL);
		fuelPurposes.put(FuelComponent.FBO, FuelPurpose.TRAVEL);
		fuelPurposes.put(FuelComponent.IdleBase, FuelPurpose.IDLE);
		fuelPurposes.put(FuelComponent.IdleNBO, FuelPurpose.IDLE);
		fuelPurposes.put(FuelComponent.PilotLight, FuelPurpose.PILOT_LIGHT);
		fuelPurposes.put(FuelComponent.IdlePilotLight, FuelPurpose.PILOT_LIGHT);
		fuelPurposes.put(FuelComponent.Cooldown, FuelPurpose.COOLDOWN);
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
	public void setScenario(final MMXRootObject inputScenario) {
		this.inputScenario = inputScenario;
	}

	@Override
	public void setModelEntityMap(final ModelEntityMap entities) {
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
		fq.setPurpose(fuelPurposes.get(fc));
		
		return fq;
	}

	protected void addFuelQuantity(final FuelMixture mixture, final FuelComponent component, final long consumption, final long cost) {
		final FuelType fuelType = fuelTypes.get(component);
		final FuelPurpose purpose = fuelPurposes.get(component);
		for (final FuelQuantity fq : mixture.getFuelUsage()) {
			if (fq.getFuelType().equals(fuelType) && fq.getPurpose().equals(purpose)) {
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
		final Iterator<FuelQuantity> iterator = mixture.getFuelUsage().iterator();
		while (iterator.hasNext()) {
			final FuelQuantity fq = iterator.next();
			if (fq.getQuantity() == 0) {
				iterator.remove();
			} else {
			fq.setQuantity(fq.getQuantity() / Calculator.ScaleFactor);
			fq.setTotalPrice(fq.getTotalPrice() / Calculator.ScaleFactor);
			}
		}
	}
}

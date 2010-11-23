/**
 *
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.allocation;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IAnnotations;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.components.AbstractSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator.CargoAllocation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.AllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.ComparingCargoAllocator;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * A fitness component which does cargo allocation within the scheduler loop.
 * This will be slower but better.
 * 
 * @author hinton
 * 
 */
public class CargoAllocatingSchedulerComponent<T> extends
		AbstractSchedulerFitnessComponent<T> {
	private IVesselProvider vesselProvider;
	private final ICargoAllocator<T> allocator = new ComparingCargoAllocator<T>();

	private final String vesselProviderKey, volumeLimitProviderKey;
	private IPortSlotProvider<T> portSlotProvider;
	private final String portSlotProviderKey;

	/**
	 * @param dcpVesselprovider
	 * @param dcpTotalvolumelimitprovider
	 * @param cargoAllocationComponentName
	 * @param cargoSchedulerFitnessCore
	 */
	public CargoAllocatingSchedulerComponent(final String name,
			final String dcpVesselprovider,
			final String dcpTotalvolumelimitprovider,
			final String dcpPortslotprovider, final IFitnessCore<T> core) {
		super(name, core);
		vesselProviderKey = dcpVesselprovider;
		volumeLimitProviderKey = dcpTotalvolumelimitprovider;
		portSlotProviderKey = dcpPortslotprovider;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(IOptimisationData<T> data) {
		super.init(data);
		vesselProvider = data.getDataComponentProvider(vesselProviderKey,
				IVesselProvider.class);

		portSlotProvider = data.getDataComponentProvider(portSlotProviderKey,
				IPortSlotProvider.class);
		allocator.setTotalVolumeLimitProvider(data.getDataComponentProvider(
				volumeLimitProviderKey, ITotalVolumeLimitProvider.class));
		allocator.init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent
	 * #startEvaluation()
	 */
	@Override
	public void startEvaluation() {
		allocator.reset();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent
	 * #startSequence(com.mmxlabs.optimiser.core.IResource, boolean)
	 */
	@Override
	public void startSequence(final IResource resource,
			final boolean sequenceHasChanged) {
		loadDetails = null;
		vesselCapacity = vesselProvider.getVessel(resource).getVesselClass()
				.getCargoCapacity();
		loadTime = 0;
	}

	private PortDetails loadDetails = null;

	private long vesselCapacity;
	private long capacityUsedForFuel;
	private int loadTime;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent
	 * #evaluateObject(java.lang.Object, int)
	 */
	@Override
	public boolean nextObject(Object object, int time) {
		if (object instanceof PortDetails) {
			final PortDetails portDetails = (PortDetails) object;
			final IPortSlot slot = portDetails.getPortSlot();
			if (slot instanceof ILoadSlot) {
				loadDetails = portDetails;
				loadTime = time;
			} else if (slot instanceof IDischargeSlot) {
				assert loadDetails != null;

				allocator.addCargo(loadDetails, portDetails, loadTime, time,
						capacityUsedForFuel, vesselCapacity);

				loadDetails = null;
				capacityUsedForFuel = 0;
			}
		}
		return true;
	}

	@Override
	public boolean nextVoyagePlan(final VoyagePlan voyagePlan, final int time) {
		capacityUsedForFuel = voyagePlan.getLNGFuelVolume();
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent
	 * #endSequence()
	 */
	@Override
	public boolean endSequence() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent
	 * #endEvaluationAndGetCost()
	 */
	@Override
	public long endEvaluationAndGetCost() {
		allocator.solve();

		return setLastEvaluatedFitness(-allocator.getProfit()); // cost is just
																// the p&l.
	}

	/**
	 * Do extra annotations for cargo allocation
	 */
	@Override
	public void endEvaluationAndAnnotate(IAnnotatedSolution<T> solution) {
		allocator.solve(); //don't store the value

		final IAnnotations<T> elementAnnotations = solution
				.getElementAnnotations();

		// now add some more data for each load slot
		for (final CargoAllocation allocation : allocator.getAllocations()) {
			final T loadElement = portSlotProvider
					.getElement(allocation.loadSlot);
			final T dischargeElement = portSlotProvider
					.getElement(allocation.dischargeSlot);

			// make annotations
			final AllocationAnnotation annotation = new AllocationAnnotation();
			annotation.setDischargeVolume(allocation.loadVolume);
			annotation.setUnitProfit(allocation.unitPrice);
			annotation.setTotalProfit(Calculator.multiply(allocation.unitPrice,
					allocation.loadVolume));
			annotation.setDischargeCV(0);// TODO fix this

			elementAnnotations.setAnnotation(loadElement,
					SchedulerConstants.AI_volumeAllocationInfo, annotation);
			elementAnnotations.setAnnotation(dischargeElement,
					SchedulerConstants.AI_volumeAllocationInfo, annotation);
		}
	}
}

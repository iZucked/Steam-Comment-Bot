/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.export;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.emf.common.util.EList;

import scenario.Scenario;
import scenario.cargo.Cargo;
import scenario.cargo.CargoType;
import scenario.contract.FixedPricePurchaseContract;
import scenario.contract.IndexPricePurchaseContract;
import scenario.contract.PurchaseContract;
import scenario.contract.SalesContract;
import scenario.fleet.Vessel;
import scenario.fleet.VesselClass;
import scenario.schedule.CargoAllocation;
import scenario.schedule.Schedule;
import scenario.schedule.ScheduleFactory;
import scenario.schedule.ScheduleFitness;
import scenario.schedule.SchedulePackage;
import scenario.schedule.Sequence;
import scenario.schedule.events.Idle;
import scenario.schedule.events.Journey;
import scenario.schedule.events.PortVisit;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.events.SlotVisit;
import scenario.schedule.fleetallocation.AllocatedVessel;
import scenario.schedule.fleetallocation.FleetVessel;
import scenario.schedule.fleetallocation.SpotVessel;

import com.mmxlabs.lngscheduler.emf.extras.ModelEntityMap;
import com.mmxlabs.lngscheduler.emf.extras.pandl.ProfitAndLossCalculator;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IAnnotations;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * A utility class for turning an annotated solution into some EMF
 * representation, for the presentation layer.
 * 
 * At the moment this has the simplistic idea that every annotation corresponds
 * to a scheduled event in the EMF output, which is almost certainly not true.
 * 
 * @author hinton
 * 
 */
public class AnnotatedSolutionExporter {
	private List<IAnnotationExporter> exporters = new LinkedList<IAnnotationExporter>();
	final ScheduleFactory factory = SchedulePackage.eINSTANCE
			.getScheduleFactory();

	private boolean exportRuntimeAndFitness = false;

	public boolean isExportRuntimeAndFitness() {
		return exportRuntimeAndFitness;
	}

	public void setExportRuntimeAndFitness(boolean exportRuntimeAndFitness) {
		this.exportRuntimeAndFitness = exportRuntimeAndFitness;
	}

	public AnnotatedSolutionExporter() {
		final VisitEventExporter visitExporter = new VisitEventExporter();
		exporters.add(new IdleEventExporter(visitExporter));
		exporters.add(new JourneyEventExporter());
		exporters.add(visitExporter);
	}

	public Schedule exportAnnotatedSolution(final Scenario inputScenario,
			final ModelEntityMap entities,
			final IAnnotatedSolution<ISequenceElement> annotatedSolution) {
		final IOptimisationData<ISequenceElement> data = annotatedSolution
				.getContext().getOptimisationData();
		final IVesselProvider vesselProvider = data.getDataComponentProvider(
				SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);
		final IAnnotations<ISequenceElement> elementAnnotations = annotatedSolution
				.getElementAnnotations();

		final Schedule output = factory.createSchedule();

		// go through the annotated solution and build stuff for the EMF;
		entities.setScenario(inputScenario);

		// prepare exporters
		for (final IAnnotationExporter exporter : exporters) {
			exporter.setOutput(output);
			exporter.setScenario(inputScenario);
			exporter.setModelEntityMap(entities);
			exporter.setAnnotatedSolution(annotatedSolution);

			exporter.init();
		}

		final List<Sequence> sequences = output.getSequences();
		final List<IResource> resources = annotatedSolution.getSequences()
				.getResources();
		// Create sequences and run other exporters

		final Map<IVesselClass, AtomicInteger> counter = new HashMap<IVesselClass, AtomicInteger>();
		@SuppressWarnings("unchecked")
		final Map<IResource, Map<String, Long>> sequenceFitnesses = annotatedSolution
				.getGeneralAnnotation(SchedulerConstants.G_AI_fitnessPerRoute,
						Map.class);
		for (final IResource resource : resources) {
			final IVessel vessel = vesselProvider.getVessel(resource);
			final AllocatedVessel outputVessel;
			switch (vessel.getVesselInstanceType()) {
			case TIME_CHARTER:
			case FLEET:
				final FleetVessel fv = scenario.schedule.fleetallocation.FleetallocationPackage.eINSTANCE
						.getFleetallocationFactory().createFleetVessel();

				fv.setVessel(entities.getModelObject(vessel, Vessel.class));

				outputVessel = fv;
				break;
			case SPOT_CHARTER:
				if (annotatedSolution.getSequences().getSequence(resource)
						.size() < 2)
					continue;
				final SpotVessel sv = scenario.schedule.fleetallocation.FleetallocationPackage.eINSTANCE
						.getFleetallocationFactory().createSpotVessel();

				final AtomicInteger ai = counter.get(vessel.getVesselClass());
				int ix = 0;

				if (ai == null) {
					counter.put(vessel.getVesselClass(), new AtomicInteger(ix));
				} else {
					ix = ai.incrementAndGet();
				}

				sv.setVesselClass(entities.getModelObject(
						vessel.getVesselClass(), VesselClass.class));

				sv.setIndex(ix);

				outputVessel = sv;
				break;
			default:
				outputVessel = null;
				break;
			}

			final Sequence eSequence = factory.createSequence();
			sequences.add(eSequence);

			{
				// set sequence fitness values
				final EList<ScheduleFitness> eSequenceFitness = eSequence
						.getFitness();
				final Map<String, Long> sequenceFitness = sequenceFitnesses
						.get(resource);
				for (final Map.Entry<String, Long> e : sequenceFitness
						.entrySet()) {
					final ScheduleFitness sf = ScheduleFactory.eINSTANCE
							.createScheduleFitness();
					sf.setName(e.getKey());
					sf.setValue(e.getValue());
					eSequenceFitness.add(sf);
				}
			}
			output.getFleet().add(outputVessel);
			eSequence.setVessel(outputVessel);

			final EList<ScheduledEvent> events = eSequence.getEvents();

			Comparator<ScheduledEvent> eventComparator = new Comparator<ScheduledEvent>() {
				@Override
				public int compare(ScheduledEvent arg0, ScheduledEvent arg1) {
					if (arg0.getStartTime().before(arg1.getStartTime())) {
						return -1;
					} else if (arg0.getStartTime().after(arg1.getStartTime())) {
						return 1;
					}

					// idle must come after journey
					if (arg0 instanceof PortVisit)
						return (arg1 instanceof PortVisit) ? 0 : -1;
					if (arg1 instanceof PortVisit)
						return 1;

					if (arg0 instanceof Journey)
						return (arg1 instanceof Journey) ? 0 : -1;
					if (arg1 instanceof Journey)
						return 1;

					return 0;
				}
			};

			final IPortTypeProvider<ISequenceElement> portTypeProvider = data
					.getDataComponentProvider(
							SchedulerConstants.DCP_portTypeProvider,
							IPortTypeProvider.class);

			final List<ScheduledEvent> eventsForElement = new ArrayList<ScheduledEvent>();
			for (final ISequenceElement element : annotatedSolution
					.getSequences().getSequence(resource)) {
				// get annotations for this element
				final Map<String, Object> annotations = elementAnnotations
						.getAnnotations(element);

				// filter virtual ports out here?
				
				for (final IAnnotationExporter exporter : exporters) {
					final ScheduledEvent result = exporter.export(element,
							annotations, outputVessel);
					if (result != null)
						eventsForElement.add(result);
				}

				// this is messy, but we want to be sure stuff is in the right
				// order or it won't make any sense.
				Collections.sort(eventsForElement, eventComparator);
				events.addAll(eventsForElement);
				eventsForElement.clear();
			}
		}

		// now patch up laden/ballast journey references in the cargos
		for (final Sequence eSequence : output.getSequences()) {
			CargoAllocation allocation = null;
			for (final ScheduledEvent event : eSequence.getEvents()) {
				if (event instanceof SlotVisit) {
					final SlotVisit visit = (SlotVisit) event;
					allocation = visit.getCargoAllocation();
				} else if (event instanceof Journey && allocation != null) {
					if (allocation.getLadenLeg() == null) {
						allocation.setLadenLeg((Journey) event);
					} else if (allocation.getBallastLeg() == null) {
						allocation.setBallastLeg((Journey) event);
					}
				} else if (event instanceof Idle && allocation != null) {
					if (allocation.getLadenIdle() == null) {
						allocation.setLadenIdle((Idle) event);
					} else if (allocation.getBallastIdle() == null) {
						allocation.setBallastIdle((Idle) event);
					}
				}
				if (allocation != null && allocation.getBallastLeg() != null
						&& allocation.getLadenLeg() != null
						&& allocation.getLadenIdle() != null
						&& allocation.getBallastIdle() != null)
					allocation = null;
			}
		}

		@SuppressWarnings("unchecked")
		final Map<String, Long> fitnesses = annotatedSolution
				.getGeneralAnnotation(
						OptimiserConstants.G_AI_fitnessComponents, Map.class);

		final Long runtime = annotatedSolution.getGeneralAnnotation(
				OptimiserConstants.G_AI_runtime, Long.class);
		final Integer iterations = annotatedSolution.getGeneralAnnotation(
				OptimiserConstants.G_AI_iterations, Integer.class);

		final EList<ScheduleFitness> outputFitnesses = output.getFitness();

		for (final Map.Entry<String, Long> entry : fitnesses.entrySet()) {
			final ScheduleFitness fitness = factory.createScheduleFitness();
			fitness.setName(entry.getKey());
			fitness.setValue(entry.getValue());
			outputFitnesses.add(fitness);
		}

		if (isExportRuntimeAndFitness()) {
			final ScheduleFitness eRuntime = factory.createScheduleFitness();
			eRuntime.setName("runtime");
			eRuntime.setValue(runtime);

			outputFitnesses.add(eRuntime);

			final ScheduleFitness eIters = factory.createScheduleFitness();
			eIters.setName("iterations");
			eIters.setValue(iterations.longValue());

			outputFitnesses.add(eIters);
		}

		// Process DES cargoes from the input side

		for (final Cargo cargo : inputScenario.getCargoModel().getCargoes()) {
			if (CargoType.DES.equals(cargo.getCargoType())) {
				// we can create a dummy cargo allocation
				// for the P&L calculator to work on

				final CargoAllocation allocation = ScheduleFactory.eINSTANCE
						.createCargoAllocation();

				allocation.setCargoType(CargoType.DES);

				allocation.setLoadSlot(cargo.getLoadSlot());
				allocation.setDischargeSlot(cargo.getDischargeSlot());
				allocation.setDischargeDate(cargo.getDischargeSlot()
						.getWindowStart());
				// TODO check this makes sense
				allocation.setDischargeVolume(cargo.getDischargeSlot()
						.getMaxQuantity());
				// find discharge price per mmbtu
				final SalesContract dischargeContract = (SalesContract) cargo
						.getDischargeSlot().getSlotOrPortContract();
				final float salesPricePerMMBTU = dischargeContract.getIndex()
						.getPriceCurve()
						.getValueAtDate(allocation.getDischargeDate());
				// * dischargeContract.getRegasEfficiency();

				final PurchaseContract purchaseContract = (PurchaseContract) cargo
						.getLoadSlot().getSlotOrPortContract();
				final float purchasePricePerMMBTU;
				if (purchaseContract instanceof FixedPricePurchaseContract) {
					purchasePricePerMMBTU = ((FixedPricePurchaseContract) purchaseContract)
							.getUnitPrice();
				} else if (purchaseContract instanceof IndexPricePurchaseContract) {
					purchasePricePerMMBTU = ((IndexPricePurchaseContract) purchaseContract)
							.getIndex().getPriceCurve()
							.getValueAtDate(allocation.getDischargeDate());
				} else {
					System.err
							.println("A DES load slot should not have a "
									+ purchaseContract.eClass().getName()
									+ " contract");
					continue;
				}

				// get MMBTU / M3 (this has to be a notional value to make any
				// sense)
				// TODO this may not be a sensible way of doing a DES cargo

				final float salesPricePerM3 = salesPricePerMMBTU
						/ cargo.getLoadSlot().getCargoCVvalue();
				final float purchasePricePerM3 = purchasePricePerMMBTU
						/ cargo.getLoadSlot().getCargoCVvalue();

				allocation.setDischargePriceM3(Calculator
						.scaleToInt(salesPricePerM3));
				allocation.setLoadPriceM3(Calculator
						.scaleToInt(purchasePricePerM3));

				output.getCargoAllocations().add(allocation);
			}
		}

		final ProfitAndLossCalculator pAndL = new ProfitAndLossCalculator();

		pAndL.addProfitAndLoss(inputScenario, output, entities);

		return output;
	}
}

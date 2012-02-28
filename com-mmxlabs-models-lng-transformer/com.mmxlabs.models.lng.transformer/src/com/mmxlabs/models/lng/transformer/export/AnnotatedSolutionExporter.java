/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IAnnotations;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * A utility class for turning an annotated solution into some EMF representation, for the presentation layer.
 * 
 * At the moment this has the simplistic idea that every annotation corresponds to a scheduled event in the EMF output, which is almost certainly not true.
 * 
 * @author hinton
 * 
 */
public class AnnotatedSolutionExporter {
	private static final Logger log = LoggerFactory.getLogger(AnnotatedSolutionExporter.class);
	private final List<IAnnotationExporter> exporters = new LinkedList<IAnnotationExporter>();
	final ScheduleFactory factory = SchedulePackage.eINSTANCE.getScheduleFactory();
	final List<IExporterExtension> extensions = new LinkedList<IExporterExtension>();
	
	private boolean exportRuntimeAndFitness = false;

	public boolean isExportRuntimeAndFitness() {
		return exportRuntimeAndFitness;
	}

	public void setExportRuntimeAndFitness(final boolean exportRuntimeAndFitness) {
		this.exportRuntimeAndFitness = exportRuntimeAndFitness;
	}

	public AnnotatedSolutionExporter() {
		final VisitEventExporter visitExporter = new VisitEventExporter();
		exporters.add(new IdleEventExporter(visitExporter));
		exporters.add(new JourneyEventExporter());
		exporters.add(visitExporter);
	}

	public boolean addPlatformExporterExtensions() {
		if (Platform.getExtensionRegistry() == null) {
			log.warn("addPlatformExporterExtensions() called without a platform - skipping");
			return false;
		}
		final String EXTENSION_ID = "com.mmxlabs.lngscheduler.exporter";
		final IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_ID);

		for (final IConfigurationElement e : config) {
			try {
				final Object object = e.createExecutableExtension("exporter");
				if (object instanceof IExporterExtension) {
					addExporterExtension((IExporterExtension) object);
				}
			} catch (final Exception ex) {
				log.error("Exception caught when adding platform exporter extensions", ex);
			}
		}
		return true;
	}
	
	public void addExporterExtension(final IExporterExtension extension) {
		extensions.add(extension);
	}
	
	public Schedule exportAnnotatedSolution(final Scenario inputScenario, final ModelEntityMap entities, final IAnnotatedSolution annotatedSolution) {
		final IOptimisationData data = annotatedSolution.getContext().getOptimisationData();
		final IVesselProvider vesselProvider = data.getDataComponentProvider(SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);
		final IAnnotations elementAnnotations = annotatedSolution.getElementAnnotations();

		final Schedule output = factory.createSchedule();

		// go through the annotated solution and build stuff for the EMF;
		entities.setScenario(inputScenario);

		for (final IExporterExtension extension : extensions) {
			extension.startExporting(inputScenario, output, entities, annotatedSolution);
		}

		// prepare exporters
		for (final IAnnotationExporter exporter : exporters) {
			exporter.setOutput(output);
			exporter.setScenario(inputScenario);
			exporter.setModelEntityMap(entities);
			exporter.setAnnotatedSolution(annotatedSolution);

			exporter.init();
		}

		final List<Sequence> sequences = output.getSequences();
		final List<IResource> resources = annotatedSolution.getSequences().getResources();
		// Create sequences and run other exporters

		final Map<IVesselClass, AtomicInteger> counter = new HashMap<IVesselClass, AtomicInteger>();
		@SuppressWarnings("unchecked")
		final Map<IResource, Map<String, Long>> sequenceFitnesses = annotatedSolution.getGeneralAnnotation(SchedulerConstants.G_AI_fitnessPerRoute, Map.class);
		for (final IResource resource : resources) {
			final IVessel vessel = vesselProvider.getVessel(resource);
			final AllocatedVessel outputVessel;
			switch (vessel.getVesselInstanceType()) {
			case TIME_CHARTER:
			case FLEET:
				final FleetVessel fv = scenario.schedule.fleetallocation.FleetallocationPackage.eINSTANCE.getFleetallocationFactory().createFleetVessel();

				fv.setVessel(entities.getModelObject(vessel, Vessel.class));

				outputVessel = fv;
				break;
			case SPOT_CHARTER:
				if (annotatedSolution.getSequences().getSequence(resource).size() < 2)
					continue;
				final SpotVessel sv = scenario.schedule.fleetallocation.FleetallocationPackage.eINSTANCE.getFleetallocationFactory().createSpotVessel();

				final AtomicInteger ai = counter.get(vessel.getVesselClass());
				int ix = 0;

				if (ai == null) {
					counter.put(vessel.getVesselClass(), new AtomicInteger(ix));
				} else {
					ix = ai.incrementAndGet();
				}

				sv.setVesselClass(entities.getModelObject(vessel.getVesselClass(), VesselClass.class));

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
				final EList<ScheduleFitness> eSequenceFitness = eSequence.getFitness();
				final Map<String, Long> sequenceFitness = sequenceFitnesses.get(resource);
				for (final Map.Entry<String, Long> e : sequenceFitness.entrySet()) {
					final ScheduleFitness sf = ScheduleFactory.eINSTANCE.createScheduleFitness();
					sf.setName(e.getKey());
					sf.setValue(e.getValue());
					eSequenceFitness.add(sf);
				}
			}
			output.getFleet().add(outputVessel);
			eSequence.setVessel(outputVessel);

			final EList<ScheduledEvent> events = eSequence.getEvents();

			final Comparator<ScheduledEvent> eventComparator = new Comparator<ScheduledEvent>() {
				@Override
				public int compare(final ScheduledEvent arg0, final ScheduledEvent arg1) {
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


			final List<ScheduledEvent> eventsForElement = new ArrayList<ScheduledEvent>();
			for (final ISequenceElement element : annotatedSolution.getSequences().getSequence(resource)) {
				// get annotations for this element
				final Map<String, Object> annotations = elementAnnotations.getAnnotations(element);

				// filter virtual ports out here?

				for (final IAnnotationExporter exporter : exporters) {
					final ScheduledEvent result = exporter.export(element, annotations, outputVessel);
					if (result != null) {
						eventsForElement.add(result);
					}
				}

				// this is messy, but we want to be sure stuff is in the right
				// order or it won't make any sense.
				Collections.sort(eventsForElement, eventComparator);
				events.addAll(eventsForElement);
				eventsForElement.clear();
			}
		}

		// patch up idle events with no port
		for (final Sequence eSequence : output.getSequences()) {
			Idle firstIdle = null;
			for (final ScheduledEvent event : eSequence.getEvents()) {
				if (firstIdle != null && firstIdle.getPort() != null)
					break;
				if (event instanceof Idle) {
					firstIdle = (Idle) event;
				}
				if (event instanceof PortVisit) {
					final PortVisit pv = (PortVisit) event;
					if (firstIdle != null && firstIdle.getPort() == null && pv.getPort() != null) {
						firstIdle.setPort(pv.getPort());
					}
				}
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
				if (allocation != null && allocation.getBallastLeg() != null && allocation.getLadenLeg() != null && allocation.getLadenIdle() != null && allocation.getBallastIdle() != null)
					allocation = null;
			}
		}

		@SuppressWarnings("unchecked")
		final Map<String, Long> fitnesses = annotatedSolution.getGeneralAnnotation(OptimiserConstants.G_AI_fitnessComponents, Map.class);

		final Long runtime = annotatedSolution.getGeneralAnnotation(OptimiserConstants.G_AI_runtime, Long.class);
		final Integer iterations = annotatedSolution.getGeneralAnnotation(OptimiserConstants.G_AI_iterations, Integer.class);

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
		//TODO this is all outdated now

		for (final Cargo cargo : inputScenario.getCargoModel().getCargoes()) {
			if (CargoType.DES.equals(cargo.getCargoType())) {
				// we can create a dummy cargo allocation
				// for the P&L calculator to work on

				final CargoAllocation allocation = ScheduleFactory.eINSTANCE.createCargoAllocation();

				allocation.setCargoType(CargoType.DES);

				allocation.setLoadSlot(cargo.getLoadSlot());
				allocation.setLoadDate(cargo.getLoadSlot().getWindowStart());
				allocation.setDischargeSlot(cargo.getDischargeSlot());
				allocation.setDischargeDate(cargo.getDischargeSlot().getWindowStart());
				// TODO check this makes sense
				allocation.setDischargeVolume(cargo.getDischargeSlot().getMaxQuantity());
				// find discharge price per mmbtu
				final SalesContract dischargeContract = (SalesContract) cargo.getDischargeSlot().getSlotOrPortContract(inputScenario);
				final float salesPricePerMMBTU = cargo.getDischargeSlot().isSetFixedPrice() ? cargo.getDischargeSlot().getFixedPrice() : dischargeContract.getIndex().getPriceCurve()
						.getValueAtDate(allocation.getDischargeDate());
				// * dischargeContract.getRegasEfficiency();

				final Contract purchaseContract = cargo.getLoadSlot().getSlotOrPortContract(inputScenario);
				final float purchasePricePerMMBTU;
				if (cargo.getLoadSlot().isSetFixedPrice()) {
					purchasePricePerMMBTU = cargo.getLoadSlot().getFixedPrice();
				} else if (purchaseContract instanceof FixedPricePurchaseContract) {
					purchasePricePerMMBTU = ((FixedPricePurchaseContract) purchaseContract).getUnitPrice();
				} else if (purchaseContract instanceof IndexPricePurchaseContract) {
					purchasePricePerMMBTU = ((IndexPricePurchaseContract) purchaseContract).getIndex().getPriceCurve().getValueAtDate(allocation.getDischargeDate());
				} else {
					// FIXME: for P&L opt - DES cargoes will have a purchase contract (probably based on a price curve) & possibly a fixed price & quantity
					log.error("A DES load slot should not have a " + purchaseContract.eClass().getName() + " contract");
					continue;
				}

				// get MMBTU / M3 (this has to be a notional value to make any
				// sense)
				// TODO this may not be a sensible way of doing a DES cargo

				final float salesPricePerM3 = salesPricePerMMBTU / cargo.getLoadSlot().getCargoOrPortCVValue();
				final float purchasePricePerM3 = purchasePricePerMMBTU / cargo.getLoadSlot().getCargoOrPortCVValue();

				allocation.setDischargePriceM3(Calculator.scaleToInt(salesPricePerM3));
				allocation.setLoadPriceM3(Calculator.scaleToInt(purchasePricePerM3));

				output.getCargoAllocations().add(allocation);
			}
		}

		for (final IExporterExtension extension : extensions) {
			extension.finishExporting();
		}
		//
		// final ProfitAndLossCalculator pAndL = new ProfitAndLossCalculator();
		//
		// pAndL.addProfitAndLoss(inputScenario, output, entities);

		return output;
	}
}

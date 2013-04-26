/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.transformer.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.model.Category;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.IConstraintDescriptor;
import org.eclipse.emf.validation.service.IConstraintFilter;
import org.eclipse.emf.validation.service.ModelValidationService;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.common.Association;
import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.CostComponent;
import com.mmxlabs.models.lng.analytics.DestinationType;
import com.mmxlabs.models.lng.analytics.ShippingCostPlan;
import com.mmxlabs.models.lng.analytics.ShippingCostRow;
import com.mmxlabs.models.lng.analytics.UnitCostLine;
import com.mmxlabs.models.lng.analytics.Visit;
import com.mmxlabs.models.lng.analytics.Voyage;
import com.mmxlabs.models.lng.analytics.transformer.IShippingCostTransformer;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.TransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.ScheduleBuilderModule;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.ValidationHelper;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.FixedPriceContract;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.UnconstrainedCargoAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.AbstractSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.manipulators.SequencesManipulatorModule;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.providers.guice.DataComponentProviderModule;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.LNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * @since 2.0
 */
public class ShippingCostTransformer implements IShippingCostTransformer {

	@Override
	public List<UnitCostLine> evaulateShippingPlan(final MMXRootObject root, final ShippingCostPlan plan, final IProgressMonitor monitor) {
		final IBatchValidator validator = (IBatchValidator) ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
		validator.setOption(IBatchValidator.OPTION_INCLUDE_LIVE_CONSTRAINTS, true);

		validator.addConstraintFilter(new IConstraintFilter() {

			@Override
			public boolean accept(final IConstraintDescriptor constraint, final EObject target) {

				for (final Category cat : constraint.getCategories()) {
					if (cat.getId().equals("com.mmxlabs.models.lng.analytics.validation.constraints.shippingcosts")) {
						return true;
					} else if (cat.getId().endsWith(".base")) {
						return true;
					}
				}
				return false;
			}
		});

		final ValidationHelper helper = new ValidationHelper();
		final IStatus validationStatus = helper.runValidation(validator, new DefaultExtraValidationContext(root), Collections.singleton(plan));
		if (!validationStatus.isOK()) {
			return Collections.emptyList();
		}

		try {
			final PortModel portModel = root.getSubModel(PortModel.class);
			final PricingModel pricing = root.getSubModel(PricingModel.class);

			// Check for valid plan
			if (plan == null || plan.getRows().size() < 2) {
				monitor.setCanceled(true);
				return Collections.emptyList();
			}
			// Check for a valid vessel.
			final Vessel modelVessel = plan.getVessel();
			if (modelVessel == null) {
				monitor.setCanceled(true);
				return Collections.emptyList();
			}

			monitor.beginTask("Creating unit cost matrix", 1);

			final Injector injector = Guice.createInjector(new DataComponentProviderModule(), new ScheduleBuilderModule(), new SequencesManipulatorModule(), createShippingCostModule());
			final ISchedulerBuilder builder = injector.getInstance(ISchedulerBuilder.class);

			final ModelEntityMap entities = injector.getInstance(ModelEntityMap.class);

			/*
			 * Create ports and distances
			 */
			final Association<Port, IPort> ports = new Association<Port, IPort>();
			final ICooldownPriceCalculator nullCalculator = new FixedPriceContract(0);
			for (final Port port : portModel.getPorts()) {
				final IPort optPort = builder.createPort(port.getName(), !port.isAllowCooldown(), nullCalculator);
				ports.add(port, optPort);
				entities.addModelObject(portModel, optPort);
			}
			for (final Route route : portModel.getRoutes()) {
				for (final RouteLine line : route.getLines()) {
					builder.setPortToPortDistance(ports.lookup(line.getFrom()), ports.lookup(line.getTo()), route.getName(), line.getDistance());
				}
			}

			/*
			 * Create vessel class
			 */

			final VesselClass eVc = modelVessel.getVesselClass();

			final IVesselClass vesselClass = TransformerHelper.buildIVesselClass(builder, eVc, plan.getBaseFuelPrice());

			/**
			 * Set up vessel class route parameters
			 */
			for (final Route route : portModel.getRoutes()) {
				VesselClassRouteParameters parametersForRoute = null;
				RouteCost costForRoute = null;
				for (final VesselClassRouteParameters parameters : modelVessel.getVesselClass().getRouteParameters()) {
					if (parameters.getRoute() == route) {
						parametersForRoute = parameters;
						break;
					}
				}
				for (final RouteCost routeCost : pricing.getRouteCosts()) {
					if (routeCost.getRoute() == route && routeCost.getVesselClass() == modelVessel.getVesselClass()) {
						costForRoute = routeCost;
						break;
					}
				}
				for (final RouteLine line : route.getLines()) {
					builder.setPortToPortDistance(ports.lookup(line.getFrom()), ports.lookup(line.getTo()), route.getName(), line.getDistance());
					if (parametersForRoute != null) {
						builder.setVesselClassRouteTransitTime(route.getName(), vesselClass, parametersForRoute.getExtraTransitTime());
						builder.setVesselClassRouteFuel(route.getName(), vesselClass, VesselState.Laden,
								OptimiserUnitConvertor.convertToInternalHourlyRate(parametersForRoute.getLadenConsumptionRate()),
								OptimiserUnitConvertor.convertToInternalHourlyRate(parametersForRoute.getLadenNBORate()));
						builder.setVesselClassRouteFuel(route.getName(), vesselClass, VesselState.Ballast,
								OptimiserUnitConvertor.convertToInternalHourlyRate(parametersForRoute.getBallastConsumptionRate()),
								OptimiserUnitConvertor.convertToInternalHourlyRate(parametersForRoute.getBallastNBORate()));
					}
					if (costForRoute != null) {
						builder.setVesselClassRouteCost(route.getName(), vesselClass, VesselState.Laden, OptimiserUnitConvertor.convertToInternalFixedCost(costForRoute.getLadenCost()));
						builder.setVesselClassRouteCost(route.getName(), vesselClass, VesselState.Ballast, OptimiserUnitConvertor.convertToInternalFixedCost(costForRoute.getBallastCost()));
					}
				}
			}

			/**
			 * Start to build up the voyages
			 */

			// / Build up list of elements to schedule
			final List<IPortSlot> elements = new LinkedList<IPortSlot>();
			IStartEndRequirement startConstraint = null;
			IStartEndRequirement endConstraint = null;
			int startHeelPrice = 0;
			int startHeelCV = 0;
			int startHeelVolume = 0;
			int idx = 0;

			Map<IPortSlot, ShippingCostRow> mapPortSlotToRow = new HashMap<IPortSlot, ShippingCostRow>();

			for (final ShippingCostRow row : plan.getRows()) {

				if (row.getDate() == null) {
					monitor.setCanceled(true);
					return Collections.emptyList();
				}

				if (idx == 0) {

					// Earliest date, record!
					builder.setEarliestDate(row.getDate());
					entities.setEarliestDate(row.getDate());

				}

				final int time = entities.getHoursFromDate(row.getDate());
				final IPort port = ports.lookup(row.getPort());

				if (idx == 0) {
					// Start event

					if (row.getDestinationType() != DestinationType.START) {
						return Collections.emptyList();
						// throw new IllegalStateException("First element must be a START element");
					}
					final ITimeWindow timeWindow = builder.createTimeWindow(time, time);
					startConstraint = builder.createStartEndRequirement(port, timeWindow);
					startHeelCV = OptimiserUnitConvertor.convertToInternalConversionFactor(row.getCvValue());
					startHeelPrice = OptimiserUnitConvertor.convertToInternalConversionFactor(row.getCargoPrice());
					startHeelVolume = OptimiserUnitConvertor.convertToInternalConversionFactor(row.getHeelVolume());
				} else if (idx == plan.getRows().size() - 1) {
					// End Event

					if (row.getDestinationType() != DestinationType.END) {
						return Collections.emptyList();
						// throw new IllegalStateException("First element must be an END element");
					}
					final ITimeWindow timeWindow = builder.createTimeWindow(time, time);
					endConstraint = builder.createStartEndRequirement(port, timeWindow);
				} else {
					if (row.getDestinationType() == DestinationType.START || row.getDestinationType() == DestinationType.END) {
						return Collections.emptyList();
						// throw new IllegalStateException("....");
					}
					final String id = "row-" + idx;
					final ITimeWindow timeWindow = builder.createTimeWindow(time, time + 24);

					final int cargoCVValue = OptimiserUnitConvertor.convertToInternalConversionFactor(row.getCvValue());
					final int gasPrice = OptimiserUnitConvertor.convertToInternalPrice(row.getCargoPrice());
					final int gasVolume = OptimiserUnitConvertor.convertToInternalConversionFactor(row.getHeelVolume());
					final IPortSlot slot;
					if (row.getDestinationType() == DestinationType.LOAD) {
						final ILoadPriceCalculator priceCalculator = new FixedPriceContract(gasPrice);
						slot = builder.createLoadSlot(id, port, timeWindow, 0, gasVolume, priceCalculator, cargoCVValue, row.getPort().getLoadDuration(), false, true, false);
					} else {
						if (row.getDestinationType() == DestinationType.DISCHARGE) {
							final ISalesPriceCalculator priceCalculator = new FixedPriceContract(gasPrice);
							// TODO: find out from Simon Goodall what the min and max cv values are for a ShippingCostRow
							final long minCv = 0;
							final long maxCv = Long.MAX_VALUE;
							slot = builder.createDischargeSlot(id, port, timeWindow, 0, gasVolume, minCv, maxCv, priceCalculator, row.getPort().getDischargeDuration(), false);
						} else {
							// TODO: Need general waypoint type
							slot = builder.createCharterOutEvent(id, timeWindow, port, port, 24, gasVolume, cargoCVValue, gasPrice, 0, 0);
						}
					}
					elements.add(slot);
					mapPortSlotToRow.put(slot, row);
				}
				++idx;
			}

			// Create the vessel now we have the start/end requirements
			final IVessel vessel = builder.createVessel(modelVessel.getName(), vesselClass, new ConstantValueCurve(OptimiserUnitConvertor.convertToInternalHourlyRate(plan.getNotionalDayRate())),
					startConstraint, endConstraint, startHeelVolume, startHeelCV, startHeelPrice);

			/*
			 * Create the sequences object and generate the arrival times based on window start TODO: We could use the sequence scheduler to do this for us.
			 */
			final IOptimisationData data = builder.getOptimisationData();
			final IVesselProvider vesselProvider = data.getDataComponentProvider(SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);
			final IModifiableSequences sequences = new ModifiableSequences(data.getResources());
			final IStartEndRequirementProvider startEndProvider = data.getDataComponentProvider(SchedulerConstants.DCP_startEndRequirementProvider, IStartEndRequirementProvider.class);
			final IPortSlotProvider slotProvider = data.getDataComponentProvider(SchedulerConstants.DCP_portSlotsProvider, IPortSlotProvider.class);

			// Add start/end port slots into the row map.
			{
				IResource r = vesselProvider.getResource(vessel);
				IStartEndRequirementProvider startEndRequirementProvider = data.getDataComponentProvider(SchedulerConstants.DCP_startEndRequirementProvider, IStartEndRequirementProvider.class);
				ISequenceElement startElement = startEndRequirementProvider.getStartElement(r);
				ISequenceElement endElement = startEndRequirementProvider.getEndElement(r);

				mapPortSlotToRow.put(slotProvider.getPortSlot(startElement), plan.getRows().get(0));
				mapPortSlotToRow.put(slotProvider.getPortSlot(endElement), plan.getRows().get(plan.getRows().size() - 1));
			}

			final int[][] arrivalTimes = new int[1][elements.size() + 2];
			int index = 0;
			final IResource resource = vesselProvider.getResource(vessel);
			{
				final IModifiableSequence sequence = sequences.getModifiableSequence(resource);
				// set up sequence and arrival times
				sequence.add(startEndProvider.getStartElement(resource));

				arrivalTimes[0][index++] = startEndProvider.getStartRequirement(resource).getTimeWindow().getStart();
				for (final IPortSlot slot : elements) {
					sequence.add(slotProvider.getElement(slot));
					arrivalTimes[0][index++] = slot.getTimeWindow().getStart();
				}
				sequence.add(startEndProvider.getEndElement(resource));

				arrivalTimes[0][index++] = startEndProvider.getEndRequirement(resource).getTimeWindow().getStart();
			}
			/*
			 * Create a fitness core and evaluate+annotate the sequences
			 */
			final ISequencesManipulator manipulator = injector.getInstance(ISequencesManipulator.class);
			manipulator.init(data);
			manipulator.manipulate(sequences); // this will set the return elements to the right places, and remove the start elements.

			// final VoyagePlanOptimiser optimiser = injector.getInstance(VoyagePlanOptimiser.class);

			final AbstractSequenceScheduler scheduler = new AbstractSequenceScheduler() {
				@Override
				public ScheduledSequences schedule(final ISequences sequences, final IAnnotatedSolution solution) {
					return null;
				}

				@Override
				public ScheduledSequences schedule(final ISequences sequences, final Collection<IResource> affectedResources, final IAnnotatedSolution solution) {
					return null;
				}

				@Override
				public void acceptLastSchedule() {

				}
			};

			injector.injectMembers(scheduler);
			// injector.injectMembers(scheduler);
			// SchedulerUtils.setDataComponentProviders(data, scheduler);
			// scheduler.setVoyagePlanOptimiser(optimiser);
			// scheduler.init();
			// run the scheduler on the sequences
			final ScheduledSequences result = scheduler.schedule(sequences, arrivalTimes);

			if (result == null) {
				return Collections.emptyList();
			}

			final UnconstrainedCargoAllocator aca = injector.getInstance(UnconstrainedCargoAllocator.class);
			aca.setVesselProvider(vesselProvider);

			final Map<VoyagePlan, IAllocationAnnotation> allocations = aca.allocate(result);
			final Iterator<IAllocationAnnotation> allocationIterator = allocations.values().iterator();

			/*
			 * Unpack the annotated solution and create output lines //
			 */

			IAnnotatedSolution solution;

			IAllocationAnnotation currentAllocationAnnotation = null;
			final List<UnitCostLine> lines = new ArrayList<UnitCostLine>();
			for (final ScheduledSequence sequence : result) {
				// final IAllocationAnnotation allocation = allocationIterator.next();
				// create line for plan

				int totalDuration = 0;
				int totalFuelCost = 0;
				int totalRouteCost = 0;
				int totalPortCost = 0;
				final UnitCostLine line = AnalyticsFactory.eINSTANCE.createUnitCostLine();
				final Map<FuelComponent, Integer[]> voyageSums = new EnumMap<FuelComponent, Integer[]>(FuelComponent.class);
				int idxX = 1;
				// final VoyagePlan voyagePlan = sequence.getVoyagePlans().get(0);
				final VoyagePlanIterator itr = injector.getInstance(VoyagePlanIterator.class);
				itr.setVoyagePlans(sequence.getResource(), sequence.getVoyagePlans(), arrivalTimes[0]);

				long totalDischargeVolumeInMMBTu = 0;

				while (itr.hasNextObject()) {
					final Object obj = itr.nextObject();
					idxX++;
					final String idxStr = String.format("%02d", idxX);
					if (obj instanceof VoyageDetails) {
						final VoyageDetails voyageDetails = (VoyageDetails) obj;
						createVoyageCostComponent(
								line.addExtraData("leg" + idxStr, idxStr + " - " + voyageDetails.getOptions().getFromPortSlot().getPort().getName() + " to "
										+ voyageDetails.getOptions().getToPortSlot().getPort().getName()), plan, voyageDetails);
						sumVoyageCostComponent(plan, voyageDetails, voyageSums);
					} else if (obj instanceof PortDetails) {

						final PortDetails portDetails = (PortDetails) obj;

						final PortType portType = portDetails.getOptions().getPortSlot().getPortType();
						// Charter out is currently our OTHER destination
						final String typeString = portType == PortType.CharterOut ? "Other" : portType.name();
						final ExtraData extraData = line.addExtraData("port" + idxX, idxStr + " - " + portDetails.getOptions().getPortSlot().getPort().getName() + " - (" + typeString + ")");

						// Exlcude end port from port costs
						// if (!(portDetails.getOptions().getPortSlot() instanceof EndPortSlot)) {
						createPortCostComponent(extraData, ports, pricing, plan, portDetails, mapPortSlotToRow.get(portDetails.getOptions().getPortSlot()));
						// }
						if (portDetails.getOptions().getPortSlot() instanceof ILoadOption) {
							currentAllocationAnnotation = allocationIterator.next();
							assert(currentAllocationAnnotation.getSlots().size() == 2);
							IPortSlot loadSlot = currentAllocationAnnotation.getSlots().get(0);
							// Add in LOAD VOLUME
							final int loadVolume = OptimiserUnitConvertor.convertToExternalVolume(currentAllocationAnnotation.getSlotVolumeInM3(loadSlot));
							extraData.addExtraData("volume", "Load Volume", loadVolume, ExtraDataFormatType.INTEGER);
						} else if (portDetails.getOptions().getPortSlot() instanceof IDischargeOption) {
							// Add in DISCHARGE VOLUME
							if (currentAllocationAnnotation != null) {
								assert(currentAllocationAnnotation.getSlots().size() == 2);
								IPortSlot dischargeSlot = currentAllocationAnnotation.getSlots().get(1);
								final int dischargeVolume = OptimiserUnitConvertor.convertToExternalVolume(currentAllocationAnnotation.getSlotVolumeInM3(dischargeSlot));
								extraData.addExtraData("volume", "Discharge Volume", dischargeVolume, ExtraDataFormatType.INTEGER);

								
								// for now, only handle single load/discharge case
								assert(currentAllocationAnnotation.getSlots().size() == 2);
								final ILoadOption loadOption = (ILoadOption) currentAllocationAnnotation.getSlots().get(0);
								final int cargoCVValue = loadOption.getCargoCVValue();
								final long dischargeVolumeInMMBTu = OptimiserUnitConvertor.convertToExternalVolume(Calculator.convertM3ToMMBTu(currentAllocationAnnotation.getSlotVolumeInM3(dischargeSlot),
										cargoCVValue));
								totalDischargeVolumeInMMBTu += dischargeVolumeInMMBTu;
								currentAllocationAnnotation = null;
							}
						} else {
							// Ignore
						}
					}
				}
				// line.setFrom(ports.reverseLookup(((PortOptions) voyagePlan.getSequence()[0]).getPortSlot().getPort()));
				// line.setTo(ports.reverseLookup(((PortOptions) voyagePlan.getSequence()[2]).getPortSlot().getPort()));

				// final Pair<Port, Port> key = new Pair<Port, Port>(line.getFrom(), line.getTo());

				// // unpack costs from plan
				// sumVoyageCostComponent(plan, (VoyageDetails) voyagePlan.getSequence()[1], voyageSums);
				// createPortCostComponent(line.addExtraData("discharging", "3 Discharging"), ports, pricing, plan, (PortDetails) voyagePlan.getSequence()[2]);
				// createVoyageCostComponent(line.addExtraData("ballast", "4 Ballast Journey"), plan, (VoyageDetails) voyagePlan.getSequence()[3]);

				for (final ExtraData d : line.getExtraData()) {
					final ExtraData duration = d.getDataWithKey("duration");
					if (duration != null) {
						totalDuration += duration.getValueAs(Integer.class);
					}
					final ExtraData fuelCost = d.getDataWithKey("fuelcost");
					if (fuelCost != null) {
						totalFuelCost += fuelCost.getValueAs(Integer.class);
					}
					final ExtraData routeCost = d.getDataWithKey("routecost");
					if (routeCost != null) {
						totalRouteCost += routeCost.getValueAs(Integer.class);
					}
					final ExtraData portcost = d.getDataWithKey("portcost");
					if (portcost != null) {
						totalPortCost += portcost.getValueAs(Integer.class);
					}
				}
				//
				for (final CostComponent cc : line.getCostComponents()) {
					totalDuration += cc.getDuration();
					totalFuelCost += cc.getFuelCost();
					if (cc instanceof Voyage)
						totalRouteCost += ((Voyage) cc).getRouteCost();
					if (cc instanceof Visit)
						totalPortCost += ((Visit) cc).getPortCost();
				}

				line.setDuration(totalDuration);
				line.setFuelCost(totalFuelCost);
				line.setCanalCost(totalRouteCost);
				line.setHireCost((plan.getNotionalDayRate() * totalDuration) / 24);
				line.setPortCost(totalPortCost);

				// line.setVolumeLoaded(OptimiserUnitConvertor.convertToExternalVolume(allocation.getDischargeVolume() + allocation.getFuelVolume()));
				// line.setVolumeDischarged(OptimiserUnitConvertor.convertToExternalVolume(allocation.getDischargeVolume()) );
				//
				// final double cv = spec.isSetCvValue() ? spec.getCvValue() : line.getFrom().getCvValue();
				//
				// line.setMmbtuDelivered((int) (line.getVolumeDischarged() * cv));
				// line.setUnitCost(line.getTotalCost() / (double) line.getMmbtuDelivered());
				//
				{
					final ExtraData summary;
					if (totalDischargeVolumeInMMBTu == 0) {
						summary = line.addExtraData("summary", "01 - Summary", line.getTotalCost(), ExtraDataFormatType.STRING_FORMAT);
						summary.setFormat("$%,d");
					} else {

						final double totalCostPerMMBTu = (double) line.getTotalCost() / (double) totalDischargeVolumeInMMBTu;
						summary = line.addExtraData("summary", "01 - Summary", totalCostPerMMBTu, ExtraDataFormatType.STRING_FORMAT);
						summary.setFormat("%,.02f $/MMBTu");
					}
					summary.addExtraData("duration", "Duration", totalDuration, ExtraDataFormatType.DURATION);
					final ExtraData fuelCostData = summary.addExtraData("fuelcost", "Fuel Cost", totalFuelCost, ExtraDataFormatType.CURRENCY);

					{
						for (final FuelComponent component : FuelComponent.values()) {

							if (voyageSums.containsKey(component)) {
								final Integer[] sums = voyageSums.get(component);
								// totalFuelCost += componentCost;
								final ExtraData componentData = fuelCostData.addExtraData(component.name(), component.name(), sums[1], ExtraDataFormatType.CURRENCY);
								componentData.addExtraData("quantity", "Usage (" + component.getDefaultFuelUnit().name() + ")", sums[0], ExtraDataFormatType.INTEGER);
							}
						}
					}

					summary.addExtraData("routecost", "Route Cost", totalRouteCost, ExtraDataFormatType.CURRENCY);
					summary.addExtraData("portcost", "Port Cost", totalPortCost, ExtraDataFormatType.CURRENCY);
					summary.addExtraData("hirecost", "Hire Cost", (plan.getNotionalDayRate() * totalDuration) / 24, ExtraDataFormatType.CURRENCY);

					// final ExtraData dischargeData = summary.addExtraData("discharged", "MMBTU Discharged", (int) (line.getVolumeDischarged() * cv), ExtraDataFormatType.INTEGER);
					// dischargeData.addExtraData("m3", "Volume", line.getVolumeDischarged(), ExtraDataFormatType.INTEGER);
					//
					// final ExtraData loadData = summary.addExtraData("loaded", "MMBTU Loaded", (int) (line.getVolumeLoaded() * cv), ExtraDataFormatType.INTEGER);
					// loadData.addExtraData("m3", "Volume", line.getVolumeLoaded(), ExtraDataFormatType.INTEGER);

				}
				lines.add(line);
			}
			if (monitor.isCanceled()) {
				return null;
			}
			monitor.worked(1);
			return lines;
		} finally {
			monitor.done();
		}
	}

	private Module createShippingCostModule() {
		return new AbstractModule() {

			@Override
			protected void configure() {
				bind(IVoyagePlanOptimiser.class).to(VoyagePlanOptimiser.class);
				bind(ILNGVoyageCalculator.class).to(LNGVoyageCalculator.class);
			}
		};
	}

	private void createVoyageCostComponent(final ExtraData d, final ShippingCostPlan spec, final VoyageDetails voyageDetails) {
		final int duration = voyageDetails.getIdleTime() + voyageDetails.getTravelTime();
		int totalCost = 0;
		d.addExtraData("distance", "Distance", voyageDetails.getOptions().getDistance(), ExtraDataFormatType.INTEGER);
		d.addExtraData("duration", "Duration", duration, ExtraDataFormatType.DURATION);
		d.addExtraData("idletime", "Idle Time", voyageDetails.getIdleTime(), ExtraDataFormatType.DURATION);
		d.addExtraData("traveltime", "Travel Time", voyageDetails.getTravelTime(), ExtraDataFormatType.DURATION);
		d.addExtraData("speed", "Speed", OptimiserUnitConvertor.convertToExternalSpeed(voyageDetails.getSpeed()), ExtraDataFormatType.STRING_FORMAT).setFormat("%,f");
		final int routeCost = OptimiserUnitConvertor.convertToExternalFixedCost(voyageDetails.getRouteCost());
		totalCost += routeCost;
		d.addExtraData("routecost", "Route Cost", routeCost, ExtraDataFormatType.CURRENCY).addExtraData("route", "Route", voyageDetails.getOptions().getRoute(), ExtraDataFormatType.AUTO);

		final ExtraData fuelData = d.addExtraData("fuelcost", "Fuel Cost");
		int totalFuelCost = 0;

		for (final FuelComponent component : FuelComponent.values()) {
			final long consumption = voyageDetails.getFuelConsumption(component, component.getDefaultFuelUnit());
			final int unitPrice = voyageDetails.getFuelUnitPrice(component);

			if (consumption == 0) {
				continue;
			}
			final int componentCost = OptimiserUnitConvertor.convertToExternalFixedCost(Calculator.costFromConsumption(consumption, unitPrice));
			totalFuelCost += componentCost;
			final ExtraData componentData = fuelData.addExtraData(component.name(), component.name(), componentCost, ExtraDataFormatType.CURRENCY);
			componentData.addExtraData("quantity", "Usage (" + component.getDefaultFuelUnit().name() + ")", OptimiserUnitConvertor.convertToExternalVolume(consumption), ExtraDataFormatType.INTEGER);
			componentData.addExtraData("unitprice", "Cost/" + component.getDefaultFuelUnit().name(), OptimiserUnitConvertor.convertToExternalPrice(unitPrice), ExtraDataFormatType.STRING_FORMAT)
					.setFormat("$%,f");

		}
		totalCost += totalFuelCost;
		fuelData.setValue(totalFuelCost);
		fuelData.setFormatType(ExtraDataFormatType.CURRENCY);

		final int hireCost = (spec.getNotionalDayRate() * duration) / 24;
		totalCost += hireCost;
		d.setValue(totalCost);
		d.setFormatType(ExtraDataFormatType.CURRENCY);
		d.addExtraData("hirecost", "Hire Cost", hireCost, ExtraDataFormatType.CURRENCY);
	}

	private void sumVoyageCostComponent(final ShippingCostPlan spec, final VoyageDetails voyageDetails, final Map<FuelComponent, Integer[]> sums) {

		for (final FuelComponent component : FuelComponent.values()) {
			final long consumption = voyageDetails.getFuelConsumption(component, component.getDefaultFuelUnit());
			final int unitPrice = voyageDetails.getFuelUnitPrice(component);

			if (consumption == 0) {
				continue;
			}
			Integer[] data;
			if (sums.containsKey(component)) {
				data = sums.get(component);
			} else {
				data = new Integer[2];
				data[0] = new Integer(0);
				data[1] = new Integer(0);
				sums.put(component, data);
			}
			data[0] = data[0].intValue() + OptimiserUnitConvertor.convertToExternalVolume(consumption);
			final int componentCost = OptimiserUnitConvertor.convertToExternalFixedCost(Calculator.costFromConsumption(consumption, unitPrice));
			data[1] = data[1].intValue() + componentCost;
		}
	}

	private void createPortCostComponent(final ExtraData result, final Association<Port, IPort> ports, final PricingModel pricing, final ShippingCostPlan plan, final PortDetails portDetails,
			ShippingCostRow row) {
		result.addExtraData("duration", "Duration", portDetails.getOptions().getVisitDuration(), ExtraDataFormatType.DURATION);
		final Port port = ports.reverseLookup(portDetails.getOptions().getPortSlot().getPort());
		result.addExtraData("location", "Location", port.getName(), ExtraDataFormatType.AUTO);
		int total = 0;
		if (row.isIncludePortCosts()) {
			for (final PortCost cost : pricing.getPortCosts()) {
				if (SetUtils.getObjects(cost.getPorts()).contains(port)) {
					// this is the cost for the given port
					// FIXME: This does not take into account START/END/OTHER COSTS
					total += cost.getPortCost(plan.getVessel().getVesselClass(), portDetails.getOptions().getPortSlot() instanceof ILoadSlot ? PortCapability.LOAD : PortCapability.DISCHARGE);
					result.addExtraData("portcost", "Port Cost", total, ExtraDataFormatType.CURRENCY);

					break;
				}
			}
		}
		total += (plan.getNotionalDayRate() * portDetails.getOptions().getVisitDuration()) / 24;
		result.addExtraData("hirecost", "Hire Cost", (plan.getNotionalDayRate() * portDetails.getOptions().getVisitDuration()) / 24, ExtraDataFormatType.CURRENCY);

		result.setValue(total);
		result.setFormatType(ExtraDataFormatType.CURRENCY);
	}

}

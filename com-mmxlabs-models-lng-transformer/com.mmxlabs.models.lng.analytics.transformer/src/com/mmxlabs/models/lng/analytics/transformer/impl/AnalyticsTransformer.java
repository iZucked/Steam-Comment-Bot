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
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.common.Association;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.CostComponent;
import com.mmxlabs.models.lng.analytics.UnitCostLine;
import com.mmxlabs.models.lng.analytics.UnitCostMatrix;
import com.mmxlabs.models.lng.analytics.Visit;
import com.mmxlabs.models.lng.analytics.Voyage;
import com.mmxlabs.models.lng.analytics.transformer.IAnalyticsTransformer;
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
import com.mmxlabs.models.lng.transformer.TransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.ScheduleBuilderModule;
import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.lng.types.ExtraData;
import com.mmxlabs.models.lng.types.ExtraDataFormatType;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
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
import com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanChoice;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.fitness.impl.SchedulerUtils;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.manipulators.SequencesManipulatorModule;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.guice.DataComponentProviderModule;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.LNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Analytics transformer - this implementation replicates a lot of things in the LNGScenarioTransformer. At some point we should break each submodel transformer out separately, as far as possible.
 * They could perhaps be services - not sure
 * 
 * @author hinton
 * 
 */
public class AnalyticsTransformer implements IAnalyticsTransformer {
	@Override
	public UnitCostLine createCostLine(final MMXRootObject root, final UnitCostMatrix spec, final Port from, final Port to) {
		final UnitCostMatrix spec2 = EcoreUtil.copy(spec);
		// spec2.getPorts().clear();
		// spec2.getPorts().add(from);
		// spec2.getPorts().add(to);
		//

		spec2.getFromPorts().clear();
		spec2.getFromPorts().add(from);
		spec2.getToPorts().clear();
		spec2.getToPorts().add(to);

		final List<UnitCostLine> createCostLines = createCostLines(root, spec2, new NullProgressMonitor());
		if (createCostLines.isEmpty())
			return null;
		return createCostLines.get(0);
	}

	@Override
	public List<UnitCostLine> createCostLines(final MMXRootObject root, final UnitCostMatrix spec, final IProgressMonitor monitor) {
		try {
			final PortModel portModel = root.getSubModel(PortModel.class);
			final PricingModel pricing = root.getSubModel(PricingModel.class);

			/*
			 * Create ports and distances
			 */

			final Map<Pair<Port, Port>, List<Integer>> minTravelTimeAtSpeed = new HashMap<Pair<Port, Port>, List<Integer>>();

			final double speed;
			final Vessel vessel = spec.getVessel();
			if (vessel == null) {
				monitor.setCanceled(true);
				return Collections.emptyList();
			}

			if (spec.isSetSpeed()) {
				speed = spec.getSpeed();
			} else {
				speed = vessel.getVesselClass().getMaxSpeed();
			}

			for (final Route route : spec.getAllowedRoutes().isEmpty() ? portModel.getRoutes() : spec.getAllowedRoutes()) {
				VesselClassRouteParameters parametersForRoute = null;

				for (final VesselClassRouteParameters parameters : vessel.getVesselClass().getRouteParameters()) {
					if (parameters.getRoute() == route) {
						parametersForRoute = parameters;
						break;
					}
				}

				for (final RouteLine line : route.getLines()) {
					final int travelTime = (int) Math.ceil(line.getDistance() / speed) + (parametersForRoute == null ? 0 : parametersForRoute.getExtraTransitTime());
					final Pair<Port, Port> key = new Pair<Port, Port>(line.getFrom(), line.getTo());
					List<Integer> existingValue = minTravelTimeAtSpeed.get(key);
					if (existingValue == null) {
						existingValue = new ArrayList<Integer>();
						minTravelTimeAtSpeed.put(key, existingValue);
					}
					existingValue.add(travelTime);
				}
			}

			/*
			 * Create a cargo for each pair
			 */
			final List<Port> loadPorts = new ArrayList<Port>();
			final List<Port> dischargePorts = new ArrayList<Port>();

			for (final APort port : SetUtils.getPorts(spec.getFromPorts())) {
				if (port instanceof Port) {
					loadPorts.add((Port) port);
				}
			}

			for (final APort port : SetUtils.getPorts(spec.getToPorts())) {
				if (port instanceof Port) {
					dischargePorts.add((Port) port);
				}
			}

			if (loadPorts.isEmpty()) {
				for (final Port port : portModel.getPorts()) {
					if (port.getCapabilities().contains(PortCapability.LOAD))
						loadPorts.add(port);
				}
			}

			if (dischargePorts.isEmpty()) {
				for (final Port port : portModel.getPorts()) {
					if (port.getCapabilities().contains(PortCapability.DISCHARGE))
						dischargePorts.add(port);
				}
			}

			final HashMap<Pair<Port, Port>, UnitCostLine> bestCostSoFar = new HashMap<Pair<Port, Port>, UnitCostLine>();

			final ILoadPriceCalculator loadCalculator = new FixedPriceContract(0);
			final ISalesPriceCalculator dischargeCalculator = new FixedPriceContract(OptimiserUnitConvertor.convertToInternalPrice(spec.getCargoPrice()));
			int i = 0;

			monitor.beginTask("Creating unit cost matrix", loadPorts.size());

			for (final Port loadPort : loadPorts) {
				monitor.subTask("Evaluating costs for journeys from " + loadPort.getName());
				final Injector injector = Guice.createInjector(new DataComponentProviderModule(), new ScheduleBuilderModule(), new SequencesManipulatorModule(), createShippingCostModule());
				final ISchedulerBuilder builder = injector.getInstance(ISchedulerBuilder.class);

				final Association<Port, IPort> ports = new Association<Port, IPort>();
				final ICooldownPriceCalculator nullCalculator = new FixedPriceContract(0);
				for (final Port port : portModel.getPorts()) {
					ports.add(port, builder.createPort(port.getName(), !port.isAllowCooldown(), nullCalculator));
				}

				/*
				 * Create vessel class
				 */
				final Vessel modelVessel = vessel;

				final VesselClass eVc = modelVessel.getVesselClass();
				
				final IVesselClass vesselClass = TransformerHelper.buildIVesselClass(builder, eVc,spec.getBaseFuelPrice());				

				for (final Route route : spec.getAllowedRoutes().isEmpty() ? portModel.getRoutes() : spec.getAllowedRoutes()) {
					VesselClassRouteParameters parametersForRoute = null;
					RouteCost costForRoute = null;
					for (final VesselClassRouteParameters parameters : vessel.getVesselClass().getRouteParameters()) {
						if (parameters.getRoute() == route) {
							parametersForRoute = parameters;
							break;
						}
					}
					for (final RouteCost routeCost : pricing.getRouteCosts()) {
						if (routeCost.getRoute() == route && routeCost.getVesselClass() == vessel.getVesselClass()) {
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

				final List<ICargo> cargoes = new ArrayList<ICargo>(dischargePorts.size());
				final List<IVessel> vessels = new ArrayList<IVessel>(dischargePorts.size());

				// final Map<ICargo, int[]> allowances = spec.isSetSpeed() ? new HashMap<ICargo, int[]>() : null;

				for (final Port dischargePort : dischargePorts) {
					// compute time windows
					final List<Integer> minTimesLD = minTravelTimeAtSpeed.get(new Pair<Port, Port>(loadPort, dischargePort));
					final List<Integer> minTimesDL = minTravelTimeAtSpeed.get(new Pair<Port, Port>(dischargePort, loadPort));
					if (minTimesLD == null || minTimesDL == null) {
						continue;
					} else {
					}

					if (!spec.isSetSpeed()) {
						final Integer minTimeLD = Collections.min(minTimesLD);
						final Integer minTimeDL = Collections.min(minTimesDL);
						minTimesLD.clear();
						minTimesDL.clear();
						if (minTimeLD != null)
							minTimesLD.add(minTimeLD);
						if (minTimeDL != null)
							minTimesDL.add(minTimeDL);
					}

					int counter = 0;
					for (final int minTimeLD : minTimesLD) {
						final int ladenAllowance = (int) Math.round((double) minTimeLD * spec.getLadenTimeAllowance());
						for (final int minTimeDL : minTimesDL) {
							// create round trip cargo
							final String id = (counter++) + "-" + loadPort.getName() + "-to-" + dischargePort.getName();
							final ILoadSlot loadSlot = builder.createLoadSlot("load-" + id, ports.lookup(loadPort), builder.createTimeWindow(0, 0),
									OptimiserUnitConvertor.convertToInternalVolume(spec.getMinimumLoad()), OptimiserUnitConvertor.convertToInternalVolume(spec.getMaximumLoad()), loadCalculator,
									OptimiserUnitConvertor.convertToInternalConversionFactor(spec.isSetCvValue() ? spec.getCvValue() : loadPort.getCvValue()), loadPort.getLoadDuration(), false,
									false, false);

							final int ballastAllowance = (int) Math.round((double) minTimeDL * spec.getBallastTimeAllowance());

							final int timeAtDischarge = loadPort.getLoadDuration() + minTimeLD + ladenAllowance;
							final ITimeWindow dischargeWindow = builder.createTimeWindow(timeAtDischarge, timeAtDischarge);
							
							final long minCv = 0;
							final long maxCv = Long.MAX_VALUE;
							
							
							final IDischargeSlot dischargeSlot = builder.createDischargeSlot("discharge-" + id, ports.lookup(dischargePort), dischargeWindow,
									OptimiserUnitConvertor.convertToInternalVolume(spec.getMinimumDischarge()), OptimiserUnitConvertor.convertToInternalVolume(spec.getMaximumDischarge()),
									minCv, maxCv, dischargeCalculator, dischargePort.getDischargeDuration(), false);

							final ICargo cargo = builder.createCargo(id, loadSlot, dischargeSlot, false);
							cargoes.add(cargo);

							// create vessel

							final int timeAtReturn = timeAtDischarge + dischargePort.getDischargeDuration() + minTimeDL + ballastAllowance;
							final ITimeWindow returnWindow = builder.createTimeWindow(timeAtReturn, timeAtReturn);

							final ICurve charterInRate = new ConstantValueCurve((int) OptimiserUnitConvertor.convertToInternalHourlyCost(spec.getNotionalDayRate()));

							vessels.add(builder.createVessel("vessel-" + i++, vesselClass, charterInRate, VesselInstanceType.SPOT_CHARTER, builder.createStartEndRequirement(),
									builder.createStartEndRequirement(returnWindow), 0, 0, 0));
						}
					}
				}

				/*
				 * Create a sequences assigning each cargo to the right vessel
				 */
				final IOptimisationData data = builder.getOptimisationData();
				final IVesselProvider vesselProvider = data.getDataComponentProvider(SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);
				final IModifiableSequences sequences = new ModifiableSequences(data.getResources());
				final Iterator<IVessel> vesselIterator = vessels.iterator();
				final IStartEndRequirementProvider startEndProvider = data.getDataComponentProvider(SchedulerConstants.DCP_startEndRequirementProvider, IStartEndRequirementProvider.class);
				final IPortSlotProvider slotProvider = data.getDataComponentProvider(SchedulerConstants.DCP_portSlotsProvider, IPortSlotProvider.class);

				final int[][] arrivalTimes = new int[cargoes.size()][3];
				int index = 0;
				for (final ICargo cargo : cargoes) {
					final IResource resource = vesselProvider.getResource(vesselIterator.next());
					final IModifiableSequence sequence = sequences.getModifiableSequence(resource);
					// set up sequence and arrival times
					sequence.add(startEndProvider.getStartElement(resource));
					sequence.add(slotProvider.getElement(cargo.getLoadOption()));
					sequence.add(slotProvider.getElement(cargo.getDischargeOption()));
					sequence.add(startEndProvider.getEndElement(resource));

					final int[] times = arrivalTimes[index++];
					times[0] = 0;
					times[1] = cargo.getDischargeOption().getTimeWindow().getStart();
					times[2] = startEndProvider.getEndRequirement(resource).getTimeWindow().getStart();
				}
				/*
				 * Create a fitness core and evaluate+annotate the sequences
				 */
				final ISequencesManipulator manipulator = injector.getInstance(ISequencesManipulator.class);
				manipulator.init(data);
				manipulator.manipulate(sequences); // this will set the return elements to the right places, and remove the start elements.
				final LNGVoyageCalculator calculator = new LNGVoyageCalculator();
				injector.injectMembers(calculator);

				final VoyagePlanOptimiser optimiser = new VoyagePlanOptimiser(calculator) {
					@Override
					public void addChoice(final IVoyagePlanChoice choice) {
						super.addChoice(choice);
					}

					@Override
					public void setBasicSequence(final List<Object> basicSequence) {
						// enforce use of NBO
						for (final Object o : basicSequence) {
							if (o instanceof VoyageOptions) {
								((VoyageOptions) o).setAllowCooldown(false);
								((VoyageOptions) o).setShouldBeCold(true);
							}
						}
						super.setBasicSequence(basicSequence);
					}
				};

				final AbstractSequenceScheduler scheduler = new AbstractSequenceScheduler() {
					@Override
					public ScheduledSequences schedule(final ISequences sequences, final boolean forExport) {
						return null;
					}

					@Override
					public ScheduledSequences schedule(final ISequences sequences, final Collection<IResource> affectedResources, final boolean forExport) {
						return null;
					}

					@Override
					public void acceptLastSchedule() {

					}
				};

				// injector.injectMembers(scheduler);
				SchedulerUtils.setDataComponentProviders(data, scheduler);
				scheduler.setVoyagePlanOptimiser(optimiser);
				scheduler.init();

				// run the scheduler on the sequences
				final ScheduledSequences result = scheduler.schedule(sequences, arrivalTimes);

				final UnconstrainedCargoAllocator aca = injector.getInstance(UnconstrainedCargoAllocator.class);
				aca.setVesselProvider(vesselProvider);

				final Map<VoyagePlan, IAllocationAnnotation> allocations = aca.allocate(result);
				final Iterator<IAllocationAnnotation> allocationIterator = allocations.values().iterator();

				/*
				 * Unpack the annotated solution and create output lines
				 */
				for (final ScheduledSequence sequence : result) {
					final IAllocationAnnotation allocation = allocationIterator.next();
					final VoyagePlan plan = sequence.getVoyagePlans().get(0);
					// create line for plan

					final UnitCostLine line = AnalyticsFactory.eINSTANCE.createUnitCostLine();

					line.setFrom(ports.reverseLookup(((PortDetails) plan.getSequence()[0]).getOptions().getPortSlot().getPort()));
					line.setTo(ports.reverseLookup(((PortDetails) plan.getSequence()[2]).getOptions().getPortSlot().getPort()));

					final Pair<Port, Port> key = new Pair<Port, Port>(line.getFrom(), line.getTo());

					final Map<FuelComponent, Integer[]> voyageSums = new EnumMap<FuelComponent, Integer[]>(FuelComponent.class);

					// unpack costs from plan
					createPortCostComponent(line.addExtraData("loading", "1 Loading"), ports, pricing, spec, (PortDetails) plan.getSequence()[0]);
					createVoyageCostComponent(line.addExtraData("laden", "2 Laden Journey"), spec, (VoyageDetails) plan.getSequence()[1]);
					sumVoyageCostComponent(spec, (VoyageDetails) plan.getSequence()[1], voyageSums);
					createPortCostComponent(line.addExtraData("discharging", "3 Discharging"), ports, pricing, spec, (PortDetails) plan.getSequence()[2]);
					createVoyageCostComponent(line.addExtraData("ballast", "4 Ballast Journey"), spec, (VoyageDetails) plan.getSequence()[3]);
					sumVoyageCostComponent(spec, (VoyageDetails) plan.getSequence()[3], voyageSums);

					int totalDuration = 0;
					int totalFuelCost = 0;
					int totalRouteCost = 0;
					int totalPortCost = 0;

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
					line.setHireCost((spec.getNotionalDayRate() * totalDuration) / 24);
					line.setPortCost(totalPortCost);

					line.setVolumeLoaded(OptimiserUnitConvertor.convertToExternalVolume(allocation.getLoadVolumeInM3()));
					line.setVolumeDischarged(OptimiserUnitConvertor.convertToExternalVolume(allocation.getDischargeVolumeInM3()) - spec.getRetainHeel());

					final double cv = spec.isSetCvValue() ? spec.getCvValue() : line.getFrom().getCvValue();

					line.setMmbtuDelivered((int) (line.getVolumeDischarged() * cv));
					line.setUnitCost(line.getTotalCost() / (double) line.getMmbtuDelivered());

					{
						final ExtraData summary = line.addExtraData("summary", "Summary", line.getTotalCost() / (double) line.getMmbtuDelivered(), ExtraDataFormatType.STRING_FORMAT);
						summary.setFormat("$%,f");
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
						summary.addExtraData("hirecost", "Hire Cost", (spec.getNotionalDayRate() * totalDuration) / 24, ExtraDataFormatType.CURRENCY);

						final ExtraData dischargeData = summary.addExtraData("discharged", "MMBTU Discharged", (int) (line.getVolumeDischarged() * cv), ExtraDataFormatType.INTEGER);
						dischargeData.addExtraData("m3", "Volume", line.getVolumeDischarged(), ExtraDataFormatType.INTEGER);

						final ExtraData loadData = summary.addExtraData("loaded", "MMBTU Loaded", (int) (line.getVolumeLoaded() * cv), ExtraDataFormatType.INTEGER);
						loadData.addExtraData("m3", "Volume", line.getVolumeLoaded(), ExtraDataFormatType.INTEGER);

						summary.addExtraData("unitcost", "Unit Cost", line.getTotalCost() / (double) line.getMmbtuDelivered(), ExtraDataFormatType.STRING_FORMAT).setFormat("$%,f");
					}
					final UnitCostLine d = bestCostSoFar.get(key);
					if (d == null || d.getUnitCost() > line.getUnitCost()) {
						bestCostSoFar.put(key, line);
					}
				}
				if (monitor.isCanceled())
					return null;
				monitor.worked(1);
			}

			final List<UnitCostLine> lines = new ArrayList<UnitCostLine>();
			lines.addAll(bestCostSoFar.values());

			return lines;
		} finally {
			monitor.done();
		}
	}

	private void createVoyageCostComponent(final ExtraData d, final UnitCostMatrix spec, final VoyageDetails voyageDetails) {
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

	private void sumVoyageCostComponent(final UnitCostMatrix spec, final VoyageDetails voyageDetails, final Map<FuelComponent, Integer[]> sums) {

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

	private void createPortCostComponent(final ExtraData result, final Association<Port, IPort> ports, final PricingModel pricing, final UnitCostMatrix spec, final PortDetails portDetails) {
		result.addExtraData("duration", "Duration", portDetails.getOptions().getVisitDuration(), ExtraDataFormatType.DURATION);
		final Port port = ports.reverseLookup(portDetails.getOptions().getPortSlot().getPort());
		result.addExtraData("location", "Location", port.getName(), ExtraDataFormatType.AUTO);
		int total = 0;
		for (final PortCost cost : pricing.getPortCosts()) {
			if (SetUtils.getPorts(cost.getPorts()).contains(port)) {
				// this is the cost for the given port
				total += cost.getPortCost(spec.getVessel().getVesselClass(), portDetails.getOptions().getPortSlot() instanceof ILoadSlot ? PortCapability.LOAD : PortCapability.DISCHARGE);
				result.addExtraData("portcost", "Port Cost", total, ExtraDataFormatType.CURRENCY);

				break;
			}
		}

		total += (spec.getNotionalDayRate() * portDetails.getOptions().getVisitDuration()) / 24;
		result.addExtraData("hirecost", "Hire Cost", (spec.getNotionalDayRate() * portDetails.getOptions().getVisitDuration()) / 24, ExtraDataFormatType.CURRENCY);

		result.setValue(total);
		result.setFormatType(ExtraDataFormatType.CURRENCY);
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

}

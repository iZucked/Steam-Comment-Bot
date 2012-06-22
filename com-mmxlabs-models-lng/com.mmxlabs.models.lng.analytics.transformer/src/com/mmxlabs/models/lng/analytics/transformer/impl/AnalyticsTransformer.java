/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.transformer.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.core.runtime.IProgressMonitor;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.common.Association;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.CostComponent;
import com.mmxlabs.models.lng.analytics.FuelCost;
import com.mmxlabs.models.lng.analytics.UnitCostLine;
import com.mmxlabs.models.lng.analytics.UnitCostMatrix;
import com.mmxlabs.models.lng.analytics.Visit;
import com.mmxlabs.models.lng.analytics.Voyage;
import com.mmxlabs.models.lng.analytics.transformer.IAnalyticsTransformer;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.lng.types.ExtraData;
import com.mmxlabs.models.lng.types.ExtraDataFormatType;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.TypesFactory;
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
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.builder.impl.SchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.InterpolatingConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.LookupTableConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator2;
import com.mmxlabs.scheduler.optimiser.contracts.IShippingPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.FixedPriceContract;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.UnconstrainedCargoAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.AbstractSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanChoice;
import com.mmxlabs.scheduler.optimiser.fitness.impl.SchedulerUtils;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.manipulators.SequencesManipulatorUtil;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.guice.DataComponentProviderModule;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.impl.LNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Analytics transformer - this implementation replicates a lot of things in the LNGScenarioTransformer. At some point we should break
 * each submodel transformer out separately, as far as possible. They could perhaps be services - not sure
 * 
 * @author hinton
 *
 */
public class AnalyticsTransformer implements IAnalyticsTransformer {
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
			if (spec.isSetSpeed()) {
				speed = spec.getSpeed();
			} else {
				speed = spec.getVessel().getVesselClass().getMaxSpeed();
			}
			
			
			for (final Route route : spec.getAllowedRoutes().isEmpty() ? portModel.getRoutes() : spec.getAllowedRoutes()) {
				VesselClassRouteParameters parametersForRoute = null;
				RouteCost costForRoute = null;
				for (final VesselClassRouteParameters parameters : spec.getVessel().getVesselClass().getRouteParameters()) {
					if (parameters.getRoute() == route) {
						parametersForRoute = parameters;
						break;
					}
				}
				for (final RouteCost routeCost : pricing.getRouteCosts()) {
					if (routeCost.getRoute() == route && routeCost.getVesselClass() == spec.getVessel().getVesselClass()) {
						costForRoute = routeCost;
						break;
					}
				}
				for (final RouteLine line : route.getLines()) {
					int travelTime = (int) Math
							.ceil(line.getDistance() / speed)
							+ (parametersForRoute == null ? 0
									: parametersForRoute.getExtraTransitTime());
					final Pair<Port, Port> key = new Pair<Port, Port>(
							line.getFrom(), line.getTo());
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
			
			
			final Set<APort> includedPorts = SetUtils.getPorts(spec.getPorts());
			if (includedPorts.isEmpty()) {
				includedPorts.addAll(portModel.getPorts());
			}
			final List<Port> loadPorts = new ArrayList<Port>();
			final List<Port> dischargePorts = new ArrayList<Port>();
			for (final APort aport : includedPorts) {
				if (aport instanceof Port) {
					final Port port = (Port) aport;
					if (port.getCapabilities().contains(PortCapability.LOAD))
						loadPorts.add(port);
					if (port.getCapabilities().contains(PortCapability.DISCHARGE))
						dischargePorts.add(port);
				}
			}

			
			
			
			final HashMap<Pair<Port, Port>, UnitCostLine> bestCostSoFar = new HashMap<Pair<Port, Port>, UnitCostLine>();
			
			final ILoadPriceCalculator2 loadCalculator = new FixedPriceContract(0);
			final IShippingPriceCalculator dischargeCalculator = new FixedPriceContract(Calculator.scaleToInt(spec.getCargoPrice()));
			int i = 0;
			
			monitor.beginTask("Creating unit cost matrix", loadPorts.size());
			
			for (final Port loadPort : loadPorts) {
				monitor.subTask("Evaluating costs for journeys from " + loadPort.getName());
				final Injector injector = Guice.createInjector(new DataComponentProviderModule());
				final ISchedulerBuilder builder = new SchedulerBuilder();
				injector.injectMembers(builder);
				
				final Association<Port, IPort> ports = new Association<Port, IPort>();
				final IShippingPriceCalculator nullCalculator = new FixedPriceContract(0);
				for (final Port port : portModel.getPorts()) {
					ports.add(port, builder.createPort(port.getName(), !port.isAllowCooldown(), nullCalculator));
				}
				
				/*
				 * Create vessel class
				 */
				final Vessel modelVessel = spec.getVessel();

				final VesselClass eVc = modelVessel.getVesselClass();
				final IVesselClass vesselClass = builder.createVesselClass(eVc.getName(), Calculator.scaleToInt(eVc.getMinSpeed()), Calculator.scaleToInt(eVc.getMaxSpeed()),
						Calculator.scale((int) (eVc.getFillCapacity() * eVc.getCapacity())), Calculator.scaleToInt(eVc.getMinHeel()),

						Calculator.scaleToInt(spec.getBaseFuelPrice()),

						Calculator.scaleToInt(eVc.getBaseFuel().getEquivalenceFactor()), Calculator.scaleToInt(eVc.getPilotLightRate()) / 24, Calculator.scaleToInt(spec.getNotionalDayRate() / 24.0),
						eVc.getWarmingTime(), eVc.getCoolingTime(), Calculator.scale(eVc.getCoolingVolume()));

				buildVesselStateAttributes(builder, vesselClass, com.mmxlabs.scheduler.optimiser.components.VesselState.Laden, eVc.getLadenAttributes());
				buildVesselStateAttributes(builder, vesselClass, com.mmxlabs.scheduler.optimiser.components.VesselState.Ballast, eVc.getBallastAttributes());
				
				for (final Route route : spec.getAllowedRoutes().isEmpty() ? portModel.getRoutes() : spec.getAllowedRoutes()) {
					VesselClassRouteParameters parametersForRoute = null;
					RouteCost costForRoute = null;
					for (final VesselClassRouteParameters parameters : spec.getVessel().getVesselClass().getRouteParameters()) {
						if (parameters.getRoute() == route) {
							parametersForRoute = parameters;
							break;
						}
					}
					for (final RouteCost routeCost : pricing.getRouteCosts()) {
						if (routeCost.getRoute() == route && routeCost.getVesselClass() == spec.getVessel().getVesselClass()) {
							costForRoute = routeCost;
							break;
						}
					}
					for (final RouteLine line : route.getLines()) {
						builder.setPortToPortDistance(ports.lookup(line.getFrom()), ports.lookup(line.getTo()), route.getName(), line.getDistance());
						if (parametersForRoute != null) {
							builder.setVesselClassRouteTransitTime(route.getName(), vesselClass, parametersForRoute.getExtraTransitTime());
							builder.setVesselClassRouteFuel(route.getName(), vesselClass, VesselState.Laden, 
									Calculator.scale(parametersForRoute.getLadenConsumptionRate()) / 24,
									Calculator.scale(parametersForRoute.getLadenNBORate()) / 24);
							builder.setVesselClassRouteFuel(route.getName(), vesselClass, VesselState.Ballast, 
									Calculator.scale(parametersForRoute.getBallastConsumptionRate()) / 24,
									Calculator.scale(parametersForRoute.getBallastNBORate()) / 24);
						}
						if (costForRoute != null) {
							builder.setVesselClassRouteCost(route.getName(), vesselClass, VesselState.Laden, (int) Calculator.scale(costForRoute.getLadenCost()));
							builder.setVesselClassRouteCost(route.getName(), vesselClass, VesselState.Ballast, (int) Calculator.scale(costForRoute.getBallastCost()));
						}
					}
				}
				
				final List<ICargo> cargoes = new ArrayList<ICargo>(dischargePorts.size());
				final List<IVessel> vessels = new ArrayList<IVessel>(dischargePorts.size());
				
				for (final Port dischargePort : dischargePorts) {
					// compute time windows
					final List<Integer> minTimesLD = minTravelTimeAtSpeed.get(new Pair<Port, Port>(loadPort, dischargePort));
					final List<Integer> minTimesDL = minTravelTimeAtSpeed.get(new Pair<Port, Port>(dischargePort, loadPort));
					if (minTimesLD == null || minTimesDL == null) {
						continue;
					} else {
					}
					
					if (!spec.isSetSpeed()) {
						Integer minTimeLD = Collections.min(minTimesLD);
						Integer minTimeDL = Collections.min(minTimesDL);
						minTimesLD.clear();
						minTimesDL.clear();
						if (minTimeLD != null)
							minTimesLD.add(minTimeLD);
						if (minTimeDL != null)
							minTimesDL.add(minTimeDL);
					}
					
					int counter = 0;
					for (final int minTimeLD : minTimesLD) {
						for (final int minTimeDL : minTimesDL) {
							// create round trip cargo
							final String id = (counter++) + "-" + loadPort.getName() + "-to-" + dischargePort.getName();
							final ILoadSlot loadSlot = builder.createLoadSlot("load-" + id, ports.lookup(loadPort), builder.createTimeWindow(0, 0), Calculator.scale(spec.getMinimumLoad()),
									Calculator.scale(spec.getMaximumLoad()), loadCalculator, Calculator.scaleToInt(spec.isSetCvValue() ? spec.getCvValue() : loadPort.getCvValue()),
									loadPort.getLoadDuration(), false, false, false);

							final int timeAtDischarge = loadPort.getLoadDuration() + minTimeLD + spec.getDischargeIdleTime();
							final ITimeWindow dischargeWindow = builder.createTimeWindow(timeAtDischarge, timeAtDischarge);;
							final IDischargeSlot dischargeSlot = builder.createDischargeSlot("discharge-" + id, ports.lookup(dischargePort), dischargeWindow,
									Calculator.scale(spec.getMinimumDischarge()), Calculator.scale(spec.getMaximumDischarge()), dischargeCalculator, dischargePort.getDischargeDuration(), false);

							final ICargo cargo = builder.createCargo(id, loadSlot, dischargeSlot, false);
							cargoes.add(cargo);
							// create vessel
							
							final int timeAtReturn = timeAtDischarge + dischargePort.getDischargeDuration() + minTimeDL + spec.getReturnIdleTime();
							final ITimeWindow returnWindow = builder.createTimeWindow(timeAtReturn, timeAtReturn);

							vessels.add(builder.createVessel("vessel-" + i++, vesselClass, 0, VesselInstanceType.SPOT_CHARTER, builder.createStartEndRequirement(),
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
					
					if (spec.isSetSpeed()) {
						times[1] -= spec.getDischargeIdleTime();
						times[2] -= spec.getReturnIdleTime();
					}
				}
				/*
				 * Create a fitness core and evaluate+annotate the sequences
				 */
				final ISequencesManipulator manipulator = SequencesManipulatorUtil.createDefaultSequenceManipulators(data);
				manipulator.manipulate(sequences); // this will set the return elements to the right places, and remove the start elements.
				final LNGVoyageCalculator calculator = new LNGVoyageCalculator();
				calculator.setRouteCostDataComponentProvider(data.getDataComponentProvider(SchedulerConstants.DCP_routePriceProvider, IRouteCostProvider.class));
				final VoyagePlanOptimiser optimiser = new VoyagePlanOptimiser(calculator) {
					@Override
					public void addChoice(final IVoyagePlanChoice choice) {
						super.addChoice(choice);
					}

					@Override
					public void setBasicSequence(List<Object> basicSequence) {
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
					public ScheduledSequences schedule(ISequences sequences, boolean forExport) {
						return null;
					}

					@Override
					public ScheduledSequences schedule(ISequences sequences, Collection<IResource> affectedResources, boolean forExport) {
						return null;
					}

					@Override
					public void acceptLastSchedule() {

					}
				};

				SchedulerUtils.setDataComponentProviders(data, scheduler);
				scheduler.setVoyagePlanOptimiser(optimiser);
				scheduler.init();
				
				
				// run the scheduler on the sequences
				final ScheduledSequences result = scheduler.schedule(sequences, arrivalTimes);
				

				final UnconstrainedCargoAllocator aca = new UnconstrainedCargoAllocator();
				aca.setVesselProvider(vesselProvider);

				final Collection<IAllocationAnnotation> allocations = aca.allocate(result);
				final Iterator<IAllocationAnnotation> allocationIterator = allocations.iterator();
				
				/*
				 * Unpack the annotated solution and create output lines
				 */
				for (final ScheduledSequence sequence : result) {
					final IAllocationAnnotation allocation = allocationIterator.next();
					final VoyagePlan plan = sequence.getVoyagePlans().get(0);
					// create line for plan

					final UnitCostLine line = AnalyticsFactory.eINSTANCE.createUnitCostLine();
					
					line.setFrom(ports.reverseLookup(((PortDetails) plan.getSequence()[0]).getPortSlot().getPort()));
					line.setTo(ports.reverseLookup(((PortDetails) plan.getSequence()[2]).getPortSlot().getPort()));

					final Pair<Port, Port> key = new Pair<Port, Port>(line.getFrom(), line.getTo());
					
					// unpack costs from plan
					createPortCostComponent(line.addExtraData("loading", "1 Loading"), ports, pricing, spec, (PortDetails) plan.getSequence()[0]);
					createVoyageCostComponent(line.addExtraData("laden", "2 Laden Journey"),spec, (VoyageDetails) plan.getSequence()[1]);
					createPortCostComponent(line.addExtraData("discharging", "3 Discharging"), ports, pricing, spec, (PortDetails) plan.getSequence()[2]);
					createVoyageCostComponent(line.addExtraData("ballast", "4 Ballast Journey"),spec, (VoyageDetails) plan.getSequence()[3]);
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
					line.setHireCost((spec.getNotionalDayRate() * totalDuration)/24);
					line.setPortCost(totalPortCost);

					line.setVolumeLoaded((int) ((allocation.getDischargeVolume() + allocation.getFuelVolume()) / Calculator.ScaleFactor));
					line.setVolumeDischarged((int) (allocation.getDischargeVolume() / Calculator.ScaleFactor) - spec.getRetainHeel());

					double cv = spec.isSetCvValue() ? spec.getCvValue() : line.getFrom().getCvValue();

					line.setMmbtuDelivered((int) (line.getVolumeDischarged() * cv));
					line.setUnitCost(line.getTotalCost() / (double) line.getMmbtuDelivered());
					
					{
						final ExtraData summary = line.addExtraData("summary", "Summary", line.getTotalCost() / (double) line.getMmbtuDelivered(), ExtraDataFormatType.STRING_FORMAT);
						summary.setFormat("$%,f");
						summary.addExtraData("duration", "Duration", totalDuration, ExtraDataFormatType.DURATION);
						summary.addExtraData("fuelcost", "Fuel Cost", totalFuelCost, ExtraDataFormatType.CURRENCY);
						summary.addExtraData("routecost", "Route Cost", totalRouteCost, ExtraDataFormatType.CURRENCY);
						summary.addExtraData("portcost", "Port Cost", totalPortCost, ExtraDataFormatType.CURRENCY);
						summary.addExtraData("hirecost", "Hire Cost", (spec.getNotionalDayRate() * totalDuration)/24, ExtraDataFormatType.CURRENCY);
						
						ExtraData dischargeData = summary.addExtraData("discharged", "MMBTU Discharged",(int) (line.getVolumeDischarged() * cv), ExtraDataFormatType.INTEGER);
						dischargeData.addExtraData("m3", "Volume", line.getVolumeDischarged(), ExtraDataFormatType.INTEGER);
						
						ExtraData loadData = summary.addExtraData("loaded", "MMBTU Loaded",(int) (line.getVolumeLoaded() * cv), ExtraDataFormatType.INTEGER);
						loadData.addExtraData("m3", "Volume", line.getVolumeLoaded(), ExtraDataFormatType.INTEGER);
						
						summary.addExtraData("unitcost", "Unit Cost", line.getTotalCost() / (double) line.getMmbtuDelivered(), ExtraDataFormatType.STRING_FORMAT).setFormat("$%,f");
					}
					UnitCostLine d = bestCostSoFar.get(key);
					if (d == null || d.getUnitCost() > line.getUnitCost()) {
						bestCostSoFar.put(key, line);
					}
				}
				if (monitor.isCanceled()) return null;
				monitor.worked(1);
			}
			
			final List<UnitCostLine> lines = new ArrayList<UnitCostLine>();
			lines.addAll(bestCostSoFar.values());

			return lines;
		} finally {
			monitor.done();
		}
	}

	private void createVoyageCostComponent(ExtraData d, final UnitCostMatrix spec, final VoyageDetails voyageDetails) {
		final int duration = voyageDetails.getIdleTime() + voyageDetails.getTravelTime();
		int totalCost = 0;
		d.addExtraData("distance", "Distance", voyageDetails.getOptions().getDistance(), ExtraDataFormatType.INTEGER);
		d.addExtraData("duration", "Duration", duration, ExtraDataFormatType.DURATION);
		d.addExtraData("idletime", "Idle Time", voyageDetails.getIdleTime(), ExtraDataFormatType.DURATION);
		d.addExtraData("traveltime", "Travel Time", voyageDetails.getTravelTime(), ExtraDataFormatType.DURATION);
		d.addExtraData("speed", "Speed", voyageDetails.getSpeed() / (double) Calculator.ScaleFactor, ExtraDataFormatType.STRING_FORMAT).setFormat("%,f");
		int routeCost = (int) (voyageDetails.getRouteCost() / Calculator.ScaleFactor);
		totalCost += routeCost;
		d.addExtraData("routecost", "Route Cost", routeCost, ExtraDataFormatType.CURRENCY)
			.addExtraData("route", "Route", voyageDetails.getOptions().getRoute(), ExtraDataFormatType.AUTO);
		
		final ExtraData fuelData = d.addExtraData("fuelcost", "Fuel Cost");
		int totalFuelCost = 0;
		
		for (final FuelComponent component : FuelComponent.values()) {
			final long consumption = voyageDetails.getFuelConsumption(component, component.getDefaultFuelUnit());
			final long unitPrice = voyageDetails.getFuelUnitPrice(component);
			
			if (consumption == 0) continue;
			final int componentCost = (int) (Calculator.multiply(consumption, unitPrice) / Calculator.ScaleFactor);
			totalFuelCost += componentCost;
			ExtraData componentData = fuelData.addExtraData(component.name(), component.name(), componentCost, ExtraDataFormatType.CURRENCY);
			componentData.addExtraData("quantity", "Usage (" + component.getDefaultFuelUnit().name() + ")", (int) (consumption/Calculator.ScaleFactor), ExtraDataFormatType.INTEGER);
			componentData.addExtraData("unitprice", "Cost/"+ component.getDefaultFuelUnit().name(), unitPrice / (double) Calculator.ScaleFactor, ExtraDataFormatType.STRING_FORMAT).setFormat("$%,f");
			
		}
		totalCost += totalFuelCost;
		fuelData.setValue(totalFuelCost);
		fuelData.setFormatType(ExtraDataFormatType.CURRENCY);
		
		int hireCost = (spec.getNotionalDayRate() * duration) / 24;
		totalCost += hireCost;
		d.setValue(totalCost);
		d.setFormatType(ExtraDataFormatType.CURRENCY);
		d.addExtraData("hirecost", "Hire Cost", hireCost, ExtraDataFormatType.CURRENCY);
	}

	private void createPortCostComponent(final ExtraData result, final Association<Port, IPort> ports, final PricingModel pricing, final UnitCostMatrix spec, final PortDetails portDetails) {
		result.addExtraData("duration", "Duration", portDetails.getVisitDuration(), ExtraDataFormatType.DURATION);
		final Port port = ports.reverseLookup(portDetails.getPortSlot().getPort());
		result.addExtraData("location", "Location", port.getName(), ExtraDataFormatType.AUTO);
		int total = 0;
		for (final PortCost cost : pricing.getPortCosts()) {
			if (SetUtils.getPorts(cost.getPorts()).contains(port)) {
				// this is the cost for the given port
				total += cost.getPortCost(spec.getVessel().getVesselClass(), portDetails.getPortSlot() instanceof ILoadSlot ? PortCapability.LOAD : PortCapability.DISCHARGE);
				result.addExtraData("portcost", "Port Cost", total,ExtraDataFormatType.CURRENCY);
				
				break;
			}
		}
		
		total += (spec.getNotionalDayRate() * portDetails.getVisitDuration()) / 24;
		result.addExtraData("hirecost", "Hire Cost", (spec.getNotionalDayRate() * portDetails.getVisitDuration()) / 24, ExtraDataFormatType.CURRENCY);
		
		result.setValue(total);
		result.setFormatType(ExtraDataFormatType.CURRENCY);
	}
	
	/**
	 * Tell the builder to set up the given vessel state from the EMF fleet model
	 * 
	 * @param builder
	 *            the builder which is currently in use
	 * @param vc
	 *            the {@link IVesselClass} which the builder has constructed whose attributes we are setting
	 * @param laden
	 *            the {@link com.mmxlabs.scheduler.optimiser.components.VesselState} we are setting attributes for
	 * @param ladenAttributes
	 *            the {@link VesselStateAttributes} from the EMF model
	 */
	private void buildVesselStateAttributes(final ISchedulerBuilder builder, final IVesselClass vc, final com.mmxlabs.scheduler.optimiser.components.VesselState state,
			final VesselStateAttributes attrs) {

		// create consumption rate calculator for the curve
		final TreeMap<Integer, Long> keypoints = new TreeMap<Integer, Long>();

		for (final FuelConsumption line : attrs.getFuelConsumption()) {
			keypoints.put(Calculator.scaleToInt(line.getSpeed()), Calculator.scale(line.getConsumption()) / 24);
		}

		final InterpolatingConsumptionRateCalculator consumptionCalculator = new InterpolatingConsumptionRateCalculator(keypoints);

		final LookupTableConsumptionRateCalculator cc = new LookupTableConsumptionRateCalculator(vc.getMinSpeed(), vc.getMaxSpeed(), consumptionCalculator);

		builder.setVesselClassStateParamaters(vc, state, Calculator.scaleToInt(attrs.getNboRate()) / 24, Calculator.scaleToInt(attrs.getIdleNBORate()) / 24,
				Calculator.scaleToInt(attrs.getIdleBaseRate()) / 24, cc);
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**

 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.builder;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.ILongCurve;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.MutableTimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IHeelOptions;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IMarkToMarket;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISpotCharterInMarket;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IXYPort;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.SequenceElement;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

/**
 * A builder to create {@link IOptimisationData} instances for Scheduler problems.
 * 
 * @author Simon Goodall
 * 
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface ISchedulerBuilder {

	/**
	 * Add an {@link IBuilderExtension} to this builder. The extension ought to be added before any other builder methods are used.
	 * 
	 * @param extension
	 */
	void addBuilderExtension(@NonNull IBuilderExtension extension);

	/**
	 * Finalise build process and return an {@link IOptimisationData} instance. The builder should not be used after calling this method.
	 * 
	 * @return
	 */
	@NonNull
	IOptimisationData getOptimisationData();

	/**
	 * Like the other {@link createVesselClass}, but with an hourly charter price specified (defaults to zero otherwise);
	 * 
	 * @param name
	 * @param minSpeed
	 *            Minimum vessel speed in scaled knots
	 * @param maxSpeed
	 *            Maximum vessel speed in scaled knots
	 * @param capacity
	 *            Maximum loadable cargo quantity in scaled M3
	 * @param safetyHeel
	 *            Minimum heel to retain (when appropriate) in scaled M3
	 * @param baseFuelUnitPrice
	 *            Price of base fuel in scaled $/MT
	 * @param baseFuelEquivalenceInM3TOMT
	 *            Scaled Conversion factor to convert M3 LNG to equivalent MT base fuel
	 * @return
	 */
	@NonNull
	IVesselClass createVesselClass(@NonNull String name, int minSpeed, int maxSpeed, long capacity, long safetyHeel, @NonNull IBaseFuel baseFuel, int pilotLightRate, int warmupTimeInHours,
			long cooldownVolumeInM3, int minBaseFuelConsumptionPerDay, boolean hasReliqCapability);

	/**
	 * Set {@link IVesselClass} parameters that depend upon the {@link VesselState}.
	 * 
	 * @param vesselClass
	 * @param state
	 * @param nboRateInM3PerHour
	 *            Hourly scaled M3 of LNG rate of boil-off when travelling
	 * @param idleNBORateInM3PerHour
	 *            Hourly scaled M3 of LNG rate of boil-off when idling
	 * @param idleConsumptionRateInMTPerHour
	 *            Hourly scaled MT of base fuel consumption rate when idle
	 * @param inPortConsumptionRateInMTPerHour
	 *            Hourly scale MT of base fuel consumption when in port.
	 * @param consumptionRateCalculatorInMTPerHour
	 *            {@link IConsumptionRateCalculator} returning hourly scaled MT of base fuel consumption rate when travelling based upon speed.
	 * @param nboSpeed
	 *            Scaled speed in knots indicating the speed at which the vessel can travel to use up all NBO when travelling. * @param serviceSpeed Service speed of vessel in scaled knots
	 */
	void setVesselClassStateParameters(@NonNull IVesselClass vc, @NonNull VesselState state, int nboRateInM3PerHour, int idleNBORateInM3PerHour, int idleConsumptionRateInMTPerHour,
			@NonNull IConsumptionRateCalculator consumptionRateCalculatorInMTPerHour, int serviceSpeed, int inPortNBORateInM3PerHour);

	/**
	 * Set {@link IVesselClass} parameters that depend upon the {@link PortType}.
	 * 
	 * @param vesselClass
	 * @param portType
	 * @param inPortConsumptionRateInMTPerHour
	 *            Hourly scale MT of base fuel consumption when in port.
	 */
	void setVesselClassPortTypeParameters(@NonNull IVesselClass vc, @NonNull PortType portType, int inPortConsumptionRateInMTPerDay);

	/**
	 * Create a base fuel
	 * 
	 * @param name
	 * @param equivalenceFactor
	 * @return
	 */
	@NonNull
	IBaseFuel createBaseFuel(@NonNull String name, int equivalenceFactor);

	/**
	 * Create a charter out event
	 * 
	 * @param id
	 *            the ID of the charter out
	 * @param arrivalTimeWindow
	 *            a time window in which the vessel must arrive at the port
	 * @param startPort
	 *            the port where the client is collecting the vessel
	 * @param endPort
	 *            the port where the vessel is being returned to
	 * @param durationHours
	 *            how long the charter out is for, in hours
	 * @param maxHeelOut
	 *            the maximum amount of heel available for travel
	 * @param heelCVValue
	 *            the CV value of heel available for travel
	 * @return
	 */
	@NonNull
	IVesselEventPortSlot createCharterOutEvent(@NonNull String id, @NonNull ITimeWindow arrivalTimeWindow, @NonNull IPort startPort, @NonNull IPort endPort, int durationHours, long maxHeelOut,
			int heelCVValue, int heelUnitPrice, final long hireRevenue, final long repositioning, final long ballastBonus);

	/**
	 * Create a dry dock event
	 * 
	 * @param id
	 *            the ID of the dry dock
	 * @param arrivalTimeWindow
	 *            the time window in which the vessel must arrive at the port
	 * @param port
	 *            the port where the dry dock is
	 * @param durationHours
	 *            the number of hours the dry dock will take
	 * @return
	 */
	@NonNull
	IVesselEventPortSlot createDrydockEvent(@NonNull String id, @NonNull ITimeWindow arrivalTimeWindow, @NonNull IPort port, int durationHours);

	/**
	 * Create a maintenance event
	 * 
	 * @param id
	 *            the ID of the maintenance
	 * @param arrivalTimeWindow
	 *            the time window in which the vessel must arrive at the port
	 * @param port
	 *            the port where the maintenance is
	 * @param durationHours
	 *            the number of hours the maintenance will take
	 * @return
	 */
	@NonNull
	IVesselEventPortSlot createMaintenanceEvent(@NonNull String id, @NonNull ITimeWindow arrival, @NonNull IPort port, int durationHours);

	/**
	 * Create a vessel with the given name, class and capacity.
	 * 
	 * @param name
	 * @param vesselClass
	 * @return
	 */
	@NonNull
	IVessel createVessel(@NonNull String name, @NonNull IVesselClass vesselClass, long cargoCapacity);

	@NonNull
	IHeelOptions createHeelOptions(final long heelLimitInM3, final int heelCVValue, final int heelUnitPrice);

	/**
	 * Create a vessel availability for the with the given vessel .
	 * 
	 * @param vessel
	 * @param vesselInstanceType
	 * @param start
	 * @param end
	 * @param repositioningFee TODO
	 * @param ballastBonus TODO
	 * @param isOptional TODO
	 * @return
	 */
	@NonNull
	IVesselAvailability createVesselAvailability(@NonNull IVessel vessel, @NonNull ILongCurve dailyCharterInPrice, @NonNull VesselInstanceType vesselInstanceType, @NonNull IStartRequirement start,
			@NonNull IEndRequirement end, ILongCurve repositioningFee, ILongCurve ballastBonus, boolean isOptional);

	@NonNull
	public IStartRequirement createStartRequirement(@Nullable IPort fixedPort, @Nullable ITimeWindow timeWindow, @Nullable IHeelOptions heelOptions);

	@NonNull
	public IEndRequirement createEndRequirement(@Nullable Collection<IPort> portSet, @Nullable ITimeWindow timeWindow, boolean endCold, long targetHeelInM3, boolean isOpenEnded);

	/**
	 * Create a port with the given name and cooldown requirement
	 * 
	 * @param name
	 * @return
	 */
	@NonNull
	IPort createPort(@NonNull String name, boolean arriveCold, @Nullable final ICooldownCalculator cooldownCalculator, @NonNull final String timezoneId, final int minCvValue, final int maxCvValue);

	/**
	 * Create a port with an x/y co-ordinate.
	 * 
	 * @param name
	 * @param x
	 * @param y
	 * @return
	 */
	@NonNull
	IXYPort createPort(@NonNull String name, boolean arriveCold, @Nullable final ICooldownCalculator cooldownCalculator, float x, float y, @NonNull final String timezoneId, final int minCvValue,
			final int maxCvValue);

	/**
	 * Create a cargo with the initial port slots. If allowRewiring is false, bind the slot sequence.
	 * 
	 * @param slots
	 *            A {@link Collection} of {@link ILoadOption}s and {@link IDischargeOption}s
	 * @return
	 */
	@NonNull
	ICargo createCargo(@NonNull final Collection<@NonNull IPortSlot> slots, final boolean allowRewiring);

	/**
	 */
	@NonNull
	ICargo createCargo(final boolean allowRewiring, @NonNull final IPortSlot... slots);

	// /**
	// * Restrict the set of vessels which can carry this slot to those in the second argument.
	// *
	// * If this method is never called, the slot can be carried by any vessel.
	// *
	// * @param slot
	// * a {@link ILoadOption} or {@link IDischargeOption}
	// * @param vessels
	// * a set of vessels on which this cargo may be carried
	// */
	// void setSlotVesselAvailabilityRestriction(@NonNull IPortSlot slot, @NonNull Set<IVesselAvailability> vessels);

	// /**
	// * Create a time window with the specified start and end time. If the end time is {@link Integer#MIN_VALUE}, then assume the end time is unbounded and it will be replaced with the latest time in
	// * the scenario.
	// *
	// * @param start
	// * Time window start
	// * @param end
	// * Time window end
	// * @return
	// */
	// @NonNull
	// ITimeWindow createTimeWindow(int start, int end);
	//
	// /**
	// * Create a time window with the specified start and end time. If the end time is {@link Integer#MIN_VALUE}, then assume the end time is unbounded and it will be replaced with the latest time in
	// * the scenario.
	// *
	// * @param start
	// * Time window start
	// * @param end
	// * Time window end
	// * @param endFlex
	// * Time window endFlex
	// * @return
	// */
	// @NonNull
	// ITimeWindow createTimeWindow(int start, int end, int endFlex);

	/**
	 * Specify a one-way distance between two ports
	 * 
	 * @param from
	 * @param to
	 * @param distance
	 */
	void setPortToPortDistance(@NonNull IPort from, @NonNull IPort to, @NonNull ERouteOption route, int distance);

	/**
	 * Set a toll for sending a given vessel + state via a given route
	 * 
	 * @param route
	 * @param vessel
	 * @param state
	 * @param tollPrice
	 */
	void setVesselRouteCost(final @NonNull ERouteOption route, @NonNull final IVessel vessel, final IRouteCostProvider.@NonNull CostType costType, final @NonNull ILongCurve tollPrice);

	/**
	 * Set the default toll associated with passing by a given route
	 * 
	 * @param route
	 *            the route name
	 * @param defaultPrice
	 *            the associated toll in dollars
	 */
	void setDefaultRouteCost(@NonNull ERouteOption route, @NonNull ILongCurve defaultPrice);

	/**
	 * Set the extra time and fuel required for the given vessel to travel by the given route
	 * 
	 * @param route
	 *            the route
	 * @param vessel
	 *            the vessel
	 * @param vesselState
	 *            the vessel state
	 * @param baseFuelInScaledMT
	 *            the extra base fuel or equivalent required, in up-scaled MT (see {@link Calculator#ScaleFactor})
	 * @param nboRateInScaledM3
	 *            the NBO rate in up-scaled M3 (see {@link Calculator#ScaleFactor})
	 */
	void setVesselRouteFuel(@NonNull ERouteOption route, @NonNull IVessel vessel, VesselState vesselState, long baseFuelInScaledMT, long nboRateInScaledM3);

	/**
	 * Set the extra time required for the given vessel to travel by the given route
	 * 
	 * @param name
	 *            the name of the route
	 * @param vessel
	 *            the vessel
	 * @param time
	 *            the extra transit time required, in hours
	 */
	void setVesselRouteTransitTime(@NonNull ERouteOption route, @NonNull IVessel vessel, int timeInHours);

	/**
	 * Specify an amount of time a given {@link IResource} must incur if assigned to the given {@link ISequenceElement}.
	 * 
	 * @param element
	 * @param resource
	 * @param duration
	 */
	void setElementDurations(@NonNull ISequenceElement element, @NonNull IResource resource, int duration);

	/**
	 * Create a new {@link ILoadSlot} instance. This is currently expected to be assigned to a cargo.
	 * 
	 * @param id
	 * @param port
	 * @param window
	 * @param minVolume
	 *            Scaled minimum loadable quantity of LNG in M3
	 * @param maxVolume
	 *            Scaled maximum loadable quantity of LNG in M3
	 * @param cargoCVValue
	 *            Scaled conversion factor to convert from M3 to MMBTU of LNG
	 * @param slotIsLocked
	 *            TODO
	 * @param price
	 *            Scaled purchase price in $/MMBTu
	 * @return
	 */
	@NonNull
	ILoadSlot createLoadSlot(String id, @NonNull IPort port, @NonNull ITimeWindow window, long minVolume, long maxVolume, @NonNull ILoadPriceCalculator priceCalculator, int cargoCVValue,
			int durationHours, boolean cooldownSet, boolean cooldownForbidden, int pricingDate, @NonNull PricingEventType pricingEvent, boolean slotIsOptional, boolean slotIsLocked,
			boolean isSpotMarketSlot, boolean isVolumeLimitInM3);

	/**
	 */
	@NonNull
	ILoadOption createDESPurchaseLoadSlot(String id, @Nullable IPort port, ITimeWindow window, long minVolume, long maxVolume, @NonNull ILoadPriceCalculator priceCalculator, int cargoCVValue,
			int durationInHours, int pricingDate, @NonNull PricingEventType pricingEvent, boolean slotIsOptional, boolean slotIslocked, boolean isSpotMarketSlot, boolean isVolumeLimitInM3);

	/**
	 * Create a new {@link IDischargeSlot} instance. This is currently expected to be assigned to a cargo.
	 * 
	 * @param id
	 * @param port
	 * @param window
	 * @param minVolume
	 *            Scaled minimum dischargable quantity of LNG in M3 (TODO: this may need to be MMBTu, perhaps add a {@link FuelUnit}?)
	 * @param maxVolume
	 *            Scaled maximum dischargable quantity of LNG in M3 (TODO: this may need to be MMBTu, perhaps add a {@link FuelUnit}?)
	 * @param price
	 *            Scaled sales price in $/MMBTu
	 * @return
	 */
	@NonNull
	IDischargeSlot createDischargeSlot(String id, @NonNull IPort port, @NonNull ITimeWindow window, long minVolumeInM3, long maxVolumeInM3, long minCvValue, long maxCvValue,
			@NonNull ISalesPriceCalculator pricePerMMBTu, int durationHours, int pricingDate, @NonNull PricingEventType pricingEvent, boolean optional, boolean isLockedSlot, boolean isSpotMarketSlot,
			boolean isVolumeLimitInM3);

	/**
	 * 
	 * @param id
	 * @param port
	 * @param window
	 * @param minVolume
	 * @param maxVolume
	 * @param priceCalculator
	 * @param slotIsOptional
	 * @return
	 */
	@NonNull
	IDischargeOption createFOBSaleDischargeSlot(@NonNull String id, @Nullable IPort port, @NonNull ITimeWindow window, long minVolume, long maxVolume, long minCvValue, long maxCvValue,
			@NonNull ISalesPriceCalculator priceCalculator, int durationInHours, int pricingDate, PricingEventType pricingEvent, boolean slotIsOptional, boolean slotIsLocked, boolean isSpotMarketSlot,
			boolean isVolumeLimitInM3);

	/**
	 * Clean up builder resources. TODO: We assume the opt-data object owns the data providers. However, the builder will own them until then. Dispose should selectively clean these
	 * upbaseFuelConversionFactor.
	 */
	void dispose();

	/**
	 * Create {@code count} spot charter vessels of the given type. The ith vessel will be named namePrefix-i. The vessels are created by the {@code createVessel()} method.
	 * 
	 * @param namePrefix
	 *            the common prefix for all these vessels' names
	 * @param spotCharterInMarket
	 * @return
	 */
	@NonNull
	List<IVesselAvailability> createSpotVessels(@NonNull String namePrefix, @NonNull ISpotCharterInMarket spotCharterInMarket);

	/**
	 * Create a single spot vessel of the given class, with the given name. This is equivalent to
	 * {@code createVessel(name, vesselClass, VesselInstanceType.SPOT_CHARTER, createStartEndRequirement(), createStartEndRequirement())} .
	 * 
	 * @param name
	 * @return
	 */
	@NonNull
	IVesselAvailability createSpotVessel(@NonNull String name, int spotIndex, @NonNull ISpotCharterInMarket spotCharterInMarket);

	/**
	 * Set the list of ports this vessel is not permitted to travel to.
	 * 
	 * @param vessel
	 * @param inaccessiblePorts
	 */
	void setVesselInaccessiblePorts(@NonNull IVessel vessel, @NonNull Set<IPort> inaccessiblePorts);

	/**
	 * Set the list of ports vessels of this class are not permitted to travel to.
	 * 
	 * @param vessel
	 * @param inaccessiblePorts
	 */
	void setVesselClassInaccessiblePorts(@NonNull IVesselClass vc, @NonNull Set<IPort> inaccessiblePorts);

	/**
	 * Add a global total volume limit
	 * 
	 * @param ports
	 *            The set of ports for which the limit should apply
	 * @param loads
	 *            Whether to apply to load slots
	 * @param discharges
	 *            Whether to apply to discharge slots
	 * @param maximumTotalVolume
	 *            The maximum total quantity which can be allocated to slots in the time window
	 * @param timeWindow
	 *            The time window within which the limit applies.
	 */
	void addTotalVolumeConstraint(@NonNull Set<IPort> ports, boolean loads, boolean discharges, long maximumTotalVolume, @NonNull ITimeWindow timeWindow);

	// /**
	// * Constrains the given slot to lie only on the given vessels. Note: Special vessels such as those for DES Purchases and FOB Sales are still permitted.
	// *
	// * Note that this does not ensure the compatibility of any other constraints; for example, if you use {@link #setVesselClassInaccessiblePorts(IVesselClass, Set)} to prevent vessels of this class
	// * from visiting the port for this slot, you will have an unsolvable scenario.
	// *
	// * Passing an empty set or null will clear any constraint
	// *
	// * @param slot
	// * the slot to bind to a vessel
	// * @param vessel
	// * the vessel to keep this slot on
	// */
	// void constrainSlotToVesselAvailabilities(@NonNull IPortSlot slot, @Nullable Set<IVesselAvailability> vessels);
	//
	// /**
	// * Constrains the given slot to lie only on vessels with the given classes.
	// *
	// * In the end the slot will be on the union of vessels with these classes and any vessels set with {@link #constrainSlotToVesselAvailabilities(IPortSlot, Set)}.
	// *
	// * Passing an empty or null set will clear any constraint.
	// *
	// * Calls to this method <em>replace</em> previous calls, rather than combining them.
	// *
	// * @param slot
	// * @param vesselClasses
	// */
	// void constrainSlotToVesselClasses(@NonNull IPortSlot slot, @Nullable Set<IVesselClass> vesselClasses);

	/**
	 * <p>
	 * Constraints the given port slots to be adjacent to one another in the solution, in the order that they occur as arguments here.
	 * </p>
	 * <p>
	 * Notes:
	 * <ol>
	 * <li>This does nothing to ensure the compatibility of other constraints, so if you constrain two slots to be adjacent and then restrict them to different vessels with
	 * {@link #constrainSlotToVesselAvailabilities(IPortSlot, IVessel)} (for example) you will have an unsolvable scenario.</li>
	 * <li>Slots do not always relate directly to sequence elements; slots which produce a redirection, like some charter outs, can result in the creation of several sequence elements (although those
	 * elements have their own slots, they are internal). This method does account for that, so if you constrain something to come before a charter out with a redirection the virtual elements
	 * introduced for the redirection won't be a problem.</li>
	 * <li>Passing null for either argument removes any existing constraint set with the non-null argument in the same position, so
	 * <code>constrainSlotAdjacency(x, y); constrainSlotAdjacency(null, y);</code> is an identity operation.</li>
	 * </ol>
	 * </p>
	 * 
	 * @param firstSlot
	 *            this slot will always precede secondSlot
	 * @param secondSlot
	 *            this slot will always after firstSlot
	 */
	void constrainSlotAdjacency(@Nullable IPortSlot firstSlot, @Nullable IPortSlot secondSlot);

	/**
	 * Set a discount curve for the given fitness component name
	 * 
	 * @param name
	 * @param iCurve
	 */
	void setFitnessComponentDiscountCurve(@NonNull String name, @NonNull ICurve iCurve);

	/**
	 * Set the cost of visiting the given port + vessel in the given way.
	 * 
	 * @param port
	 * @param vessel
	 * @param portType
	 * @param cost
	 */
	void setPortCost(@NonNull IPort port, @NonNull IVessel vessel, @NonNull PortType portType, long cost);

	/**
	 * Permit all real discharge slots which are located at one of the {@link IPort}s in the provided {@link Collection} to be re-wired to the given DES Purchase.
	 * 
	 * @param desPurchase
	 * @param dischargePorts
	 */
	void bindDischargeSlotsToDESPurchase(@NonNull ILoadOption desPurchase, @NonNull Map<IPort, ITimeWindow> dischargePorts);

	/**
	 * Permit all real load slots at the given {@link IPort}s to be re-wired to the given FOB Sale.
	 * 
	 * @param desPurchase
	 * @param dischargePorts
	 */
	void bindLoadSlotsToFOBSale(@NonNull IDischargeOption fobSale, @NonNull Map<IPort, ITimeWindow> loadPorts);

	/**
	 * Place a {@link Collection} of {@link IPortSlot}s into a "count" group - that is a group in which only the count number of elements may be used.
	 * 
	 * @param slots
	 * @param count
	 */
	void createSlotGroupCount(@NonNull Collection<IPortSlot> slots, int count);

	/**
	 * Set the earliest {@link Date} that will represent time zero.
	 * 
	 */
	void setEarliestDate(@NonNull ZonedDateTime earliestTime);

	/**
	 * @param vesselClass
	 *            The {@link IVesselClass} this data applies to
	 * @param charterOutCurve
	 *            The hourly charter out price curve
	 * @param minDuration
	 *            The minimum duration in hours a charter out can be.
	 */
	void createCharterOutCurve(@NonNull IVesselClass vesselClass, @NonNull ILongCurve charterOutCurve, int minDuration, @NonNull Set<IPort> allowedPorts);

	/**
	 * Set a flag to indicate that the given {@link IPortSlot} is to be treated as "soft required". That is generally optional, but not entirely. For example a fitness component may penalise such
	 * slots for not being used.
	 * 
	 * @param slot
	 */
	void setSoftRequired(@NonNull IPortSlot slot);

	/**
	 * Set the default CV value for a port.
	 * 
	 * @param port
	 * @param cv
	 *            value
	 */
	void setPortCV(@NonNull IPort port, int cv);

	/**
	 * Set the Min CV value for a port.
	 * 
	 * @param port
	 * @param cv
	 *            value
	 */
	void setPortMinCV(@NonNull IPort port, int cv);

	/**
	 * Set the Max CV value for a port.
	 * 
	 * @param port
	 * @param cv
	 *            value
	 */
	void setPortMaxCV(@NonNull IPort port, int cv);

	/**
	 * Create a Mark-To-Market market for DES Purchases valid against the given set of {@link IPort}s
	 * 
	 */
	@NonNull
	IMarkToMarket createDESPurchaseMTM(@NonNull Set<IPort> marketPorts, int cargoCVValue, @NonNull ILoadPriceCalculator priceCalculator, @NonNull IEntity entity);

	/**
	 * Create a Mark-To-Market market for FOB sales valid against the given set of {@link IPort}s
	 * 
	 */
	@NonNull
	IMarkToMarket createFOBSaleMTM(@NonNull Set<IPort> marketPorts, @NonNull ISalesPriceCalculator priceCalculator, @NonNull IEntity entity);

	/**
	 * Create a Mark-To-Market market for FOB Purchases valid against the given set of {@link IPort}s
	 * 
	 * @param cargoCVValue
	 * 
	 */
	@NonNull
	IMarkToMarket createFOBPurchaseMTM(@NonNull Set<IPort> marketPorts, int cargoCVValue, @NonNull ILoadPriceCalculator priceCalculator, @NonNull IEntity entity);

	/**
	 * Create a Mark-To-Market market for DES sales valid against the given set of {@link IPort}s
	 * 
	 */
	@NonNull
	IMarkToMarket createDESSalesMTM(@NonNull Set<IPort> marketPorts, @NonNull ISalesPriceCalculator priceCalculator, @NonNull IEntity entity);

	/**
	 * For DES Purchases and FOB Sales specify the nominated vessel
	 * 
	 * @param slot
	 * @param hours
	 */
	void setNominatedVessel(@NonNull IPortSlot slot, @NonNull IVessel vessel);

	/**
	 * For DES Purchases and FOB Sales specify the restriction on shipping for redirection basis.
	 * 
	 * @param slot
	 * @param hours
	 */
	void setShippingHoursRestriction(@NonNull IPortSlot slot, @NonNull ITimeWindow baseTime, int hours);

	void setShippingDaysRestrictionReferenceSpeed(@NonNull IVessel vessel, @NonNull VesselState vesselState, int referenceSpeed);

	/**
	 * Freeze a {@link IPortSlot} to a single {@link IVesselAvailability}. Unlike {@link #constrainSlotToVesselAvailabilities(IPortSlot, Set)} which still permits allocations to special vessels, this
	 * method restricts purely to the specified {@link IVessel}
	 * 
	 * @param portSlot
	 * @param vesselAvailability
	 */
	void freezeSlotToVesselAvailability(@NonNull IPortSlot portSlot, @NonNull IVesselAvailability vesselAvailability);

	/**
	 * Set the earliest time we can start generating charter outs.
	 * 
	 * @param charterOutStartTime
	 */
	void setGeneratedCharterOutStartTime(int charterOutStartTime);

	@NonNull
	ISpotCharterInMarket createSpotCharterInMarket(@NonNull String name, @NonNull IVesselClass oVesselClass, @NonNull ILongCurve charterInCurve, int charterCount);

	/***
	 * Create a sequence element
	 * 
	 * @param name
	 * @return
	 */
	@NonNull
	SequenceElement createSequenceElement(@NonNull String name);

	void setDivertableDESAllowedRoute(@NonNull ILoadOption loadOption, @NonNull List<ERouteOption> allowedRoutes);

	@NonNull
	IVesselAvailability createRoundTripCargoVessel(@NonNull String name, @NonNull ISpotCharterInMarket market);

	void bindSlotsToRoundTripVessel(@NonNull IVesselAvailability roundTripCargoVessel, @NonNull IPortSlot @NonNull... slots);

	void setVesselAndClassPermissions(@NonNull IPortSlot portSlot, @Nullable List<@NonNull IVessel> permittedVessels, @Nullable List<@NonNull IVesselClass> permittedVesselClasses);

	/**
	 * Register a time window with an open end date that needs to be adjusted to sync up with optimisation end date
	 * 
	 * @param window
	 */
	void addOpenEndWindow(@NonNull MutableTimeWindow window);

	/**
	 * Set Inaccessible Routes (canals) for a vessel
	 * 
	 * @param vessel
	 * @param inaccessibleRoutes
	 */
	void setVesselInaccessibleRoutes(@NonNull IVessel vessel, Set<ERouteOption> inaccessibleRoutes);

	/**
	 * Set Inaccessible Routes (canals) for a class of vessel
	 * 
	 * @param vessel
	 * @param inaccessibleRoutes
	 */
	void setVesselClassInaccessibleRoutes(@NonNull IVesselClass vesselClass, Set<ERouteOption> inaccessibleRoutes);
}

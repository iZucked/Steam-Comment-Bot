/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.builder;

import java.util.List;
import java.util.Set;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IXYPort;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISimpleLoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

/**
 * A builder to create {@link IOptimisationData} instances for Scheduler problems.
 * 
 * @author Simon Goodall
 * 
 */
public interface ISchedulerBuilder {

	/**
	 * Finalise build process and return an {@link IOptimisationData} instance. The builder should not be used after calling this method.
	 * 
	 * @return
	 */
	IOptimisationData<ISequenceElement> getOptimisationData();

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
	 * @param minHeel
	 *            Minimum heel to retain (when appropriate) in scaled M3
	 * @param baseFuelUnitPrice
	 *            Price of base fuel in scaled $/MT
	 * @param baseFuelEquivalenceInM3TOMT
	 *            Scaled Conversion factor to convert M3 LNG to equivalent MT base fuel
	 * @param hourlyCharterPrice
	 *            $/Hour rate to charter-in vessels of this class
	 * @return
	 */
	IVesselClass createVesselClass(String name, int minSpeed, int maxSpeed, long capacity, int minHeel, int baseFuelUnitPrice, int baseFuelEquivalenceInM3TOMT, int hourlyCharterPrice,
			int warmupTimeInHours, int cooldownTimeInHours, int cooldownVolumeInM3);

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
	 * @param consumptionRateCalculatorInMTPerHour
	 *            {@link IConsumptionRateCalculator} returning hourly scaled MT of base fuel consumption rate when travelling based upon speed.
	 * @param nboSpeed
	 *            Scaled speed in knots indicating the speed at which the vessel can travel to use up all NBO when travelling.
	 */
	void setVesselClassStateParamaters(IVesselClass vesselClass, VesselState state, int nboRateInM3PerHour, int idleNBORateInM3PerHour, int idleConsumptionRateInMTPerHour,
			IConsumptionRateCalculator consumptionRateCalculatorInMTPerHour, int nboSpeed);

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
	IVesselEventPortSlot createCharterOutEvent(String id, ITimeWindow arrivalTimeWindow, IPort startPort, IPort endPort, int durationHours, long maxHeelOut, int heelCVValue);

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
	IVesselEventPortSlot createDrydockEvent(String id, ITimeWindow arrivalTimeWindow, IPort port, int durationHours);

	/**
	 * Add a single vessel to the list of vessels which can service the given {@link IVesselEventPortSlot}
	 * 
	 * @param charterOut
	 * @param vessel
	 */
	void addVesselEventVessel(IVesselEventPortSlot event, IVessel vessel);

	/**
	 * Add all the vessels in a given class to the vessels which can service the given event slot.
	 * 
	 * @param event
	 * @param vesselClass
	 */
	void addVesselEventVesselClass(IVesselEventPortSlot event, IVesselClass vesselClass);

	/**
	 * Create a core fleet vessel with the given name and class.
	 * 
	 * @param name
	 * @param vesselClass
	 * @return
	 */
	IVessel createVessel(String name, IVesselClass vesselClass, int hourlyCharterOutRate, IStartEndRequirement startConstraint, IStartEndRequirement endConstraint, final long heelLimit,
			final int heelCVValue, final int heelUnitPrice);

	/**
	 * Create a fleet vessel with the given name, class and instance type.
	 * 
	 * @param name
	 * @param vesselClass
	 * @param vesselInstanceType
	 * @param start
	 * @param end
	 * @return
	 */
	IVessel createVessel(String name, IVesselClass vesselClass, int hourlyCharterOutRate, VesselInstanceType vesselInstanceType, IStartEndRequirement start, IStartEndRequirement end,
			final long heelLimit, final int heelCVValue, final int heelUnitPrice);

	/**
	 * Create a start/end requirement which constrains nothing
	 * 
	 * @return
	 */
	IStartEndRequirement createStartEndRequirement();

	/**
	 * Create a requirement that the vessel start/end at the given port, but at an arbitrary time
	 * 
	 * @param fixedPort
	 * @return
	 */
	IStartEndRequirement createStartEndRequirement(IPort fixedPort);

	/**
	 * Create a requirement that the vessel start/end at any port at the given time
	 * 
	 * @param fixedTime
	 * @return
	 */
	IStartEndRequirement createStartEndRequirement(ITimeWindow timeWindow);

	/**
	 * Create a requirement that the vessel start/end at the given port and time
	 * 
	 * @param fixedPort
	 * @param fixedTime
	 * @return
	 */
	IStartEndRequirement createStartEndRequirement(IPort fixedPort, ITimeWindow timeWindow);

	/**
	 * Create a port with the given name and cooldown requirement
	 * 
	 * @param name
	 * @return
	 */
	IPort createPort(String name, boolean arriveCold, final ISimpleLoadPriceCalculator cooldownPriceCalculator);

	/**
	 * Create a port with an x/y co-ordinate.
	 * 
	 * @param name
	 * @param x
	 * @param y
	 * @return
	 */
	IXYPort createPort(String name, boolean arriveCold, final ISimpleLoadPriceCalculator cooldownPriceCalculator, float x, float y);

	/**
	 * Create a cargo with the specific from and to ports and associated time windows.
	 * 
	 * @param id
	 * @param loadSlot
	 * @param dischargeSlot
	 * @return
	 */
	ICargo createCargo(String id, ILoadSlot loadSlot, IDischargeSlot dischargeSlot);

	/**
	 * Restrict the set of vessels which can carry this cargo to those in the second argument.
	 * 
	 * If this method is never called, the cargo can be carried by any vessel.
	 * 
	 * @param cargo
	 *            a cargo created by {@link #createCargo()}
	 * @param vessels
	 *            a set of vessels on which this cargo may be carried
	 */
	void setCargoVesselRestriction(ICargo cargo, Set<IVessel> vessels);

	/**
	 * Create a time window with the specified start and end time.
	 * 
	 * @param start
	 *            Time window start
	 * @param end
	 *            Time window end
	 * @return
	 */
	ITimeWindow createTimeWindow(int start, int end);

	/**
	 * Specify a one-way distance between two ports
	 * 
	 * @param from
	 * @param to
	 * @param distance
	 */
	void setPortToPortDistance(IPort from, IPort to, String route, int distance);

	/**
	 * Set a toll for sending a given vessel class + state via a given route
	 * 
	 * @param route
	 * @param vesselClass
	 * @param state
	 * @param tollPrice
	 */
	void setVesselClassRouteCost(final String route, final IVesselClass vesselClass, final VesselState state, final int tollPrice);

	/**
	 * Set the default toll associated with passing by a given route
	 * 
	 * @param route
	 *            the route name
	 * @param defaultPrice
	 *            the associated toll in dollars
	 */
	void setDefaultRouteCost(String route, int defaultPrice);

	/**
	 * Set the extra time and fuel required for the given vessel class to travel by the given route
	 * 
	 * @param name
	 *            the name of the route
	 * @param vc
	 *            the vessel class
	 * @param time
	 *            the extra transit time required, in hours
	 * @param fuel
	 *            the extra base fuel or equivalent required, in up-scaled MT (see {@link Calculator#ScaleFactor})
	 */
	void setVesselClassRouteTimeAndFuel(String name, IVesselClass vc, int time, long fuel);

	/**
	 * Specify an amount of time a given {@link IResource} must incur if assigned to the given {@link ISequenceElement}.
	 * 
	 * @param element
	 * @param resource
	 * @param duration
	 */
	void setElementDurations(ISequenceElement element, IResource resource, int duration);

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
	 * @param price
	 *            Scaled purchase price in $/MMBTu
	 * @param cargoCVValue
	 *            Scaled conversion factor to convert from M3 to MMBTU of LNG
	 * @return
	 */
	ILoadSlot createLoadSlot(String id, IPort port, ITimeWindow window, long minVolume, long maxVolume, ILoadPriceCalculator priceCalculator, int cargoCVValue, int durationHours, boolean cooldownSet,
			boolean cooldownForbidden);

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
	IDischargeSlot createDischargeSlot(String id, IPort port, ITimeWindow window, long minVolume, long maxVolume, ICurve unitPrice, int durationHours);

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
	 * @param vesselClass
	 *            the class of spot charter to create
	 * @param count
	 *            the number of spot charters to create
	 * @return
	 */
	List<IVessel> createSpotVessels(String namePrefix, IVesselClass vesselClass, int count);

	/**
	 * Create a single spot vessel of the given class, with the given name. This is equivalent to
	 * {@code createVessel(name, vesselClass, VesselInstanceType.SPOT_CHARTER, createStartEndRequirement(), createStartEndRequirement())} .
	 * 
	 * @param name
	 * @param vesselClass
	 * @return
	 */
	IVessel createSpotVessel(String name, IVesselClass vesselClass);

	void setVesselClassInaccessiblePorts(IVesselClass vc, Set<IPort> inaccessiblePorts);

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
	void addTotalVolumeConstraint(Set<IPort> ports, boolean loads, boolean discharges, long maximumTotalVolume, ITimeWindow timeWindow);

	ILoadPriceCalculator createFixedPriceContract(int pricePerMMBTU);

	ILoadPriceCalculator createMarketPriceContract(ICurve index);

	ILoadPriceCalculator createProfitSharingContract(ICurve actualMarket, ICurve referenceMarket, int alpha, int beta, int gamma);

	ILoadPriceCalculator createNetbackContract(int buyersMargin);

	/**
	 * Constrains the given slot to lie only on the given vessels.
	 * 
	 * Note that this does not ensure the compatibility of any other constraints; for example, if you use {@link #setVesselClassInaccessiblePorts(IVesselClass, Set)} to prevent vessels of this class
	 * from visiting the port for this slot, you will have an unsolvable scenario.
	 * 
	 * Passing an empty set or null will clear any constraint
	 * 
	 * @param slot
	 *            the slot to bind to a vessel
	 * @param vessel
	 *            the vessel to keep this slot on
	 */
	void constrainSlotToVessels(IPortSlot slot, Set<IVessel> vessels);

	/**
	 * Constrains the given slot to lie only on vessels with the given classes.
	 * 
	 * In the end the slot will be on the union of vessels with these classes and any vessels set with {@link #constrainSlotToVessels(IPortSlot, Set)}.
	 * 
	 * Passing an empty or null set will clear any constraint.
	 * 
	 * Calls to this method <em>replace</em> previous calls, rather than combining them.
	 * 
	 * @param slot
	 * @param vesselClasses
	 */
	void constrainSlotToVesselClasses(IPortSlot slot, Set<IVesselClass> vesselClasses);

	/**
	 * <p>
	 * Constraints the given port slots to be adjacent to one another in the solution, in the order that they occur as arguments here.
	 * </p>
	 * <p>
	 * Notes:
	 * <ol>
	 * <li>This does nothing to ensure the compatibility of other constraints, so if you constrain two slots to be adjacent and then restrict them to different vessels with
	 * {@link #constrainSlotToVessels(IPortSlot, IVessel)} (for example) you will have an unsolvable scenario.</li>
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
	void constrainSlotAdjacency(IPortSlot firstSlot, IPortSlot secondSlot);

	/**
	 * Set a discount curve for the given fitness component name
	 * 
	 * @param name
	 * @param iCurve
	 */
	void setFitnessComponentDiscountCurve(String name, ICurve iCurve);
}

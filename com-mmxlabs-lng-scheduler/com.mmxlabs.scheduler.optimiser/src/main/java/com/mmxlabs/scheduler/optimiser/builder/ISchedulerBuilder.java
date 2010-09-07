package com.mmxlabs.scheduler.optimiser.builder;

import java.util.List;
import java.util.Set;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.IXYPort;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;

/**
 * A builder to create {@link IOptimisationData} instances for Scheduler
 * problems.
 * 
 * @author Simon Goodall
 * 
 */
public interface ISchedulerBuilder {

	/**
	 * Finalise build process and return an {@link IOptimisationData} instance.
	 * The builder should not be used after calling this method.
	 * 
	 * @return
	 */
	IOptimisationData<ISequenceElement> getOptimisationData();

	/**
	 * Create a {@link IVesselClass} with the given parameters. Additional
	 * parameters should be set via
	 * {@link #setVesselClassStateParamaters(IVesselClass, VesselState, int, int, int, IConsumptionRateCalculator, int)}
	 * for each {@link VesselState}.
	 * 
	 * @param name
	 * @param minSpeed
	 * @param maxSpeed
	 * @param capacity
	 * @param minHeel
	 * @param baseFuelUnitPrice
	 * @param baseFuelEquivalenceInM3TOMT
	 * @return
	 */
	IVesselClass createVesselClass(String name, int minSpeed, int maxSpeed,
			long capacity, int minHeel, int baseFuelUnitPrice,
			int baseFuelEquivalenceInM3TOMT);

	/**
	 * Like the other {@link createVesselClass}, but with an hourly charter
	 * price specified (defaults to zero otherwise);
	 * 
	 * @param name
	 * @param minSpeed
	 * @param maxSpeed
	 * @param capacity
	 * @param minHeel
	 * @param baseFuelUnitPrice
	 * @param baseFuelEquivalenceInM3TOMT
	 * @param dailyCharterPrice
	 * @return
	 */
	IVesselClass createVesselClass(String name, int minSpeed, int maxSpeed,
			long capacity, int minHeel, int baseFuelUnitPrice, int baseFuelEquivalenceInM3TOMT, int hourlyCharterPrice);
	
	/**
	 * Set {@link IVesselClass} parameters that depend upon the
	 * {@link VesselState}.
	 * 
	 * @param vesselClass
	 * @param state
	 * @param nboRate
	 * @param idleNBORate
	 * @param idleConsumptionRate
	 * @param consumptionRateCalculator
	 * @param nboSpeed
	 */
	void setVesselClassStateParamaters(IVesselClass vesselClass,
			VesselState state, int nboRate, int idleNBORate,
			int idleConsumptionRate,
			IConsumptionRateCalculator consumptionRateCalculator, int nboSpeed);

	/**
	 * Create a core fleet vessel with the given name and class.
	 * 
	 * @param name
	 * @param vesselClass
	 * @return
	 */
	IVessel createVessel(String name, IVesselClass vesselClass, IStartEndRequirement startConstraint, IStartEndRequirement endConstraint);
	
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
	IVessel createVessel(String name, IVesselClass vesselClass,
			VesselInstanceType vesselInstanceType, IStartEndRequirement start,
			IStartEndRequirement end);

	/**
	 * Create a start/end requirement which constrains nothing
	 * 
	 * @return
	 */
	IStartEndRequirement createStartEndRequirement();

	/**
	 * Create a requirement that the vessel start/end at the given port, but at an arbitrary time
	 * @param fixedPort
	 * @return
	 */
	IStartEndRequirement createStartEndRequirement(IPort fixedPort);
	/**
	 * Create a requirement that the vessel start/end at any port at the given time
	 * @param fixedTime
	 * @return
	 */
	IStartEndRequirement createStartEndRequirement(int fixedTime);
	/**
	 * Create a requirement that the vessel start/end at the given port and time
	 * @param fixedPort
	 * @param fixedTime
	 * @return
	 */
	IStartEndRequirement createStartEndRequirement(IPort fixedPort, int fixedTime);
	
	/**
	 * Create a port with the given name.
	 * 
	 * @param name
	 * @return
	 */
	IPort createPort(String name);

	/**
	 * Create a port with an x/y co-ordinate.
	 * 
	 * @param name
	 * @param x
	 * @param y
	 * @return
	 */
	IXYPort createPort(String name, float x, float y);

	/**
	 * Create a cargo with the specific from and to ports and associated time
	 * windows.
	 * 
	 * @param id
	 * @param loadSlot
	 * @param dischargeSlot
	 * @return
	 */
	ICargo createCargo(String id, ILoadSlot loadSlot,
			IDischargeSlot dischargeSlot);

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
	 * Specify an amount of time a given {@link IResource} must incur if
	 * assigned to the given {@link ISequenceElement}.
	 * 
	 * @param element
	 * @param resource
	 * @param duration
	 */
	void setElementDurations(ISequenceElement element, IResource resource,
			int duration);

	/**
	 * Create a new {@link ILoadSlot} instance. This is currently expected to be
	 * assigned to a cargo.
	 * 
	 * @param id
	 * @param port
	 * @param window
	 * @param minVolume
	 * @param maxVolume
	 * @param price
	 * @return
	 */
	ILoadSlot createLoadSlot(String id, IPort port, ITimeWindow window,
			long minVolume, long maxVolume, int unitPrice);

	/**
	 * Create a new {@link IDischargeSlot} instance. This is currently expected
	 * to be assigned to a cargo.
	 * 
	 * @param id
	 * @param port
	 * @param window
	 * @param minVolume
	 * @param maxVolume
	 * @param price
	 * @return
	 */
	IDischargeSlot createDischargeSlot(String id, IPort port,
			ITimeWindow window, long minVolume, long maxVolume, int unitPrice);

	/**
	 * Clean up builder resources. TODO: We assume the opt-data object owns the
	 * data providers. However, the builder will own them until then. Dispose
	 * should selectively clean these upbaseFuelConversionFactor.
	 */
	void dispose();

	/**
	 * Create {@code count} spot charter vessels of the given type. The ith vessel will be named namePrefix-i. 
	 * The vessels are created by the {@code createVessel()} method.
	 * @param namePrefix the common prefix for all these vessels' names
	 * @param vesselClass the class of spot charter to create
	 * @param count the number of spot charters to create
	 * @return
	 */
	List<IVessel> createSpotVessels(String namePrefix,
			IVesselClass vesselClass, int count);

	/**
	 * Create a single spot vessel of the given class, with the given name. This is equivalent to
	 * {@code createVessel(name, vesselClass, VesselInstanceType.SPOT_CHARTER, createStartEndRequirement(), createStartEndRequirement())}.
	 * @param name
	 * @param vesselClass
	 * @return
	 */
	IVessel createSpotVessel(String name, IVesselClass vesselClass);

	void setVesselClassInaccessiblePorts(IVesselClass vc,
			Set<IPort> inaccessiblePorts);


}

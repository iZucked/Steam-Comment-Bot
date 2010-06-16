package com.mmxlabs.scheduler.optimiser.builder;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.optimiser.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IXYPort;

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
	 * Create a vessel with the given name.
	 * 
	 * @param name
	 * @return
	 */
	IVessel createVessel(String name);

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
	void setPortToPortDistance(IPort from, IPort to, int distance);

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
	 * Clean up builder resources. TODO: We assume the opt-data object owns the
	 * data providers. However, the builder will own them until then. Dispose
	 * should selectively clean these up.
	 */
	void dispose();

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
			long minVolume, long maxVolume, long price);

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
			ITimeWindow window, long minVolume, long maxVolume, long price);

}

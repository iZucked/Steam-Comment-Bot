package com.mmxlabs.scheduler.optimiser.builder;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.optimiser.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.ICargo;
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
	 * @param loadPort
	 * @param loadWindow
	 * @param dischargePort
	 * @param dischargeWindow
	 * @return
	 */
	ICargo createCargo(IPort loadPort, ITimeWindow loadWindow,
			IPort dischargePort, ITimeWindow dischargeWindow);

	ITimeWindow createTimeWindow(int start, int end);

	void setPortToPortDistance(IPort from, IPort to, int distance);

	void setElementDurations(ISequenceElement element, IResource resource,
			int duration);

	/**
	 * Clean up builder resources. TODO: We assume the opt-data object owns the
	 * data providers. However, the builder will own them until then. Dispose
	 * should selectively clean these up.
	 */
	void dispose();

}

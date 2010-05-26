package com.mmxlabs.scheduler.optimiser.components;

import com.mmxlabs.optimiser.components.ITimeWindow;

/**
 * A cargo ties together a load and discharge pairing.
 * 
 * @author Simon Goodall
 * 
 */
public interface ICargo {

	/**
	 * The {@link IPort} this cargo originates from.
	 * 
	 * @return
	 */
	IPort getLoadPort();

	/**
	 * The {@link ITimeWindow} in which this cargo needs to be loaded.
	 * 
	 * @return
	 */
	ITimeWindow getLoadWindow();

	/**
	 * The {@link IPort} this cargo is destined for.
	 * 
	 * @return
	 */
	IPort getDischargePort();

	/**
	 * The {@link ITimeWindow} in which this cargo needs to be discharged.
	 * 
	 * @return
	 */
	ITimeWindow getDischargeWindow();

	/**
	 * The ID of this cargo.
	 * 
	 * @return
	 */
	String getId();
}

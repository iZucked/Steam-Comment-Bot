package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;

/**
 * @since 8.0
 */
public interface IPortVisitDurationProvider extends IDataComponentProvider {

	/**
	 * Returns the default visit duration for a port
	 * 
	 * @param port
	 * @param portType
	 * @return
	 */
	int getVisitDuration(@NonNull IPort port, @NonNull PortType portType);
}

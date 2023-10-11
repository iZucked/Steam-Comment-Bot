package com.mmxlabs.scheduler.optimiser;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.common.components.InternalElementNameMapper;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

/**
 * Maps optimiser objects to an external name for user facing messages. Extended version supporting domain level objects.
 * 
 * @author Simon Goodall
 *
 */
@NonNullByDefault
public interface InternalNameMapper extends InternalElementNameMapper {

	default String generateString(IPortSlot e) {
		return e.getId();
	}

	default String generateString(IVessel vessel) {
		return vessel.getName();
	}

	default String generateString(IPort port) {
		return port.getName();
	}

}

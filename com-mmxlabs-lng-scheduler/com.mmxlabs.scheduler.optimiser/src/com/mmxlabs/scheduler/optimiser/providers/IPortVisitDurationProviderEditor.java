package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IPort;

/**
 * @since 8.0
 */
public interface IPortVisitDurationProviderEditor extends IPortVisitDurationProvider {

	void setVisitDuration(@NonNull IPort port, @NonNull PortType portType, int duration);
}

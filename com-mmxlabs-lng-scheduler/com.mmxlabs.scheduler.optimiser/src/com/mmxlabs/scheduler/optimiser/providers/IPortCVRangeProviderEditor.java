/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.components.IPort;

/**
 * Editor interface for a {@link IPortCVRangeProvider}
 * 
 * @author Alex Churchill
 * 
 */
public interface IPortCVRangeProviderEditor extends IPortCVRangeProvider {

	void setPortMinCV(@NonNull IPort port, int cv);
	void setPortMaxCV(@NonNull IPort port, int cv);
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IPort;

public interface IElementPortProviderEditor extends IElementPortProvider {

	void setPortForElement(@NonNull IPort port, @NonNull ISequenceElement element);
}

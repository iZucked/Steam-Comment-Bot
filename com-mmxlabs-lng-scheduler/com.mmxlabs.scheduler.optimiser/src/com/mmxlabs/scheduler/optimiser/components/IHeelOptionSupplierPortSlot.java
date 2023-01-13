/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.NonNull;

/**
 * A port slot for which there are some heel options
 * 
 * @author hinton
 * 
 */
public interface IHeelOptionSupplierPortSlot extends IPortSlot {
	@NonNull
	IHeelOptionSupplier getHeelOptionsSupplier();
}

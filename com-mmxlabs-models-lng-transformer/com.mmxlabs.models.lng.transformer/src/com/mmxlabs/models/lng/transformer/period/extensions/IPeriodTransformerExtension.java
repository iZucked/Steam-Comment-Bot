/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.period.extensions;

import java.util.List;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Slot;

public interface IPeriodTransformerExtension {

	
	@Nullable List<Slot> getExtraDependenciesForSlot(Slot slot);
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.parametermodes;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;

/**
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IParameterModesRegistry {

	Collection<@NonNull IParameterModeExtender> getExtenders();

	@NonNull
	Collection<@NonNull IParameterModeCustomiser> getCustomisers();
}

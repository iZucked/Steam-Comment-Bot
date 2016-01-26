/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.parametermodes;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IParameterModesRegistry {

	@Nullable
	IParameterModeCustomiser getCustomiser(@NonNull String name);

	@NonNull
	Collection<String> getParameterModes();

	/**
	 */
	@NonNull
	Collection<IParameterModeExtender> getExtenders();
}

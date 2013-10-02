/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.parametermodes;

import java.util.Collection;

/**
 * @since 5.0
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IParameterModesRegistry {

	IParameterModeCustomiser getCustomiser(String name);

	Collection<String> getParameterModes();

	/**
	 * @since 6.2
	 */
	Collection<IParameterModeExtender> getExtenders();
}

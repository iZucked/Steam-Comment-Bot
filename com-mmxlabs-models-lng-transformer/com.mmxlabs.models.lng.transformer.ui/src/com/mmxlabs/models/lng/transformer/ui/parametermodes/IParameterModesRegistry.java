/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.parametermodes;

import java.util.Collection;

/**
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IParameterModesRegistry {

	IParameterModeCustomiser getCustomiser(String name);

	Collection<String> getParameterModes();

	/**
	 */
	Collection<IParameterModeExtender> getExtenders();
}

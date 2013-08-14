/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.parametermodes;

import java.util.Collection;

/**
 * @since 5.0
 */
public interface IParameterModesRegistry {

	IParameterModeCustomiser getCustomiser(String name);

	Collection<String> getParameterModes();
}

package com.mmxlabs.models.lng.transformer.ui.parametermodes;

import java.util.Collection;

public interface IParameterModesRegistry {

	IParameterModeCustomiser getCustomiser(String name);

	Collection<String> getParameterModes();
}

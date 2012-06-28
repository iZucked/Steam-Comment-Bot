package com.mmxlabs.shiplingo.platform.reports;

import java.util.Collection;

import com.mmxlabs.models.mmxcore.MMXRootObject;

public interface IPinnableScenarioInstanceElementCollector extends
		IScenarioInstanceElementCollector {
	public Collection<? extends Object> collectElements(final MMXRootObject rootObject, final boolean isPinned);
}

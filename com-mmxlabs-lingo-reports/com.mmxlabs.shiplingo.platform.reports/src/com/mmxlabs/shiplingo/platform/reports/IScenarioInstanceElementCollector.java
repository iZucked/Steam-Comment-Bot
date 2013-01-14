/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports;

import java.util.Collection;

import com.mmxlabs.models.mmxcore.MMXRootObject;

public interface IScenarioInstanceElementCollector {
	public void beginCollecting();
	public void endCollecting();
	public Collection<? extends Object> collectElements(final MMXRootObject rootObject, final boolean isPinned);
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.utils;

import com.mmxlabs.models.lng.cargo.util.IAssignableElementComparator;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;


public interface IAssignableElementComparatorFactory {

	IAssignableElementComparator create(LNGScenarioModel scenario);
}

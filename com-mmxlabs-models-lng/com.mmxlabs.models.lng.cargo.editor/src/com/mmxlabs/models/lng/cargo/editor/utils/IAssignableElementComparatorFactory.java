package com.mmxlabs.models.lng.cargo.editor.utils;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

public interface IAssignableElementComparatorFactory {

	IAssignableElementComparator create(LNGScenarioModel scenario);
}

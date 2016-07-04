/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.utils;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.util.IAssignableElementDateProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

public interface IAssignableElementDateProviderFactory {

	@NonNull
	IAssignableElementDateProvider create(@NonNull LNGScenarioModel scenario);
}

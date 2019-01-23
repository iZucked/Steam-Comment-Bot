/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.breakeven;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisModel;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.components.AbstractModellerComponent;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;

public abstract class AbstractBreakEvenComponent extends AbstractModellerComponent<BreakEvenModellerView, BreakEvenAnalysisModel> {

	protected AbstractBreakEvenComponent(final @NonNull IScenarioEditingLocation scenarioEditingLocation, final Map<Object, IStatus> validationErrors,
			@NonNull Supplier<BreakEvenAnalysisModel> modelProvider) {

		super(scenarioEditingLocation, validationErrors, modelProvider);
	}

	@Override
	protected Set<Object> getTargetElementsForLabelProvider(String name, Object element) {
		return Collections.singleton(element);
	}

	@Override
	protected Set<Object> getTargetElementsForWiringProvider(Object element) {
		return Collections.singleton(element);
	}
}

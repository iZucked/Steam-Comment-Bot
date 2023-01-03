/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.valuematrix;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.SwapValueMatrixModel;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;

public abstract class AbstractValueMatrixValidatedComponent extends AbstractValueMatrixComponent {

	protected final Map<Object, Collection<IStatus>> validationErrors;

	protected AbstractValueMatrixValidatedComponent(@NonNull final IScenarioEditingLocation scenarioEditingLocation, final Map<Object, Collection<IStatus>> validationErrors, @NonNull final Supplier<SwapValueMatrixModel> modelProvider) {
		super(scenarioEditingLocation, modelProvider);
		this.validationErrors = validationErrors;
	}

}

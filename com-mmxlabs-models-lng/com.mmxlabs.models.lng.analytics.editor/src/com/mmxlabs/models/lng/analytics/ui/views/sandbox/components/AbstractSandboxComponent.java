/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.sandbox.components;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.AbstractAnalysisModel;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;

public abstract class AbstractSandboxComponent<T, U extends AbstractAnalysisModel> extends AbstractModellerComponent<T, U> {

	protected AbstractSandboxComponent(final @NonNull IScenarioEditingLocation scenarioEditingLocation, final Map<Object, IStatus> validationErrors, @NonNull Supplier<U> modelProvider) {
		super(scenarioEditingLocation, validationErrors, modelProvider);
	}

	@Override
	protected Set<Object> getTargetElementsForLabelProvider(final String name, final Object element) {
		final Set<Object> targetElements = new HashSet<>();
		targetElements.add(element);
		// FIXME: Hacky!
		if (element instanceof BaseCaseRow) {
			final BaseCaseRow baseCaseRow = (BaseCaseRow) element;
			if (name == null || "Buy".equals(name)) {
				targetElements.add(baseCaseRow.getBuyOption());
			}
			if (name == null || "Sell".equals(name)) {
				targetElements.add(baseCaseRow.getSellOption());
			}
			if (name == null || "Shipping".equals(name)) {
				targetElements.add(baseCaseRow.getShipping());
			}
		}
		if (element instanceof PartialCaseRow) {
			final PartialCaseRow row = (PartialCaseRow) element;
			if (name == null || "Buy".equals(name)) {
				targetElements.addAll(row.getBuyOptions());
			}
			if (name == null || "Sell".equals(name)) {
				targetElements.addAll(row.getSellOptions());
			}
			if (name == null || "Shipping".equals(name)) {
				targetElements.add(row.getShipping());
			}
		}
		targetElements.remove(null);
		return targetElements;
	}

	@Override
	protected Set<Object> getTargetElementsForWiringProvider(final Object element) {
		final Set<Object> targetElements = new HashSet<>();
		targetElements.add(element);
		// FIXME: Hacky!
		if (element instanceof BaseCaseRow) {
			final BaseCaseRow baseCaseRow = (BaseCaseRow) element;
			targetElements.add(baseCaseRow.getBuyOption());
			targetElements.add(baseCaseRow.getSellOption());
			targetElements.add(baseCaseRow.getShipping());
		}
		if (element instanceof PartialCaseRow) {
			final PartialCaseRow row = (PartialCaseRow) element;
			targetElements.addAll(row.getBuyOptions());
			targetElements.addAll(row.getSellOptions());
			targetElements.add(row.getShipping());
		}
		targetElements.remove(null);
		return targetElements;
	}
}

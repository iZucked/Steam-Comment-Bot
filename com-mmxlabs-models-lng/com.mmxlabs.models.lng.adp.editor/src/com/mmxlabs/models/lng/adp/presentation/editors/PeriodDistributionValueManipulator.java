/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.editors;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.PeriodDistribution;
import com.mmxlabs.models.lng.adp.PeriodDistributionProfileConstraint;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.tabular.manipulators.ValueListAttributeManipulator;

/**
 * @author hinton
 *
 */
public class PeriodDistributionValueManipulator extends ValueListAttributeManipulator<YearMonth> {
	private static @NonNull List<Pair<String, YearMonth>> defaultValues = getDefaultValues();

	public PeriodDistributionValueManipulator(final EAttribute feature, ICommandHandler commandHandler) {
		super(feature, commandHandler, defaultValues);
	}

	@Override
	protected void updateDisplay(Object input) {
		List<Pair<String, YearMonth>> values = getDefaultValues();

		if (input instanceof PeriodDistribution) {
			final PeriodDistribution distribution = (PeriodDistribution) input;
			if (distribution.eContainer() instanceof PeriodDistributionProfileConstraint) {
				// final PeriodDistributionModel periodDistributionModel = (PeriodDistributionModel) distribution.eContainer();
				//
				// final List<Pair<String, YearMonth>> result = new ArrayList<>();
				// switch (periodDistributionModel.getPeriodType()) {
				// case YEARLY: {
				// for (int i = 0; i < 4; ++i) {
				// YearMonth ym = YearMonth.now().withMonth(1).plusYears(i);
				// result.add(new Pair<>(String.format("%s", ym), ym));
				// }
				// break;
				// }
				// case MONTHLY: {
				// for (int i = 1; i <= 12; ++i) {
				// YearMonth ym = YearMonth.now().withMonth(i);
				// result.add(new Pair<>(String.format("%s", ym), ym));
				// }
				// break;
				// }
				// case QUARTERLY: {
				// for (int i = 0; i < 4; ++i) {
				// YearMonth ym = YearMonth.now().withMonth(1+ 4 * i);
				// result.add(new Pair<>(String.format("%s", ym), ym));
				// }
				// break;
				// }
				// }
				// values = result;
			}
		}
		names.clear();
		this.valueList.clear();
		for (final Pair<String, YearMonth> pair : values) {
			names.add(pair.getFirst());
			this.valueList.add(pair.getSecond());
		}
	}

	// @Override
	// public void display(final IDialogEditingContext dialogContext, final MMXRootObject rootObject, final EObject input, final Collection<EObject> range) {
	//
	// List<Pair<String, YearMonth>> values = getDefaultValues();
	//
	// updateCombo(values);
	//
	// super.display(dialogContext, rootObject, input, range);
	// }

	private static @NonNull List<Pair<String, YearMonth>> getDefaultValues() {
		final List<Pair<String, YearMonth>> result = new ArrayList<>();
		result.add(new Pair<>("Unknown", YearMonth.now()));
		return result;
	}
	//
	// @Override
	// protected boolean updateOnChangeToFeature(Object changedFeature) {
	// return changedFeature == ADPPackage.Literals.PERIOD_DISTRIBUTION_MODEL__PERIOD_TYPE || super.updateOnChangeToFeature(changedFeature);
	// }

}

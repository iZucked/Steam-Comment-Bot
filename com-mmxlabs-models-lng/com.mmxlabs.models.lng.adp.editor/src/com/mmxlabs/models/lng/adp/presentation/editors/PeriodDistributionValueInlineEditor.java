/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.editors;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.PeriodDistribution;
import com.mmxlabs.models.lng.adp.PeriodDistributionProfileConstraint;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.ValueListInlineEditor;

/**
 * @author hinton
 *
 */
public class PeriodDistributionValueInlineEditor extends ValueListInlineEditor<YearMonth> {
	private static @NonNull List<Pair<String, YearMonth>> defaultValues = getDefaultValues();

	public PeriodDistributionValueInlineEditor(final EAttribute feature) {
		super(feature, defaultValues);
	}

	@Override
	protected void updateDisplay(Object value) {
		List<Pair<String, YearMonth>> values = getDefaultValues();

		if (input instanceof PeriodDistribution) {
			final PeriodDistribution distribution = (PeriodDistribution) input;
			if (distribution.eContainer() instanceof PeriodDistributionProfileConstraint) {
				final PeriodDistributionProfileConstraint periodDistributionModel = (PeriodDistributionProfileConstraint) distribution.eContainer();

//				final List<Pair<String, YearMonth>> result = new ArrayList<>();
//				switch (periodDistributionModel.getPeriodType()) {
//				case YEARLY: {
//					for (int i = 0; i < 4; ++i) {
//						YearMonth ym = YearMonth.now().withMonth(1).plusYears(i);
//						result.add(new Pair<>(String.format("%s", ym), ym));
//					}
//					break;
//				}
//				case MONTHLY: {
//					for (int i = 0; i < 12; ++i) {
//						YearMonth ym = YearMonth.now().withMonth(i);
//						result.add(new Pair<>(String.format("%s", ym), ym));
//					}
//					break;
//				}
//				case QUARTERLY: {
//					for (int i = 0; i < 4; ++i) {
//						YearMonth ym = YearMonth.now().withMonth(4 * i);
//						result.add(new Pair<>(String.format("%s", ym), ym));
//					}
//					break;
//				}
//				}
//				values = result;
			}
		}
		updateCombo(values);

		super.updateDisplay(value);
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject rootObject, final EObject input, final Collection<EObject> range) {

		List<Pair<String, YearMonth>> values = getDefaultValues();

		updateCombo(values);

		super.display(dialogContext, rootObject, input, range);
	}

	private static @NonNull List<Pair<String, YearMonth>> getDefaultValues() {
		final List<Pair<String, YearMonth>> result = new ArrayList<>();
		result.add(new Pair<>("Unknown", YearMonth.now()));
		return result;
	}

	@Override
	protected boolean updateOnChangeToFeature(Object changedFeature) {
		return //changedFeature == ADPPackage.Literals.PERIOD_DISTRIBUTION_MODEL__PERIOD_TYPE ||
super.updateOnChangeToFeature(changedFeature);
	}

}

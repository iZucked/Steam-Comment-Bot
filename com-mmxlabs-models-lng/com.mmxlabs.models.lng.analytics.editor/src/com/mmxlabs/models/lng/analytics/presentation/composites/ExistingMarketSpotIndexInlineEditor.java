/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.presentation.composites;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.adp.ShippingOption;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.ValueListInlineEditor;

/**
 */
public class ExistingMarketSpotIndexInlineEditor extends ValueListInlineEditor {
	private static @NonNull List<Pair<String, Object>> defaultValues = getDefaultValues();

	public ExistingMarketSpotIndexInlineEditor(final EAttribute feature) {
		super(feature, defaultValues);
	}
	
	@Override
	protected void updateDisplay(Object value) {
		generateValuesAndUpdateCombo(input);
		super.updateDisplay(value);
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject rootObject, final EObject input, final Collection<EObject> range) {
		generateValuesAndUpdateCombo(input);
		super.display(dialogContext, rootObject, input, range);
	}
	
	private void generateValuesAndUpdateCombo(final EObject input) {
		List<Pair<String, Object>> values = getDefaultValues();

		if (input instanceof ExistingCharterMarketOption) {
			final ExistingCharterMarketOption option = (ExistingCharterMarketOption) input;
			final CharterInMarket charterInMarket = option.getCharterInMarket();
			values = getValues(charterInMarket, option.getSpotIndex());
		}
		updateCombo(values);
	}

	private static @NonNull List<Pair<String, Object>> getDefaultValues() {
		final ArrayList<Pair<String, Object>> result = new ArrayList<>();
		result.add(new Pair<String, Object>("Unknown", Integer.valueOf(0)));
		return result;
	}

	private static @NonNull List<Pair<String, Object>> getValues(final CharterInMarket market, final int currentIndex) {
		final ArrayList<Pair<String, Object>> result = new ArrayList<>();
		// Always show nominal if current index, even if not licensed otherwise we may get null pointer or array index exceptions.
		if (currentIndex == -1 || (LicenseFeatures.isPermitted("features:nominals") && market.isNominal())) {
			result.add(new Pair<String, Object>("Nominal", Integer.valueOf(-1)));
		}
		if (market.isEnabled()) {
			for (int i = 0; i < market.getSpotCharterCount(); ++i) {
				result.add(new Pair<String, Object>(String.format("%d", 1 + i), Integer.valueOf(i)));
			}
		}
		if (currentIndex != -1 && (!market.isEnabled() || currentIndex >= market.getSpotCharterCount())) {
			result.add(new Pair<String, Object>(String.format("%d", 1 + currentIndex), Integer.valueOf(currentIndex)));
		}
		return result;
	}
	
	@Override
	protected boolean updateOnChangeToFeature(Object changedFeature) {
		return changedFeature == AnalyticsPackage.Literals.EXISTING_CHARTER_MARKET_OPTION__CHARTER_IN_MARKET || super.updateOnChangeToFeature(changedFeature);
	}
}

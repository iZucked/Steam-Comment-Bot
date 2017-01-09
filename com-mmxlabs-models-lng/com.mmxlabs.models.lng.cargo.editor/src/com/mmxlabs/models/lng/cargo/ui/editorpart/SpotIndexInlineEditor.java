/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.ValueListInlineEditor;

/**
 */
public class SpotIndexInlineEditor extends ValueListInlineEditor {
	private static @NonNull List<Pair<String, Object>> defaultValues = getDefaultValues();

	public SpotIndexInlineEditor(final EAttribute feature) {
		super(feature, defaultValues);
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject rootObject, final EObject input, final Collection<EObject> range) {

		List<Pair<String, Object>> values = getDefaultValues();

		if (input instanceof AssignableElement) {
			final AssignableElement assignableElement = (AssignableElement) input;
			final VesselAssignmentType vesselAssignmentType = assignableElement.getVesselAssignmentType();
			if (vesselAssignmentType instanceof CharterInMarket) {
				final CharterInMarket charterInMarket = (CharterInMarket) vesselAssignmentType;
				values = getValues(charterInMarket, assignableElement.getSpotIndex());
			}
		}
		updateCombo(values);

		super.display(dialogContext, rootObject, input, range);
	}

	private static @NonNull List<Pair<String, Object>> getDefaultValues() {
		final ArrayList<Pair<String, Object>> result = new ArrayList<Pair<String, Object>>();
		result.add(new Pair<String, Object>("Unknown", Integer.valueOf(0)));
		return result;
	}

	private static @NonNull List<Pair<String, Object>> getValues(final CharterInMarket market, final int currentIndex) {
		final ArrayList<Pair<String, Object>> result = new ArrayList<Pair<String, Object>>();
		// Always show nominal if current index, even if not licensed otherwise we may get null pointer or array index exceptions.
		if (currentIndex == -1 || LicenseFeatures.isPermitted("features:nominals")) {
			result.add(new Pair<String, Object>("Nominal", Integer.valueOf(-1)));
		}
		for (int i = 0; i < market.getSpotCharterCount(); ++i) {
			result.add(new Pair<String, Object>(String.format("%d", 1 + i), Integer.valueOf(i)));
		}
		if (currentIndex >= market.getSpotCharterCount()) {
			result.add(new Pair<String, Object>(String.format("%d", 1 + currentIndex), Integer.valueOf(currentIndex)));
		}
		return result;
	}

}

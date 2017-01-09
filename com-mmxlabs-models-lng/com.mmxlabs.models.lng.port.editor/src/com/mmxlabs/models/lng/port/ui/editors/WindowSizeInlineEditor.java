/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.editors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.ValueListInlineEditor;

/**
 */
public class WindowSizeInlineEditor extends ValueListInlineEditor {
	private static @NonNull List<Pair<String, Object>> defaultValues = getDefaultValues(TimePeriod.HOURS, null);

	private final EAttribute sizeAttribute;
	private final EAttribute unitAttribute;

	public WindowSizeInlineEditor(final EAttribute sizeAttribute, final EAttribute unitAttribute) {
		super(sizeAttribute, defaultValues);
		this.unitAttribute = unitAttribute;
		this.sizeAttribute = sizeAttribute;
	}

	@Override
	public void notifyChanged(final Notification notification) {
		super.notifyChanged(notification);
	}

	@Override
	protected void updateDisplay(final Object value) {

		TimePeriod p;
		Integer v;
		if (input instanceof MMXObject) {
			final MMXObject mmxObject = (MMXObject) input;
			p = (TimePeriod) mmxObject.eGetWithDefault(unitAttribute);
			v = (Integer) mmxObject.eGetWithDefault(sizeAttribute);
		} else {
			p = (TimePeriod) input.eGet(unitAttribute);
			v = (Integer) input.eGet(sizeAttribute);
		}

		final List<Pair<String, Object>> values = getDefaultValues(p, v);

		updateCombo(values);

		super.updateDisplay(value);
	}

	private static @NonNull List<Pair<String, Object>> getDefaultValues(final int start, final int count, final Integer current, BiFunction<Integer, String, String> mapping) {

		if (mapping == null) {
			mapping = (i, s) -> s;
		}

		final ArrayList<Pair<String, Object>> result = new ArrayList<Pair<String, Object>>();
		for (int i = start; i < start + count; ++i) {
			result.add(new Pair<String, Object>(mapping.apply(i, String.format("%d", i)), Integer.valueOf(i)));
		}
		if (current != null) {
			final int iCurrent = current;
			if (iCurrent >= count) {
				result.add(new Pair<String, Object>(mapping.apply(iCurrent, String.format("%d", iCurrent)), current));
			}
		}
		return result;
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject rootObject, final EObject input, final Collection<EObject> range) {
		TimePeriod p;
		Integer v;
		if (input instanceof MMXObject) {
			final MMXObject mmxObject = (MMXObject) input;
			p = (TimePeriod) mmxObject.eGetWithDefault(unitAttribute);
			v = (Integer) mmxObject.eGetWithDefault(sizeAttribute);
		} else {
			p = (TimePeriod) input.eGet(unitAttribute);
			v = (Integer) input.eGet(sizeAttribute);
		}

		final List<Pair<String, Object>> values = getDefaultValues(p, v);

		updateCombo(values);

		super.display(dialogContext, rootObject, input, range);
	}

	private static @NonNull List<Pair<String, Object>> getDefaultValues(final TimePeriod timePeriod, final Integer currentValue) {
		switch (timePeriod) {
		case DAYS:
			return getDefaultValues(1, 31, currentValue, null);
		case HOURS:
			@NonNull
			final List<Pair<String, Object>> l = getDefaultValues(0, 24, currentValue, (i, s) -> i == 0 ? "Exact" : String.format("%d", i + 1));
			// Add in day and a half special case
			l.add(new Pair<>("36", 35));
			return l;
		case MONTHS:
			return getDefaultValues(1, 3, currentValue, null);
		}
		throw new IllegalStateException();
	}

	/**
	 * Subclasses can override this to trigger a redisplay when other fields change
	 * 
	 * @param changedFeature
	 * @return
	 */
	@Override
	protected boolean updateOnChangeToFeature(final Object changedFeature) {
		final boolean b = changedFeature.equals(unitAttribute) || super.updateOnChangeToFeature(changedFeature);
		return b;
	}
}

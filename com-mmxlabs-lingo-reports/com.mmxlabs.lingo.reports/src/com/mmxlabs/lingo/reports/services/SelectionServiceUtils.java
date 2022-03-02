/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.PropertySheet;

import com.mmxlabs.rcp.common.SelectionHelper;

public class SelectionServiceUtils {
	public static boolean isSelectionValid(final MPart part, final Object selectedObject) {
		// Check that selection is not null
		if (selectedObject == null) {
			return false;
		}
		// Check that selection is not text selection, like selecting the text in the log editor
		final String selectionClassName = selectedObject.getClass().getCanonicalName();
		if ("org.eclipse.jface.text.TextSelection".equals(selectionClassName)) {
			return false;
		}
		// Check that the selection is not coming from the log viewer or from properties
		final IWorkbenchPart e3Part = SelectionHelper.getE3Part(part);
		if (e3Part != null) {

			if (e3Part instanceof PropertySheet) {
				return false;
			}
			String g = e3Part.getClass().getCanonicalName();
			if ("org.eclipse.ui.internal.views.log.LogView".equals(g)) {
				return false;
			}
		}
		return true;
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.ui.dates;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.nebula.widgets.formattedtext.DateFormatter;

import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;

public class DateAndLocalTimeInlineEditorFactory implements
		IInlineEditorFactory {
	@Override
	public IInlineEditor createEditor(final EClass owner, final EStructuralFeature feature) {
		final EAnnotation annotation = feature.getEAnnotation("http://www.mmxlabs.com/models/lng/ui/datetime");
		boolean showTime = true;
		if (annotation != null) {
			final String showTimeAnnotation = annotation.getDetails().get("showTime");
			if (showTimeAnnotation != null) {
				showTime = Boolean.parseBoolean(showTimeAnnotation);
			}
		}
		
		if (showTime) {
			return new DateInlineEditor(feature); 
		} else {
			return new DateInlineEditor(feature, new DateFormatter());
		}
	}
}

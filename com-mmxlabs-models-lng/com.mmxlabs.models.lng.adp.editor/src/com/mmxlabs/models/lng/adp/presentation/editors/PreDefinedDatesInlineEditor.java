/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.editors;

import java.util.Collection;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.window.Window;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.DialogInlineEditor;

public class PreDefinedDatesInlineEditor extends DialogInlineEditor {
	private MMXRootObject rootObject;

	public PreDefinedDatesInlineEditor(final EStructuralFeature feature) {
		super(feature);
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, MMXRootObject context, EObject input, Collection<EObject> range) {
		this.rootObject = context;
		super.display(dialogContext, context, input, range);
	}

	@Override
	protected Object displayDialog(final Object currentValue) {
		final PreDefinedDatesInlineEditorDialog ccd = new PreDefinedDatesInlineEditorDialog(getShell());
		if (ccd.open(new AdapterFactoryImpl(), commandHandler.getModelReference(), rootObject, input, (EReference) feature) == Window.OK) {
			return ccd.getResult();
		}
		return null;
	}

	@Override
	protected String render(Object value) {
		if (value instanceof Collection<?>) {
			Collection<?> collection = (Collection<?>) value;
			return String.format("%d dates", collection.size());
		}
		return "";
	}

	@Override
	protected Object getInitialUnsetValue() {
		return null;
	}
}

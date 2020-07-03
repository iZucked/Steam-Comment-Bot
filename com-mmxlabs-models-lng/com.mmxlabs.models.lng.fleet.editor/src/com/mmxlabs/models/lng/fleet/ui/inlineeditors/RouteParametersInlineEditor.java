/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.inlineeditors;

import java.util.Collection;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.DialogInlineEditor;

public class RouteParametersInlineEditor extends DialogInlineEditor {
	private MMXRootObject rootObject;

	public RouteParametersInlineEditor(final EStructuralFeature feature) {
		super(feature);
	}

	@Override
	public Control createControl(Composite parent, EMFDataBindingContext dbc, FormToolkit toolkit) {
		isOverridable = false;
		EAnnotation eAnnotation = feature.getEContainingClass().getEAnnotation("http://www.mmxlabs.com/models/featureOverride");
		if (eAnnotation == null) {
			eAnnotation = feature.getEContainingClass().getEAnnotation("http://www.mmxlabs.com/models/featureOverrideByContainer");
		}
		if (eAnnotation != null) {
			for (EStructuralFeature f : feature.getEContainingClass().getEAllAttributes()) {
				if (f.getName().equals(feature.getName() + "Override")) {
					isOverridable = true;
					this.overrideToggleFeature = f;
				}
			}
			if (feature.isUnsettable()) {
				isOverridable = true;
			}
		}
		if (isOverridable) {
			isOverridableWithButton = true;
		}
		return super.createControl(parent, dbc, toolkit);
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, MMXRootObject context, EObject input, Collection<EObject> range) {
		this.rootObject = context;
		super.display(dialogContext, context, input, range);
	}

	@Override
	protected Object displayDialog(final Object currentValue) {
		final CanalParametersDialog ccd = new CanalParametersDialog(getShell());
		if (ccd.open(new AdapterFactoryImpl(), commandHandler.getModelReference(), rootObject, input, (EReference) feature) == Window.OK) {
			return ccd.getResult();
		}
		return null;
	}

	@Override
	protected String render(Object value) {
		return "";
	}

	@Override
	protected Object getInitialUnsetValue() {
		return null;
	}
}

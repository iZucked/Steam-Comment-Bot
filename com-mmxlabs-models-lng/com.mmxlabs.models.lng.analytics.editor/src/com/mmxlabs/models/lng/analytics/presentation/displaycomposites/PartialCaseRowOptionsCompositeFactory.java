/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.presentation.displaycomposites;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeFactory;

/**
 * Factory for {@link PartialCaseRowOptionsTopLevelComposite} application.
 * 
 * @author hinton
 * 
 */
public class PartialCaseRowOptionsCompositeFactory extends DefaultDisplayCompositeFactory {
	public PartialCaseRowOptionsCompositeFactory() {

	}

	@Override
	public IDisplayComposite createToplevelComposite(final Composite composite, final EClass eClass, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		return new PartialCaseRowOptionsTopLevelComposite(composite, SWT.NONE, dialogContext, toolkit);
	}

	@Override
	public IDisplayComposite createSublevelComposite(final Composite composite, final EClass eClass, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		// This is not expected to be called. The CargoTopLevelComposite will create it's own instances directly.
		throw new UnsupportedOperationException("Unexpected method invocations");
	}

	@Override
	public List<EObject> getExternalEditingRange(final MMXRootObject root, final EObject value) {
		final List<EObject> external = super.getExternalEditingRange(root, value);

		return external;
	}
}

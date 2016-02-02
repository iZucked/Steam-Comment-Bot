/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.displaycomposites;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeFactory;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;

public class PanamaCanalTariffDisplayCompositeFactory implements IDisplayCompositeFactory {
	final IDisplayCompositeFactory defaultFactory = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(EcorePackage.eINSTANCE.getEObject());

	@Override
	public IDisplayComposite createToplevelComposite(final Composite composite, final EClass eClass, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		return defaultFactory.createToplevelComposite(composite, eClass, dialogContext, toolkit);
	}

	@Override
	public IDisplayComposite createSublevelComposite(final Composite composite, final EClass eClass, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		return new PanamaCanalTariffDetailComposite(composite, SWT.NONE, toolkit);
	}

	@Override
	public List<EObject> getExternalEditingRange(final MMXRootObject root, final EObject value) {
		return defaultFactory.getExternalEditingRange(root, value);
	}
}

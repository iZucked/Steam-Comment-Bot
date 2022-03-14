/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.composites;

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

public class SpacingProfileDetailCompositeFactory implements IDisplayCompositeFactory {
	final IDisplayCompositeFactory defaultFactory = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(EcorePackage.eINSTANCE.getEObject());

	@Override
	public IDisplayComposite createToplevelComposite(Composite composite, EClass eClass, IDialogEditingContext context, FormToolkit toolkit) {
		return defaultFactory.createToplevelComposite(composite, eClass, context, toolkit);
	}

	@Override
	public IDisplayComposite createSublevelComposite(Composite composite, EClass eClass, IDialogEditingContext context, FormToolkit toolkit) {
		return new SpacingProfileDetailComposite(composite, context, SWT.NONE, toolkit);
	}

	@Override
	public List<EObject> getExternalEditingRange(MMXRootObject root, EObject value) {
		return defaultFactory.getExternalEditingRange(root, value);
	}
	
}
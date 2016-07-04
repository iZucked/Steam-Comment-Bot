/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeFactory;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;

public class DefaultDisplayCompositeFactory implements IDisplayCompositeFactory {

	/**
	 */
	@Override
	public IDisplayComposite createToplevelComposite(final Composite parent, final EClass eClass, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		return new DefaultTopLevelComposite(parent, SWT.NONE, dialogContext, toolkit);
	}

	/**
	 */
	@Override
	public IDisplayComposite createSublevelComposite(final Composite parent, final EClass eClass, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		return new DefaultDetailComposite(parent, SWT.NONE, toolkit);
	}

	@Override
	public List<EObject> getExternalEditingRange(final MMXRootObject root, final EObject value) {
		final ArrayList<EObject> external = new ArrayList<EObject>();

		for (final IComponentHelper helper : Activator.getDefault().getComponentHelperRegistry().getComponentHelpers(value.eClass())) {
			external.addAll(helper.getExternalEditingRange(root, value));
		}

		// default is to add sublevel composites as well
		for (final EReference ref : value.eClass().getEAllContainments()) {
			if (ref.isMany() == false) {
				final EObject object = (EObject) value.eGet(ref);
				if (object != null) {
					for (final IComponentHelper helper : Activator.getDefault().getComponentHelperRegistry().getComponentHelpers(object.eClass())) {
						external.addAll(helper.getExternalEditingRange(root, object));
					}
				}
			}
		}

		return external;
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
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

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeFactory;

public class DefaultDisplayCompositeFactory implements IDisplayCompositeFactory {
	@Override
	public IDisplayComposite createToplevelComposite(Composite parent, EClass eClass) {
		return new DefaultTopLevelComposite(parent, SWT.NONE);
	}

	@Override
	public IDisplayComposite createSublevelComposite(Composite parent, EClass eClass) {
		return new DefaultDetailComposite(parent, SWT.NONE);
	}

	@Override
	public List<EObject> getExternalEditingRange(final MMXRootObject root,
			final EObject value) {
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

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.ui.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

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
}

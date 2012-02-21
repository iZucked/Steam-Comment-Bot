package com.mmxlabs.models.ui.editors;

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */


import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * This is an interface for an inline editor. It is given an input, and should
 * generate commands and send them to the appropriate command processor when
 * stuff happens. It doesn't handle layout or provision of labels or anything.
 * 
 * @author Tom Hinton
 * 
 */
public interface IInlineEditor {
	public void setCommandHandler(final ICommandHandler handler);
	public void display(final MMXRootObject scenario, final EObject object);
	public Control createControl(final Composite parent);
	void processValidation(IStatus status);	
	public EStructuralFeature getFeature();
	void setLabel(Label label);
}
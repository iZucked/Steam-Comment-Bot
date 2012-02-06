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

/**
 * This is an interface for an inline editor. It is given an input, and should
 * generate commands and send them to the appropriate command processor when
 * stuff happens.
 * 
 * @author Tom Hinton
 * 
 */
public interface IInlineEditor {
	public void setInput(final EObject object);
	public Control createControl(final Composite parent);
	void processValidation(IStatus status);	
	public EStructuralFeature getFeature();
}
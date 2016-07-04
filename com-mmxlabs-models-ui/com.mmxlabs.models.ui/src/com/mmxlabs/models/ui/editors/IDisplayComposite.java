/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors;

import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;

/**
 * This is an interface for things which can render EObjects from {@link MMXRootObject}s (scenarios, roughly speaking) to the display.
 * 
 * Most implementations will just be {@link Composite}s, and {@link #getComposite()} will return this.
 * 
 * @author hinton
 * 
 */
public interface IDisplayComposite {

	/**
	 * Key used to associate a label with it's control for tool tips
	 */
	public static String LABEL_CONTROL_KEY = "label-control";

	/**
	 * Get the SWT {@link Composite} which will be used to display values
	 * 
	 * @return
	 */
	public Composite getComposite();

	/**
	 * Display the given value, in the context of the given root object (scenario)
	 * 
	 * @param root
	 * @param value
	 */
	public void display(IDialogEditingContext dialogContext, MMXRootObject root, EObject value, Collection<EObject> range, EMFDataBindingContext dbc);

	/**
	 * Sets the command handler for any stuff that goes on in this composite.
	 * 
	 * @param commandHandler
	 */
	public void setCommandHandler(ICommandHandler commandHandler);

	/**
	 * Wrap controls from the next display() with this wrapper
	 * 
	 * @param wrapper
	 */
	public void setEditorWrapper(IInlineEditorWrapper wrapper);

	/**
	 * Update contents with validation data.
	 * 
	 * @param status
	 */
	public void displayValidationStatus(IStatus status);

	/**
	 * Checks the current editor visiblilty and updates the UI state. Returns true if UI state is changed.
	 * @param context
	 * @return
	 */
	boolean checkVisibility(IDialogEditingContext context);
}

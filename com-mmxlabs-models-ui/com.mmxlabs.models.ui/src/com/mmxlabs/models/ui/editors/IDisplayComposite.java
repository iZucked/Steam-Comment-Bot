/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.mmxcore.MMXRootObject;
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
	 * Get the SWT {@link Composite} which will be used to display values
	 * @return
	 */
	public Composite getComposite();
	/**
	 * Display the given value, in the context of the given root object (scenario)
	 * @param root
	 * @param value
	 */
	public void display(final MMXRootObject root, final EObject value);
	/**
	 * Sets the command handler for any stuff that goes on in this composite.
	 * @param commandHandler
	 */
	public void setCommandHandler(ICommandHandler commandHandler);
	
	/**
	 * Wrap controls from the next display() with this wrapper
	 * @param wrapper
	 */
	public void setEditorWrapper(final IInlineEditorWrapper wrapper);
	
	/**
	 * Should return a collection of the branches that this object might edit in its display.
	 * 
	 * This is useful for the editor dialogs, which need to take copies of any objects that might be edited
	 * before they get edited, without taking a copy of all the scenario just in case.
	 * 
	 * Please don't return a list whose members contain one another, as this won't work properly.
	 * 
	 * @param root
	 * @param value
	 * @return a list of objects which might be modified when this display composite is displaying value
	 */
	public List<EObject> getEditingRange(final MMXRootObject root, final EObject value);
	
	/**
	 * Update contents with validation data.
	 * 
	 * @param status
	 */
	public void displayValidationStatus(final IStatus status);
}

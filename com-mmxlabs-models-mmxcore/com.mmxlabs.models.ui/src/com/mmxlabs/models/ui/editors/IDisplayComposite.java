package com.mmxlabs.models.ui.editors;

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
}

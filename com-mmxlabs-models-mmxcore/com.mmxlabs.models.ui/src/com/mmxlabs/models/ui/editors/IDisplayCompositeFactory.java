/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.widgets.Composite;

/**
 * Interface for factories which will create composites for displaying model objects.
 * 
 * @author hinton
 *
 */
public interface IDisplayCompositeFactory {
	/**
	 * This is for creating a composite that can be displayed in a properties view or editor dialog
	 * @param eClass
	 * @return
	 */
	public IDisplayComposite createToplevelComposite(final Composite composite, final EClass eClass);
	/**
	 * This is for creating a composite which can be displayed within a top level composite somewhere;
	 * it should just handle the direct fields on the eClass, not any contained classes.
	 * 
	 * @param eClass
	 * @return
	 */
	public IDisplayComposite createSublevelComposite(final Composite composite, final EClass eClass);
}

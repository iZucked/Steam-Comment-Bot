/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.ui;

import org.eclipse.emf.ecore.EClass;

/**
 * Instances of this interface are used to populate AbstractDetailComposite
 * 
 * @author hinton
 *
 */
public interface IComponentHelper {
	/**
	 * The default method
	 */
	public void addEditorsToComposite(IInlineEditorContainer detailComposite);
	
	/**
	 * Called when a composite is being filled with editors for a particular type.
	 */
	public void addEditorsToComposite(IInlineEditorContainer detailComposite, final EClass displayedClass);
}

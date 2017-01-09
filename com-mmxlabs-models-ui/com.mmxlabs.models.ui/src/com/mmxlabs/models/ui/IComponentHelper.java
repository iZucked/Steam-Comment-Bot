/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.mmxcore.MMXRootObject;

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
	
	/**
	 * Get the external editing range for things produced by this CH.
	 * These should be any objects in fields where those objects need to be cloned
	 * when the original object is cloned, because they can be edited along with the 
	 * object.
	 * 
	 * @param root
	 * @param value
	 * @return
	 */
	public List<EObject> getExternalEditingRange(final MMXRootObject root, final EObject value);
	
	public int getDisplayPriority();
}

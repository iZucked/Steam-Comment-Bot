/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;

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
	void addEditorsToComposite(IInlineEditorContainer detailComposite);

	/**
	 * Called when a composite is being filled with editors for a particular type.
	 */
	void addEditorsToComposite(IInlineEditorContainer detailComposite, final EClass displayedClass);

	/**
	 * Get the external editing range for things produced by this CH. These should
	 * be any objects in fields where those objects need to be cloned when the
	 * original object is cloned, because they can be edited along with the object.
	 * 
	 * @param root
	 * @param value
	 * @return
	 */
	default List<EObject> getExternalEditingRange(final MMXRootObject root, final EObject value) {
		return Collections.emptyList();
	}

	default int getDisplayPriority() {
		return 0;
	}

	/**
	 * Create a row layout provider to override the default in
	 * {@link DefaultDetailComposite}. This is to avoid needing to create a
	 * specific subclass just for this purpose
	 * 
	 * @return
	 */
	default @Nullable IDisplayCompositeLayoutProvider createLayoutProvider() {
		return null;
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.registries;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.ui.editors.IInlineEditorFactory;

public interface IEditorFactoryRegistry {

	/**
	 * Search for an editor factory in the extensions list
	 * 
	 * @param owner
	 * @param feature
	 * @return
	 */
	public abstract IInlineEditorFactory getEditorFactory(final EClass owner,
			final EStructuralFeature feature);

}
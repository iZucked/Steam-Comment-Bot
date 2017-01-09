/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;

/**
 * Helper methods for {@link IComponentHelper} instances, mainly to add editors to {@link IInlineEditorContainer} instances.
 * 
 * @author hinton
 *
 */
public class ComponentHelperUtils {
	private static final Logger log = LoggerFactory.getLogger(ComponentHelperUtils.class);
	/**
	 * Add an editor to the given detail composite
	 * 
	 * @param detailComposite
	 * @param feature
	 */
	public static IInlineEditor createDefaultEditor(final EClass owner, final EStructuralFeature feature) {
		final IInlineEditorFactory factory = Activator.getDefault().getEditorFactoryRegistry().getEditorFactory(owner, feature);
		if (factory == null) {
			log.warn("Unable to find an editor factory for " + owner.getName() + "." + feature.getName());
			return null;
		} else {
			return factory.createEditor(owner, feature);
		}
	}
}

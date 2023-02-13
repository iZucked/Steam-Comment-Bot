/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ETypedElement;
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
	private static final Logger LOG = LoggerFactory.getLogger(ComponentHelperUtils.class);
	/**
	 * Add an editor to the given detail composite
	 * 
	 * @param detailComposite
	 * @param typedElement
	 */
	public static IInlineEditor createDefaultEditor(final EClass owner, final ETypedElement typedElement) {
		final IInlineEditorFactory factory = Activator.getDefault().getEditorFactoryRegistry().getEditorFactory(owner, typedElement);
		if (factory == null) {
			LOG.warn("Unable to find an editor factory for " + owner.getName() + "." + typedElement.getName());
			return null;
		} else {
			return factory.createEditor(owner, typedElement);
		}
	}
}

package com.mmxlabs.models.ui;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;

/**
 * Helper methods for {@link IComponentHelper} instances, mainly to add editors to {@link IInlineEditorContainer} instances.
 * 
 * @author hinton
 *
 */
public class ComponentHelperUtils {
	/**
	 * Add an editor to the given detail composite
	 * 
	 * @param detailComposite
	 * @param feature
	 */
	public static IInlineEditor createDefaultEditor(final EClass owner, final EStructuralFeature feature) {
		return Activator.getDefault().getEditorFactoryRegistry().getEditorFactory(owner, feature).createEditor(owner, feature);
	}
}

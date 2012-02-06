package com.mmxlabs.models.ui;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.ui.editors.IInlineEditor;

/**
 * 
 * @author hinton
 *
 */
public interface IDetailComposite {
	/**
	 * Add the given inline editor to this composite.
	 * 
	 * Typically you will get one of these from {@link ComponentHelperUtils#createDefaultEditor(org.eclipse.emf.ecore.EStructuralFeature)}
	 * 
	 * @param editor
	 */
	public void addInlineEditor(final IInlineEditor editor);
	
	public void addInlineEditor(final IInlineEditor editor, final EClass topClass);
}

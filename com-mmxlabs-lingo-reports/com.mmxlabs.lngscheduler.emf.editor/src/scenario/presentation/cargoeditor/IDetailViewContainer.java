/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EStructuralFeature;

import scenario.presentation.cargoeditor.EObjectDetailView.IInlineEditorFactory;

/**
 * @author Tom Hinton
 *
 */
public interface IDetailViewContainer {

	public abstract void addDefaultEditorFactories();

	public abstract void setNameForFeature(EStructuralFeature feature,
			String string);

	public abstract void setEditorFactoryForClassifier(EClassifier classifier,
			IInlineEditorFactory factory);

	public abstract void setEditorFactoryForFeature(EStructuralFeature feature,
			IInlineEditorFactory iInlineEditorFactory);

}
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.dialogs;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Default implementation of {@link IDialogController} which does nothing at all.
 * 
 * @author Simon Goodall
 * 
 */
public class NullDialogController implements IDialogController {

	@Override
	public void validate() {

	}

	@Override
	public void relayout() {

	}

	@Override
	public void setEditorVisibility(EObject target, EStructuralFeature feature, boolean visible) {

	}

	@Override
	public boolean getEditorVisibility(EObject target, EStructuralFeature feature) {
		return true;
	}

	@Override
	public void updateEditorVisibility() {

	}
}

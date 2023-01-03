/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.dialogs;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.ETypedElement;

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
	public void rebuild(boolean pack) {

	}

	@Override
	public void setEditorVisibility(EObject target, final ETypedElement typedElement, boolean visible) {

	}

	@Override
	public boolean getEditorVisibility(EObject target, final ETypedElement typedElement) {
		return true;
	}

	@Override
	public void updateEditorVisibility() {

	}

	@Override
	public void relayout() {

	}
}

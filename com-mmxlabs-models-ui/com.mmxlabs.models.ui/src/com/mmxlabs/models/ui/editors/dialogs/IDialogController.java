/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.dialogs;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.ui.editors.ICommandHandler;

public interface IDialogController {

	/**
	 * Request validation to be performed. Normally the dialog {@link ICommandHandler} would trigger this automatically on data changes. This method exists for triggering validation from code which is
	 * unable to use the dialog command handler.
	 */
	void validate();

	/**
	 * Request the editor be redrawn from the top-down disposing and recreating child composites.
	 */
	void relayout();

	/**
	 * Change the visibility of an object feature as registered in the framework (features are not necessarily linked directly the the target object). See #updateVisibility() to request UI state change.
	 */
	void setEditorVisibility(EObject object, EStructuralFeature feature, boolean visible);

	/**
	 * Returns the visibility of an object feature. Default is true.
	 */
	boolean getEditorVisibility(EObject object, EStructuralFeature feature);

	/**
	 * Request a top-down check of object feature visibility state changes and update UI accordingly.
	 */
	void updateEditorVisibility();
}

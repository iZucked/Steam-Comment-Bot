/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.dialogs;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;

public interface IDialogEditingContext {
	IDialogController getDialogController();

	IScenarioEditingLocation getScenarioEditingLocation();

	boolean isMultiEdit();

	/**
	 * Returns all the controls of interest for the given object feature.
	 */
	List<Control> getEditorControls(EObject target, EStructuralFeature feature);

	/**
	 * Register the controls of interest for a given object feature. This is used in combination with IDialogController get/setEditorVisibility.
	 */
	void registerEditorControl(EObject target, EStructuralFeature feature, Control control);
}

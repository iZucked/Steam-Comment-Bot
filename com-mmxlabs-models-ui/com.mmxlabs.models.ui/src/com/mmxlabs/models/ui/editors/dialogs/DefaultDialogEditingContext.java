/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.dialogs;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.common.PairKeyedMap;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;

public class DefaultDialogEditingContext implements IDialogEditingContext {
	private final IDialogController dialogController;
	private final IScenarioEditingLocation scenarioEditingLocation;
	private final boolean multiEdit;
	private final boolean newEdit;

	private PairKeyedMap<EObject, EStructuralFeature, List<Control>> objectControls = new PairKeyedMap<>();

	/**
	 * Creates an object encapsulating relevant information about the editing context.
	 * 
	 * 
	 * @param dc
	 * @param sel
	 * @param multiEdit
	 * @param newEdit
	 */
	public DefaultDialogEditingContext(@NonNull final IDialogController dc, @NonNull final IScenarioEditingLocation sel, final boolean multiEdit, final boolean newEdit) {
		dialogController = dc;
		scenarioEditingLocation = sel;
		this.multiEdit = multiEdit;
		this.newEdit = newEdit;
	}

	@Override
	public IDialogController getDialogController() {
		return dialogController;
	}

	@Override
	public IScenarioEditingLocation getScenarioEditingLocation() {
		return scenarioEditingLocation;
	}

	@Override
	public List<Control> getEditorControls(EObject target, EStructuralFeature feature) {

		if (objectControls.contains(target, feature)) {
			return objectControls.get(target, feature);
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public void registerEditorControl(EObject target, EStructuralFeature feature, Control control) {

		List<Control> controls;
		if (objectControls.contains(target, feature)) {
			controls = objectControls.get(target, feature);
		} else {
			controls = new LinkedList<>();
			objectControls.put(target, feature, controls);
		}
		controls.add(control);
	}

	@Override
	public boolean isMultiEdit() {
		return multiEdit;
	}
	
	@Override
	public boolean isNewEdit() {
		// TODO Auto-generated method stub
		return newEdit;
	}

}

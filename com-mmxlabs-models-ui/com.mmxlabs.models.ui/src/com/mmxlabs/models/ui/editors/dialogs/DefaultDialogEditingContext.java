/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.dialogs;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.common.PairKeyedMap;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;

public class DefaultDialogEditingContext implements IDialogEditingContext {
	private final IDialogController dialogController;
	private final IScenarioEditingLocation scenarioEditingLocation;
	private final boolean multiEdit;
	private final boolean newEdit;

	private PairKeyedMap<EObject, ETypedElement, List<Control>> objectControls = new PairKeyedMap<>();

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
	public List<Control> getEditorControls(EObject target, ETypedElement typedElement) {

		if (objectControls.contains(target, typedElement)) {
			return objectControls.get(target, typedElement);
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public void registerEditorControl(EObject target, ETypedElement typedElement, Control control) {

		List<Control> controls;
		if (objectControls.contains(target, typedElement)) {
			controls = objectControls.get(target, typedElement);
		} else {
			controls = new LinkedList<>();
			objectControls.put(target, typedElement, controls);
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

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editorpart;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;

public final class JointModelEditorPartSelectionChangedListener implements ISelectionChangedListener {
	/**
	 * 
	 */
	private final JointModelEditorPart jointModelEditorPart;

	/**
	 * @param jointModelEditorPart
	 */
	JointModelEditorPartSelectionChangedListener(final JointModelEditorPart jointModelEditorPart) {
		this.jointModelEditorPart = jointModelEditorPart;
	}

	// This just notifies those things that are affected by the
	// selection.
	//
	@Override
	public void selectionChanged(final SelectionChangedEvent selectionChangedEvent) {
		this.jointModelEditorPart.setSelection(selectionChangedEvent.getSelection());
	}
}
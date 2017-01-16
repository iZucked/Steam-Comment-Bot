/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.editor.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.ui.editorpart.ScenarioInstanceView;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class DistanceMatrixView extends ScenarioInstanceView {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.models.lng.port.editor.views.DistanceMatrixView";

	private Composite childComposite;

	
	/**
	 * The constructor.
	 */
	public DistanceMatrixView() {
		
	}
	
	
	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		this.childComposite = new Composite(parent, SWT.NONE);
		listenToScenarioSelection();
		
		// add toolbar buttons ? (I, E, +, -, name, sync?)
		
	}
	
	@Override
	protected void displayScenarioInstance(ScenarioInstance instance) {
		if (instance != getScenarioInstance()) {
			// update display
		}
	}
	

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		childComposite.setFocus();
	}
	
}

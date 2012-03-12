/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.IWorkbenchPage;

import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;

public class EntityEditorPane extends ScenarioTableViewerPane {
	public EntityEditorPane(IWorkbenchPage page, JointModelEditorPart part) {
		super(page, part);
	}

	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		addNameManipulator("Name");
		// TODO add shipping entity picker
		
		defaultSetTitle("Entities");
	}
	
	
}

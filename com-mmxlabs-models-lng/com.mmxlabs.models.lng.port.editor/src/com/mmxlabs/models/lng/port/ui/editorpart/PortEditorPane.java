/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.IWorkbenchPage;

import com.mmxlabs.models.lng.port.PortCapability;
import com.mmxlabs.models.lng.types.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;

public class PortEditorPane extends ScenarioTableViewerPane {

	private JointModelEditorPart jointModelEditorPart;

	public PortEditorPane(final IWorkbenchPage page, final JointModelEditorPart part) {
		super(page, part);
		this.jointModelEditorPart = part;
		
	}

	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		
		addNameManipulator("Name");
	
		for (final PortCapability capability : PortCapability.values()) {
			addTypicalColumn(capability.getName(), new CapabilityManipulator(capability, jointModelEditorPart.getEditingDomain()));
		}
		
		defaultSetTitle("Ports");
	}
}

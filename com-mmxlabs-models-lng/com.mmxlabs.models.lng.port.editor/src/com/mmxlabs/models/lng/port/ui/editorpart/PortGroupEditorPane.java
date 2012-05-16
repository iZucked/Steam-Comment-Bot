/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.BasicAttributeManipulator;

public class PortGroupEditorPane extends ScenarioTableViewerPane {
	private IScenarioEditingLocation jointModelEditor;

	public PortGroupEditorPane(IWorkbenchPage page, IWorkbenchPart part, IScenarioEditingLocation location) {
		super(page, part, location);
		this.jointModelEditor = location;
		
	}

	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		
		addTypicalColumn("Name", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), jointModelEditor.getEditingDomain()));
		setTitle("Port Groups", PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.IWorkbenchPage;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.types.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;
import com.mmxlabs.models.ui.tabular.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.SingleReferenceManipulator;

public class ContractEditorPane extends ScenarioTableViewerPane {
	public ContractEditorPane(IWorkbenchPage page, JointModelEditorPart part) {
		super(page, part);
	}

	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		addNameManipulator("Name");
		
		addTypicalColumn("Entity", new SingleReferenceManipulator(CommercialPackage.eINSTANCE.getContract_Entity(), getReferenceValueProviderCache(), getEditingDomain()));
		addTypicalColumn("Preferred Port", new SingleReferenceManipulator(CommercialPackage.eINSTANCE.getContract_PreferredPort(), getReferenceValueProviderCache(), getEditingDomain()));
		addTypicalColumn("Allowed Ports", new MultipleReferenceManipulator(CommercialPackage.eINSTANCE.getContract_AllowedPorts(), getReferenceValueProviderCache(), getEditingDomain(), MMXCorePackage.eINSTANCE.getNamedObject_Name()));
		
		defaultSetTitle("Contracts");
	}
}

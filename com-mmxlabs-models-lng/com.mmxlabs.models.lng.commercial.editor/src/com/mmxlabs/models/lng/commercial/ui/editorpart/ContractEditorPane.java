/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.manipulators.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;

public class ContractEditorPane extends ScenarioTableViewerPane {
	public ContractEditorPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final CommandStack commandStack) {
		super.init(path, adapterFactory, commandStack);
		addNameManipulator("Name");

		addTypicalColumn("Entity", new SingleReferenceManipulator(CommercialPackage.eINSTANCE.getContract_Entity(), getReferenceValueProviderCache(), getEditingDomain()));
		addTypicalColumn("Preferred Port", new SingleReferenceManipulator(CommercialPackage.eINSTANCE.getContract_PreferredPort(), getReferenceValueProviderCache(), getEditingDomain()));
		addTypicalColumn("Allowed Ports", new MultipleReferenceManipulator(CommercialPackage.eINSTANCE.getContract_AllowedPorts(), getReferenceValueProviderCache(), getEditingDomain(),
				MMXCorePackage.eINSTANCE.getNamedObject_Name()));

		defaultSetTitle("Contracts");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.lng.types.ui.tabular.ScenarioTableViewerPane#defaultSetTitle(java.lang.String)
	 */
	@Override
	public void defaultSetTitle(final String string) {
		super.defaultSetTitle(string);
	}

}

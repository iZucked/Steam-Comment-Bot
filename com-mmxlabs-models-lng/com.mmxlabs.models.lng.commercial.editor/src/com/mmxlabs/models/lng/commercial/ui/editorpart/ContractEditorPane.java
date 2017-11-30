/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.models.lng.cargo.ui.editorpart.VolumeAttributeManipulator;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.ui.manipulators.ContractTypeEnumAttributeManipulator;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class ContractEditorPane extends ScenarioTableViewerPane {
	public ContractEditorPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);
		addNameManipulator("Name");

		addTypicalColumn("Entity", new SingleReferenceManipulator(CommercialPackage.eINSTANCE.getContract_Entity(), getReferenceValueProviderCache(), getEditingDomain()));
		addTypicalColumn("Type", new ContractTypeEnumAttributeManipulator(CommercialPackage.eINSTANCE.getContract_ContractType(), getEditingDomain()));
		addTypicalColumn("Volume", new VolumeAttributeManipulator(CommercialPackage.eINSTANCE.getContract_MaxQuantity(), getEditingDomain()));

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

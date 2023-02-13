/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.models.lng.cargo.ui.editorpart.VolumeAttributeManipulator;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.ui.manipulators.ContractTypeEnumAttributeManipulator;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class ContractEditorPane extends ScenarioTableViewerPane {
	public ContractEditorPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);
		addNameManipulator("      Name      ");

		addTypicalColumn("Inco", new ContractTypeEnumAttributeManipulator(CommercialPackage.eINSTANCE.getContract_ContractType(), getCommandHandler()));
		addTypicalColumn("Volume", new VolumeAttributeManipulator(CommercialPackage.eINSTANCE.getContract_MaxQuantity(), getCommandHandler()));
		addTypicalColumn("        Price        ", new BasicAttributeManipulator(CommercialPackage.eINSTANCE.getExpressionPriceParameters_PriceExpression(), getCommandHandler()) {
			@Override
			public @Nullable String render(Object object) {
				if (!(object instanceof ExpressionPriceParameters)) {
					return "n/a";
				}
				return super.render(object);
			}
		}, CommercialPackage.eINSTANCE.getContract_PriceInfo());
		addTypicalColumn("Entity", new SingleReferenceManipulator(CommercialPackage.eINSTANCE.getContract_Entity(), getReferenceValueProviderCache(), getCommandHandler()));
		addTypicalColumn("Counterparty", new BasicAttributeManipulator(CommercialPackage.eINSTANCE.getContract_Counterparty(),  getCommandHandler()));

		defaultSetTitle("Contracts");
		
		final ToolBarManager toolbar = getToolBarManager();
		final ActionContributionItem filter = filterField.getContribution();
		if (toolbar != null && filter != null) {
			toolbar.remove(filter);
			toolbar.update(true);
		}
	}
	
	@Override
	protected Action createCopyToClipboardAction() {
		return null;
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

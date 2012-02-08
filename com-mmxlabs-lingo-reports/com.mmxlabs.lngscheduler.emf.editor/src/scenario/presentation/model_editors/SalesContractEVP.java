/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.presentation.model_editors;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.IWorkbenchPage;

import scenario.contract.ContractPackage;
import scenario.presentation.ScenarioEditor;

import com.mmxlabs.shiplingo.ui.tableview.NumericAttributeManipulator;
import com.mmxlabs.shiplingo.ui.tableview.SingleReferenceManipulator;

/**
 * @author Tom Hinton
 * 
 */
public class SalesContractEVP extends ContractEVP {
	public SalesContractEVP(final IWorkbenchPage page, final ScenarioEditor part) {
		super(page, part);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory) {
		// TODO Auto-generated method stub
		super.init(path, adapterFactory);
		addTypicalColumn("Index", new SingleReferenceManipulator(ContractPackage.eINSTANCE.getSalesContract_Index(), part.getIndexProvider(), part.getEditingDomain()));

		// salesPane.addTypicalColumn(
		// "Regas Efficiency",
		// new NumericAttributeManipulator(ContractPackage.eINSTANCE
		// .getSalesContract_RegasEfficiency(),
		// getEditingDomain()));

		addTypicalColumn("Sales Mark-up", new NumericAttributeManipulator(ContractPackage.eINSTANCE.getSalesContract_Markup(), part.getEditingDomain()));

	}

}

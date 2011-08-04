/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.model_editors;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.IWorkbenchPage;

import scenario.contract.ContractPackage;
import scenario.presentation.ScenarioEditor;

import com.mmxlabs.shiplingo.ui.tableview.SingleReferenceManipulator;

/**
 * Base for contract editor viewer panes
 * @author Tom Hinton
 *
 */
public abstract class ContractEVP extends NamedObjectEVP {
	public ContractEVP(IWorkbenchPage page, ScenarioEditor part) {
		super(page, part);
	}

	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		addTypicalColumn(
				"Entity",
				new SingleReferenceManipulator(ContractPackage.eINSTANCE
						.getContract_Entity(), part.getEntityProvider(),
						part.getEditingDomain()));
	}
}

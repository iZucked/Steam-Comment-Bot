/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.presentation.model_editors;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.IWorkbenchPage;

import scenario.port.PortPackage;
import scenario.presentation.ScenarioEditor;

import com.mmxlabs.shiplingo.ui.tableview.MultipleReferenceManipulator;

/**
 * EVP for port groups
 * 
 * doesn't currently show anything hierarchical.
 * 
 * @author hinton
 * 
 */
public class PortGroupEVP extends NamedObjectEVP {
	public PortGroupEVP(final IWorkbenchPage page, final ScenarioEditor part) {
		super(page, part);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);

		addTypicalColumn("Contents", new MultipleReferenceManipulator(PortPackage.eINSTANCE.getPortGroup_Contents(), part.getEditingDomain(), part.getPortSelectionProvider(), namedObjectName));
	}
}

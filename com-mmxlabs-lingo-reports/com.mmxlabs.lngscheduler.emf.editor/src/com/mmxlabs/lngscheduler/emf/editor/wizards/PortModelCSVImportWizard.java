/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.editor.wizards;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import scenario.Scenario;
import scenario.port.PortPackage;

/**
 * A reduced import wizard which imports "static" data.
 * 
 * @author Tom Hinton
 * 
 */
public class PortModelCSVImportWizard extends CSVImportWizard {
	@Override
	protected void postProcess(Scenario scenario2) {

	}

	@Override
	protected void createScenarioModels(Scenario scenario) {
		scenario.createMissingModels();
		scenario.setContractModel(null);
		scenario.setFleetModel(null);
		scenario.setMarketModel(null);
		scenario.setOptimisation(null);
		scenario.setScheduleModel(null);
		scenario.setCargoModel(null);
	}

	@Override
	protected void createWizardPageControls(CSVWizardPage page, Composite topLevel) {
		final String[] extensions = new String[] { "*.csv" };

		{
			final Group group = page.createGroup(topLevel, "Ports and Distances");

			page.makeEditor(group, "Ports", PortPackage.eINSTANCE.getPort(), extensions);
			page.makeEditor(group, "Distance Matrix", PortPackage.eINSTANCE.getDistanceModel(), extensions);
			page.makeEditor(group, "Canals", PortPackage.eINSTANCE.getCanal(), extensions);
		}
		// {
		// final Group group = page.createGroup(topLevel,
		// "Indices and Contracts");
		//
		// page.makeEditor(group, "Indices",
		// MarketPackage.eINSTANCE.getIndex(), extensions);
		// page.makeEditor(group, "Purchase Contracts",
		// ContractPackage.eINSTANCE.getPurchaseContract(), extensions);
		// page.makeEditor(group, "Sales Contracts",
		// ContractPackage.eINSTANCE.getSalesContract(), extensions);
		// page.makeEditor(group, "Entities",
		// ContractPackage.eINSTANCE.getEntity(), extensions);
		// }
	}

}

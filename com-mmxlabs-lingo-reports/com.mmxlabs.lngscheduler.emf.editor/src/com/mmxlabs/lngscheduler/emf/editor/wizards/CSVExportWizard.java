/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.editor.wizards;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.dialogs.WizardExportResourcesPage;

import scenario.Scenario;
import scenario.cargo.CargoPackage;
import scenario.contract.ContractPackage;
import scenario.fleet.FleetPackage;
import scenario.market.MarketPackage;
import scenario.port.PortPackage;
import scenario.schedule.SchedulePackage;

import com.mmxlabs.shiplingo.importer.importers.ImporterUtil;

/**
 * @author Tom Hinton
 * 
 */
public class CSVExportWizard extends Wizard implements IWorkbenchWizard {
	private ExportToDirectoryPage page = null;
	private File currentFile;

	private class ExportToDirectoryPage extends WizardExportResourcesPage {
		/**
		 * @param pageName
		 * @param selection
		 */
		protected ExportToDirectoryPage(String pageName,
				IStructuredSelection selection) {
			super(pageName, selection);
		}

		@Override
		public void handleEvent(Event event) {

		}

		@Override
		protected void createDestinationGroup(Composite parent) {

			Composite destinationSelectionGroup = new Composite(parent,
					SWT.NONE);
			GridLayout layout = new GridLayout();
			layout.numColumns = 1;
			destinationSelectionGroup.setLayout(layout);
			destinationSelectionGroup.setLayoutData(new GridData(
					GridData.HORIZONTAL_ALIGN_FILL
							| GridData.VERTICAL_ALIGN_FILL));

			final DirectoryFieldEditor destinationPicker = new DirectoryFieldEditor(
					"destination", "Destination Directory:",
					destinationSelectionGroup);

			destinationPicker.getTextControl(destinationSelectionGroup)
					.addModifyListener(new ModifyListener() {
						@Override
						public void modifyText(ModifyEvent e) {
							final File temp = new File(((Text) e.widget)
									.getText());
							if (temp.exists()) {
								currentFile = temp;
							} else {
								currentFile = null;
							}
							boolean pageComplete = determinePageCompletion();
							setPageComplete(pageComplete);
						}
					});
		}

		@Override
		protected boolean validateDestinationGroup() {
			return super.validateDestinationGroup() && currentFile != null;
		}

		public List getSelectedResources() {
			return super.getSelectedResources();
		}

		@Override
		protected void createOptionsGroup(Composite parent) {
		}
	}

	public CSVExportWizard() {
		setWindowTitle("Export Scenario to CSV Files");
	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		if (currentFile == null)
			return false;
		final List toExport = page.getSelectedResources();
		for (final Object o : toExport) {
			if (o instanceof IResource) {
				final IResource resource = (IResource) o;
				final Scenario scenario = (Scenario) resource
						.getAdapter(Scenario.class);

				if (scenario == null)
					continue;
				if (!exportScenario(scenario, resource.getName()))
					return false;
			}
		}
		return true;
	}

	/**
	 * Do the actual work of exporting a scenario into a bunch of CSV files.
	 * 
	 * @param scenario
	 * @return
	 */
	private boolean exportScenario(final Scenario scenario, final String name2) {
		final String name = name2.replace(".scenario", "");
		exportObjects(CargoPackage.eINSTANCE.getCargo(), scenario
				.getCargoModel().getCargoes(), name);

		exportObjects(FleetPackage.eINSTANCE.getVessel(), scenario
				.getFleetModel().getFleet(), name);
		exportObjects(FleetPackage.eINSTANCE.getVesselClass(), scenario
				.getFleetModel().getVesselClasses(), name);
		exportObjects(FleetPackage.eINSTANCE.getVesselEvent(), scenario
				.getFleetModel().getVesselEvents(), name);
		exportObjects(FleetPackage.eINSTANCE.getVesselFuel(), scenario
				.getFleetModel().getFuels(), name);

		exportObjects(PortPackage.eINSTANCE.getPort(), scenario.getPortModel()
				.getPorts(), name);
		exportObjects(PortPackage.eINSTANCE.getDistanceModel(),
				Collections.singletonList(scenario.getDistanceModel()), name);
		exportObjects(PortPackage.eINSTANCE.getCanal(), scenario
				.getCanalModel().getCanals(), name);

		exportObjects(ContractPackage.eINSTANCE.getSalesContract(), scenario
				.getContractModel().getSalesContracts(), name);
		exportObjects(ContractPackage.eINSTANCE.getPurchaseContract(), scenario
				.getContractModel().getPurchaseContracts(), name);
		final List<EObject> allEntities = new ArrayList<EObject>();
		allEntities.addAll(scenario.getContractModel().getEntities());
		allEntities.add(scenario.getContractModel().getShippingEntity());
		exportObjects(ContractPackage.eINSTANCE.getEntity(), allEntities, name);

		exportObjects(MarketPackage.eINSTANCE.getIndex(), scenario
				.getMarketModel().getIndices(), name);

		exportObjects(SchedulePackage.eINSTANCE.getSchedule(), scenario
				.getScheduleModel().getSchedules(), name);

		return true;
	}

	/**
	 * Export some objects to a file
	 * 
	 * @param cargo
	 * @param cargoes
	 * @param name
	 */
	private void exportObjects(final EClass eclass,
			final List<? extends EObject> objects, final String name) {
		final Map<String, Collection<Map<String, String>>> maps = ImporterUtil
				.getInstance().exportEObjects(eclass, objects);

		for (final Map.Entry<String, Collection<Map<String, String>>> entry : maps
				.entrySet()) {
			// TODO ignore name so we can use default things for import?
			final String suffix = /* name + "-" + */entry.getKey() + ".csv";
			ImporterUtil.getInstance().writeObjects(
					currentFile.getAbsolutePath() + "/" + suffix,
					entry.getValue());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 * org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		page = new ExportToDirectoryPage("Export Scenario", selection);
	}

}

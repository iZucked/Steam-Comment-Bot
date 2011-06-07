/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.mmxlabs.lngscheduler.emf.editor.wizards;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

import scenario.Scenario;
import scenario.ScenarioFactory;
import scenario.cargo.Cargo;
import scenario.cargo.CargoPackage;
import scenario.contract.ContractPackage;
import scenario.contract.Entity;
import scenario.contract.PurchaseContract;
import scenario.contract.SalesContract;
import scenario.fleet.FleetPackage;
import scenario.fleet.Vessel;
import scenario.fleet.VesselClass;
import scenario.fleet.VesselEvent;
import scenario.fleet.VesselFuel;
import scenario.market.Index;
import scenario.market.MarketPackage;
import scenario.optimiser.Constraint;
import scenario.optimiser.Objective;
import scenario.optimiser.Optimisation;
import scenario.optimiser.OptimiserFactory;
import scenario.optimiser.OptimiserPackage;
import scenario.optimiser.lso.LSOSettings;
import scenario.optimiser.lso.LsoFactory;
import scenario.optimiser.lso.LsoPackage;
import scenario.optimiser.lso.ThresholderSettings;
import scenario.port.Canal;
import scenario.port.DistanceModel;
import scenario.port.Port;
import scenario.port.PortPackage;
import scenario.presentation.cargoeditor.importer.CSVReader;
import scenario.presentation.cargoeditor.importer.DeferredReference;
import scenario.presentation.cargoeditor.importer.EObjectImporter;
import scenario.presentation.cargoeditor.importer.EObjectImporterFactory;
import scenario.presentation.cargoeditor.importer.NamedObjectRegistry;
import scenario.presentation.cargoeditor.importer.Postprocessor;

import com.mmxlabs.lngscheduler.emf.extras.EMFUtils;
import com.mmxlabs.optimiser.common.constraints.OrderedSequenceElementsConstraintCheckerFactory;
import com.mmxlabs.optimiser.common.constraints.ResourceAllocationConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortTypeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.TravelTimeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCoreFactory;

public class CSVImportWizard extends Wizard implements IImportWizard {
	WizardNewFileCreationPage destinationPage;
	WizardPage inputPage;

	private Scenario scenario;
	final private Map<EClass, String> inputFilePaths = new HashMap<EClass, String>();

	public CSVImportWizard() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	public boolean performFinish() {
		// try and run imports

		final NamedObjectRegistry nor = new NamedObjectRegistry();
		final LinkedList<DeferredReference> refs = new LinkedList<DeferredReference>();

		final LinkedList<EObject> importedObjects = new LinkedList<EObject>();

		for (final Map.Entry<EClass, String> job : inputFilePaths.entrySet()) {
			if (job.getKey().equals(
					FleetPackage.eINSTANCE.getFuelConsumptionLine()))
				continue;
			if (job.getValue() == null || job.getValue().isEmpty())
				continue;
			try {
				final EObjectImporter importer = EObjectImporterFactory
						.getInstance().getImporter(job.getKey());
				final CSVReader reader = new CSVReader(job.getValue());

				importedObjects.addAll(importer
						.importObjects(reader, refs, nor));
			} catch (final IOException ex) {
			}
		}

		// do final postprocessing
		// first add names for all the new objects
		for (final EObject object : importedObjects) {
			nor.addEObject(object);
		}
		// process fuel curves
		if (inputFilePaths.containsKey(FleetPackage.eINSTANCE
				.getFuelConsumptionLine())) {
			final String pth = inputFilePaths.get(FleetPackage.eINSTANCE
					.getFuelConsumptionLine());
			if (pth != null && pth.isEmpty() == false) {
				try {
					final CSVReader reader = new CSVReader(pth);
					final EObjectImporter importer = EObjectImporterFactory
							.getInstance().getImporter(
									FleetPackage.eINSTANCE
											.getFuelConsumptionLine());
					importer.importObjects(reader, refs, nor);
				} catch (final IOException ex) {
				}
			}
		}

		// next link everything up
		for (final DeferredReference ref : refs) {
			ref.setRegistry(nor.getContents());
			ref.run();
		}
		// finally tidy up dates etc
		for (final EObject object : importedObjects) {
			Postprocessor.getInstance().postprocess(object);
		}

		scenario = ScenarioFactory.eINSTANCE.createScenario();

		scenario.createMissingModels();
		scenario.setName(destinationPage.getFileName());

		// and last of all put all the results into a new scenario according to
		// their kind.
		for (final EObject object : importedObjects) {
			if (object != null) {
				if (object instanceof Cargo) {
					scenario.getCargoModel().getCargoes().add((Cargo) object);
				} else if (object instanceof VesselClass) {
					scenario.getFleetModel().getVesselClasses()
							.add((VesselClass) object);
				} else if (object instanceof Vessel) {
					scenario.getFleetModel().getFleet().add((Vessel) object);
				} else if (object instanceof Index) {
					scenario.getMarketModel().getIndices().add((Index) object);
				} else if (object instanceof SalesContract) {
					scenario.getContractModel().getSalesContracts()
							.add((SalesContract) object);
				} else if (object instanceof PurchaseContract) {
					scenario.getContractModel().getPurchaseContracts()
							.add((PurchaseContract) object);
				} else if (object instanceof DistanceModel) {
					scenario.setDistanceModel((DistanceModel) object);
				} else if (object instanceof Port) {
					scenario.getPortModel().getPorts().add((Port) object);
				} else if (object instanceof Entity) {
					scenario.getContractModel().getEntities()
							.add((Entity) object);
				} else if (object instanceof VesselEvent) {
					scenario.getFleetModel().getVesselEvents()
							.add((VesselEvent) object);
				} else if (object instanceof VesselFuel) {
					scenario.getFleetModel().getFuels()
							.add((VesselFuel) object);
				} else if (object instanceof Canal) {
					scenario.getCanalModel().getCanals().add((Canal) object);
				}
			}
		}

		addDefaultSettings(scenario);

		IFile file = destinationPage.createNewFile();
		if (file == null)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 * org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("File Import Wizard"); // NON-NLS-1
		setNeedsProgressMonitor(true);
		destinationPage = new WizardNewFileCreationPage(
				"Choose Scenario Destination", selection) {
			@Override
			protected InputStream getInitialContents() {
				return new ByteArrayInputStream(
						EMFUtils.serializeEObject(scenario));
			}
		};
		destinationPage.setTitle("Choose scenario destination");
		destinationPage
				.setDescription("Select a location in the workspace where the new scenario will be created and provide a name for it.");
		inputPage = new WizardPage("Choose CSV Data to Import") {
			{
				setTitle("Choose Input Data");
				setDescription("Choose the various files from which data should be imported; blank fields will be ignored.");
			}

			private ModifyListener listener(final EClass key) {
				return new ModifyListener() {
					@Override
					public void modifyText(final ModifyEvent e) {
						final Text text = (Text) e.widget;
						inputFilePaths.put(key, text.getText());
					}
				};
			}

			@Override
			public void createControl(final Composite parent) {
				final Composite topLevel = new Composite(parent, SWT.NONE);
				topLevel.setLayout(new GridLayout(1, false));

				final String[] extensions = new String[] { "*.csv" };
				{
					final Group group = new Group(topLevel, SWT.NONE);
					group.setLayout(new RowLayout(SWT.VERTICAL));
					group.setText("Ports and Distances");
					group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
							false));

					makeEditor(group, "Ports", PortPackage.eINSTANCE.getPort(),
							extensions);
					makeEditor(group, "Distance Matrix",
							PortPackage.eINSTANCE.getDistanceModel(),
							extensions);
					makeEditor(group, "Canals",
							PortPackage.eINSTANCE.getCanal(), extensions);
				}

				{
					final Group group = new Group(topLevel, SWT.NONE);
					group.setLayout(new RowLayout(SWT.VERTICAL));
					group.setText("Fleet");
					group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
							false));
					makeEditor(group, "Vessel Classes",
							FleetPackage.eINSTANCE.getVesselClass(), extensions);
					makeEditor(group, "Fuel Curves",
							FleetPackage.eINSTANCE.getFuelConsumptionLine(),
							extensions);
					makeEditor(group, "Vessels",
							FleetPackage.eINSTANCE.getVessel(), extensions);
					makeEditor(group, "Base Fuels",
							FleetPackage.eINSTANCE.getVesselFuel(), extensions);
				}

				{
					final Group group = new Group(topLevel, SWT.NONE);
					group.setLayout(new RowLayout(SWT.VERTICAL));
					group.setText("Indices and Contracts");
					group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
							false));
					
					makeEditor(group, "Indices", MarketPackage.eINSTANCE.getIndex(), extensions);
					makeEditor(group, "Purchase Contracts", ContractPackage.eINSTANCE.getPurchaseContract(), extensions);
					makeEditor(group, "Sales Contracts", ContractPackage.eINSTANCE.getSalesContract(), extensions);
					makeEditor(group, "Entities", ContractPackage.eINSTANCE.getEntity(), extensions);
				}

				{
					final Group group = new Group(topLevel, SWT.NONE);
					group.setLayout(new RowLayout(SWT.VERTICAL));
					group.setText("Cargoes and Events");
					group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
							false));
					makeEditor(group, "Cargoes", CargoPackage.eINSTANCE.getCargo(), extensions);
					makeEditor(group, "Events", FleetPackage.eINSTANCE.getVesselEvent(), extensions);
				}

				setControl(topLevel);
			}

			private void makeEditor(Group group, String name, EClass ec,
					String[] extensions) {
				final FileFieldEditor portsEditor = new FileFieldEditor(name
						+ "Select", name, group);
				portsEditor.setFileExtensions(extensions);
				portsEditor.setEmptyStringAllowed(true);
				portsEditor.getTextControl(group).addModifyListener(
						listener(ec));
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	public void addPages() {
		super.addPages();
		addPage(inputPage);
		addPage(destinationPage);
	}

	/**
	 * Add some standard optimiser settings to the given scenario
	 * 
	 * Copied from RandomScenarioUtils, due to build cycle issues.
	 * 
	 * TODO check these are reasonable settings (num. iters etc)
	 * 
	 * @param scenario
	 */
	private void addDefaultSettings(Scenario scenario) {
		final OptimiserFactory of = OptimiserPackage.eINSTANCE
				.getOptimiserFactory();
		final LsoFactory lsof = LsoPackage.eINSTANCE.getLsoFactory();

		Optimisation optimisation = of.createOptimisation();

		LSOSettings settings = lsof.createLSOSettings();

		settings.setName("Default LSO Settings");

		// create constraints
		{
			final EList<Constraint> constraints = settings.getConstraints();
			constraints.add(createConstraint(of,
					ResourceAllocationConstraintCheckerFactory.NAME, true));
			constraints
					.add(createConstraint(
							of,
							OrderedSequenceElementsConstraintCheckerFactory.NAME,
							true));
			constraints.add(createConstraint(of,
					PortTypeConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(of,
					TravelTimeConstraintCheckerFactory.NAME, true));
		}

		// create objectives
		{
			final EList<Objective> objectives = settings.getObjectives();
			objectives
					.add(createObjective(
							of,
							CargoSchedulerFitnessCoreFactory.CHARTER_COST_COMPONENT_NAME,
							1));
			objectives.add(createObjective(of,
					CargoSchedulerFitnessCoreFactory.COST_BASE_COMPONENT_NAME,
					1));
			objectives
					.add(createObjective(
							of,
							CargoSchedulerFitnessCoreFactory.COST_LNG_COMPONENT_NAME,
							1));
			objectives
					.add(createObjective(
							of,
							CargoSchedulerFitnessCoreFactory.LATENESS_COMPONENT_NAME,
							1));
			objectives
					.add(createObjective(
							of,
							CargoSchedulerFitnessCoreFactory.DISTANCE_COMPONENT_NAME,
							0));
			objectives
					.add(createObjective(
							of,
							CargoSchedulerFitnessCoreFactory.ROUTE_PRICE_COMPONENT_NAME,
							1));
			objectives
					.add(createObjective(
							of,
							CargoSchedulerFitnessCoreFactory.CARGO_ALLOCATION_COMPONENT_NAME,
							1));

		}

		settings.setNumberOfSteps(20000);
		ThresholderSettings thresholderSettings = lsof
				.createThresholderSettings();
		thresholderSettings.setAlpha(0.9);
		thresholderSettings.setEpochLength(1000);
		thresholderSettings.setInitialAcceptanceRate(0.2);
		settings.setThresholderSettings(thresholderSettings);

		optimisation.getAllSettings().add(settings);
		optimisation.setCurrentSettings(settings);
		scenario.setOptimisation(optimisation);
	}

	private Constraint createConstraint(OptimiserFactory of, String name,
			boolean enabled) {
		Constraint c = of.createConstraint();
		c.setName(name);
		c.setEnabled(enabled);
		return c;
	}

	private Objective createObjective(OptimiserFactory of, String name,
			double weight) {
		Objective o = of.createObjective();
		o.setName(name);
		o.setWeight(weight);
		return o;
	}
}

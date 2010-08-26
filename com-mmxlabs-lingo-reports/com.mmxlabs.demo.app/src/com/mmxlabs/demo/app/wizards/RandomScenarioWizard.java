package com.mmxlabs.demo.app.wizards;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.mmxlabs.common.csv.DistanceImporter;

import scenario.Scenario;
import scenario.ScenarioPackage;
import scenario.cargo.CargoPackage;
import scenario.fleet.FleetPackage;
import scenario.port.DistanceLine;
import scenario.port.DistanceModel;
import scenario.port.Port;
import scenario.port.PortFactory;
import scenario.port.PortPackage;
import scenario.presentation.LngEditorAdvisor;



public class RandomScenarioWizard extends Wizard implements INewWizard {

	private IWorkbench workbench;
	private DetailsPage details;


	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.details = new DetailsPage("Details");
	}


	@Override
	public void addPages() {
		super.addPages();
		addPage(details);
	}



	@Override
	public boolean performFinish() {
		final URI fileURI = details.getFileURI();

		/*
		 * Have to create scenario first, because otherwise it won't be registered with the resourcesetimpl
		 * when we try to save.
		 */
		Scenario scenario = ScenarioPackage.eINSTANCE.getScenarioFactory().createScenario();
		/*
		 * Now load distance matrix and add to scenario
		 */


		try {
			DistanceImporter di = new DistanceImporter(details.getMatrixURI().toFileString());
			final PortFactory pf = PortPackage.eINSTANCE.getPortFactory();
			scenario.setPortModel(pf.createPortModel());
			//TODO put this somewhere more sensible
			for (String s : di.getKeys()) {
				Port port = pf.createPort();
				port.setName(s);
				scenario.getPortModel().getPorts().add(port);
			}

			//add useful default models
			scenario.setFleetModel(FleetPackage.eINSTANCE.getFleetFactory().createFleetModel());
			scenario.setCargoModel(CargoPackage.eINSTANCE.getCargoFactory().createCargoModel());

			scenario.setDistanceModel(pf.createDistanceModel());
			final DistanceModel dm = scenario.getDistanceModel();

			for (Port a : scenario.getPortModel().getPorts()) {
				for (Port b : scenario.getPortModel().getPorts()) {
					final int distance = di.getDistance(a.getName(), b.getName());
					if (!(a.equals(b)) && distance != Integer.MAX_VALUE) {
						final DistanceLine dl = pf.createDistanceLine();
						dl.setFromPort(a);
						dl.setToPort(b);
						dl.setDistance(distance);
						dm.getDistances().add(dl);
					}
				}
			}

			//hack
			RandomScenarioUtils utils = new RandomScenarioUtils();

			if (details.shouldCreateVesselClasses()) {
				utils.addStandardFleet(scenario);
				utils.createRandomCargoes(scenario, details.getCargoCount());
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return false;
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}

		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(fileURI);

		resource.getContents().add(scenario);

		try {
			resource.save(new HashMap<Object, Object>());
			System.err.println("Created " + fileURI.path());
		} catch (IOException e) {
			return false;
		}

		return LngEditorAdvisor.openEditor(workbench, fileURI);
	}

	class DetailsPage extends WizardPage {
		protected DetailsPage(String pageName) {
			super(pageName);
		}

		public URI getFileURI() {
			String s = fileField.getText();

			if (!s.endsWith(".scenario")) {
				s = s + ".scenario";
			}

			return URI.createFileURI(s);
		}

		public URI getMatrixURI() {
			return URI.createFileURI(matrixField.getText());
		}

		public boolean shouldCreateVesselClasses() {
			return vesselClasses.getSelection();
		}

		public int getCargoCount() {
			return cargoSpinner.getSelection();
		}

		protected Text fileField;
		protected Text matrixField;
		private Spinner cargoSpinner;
		protected Button vesselClasses;

		@Override
		public void createControl(Composite parent) {
			Composite myContents = new Composite(parent, SWT.NONE);
			GridLayout layout = new GridLayout(1, false);
			myContents.setLayout(layout);

			fileField = makeElement(myContents,"Scenario file to create", "scenario", false);
			matrixField = makeElement(myContents, "Distance matrix to import", "csv", true);

			Label label = new Label(myContents, SWT.NONE);
			label.setText("Other model features");
			Composite row = new Composite(myContents, SWT.NONE);
			row.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			row.setLayout(new GridLayout(3, false));

			vesselClasses = new Button(row, SWT.CHECK);

			vesselClasses.setText("Add default Fleet Model");

			label = new Label(row, SWT.NONE);
			label.setText("Number of random cargo slots:");
			cargoSpinner = new Spinner(row, SWT.NONE);

			setControl(myContents);
		}

		public Text makeElement(Composite contents, String caption, String extension, boolean open) {
			Label label = new Label(contents, SWT.NONE);
			label.setText(caption);
			return makeFilePicker(contents, new String[] {"*." + extension}, open);
		}

		public Text makeFilePicker(Composite container, final String[] extensions, final boolean open) {
			Composite row = new Composite(container, SWT.NONE);

			row.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			row.setLayout(new GridLayout(2, false));

			final Text t = new Text(row, SWT.BORDER);
			t.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

			Button picker = new Button(row, SWT.PUSH);
			picker.setLayoutData(new GridData(SWT.END, SWT.CENTER, false, false));

			picker.setText("Choose");

			picker.addSelectionListener(

					new SelectionListener() {

						@Override
						public void widgetSelected(SelectionEvent e) {
							FileDialog fd = 
								new FileDialog(getShell(), open ? SWT.OPEN : SWT.SAVE);
							fd.setFilterExtensions(extensions);
							String sd = fd.open();
							t.setText(sd);
						}

						@Override
						public void widgetDefaultSelected(SelectionEvent e) {
						}
					}

			);


			return t;
		}
	}
}

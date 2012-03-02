/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
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

import scenario.Scenario;
import scenario.presentation.LngEditorAdvisor;

import com.mmxlabs.lngscheduler.emf.extras.ScenarioUtils;

public class RandomScenarioWizard extends Wizard implements INewWizard {
	private IWorkbench workbench;
	private DetailsPage details;

	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
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
		final String outputFileName = details.getFileName();

		Scenario scenario = null;

		try {
			final RandomScenarioUtils utils = new RandomScenarioUtils();
			scenario = utils.createScenario();
			final String matrixPath = details.getMatrixURI().toFileString();
			final String slotsPath = details.getSlotsURI().toFileString();

			utils.addDistanceModel(scenario, matrixPath);

			if (details.shouldCreateVesselClasses()) {
				// default setting: add 3* each vessel class as a charter-in
				utils.addDefaultFleet(scenario, 3);
			}

			if (details.getCargoCount() > 0) {
				// default settings:
				// 6 hr time windows, 24 hr visits, 1-15 days of slack between pickup and delivery,
				// no locality bias and 1 year total duration.

				// if you want to control all of these variables, there is a command-line
				// class called {@link HeadlessScenarioGenerator} which exposes these parameters

				utils.addRandomCargoes(scenario, matrixPath, slotsPath, details.getCargoCount(), 6, 6, 24, 24,

				1

				, 15,

				details.getLocality(),// /locality

						365);
				utils.addCharterOuts(scenario, 1, 25, 35, 365);
			}

			ScenarioUtils.addDefaultSettings(scenario);

		} catch (final FileNotFoundException e1) {
			e1.printStackTrace();
			return false;
		} catch (final IOException e1) {
			e1.printStackTrace();
			return false;
		}

		final URI fileURI = URI.createFileURI(outputFileName);

		// save scenario to file
		final ResourceSet resourceSet = new ResourceSetImpl();
		final Resource resource = resourceSet.createResource(fileURI);

		resource.getContents().add(scenario);

		try {
			resource.save(new HashMap<Object, Object>());
		} catch (final IOException e) {
			return false;
		}

		// try adding file to selected project?

		return LngEditorAdvisor.openEditor(workbench, fileURI);
	}

	static class DetailsPage extends WizardPage {
		protected DetailsPage(final String pageName) {
			super(pageName);
		}

		public URI getSlotsURI() {
			return URI.createFileURI(slotsField.getText());
		}

		public String getFileName() {
			String s = fileField.getText();

			if (!s.endsWith(".scenario")) {
				s = s + ".scenario";
			}
			return s;
			// return URI.createFileURI(s);
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

		public double getLocality() {
			return localitySpinner.getSelection() / Math.pow(10, localitySpinner.getDigits());
		}

		protected Text fileField;
		protected Text matrixField;
		private Spinner cargoSpinner;
		private Spinner localitySpinner;
		protected Button vesselClasses;
		private Text slotsField;

		@Override
		public void createControl(final Composite parent) {
			final Composite myContents = new Composite(parent, SWT.NONE);
			final GridLayout layout = new GridLayout(1, false);
			myContents.setLayout(layout);

			fileField = makeElement(myContents, "Scenario file to create", "scenario", false);
			matrixField = makeElement(myContents, "Distance matrix to import", "csv", true);

			slotsField = makeElement(myContents, "Slot count list", "csv", true);

			Label label = new Label(myContents, SWT.NONE);
			label.setText("Other model features");
			final Composite row = new Composite(myContents, SWT.NONE);
			row.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
			row.setLayout(new GridLayout(3, false));

			vesselClasses = new Button(row, SWT.CHECK);

			vesselClasses.setText("Add default Fleet Model");

			label = new Label(row, SWT.NONE);
			label.setText("Cargo count:");
			cargoSpinner = new Spinner(row, SWT.NONE);
			cargoSpinner.setMaximum(1000);
			cargoSpinner.setMinimum(10);
			cargoSpinner.setSelection(200);
			label = new Label(row, SWT.NONE);
			label.setText("Locality:");
			localitySpinner = new Spinner(row, SWT.NONE);
			localitySpinner.setMaximum(100);
			localitySpinner.setMinimum(0);
			localitySpinner.setDigits(1);

			localitySpinner.setSelection(60);
			setControl(myContents);
		}

		public Text makeElement(final Composite contents, final String caption, final String extension, final boolean open) {
			final Label label = new Label(contents, SWT.NONE);
			label.setText(caption);
			return makeFilePicker(contents, new String[] { "*." + extension }, open);
		}

		public Text makeFilePicker(final Composite container, final String[] extensions, final boolean open) {
			final Composite row = new Composite(container, SWT.NONE);

			row.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

			row.setLayout(new GridLayout(2, false));

			final Text t = new Text(row, SWT.BORDER);
			t.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

			final Button picker = new Button(row, SWT.PUSH);
			picker.setLayoutData(new GridData(SWT.END, SWT.CENTER, false, false));

			picker.setText("Choose");

			picker.addSelectionListener(

			new SelectionListener() {

				@Override
				public void widgetSelected(final SelectionEvent e) {
					final FileDialog fd = new FileDialog(getShell(), open ? SWT.OPEN : SWT.SAVE);
					fd.setFilterExtensions(extensions);
					final String sd = fd.open();
					if (sd != null) {
						t.setText(sd);
					}
				}

				@Override
				public void widgetDefaultSelected(final SelectionEvent e) {
				}
			}

			);

			return t;
		}
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.optimisation.navigator.derive;

import java.io.IOException;
import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.input.Assignment;
import com.mmxlabs.models.lng.input.InputFactory;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.shiplingo.platform.models.manifest.wizards.ScenarioServiceNewScenarioPage;

/**
 * Export the selected scenario to the filesystem somehow.
 * 
 * @author hinton
 * 
 */
public class DeriveWizard extends Wizard implements IExportWizard {
	private DeriveWizardPage sourcePage;
	private ScenarioServiceNewScenarioPage destinationPage;

	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		setWindowTitle("Save Optimisation Wizard");
		sourcePage = new DeriveWizardPage(selection);
		destinationPage = new ScenarioServiceNewScenarioPage(selection);
	}

	@Override
	public boolean performFinish() {
		final Collection<ScenarioInstance> inputs = sourcePage.getScenarioInstance();
		final Container output = destinationPage.getScenarioContainer();
		final String outputName = destinationPage.getFileName();

		if (inputs.isEmpty())
			return false;
		final ScenarioInstance input = inputs.iterator().next();

		return derive(input, output, outputName);
	}

	private boolean derive(ScenarioInstance input, Container output, String outputName) {
		try {
			final ScenarioInstance duplicate = output.getScenarioService().duplicate(input, output);
			duplicate.setName(outputName);
		} catch (IOException e) {

		}
		return false;
	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(sourcePage);
		addPage(destinationPage);
	}
}
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
		
		if (inputs.isEmpty()) return false;
		final ScenarioInstance input = inputs.iterator().next();
		
		return derive(input, output, outputName);
	}

	private boolean derive(ScenarioInstance input, Container output, String outputName) {
		try {
			final ScenarioInstance duplicate = output.getScenarioService().duplicate(input, output);
			duplicate.setName(outputName);
			
//			// fix output model
//			final EObject top = duplicate.getScenarioService().load(duplicate);
//			if (top instanceof MMXRootObject) {
//				final MMXRootObject root = (MMXRootObject) top;
//				final InputModel inputModel = root.getSubModel(InputModel.class);
//				final ScheduleModel scheduleModel = root.getSubModel(ScheduleModel.class);
//				final CargoModel cargoModel = root.getSubModel(CargoModel.class);
//				if (inputModel != null && scheduleModel != null && cargoModel != null) {
//					inputModel.getAssignments().clear();
//					
//					if (scheduleModel.getOptimisedSchedule() != null) {
//						derive(scheduleModel.getOptimisedSchedule(), inputModel, cargoModel);
//					} else if (scheduleModel.getInitialSchedule() != null) {
//						derive(scheduleModel.getInitialSchedule(), inputModel, cargoModel);
//					}
//					
//					duplicate.getScenarioService().save(duplicate);
//					return true;
//				}
//			}
		} catch (IOException e) {
		
		}
		return false;
	}

	private void derive(final Schedule schedule, final InputModel inputModel, final CargoModel cargoModel) {
		for (final Sequence sequence : schedule.getSequences()) {
			final Assignment a = InputFactory.eINSTANCE.createAssignment();
			if (sequence.isSpotVessel()) {
				a.setAssignToSpot(true);
				a.getVessels().add(sequence.getVesselClass());
			} else {
				a.setAssignToSpot(false);
				a.getVessels().add(sequence.getVessel());
			}
			for (final Event event : sequence.getEvents()) {
				if (event instanceof SlotVisit) {
					a.getAssignedObjects().add(((SlotVisit) event).getSlotAllocation().getSlot());
				} else if (event instanceof VesselEventVisit) {
					a.getAssignedObjects().add(((VesselEventVisit) event).getVesselEvent());
				}
			}
			if (!a.getAssignedObjects().isEmpty()) {
				inputModel.getAssignments().add(a);
			}
		}
		// rewire any cargos which require it
		// TODO handle spot market cases, and free slots
		for (final CargoAllocation allocation : schedule.getCargoAllocations()) {
			if (allocation.getInputCargo() == null) {
				// this does not correspond directly to an input cargo;
				// get the slots, find their cargos, and adjust them?
				final LoadSlot load = (LoadSlot) allocation.getLoadAllocation().getSlot();
				final DischargeSlot discharge = (DischargeSlot) allocation.getDischargeAllocation().getSlot();
				
				final Cargo loadCargo = load.getCargo();
				final Cargo dischargeCargo = discharge.getCargo();
				
				// the cargo "belongs" to the load slot
				loadCargo.setDischargeSlot(discharge);
				dischargeCargo.setDischargeSlot(null);
			}
		}
	}

	@Override
	public void addPages() {
		super.addPages();
		addPage(sourcePage);
		addPage(destinationPage);
	}
}
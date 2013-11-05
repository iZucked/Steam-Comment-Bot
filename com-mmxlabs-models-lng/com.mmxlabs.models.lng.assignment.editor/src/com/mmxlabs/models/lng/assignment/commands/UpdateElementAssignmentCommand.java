package com.mmxlabs.models.lng.assignment.commands;

import java.util.ArrayList;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.assignment.AssignmentFactory;
import com.mmxlabs.models.lng.assignment.AssignmentModel;
import com.mmxlabs.models.lng.assignment.AssignmentPackage;
import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.assignment.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.mmxcore.UUIDObject;

public class UpdateElementAssignmentCommand extends CompoundCommand {

	/**
	 * This caches the label.
	 */
	protected static final String LABEL = "Update Element Assignment";

	/**
	 * This caches the description.
	 */
	protected static final String DESCRIPTION = "Update element assignment data";

	private final EditingDomain domain;

	private final UUIDObject assignedObject;

	private final AssignmentModel assignmentModel;

	/**
	 * This constructs a command that deletes the objects in the given collection.
	 */
	public UpdateElementAssignmentCommand(@NonNull final EditingDomain domain, @NonNull final AssignmentModel assignmentModel, @NonNull final UUIDObject assignedObject) {
		super(0, LABEL, DESCRIPTION);
		this.domain = domain;
		this.assignmentModel = assignmentModel;
		this.assignedObject = assignedObject;
	}

	@Override
	protected boolean prepare() {
		for (final Command command : commandList) {
			if (!command.canExecute()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void execute() {
		super.execute();

		if (assignedObject instanceof LoadSlot) {
			// If a DES Purchase, then the DES Purchase should have the element assignment, not the cargo. Otherwise it should only be on the cargo
			final LoadSlot loadSlot = (LoadSlot) assignedObject;
			if (loadSlot.isDESPurchase()) {
				{
					// Create an assignment if missing
					createElementAssignment(assignedObject);
				}
				// Remove existing cargo assignment
				final Cargo cargo = loadSlot.getCargo();
				if (cargo != null) {
					deleteAssignments(cargo);
				}
			} else {
				// Remove non-DES Purchase assignment
				{
					deleteAssignments(assignedObject);
				}
				// Create cargo assignment if missing
				final Cargo cargo = loadSlot.getCargo();
				if (cargo != null&& cargo.getCargoType() == CargoType.FLEET) {
					createElementAssignment(cargo);
				}
			}
		} else if (assignedObject instanceof DischargeSlot) {
			final DischargeSlot dischargeSlot = (DischargeSlot) assignedObject;
			if (dischargeSlot.isFOBSale()) {
				{
					// Create an assignment if missing
					createElementAssignment(assignedObject);
				}
				// Remove existing cargo assignment
				final Cargo cargo = dischargeSlot.getCargo();
				if (cargo != null) {
					deleteAssignments(cargo);
				}
			} else {
				// Remove non-FOB Sale assignment
				{
					deleteAssignments(assignedObject);
				}
				// Create cargo assignment if missing
				final Cargo cargo = dischargeSlot.getCargo();
				if (cargo != null && cargo.getCargoType() == CargoType.FLEET) {
					createElementAssignment(cargo);
				}
			}
		} else if (assignedObject instanceof Cargo) {
			final Cargo cargo = (Cargo) assignedObject;
			if (cargo.getCargoType() == CargoType.FLEET) {
				createElementAssignment(cargo);
			} else {
				deleteAssignments(cargo);
			}
		} else if (assignedObject instanceof VesselEvent) {
			createElementAssignment(assignedObject);
		}
	}

	private void createElementAssignment(final UUIDObject assignedObject) {
		ElementAssignment elementAssignment = AssignmentEditorHelper.getElementAssignment(assignmentModel, assignedObject);
		if (elementAssignment == null) {
			elementAssignment = AssignmentFactory.eINSTANCE.createElementAssignment();
			elementAssignment.setAssignedObject(assignedObject);
			appendAndExecute(AddCommand.create(domain, assignmentModel, AssignmentPackage.eINSTANCE.getAssignmentModel_ElementAssignments(), elementAssignment));
		}
	}

	private void deleteAssignments(final UUIDObject uuidObject) {

		for (final ElementAssignment ea : new ArrayList<>(assignmentModel.getElementAssignments())) {
			if (ea != null && ea.getAssignedObject() == uuidObject) {
				appendAndExecute(DeleteCommand.create(domain, ea));
			}
		}
	}
}

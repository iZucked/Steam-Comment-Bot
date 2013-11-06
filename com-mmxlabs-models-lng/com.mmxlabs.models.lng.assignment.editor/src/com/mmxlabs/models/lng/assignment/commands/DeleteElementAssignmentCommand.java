package com.mmxlabs.models.lng.assignment.commands;

import java.util.ArrayList;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.assignment.AssignmentModel;
import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.mmxcore.UUIDObject;

public class DeleteElementAssignmentCommand extends CompoundCommand {

	/**
	 * This caches the label.
	 */
	protected static final String LABEL = "Delete Element Assignment";

	/**
	 * This caches the description.
	 */
	protected static final String DESCRIPTION = "Delete element assignment data";

	private final EditingDomain domain;

	private final UUIDObject assignedObject;

	private final AssignmentModel assignmentModel;

	/**
	 * This constructs a command that deletes the objects in the given collection.
	 */
	public DeleteElementAssignmentCommand(@NonNull final EditingDomain domain, @NonNull final AssignmentModel assignmentModel, @NonNull final UUIDObject assignedObject) {
		super(0, LABEL, DESCRIPTION);
		this.domain = domain;
		this.assignmentModel = assignmentModel;
		this.assignedObject = assignedObject;
	}

	@Override
	protected boolean prepare() {
		return true;
	}

	@Override
	public void execute() {
		super.execute();
		deleteAssignments(assignedObject);
	}

	private void deleteAssignments(final UUIDObject uuidObject) {

		for (final ElementAssignment ea : new ArrayList<>(assignmentModel.getElementAssignments())) {
			// General clean up...
			if (ea.getAssignedObject() == null && ea.getAssignment() == null) {
				appendAndExecute(DeleteCommand.create(domain, ea));
			}

			if (ea != null && ea.getAssignedObject() == uuidObject) {
				appendAndExecute(DeleteCommand.create(domain, ea));
			}
		}
	}
}

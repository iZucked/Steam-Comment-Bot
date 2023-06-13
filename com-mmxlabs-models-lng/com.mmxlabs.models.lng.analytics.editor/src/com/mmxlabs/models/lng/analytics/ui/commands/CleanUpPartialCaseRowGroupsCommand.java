package com.mmxlabs.models.lng.analytics.ui.commands;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.PartialCase;
import com.mmxlabs.models.lng.analytics.PartialCaseRowGroup;

public class CleanUpPartialCaseRowGroupsCommand extends CompoundCommand {

	private final EditingDomain domain;
	private final PartialCase partialCase;

	public CleanUpPartialCaseRowGroupsCommand(final EditingDomain domain, final PartialCase partialCase) {
		this.domain = domain;
		this.partialCase = partialCase;

	}

	@Override
	protected boolean prepare() {
		return true;
	}

	@Override
	public void append(final Command command) {
		super.appendAndExecute(command);
	}

	@Override
	public void execute() {

		final List<PartialCaseRowGroup> toRemove = new LinkedList<>();
		for (final PartialCaseRowGroup grp : partialCase.getGroups()) {
			if (grp.getRows().isEmpty()) {
				toRemove.add(grp);
			}
		}

		if (!toRemove.isEmpty()) {
			appendAndExecute(RemoveCommand.create(domain, partialCase, AnalyticsPackage.Literals.PARTIAL_CASE__GROUPS, toRemove));

		}
	}
}

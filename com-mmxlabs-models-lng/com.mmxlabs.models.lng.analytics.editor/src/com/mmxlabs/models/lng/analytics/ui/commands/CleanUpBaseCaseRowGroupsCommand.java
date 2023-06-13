package com.mmxlabs.models.lng.analytics.ui.commands;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BaseCaseRowGroup;

public class CleanUpBaseCaseRowGroupsCommand extends CompoundCommand {

	private final EditingDomain domain;
	private final BaseCase baseCase;

	public CleanUpBaseCaseRowGroupsCommand(final EditingDomain domain, final BaseCase baseCase) {
		this.domain = domain;
		this.baseCase = baseCase;

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

		final List<BaseCaseRowGroup> toRemove = new LinkedList<>();
		for (final BaseCaseRowGroup grp : baseCase.getGroups()) {
			if (grp.getRows().isEmpty()) {
				toRemove.add(grp);
			}
		}

		if (!toRemove.isEmpty()) {
			appendAndExecute(RemoveCommand.create(domain, baseCase, AnalyticsPackage.Literals.BASE_CASE__GROUPS, toRemove));

		}
	}
}

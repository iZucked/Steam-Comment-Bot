package com.mmxlabs.lingo.reports.views.changeset.actions;

import java.io.IOException;

import org.eclipse.jface.action.Action;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRoot;
import com.mmxlabs.models.lng.transformer.ui.ExportScheduleHelper;

public class ExportChangeAction extends Action {
	private ChangeSetTableGroup changeSetTableGroup;

	public ExportChangeAction(ChangeSetTableGroup changeSetTableGroup) {
		super("Export change");
		this.changeSetTableGroup = changeSetTableGroup;
	}

	@Override
	public void run() {
		try {
			final ChangeSetTableRoot root = (ChangeSetTableRoot) changeSetTableGroup.eContainer();
			String name;
			if (changeSetTableGroup.getDescription() != null && !changeSetTableGroup.getDescription().isEmpty()) {
				name = changeSetTableGroup.getDescription();
			} else {
				int idx = 0;
				if (root != null) {
					idx = root.getGroups().indexOf(changeSetTableGroup);
				}
				name = String.format("Set %d", idx);
			}
			ExportScheduleHelper.export(changeSetTableGroup.getChangeSet().getCurrentScenario(), name);
		} catch (final IOException e1) {
			e1.printStackTrace();
		}
	}
}

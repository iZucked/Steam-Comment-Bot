package com.mmxlabs.lingo.reports.views.changeset.actions;

import java.util.Collection;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRoot;

public class MergeChangesAction extends Action {
	private final Collection<ChangeSetTableGroup> selectedSets;
	private final Viewer viewer;

	public MergeChangesAction(final Collection<ChangeSetTableGroup> selectedSets, final Viewer viewer) {
		super("Merge changes");
		this.selectedSets = selectedSets;
		this.viewer = viewer;
	}

	@Override
	public void run() {
		final ChangeSetTableGroup firstChangeSet = selectedSets.iterator().next();
		selectedSets.remove(firstChangeSet);
		for (final ChangeSetTableGroup cs : selectedSets) {
			firstChangeSet.getRows().addAll(cs.getRows());
			if (firstChangeSet.getCurrentMetrics() != null) {
				final int pnl = firstChangeSet.getCurrentMetrics().getPnl();
				firstChangeSet.getCurrentMetrics().setPnl(pnl + cs.getCurrentMetrics().getPnl());

				final int capacity = firstChangeSet.getCurrentMetrics().getCapacity();
				firstChangeSet.getCurrentMetrics().setCapacity(capacity + cs.getCurrentMetrics().getCapacity());

				final int lateness = firstChangeSet.getCurrentMetrics().getLateness();
				firstChangeSet.getCurrentMetrics().setLateness(lateness + cs.getCurrentMetrics().getLateness());
			}
			if (firstChangeSet.getDeltaMetrics() != null) {
				final int pnl = firstChangeSet.getDeltaMetrics().getPnlDelta();
				firstChangeSet.getDeltaMetrics().setPnlDelta(pnl + cs.getDeltaMetrics().getPnlDelta());

				final int capacity = firstChangeSet.getDeltaMetrics().getCapacityDelta();
				firstChangeSet.getDeltaMetrics().setCapacityDelta(capacity + cs.getDeltaMetrics().getCapacityDelta());

				final int lateness = firstChangeSet.getDeltaMetrics().getLatenessDelta();
				firstChangeSet.getDeltaMetrics().setLatenessDelta(lateness + cs.getDeltaMetrics().getLatenessDelta());
			}
		}
		final ChangeSetTableRoot root = (ChangeSetTableRoot) firstChangeSet.eContainer();
		root.getGroups().removeAll(selectedSets);
		viewer.refresh();
	}
}

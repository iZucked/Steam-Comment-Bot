package com.mmxlabs.lingo.reports.views.changeset;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;

import com.google.common.base.Objects;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;

public class InsertionPlanSorter extends ViewerComparator {

	private Set<ChangeSet> setsToInclude = new HashSet<>();
	private boolean filterActive = false;

	 @Override
	public int category(Object element) {
		// TODO Auto-generated method stub
		return super.category(element);
	}
	 

	public class InsertionDescription {
		String description = "";
		int changeCount;
		ChangeSet representativeChangeSet = null;
		Collection<ChangeSet> allChangeSets = new LinkedList<>();
	}

	public class ChangeSetMetadata {
		public int changeCount;
		public Object sendTo;

		@Override
		public int hashCode() {
			return Objects.hashCode(changeCount, sendTo);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			}
			if (obj instanceof ChangeSetMetadata) {
				ChangeSetMetadata other = (ChangeSetMetadata) obj;
				return this.changeCount == other.changeCount && this.sendTo == other.sendTo;
			}
			return false;
		}
	}

	public void processChangeSetRoot(ChangeSetRoot root, Slot target) {
		setsToInclude.clear();
		// Group by change count and target
		Map<ChangeSetMetadata, List<ChangeSet>> grouper = new HashMap<>();
		for (ChangeSet changeSet : root.getChangeSets()) {
			Collection<ChangeSetRow> changeSetRows = changeSet.getChangeSetRowsToBase();
			int structuralChanges = 0;
			ChangeSetRow targetRow = null;
			for (ChangeSetRow row : changeSetRows) {
				if (row.isWiringChange() || row.isVesselChange()) {
					++structuralChanges;
				}
				if (row.getLoadSlot() == target || row.getDischargeSlot() == target) {
					assert targetRow == null;
					targetRow = row;
				}
			}
			ChangeSetMetadata key = new ChangeSetMetadata();
			key.changeCount = structuralChanges;
			Object sendTo = null;
			if (targetRow != null) {
				if (targetRow.getLoadSlot() == target) {
					DischargeSlot dischargeSlot = targetRow.getDischargeSlot();
					if (dischargeSlot instanceof SpotSlot) {
						sendTo = ((SpotSlot) dischargeSlot).getMarket();
					} else if (dischargeSlot.getContract() != null) {
						sendTo = dischargeSlot.getContract();
					} else {
						sendTo = dischargeSlot.getPort();
					}

				} else if (targetRow.getDischargeSlot() == target) {
					LoadSlot loadSlot = targetRow.getLoadSlot();
					if (loadSlot instanceof SpotSlot) {
						sendTo = ((SpotSlot) loadSlot).getMarket();
					} else if (loadSlot.getContract() != null) {
						sendTo = loadSlot.getContract();
					} else {
						sendTo = loadSlot.getPort();
					}
				}
				key.sendTo = sendTo;
				grouper.computeIfAbsent(key, k -> new LinkedList<ChangeSet>()).add(changeSet);
			}
		}

		for (Map.Entry<ChangeSetMetadata, List<ChangeSet>> e : grouper.entrySet()) {
			double bestDelta = Double.MIN_VALUE;
			ChangeSet bestChangeSet = null;

			for (ChangeSet changeSet : e.getValue()) {
				double delta = changeSet.getMetricsToBase().getPnlDelta();
				if (delta > bestDelta) {
					bestDelta = delta;
					bestChangeSet = changeSet;
				}
			}
			if (bestChangeSet != null) {
				setsToInclude.add(bestChangeSet);
			}
		}

	}

	public void toggleFilter() {
		this.filterActive = !filterActive;
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.google.common.base.Objects;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.mmxcore.NamedObject;

/**
 * Class to organise insertion plans and optionally filter out related but "poorer" choices
 *
 */
public class InsertionPlanGrouperAndFilter extends ViewerFilter {
	public static enum GroupMode {
		TargetAndComplexity, // Group by target and then by complexity count
		Target, // Group by Target
		Complexity, // Group by complexity
	}

	private final Set<ChangeSet> setsToInclude = new HashSet<>();
	private boolean filterActive = false;
	private GroupMode groupMode = GroupMode.TargetAndComplexity;
	private String slotId;

	@Override
	public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
		if (!filterActive) {
			return true;
		}
		if (element instanceof ChangeSet) {
			return setsToInclude.contains(element);
		}
		if (parentElement instanceof ChangeSet) {
			return setsToInclude.contains(parentElement);
		}
		if (element instanceof ChangeSetTableGroup) {
			return setsToInclude.contains(((ChangeSetTableGroup) element).getChangeSet());
		}
		if (parentElement instanceof ChangeSetTableGroup) {
			return setsToInclude.contains(((ChangeSetTableGroup) parentElement).getChangeSet());
		}
		return true;
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
		private final GroupMode mode;

		public ChangeSetMetadata(GroupMode mode) {
			this.mode = mode;
		}

		@Override
		public int hashCode() {
			if (mode == GroupMode.Complexity) {
				return changeCount;
			} else if (mode == GroupMode.Target) {
				return sendTo.hashCode();
			} else if (mode == GroupMode.TargetAndComplexity) {
				return Objects.hashCode(changeCount, sendTo);
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(final Object obj) {
			if (obj == this) {
				return true;
			}
			if (obj instanceof ChangeSetMetadata) {
				final ChangeSetMetadata other = (ChangeSetMetadata) obj;
				if (mode == GroupMode.Complexity) {
					return changeCount == other.changeCount;
				} else if (mode == GroupMode.Target) {
					return sendTo == other.sendTo;
				} else if (mode == GroupMode.TargetAndComplexity) {
					return this.changeCount == other.changeCount && this.sendTo == other.sendTo;
				}
				throw new IllegalStateException();
			}
			return false;
		}
	}

	public List<ChangeSet> processChangeSetRoot(final ChangeSetRoot root, final NamedObject target) {
		setsToInclude.clear();

		// Group by change count and target
		final Map<ChangeSetMetadata, List<ChangeSet>> grouper = new LinkedHashMap<>();
		for (final ChangeSet changeSet : root.getChangeSets()) {
			final Collection<ChangeSetRow> changeSetRows = changeSet.getChangeSetRowsToBase();
			int structuralChanges = 0;
			ChangeSetRow targetRow = null;
			ChangeSetRowData targetRowData = null;
			for (final ChangeSetRow row : changeSetRows) {
				if (row.getAfterData() != null) {
					if (!row.getAfterData().getMembers().isEmpty() && (row.isWiringChange() || row.isVesselChange())) {
						++structuralChanges;
					}
					for (ChangeSetRowData d : row.getAfterData().getMembers()) {
						if (d.getLoadSlot() == target || d.getDischargeSlot() == target) {
							assert targetRow == null;
							targetRow = row;
							targetRowData = d;
							break;
						} else if (d.getLhsEvent() instanceof VesselEventVisit) {
							VesselEventVisit vesselEventVisit = (VesselEventVisit) d.getLhsEvent();
							if (vesselEventVisit.getVesselEvent() == target ){
								assert targetRow == null;
								targetRow = row;
								targetRowData = d;
								break;
							} 
						}
					}
				}
			}
			final ChangeSetMetadata key = new ChangeSetMetadata(groupMode);
			key.changeCount = structuralChanges;
			Object sendTo = null;
			if (targetRow != null) {
				if (targetRowData.getLoadSlot() == target) {
					final DischargeSlot dischargeSlot = targetRowData.getDischargeSlot();
					if (dischargeSlot == null) {
						sendTo = "Open";
					} else if (dischargeSlot instanceof SpotSlot) {
						sendTo = ((SpotSlot) dischargeSlot).getMarket().eClass();
					} else if (dischargeSlot.getContract() != null) {
						sendTo = dischargeSlot.getContract();
					} else {
						sendTo = dischargeSlot.getName();
					}

				} else if (targetRowData.getDischargeSlot() == target) {
					final LoadSlot loadSlot = targetRowData.getLoadSlot();
					if (loadSlot == null) {
						sendTo = "Open";
					} else if (loadSlot instanceof SpotSlot) {
						sendTo = ((SpotSlot) loadSlot).getMarket().eClass();
					} else if (loadSlot.getContract() != null) {
						sendTo = loadSlot.getContract();
					} else {
						sendTo = loadSlot.getName();
					}
				}
				if (sendTo == null) {
					final int ii = 0;
				}
				key.sendTo = sendTo;
				grouper.computeIfAbsent(key, k -> new LinkedList<ChangeSet>()).add(changeSet);
			}
		}

		for (final Map.Entry<ChangeSetMetadata, List<ChangeSet>> e : grouper.entrySet()) {
			double bestDelta = Double.MIN_VALUE;
			ChangeSet bestChangeSet = null;
			String dest = "";
			final ChangeSetMetadata m = e.getKey();
			String prefix;
			if (target instanceof LoadSlot) {
				prefix = "Send to";
			} else {
				prefix = "Deliver from";
			}

			if (m.sendTo instanceof Contract) {
				dest = ((Contract) m.sendTo).getName();
			} else if (m.sendTo instanceof Port) {
				dest = "Spot at " + ((Port) m.sendTo).getName();
			} else if (m.sendTo instanceof EClass) {
				final EClass market = (EClass) m.sendTo;
				if (market == SpotMarketsPackage.Literals.FOB_PURCHASES_MARKET) {
					dest = "Spot FOB market";
				} else if (market == SpotMarketsPackage.Literals.DES_PURCHASE_MARKET) {
					dest = "Spot DES market";
				} else if (market == SpotMarketsPackage.Literals.DES_SALES_MARKET) {
					dest = "Spot DES market";
				} else if (market == SpotMarketsPackage.Literals.FOB_SALES_MARKET) {
					dest = "Spot FOB market";
				} else {
					assert false;
				}
			} else if (m.sendTo instanceof String) {
				dest = ((String) m.sendTo);
			} else {
				final int ii = 0;
			}
			{
				double lastDelta = Double.MAX_VALUE;
				final Iterator<ChangeSet> itr = e.getValue().iterator();
				while (itr.hasNext()) {
					final ChangeSet cs = itr.next();
					final double delta = cs.getMetricsToBase().getPnlDelta();
					if (Math.abs(delta - lastDelta) < 10_000.0) {
						itr.remove();
						// lastDelta = delta;
					} else {
						lastDelta = delta;
					}
				}
			}

			int idx = 0;
			for (final ChangeSet changeSet : e.getValue()) {
				switch (groupMode) {
				case TargetAndComplexity: {
					if (idx == 0) {
						changeSet.setDescription(String.format("%s, ∆%d (%d/%d)", dest, m.changeCount, 1 + idx, e.getValue().size()));
					} else {
						changeSet.setDescription(String.format("%s, ∆%d (%d)", dest, m.changeCount, 1 + idx));
					}
					break;
				}
				case Target: {
					if (idx == 0) {
						changeSet.setDescription(String.format("%s, (%d/%d)", dest, 1 + idx, e.getValue().size()));
					} else {
						changeSet.setDescription(String.format("%s, (%d)", dest, 1 + idx));
					}
					break;
				}
				case Complexity: {
					if (idx == 0) {
						changeSet.setDescription(String.format("∆%d (%d/%d)", m.changeCount, 1 + idx, e.getValue().size()));
					} else {
						changeSet.setDescription(String.format("∆%d (%d)", m.changeCount, 1 + idx));
					}
					break;
				}
				default:
					throw new IllegalStateException();
				}
				final double delta = changeSet.getMetricsToBase().getPnlDelta();
				if (delta > bestDelta) {
					bestDelta = delta;
					bestChangeSet = changeSet;
				}
				++idx;
			}
			if (bestChangeSet != null) {
				setsToInclude.add(bestChangeSet);
			}
		}

		// Assume initial data is correctly ordered.
		final List<ChangeSet> reorderedElements = new LinkedList<>();
		for (final Map.Entry<ChangeSetMetadata, List<ChangeSet>> e : grouper.entrySet()) {
			reorderedElements.addAll(e.getValue());
		}
		return reorderedElements;
	}

	public void toggleFilter() {
		this.filterActive = !filterActive;
	}

	public void setFilterActive(final boolean b) {
		this.filterActive = b;
	}

	public void setGroupMode(GroupMode mode) {
		this.groupMode = mode;
	}

	public void setTargetSlot(String slotId) {
		this.slotId = slotId;
	}
}

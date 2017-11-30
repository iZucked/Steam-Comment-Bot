/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;

import com.google.common.base.Objects;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.views.changeset.filter.UserFilter;
import com.mmxlabs.lingo.reports.views.changeset.filter.UserFilter.FilterSlotType;
import com.mmxlabs.lingo.reports.views.changeset.filter.UserFilter.FilterVesselType;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowDataGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.rcp.common.menus.LocalMenuHelper;
import com.mmxlabs.rcp.common.menus.SubLocalMenuHelper;

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

	public final Set<Object> expandedGroups = new HashSet<>();
	// private boolean filterActive = false;
	private GroupMode groupMode = GroupMode.TargetAndComplexity;
	private int maxComplexity = 4;

	private final List<UserFilter> userFilters = new LinkedList<>();

	public Map<Pair<String, UserFilter.FilterSlotType>, Set<UserFilter>> exploreSlotOptions = new HashMap<>();

	public boolean insertionModeActive = false;

	public boolean isMultipleSolutionView() {
		return multipleSolutionView;
	}

	public void setMultipleSolutionView(boolean multipleSolutionView) {
		this.multipleSolutionView = multipleSolutionView;
	}

	private boolean multipleSolutionView = false;

	public boolean isInsertionModeActive() {
		return insertionModeActive;
	}

	public void setInsertionModeActive(boolean insertionModeActive) {
		this.insertionModeActive = insertionModeActive;
	}

	public int getMaxComplexity() {
		return maxComplexity;
	}

	public void setMaxComplexity(int maxComplexity) {
		this.maxComplexity = maxComplexity;
	}

	@Override
	public boolean select(final Viewer viewer, final Object parentElement, Object element) {
		if (element instanceof ChangeSetTableGroup) {
			final ChangeSetTableGroup group = (ChangeSetTableGroup) element;
			for (final UserFilter filter : userFilters) {
				if (!filter.include(group)) {
					return false;
				}
			}
		}
		if (parentElement instanceof ChangeSetTableRow) {
			final ChangeSetTableGroup group = (ChangeSetTableGroup) ((ChangeSetTableRow) parentElement).eContainer();
			for (final UserFilter filter : userFilters) {
				if (!filter.include(group)) {
					return false;
				}
			}
		}

		if (!insertionModeActive) {
			return true;
		}
		if (element instanceof ChangeSetTableGroup) {

			ChangeSetTableGroup changeSetTableGroup = (ChangeSetTableGroup) element;
			if (changeSetTableGroup.getComplexity() > getMaxComplexity()) {
				return false;
			}
		}
		if (parentElement instanceof ChangeSetTableGroup) {
			ChangeSetTableGroup changeSetTableGroup = (ChangeSetTableGroup) parentElement;
			if (changeSetTableGroup.getComplexity() > getMaxComplexity()) {
				return false;
			}
		}
		if (userFilters.isEmpty()) {

			if (element instanceof ChangeSetTableGroup) {

				ChangeSetTableGroup changeSetTableGroup = (ChangeSetTableGroup) element;
				return !changeSetTableGroup.isGroupAlternative() || expandedGroups.contains(changeSetTableGroup.getGroupObject());
			}
			if (parentElement instanceof ChangeSetTableGroup) {
				ChangeSetTableGroup changeSetTableGroup = (ChangeSetTableGroup) parentElement;
				return !changeSetTableGroup.isGroupAlternative() || expandedGroups.contains(changeSetTableGroup.getGroupObject());
			}
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

		public ChangeSetMetadata(final GroupMode mode) {
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
					return this.changeCount == other.changeCount && Objects.equal(this.sendTo, other.sendTo);
				}
				throw new IllegalStateException();
			}
			return false;
		}
	}

	public Consumer<ChangeSetTableRoot> processChangeSetRoot(ChangeSetRoot root, final NamedObject target) {
		// setsToInclude.clear();
		userFilters.clear();
		exploreSlotOptions.clear();

		// Group by change count and target
		for (final ChangeSet changeSet : root.getChangeSets()) {
			final Collection<ChangeSetRow> changeSetRows = changeSet.getChangeSetRowsToBase();

			for (final ChangeSetRow row : changeSetRows) {
				final ChangeSetRowDataGroup afterData = row.getAfterData();
				if (afterData != null) {
					// Create Export records

					// TODO: Checks/filters on spot slots!

					{
						for (final ChangeSetRowData d : afterData.getMembers()) {
							if (!afterData.getMembers().isEmpty() && (row.isWiringChange() || row.isVesselChange())) {

								if (d.getLoadAllocation() != null) {
									if (d.getDischargeAllocation() != null) {
										final Pair<String, UserFilter.FilterSlotType> key = new Pair<>(d.getLhsName(), UserFilter.FilterSlotType.BY_ID);
										if (d.getDischargeAllocation().isSetSpotMarket()) {
											{
												final UserFilter f = new UserFilter(d.getLhsName() + " to " + d.getDischargeAllocation().getSpotMarket().getName());
												f.lhsKey = d.getLhsName();
												f.lhsType = UserFilter.FilterSlotType.BY_ID;
												f.rhsKey = d.getDischargeAllocation().getSpotMarket().getName();
												f.rhsType = UserFilter.FilterSlotType.BY_SPOT_MARKET;
												exploreSlotOptions.computeIfAbsent(key, (k) -> new HashSet<>()).add(f);
											}
										} else {
											{
												final UserFilter f = new UserFilter(d.getLhsName() + " to " + d.getRhsName());
												f.lhsKey = d.getLhsName();
												f.lhsType = UserFilter.FilterSlotType.BY_ID;
												f.rhsKey = d.getRhsName();
												f.rhsType = UserFilter.FilterSlotType.BY_ID;
												exploreSlotOptions.computeIfAbsent(key, (k) -> new HashSet<>()).add(f);
											}
											if (d.getDischargeAllocation().getContract() != null) {
												final UserFilter f = new UserFilter(d.getLhsName() + " to " + d.getDischargeAllocation().getContract().getName());
												f.lhsKey = d.getLhsName();
												f.lhsType = UserFilter.FilterSlotType.BY_ID;
												f.rhsKey = d.getDischargeAllocation().getContract().getName();
												f.rhsType = UserFilter.FilterSlotType.BY_CONTRACT;
												exploreSlotOptions.computeIfAbsent(key, (k) -> new HashSet<>()).add(f);
											}
										}
										if (d.getVesselName() != null) {
											final UserFilter f = new UserFilter(d.getLhsName() + " on " + d.getVesselName());
											f.lhsKey = d.getLhsName();
											f.lhsType = UserFilter.FilterSlotType.BY_ID;
											f.vesselKey = d.getVesselName();
											f.vesselType = UserFilter.FilterVesselType.BY_NAME;
											exploreSlotOptions.computeIfAbsent(key, (k) -> new HashSet<>()).add(f);
										}
									}
								}

								// Discharge side
								if (d.getDischargeAllocation() != null) {
									if (d.getLoadAllocation() != null) {
										final Pair<String, UserFilter.FilterSlotType> key = new Pair<>(d.getRhsName(), UserFilter.FilterSlotType.BY_ID);
										if (d.getLoadAllocation().isSetSpotMarket()) {
											{
												final UserFilter f = new UserFilter(d.getLoadAllocation().getSpotMarket().getName() + " to " + d.getRhsName());
												f.rhsKey = d.getRhsName();
												f.rhsType = UserFilter.FilterSlotType.BY_ID;
												f.lhsKey = d.getLoadAllocation().getSpotMarket().getName();
												f.lhsType = UserFilter.FilterSlotType.BY_SPOT_MARKET;
												exploreSlotOptions.computeIfAbsent(key, (k) -> new HashSet<>()).add(f);
											}
										} else {
											{
												final UserFilter f = new UserFilter(d.getLhsName() + " to " + d.getRhsName());
												f.lhsKey = d.getLhsName();
												f.lhsType = UserFilter.FilterSlotType.BY_ID;
												f.rhsKey = d.getRhsName();
												f.rhsType = UserFilter.FilterSlotType.BY_ID;
												exploreSlotOptions.computeIfAbsent(key, (k) -> new HashSet<>()).add(f);
											}
											if (d.getLoadAllocation().getContract() != null) {
												final UserFilter f = new UserFilter(d.getLoadAllocation().getContract().getName() + " to " + d.getRhsName());
												f.lhsKey = d.getLoadAllocation().getContract().getName();
												f.lhsType = UserFilter.FilterSlotType.BY_CONTRACT;
												f.rhsKey = d.getRhsName();
												f.rhsType = UserFilter.FilterSlotType.BY_ID;
												exploreSlotOptions.computeIfAbsent(key, (k) -> new HashSet<>()).add(f);
											}
										}
										if (d.getVesselName() != null) {
											final UserFilter f = new UserFilter(d.getRhsName() + " on " + d.getVesselName());
											f.rhsKey = d.getRhsName();
											f.rhsType = UserFilter.FilterSlotType.BY_ID;
											f.vesselKey = d.getVesselName();
											f.vesselType = UserFilter.FilterVesselType.BY_NAME;
											exploreSlotOptions.computeIfAbsent(key, (k) -> new HashSet<>()).add(f);
										}
									}
								}
							}
						}
					}

					// if (!afterData.getMembers().isEmpty() && (row.isWiringChange() || row.isVesselChange())) {
					// ++structuralChanges;
					// }
					// for (final ChangeSetRowData d : afterData.getMembers()) {
					// if (d.getLoadSlot() == target || d.getDischargeSlot() == target) {
					// assert targetRow == null;
					// targetRow = row;
					// targetRowData = d;
					// break;
					// } else if (d.getLhsEvent() instanceof VesselEventVisit) {
					// VesselEventVisit vesselEventVisit = (VesselEventVisit) d.getLhsEvent();
					// if (vesselEventVisit.getVesselEvent() == target) {
					// assert targetRow == null;
					// targetRow = row;
					// targetRowData = d;
					// break;
					// }
					// }
					// }
				}
			}
		}

		return (tableRoot) -> {
			final Map<ChangeSetMetadata, List<ChangeSetTableGroup>> grouper = new LinkedHashMap<>();
			for (ChangeSetTableGroup tableGroup : tableRoot.getGroups()) {
				Pair<String, Object> p = getDestination(tableGroup, target);
				String label = p.getFirst();
				ChangeSetMetadata key = new ChangeSetMetadata(groupMode);
				key.sendTo = p.getSecond();
				key.changeCount = tableGroup.getComplexity();
				tableGroup.setDescription(label);
				grouper.computeIfAbsent(key, k -> new LinkedList<ChangeSetTableGroup>()).add(tableGroup);
			}
			for (Map.Entry<ChangeSetMetadata, List<ChangeSetTableGroup>> e : grouper.entrySet()) {
				List<ChangeSetTableGroup> groups = e.getValue();
				Collections.sort(groups, (a, b) -> Integer.compare(b.getDeltaMetrics().getPnlDelta(), a.getDeltaMetrics().getPnlDelta()));
				boolean first = true;
				double sortValue = 0.0;
				for (ChangeSetTableGroup g : groups) {
					if (g.getComplexity() > getMaxComplexity()) {
						continue;
					}
					g.setGroupAlternative(!first);
					if (first) {
						sortValue = g.getDeltaMetrics().getPnlDelta();
						first = false;
					}
					g.setGroupSortValue(sortValue);
					g.setGroupObject(e.getKey());
				}
			}
		};

	}

	public void setGroupMode(final GroupMode mode) {
		this.groupMode = mode;
	}

	public void mergeFilter(final UserFilter filter) {
		if (!userFilters.contains(filter)) {
			userFilters.add(filter);
		}
	}

	public void clearFilter() {
		userFilters.clear();
	}

	public List<UserFilter> getUserFilters() {
		return userFilters;
	}

	public boolean generateMenus(final LocalMenuHelper helper, final GridTreeViewer viewer, final Set<ChangeSetTableRow> directSelectedRows, final Set<ChangeSetTableGroup> selectedSets,
			@Nullable Object targetElement) {
		if (insertionModeActive) {

			if (helper.hasActions()) {
				helper.addSeparator();
			}

			if (directSelectedRows.size() == 0) {
				generateInsertionSubMenus_ExploreAll(helper, viewer);

			}
			if (selectedSets.size() > 0 && directSelectedRows.size() == 0) {
				generateInsertionSubMenus_FilterSets(helper, viewer, selectedSets, targetElement);
			}
			if (directSelectedRows.size() == 1) {
				generateInsertionSubMenus_Explore(helper, viewer, directSelectedRows);
				generateInsertionSubMenus_FilterOn(helper, viewer, false, directSelectedRows);
				generateInsertionSubMenus_FilterOn(helper, viewer, true, directSelectedRows);
			}
			if (getUserFilters().size() > 0) {
				final SubLocalMenuHelper remove = new SubLocalMenuHelper("Remove filter...");
				if (getUserFilters().size() > 1) {
					remove.addAction(new RunnableAction("All filters", () -> {
						clearFilter();
						ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());
					}));
				}
				for (final UserFilter f : getUserFilters()) {
					remove.addAction(new RunnableAction(f.getLabel(), () -> {
						getUserFilters().remove(f);
						ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());
					}));
				}
				helper.addSubMenu(remove);
			}
			return true;
		}
		return false;
	}

	private void generateInsertionSubMenus_FilterOn(final LocalMenuHelper helper, final GridTreeViewer viewer, boolean exclude, final Set<ChangeSetTableRow> directSelectedRows) {

		final boolean showLHSActions = true;
		final boolean showRHSActions = true;

		final SubLocalMenuHelper showFromMenu;
		if (exclude) {
			showFromMenu = new SubLocalMenuHelper("Exclude related...");
		} else {
			showFromMenu = new SubLocalMenuHelper("Show related...");
		}
		helper.addSubMenu(showFromMenu);

		{
			final ChangeSetTableRow row = directSelectedRows.iterator().next();
			if (showLHSActions && row.isLhsSlot()) {
				final LoadSlot slot = row.getLhsAfter() != null ? row.getLhsAfter().getLoadSlot() : null;
				if (slot != null) {
					if (row.isRhsSlot()) {
						final Slot discharge = row.getRhsAfter() != null ? row.getRhsAfter().getDischargeSlot() : null;
						{
							final String label = row.getLhsName() + " to " + row.getRhsName();
							showFromMenu.addAction(new RunnableAction(label, () -> {
								final UserFilter f = new UserFilter(label);
								f.lhsKey = row.getLhsName();
								f.lhsType = FilterSlotType.BY_ID;
								f.rhsKey = row.getRhsName();
								f.rhsType = FilterSlotType.BY_ID;
								f.rhsNegate = exclude;
								mergeFilter(f);
								ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());

							}));
						}
						final Contract contract = discharge.getContract();
						if (contract != null) {
							final String label = row.getLhsName() + " to " + contract.getName();
							showFromMenu.addAction(new RunnableAction(label, () -> {
								final UserFilter f = new UserFilter(label);
								f.lhsKey = row.getLhsName();
								f.lhsType = FilterSlotType.BY_ID;
								f.rhsKey = contract.getName();
								f.rhsType = FilterSlotType.BY_CONTRACT;
								f.rhsNegate = exclude;
								mergeFilter(f);
								ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());

							}));
						}
						if (discharge instanceof SpotSlot) {
							final SpotSlot spotSlot = (SpotSlot) discharge;
							final SpotMarket market = spotSlot.getMarket();
							if (market != null) {
								final String label1 = row.getLhsName() + " to spot";
								showFromMenu.addAction(new RunnableAction(label1, () -> {
									final UserFilter f = new UserFilter(label1);
									f.lhsKey = row.getLhsName();
									f.lhsType = FilterSlotType.BY_ID;
									f.rhsKey = null;
									f.rhsType = FilterSlotType.BY_SPOT_MARKET;
									f.rhsNegate = exclude;
									mergeFilter(f);
									ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());

								}));
								final String label2 = row.getLhsName() + " to " + market.getName();
								showFromMenu.addAction(new RunnableAction(label2, () -> {
									final UserFilter f = new UserFilter(label2);
									f.lhsKey = row.getLhsName();
									f.lhsType = FilterSlotType.BY_ID;
									f.rhsKey = market.getName();
									f.rhsType = FilterSlotType.BY_SPOT_MARKET;
									f.rhsNegate = exclude;
									mergeFilter(f);
									ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());

								}));
							}
						}

					} else {
						final String label = row.getLhsName() + " to open";
						showFromMenu.addAction(new RunnableAction(label, () -> {
							final UserFilter f = new UserFilter(label);
							f.lhsKey = row.getLhsName();
							f.lhsType = FilterSlotType.BY_ID;
							f.rhsType = FilterSlotType.BY_OPEN;
							f.rhsNegate = exclude;
							mergeFilter(f);
							ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());
						}));
					}
				}
				if (row.getAfterVesselName() != null && !row.getAfterVesselName().isEmpty()) {
					final String label = row.getLhsName() + " on " + row.getAfterVesselName();
					showFromMenu.addAction(new RunnableAction(label, () -> {
						final UserFilter f = new UserFilter(label);
						f.lhsKey = row.getLhsName();
						f.lhsType = FilterSlotType.BY_ID;
						f.vesselType = FilterVesselType.BY_NAME;
						f.vesselKey = row.getAfterVesselName();
						f.vesselNegate = exclude;
						mergeFilter(f);
						ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());
					}));
				}
			}
			if (showRHSActions && row.isRhsSlot()) {
				final Slot slot = row.getRhsAfter() != null ? row.getRhsAfter().getDischargeSlot() : null;
				if (slot != null) {

					if (showFromMenu.hasActions()) {
						showFromMenu.addSeparator();
					}

					if (row.isLhsSlot()) {
						final Slot load = row.getLhsAfter() != null ? row.getLhsAfter().getLoadSlot() : null;
						if (!showLHSActions) {
							final String label = row.getLhsName() + " to " + row.getRhsName();
							showFromMenu.addAction(new RunnableAction(label, () -> {
								final UserFilter f = new UserFilter(label);
								f.lhsKey = row.getLhsName();
								f.lhsType = FilterSlotType.BY_ID;
								f.rhsKey = row.getRhsName();
								f.rhsType = FilterSlotType.BY_ID;
								f.lhsNegate = exclude;
								mergeFilter(f);
								ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());
							}));
						}
						final Contract contract = load.getContract();
						if (contract != null) {
							final String label = contract.getName() + " to " + row.getRhsName();
							showFromMenu.addAction(new RunnableAction(label, () -> {
								final UserFilter f = new UserFilter(label);
								f.lhsKey = contract.getName();
								f.lhsType = FilterSlotType.BY_CONTRACT;
								f.rhsKey = row.getRhsName();
								f.rhsType = FilterSlotType.BY_ID;
								f.lhsNegate = exclude;
								mergeFilter(f);
								ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());
							}));
						}
						if (load instanceof SpotSlot) {
							final SpotSlot spotSlot = (SpotSlot) load;
							final SpotMarket market = spotSlot.getMarket();
							if (market != null) {
								final String label1 = "Spot to " + row.getRhsName();
								showFromMenu.addAction(new RunnableAction(label1, () -> {
									final UserFilter f = new UserFilter(label1);
									f.lhsKey = null;
									f.lhsType = FilterSlotType.BY_SPOT_MARKET;
									f.rhsKey = row.getRhsName();
									f.rhsType = FilterSlotType.BY_ID;
									f.lhsNegate = exclude;
									mergeFilter(f);
									ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());
								}));
								final String label2 = market.getName() + " to " + row.getRhsName();
								showFromMenu.addAction(new RunnableAction(label2, () -> {
									final UserFilter f = new UserFilter(label2);
									f.lhsKey = market.getName();
									f.lhsType = FilterSlotType.BY_SPOT_MARKET;
									f.rhsKey = row.getRhsName();
									f.rhsType = FilterSlotType.BY_ID;
									f.lhsNegate = exclude;
									mergeFilter(f);
									ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());

								}));
							}
						}

					} else {
						final String label = "Open to " + row.getRhsName();
						showFromMenu.addAction(new RunnableAction(label, () -> {
							final UserFilter f = new UserFilter(label);
							f.lhsType = FilterSlotType.BY_OPEN;
							f.rhsKey = row.getRhsName();
							f.rhsType = FilterSlotType.BY_ID;
							f.lhsNegate = exclude;
							mergeFilter(f);
							ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());
						}));
					}
				}
				if (row.getAfterVesselName() != null && !row.getAfterVesselName().isEmpty()) {

					if (showFromMenu.hasActions()) {
						showFromMenu.addSeparator();
					}

					{

						final String label = row.getRhsName() + " on " + row.getAfterVesselName();
						showFromMenu.addAction(new RunnableAction(label, () -> {
							final UserFilter f = new UserFilter(label);
							f.rhsKey = row.getRhsName();
							f.rhsType = FilterSlotType.BY_ID;
							f.vesselType = FilterVesselType.BY_NAME;
							f.vesselKey = row.getAfterVesselName();
							f.vesselNegate = exclude;
							mergeFilter(f);
							ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());
						}));
					}

				}
			}
		}
	}

	private void generateInsertionSubMenus_FilterSets(final LocalMenuHelper helper, final GridTreeViewer viewer, final Set<ChangeSetTableGroup> selectedSets, @Nullable Object targetElement) {

		final SubLocalMenuHelper showFromMenu = new SubLocalMenuHelper("Show all...");
		helper.addSubMenu(showFromMenu);
		{

			final ChangeSetTableGroup group = selectedSets.iterator().next();
			for (ChangeSetTableRow row : group.getRows()) {
				if (row.getLhsAfter() != null && row.getLhsAfter().getLoadSlot() == targetElement) {
					if (row.isRhsSlot()) {
						final Slot discharge = row.getRhsAfter() != null ? row.getRhsAfter().getDischargeSlot() : null;
						final Contract contract = discharge.getContract();

						if (discharge instanceof SpotSlot) {
							final SpotSlot spotSlot = (SpotSlot) discharge;
							final SpotMarket market = spotSlot.getMarket();
							if (market != null) {
								final String label1 = row.getLhsName() + " to spot";
								showFromMenu.addAction(new RunnableAction(label1, () -> {
									final UserFilter f = new UserFilter(label1);
									f.lhsKey = row.getLhsName();
									f.lhsType = FilterSlotType.BY_ID;
									f.rhsKey = null;
									f.rhsType = FilterSlotType.BY_SPOT_MARKET;
									mergeFilter(f);
									ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());

								}));
								final String label2 = row.getLhsName() + " to " + market.getName();
								showFromMenu.addAction(new RunnableAction(label2, () -> {
									final UserFilter f = new UserFilter(label2);
									f.lhsKey = row.getLhsName();
									f.lhsType = FilterSlotType.BY_ID;
									f.rhsKey = market.getName();
									f.rhsType = FilterSlotType.BY_SPOT_MARKET;
									mergeFilter(f);
									ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());

								}));
							}
						} else if (contract != null) {
							final String label = row.getLhsName() + " to " + contract.getName();
							showFromMenu.addAction(new RunnableAction(label, () -> {
								final UserFilter f = new UserFilter(label);
								f.lhsKey = row.getLhsName();
								f.lhsType = FilterSlotType.BY_ID;
								f.rhsKey = contract.getName();
								f.rhsType = FilterSlotType.BY_CONTRACT;
								mergeFilter(f);
								ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());

							}));
						} else {
							final String label = row.getLhsName() + " to " + row.getRhsName();
							showFromMenu.addAction(new RunnableAction(label, () -> {
								final UserFilter f = new UserFilter(label);
								f.lhsKey = row.getLhsName();
								f.lhsType = FilterSlotType.BY_ID;
								f.rhsKey = row.getRhsName();
								f.rhsType = FilterSlotType.BY_ID;
								mergeFilter(f);
								ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());

							}));
						}

					} else {
						final String label = row.getLhsName() + " to open";
						showFromMenu.addAction(new RunnableAction(label, () -> {
							final UserFilter f = new UserFilter(label);
							f.lhsKey = row.getLhsName();
							f.lhsType = FilterSlotType.BY_ID;
							f.rhsType = FilterSlotType.BY_OPEN;
							mergeFilter(f);
							ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());
						}));
					}
				} else if (row.getRhsAfter() != null && row.getRhsAfter().getDischargeSlot() == targetElement) {
					int ii = 0;
					// final Slot slot = row.getRhsAfter() != null ? row.getRhsAfter().getDischargeSlot() : null;
					// if (slot != null) {

					if (row.isLhsSlot()) {
						final Slot load = row.getLhsAfter() != null ? row.getLhsAfter().getLoadSlot() : null;
						final Contract contract = load.getContract();

						if (load instanceof SpotSlot) {
							final SpotSlot spotSlot = (SpotSlot) load;
							final SpotMarket market = spotSlot.getMarket();
							if (market != null) {
								final String label1 = "Spot to " + row.getRhsName();
								showFromMenu.addAction(new RunnableAction(label1, () -> {
									final UserFilter f = new UserFilter(label1);
									f.lhsKey = null;
									f.lhsType = FilterSlotType.BY_SPOT_MARKET;
									f.rhsKey = row.getRhsName();
									f.rhsType = FilterSlotType.BY_ID;
									mergeFilter(f);
									ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());
								}));
								final String label2 = market.getName() + " to " + row.getRhsName();
								showFromMenu.addAction(new RunnableAction(label2, () -> {
									final UserFilter f = new UserFilter(label2);
									f.lhsKey = market.getName();
									f.lhsType = FilterSlotType.BY_SPOT_MARKET;
									f.rhsKey = row.getRhsName();
									f.rhsType = FilterSlotType.BY_ID;
									mergeFilter(f);
									ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());

								}));
							}
						} else if (contract != null) {
							final String label = contract.getName() + " to " + row.getRhsName();
							showFromMenu.addAction(new RunnableAction(label, () -> {
								final UserFilter f = new UserFilter(label);
								f.lhsKey = contract.getName();
								f.lhsType = FilterSlotType.BY_CONTRACT;
								f.rhsKey = row.getRhsName();
								f.rhsType = FilterSlotType.BY_ID;
								mergeFilter(f);
								ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());
							}));
						} else {

							final String label = load.getName() + " to " + row.getRhsName();
							showFromMenu.addAction(new RunnableAction(label, () -> {
								final UserFilter f = new UserFilter(label);
								f.lhsKey = row.getLhsName();
								f.lhsType = FilterSlotType.BY_ID;
								f.rhsKey = row.getRhsName();
								f.rhsType = FilterSlotType.BY_ID;
								mergeFilter(f);
								ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());
							}));
						}

					} else {
						final String label = "Open to " + row.getRhsName();
						showFromMenu.addAction(new RunnableAction(label, () -> {
							final UserFilter f = new UserFilter(label);
							f.lhsType = FilterSlotType.BY_OPEN;
							f.rhsKey = row.getRhsName();
							f.rhsType = FilterSlotType.BY_ID;
							mergeFilter(f);
							ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());
						}));
					}

				}
			}

		}
	}

	private void generateInsertionSubMenus_Explore(final LocalMenuHelper helper, final GridTreeViewer viewer, final Set<ChangeSetTableRow> directSelectedRows) {

		final boolean showLHSActions = true;
		final boolean showRHSActions = true;

		{
			// TODO: Make this delayed?
			final SubLocalMenuHelper showFromMenu = new SubLocalMenuHelper("Explore related...");
			helper.addSubMenu(showFromMenu);
			{
				final ChangeSetTableRow row = directSelectedRows.iterator().next();
				if (showLHSActions && row.isLhsSlot()) {

					final SubLocalMenuHelper loadMenuContract = new SubLocalMenuHelper(row.getLhsName() + " by contract");
					final SubLocalMenuHelper loadMenuSlot = new SubLocalMenuHelper(row.getLhsName() + " by slot");
					final SubLocalMenuHelper loadMenuMarket = new SubLocalMenuHelper(row.getLhsName() + " by spot market");
					final SubLocalMenuHelper loadMenuVessel = new SubLocalMenuHelper(row.getLhsName() + " by vessel");

					final Pair<String, UserFilter.FilterSlotType> key = new Pair<>(row.getLhsName(), UserFilter.FilterSlotType.BY_ID);
					final Collection<UserFilter> filters = exploreSlotOptions.get(key);
					if (filters != null) {
						for (final UserFilter f : filters) {

							SubLocalMenuHelper menu = null;
							if (f.vesselType == FilterVesselType.BY_NAME) {
								menu = loadMenuVessel;
							} else if (f.rhsType == FilterSlotType.BY_ID) {
								menu = loadMenuSlot;
							} else if (f.rhsType == FilterSlotType.BY_CONTRACT) {
								menu = loadMenuContract;
							} else if (f.rhsType == FilterSlotType.BY_SPOT_MARKET) {
								menu = loadMenuMarket;
							}
							if (menu != null) {
								menu.addAction(new RunnableAction(f.getLabel(), () -> {
									// clearFilter();
									mergeFilter(f);
									ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());
								}));
							}
						}
					}
					if (loadMenuContract.hasActions()) {
						showFromMenu.addSubMenu(loadMenuContract);
					}
					if (loadMenuSlot.hasActions()) {
						showFromMenu.addSubMenu(loadMenuSlot);
					}
					if (loadMenuMarket.hasActions()) {
						showFromMenu.addSubMenu(loadMenuMarket);
					}
					if (loadMenuVessel.hasActions()) {
						showFromMenu.addSubMenu(loadMenuVessel);
					}
				}
				if (showRHSActions && row.isRhsSlot()) {
					if (showFromMenu.hasActions()) {
						showFromMenu.addSeparator();
					}

					final SubLocalMenuHelper dischargeMenuContract = new SubLocalMenuHelper(row.getRhsName() + " by contract");
					final SubLocalMenuHelper dischargeMenuSlot = new SubLocalMenuHelper(row.getRhsName() + " by slot");
					final SubLocalMenuHelper dischargeMenuMarket = new SubLocalMenuHelper(row.getRhsName() + " by spot market");
					final SubLocalMenuHelper dischargeMenuVessel = new SubLocalMenuHelper(row.getRhsName() + " by vessel");
					final Pair<String, UserFilter.FilterSlotType> key = new Pair<>(row.getRhsName(), UserFilter.FilterSlotType.BY_ID);
					final Collection<UserFilter> filters = exploreSlotOptions.get(key);
					if (filters != null) {
						for (final UserFilter f : filters) {

							SubLocalMenuHelper menu = null;
							if (f.vesselType == FilterVesselType.BY_NAME) {
								menu = dischargeMenuVessel;
							} else if (f.lhsType == FilterSlotType.BY_ID) {
								menu = dischargeMenuSlot;
							} else if (f.lhsType == FilterSlotType.BY_CONTRACT) {
								menu = dischargeMenuContract;
							} else if (f.lhsType == FilterSlotType.BY_SPOT_MARKET) {
								menu = dischargeMenuMarket;
							}
							if (menu != null) {
								menu.addAction(new RunnableAction(f.getLabel(), () -> {
									// clearFilter();
									mergeFilter(f);
									ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());
								}));
							}
						}
					}

					if (dischargeMenuContract.hasActions()) {
						showFromMenu.addSubMenu(dischargeMenuContract);
					}
					if (dischargeMenuSlot.hasActions()) {
						showFromMenu.addSubMenu(dischargeMenuSlot);
					}
					if (dischargeMenuMarket.hasActions()) {
						showFromMenu.addSubMenu(dischargeMenuMarket);
					}
					if (dischargeMenuVessel.hasActions()) {
						showFromMenu.addSubMenu(dischargeMenuVessel);
					}
				}
			}
		}
	}

	private void generateInsertionSubMenus_ExploreAll(final LocalMenuHelper helper, final GridTreeViewer viewer) {

		{
			// TODO: Make this delayed?
			final SubLocalMenuHelper showFromMenuParent = new SubLocalMenuHelper("Explore all...");
			helper.addSubMenu(showFromMenuParent);
			// {
			List<Pair<String, SubLocalMenuHelper>> items = new ArrayList<>(exploreSlotOptions.size());

			for (Pair<String, UserFilter.FilterSlotType> key : exploreSlotOptions.keySet()) {
				if (key.getSecond() != UserFilter.FilterSlotType.BY_ID) {
					continue;
				}
				String lhsName = key.getFirst();
				final SubLocalMenuHelper showFromMenu = new SubLocalMenuHelper(lhsName + "...");

				final SubLocalMenuHelper loadMenuContract = new SubLocalMenuHelper("by contract");
				final SubLocalMenuHelper loadMenuSlot = new SubLocalMenuHelper("by slot");
				final SubLocalMenuHelper loadMenuMarket = new SubLocalMenuHelper("by spot market");
				final SubLocalMenuHelper loadMenuVessel = new SubLocalMenuHelper("by vessel");

				// final Pair<String, UserFilter.FilterSlotType> key = new Pair<>(row.getLhsName(), UserFilter.FilterSlotType.BY_ID);

				final Collection<UserFilter> filters = exploreSlotOptions.get(key);
				if (filters != null) {
					for (final UserFilter f : filters) {

						SubLocalMenuHelper menu = null;
						if (f.vesselType == FilterVesselType.BY_NAME) {
							menu = loadMenuVessel;
						} else if (f.rhsType == FilterSlotType.BY_ID) {
							menu = loadMenuSlot;
						} else if (f.rhsType == FilterSlotType.BY_CONTRACT) {
							menu = loadMenuContract;
						} else if (f.rhsType == FilterSlotType.BY_SPOT_MARKET) {
							menu = loadMenuMarket;
						}
						if (menu != null) {
							menu.addAction(new RunnableAction(f.getLabel(), () -> {
								// clearFilter();
								mergeFilter(f);
								ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());
							}));
						}
					}
				}
				boolean addToParent = false;
				if (loadMenuContract.hasActions()) {
					showFromMenu.addSubMenu(loadMenuContract);
					addToParent = true;
				}
				if (loadMenuSlot.hasActions()) {
					showFromMenu.addSubMenu(loadMenuSlot);
					addToParent = true;
				}
				if (loadMenuMarket.hasActions()) {
					showFromMenu.addSubMenu(loadMenuMarket);
					addToParent = true;
				}
				if (loadMenuVessel.hasActions()) {
					showFromMenu.addSubMenu(loadMenuVessel);
					addToParent = true;
				}
				if (addToParent) {
					items.add(new Pair<>(lhsName, showFromMenu));

				}
			}

			items.sort((a, b) -> a.getFirst().compareTo(b.getFirst()));
			if (items.size() > 15) {
				int counter = 0;
				SubLocalMenuHelper m = new SubLocalMenuHelper("");
				String firstEntry = null;
				String lastEntry = null;
				for (final Pair<String, SubLocalMenuHelper> p : items) {

					m.addSubMenu(p.getSecond());
					counter++;
					if (firstEntry == null) {
						firstEntry = p.getFirst();
					}
					lastEntry = p.getFirst();

					if (counter == 15) {
						final String title = String.format("%s ... %s", firstEntry, lastEntry);
						m.setTitle(title);
						showFromMenuParent.addSubMenu(m);
						m = new SubLocalMenuHelper("");
						counter = 0;
						firstEntry = null;
						lastEntry = null;
					}
				}
				if (counter > 0) {
					final String title = String.format("%s ... %s", firstEntry, lastEntry);
					m.setTitle(title);
					showFromMenuParent.addSubMenu(m);
				}

			} else {
				items.forEach(p -> showFromMenuParent.addSubMenu(p.getSecond()));
			}
			// {

			//
			// if (showFromMenu.hasActions()) {
			// showFromMenu.addSeparator();
			// }
			// rhsName
			// final SubLocalMenuHelper dischargeMenuContract = new SubLocalMenuHelper(rhsName+ " by contract");
			// final SubLocalMenuHelper dischargeMenuSlot = new SubLocalMenuHelper(rhsName + " by slot");
			// final SubLocalMenuHelper dischargeMenuMarket = new SubLocalMenuHelper(rhsName + " by spot market");
			// final SubLocalMenuHelper dischargeMenuVessel = new SubLocalMenuHelper(rhsName + " by vessel");
			// final Pair<String, UserFilter.FilterSlotType> key = new Pair<>(rhsName, UserFilter.FilterSlotType.BY_ID);
			// final Collection<UserFilter> filters = exploreSlotOptions.get(key);
			// if (filters != null) {
			// for (final UserFilter f : filters) {
			//
			// SubLocalMenuHelper menu = null;
			// if (f.vesselType == FilterVesselType.BY_NAME) {
			// menu = dischargeMenuVessel;
			// } else if (f.lhsType == FilterSlotType.BY_ID) {
			// menu = dischargeMenuSlot;
			// } else if (f.lhsType == FilterSlotType.BY_CONTRACT) {
			// menu = dischargeMenuContract;
			// } else if (f.lhsType == FilterSlotType.BY_SPOT_MARKET) {
			// menu = dischargeMenuMarket;
			// }
			// if (menu != null) {
			// menu.addAction(new RunnableAction(f.label, () -> {
			// clearFilter();
			// mergeFilter(f);
			// ViewerHelper.refreshThen(viewer, true, () -> viewer.expandAll());
			// }));
			// }
			// }
			// }

			// if (dischargeMenuContract.hasActions()) {
			// showFromMenu.addSubMenu(dischargeMenuContract);
			// }
			// if (dischargeMenuSlot.hasActions()) {
			// showFromMenu.addSubMenu(dischargeMenuSlot);
			// }
			// if (dischargeMenuMarket.hasActions()) {
			// showFromMenu.addSubMenu(dischargeMenuMarket);
			// }
			// if (dischargeMenuVessel.hasActions()) {
			// showFromMenu.addSubMenu(dischargeMenuVessel);
			// }
			// }
		}
		// }
	}

	public @NonNull BiFunction<ChangeSetTableGroup, Integer, String> createLabelProvider() {
		@NonNull
		BiFunction<ChangeSetTableGroup, Integer, String> defaultLabelProvider = ChangeSetViewColumnHelper.getDefaultLabelProvider();
		return (changeSetTableGroup, index) -> {
			if (!insertionModeActive) {
				if (!multipleSolutionView) {
					return defaultLabelProvider.apply(changeSetTableGroup, index);
				} else {
					return ChangeSetViewColumnHelper.getMultipleSolutionLabelProvider().apply(changeSetTableGroup, index);
				}
			}

			String dest = changeSetTableGroup.getDescription();
			int complexity = changeSetTableGroup.getComplexity();
			final String base;
			switch (groupMode) {
			case Complexity:
				base = String.format("∆%d ",  complexity);
				break;
			case Target:
				base = String.format("%s ", dest);
				break;
			case TargetAndComplexity:
				base = String.format("%s, ∆%d ", dest, complexity);
				break;
			default:
				throw new IllegalStateException();
			}
			if (userFilters.isEmpty()) {
				if (expandedGroups.contains(changeSetTableGroup.getGroupObject())) {
					if (changeSetTableGroup.isGroupAlternative()) {

					} else {

					}

				} else {
					return base + " (+)";
				}
			}
			return base;
		};

	}

	private Pair<String, Object> getDestination(ChangeSetTableGroup tableGroup, Object target) {
		ChangeSet changeSet = tableGroup.getChangeSet();
		Object sendTo = null;
		for (ChangeSetRow row : changeSet.getChangeSetRowsToPrevious()) {
			final ChangeSetRowDataGroup afterData = row.getAfterData();
			if (afterData == null) {
				continue;
			}
			for (ChangeSetRowData rowData : afterData.getMembers()) {
				if (rowData.getLoadSlot() == target) {
					final DischargeSlot dischargeSlot = rowData.getDischargeSlot();
					if (dischargeSlot == null) {
						sendTo = "Open";
					} else if (dischargeSlot instanceof SpotSlot) {
						sendTo = ((SpotSlot) dischargeSlot).getMarket().eClass();
					} else if (dischargeSlot.getContract() != null) {
						sendTo = dischargeSlot.getContract();
					} else {
						sendTo = dischargeSlot.getName();
					}

				} else if (rowData.getDischargeSlot() == target) {
					final LoadSlot loadSlot = rowData.getLoadSlot();
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
				if (sendTo != null) {
					break;
				}
			}
		}
		if (sendTo == null) {
			return new Pair<>("?", null);
		}
		if (sendTo instanceof String) {
			return new Pair<>((String) sendTo, sendTo);
		}
		String dest;
		if (sendTo instanceof Contract) {
			dest = ((Contract) sendTo).getName();
		} else if (sendTo instanceof Port) {
			dest = "Spot at " + ((Port) sendTo).getName();
		} else if (sendTo instanceof EClass) {
			final EClass market = (EClass) sendTo;
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
				throw new IllegalStateException();
			}
		} else {
			dest = sendTo.toString();
		}
		return new Pair<>(dest, sendTo);
	}

	public GroupMode getGroupMode() {
		return groupMode;
	}
}

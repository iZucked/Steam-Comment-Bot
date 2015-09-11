/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.joda.time.LocalDate;

import com.google.common.base.Objects;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics;
import com.mmxlabs.lingo.reports.views.changeset.model.Metrics;
import com.mmxlabs.lingo.reports.views.schedule.EquivalanceGroupBuilder;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;

public final class ChangeSetTransformerUtil {
	public static void createOrUpdateRow(@NonNull final Map<String, ChangeSetRow> lhsRowMap, @NonNull final Map<String, ChangeSetRow> rhsRowMap, @NonNull final List<ChangeSetRow> rows,
			@NonNull final EObject element, final boolean isBase) {

		if (element instanceof SlotVisit) {
			final SlotVisit slotVisit = (SlotVisit) element;
			if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) slotVisit.getSlotAllocation().getSlot();
				assert loadSlot != null;
				createOrUpdateSlotVisitRow(lhsRowMap, rhsRowMap, rows, slotVisit, loadSlot, isBase);
			}
		} else if (element instanceof OpenSlotAllocation) {
			final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) element;
			createOrUpdateOpenSlotAllocationRow(lhsRowMap, rhsRowMap, rows, openSlotAllocation, isBase);
		} else if (element instanceof Event) {
			final Event event = (Event) element;
			createOrUpdateEventRow(lhsRowMap, rhsRowMap, rows, event, isBase);
		}
	}

	public static void createOrUpdateSlotVisitRow(@NonNull final Map<String, ChangeSetRow> lhsRowMap, @NonNull final Map<String, ChangeSetRow> rhsRowMap, @NonNull final List<ChangeSetRow> rows,
			@NonNull final SlotVisit slotVisit, @NonNull final LoadSlot loadSlot, final boolean isBase) {

		final ChangeSetRow row;
		{
			final String rowKey = getKeyName(loadSlot);
			if (lhsRowMap.containsKey(rowKey)) {
				row = lhsRowMap.get(rowKey);
			} else {
				String rowName = getRowName(loadSlot);
				// String rowName = getRowName(loadSlot);
				if (loadSlot instanceof SpotSlot) {
					if (lhsRowMap.containsKey("market-" + loadSlot.getName())) {
						row = lhsRowMap.get("market-" + loadSlot.getName());
					} else {
						row = ChangesetFactory.eINSTANCE.createChangeSetRow();
						rows.add(row);
						row.setLhsName(rowName);
						row.setLoadSlot(loadSlot);
						lhsRowMap.put(rowKey, row);
						lhsRowMap.put("market-" + loadSlot.getName(), row);
					}
				} else {

					row = ChangesetFactory.eINSTANCE.createChangeSetRow();
					rows.add(row);
					row.setLhsName(rowName);
					row.setLoadSlot(loadSlot);
					lhsRowMap.put(rowKey, row);
				}
			}
		}

		final CargoAllocation cargoAllocation = slotVisit.getSlotAllocation().getCargoAllocation();
		if (cargoAllocation.getSlotAllocations().size() != 2) {
			throw new RuntimeException("Complex cargoes are not supported");
		}
		if (isBase) {
			row.setNewGroupProfitAndLoss(cargoAllocation);
			row.setNewEventGrouping(cargoAllocation);
			row.setLhsVesselName(getName(slotVisit.getSequence()));
			row.setNewLoadAllocation(slotVisit.getSlotAllocation());
		} else {
			row.setOriginalGroupProfitAndLoss(cargoAllocation);
			row.setOriginalEventGrouping(cargoAllocation);
			row.setOriginalLoadAllocation(slotVisit.getSlotAllocation());
			row.setRhsVesselName(getName(slotVisit.getSequence()));
		}
		// Get discharge data
		for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
			if (slotAllocation == slotVisit.getSlotAllocation()) {
				continue;
			}

			if (isBase) {
				row.setRhsName(getRowName(slotAllocation.getSlot()));
				row.setNewDischargeAllocation(slotAllocation);
				row.setDischargeSlot((DischargeSlot) slotAllocation.getSlot());
				// FIXME: This can replace an existing entry -- is this ok?
				rhsRowMap.put(getKeyName(slotAllocation.getSlot()), row);
				{
					// String rowName = getRowName(slotAllocation.getSlot());
					if (slotAllocation.getSlot() instanceof SpotSlot) {
						rhsRowMap.put("market-" + slotAllocation.getSlot().getName(), row);
					}
				}
			} else {
				row.setOriginalDischargeAllocation(slotAllocation);
			}
			if (!isBase) {
				if (slotAllocation.getSlot() != null) {
					ChangeSetRow otherRow = rhsRowMap.get(getKeyName(slotAllocation.getSlot()));

					if (otherRow == null) {
						// String rowName = getRowName(slotAllocation.getSlot());
						// if (loadSlot instanceof SpotSlot) {
						if (rhsRowMap.containsKey("market-" + slotAllocation.getSlot().getName())) {
							otherRow = rhsRowMap.get("market-" + slotAllocation.getSlot().getName());
						} else {
							// row = ChangesetFactory.eINSTANCE.createChangeSetRow();
							// rows.add(row);
							// row.setLhsName(rowName);
							// row.setLoadSlot(loadSlot);
							// lhsRowMap.put(rowKey, row);
							// lhsRowMap.put(row.getLhsName(), row);
							// }
							// } else
							//

							// Special case, a spot slot will have no "OpenSlotAllocation" to pair up to, so create a new row here.
							assert slotAllocation.getSlot() instanceof SpotSlot;
							otherRow = ChangesetFactory.eINSTANCE.createChangeSetRow();
							rows.add(otherRow);
							rhsRowMap.put(getKeyName(slotAllocation.getSlot()), otherRow);
							rhsRowMap.put("market-" + slotAllocation.getSlot().getName(), otherRow);
							otherRow.setRhsName(getRowName(slotAllocation.getSlot()));
						}
						otherRow.setOriginalDischargeAllocation(slotAllocation);
						otherRow.setDischargeSlot((DischargeSlot) slotAllocation.getSlot());
						// otherRow.setWiringChange(true);
					}
					if (row != otherRow) {
						row.setRhsWiringLink(otherRow);
					}
				}
			}
		}
	}

	public static void createOrUpdateOpenSlotAllocationRow(@NonNull final Map<String, ChangeSetRow> lhsRowMap, @NonNull final Map<String, ChangeSetRow> rhsRowMap,
			@NonNull final List<ChangeSetRow> rows, @NonNull final OpenSlotAllocation openSlotAllocation, final boolean isBase) {
		final Slot slot = openSlotAllocation.getSlot();

		if (slot instanceof LoadSlot) {
			final LoadSlot loadSlot = (LoadSlot) slot;
			final ChangeSetRow row;
			{
				final String rowKey = getKeyName(loadSlot);
				if (lhsRowMap.containsKey(rowKey)) {
					row = lhsRowMap.get(rowKey);
				} else {
					row = ChangesetFactory.eINSTANCE.createChangeSetRow();
					rows.add(row);
					row.setLhsName(getRowName(loadSlot));
					row.setLoadSlot(loadSlot);
					lhsRowMap.put(getKeyName(loadSlot), row);
				}
			}
			if (isBase) {
				row.setNewGroupProfitAndLoss(openSlotAllocation);
			} else {
				row.setOriginalGroupProfitAndLoss(openSlotAllocation);
				if (openSlotAllocation.getSlot() != null) {
					final ChangeSetRow otherRow = lhsRowMap.get(getKeyName(openSlotAllocation.getSlot()));
					if (otherRow != null) {
						if (row != otherRow) {
							row.setLhsWiringLink(otherRow);
						}
					}
				}

			}
		} else if (slot instanceof DischargeSlot) {
			final DischargeSlot dischargeSlot = (DischargeSlot) slot;
			final ChangeSetRow row;
			{
				final String rowKey = getKeyName(dischargeSlot);
				if (rhsRowMap.containsKey(rowKey)) {
					row = rhsRowMap.get(rowKey);
				} else {
					row = ChangesetFactory.eINSTANCE.createChangeSetRow();
					rows.add(row);
					row.setRhsName(getRowName(dischargeSlot));
					row.setDischargeSlot(dischargeSlot);
					rhsRowMap.put(getKeyName(dischargeSlot), row);
				}
			}
			if (isBase) {
				row.setNewGroupProfitAndLoss(openSlotAllocation);
			} else {
				row.setOriginalGroupProfitAndLoss(openSlotAllocation);
				if (openSlotAllocation.getSlot() != null) {
					final ChangeSetRow otherRow = rhsRowMap.get(getKeyName(openSlotAllocation.getSlot()));
					if (otherRow != null) {
						if (row != otherRow) {
							row.setRhsWiringLink(otherRow);
						}
					}
				}
			}
		} else {
			//
			assert false;
		}
	}

	public static void createOrUpdateEventRow(@NonNull final Map<String, ChangeSetRow> lhsRowMap, @NonNull final Map<String, ChangeSetRow> rhsRowMap, @NonNull final List<ChangeSetRow> rows,
			@NonNull final Event event, final boolean isBase) {

		String eventName = event.name();
		if (isBase) {
			final ChangeSetRow row = ChangesetFactory.eINSTANCE.createChangeSetRow();
			rows.add(row);

			// TODO: Unique name?
			lhsRowMap.put(eventName, row);

			row.setLhsName(eventName);
			if (event instanceof ProfitAndLossContainer) {
				row.setNewGroupProfitAndLoss((ProfitAndLossContainer) event);
			}
			if (event instanceof EventGrouping) {
				row.setNewEventGrouping((EventGrouping) event);
			}
			row.setLhsVesselName(getName(event.getSequence()));
		} else {
			final ChangeSetRow row = lhsRowMap.get(eventName);

			if (event instanceof ProfitAndLossContainer) {
				row.setOriginalGroupProfitAndLoss((ProfitAndLossContainer) event);
			}
			if (event instanceof EventGrouping) {
				row.setOriginalEventGrouping((EventGrouping) event);
			}
			row.setRhsVesselName(getName(event.getSequence()));
		}
	}

	public static void convertToSortedGroups(@NonNull final Map<ChangeSetRow, Collection<ChangeSetRow>> rowToGroups) {

		for (final Map.Entry<ChangeSetRow, Collection<ChangeSetRow>> entry : rowToGroups.entrySet()) {
			final Collection<ChangeSetRow> value = entry.getValue();
			if (value.size() < 2) {
				continue;
			}
			if (value instanceof Set<?>) {
				if (true) {
					// Convert to list and sort.
					final List<ChangeSetRow> newGroup = new ArrayList<>(value.size());

					ChangeSetRow firstAlphabetically = null;
					ChangeSetRow head = null;
					for (final ChangeSetRow r : value) {
						if (r.getLhsWiringLink() == null) {
							head = r;
							break;
						}
						if (firstAlphabetically == null && r.getLhsName() != null) {
							firstAlphabetically = r;
						} else if (firstAlphabetically != null && r.getLhsName() != null) {
							if (firstAlphabetically.getLhsName().compareTo(r.getLhsName()) > 0) {
								firstAlphabetically = r;
							}
						}
					}
					if (head == null) {
						head = firstAlphabetically;
					}
					while (head != null && !newGroup.contains(head)) {
						newGroup.add(head);
						head = head.getRhsWiringLink();
					}

					entry.setValue(newGroup);
				} else {
					final List<ChangeSetRow> newGroup = new LinkedList<>(value);

					// Sort the group with head, tail elements fixed then alphabetically
					// This creates a stable sort even with cyclic wiring groups
					Collections.sort(newGroup, new Comparator<ChangeSetRow>() {

						@Override
						public int compare(final ChangeSetRow o1, final ChangeSetRow o2) {

							if (o1.getLhsWiringLink() == null) {
								return -1;
							}
							if (o2.getLhsWiringLink() == null) {
								return 1;
							}
							if (o1.getRhsWiringLink() == null) {
								return 1;
							}
							if (o2.getRhsWiringLink() == null) {
								return -1;
							}

							if (o1.getLhsWiringLink() == o2) {
								return -1;
							}

							if (o1.getRhsWiringLink() == o2) {
								return 1;
							}

							String n1 = o1.getLhsName();
							String n2 = o2.getLhsName();

							if (n1 != null && n2 == null) {
								return -1;
							}
							if (n1 == null && n2 != null) {
								return 1;
							}
							if (n1 != null && n2 != null) {
								return n1.compareTo(n2);
							}

							n1 = o1.getRhsName();
							n2 = o2.getRhsName();
							if (n1 != null && n2 == null) {
								return -1;
							}
							if (n1 == null && n2 != null) {
								return 1;
							}
							if (n1 != null && n2 != null) {
								return n1.compareTo(n2);
							}

							return 0;
						}
					});
					entry.setValue(newGroup);
				}
			}
		}

	}

	public static void updateRowGroups(@NonNull final ChangeSetRow row, @NonNull final Map<ChangeSetRow, Collection<ChangeSetRow>> rowToGroups) {
		if (rowToGroups.containsKey(row)) {
			return;
		}
		// Try and find an existing group.
		Collection<ChangeSetRow> group = null;
		if (row.getLhsWiringLink() != null) {
			if (rowToGroups.containsKey(row.getLhsWiringLink())) {
				group = rowToGroups.get(row.getLhsWiringLink());
			}
		} else if (row.getRhsWiringLink() != null) {
			if (rowToGroups.containsKey(row.getRhsWiringLink())) {
				group = rowToGroups.get(row.getRhsWiringLink());
			}
		}
		// Create new group if required.
		if (group == null) {
			group = new LinkedHashSet<>();
		}
		rowToGroups.put(row, group);
		// Add to group
		group.add(row);
		// Merge existing groups if required.
		if (row.getLhsWiringLink() != null) {
			if (rowToGroups.containsKey(row.getLhsWiringLink())) {
				final Collection<ChangeSetRow> otherGroup = rowToGroups.get(row.getLhsWiringLink());
				if (otherGroup != null && group != otherGroup) {
					group.addAll(otherGroup);
					for (final ChangeSetRow r : otherGroup) {
						rowToGroups.put(r, group);
					}
				}
			}
		}
		if (row.getRhsWiringLink() != null) {
			if (rowToGroups.containsKey(row.getRhsWiringLink())) {
				final Collection<ChangeSetRow> otherGroup = rowToGroups.get(row.getRhsWiringLink());
				if (otherGroup != null && group != otherGroup) {
					group.addAll(otherGroup);
					for (final ChangeSetRow r : otherGroup) {
						rowToGroups.put(r, group);
					}
				}
			}
		}
		assert rowToGroups.containsKey(row);
	}

	@NonNull
	public static String getName(@NonNull final Sequence sequence) {
		if (sequence.isSetCharterInMarket()) {
			return getName(sequence.getCharterInMarket());
		} else if (sequence.isSetVesselAvailability()) {
			return getName(sequence.getVesselAvailability());
		} else {
			return "";
		}
	}

	@NonNull
	public static String getName(@Nullable final VesselAssignmentType t) {
		if (t instanceof VesselAvailability) {
			return ((VesselAvailability) t).getVessel().getName();
		} else if (t instanceof CharterInMarket) {
			return ((CharterInMarket) t).getName();
		}
		throw new NullPointerException();
	}

	@Nullable
	public static String getRowName(@Nullable final Slot slot) {
		if (true) {
			// return getKeyName(slot);
		}
		if (slot == null) {
			return null;
		}
		if (slot instanceof SpotSlot) {
			if (slot instanceof SpotSlot) {
				final SpotMarket market = ((SpotSlot) slot).getMarket();
				return String.format("%s-%s", market.getName(), format(slot.getWindowStart()));
			}
		}
		return slot.getName();
	}

	@Nullable
	public static String getKeyName(@Nullable final Slot slot) {
		if (slot == null) {
			return null;
		}
		if (slot instanceof SpotSlot) {
			return EquivalanceGroupBuilder.getElementKey(slot);
		}
		return slot.getName();
	}

	@NonNull
	public static String format(@Nullable final LocalDate date) {
		if (date == null) {
			return "<no date>";
		}
		return String.format("%04d-%02d", date.getYear(), date.getMonthOfYear());

	}

	public static void setRowFlags(@NonNull final List<ChangeSetRow> rows) {
		// Update with vessel and wiring changes
		for (final ChangeSetRow row : rows) {
			if (!Objects.equal(row.getLhsVesselName(), row.getRhsVesselName())) {
				row.setVesselChange(true);
			}
			if (row.getLhsWiringLink() != null || row.getRhsWiringLink() != null) {
				row.setWiringChange(true);
			} else if ((row.getNewDischargeAllocation() == null) != (row.getOriginalDischargeAllocation() == null)) {
				row.setWiringChange(true);
			} else if ((row.getNewLoadAllocation() == null) != (row.getOriginalLoadAllocation() == null)) {
				row.setWiringChange(true);
			}

		}
	}

	public static void filterRows(@NonNull final List<ChangeSetRow> rows) {
		// Filter out zero P&L changes
		final Iterator<ChangeSetRow> itr = rows.iterator();
		while (itr.hasNext()) {
			final ChangeSetRow row = itr.next();
			if (row.isWiringChange()) {
				continue;
			}
			if (row.isVesselChange()) {
				continue;
			}
			long a = ChangeSetUtils.getGroupProfitAndLoss(row.getOriginalGroupProfitAndLoss());
			long b = ChangeSetUtils.getGroupProfitAndLoss(row.getNewGroupProfitAndLoss());
			if (a == b) {
				a = ChangeSetUtils.getCapacityViolationCount(row.getOriginalEventGrouping());
				b = ChangeSetUtils.getCapacityViolationCount(row.getNewEventGrouping());
				if (a == b) {
					a = ChangeSetUtils.getLateness(row.getOriginalEventGrouping());
					b = ChangeSetUtils.getLateness(row.getNewEventGrouping());
					if (a == b) {
						itr.remove();
						continue;
					}
				}
			}
		}
	}

	public static void sortRows(@NonNull final List<ChangeSetRow> rows) {
		// Sort into wiring groups.
		final Map<ChangeSetRow, Collection<ChangeSetRow>> rowToRowGroup = new HashMap<>();
		for (final ChangeSetRow row : rows) {
			ChangeSetTransformerUtil.updateRowGroups(row, rowToRowGroup);
		}

		ChangeSetTransformerUtil.convertToSortedGroups(rowToRowGroup);
		for (final ChangeSetRow row : rows) {
			assert rowToRowGroup.containsKey(row);
			// updateRowGroups(row, rowToRowGroup);
		}
		Collections.sort(rows, new Comparator<ChangeSetRow>() {

			@Override
			public int compare(final ChangeSetRow o1, final ChangeSetRow o2) {
				// Sort wiring changes first
				if (o1.isWiringChange() != o2.isWiringChange()) {
					if (o1.isWiringChange()) {
						return -1;
					} else {
						return 1;
					}
				} else if (o1.isWiringChange() && o2.isWiringChange()) {
					// If wiring change, sort related blocks together

					if (rowToRowGroup.containsKey(o1) && rowToRowGroup.get(o1).contains(o2)) {
						// Related elements, sort together.
						// Are these elements related?
						final Collection<ChangeSetRow> group = rowToRowGroup.get(o1);
						// Start from the first element in the sorted group
						ChangeSetRow link = group.iterator().next();
						while (link != null) {
							if (link == o1) {
								return -1;
							}
							if (link == o2) {
								return 1;
							}
							link = link.getRhsWiringLink();
						}
						assert false;
					} else {
						final Collection<ChangeSetRow> group1 = rowToRowGroup.get(o1);
						final Collection<ChangeSetRow> group2 = rowToRowGroup.get(o2);

						// Sort on head element ID
						return ("" + group1.iterator().next().getLhsName()).compareTo("" + group2.iterator().next().getLhsName());
					}
				}
				// Sort vessel changes above anything else.
				if (o1.isVesselChange() != o2.isVesselChange()) {
					if (o1.isVesselChange()) {
						return -1;
					} else {
						return 1;
					}
				}

				// Compare name
				return ("" + o1.getLhsName()).compareTo("" + o2.getLhsName());
			}
		});
	}

	public static void calculateMetrics(@NonNull final ChangeSet changeSet, @NonNull final Schedule fromSchedule, @NonNull final Schedule toSchedule, final boolean isBase) {
		{
			final Metrics currentMetrics = ChangesetFactory.eINSTANCE.createMetrics();
			final DeltaMetrics deltaMetrics = ChangesetFactory.eINSTANCE.createDeltaMetrics();

			long pnl = 0;
			long lateness = 0;
			long violations = 0;
			{
				for (final Sequence sequence : toSchedule.getSequences()) {
					for (final Event event : sequence.getEvents()) {
						if (event instanceof ProfitAndLossContainer) {
							final ProfitAndLossContainer profitAndLossContainer = (ProfitAndLossContainer) event;
							final GroupProfitAndLoss groupProfitAndLoss = profitAndLossContainer.getGroupProfitAndLoss();
							if (groupProfitAndLoss != null) {
								pnl += groupProfitAndLoss.getProfitAndLoss();
							}
						} else if (event instanceof SlotVisit) {
							final SlotVisit slotVisit = (SlotVisit) event;
							if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
								final CargoAllocation cargoAllocation = slotVisit.getSlotAllocation().getCargoAllocation();
								pnl += cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();
								lateness += ChangeSetUtils.getLateness(cargoAllocation);
								violations += ChangeSetUtils.getCapacityViolationCount(cargoAllocation);
							}
						}
					}
				}

				for (final OpenSlotAllocation openSlotAllocation : fromSchedule.getOpenSlotAllocations()) {
					pnl += openSlotAllocation.getGroupProfitAndLoss().getProfitAndLoss();
				}
			}
			// THIS SHOULD BE THE TO SCHENAR> -- FLIP CODE ROUND
			currentMetrics.setPnl((int) pnl);
			currentMetrics.setCapacity((int) violations);
			currentMetrics.setLateness((int) lateness);
			{
				for (final Sequence sequence : fromSchedule.getSequences()) {
					for (final Event event : sequence.getEvents()) {
						if (event instanceof ProfitAndLossContainer) {
							final ProfitAndLossContainer profitAndLossContainer = (ProfitAndLossContainer) event;
							final GroupProfitAndLoss groupProfitAndLoss = profitAndLossContainer.getGroupProfitAndLoss();
							if (groupProfitAndLoss != null) {
								pnl -= groupProfitAndLoss.getProfitAndLoss();
							}
						} else if (event instanceof SlotVisit) {
							final SlotVisit slotVisit = (SlotVisit) event;
							if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
								final CargoAllocation cargoAllocation = slotVisit.getSlotAllocation().getCargoAllocation();
								pnl -= cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();
								lateness -= ChangeSetUtils.getLateness(cargoAllocation);
								violations -= ChangeSetUtils.getCapacityViolationCount(cargoAllocation);
							}
						}
					}
				}

				for (final OpenSlotAllocation openSlotAllocation : toSchedule.getOpenSlotAllocations()) {
					pnl -= openSlotAllocation.getGroupProfitAndLoss().getProfitAndLoss();
				}
			}
			deltaMetrics.setPnlDelta((int) pnl);
			deltaMetrics.setLatenessDelta((int) lateness);
			deltaMetrics.setCapacityDelta((int) violations);
			if (isBase) {
				changeSet.setMetricsToBase(deltaMetrics);
			} else {
				changeSet.setMetricsToPrevious(deltaMetrics);
			}
			changeSet.setCurrentMetrics(currentMetrics);
		}
	}
}

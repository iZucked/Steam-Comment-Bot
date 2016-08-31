/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.base.Objects;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics;
import com.mmxlabs.lingo.reports.views.changeset.model.Metrics;
import com.mmxlabs.lingo.reports.views.schedule.EquivalanceGroupBuilder;
import com.mmxlabs.lingo.reports.views.schedule.formatters.VesselAssignmentFormatter;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.LatenessUtils;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;

public final class ChangeSetTransformerUtil {
	public static boolean createOrUpdateRow(@NonNull final Map<String, ChangeSetRow> lhsRowMap, @NonNull final Map<String, ChangeSetRow> rhsRowMap,

			@NonNull final Map<String, List<ChangeSetRow>> lhsRowMarketMap, @NonNull final Map<String, List<ChangeSetRow>> rhsRowMarketMap, @NonNull final List<ChangeSetRow> rows,
			@NonNull final EObject element, final boolean isBase, final boolean canDefer) {

		if (element instanceof SlotVisit) {
			final SlotVisit slotVisit = (SlotVisit) element;
			if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) slotVisit.getSlotAllocation().getSlot();
				assert loadSlot != null;
				return createOrUpdateSlotVisitRow(lhsRowMap, rhsRowMap, lhsRowMarketMap, rhsRowMarketMap, rows, slotVisit, loadSlot, isBase, canDefer);
			}
		} else if (element instanceof OpenSlotAllocation) {
			final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) element;
			// return
			createOrUpdateOpenSlotAllocationRow(lhsRowMap, rhsRowMap, rows, openSlotAllocation, isBase /* , canDefer */);
		} else if (element instanceof Event) {
			final Event event = (Event) element;

			// return
			createOrUpdateEventRow(lhsRowMap, rhsRowMap, rows, event, isBase /* , canDefer */);
		}
		return false;
	}

	public static boolean createOrUpdateSlotVisitRow(@NonNull final Map<String, ChangeSetRow> lhsRowMap, @NonNull final Map<String, ChangeSetRow> rhsRowMap,
			final Map<String, List<ChangeSetRow>> lhsRowMarketMap, final Map<String, List<ChangeSetRow>> rhsRowMarketMap, @NonNull final List<ChangeSetRow> rows, @NonNull final SlotVisit slotVisit,
			@NonNull final LoadSlot loadSlot, final boolean isBase, final boolean canDefer) {

		final ChangeSetRow row;
		{
			final String rowKey = getKeyName(loadSlot);
			if (lhsRowMap.containsKey(rowKey)) {
				row = lhsRowMap.get(rowKey);
			} else {
				final String rowName = getRowName(loadSlot);
				// String rowName = getRowName(loadSlot);
				// if (loadSlot instanceof SpotSlot) {
				//// if (lhsRowMap.containsKey("market-" + loadSlot.getName())) {
				//// row = lhsRowMap.get("market-" + loadSlot.getName());
				//// } else {
				// row = ChangesetFactory.eINSTANCE.createChangeSetRow();
				// rows.add(row);
				// row.setLhsName(rowName);
				// row.setLoadSlot(loadSlot);
				// lhsRowMap.put(rowKey, row);
				// lhsRowMap.put("market-" + loadSlot.getName(), row);
				//// }
				// } else {

				row = ChangesetFactory.eINSTANCE.createChangeSetRow();
				rows.add(row);
				row.setLhsName(rowName);
				row.setLoadSlot(loadSlot);
				lhsRowMap.put(rowKey, row);
				// }
			}
		}

		final CargoAllocation cargoAllocation = slotVisit.getSlotAllocation().getCargoAllocation();
		if (cargoAllocation.getSlotAllocations().size() != 2) {
			throw new RuntimeException("Complex cargoes are not supported");
		}
		if (isBase) {
			row.setNewGroupProfitAndLoss(cargoAllocation);
			row.setNewEventGrouping(cargoAllocation);
			row.setNewLoadAllocation(slotVisit.getSlotAllocation());
			row.setNewVesselName(getName(slotVisit.getSequence()));
			row.setNewVesselShortName(getShortName(slotVisit.getSequence()));
		} else {
			row.setOriginalGroupProfitAndLoss(cargoAllocation);
			row.setOriginalEventGrouping(cargoAllocation);
			row.setOriginalLoadAllocation(slotVisit.getSlotAllocation());
			row.setOriginalVesselName(getName(slotVisit.getSequence()));
			row.setOriginalVesselShortName(getShortName(slotVisit.getSequence()));
		}
		// Get discharge data
		for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
			if (slotAllocation == slotVisit.getSlotAllocation()) {
				continue;
			}

			// Store basic key
			final String otherRowKey = getKeyName(slotAllocation.getSlot());
			if (isBase) {
				row.setRhsName(getRowName(slotAllocation.getSlot()));
				row.setNewDischargeAllocation(slotAllocation);
				row.setDischargeSlot((DischargeSlot) slotAllocation.getSlot());
				// FIXME: This can replace an existing entry -- is this ok?
				// if (rhsRowMap.containsKey(otherRowKey)) {
				// System.out.println("Clash " + otherRowKey);
				// }
				rhsRowMap.put(otherRowKey, row);
				{
					// String rowName = getRowName(slotAllocation.getSlot());
					if (slotAllocation.getSlot() instanceof SpotSlot) {
						// FIXME: THis can overwrite other options and lead to a wiring mess.
						// TODO: Store as list and process later.
						// 1. Is before and after the same market? No change, remove option.
						// 2. Whatever is left, pull from list
						addToMarketMap(rhsRowMarketMap, slotAllocation.getSlot(), row);
					}
				}
			} else {
				row.setOriginalDischargeAllocation(slotAllocation);
			}
			if (!isBase) {
				if (slotAllocation.getSlot() != null) {

					ChangeSetRow otherRow = rhsRowMap.get(otherRowKey);

					if (otherRow == null) {

						if (slotAllocation.getSlot() instanceof SpotSlot) {
							final List<ChangeSetRow> possibleRows = getMarketOptions(rhsRowMarketMap, slotAllocation.getSlot());
							for (final ChangeSetRow option : possibleRows) {
								if (option.getLhsName().equals(row.getLhsName())) {
									// Match, so remove option from list
									possibleRows.remove(option);
									otherRow = option;
									break;
								}
							}
							if (otherRow == null && canDefer) {
								return true;
							} else if (!possibleRows.isEmpty()) {
								// Cannot defer, but remaining options should be interchangeable.
								otherRow = possibleRows.remove(0);
							}
						}
					} else {
						// Exact match found, so remove this option from the available set.
						if (slotAllocation.getSlot() instanceof SpotSlot) {
							removeFromMarketMap(rhsRowMarketMap, slotAllocation.getSlot(), otherRow);
						}
					}
					if (otherRow == null) {
						otherRow = ChangesetFactory.eINSTANCE.createChangeSetRow();
						rows.add(otherRow);
						rhsRowMap.put(otherRowKey, otherRow);
						if (slotAllocation.getSlot() instanceof SpotSlot) {
							rhsRowMap.put(EquivalanceGroupBuilder.getElementKey(slotAllocation.getSlot()), otherRow);
							// FIXME: Random key used here, no point inserting into map.
							// Probably really want #addToMarketMap
							rhsRowMap.put("market-" + slotAllocation.getSlot().getName(), otherRow);
						}
						otherRow.setRhsName(getRowName(slotAllocation.getSlot()));
					}
					// otherRow.setOriginalDischargeAllocation(slotAllocation);
					otherRow.setDischargeSlot((DischargeSlot) slotAllocation.getSlot());

					if (row != otherRow) {
						row.setRhsWiringLink(otherRow);
					}
				}
			}
		}
		return false;
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

		final String eventName = event.name();
		// Skip charter market end events.
		if (event instanceof EndEvent) {
			if (event.getSequence().isSetCharterInMarket()) {
				return;
			}
		}
		final String key = EquivalanceGroupBuilder.getElementKey(event);
		if (isBase) {
			final ChangeSetRow row = ChangesetFactory.eINSTANCE.createChangeSetRow();
			rows.add(row);

			// TODO: Unique name?
			lhsRowMap.put(key, row);

			row.setLhsName(eventName);
			if (event instanceof ProfitAndLossContainer) {
				row.setNewGroupProfitAndLoss((ProfitAndLossContainer) event);
			}
			if (event instanceof EventGrouping) {
				row.setNewEventGrouping((EventGrouping) event);
			}
			row.setNewVesselName(getName(event.getSequence()));
			row.setNewVesselShortName(getShortName(event.getSequence()));
		} else {
			ChangeSetRow row = lhsRowMap.get(key);
			if (row == null) {
				row = ChangesetFactory.eINSTANCE.createChangeSetRow();
				row.setLhsName(eventName);
				rows.add(row);

				// TODO: Unique name?
				lhsRowMap.put(key, row);

			}

			if (event instanceof ProfitAndLossContainer) {
				row.setOriginalGroupProfitAndLoss((ProfitAndLossContainer) event);
			}
			if (event instanceof EventGrouping) {
				row.setOriginalEventGrouping((EventGrouping) event);
			}
			row.setOriginalVesselName(getName(event.getSequence()));
			row.setOriginalVesselShortName(getShortName(event.getSequence()));
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
		if (sequence.getSequenceType() == SequenceType.DES_PURCHASE) {
			return "";
		} else if (sequence.getSequenceType() == SequenceType.FOB_SALE) {
			return "";
		} else {
			// Use consistent naming
			final VesselAssignmentFormatter formatter = new VesselAssignmentFormatter();
			@Nullable
			final String renderedName = formatter.render(sequence);
			if (renderedName != null) {
				return renderedName;
			} else {
				return "";
			}
		}
	}

	@NonNull
	public static String getShortName(@NonNull final Sequence sequence) {
		if (sequence.isSetVesselAvailability()) {
			return getShortName(sequence.getVesselAvailability());
		} else {
			return getName(sequence);
		}
	}

	@NonNull
	public static String getShortName(@Nullable final VesselAssignmentType t) {
		if (t instanceof VesselAvailability) {
			Vessel vessel = ((VesselAvailability) t).getVessel();
			if (vessel != null) {
				return vessel.getShortenedName();
			} else {
				return "";
			}
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
			final SpotMarket market = ((SpotSlot) slot).getMarket();
			return String.format("%s-%s", market.getName(), format(slot.getWindowStart()));
		}
		return slot.getName();
	}

	@Nullable
	public static String getKeyName(@Nullable final Slot slot) {
		if (slot == null) {
			return null;
		}
		if (slot instanceof SpotSlot) {
			final String key = EquivalanceGroupBuilder.getElementKey(slot);
			final StringBuilder sb = new StringBuilder();
			final Cargo cargo = slot.getCargo();
			if (cargo != null) {
				for (final Slot slot2 : cargo.getSortedSlots()) {
					if (slot == slot2) {
						sb.append(key);
					} else {
						sb.append(slot2.getName());
					}

				}
			} else {
				sb.append(key);
			}
			return sb.toString();

		}
		return slot.getName();
	}

	@NonNull
	public static String format(@Nullable final LocalDate date) {
		if (date == null) {
			return "<no date>";
		}
		return String.format("%04d-%02d", date.getYear(), date.getMonthValue());

	}

	public static void setRowFlags(@NonNull final List<ChangeSetRow> rows) {
		// Update with vessel and wiring changes
		for (final ChangeSetRow row : rows) {

			if (!Objects.equal(row.getOriginalVesselName(), row.getNewVesselName())) {
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
			long a = ScheduleModelKPIUtils.getGroupProfitAndLoss(row.getOriginalGroupProfitAndLoss());
			long b = ScheduleModelKPIUtils.getGroupProfitAndLoss(row.getNewGroupProfitAndLoss());
			if (a == b) {
				a = ScheduleModelKPIUtils.getCapacityViolationCount(row.getOriginalEventGrouping());
				b = ScheduleModelKPIUtils.getCapacityViolationCount(row.getNewEventGrouping());
				if (a == b) {
					a = LatenessUtils.getLatenessExcludingFlex(row.getOriginalEventGrouping());
					b = LatenessUtils.getLatenessExcludingFlex(row.getNewEventGrouping());
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
			int lateness = 0;
			int violations = 0;
			if (toSchedule != null) {
				for (final Sequence sequence : toSchedule.getSequences()) {
					for (final Event event : sequence.getEvents()) {
						if (event instanceof ProfitAndLossContainer) {
							final ProfitAndLossContainer profitAndLossContainer = (ProfitAndLossContainer) event;
							final GroupProfitAndLoss groupProfitAndLoss = profitAndLossContainer.getGroupProfitAndLoss();
							if (groupProfitAndLoss != null) {
								pnl += groupProfitAndLoss.getProfitAndLoss();
							}
						}

						if (event instanceof SlotVisit) {
							final SlotVisit slotVisit = (SlotVisit) event;
							if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
								final CargoAllocation cargoAllocation = slotVisit.getSlotAllocation().getCargoAllocation();
								pnl += cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();
								lateness += LatenessUtils.getLatenessExcludingFlex(cargoAllocation);
								violations += ScheduleModelKPIUtils.getCapacityViolationCount(cargoAllocation);
							}
						}

						if (event instanceof EventGrouping) {
							final EventGrouping eventGrouping = (EventGrouping) event;
							lateness += LatenessUtils.getLatenessExcludingFlex(eventGrouping);
							violations += ScheduleModelKPIUtils.getCapacityViolationCount(eventGrouping);
						}
					}
				}

				for (final OpenSlotAllocation openSlotAllocation : toSchedule.getOpenSlotAllocations()) {
					pnl += openSlotAllocation.getGroupProfitAndLoss().getProfitAndLoss();
				}
			}

			currentMetrics.setPnl((int) pnl);
			currentMetrics.setCapacity(violations);
			currentMetrics.setLateness(lateness);
			if (fromSchedule != null) {
				for (final Sequence sequence : fromSchedule.getSequences()) {
					for (final Event event : sequence.getEvents()) {
						if (event instanceof ProfitAndLossContainer) {
							final ProfitAndLossContainer profitAndLossContainer = (ProfitAndLossContainer) event;
							final GroupProfitAndLoss groupProfitAndLoss = profitAndLossContainer.getGroupProfitAndLoss();
							if (groupProfitAndLoss != null) {
								pnl -= groupProfitAndLoss.getProfitAndLoss();
							}
						}
						if (event instanceof SlotVisit) {
							final SlotVisit slotVisit = (SlotVisit) event;
							if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
								final CargoAllocation cargoAllocation = slotVisit.getSlotAllocation().getCargoAllocation();
								pnl -= cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();
								lateness -= LatenessUtils.getLatenessExcludingFlex(cargoAllocation);
								violations -= ScheduleModelKPIUtils.getCapacityViolationCount(cargoAllocation);
							}
						}

						if (event instanceof EventGrouping) {
							final EventGrouping eventGrouping = (EventGrouping) event;
							lateness -= LatenessUtils.getLatenessExcludingFlex(eventGrouping);
							violations -= ScheduleModelKPIUtils.getCapacityViolationCount(eventGrouping);
						}

					}
				}

				for (final OpenSlotAllocation openSlotAllocation : fromSchedule.getOpenSlotAllocations()) {
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

	public static void mergeSpots(final List<ChangeSetRow> rows) {
		{
			// Phase 1. Find wiring groups which have compatible (i.e. same market, different instance count) heads and tails and join them together.
			// For example a Spot Purchase may now be open, but previously paired to a sale. Another sale in the wiring group may have previously been paired to a different spot purchase in the same
			// market/month as the open position. We can assume these are equivalent and join the head to the tail.
			final Map<ChangeSetRow, ChangeSetRow> headToTails = new LinkedHashMap<>();
			for (final ChangeSetRow row : rows) {
				if (row.getLhsWiringLink() == null) {
					ChangeSetRow link = row.getRhsWiringLink();
					ChangeSetRow tail = link;
					while (link != null) {
						tail = link;
						link = link.getRhsWiringLink();
					}
					if (tail != null) {
						headToTails.put(row, tail);
					}
				}
			}
			for (final Map.Entry<ChangeSetRow, ChangeSetRow> e : headToTails.entrySet()) {
				if (mergeSpotSales(e.getKey(), e.getValue())) {
					rows.remove(e.getValue());
				}
			}
			for (final Map.Entry<ChangeSetRow, ChangeSetRow> e : headToTails.entrySet()) {
				if (mergeSpotPurchases(e.getKey(), e.getValue())) {
					rows.remove(e.getValue());
				}
			}
		}
		// Phase 2. It is possible that we can end up with separate wiring groups but both share compatible head tails and these can be merged together into a single wiring group.
		{
			final Map<ChangeSetRow, ChangeSetRow> headToTails = new LinkedHashMap<>();
			for (final ChangeSetRow row : rows) {
				if (row.getLhsWiringLink() == null) {
					ChangeSetRow link = row.getRhsWiringLink();
					ChangeSetRow tail = link;
					while (link != null) {
						tail = link;
						link = link.getRhsWiringLink();
					}
					if (tail != null) {
						headToTails.put(row, tail);
					}
				}
			}

			final Map<Pair<String, String>, Pair<ChangeSetRow, ChangeSetRow>> headTailMap = new LinkedHashMap<>();
			for (final Map.Entry<ChangeSetRow, ChangeSetRow> e : headToTails.entrySet()) {

				final Pair<String, String> keyA = new Pair<>(e.getKey().getLhsName(), e.getValue().getRhsName());
				final Pair<String, String> keyB = new Pair<>(e.getValue().getLhsName(), e.getKey().getRhsName());

				if (headTailMap.containsKey(keyA)) {
					final Pair<ChangeSetRow, ChangeSetRow> existing = headTailMap.get(keyA);
					mergeSpotSales(existing.getSecond(), e.getValue());
					mergeSpotPurchases(e.getKey(), existing.getFirst());
					rows.remove(e.getValue());
					rows.remove(existing.getFirst());
					headTailMap.remove(keyA);
				} else if (headTailMap.containsKey(keyB)) {
					final Pair<ChangeSetRow, ChangeSetRow> existing = headTailMap.get(keyB);
					mergeSpotPurchases(existing.getFirst(), e.getValue());
					mergeSpotSales(e.getKey(), existing.getSecond());
					rows.remove(existing.getSecond());
					rows.remove(e.getValue());
					headTailMap.remove(keyB);
				} else {
					headTailMap.put(keyA, new Pair<>(e.getKey(), e.getValue()));
					headTailMap.put(keyB, new Pair<>(e.getValue(), e.getKey()));
				}
			}
		}
		// Change set 20
		// Phase 3. We may have a load which swaps spot markets with another load. E.g. l1 -> s1 diverted to s2 and l2 -> s2 diverted to s1. This would otherwise be reported as two separate group when
		// in fact it could be one group.
		{
			final Map<Pair<String, String>, Pair<ChangeSetRow, ChangeSetRow>> mapping = new LinkedHashMap<>();

			for (final ChangeSetRow row : new LinkedList<>(rows)) {
				// Is this a connected head element?
				if (!(row.getLhsWiringLink() == null && row.getRhsWiringLink() != null)) {
					continue;
				}
				// Head
				final ChangeSetRow head = row;
				final ChangeSetRow rhs = row.getRhsWiringLink();
				// Is the next element a tail?
				if (rhs.getRhsWiringLink() != null) {
					continue;
				}
				// We are a tail! (we have a head -> tail sequence)
				final ChangeSetRow tail = rhs;

				if (tail.getNewLoadAllocation() != null) {
					// Previous wiring, skip;
					continue;
				}

				// Now we have found a cargo wiring example - do they involve spots?

				final boolean headSpot = (head.getNewDischargeAllocation() != null && head.getNewDischargeAllocation().getSlot() instanceof SpotSlot);
				if (!headSpot) {
					continue;
				}
				final boolean tailSpot = (head.getOriginalDischargeAllocation() != null && head.getOriginalDischargeAllocation().getSlot() instanceof SpotSlot);
				if (!tailSpot) {
					continue;
				}

				// We have found one potential wiring cargo.
				final Pair<String, String> lookupKey = new Pair<>(tail.getRhsName(), head.getRhsName());
				if (mapping.containsKey(lookupKey)) {
					// Merge!
					final Pair<ChangeSetRow, ChangeSetRow> p = mapping.get(lookupKey);
					mergeSpotSales(head, p.getSecond());
					rows.remove(p.getSecond());
					mergeSpotSales(p.getFirst(), tail);
					rows.remove(tail);
				} else {
					final Pair<String, String> storeKey = new Pair<>(head.getRhsName(), tail.getRhsName());
					mapping.put(storeKey, new Pair<>(head, tail));
				}
			}
		}
	}

	private static boolean mergeSpotSales(@Nullable final ChangeSetRow head, @Nullable final ChangeSetRow tail) {

		assert head != tail;
		if (head == null || !(head.getDischargeSlot() instanceof SpotSlot)) {
			return false;
		}
		if (tail == null || !(tail.getDischargeSlot() instanceof SpotSlot)) {
			return false;
		}
		// Check for same market type
		if (((SpotSlot) head.getDischargeSlot()).getMarket().eClass() == ((SpotSlot) tail.getDischargeSlot()).getMarket().eClass()) {
			if (head.getRhsName() != null && head.getRhsName().equals(tail.getRhsName())) {
				final ChangeSetRow lhsWiringLink = tail.getLhsWiringLink();
				// head.setOriginalDischargeAllocation(tail.getOriginalDischargeAllocation());
				head.setLhsWiringLink(lhsWiringLink);
				return true;
			}
		}
		return false;
	}

	private static boolean mergeSpotPurchases(@Nullable final ChangeSetRow head, @Nullable final ChangeSetRow tail) {

		assert head != tail;
		if (head == null || !(head.getLoadSlot() instanceof SpotSlot)) {
			return false;
		}
		if (tail == null || !(tail.getLoadSlot() instanceof SpotSlot)) {
			return false;
		}

		// Check for same market type
		if (((SpotSlot) head.getLoadSlot()).getMarket().eClass() == ((SpotSlot) tail.getLoadSlot()).getMarket().eClass()) {

			if (head.getLhsName() != null && head.getLhsName().equals(tail.getLhsName())) {

				final ChangeSetRow lhsWiringLink = tail.getLhsWiringLink();
				head.setLhsWiringLink(lhsWiringLink);

				// Copy data across - not needed for sales as that should already have happened. However all data is keyed off the load, hence extra work here
				head.setNewDischargeAllocation(tail.getNewDischargeAllocation());
				head.setNewEventGrouping(tail.getNewEventGrouping());
				head.setNewGroupProfitAndLoss(tail.getNewGroupProfitAndLoss());
				head.setNewLoadAllocation(tail.getNewLoadAllocation());
				head.setNewVesselName(tail.getNewVesselName());
				head.setNewVesselShortName(tail.getNewVesselShortName());
				head.setRhsName(tail.getRhsName());
				head.setDischargeSlot(tail.getDischargeSlot());

				return true;
			}
		}
		return false;
	}

	private static void addToMarketMap(final Map<String, List<ChangeSetRow>> rowMarketMap, final Slot slot, final ChangeSetRow row) {
		final String marketKey = EquivalanceGroupBuilder.getElementKey(slot);
		final List<ChangeSetRow> list;
		if (rowMarketMap.containsKey(marketKey)) {
			list = rowMarketMap.get(marketKey);
		} else {
			list = new LinkedList<>();
		}
		list.add(row);
	}

	@NonNull
	private static List<ChangeSetRow> getMarketOptions(final Map<String, List<ChangeSetRow>> rowMarketMap, final Slot slot) {
		final String marketKey = EquivalanceGroupBuilder.getElementKey(slot);
		if (rowMarketMap.containsKey(marketKey)) {
			return rowMarketMap.get(marketKey);
		} else {
			return Collections.emptyList();
		}
	}

	private static void removeFromMarketMap(final Map<String, List<ChangeSetRow>> rowMarketMap, final Slot slot, final ChangeSetRow row) {
		final String marketKey = EquivalanceGroupBuilder.getElementKey(slot);
		final List<ChangeSetRow> list;
		if (rowMarketMap.containsKey(marketKey)) {
			list = rowMarketMap.get(marketKey);
		} else {
			list = new LinkedList<>();
		}
		list.remove(row);
	}

	/**
	 * Find elements that we are interested in showing in the view.
	 * 
	 * @param schedule
	 * @param interestingEvents
	 * @param allEvents
	 */
	public static void extractElements(final @Nullable Schedule schedule, final Collection<EObject> interestingEvents, final Collection<EObject> allEvents) {
		if (schedule == null) {
			return;
		}

		for (final Sequence sequence : schedule.getSequences()) {
			for (final Event event : sequence.getEvents()) {
				boolean includeEvent = false;
				if (event instanceof SlotVisit) {
					includeEvent = true;
				} else if (event instanceof VesselEventVisit) {
					includeEvent = true;
				} else if (event instanceof GeneratedCharterOut) {
					includeEvent = true;

				} else if (event instanceof StartEvent) {
					includeEvent = true;
				} else if (event instanceof EndEvent) {
					includeEvent = true;
				}
				if (includeEvent) {
					interestingEvents.add(event);
				}
				allEvents.add(event);
			}
		}

		for (final OpenSlotAllocation openSlotAllocation : schedule.getOpenSlotAllocations()) {
			interestingEvents.add(openSlotAllocation);
			allEvents.add(openSlotAllocation);
		}
	}
}

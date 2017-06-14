/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.ToLongFunction;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.base.Objects;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetKPIUtil.FlexType;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetKPIUtil.ResultType;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowDataGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics;
import com.mmxlabs.lingo.reports.views.changeset.model.Metrics;
import com.mmxlabs.lingo.reports.views.schedule.EquivalanceGroupBuilder;
import com.mmxlabs.lingo.reports.views.schedule.formatters.VesselAssignmentFormatter;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocationType;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.LatenessUtils;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.mmxcore.NamedObject;

public final class ChangeSetTransformerUtil {

	/**
	 * Data structure to hold element ID to row mappings.
	 *
	 */
	public static class MappingModel {
		final List<ChangeSetRowDataGroup> groups = new LinkedList<>();

		final Map<String, ChangeSetRowData> lhsRowMap = new LinkedHashMap<>();
		final Map<String, ChangeSetRowData> rhsRowMap = new LinkedHashMap<>();

		final Map<String, List<ChangeSetRowData>> lhsRowMarketMap = new LinkedHashMap<>();
		final Map<String, List<ChangeSetRowData>> rhsRowMarketMap = new LinkedHashMap<>();

	}

	/**
	 * Extract all Change Set targets from a {@link Schedule}.
	 * 
	 * @param schedule
	 * @return
	 */
	public static List<EObject> extractTargets(final @NonNull Schedule schedule) {

		final List<EObject> targets = new LinkedList<>();
		for (final CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {
			targets.add(cargoAllocation);
		}
		for (final OpenSlotAllocation openSlotAllocation : schedule.getOpenSlotAllocations()) {
			targets.add(openSlotAllocation);
		}

		for (final Sequence sequence : schedule.getSequences()) {
			if (sequence.getSequenceType() == SequenceType.VESSEL //
					|| sequence.getSequenceType() == SequenceType.ROUND_TRIP //
					|| sequence.getSequenceType() == SequenceType.SPOT_VESSEL) {
				for (final Event event : sequence.getEvents()) {
					// Filter events
					if (event instanceof Journey) {
						continue;
					} else if (event instanceof Idle) {
						continue;
					} else if (event instanceof Cooldown) {
						continue;
					}

					else if (event instanceof StartEvent //
							|| event instanceof EndEvent) {
						if (sequence.getSequenceType() == SequenceType.VESSEL) {
							// Only include these for fleet vessels.

						} else {
							continue;
						}
					} else if (event instanceof VesselEventVisit) {
						// Keep going!
					} else if (event instanceof GeneratedCharterOut) {
						// Keep going!
					} else if (event instanceof SlotVisit) {
						// Already processed
						continue;
					} else if (event instanceof PortVisit) {
						if (sequence.getSequenceType() == SequenceType.VESSEL) {
							// Only include these for fleet vessels.

						} else {
							// On a spot vessel "probably" the fake end ports -- TODO; tighten check up
							continue;
						}
					} else {
						// Unknown event!
						assert false;
					}

					targets.add(event);
				}
			}
		}
		return targets;
	}

	/**
	 * Given a list of target {@link EObject}s, generate a {@link MappingModel} which can be used to compare against other {@link MappingModel}s
	 * 
	 * @param targets
	 * @return
	 */
	public static MappingModel generateMappingModel(final Collection<EObject> targets) {

		final MappingModel mappingModel = new MappingModel();

		for (final EObject target : targets) {
			if (target instanceof CargoAllocation) {
				final CargoAllocation cargoAllocation = (CargoAllocation) target;

				final ChangeSetRowDataGroup group = ChangesetFactory.eINSTANCE.createChangeSetRowDataGroup();
				mappingModel.groups.add(group);

				final List<SlotAllocation> loadAllocations = new LinkedList<>();
				final List<SlotAllocation> dischargeAllocations = new LinkedList<>();
				for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
					if (slotAllocation.getSlotAllocationType() == SlotAllocationType.PURCHASE) {
						loadAllocations.add(slotAllocation);
					} else if (slotAllocation.getSlotAllocationType() == SlotAllocationType.SALE) {
						dischargeAllocations.add(slotAllocation);
					} else {
						assert false;
					}
				}

				// Create a row for each pair of load and discharge slots in the cargo. This may lead to a row with only one slot
				for (int i = 0; i < Math.max(loadAllocations.size(), dischargeAllocations.size()); ++i) {
					final ChangeSetRowData row = ChangesetFactory.eINSTANCE.createChangeSetRowData();
					group.getMembers().add(row);

					row.setPrimaryRecord(i == 0);
					if (i == 0) {
						row.setLhsGroupProfitAndLoss(cargoAllocation);
						row.setEventGrouping(cargoAllocation);
						row.setVesselName(ChangeSetTransformerUtil.getName(cargoAllocation.getSequence()));
						row.setVesselShortName(ChangeSetTransformerUtil.getShortName(cargoAllocation.getSequence()));
					}

					if (i < loadAllocations.size()) {
						final SlotAllocation loadAllocation = loadAllocations.get(i);
						final LoadSlot slot = (LoadSlot) loadAllocation.getSlot();

						final String key = ChangeSetTransformerUtil.getKeyName(loadAllocation);
						final String name = ChangeSetTransformerUtil.getRowName(slot);

						row.setLhsName(name);
						row.setLoadAllocation(loadAllocation);
						row.setLoadSlot(slot);

						if (row.getLoadSlot() instanceof SpotLoadSlot) {
							final String mKey = getMarketSlotKey((SpotLoadSlot) slot);
							mappingModel.lhsRowMarketMap.computeIfAbsent(mKey, k -> new LinkedList<>()).add(row);
						}
						mappingModel.lhsRowMap.put(key, row);

					}
					SlotAllocation dischargeAllocation = null;
					if (i < dischargeAllocations.size()) {
						dischargeAllocation = dischargeAllocations.get(i);
						final DischargeSlot slot = (DischargeSlot) dischargeAllocation.getSlot();

						final String key = ChangeSetTransformerUtil.getKeyName(dischargeAllocation);
						final String name = ChangeSetTransformerUtil.getRowName(slot);

						row.setRhsName(name);
						row.setDischargeAllocation(dischargeAllocation);
						row.setDischargeSlot(slot);
						if (row.getDischargeSlot() instanceof SpotDischargeSlot) {
							final String mKey = getMarketSlotKey((SpotDischargeSlot) slot);
							mappingModel.rhsRowMarketMap.computeIfAbsent(mKey, k -> new LinkedList<>()).add(row);
						}
						mappingModel.rhsRowMap.put(key, row);

					}

				}
			} else if (target instanceof OpenSlotAllocation) {
				final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) target;
				final ChangeSetRowDataGroup group = ChangesetFactory.eINSTANCE.createChangeSetRowDataGroup();
				mappingModel.groups.add(group);

				final ChangeSetRowData row = ChangesetFactory.eINSTANCE.createChangeSetRowData();
				group.getMembers().add(row);

				final Slot slot = openSlotAllocation.getSlot();

				final String key = ChangeSetTransformerUtil.getKeyName(openSlotAllocation);
				final String name = ChangeSetTransformerUtil.getRowName(slot);
				if (slot instanceof LoadSlot) {
					row.setLhsName(name);
					row.setOpenLoadAllocation(openSlotAllocation);
					row.setLoadSlot((LoadSlot) slot);
					row.setLhsGroupProfitAndLoss(openSlotAllocation);

					if (slot instanceof SpotLoadSlot) {
						final String mKey = getMarketSlotKey((SpotLoadSlot) slot);
						mappingModel.lhsRowMarketMap.computeIfAbsent(mKey, k -> new LinkedList<>()).add(row);
					}
					mappingModel.lhsRowMap.put(key, row);

				} else if (slot instanceof DischargeSlot) {
					row.setRhsName(name);
					row.setOpenDischargeAllocation(openSlotAllocation);
					row.setDischargeSlot((DischargeSlot) slot);
					row.setRhsGroupProfitAndLoss(openSlotAllocation);

					if (slot instanceof SpotDischargeSlot) {
						final String mKey = getMarketSlotKey((SpotDischargeSlot) slot);

						mappingModel.rhsRowMarketMap.computeIfAbsent(mKey, k -> new LinkedList<>()).add(row);
					}
					mappingModel.rhsRowMap.put(key, row);
				}
			} else if (target instanceof Event) {
				final Event event = (Event) target;

				// Assume we are a valid event to include if we get here
				final ChangeSetRowDataGroup group = ChangesetFactory.eINSTANCE.createChangeSetRowDataGroup();
				mappingModel.groups.add(group);

				final ChangeSetRowData row = ChangesetFactory.eINSTANCE.createChangeSetRowData();
				group.getMembers().add(row);

				final String eventName = event.name();

				// NOTE: GCO ARE NEVER EQUIV, BREAKS CONSISTENT SORTING IN CHANGE SET VIEW

				final String key = EquivalanceGroupBuilder.getElementKey(event);
				// TODO: Unique name?
				mappingModel.lhsRowMap.put(key, row);

				row.setLhsName(eventName);
				row.setLhsEvent(event);

				if (event instanceof ProfitAndLossContainer) {
					row.setLhsGroupProfitAndLoss((ProfitAndLossContainer) event);
				}
				if (event instanceof EventGrouping) {
					row.setEventGrouping((EventGrouping) event);
				}
				row.setVesselName(ChangeSetTransformerUtil.getName(event.getSequence()));
				row.setVesselShortName(ChangeSetTransformerUtil.getShortName(event.getSequence()));
			}
		}

		return mappingModel;
	}

	public static List<ChangeSetRow> generateChangeSetRows(final @NonNull MappingModel beforeMapping, final @NonNull MappingModel afterMapping) {

		// Now start to generate the ChangeSetRows. There should be one for each Cargo, open position or other Vessel event.

		generateSimpleBindings(beforeMapping, afterMapping);
		return generateRowData(beforeMapping, afterMapping);
	}

	/**
	 * Bind the before and after state based on identical keys. For spot market slots which are not identical, but have exactly one free equivalent, bind them. (For example DES Sale market in Feb 2017
	 * may use in stance 1 in the before state and instance 2 in the after state. This should be considered the same). This method sets the LHS/RHS links
	 * 
	 * @param beforeMapping
	 * @param afterMapping
	 * @param rows
	 * @param seenRows
	 */
	private static void generateSimpleBindings(final MappingModel beforeMapping, final MappingModel afterMapping) {
		// Bind the "simple" stuff together
		final Set<String> lhsKeys = new HashSet<>();
		lhsKeys.addAll(beforeMapping.lhsRowMap.keySet());
		lhsKeys.addAll(afterMapping.lhsRowMap.keySet());

		final Set<String> rhsKeys = new HashSet<>();
		rhsKeys.addAll(beforeMapping.rhsRowMap.keySet());
		rhsKeys.addAll(afterMapping.rhsRowMap.keySet());

		// Bind before and after data. For spots without a direct match, if there is exactly one other equivalent spot market option, bind to that. This could happen if the before used instance 1 and
		// the after used instance 2.
		// Note - this should part should be a second pass.
		final Set<String> tmp_lhsKeys = new HashSet<>(lhsKeys);

		// Pass 0: bind the identical values.
		// Pass 1: search for the equivalent spot market slots.
		for (int pass = 0; pass < 2; ++pass) {
			final Iterator<String> itr = tmp_lhsKeys.iterator();
			while (itr.hasNext()) {
				final String lhsKey = itr.next();
				final ChangeSetRowData fromData = beforeMapping.lhsRowMap.get(lhsKey);
				final ChangeSetRowData toData = afterMapping.lhsRowMap.get(lhsKey);
				if (fromData != null && toData != null) {
					fromData.setLhsLink(toData);
					toData.setLhsLink(fromData);

					if (fromData.getLoadSlot() instanceof SpotLoadSlot) {
						{
							final String mKey = getMarketSlotKey((SpotLoadSlot) fromData.getLoadSlot());
							beforeMapping.lhsRowMarketMap.get(mKey).remove(fromData);
						}
						if (toData.getLoadSlot() instanceof SpotLoadSlot) {
							final String mKey = getMarketSlotKey((SpotLoadSlot) toData.getLoadSlot());
							afterMapping.lhsRowMarketMap.get(mKey).remove(toData);
						} else {
							assert false; // Both slots should be spot
						}
					}

				} else if (fromData == null) {
					if (toData.getLhsLink() != null) {
						// Should be a spot already linked from a previous iteration
						continue;
					}
					boolean foundSpotMatch = false;
					if (toData.getLoadSlot() instanceof SpotLoadSlot) {
						final String mKey = getMarketSlotKey((SpotLoadSlot) toData.getLoadSlot());
						final List<ChangeSetRowData> beforeDataList = beforeMapping.lhsRowMarketMap.get(mKey);
						final List<ChangeSetRowData> afterDataList = afterMapping.lhsRowMarketMap.get(mKey);

						if ((afterDataList != null && afterDataList.size() == 1) && (beforeDataList != null && beforeDataList.size() == 1)) {
							final ChangeSetRowData afterData = afterDataList.get(0);
							final ChangeSetRowData beforeData = beforeDataList.get(0);
							beforeData.setLhsLink(afterData);
							afterData.setLhsLink(beforeData);

							afterDataList.remove(afterData);
							beforeDataList.remove(beforeData);

							foundSpotMatch = true;
						}
						if (!foundSpotMatch && pass == 0) {
							continue;
						}
					}
					if (!foundSpotMatch) {
						final ChangeSetRowData d = ChangesetFactory.eINSTANCE.createChangeSetRowData();
						beforeMapping.lhsRowMap.put(lhsKey, d);
						d.setPrimaryRecord(toData.isPrimaryRecord());
						d.setLhsName(toData.getLhsName());
						d.setRowDataGroup(ChangesetFactory.eINSTANCE.createChangeSetRowDataGroup());
						d.setLhsLink(toData);
						toData.setLhsLink(d);
					}
				} else if (toData == null) {
					if (fromData.getLhsLink() != null) {
						// Should be a spot already linked from a previous iteration
						continue;
					}
					boolean foundSpotMatch = false;
					if (fromData.getLoadSlot() instanceof SpotLoadSlot) {
						final String mKey = getMarketSlotKey((SpotLoadSlot) fromData.getLoadSlot());
						final List<ChangeSetRowData> beforeDataList = beforeMapping.lhsRowMarketMap.get(mKey);
						final List<ChangeSetRowData> afterDataList = afterMapping.lhsRowMarketMap.get(mKey);

						if ((afterDataList != null && afterDataList.size() == 1) && (beforeDataList != null && beforeDataList.size() == 1)) {
							final ChangeSetRowData afterData = afterDataList.get(0);
							final ChangeSetRowData beforeData = beforeDataList.get(0);
							afterData.setLhsLink(beforeData);
							beforeData.setLhsLink(afterData);

							afterDataList.remove(afterData);
							beforeDataList.remove(beforeData);

							foundSpotMatch = true;
						}
						if (!foundSpotMatch && pass == 0) {
							continue;
						}
					}
					if (!foundSpotMatch) {
						final ChangeSetRowData d = ChangesetFactory.eINSTANCE.createChangeSetRowData();
						afterMapping.lhsRowMap.put(lhsKey, d);
						d.setPrimaryRecord(fromData.isPrimaryRecord());
						d.setLhsName(fromData.getLhsName());
						d.setRowDataGroup(ChangesetFactory.eINSTANCE.createChangeSetRowDataGroup());
						d.setLhsLink(fromData);
						fromData.setLhsLink(d);
					}
				}
				itr.remove();
			}
		}

		final Set<String> tmp_rhsKeys = new HashSet<>(rhsKeys);
		// Pass 0: bind the identical values.
		// Pass 1: search for the equivalent spot market slots.
		for (int pass = 0; pass < 2; ++pass) {
			final Iterator<String> itr = tmp_rhsKeys.iterator();
			while (itr.hasNext()) {
				final String rhsKey = itr.next();
				final ChangeSetRowData fromData = beforeMapping.rhsRowMap.get(rhsKey);
				final ChangeSetRowData toData = afterMapping.rhsRowMap.get(rhsKey);
				if (fromData != null && toData != null) {
					fromData.setRhsLink(toData);
					toData.setRhsLink(fromData);

					if (fromData.getDischargeSlot() instanceof SpotDischargeSlot) {
						{
							final String mKey = getMarketSlotKey((SpotDischargeSlot) fromData.getDischargeSlot());
							beforeMapping.rhsRowMarketMap.get(mKey).remove(fromData);
						}
						if (toData.getDischargeSlot() instanceof SpotDischargeSlot) {
							final String mKey = getMarketSlotKey((SpotDischargeSlot) toData.getDischargeSlot());
							afterMapping.rhsRowMarketMap.get(mKey).remove(toData);
						} else {
							assert false; // Both slots should be spot
						}
					}

				} else if (fromData == null) {
					if (toData.getRhsLink() != null) {
						// Should be a spot already linked from a previous iteration
						continue;
					}
					boolean foundSpotMatch = false;
					if (toData.getDischargeSlot() instanceof SpotDischargeSlot) {
						final String mKey = getMarketSlotKey((SpotDischargeSlot) toData.getDischargeSlot());
						final List<ChangeSetRowData> beforeDataList = beforeMapping.rhsRowMarketMap.get(mKey);
						final List<ChangeSetRowData> afterDataList = afterMapping.rhsRowMarketMap.get(mKey);

						if ((afterDataList != null && afterDataList.size() == 1) && (beforeDataList != null && beforeDataList.size() == 1)) {
							final ChangeSetRowData afterData = afterDataList.get(0);
							final ChangeSetRowData beforeData = beforeDataList.get(0);
							afterData.setRhsLink(beforeData);
							beforeData.setRhsLink(afterData);

							afterDataList.remove(afterData);
							beforeDataList.remove(beforeData);

							foundSpotMatch = true;
						}
						if (!foundSpotMatch && pass == 0) {
							continue;
						}
					}
					if (!foundSpotMatch) {
						final ChangeSetRowData d = ChangesetFactory.eINSTANCE.createChangeSetRowData();
						beforeMapping.rhsRowMap.put(rhsKey, d);
						d.setPrimaryRecord(toData.isPrimaryRecord());
						d.setRhsName(toData.getRhsName());
						d.setRowDataGroup(ChangesetFactory.eINSTANCE.createChangeSetRowDataGroup());
						d.setRhsLink(toData);
						toData.setRhsLink(d);
					}
				} else if (toData == null) {
					if (fromData.getRhsLink() != null) {
						// Should be a spot already linked from a previous iteration
						continue;
					}
					boolean foundSpotMatch = false;
					if (fromData.getDischargeSlot() instanceof SpotDischargeSlot) {
						final String mKey = getMarketSlotKey((SpotDischargeSlot) fromData.getDischargeSlot());
						final List<ChangeSetRowData> beforeDataList = beforeMapping.rhsRowMarketMap.get(mKey);
						final List<ChangeSetRowData> afterDataList = afterMapping.rhsRowMarketMap.get(mKey);

						if ((afterDataList != null && afterDataList.size() == 1) && (beforeDataList != null && beforeDataList.size() == 1)) {
							final ChangeSetRowData afterData = afterDataList.get(0);
							final ChangeSetRowData beforeData = beforeDataList.get(0);
							afterData.setRhsLink(beforeData);
							beforeData.setRhsLink(afterData);

							afterDataList.remove(afterData);
							beforeDataList.remove(beforeData);

							foundSpotMatch = true;
						}
						if (!foundSpotMatch && pass == 0) {
							continue;
						}
					}
					if (!foundSpotMatch) {
						final ChangeSetRowData d = ChangesetFactory.eINSTANCE.createChangeSetRowData();
						afterMapping.rhsRowMap.put(rhsKey, d);
						d.setPrimaryRecord(true);
						d.setRhsName(fromData.getRhsName());
						d.setRowDataGroup(ChangesetFactory.eINSTANCE.createChangeSetRowDataGroup());
						d.setRhsLink(fromData);
						fromData.setRhsLink(d);
					}
				}
				itr.remove();
			}
		}
	}

	/**
	 * Given the rows with the correct LHS/RHS links, generate the ChangeSetRows
	 * 
	 * @param beforeMapping
	 * @param afterMapping
	 * @param rows
	 * @param seenRows
	 */
	private static List<ChangeSetRow> generateRowData(final MappingModel beforeMapping, final MappingModel afterMapping) {
		final List<ChangeSetRow> rows = new LinkedList<ChangeSetRow>();

		final Set<String> lhsKeys = new LinkedHashSet<>();
		lhsKeys.addAll(beforeMapping.lhsRowMap.keySet());
		lhsKeys.addAll(afterMapping.lhsRowMap.keySet());

		final Set<String> rhsKeys = new LinkedHashSet<>();
		rhsKeys.addAll(beforeMapping.rhsRowMap.keySet());
		rhsKeys.addAll(afterMapping.rhsRowMap.keySet());

		// First pass - generate rows data based on LHS After state
		{
			final Iterator<String> lhsIterator = lhsKeys.iterator();
			while (lhsIterator.hasNext()) {
				final String key = lhsIterator.next();

				final ChangeSetRowData afterData = afterMapping.lhsRowMap.get(key);
				if (afterData != null && afterData.isPrimaryRecord()) {
					final ChangeSetRow row = ChangesetFactory.eINSTANCE.createChangeSetRow();
					row.setAfterData(afterData.getRowDataGroup());

					// If there is a before record, link it.
					if (afterData.getLhsLink() != null) {
						row.setBeforeData(afterData.getLhsLink().getRowDataGroup());
					} else {
						row.setBeforeData(ChangesetFactory.eINSTANCE.createChangeSetRowDataGroup());
					}

					rows.add(row);
				}
			}
		}

		// TODO: I;ve fixed how the view looks. We need to test it. Make sure existing tests are not broken.
		// Also need to update tests and make sure the output is as expected -

		// Pass two - look for RHS cases not covered before. I.e. open discharges
		{
			final Iterator<String> rhsIterator = rhsKeys.iterator();
			while (rhsIterator.hasNext()) {
				final String key = rhsIterator.next();
				// Check the before state
				{
					final ChangeSetRowData data = beforeMapping.rhsRowMap.get(key);
					if (data != null && data.isPrimaryRecord() && data.getLhsName() == null) {
						boolean handled = false;
						final ChangeSetRowData rhsLink = data.getRhsLink();
						if (rhsLink != null) {
							// LDD? Merge before records)
							if (!rhsLink.isPrimaryRecord()) {
								final ChangeSetRowDataGroup rowDataGroup = rhsLink.getRowDataGroup().getMembers().get(0).getLhsLink().getRowDataGroup();
								data.setRowDataGroup(rowDataGroup);
								handled = true;
							}
						}
						if (!handled) {
							final ChangeSetRow row = ChangesetFactory.eINSTANCE.createChangeSetRow();
							row.setBeforeData(data.getRowDataGroup());

							if (rhsLink != null && rhsLink.getLhsName() == null) {
								final ChangeSetRowDataGroup rowDataGroup = rhsLink.getRowDataGroup();
								row.setAfterData(rowDataGroup);
							} else {
								row.setAfterData(ChangesetFactory.eINSTANCE.createChangeSetRowDataGroup());
							}
							rows.add(row);
						}
					}
				}
				// Check the after state
				{
					final ChangeSetRowData data = afterMapping.rhsRowMap.get(key);
					if (data != null && !data.isPrimaryRecord()) {
						continue;
					}
					if (data != null && data.getLhsName() == null) {
						boolean handled = false;
						final ChangeSetRowData rhsLink = data.getRhsLink();
						if (rhsLink != null) {
							// LDD? Merge after records)
							if (!rhsLink.isPrimaryRecord()) {
								final ChangeSetRowDataGroup rowDataGroup = rhsLink.getRowDataGroup().getMembers().get(0).getLhsLink().getRowDataGroup();
								data.setRowDataGroup(rowDataGroup);
								handled = true;
							}
						}
						if (!handled) {
							final ChangeSetRow row = ChangesetFactory.eINSTANCE.createChangeSetRow();
							row.setAfterData(data.getRowDataGroup());

							if (data.getRhsLink() != null && data.getRhsLink().getLhsName() == null) {
								row.setBeforeData(data.getRhsLink().getRowDataGroup());
							} else {
								row.setBeforeData(ChangesetFactory.eINSTANCE.createChangeSetRowDataGroup());
							}
							rows.add(row);
						}
					}
				}
			}

		}
		return rows;
	}

	public static void convertToSortedGroups(@NonNull final Map<ChangeSetTableRow, Collection<ChangeSetTableRow>> rowToGroups) {

		for (final Map.Entry<ChangeSetTableRow, Collection<ChangeSetTableRow>> entry : rowToGroups.entrySet()) {
			final Collection<ChangeSetTableRow> value = entry.getValue();
			if (value.size() < 2) {
				continue;
			}
			if (value instanceof Set<?>) {
				if (true) {
					// Convert to list and sort.
					final List<ChangeSetTableRow> newGroup = new ArrayList<>(value.size());

					ChangeSetTableRow firstAlphabetically = null;
					ChangeSetTableRow head = null;
					for (final ChangeSetTableRow r : value) {
						if (r.getNextLHS() == null) {
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
						head = head.getPreviousRHS();
					}

					entry.setValue(newGroup);
				}

			}
		}

	}

	public static void updateRowGroups(@NonNull final ChangeSetTableRow row, @NonNull final Map<ChangeSetTableRow, Collection<ChangeSetTableRow>> rowToGroups) {
		if (rowToGroups.containsKey(row)) {
			return;
		}
		// Try and find an existing group.
		Collection<ChangeSetTableRow> group = null;
		if (row.getNextLHS() != null) {
			if (rowToGroups.containsKey(row.getNextLHS())) {
				group = rowToGroups.get(row.getNextLHS());
			}
		} else if (row.getPreviousRHS() != null) {
			if (rowToGroups.containsKey(row.getPreviousRHS())) {
				group = rowToGroups.get(row.getPreviousRHS());
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
		if (row.getNextLHS() != null) {
			if (rowToGroups.containsKey(row.getNextLHS())) {
				final Collection<ChangeSetTableRow> otherGroup = rowToGroups.get(row.getNextLHS());
				if (otherGroup != null && group != otherGroup) {
					group.addAll(otherGroup);
					for (final ChangeSetTableRow r : otherGroup) {
						rowToGroups.put(r, group);
					}
				}
			}
		}
		if (row.getPreviousRHS() != null) {
			if (rowToGroups.containsKey(row.getPreviousRHS())) {
				final Collection<ChangeSetTableRow> otherGroup = rowToGroups.get(row.getPreviousRHS());
				if (otherGroup != null && group != otherGroup) {
					group.addAll(otherGroup);
					for (final ChangeSetTableRow r : otherGroup) {
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
			final Vessel vessel = ((VesselAvailability) t).getVessel();
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
	public static String getKeyName(@Nullable final SlotAllocation slotAllocation) {

		if (slotAllocation == null) {
			return null;
		}
		final Slot slot = slotAllocation.getSlot();
		if (slot == null) {
			return null;
		}
		if (slot instanceof SpotSlot) {
			final String key = EquivalanceGroupBuilder.getElementKey(slot);
			final StringBuilder sb = new StringBuilder();
			final CargoAllocation cargoAllocation = slotAllocation.getCargoAllocation();
			if (cargoAllocation != null) {
				for (final SlotAllocation slotAllocation2 : cargoAllocation.getSlotAllocations()) {
					final Slot slot2 = slotAllocation2.getSlot();
					if (slotAllocation == slotAllocation2) {
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

	@Nullable
	public static String getKeyName(@Nullable final OpenSlotAllocation slotAllocation) {

		if (slotAllocation == null) {
			return null;
		}
		final Slot slot = slotAllocation.getSlot();
		if (slot == null) {
			return null;
		}
		if (slot instanceof SpotSlot) {
			assert false;
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

	public static long getRowProfitAndLossValue(@Nullable final ChangeSetRowDataGroup row, final @NonNull ToLongFunction<@Nullable ProfitAndLossContainer> f) {
		if (row == null) {
			return 0L;
		}

		final Set<ProfitAndLossContainer> containers = new HashSet<ProfitAndLossContainer>();
		for (final ChangeSetRowData d : row.getMembers()) {
			containers.add(d.getLhsGroupProfitAndLoss());
			containers.add(d.getRhsGroupProfitAndLoss());
			containers.add(d.getOpenLoadAllocation());
			containers.add(d.getOpenDischargeAllocation());
		}

		long sum = 0L;
		for (final ProfitAndLossContainer c : containers) {
			if (c == null) {
				continue;
			}
			sum += f.applyAsLong(c);
		}
		return sum;
	}

	public static void sortRows(@NonNull final List<ChangeSetTableRow> rows, @Nullable final NamedObject targetToSortFirst) {
		// Sort into wiring groups.
		final Map<ChangeSetTableRow, Collection<ChangeSetTableRow>> rowToRowGroup = new HashMap<>();
		for (final ChangeSetTableRow row : rows) {
			ChangeSetTransformerUtil.updateRowGroups(row, rowToRowGroup);
		}

		ChangeSetTransformerUtil.convertToSortedGroups(rowToRowGroup);
		for (final ChangeSetTableRow row : rows) {
			assert rowToRowGroup.containsKey(row);
			// updateRowGroups(row, rowToRowGroup);
		}
		Collections.sort(rows, new Comparator<ChangeSetTableRow>() {

			@Override
			public int compare(final ChangeSetTableRow o1, final ChangeSetTableRow o2) {

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
						final Collection<ChangeSetTableRow> group = rowToRowGroup.get(o1);
						// Start from the first element in the sorted group
						ChangeSetTableRow link = group.iterator().next();
						while (link != null) {
							if (link == o1) {
								return -1;
							}
							if (link == o2) {
								return 1;
							}
							link = link.getPreviousRHS();
						}
						assert false;
					} else {
						final Collection<ChangeSetTableRow> group1 = rowToRowGroup.get(o1);
						final Collection<ChangeSetTableRow> group2 = rowToRowGroup.get(o2);

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
		if (targetToSortFirst != null) {
			ChangeSetTableRow targetRow = null;
			for (final ChangeSetTableRow row : rows) {
				if (targetToSortFirst instanceof VesselEvent && !row.isLhsSlot() && targetToSortFirst.getName().equals(row.getLhsName())) {
					targetRow = row;
					break;
				}
				if (targetToSortFirst instanceof LoadSlot && row.isLhsSlot() && targetToSortFirst.getName().equals(row.getLhsName())) {
					targetRow = row;
					break;
				}
				if (targetToSortFirst instanceof DischargeSlot && row.isRhsSlot() && targetToSortFirst.getName().equals(row.getRhsName())) {
					targetRow = row;
					break;
				}
			}

			if (targetRow != null) {
				final Collection<ChangeSetTableRow> collection = rowToRowGroup.get(targetRow);
				final List<ChangeSetTableRow> l = new LinkedList<>(collection);
				Collections.rotate(l, -l.indexOf(targetRow));

				rows.removeAll(l);
				l.addAll(rows);

				rows.clear();
				rows.addAll(l);
			}
		}

	}

	public static void calculateMetrics(@NonNull final ChangeSet changeSet, @NonNull final Schedule fromSchedule, @NonNull final Schedule toSchedule, final boolean isBase) {
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

	public static boolean isSet(@Nullable final String str) {
		return str != null && !str.isEmpty();
	}

	public static void setRowFlags(@NonNull final List<ChangeSetRow> rows) {
		for (final ChangeSetRow row : rows) {

			final ChangeSetRowDataGroup beforeGroup = row.getBeforeData();
			final ChangeSetRowDataGroup afterGroup = row.getAfterData();

			if (beforeGroup != null && afterGroup != null) {
				if (beforeGroup.getMembers().size() != afterGroup.getMembers().size()) {
					row.setWiringChange(true);
				} else {
					int beforePrimaryCount = 0;
					int afterPrimaryCount = 0;
					for (int i = 0; i < beforeGroup.getMembers().size(); ++i) {
						final ChangeSetRowData beforeData = beforeGroup.getMembers().get(i);
						final ChangeSetRowData afterData = afterGroup.getMembers().get(i);

						if (beforeData.isPrimaryRecord()) {
							++beforePrimaryCount;
						}
						if (afterData.isPrimaryRecord()) {
							++afterPrimaryCount;
						}

						if (i == 0) {
							row.setVesselChange(!Objects.equal(beforeData.getVesselName(), afterData.getVesselName()));
						}

						if (!Objects.equal(beforeData.getRhsName(), afterData.getRhsName())) {
							row.setWiringChange(true);
						} else {
							if (beforeData.getDischargeSlot() instanceof SpotSlot || afterData.getDischargeSlot() instanceof SpotSlot) {
								// Check if both spot slots otherwise wiring change!
								if (!(beforeData.getDischargeSlot() instanceof SpotSlot) || !(afterData.getDischargeSlot() instanceof SpotSlot)) {
									row.setWiringChange(true);
								} else {
									final SpotMarket beforeMarket = ((SpotSlot) beforeData.getDischargeSlot()).getMarket();
									final SpotMarket afterMarket = ((SpotSlot) afterData.getDischargeSlot()).getMarket();
									// Same name but different market type,
									if (beforeMarket.eClass() != afterMarket.eClass()) {
										row.setWiringChange(true);
									}
								}
							}
						}
					}
					if (beforePrimaryCount != afterPrimaryCount) {
						row.setWiringChange(true);
					}
				}
			}
		}
	}

	public static void filterRows(final List<ChangeSetTableRow> rows) {
		final Iterator<ChangeSetTableRow> itr = rows.iterator();
		// TEMP FILTERING _ DOES NOT WORK WITH LDD - we filter the wrong stuff...
		final int size = 1;
		while (itr.hasNext()) {
			final ChangeSetTableRow changeSetTableRow = itr.next();

			if (size == 1) {
				if (!changeSetTableRow.isWiringChange() && !changeSetTableRow.isVesselChange()) {
					long a = ChangeSetKPIUtil.getPNL(changeSetTableRow, ResultType.Before);
					long b = ChangeSetKPIUtil.getPNL(changeSetTableRow, ResultType.After);
					if (a == b) {
						a = ChangeSetKPIUtil.getViolations(changeSetTableRow, ResultType.Before);
						b = ChangeSetKPIUtil.getViolations(changeSetTableRow, ResultType.After);
						if (a == b) {
							a = ChangeSetKPIUtil.getLateness(changeSetTableRow, ResultType.Before)[FlexType.WithoutFlex.ordinal()];
							b = ChangeSetKPIUtil.getLateness(changeSetTableRow, ResultType.After)[FlexType.WithoutFlex.ordinal()];
							if (a == b) {
								itr.remove();
							}
						}
					}
				}
			}
		}
	}

	public static <T extends SpotSlot & Slot> String getMarketSlotKey(final T slot) {
		final SpotMarket market = slot.getMarket();
		return String.format("%s-%s-%04d-%02d", getSlotTypePrefix(slot), market.getName(), slot.getWindowStart().getYear(), slot.getWindowStart().getMonthValue());
	}

	protected static @NonNull String getSlotTypePrefix(final Slot slot) {
		String prefix;
		if (slot instanceof LoadSlot) {
			final LoadSlot loadSlot = (LoadSlot) slot;
			if (loadSlot.isDESPurchase()) {
				prefix = "des-purchase";
			} else {
				prefix = "fob-purchase";
			}
		} else {
			final DischargeSlot dischargeSlot = (DischargeSlot) slot;
			if (dischargeSlot.isFOBSale()) {
				prefix = "fob-sale";
			} else {
				prefix = "des-sale";
			}
		}
		return prefix;
	}
}

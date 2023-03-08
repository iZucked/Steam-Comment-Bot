/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.ToLongFunction;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.base.Objects;
import com.mmxlabs.common.time.Days;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetKPIUtil.FlexType;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetKPIUtil.ResultType;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowDataGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetVesselType;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetWiringGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics;
import com.mmxlabs.lingo.reports.views.changeset.model.Metrics;
import com.mmxlabs.lingo.reports.views.schedule.formatters.VesselAssignmentFormatter;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.CharterLengthEvent;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.GroupedCharterLengthEvent;
import com.mmxlabs.models.lng.schedule.GroupedCharterOutEvent;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.PaperDealAllocation;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Purge;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
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
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.mmxcore.NamedObject;

@NonNullByDefault
public final class ChangeSetTransformerUtil {

	/**
	 * How many days difference needed to count as a major change
	 */
	private static final int MAJOR_CHANGE_DAYS_THRESHOLD = 2;

	private ChangeSetTransformerUtil() {

	}

	/**
	 * Data structure to hold element ID to row mappings.
	 *
	 */
	public static class MappingModel {
		public final List<ChangeSetRowDataGroup> groups = new LinkedList<>();

		public final Map<String, @Nullable ChangeSetRowData> lhsRowMap = new LinkedHashMap<>();
		public final Map<String, @Nullable ChangeSetRowData> rhsRowMap = new LinkedHashMap<>();

		public final Map<String, @Nullable List<ChangeSetRowData>> lhsRowMarketMap = new LinkedHashMap<>();
		public final Map<String, @Nullable List<ChangeSetRowData>> rhsRowMarketMap = new LinkedHashMap<>();

	}

	/**
	 * Extract all Change Set targets from a {@link Schedule}.
	 * 
	 * @param schedule
	 * @return
	 */
	public static List<EObject> extractTargets(final Schedule schedule) {

		final List<EObject> targets = new LinkedList<>();
		for (final CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {
			targets.add(cargoAllocation);
		}
		for (final OpenSlotAllocation openSlotAllocation : schedule.getOpenSlotAllocations()) {
			targets.add(openSlotAllocation);
		}
		for (final PaperDealAllocation paperDealAllocation : schedule.getPaperDealAllocations()) {
			targets.add(paperDealAllocation);
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
					} else if (event instanceof Purge) {
						continue;
					}

					else if (event instanceof StartEvent //
							|| event instanceof EndEvent) {
						if (sequence.getSequenceType() == SequenceType.VESSEL //
								|| sequence.getSequenceType() == SequenceType.SPOT_VESSEL) {
							// Only include these for fleet or spot vessels.
						} else {
							continue;
						}
					} else if (event instanceof VesselEventVisit) {
						// Keep going!
					} else if (event instanceof GeneratedCharterOut) {
						// Keep going!
					} else if (event instanceof CharterLengthEvent) {
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
	 * Given a list of target {@link EObject}s, generate a {@link MappingModel}
	 * which can be used to compare against other {@link MappingModel}s
	 * 
	 * @param targets
	 * @return
	 */
	public static MappingModel generateMappingModel(final Collection<EObject> targets) {

		final MappingModel mappingModel = new MappingModel();
		final Map<Sequence, GroupedCharterLengthEvent> extraLengthEvents = new HashMap<>();
		final Map<Sequence, GroupedCharterOutEvent> extraGCOEvents = new HashMap<>();
		// Split this code out as it is used in two places.
		final Consumer<Event> eventMapper = event -> {
			// Assume we are a valid event to include if we get here
			final ChangeSetRowDataGroup group = ChangesetFactory.eINSTANCE.createChangeSetRowDataGroup();
			mappingModel.groups.add(group);

			final ChangeSetRowData row = ChangesetFactory.eINSTANCE.createChangeSetRowData();
			group.getMembers().add(row);

			final String eventName = event.name();

			// NOTE: GCO ARE NEVER EQUIV, BREAKS CONSISTENT SORTING IN CHANGE SET VIEW

			final String key = ElementKeyUtil.getElementKey(event);
			// TODO: Unique name?
			mappingModel.lhsRowMap.put(key, row);

			row.setLhsName(eventName);
			row.setLhsEvent(event);

			if (event instanceof final ProfitAndLossContainer plc) {
				row.setLhsGroupProfitAndLoss(plc);
			}
			if (event instanceof final EventGrouping eg) {
				row.setEventGrouping(eg);
			}
			final Sequence sequence = event.getSequence();
			row.setVesselName(ChangeSetTransformerUtil.getName(sequence));
			row.setVesselShortName(ChangeSetTransformerUtil.getShortName(sequence));
			row.setVesselType(ChangeSetTransformerUtil.getVesselType(sequence));
			row.setVesselCharterNumber(ChangeSetTransformerUtil.getCharterNumber(sequence));
		};
		for (final EObject target : targets) {
			if (target instanceof final CargoAllocation cargoAllocation) {

				// The group data - for LDD an other complex cargoes this stores the
				// relationship between ChangeSetRowData's
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

				// Create a row for each pair of load and discharge slots in the cargo. This may
				// lead to a row with only one slot
				for (int i = 0; i < Math.max(loadAllocations.size(), dischargeAllocations.size()); ++i) {
					final ChangeSetRowData row = ChangesetFactory.eINSTANCE.createChangeSetRowData();
					group.getMembers().add(row);

					row.setPrimaryRecord(i == 0);
					if (i == 0) {
						row.setLhsGroupProfitAndLoss(cargoAllocation);
						row.setEventGrouping(cargoAllocation);
						final Sequence sequence = cargoAllocation.getSequence();
						row.setVesselName(ChangeSetTransformerUtil.getName(sequence));
						row.setVesselShortName(ChangeSetTransformerUtil.getShortName(sequence));
						row.setVesselType(ChangeSetTransformerUtil.getVesselType(sequence));
						row.setVesselCharterNumber(ChangeSetTransformerUtil.getCharterNumber(sequence));
					}

					if (i < loadAllocations.size()) {
						final SlotAllocation loadAllocation = loadAllocations.get(i);
						// We sometimes get class cast exceptions here. The SlotAllocation has the wrong
						// SlotAllocationType.
						// This is probably caused from schedule models pre-dating the
						// SlotAllocationType. The purchase type appears to be used as default.
						// We should add in migration into the relevant place to add it in (NOT CURRENT
						// MIGRATION).
						final LoadSlot ls = (LoadSlot) loadAllocation.getSlot();

						final String key = ChangeSetTransformerUtil.getKeyName(loadAllocation);
						if (key != null) {
							final String name = ChangeSetTransformerUtil.getRowName(ls);
	
							row.setLhsName(name);
							row.setLoadAllocation(loadAllocation);
							row.setLoadSlot(ls);
	
							row.setLhsSlot(true);
							row.setLhsOptional(ls.isOptional());
							row.setLhsNonShipped(ls.isDESPurchase());
	
							if (ls instanceof final SpotLoadSlot spotSlot) {
								final String mKey = getMarketSlotKey(spotSlot);
								mappingModel.lhsRowMarketMap.computeIfAbsent(mKey, k -> new LinkedList<>()).add(row);
								row.setLhsSpot(true);
							}

							mappingModel.lhsRowMap.put(key, row);
						}
					}
					SlotAllocation dischargeAllocation = null;
					if (i < dischargeAllocations.size()) {
						dischargeAllocation = dischargeAllocations.get(i);
						final DischargeSlot ds = (DischargeSlot) dischargeAllocation.getSlot();

						final String key = ChangeSetTransformerUtil.getKeyName(dischargeAllocation);
						if (key != null) {
							final String name = ChangeSetTransformerUtil.getRowName(ds);
	
							row.setRhsName(name);
							row.setDischargeAllocation(dischargeAllocation);
							row.setDischargeSlot(ds);
	
							row.setRhsSlot(true);
							row.setRhsOptional(ds.isOptional());
							row.setRhsNonShipped(ds.isFOBSale());
	
							if (ds instanceof final SpotDischargeSlot spotSlot) {
								final String mKey = getMarketSlotKey(spotSlot);
								mappingModel.rhsRowMarketMap.computeIfAbsent(mKey, k -> new LinkedList<>()).add(row);
	
								row.setRhsSpot(true);
							}
							mappingModel.rhsRowMap.put(key, row);
						}
					}
				}
			} else if (target instanceof final OpenSlotAllocation openSlotAllocation) {
				final ChangeSetRowDataGroup group = ChangesetFactory.eINSTANCE.createChangeSetRowDataGroup();
				mappingModel.groups.add(group);

				final ChangeSetRowData row = ChangesetFactory.eINSTANCE.createChangeSetRowData();
				group.getMembers().add(row);

				final Slot<?> slot = openSlotAllocation.getSlot();

				final String key = ChangeSetTransformerUtil.getKeyName(openSlotAllocation);
				if (key != null) {
					final String name = ChangeSetTransformerUtil.getRowName(slot);
					if (slot instanceof final LoadSlot ls) {
						row.setLhsName(name);
						row.setOpenLoadAllocation(openSlotAllocation);
						row.setLoadSlot(ls);
						row.setLhsGroupProfitAndLoss(openSlotAllocation);
	
						row.setLhsSlot(true);
						row.setLhsOptional(ls.isOptional());
						row.setLhsNonShipped(ls.isDESPurchase());
	
						if (slot instanceof final SpotLoadSlot spotSlot) {
							final String mKey = getMarketSlotKey(spotSlot);
							mappingModel.lhsRowMarketMap.computeIfAbsent(mKey, k -> new LinkedList<>()).add(row);
	
							row.setLhsSpot(true);
						}
						mappingModel.lhsRowMap.put(key, row);
	
					} else if (slot instanceof final DischargeSlot ds) {
						row.setRhsName(name);
						row.setOpenDischargeAllocation(openSlotAllocation);
						row.setDischargeSlot(ds);
						row.setRhsGroupProfitAndLoss(openSlotAllocation);
	
						row.setRhsSlot(true);
						row.setRhsOptional(ds.isOptional());
						row.setRhsNonShipped(ds.isFOBSale());
						if (slot instanceof final SpotDischargeSlot spotSlot) {
							final String mKey = getMarketSlotKey(spotSlot);
							mappingModel.rhsRowMarketMap.computeIfAbsent(mKey, k -> new LinkedList<>()).add(row);
							row.setRhsSpot(true);
						}
	
						mappingModel.rhsRowMap.put(key, row);
					}
				}
			} else if (target instanceof final Event event) {
				if (event instanceof final CharterLengthEvent charterLengthEvent) {
					// Record these events so we can group them up later
					extraLengthEvents.computeIfAbsent(charterLengthEvent.getSequence(), k -> ScheduleFactory.eINSTANCE.createGroupedCharterLengthEvent()).getEvents()
							.addAll(charterLengthEvent.getEvents());
				} else if (event instanceof final GeneratedCharterOut charterLengthEvent) {
					// Record these events so we can group them up later
					extraGCOEvents.computeIfAbsent(charterLengthEvent.getSequence(), k -> ScheduleFactory.eINSTANCE.createGroupedCharterOutEvent()).getEvents().addAll(charterLengthEvent.getEvents());

				} else {
					eventMapper.accept(event);
				}
			} else if (target instanceof final PaperDealAllocation paperDealAllocation) {
				final PaperDeal paperDeal = paperDealAllocation.getPaperDeal();
				final ChangeSetRowDataGroup group = ChangesetFactory.eINSTANCE.createChangeSetRowDataGroup();
				mappingModel.groups.add(group);

				final ChangeSetRowData row = ChangesetFactory.eINSTANCE.createChangeSetRowData();
				group.getMembers().add(row);

				row.setPaperDealAllocation(paperDealAllocation);
				row.setLhsGroupProfitAndLoss(paperDealAllocation);
				row.setLhsName(paperDeal.getName());
				final String key = paperDeal.getName();
				if (key != null) {
					mappingModel.lhsRowMap.put(key, row);
				}
			}
		}

		for (final GroupedCharterLengthEvent cle : extraLengthEvents.values()) {
			long pnl1 = 0L;
			long pnl2 = 0L;
			for (final Event e : cle.getEvents()) {
				if (e instanceof final CharterLengthEvent c) {
					cle.setLinkedSequence(c.getSequence());
				}
				if (e instanceof final ProfitAndLossContainer c) {
					pnl1 += c.getGroupProfitAndLoss().getProfitAndLoss();
					pnl2 += c.getGroupProfitAndLoss().getProfitAndLossPreTax();
				}
			}
			final GroupProfitAndLoss groupProfitAndLoss = ScheduleFactory.eINSTANCE.createGroupProfitAndLoss();
			groupProfitAndLoss.setProfitAndLoss(pnl1);
			groupProfitAndLoss.setProfitAndLossPreTax(pnl2);
			cle.setGroupProfitAndLoss(groupProfitAndLoss);

			// Create an entry for the new grouped item
			eventMapper.accept(cle);
		}
		for (final GroupedCharterOutEvent cle : extraGCOEvents.values()) {
			long pnl1 = 0L;
			long pnl2 = 0L;
			for (final Event e : cle.getEvents()) {
				if (e instanceof final GeneratedCharterOut c) {
					cle.setLinkedSequence(c.getSequence());
				}
				if (e instanceof final ProfitAndLossContainer c) {
					pnl1 += c.getGroupProfitAndLoss().getProfitAndLoss();
					pnl2 += c.getGroupProfitAndLoss().getProfitAndLossPreTax();
				}
			}
			final GroupProfitAndLoss groupProfitAndLoss = ScheduleFactory.eINSTANCE.createGroupProfitAndLoss();
			groupProfitAndLoss.setProfitAndLoss(pnl1);
			groupProfitAndLoss.setProfitAndLossPreTax(pnl2);
			cle.setGroupProfitAndLoss(groupProfitAndLoss);

			// Create an entry for the new grouped item
			eventMapper.accept(cle);
		}

		return mappingModel;
	}

	public static List<ChangeSetTableRow> generateChangeSetRows(final MappingModel beforeMapping, final MappingModel afterMapping) {

		// Link together ChangeSetRowData between the before and after cases.
		// This may add new data to account for elements existing on one side only
		generateSimpleBindings(beforeMapping, afterMapping);

		// Now start to generate the ChangeSetTableRows. There should be one for each
		// Cargo,
		// open position or other Vessel event or paper deal.
		final IChangeSetRowGenerationCustomiser rowGenerationCustomiser = new DefaultChangeSetRowGenerationCustomiser();
		return generateRowData(beforeMapping, afterMapping, rowGenerationCustomiser);
	}

	public static List<ChangeSetTableRow> generateChangeSetRows(final MappingModel beforeMapping, final MappingModel afterMapping, final IChangeSetRowGenerationCustomiser rowGenerationCustomiser) {

		// Link together ChangeSetRowData between the before and after cases.
		// This may add new data to account for elements existing on one side only
		generateSimpleBindings(beforeMapping, afterMapping);

		// Now start to generate the ChangeSetTableRows. There should be one for each
		// Cargo,
		// open position or other Vessel event or paper deal.
		return generateRowData(beforeMapping, afterMapping, rowGenerationCustomiser);
	}

	/**
	 * Bind the before and after state based on identical keys. For spot market
	 * slots which are not identical, but have exactly one free equivalent, bind
	 * them. (For example DES Sale market in Feb 2017 may use in stance 1 in the
	 * before state and instance 2 in the after state. This should be considered the
	 * same). This method sets the LHS/RHS links
	 * 
	 * @param beforeMapping
	 * @param afterMapping
	 * @param rows
	 * @param seenRows
	 */
	private static void generateSimpleBindings(final MappingModel beforeMapping, final MappingModel afterMapping) {
		// Bind the "simple" stuff together
		final Set<String> lhsKeys = new LinkedHashSet<>();
		lhsKeys.addAll(beforeMapping.lhsRowMap.keySet());
		lhsKeys.addAll(afterMapping.lhsRowMap.keySet());

		final Set<String> rhsKeys = new LinkedHashSet<>();
		rhsKeys.addAll(beforeMapping.rhsRowMap.keySet());
		rhsKeys.addAll(afterMapping.rhsRowMap.keySet());

		// Bind before and after data. For spots without a direct match, if there is
		// exactly one other equivalent spot market option, bind to that. This could
		// happen if the before used instance 1 and
		// the after used instance 2.
		// Note - this should part should be a second pass.
		final Set<String> tmpLhsKeys = new HashSet<>(lhsKeys);

		// Pass 0: bind the identical values.
		// Pass 1: search for the equivalent spot market slots.
		for (int pass = 0; pass < 2; ++pass) {
			final Iterator<String> itr = tmpLhsKeys.iterator();
			while (itr.hasNext()) {
				final String lhsKey = itr.next();
				final ChangeSetRowData fromData = beforeMapping.lhsRowMap.get(lhsKey);
				final ChangeSetRowData toData = afterMapping.lhsRowMap.get(lhsKey);
				if (fromData != null && toData != null) {
					fromData.setLhsLink(toData);
					toData.setLhsLink(fromData);

					if (fromData.getLoadSlot() instanceof final SpotLoadSlot spotSlot) {
						{
							final String mKey = getMarketSlotKey(spotSlot);
							beforeMapping.lhsRowMarketMap.get(mKey).remove(fromData);
						}
						if (toData.getLoadSlot() instanceof final SpotLoadSlot spotSlot2) {
							final String mKey = getMarketSlotKey(spotSlot2);
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
					// No record found. Either we are looking at a spot slot OR the position only
					// exists in one of the schedules

					boolean foundMatch = false;
					// Before we create a new record, is there a spare spot slot record we can use?
					if (toData.getLoadSlot() instanceof final SpotLoadSlot spotSlot) {
						final String mKey = getMarketSlotKey(spotSlot);
						final List<ChangeSetRowData> beforeDataList = beforeMapping.lhsRowMarketMap.get(mKey);
						final List<ChangeSetRowData> afterDataList = afterMapping.lhsRowMarketMap.get(mKey);

						if ((afterDataList != null && afterDataList.size() == 1) && (beforeDataList != null && beforeDataList.size() == 1)) {
							final ChangeSetRowData afterData = afterDataList.get(0);
							final ChangeSetRowData beforeData = beforeDataList.get(0);
							beforeData.setLhsLink(afterData);
							afterData.setLhsLink(beforeData);

							afterDataList.remove(afterData);
							beforeDataList.remove(beforeData);

							foundMatch = true;
						}
						if (!foundMatch && pass == 0) {
							continue;
						}
					}
					if (!foundMatch) {
						// No matching record, so create one so we have a before and after case.
						final ChangeSetRowData d = ChangesetFactory.eINSTANCE.createChangeSetRowData();
						beforeMapping.lhsRowMap.put(lhsKey, d);
						d.setPrimaryRecord(toData.isPrimaryRecord());
						d.setLhsName(toData.getLhsName());
						d.setRowDataGroup(ChangesetFactory.eINSTANCE.createChangeSetRowDataGroup());
						d.setLhsLink(toData);
						toData.setLhsLink(d);

						// Copy across attributes
						d.setLhsSlot(toData.isLhsSlot());
						d.setLhsSpot(toData.isLhsSpot());
						d.setLhsOptional(toData.isLhsOptional());
						d.setLhsNonShipped(toData.isLhsNonShipped());

						beforeMapping.groups.add(d.getRowDataGroup());

					}
				} else if (toData == null) {
					if (fromData.getLhsLink() != null) {
						// Should be a spot already linked from a previous iteration
						continue;
					}

					// No record found. Either we are looking at a spot slot OR the position only
					// exists in one of the schedules

					boolean foundSpotMatch = false;
					if (fromData.getLoadSlot() instanceof final SpotLoadSlot spotSlot) {
						final String mKey = getMarketSlotKey(spotSlot);
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

						d.setLhsSlot(fromData.isLhsSlot());
						d.setLhsSpot(fromData.isLhsSpot());
						d.setLhsOptional(fromData.isLhsOptional());
						d.setLhsNonShipped(fromData.isLhsNonShipped());

						fromData.setLhsLink(d);
						afterMapping.groups.add(d.getRowDataGroup());

					}
				}
				itr.remove();
			}
		}

		final Set<String> tmpRhsKeys = new HashSet<>(rhsKeys);
		// Pass 0: bind the identical values.
		// Pass 1: search for the equivalent spot market slots.
		for (int pass = 0; pass < 2; ++pass) {
			final Iterator<String> itr = tmpRhsKeys.iterator();
			LOOP_MAIN: while (itr.hasNext()) {
				final String rhsKey = itr.next();
				final ChangeSetRowData fromData = beforeMapping.rhsRowMap.get(rhsKey);
				final ChangeSetRowData toData = afterMapping.rhsRowMap.get(rhsKey);
				if (fromData != null && toData != null) {
					fromData.setRhsLink(toData);
					toData.setRhsLink(fromData);

					if (fromData.getDischargeSlot() instanceof final SpotDischargeSlot spotSlot) {
						{
							final String mKey = getMarketSlotKey(spotSlot);
							beforeMapping.rhsRowMarketMap.get(mKey).remove(fromData);
						}
						if (toData.getDischargeSlot() instanceof final SpotDischargeSlot spotSlot2) {
							final String mKey = getMarketSlotKey(spotSlot2);
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
					// No record found. Either we are looking at a spot slot OR the position only
					// exists in one of the schedules
					boolean foundSpotMatch = false;
					if (toData.getDischargeSlot() instanceof final SpotDischargeSlot spotSlot) {
						final String mKey = getMarketSlotKey(spotSlot);
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

						d.setRhsSlot(toData.isRhsSlot());
						d.setRhsSpot(toData.isRhsSpot());
						d.setRhsOptional(toData.isRhsOptional());
						d.setRhsNonShipped(toData.isRhsNonShipped());

						beforeMapping.groups.add(d.getRowDataGroup());

					}
				} else if (toData == null) {
					if (fromData.getRhsLink() != null) {
						// Should be a spot already linked from a previous iteration
						continue;
					}
					// No record found. Either we are looking at a spot slot OR the position only
					// exists in one of the schedules
					boolean foundSpotMatch = false;
					if (fromData.getDischargeSlot() instanceof final SpotDischargeSlot spotSlot) {
						final String mKey = getMarketSlotKey(spotSlot);
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
						// TODO: This may need to be replicated above to the other cases e.g. spot
						// loads.
						// Try and find a match within the current group to link up to (head -> tail
						// matching)
						// This case can occur if we have two wiring groups both involving the same spot
						// market/month combio
						if (fromData.getDischargeSlot() instanceof final SpotDischargeSlot spotSlot) {
							final String mKey = getMarketSlotKey(spotSlot);
							final List<ChangeSetRowData> beforeDataList = beforeMapping.rhsRowMarketMap.get(mKey);
							final List<ChangeSetRowData> afterDataList = afterMapping.rhsRowMarketMap.get(mKey);

							if (afterDataList != null) {

								for (final ChangeSetRowData d : afterDataList) {
									if (d.getRhsLink() != null) {
										// Assert false?
										continue;
									}

									ChangeSetRowData d2 = d.getLhsLink();
									boolean partOfGroup = false;
									while (d2 != null) {
										if (d2 == fromData) {
											partOfGroup = true;
										}
										if (d2.getRhsLink() != null) {
											// Find next toData equiv in chain
											d2 = d2.getRhsLink().getLhsLink();
										} else {
											d2 = null;
										}
									}
									if (partOfGroup) {
										d.setRhsLink(fromData);
										fromData.setRhsLink(d);

										afterDataList.remove(d);
										beforeDataList.remove(fromData);

										foundSpotMatch = true;

										continue LOOP_MAIN;
									}
								}
							}
						}
						final ChangeSetRowData d = ChangesetFactory.eINSTANCE.createChangeSetRowData();
						afterMapping.rhsRowMap.put(rhsKey, d);
						d.setPrimaryRecord(true);
						d.setRhsName(fromData.getRhsName());
						d.setRowDataGroup(ChangesetFactory.eINSTANCE.createChangeSetRowDataGroup());
						d.setRhsLink(fromData);
						fromData.setRhsLink(d);

						d.setRhsSlot(fromData.isRhsSlot());
						d.setRhsSpot(fromData.isRhsSpot());
						d.setRhsOptional(fromData.isRhsOptional());
						d.setRhsNonShipped(fromData.isRhsNonShipped());

						afterMapping.groups.add(d.getRowDataGroup());
					}
				}
				itr.remove();
			}
		}
	}

	/**
	 * Given the rows with the correct LHS/RHS links, generate the
	 * ChangeSetTablesRows
	 * 
	 * @param beforeMapping
	 * @param afterMapping
	 * @param rows
	 * @param seenRows
	 */
	private static List<ChangeSetTableRow> generateRowData(final MappingModel beforeMapping, final MappingModel afterMapping, final IChangeSetRowGenerationCustomiser rowGenerationCustomiser) {
		final List<ChangeSetTableRow> rows = new LinkedList<>();

		final Map<ChangeSetRowData, List<ChangeSetTableRow>> lm = new HashMap<>();
		final Map<ChangeSetRowData, List<ChangeSetTableRow>> rm = new HashMap<>();

		// The after case is the main case we want to see, so loop through the after
		// groups and generate the rows.
		// We will also record a mapping to the before links so the before loops can
		// attach to the correct row.

		for (final ChangeSetRowDataGroup afterGroup : afterMapping.groups) {
			// For LDD we automatically link the rows into the same wiring group.
			ChangeSetWiringGroup group = ChangesetFactory.eINSTANCE.createChangeSetWiringGroup();
			ChangeSetRowData firstData = null;
			for (final var afterData : afterGroup.getMembers()) {
				if (firstData == null) {
					firstData = afterData;
				}
				final ChangeSetTableRow changeSetTableRow = ChangesetFactory.eINSTANCE.createChangeSetTableRow();
				changeSetTableRow.setWiringGroup(group);

				// Is there a LHS event?
				if (ChangeSetTransformerUtil.isSet(afterData.getLhsName())) {
					changeSetTableRow.setLhsAfter(afterData);
					// Set link to before LHS data.
					if (afterData.getLhsLink() != null) {
						changeSetTableRow.setLhsBefore(afterData.getLhsLink());
						// Record the LHS before key to this row for the before loop.
						lm.computeIfAbsent(afterData.getLhsLink(), k -> new LinkedList<>()).add(changeSetTableRow);
					}
					rowGenerationCustomiser.setLhsValid(afterData, changeSetTableRow);
				} else {
					// Secondary row, add to the list so we can patch up the before case properly
					// for the second D.
					if (ChangeSetTransformerUtil.isSet(firstData.getLhsName())) {
						// Set link to before LHS data.
						if (firstData.getLhsLink() != null) {
							// Record the LHS before key to this row for the before loop.
							lm.computeIfAbsent(firstData.getLhsLink(), k -> new LinkedList<>()).add(changeSetTableRow);
						}
					}
				}
				// Is there a RHS event?
				if (ChangeSetTransformerUtil.isSet(afterData.getRhsName())) {
					rm.computeIfAbsent(afterData.getRhsLink(), k -> new LinkedList<>()).add(changeSetTableRow);
					changeSetTableRow.setCurrentRhsAfter(afterData);
					if (afterData.getRhsLink() != null) {
						// Here we link the previous data for the discharge. I.e. this is the load the
						// discharge was linked to in the before case.
						changeSetTableRow.setCurrentRhsBefore(afterData.getRhsLink());
					}

					rowGenerationCustomiser.setCurrentRhsValid(afterData, changeSetTableRow);
				}

				rows.add(changeSetTableRow);
			}
		}

		// Run through the before cases and link up.
		for (final ChangeSetRowDataGroup beforeGroup : beforeMapping.groups) {

			// The wiring group to use. There should always be one of LHS or a RHS after row
			// data mappings.
			ChangeSetWiringGroup group = null;
			for (final var before : beforeGroup.getMembers()) {
				{
					// Do we have an equivalent after case for the LHS?
					// Special handling for LDD. Need to handle LDD to new LDD, LD to LDD and LDD to
					// LD. We want to match the before and after rows by index. If one if larger
					// than the other, we ignore
					// the excess.
					final List<ChangeSetTableRow> changeSetTableRows = lm.get(beforeGroup.getMembers().get(0));
					if (changeSetTableRows != null) {
						for (int idx = beforeGroup.getMembers().indexOf(before); idx < Math.min(changeSetTableRows.size(), beforeGroup.getMembers().size()); ++idx) {
							var changeSetTableRow = changeSetTableRows.get(idx);

							// Merge wiring groups for all members of the before case.
							if (group == null) {
								group = changeSetTableRow.getWiringGroup();
							} else {
								final var groupA = group;
								final var groupB = changeSetTableRow.getWiringGroup();
								if (groupA != groupB) {
									for (final var b : new ArrayList<>(groupB.getRows())) {
										// Bi-directional reference will also move b out of groupB
										b.setWiringGroup(groupA);
									}
								}
							}
							// Here we link wiring change rows. I.e. we record the discharge the load was
							// paired to in the before case.
							if (ChangeSetTransformerUtil.isSet(before.getRhsName())) {
								changeSetTableRow.setPreviousRhsBefore(before);
								if (before.getRhsLink() != null) {
									changeSetTableRow.setPreviousRhsAfter(before.getRhsLink());
								}
							}
						}
					}
				}
				{
					// Do we have an equivalent after case for the RHS?
					// Just merge wiring groups
					final List<ChangeSetTableRow> changeSetTableRows = rm.get(before);
					if (changeSetTableRows != null) {
						for (var changeSetTableRow : changeSetTableRows) {

							if (group == null) {
								group = changeSetTableRow.getWiringGroup();
							} else {
								final var groupA = group;
								final var groupB = changeSetTableRow.getWiringGroup();
								if (groupA != groupB) {
									for (final var b : new ArrayList<>(groupB.getRows())) {
										// Bi-directional reference will also move b out of groupB
										b.setWiringGroup(groupA);
									}
								}
							}
						}
						// // Here we link wiring change rows. I.e. we record the discharge the load was
						// paired to in the before case.
						// if (ChangeSetTransformerUtil.isSet(before.getRhsName())) {
						// changeSetTableRow.setPreviousRhsBefore(before);
						// if (before.getRhsLink() != null) {
						// changeSetTableRow.setPreviousRhsAfter(before.getRhsLink());
						// }
						// }
					}
				}
			}
		}

		return rows;
	}

	public static String getName(final Sequence sequence) {
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

	public static ChangeSetVesselType getVesselType(final Sequence sequence) {
		if (sequence.getSequenceType() == SequenceType.DES_PURCHASE) {
			return ChangeSetVesselType.DES;
		} else if (sequence.getSequenceType() == SequenceType.FOB_SALE) {
			return ChangeSetVesselType.FOB;
		} else if (sequence.getSequenceType() == SequenceType.ROUND_TRIP) {
			return ChangeSetVesselType.NOMINAL;
		} else if (sequence.getSequenceType() == SequenceType.SPOT_VESSEL) {
			return ChangeSetVesselType.MARKET;
		} else if (sequence.getSequenceType() == SequenceType.VESSEL) {
			return ChangeSetVesselType.FLEET;
		}
		throw new IllegalArgumentException("Unknown sequence type");
	}

	public static String getShortName(final Sequence sequence) {
		if (sequence.isSetVesselCharter()) {
			return getShortName(sequence.getVesselCharter());
		} else {
			return getName(sequence);
		}
	}

	public static String getShortName(@Nullable final VesselAssignmentType t) {
		if (t instanceof final VesselCharter vc) {
			final Vessel vessel = vc.getVessel();
			if (vessel != null) {
				return vessel.getShortenedName();
			} else {
				return "";
			}
		}
		throw new NullPointerException();
	}

	public static Integer getCharterNumber(final Sequence sequence) {
		if (sequence.isSetVesselCharter()) {
			return getCharterNumber(sequence.getVesselCharter());
		}
		return 0;
	}

	public static Integer getCharterNumber(@Nullable final VesselAssignmentType t) {
		if (t instanceof final VesselCharter vc) {
			return vc.getCharterNumber();
		}
		throw new NullPointerException();
	}

	@Nullable
	public static String getRowName(@Nullable final Slot<?> slot) {
		if (slot == null) {
			return null;
		}
		if (slot instanceof final SpotSlot spotSlot) {
			final SpotMarket market = spotSlot.getMarket();
			final int windowSize = slot.getWindowSize();
			String formattedWindowStart = format(slot.getWindowStart());
			if (windowSize > 1 && slot.getWindowSizeUnits() == TimePeriod.MONTHS) {
				final String formattedWindowPeriodEnd = format(slot.getWindowStart().plusMonths(windowSize));
				formattedWindowStart = String.format("%s-%s", formattedWindowStart, formattedWindowPeriodEnd);
			}
			return String.format("%s-%s", market.getName(), formattedWindowStart);
		}
		return slot.getName();
	}

	@Nullable
	public static String getKeyName(@Nullable final SlotAllocation slotAllocation) {

		if (slotAllocation == null) {
			return null;
		}
		final Slot<?> slot = slotAllocation.getSlot();
		if (slot == null) {
			return null;
		}
		if (slot instanceof SpotSlot) {
			final String key = ElementKeyUtil.getElementKey(slot);
			final StringBuilder sb = new StringBuilder();
			final CargoAllocation cargoAllocation = slotAllocation.getCargoAllocation();
			if (cargoAllocation != null) {
				for (final SlotAllocation slotAllocation2 : cargoAllocation.getSlotAllocations()) {
					final Slot<?> slot2 = slotAllocation2.getSlot();
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
		final Slot<?> slot = slotAllocation.getSlot();
		if (slot == null) {
			return null;
		}
		if (slot instanceof SpotSlot) {
			assert false;
		}
		return slot.getName();
	}

	public static String format(@Nullable final LocalDate date) {
		if (date == null) {
			return "<no date>";
		}
		return String.format("%04d-%02d", date.getYear(), date.getMonthValue());
	}

	public static long getRowProfitAndLossValue(@Nullable final ChangeSetRowDataGroup row, final ToLongFunction<@Nullable ProfitAndLossContainer> f) {
		if (row == null) {
			return 0L;
		}

		final Set<ProfitAndLossContainer> containers = new HashSet<>();
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

	public static void sortRows(final List<ChangeSetTableRow> rows, @Nullable final NamedObject targetToSortFirst) {

		// Perform a multi-stage sort process.

		// Stage 1 - sort the rows within a wiring group. Sometimes there is a natural
		// start to end in the chain (for simple LD cargoes).
		// Sometimes there is a circular wiring change where we find the first load
		// alphabetically as the starting point.

		// Collect the unique groups. There is a many-to-one relationship between rows
		// and groups
		final Set<ChangeSetWiringGroup> groups = new HashSet<>();
		for (final ChangeSetTableRow rowA : rows) {
			groups.add(rowA.getWiringGroup());
		}

		// Sort each group
		for (final var group : groups) {
			final List<ChangeSetTableRow> rows2 = group.getRows();
			// No need to sort single element groups
			if (rows2.size() < 2) {
				continue;
			}

			// The way the group is created should generally mean the head is the first row
			// (unless it is a circular group) and rows are sorted according to the change
			// chain. If this is a non-circular
			// group, then skip it.
			final ChangeSetTableRow firstElement = rows2.get(0);
			final ChangeSetRowData feRhsAfter = firstElement.getCurrentRhsAfter();
			if (feRhsAfter != null) {
				final var link = feRhsAfter.getRhsLink();
				if (link == null || link.getLhsName() == null) {
					continue;
				}
			}

			// This should be a circular group, find the first load alphabetically
			// Find the first element alphabetically
			ChangeSetTableRow firstAlphabetically = null;
			for (final ChangeSetTableRow r : rows2) {
				if (firstAlphabetically == null && r.getLhsName() != null) {
					firstAlphabetically = r;
				} else if (firstAlphabetically != null && r.getLhsName() != null) {
					if (firstAlphabetically.getLhsName().compareTo(r.getLhsName()) > 0) {
						firstAlphabetically = r;
					}
				}
			}

			// Take a copy of the data as the underlying EList can break with Collections
			// operations
			final List<ChangeSetTableRow> value = new ArrayList<>(rows2);
			// Shift the list element to retain the order, but with the new starting point
			Collections.rotate(value, -value.indexOf(firstAlphabetically));
			// Clear and re-add data to the datamodel in new order
			group.getRows().clear();
			group.getRows().addAll(value);

		}

		// Stage 2 - sort first by change type then (wiring, vessel, date, other) then
		// use the pre-determined group order if part of the same wiring group otherwise
		// alphabetically.
		Collections.sort(rows, (o1, o2) -> { //
			// Sort wiring changes first
			ChangeSetWiringGroup g1 = o1.getWiringGroup();
			ChangeSetWiringGroup g2 = o2.getWiringGroup();
			if (g1 == g2) {
				// Related elements, sort together.
				final List<ChangeSetTableRow> group = g1.getRows();

				// Group order should be the desired order
				return Integer.compare(group.indexOf(o1), group.indexOf(o2));
			} else {
				if (g1.isWiringChange() != g2.isWiringChange()) {
					if (g1.isWiringChange()) {
						return -1;
					} else {
						return 1;
					}
				}
				if (!g1.isWiringChange()) {
					// // Sort vessel changes above anything else.
					if (g1.isVesselChange() != g2.isVesselChange()) {
						if (g1.isVesselChange()) {
							return -1;
						} else {
							return 1;
						}
					}
					// // Sort by date.
					if (g1.isDateChange() != g2.isDateChange()) {
						if (g1.isDateChange()) {
							return -1;
						} else {
							return 1;
						}
					}
				}
				final Collection<ChangeSetTableRow> group1 = g1.getRows();
				final Collection<ChangeSetTableRow> group2 = g2.getRows();

				// Sort on head element ID
				return ("" + group1.iterator().next().getLhsName()).compareTo("" + group2.iterator().next().getLhsName());
			}
		});

		// Stage 3 - for e.g. an optionise result we want a particular element to be
		// first if present regardless of change type.
		if (targetToSortFirst != null) {
			// Find the row with the target
			ChangeSetTableRow targetRow = null;
			for (final ChangeSetTableRow row : rows) {
				if (targetToSortFirst instanceof VesselEvent && !row.getLHSAfterOrBefore().isLhsSlot() && targetToSortFirst.getName().equals(row.getLhsName())) {
					targetRow = row;
					break;
				}
				if (targetToSortFirst instanceof LoadSlot && row.getLHSAfterOrBefore() != null && row.getLHSAfterOrBefore().isLhsSlot()
						&& targetToSortFirst.getName().equals(row.getLHSAfterOrBefore().getLhsName())) {
					targetRow = row;
					break;
				}
				if (targetToSortFirst instanceof DischargeSlot && row.getCurrentRHSAfterOrBefore() != null && row.getCurrentRHSAfterOrBefore().isRhsSlot()
						&& targetToSortFirst.getName().equals(row.getCurrentRHSAfterOrBefore().getRhsName())) {
					targetRow = row;
					break;
				}
			}

			if (targetRow != null) {
				// Grab the wiring group
				final Collection<ChangeSetTableRow> collection = targetRow.getWiringGroup().getRows();
				// Create a copy so we can mutate it
				final List<ChangeSetTableRow> l = new LinkedList<>(collection);
				if (collection.size() > 1) {
					// re-order the wiring group list so the target is first
					Collections.rotate(l, -l.indexOf(targetRow));
				}

				// Remove the whole wiring group from the full change set rows list
				rows.removeAll(l);
				// Insert the wiring group at the head of the list in the new order.
				rows.addAll(0, l);
			}
		}

		if (false) {
			// Debug code - prefix wiring group ID and w if the row has the wiring change
			// flag set
			final List<Object> seen = new LinkedList<>();
			for (final var v : rows) {

				if (!seen.contains(v.getWiringGroup())) {
					seen.add(v.getWiringGroup());
				}

				final String s = v.getLhsName();
				final String s2 = v.getCurrentRhsName();
				if (v.getLHSAfterOrBefore() != null) {
					v.getLHSAfterOrBefore().setLhsName(seen.indexOf(v.getWiringGroup()) + (v.isWiringChange() ? "w" : "") + "-" + s);
				}

				if (v.getCurrentRHSAfterOrBefore() != null) {
					v.getCurrentRHSAfterOrBefore().setRhsName(seen.indexOf(v.getWiringGroup()) + (v.isWiringChange() ? "w" : "") + "-" + s2);
				}
			}
		}

	}

	public static void calculateMetrics(final ChangeSet changeSet, final Schedule fromSchedule, final Schedule toSchedule, final boolean isAlternative) {
		final Metrics currentMetrics = ChangesetFactory.eINSTANCE.createMetrics();
		final DeltaMetrics deltaMetrics = ChangesetFactory.eINSTANCE.createDeltaMetrics();

		long pnl = 0;
		int lateness = 0;
		int violations = 0;
		if (toSchedule != null) {
			for (final Sequence sequence : toSchedule.getSequences()) {
				for (final Event event : sequence.getEvents()) {
					if (event instanceof final ProfitAndLossContainer profitAndLossContainer) {
						final GroupProfitAndLoss groupProfitAndLoss = profitAndLossContainer.getGroupProfitAndLoss();
						if (groupProfitAndLoss != null) {
							pnl += groupProfitAndLoss.getProfitAndLoss();
						}
					}

					if (event instanceof final SlotVisit slotVisit) {
						final SlotAllocation slotAllocation = slotVisit.getSlotAllocation();
						if (slotAllocation != null && slotAllocation.getSlot() instanceof LoadSlot) {
							final CargoAllocation cargoAllocation = slotAllocation.getCargoAllocation();
							if (cargoAllocation != null) {
								pnl += cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();
								lateness += LatenessUtils.getLatenessExcludingFlex(cargoAllocation);
								violations += ScheduleModelKPIUtils.getCapacityViolationCount(cargoAllocation);
							}
						}
					}

					if (event instanceof final EventGrouping eventGrouping) {
						lateness += LatenessUtils.getLatenessExcludingFlex(eventGrouping);
						violations += ScheduleModelKPIUtils.getCapacityViolationCount(eventGrouping);
					}
				}
			}

			for (final OpenSlotAllocation openSlotAllocation : toSchedule.getOpenSlotAllocations()) {
				final GroupProfitAndLoss groupProfitAndLoss = openSlotAllocation.getGroupProfitAndLoss();
				if (groupProfitAndLoss != null) {
					pnl += groupProfitAndLoss.getProfitAndLoss();
				}
			}
			if (toSchedule.getOtherPNL() != null && toSchedule.getOtherPNL().getGroupProfitAndLoss() != null) {
				pnl += toSchedule.getOtherPNL().getGroupProfitAndLoss().getProfitAndLoss();
				lateness += LatenessUtils.getLatenessExcludingFlex(toSchedule.getOtherPNL());
				// violations +=
				// ScheduleModelKPIUtils.getCapacityViolationCount(toSchedule.getOtherPNL());

			}
			for (final PaperDealAllocation paperDealAllocation : toSchedule.getPaperDealAllocations()) {
				final GroupProfitAndLoss groupProfitAndLoss = paperDealAllocation.getGroupProfitAndLoss();
				if (groupProfitAndLoss != null) {
					pnl += groupProfitAndLoss.getProfitAndLoss();
				}
			}
		}

		currentMetrics.setPnl(pnl);
		currentMetrics.setCapacity(violations);
		currentMetrics.setLateness(lateness);
		if (fromSchedule != null) {
			for (final Sequence sequence : fromSchedule.getSequences()) {
				for (final Event event : sequence.getEvents()) {
					if (event instanceof final ProfitAndLossContainer profitAndLossContainer) {
						final GroupProfitAndLoss groupProfitAndLoss = profitAndLossContainer.getGroupProfitAndLoss();
						if (groupProfitAndLoss != null) {
							pnl -= groupProfitAndLoss.getProfitAndLoss();
						}
					}
					if (event instanceof final SlotVisit slotVisit) {
						final SlotAllocation slotAllocation = slotVisit.getSlotAllocation();
						if (slotAllocation != null && slotAllocation.getSlot() instanceof LoadSlot) {
							final CargoAllocation cargoAllocation = slotAllocation.getCargoAllocation();
							if (cargoAllocation != null) {
								pnl -= cargoAllocation.getGroupProfitAndLoss().getProfitAndLoss();
								lateness -= LatenessUtils.getLatenessExcludingFlex(cargoAllocation);
								violations -= ScheduleModelKPIUtils.getCapacityViolationCount(cargoAllocation);
							}
						}
					}

					if (event instanceof final EventGrouping eventGrouping) {
						lateness -= LatenessUtils.getLatenessExcludingFlex(eventGrouping);
						violations -= ScheduleModelKPIUtils.getCapacityViolationCount(eventGrouping);
					}
				}
			}

			for (final OpenSlotAllocation openSlotAllocation : fromSchedule.getOpenSlotAllocations()) {
				pnl -= openSlotAllocation.getGroupProfitAndLoss().getProfitAndLoss();
			}
			if (fromSchedule.getOtherPNL() != null && fromSchedule.getOtherPNL().getGroupProfitAndLoss() != null) {
				pnl -= fromSchedule.getOtherPNL().getGroupProfitAndLoss().getProfitAndLoss();
				lateness -= LatenessUtils.getLatenessExcludingFlex(fromSchedule.getOtherPNL());

			}
			for (final PaperDealAllocation paperDealAllocation : fromSchedule.getPaperDealAllocations()) {
				final GroupProfitAndLoss groupProfitAndLoss = paperDealAllocation.getGroupProfitAndLoss();
				if (groupProfitAndLoss != null) {
					pnl -= groupProfitAndLoss.getProfitAndLoss();
				}
			}
		}
		deltaMetrics.setPnlDelta(pnl);
		deltaMetrics.setLatenessDelta(lateness);
		deltaMetrics.setCapacityDelta(violations);
		if (isAlternative) {
			changeSet.setMetricsToAlternativeBase(deltaMetrics);
		} else {
			changeSet.setMetricsToDefaultBase(deltaMetrics);
		}
		changeSet.setCurrentMetrics(currentMetrics);
	}

	public static boolean isSet(@Nullable final String str) {
		return str != null && !str.isEmpty();
	}

	public static void setRowFlags(final List<ChangeSetTableRow> rows) {
		for (final ChangeSetTableRow row : rows) {

			// For LD cargoes, multiple rows in the wiring group implies a wiring change.
			// However, for LDD cargoes multiple rows in the wiring group does not
			// necessarily mean there is a wiring change. We need to see if there are
			// multiple cargoes present in the collection.
			if (row.getWiringGroup().getRows().size() > 1) {
				for (var rowA : row.getWiringGroup().getRows()) {
					for (var rowB : row.getWiringGroup().getRows()) {
						// Skip identity
						if (rowA == rowB) {
							continue;
						}
						// Does one row have a D and other does not D? Assume this must be a wiring
						// change.
						// E.g. one row is an open Load
						if ((rowA.getCurrentRhsAfter() == null) != (rowB.getCurrentRhsAfter() == null)) {
							row.setWiringChange(true);
						}
						// Are the two discharges part of a different cargo?
						if (rowA.getCurrentRhsAfter() != null && rowB.getCurrentRhsAfter() != null) {
							final Set<ChangeSetRowData> a = new HashSet<>(rowA.getCurrentRhsAfter().getRowDataGroup().getMembers());
							final Set<ChangeSetRowData> b = new HashSet<>(rowB.getCurrentRhsAfter().getRowDataGroup().getMembers());
							if (!a.equals(b)) {
								row.setWiringChange(true);
							}
						}
					}
				}
			}

			final ChangeSetRowDataGroup beforeGroup = row.getLhsBefore() == null ? null : row.getLhsBefore().getRowDataGroup();
			final ChangeSetRowDataGroup afterGroup = row.getLhsAfter() == null ? null : row.getLhsAfter().getRowDataGroup();

			// Does the element only appear on the before or the after case?
			if ((beforeGroup == null) != (afterGroup == null)) {
				row.setWiringChange(true);
			}

			if (beforeGroup != null && afterGroup != null) {
				if (beforeGroup.getMembers().size() != afterGroup.getMembers().size()) {
					// A different number of members means e.g. LD to LDD change.
					row.setWiringChange(true);
				} else {
					if (beforeGroup.getMembers().size() == 1) {
						final ChangeSetRowData beforeData = beforeGroup.getMembers().get(0);
						final ChangeSetRowData afterData = afterGroup.getMembers().get(0);

						// Mark up lateness over 24 hours as a major change.
						{
							long beforeLateness = 0L;
							for (final ChangeSetRowData data : beforeGroup.getMembers()) {
								beforeLateness += LatenessUtils.getLatenessExcludingFlex(data.getEventGrouping());
							}
							long afterLateness = 0L;
							for (final ChangeSetRowData data : afterGroup.getMembers()) {
								afterLateness += LatenessUtils.getLatenessExcludingFlex(data.getEventGrouping());
							}
							if (Math.abs(beforeLateness - afterLateness) > 24) {
								row.setVesselChange(true);
							}
						}

						// Start / end events are minor changes.
						if (beforeData.getLhsEvent() instanceof StartEvent //
								|| afterData.getLhsEvent() instanceof StartEvent //
								|| beforeData.getLhsEvent() instanceof EndEvent //
								|| afterData.getLhsEvent() instanceof EndEvent) {
							continue;
						}
						// GCO are minor changes.
						if ((beforeData.getLhsEvent() instanceof GeneratedCharterOut) || (afterData.getLhsEvent() instanceof GeneratedCharterOut)) {
							continue;
						}
						if ((beforeData.getLhsEvent() instanceof CharterLengthEvent) || (afterData.getLhsEvent() instanceof CharterLengthEvent)) {
							continue;
						}
						if ((beforeData.getLhsEvent() instanceof GroupedCharterLengthEvent) || (afterData.getLhsEvent() instanceof GroupedCharterLengthEvent)) {
							continue;
						}
						if ((beforeData.getLhsEvent() instanceof GroupedCharterOutEvent) || (afterData.getLhsEvent() instanceof GroupedCharterOutEvent)) {
							continue;
						}
					}
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

						if (!row.isWiringChange()) {
							{
								final SlotAllocation beforeAllocation = beforeData.getLoadAllocation();
								final SlotAllocation afterAllocation = afterData.getLoadAllocation();
								if (beforeAllocation != null && afterAllocation != null) {

									final SlotVisit beforeVisit = beforeAllocation.getSlotVisit();
									final SlotVisit afterVisit = afterAllocation.getSlotVisit();

									if (beforeVisit != null && afterVisit != null) {
										if (beforeVisit.getStart() != null && afterVisit.getStart() != null) {
											if (Math.abs(Days.between(beforeVisit.getStart(), afterVisit.getStart())) > MAJOR_CHANGE_DAYS_THRESHOLD) {
												row.setDateChange(true);
											}
										}
									}

								}
							}
							{
								final SlotAllocation beforeAllocation = beforeData.getDischargeAllocation();
								final SlotAllocation afterAllocation = afterData.getDischargeAllocation();
								if (beforeAllocation != null && afterAllocation != null) {

									final SlotVisit beforeVisit = beforeAllocation.getSlotVisit();
									final SlotVisit afterVisit = afterAllocation.getSlotVisit();

									if (beforeVisit != null && afterVisit != null) {
										if (beforeVisit.getStart() != null && afterVisit.getStart() != null) {
											if (Math.abs(Days.between(beforeVisit.getStart(), afterVisit.getStart())) > MAJOR_CHANGE_DAYS_THRESHOLD) {
												row.setDateChange(true);
											}
										}
									}
								}
							}

							if (beforeData.getLhsEvent() != null && afterData.getLhsEvent() != null) {
								final Event beforeEvent = beforeData.getLhsEvent();
								final Event afterEvent = afterData.getLhsEvent();
								if (beforeEvent.getStart() != null && afterEvent.getStart() != null) {
									if (Math.abs(Days.between(beforeEvent.getStart(), afterEvent.getStart())) > MAJOR_CHANGE_DAYS_THRESHOLD) {
										row.setDateChange(true);
									}
								}
							}
							if (beforeData.getRhsEvent() != null && afterData.getRhsEvent() != null) {
								final Event beforeEvent = beforeData.getRhsEvent();
								final Event afterEvent = afterData.getRhsEvent();
								if (beforeEvent.getStart() != null && afterEvent.getStart() != null) {
									if (Math.abs(Days.between(beforeEvent.getStart(), afterEvent.getStart())) > MAJOR_CHANGE_DAYS_THRESHOLD) {
										row.setDateChange(true);
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

		// Post process - ensure if one row in a wiring group is flagged as a wiring
		// change, then all rows are.
		// The above loop doesn't pick up LDD changes where there is a D row with no LHS
		// data before or after
		for (final ChangeSetTableRow row : rows) {
			if (row.getWiringGroup().getRows().size() > 1) {
				// Check state
				boolean hasWiring = false;
				for (final var r2 : row.getWiringGroup().getRows()) {
					hasWiring |= r2.isWiringChange();
				}
				// Apply state
				for (final var r2 : row.getWiringGroup().getRows()) {
					r2.setWiringChange(hasWiring);
				}
			}
		}
	}

	public static void filterRows(final List<ChangeSetTableRow> rows) {
		final Iterator<ChangeSetTableRow> itr = rows.iterator();
		while (itr.hasNext()) {
			final ChangeSetTableRow changeSetTableRow = itr.next();

			int toRemove = 0;
			final var cargoGroup = changeSetTableRow.getWiringGroup();
			final Collection<ChangeSetTableRow> loopRows = cargoGroup == null ? Collections.singleton(changeSetTableRow) : cargoGroup.getRows();
			// Determine how many rows we want to remove
			for (final var r : loopRows) {
				if (!r.isMajorChange()) {
					long a = ChangeSetKPIUtil.getPNL(r, ResultType.Before);
					long b = ChangeSetKPIUtil.getPNL(r, ResultType.After);
					if (a == b) {
						a = ChangeSetKPIUtil.getViolations(r, ResultType.Before);
						b = ChangeSetKPIUtil.getViolations(r, ResultType.After);
						if (a == b) {
							a = ChangeSetKPIUtil.getLateness(r, ResultType.Before)[FlexType.TotalIfFlexInsufficient.ordinal()];
							b = ChangeSetKPIUtil.getLateness(r, ResultType.After)[FlexType.TotalIfFlexInsufficient.ordinal()];
							if (a == b) {
								++toRemove;
							}
						}
					}
				}
			}
			// Only remove this row if we will remove all rows in the group. (otherwise e.g.
			// we may keep the LD row and not the D row from a LDD cargo)
			if (toRemove == loopRows.size()) {
				itr.remove();
			}
		}
	}

	public static <T extends SpotSlot & Slot<?>> String getMarketSlotKey(final T slot) {
		final SpotMarket market = slot.getMarket();
		return String.format("%s-%s-%04d-%02d", getSlotTypePrefix(slot), market.getName(), slot.getWindowStart().getYear(), slot.getWindowStart().getMonthValue());
	}

	protected static String getSlotTypePrefix(final Slot<?> slot) {
		String prefix;
		if (slot instanceof final LoadSlot loadSlot) {
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

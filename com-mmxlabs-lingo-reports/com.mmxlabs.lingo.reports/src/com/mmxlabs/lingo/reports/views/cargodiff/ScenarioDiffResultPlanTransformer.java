/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.cargodiff;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.lingo.reports.views.changeset.ChangeSetTransformerUtil;
import com.mmxlabs.lingo.reports.views.changeset.ChangeSetTransformerUtil.MappingModel;
import com.mmxlabs.lingo.reports.views.changeset.IChangeSetRowGenerationCustomiser;
import com.mmxlabs.lingo.reports.views.changeset.IPinDiffResultPlanTransformer;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowDataGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics;
import com.mmxlabs.lingo.reports.views.changeset.model.Metrics;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.scenario.service.ScenarioResult;

@NonNullByDefault
public class ScenarioDiffResultPlanTransformer implements IPinDiffResultPlanTransformer {

	@Override
	public ChangeSetRoot createDataModel(final ScenarioResult pin, @Nullable final ScenarioResult other, final IProgressMonitor monitor) {
		final ChangeSetRoot root = ChangesetFactory.eINSTANCE.createChangeSetRoot();
		try {
			monitor.beginTask("Comparing solutions", 1);
			root.getChangeSets().add(buildDiffToBaseChangeSet(pin, other));
			monitor.worked(1);
		} finally {
			monitor.done();
		}
		return root;
	}

	public ChangeSet buildDiffToBaseChangeSet(final ScenarioResult prev, @Nullable final ScenarioResult current) {
		final ChangeSet changeSet = ChangesetFactory.eINSTANCE.createChangeSet();
		changeSet.setBaseScenario(prev);
		changeSet.setCurrentScenario(current);
		if (current != null) {
			generateDifferences(prev, current, changeSet, false);
		}
		return changeSet;
	}

	private void generateDifferences(final ScenarioResult from, final ScenarioResult to, final ChangeSet changeSet, final boolean isAlternative) {
		final List<Cargo> beforeCargoes = from.getScenarioDataProvider().getTypedScenario(LNGScenarioModel.class).getCargoModel().getCargoes();
		final List<Cargo> afterCargoes = to.getScenarioDataProvider().getTypedScenario(LNGScenarioModel.class).getCargoModel().getCargoes();

		if (beforeCargoes != null && afterCargoes != null) {
			final MappingModel beforeDifferences = generateMappingModel(beforeCargoes);
			final MappingModel afterDifferences = generateMappingModel(afterCargoes);

			final IChangeSetRowGenerationCustomiser rowGenerationCustomiser = new ScenarioDiffChangeSetRowGenerationCustomiser();
			@NonNull
			final List<@NonNull ChangeSetTableRow> rows = ChangeSetTransformerUtil.generateChangeSetRows(beforeDifferences, afterDifferences, rowGenerationCustomiser);

			final List<ChangeSetTableRow> tableRows = isAlternative ? changeSet.getChangeSetRowsToAlternativeBase() : changeSet.getChangeSetRowsToDefaultBase();
			tableRows.addAll(rows);

			ChangeSetTransformerUtil.setRowFlags(rows);

			calculateMetrics(changeSet, isAlternative);
		}
	}

	private static void calculateMetrics(final ChangeSet changeSet, final boolean isAlternative) {
		final Metrics currentMetrics = ChangesetFactory.eINSTANCE.createMetrics();
		final DeltaMetrics deltaMetrics = ChangesetFactory.eINSTANCE.createDeltaMetrics();
		if (isAlternative) {
			changeSet.setMetricsToAlternativeBase(deltaMetrics);
		} else {
			changeSet.setMetricsToDefaultBase(deltaMetrics);
		}
		changeSet.setCurrentMetrics(currentMetrics);
	}

	private static MappingModel generateMappingModel(List<Cargo> cs) {

		Set<LoadSlot> lsset = new HashSet<>();
		final MappingModel mappingModel = new MappingModel();

		for (final Cargo cargo : cs) {

			final ChangeSetRowDataGroup group = ChangesetFactory.eINSTANCE.createChangeSetRowDataGroup();
			mappingModel.groups.add(group);

			final List<LoadSlot> loadslots = new LinkedList<>();
			final List<DischargeSlot> dischargeslots = new LinkedList<>();

			for (final Slot<?> slot : cargo.getSlots()) {
				if (slot instanceof @NonNull final LoadSlot ls) {
					loadslots.add(ls);
					lsset.add(ls);
				} else if (slot instanceof @NonNull final DischargeSlot ds) {
					dischargeslots.add(ds);
				} else {
					assert false;
				}
			}

			for (int i = 0; i < Math.max(loadslots.size(), dischargeslots.size()); ++i) {
				final ChangeSetRowData row = ChangesetFactory.eINSTANCE.createChangeSetRowData();
				group.getMembers().add(row);

				row.setPrimaryRecord(i == 0);

				if (i == 0) {
					LoadSlot ls = loadslots.get(i);
					VesselAssignmentType vat = ls.getCargo().getVesselAssignmentType();
					if (vat instanceof VesselCharter vc) {
						row.setVesselName(vc.getVessel().getName());
						row.setVesselShortName(vc.getVessel().getName());
						row.setVesselCharterNumber(ChangeSetTransformerUtil.getCharterNumber(vc));
					}
					if (vat instanceof CharterInMarket cim) {
						row.setVesselName(cim.getVessel().getName());
						row.setVesselShortName(cim.getVessel().getName());
					}
				}

				if (i < loadslots.size()) {

					final LoadSlot slot = loadslots.get(i);

					final String key = slot.getName();
					if (key != null) {
						final String name = slot.getName();

						row.setLhsName(name);
						row.setLoadSlot(slot);
						row.setLhsSlot(true);
						row.setLhsOptional(slot.isOptional());
						row.setLhsNonShipped(slot.isDESPurchase());

						if (slot instanceof @NonNull final SpotLoadSlot spotLoadSlot) {
							final String mKey = getMarketSlotKey(spotLoadSlot);
							mappingModel.lhsRowMarketMap.computeIfAbsent(mKey, k -> new LinkedList<>()).add(row);
							row.setLhsSpot(true);
						}
						mappingModel.lhsRowMap.put(key, row);
					}
				}

				if (i < dischargeslots.size()) {
					final DischargeSlot slot = dischargeslots.get(i);

					final String key = slot.getName();
					if (key != null) {
						final String name = slot.getName();

						row.setRhsName(name);
						row.setDischargeSlot(slot);
						row.setRhsSlot(true);
						row.setRhsOptional(slot.isOptional());
						row.setRhsNonShipped(slot.isFOBSale());
						if (slot instanceof @NonNull final SpotDischargeSlot spotDischargeSlot) {
							final String mKey = getMarketSlotKey(spotDischargeSlot);
							mappingModel.rhsRowMarketMap.computeIfAbsent(mKey, k -> new LinkedList<>()).add(row);
							row.setRhsSpot(true);
						}
						mappingModel.rhsRowMap.put(key, row);
					}
				}
			}
		}

		return mappingModel;
	}

	private static <@NonNull T extends SpotSlot & Slot<?>> String getMarketSlotKey(final T slot) {
		final SpotMarket market = slot.getMarket();
		return String.format("%s-%s-%04d-%02d", getSlotTypePrefix(slot), market.getName(), slot.getWindowStart().getYear(), slot.getWindowStart().getMonthValue());
	}

	private static String getSlotTypePrefix(final Slot<?> slot) {
		if (slot instanceof LoadSlot loadSlot) {
			return loadSlot.isDESPurchase() ? "des-purchase" : "fob-purchase";
		} else {
			final DischargeSlot dischargeSlot = (DischargeSlot) slot;
			return dischargeSlot.isFOBSale() ? "fob-sale" : "des-sale";
		}
	}
}

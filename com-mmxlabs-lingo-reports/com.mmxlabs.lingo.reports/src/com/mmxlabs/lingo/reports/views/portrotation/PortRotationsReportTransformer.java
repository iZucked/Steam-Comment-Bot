/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.portrotation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.ScheduledEventCollector;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlock;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public class PortRotationsReportTransformer {
	private final PortRotationBasedReportBuilder builder;

	public PortRotationsReportTransformer(final PortRotationBasedReportBuilder builder) {
		this.builder = builder;
	}

	public IScenarioInstanceElementCollector getElementCollector(final List<Object> elements, final PortRotationReportView viewer) {
		return new ScheduledEventCollector() {

			private int numberOfSchedules;
			private boolean isPinned;
			private final List<Object> elementList = new ArrayList<>();
			private final List<LNGScenarioModel> rootObjects = new ArrayList<>();

			@Override
			public void beginCollecting(final boolean pinDiffMode) {
				super.beginCollecting(pinDiffMode);

				elements.clear();
				rootObjects.clear();
				numberOfSchedules = 0;
				isPinned = false;

				elementList.clear();
			}

			@Override
			protected Collection<? extends Object> collectElements(final ScenarioResult scenarioResult, LNGScenarioModel scenarioModel, final Schedule schedule, final boolean pinned) {
				numberOfSchedules++;
				rootObjects.add(scenarioModel);
				isPinned |= pinned;
				final Collection<? extends Object> collectElements = super.collectElements(scenarioResult, scenarioModel, schedule, pinned);
				elementList.addAll(collectElements);
				return collectElements;
			}

			@Override
			public void endCollecting() {

				final boolean pinDiffMode = numberOfSchedules > 1 && isPinned;
				for (final ColumnBlock handler : builder.getBlockManager().getBlocksInVisibleOrder()) {
					if (handler != null) {
						handler.setViewState(numberOfSchedules > 1, pinDiffMode);
					}
				}

				super.endCollecting();

				viewer.processInputs(elements);
				builder.refreshDataColumns(rootObjects);
				elements.addAll(elementList);
			}
		};
	}
}

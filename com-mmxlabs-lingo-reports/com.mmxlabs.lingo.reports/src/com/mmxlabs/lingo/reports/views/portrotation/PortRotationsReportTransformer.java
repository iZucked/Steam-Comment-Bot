/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.portrotation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.ScheduledEventCollector;
import com.mmxlabs.lingo.reports.components.ColumnBlock;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
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
			private final Map<Object, ScenarioResult> _elementToInstanceMap = new HashMap<>();
			private final Map<Object, LNGScenarioModel> _elementToModelMap = new HashMap<>();
			private final List<Object> elementList = new ArrayList<>();

			@Override
			public void beginCollecting(final boolean pinDiffMode) {
				super.beginCollecting(pinDiffMode);

				elements.clear();
				numberOfSchedules = 0;
				isPinned = false;

				_elementToInstanceMap.clear();
				_elementToModelMap.clear();
				elementList.clear();
			}

			@Override
			protected Collection<? extends Object> collectElements(final ScenarioResult scenarioResult, LNGScenarioModel scenarioModel, final Schedule schedule, final boolean pinned) {
				numberOfSchedules++;
				isPinned |= pinned;
				final Collection<? extends Object> collectElements = super.collectElements(scenarioResult, scenarioModel, schedule, pinned);
				elementList.addAll(collectElements);
				_elementToInstanceMap.put(schedule, scenarioResult);
				_elementToModelMap.put(schedule, scenarioModel);
				for (final Object element : collectElements) {
					_elementToInstanceMap.put(element, scenarioResult);
					_elementToModelMap.put(element, scenarioModel);
				}
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
				viewer.mapInputs(_elementToInstanceMap, _elementToModelMap);
				elements.addAll(elementList);
				// viewer.setInput(elements);
			}
		};
	}
}

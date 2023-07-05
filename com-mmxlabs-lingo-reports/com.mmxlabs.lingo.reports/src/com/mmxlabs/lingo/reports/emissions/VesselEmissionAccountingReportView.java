/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.lingo.reports.modelbased.AbstractSimpleModelBasedReportView;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.scenario.service.ScenarioResult;

public class VesselEmissionAccountingReportView extends AbstractSimpleModelBasedReportView<VesselEmissionAccountingReportModelV1> {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.lingo.reports.emissions.VesselEmissionAccountingReportView";
	private DeltaHelper deltaHelper;

	@SuppressWarnings("null")
	public VesselEmissionAccountingReportView() {
		super(VesselEmissionAccountingReportModelV1.class);
	}

	@Override
	protected synchronized List<VesselEmissionAccountingReportModelV1> transform(final ISelectedDataProvider selectedDataProvider) {

		final List<VesselEmissionAccountingReportModelV1> rows = new LinkedList<>();
		for (final ScenarioResult scenarioResult : selectedDataProvider.getAllScenarioResults()) {
			final ScheduleModel typedResult = scenarioResult.getTypedResult(ScheduleModel.class);
			final String scenarioName = scenarioResult.getScenarioInstance().getName();
			if (typedResult != null) {
				final List<VesselEmissionAccountingReportModelV1> data = //
						VesselEmissionAccountingReportJSONGenerator.createReportData(typedResult, //
								scenarioResult.equals(selectedDataProvider.getPinnedScenarioResult()), scenarioName);
				rows.addAll(data);
			}
		}

		final boolean processDeltas = selectedDataProvider.inPinDiffMode();
		final boolean showScenarioColumn = processDeltas || selectedDataProvider.getAllScenarioResults().size() >= 2;
		EmissionsUtils.changeScenarioNameColumnVisibility(columnInfo, showScenarioColumn);
		deltaHelper.setProcessDeltas(processDeltas);
		if (processDeltas) {
			return processDeltas(rows);
		}
		return rows;
	}

	private List<VesselEmissionAccountingReportModelV1> processDeltas(final List<VesselEmissionAccountingReportModelV1> rows) {
		final List<VesselEmissionAccountingReportModelV1> result = new ArrayList<>();

		final VesselEmissionAccountingReportModelV1 totalModel = new VesselEmissionAccountingReportModelV1();
		totalModel.initZeroes();

		int group = 0;

		for (final VesselEmissionAccountingReportModelV1 r1 : rows) {
			if (r1.isPinned()) {
				for (final VesselEmissionAccountingReportModelV1 r2 : rows) {
					if (!r2.isPinned() && EmissionsUtils.checkIVesselEmissionsAreSimilar(r1, r2)) {

						final VesselEmissionAccountingReportModelV1 model = new VesselEmissionAccountingReportModelV1();
						model.setDelta(r2, r1);

						if (!r1.equals(r2) && !(result.contains(r1) && result.contains(r2))) {
							totalModel.addToTotal(model);
							r1.setGroup(group);
							r2.setGroup(group);
							model.setGroup(group);
							result.add(r1);
							result.add(r2);
							result.add(model);
							++group;
						}
						break;
					}
				}
			}
		}

		if (totalModel.isNonZero()) {
			totalModel.setGroup(Integer.MIN_VALUE);
			totalModel.scenarioName = "Total Δ";
			result.add(0, totalModel);
		}

		return result;
	}

	@Override
	protected BiConsumer<ViewerCell, Field> createStyler() {
		return EmissionsUtils::styleTheCell;
	}

	@Override
	protected void makeActions() {
		getViewSite().getActionBars().getToolBarManager().add(deltaHelper.createDeltaAction());

		super.makeActions();
	}

	@Override
	public void createPartControl(final Composite parent) {
		deltaHelper = new DeltaHelper();
		super.createPartControl(parent);

		if (viewer != null) {
			deltaHelper.setViewer(viewer);
		}
	}
}

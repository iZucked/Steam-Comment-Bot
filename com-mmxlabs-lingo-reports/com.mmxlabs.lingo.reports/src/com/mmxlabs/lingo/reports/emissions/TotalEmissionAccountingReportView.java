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

public class TotalEmissionAccountingReportView extends AbstractSimpleModelBasedReportView<TotalEmissionAccountingReportModelV1> {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.lingo.reports.emissions.SecondCargoEmissionAccountingReportView";
	private DeltaHelper deltaHelper;
	
	public TotalEmissionAccountingReportView() {
		super(TotalEmissionAccountingReportModelV1.class);
	}

	@Override
	protected synchronized List<TotalEmissionAccountingReportModelV1> transform(final ISelectedDataProvider selectedDataProvider) {

		final List<TotalEmissionAccountingReportModelV1> rows = new LinkedList<>();
		for (final var o : selectedDataProvider.getAllScenarioResults()) {
			final ScheduleModel typedResult = o.getTypedResult(ScheduleModel.class);
			final String scenarioName = o.getScenarioInstance().getName();
			if (typedResult != null) {
				final List<TotalEmissionAccountingReportModelV1> data = TotalEmissionAccountingReportJSONGenerator.createReportData(typedResult, //
						o.equals(selectedDataProvider.getPinnedScenarioResult()), scenarioName);
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
	
	private List<TotalEmissionAccountingReportModelV1> processDeltas(final List<TotalEmissionAccountingReportModelV1> rows){
		final List<TotalEmissionAccountingReportModelV1> result = new ArrayList<>();
		
		Long upstreamEmission = 0L;
		Long pipelineEmission = 0L;
		Long liquefactionEmission = 0L;
		Long shippingEmission = 0L;
		Long totalEmission = 0L;
		
		int group = 0;
		
		for (final var r1 : rows) {
			if (r1.isPinned()) {
				for (final var r2 : rows) {
					if (!r2.isPinned()) {
						if (EmissionsUtils.checkIVesselEmissionsAreSimilar(r1, r2)) {

							final TotalEmissionAccountingReportModelV1 model = new TotalEmissionAccountingReportModelV1();
							model.scenarioName = "Δ";
							model.vesselName = r1.vesselName;
							model.eventID = r1.eventID;
							model.eventStart = r1.eventStart;
							model.eventEnd = r1.eventEnd;

							model.upstreamEmission = r2.upstreamEmission - r1.upstreamEmission;
							model.pipelineEmission = r2.pipelineEmission - r1.pipelineEmission;
							model.liquefactionEmission = r2.liquefactionEmission - r1.liquefactionEmission;
							model.shippingEmission = r2.shippingEmission - r1.shippingEmission;
							model.totalEmission = r2.totalEmission - r1.totalEmission;

							if (model.upstreamEmission != 0 || model.pipelineEmission != 0 || model.liquefactionEmission != 0 || model.shippingEmission != 0|| model.totalEmission != 0) {
								if (!result.contains(r1) || !result.contains(r2)) {
									upstreamEmission += model.upstreamEmission;
									pipelineEmission += model.pipelineEmission;
									liquefactionEmission += model.liquefactionEmission;
									shippingEmission += model.shippingEmission;
									totalEmission += model.totalEmission;
									r1.setGroup(group);
									r2.setGroup(group);
									model.setGroup(group);
									result.add(r1);
									result.add(r2);
									result.add(model);
									++group;
								}
							}
							break;
						}
					}
				}
			}
		}
		
		if (upstreamEmission != 0 || pipelineEmission != 0 || liquefactionEmission != 0 || shippingEmission != 0 || totalEmission != 0) {
			final TotalEmissionAccountingReportModelV1 model = new TotalEmissionAccountingReportModelV1();
			model.scenarioName = "Total Δ";
			model.upstreamEmission = upstreamEmission;
			model.pipelineEmission = pipelineEmission;
			model.liquefactionEmission = liquefactionEmission;
			model.shippingEmission = shippingEmission;
			model.totalEmission = totalEmission;
			result.add(0, model);
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

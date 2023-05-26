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

public class VesselEmissionAccountingReportView extends AbstractSimpleModelBasedReportView<VesselEmissionAccountingReportModelV1> {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.lingo.reports.emissions.VesselEmissionAccountingReportView";
	private DeltaHelper deltaHelper;

	public VesselEmissionAccountingReportView() {
		super(VesselEmissionAccountingReportModelV1.class);
	}

	@Override
	protected synchronized List<VesselEmissionAccountingReportModelV1> transform(final ISelectedDataProvider selectedDataProvider) {

		final List<VesselEmissionAccountingReportModelV1> rows = new LinkedList<>();
		for (final var o : selectedDataProvider.getAllScenarioResults()) {
			final ScheduleModel typedResult = o.getTypedResult(ScheduleModel.class);
			final String scenarioName = o.getScenarioInstance().getName();
			if (typedResult != null) {
				final List<VesselEmissionAccountingReportModelV1> data = //
						VesselEmissionAccountingReportJSONGenerator.createReportData(typedResult, //
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

	private List<VesselEmissionAccountingReportModelV1> processDeltas(final List<VesselEmissionAccountingReportModelV1> rows) {
		final List<VesselEmissionAccountingReportModelV1> result = new ArrayList<>();

		long baseFuelEmission = 0L;
		long bogEmission = 0L;
		long pilotLightEmission = 0L;
		long totalEmission = 0L;
		
		int group = 0;

		for (final var r1 : rows) {
			if (r1.isPinned()) {
				for (final var r2 : rows) {
					if (!r2.isPinned()) {
						if (EmissionsUtils.checkIVesselEmissionsAreSimilar(r1, r2)) {

							final VesselEmissionAccountingReportModelV1 model = new VesselEmissionAccountingReportModelV1();
							model.scenarioName = "Δ";
							model.vesselName = r1.vesselName;
							model.eventID = r1.eventID;
							model.otherID = r1.otherID;
							model.eventStart = r1.eventStart;
							model.eventEnd = r1.eventEnd;

							model.baseFuelEmission = r2.baseFuelEmission - r1.baseFuelEmission;
							model.bogEmission = r2.bogEmission - r1.bogEmission;
							model.pilotLightEmission = r2.pilotLightEmission - r1.pilotLightEmission;
							model.totalEmission = r2.totalEmission - r1.totalEmission;

							if (model.baseFuelEmission != 0 || model.bogEmission != 0 || model.pilotLightEmission != 0 || model.totalEmission != 0) {
								if (!result.contains(r1) || !result.contains(r2)) {
									baseFuelEmission += model.baseFuelEmission;
									bogEmission += model.bogEmission;
									pilotLightEmission += model.pilotLightEmission;
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

		if (baseFuelEmission != 0 || bogEmission != 0 || pilotLightEmission != 0 || totalEmission != 0) {
			final VesselEmissionAccountingReportModelV1 model = new VesselEmissionAccountingReportModelV1();
			model.setGroup(Integer.MIN_VALUE);
			model.scenarioName = "Total Δ";
			model.baseFuelEmission = baseFuelEmission;
			model.bogEmission = bogEmission;
			model.pilotLightEmission = pilotLightEmission;
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

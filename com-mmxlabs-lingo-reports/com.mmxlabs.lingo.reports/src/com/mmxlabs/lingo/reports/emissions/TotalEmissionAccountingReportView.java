/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
* Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

import org.eclipse.jface.viewers.ViewerCell;

import com.mmxlabs.lingo.reports.modelbased.AbstractSimpleModelBasedReportView;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.models.lng.schedule.ScheduleModel;

public class TotalEmissionAccountingReportView extends AbstractSimpleModelBasedReportView<TotalEmissionAccountingReportModelV1> {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.lingo.reports.emissions.SecondCargoEmissionAccountingReportView";

	public TotalEmissionAccountingReportView() {
		super(TotalEmissionAccountingReportModelV1.class);
	}

	@Override
	protected synchronized List<TotalEmissionAccountingReportModelV1> transform(final ISelectedDataProvider selectedDataProvider) {

		final List<TotalEmissionAccountingReportModelV1> rows = new LinkedList<>();
		for (final var o : selectedDataProvider.getAllScenarioResults()) {
			final ScheduleModel typedResult = o.getTypedResult(ScheduleModel.class);
			if (typedResult != null) {
				final List<TotalEmissionAccountingReportModelV1> data = TotalEmissionAccountingReportJSONGenerator.createReportData(o.getScenarioDataProvider(), //
						typedResult);
				rows.addAll(data);
			}
		}
		return rows;
	}
	
	

	@Override
	protected BiConsumer<ViewerCell, Field> createStyler() {
		return (cell, f) -> {};
	}
}

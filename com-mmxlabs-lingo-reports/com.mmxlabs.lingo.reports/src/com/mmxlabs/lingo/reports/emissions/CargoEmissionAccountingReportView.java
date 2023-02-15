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
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

import org.eclipse.jface.viewers.ViewerCell;

import com.mmxlabs.lingo.reports.modelbased.AbstractSimpleModelBasedReportView;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.models.lng.schedule.ScheduleModel;

public class CargoEmissionAccountingReportView extends AbstractSimpleModelBasedReportView<CargoEmissionAccountingReportModelV1> {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.lingo.reports.emissions.CargoEmissionAccountingReportView";

	public CargoEmissionAccountingReportView() {
		super(CargoEmissionAccountingReportModelV1.class);
	}

	@Override
	protected synchronized List<CargoEmissionAccountingReportModelV1> transform(final ISelectedDataProvider selectedDataProvider) {

		final List<CargoEmissionAccountingReportModelV1> rows = new LinkedList<>();
		for (final var o : selectedDataProvider.getAllScenarioResults()) {
			final ScheduleModel typedResult = o.getTypedResult(ScheduleModel.class);
			if (typedResult != null) {
				final List<CargoEmissionAccountingReportModelV1> data = CargoEmissionAccountingReportJSONGenerator.createReportData(o.getScenarioDataProvider(), //
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

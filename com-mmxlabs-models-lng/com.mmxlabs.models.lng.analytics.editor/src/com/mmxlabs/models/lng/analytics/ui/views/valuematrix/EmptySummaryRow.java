package com.mmxlabs.models.lng.analytics.ui.views.valuematrix;

public class EmptySummaryRow implements ISummaryRow {

	private static final EmptySummaryRow INSTANCE = new EmptySummaryRow();

	private EmptySummaryRow() {
	}

	@Override
	public String getRowHeader() {
		return "";
	}

	public static EmptySummaryRow getInstance() {
		return INSTANCE;
	}

}

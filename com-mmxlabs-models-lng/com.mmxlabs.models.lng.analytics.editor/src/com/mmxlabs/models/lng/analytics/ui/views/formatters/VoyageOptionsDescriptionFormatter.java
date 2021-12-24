/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.formatters;

import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BaseCaseRowOptions;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.PartialCaseRowOptions;
import com.mmxlabs.models.ui.tabular.BaseFormatter;

public class VoyageOptionsDescriptionFormatter extends BaseFormatter {

	@Override
	public String render(Object object) {

		if (object instanceof final PartialCaseRow row) {
			object = row.getOptions();
		}
		if (object instanceof final PartialCaseRowOptions rowOptions) {

			String lSide = null;
			if (!rowOptions.getLoadDates().isEmpty() || !rowOptions.getLadenFuelChoices().isEmpty() || !rowOptions.getLadenRoutes().isEmpty()) {
				final StringBuilder sb = new StringBuilder();
				sb.append("Buy: ");
				if (!rowOptions.getLoadDates().isEmpty()) {
					sb.append("D");
				}
				if (!rowOptions.getLadenFuelChoices().isEmpty()) {
					sb.append("F");
				}
				if (!rowOptions.getLadenRoutes().isEmpty()) {
					sb.append("R");
				}
				lSide = sb.toString();
			}
			String rSide = null;
			if (!rowOptions.getDischargeDates().isEmpty() || !rowOptions.getBallastFuelChoices().isEmpty() || !rowOptions.getBallastRoutes().isEmpty()) {
				final StringBuilder sb = new StringBuilder();
				sb.append("Sell: ");
				if (!rowOptions.getDischargeDates().isEmpty()) {
					sb.append("D");
				}
				if (!rowOptions.getBallastFuelChoices().isEmpty()) {
					sb.append("F");
				}
				if (!rowOptions.getBallastRoutes().isEmpty()) {
					sb.append("R");
				}
				rSide = sb.toString();
			}

			if (lSide != null && rSide != null) {
				return lSide + " " + rSide;
			}
			if (lSide != null) {
				return lSide;
			}
			if (rSide != null) {
				return rSide;
			}

			return "";
		}
		if (object instanceof final BaseCaseRow row) {
			object = row.getOptions();
		}
		if (object instanceof final BaseCaseRowOptions rowOptions) {
			String lSide = null;
			if (rowOptions.isSetLoadDate() || rowOptions.isSetLadenFuelChoice() || rowOptions.isSetLadenRoute()) {
				final StringBuilder sb = new StringBuilder();
				sb.append("Buy: ");
				if (rowOptions.isSetLoadDate()) {
					sb.append("D");
				}
				if (rowOptions.isSetLadenFuelChoice()) {
					sb.append("F");
				}
				if (rowOptions.isSetLadenRoute()) {
					sb.append("R");
				}
				lSide = sb.toString();
			}
			String rSide = null;
			if (rowOptions.isSetDischargeDate() || rowOptions.isSetBallastFuelChoice() || rowOptions.isSetBallastRoute()) {
				final StringBuilder sb = new StringBuilder();
				sb.append("Sell: ");
				if (rowOptions.isSetDischargeDate()) {
					sb.append("D");
				}
				if (rowOptions.isSetBallastFuelChoice()) {
					sb.append("F");
				}
				if (rowOptions.isSetBallastRoute()) {
					sb.append("R");
				}
				rSide = sb.toString();
			}

			if (lSide != null && rSide != null) {
				return lSide + " " + rSide;
			}
			if (lSide != null) {
				return lSide;
			}
			if (rSide != null) {
				return rSide;
			}

			return "";
		}

		return "";
	}

}

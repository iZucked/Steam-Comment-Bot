/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.incomestatement;

import java.util.Collection;

import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;

public class IncomeStatementByRegion extends AbstractIncomeStatement<Regions> {

	public IncomeStatementByRegion() {
		super("com.mmxlabs.lingo.reports.Reports_IncomeStatementByRegion");
	}

	@Override
	protected Regions getSubType(final DischargeSlot dischargeSlot) {
		if (dischargeSlot == null) {
			return Regions.OTHER;
		}
		final Port port = dischargeSlot.getPort();
		if (port == null) {
			return Regions.OTHER;
		}
		final Location location = port.getLocation();
		if (location == null) {
			return Regions.OTHER;
		}
		if (location.getCountry() == null) {
			return Regions.OTHER;
		}
		final String country = location.getCountry().toLowerCase();
		switch (country) {
		case "china":
		case "china, peoples rep":
		case "taiwan":
		case "south korea":
		case "japan":
		case "australia":
		case "united arab emirates":
		case "u.a.e.":
		case "kuwait":
		case "pakistan":
		case "india":
			return Regions.FAR_EAST_AND_MIDDLE_EAST;
		case "spain":
		case "portugal":
		case "norway":
		case "united kingdom":
		case "turkey":
		case "greece":
		case "cyprus":
			return Regions.EUROPE;
		case "canada":
		case "chile":
		case "u.k.":
		case "u.s.a.":
		case "argentina":
		case "brazil":
			return Regions.AMERICAS;
		default:
			return Regions.OTHER;
		}
	}

	@Override
	protected Collection<Regions> getSubTypes() {
		return Lists.newArrayList(Regions.values());
	}

	@Override
	protected int doCompare(final IncomeStatementData o1, final IncomeStatementData o2) {
		return ((Enum) o1.key).ordinal() - ((Enum) o2.key).ordinal();
	}

	@Override
	protected void resetData() {

	}

	@Override
	protected String doGetColumnText(final IncomeStatementData data) {
		return data.key.toString();
	}
}
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;

import com.mmxlabs.lingo.reports.modelbased.ColumnGenerator.ColumnInfo;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

public class EmissionsUtils {
	
	private EmissionsUtils() {}
	
	public static final double LNG_DENSITY_TON_PER_M3 = 0.450;
	public static final double LNG_EMISSION_RATE_TON_CO2_PER_TON_FUEL = 2.750;
	
	public static final int METHANE_CO2_EQUIVALENT = 25;
	public static final double MT_TO_GRAMS = 1_000_000.0;
	public static final double GRAMS_TO_TONS = 0.000_001;
	
	@SuppressWarnings("null")
	public static Long consumedQuantityLNG(final FuelQuantity fuelQuantity) {

		final Set<FuelUnit> uniqueUnits = new HashSet<>();
		for (final FuelAmount fuelAmount : fuelQuantity.getAmounts()) {
			uniqueUnits.add(fuelAmount.getUnit());
		}

		final boolean unitsAreTheSame = uniqueUnits.size() == 1;
		double quantity = 0.0;

		if (unitsAreTheSame) {
			final FuelUnit unitItSelf = uniqueUnits.stream().findAny().orElseThrow();
			quantity = fuelQuantity.getAmounts().stream().map(FuelAmount::getQuantity).reduce(Double::sum).orElse(0.0);

			// Convert whatever to MT
			quantity = switch (unitItSelf) {
			case MMBTU -> throw new IllegalStateException("Bad");
			case M3 -> quantity * LNG_DENSITY_TON_PER_M3;
			case MT -> quantity;
			};
		} else {
			for (final FuelAmount amount : fuelQuantity.getAmounts()) {
				if (amount.getUnit() == FuelUnit.M3) {
					quantity = amount.getQuantity();
				}
			}
			quantity *= LNG_DENSITY_TON_PER_M3;
		}
		return Math.round(quantity);
	}
	
	public static void styleTheCell(final ViewerCell cell, final Field f) {
		IEmissionReportIDData model = (IEmissionReportIDData) cell.getItem().getData();
		if ("scenarioName".equals(f.getName()) && model.isPinned()) {
			cell.setImage(CommonImages.getImage(IconPaths.Pin_8, IconMode.Enabled));
		}
		if ("TOTAL".equals(model.getScenarioName())) {
			// make BOLD
		}
	}
	
	public static void changeScenarioNameColumnVisibility(ColumnInfo columnInfo, final boolean isVisible) {
		for (Map.Entry<Field, GridViewerColumn> e : columnInfo.mapOfFieldColumns.entrySet()) {
			final Field f = e.getKey();
			if (f != null && "scenarioName".equals(f.getName())) {
				e.getValue().getColumn().setVisible(isVisible);
			}
		}
	}
	
	public static boolean checkIVesselEmissionsAreSimilar(final IEmissionReportIDData r1, final IEmissionReportIDData r2) {
		boolean result = false;
		if (r1 != r2 && !r1.getSchedule().equals(r2.getSchedule()) && r1.getEventID().equals(r2.getEventID()) //
				&& (r1.getOtherID() != null && r2.getOtherID() != null && r1.getOtherID().equals(r2.getOtherID()))) {
			result = true;
		}
		if (result) {
			result = (r1.getVesselName() == null && r2.getVesselName() == null) || (r1.getVesselName() != null && r2.getVesselName() != null && r1.getVesselName().equals(r2.getVesselName()));
		}
		return result;
	}
	
	public static ViewerFilter createFilter(final boolean deltaMode, final boolean processDeltas) {
		return new ViewerFilter() {

			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				if (!deltaMode) {
					return true;
				}
				if (element instanceof final IEmissionReportIDData ive) {
					return processDeltas && ("Total Δ".equals(ive.getScenarioName()) || "Δ".equals(ive.getScenarioName()));
				}
				return false;
			}
		};
	}
}

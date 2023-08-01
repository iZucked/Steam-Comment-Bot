/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions;

import java.lang.reflect.Field;
import java.util.Map;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;

import com.mmxlabs.lingo.reports.modelbased.ColumnGenerator.ColumnInfo;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

public class EmissionReportUtils {
	
	private EmissionReportUtils() {}
	
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
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views.colourschemes;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.lingo.reports.IScenarioViewerSynchronizerOutput;
import com.mmxlabs.models.lng.cargo.CargoGroup;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.schedule.SlotVisit;

public class StatoilColourScheme extends VesselStateColourScheme {

	private static final String Load_Prog = "LoadProg";

	@Override
	public String getName() {
		return "Loading Prog";
	}

	@Override
	public Color getBorderColour(final Object element) {

		if (isHammProgFOB(element)) {
			return Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
		} else {
			return super.getBorderColour(element);
		}
	}

	private boolean isHammProgFOB(final Object element) {
		if (element instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) element;
			final Object input = viewer.getInput();
			if (input instanceof IScenarioViewerSynchronizerOutput) {
				final IScenarioViewerSynchronizerOutput output = (IScenarioViewerSynchronizerOutput) input;
				final LNGPortfolioModel portfolioModel = output.getLNGPortfolioModel(visit.getSequence().eContainer());
				final CargoModel cargoModel = portfolioModel.getCargoModel();
				final List<CargoGroup> cargoGroups = cargoModel.getCargoGroups();
				boolean isFOB;
				isFOB = ColourSchemeUtil.isFOBSaleCargo(visit);
				for (final CargoGroup cg : cargoGroups) {
					// if (true || cg.getName().equalsIgnoreCase(Load_Prog)) {
					if (isFOB && cg.getCargoes().contains(visit.getSlotAllocation().getCargoAllocation().getInputCargo())) {
						return true;
					}
					// }
				}
			}
		}
		return false;
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes;

import static com.mmxlabs.shiplingo.platform.scheduleview.views.colourschemes.ColourSchemeUtil.Alert_Crimson;

import java.util.List;

import org.eclipse.nebula.widgets.ganttchart.ColorCache;
import org.eclipse.swt.graphics.Color;

import com.mmxlabs.models.lng.cargo.CargoGroup;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.shiplingo.platform.reports.IScenarioViewerSynchronizerOutput;

public class StatoilColourScheme extends VesselStateColourScheme {
	
	private static final String Load_Prog = "LoadProg";

	@Override
	public String getName() {
		return "Loading Prog";
	}

	@Override
	public Color getBorderColour(final Object element) {

		if(isHammProgFOB(element)) {
			return ColorCache.getColor(Alert_Crimson);			
		}		
		else{
			return super.getBorderColour(element);
		}
	}

	private boolean isHammProgFOB(final Object element) {
		if(element instanceof SlotVisit){
			final SlotVisit visit = (SlotVisit) element;
			final Object input = viewer.getInput();
			if (input instanceof IScenarioViewerSynchronizerOutput) {
				final IScenarioViewerSynchronizerOutput output = (IScenarioViewerSynchronizerOutput) input;
				MMXRootObject rootObject = output.getRootObject(visit.getSequence().eContainer());
				CargoModel cargoModel = rootObject.getSubModel(CargoModel.class);
				List<CargoGroup> cargoGroups = cargoModel.getCargoGroups();
				boolean isFOB;
				isFOB = ColourSchemeUtil.isFOBSaleCargo(visit);
				for (CargoGroup cg : cargoGroups) {
//					if (true || cg.getName().equalsIgnoreCase(Load_Prog)) {
						if(isFOB && cg.getCargoes().contains(visit.getSlotAllocation().getCargoAllocation().getInputCargo())) {
							return true;							
						}
//					}
				}
			}
		}
		return false;
	}
}

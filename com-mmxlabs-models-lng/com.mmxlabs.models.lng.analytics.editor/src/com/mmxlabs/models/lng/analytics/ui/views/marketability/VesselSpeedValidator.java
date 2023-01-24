package com.mmxlabs.models.lng.analytics.ui.views.marketability;

import java.util.Collection;
import java.util.List;

import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;

public class VesselSpeedValidator {
	public static boolean validate(Collection<Vessel> vessels, int speed) {
		// TODO: USE CONSUMPTION CURVES
		for(Vessel vessel : vessels) {
			if(vessel.getMinSpeed() > speed || vessel.getMaxSpeed() < speed ) {
				return false;
			}
			
//			List<VesselStateAttributes> attributes = List.of(vessel.getBallastAttributes(), vessel.getLadenAttributes());
//			attributes.stream().forEach( x->
//			{
//				x.getFuelConsumption().stream().forEach(fc -> {
//					//fc
//				});
//			}
//			);
		}
		return true;
	}
	
	private VesselSpeedValidator() {}
}

package com.mmxlabs.models.lng.analytics.ui.views.formatters;

import com.mmxlabs.lingo.reports.views.formatters.BaseFormatter;
import com.mmxlabs.models.lng.analytics.FleetShippingOption;
import com.mmxlabs.models.lng.analytics.NominatedShippingOption;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;

public class ShippingOptionDescriptionFormatter extends BaseFormatter {
	@Override
	public String render(final Object object) {

		if (object instanceof RoundTripShippingOption) {
			RoundTripShippingOption option = (RoundTripShippingOption) object;
			VesselClass vesselClass = option.getVesselClass();
			String vesselName = "<No vessel>";
			if (vesselClass != null) {
				String s = vesselClass.getName();
				if (s != null && !s.trim().isEmpty()) {
					vesselName = s;
				}
			}
			String hireCost = "<No hire cost>";
			String s = option.getHireCost();
			if (s != null && !s.trim().isEmpty()) {
				hireCost = s;
			}
			return String.format("RT: %s @ %s", vesselName, hireCost);
		} else if (object instanceof FleetShippingOption) {
			FleetShippingOption option = (FleetShippingOption) object;
			Vessel nominatedVessel = option.getVessel();
			String vesselName = "<No vessel>";
			if (nominatedVessel != null) {
				String s = nominatedVessel.getName();
				if (s != null && !s.trim().isEmpty()) {
					vesselName = s;
				}
			}
			String hireCost = "<No hire cost>";
			String s = option.getHireCost();
			if (s != null && !s.trim().isEmpty()) {
				hireCost = s;
			}
			return String.format("RT: %s @ %s", vesselName, hireCost);
		} else if (object instanceof NominatedShippingOption) {
			NominatedShippingOption option = (NominatedShippingOption) object;
			Vessel nominatedVessel = option.getNominatedVessel();
			String vesselName = "<No vessel>";
			if (nominatedVessel != null) {
				String s = nominatedVessel.getName();
				if (s != null && !s.trim().isEmpty()) {
					vesselName = s;
				}
			}
			return String.format("Nominated %s", vesselName);
		}

		if (object == null) {
			return "";
		} else {
			return object.toString();
		}
	}
}

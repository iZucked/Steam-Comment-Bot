package com.mmxlabs.models.lng.analytics.ui.views.formatters;

import java.util.Collection;

import com.mmxlabs.models.lng.analytics.FleetShippingOption;
import com.mmxlabs.models.lng.analytics.NominatedShippingOption;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.ui.tabular.BaseFormatter;

public class ShippingOptionDescriptionFormatter extends BaseFormatter {
	@Override
	public String render(final Object object) {

		if (object instanceof PartialCaseRow) {
			final PartialCaseRow partialCaseRow = (PartialCaseRow) object;
			final Collection<?> shipping = partialCaseRow.getShipping();

			return render(shipping);

		} else if (object instanceof Collection<?>) {
			final Collection<?> collection = (Collection<?>) object;

			if (collection.isEmpty()) {
				return "<unset>";
			}

			final StringBuilder sb = new StringBuilder();
			boolean first = true;
			for (final Object o : collection) {
				if (!first) {
					sb.append("\n");
				}
				sb.append(render(o));
				first = false;
			}
			return sb.toString();
		} else if (object instanceof Object[]) {
			final Object[] objects = (Object[]) object;

			if (objects.length == 0) {
				return "<open>";
			}
			final StringBuilder sb = new StringBuilder();
			boolean first = true;
			for (final Object o : objects) {
				if (!first) {
					sb.append("\n");
				}
				sb.append(render(o));
				first = false;
			}
			return sb.toString();
		} else if (object instanceof RoundTripShippingOption) {
			final RoundTripShippingOption option = (RoundTripShippingOption) object;
			final VesselClass vesselClass = option.getVesselClass();
			String vesselName = "<No vessel>";
			if (vesselClass != null) {
				final String s = vesselClass.getName();
				if (s != null && !s.trim().isEmpty()) {
					vesselName = s;
				}
			}
			String hireCost = "<No hire cost>";
			final String s = option.getHireCost();
			if (s != null && !s.trim().isEmpty()) {
				hireCost = s;
			}
			return String.format("%s @ %s", vesselName, hireCost);
		} else if (object instanceof FleetShippingOption) {
			final FleetShippingOption option = (FleetShippingOption) object;
			final Vessel nominatedVessel = option.getVessel();
			String vesselName = "<No vessel>";
			if (nominatedVessel != null) {
				final String s = nominatedVessel.getName();
				if (s != null && !s.trim().isEmpty()) {
					vesselName = s;
				}
			}
			String hireCost = "<No hire cost>";
			final String s = option.getHireCost();
			if (s != null && !s.trim().isEmpty()) {
				hireCost = s;
			}
			return String.format("%s%s @ %s", ""// object instanceof OptionalAvailabilityShippingOption ? "~" : ""
					, vesselName, hireCost);
		} else if (object instanceof NominatedShippingOption) {
			final NominatedShippingOption option = (NominatedShippingOption) object;
			final Vessel nominatedVessel = option.getNominatedVessel();
			String vesselName = "<No vessel>";
			if (nominatedVessel != null) {
				final String s = nominatedVessel.getName();
				if (s != null && !s.trim().isEmpty()) {
					vesselName = s;
				}
			}
			return String.format("%s (nominated)", vesselName);
		}

		if (object == null) {
			return "<unset>";
		} else {
			return object.toString();
		}
	}

}

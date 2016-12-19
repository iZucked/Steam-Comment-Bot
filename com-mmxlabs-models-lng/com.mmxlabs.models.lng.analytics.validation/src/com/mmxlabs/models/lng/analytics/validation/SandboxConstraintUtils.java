package com.mmxlabs.models.lng.analytics.validation;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.AnalyticsBuilder;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.util.SetUtils;

public class SandboxConstraintUtils {

	public static boolean portRestrictionsValid(final BuyOption buy, final SellOption sell, final ShippingOption shippingOption) {
		final Collection<APortSet<Port>> portRestrictions = AnalyticsBuilder.getPortRestrictions(shippingOption);
		final Port buyPort = AnalyticsBuilder.getPort(buy);
		final Port sellPort = AnalyticsBuilder.getPort(sell);
		if (!portRestrictions.isEmpty()) {
			for (final APortSet<Port> portFromSet : portRestrictions) {
				final Optional<Port> value = SetUtils.getObjects(portFromSet).stream().filter(p -> p.equals(buyPort) || p.equals(sellPort)).findFirst();
				if (value.isPresent() && value.get() != null) {
					return false;
				}
			}
		}
		return true;
	}
		
	public static boolean vesselRestrictionsValid(final BuyOption buy, final SellOption sell, final ShippingOption shippingOption) {
		if (buy instanceof BuyReference) {
			final Set<AVesselSet<Vessel>> allowedVessels = AnalyticsBuilder.getBuyVesselRestrictions(buy);
			final AVesselSet<Vessel> vesselSet = AnalyticsBuilder.getAVesselSet(shippingOption);
			 if (!allowedVessels.isEmpty() && vesselSet != null) {
				 return allowedVessels.contains(vesselSet);
			 }
		}
		return true;
	}
	
	public static boolean checkVolumeAgainstBuyAndSell(final BuyOption buy, final SellOption sell) {
		if (buy == null || sell == null) {
			return true;
		}
		final int[] buyVolumeInMMBTU = AnalyticsBuilder.getBuyVolumeInMMBTU(buy);
		final int[] sellVolumeInMMBTU = AnalyticsBuilder.getSellVolumeInMMBTU(buy, sell);
		if (buyVolumeInMMBTU == null
				|| sellVolumeInMMBTU == null) {
			return true;
		}
		if (buyVolumeInMMBTU[0] > sellVolumeInMMBTU[1]) {
			return false;
		}
		return true;
	}

	public static boolean checkVolumeAgainstVessel(final BuyOption buy, final SellOption sell,
			final ShippingOption shippingOption) {
		if (buy == null || sell == null || shippingOption == null) {
			return true;
		}
		final double cargoCV = AnalyticsBuilder.getCargoCV(buy);
		if (cargoCV == 0) {
			return false;
		}
		final int[] buyVolumeInMMBTU = AnalyticsBuilder.getBuyVolumeInMMBTU(buy);
		final int[] sellVolumeInMMBTU = AnalyticsBuilder.getSellVolumeInMMBTU(buy, sell);
		if (buyVolumeInMMBTU == null
				|| sellVolumeInMMBTU == null) {
			return true;
		}
		final int capacityInMMBTU = (int) ((double) AnalyticsBuilder.getVesselCapacityInM3(shippingOption) * cargoCV);
		if (buyVolumeInMMBTU[0] > capacityInMMBTU) {
			return false;
		}
		if (sellVolumeInMMBTU[0] > capacityInMMBTU) {
			return false;
		}
		return true;
	}

}

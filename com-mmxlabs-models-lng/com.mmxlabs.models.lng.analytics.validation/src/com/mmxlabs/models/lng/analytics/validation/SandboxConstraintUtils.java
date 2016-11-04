package com.mmxlabs.models.lng.analytics.validation;

import java.util.Collection;
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

	public static boolean portRestrictionsValid(BuyOption buy, SellOption sell, ShippingOption shippingOption) {
		Collection<APortSet<Port>> portRestrictions = AnalyticsBuilder.getPortRestrictions(shippingOption);
		Port buyPort = AnalyticsBuilder.getPort(buy);
		Port sellPort = AnalyticsBuilder.getPort(sell);
		if (!portRestrictions.isEmpty()) {
			for (APortSet<Port> portFromSet : portRestrictions) {
				if (SetUtils.getObjects(portFromSet).stream().filter(p -> p.equals(buyPort) || p.equals(sellPort)).findFirst().get() != null) {
					return false;
				}
			}
		}
		return true;
	}
		
	public static boolean vesselRestrictionsValid(BuyOption buy, SellOption sell, ShippingOption shippingOption) {
		if (buy instanceof BuyReference) {
			Set<AVesselSet<Vessel>> allowedVessels = AnalyticsBuilder.getBuyVesselRestrictions(buy);
			AVesselSet<Vessel> vesselSet = AnalyticsBuilder.getAVesselSet(shippingOption);
			 if (!allowedVessels.isEmpty() && vesselSet != null) {
				 return allowedVessels.contains(vesselSet);
			 }
		}
		return true;
	}
	
	public static boolean checkVolumeAgainstBuyAndSell(BuyOption buy, SellOption sell) {
		if (buy == null || sell == null) {
			return true;
		}
		int[] buyVolumeInMMBTU = AnalyticsBuilder.getBuyVolumeInMMBTU(buy);
		int[] sellVolumeInMMBTU = AnalyticsBuilder.getSellVolumeInMMBTU(buy, sell);
		if (buyVolumeInMMBTU == null
				&& sellVolumeInMMBTU == null) {
			return true;
		}
		if (buyVolumeInMMBTU[0] > sellVolumeInMMBTU[1]) {
			return false;
		}
		return true;
	}

	public static boolean checkVolumeAgainstVessel(BuyOption buy, SellOption sell,
			ShippingOption shippingOption) {
		if (buy == null || sell == null || shippingOption == null) {
			return true;
		}
		double cargoCV = AnalyticsBuilder.getCargoCV(buy);
		if (cargoCV == 0) {
			return false;
		}
		int[] buyVolumeInMMBTU = AnalyticsBuilder.getBuyVolumeInMMBTU(buy);
		int[] sellVolumeInMMBTU = AnalyticsBuilder.getSellVolumeInMMBTU(buy, sell);
		if (buyVolumeInMMBTU == null
				&& sellVolumeInMMBTU == null) {
			return true;
		}
		int capacityInMMBTU = (int) ((double) AnalyticsBuilder.getVesselCapacityInM3(shippingOption) * cargoCV);
		if (buyVolumeInMMBTU[0] > capacityInMMBTU) {
			return false;
		}
		if (sellVolumeInMMBTU[0] > capacityInMMBTU) {
			return false;
		}
		return true;
	}

}

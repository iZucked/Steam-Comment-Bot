package com.mmxlabs.models.lng.analytics.ui.views.viability;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;
import com.mmxlabs.models.lng.analytics.ExistingVesselAvailability;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.ViabilityModel;
import com.mmxlabs.models.lng.analytics.ViabilityRow;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;

public final class ViabilityUtils {
	public static ViabilityModel createModelFromScenario(final @NonNull LNGScenarioModel sm, final @NonNull String name) {
		final ViabilityModel model = AnalyticsFactory.eINSTANCE.createViabilityModel();
		model.setName(name);
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sm);
		final SpotMarketsModel spotModel = ScenarioModelUtil.getSpotMarketsModel(sm);

		for (final LoadSlot slot : cargoModel.getLoadSlots()) {
			if (slot.getCargo() == null) {
				final BuyReference buy = AnalyticsFactory.eINSTANCE.createBuyReference();
				buy.setSlot(slot);
				model.getBuys().add(buy);
			}
		}
		for (final DischargeSlot slot : cargoModel.getDischargeSlots()) {
			if (slot.getCargo() == null) {
				final SellReference sale = AnalyticsFactory.eINSTANCE.createSellReference();
				sale.setSlot(slot);
				model.getSells().add(sale);
			}
		}
		for (final VesselAvailability vessel : cargoModel.getVesselAvailabilities()) {
			if (vessel != null) {
				final ExistingVesselAvailability v = AnalyticsFactory.eINSTANCE.createExistingVesselAvailability();
				v.setVesselAvailability(vessel);
				model.getShippingTemplates().add(v);
			}
		}
		for (final CharterInMarket cim : spotModel.getCharterInMarkets()) {
			if (cim != null) {
				if (cim.getVessel() != null) {
					if (cim.isEnabled() && cim.getSpotCharterCount() > 0) {
						final ExistingCharterMarketOption ecmo = AnalyticsFactory.eINSTANCE.createExistingCharterMarketOption();
						
						ecmo.setCharterInMarket(cim);
						ecmo.setSpotIndex(0);
						//ecmo.setSpotIndex(cim.getSpotCharterCount());
						
						model.getShippingTemplates().add(ecmo);
					}
				}
			}
		}
		// final SpotMarketGroup smgDP = spotModel.getDesPurchaseSpotMarket();
		// if (smgDP != null) {
		// for (final SpotMarket spotMarket : smgDP.getMarkets()) {
		// if (spotMarket != null) {
		// model.getMarkets().add(spotMarket);
		// }
		// }
		// }
		final SpotMarketGroup smgDS = spotModel.getDesSalesSpotMarket();
		if (smgDS != null) {
			for (final SpotMarket spotMarket : smgDS.getMarkets()) {
				if (spotMarket != null) {
					if (spotMarket.isEnabled()) {
						model.getMarkets().add(spotMarket);
					}
				}
			}
		}
		// final SpotMarketGroup smgFP = spotModel.getFobPurchasesSpotMarket();
		// if (smgFP != null) {
		// for (final SpotMarket spotMarket : smgFP.getMarkets()) {
		// if (spotMarket != null) {
		// model.getMarkets().add(spotMarket);
		// }
		// }
		// }
		final SpotMarketGroup smgFS = spotModel.getFobSalesSpotMarket();
		if (smgFS != null) {
			for (final SpotMarket spotMarket : smgFS.getMarkets()) {
				if (spotMarket != null) {
					if (spotMarket.isEnabled()) {
						model.getMarkets().add(spotMarket);
					}
				}
			}
		}

		populateModel(model);
		
		return model;
	}
	
	public static void populateModel(final @NonNull ViabilityModel model) {
		for (final BuyOption bo : model.getBuys()) {
			for (final ShippingOption so : model.getShippingTemplates()) {
				final ViabilityRow row = AnalyticsFactory.eINSTANCE.createViabilityRow();
				row.setBuyOption(bo);
				row.setShipping(so);
				model.getRows().add(row);
			}
		}
	}
}

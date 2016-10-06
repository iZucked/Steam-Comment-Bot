package com.mmxlabs.models.lng.analytics.ui.views.evaluators;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

public class AnalyticsBuilder {
	public static @Nullable LoadSlot makeLoadSlot(final @Nullable BuyOption buy, final @NonNull LNGScenarioModel lngScenarioModel) {

		if (buy instanceof BuyReference) {
			return ((BuyReference) buy).getSlot();
		} else if (buy instanceof BuyOpportunity) {
			final BuyOpportunity buyOpportunity = (BuyOpportunity) buy;
			final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
			slot.setOptional(true);
			slot.setName(EcoreUtil.generateUUID());
			slot.setPort(buyOpportunity.getPort());
			if (buyOpportunity.isDesPurchase()) {
				slot.setDESPurchase(true);
			}
			if (buyOpportunity.getContract() != null) {
				slot.setContract(buyOpportunity.getContract());
			}
			if (buyOpportunity.getPriceExpression() != null) {
				slot.setPriceExpression(buyOpportunity.getPriceExpression());
			}
			if (buyOpportunity.getDate() != null) {
				slot.setWindowStart(buyOpportunity.getDate());
			}
			if (buyOpportunity.getEntity() != null) {
				slot.setEntity(buyOpportunity.getEntity());
			}
			return slot;
		} else if (buy instanceof BuyMarket) {
			final BuyMarket buyMarket = (BuyMarket) buy;
			final SpotLoadSlot slot = CargoFactory.eINSTANCE.createSpotLoadSlot();
			slot.setOptional(true);
			final SpotMarket market = buyMarket.getMarket();
			if (market instanceof DESPurchaseMarket) {
				slot.setDESPurchase(true);
			} else if (market instanceof FOBPurchasesMarket) {
				final FOBPurchasesMarket desSalesMarket = (FOBPurchasesMarket) market;
				slot.setPort(desSalesMarket.getNotionalPort());
			}
			slot.setMarket(market);
			slot.setName(EcoreUtil.generateUUID());

			return slot;
		}

		return null;
	}

	public static @Nullable DischargeSlot makeDischargeSlot(final @Nullable SellOption sell, final @NonNull LNGScenarioModel lngScenarioModel) {
		if (sell instanceof SellReference) {
			return ((SellReference) sell).getSlot();
		} else if (sell instanceof SellOpportunity) {
			final SellOpportunity sellOpportunity = (SellOpportunity) sell;
			final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();
			slot.setOptional(true);
			slot.setName(EcoreUtil.generateUUID());
			slot.setPort(sellOpportunity.getPort());
			if (sellOpportunity.isFobSale()) {
				slot.setFOBSale(true);
			}
			if (sellOpportunity.getContract() != null) {
				slot.setContract(sellOpportunity.getContract());
			}
			if (sellOpportunity.getPriceExpression() != null) {
				slot.setPriceExpression(sellOpportunity.getPriceExpression());
			}
			if (sellOpportunity.getDate() != null) {
				slot.setWindowStart(sellOpportunity.getDate());
			}
			if (sellOpportunity.getEntity() != null) {
				slot.setEntity(sellOpportunity.getEntity());
			}
			return slot;
		} else if (sell instanceof SellMarket) {
			final SellMarket sellMarket = (SellMarket) sell;
			final SpotDischargeSlot slot = CargoFactory.eINSTANCE.createSpotDischargeSlot();
			slot.setOptional(true);
			final SpotMarket market = sellMarket.getMarket();
			if (market instanceof FOBSalesMarket) {
				slot.setFOBSale(true);
			} else if (market instanceof DESSalesMarket) {
				final DESSalesMarket desSalesMarket = (DESSalesMarket) market;
				slot.setPort(desSalesMarket.getNotionalPort());
			}
			slot.setMarket(market);
			slot.setName(EcoreUtil.generateUUID());

			return slot;
		}

		return null;
	}

	public static int calculateTravelDaysForLoad(final LoadSlot loadSlot, final DischargeSlot dischargeSlot, final ShippingOption shippingOption) {

		// TODO: Get speed from shipping option.
		// double speed = 17.0;
		// TODO: Get vessel class from shipping option.
		// VesselClass vesselClass;
		// TODO: Single route or closed route?
		// TravelTimeUtils.getTimeForRoute(, referenceSpeed, route, fromPort, toPort);

		return 20;
	}

	public static int calculateTravelDaysForDischarge(final LoadSlot loadSlot, final DischargeSlot dischargeSlot, final ShippingOption shippingOption) {
		return 20;
	}

	public enum ShippingType {
		Shipped, NonShipped, Mixed
	}

	public static ShippingType isNonShipped(@NonNull final PartialCaseRow row) {
		if (row.getBuyOptions().isEmpty() || row.getSellOptions().isEmpty()) {
			return ShippingType.Mixed;
		}

		ShippingType buyType = null;
		for (final BuyOption buy : row.getBuyOptions()) {
			buyType = updateTypeForLoad(buy, buyType);
		}
		if (buyType == ShippingType.Mixed) {
			return ShippingType.Mixed;
		}
		ShippingType sellType = null;
		for (final SellOption sell : row.getSellOptions()) {
			sellType = updateTypeForDischarge(sell, sellType);
		}
		if (sellType == ShippingType.Mixed) {
			return ShippingType.Mixed;
		}
		if (buyType == ShippingType.NonShipped && sellType == ShippingType.NonShipped) {
			return ShippingType.Mixed;
		}
		if (buyType == ShippingType.Shipped && sellType == ShippingType.Shipped) {
			return ShippingType.Shipped;
		}
		return ShippingType.NonShipped;
	}

	private static ShippingType updateTypeForLoad(final BuyOption buy, final ShippingType type) {
		if (type == ShippingType.Mixed) {
			return ShippingType.Mixed;
		}

		final ShippingType thisType = getBuyShippingType(buy);

		if (type == null) {
			return thisType;
		} else {
			if (type == ShippingType.Shipped && thisType == ShippingType.Shipped) {
				return ShippingType.Shipped;
			}
			if (type == ShippingType.NonShipped && thisType == ShippingType.NonShipped) {
				return ShippingType.NonShipped;
			}
		}
		return ShippingType.Mixed;
	}

	private static ShippingType updateTypeForDischarge(final SellOption sell, final ShippingType type) {
		if (type == ShippingType.Mixed) {
			return ShippingType.Mixed;
		}

		final ShippingType thisType = getSellShippingType(sell);

		if (type == null) {
			return thisType;
		} else {
			if (type == ShippingType.Shipped && thisType == ShippingType.Shipped) {
				return ShippingType.Shipped;
			}
			if (type == ShippingType.NonShipped && thisType == ShippingType.NonShipped) {
				return ShippingType.NonShipped;
			}
		}
		return ShippingType.Mixed;
	}

	public static ShippingType isNonShipped(@NonNull final BaseCaseRow row) {
		if (row.getBuyOption() == null || row.getSellOption() == null) {
			return ShippingType.Mixed;
		}
		final BuyOption buy = row.getBuyOption();

		final ShippingType buyType = getBuyShippingType(buy);

		if (buyType == ShippingType.Mixed) {
			return ShippingType.Mixed;
		}

		final SellOption sell = row.getSellOption();
		final ShippingType sellType = getSellShippingType(sell);

		if (sellType == ShippingType.Mixed) {
			return ShippingType.Mixed;
		}
		if (buyType == ShippingType.NonShipped && sellType == ShippingType.NonShipped) {
			return ShippingType.Mixed;
		}
		if (buyType == ShippingType.Shipped && sellType == ShippingType.Shipped) {
			return ShippingType.Shipped;
		}
		return ShippingType.NonShipped;
	}

	public static ShippingType getBuyShippingType(@NonNull final BuyOption buy) {
		if (buy instanceof BuyReference) {
			final BuyReference buyReference = (BuyReference) buy;
			final LoadSlot load = buyReference.getSlot();
			if (load != null) {
				return load.isDESPurchase() ? ShippingType.NonShipped : ShippingType.Shipped;
			}
			return ShippingType.Mixed;

		} else if (buy instanceof BuyOpportunity) {
			final BuyOpportunity buyOpportunity = (BuyOpportunity) buy;
			return buyOpportunity.isDesPurchase() ? ShippingType.NonShipped : ShippingType.Shipped;
		} else if (buy instanceof BuyMarket) {
			final BuyMarket buyMarket = (BuyMarket) buy;
			final SpotMarket spotMarket = buyMarket.getMarket();
			return spotMarket instanceof DESPurchaseMarket ? ShippingType.NonShipped : ShippingType.Shipped;
		}

		throw new IllegalArgumentException("Unsupported BuyOption type");
	}

	public static ShippingType getSellShippingType(@NonNull final SellOption sell) {
		if (sell instanceof SellReference) {
			final SellReference sellReference = (SellReference) sell;
			final DischargeSlot discharge = sellReference.getSlot();
			if (discharge != null) {
				return discharge.isFOBSale() ? ShippingType.NonShipped : ShippingType.Shipped;
			}
			return ShippingType.Mixed;

		} else if (sell instanceof SellOpportunity) {
			final SellOpportunity sellOpportunity = (SellOpportunity) sell;
			return sellOpportunity.isFobSale() ? ShippingType.NonShipped : ShippingType.Shipped;
		} else if (sell instanceof SellMarket) {
			final SellMarket buyMarket = (SellMarket) sell;
			final SpotMarket spotMarket = buyMarket.getMarket();
			return spotMarket instanceof FOBSalesMarket ? ShippingType.NonShipped : ShippingType.Shipped;
		}

		throw new IllegalArgumentException("Unsupported SellOption type");
	}

}

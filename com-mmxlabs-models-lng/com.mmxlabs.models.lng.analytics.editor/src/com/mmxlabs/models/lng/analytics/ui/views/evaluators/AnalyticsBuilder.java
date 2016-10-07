package com.mmxlabs.models.lng.analytics.ui.views.evaluators;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.FleetShippingOption;
import com.mmxlabs.models.lng.analytics.NominatedShippingOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;

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
		None, Shipped, NonShipped, Mixed
	}

	public static ShippingType isNonShipped(@NonNull final PartialCaseRow row) {
		if (row.getBuyOptions().isEmpty() || row.getSellOptions().isEmpty()) {
			return ShippingType.None;
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
			if (thisType == ShippingType.None) {
				return type;
			}
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
			if (thisType == ShippingType.None) {
				return type;
			}
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
		if (buyType == ShippingType.None) {
			return sellType;
		}
		if (sellType == ShippingType.None) {
			return buyType;
		}
		return ShippingType.NonShipped;
	}

	public static ShippingType getBuyShippingType(@Nullable final BuyOption buy) {
		if (buy == null) {
			return ShippingType.None;
		} else if (buy instanceof BuyReference) {
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

	public static ShippingType getSellShippingType(@Nullable final SellOption sell) {
		if (sell == null) {
			return ShippingType.None;
		} else if (sell instanceof SellReference) {
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

	public static void setDefaultEntity(final IScenarioEditingLocation scenarioEditingLocation, final BuyOpportunity buy) {
		final BaseLegalEntity entity = getDefaultEntity(scenarioEditingLocation);
		if (entity != null) {
			buy.setEntity(entity);
		}
	}

	public static void setDefaultEntity(final IScenarioEditingLocation scenarioEditingLocation, final SellOpportunity sell) {
		final BaseLegalEntity entity = getDefaultEntity(scenarioEditingLocation);
		if (entity != null) {
			sell.setEntity(entity);
		}
	}

	public static void setDefaultEntity(final IScenarioEditingLocation scenarioEditingLocation, final FleetShippingOption option) {
		final BaseLegalEntity entity = getDefaultEntity(scenarioEditingLocation);
		if (entity != null) {
			option.setEntity(entity);
		}
	}

	public static @Nullable BaseLegalEntity getDefaultEntity(final IScenarioEditingLocation scenarioEditingLocation) {
		final MMXRootObject rootObject = scenarioEditingLocation.getRootObject();
		if (rootObject instanceof LNGScenarioModel) {
			final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel((LNGScenarioModel) rootObject);
			if (commercialModel.getEntities().size() == 1) {
				return commercialModel.getEntities().get(0);
			}
		}
		return null;
	}

	public static BuyOption getOrCreateBuyOption(final LoadSlot loadSlot, final OptionAnalysisModel optionAnalysisModel, final IScenarioEditingLocation scenarioEditingLocation,
			final CompoundCommand cmd) {

		BuyReference buyRef = null;

		if (loadSlot != null) {
			for (final BuyOption buy : optionAnalysisModel.getBuys()) {
				if (buy instanceof BuyReference) {
					final BuyReference buyReference = (BuyReference) buy;
					if (buyReference.getSlot() == loadSlot) {
						buyRef = buyReference;
						break;
					}
				}
			}
			if (buyRef == null) {
				buyRef = AnalyticsFactory.eINSTANCE.createBuyReference();
				buyRef.setSlot(loadSlot);
				cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BUYS, buyRef));
			}
		}
		return buyRef;
	}

	public static ShippingOption getOrCreateShippingOption(final Cargo cargo, final LoadSlot load, final DischargeSlot dischargeSlot, final OptionAnalysisModel optionAnalysisModel,
			final IScenarioEditingLocation scenarioEditingLocation, final CompoundCommand cmd) {

		if (cargo != null) {
			if (cargo.getCargoType() == CargoType.FLEET) {
				final VesselAssignmentType vat = cargo.getVesselAssignmentType();
				if (vat instanceof VesselAvailability) {
					final VesselAvailability vesselAvailability = (VesselAvailability) vat;

					final FleetShippingOption opt = AnalyticsFactory.eINSTANCE.createFleetShippingOption();
					opt.setEntity(vesselAvailability.getEntity());
					opt.setHireCost(vesselAvailability.getTimeCharterRate());
					opt.setVessel(vesselAvailability.getVessel());

					return opt;
				} else if (vat instanceof CharterInMarket) {
					final CharterInMarket charterInMarket = (CharterInMarket) vat;

					final RoundTripShippingOption opt = AnalyticsFactory.eINSTANCE.createRoundTripShippingOption();
					opt.setHireCost(charterInMarket.getCharterInRate());
					opt.setVesselClass(charterInMarket.getVesselClass());

					return opt;
				}

			}
		} else if (load != null) {
			if (load.isDESPurchase()) {
				if (load.getNominatedVessel() != null) {
					final NominatedShippingOption opt = AnalyticsFactory.eINSTANCE.createNominatedShippingOption();
					opt.setNominatedVessel(load.getNominatedVessel());

					return opt;
				}
			}
		} else if (dischargeSlot != null) {
			if (dischargeSlot.isFOBSale()) {
				if (dischargeSlot.getNominatedVessel() != null) {
					final NominatedShippingOption opt = AnalyticsFactory.eINSTANCE.createNominatedShippingOption();
					opt.setNominatedVessel(dischargeSlot.getNominatedVessel());

					return opt;
				}
			}
		}
		return null;
	}

	public static SellOption getOrCreateSellOption(final DischargeSlot dischargeSlot, final OptionAnalysisModel optionAnalysisModel, final IScenarioEditingLocation scenarioEditingLocation,
			final CompoundCommand cmd) {
		SellReference sellRef = null;

		if (dischargeSlot != null) {
			for (final SellOption sell : optionAnalysisModel.getSells()) {
				if (sell instanceof SellReference) {
					final SellReference sellReference = (SellReference) sell;
					if (sellReference.getSlot() == dischargeSlot) {
						sellRef = sellReference;
						break;
					}
				}
			}
			if (sellRef == null) {
				sellRef = AnalyticsFactory.eINSTANCE.createSellReference();
				sellRef.setSlot(dischargeSlot);
				cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SELLS, sellRef));
			}
		}
		return sellRef;
	}
}

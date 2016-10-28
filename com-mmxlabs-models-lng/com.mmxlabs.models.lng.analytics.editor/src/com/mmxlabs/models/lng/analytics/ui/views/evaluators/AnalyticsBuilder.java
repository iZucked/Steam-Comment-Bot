package com.mmxlabs.models.lng.analytics.ui.views.evaluators;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.StructuredSelection;

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
import com.mmxlabs.models.lng.analytics.ui.views.formatters.BuyOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.SellOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.ShippingOptionDescriptionFormatter;
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
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.util.TravelTimeUtils;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.rcp.common.menus.LocalMenuHelper;

public class AnalyticsBuilder {
	private static BuyOptionDescriptionFormatter buyOptionDescriptionFormatter = new BuyOptionDescriptionFormatter();
	private static SellOptionDescriptionFormatter sellOptionDescriptionFormatter = new SellOptionDescriptionFormatter();

	public static @Nullable LoadSlot makeLoadSlot(final @Nullable BuyOption buy, final @NonNull LNGScenarioModel lngScenarioModel) {

		final String baseName = buyOptionDescriptionFormatter.render(buy);

		// Get existing names
		final Set<String> usedIDStrings = new HashSet<>();
		for (final LoadSlot lSlot : lngScenarioModel.getCargoModel().getLoadSlots()) {
			usedIDStrings.add(lSlot.getName());
		}

		final String id = getUniqueID(baseName, usedIDStrings);

		if (buy instanceof BuyReference) {
			return ((BuyReference) buy).getSlot();
		} else if (buy instanceof BuyOpportunity) {
			final BuyOpportunity buyOpportunity = (BuyOpportunity) buy;
			final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
			slot.setOptional(true);
			slot.setName(id);
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
			if (buyOpportunity.getCancellationExpression() != null && !buyOpportunity.getCancellationExpression().isEmpty()) {
				slot.setCancellationExpression(buyOpportunity.getCancellationExpression());
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

			slot.setName(id);

			return slot;
		}

		return null;
	}

	public static String getUniqueID(final String baseName, final Set<String> usedIDStrings) {
		String id;
		if (usedIDStrings.contains(baseName)) {
			int i = 1;
			id = baseName + "-" + (i);
			while (usedIDStrings.contains(id)) {
				id = baseName + "-" + (i++);
			}
		} else {
			id = baseName;
		}
		return id;
	}

	public static String createIncrementedName(final String base, final int increment) {
		if (increment == 0) {
			return base;
		} else {
			return base + "-" + increment;
		}
	}

	public static @Nullable DischargeSlot makeDischargeSlot(final @Nullable SellOption sell, final @NonNull LNGScenarioModel lngScenarioModel) {
		final String baseName = sellOptionDescriptionFormatter.render(sell);

		// Get existing names
		final Set<String> usedIDStrings = new HashSet<>();
		for (final DischargeSlot dSlot : lngScenarioModel.getCargoModel().getDischargeSlots()) {
			usedIDStrings.add(dSlot.getName());
		}

		final String id = getUniqueID(baseName, usedIDStrings);

		if (sell instanceof SellReference) {
			return ((SellReference) sell).getSlot();
		} else if (sell instanceof SellOpportunity) {
			final SellOpportunity sellOpportunity = (SellOpportunity) sell;
			final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();
			slot.setOptional(true);
			slot.setName(id);
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
			if (sellOpportunity.getCancellationExpression() != null && !sellOpportunity.getCancellationExpression().isEmpty()) {
				slot.setCancellationExpression(sellOpportunity.getCancellationExpression());
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
			slot.setName(id);

			return slot;
		}

		return null;
	}

	public static int calculateTravelHoursForLoad(PortModel portModel, final LoadSlot loadSlot, final DischargeSlot dischargeSlot, final ShippingOption shippingOption) {

		final Port fromPort = loadSlot.getPort();
		final Port toPort = dischargeSlot.getPort();

		VesselClass vesselClass = null;
		if (shippingOption instanceof RoundTripShippingOption) {
			final RoundTripShippingOption roundTripShippingOption = (RoundTripShippingOption) shippingOption;
			vesselClass = roundTripShippingOption.getVesselClass();

		} else if (shippingOption instanceof FleetShippingOption) {
			final FleetShippingOption roundTripShippingOption = (FleetShippingOption) shippingOption;
			final Vessel vessel = roundTripShippingOption.getVessel();
			if (vessel != null) {
				vesselClass = vessel.getVesselClass();
			}
		}
		if (fromPort != null && toPort != null && vesselClass != null) {
			// TODO: Get from input
			final double speed = vesselClass.getMaxSpeed();
			final int travelTime = TravelTimeUtils.getMinTimeFromAllowedRoutes(fromPort, toPort, vesselClass, speed, portModel.getRoutes());
			return travelTime;
		}

		return 0;
	}

	public static int calculateTravelHoursForDischarge(PortModel portModel, final LoadSlot loadSlot, final DischargeSlot dischargeSlot, final ShippingOption shippingOption) {
		final Port fromPort = loadSlot.getPort();
		final Port toPort = dischargeSlot.getPort();

		VesselClass vesselClass = null;
		if (shippingOption instanceof RoundTripShippingOption) {
			final RoundTripShippingOption roundTripShippingOption = (RoundTripShippingOption) shippingOption;
			vesselClass = roundTripShippingOption.getVesselClass();

		} else if (shippingOption instanceof FleetShippingOption) {
			final FleetShippingOption roundTripShippingOption = (FleetShippingOption) shippingOption;
			final Vessel vessel = roundTripShippingOption.getVessel();
			if (vessel != null) {
				vesselClass = vessel.getVesselClass();
			}
		}
		if (fromPort != null && toPort != null && vesselClass != null) {
			// TODO: Get from input
			final double speed = vesselClass.getMaxSpeed();
			final int travelTime = TravelTimeUtils.getMinTimeFromAllowedRoutes(fromPort, toPort, vesselClass, speed, portModel.getRoutes());
			return travelTime;
		}

		return 0;
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

	public static void applyShipping(final IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel optionAnalysisModel, final PartialCaseRow row, final Cargo cargo,
			final LoadSlot load, final DischargeSlot dischargeSlot, final CompoundCommand cmd) {

		final ShippingOption opt = AnalyticsBuilder.getOrCreateShippingOption(cargo, load, dischargeSlot, optionAnalysisModel);
		if (opt.eContainer() == null) {
			cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt));
		}
		cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING, Collections.singletonList(opt)));

	}

	public static void applyShipping(final IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel optionAnalysisModel, final BaseCaseRow row, final Cargo cargo,
			final LoadSlot load, final DischargeSlot dischargeSlot, final CompoundCommand cmd) {

		final ShippingOption opt = AnalyticsBuilder.getOrCreateShippingOption(cargo, load, dischargeSlot, optionAnalysisModel);
		if (opt.eContainer() == null) {
			cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt));
		}
		cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING, opt));

	}

	public static ShippingOption getOrCreateShippingOption(final Cargo cargo, final LoadSlot load, final DischargeSlot dischargeSlot, final OptionAnalysisModel optionAnalysisModel) {

		if (cargo != null) {
			if (cargo.getCargoType() == CargoType.FLEET) {
				final VesselAssignmentType vat = cargo.getVesselAssignmentType();
				if (vat instanceof VesselAvailability) {
					final VesselAvailability vesselAvailability = (VesselAvailability) vat;

					for (final ShippingOption shipOpt : optionAnalysisModel.getShippingTemplates()) {
						if (shipOpt instanceof FleetShippingOption) {
							final FleetShippingOption opt = (FleetShippingOption) shipOpt;
							if (opt.getVessel() != vesselAvailability.getVessel()) {
								continue;
							}
							if (opt.getEntity() != vesselAvailability.getEntity()) {
								continue;
							}
							if (!opt.getHireCost().equals(vesselAvailability.getTimeCharterRate())) {
								continue;
							}

							// TODO: Check other fields

							return opt;
						}
					}

					final FleetShippingOption opt = AnalyticsFactory.eINSTANCE.createFleetShippingOption();
					opt.setEntity(vesselAvailability.getEntity());
					opt.setHireCost(vesselAvailability.getTimeCharterRate());
					opt.setVessel(vesselAvailability.getVessel());

					return opt;
				} else if (vat instanceof CharterInMarket) {
					final CharterInMarket charterInMarket = (CharterInMarket) vat;

					for (final ShippingOption shipOpt : optionAnalysisModel.getShippingTemplates()) {
						if (shipOpt instanceof RoundTripShippingOption) {
							final RoundTripShippingOption opt = (RoundTripShippingOption) shipOpt;
							if (opt.getVesselClass() != charterInMarket.getVesselClass()) {
								continue;
							}

							if (!opt.getHireCost().equals(charterInMarket.getCharterInRate())) {
								continue;
							}

							// TODO: Check other fields

							return opt;
						}
					}

					final RoundTripShippingOption opt = AnalyticsFactory.eINSTANCE.createRoundTripShippingOption();
					opt.setHireCost(charterInMarket.getCharterInRate());
					opt.setVesselClass(charterInMarket.getVesselClass());

					return opt;
				}

			}
		} else if (load != null) {
			if (load.isDESPurchase()) {
				if (load.getNominatedVessel() != null) {

					for (final ShippingOption shipOpt : optionAnalysisModel.getShippingTemplates()) {
						if (shipOpt instanceof NominatedShippingOption) {
							final NominatedShippingOption opt = (NominatedShippingOption) shipOpt;
							if (opt.getNominatedVessel() != load.getNominatedVessel()) {
								continue;
							}

							return opt;
						}
					}

					final NominatedShippingOption opt = AnalyticsFactory.eINSTANCE.createNominatedShippingOption();
					opt.setNominatedVessel(load.getNominatedVessel());

					return opt;
				}
			}
		} else if (dischargeSlot != null) {
			if (dischargeSlot.isFOBSale()) {
				if (dischargeSlot.getNominatedVessel() != null) {

					for (final ShippingOption shipOpt : optionAnalysisModel.getShippingTemplates()) {
						if (shipOpt instanceof NominatedShippingOption) {
							final NominatedShippingOption opt = (NominatedShippingOption) shipOpt;
							if (opt.getNominatedVessel() != dischargeSlot.getNominatedVessel()) {
								continue;
							}

							return opt;
						}
					}

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

	public static NominatedShippingOption getOrCreateNominatedShippingOption(final OptionAnalysisModel optionAnalysisModel, final Vessel vessel) {
		for (final ShippingOption opt : optionAnalysisModel.getShippingTemplates()) {
			if (opt instanceof NominatedShippingOption) {
				final NominatedShippingOption nominatedShippingOption = (NominatedShippingOption) opt;
				if (nominatedShippingOption.getNominatedVessel() == vessel) {
					return nominatedShippingOption;
				}
			}
		}
		final NominatedShippingOption opt = AnalyticsFactory.eINSTANCE.createNominatedShippingOption();
		opt.setNominatedVessel(vessel);

		return opt;
	}

	public static void applyShipping(final IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel optionAnalysisModel, final BaseCaseRow row, final Vessel vessel) {
		if (row == null) {
			return;
		}
		if (AnalyticsBuilder.isNonShipped(row) == ShippingType.NonShipped) {
			applyShipping(scenarioEditingLocation, optionAnalysisModel, row, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING, vessel);
		}
	}

	public static void applyShipping(final IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel optionAnalysisModel, final PartialCaseRow row, final Vessel vessel,
			final LocalMenuHelper menuHelper) {
		if (row == null) {
			return;
		}
		if (AnalyticsBuilder.isNonShipped(row) == ShippingType.NonShipped) {
			applyShipping(scenarioEditingLocation, optionAnalysisModel, row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING, vessel);
		}
	}

	private static void applyShipping(final IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel optionAnalysisModel, final EObject row, final EStructuralFeature feature,
			final Vessel vessel) {
		final NominatedShippingOption opt = AnalyticsBuilder.getOrCreateNominatedShippingOption(optionAnalysisModel, vessel);
		final CompoundCommand cmd = new CompoundCommand();
		if (opt.eContainer() == null) {
			cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt));
		}
		final Object target = feature.isMany() ? Collections.singletonList(opt) : opt;

		cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, feature, target));
		scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, optionAnalysisModel, null);
	}

	public static void applyShipping(final IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel optionAnalysisModel, final BaseCaseRow row, final VesselClass vesselClass,
			final LocalMenuHelper menuHelper) {
		if (row == null) {
			return;
		}
		if (AnalyticsBuilder.isNonShipped(row) == ShippingType.Shipped) {
			applyShipping(scenarioEditingLocation, optionAnalysisModel, row, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING, vesselClass, menuHelper);
		}
	}

	public static void applyShipping(final IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel optionAnalysisModel, final PartialCaseRow row, final VesselClass vesselClass,
			final LocalMenuHelper menuHelper) {
		if (row == null) {
			return;
		}
		if (AnalyticsBuilder.isNonShipped(row) == ShippingType.Shipped) {
			applyShipping(scenarioEditingLocation, optionAnalysisModel, row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING, vesselClass, menuHelper);
		}
	}

	private static void applyShipping(final IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel optionAnalysisModel, final EObject row, final EStructuralFeature feature,
			final VesselClass vesselClass, final LocalMenuHelper menuHelper) {

		menuHelper.clearActions();
		final ShippingOptionDescriptionFormatter f = new ShippingOptionDescriptionFormatter();
		for (final ShippingOption option : optionAnalysisModel.getShippingTemplates()) {
			if (option instanceof RoundTripShippingOption) {
				final RoundTripShippingOption roundTripShippingOption = (RoundTripShippingOption) option;
				if (roundTripShippingOption.getVesselClass() == vesselClass) {
					menuHelper.addAction(new RunnableAction(f.render(option), () -> {
						final CompoundCommand cmd = new CompoundCommand();
						final Object target = feature.isMany() ? Collections.singletonList(option) : option;
						cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, feature, target));

						scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, optionAnalysisModel, null);

					}));
				}
			}
		}
		menuHelper.addAction(new RunnableAction("New", () -> {
			final RoundTripShippingOption opt = AnalyticsFactory.eINSTANCE.createRoundTripShippingOption();
			opt.setVesselClass(vesselClass);

			final CompoundCommand cmd = new CompoundCommand();
			cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt));
			final Object target = feature.isMany() ? Collections.singletonList(opt) : opt;
			cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, feature, target));
			scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, optionAnalysisModel, null);

			DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, new StructuredSelection(opt));
		}));

		menuHelper.open();
	}

	public static Port getPort(final SellOption option) {
		if (option instanceof SellOpportunity) {
			final SellOpportunity sellOpportunity = (SellOpportunity) option;
			return sellOpportunity.getPort();
		} else if (option instanceof SellReference) {
			final SellReference sellReference = (SellReference) option;
			final DischargeSlot slot = sellReference.getSlot();
			if (slot != null) {
				return slot.getPort();
			}
		}
		return null;
	}

	public static LocalDate getDate(final SellOption option) {
		if (option instanceof SellOpportunity) {
			final SellOpportunity sellOpportunity = (SellOpportunity) option;
			return sellOpportunity.getDate();
		} else if (option instanceof SellReference) {
			final SellReference sellReference = (SellReference) option;
			final DischargeSlot slot = sellReference.getSlot();
			if (slot != null) {
				return slot.getWindowStart();
			}
		}
		return null;
	}

	public static boolean isShipped(final BuyOption option) {
		if (option instanceof BuyOpportunity) {
			final BuyOpportunity buyOpportunity = (BuyOpportunity) option;
			return !buyOpportunity.isDesPurchase();
		} else if (option instanceof BuyReference) {
			final BuyReference buyReference = (BuyReference) option;
			final LoadSlot slot = buyReference.getSlot();
			if (slot != null) {
				return !slot.isDESPurchase();
			}
		}
		return true;
	}

	public static Port getPort(final BuyOption option) {
		if (option instanceof BuyOpportunity) {
			final BuyOpportunity buyOpportunity = (BuyOpportunity) option;
			return buyOpportunity.getPort();
		} else if (option instanceof BuyReference) {
			final BuyReference buyReference = (BuyReference) option;
			final LoadSlot slot = buyReference.getSlot();
			if (slot != null) {
				return slot.getPort();
			}
		}
		return null;
	}

	public static LocalDate getDate(final BuyOption option) {
		if (option instanceof BuyOpportunity) {
			final BuyOpportunity buyOpportunity = (BuyOpportunity) option;
			return buyOpportunity.getDate();
		} else if (option instanceof BuyReference) {
			final BuyReference buyReference = (BuyReference) option;
			final LoadSlot slot = buyReference.getSlot();
			if (slot != null) {
				return slot.getWindowStart();
			}
		}
		return null;
	}

	public static ZonedDateTime getWindowStartDate(final BuyOption option) {
		if (option instanceof BuyOpportunity) {
			final BuyOpportunity buyOpportunity = (BuyOpportunity) option;
			final Port port = buyOpportunity.getPort();
			if (port != null) {
				return buyOpportunity.getDate().atStartOfDay(ZoneId.of(port.getTimeZone()));
			}
		} else if (option instanceof BuyReference) {
			final BuyReference buyReference = (BuyReference) option;
			final LoadSlot slot = buyReference.getSlot();
			if (slot != null) {
				return slot.getWindowStartWithSlotOrPortTime();
			}
		}
		return null;
	}

	public static ZonedDateTime getWindowStartDate(final SellOption option) {
		if (option instanceof SellOpportunity) {
			final SellOpportunity sellOpportunity = (SellOpportunity) option;
			final Port port = sellOpportunity.getPort();
			if (port != null) {
				return sellOpportunity.getDate().atStartOfDay(ZoneId.of(port.getTimeZone()));
			}
		} else if (option instanceof SellReference) {
			final SellReference sellReference = (SellReference) option;
			final DischargeSlot slot = sellReference.getSlot();
			if (slot != null) {
				return slot.getWindowStartWithSlotOrPortTime();
			}
		}
		return null;
	}

	public static ZonedDateTime getWindowEndDate(final BuyOption option) {
		if (option instanceof BuyOpportunity) {
			final BuyOpportunity buyOpportunity = (BuyOpportunity) option;
			final Port port = buyOpportunity.getPort();
			if (port != null) {
				ZonedDateTime t = buyOpportunity.getDate().atStartOfDay(ZoneId.of(port.getTimeZone()));
				if (port.getDefaultWindowSizeUnits() == TimePeriod.HOURS) {
					t = t.plusHours(port.getDefaultWindowSize());
				} else if (port.getDefaultWindowSizeUnits() == TimePeriod.DAYS) {
					t = t.plusDays(port.getDefaultWindowSize());
				} else if (port.getDefaultWindowSizeUnits() == TimePeriod.MONTHS) {
					t = t.plusMonths(port.getDefaultWindowSize());
				} else {
					return null;
				}
				return t;
			}
		} else if (option instanceof BuyReference) {
			final BuyReference buyReference = (BuyReference) option;
			final LoadSlot slot = buyReference.getSlot();
			if (slot != null) {
				return slot.getWindowEndWithSlotOrPortTime();
			}
		}
		return null;
	}

	public static ZonedDateTime getWindowEndDate(final SellOption option) {
		if (option instanceof SellOpportunity) {
			final SellOpportunity sellOpportunity = (SellOpportunity) option;
			final Port port = sellOpportunity.getPort();
			if (port != null) {
				ZonedDateTime t = sellOpportunity.getDate().atStartOfDay(ZoneId.of(port.getTimeZone()));
				if (port.getDefaultWindowSizeUnits() == TimePeriod.HOURS) {
					t = t.plusHours(port.getDefaultWindowSize());
				} else if (port.getDefaultWindowSizeUnits() == TimePeriod.DAYS) {
					t = t.plusDays(port.getDefaultWindowSize());
				} else if (port.getDefaultWindowSizeUnits() == TimePeriod.MONTHS) {
					t = t.plusMonths(port.getDefaultWindowSize());
				} else {
					return null;
				}
				return t;
			}
		} else if (option instanceof SellReference) {
			final SellReference sellReference = (SellReference) option;
			final DischargeSlot slot = sellReference.getSlot();
			if (slot != null) {
				return slot.getWindowEndWithSlotOrPortTime();
			}
		}
		return null;
	}

	public static boolean isShipped(final SellOption option) {
		if (option instanceof SellOpportunity) {
			final SellOpportunity sellOpportunity = (SellOpportunity) option;
			return !sellOpportunity.isFobSale();
		} else if (option instanceof SellReference) {
			final SellReference sellReference = (SellReference) option;
			final DischargeSlot slot = sellReference.getSlot();
			if (slot != null) {
				return !slot.isFOBSale();
			}
		}
		return true;
	}

	public static int getDuration(SellOption option) {
		if (option instanceof SellOpportunity) {
			final SellOpportunity sellOpportunity = (SellOpportunity) option;
			final Port port = sellOpportunity.getPort();
			if (port != null) {
				return port.getDischargeDuration();
			}
		} else if (option instanceof SellReference) {

			final SellReference sellReference = (SellReference) option;
			final DischargeSlot slot = sellReference.getSlot();
			if (slot != null) {
				return slot.getSlotOrPortDuration();
			}
		} else if (option instanceof SellMarket) {

			final SellMarket sellMarket = (SellMarket) option;
			final SpotMarket market = sellMarket.getMarket();
			if (market instanceof DESSalesMarket) {
				DESSalesMarket desSalesMarket = (DESSalesMarket) market;
				Port port = desSalesMarket.getNotionalPort();
				if (port != null) {
					return port.getDischargeDuration();
				}
			}
		}
		return 0;
	}

	public static int getDuration(BuyOption option) {
		if (option instanceof BuyOpportunity) {
			final BuyOpportunity buyOpportunity = (BuyOpportunity) option;
			final Port port = buyOpportunity.getPort();
			if (port != null) {
				return port.getDischargeDuration();
			}
		} else if (option instanceof BuyReference) {
			final BuyReference buyReference = (BuyReference) option;
			final LoadSlot slot = buyReference.getSlot();
			if (slot != null) {
				return slot.getSlotOrPortDuration();
			}
		} else if (option instanceof BuyMarket) {

			final BuyMarket buylMarket = (BuyMarket) option;
			final SpotMarket market = buylMarket.getMarket();
			if (market instanceof FOBPurchasesMarket) {
				FOBPurchasesMarket fobPurchasesMarket = (FOBPurchasesMarket) market;
				Port port = fobPurchasesMarket.getNotionalPort();
				if (port != null) {
					return port.getLoadDuration();
				}
			}
		}
		return 0;
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.sandbox;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.StructuredSelection;

import com.google.common.base.Objects;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.analytics.AbstractAnalysisModel;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.CharterOutOpportunity;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.FullVesselCharterOption;
import com.mmxlabs.models.lng.analytics.NominatedShippingOption;
import com.mmxlabs.models.lng.analytics.OpenBuy;
import com.mmxlabs.models.lng.analytics.OpenSell;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.OptionalSimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.RoundTripShippingOption;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption;
import com.mmxlabs.models.lng.analytics.VesselEventOption;
import com.mmxlabs.models.lng.analytics.VesselEventReference;
import com.mmxlabs.models.lng.analytics.VolumeMode;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.BuyOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.SellOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.ShippingOptionDescriptionFormatter;
import com.mmxlabs.models.lng.analytics.ui.views.formatters.VesselEventOptionDescriptionFormatter;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.CargoTravelTimeUtils;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.EVesselTankState;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.IBallastBonus;
import com.mmxlabs.models.lng.commercial.IRepositioningFee;
import com.mmxlabs.models.lng.commercial.LumpSumBallastBonusTerm;
import com.mmxlabs.models.lng.commercial.LumpSumRepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.SimpleBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.SimpleRepositioningFeeContainer;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.util.TravelTimeUtils;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.rcp.common.menus.LocalMenuHelper;

public class AnalyticsBuilder {
	private static VesselEventOptionDescriptionFormatter vesselEventOptionDescriptionFormatter = new VesselEventOptionDescriptionFormatter();
	private static BuyOptionDescriptionFormatter buyOptionDescriptionFormatter = new BuyOptionDescriptionFormatter();
	private static SellOptionDescriptionFormatter sellOptionDescriptionFormatter = new SellOptionDescriptionFormatter();
	private static final boolean DISABLE_FLEET = true;
	private static final double DEFAULT_MAX_VOLUME = 140_000;

	public static @Nullable LoadSlot makeLoadSlot(final @Nullable BuyOption buy, final @NonNull LNGScenarioModel lngScenarioModel) {
		return makeLoadSlot(buy, lngScenarioModel, SlotMode.ORIGINAL_SLOT, null);
	}

	public static @Nullable LoadSlot makeLoadSlot(final @Nullable BuyOption buy, final @NonNull LNGScenarioModel lngScenarioModel, final SlotMode slotMode, Set<String> usedIDStrings) {

		final String baseName = buyOptionDescriptionFormatter.render(buy);

		// Get existing names
		if (usedIDStrings == null) {
			usedIDStrings = new HashSet<>();
			for (final LoadSlot lSlot : lngScenarioModel.getCargoModel().getLoadSlots()) {
				usedIDStrings.add(lSlot.getName());
			}
		}

		final String id = getUniqueID(baseName, usedIDStrings);
		usedIDStrings.add(id);

		if (buy instanceof BuyReference) {
			final LoadSlot originalLoadSlot = ((BuyReference) buy).getSlot();
			if (slotMode != SlotMode.ORIGINAL_SLOT) {
				// TODO: Copy other params!
				final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
				slot.setOptional(true);
				slot.setName(id);
				slot.setPort(originalLoadSlot.getPort());

				slot.setArriveCold(originalLoadSlot.isArriveCold());
				slot.setDESPurchase(originalLoadSlot.isDESPurchase());
				slot.setDesPurchaseDealType(originalLoadSlot.getSlotOrDelegateDESPurchaseDealType());
				slot.setCounterparty(originalLoadSlot.getCounterparty());
				slot.setDuration(originalLoadSlot.getDuration());
				slot.setWindowSize(originalLoadSlot.getWindowSize());
				slot.setWindowSizeUnits(originalLoadSlot.getWindowSizeUnits());
				slot.setWindowCounterParty(originalLoadSlot.isWindowCounterParty());

				if (originalLoadSlot.getSlotOrDelegateCV() != 0.0) {
					slot.setCargoCV(originalLoadSlot.getSlotOrDelegateCV());
				}
				if (slotMode == SlotMode.CHANGE_PRICE_VARIANT) {
					slot.setPriceExpression("??");
				} else if (slotMode == SlotMode.BREAK_EVEN_VARIANT) {
					slot.setPriceExpression("?");
				}
				slot.setWindowStart(originalLoadSlot.getWindowStart());

				slot.setEntity(originalLoadSlot.getSlotOrDelegateEntity());

				if (originalLoadSlot.getCancellationExpression() != null && !originalLoadSlot.getCancellationExpression().isEmpty()) {
					slot.setCancellationExpression(originalLoadSlot.getCancellationExpression());
				}
				slot.setMiscCosts(originalLoadSlot.getMiscCosts());

				slot.setVolumeLimitsUnit(originalLoadSlot.getSlotOrDelegateVolumeLimitsUnit());
				slot.setMinQuantity(originalLoadSlot.getSlotOrDelegateMinQuantity());
				slot.setMaxQuantity(originalLoadSlot.getSlotOrDelegateMaxQuantity());

				return slot;
			}

			return originalLoadSlot;
		} else if (buy instanceof BuyOpportunity) {
			final BuyOpportunity buyOpportunity = (BuyOpportunity) buy;
			final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
			slot.setOptional(true);
			slot.setName(id);
			slot.setPort(buyOpportunity.getPort());
			if (buyOpportunity.isDesPurchase()) {
				slot.setDESPurchase(true);
			}
			if (buyOpportunity.getCv() != 0.0) {
				slot.setCargoCV(buyOpportunity.getCv());
			}
			if (buyOpportunity.getContract() != null) {
				slot.setContract(buyOpportunity.getContract());
			}
			if (slotMode == SlotMode.CHANGE_PRICE_VARIANT) {
				slot.setPriceExpression("??");
			} else if (slotMode == SlotMode.BREAK_EVEN_VARIANT) {
				slot.setPriceExpression("?");
			} else if (buyOpportunity.getPriceExpression() != null && !buyOpportunity.getPriceExpression().equals("")) {
				slot.setPriceExpression(buyOpportunity.getPriceExpression().contains("?") ? "?" : buyOpportunity.getPriceExpression());
			}
			if (buyOpportunity.getDate() != null) {
				slot.setWindowStart(buyOpportunity.getDate());
			}

			if (buyOpportunity.isSpecifyWindow()) {
				slot.setWindowSize(buyOpportunity.getWindowSize());
				slot.setWindowSizeUnits(buyOpportunity.getWindowSizeUnits());
			}

			if (buyOpportunity.getEntity() != null) {
				slot.setEntity(buyOpportunity.getEntity());
			}
			if (buyOpportunity.getCancellationExpression() != null && !buyOpportunity.getCancellationExpression().isEmpty()) {
				slot.setCancellationExpression(buyOpportunity.getCancellationExpression());
			}
			slot.setMiscCosts(buyOpportunity.getMiscCosts());

			if (buyOpportunity.getVolumeMode() == VolumeMode.FIXED) {
				slot.setVolumeLimitsUnit(buyOpportunity.getVolumeUnits());
				slot.setMinQuantity(buyOpportunity.getMaxVolume());
				slot.setMaxQuantity(buyOpportunity.getMaxVolume());
			} else if (buyOpportunity.getVolumeMode() == VolumeMode.RANGE) {
				slot.setVolumeLimitsUnit(buyOpportunity.getVolumeUnits());
				slot.setMinQuantity(buyOpportunity.getMinVolume());
				slot.setMaxQuantity(buyOpportunity.getMaxVolume());
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

			if (slotMode == SlotMode.CHANGE_PRICE_VARIANT) {
				slot.setPriceExpression("??");
			} else if (slotMode == SlotMode.BREAK_EVEN_VARIANT) {
				slot.setPriceExpression("?");
			}

			return slot;
		}

		return null;
	}

	public static int getVesselCapacityInM3(final ShippingOption shippingOption) {
		if (shippingOption instanceof SimpleVesselCharterOption) {
			return ((SimpleVesselCharterOption) shippingOption).getVessel().getVesselOrDelegateCapacity();
		} else if (shippingOption instanceof RoundTripShippingOption) {
			return ((RoundTripShippingOption) shippingOption).getVessel().getVesselOrDelegateCapacity();
		} else if (shippingOption instanceof NominatedShippingOption) {
			return ((NominatedShippingOption) shippingOption).getNominatedVessel().getVesselOrDelegateCapacity();
		} else if (shippingOption instanceof ExistingCharterMarketOption) {
			return ((ExistingCharterMarketOption) shippingOption).getCharterInMarket().getVessel().getVesselOrDelegateCapacity();
		} else if (shippingOption instanceof ExistingVesselCharterOption) {
			return ((ExistingVesselCharterOption) shippingOption).getVesselCharter().getVessel().getVesselOrDelegateCapacity();
		} else if (shippingOption instanceof FullVesselCharterOption) {
			return ((FullVesselCharterOption) shippingOption).getVesselCharter().getVessel().getVesselOrDelegateCapacity();
		}
		return 0;
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
		return makeDischargeSlot(sell, lngScenarioModel, SlotMode.ORIGINAL_SLOT, null);
	}

	public static @Nullable DischargeSlot makeDischargeSlot(final @Nullable SellOption sell, final @NonNull LNGScenarioModel lngScenarioModel, final SlotMode slotMode, Set<String> usedIDStrings) {
		final String baseName = sellOptionDescriptionFormatter.render(sell);

		// Get existing names
		if (usedIDStrings == null) {
			usedIDStrings = new HashSet<>();
			for (final LoadSlot lSlot : lngScenarioModel.getCargoModel().getLoadSlots()) {
				usedIDStrings.add(lSlot.getName());
			}
		}

		final String id = getUniqueID(baseName, usedIDStrings);
		usedIDStrings.add(id);

		if (sell instanceof SellReference) {

			final DischargeSlot originalDischargeSlot = ((SellReference) sell).getSlot();
			if (slotMode != SlotMode.ORIGINAL_SLOT) {

				final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();
				slot.setOptional(true);
				slot.setName(id);
				slot.setPort(originalDischargeSlot.getPort());

				slot.setFOBSale(originalDischargeSlot.isFOBSale());
				slot.setFobSaleDealType(originalDischargeSlot.getSlotOrDelegateFOBSaleDealType());
				slot.setCounterparty(originalDischargeSlot.getSlotOrDelegateCounterparty());
				slot.setDuration(originalDischargeSlot.getDuration());
				slot.setWindowSize(originalDischargeSlot.getWindowSize());
				slot.setWindowSizeUnits(originalDischargeSlot.getWindowSizeUnits());
				slot.setWindowCounterParty(originalDischargeSlot.isWindowCounterParty());

				// TODO: Copy other params!
				if (slotMode == SlotMode.CHANGE_PRICE_VARIANT) {
					slot.setPriceExpression("??");
				} else if (slotMode == SlotMode.BREAK_EVEN_VARIANT) {
					slot.setPriceExpression("?");
				}
				slot.setWindowStart(originalDischargeSlot.getWindowStart());

				slot.setEntity(originalDischargeSlot.getSlotOrDelegateEntity());

				if (originalDischargeSlot.getCancellationExpression() != null && !originalDischargeSlot.getCancellationExpression().isEmpty()) {
					slot.setCancellationExpression(originalDischargeSlot.getCancellationExpression());
				}
				slot.setMiscCosts(originalDischargeSlot.getMiscCosts());

				slot.setVolumeLimitsUnit(originalDischargeSlot.getSlotOrDelegateVolumeLimitsUnit());
				slot.setMinQuantity(originalDischargeSlot.getSlotOrDelegateMinQuantity());
				slot.setMaxQuantity(originalDischargeSlot.getSlotOrDelegateMaxQuantity());

				return slot;
			}

			return originalDischargeSlot;
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
			if (sellOpportunity.getPriceExpression() != null && !sellOpportunity.getPriceExpression().equals("")) {
				slot.setPriceExpression(sellOpportunity.getPriceExpression().contains("?") ? "?" : sellOpportunity.getPriceExpression());
			}
			if (sellOpportunity.getDate() != null) {
				slot.setWindowStart(sellOpportunity.getDate());
			}

			if (sellOpportunity.isSpecifyWindow()) {
				slot.setWindowSize(sellOpportunity.getWindowSize());
				slot.setWindowSizeUnits(sellOpportunity.getWindowSizeUnits());
			}

			if (sellOpportunity.getEntity() != null) {
				slot.setEntity(sellOpportunity.getEntity());
			}

			if (slotMode == SlotMode.CHANGE_PRICE_VARIANT) {
				slot.setPriceExpression("??");
			} else if (slotMode == SlotMode.BREAK_EVEN_VARIANT) {
				slot.setPriceExpression("?");
			} else if (sellOpportunity.getCancellationExpression() != null && !sellOpportunity.getCancellationExpression().isEmpty()) {
				slot.setCancellationExpression(sellOpportunity.getCancellationExpression());
			}
			slot.setMiscCosts(sellOpportunity.getMiscCosts());

			if (sellOpportunity.getVolumeMode() == VolumeMode.FIXED) {
				slot.setVolumeLimitsUnit(sellOpportunity.getVolumeUnits());
				slot.setMinQuantity(sellOpportunity.getMaxVolume());
				slot.setMaxQuantity(sellOpportunity.getMaxVolume());
			} else if (sellOpportunity.getVolumeMode() == VolumeMode.RANGE) {
				slot.setVolumeLimitsUnit(sellOpportunity.getVolumeUnits());
				slot.setMinQuantity(sellOpportunity.getMinVolume());
				slot.setMaxQuantity(sellOpportunity.getMaxVolume());
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

			if (slotMode == SlotMode.CHANGE_PRICE_VARIANT) {
				slot.setPriceExpression("??");
			} else if (slotMode == SlotMode.BREAK_EVEN_VARIANT) {
				slot.setPriceExpression("?");
			}
			return slot;
		}

		return null;
	}

	public static int calculateTravelHoursForLoad(final PortModel portModel, final LoadSlot loadSlot, final DischargeSlot dischargeSlot, final ShippingOption shippingOption,
			final ModelDistanceProvider modelDistanceProvider) {

		final Port fromPort = loadSlot.getPort();
		final Port toPort = dischargeSlot.getPort();

		final Vessel vessel = getVessel(shippingOption);

		if (fromPort != null && toPort != null && vessel != null) {
			// TODO: Get from input
			final double speed = vessel.getVesselOrDelegateMaxSpeed();
			final int travelTime = CargoTravelTimeUtils.getMinTimeFromAllowedRoutes(loadSlot, dischargeSlot, vessel, speed, portModel.getRoutes(), modelDistanceProvider);
			return travelTime;
		}

		return 0;
	}

	public static int calculateTravelHoursForDischarge(final PortModel portModel, final LoadSlot loadSlot, final DischargeSlot dischargeSlot, final ShippingOption shippingOption,
			final ModelDistanceProvider modelDistanceProvider) {
		final Port fromPort = loadSlot.getPort();
		final Port toPort = dischargeSlot.getPort();

		final Vessel vessel = getVessel(shippingOption);

		if (fromPort != null && toPort != null && vessel != null) {
			// TODO: Get from input
			final double speed = vessel.getVesselOrDelegateMaxSpeed();
			final int travelTime = CargoTravelTimeUtils.getMinTimeFromAllowedRoutes(loadSlot, dischargeSlot, vessel, speed, portModel.getRoutes(), modelDistanceProvider);
			return travelTime;
		}

		return 0;
	}

	public static int calculateLateness(final @Nullable BuyOption buyOption, final @Nullable SellOption sellOption, final PortModel portModel, final @Nullable Vessel vessel,
			final ModelDistanceProvider modelDistanceProvider) {
		if (buyOption == null || sellOption == null) {
			return 0;
		}
		final Port fromPort = AnalyticsBuilder.getPort(buyOption);
		final Port toPort = AnalyticsBuilder.getPort(sellOption);
		if (fromPort != null && toPort != null && vessel != null) {

			final ZonedDateTime windowStartDate = AnalyticsBuilder.getWindowStartDate(buyOption);
			final ZonedDateTime windowEndDate = AnalyticsBuilder.getWindowEndDate(sellOption);

			final double speed = vessel.getVesselOrDelegateMaxSpeed();

			final int travelTime = TravelTimeUtils.getMinTimeFromAllowedRoutes(fromPort, toPort, vessel, speed, portModel.getRoutes(), modelDistanceProvider);

			if (windowStartDate != null && windowEndDate != null) {
				final int optionDuration = AnalyticsBuilder.getDuration(buyOption);
				final int availableTime = Hours.between(windowStartDate, windowEndDate) - optionDuration;
				return availableTime - travelTime;
			}
		}
		return 0;
	}

	public static int calculateVoyageDurationInHours(final @Nullable BuyOption buyOption, final @Nullable SellOption sellOption, final PortModel portModel, final @Nullable Vessel vessel,
			final ModelDistanceProvider modelDistanceProvider) {
		if (buyOption == null || sellOption == null) {
			return 0;
		}
		final Port fromPort = AnalyticsBuilder.getPort(buyOption);
		final Port toPort = AnalyticsBuilder.getPort(sellOption);
		if (fromPort != null && toPort != null && vessel != null) {

			final ZonedDateTime windowStartDate = AnalyticsBuilder.getWindowStartDate(buyOption);
			final ZonedDateTime windowEndDate = AnalyticsBuilder.getWindowEndDate(sellOption);

			if (windowStartDate != null && windowEndDate != null) {
				final int loadDuration = AnalyticsBuilder.getDuration(buyOption);
				final int availableTime = Hours.between(windowStartDate, windowEndDate) - loadDuration;
				return availableTime;
			}
		}
		return 0;
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
		final SellOption sell = row.getSellOption();
		return getShippingType(buy, sell);
	}

	public static ShippingType getShippingType(final BuyOption buy, final SellOption sell) {

		final ShippingType buyType = getBuyShippingType(buy);

		if (buyType == ShippingType.Mixed) {
			return ShippingType.Mixed;
		}

		final ShippingType sellType = getSellShippingType(sell);

		if (buyType == ShippingType.None && sellType != ShippingType.None) {
			return ShippingType.Open;
		}
		if (buyType != ShippingType.None && sellType == ShippingType.None) {
			return ShippingType.Open;
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
		if (buyType == ShippingType.None) {
			return sellType;
		}
		if (sellType == ShippingType.None) {
			return buyType;
		}
		return ShippingType.NonShipped;
	}

	public static boolean isShipped(final ShippingOption option) {
		if (option == null) {
			return false;
		} else if (option instanceof NominatedShippingOption) {
			return false;
		} else {
			return true;
		}
	}

	public static ShippingType getBuyShippingType(@Nullable final BuyOption buy) {
		if (buy == null || buy instanceof OpenBuy) {
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
		if (sell == null || sell instanceof OpenSell) {
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

	public static void setDefaultEntity(final IScenarioEditingLocation scenarioEditingLocation, final SimpleVesselCharterOption option) {
		final BaseLegalEntity entity = getDefaultEntity(scenarioEditingLocation);
		if (entity != null) {
			option.setEntity(entity);
		}
	}

	public static void setDefaultEntity(final IScenarioEditingLocation scenarioEditingLocation, final FullVesselCharterOption option) {
		final BaseLegalEntity entity = getDefaultEntity(scenarioEditingLocation);
		if (entity != null) {
			option.getVesselCharter().setEntity(entity);
		}
	}

	public static void setDefaultEntity(final IScenarioEditingLocation scenarioEditingLocation, final OptionalSimpleVesselCharterOption option) {
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

	public static BuyOption getOrCreateBuyOption(final LoadSlot loadSlot, final AbstractAnalysisModel optionAnalysisModel, final IScenarioEditingLocation scenarioEditingLocation,
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
				cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel, AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__BUYS, buyRef));
			}
		}
		return buyRef;
	}

	public static VesselEventOption getOrCreateVesselEventOption(final VesselEvent vesselEvent, final AbstractAnalysisModel optionAnalysisModel, final IScenarioEditingLocation scenarioEditingLocation,
			final CompoundCommand cmd) {

		VesselEventReference veRef = null;

		if (vesselEvent != null) {
			for (final VesselEventOption opt : optionAnalysisModel.getVesselEvents()) {
				if (opt instanceof VesselEventReference) {
					final VesselEventReference reference = (VesselEventReference) opt;
					if (reference.getEvent() == vesselEvent) {
						veRef = reference;
						break;
					}
				} else if (opt instanceof CharterOutOpportunity) {
					final CharterOutOpportunity charterOutOpportunity = (CharterOutOpportunity) opt;

				}
			}
			if (veRef == null) {
				veRef = AnalyticsFactory.eINSTANCE.createVesselEventReference();
				veRef.setEvent(vesselEvent);
				cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel, AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__VESSEL_EVENTS, veRef));
			}
		}
		return veRef;
	}

	public static ShippingOption applyShipping(final IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel optionAnalysisModel, final PartialCaseRow row, final Cargo cargo,
			final LoadSlot load, final DischargeSlot dischargeSlot, final boolean portfolioMode, final CompoundCommand cmd) {

		final ShippingOption opt = AnalyticsBuilder.getOrCreateShippingOption(cargo, load, dischargeSlot, portfolioMode, optionAnalysisModel);
		if (opt.eContainer() == null) {
			cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel, AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt));
		}
		cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING, Collections.singletonList(opt)));
		return opt;
	}

	/*
	 * Execute within a command
	 */
	public static void applyShipping(final IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel optionAnalysisModel, final BaseCaseRow row, final Cargo cargo,
			final LoadSlot load, final DischargeSlot dischargeSlot, final boolean portfolioMode, final CompoundCommand cmd) {

		final ShippingOption opt = AnalyticsBuilder.getOrCreateShippingOption(cargo, load, dischargeSlot, portfolioMode, optionAnalysisModel);
		if (opt != null) {
			if (opt.eContainer() == null) {
				cmd.appendAndExecute(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel, AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt));
			}
			cmd.appendAndExecute(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING, opt));
		}
	}

	public static void applyShipping(final IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel optionAnalysisModel, final BaseCaseRow row, final VesselEvent vesselEvent,
			final boolean portfolioMode, final CompoundCommand cmd) {

		final ShippingOption opt = AnalyticsBuilder.getOrCreateShippingOption(vesselEvent, portfolioMode, optionAnalysisModel);
		if (opt != null) {
			if (opt.eContainer() == null) {
				cmd.appendAndExecute(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel, AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt));
			}
			cmd.appendAndExecute(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.BASE_CASE_ROW__SHIPPING, opt));
		}
	}

	public static ShippingOption applyShipping(final IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel optionAnalysisModel, final PartialCaseRow row,
			final VesselEvent vesselEvent, final boolean portfolioMode, final CompoundCommand cmd) {

		final ShippingOption opt = AnalyticsBuilder.getOrCreateShippingOption(vesselEvent, portfolioMode, optionAnalysisModel);
		if (opt != null) {
			if (opt.eContainer() == null) {
				cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel, AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt));
			}
			cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING, Collections.singletonList(opt)));
		}
		return opt;
	}

	public static ShippingOption getOrCreateShippingOption(final Cargo cargo, final LoadSlot load, final DischargeSlot dischargeSlot, final boolean portfolioMode,
			final AbstractAnalysisModel optionAnalysisModel) {

		if (cargo != null) {
			if (cargo.getCargoType() == CargoType.FLEET) {
				final VesselAssignmentType vat = cargo.getVesselAssignmentType();
				if (vat instanceof VesselAvailability) {
					final VesselAvailability vesselAvailability = (VesselAvailability) vat;
					if (portfolioMode) {
						for (final ShippingOption shipOpt : optionAnalysisModel.getShippingTemplates()) {
							if (shipOpt instanceof ExistingVesselCharterOption) {
								final ExistingVesselCharterOption opt = (ExistingVesselCharterOption) shipOpt;
								if (opt.getVesselCharter() == vesselAvailability) {
									return opt;
								}

							}
						}
						final ExistingVesselCharterOption opt = AnalyticsFactory.eINSTANCE.createExistingVesselCharterOption();
						opt.setVesselCharter(vesselAvailability);
						return opt;
					} else if (!DISABLE_FLEET) {

						for (final ShippingOption shipOpt : optionAnalysisModel.getShippingTemplates()) {
							if (shipOpt instanceof SimpleVesselCharterOption) {
								final SimpleVesselCharterOption opt = (SimpleVesselCharterOption) shipOpt;
								if (opt.getVessel() != vesselAvailability.getVessel()) {
									continue;
								}
								if (opt.getEntity() != vesselAvailability.getCharterOrDelegateEntity()) {
									continue;
								}
								if (!opt.getHireCost().equals(vesselAvailability.getTimeCharterRate())) {
									continue;
								}

								// TODO: Check other fields

								return opt;
							}
						}

						final SimpleVesselCharterOption opt = AnalyticsFactory.eINSTANCE.createSimpleVesselCharterOption();
						opt.setEntity(vesselAvailability.getCharterOrDelegateEntity());
						opt.setHireCost(vesselAvailability.getTimeCharterRate());
						opt.setVessel(vesselAvailability.getVessel());

						return opt;
					} else {
						// create RT
						for (final ShippingOption shipOpt : optionAnalysisModel.getShippingTemplates()) {
							if (shipOpt instanceof RoundTripShippingOption) {
								final RoundTripShippingOption opt = (RoundTripShippingOption) shipOpt;
								if (opt.getVessel() != vesselAvailability.getVessel()) {
									continue;
								}

								if (opt.getEntity() != vesselAvailability.getEntity()) {
									continue;
								}

								if (!Objects.equal(opt.getHireCost(), vesselAvailability.getTimeCharterRate())) {
									continue;
								}

								// TODO: Check other fields

								return opt;
							}
						}
						final RoundTripShippingOption opt = AnalyticsFactory.eINSTANCE.createRoundTripShippingOption();
						opt.setHireCost(vesselAvailability.getTimeCharterRate());
						opt.setVessel(vesselAvailability.getVessel());
						opt.setEntity(vesselAvailability.getEntity());

						return opt;

					}
				} else if (vat instanceof CharterInMarket) {
					final CharterInMarket charterInMarket = (CharterInMarket) vat;

					if (portfolioMode) {
						for (final ShippingOption shipOpt : optionAnalysisModel.getShippingTemplates()) {
							if (shipOpt instanceof ExistingCharterMarketOption) {
								final ExistingCharterMarketOption opt = (ExistingCharterMarketOption) shipOpt;
								if (opt.getCharterInMarket() == charterInMarket && opt.getSpotIndex() == cargo.getSpotIndex()) {
									return opt;
								}
							}
						}
						final ExistingCharterMarketOption opt = AnalyticsFactory.eINSTANCE.createExistingCharterMarketOption();
						opt.setCharterInMarket(charterInMarket);
						opt.setSpotIndex(cargo.getSpotIndex());
						return opt;
					} else {
						for (final ShippingOption shipOpt : optionAnalysisModel.getShippingTemplates()) {
							if (shipOpt instanceof RoundTripShippingOption) {
								final RoundTripShippingOption opt = (RoundTripShippingOption) shipOpt;
								if (opt.getVessel() != charterInMarket.getVessel()) {
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
						opt.setVessel(charterInMarket.getVessel());
						opt.setEntity(charterInMarket.getEntity());
						return opt;
					}
				}

			}
		} else if (load != null && load.isDESPurchase() && load.getNominatedVessel() != null) {

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
		} else if (dischargeSlot != null && dischargeSlot.isFOBSale() && dischargeSlot.getNominatedVessel() != null) {

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
		return null;
	}

	public static ShippingOption getOrCreateShippingOption(final VesselEvent vesselEvent, final boolean portfolioMode, final AbstractAnalysisModel optionAnalysisModel) {

		if (vesselEvent != null) {
			final VesselAssignmentType vat = vesselEvent.getVesselAssignmentType();
			if (vat instanceof VesselAvailability) {
				final VesselAvailability vesselAvailability = (VesselAvailability) vat;
				if (portfolioMode) {
					for (final ShippingOption shipOpt : optionAnalysisModel.getShippingTemplates()) {
						if (shipOpt instanceof ExistingVesselCharterOption) {
							final ExistingVesselCharterOption opt = (ExistingVesselCharterOption) shipOpt;
							if (opt.getVesselCharter() == vesselAvailability) {
								return opt;
							}

						}
					}
					final ExistingVesselCharterOption opt = AnalyticsFactory.eINSTANCE.createExistingVesselCharterOption();
					opt.setVesselCharter(vesselAvailability);
					return opt;
				} else if (!DISABLE_FLEET) {

					for (final ShippingOption shipOpt : optionAnalysisModel.getShippingTemplates()) {
						if (shipOpt instanceof SimpleVesselCharterOption) {
							final SimpleVesselCharterOption opt = (SimpleVesselCharterOption) shipOpt;
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

					final SimpleVesselCharterOption opt = AnalyticsFactory.eINSTANCE.createSimpleVesselCharterOption();
					opt.setEntity(vesselAvailability.getEntity());
					opt.setHireCost(vesselAvailability.getTimeCharterRate());
					opt.setVessel(vesselAvailability.getVessel());

					return opt;
				} else {
					// create RT
					for (final ShippingOption shipOpt : optionAnalysisModel.getShippingTemplates()) {
						if (shipOpt instanceof RoundTripShippingOption) {
							final RoundTripShippingOption opt = (RoundTripShippingOption) shipOpt;
							if (opt.getVessel() != vesselAvailability.getVessel()) {
								continue;
							}
							if (opt.getEntity() != vesselAvailability.getEntity()) {
								continue;
							}

							if (!Objects.equal(opt.getHireCost(), vesselAvailability.getTimeCharterRate())) {
								continue;
							}

							// TODO: Check other fields

							return opt;
						}
					}
					final RoundTripShippingOption opt = AnalyticsFactory.eINSTANCE.createRoundTripShippingOption();
					opt.setHireCost(vesselAvailability.getTimeCharterRate());
					opt.setVessel(vesselAvailability.getVessel());
					opt.setEntity(vesselAvailability.getEntity());

					return opt;

				}
			} else if (vat instanceof CharterInMarket) {
				final CharterInMarket charterInMarket = (CharterInMarket) vat;

				if (portfolioMode) {
					for (final ShippingOption shipOpt : optionAnalysisModel.getShippingTemplates()) {
						if (shipOpt instanceof ExistingCharterMarketOption) {
							final ExistingCharterMarketOption opt = (ExistingCharterMarketOption) shipOpt;
							if (opt.getCharterInMarket() == charterInMarket && opt.getSpotIndex() == vesselEvent.getSpotIndex()) {
								return opt;
							}
						}
					}
					final ExistingCharterMarketOption opt = AnalyticsFactory.eINSTANCE.createExistingCharterMarketOption();
					opt.setCharterInMarket(charterInMarket);
					opt.setSpotIndex(vesselEvent.getSpotIndex());
					return opt;
				} else {
					for (final ShippingOption shipOpt : optionAnalysisModel.getShippingTemplates()) {
						if (shipOpt instanceof RoundTripShippingOption) {
							final RoundTripShippingOption opt = (RoundTripShippingOption) shipOpt;
							if (opt.getVessel() != charterInMarket.getVessel()) {
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
					opt.setVessel(charterInMarket.getVessel());
					opt.setEntity(charterInMarket.getEntity());
					return opt;
				}
			}
		}
		return null;
	}

	public static SellOption getOrCreateSellOption(final DischargeSlot dischargeSlot, final AbstractAnalysisModel optionAnalysisModel, final IScenarioEditingLocation scenarioEditingLocation,
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
				cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel, AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SELLS, sellRef));
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
			cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel, AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt));
		}
		final Object target = feature.isMany() ? Collections.singletonList(opt) : opt;

		cmd.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), row, feature, target));
		scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, optionAnalysisModel, null);
	}

	public static void applyShipping(final IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel optionAnalysisModel, final BaseCaseRow row, final Vessel vessel,
			final LocalMenuHelper menuHelper) {
		if (row == null) {
			return;
		}
		if (AnalyticsBuilder.isNonShipped(row) == ShippingType.Shipped) {
			applyShipping(scenarioEditingLocation, optionAnalysisModel, row, AnalyticsPackage.Literals.PARTIAL_CASE_ROW__SHIPPING, vessel, menuHelper);
		}
	}

	private static void applyShipping(final IScenarioEditingLocation scenarioEditingLocation, final OptionAnalysisModel optionAnalysisModel, final EObject row, final EStructuralFeature feature,
			final Vessel vessel, final LocalMenuHelper menuHelper) {

		menuHelper.clearActions();
		final ShippingOptionDescriptionFormatter f = new ShippingOptionDescriptionFormatter();
		for (final ShippingOption option : optionAnalysisModel.getShippingTemplates()) {
			if (option instanceof RoundTripShippingOption) {
				final RoundTripShippingOption roundTripShippingOption = (RoundTripShippingOption) option;
				if (roundTripShippingOption.getVessel() == vessel) {
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
			opt.setVessel(vessel);
			final CommercialModel cm = ScenarioModelUtil.getCommercialModel(scenarioEditingLocation.getScenarioDataProvider());
			if (cm.getEntities().size() == 1) {
				opt.setEntity(cm.getEntities().get(0));
			}

			final CompoundCommand cmd = new CompoundCommand();
			cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), optionAnalysisModel, AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES, opt));
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
				final LocalDate date = buyOpportunity.getDate();
				if (date != null) {
					return date.atStartOfDay(ZoneId.of(port.getLocation().getTimeZone())).plusHours(port.getDefaultStartTime());
				}
			}
		} else if (option instanceof BuyReference) {
			final BuyReference buyReference = (BuyReference) option;
			final LoadSlot slot = buyReference.getSlot();
			if (slot != null) {
				return slot.getSchedulingTimeWindow().getStart();
			}
		}
		return null;
	}

	public static ZonedDateTime getWindowStartDate(final SellOption option) {
		if (option instanceof SellOpportunity) {
			final SellOpportunity sellOpportunity = (SellOpportunity) option;
			final Port port = sellOpportunity.getPort();
			if (port != null) {
				return sellOpportunity.getDate().atStartOfDay(port.getZoneId()).plusHours(port.getDefaultStartTime());
			}
		} else if (option instanceof SellReference) {
			final SellReference sellReference = (SellReference) option;
			final DischargeSlot slot = sellReference.getSlot();
			if (slot != null) {
				return slot.getSchedulingTimeWindow().getStart();
			}
		}
		return null;
	}

	public static ZonedDateTime getWindowEndDate(final BuyOption option) {
		if (option instanceof BuyOpportunity) {
			final BuyOpportunity buyOpportunity = (BuyOpportunity) option;
			final Port port = buyOpportunity.getPort();
			if (port != null) {
				ZonedDateTime t = buyOpportunity.getDate().atStartOfDay(ZoneId.of(port.getLocation().getTimeZone())).plusHours(port.getDefaultStartTime());
				if (buyOpportunity.isSpecifyWindow()) {
					if (buyOpportunity.getWindowSizeUnits() == TimePeriod.HOURS) {
						t = t.plusHours(buyOpportunity.getWindowSize());
					} else if (buyOpportunity.getWindowSizeUnits() == TimePeriod.DAYS) {
						t = t.plusDays(buyOpportunity.getWindowSize());
					} else if (buyOpportunity.getWindowSizeUnits() == TimePeriod.MONTHS) {
						t = t.plusMonths(buyOpportunity.getWindowSize());
					} else {
						return null;
					}
				} else {
					if (port.getDefaultWindowSizeUnits() == TimePeriod.HOURS) {
						t = t.plusHours(port.getDefaultWindowSize());
					} else if (port.getDefaultWindowSizeUnits() == TimePeriod.DAYS) {
						t = t.plusDays(port.getDefaultWindowSize());
					} else if (port.getDefaultWindowSizeUnits() == TimePeriod.MONTHS) {
						t = t.plusMonths(port.getDefaultWindowSize());
					} else {
						return null;
					}
				}
				return t;
			}
		} else if (option instanceof BuyReference) {
			final BuyReference buyReference = (BuyReference) option;
			final LoadSlot slot = buyReference.getSlot();
			if (slot != null) {
				return slot.getSchedulingTimeWindow().getEnd();
			}
		}
		return null;
	}

	public static ZonedDateTime getWindowEndDate(final SellOption option) {
		if (option instanceof SellOpportunity) {
			final SellOpportunity sellOpportunity = (SellOpportunity) option;
			final Port port = sellOpportunity.getPort();
			if (port != null) {
				final LocalDate date = sellOpportunity.getDate();
				if (date != null) {
					ZonedDateTime t = date.atStartOfDay(ZoneId.of(port.getLocation().getTimeZone()));
					if (sellOpportunity.isSpecifyWindow()) {
						if (sellOpportunity.getWindowSizeUnits() == TimePeriod.HOURS) {
							t = t.plusHours(sellOpportunity.getWindowSize());
						} else if (sellOpportunity.getWindowSizeUnits() == TimePeriod.DAYS) {
							t = t.plusDays(sellOpportunity.getWindowSize());
						} else if (sellOpportunity.getWindowSizeUnits() == TimePeriod.MONTHS) {
							t = t.plusMonths(sellOpportunity.getWindowSize());
						} else {
							return null;
						}
					} else {
						if (port.getDefaultWindowSizeUnits() == TimePeriod.HOURS) {
							t = t.plusHours(port.getDefaultWindowSize());
						} else if (port.getDefaultWindowSizeUnits() == TimePeriod.DAYS) {
							t = t.plusDays(port.getDefaultWindowSize());
						} else if (port.getDefaultWindowSizeUnits() == TimePeriod.MONTHS) {
							t = t.plusMonths(port.getDefaultWindowSize());
						} else {
							return null;
						}
					}
					return t;
				}
			}
		} else if (option instanceof SellReference) {
			final SellReference sellReference = (SellReference) option;
			final DischargeSlot slot = sellReference.getSlot();
			if (slot != null) {
				return slot.getSchedulingTimeWindow().getEnd();
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

	public static int getDuration(final SellOption option) {
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
				return slot.getSchedulingTimeWindow().getDuration();
			}
		} else if (option instanceof SellMarket) {

			final SellMarket sellMarket = (SellMarket) option;
			final SpotMarket market = sellMarket.getMarket();
			if (market instanceof DESSalesMarket) {
				final DESSalesMarket desSalesMarket = (DESSalesMarket) market;
				final Port port = desSalesMarket.getNotionalPort();
				if (port != null) {
					return port.getDischargeDuration();
				}
			}
		}
		return 0;
	}

	public static int getDuration(final BuyOption option) {
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
				return slot.getSchedulingTimeWindow().getDuration();
			}
		} else if (option instanceof BuyMarket) {

			final BuyMarket buylMarket = (BuyMarket) option;
			final SpotMarket market = buylMarket.getMarket();
			if (market instanceof FOBPurchasesMarket) {
				final FOBPurchasesMarket fobPurchasesMarket = (FOBPurchasesMarket) market;
				final Port port = fobPurchasesMarket.getNotionalPort();
				if (port != null) {
					return port.getLoadDuration();
				}
			}
		}
		return 0;
	}

	public static Vessel getVessel(final ShippingOption shippingOption) {
		Vessel vessel = null;
		if (shippingOption instanceof RoundTripShippingOption) {
			final RoundTripShippingOption roundTripShippingOption = (RoundTripShippingOption) shippingOption;
			vessel = roundTripShippingOption.getVessel();

		} else if (shippingOption instanceof SimpleVesselCharterOption) {
			final SimpleVesselCharterOption roundTripShippingOption = (SimpleVesselCharterOption) shippingOption;
			vessel = roundTripShippingOption.getVessel();
		} else if (shippingOption instanceof ExistingVesselCharterOption) {

			final ExistingVesselCharterOption existingVesselAvailability = (ExistingVesselCharterOption) shippingOption;
			vessel = existingVesselAvailability.getVesselCharter().getVessel();
		} else if (shippingOption instanceof FullVesselCharterOption) {

			final FullVesselCharterOption existingVesselAvailability = (FullVesselCharterOption) shippingOption;
			vessel = existingVesselAvailability.getVesselCharter().getVessel();
		} else if (shippingOption instanceof ExistingCharterMarketOption) {
			final ExistingCharterMarketOption existingCharterMarketOption = (ExistingCharterMarketOption) shippingOption;
			final CharterInMarket market = existingCharterMarketOption.getCharterInMarket();
			if (market != null) {
				vessel = market.getVessel();
			}
		}
		return vessel;
	}

	public static double getCargoCV(final BuyOption option) {
		if (option instanceof final BuyOpportunity buyOpportunity) {
			if (buyOpportunity.getCv() != 0.0) {
				return buyOpportunity.getCv();
			}
			final Port port = buyOpportunity.getPort();
			if (port != null) {
				return port.getCvValue();
			}
		} else if (option instanceof final BuyReference buyReference) {
			final LoadSlot slot = buyReference.getSlot();
			if (slot != null) {
				return slot.getSlotOrDelegateCV();
			}
		} else if (option instanceof final BuyMarket buyMarket) {
			final SpotMarket market = buyMarket.getMarket();
			if (market instanceof final FOBPurchasesMarket fobPurchasesMarket) {
				return fobPurchasesMarket.getCv();
			} else if (market instanceof final DESPurchaseMarket desPurchaseMarket) {
				return desPurchaseMarket.getCv();
			}
		}
		return 0;
	}

	public static double[] getCargoCVRange(final SellOption option) {
		if (option instanceof SellOpportunity) {
			final SellOpportunity sellOpportunity = (SellOpportunity) option;
			if (sellOpportunity.getPort() != null) {
				return new double[] { sellOpportunity.getPort().getMinCvValue(), sellOpportunity.getPort().getMaxCvValue() };
			}
		} else if (option instanceof SellReference) {
			final SellReference sellReference = (SellReference) option;
			final DischargeSlot slot = sellReference.getSlot();
			if (slot != null) {
				return new double[] { slot.getSlotOrDelegateMinCv(), slot.getSlotOrDelegateMaxCv() };
			}
		} else if (option instanceof SellMarket) {
			final SellMarket sellMarket = (SellMarket) option;
			final SpotMarket market = sellMarket.getMarket();
			if (market instanceof FOBSalesMarket) {
				final FOBSalesMarket fobSalesMarket = (FOBSalesMarket) market;
				// return fobSalesMarket.getCv();
			} else if (market instanceof DESSalesMarket) {
				final DESSalesMarket desSalesMarket = (DESSalesMarket) market;
				if (desSalesMarket.getNotionalPort() != null) {
					return new double[] { desSalesMarket.getNotionalPort().getMinCvValue(), desSalesMarket.getNotionalPort().getMaxCvValue() };
				}
			}
		}
		return null;
	}

	public static int[] getBuyVolumeInMMBTU(final BuyOption buy) {
		final double cargoCV = AnalyticsBuilder.getCargoCV(buy);
		if (cargoCV == 0) {
			return null;
		}
		if (buy instanceof BuyReference) {
			final BuyReference buyReference = (BuyReference) buy;
			final LoadSlot slot = ((BuyReference) buy).getSlot();
			if (slot == null) {
				return null;
			}
			final int slotOrContractMinQuantity = slot.getSlotOrDelegateMinQuantity();
			final int slotOrContractMaxQuantity = slot.getSlotOrDelegateMaxQuantity();
			return new int[] { slot.getVolumeLimitsUnit() == VolumeUnits.MMBTU ? slotOrContractMinQuantity : (int) ((double) slotOrContractMinQuantity * cargoCV),
					slotOrContractMaxQuantity == Integer.MAX_VALUE || slot.getVolumeLimitsUnit() == VolumeUnits.MMBTU ? slotOrContractMaxQuantity
							: (int) ((double) slotOrContractMaxQuantity * cargoCV) };
		} else if (buy instanceof BuyOpportunity) {
			final BuyOpportunity buyOpportunity = (BuyOpportunity) buy;
			if (buyOpportunity.getVolumeMode() == VolumeMode.FIXED) {
				return new int[] { buyOpportunity.getVolumeUnits() == VolumeUnits.MMBTU ? buyOpportunity.getMaxVolume() : (int) ((double) buyOpportunity.getMaxVolume() * cargoCV),
						buyOpportunity.getVolumeUnits() == VolumeUnits.MMBTU ? buyOpportunity.getMaxVolume() : (int) ((double) buyOpportunity.getMaxVolume() * cargoCV) };
			} else if (buyOpportunity.getVolumeMode() == VolumeMode.RANGE) {
				return new int[] { buyOpportunity.getVolumeUnits() == VolumeUnits.MMBTU ? buyOpportunity.getMinVolume() : (int) ((double) buyOpportunity.getMinVolume() * cargoCV),
						buyOpportunity.getVolumeUnits() == VolumeUnits.MMBTU ? buyOpportunity.getMaxVolume() : (int) ((double) buyOpportunity.getMaxVolume() * cargoCV) };
			} else {
				return new int[] { 0, (int) ((double) DEFAULT_MAX_VOLUME * cargoCV) };
			}
		} else if (buy instanceof BuyMarket) {
			final BuyMarket market = (BuyMarket) buy;
			return new int[] { market.getMarket().getVolumeLimitsUnit() == VolumeUnits.MMBTU ? market.getMarket().getMinQuantity() : (int) ((double) market.getMarket().getMinQuantity() * cargoCV),
					market.getMarket().getVolumeLimitsUnit() == VolumeUnits.MMBTU ? market.getMarket().getMaxQuantity() : (int) ((double) market.getMarket().getMaxQuantity() * cargoCV) };
		}
		return null;
	}

	public static int[] getSellVolumeInMMBTU(final BuyOption buy, final SellOption sell) {
		final double cargoCV = AnalyticsBuilder.getCargoCV(buy);
		if (cargoCV == 0) {
			return null;
		}
		if (sell instanceof SellReference) {
			final SellReference sellReference = (SellReference) sell;
			final DischargeSlot slot = ((SellReference) sell).getSlot();
			final int slotOrContractMinQuantity = slot.getSlotOrDelegateMinQuantity();
			final int slotOrContractMaxQuantity = slot.getSlotOrDelegateMaxQuantity();
			return new int[] { slot.getVolumeLimitsUnit() == VolumeUnits.MMBTU ? slotOrContractMinQuantity : (int) ((double) slotOrContractMinQuantity * cargoCV),
					slotOrContractMaxQuantity == Integer.MAX_VALUE || slot.getVolumeLimitsUnit() == VolumeUnits.MMBTU ? slotOrContractMaxQuantity
							: (int) ((double) slotOrContractMaxQuantity * cargoCV) };
		} else if (sell instanceof SellOpportunity) {
			final SellOpportunity sellOpportunity = (SellOpportunity) sell;
			if (sellOpportunity.getVolumeMode() == VolumeMode.FIXED) {
				return new int[] { sellOpportunity.getVolumeUnits() == VolumeUnits.MMBTU ? sellOpportunity.getMaxVolume() : (int) ((double) sellOpportunity.getMaxVolume() * cargoCV),
						sellOpportunity.getVolumeUnits() == VolumeUnits.MMBTU ? sellOpportunity.getMaxVolume() : (int) ((double) sellOpportunity.getMaxVolume() * cargoCV) };
			} else if (sellOpportunity.getVolumeMode() == VolumeMode.RANGE) {
				return new int[] { sellOpportunity.getVolumeUnits() == VolumeUnits.MMBTU ? sellOpportunity.getMinVolume() : (int) ((double) sellOpportunity.getMinVolume() * cargoCV),
						sellOpportunity.getVolumeUnits() == VolumeUnits.MMBTU ? sellOpportunity.getMaxVolume() : (int) ((double) sellOpportunity.getMaxVolume() * cargoCV) };
			}
		} else if (sell instanceof SellMarket) {
			final SellMarket market = (SellMarket) sell;
			return new int[] { market.getMarket().getVolumeLimitsUnit() == VolumeUnits.MMBTU ? market.getMarket().getMinQuantity() : (int) ((double) market.getMarket().getMinQuantity() * cargoCV),
					market.getMarket().getVolumeLimitsUnit() == VolumeUnits.MMBTU ? market.getMarket().getMaxQuantity() : (int) ((double) market.getMarket().getMaxQuantity() * cargoCV) };
		}
		return null;
	}

	public static Collection<APortSet<Port>> getPortRestrictions(final ShippingOption option) {
		if (option == null) {
			return Collections.emptyList();
		} else if (option instanceof SimpleVesselCharterOption) {
			final Vessel vessel = ((SimpleVesselCharterOption) option).getVessel();
			if (vessel != null) {
				return vessel.getVesselOrDelegateInaccessiblePorts();
			}
		} else if (option instanceof RoundTripShippingOption) {
			final Vessel vessel = ((RoundTripShippingOption) option).getVessel();
			if (vessel != null) {
				return vessel.getVesselOrDelegateInaccessiblePorts();
			}
		} else if (option instanceof NominatedShippingOption) {
			final Vessel nominatedVessel = ((NominatedShippingOption) option).getNominatedVessel();
			if (nominatedVessel != null) {
				nominatedVessel.getVesselOrDelegateInaccessiblePorts();
			}
		}
		return Collections.emptyList();
	}

	public static Pair<Boolean, Set<AVesselSet<Vessel>>> getBuyVesselRestrictions(final BuyOption buy) {
		final Set<AVesselSet<Vessel>> expandedVessels = new HashSet<>();
		boolean permitted = false;
		if (buy instanceof BuyReference) {
			final LoadSlot slot = ((BuyReference) buy).getSlot();
			if (slot != null) {
				final List<AVesselSet<Vessel>> allowedVessels = slot.getSlotOrDelegateVesselRestrictions();
				permitted = slot.getSlotOrDelegateVesselRestrictionsArePermissive();
				for (final AVesselSet<Vessel> s : allowedVessels) {
					if (s instanceof Vessel) {
						expandedVessels.add(s);
					} else {
						// This is ok as other impl (VesselGroup and
						// VesselTypeGroup) only permit contained Vessels
						expandedVessels.addAll(SetUtils.getObjects(s));
					}
				}
			}
		}
		return Pair.of(permitted, expandedVessels);
	}

	public static Pair<Boolean, Set<AVesselSet<Vessel>>> getSellVesselRestrictions(final SellOption sell) {
		final Set<AVesselSet<Vessel>> expandedVessels = new HashSet<>();
		boolean permitted = false;
		if (sell instanceof SellReference) {
			final DischargeSlot slot = ((SellReference) sell).getSlot();
			if (slot != null) {
				final List<AVesselSet<Vessel>> allowedVessels = slot.getSlotOrDelegateVesselRestrictions();
				permitted = slot.getSlotOrDelegateVesselRestrictionsArePermissive();
				for (final AVesselSet<Vessel> s : allowedVessels) {
					if (s instanceof Vessel) {
						expandedVessels.add(s);
					} else {
						// This is ok as other impl (VesselGroup and
						// VesselTypeGroup) only permit contained Vessels
						expandedVessels.addAll(SetUtils.getObjects(s));
					}
				}
			}
		}
		return Pair.of(permitted, expandedVessels);
	}

	public static Pair<Boolean, Set<APortSet<Port>>> getBuyPortRestrictions(final BuyOption buy) {
		final Set<APortSet<Port>> expandedPorts = new HashSet<>();
		boolean permitted = false;
		if (buy instanceof BuyReference) {
			final LoadSlot slot = ((BuyReference) buy).getSlot();
			if (slot != null) {
				final List<APortSet<Port>> allowedPorts = slot.getSlotOrDelegatePortRestrictions();
				permitted = slot.getSlotOrDelegatePortRestrictionsArePermissive();
				for (final APortSet<Port> s : allowedPorts) {
					if (s instanceof Port) {
						expandedPorts.add(s);
					} else {
						expandedPorts.addAll(SetUtils.getObjects(s));
					}
				}
			}
		}
		return Pair.of(permitted, expandedPorts);
	}

	public static Pair<Boolean, Set<APortSet<Port>>> getSellPortRestrictions(final SellOption sell) {
		final Set<APortSet<Port>> expandedPorts = new HashSet<>();
		boolean permitted = false;
		if (sell instanceof SellReference) {
			final DischargeSlot slot = ((SellReference) sell).getSlot();
			if (slot != null) {
				final List<APortSet<Port>> allowedPorts = slot.getSlotOrDelegatePortRestrictions();
				permitted = slot.getSlotOrDelegatePortRestrictionsArePermissive();
				for (final APortSet<Port> s : allowedPorts) {
					if (s instanceof Port) {
						expandedPorts.add(s);
					} else {
						expandedPorts.addAll(SetUtils.getObjects(s));
					}
				}
			}
		}
		return Pair.of(permitted, expandedPorts);
	}

	public static Predicate<BuyOption> isFOBPurchase() {
		return b -> ((b instanceof BuyReference && ((BuyReference) b).getSlot() != null && ((BuyReference) b).getSlot().isDESPurchase() == false) //
				|| (b instanceof BuyMarket && ((BuyMarket) b).getMarket() instanceof FOBPurchasesMarket) //
				|| (b instanceof BuyOpportunity && ((BuyOpportunity) b).isDesPurchase() == false));
	}

	public static Predicate<BuyOption> isDESPurchase() {
		return b -> ((b instanceof BuyReference && ((BuyReference) b).getSlot() != null && ((BuyReference) b).getSlot().isDESPurchase() == true) //
				|| (b instanceof BuyMarket && ((BuyMarket) b).getMarket() instanceof DESPurchaseMarket) //
				|| (b instanceof BuyOpportunity && ((BuyOpportunity) b).isDesPurchase() == true));
	}

	public static Predicate<SellOption> isFOBSale() {
		return s -> ((s instanceof SellReference && ((SellReference) s).getSlot() != null && ((SellReference) s).getSlot().isFOBSale() == true) //
				|| (s instanceof SellMarket && ((SellMarket) s).getMarket() instanceof FOBSalesMarket) //
				|| (s instanceof SellOpportunity && ((SellOpportunity) s).isFobSale() == true));
	}

	public static Predicate<SellOption> isDESSale() {
		return s -> ((s instanceof SellReference && ((SellReference) s).getSlot() != null && ((SellReference) s).getSlot().isFOBSale() == false) //
				|| (s instanceof SellMarket && ((SellMarket) s).getMarket() instanceof DESSalesMarket) //
				|| (s instanceof SellOpportunity && ((SellOpportunity) s).isFobSale() == false));
	}

	public static Predicate<ShippingOption> isNominated() {
		return NominatedShippingOption.class::isInstance;
	}

	public static boolean isRoundTripOption(final ShippingOption opt) {
		return opt instanceof RoundTripShippingOption;
	}

	public static VesselEvent makeVesselEvent(final VesselEventOption vesselEventOption, final LNGScenarioModel scenarioModel) {

		if (vesselEventOption instanceof VesselEventReference) {
			final VesselEventReference vesselEventReference = (VesselEventReference) vesselEventOption;
			return vesselEventReference.getEvent();
		} else if (vesselEventOption instanceof CharterOutOpportunity) {
			final CharterOutOpportunity opportunity = (CharterOutOpportunity) vesselEventOption;

			final String baseName = vesselEventOptionDescriptionFormatter.render(opportunity);

			// Get existing names
			final Set<String> usedIDStrings = new HashSet<>();
			for (final VesselEvent ve : scenarioModel.getCargoModel().getVesselEvents()) {
				usedIDStrings.add(ve.getName());
			}

			final String id = getUniqueID(baseName, usedIDStrings);

			final CharterOutEvent event = CargoFactory.eINSTANCE.createCharterOutEvent();
			event.setName(id);
			event.setPort(opportunity.getPort());
			event.setHireRate(opportunity.getHireCost());
			event.setDurationInDays(opportunity.getDuration());
			event.setStartAfter(opportunity.getDate().atStartOfDay());
			event.setStartBy(opportunity.getDate().plusDays(1).atStartOfDay());

			event.setAvailableHeel(CommercialFactory.eINSTANCE.createStartHeelOptions());
			event.setRequiredHeel(CommercialFactory.eINSTANCE.createEndHeelOptions());
			event.getRequiredHeel().setTankState(EVesselTankState.EITHER);

			return event;
		}

		return null;
	}

	public static boolean isSpot(final BuyOption buy) {
		if (buy instanceof BuyReference) {
			final BuyReference ref = (BuyReference) buy;
			return ref.getSlot() instanceof SpotSlot;
		} else if (buy instanceof BuyMarket) {
			return true;
		}
		return false;
	}

	public static boolean isSpot(final SellOption sell) {
		if (sell instanceof SellReference) {
			final SellReference ref = (SellReference) sell;
			return ref.getSlot() instanceof SpotSlot;
		} else if (sell instanceof SellMarket) {
			return true;
		}
		return false;
	}

	public static GenericCharterContract createCharterTerms(final String sRepositioningFee, final String sBallastBonus) {
		final GenericCharterContract gcc = CommercialFactory.eINSTANCE.createGenericCharterContract();
		if (sRepositioningFee != null) {
			final IRepositioningFee repositioningFee = CommercialFactory.eINSTANCE.createSimpleRepositioningFeeContainer();
			final LumpSumRepositioningFeeTerm term = CommercialFactory.eINSTANCE.createLumpSumRepositioningFeeTerm();
			term.setPriceExpression(sRepositioningFee);
			((SimpleRepositioningFeeContainer) repositioningFee).getTerms().add(term);
			gcc.setRepositioningFeeTerms(repositioningFee);
		}
		if (sBallastBonus != null) {
			final IBallastBonus ballastBonus = CommercialFactory.eINSTANCE.createSimpleBallastBonusContainer();
			final LumpSumBallastBonusTerm term = CommercialFactory.eINSTANCE.createLumpSumBallastBonusTerm();
			term.setPriceExpression(sBallastBonus);
			((SimpleBallastBonusContainer) ballastBonus).getTerms().add(term);
			gcc.setBallastBonusTerms(ballastBonus);
		}
		return gcc;
	}

	public static boolean isNullOrOpen(final BuyOption opt) {
		return opt == null || opt instanceof OpenBuy;
	}

	public static boolean isNullOrOpen(final SellOption opt) {
		return opt == null || opt instanceof OpenSell;
	}
}

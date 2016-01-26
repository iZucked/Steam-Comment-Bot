/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.time.LocalDate;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.VolumeUnits;

public class AbstractSlotMaker<T extends AbstractSlotMaker<T>> {
	@NonNull
	protected final CargoModelBuilder cargoModelBuilder;

	protected Slot slot = null;

	public AbstractSlotMaker(@NonNull final CargoModelBuilder cargoModelBuilder) {
		this.cargoModelBuilder = cargoModelBuilder;
	}

	public T withFOBPurchase(@NonNull final String name, @NonNull final LocalDate windowStart, @NonNull final Port port, @Nullable final PurchaseContract purchaseContract,
			@Nullable final BaseLegalEntity entity, @Nullable final String priceExpression) {

		final Slot slot = cargoModelBuilder.createFOBPurchase(name, windowStart, port, purchaseContract, entity, priceExpression);
		this.slot = slot;
		return (T) this;
	}

	public T withDESPurchase(@NonNull final String name, final boolean divertible, @NonNull final LocalDate windowStart, @NonNull final Port port, @Nullable final PurchaseContract purchaseContract,
			@Nullable final BaseLegalEntity entity, @Nullable final String priceExpression, @Nullable final Vessel nominatedVessel) {
		final Slot slot = cargoModelBuilder.createDESPurchase(name, divertible, windowStart, port, purchaseContract, entity, priceExpression, nominatedVessel);
		this.slot = slot;
		return (T) this;
	}

	public T withMarketFOBPurchase(@NonNull final String name, @NonNull final FOBPurchasesMarket market, @NonNull final LocalDate windowStart, @NonNull final Port port,
			@Nullable final PurchaseContract purchaseContract, @Nullable final BaseLegalEntity entity, @Nullable final String priceExpression) {
		final Slot slot = cargoModelBuilder.createSpotFOBPurchase(name, market, windowStart, port, purchaseContract, entity, priceExpression);
		this.slot = slot;
		return (T) this;
	}

	public T withMarketDESPurchase(@NonNull final String name, @NonNull final DESPurchaseMarket market, @NonNull final LocalDate windowStart, @NonNull final Port port,
			@Nullable final PurchaseContract purchaseContract, @Nullable final BaseLegalEntity entity, @Nullable final String priceExpression) {
		final Slot slot = cargoModelBuilder.createSpotDESPurchase(name, market, windowStart, port, purchaseContract, entity, priceExpression);
		this.slot = slot;
		return (T) this;
	}

	public T withDESSale(@NonNull final String name, @NonNull final LocalDate windowStart, @NonNull final Port port, @Nullable final SalesContract salesContract,
			@Nullable final BaseLegalEntity entity, @Nullable final String priceExpression) {
		final Slot slot = cargoModelBuilder.createDESSale(name, windowStart, port, salesContract, entity, priceExpression);
		this.slot = slot;
		return (T) this;
	}

	public T withFOBSale(@NonNull final String name, final boolean divertible, @NonNull final LocalDate windowStart, @NonNull final Port port, @Nullable final SalesContract salesContract,
			@Nullable final BaseLegalEntity entity, @Nullable final String priceExpression, @Nullable final Vessel nominatedVessel) {
		final Slot slot = cargoModelBuilder.createFOBSale(name, divertible, windowStart, port, salesContract, entity, priceExpression, nominatedVessel);
		this.slot = slot;
		return (T) this;
	}

	public T withMarketDESSale(@NonNull final String name, @NonNull final DESSalesMarket market, @NonNull final LocalDate windowStart, @NonNull final Port port,
			@Nullable final SalesContract salesContract, @Nullable final BaseLegalEntity entity, @Nullable final String priceExpression) {
		final Slot slot = cargoModelBuilder.createSpotDESSale(name, market, windowStart, port, salesContract, entity, priceExpression);
		this.slot = slot;
		return (T) this;
	}

	public T withMarketFOBSale(@NonNull final String name, @NonNull final FOBSalesMarket market, @NonNull final LocalDate windowStart, @NonNull final Port port,
			@Nullable final SalesContract salesContract, @Nullable final BaseLegalEntity entity, @Nullable final String priceExpression) {
		final Slot slot = cargoModelBuilder.createSpotFOBSale(name, market, windowStart, port, salesContract, entity, priceExpression);
		this.slot = slot;
		return (T) this;
	}

	// TODO: Move this into the cargo builder as configure methods
	public T withVolumeLimits(@Nullable final Integer minVolume, @Nullable final Integer maxVolume, @Nullable final VolumeUnits volumeUnit) {
		if (volumeUnit != null) {
			slot.setVolumeLimitsUnit(volumeUnit);
		} else {
			slot.unsetVolumeLimitsUnit();
		}
		if (minVolume != null) {
			slot.setMinQuantity(minVolume);
		} else {
			slot.unsetMinQuantity();
		}
		if (maxVolume != null) {
			slot.setMaxQuantity(maxVolume);
		} else {
			slot.unsetMaxQuantity();
		}
		return (T) this;
	}

	public T withOptional(final boolean optional) {
		slot.setOptional(optional);
		return (T) this;
	}

	public T withVisitDuration(@Nullable final Integer visitDurationInHours) {
		if (visitDurationInHours != null) {
			slot.setDuration(visitDurationInHours);
		} else {
			slot.unsetDuration();
		}
		return (T) this;
	}

	public T withWindowSize(@Nullable final Integer windowSizeInHours) {
		if (windowSizeInHours != null) {
			slot.setWindowSize(windowSizeInHours);
		} else {
			slot.unsetWindowSize();
		}
		return (T) this;
	}

	public T withWindowStartTime(@Nullable final Integer windowStartHourOfDay) {
		if (windowStartHourOfDay != null) {
			slot.setWindowStartTime(windowStartHourOfDay);
		} else {
			slot.unsetWindowStartTime();
		}
		return (T) this;
	}

	public T withLocked(final boolean locked) {
		slot.setLocked(locked);
		return (T) this;
	}

	public T withVesselRestriction(@NonNull final AVesselSet<Vessel> restrictedVessel) {
		slot.getAllowedVessels().clear();
		slot.getAllowedVessels().add(restrictedVessel);
		return (T) this;
	}

	public T withPricingEvent(@Nullable final PricingEvent pricingEvent, @Nullable final LocalDate pricingDate) {
		if (pricingDate != null) {
			slot.setPricingDate(pricingDate);
		} else {
			slot.unsetPricingDate();
		}
		if (pricingEvent != null) {
			slot.setPricingEvent(pricingEvent);
		} else {
			slot.unsetPricingEvent();
		}
		return (T) this;
	}
}

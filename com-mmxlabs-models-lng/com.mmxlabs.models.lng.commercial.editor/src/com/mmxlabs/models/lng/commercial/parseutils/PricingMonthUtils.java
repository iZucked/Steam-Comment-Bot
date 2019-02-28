/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.parseutils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

public class PricingMonthUtils {
	/**
	 * Returns the pricing {@link YearMonth} for this {@link SlotAllocation}
	 * 
	 * @param slotAllocation
	 * @return
	 */
	public static @Nullable YearMonth getPricingDate(@NonNull final SlotAllocation slotAllocation) {
		LocalDate localDate = getFullPricingDate(slotAllocation);
		return YearMonth.of(localDate.getYear(), localDate.getMonth());
	}
	
	public static SlotAllocation getDischargeIfExists(@NonNull final SlotAllocation slotAllocation) {
		SlotAllocation result = slotAllocation;
		final Slot slot = result.getSlot();
		if (slot instanceof LoadSlot) {
			final CargoAllocation cargoAllocation = slotAllocation.getCargoAllocation();
			if (cargoAllocation != null && cargoAllocation.getCargoType() == CargoType.FLEET) {
				for (final SlotAllocation sa : cargoAllocation.getSlotAllocations()) {
					if (sa.equals(slotAllocation)) continue;
					final Slot s = sa.getSlot();
					if (s instanceof DischargeSlot) {
						result = sa;
					}
				}
			}
		}
		return result;
	}
	
	public static @Nullable LocalDate getFullPricingDate(@NonNull final SlotAllocation slotAllocation) {
		final Slot slot = slotAllocation.getSlot();

		Optional<LocalDate> pricingDate = Optional.empty();
		if (slot.isSetPricingDate()) {
			final LocalDate slotPricingDate = slot.getPricingDate();
			pricingDate = Optional.of(slotPricingDate);
		} else {
			switch (slot.getSlotOrDelegatePricingEvent()) {
			case END_DISCHARGE:
				pricingDate = getCompletionOf.apply(getDischargeAllocationOf.apply(slotAllocation));
				break;
			case END_LOAD:
				pricingDate = getCompletionOf.apply(getLoadAllocationOf.apply(slotAllocation));
				break;
			case START_DISCHARGE:
				pricingDate = getStartOf.apply(getDischargeAllocationOf.apply(slotAllocation));
				break;
			case START_LOAD:
				pricingDate = getStartOf.apply(getLoadAllocationOf.apply(slotAllocation));
				break;
			default:
				// pricingDate = null;
			}

			// Special case for spot market date shift pricing. Transform the pricing date to the correct month.
			// TODO: Should be part of pricing syntax
			if (pricingDate != null && pricingDate.isPresent() && slot instanceof SpotSlot) {
				final SpotSlot spotSlot = (SpotSlot) slot;
				final SpotMarket market = spotSlot.getMarket();
				if (market.getPriceInfo() instanceof DateShiftExpressionPriceParameters) {
					final DateShiftExpressionPriceParameters params = (DateShiftExpressionPriceParameters) market.getPriceInfo();
					if (params.isSpecificDay()) {
						if (pricingDate.get().getDayOfMonth() >= params.getValue()) {
							pricingDate = Optional.of(pricingDate.get().plusMonths(1));
						}
					} else {
						pricingDate = Optional.of(pricingDate.get().minusDays(params.getValue()));
					}
				}
			}

		}
		if (pricingDate.isPresent()) {
			return pricingDate.get();
		}
		return null;
	}

	private static final Function<Optional<SlotAllocation>, Optional<LocalDate>> getCompletionOf = slotAllocation -> {
		if (slotAllocation.isPresent()) {
			final ZonedDateTime end = slotAllocation.get().getSlotVisit().getEnd();
			final ZonedDateTime withZoneSameLocal = end.withZoneSameLocal(ZoneId.of("UTC"));
			return Optional.of(withZoneSameLocal.toLocalDate());
		} else {
			return Optional.empty();
		}
	};

	private static final Function<Optional<SlotAllocation>, Optional<LocalDate>> getStartOf = slotAllocation -> {
		if (slotAllocation.isPresent()) {
			final ZonedDateTime end = slotAllocation.get().getSlotVisit().getStart();
			final ZonedDateTime withZoneSameLocal = end.withZoneSameLocal(ZoneId.of("UTC"));
			return Optional.of(withZoneSameLocal.toLocalDate());
		} else {
			return Optional.empty();
		}
	};

	private static final BiFunction<Optional<SlotAllocation>, Class<? extends Slot>, Optional<SlotAllocation>> getAllocationOf = (slotAllocation, cls) -> {
		if (slotAllocation.isPresent()) {
			final Slot slot = slotAllocation.get().getSlot();
			if (cls.isInstance(slot)) {
				return slotAllocation;
			}
			final CargoAllocation cargoAllocation = slotAllocation.get().getCargoAllocation();
			if (cargoAllocation != null) {
				for (final SlotAllocation slotAllocation2 : cargoAllocation.getSlotAllocations()) {
					if (cls.isInstance(slotAllocation2.getSlot())) {
						return Optional.of(slotAllocation2);
					}
				}
			}
		}
		return Optional.empty();
	};

	private static final Function<SlotAllocation, Optional<SlotAllocation>> getLoadAllocationOf = (slotAllocation) -> getAllocationOf.apply(Optional.of(slotAllocation), LoadSlot.class);
	private static final Function<SlotAllocation, Optional<SlotAllocation>> getDischargeAllocationOf = (slotAllocation) -> getAllocationOf.apply(Optional.of(slotAllocation), DischargeSlot.class);

}

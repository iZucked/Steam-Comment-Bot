package com.mmxlabs.models.lng.commercial.parseutils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocation;

public class PricingMonthUtils {
	/**
	 * Returns the pricing {@link YearMonth} for this {@link SlotAllocation}
	 * 
	 * @param slotAllocation
	 * @return
	 */
	public static @Nullable YearMonth getPricingDate(@NonNull final SlotAllocation slotAllocation) {
		final Slot slot = slotAllocation.getSlot();

		final Optional<YearMonth> pricingDate;
		if (slot.isSetPricingDate()) {
			final LocalDate slotPricingDate = slot.getPricingDate();
			pricingDate = Optional.of(YearMonth.of(slotPricingDate.getYear(), slotPricingDate.getMonthValue()));
		} else {
			switch (slot.getSlotOrDelegatedPricingEvent()) {
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
				pricingDate = null;
			}
		}
		return pricingDate.get();
	}

	private static final Function<Optional<SlotAllocation>, Optional<YearMonth>> getCompletionOf = slotAllocation -> {
		if (slotAllocation.isPresent()) {
			final ZonedDateTime end = slotAllocation.get().getSlotVisit().getEnd();
			final ZonedDateTime withZoneSameLocal = end.withZoneSameLocal(ZoneId.of("UTC"));
			return Optional.of(YearMonth.of(withZoneSameLocal.getYear(), withZoneSameLocal.getMonthValue()));
		} else {
			return Optional.empty();
		}
	};

	private static final Function<Optional<SlotAllocation>, Optional<YearMonth>> getStartOf = slotAllocation -> {
		if (slotAllocation.isPresent()) {
			final ZonedDateTime end = slotAllocation.get().getSlotVisit().getStart();
			final ZonedDateTime withZoneSameLocal = end.withZoneSameLocal(ZoneId.of("UTC"));
			return Optional.of(YearMonth.of(withZoneSameLocal.getYear(), withZoneSameLocal.getMonthValue()));
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

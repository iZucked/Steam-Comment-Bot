/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.time.LocalDateTime;
import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.EVesselTankState;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;

public class VesselAvailabilityMaker {

	private final CargoModelBuilder cargoModelBuilder;

	@NonNull
	private final VesselAvailability vesselAvailability;

	public VesselAvailabilityMaker(final CargoModelBuilder cargoModelBuilder, @NonNull final Vessel vessel, @NonNull final BaseLegalEntity entity) {
		this.cargoModelBuilder = cargoModelBuilder;
		this.vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		this.vesselAvailability.setStartHeel(CargoFactory.eINSTANCE.createStartHeelOptions());
		this.vesselAvailability.setEndHeel(CargoFactory.eINSTANCE.createEndHeelOptions());
		this.vesselAvailability.setVessel(vessel);
		this.vesselAvailability.setEntity(entity);
	}

	public VesselAvailabilityMaker withStartPort(@Nullable final Port port) {
		vesselAvailability.setStartAt(port);

		return this;
	}

	public VesselAvailabilityMaker withEndPort(@Nullable final Port port) {
		vesselAvailability.getEndAt().clear();
		if (port != null) {
			vesselAvailability.getEndAt().add(port);
		}

		return this;
	}

	public VesselAvailabilityMaker withEndPorts(APortSet<Port> endPorts) {
		vesselAvailability.getEndAt().clear();
		vesselAvailability.getEndAt().add(endPorts);
		return this;
	}

	public VesselAvailabilityMaker withEndPorts(Collection<APortSet<Port>> endPorts) {
		vesselAvailability.getEndAt().clear();
		vesselAvailability.getEndAt().addAll(endPorts);
		return this;
	}

	public VesselAvailabilityMaker withStartWindow(@NonNull final LocalDateTime exactTime) {
		return withStartWindow(exactTime, exactTime);
	}

	public VesselAvailabilityMaker withStartWindow(@Nullable final LocalDateTime windowStart, @Nullable final LocalDateTime windowEnd) {
		if (windowStart != null) {
			vesselAvailability.setStartAfter(windowStart);
		} else {
			vesselAvailability.unsetStartAfter();
		}
		if (windowEnd != null) {
			vesselAvailability.setStartBy(windowEnd);
		} else {
			vesselAvailability.unsetStartBy();
		}
		return this;
	}

	public VesselAvailabilityMaker withEndWindow(@NonNull final LocalDateTime exactTime) {
		return withEndWindow(exactTime, exactTime);
	}

	public VesselAvailabilityMaker withEndWindow(@Nullable final LocalDateTime windowStart, @Nullable final LocalDateTime windowEnd) {
		if (windowStart != null) {
			vesselAvailability.setEndAfter(windowStart);
		} else {
			vesselAvailability.unsetEndAfter();
		}
		if (windowEnd != null) {
			vesselAvailability.setEndBy(windowEnd);
		} else {
			vesselAvailability.unsetEndBy();
		}
		return this;
	}

	public VesselAvailabilityMaker withOptionality(final boolean isOptional) {
		vesselAvailability.setOptional(isOptional);
		return this;
	}

	public VesselAvailabilityMaker withRepositioning(final String fee) {
		if (fee != null) {
			vesselAvailability.setRepositioningFee(fee);
		} else {
			vesselAvailability.setRepositioningFee("");
		}
		return this;
	}

	/**
	 * Use the safety heel as the start level, 22.6 CV and 0 price
	 * 
	 * @param minVolumeInM3
	 * @param maxVolumeInM3
	 * @param cvValue
	 * @param pricePerMMBTu
	 * @return
	 */
	public VesselAvailabilityMaker withSafeyStartHeel() {
		return withStartHeel(vesselAvailability.getVessel().getSafetyHeel(), vesselAvailability.getVessel().getSafetyHeel(), 22.6, "0");
	}

	/**
	 * Use the safety heel as the end heel, 0 price
	 * 
	 * @param minVolumeInM3
	 * @param maxVolumeInM3
	 * @param cvValue
	 * @param pricePerMMBTu
	 * @return
	 */
	public VesselAvailabilityMaker withSafeyEndHeel() {
		return withEndHeel(vesselAvailability.getVessel().getSafetyHeel(), vesselAvailability.getVessel().getSafetyHeel(), EVesselTankState.MUST_BE_COLD, "0");
	}

	public VesselAvailabilityMaker withStartHeel(final double minVolumeInM3, final double maxVolumeInM3, final double cvValue, final String pricePerMMBTu) {
		if (minVolumeInM3 < 0) {
			throw new IllegalArgumentException();
		}

		if (maxVolumeInM3 < 0) {
			throw new IllegalArgumentException();
		}
		if (minVolumeInM3 > maxVolumeInM3) {
			throw new IllegalArgumentException();
		}

		vesselAvailability.getStartHeel().setMinVolumeAvailable(minVolumeInM3);
		vesselAvailability.getStartHeel().setMaxVolumeAvailable(maxVolumeInM3);
		vesselAvailability.getStartHeel().setCvValue(cvValue);
		vesselAvailability.getStartHeel().setPriceExpression(pricePerMMBTu);

		return this;

	}

	public VesselAvailabilityMaker withEndHeel(final int minVolumeInM3, final int maxVolumeInM3, @NonNull final EVesselTankState state, @Nullable final String priceExpression) {

		if (minVolumeInM3 < 0) {
			throw new IllegalArgumentException();
		}

		if (maxVolumeInM3 < 0) {
			throw new IllegalArgumentException();
		}
		if (minVolumeInM3 > maxVolumeInM3) {
			throw new IllegalArgumentException();
		}

		if (state == EVesselTankState.MUST_BE_WARM) {
			if (minVolumeInM3 > 0) {
				throw new IllegalArgumentException();
			}
			if (maxVolumeInM3 > 0) {
				throw new IllegalArgumentException();
			}
		}

		vesselAvailability.getEndHeel().setMinimumEndHeel(minVolumeInM3);
		vesselAvailability.getEndHeel().setMaximumEndHeel(maxVolumeInM3);
		vesselAvailability.getEndHeel().setTankState(state);
		vesselAvailability.getEndHeel().setPriceExpression(priceExpression);

		return this;

	}

	public VesselAvailabilityMaker withEndHeel(final int minVolumeInM3, final int maxVolumeInM3, @NonNull final EVesselTankState state, boolean useLastPrice) {

		if (minVolumeInM3 < 0) {
			throw new IllegalArgumentException();
		}

		if (maxVolumeInM3 < 0) {
			throw new IllegalArgumentException();
		}
		if (minVolumeInM3 > maxVolumeInM3) {
			throw new IllegalArgumentException();
		}

		if (state == EVesselTankState.MUST_BE_WARM) {
			if (minVolumeInM3 > 0) {
				throw new IllegalArgumentException();
			}
			if (maxVolumeInM3 > 0) {
				throw new IllegalArgumentException();
			}
		}

		vesselAvailability.getEndHeel().setMinimumEndHeel(minVolumeInM3);
		vesselAvailability.getEndHeel().setMaximumEndHeel(maxVolumeInM3);
		vesselAvailability.getEndHeel().setTankState(state);
		vesselAvailability.getEndHeel().setPriceExpression("");
		vesselAvailability.getEndHeel().setUseLastHeelPrice(useLastPrice);

		return this;

	}

	@NonNull
	public VesselAvailabilityMaker withCharterRate(final @NonNull String priceExpression) {
		vesselAvailability.setTimeCharterRate(priceExpression);
		return this;
	}

	@NonNull
	public VesselAvailabilityMaker withMinDuration(final int durationInDays) {
		vesselAvailability.setMinDuration(durationInDays);
		return this;
	}

	@NonNull
	public VesselAvailabilityMaker withMaxDuration(final int durationInDays) {
		vesselAvailability.setMaxDuration(durationInDays);
		return this;
	}

	@NonNull
	public VesselAvailability build() {

		cargoModelBuilder.getCargoModel().getVesselAvailabilities().add(vesselAvailability);

		return vesselAvailability;
	}

}

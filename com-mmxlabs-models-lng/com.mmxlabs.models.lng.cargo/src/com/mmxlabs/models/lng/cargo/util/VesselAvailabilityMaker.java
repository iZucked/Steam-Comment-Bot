/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.time.LocalDateTime;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.EndHeelOptions;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.HeelOptions;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;

public class VesselAvailabilityMaker {
	@NonNull
	private final CargoModelBuilder cargoModelBuilder;

	@NonNull
	private final VesselAvailability vesselAvailability;

	public VesselAvailabilityMaker(@NonNull final CargoModelBuilder cargoModelBuilder, @NonNull Vessel vessel, @NonNull BaseLegalEntity entity) {
		this.cargoModelBuilder = cargoModelBuilder;
		this.vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		this.vesselAvailability.setStartHeel(FleetFactory.eINSTANCE.createHeelOptions());
		this.vesselAvailability.setEndHeel(CargoFactory.eINSTANCE.createEndHeelOptions());
		this.vesselAvailability.setVessel(vessel);
		this.vesselAvailability.setEntity(entity);
	}

	public VesselAvailabilityMaker withStartPort(@Nullable final Port port) {
		vesselAvailability.getStartAt().clear();
		if (port != null) {
			vesselAvailability.getStartAt().add(port);
		}

		return this;
	}

	public VesselAvailabilityMaker withEndPort(@Nullable final Port port) {
		vesselAvailability.getEndAt().clear();
		if (port != null) {
			vesselAvailability.getEndAt().add(port);
		}

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
			vesselAvailability.unsetRepositioningFee();
		}
		return this;
	}

	public VesselAvailabilityMaker withBallastBonus(final String fee) {
		if (fee != null) {
			vesselAvailability.setBallastBonus(fee);
		} else {
			vesselAvailability.unsetBallastBonus();
		}
		return this;
	}

	public VesselAvailabilityMaker withStartHeel(@Nullable final Double startingHeelInM3, final double cv, final double pricePerMMBTu) {

		final HeelOptions heelOptions = vesselAvailability.getStartHeel();
		if (startingHeelInM3 != null) {
			heelOptions.setVolumeAvailable(startingHeelInM3);
			heelOptions.setCvValue(cv);
			heelOptions.setPricePerMMBTU(pricePerMMBTu);
		} else {
			heelOptions.unsetVolumeAvailable();
			heelOptions.setCvValue(0.0);
			heelOptions.setPricePerMMBTU(0.0);
		}
		return this;

	}

	public VesselAvailabilityMaker withEndHeel(@Nullable final Integer targetEndHeelInM3) {

		final EndHeelOptions heelOptions = vesselAvailability.getEndHeel();
		if (targetEndHeelInM3 != null) {
			heelOptions.setTargetEndHeel(targetEndHeelInM3);
		} else {
			heelOptions.unsetTargetEndHeel();
		}
		return this;

	}

	@NonNull
	public VesselAvailabilityMaker withCharterRate(@NonNull String priceExpression) {
		vesselAvailability.setTimeCharterRate(priceExpression);
		return this;
	}

	@NonNull
	public VesselAvailability build() {

		cargoModelBuilder.getCargoModel().getVesselAvailabilities().add(vesselAvailability);

		return vesselAvailability;
	}
}

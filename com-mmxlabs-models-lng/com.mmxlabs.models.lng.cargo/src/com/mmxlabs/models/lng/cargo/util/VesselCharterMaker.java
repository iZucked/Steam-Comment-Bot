/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import java.time.LocalDateTime;
import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.EVesselTankState;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.IRepositioningFee;
import com.mmxlabs.models.lng.commercial.LumpSumRepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.SimpleRepositioningFeeContainer;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;

public class VesselCharterMaker {

	private final CargoModelBuilder cargoModelBuilder;

	@NonNull
	private final VesselCharter vesselCharter;

	public VesselCharterMaker(final CargoModelBuilder cargoModelBuilder, @NonNull final Vessel vessel, @NonNull final BaseLegalEntity entity) {
		this.cargoModelBuilder = cargoModelBuilder;
		this.vesselCharter = CargoFactory.eINSTANCE.createVesselCharter();
		this.vesselCharter.setStartHeel(CommercialFactory.eINSTANCE.createStartHeelOptions());
		this.vesselCharter.setEndHeel(CommercialFactory.eINSTANCE.createEndHeelOptions());
		this.vesselCharter.setVessel(vessel);
		this.vesselCharter.setEntity(entity);
	}

	public VesselCharterMaker withStartPort(@Nullable final Port port) {
		vesselCharter.setStartAt(port);

		return this;
	}

	public VesselCharterMaker withEndPort(@Nullable final Port port) {
		vesselCharter.getEndAt().clear();
		if (port != null) {
			vesselCharter.getEndAt().add(port);
		}

		return this;
	}

	public VesselCharterMaker withEndPorts(APortSet<Port> endPorts) {
		vesselCharter.getEndAt().clear();
		vesselCharter.getEndAt().add(endPorts);
		return this;
	}

	public VesselCharterMaker withEndPorts(Collection<APortSet<Port>> endPorts) {
		vesselCharter.getEndAt().clear();
		vesselCharter.getEndAt().addAll(endPorts);
		return this;
	}

	public VesselCharterMaker withStartWindow(@NonNull final LocalDateTime exactTime) {
		return withStartWindow(exactTime, exactTime);
	}

	public VesselCharterMaker withStartWindow(@Nullable final LocalDateTime windowStart, @Nullable final LocalDateTime windowEnd) {
		if (windowStart != null) {
			vesselCharter.setStartAfter(windowStart);
		} else {
			vesselCharter.unsetStartAfter();
		}
		if (windowEnd != null) {
			vesselCharter.setStartBy(windowEnd);
		} else {
			vesselCharter.unsetStartBy();
		}
		return this;
	}

	public VesselCharterMaker withEndWindow(@NonNull final LocalDateTime exactTime) {
		return withEndWindow(exactTime, exactTime);
	}

	public VesselCharterMaker withEndWindow(@Nullable final LocalDateTime windowStart, @Nullable final LocalDateTime windowEnd) {
		if (windowStart != null) {
			vesselCharter.setEndAfter(windowStart);
		} else {
			vesselCharter.unsetEndAfter();
		}
		if (windowEnd != null) {
			vesselCharter.setEndBy(windowEnd);
		} else {
			vesselCharter.unsetEndBy();
		}
		return this;
	}

	public VesselCharterMaker withOptionality(final boolean isOptional) {
		vesselCharter.setOptional(isOptional);
		return this;
	}

	/**
	 * Does nothing if fee is empty!
	 * If fee is not empty the repositioning fee lump sum term is added to charter contract
	 * If charter contract is null, new contained charter contract is created
	 * @param fee
	 * @return
	 */
	public VesselCharterMaker withRepositioning(final String fee) {
		if (fee != null && !fee.isEmpty()) {
			GenericCharterContract gcc = vesselCharter.getCharterOrDelegateCharterContract();
			if (gcc == null) {
				gcc = CommercialFactory.eINSTANCE.createGenericCharterContract();
				vesselCharter.setContainedCharterContract(gcc);
			}
			IRepositioningFee repositioningFee = gcc.getRepositioningFeeTerms();
			final LumpSumRepositioningFeeTerm term = CommercialFactory.eINSTANCE.createLumpSumRepositioningFeeTerm();
			term.setPriceExpression(fee);
			if (repositioningFee == null) {
				repositioningFee = CommercialFactory.eINSTANCE.createSimpleRepositioningFeeContainer();
				gcc.setRepositioningFeeTerms(repositioningFee);
			}
			if (repositioningFee instanceof SimpleRepositioningFeeContainer) {
				((SimpleRepositioningFeeContainer) repositioningFee).getTerms().add(term);
			}
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
	public VesselCharterMaker withSafeyStartHeel() {
		return withStartHeel(vesselCharter.getVessel().getSafetyHeel(), vesselCharter.getVessel().getSafetyHeel(), 22.6, "0");
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
	public VesselCharterMaker withSafeyEndHeel() {
		return withEndHeel(vesselCharter.getVessel().getSafetyHeel(), vesselCharter.getVessel().getSafetyHeel(), EVesselTankState.MUST_BE_COLD, "0");
	}

	public VesselCharterMaker withStartHeel(final double minVolumeInM3, final double maxVolumeInM3, final double cvValue, final String pricePerMMBTu) {
		if (minVolumeInM3 < 0) {
			throw new IllegalArgumentException();
		}

		if (maxVolumeInM3 < 0) {
			throw new IllegalArgumentException();
		}
		if (minVolumeInM3 > maxVolumeInM3) {
			throw new IllegalArgumentException();
		}

		vesselCharter.getStartHeel().setMinVolumeAvailable(minVolumeInM3);
		vesselCharter.getStartHeel().setMaxVolumeAvailable(maxVolumeInM3);
		vesselCharter.getStartHeel().setCvValue(cvValue);
		vesselCharter.getStartHeel().setPriceExpression(pricePerMMBTu);

		return this;

	}

	public VesselCharterMaker withEndHeel(final int minVolumeInM3, final int maxVolumeInM3, @NonNull final EVesselTankState state, @Nullable final String priceExpression) {

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

		vesselCharter.getEndHeel().setMinimumEndHeel(minVolumeInM3);
		vesselCharter.getEndHeel().setMaximumEndHeel(maxVolumeInM3);
		vesselCharter.getEndHeel().setTankState(state);
		vesselCharter.getEndHeel().setPriceExpression(priceExpression);

		return this;

	}

	public VesselCharterMaker withEndHeel(final int minVolumeInM3, final int maxVolumeInM3, @NonNull final EVesselTankState state, boolean useLastPrice) {

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

		vesselCharter.getEndHeel().setMinimumEndHeel(minVolumeInM3);
		vesselCharter.getEndHeel().setMaximumEndHeel(maxVolumeInM3);
		vesselCharter.getEndHeel().setTankState(state);
		vesselCharter.getEndHeel().setPriceExpression("");
		vesselCharter.getEndHeel().setUseLastHeelPrice(useLastPrice);

		return this;

	}

	@NonNull
	public VesselCharterMaker withCharterRate(final @NonNull String priceExpression) {
		vesselCharter.setTimeCharterRate(priceExpression);
		return this;
	}

	@NonNull
	public VesselCharterMaker withMinDuration(final int durationInDays) {
		vesselCharter.setMinDuration(durationInDays);
		return this;
	}

	@NonNull
	public VesselCharterMaker withMaxDuration(final int durationInDays) {
		vesselCharter.setMaxDuration(durationInDays);
		return this;
	}
	
	@NonNull
	public VesselCharterMaker withCharterNumber(final int charterNumber) {
		vesselCharter.setCharterNumber(charterNumber);
		return this;
	}

	@NonNull
	public VesselCharter build() {

		cargoModelBuilder.getCargoModel().getVesselCharters().add(vesselCharter);

		return vesselCharter;
	}

}

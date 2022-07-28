/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.util;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.FullVesselCharterOption;
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

public class FullCharterOptionMaker {
	@NonNull
	protected final SandboxModelBuilder builder;

	protected FullVesselCharterOption option = null;

	private VesselCharter vesselCharter;

	public FullCharterOptionMaker(@NonNull final SandboxModelBuilder builder) {
		this.builder = builder;
		this.option = AnalyticsFactory.eINSTANCE.createFullVesselCharterOption();

		vesselCharter = CargoFactory.eINSTANCE.createVesselCharter();
		vesselCharter.setStartHeel(CommercialFactory.eINSTANCE.createStartHeelOptions());
		vesselCharter.setEndHeel(CommercialFactory.eINSTANCE.createEndHeelOptions());

		this.option.setVesselCharter(vesselCharter);
	}

	@NonNull
	public FullCharterOptionMaker create(Vessel vessel, @NonNull BaseLegalEntity entity) {
		this.vesselCharter.setVessel(vessel);
		this.vesselCharter.setEntity(entity);
		return this;
	}

	public @NonNull FullVesselCharterOption build() {
		this.builder.getOptionAnalysisModel().getShippingTemplates().add(option);
		return option;
	}

	/**
	 * Generic modifier call
	 * 
	 * @param action
	 * @return
	 */
	@NonNull
	public FullCharterOptionMaker with(@NonNull Consumer<FullVesselCharterOption> action) {
		action.accept(option);
		return this;
	}

	public FullCharterOptionMaker withStartPort(@Nullable final Port port) {
		vesselCharter.setStartAt(port);

		return this;
	}

	public FullCharterOptionMaker withEndPort(@Nullable final Port port) {
		vesselCharter.getEndAt().clear();
		if (port != null) {
			vesselCharter.getEndAt().add(port);
		}

		return this;
	}

	public FullCharterOptionMaker withEndPorts(APortSet<Port> endPorts) {
		vesselCharter.getEndAt().clear();
		vesselCharter.getEndAt().add(endPorts);
		return this;
	}

	public FullCharterOptionMaker withEndPorts(Collection<APortSet<Port>> endPorts) {
		vesselCharter.getEndAt().clear();
		vesselCharter.getEndAt().addAll(endPorts);
		return this;
	}

	public FullCharterOptionMaker withStartWindow(@NonNull final LocalDateTime exactTime) {
		return withStartWindow(exactTime, exactTime);
	}

	public FullCharterOptionMaker withStartWindow(@Nullable final LocalDateTime windowStart, @Nullable final LocalDateTime windowEnd) {
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

	public FullCharterOptionMaker withEndWindow(@NonNull final LocalDateTime exactTime) {
		return withEndWindow(exactTime, exactTime);
	}

	public FullCharterOptionMaker withEndWindow(@Nullable final LocalDateTime windowStart, @Nullable final LocalDateTime windowEnd) {
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

	public FullCharterOptionMaker withOptionality(final boolean isOptional) {
		vesselCharter.setOptional(isOptional);
		return this;
	}

	public FullCharterOptionMaker withRepositioning(final String fee) {
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
	public FullCharterOptionMaker withSafeyStartHeel() {
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
	public FullCharterOptionMaker withSafeyEndHeel() {
		return withEndHeel(vesselCharter.getVessel().getSafetyHeel(), vesselCharter.getVessel().getSafetyHeel(), EVesselTankState.MUST_BE_COLD, "0");
	}

	public FullCharterOptionMaker withStartHeel(final double minVolumeInM3, final double maxVolumeInM3, final double cvValue, final String pricePerMMBTu) {
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

	public FullCharterOptionMaker withEndHeel(final int minVolumeInM3, final int maxVolumeInM3, @NonNull final EVesselTankState state, @Nullable final String priceExpression) {

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

	public FullCharterOptionMaker withEndHeel(final int minVolumeInM3, final int maxVolumeInM3, @NonNull final EVesselTankState state, boolean useLastPrice) {

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
	public FullCharterOptionMaker withCharterRate(final @NonNull String priceExpression) {
		vesselCharter.setTimeCharterRate(priceExpression);
		return this;
	}

	@NonNull
	public FullCharterOptionMaker withMinDuration(final int durationInDays) {
		vesselCharter.setMinDuration(durationInDays);
		return this;
	}

	@NonNull
	public FullCharterOptionMaker withMaxDuration(final int durationInDays) {
		vesselCharter.setMaxDuration(durationInDays);
		return this;
	}
}

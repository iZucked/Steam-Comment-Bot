/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.validation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.PartialCase;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.PartialCaseRowGroup;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.AnalyticsBuilder;
import com.mmxlabs.models.lng.analytics.util.SandboxModeConstants;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.util.SetUtils;

public class SandboxConstraintUtils {

	private SandboxConstraintUtils() {

	}

	public static boolean vesselPortRestrictionsValid(final BuyOption buy, final SellOption sell1, final @Nullable SellOption sell2, final ShippingOption shippingOption) {
		final Collection<APortSet<Port>> portRestrictions = AnalyticsBuilder.getPortRestrictions(shippingOption);
		if (!portRestrictions.isEmpty()) {
			final Set<Port> optionPorts = new HashSet<>();
			optionPorts.add(AnalyticsBuilder.getPort(buy));
			optionPorts.add(AnalyticsBuilder.getPort(sell1));
			optionPorts.add(AnalyticsBuilder.getPort(sell2));
			optionPorts.remove(null);

			if (!optionPorts.isEmpty()) {
				for (final APortSet<Port> portFromSet : portRestrictions) {
					if (SetUtils.getObjects(portFromSet).stream().anyMatch(optionPorts::contains)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public static boolean vesselRestrictionsValid(final BuyOption buy, final SellOption sell1, final @Nullable SellOption sell2, final ShippingOption shippingOption) {
		final Vessel vessel = AnalyticsBuilder.getVessel(shippingOption);
		if (vessel != null) {
			if (buy instanceof BuyReference) {
				final Pair<Boolean, Set<AVesselSet<Vessel>>> restrictedVessels = AnalyticsBuilder.getBuyVesselRestrictions(buy);
				final boolean permissive = restrictedVessels.getFirst();
				final Set<AVesselSet<Vessel>> allowedVessels = restrictedVessels.getSecond();

				if (!permissive && allowedVessels.isEmpty()) {
					// No restrictions
				} else if (SetUtils.getObjects(allowedVessels).contains(vessel) != permissive) {
					return false;
				}
			}
			if (sell1 instanceof SellReference) {
				final Pair<Boolean, Set<AVesselSet<Vessel>>> restrictedVessels = AnalyticsBuilder.getSellVesselRestrictions(sell1);
				final boolean permissive = restrictedVessels.getFirst();
				final Set<AVesselSet<Vessel>> allowedVessels = restrictedVessels.getSecond();
				if (!permissive && allowedVessels.isEmpty()) {
					// No restrictions
				} else if (SetUtils.getObjects(allowedVessels).contains(vessel) != permissive) {
					return false;
				}
			}
			if (sell2 instanceof SellReference) {
				final Pair<Boolean, Set<AVesselSet<Vessel>>> restrictedVessels = AnalyticsBuilder.getSellVesselRestrictions(sell2);
				final boolean permissive = restrictedVessels.getFirst();
				final Set<AVesselSet<Vessel>> allowedVessels = restrictedVessels.getSecond();
				if (!permissive && allowedVessels.isEmpty()) {
					// No restrictions
				} else if (SetUtils.getObjects(allowedVessels).contains(vessel) != permissive) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean portRestrictionsValid(final BuyOption buy, final SellOption sell1, final @Nullable SellOption sell2) {

		if (buy instanceof BuyReference) {
			{
				final Port port = AnalyticsBuilder.getPort(sell1);
				final Pair<Boolean, Set<APortSet<Port>>> restrictedPorts = AnalyticsBuilder.getBuyPortRestrictions(buy);
				final boolean permissive = restrictedPorts.getFirst();
				final Set<APortSet<Port>> allowedPorts = restrictedPorts.getSecond();

				if (!permissive && allowedPorts.isEmpty()) {
					// No restrictions
				} else if (SetUtils.getObjects(allowedPorts).contains(port) != permissive) {
					return false;
				}
			}
			if (sell2 != null) {
				final Port port = AnalyticsBuilder.getPort(sell2);
				final Pair<Boolean, Set<APortSet<Port>>> restrictedPorts = AnalyticsBuilder.getBuyPortRestrictions(buy);
				final boolean permissive = restrictedPorts.getFirst();
				final Set<APortSet<Port>> allowedPorts = restrictedPorts.getSecond();

				if (!permissive && allowedPorts.isEmpty()) {
					// No restrictions
				} else if (SetUtils.getObjects(allowedPorts).contains(port) != permissive) {
					return false;
				}
			}
		}
		if (sell1 instanceof SellReference) {
			final Port port = AnalyticsBuilder.getPort(buy);
			final Pair<Boolean, Set<APortSet<Port>>> restrictedPorts = AnalyticsBuilder.getSellPortRestrictions(sell1);
			final boolean permissive = restrictedPorts.getFirst();
			final Set<APortSet<Port>> allowedPorts = restrictedPorts.getSecond();
			if (!permissive && allowedPorts.isEmpty()) {
				// No restrictions
			} else if (SetUtils.getObjects(allowedPorts).contains(port) != permissive) {
				return false;
			}
		}
		if (sell2 instanceof SellReference) {
			final Port port = AnalyticsBuilder.getPort(buy);
			final Pair<Boolean, Set<APortSet<Port>>> restrictedPorts = AnalyticsBuilder.getSellPortRestrictions(sell2);
			final boolean permissive = restrictedPorts.getFirst();
			final Set<APortSet<Port>> allowedPorts = restrictedPorts.getSecond();
			if (!permissive && allowedPorts.isEmpty()) {
				// No restrictions
			} else if (SetUtils.getObjects(allowedPorts).contains(port) != permissive) {
				return false;
			}
		}
		return true;
	}

	public static boolean checkVolumeAgainstBuyAndSell(final BuyOption buy, final SellOption sell1, final @Nullable SellOption sell2) {
		if (buy == null || sell1 == null) {
			return true;
		}
		final int[] buyVolumeInMMBTU = AnalyticsBuilder.getBuyVolumeInMMBTU(buy);
		final int[] sell1VolumeInMMBTU = AnalyticsBuilder.getSellVolumeInMMBTU(buy, sell1);
		final int[] sell2VolumeInMMBTU = AnalyticsBuilder.getSellVolumeInMMBTU(buy, sell2);
		if (buyVolumeInMMBTU == null || sell1VolumeInMMBTU == null) {
			return true;
		}
		if (buyVolumeInMMBTU[0] > (sell1VolumeInMMBTU[1] + (sell2VolumeInMMBTU == null ? 0 : sell2VolumeInMMBTU[1]))) {
			return false;
		}
		return true;
	}

	public static boolean checkVolumeAgainstVessel(final BuyOption buy, final SellOption sell1, final @Nullable SellOption sell2, final ShippingOption shippingOption) {
		if (buy == null || sell1 == null || shippingOption == null) {
			return true;
		}
		final double cargoCV = AnalyticsBuilder.getCargoCV(buy);
		if (cargoCV == 0) {
			return false;
		}
		final int[] buyVolumeInMMBTU = AnalyticsBuilder.getBuyVolumeInMMBTU(buy);
		final int[] sell1VolumeInMMBTU = AnalyticsBuilder.getSellVolumeInMMBTU(buy, sell1);
		final int[] sell2VolumeInMMBTU = AnalyticsBuilder.getSellVolumeInMMBTU(buy, sell2);

		if (buyVolumeInMMBTU == null || sell1VolumeInMMBTU == null) {
			return true;
		}
		final int capacityInMMBTU = (int) ((double) AnalyticsBuilder.getVesselCapacityInM3(shippingOption) * cargoCV);
		if (buyVolumeInMMBTU[0] > capacityInMMBTU) {
			return false;
		}
		if (sell1VolumeInMMBTU[0] > capacityInMMBTU) {
			return false;
		}
		if (sell2VolumeInMMBTU != null && sell2VolumeInMMBTU[0] > capacityInMMBTU) {
			return false;
		}
		return true;
	}

	public static boolean checkCVAgainstBuyAndSell(final BuyOption buy, final SellOption sell1, final @Nullable SellOption sell2) {
		if (buy == null || sell1 == null) {
			return true;
		}
		final double cargoCV = AnalyticsBuilder.getCargoCV(buy);
		{
			final double[] cvRange = AnalyticsBuilder.getCargoCVRange(sell1);
			if (cvRange == null) {
				return true;
			}
			if (cargoCV < cvRange[0]) {
				return false;
			}
			if (cvRange[1] > 0.0 && cargoCV > cvRange[1]) {
				return false;
			}
		}
		if (sell2 != null) {
			final double[] cvRange = AnalyticsBuilder.getCargoCVRange(sell2);
			if (cvRange == null) {
				return true;
			}
			if (cargoCV < cvRange[0]) {
				return false;
			}
			if (cvRange[1] > 0.0 && cargoCV > cvRange[1]) {
				return false;
			}
		}
		return true;
	}

	public static boolean shouldValidate(@NonNull final PartialCaseRowGroup partialCaseRowGroup) {
		return isRunningUnderSandboxModes(partialCaseRowGroup, Collections.singleton(SandboxModeConstants.MODE_DEFINE));
	}

	public static boolean shouldValidate(@NonNull final PartialCaseRow partialCaseRow) {
		return isRunningUnderSandboxModes(partialCaseRow, Collections.singleton(SandboxModeConstants.MODE_DEFINE));
	}

	public static boolean shouldValidate(@NonNull final PartialCase partialCase) {
		return isRunningUnderSandboxModes(partialCase, Collections.singleton(SandboxModeConstants.MODE_DEFINE));
	}

	private static boolean isRunningUnderSandboxModes(@Nullable final EObject eObject, @NonNull final Collection<Integer> modes) {
		if (eObject == null) {
			return false;
		}
		if (eObject instanceof @NonNull final OptionAnalysisModel optionAnalysisModel && modes.contains(optionAnalysisModel.getMode())) {
			return true;
		}
		return isRunningUnderSandboxModes(eObject.eContainer(), modes);
	}

}

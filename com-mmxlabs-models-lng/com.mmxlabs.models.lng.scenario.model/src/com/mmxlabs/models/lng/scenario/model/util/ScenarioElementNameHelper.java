/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.model.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Triple;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.mmxcore.NamedObject;

/**
 * Class, intended primarily for the validation framework, to help generate consistent name/id strings
 * 
 * @author Simon Goodall
 *
 */
public class ScenarioElementNameHelper {

	private static final @NonNull String EMPTY_STRING = "";

	private static final @NonNull String TYPE_CONTRACT = "Contract";
	private static final @NonNull String TYPE_SPOT_MARKET = "Market";
	private static final @NonNull String TYPE_CARGO = "Cargo";
	private static final @NonNull String TYPE_SLOT = "Slot";
	private static final @NonNull String TYPE_VESSEL_EVENT = "Event";
	private static final @NonNull String TYPE_VESSEL_AVAILABILITY = "Availability";

	public static @NonNull String getName(final @Nullable EObject target) {
		return getName(target, "(unknown)");
	}

	public static @NonNull String getTypeName(@Nullable EObject target) {
		if (target instanceof Cargo) {
			return TYPE_CARGO;
		}
		if (target instanceof VesselEvent) {
			return TYPE_VESSEL_EVENT;
		}
		if (target instanceof VesselAvailability) {
			return TYPE_VESSEL_AVAILABILITY;
		}
		return "<Unknown>";
	}

	public static @NonNull String getName(final @Nullable EObject target, @NonNull final String defaultName) {
		if (target instanceof Cargo) {
			final Cargo cargo = (Cargo) target;
			return String.format("%s \"%s\"", TYPE_CARGO.toLowerCase(), getNonNullString(cargo.getLoadName()));
		} else if (target instanceof Slot) {
			final Slot slot = (Slot) target;
			return String.format("%s \"%s\"", TYPE_SLOT.toLowerCase(), getNonNullString(slot.getName()));
		} else if (target instanceof VesselEvent) {
			final VesselEvent vesselEvent = (VesselEvent) target;
			return String.format("%s \"%s\"", TYPE_VESSEL_EVENT.toLowerCase(), getNonNullString(vesselEvent.getName()));
		}
		return defaultName;
	}

	public static @NonNull String getNonNullString(@Nullable final String str) {
		if (str == null) {
			return EMPTY_STRING;
		}
		return str;
	}

	public static @Nullable Triple<@NonNull String, @NonNull EObject, @NonNull EStructuralFeature> getContainerName(@NonNull final LNGPriceCalculatorParameters params) {
		final EObject eContainer = params.eContainer();
		if (eContainer instanceof Contract) {
			final Contract contract = (Contract) eContainer;
			final String name = String.format("%s|'%s'", TYPE_CONTRACT, getName(contract, "Un-named contract"));
			return new Triple<>(name, eContainer, CommercialPackage.Literals.CONTRACT__PRICE_INFO);
		} else if (eContainer instanceof SpotMarket) {
			final SpotMarket spotMarket = (SpotMarket) eContainer;
			final String name = String.format("%s|'%s'", TYPE_SPOT_MARKET, getName(spotMarket, "Un-named market"));
			return new Triple<>(name, eContainer, SpotMarketsPackage.Literals.SPOT_MARKET__PRICE_INFO);
		}
		return null;
	}

	public static @NonNull String getName(@Nullable final NamedObject namedObject, @NonNull final String defaultName) {
		if (namedObject != null) {
			final String name = namedObject.getName();
			if (name != null && !name.trim().isEmpty()) {
				return name.trim();
			}
		}
		return defaultName;
	}
}

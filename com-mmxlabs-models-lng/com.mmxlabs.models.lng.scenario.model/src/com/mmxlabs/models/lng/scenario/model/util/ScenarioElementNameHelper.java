/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.model.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.base.Joiner;
import com.mmxlabs.common.Triple;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.types.APortSet;
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
	private static final @NonNull String TYPE_BUY = "Purchase";
	private static final @NonNull String TYPE_SELL = "Sale";
	private static final @NonNull String TYPE_SLOT = "Slot";
	private static final @NonNull String TYPE_PORT = "Port";
	private static final @NonNull String TYPE_VESSEL = "Vessel";
	private static final @NonNull String TYPE_VESSEL_EVENT = "Event";
	private static final @NonNull String TYPE_VESSEL_AVAILABILITY = "Charter";

	public static @NonNull String getName(final @Nullable EObject target) {
		return getName(target, "(unknown)");
	}

	public static @NonNull String getTypeName(@Nullable final EObject target) {
		if (target instanceof Cargo) {
			return TYPE_CARGO;
		}
		if (target instanceof LoadSlot) {
			return TYPE_BUY;
		}
		if (target instanceof DischargeSlot) {
			return TYPE_SELL;
		}
		if (target instanceof VesselEvent) {
			return TYPE_VESSEL_EVENT;
		}
		if (target instanceof Vessel) {
			return TYPE_VESSEL;
		}
		if (target instanceof VesselAvailability) {
			return TYPE_VESSEL_AVAILABILITY;
		}
		return "<Unknown>";
	}

	public static @NonNull String getName(final @Nullable EObject target, @NonNull final String defaultName) {
		if (target instanceof Cargo cargo) {
			return String.format("%s \"%s\"", TYPE_CARGO.toLowerCase(), getNonNullString(cargo.getLoadName()));
		} else if (target instanceof Slot slot) {
			return String.format("%s \"%s\"", TYPE_SLOT.toLowerCase(), getNonNullString(slot.getName()));
		} else if (target instanceof VesselEvent vesselEvent) {
			return String.format("%s \"%s\"", TYPE_VESSEL_EVENT.toLowerCase(), getNonNullString(vesselEvent.getName()));
		} else if (target instanceof APortSet<?> port) {
			return String.format("%s \"%s\"", TYPE_PORT.toLowerCase(), getNonNullString(port.getName()));
		} else if (target instanceof NamedObject namedObject) {
			return String.format("%s \"%s\"", getTypeName(namedObject).toLowerCase(), getNonNullString(namedObject.getName()));
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

	public static < U extends NamedObject> @NonNull String getName(final @Nullable Collection<U> namedObjects, final @NonNull String defaultName) {
		if (namedObjects == null || namedObjects.isEmpty()) {
			return defaultName;
		}
		final List<String> parts = new LinkedList<>();
		for (final NamedObject v : namedObjects) {
			parts.add(v.getName());
		}

		return Joiner.on(", ").join(parts);
	}

	public static int safeCompareNamedObjects(@NonNull final NamedObject a, @NonNull final NamedObject b, final @NonNull String defaultName) {
		return getName(a, defaultName).compareTo(getName(b, defaultName));
	}
}

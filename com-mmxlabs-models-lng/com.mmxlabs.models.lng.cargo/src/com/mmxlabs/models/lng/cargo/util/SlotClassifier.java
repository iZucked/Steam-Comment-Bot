package com.mmxlabs.models.lng.cargo.util;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.types.PortCapability;

/**
 * 
 * @author proshun
 * @noinstantiate This class is not intended to be instantiated by clients.
 */
public final class SlotClassifier {

	public static enum SlotType {
		FOB_Buy, DES_Buy, DES_Buy_AnyDisPort, DES_Sale, FOB_Sale, FOB_Sale_AnyLoadPort
	};

	public static @NonNull
	SlotType classify(@NonNull final Slot slot) {

		final Collection<PortCapability> caps = slot.getPort() == null ? Collections.<PortCapability>emptySet() : slot.getPort().getCapabilities();
		if (slot instanceof LoadSlot) {
			// "It's a buy!"
			final LoadSlot load = (LoadSlot) slot;
			if (load.isDESPurchase()) {
				if (!caps.contains(PortCapability.DISCHARGE)) {
					// DES purchase at a load port, so will go to some discharge port.
					return SlotType.DES_Buy_AnyDisPort;
				} else {
					return SlotType.DES_Buy;
					// NOTE: If also a LOAD port (i.e. reload), this may not be true - however, very unlikely to be buying DES for redirection at a reload port...
				}
			} else {
				return SlotType.FOB_Buy;
			}
		} else if (slot instanceof DischargeSlot) {
			// It's a sell...
			final DischargeSlot dis = (DischargeSlot) slot;
			if (dis.isFOBSale()) {
				if (slot.getPort().getCapabilities().contains(PortCapability.LOAD)) {
					// FOB sale at a load port, so fixed.
					return SlotType.FOB_Sale;
				} else {
					// This is speculative ATOW, Nov 2013
					return SlotType.FOB_Sale_AnyLoadPort;
				}
			} else {
				return SlotType.DES_Sale;
			}
		}
		throw new IllegalArgumentException("Slot date overlap constraint: Could not classify slot as LOAD/DISCHARGE");
	}

}

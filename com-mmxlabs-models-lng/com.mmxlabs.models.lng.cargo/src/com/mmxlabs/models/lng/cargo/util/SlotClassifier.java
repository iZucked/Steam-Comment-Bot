/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.util;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;

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

		if (slot instanceof LoadSlot) {
			// "It's a buy!"
			final LoadSlot load = (LoadSlot) slot;
			if (load.isDESPurchase()) {
				if (load.isDivertible()) {
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
				if (dis.isDivertible()) {
					// This is speculative ATOW, Nov 2013
					return SlotType.FOB_Sale_AnyLoadPort;
				} else {
					// FOB sale at a load port, so fixed.
					return SlotType.FOB_Sale;
				}
			} else {
				return SlotType.DES_Sale;
			}
		}
		throw new IllegalArgumentException("Slot date overlap constraint: Could not classify slot as LOAD/DISCHARGE");
	}

}

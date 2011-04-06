/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.autocorrect;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edit.domain.EditingDomain;

import scenario.cargo.CargoPackage;
import scenario.cargo.Slot;
import com.mmxlabs.common.Pair;

public class SlotVolumeCorrector extends BaseCorrector {
	final static EAttribute minQuantity = CargoPackage.eINSTANCE
			.getSlot_MinQuantity();
	final static EAttribute maxQuantity = CargoPackage.eINSTANCE
			.getSlot_MaxQuantity();

	@Override
	public Pair<String, Command> correct(final Notification notification,
			final EditingDomain editingDomain) {
		final Object feature = notification.getFeature();
		final Object target = notification.getNotifier();
		if (minQuantity.equals(feature)) {
			final Slot slot = (Slot) target;
			// min has changed
			if (slot.getMaxQuantity() < slot.getMinQuantity()) {
				return new Pair<String, Command>(
						"Adjust maximum quantity to match minimum quantity",
						makeSetter(editingDomain, slot, maxQuantity,
								slot.getMinQuantity()));
			}
		} else if (maxQuantity.equals(feature)) {
			final Slot slot = (Slot) target;
			if (slot.getMaxQuantity() < slot.getMinQuantity()) {
				return new Pair<String, Command>(
						"Adjust minimum quantity to match maximum quantity",
						makeSetter(editingDomain, slot, minQuantity,
								slot.getMaxQuantity()));
			}
		}
		return null;
	}
}

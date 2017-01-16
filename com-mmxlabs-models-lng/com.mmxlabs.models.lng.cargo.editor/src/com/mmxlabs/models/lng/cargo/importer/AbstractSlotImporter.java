/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.importer;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.SlotContractParams;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

/**
 */
public class AbstractSlotImporter extends DefaultClassImporter {
	@Override
	protected boolean shouldImportReference(final EReference reference) {
		if (reference == CargoPackage.eINSTANCE.getSlot_Cargo())
			return false;
		return super.shouldImportReference(reference);
	}

	@Override
	protected boolean shouldExportFeature(final EStructuralFeature feature) {
		if (feature == CargoPackage.eINSTANCE.getSlot_Cargo())
			return false;
		return super.shouldExportFeature(feature);
	}

	@Override
	protected boolean shouldExportExtension(final EObject object, final EObject extension) {

		if (object instanceof Slot) {
			final Slot slot = (Slot) object;
			if (extension instanceof SlotContractParams) {
				final SlotContractParams slotContractParams = (SlotContractParams) extension;
				final SlotContractParams expectedSlotContractParams = slot.getSlotContractParams();
				return expectedSlotContractParams == slotContractParams;
			}
		}

		return super.shouldExportExtension(object, extension);
	}
}

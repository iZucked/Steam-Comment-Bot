package com.mmxlabs.models.lng.cargo.importer;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;

public class LoadSlotImporter extends DefaultClassImporter {
	@Override
	protected boolean shouldImportReference(final EReference reference) {
		if (reference == CargoPackage.eINSTANCE.getLoadSlot_Cargo())
			return false;
		return super.shouldImportReference(reference);
	}

	@Override
	protected boolean shouldExportFeature(EStructuralFeature feature) {
		if (feature == CargoPackage.eINSTANCE.getLoadSlot_Cargo())
			return false;
		return super.shouldExportFeature(feature);
	}
}

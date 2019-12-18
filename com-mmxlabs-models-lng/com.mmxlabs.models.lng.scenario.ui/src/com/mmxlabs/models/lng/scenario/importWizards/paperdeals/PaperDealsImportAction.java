/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.paperdeals;

import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.ui.actions.SimpleImportAction;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.IClassImporter;

public class PaperDealsImportAction extends SimpleImportAction {
	
	public PaperDealsImportAction(final ImportHooksProvider iph, final CargoModel container) {
		super(iph, container, CargoPackage.Literals.CARGO_MODEL__PAPER_DEALS);
	}

	@Override
	protected IClassImporter getImporter(final EReference containment) {
		IClassImporter importer = Activator.getDefault().getImporterRegistry().getClassImporter(CargoPackage.eINSTANCE.getPaperDeal());
		return importer;
	}
}

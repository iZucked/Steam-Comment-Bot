/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.nominations;

import com.mmxlabs.models.lng.nominations.NominationsModel;
import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.ui.actions.SimpleImportAction;

public class NominationsImportAction extends SimpleImportAction {

	public NominationsImportAction(final ImportHooksProvider iph, final NominationsModel container) {
		super(iph, container, NominationsPackage.Literals.NOMINATIONS_MODEL__NOMINATIONS);
	}
	
//Use the default importer, equivalent to:
//	@Override
//	protected IClassImporter getImporter(final EReference containment) {
//		IClassImporter importer = Activator.getDefault().getImporterRegistry().getClassImporter(NominationsPackage.eINSTANCE.getAbstractNomination());
//		return importer;
//	}
}

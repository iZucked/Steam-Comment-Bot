/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards.nominations;

import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.nominations.NominationsModel;
import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.nominations.importer.NominationsClassImporter;
import com.mmxlabs.models.lng.ui.actions.SimpleImportAction;
import com.mmxlabs.models.util.importer.IClassImporter;

public class NominationsImportAction extends SimpleImportAction {

	private NominationsModel nominationsModel;
	
	public NominationsImportAction(final ImportHooksProvider iph, final NominationsModel container) {
		super(iph, container, NominationsPackage.Literals.NOMINATIONS_MODEL__NOMINATIONS);
		this.nominationsModel = container;
	}
	
	@Override
	protected IClassImporter getImporter(final EReference containment) {
		return new NominationsClassImporter(this.nominationsModel);
	}
}

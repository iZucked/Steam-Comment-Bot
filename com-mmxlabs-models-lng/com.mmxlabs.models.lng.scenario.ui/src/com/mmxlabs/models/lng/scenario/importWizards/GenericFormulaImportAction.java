/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.pricing.importers.FormulaImporter;
import com.mmxlabs.models.lng.ui.actions.SimpleImportAction;
import com.mmxlabs.models.util.importer.IClassImporter;

/**
 * @author FM
 * 
 */
public class GenericFormulaImportAction extends SimpleImportAction {
	final boolean multipleDataTypes;
	
	public GenericFormulaImportAction(final ImportHooksProvider iph, final EObject container, final EReference containment, final boolean multipleDataTypes) {
		super(iph, container, containment);
		this.multipleDataTypes = multipleDataTypes;
	}
	
	@Override
	protected IClassImporter getImporter(final EReference containment) {
		FormulaImporter importer = new FormulaImporter();
		importer.setMultipleDataTypeInput(multipleDataTypes);
		return importer;
	}

	
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.pricing.importers.GenericIndexImporter;
import com.mmxlabs.models.lng.ui.actions.SimpleImportAction;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.IClassImporter;

/**
 * @author achurchill
 * 
 */
public class GenericIndexImportAction extends SimpleImportAction {
	final boolean multipleDataTypes;
	
	public GenericIndexImportAction(final ImportHooksProvider iph, final EObject container, final EReference containment, final boolean multipleDataTypes) {
		super(iph, container, containment);
		this.multipleDataTypes = multipleDataTypes;
	}
	
	@Override
	protected IClassImporter getImporter(final EReference containment) {
		GenericIndexImporter<?> importer = (GenericIndexImporter<?>) Activator.getDefault().getImporterRegistry().getClassImporter(containment.getEReferenceType());
		importer.setMultipleDataTypeInput(multipleDataTypes);
		return importer;
	}

	
}

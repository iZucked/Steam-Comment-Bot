/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.importer.importers;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * Stub to prevent export of fuel consumption lines.
 * @author Tom Hinton
 *
 */
public class VesselStateAttributesImporter extends EObjectImporter {

	@Override
	protected void flattenMultiContainment(EObject object, String prefix,
			EReference reference, Map<String, String> output) {
		// TODO Auto-generated method stub
//		super.flattenMultiContainment(object, prefix, reference, output);
	}

}

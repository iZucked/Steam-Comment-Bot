/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.importer;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;

import scenario.cargo.Cargo;

/**
 * @author Tom Hinton
 *
 */
public class CargoImporter extends EObjectImporter {

	@Override
	public EObject importObject(Map<String, String> fields,
			Collection<DeferredReference> deferredReferences,
			NamedObjectRegistry registry) {
		final Cargo c = (Cargo) super.importObject(fields, deferredReferences, registry);
		
//		if (c.getLoadSlot().getId().isEmpty()) {
			c.getLoadSlot().setId("load-" + c.getId());
//		}
		
//		if (c.getDischargeSlot().getId().isEmpty()) {
			c.getDischargeSlot().setId("discharge-" + c.getId());
//		}
		
		return c;
	}
	
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.importer.importers;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import scenario.cargo.Cargo;
import scenario.cargo.CargoPackage;
import scenario.contract.ContractPackage;

/**
 * @author Tom Hinton
 * 
 */
public class CargoImporter extends EObjectImporter {

	@Override
	public EObject importObject(final Map<String, String> fields, final Collection<DeferredReference> deferredReferences, final NamedObjectRegistry registry) {
		final Cargo c = (Cargo) super.importObject(fields, deferredReferences, registry);

		// if (c.getLoadSlot().getId().isEmpty()) {
		c.getLoadSlot().setId("load-" + c.getId());
		// }

		// if (c.getDischargeSlot().getId().isEmpty()) {
		c.getDischargeSlot().setId("discharge-" + c.getId());
		// }

		return c;
	}

	@Override
	protected void populateReference(final String prefix, final EObject target, final EReference reference, final Map<String, String> fields, final Collection<DeferredReference> deferredReferences) {
		if (reference == CargoPackage.eINSTANCE.getSlot_Contract()) {
			final String referenceName = prefix + reference.getName().toLowerCase();
			if (fields.containsKey(referenceName)) {
				final String value = fields.get(referenceName);

				if (target.eClass() == CargoPackage.eINSTANCE.getSlot()) {
					// discharge slot has a sales contract
					deferredReferences.add(new DeferredReference(target, reference, ContractPackage.eINSTANCE.getSalesContract(), value.trim()));
				} else if (target.eClass() == CargoPackage.eINSTANCE.getLoadSlot()) {
					// cargo package has a purchase contract
					deferredReferences.add(new DeferredReference(target, reference, ContractPackage.eINSTANCE.getPurchaseContract(), value.trim()));
				}
			}
		}
		super.populateReference(prefix, target, reference, fields, deferredReferences);
	}

}

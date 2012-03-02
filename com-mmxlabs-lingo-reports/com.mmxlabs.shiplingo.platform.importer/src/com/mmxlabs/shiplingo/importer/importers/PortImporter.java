/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.importer.importers;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import scenario.contract.Contract;
import scenario.contract.ContractPackage;
import scenario.port.Port;

import com.mmxlabs.common.Pair;

/**
 * A custom port importer which adds backwards compatibility for default port.
 * 
 * Does not export default contract field however.
 * 
 * @author Tom Hinton
 * 
 */
public class PortImporter extends EObjectImporter {
	@Override
	public EObject importObject(final Map<String, String> fields, final Collection<DeferredReference> deferredReferences, final NamedObjectRegistry registry) {
		final Port p = (Port) super.importObject(fields, deferredReferences, registry);

		// check for old defaultcontract field
		if (fields.containsKey("defaultcontract")) {
			final String contractName = fields.get("defaultcontract");

			// add a custom deferred reference which first looks up the contract
			// and then executes the linkage.
			deferredReferences.add(new DeferredReference(null, ContractPackage.eINSTANCE.getContract_DefaultPorts(), p.getName()) {
				@Override
				public void run() {
					final EObject o = registry.get(new Pair<EClass, String>(ContractPackage.eINSTANCE.getContract(), contractName));
					if (o instanceof Contract) {
						this.target = o;
						super.run();
					}
				}
			});
		}

		return p;
	}

}

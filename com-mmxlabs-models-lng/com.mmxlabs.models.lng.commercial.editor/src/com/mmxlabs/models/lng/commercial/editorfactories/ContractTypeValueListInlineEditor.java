/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.editorfactories;

import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.ContractType;

/**
 * @since 2.0
 */

public class ContractTypeValueListInlineEditor extends AbstractValueListInlineEditor {
	public ContractTypeValueListInlineEditor(final EStructuralFeature feature) {
		super(feature, CommercialPackage.eINSTANCE.getContractType().getELiterals());

		for (final ContractType type : ContractType.values()) {
			final String name;
			switch (type) {
			case BOTH:
				name = "Any";
				break;
			case FOB:
				name = "FOB";
				break;
			case DES:
				name = "DES";
				break;
			default:
				name = type.getName();
				break;

			}
			names.add(name);
			values.add(type);
		}

	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.valueproviders;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.BaseReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

public class SubContractProfileValueProviderFactory implements IReferenceValueProviderFactory {

	@Override
	public IReferenceValueProvider createReferenceValueProvider(final EClass owner, final EReference reference, final MMXRootObject rootObject) {

		return new BaseReferenceValueProvider() {

			@Override
			public List<Pair<String, EObject>> getAllowedValues(EObject target, EStructuralFeature field) {

				if (target instanceof ContractProfile<?, ?>) {
					ContractProfile<?, ?> contractProfile = (ContractProfile<?, ?>) target;

					List<Pair<String, EObject>> values = new LinkedList<>();
					for (SubContractProfile<?, ?> subProfile : contractProfile.getSubProfiles()) {
						values.add(new Pair<>(subProfile.getName(), subProfile));
					}
					return values;
				}

				return new LinkedList<>();
			}

			@Override
			public void dispose() {

			}

			@Override
			protected void cacheValues() {

			}
		};
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.valueproviders;

import java.util.Collection;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.valueproviders.AllowedFieldFilteredReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

public class PreferredPortProviderFactory implements IReferenceValueProviderFactory {
	private final IReferenceValueProviderFactory delegateFactory;

	public PreferredPortProviderFactory() {
		this.delegateFactory = Activator.getDefault().getReferenceValueProviderFactoryRegistry().getValueProviderFactory(EcorePackage.eINSTANCE.getEClass(), PortPackage.eINSTANCE.getPort());
	}

	@Override
	public IReferenceValueProvider createReferenceValueProvider(EClass owner, EReference reference, MMXRootObject rootObject) {
		IReferenceValueProvider delegate = delegateFactory.createReferenceValueProvider(owner, reference, rootObject);

		return new AllowedFieldFilteredReferenceValueProvider<Port>(delegate) {

			@Override
			protected Collection<Port> getAllowedValuesFromField(EObject target, EStructuralFeature field) {
				return SetUtils.getObjects(((Contract) target).getAllowedPorts());
			}

			@Override
			protected Port getCurrentValue(EObject target, EStructuralFeature field) {
				return ((Contract) target).getPreferredPort();
			}

			@Override
			protected boolean fieldValueIncludesObject(Port  fieldValue, Port  queryValue) {
				final UniqueEList<APortSet<Port>> marks = new UniqueEList<APortSet<Port>>();
				return fieldValue.collect(marks).contains(queryValue) && false;
			}

			@Override
			public boolean updateOnChangeToFeature(Object changedFeature) {
				if (changedFeature == CommercialPackage.eINSTANCE.getContract_AllowedPorts()) {
					return true;
				}

				return super.updateOnChangeToFeature(changedFeature);
			}
		};
	}

}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.valueproviders;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypedElement;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.BusinessUnit;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

/**
 * 
 * @author FM
 *
 */
public class BusinessUnitReferenceValueProviderFactory implements IReferenceValueProviderFactory {

	@Override
	public IReferenceValueProvider createReferenceValueProvider(EClass owner, EReference reference, MMXRootObject rootObject) {
		if (rootObject instanceof final LNGScenarioModel lngScenarioModel) {
			final CommercialModel model = ScenarioModelUtil.getCommercialModel(lngScenarioModel);
			if (model != null) {
				return new IReferenceValueProvider() {

					@Override
					public List<Pair<String, EObject>> getAllowedValues(EObject target, ETypedElement field) {
						final List<Pair<String, EObject>> results = new LinkedList<>();
						BaseLegalEntity entity = null;
						
						if ((target instanceof final Contract contract) && (field == CommercialPackage.eINSTANCE.getContract_BusinessUnit())) {
								entity = contract.getEntity();
						}
						if (entity != null) {
							for (final BusinessUnit bu : entity.getBusinessUnits()) {
								results.add(Pair.of(bu.getName(), bu));
							}
						}
						return results;
					}

					@Override
					public String getName(EObject referer, EReference feature, EObject referenceValue) {
						if (referenceValue instanceof final NamedObject no) {
							return no.getName();
						}
						return referenceValue.toString();
					}

					@Override
					public Iterable<Pair<Notifier, List<Object>>> getNotifiers(EObject referer, EReference feature, EObject referenceValue) {
						return null;
					}

					@Override
					public boolean updateOnChangeToFeature(Object changedFeature) {
						return changedFeature == CommercialPackage.eINSTANCE.getContract_BusinessUnit();
					}

					@Override
					public void dispose() {
					}
					
				};
			}
		}
		return null;
	}

}


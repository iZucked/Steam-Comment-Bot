/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transfers.ui.valueproviders;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypedElement;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transfers.TransferModel;
import com.mmxlabs.models.lng.transfers.TransferRecord;
import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.SimpleReferenceValueProvider;

/**
 * Provides a list of all the discharge slots to select a transferred discharge slot
 * @author FM
 *
 */
public class RHSReferenceValueProviderFactory implements IReferenceValueProviderFactory {

	@Override
	public IReferenceValueProvider createReferenceValueProvider(EClass owner, EReference reference, MMXRootObject rootObject) {

		if (rootObject instanceof LNGScenarioModel lngScenarioModel) {
			final TransferModel model = ScenarioModelUtil.getTransferModel(lngScenarioModel);
			if (model != null) {
				final EClass referenceClass = reference.getEReferenceType();
				if (referenceClass == TransfersPackage.eINSTANCE.getTransferRecord_Rhs().getEReferenceType()) {
					return new SimpleReferenceValueProvider(model, TransfersPackage.eINSTANCE.getTransferModel_TransferRecords()) {
						@Override
						public List<Pair<String, EObject>> getAllowedValues(final EObject target, final ETypedElement field) {
							final List<Pair<String, EObject>> superValues = super.getAllowedValues(target, field);

							if (target instanceof TransferRecord thisRecord) {
								return superValues.stream()
								.filter(pair -> {
									if (pair.getSecond() instanceof TransferRecord filterValue) {
										final String filterValueName = filterValue.getName();
										return filterValueName != null &&
												!filterValueName.isEmpty() &&
												!filterValueName.equals(thisRecord.getName());
									}
									return false;
								})
								.collect(Collectors.toList());
							}
							
							return superValues;
						}
					};
				}
			}
		}
		
		return null;
	}

}

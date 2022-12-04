/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transfers.ui.valueproviders;

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
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transfers.TransferAgreement;
import com.mmxlabs.models.lng.transfers.TransferModel;
import com.mmxlabs.models.lng.transfers.TransferRecord;
import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;

/**
 * Provides the list of the Business Units for the Entities on the Transfer Agreement
 * @author FM
 *
 */
public class BusinessUnitReferenceValueProviderFactory implements IReferenceValueProviderFactory {

	@Override
	public IReferenceValueProvider createReferenceValueProvider(EClass owner, EReference reference, MMXRootObject rootObject) {

		if (rootObject instanceof LNGScenarioModel lngScenarioModel) {
			final TransferModel model = ScenarioModelUtil.getTransferModel(lngScenarioModel);
			if (model != null) {
					return new IReferenceValueProvider() {

						@Override
						public List<Pair<String, EObject>> getAllowedValues(EObject target, ETypedElement field) {
							final List<Pair<String, EObject>> results = new LinkedList<>();
							BaseLegalEntity entity = null;
							if (target instanceof TransferAgreement ta) {
								//final BaseLegalEntity entity;
								if (field == TransfersPackage.eINSTANCE.getTransferAgreement_FromBU()) {
									entity = ta.getFromEntity();
								} else {
									entity = ta.getToEntity();
								}
							} else if (target instanceof TransferRecord tr) {
								if (field == TransfersPackage.eINSTANCE.getTransferRecord_FromBU()) {
									entity = tr.getFromEntity();
								} else {
									entity = tr.getToEntity();
								}
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
							if (referenceValue instanceof NamedObject no) {
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
							return changedFeature == TransfersPackage.eINSTANCE.getTransferAgreement_FromEntity() //
									|| changedFeature == TransfersPackage.eINSTANCE.getTransferAgreement_ToEntity()//
									|| changedFeature == TransfersPackage.eINSTANCE.getTransferRecord_TransferAgreement();
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

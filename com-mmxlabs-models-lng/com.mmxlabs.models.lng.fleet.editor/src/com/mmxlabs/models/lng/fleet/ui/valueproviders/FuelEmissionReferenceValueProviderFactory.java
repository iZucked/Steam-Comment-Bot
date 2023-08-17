package com.mmxlabs.models.lng.fleet.ui.valueproviders;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.fleet.CIIReferenceData;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.SimpleReferenceValueProvider;

public class FuelEmissionReferenceValueProviderFactory implements IReferenceValueProviderFactory {

	@Override
	public IReferenceValueProvider createReferenceValueProvider(EClass owner, EReference reference, MMXRootObject rootObject) {
		if (rootObject instanceof final LNGScenarioModel lngScenarioModel) {
			final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(lngScenarioModel);
			if (reference.getEReferenceType() == FleetPackage.eINSTANCE.getFuelEmissionReference()) {
				final CIIReferenceData referenceData = fleetModel.getCiiReferences();
				if (referenceData != null) {
				return new SimpleReferenceValueProvider(referenceData, FleetPackage.eINSTANCE.getCIIReferenceData_FuelEmissions()) {
					protected Pair<String, EObject> getEmptyObject() {
						return new Pair<>("<Not specified>", null);
					}
				};
				}
			}
		}
		return null;
	}

}

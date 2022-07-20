package com.mmxlabs.models.lng.transfers.ui.valueproviders;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transfers.TransferModel;
import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderFactory;
import com.mmxlabs.models.ui.valueproviders.SimpleReferenceValueProvider;

public class LHSReferenceValueProviderFactory implements IReferenceValueProviderFactory {
	
	@Override
	public IReferenceValueProvider createReferenceValueProvider(EClass owner, EReference reference, MMXRootObject rootObject) {

		if (rootObject instanceof LNGScenarioModel lngScenarioModel) {
			final TransferModel model = ScenarioModelUtil.getTransferModel(lngScenarioModel);
			if (model != null) {
				final EClass referenceClass = reference.getEReferenceType();
				if (referenceClass == TransfersPackage.eINSTANCE.getTransferRecord_Lhs().getEReferenceType()) {
					final CargoModel cm = lngScenarioModel.getCargoModel();
					return new SimpleReferenceValueProvider(cm, CargoPackage.eINSTANCE.getCargoModel_LoadSlots());
				}
			}
		}
		
		return null;
	}

}

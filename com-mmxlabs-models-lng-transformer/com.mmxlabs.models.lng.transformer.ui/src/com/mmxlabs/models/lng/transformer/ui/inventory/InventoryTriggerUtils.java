/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.inventory;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transfers.TransferModel;
import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.mmxcore.NamedObject;

public class InventoryTriggerUtils {
	
	public static List<String> getUsedNames(final LNGScenarioModel model){
		final List<String> names = new LinkedList<>();
		
		for (final EObject obj : collectRequiredObjects(model)) {
			if (ooi.contains(obj.eClass())) {
				if (obj instanceof NamedObject no) {
					names.add(no.getName());
				}
			}
		}
		
		return names;
	}
	
	private static List<EObject> collectRequiredObjects(final LNGScenarioModel scenarioModel) {
		final List<EObject> data = new LinkedList<>();
		
		final CargoModel cargoModel = scenarioModel.getCargoModel();
		data.addAll(cargoModel.getLoadSlots());
		data.addAll(cargoModel.getDischargeSlots());
		data.addAll(cargoModel.getVesselCharters());
		
		final TransferModel transferModel = scenarioModel.getTransferModel();
		data.addAll(transferModel.getTransferAgreements());
		data.addAll(transferModel.getTransferRecords());
		
		final CommercialModel commercialModel = scenarioModel.getReferenceModel().getCommercialModel();
		data.addAll(commercialModel.getPurchaseContracts());
		data.addAll(commercialModel.getSalesContracts());
		
		return data;
	}

	private static List<EClass> ooi = List.of(CargoPackage.eINSTANCE.getLoadSlot(), CargoPackage.eINSTANCE.getDischargeSlot(), //
			CommercialPackage.eINSTANCE.getPurchaseContract(), CommercialPackage.eINSTANCE.getSalesContract(), //
			CargoPackage.eINSTANCE.getVesselCharter(), TransfersPackage.eINSTANCE.getTransferAgreement(), TransfersPackage.eINSTANCE.getTransferRecord());
}

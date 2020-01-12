/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;

import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV112 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 111;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 112;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {

		EPackage package_LNGTypes = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_LNGTypes);

		EEnum desDealType = MetamodelUtils.getEEnum(package_LNGTypes, "DESPurchaseDealType");
		EEnumLiteral DES_DEST_ONLY = MetamodelUtils.getEEnum_Literal(desDealType, "DEST_ONLY");
		EEnumLiteral DES_DIVERT_FROM_SOURCE = MetamodelUtils.getEEnum_Literal(desDealType, "DIVERT_FROM_SOURCE");
		EEnum fobDealType = MetamodelUtils.getEEnum(package_LNGTypes, "FOBSaleDealType");
		EEnumLiteral FOB_SOURCE_ONLY = MetamodelUtils.getEEnum_Literal(fobDealType, "SOURCE_ONLY");
		EEnumLiteral FOB_DIVERT_TO_DEST = MetamodelUtils.getEEnum_Literal(fobDealType, "DIVERT_TO_DEST");

		EObjectWrapper referenceModel = modelRecord.getModelRoot().getRef("referenceModel");
		EObjectWrapper commercialModel = referenceModel.getRef("commercialModel");
		EObjectWrapper cargoModel = modelRecord.getModelRoot().getRef("cargoModel");

		List<EObjectWrapper> purchaseContracts = commercialModel.getRefAsList("purchaseContracts");
		if (purchaseContracts != null) {
			for (EObjectWrapper c : purchaseContracts) {
				if (c.getAttribAsBoolean("divertible")) {
					c.setAttrib("desPurchaseDealType", DES_DIVERT_FROM_SOURCE);
				}
				c.unsetFeature("divertible");
			}
		}
		List<EObjectWrapper> salesContracts = commercialModel.getRefAsList("salesContracts");
		if (salesContracts != null) {
			for (EObjectWrapper c : salesContracts) {
				if (c.getAttribAsBoolean("divertible")) {
					c.setAttrib("fobSaleDealType", FOB_DIVERT_TO_DEST);
				}
				c.unsetFeature("divertible");
			}
		}
		List<EObjectWrapper> loadSlots = cargoModel.getRefAsList("loadSlots");
		if (loadSlots != null) {
			for (EObjectWrapper c : loadSlots) {
				if (c.isSetFeature("divertible")) {
					if (c.getAttribAsBoolean("divertible")) {
						c.setAttrib("desPurchaseDealType", DES_DIVERT_FROM_SOURCE);
					} else {
						c.setAttrib("desPurchaseDealType", DES_DEST_ONLY);
					}
					c.unsetFeature("divertible");
				}
			}
		}
		List<EObjectWrapper> dischargeSlots = cargoModel.getRefAsList("dischargeSlots");
		if (dischargeSlots != null) {
			for (EObjectWrapper c : dischargeSlots) {
				if (c.isSetFeature("divertible")) {
					if (c.getAttribAsBoolean("divertible")) {
						c.setAttrib("fobSaleDealType", FOB_DIVERT_TO_DEST);
					} else {
						c.setAttrib("fobSaleDealType", FOB_SOURCE_ONLY);
					}
					c.unsetFeature("divertible");
				}
			}
		}
	}
}

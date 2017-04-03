/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV68 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 67;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 68;
	}

	@Override
	protected void doMigrationWithHelper(final MetamodelLoader loader, final EObjectWrapper model) {
		final EObjectWrapper cargoModel = model.getRef("cargoModel");
		EPackage commercial_package = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CommercialModel);
		EClass contract_class = MetamodelUtils.getEClass(commercial_package, "RuleBasedBallastBonusContract");
		EClass contract_line = MetamodelUtils.getEClass(commercial_package, "LumpSumBallastBonusContractLine");

		if (cargoModel == null) {
			return;
		}
		List<EObjectWrapper> vesselAvailabilities = cargoModel.getRefAsList("vesselAvailabilities");
		for (EObjectWrapper va: vesselAvailabilities) {
			String ballastBonus = va.getAttrib("ballastBonus");
			if (ballastBonus != null && !ballastBonus.isEmpty()) {
				// we have a ballast bonus
				va.unsetFeature("ballastBonus");
				EObjectWrapper contract_instance = (EObjectWrapper) commercial_package.getEFactoryInstance().create(contract_class);
				EObjectWrapper rule = (EObjectWrapper) commercial_package.getEFactoryInstance().create(contract_line);
				rule.setAttrib("priceExpression", ballastBonus);
				LinkedList<EObject> rules = new LinkedList<>();
				rules.add(rule);
				contract_instance.setRef("rules", rules);
				va.setRef("ballastBonusContract", contract_instance);
 
			}
		}
	}
}

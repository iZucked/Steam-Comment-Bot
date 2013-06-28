/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.MetamodelVersionsUtil;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.PackageData;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

/**
 * @since 5.0
 */
public class MigrateToV3 extends AbstractMigrationUnit {

	private MetamodelLoader destiniationLoader;
	private MetamodelLoader sourceLoader;

	@Override
	public String getContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getSourceVersion() {
		return 2;
	}

	@Override
	public int getDestinationVersion() {
		return -3;
	}

	@Override
	protected MetamodelLoader getSourceMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (sourceLoader == null) {
			sourceLoader = MetamodelVersionsUtil.createV2Loader(extraPackages);
		}
		return sourceLoader;
	}

	@Override
	protected MetamodelLoader getDestinationMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (destiniationLoader == null) {
			destiniationLoader = MetamodelVersionsUtil.createV3Loader(extraPackages);
		}
		return destiniationLoader;
	}

	@Override
	protected void doMigration(final EObject model) {

		// This should get the cached loader instance
		final MetamodelLoader loader = getDestinationMetamodelLoader(null);

		migrateFOBSales(loader, model);
	}

	private void migrateFOBSales(MetamodelLoader loader, EObject model) {

		final EPackage scenarioModelPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
		final EClass lngScenarioModel_Class = MetamodelUtils.getEClass(scenarioModelPackage, "LNGScenarioModel");
		final EReference lngScenarioModel_spotMarketsModel_Reference = MetamodelUtils.getReference(lngScenarioModel_Class, "spotMarketsModel");

		final EPackage spotMarketsPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_SpotMarketsModel);

		final EClass spotMarketsModel_Class = MetamodelUtils.getEClass(spotMarketsPackage, "SpotMarketsModel");
		final EClass SpotMarketGroup_Class = MetamodelUtils.getEClass(spotMarketsPackage, "SpotMarketGroup");
		final EClass FOBSalesMarket_Class = MetamodelUtils.getEClass(spotMarketsPackage, "FOBSalesMarket");

		final EReference FOBSalesMarket_loadPort = MetamodelUtils.getReference(FOBSalesMarket_Class, "loadPort");
		final EReference FOBSalesMarket_originPorts = MetamodelUtils.getReference(FOBSalesMarket_Class, "originPorts");

		final EReference spotMarketsModel_fobSales_group = MetamodelUtils.getReference(spotMarketsModel_Class, "fobSalesSpotMarket");
		final EReference spotMarketsGroup_markets = MetamodelUtils.getReference(SpotMarketGroup_Class, "markets");

		EObject spotMarketsModel = (EObject) model.eGet(lngScenarioModel_spotMarketsModel_Reference);
		EObject group = (EObject) spotMarketsModel.eGet(spotMarketsModel_fobSales_group);
		if (group != null) {
			List<EObject> markets = MetamodelUtils.getValueAsTypedList(group, spotMarketsGroup_markets);
			if (markets != null) {
				for (EObject market : markets) {
					if (market.eIsSet(FOBSalesMarket_loadPort)) {
						List<EObject> originPorts = new ArrayList<EObject>(1);
						originPorts.add((EObject) market.eGet(FOBSalesMarket_loadPort));
						market.eSet(FOBSalesMarket_originPorts, originPorts);
						market.eUnset(FOBSalesMarket_loadPort);
					}
				}
			}
		}

	}
}

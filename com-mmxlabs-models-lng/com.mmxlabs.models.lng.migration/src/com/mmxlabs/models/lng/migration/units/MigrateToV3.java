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

	protected void migrateFOBSales(final MetamodelLoader loader, final EObject model) {

		final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
		final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
		final EReference reference_LNGScenarioModel_spotMarketsModel = MetamodelUtils.getReference(class_LNGScenarioModel, "spotMarketsModel");

		final EPackage package_SpotMarketsModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_SpotMarketsModel);

		final EClass class_SpotMarketsModel = MetamodelUtils.getEClass(package_SpotMarketsModel, "SpotMarketsModel");
		final EClass class_SpotMarketGroup = MetamodelUtils.getEClass(package_SpotMarketsModel, "SpotMarketGroup");
		final EClass class_FOBSalesMarket = MetamodelUtils.getEClass(package_SpotMarketsModel, "FOBSalesMarket");

		final EReference reference_FOBSalesMarket_loadPort = MetamodelUtils.getReference(class_FOBSalesMarket, "loadPort");
		final EReference reference_FOBSalesMarket_originPorts = MetamodelUtils.getReference(class_FOBSalesMarket, "originPorts");

		final EReference reference_SpotMarketsModel_fobSalesSpotMarket = MetamodelUtils.getReference(class_SpotMarketsModel, "fobSalesSpotMarket");
		final EReference reference_SpotMarketsGroup_markets = MetamodelUtils.getReference(class_SpotMarketGroup, "markets");

		final EObject spotMarketsModel = (EObject) model.eGet(reference_LNGScenarioModel_spotMarketsModel);
		final EObject group = (EObject) spotMarketsModel.eGet(reference_SpotMarketsModel_fobSalesSpotMarket);
		if (group != null) {
			final List<EObject> markets = MetamodelUtils.getValueAsTypedList(group, reference_SpotMarketsGroup_markets);
			if (markets != null) {
				for (final EObject market : markets) {
					if (market.eIsSet(reference_FOBSalesMarket_loadPort)) {
						final List<EObject> originPorts = new ArrayList<EObject>(1);
						originPorts.add((EObject) market.eGet(reference_FOBSalesMarket_loadPort));
						market.eSet(reference_FOBSalesMarket_originPorts, originPorts);
						market.eUnset(reference_FOBSalesMarket_loadPort);
					}
				}
			}
		}

	}
}

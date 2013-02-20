/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xml.type.AnyType;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.MetamodelVersionsUtil;
import com.mmxlabs.models.lng.migration.MetamodelVersionsUtil.ModelsLNGSet_v1;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.utils.EcoreHelper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV1 extends AbstractMigrationUnit {

	private MetamodelLoader destiniationLoader;
	private MetamodelLoader sourceLoader;

	@Override
	public String getContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getSourceVersion() {
		return 0;
	}

	@Override
	public int getDestinationVersion() {
		return -1;
	}

	@Override
	protected MetamodelLoader getSourceMetamodelLoader(final Map<String, URI> extraPackages) {
		if (sourceLoader == null) {
			sourceLoader = MetamodelVersionsUtil.createV0Loader(extraPackages);
		}
		return sourceLoader;
	}

	@Override
	protected MetamodelLoader getDestinationMetamodelLoader(final Map<String, URI> extraPackages) {
		if (destiniationLoader == null) {
			destiniationLoader = MetamodelVersionsUtil.createV1Loader(extraPackages);
		}
		return destiniationLoader;
	}

	@Override
	protected void doMigration(final Map<ModelsLNGSet_v1, EObject> models) {

		final MetamodelLoader v1Loader = destiniationLoader;
		migrateFixedPrice(v1Loader, models);
		removeOptimisedSchedule(v1Loader, models);
		clearOldElementsFromSchedule(v1Loader, models);
		clearAssignments(v1Loader, models);
		removeExtraAnalyticsFields(v1Loader, models);

		migrateSpotMarketModel(v1Loader, models);
		// From LNG Model Corrector
		fixMissingSpotCargoMarkets(v1Loader, models);

		migrateContracts(v1Loader, models);
	}

	public void migrateFixedPrice(final MetamodelLoader loader, final Map<ModelsLNGSet_v1, EObject> models) {
		final EObject cargoModel = models.get(ModelsLNGSet_v1.Cargo);
		final EPackage cargoPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);

		final EClass class_Slot = MetamodelUtils.getEClass(cargoPackage, "Slot");
		final EStructuralFeature feature_priceExpression = MetamodelUtils.getStructuralFeature(class_Slot, "priceExpression");

		final EClass class_CargoModel = MetamodelUtils.getEClass(cargoPackage, "CargoModel");
		final EStructuralFeature feature_loadSlots = MetamodelUtils.getStructuralFeature(class_CargoModel, "loadSlots");
		final EStructuralFeature feature_dischargeSlots = MetamodelUtils.getStructuralFeature(class_CargoModel, "dischargeSlots");

		final Map<EObject, AnyType> oldFeatures = ((XMLResource) cargoModel.eResource()).getEObjectToExtensionMap();

		@SuppressWarnings("unchecked")
		final EList<EObject> loadSlots = (EList<EObject>) cargoModel.eGet(feature_loadSlots);
		for (final EObject slot : loadSlots) {
			// Convert unknown features
			if (oldFeatures.containsKey(slot)) {

				final AnyType anyType = oldFeatures.get(slot);

				for (final Iterator<FeatureMap.Entry> iter = anyType.getAnyAttribute().iterator(); iter.hasNext();) {
					final FeatureMap.Entry entry = iter.next();
					final EStructuralFeature eStructuralFeature = entry.getEStructuralFeature();
					// Copy value from attributeB to attributeC
					if (eStructuralFeature.getName().equals("fixedPrice")) {
						if (!slot.eIsSet(feature_priceExpression)) {
							slot.eSet(feature_priceExpression, entry.getValue());
						}
						iter.remove();
					}
				}
			}
		}

		@SuppressWarnings("unchecked")
		final EList<EObject> dischargeSlots = (EList<EObject>) cargoModel.eGet(feature_dischargeSlots);
		for (final EObject slot : dischargeSlots) {
			// Convert unknown features
			if (oldFeatures.containsKey(slot)) {

				final AnyType anyType = oldFeatures.get(slot);
				for (final Iterator<FeatureMap.Entry> iter = anyType.getAnyAttribute().iterator(); iter.hasNext();) {
					final FeatureMap.Entry entry = iter.next();
					final EStructuralFeature eStructuralFeature = entry.getEStructuralFeature();
					// Copy value from attributeB to attributeC
					if (eStructuralFeature.getName().equals("fixedPrice")) {
						if (!slot.eIsSet(feature_priceExpression)) {
							slot.eSet(feature_priceExpression, entry.getValue());
						}
						iter.remove();
					}
				}
			}
		}

	}

	public void removeOptimisedSchedule(final MetamodelLoader loader, final Map<ModelsLNGSet_v1, EObject> models) {
		final EObject scheduleModel = models.get(ModelsLNGSet_v1.Schedule);
		final EPackage schedulePackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScheduleModel);

		final EClass class_ScheduleModel = MetamodelUtils.getEClass(schedulePackage, "ScheduleModel");
		final EStructuralFeature feature_optimisedSchedule = MetamodelUtils.getStructuralFeature(class_ScheduleModel, "optimisedSchedule");
		final EStructuralFeature feature_initialSchedule = MetamodelUtils.getStructuralFeature(class_ScheduleModel, "initialSchedule");
		final EStructuralFeature feature_schedule = MetamodelUtils.getStructuralFeature(class_ScheduleModel, "schedule");

		scheduleModel.eUnset(feature_optimisedSchedule);
		scheduleModel.eSet(feature_schedule, scheduleModel.eGet(feature_initialSchedule));
	}

	public void clearOldElementsFromSchedule(final MetamodelLoader loader, final Map<ModelsLNGSet_v1, EObject> models) {
		final EObject scheduleModel = models.get(ModelsLNGSet_v1.Schedule);
		final EPackage schedulePackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScheduleModel);

		final EClass class_ScheduleModel = MetamodelUtils.getEClass(schedulePackage, "ScheduleModel");
		final EStructuralFeature feature_schedule = MetamodelUtils.getStructuralFeature(class_ScheduleModel, "schedule");

		final EObject schedule = (EObject) scheduleModel.eGet(feature_schedule);
		final EClass class_Schedule = MetamodelUtils.getEClass(schedulePackage, "Schedule");

		if (schedule != null) {
			// Clear any unscheduled cargoes -- these have not been used for a long time, if at all.
			final EStructuralFeature feature_unscheduledCargoes = MetamodelUtils.getStructuralFeature(class_Schedule, "unscheduledCargoes");
			schedule.eUnset(feature_unscheduledCargoes);

			// Clear any additional data -> should now be ExtraData
			final EClass class_AdditionalDataHolder = MetamodelUtils.getEClass(schedulePackage, "AdditionalDataHolder");
			final EStructuralFeature feature_additionalData = MetamodelUtils.getStructuralFeature(class_AdditionalDataHolder, "additionalData");

			final Iterator<EObject> itr = schedule.eAllContents();
			while (itr.hasNext()) {
				final EObject eObj = itr.next();
				if (class_AdditionalDataHolder.isInstance(eObj)) {
					eObj.eUnset(feature_additionalData);
				}
			}
		}
	}

	public void clearAssignments(final MetamodelLoader loader, final Map<ModelsLNGSet_v1, EObject> models) {
		final EObject inputModel = models.get(ModelsLNGSet_v1.Input);
		final EPackage inputPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_InputModel);

		final EClass class_InputModel = MetamodelUtils.getEClass(inputPackage, "InputModel");
		final EStructuralFeature feature_assignments = MetamodelUtils.getStructuralFeature(class_InputModel, "assignments");
		final EStructuralFeature feature_lockedAssignedObjects = MetamodelUtils.getStructuralFeature(class_InputModel, "lockedAssignedObjects");

		inputModel.eUnset(feature_assignments);
		inputModel.eUnset(feature_lockedAssignedObjects);
	}

	public void removeExtraAnalyticsFields(final MetamodelLoader loader, final Map<ModelsLNGSet_v1, EObject> models) {

		final EObject analyticsModel = models.get(ModelsLNGSet_v1.Analytics);
		if (analyticsModel == null) {
			return;
		}
		final EPackage analyticsPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_AnalyticsModel);

		final EClass class_AnalyticsModel = MetamodelUtils.getEClass(analyticsPackage, "AnalyticsModel");
		final EStructuralFeature feature_roundTripMatrices = MetamodelUtils.getStructuralFeature(class_AnalyticsModel, "roundTripMatrices");

		final EClass class_UnitCostMatrix = MetamodelUtils.getEClass(analyticsPackage, "UnitCostMatrix");
		final EStructuralFeature feature_toPorts = MetamodelUtils.getStructuralFeature(class_UnitCostMatrix, "toPorts");
		final EStructuralFeature feature_fromPorts = MetamodelUtils.getStructuralFeature(class_UnitCostMatrix, "fromPorts");

		final List<EObject> matrices = MetamodelUtils.getValueAsTypedList(analyticsModel, feature_roundTripMatrices);

		final Map<EObject, AnyType> oldFeatures = ((XMLResource) analyticsModel.eResource()).getEObjectToExtensionMap();
		if (matrices != null) {
			for (final EObject matrix : matrices) {
				// Convert unknown features
				if (oldFeatures.containsKey(matrix)) {

					final AnyType anyType = oldFeatures.get(matrix);

					for (final Iterator<FeatureMap.Entry> iter = anyType.getAnyAttribute().iterator(); iter.hasNext();) {
						final FeatureMap.Entry entry = iter.next();
						final EStructuralFeature eStructuralFeature = entry.getEStructuralFeature();
						// Unset times
						if (eStructuralFeature.getName().equals("returnIdleTime")) {
							iter.remove();
						}
						if (eStructuralFeature.getName().equals("dischargeIdleTime")) {
							iter.remove();
						}

					}

					// Each item in the original list is an entry in the iterator, so we need to build up a copy of the list before we can use it.
					final List<EObject> ports = new ArrayList<EObject>();
					for (final Iterator<FeatureMap.Entry> iter = anyType.getAny().iterator(); iter.hasNext();) {
						final FeatureMap.Entry entry = iter.next();
						final EStructuralFeature eStructuralFeature = entry.getEStructuralFeature();

						if (eStructuralFeature.getName().equals("ports")) {

							final EObject port = (EObject) entry.getValue();
							ports.add(port);

							// Clear old reference
							iter.remove();
						}

					}
					// Clone ports list into to/from.
					if (!matrix.eIsSet(feature_toPorts)) {
						matrix.eSet(feature_toPorts, ports);
					}
					if (!matrix.eIsSet(feature_fromPorts)) {
						matrix.eSet(feature_fromPorts, ports);
					}
				}
			}
		}
	}

	public void migrateContracts(final MetamodelLoader loader, final Map<ModelsLNGSet_v1, EObject> models) {
		final EObject commercialModel = models.get(ModelsLNGSet_v1.Commercial);
		final EPackage commercialPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CommercialModel);
		// final EFactory commercialFactory = commercialPackage.getEFactoryInstance();

		final EClass class_CommercialModel = MetamodelUtils.getEClass(commercialPackage, "CommercialModel");
		final EStructuralFeature feature_purchaseContracts = MetamodelUtils.getStructuralFeature(class_CommercialModel, "purchaseContracts");
		final EStructuralFeature feature_salesContracts = MetamodelUtils.getStructuralFeature(class_CommercialModel, "salesContracts");

		{
			final List<EObject> contracts = new ArrayList<EObject>(MetamodelUtils.<EObject> getValueAsTypedList(commercialModel, feature_purchaseContracts));

			for (int i = 0; i < contracts.size(); ++i) {
				final EObject oldContract = contracts.get(i);
				EClass class_params = null;
				if (oldContract.eClass().getName().equals("FixedPriceContract")) {
					class_params = MetamodelUtils.getEClass(commercialPackage, "FixedPriceParameters");
				} else if (oldContract.eClass().getName().equals("IndexPriceContract")) {
					class_params = MetamodelUtils.getEClass(commercialPackage, "IndexPriceParameters");
				} else if (oldContract.eClass().getName().equals("PriceExpressionContract")) {
					class_params = MetamodelUtils.getEClass(commercialPackage, "ExpressionPriceParameters");
				} else if (oldContract.eClass().getName().equals("RedirectionContract")) {
					class_params = MetamodelUtils.getEClass(commercialPackage, "RedirectionPriceParameters");

				} else if (oldContract.eClass().getName().equals("RedirectionPurchaseContract")) {
					// PETRONAS MIGRATION SHOULD HANDLE THESE

					continue;
				} else if (oldContract.eClass().getName().equals("NetbackPurchaseContract")) {
					// VANILLA MIGRATION SHOULD HANDLE THESE
					continue;
					// class_params = MetamodelUtils.getEClass(commercialPackage, "NetbackPriceParameters");
				} else if (oldContract.eClass().getName().equals("ProfitSharePurchaseContract")) {
					// VANILLA MIGRATION SHOULD HANDLE THESE
					continue;
					// class_params = MetamodelUtils.getEClass(commercialPackage, "ProfitSharePriceParameters");
				} else if (oldContract.eClass().getName().equals("PurchaseContract")) {
					continue;
				}

				if (class_params == null) {
					throw new IllegalStateException("Unknown contract type: " + oldContract.eClass().getName());
				}

				final EObject newContract = convertContract(oldContract, class_params, commercialPackage, true);
				contracts.set(i, newContract);
			}
			commercialModel.eSet(feature_purchaseContracts, contracts);
		}
		{
			final List<EObject> contracts = new ArrayList<EObject>(MetamodelUtils.<EObject> getValueAsTypedList(commercialModel, feature_salesContracts));

			for (int i = 0; i < contracts.size(); ++i) {
				final EObject oldContract = contracts.get(i);
				EClass class_params = null;
				if (oldContract.eClass().getName().equals("FixedPriceContract")) {
					class_params = MetamodelUtils.getEClass(commercialPackage, "FixedPriceParameters");
				} else if (oldContract.eClass().getName().equals("IndexPriceContract")) {
					class_params = MetamodelUtils.getEClass(commercialPackage, "IndexPriceParameters");
				} else if (oldContract.eClass().getName().equals("PriceExpressionContract")) {
					class_params = MetamodelUtils.getEClass(commercialPackage, "ExpressionPriceParameters");
				} else if (oldContract.eClass().getName().equals("SalesContract")) {
					continue;
				}

				if (class_params == null) {
					throw new IllegalStateException("Unknown contract type: " + oldContract.eClass().getName());
				}

				final EObject newContract = convertContract(oldContract, class_params, commercialPackage, false);
				contracts.set(i, newContract);
			}
			commercialModel.eSet(feature_salesContracts, contracts);
		}

	}

	public EObject convertContract(final EObject original, final EClass class_params, final EPackage commercialPackage, final boolean isPurchase) {
		final EFactory commercialFactory = commercialPackage.getEFactoryInstance();

		final EClass class_targetType = MetamodelUtils.getEClass(commercialPackage, isPurchase ? "PurchaseContract" : "SalesContract");

		final EObject newContract = commercialFactory.create(class_targetType);
		// Copy contract params over
		for (final EStructuralFeature feature : class_targetType.getEAllStructuralFeatures()) {
			if (original.eIsSet(feature)) {
				newContract.eSet(feature, original.eGet(feature));
			}
		}
		final EObject paramsObject = commercialFactory.create(class_params);
		// List of features to copy over to params
		for (final EStructuralFeature feature : class_params.getEAllStructuralFeatures()) {
			final EStructuralFeature oldFeature = original.eClass().getEStructuralFeature(feature.getName());
			if (original.eIsSet(oldFeature)) {
				paramsObject.eSet(feature, original.eGet(oldFeature));
				// Clear old data
				original.eUnset(oldFeature);
			}
		}

		// Set params feature
		final EClass class_Contract = MetamodelUtils.getEClass(commercialPackage, "Contract");
		final EStructuralFeature feature_priceInfo = MetamodelUtils.getStructuralFeature(class_Contract, "priceInfo");
		newContract.eSet(feature_priceInfo, paramsObject);

		return newContract;
	}

	public void migrateSpotMarketModel(final MetamodelLoader loader, final Map<ModelsLNGSet_v1, EObject> models) {
		final EPackage mmxcorePackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_MMXCore);
		final EPackage pricingPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_PricingModel);
		final EPackage spotMarketsPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_SpotMarketsModel);
		final EFactory spotMarketsFactory = spotMarketsPackage.getEFactoryInstance();

		final EObject pricingModel = models.get(ModelsLNGSet_v1.Pricing);
		final EObject spotMarketsModel = models.get(ModelsLNGSet_v1.SpotMarkets);

		final EClass class_PricingModel = MetamodelUtils.getEClass(pricingPackage, "PricingModel");
		final EClass class_SpotMarketsModel = MetamodelUtils.getEClass(spotMarketsPackage, "SpotMarketsModel");

		// Migrate each of the four spot markets
		{

			EcoreHelper.copyEObjectFeature(mmxcorePackage, pricingModel, spotMarketsModel, MetamodelUtils.getStructuralFeature(class_PricingModel, "desPurchaseSpotMarket"));
			pricingModel.eUnset(MetamodelUtils.getStructuralFeature(class_PricingModel, "desPurchaseSpotMarket"));
			EcoreHelper.copyEObjectFeature(mmxcorePackage, pricingModel, spotMarketsModel, MetamodelUtils.getStructuralFeature(class_PricingModel, "fobPurchasesSpotMarket"));
			pricingModel.eUnset(MetamodelUtils.getStructuralFeature(class_PricingModel, "fobPurchasesSpotMarket"));
			EcoreHelper.copyEObjectFeature(mmxcorePackage, pricingModel, spotMarketsModel, MetamodelUtils.getStructuralFeature(class_PricingModel, "desSalesSpotMarket"));
			pricingModel.eUnset(MetamodelUtils.getStructuralFeature(class_PricingModel, "desSalesSpotMarket"));
			EcoreHelper.copyEObjectFeature(mmxcorePackage, pricingModel, spotMarketsModel, MetamodelUtils.getStructuralFeature(class_PricingModel, "fobSalesSpotMarket"));
			pricingModel.eUnset(MetamodelUtils.getStructuralFeature(class_PricingModel, "fobSalesSpotMarket"));
		}

		// Convert contract at same time

		// Migrate the charter costs models
		{
			final EStructuralFeature feature_PM_fleetCost = MetamodelUtils.getStructuralFeature(class_PricingModel, "fleetCost");
			final EClass class_PM_FleetCostModel = MetamodelUtils.getEClass(pricingPackage, "FleetCostModel");
			final EStructuralFeature feature_PM_FCM_charterCosts = MetamodelUtils.getStructuralFeature(class_PM_FleetCostModel, "charterCosts");

			final EStructuralFeature feature_SMM_charteringSpotMarkets = MetamodelUtils.getStructuralFeature(class_SpotMarketsModel, "charteringSpotMarkets");
			final EClass class_SMM_CharterCostModel = MetamodelUtils.getEClass(spotMarketsPackage, "CharterCostModel");

			final EObject fleetCostModel = (EObject) pricingModel.eGet(feature_PM_fleetCost);
			if (fleetCostModel != null) {
				final List<EObject> charterCosts = MetamodelUtils.getValueAsTypedList(fleetCostModel, feature_PM_FCM_charterCosts);
				if (charterCosts != null) {
					final List<EObject> newCharteringCostModels = new ArrayList<EObject>(charterCosts.size());

					for (final EObject oldCharterCost : charterCosts) {
						// TODO: CONVERT
						final EObject newCharterCost = spotMarketsFactory.create(class_SMM_CharterCostModel);

						EcoreHelper.copyEObject(mmxcorePackage, oldCharterCost, newCharterCost);
						newCharteringCostModels.add(newCharterCost);
					}

					spotMarketsModel.eSet(feature_SMM_charteringSpotMarkets, newCharteringCostModels);
					// Remove old refs
					fleetCostModel.eUnset(feature_PM_FCM_charterCosts);
				}
			}

		}
	}

	@Override
	protected void hookInNewModels(final MetamodelLoader loader, final Map<ModelsLNGSet_v1, EObject> existingModels, final Map<ModelsLNGSet_v1, EObject> newModels) {

		if (existingModels.containsKey(ModelsLNGSet_v1.SpotMarkets)) {
			return;
		}

		final EPackage spotMarketsPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_SpotMarketsModel);
		final EFactory factory = spotMarketsPackage.getEFactoryInstance();
		final EClass class_SpotMarketsModel = MetamodelUtils.getEClass(spotMarketsPackage, "SpotMarketsModel");
		final EObject spotMarketsModel = factory.create(class_SpotMarketsModel);
		newModels.put(ModelsLNGSet_v1.SpotMarkets, spotMarketsModel);

	}

	private void fixMissingSpotCargoMarkets(final MetamodelLoader loader, final Map<ModelsLNGSet_v1, EObject> models) {

		final EPackage spotMarketsPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_SpotMarketsModel);

		final EClass class_SpotMarketsModel = MetamodelUtils.getEClass(spotMarketsPackage, "SpotMarketsModel");

		final EObject spotMarketsModel = models.get(ModelsLNGSet_v1.SpotMarkets);
		if (spotMarketsModel != null) {
			{
				final EStructuralFeature feature = MetamodelUtils.getStructuralFeature(class_SpotMarketsModel, "desPurchaseSpotMarket");
				fixUpSpotMarketGroup(loader, spotMarketsModel, feature, "DES Purchase");
			}
			{
				final EStructuralFeature feature = MetamodelUtils.getStructuralFeature(class_SpotMarketsModel, "desSalesSpotMarket");
				fixUpSpotMarketGroup(loader, spotMarketsModel, feature, "DES Sale");
			}
			{
				final EStructuralFeature feature = MetamodelUtils.getStructuralFeature(class_SpotMarketsModel, "fobPurchasesSpotMarket");
				fixUpSpotMarketGroup(loader, spotMarketsModel, feature, "FOB Purchase");
			}
			{
				final EStructuralFeature feature = MetamodelUtils.getStructuralFeature(class_SpotMarketsModel, "fobSalesSpotMarket");
				fixUpSpotMarketGroup(loader, spotMarketsModel, feature, "FOB Sale");
			}
		}
	}

	/**
	 * For the given SpotMarketGroup instance, ensure ensure the correct spot type is set on the group and any contained markets. Also ensure there is a name set on any curve data.
	 * 
	 * @param loader
	 * @param spotMarketsModel
	 * @param feature
	 * @param spotTypeString
	 */
	private void fixUpSpotMarketGroup(final MetamodelLoader loader, final EObject spotMarketsModel, final EStructuralFeature feature, final String spotTypeString) {

		final EPackage spotMarketsPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_SpotMarketsModel);
		final EFactory spotMarketsFactory = spotMarketsPackage.getEFactoryInstance();

		final EClass class_SpotMarketGroup = MetamodelUtils.getEClass(spotMarketsPackage, "SpotMarketGroup");
		final EStructuralFeature feature_SMG_type = MetamodelUtils.getStructuralFeature(class_SpotMarketGroup, "type");
		final EStructuralFeature feature_SMG_markets = MetamodelUtils.getStructuralFeature(class_SpotMarketGroup, "markets");
		final EStructuralFeature feature_SMG_availability = MetamodelUtils.getStructuralFeature(class_SpotMarketGroup, "availability");

		final EClass class_SpotMarket = MetamodelUtils.getEClass(spotMarketsPackage, "SpotMarket");
		final EStructuralFeature feature_SM_type = MetamodelUtils.getStructuralFeature(class_SpotMarket, "type");
		final EStructuralFeature feature_SM_availability = MetamodelUtils.getStructuralFeature(class_SpotMarket, "availability");

		final EDataType dt_SpotType = MetamodelUtils.getEDataType(spotMarketsPackage, "SpotType");

		final Object dt_SpotType_INSTANCE = spotMarketsFactory.createFromString(dt_SpotType, spotTypeString);
		if (spotMarketsModel.eIsSet(feature) == false) {
			final EObject group = spotMarketsFactory.create(class_SpotMarketGroup);
			group.eSet(feature_SMG_type, dt_SpotType_INSTANCE);
			spotMarketsModel.eSet(feature, group);
		}
		final EObject group = (EObject) spotMarketsModel.eGet(feature);
		if (group.eIsSet(feature_SMG_availability)) {
			fixSpotAvailabilityName(loader, (EObject) group.eGet(feature_SMG_availability), spotTypeString);
		}

		if (group.eIsSet(feature_SMG_markets)) {
			final List<EObject> spotMarkets = MetamodelUtils.getValueAsTypedList(group, feature_SMG_markets);
			for (final EObject market : spotMarkets) {
				if (market.eGet(feature_SM_type) != dt_SpotType_INSTANCE) {
					market.eSet(feature_SM_type, dt_SpotType_INSTANCE);
				}
				if (market.eIsSet(feature_SM_availability)) {
					fixSpotAvailabilityName(loader, (EObject) market.eGet(feature_SM_availability), spotTypeString);
				}
			}
		}
	}

	private void fixSpotAvailabilityName(final MetamodelLoader loader, final EObject availability, final String name) {

		final EPackage mmxcorePackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_MMXCore);

		final EClass class_NamedObject = MetamodelUtils.getEClass(mmxcorePackage, "NamedObject");
		final EStructuralFeature feature_NO_name = MetamodelUtils.getStructuralFeature(class_NamedObject, "name");

		final EPackage spotMarketsPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_SpotMarketsModel);

		final EClass class_SpotAvailability = MetamodelUtils.getEClass(spotMarketsPackage, "SpotAvailability");
		final EStructuralFeature feature_SA_curve = MetamodelUtils.getStructuralFeature(class_SpotAvailability, "curve");

		if (availability != null) {
			if (availability.eIsSet(feature_SA_curve)) {
				final EObject curve = (EObject) availability.eGet(feature_SA_curve);

				if (!curve.eIsSet(feature_NO_name)) {
					curve.eSet(feature_NO_name, name);
				} else {
					final String currentName = (String) curve.eGet(feature_NO_name);
					if (currentName.isEmpty()) {

						curve.eSet(feature_NO_name, name);
					}
				}
			}
		}
	}

}

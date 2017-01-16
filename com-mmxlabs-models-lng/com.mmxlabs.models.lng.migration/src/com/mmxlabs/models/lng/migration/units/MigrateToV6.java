/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
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
 */
public class MigrateToV6 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 5;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 6;
	}

	@Override
	protected MetamodelLoader getSourceMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (sourceLoader == null) {
			sourceLoader = MetamodelVersionsUtil.createV5Loader(extraPackages);
		}
		return sourceLoader;
	}

	@Override
	protected MetamodelLoader getDestinationMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (destinationLoader == null) {
			destinationLoader = MetamodelVersionsUtil.createV6Loader(extraPackages);
		}
		return destinationLoader;
	}

	@Override
	protected void doMigration(final EObject model) {
		// Nothing to do - model is forward compatible.

		// Step 1:
		// Perform AssingmentModel migration.
		migrateAssignmentModel(model);

		// Step 2: Clean out old deprecated fields
		// DES Purchase Spot Market / FOB Sale origin ports
		removeSpotMarketFields(model);
		// OptimiserSettings
		removeOptimiserSettingsFields(model);

		// LNG ScenarioModel (params)
		removeScenario_ParamsModel(model);
		// Daily Hire rate on Sequence
		removeDailyHireRate(model);
		// Base fuel price fields
		removeBaseFuelCost_Price(model);

		// Next step - clean up the ecore models - add v7
	}

	private void migrateAssignmentModel(final EObject model) {

		// This should get the cached loader instance
		final MetamodelLoader loader = getDestinationMetamodelLoader(null);

		final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
		final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
		final EClass class_LNGPortfolioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGPortfolioModel");

		final EReference reference_LNGScenarioModel_portfolioModel = MetamodelUtils.getReference(class_LNGScenarioModel, "portfolioModel");
		final EReference reference_LNGPortfolioModel_assignmentModel = MetamodelUtils.getReference(class_LNGPortfolioModel, "assignmentModel");

		final EPackage package_AssignmentModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_AssignmentModel);
		final EPackage package_FleetModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_FleetModel);

		final EClass class_AssignmentModel = MetamodelUtils.getEClass(package_AssignmentModel, "AssignmentModel");
		final EClass class_ElementAssignment = MetamodelUtils.getEClass(package_AssignmentModel, "ElementAssignment");
		final EClass class_Vessel = MetamodelUtils.getEClass(package_FleetModel, "Vessel");

		final EReference reference_AssignableModel_elementAssignments = MetamodelUtils.getReference(class_AssignmentModel, "elementAssignments");
		final EReference reference_ElementAssignment_assignment = MetamodelUtils.getReference(class_ElementAssignment, "assignment");
		final EReference reference_ElementAssignment_assignedObject = MetamodelUtils.getReference(class_ElementAssignment, "assignedObject");
		final EAttribute attribute_ElementAssignment_sequence = MetamodelUtils.getAttribute(class_ElementAssignment, "sequence");
		final EAttribute attribute_ElementAssignment_locked = MetamodelUtils.getAttribute(class_ElementAssignment, "locked");
		final EAttribute attribute_ElementAssignment_spotIndex = MetamodelUtils.getAttribute(class_ElementAssignment, "spotIndex");

		final EClass class_AssignableElement = MetamodelUtils.getEClass(package_FleetModel, "AssignableElement");

		final EReference reference_AssignableElement_assignment = MetamodelUtils.getReference(class_AssignableElement, "assignment");
		final EAttribute attribute_AssignableElement_sequenceHint = MetamodelUtils.getAttribute(class_AssignableElement, "sequenceHint");
		final EAttribute attribute_AssignableElement_locked = MetamodelUtils.getAttribute(class_AssignableElement, "locked");
		final EAttribute attribute_AssignableElement_spotIndex = MetamodelUtils.getAttribute(class_AssignableElement, "spotIndex");

		final EObject portfolioModel = (EObject) model.eGet(reference_LNGScenarioModel_portfolioModel);
		final EObject assignmentModel = (EObject) portfolioModel.eGet(reference_LNGPortfolioModel_assignmentModel);
		if (assignmentModel != null) {
			final EList<EObject> elementAssignments = MetamodelUtils.getValueAsTypedList(assignmentModel, reference_AssignableModel_elementAssignments);
			if (elementAssignments != null) {
				for (final EObject elementAssignment : elementAssignments) {
					final EObject assignedObject = (EObject) elementAssignment.eGet(reference_ElementAssignment_assignedObject);
					if (assignedObject != null) {
						// Check cast
						if (class_AssignableElement.isInstance(assignedObject)) {
							// Copy data across
							final Object assignment = elementAssignment.eGet(reference_ElementAssignment_assignment);
							assignedObject.eSet(reference_AssignableElement_assignment, assignment);
							assignedObject.eSet(attribute_AssignableElement_locked, elementAssignment.eGet(attribute_ElementAssignment_locked));
							assignedObject.eSet(attribute_AssignableElement_sequenceHint, elementAssignment.eGet(attribute_ElementAssignment_sequence));
							if (!class_Vessel.isInstance(assignment)) {
								assignedObject.eSet(attribute_AssignableElement_spotIndex, elementAssignment.eGet(attribute_ElementAssignment_spotIndex));
							}
						}
					}
				}
			}
			// Clear assignment model refernce
			portfolioModel.eUnset(reference_LNGPortfolioModel_assignmentModel);
		}
	}

	private void removeDailyHireRate(final EObject model) {

		// This should get the cached loader instance
		final MetamodelLoader loader = getDestinationMetamodelLoader(null);

		final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
		final EPackage package_ScheduleModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScheduleModel);
		final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
		final EClass class_LNGPortfolioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGPortfolioModel");

		final EReference reference_LNGScenarioModel_portfolioModel = MetamodelUtils.getReference(class_LNGScenarioModel, "portfolioModel");
		final EReference reference_LNGPortfolioModel_scheduleModel = MetamodelUtils.getReference(class_LNGPortfolioModel, "scheduleModel");

		final EClass class_ScheduleModel = MetamodelUtils.getEClass(package_ScheduleModel, "ScheduleModel");
		final EClass class_Schedule = MetamodelUtils.getEClass(package_ScheduleModel, "Schedule");
		final EClass class_Sequence = MetamodelUtils.getEClass(package_ScheduleModel, "Sequence");

		final EReference reference_ScheduleModel_schedule = MetamodelUtils.getReference(class_ScheduleModel, "schedule");
		final EReference reference_Schedule_sequences = MetamodelUtils.getReference(class_Schedule, "sequences");
		final EAttribute attribute_Sequence_dailyHireRate = MetamodelUtils.getAttribute(class_Sequence, "dailyHireRate");

		final EObject portfolioModel = (EObject) model.eGet(reference_LNGScenarioModel_portfolioModel);
		if (portfolioModel != null) {
			final EObject scheduleModel = (EObject) portfolioModel.eGet(reference_LNGPortfolioModel_scheduleModel);
			if (scheduleModel != null) {
				final EObject schedule = (EObject) scheduleModel.eGet(reference_ScheduleModel_schedule);
				if (schedule != null) {
					final EList<EObject> sequences = MetamodelUtils.getValueAsTypedList(schedule, reference_Schedule_sequences);
					if (sequences != null) {
						for (final EObject sequence : sequences) {
							sequence.eUnset(attribute_Sequence_dailyHireRate);
						}
					}
				}
			}
		}
	}

	private void removeScenario_ParamsModel(final EObject model) {

		// This should get the cached loader instance
		final MetamodelLoader loader = getDestinationMetamodelLoader(null);

		final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
		final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
		final EReference reference_LNGScenarioModel_parametersModel = MetamodelUtils.getReference(class_LNGScenarioModel, "parametersModel");

		model.eUnset(reference_LNGScenarioModel_parametersModel);
	}

	private void removeBaseFuelCost_Price(final EObject model) {

		// This should get the cached loader instance
		final MetamodelLoader loader = getDestinationMetamodelLoader(null);

		final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
		final EPackage package_PricingModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_PricingModel);

		final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");

		final EReference reference_LNGScenarioModel_pricingModel = MetamodelUtils.getReference(class_LNGScenarioModel, "pricingModel");

		final EClass class_PricingModel = MetamodelUtils.getEClass(package_PricingModel, "PricingModel");
		final EClass class_FleetCostModel = MetamodelUtils.getEClass(package_PricingModel, "FleetCostModel");
		final EClass class_BaseFuelCost = MetamodelUtils.getEClass(package_PricingModel, "BaseFuelCost");

		final EReference reference_class_PricingModel_fleetCost = MetamodelUtils.getReference(class_PricingModel, "fleetCost");
		final EReference reference_FleetCostModel_baseFuelPrices = MetamodelUtils.getReference(class_FleetCostModel, "baseFuelPrices");
		final EAttribute attribute_BaseFuelCost_price = MetamodelUtils.getAttribute(class_BaseFuelCost, "price");

		final EObject pricingModel = (EObject) model.eGet(reference_LNGScenarioModel_pricingModel);
		if (pricingModel != null) {
			final EObject fleetCostModel = (EObject) pricingModel.eGet(reference_class_PricingModel_fleetCost);
			if (fleetCostModel != null) {
				final EList<EObject> baseFuelCosts = MetamodelUtils.getValueAsTypedList(fleetCostModel, reference_FleetCostModel_baseFuelPrices);
				if (baseFuelCosts != null) {
					for (final EObject baseFuelCost : baseFuelCosts) {
						baseFuelCost.eUnset(attribute_BaseFuelCost_price);
					}
				}
			}
		}
	}

	private void removeSpotMarketFields(final EObject model) {

		// This should get the cached loader instance
		final MetamodelLoader loader = getDestinationMetamodelLoader(null);

		final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
		final EPackage package_SpotMarketsModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_SpotMarketsModel);

		final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");

		final EReference reference_LNGScenarioModel_spotMarketsModel = MetamodelUtils.getReference(class_LNGScenarioModel, "spotMarketsModel");

		final EClass class_SpotMarketsModel = MetamodelUtils.getEClass(package_SpotMarketsModel, "SpotMarketsModel");
		final EClass class_SpotMarketGroup = MetamodelUtils.getEClass(package_SpotMarketsModel, "SpotMarketGroup");
		final EClass class_DESSalesMarket = MetamodelUtils.getEClass(package_SpotMarketsModel, "DESSalesMarket");
		final EClass class_FOBSalesMarket = MetamodelUtils.getEClass(package_SpotMarketsModel, "FOBSalesMarket");

		final EReference reference_class_class_SpotMarketsModel_desSalesSpotMarket = MetamodelUtils.getReference(class_SpotMarketsModel, "desSalesSpotMarket");
		final EReference reference_class_class_SpotMarketsModel_fobSalesSpotMarket = MetamodelUtils.getReference(class_SpotMarketsModel, "fobSalesSpotMarket");
		final EReference reference_SpotMarketGroup_markets = MetamodelUtils.getReference(class_SpotMarketGroup, "markets");

		final EReference reference_DESSalesMarket_marketPorts = MetamodelUtils.getReference(class_DESSalesMarket, "marketPorts");
		final EReference reference_FOBSalesMarket_loadPort = MetamodelUtils.getReference(class_FOBSalesMarket, "loadPort");

		final EObject spotMarketsModel = (EObject) model.eGet(reference_LNGScenarioModel_spotMarketsModel);
		if (spotMarketsModel != null) {
			final EObject desSalesMarketGroup = (EObject) spotMarketsModel.eGet(reference_class_class_SpotMarketsModel_desSalesSpotMarket);
			if (desSalesMarketGroup != null) {
				final EList<EObject> markets = MetamodelUtils.getValueAsTypedList(desSalesMarketGroup, reference_SpotMarketGroup_markets);
				if (markets != null) {
					for (final EObject market : markets) {
						if (class_DESSalesMarket.isInstance(market)) {
							market.eUnset(reference_DESSalesMarket_marketPorts);
						}
					}
				}
			}

			final EObject fobSalesMarketGroup = (EObject) spotMarketsModel.eGet(reference_class_class_SpotMarketsModel_fobSalesSpotMarket);
			if (fobSalesMarketGroup != null) {
				final EList<EObject> markets = MetamodelUtils.getValueAsTypedList(fobSalesMarketGroup, reference_SpotMarketGroup_markets);
				if (markets != null) {
					for (final EObject market : markets) {
						if (class_FOBSalesMarket.isInstance(market)) {
							market.eUnset(reference_FOBSalesMarket_loadPort);
						}
					}
				}
			}
		}

	}

	private void removeOptimiserSettingsFields(final EObject model) {

		// This should get the cached loader instance
		final MetamodelLoader loader = getDestinationMetamodelLoader(null);

		final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
		final EPackage package_ParametersModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ParametersModel);

		final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
		final EClass class_LNGPortfolioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGPortfolioModel");
		final EClass class_OptimiserSettings = MetamodelUtils.getEClass(package_ParametersModel, "OptimiserSettings");

		final EReference reference_LNGScenarioModel_portfolioModel = MetamodelUtils.getReference(class_LNGScenarioModel, "portfolioModel");
		final EReference reference_LNGPortfolioModel_parameters = MetamodelUtils.getReference(class_LNGPortfolioModel, "parameters");

		final EAttribute attribute_OptimiserSettings_rewire = MetamodelUtils.getAttribute(class_OptimiserSettings, "rewire");

		final EObject portfolioModel = (EObject) model.eGet(reference_LNGScenarioModel_portfolioModel);
		if (portfolioModel != null) {
			final EObject optimiserSettings = (EObject) portfolioModel.eGet(reference_LNGPortfolioModel_parameters);
			if (optimiserSettings != null) {
				optimiserSettings.eUnset(attribute_OptimiserSettings_rewire);
			}
		}

	}
}

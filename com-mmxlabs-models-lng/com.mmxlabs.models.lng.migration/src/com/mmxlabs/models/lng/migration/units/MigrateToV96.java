/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV96 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 95;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 96;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		final EPackage pkg_CargoModel = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);
		final EPackage pkg_MarketsModel = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_SpotMarketsModel);
		final EPackage pkg_ScheduleModel = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScheduleModel);

		EClass cls_ScheduleModel = MetamodelUtils.getEClass(pkg_ScheduleModel, "ScheduleModel");
		EClass cls_Schedule = MetamodelUtils.getEClass(pkg_ScheduleModel, "Schedule");

		final EObjectWrapper scenarioModel = modelRecord.getModelRoot();

		final List<EObjectWrapper> adpModels = scenarioModel.getRefAsList("adpModels");
		if (adpModels == null || adpModels.isEmpty()) {
			return;
		}

		final EObjectWrapper adpModel = adpModels.get(0);
		scenarioModel.unsetFeature("adpModels");
		scenarioModel.setRef("adpModel", adpModel);

		final EObjectWrapper fleetProfile = adpModel.getRef("fleetProfile");
		final EObjectWrapper spotMarketsProfile = adpModel.getRef("spotMarketsProfile");

		// Clear old data
		final EObjectWrapper scheduleModel = scenarioModel.getRef("scheduleModel");
		scheduleModel.unsetFeature("schedule");
		scheduleModel.setAttrib("dirty", Boolean.FALSE);

		final EObjectWrapper analyticsModel = scenarioModel.getRef("analyticsModel");
		analyticsModel.unsetFeature("optionModels");
		analyticsModel.unsetFeature("optimisations");

		final EObjectWrapper cargoModel = scenarioModel.getRef("cargoModel");
		cargoModel.unsetFeature("cargoes");
		cargoModel.unsetFeature("loadSlots");
		cargoModel.unsetFeature("dischargeSlots");
		cargoModel.unsetFeature("vesselAvailabilities");
		cargoModel.unsetFeature("vesselEvents");

		final EObjectWrapper referenceModel = scenarioModel.getRef("referenceModel");
		final EObjectWrapper spotMarketsModel = referenceModel.getRef("spotMarketsModel");

		if (!fleetProfile.getAttribAsBoolean("includeEnabledCharterMarkets")) {
			spotMarketsModel.unsetFeature("charterInMarkets");
		}
		// TODO: Spot markets
		{
			final List<EObjectWrapper> des_purchase_markets = new LinkedList<>();
			final List<EObjectWrapper> fob_purchase_markets = new LinkedList<>();
			final List<EObjectWrapper> des_sale_markets = new LinkedList<>();
			final List<EObjectWrapper> fob_sale_markets = new LinkedList<>();
			if (spotMarketsProfile.getAttribAsBoolean("includeEnabledSpotMarkets")) {
				{
					final List<EObjectWrapper> l = spotMarketsModel.getRef("desPurchaseSpotMarket").getRefAsList("markets");
					if (l != null) {
						des_purchase_markets.addAll(l);
					}
				}
				{
					final List<EObjectWrapper> l = spotMarketsModel.getRef("desSalesSpotMarket").getRefAsList("markets");
					if (l != null) {
						des_sale_markets.addAll(l);
					}
				}
				{
					final List<EObjectWrapper> l = spotMarketsModel.getRef("fobPurchasesSpotMarket").getRefAsList("markets");
					if (l != null) {
						fob_purchase_markets.addAll(l);
					}
				}
				{
					final List<EObjectWrapper> l = spotMarketsModel.getRef("fobSalesSpotMarket").getRefAsList("markets");
					if (l != null) {
						fob_sale_markets.addAll(l);
					}
				}
			}
			final List<EObjectWrapper> extraMarkets = spotMarketsProfile.getRefAsList("spotMarkets");
			if (extraMarkets != null) {
				final EClass cls_DESPurchasesMarket = MetamodelUtils.getEClass(pkg_MarketsModel, "DESPurchaseMarket");
				final EClass cls_DESSalesMarket = MetamodelUtils.getEClass(pkg_MarketsModel, "DESSalesMarket");
				final EClass cls_FOBPurchasesMarket = MetamodelUtils.getEClass(pkg_MarketsModel, "FOBPurchasesMarket");
				final EClass cls_FOBSalesMarket = MetamodelUtils.getEClass(pkg_MarketsModel, "FOBSalesMarket");

				for (final EObjectWrapper extraMarket : extraMarkets) {
					if (cls_DESPurchasesMarket.isInstance(extraMarket)) {
						des_purchase_markets.add(extraMarket);
					} else if (cls_DESSalesMarket.isInstance(extraMarket)) {
						des_sale_markets.add(extraMarket);
					} else if (cls_FOBPurchasesMarket.isInstance(extraMarket)) {
						fob_purchase_markets.add(extraMarket);
					} else if (cls_FOBSalesMarket.isInstance(extraMarket)) {
						fob_sale_markets.add(extraMarket);
					} else {
						assert false;
					}
				}

				spotMarketsProfile.unsetFeature("spotMarkets");
			}

			// Set new data
			{
				spotMarketsModel.getRef("desPurchaseSpotMarket").setRef("markets", des_purchase_markets);
				spotMarketsModel.getRef("desSalesSpotMarket").setRef("markets", des_sale_markets);
				spotMarketsModel.getRef("fobPurchasesSpotMarket").setRef("markets", fob_purchase_markets);
				spotMarketsModel.getRef("fobSalesSpotMarket").setRef("markets", fob_sale_markets);
			}
		}
		final List<EObjectWrapper> loadSlots = new LinkedList<>();
		final List<EObjectWrapper> dischargeSlots = new LinkedList<>();
		{
			final List<EObjectWrapper> profiles = adpModel.getRefAsList("purchaseContractProfiles");
			if (profiles != null) {
				for (final EObjectWrapper profile : profiles) {
					final List<EObjectWrapper> subProfiles = profile.getRefAsList("subProfiles");
					for (final EObjectWrapper subProfile : subProfiles) {
						if (profile.getAttribAsBoolean("enabled")) {
							final List<EObjectWrapper> slots = subProfile.getRefAsList("slots");
							if (slots != null) {
								loadSlots.addAll(slots);
							}
						}
						subProfile.unsetFeature("slots");
					}
				}
			}
		}
		{
			final List<EObjectWrapper> profiles = adpModel.getRefAsList("salesContractProfiles");
			if (profiles != null) {
				for (final EObjectWrapper profile : profiles) {
					final List<EObjectWrapper> subProfiles = profile.getRefAsList("subProfiles");
					for (final EObjectWrapper subProfile : subProfiles) {
						if (profile.getAttribAsBoolean("enabled")) {
							final List<EObjectWrapper> slots = subProfile.getRefAsList("slots");
							if (slots != null) {
								dischargeSlots.addAll(slots);
							}
						}
						subProfile.unsetFeature("slots");
					}
				}
			}
		}

		List<EObjectWrapper> charterInMarkets = spotMarketsModel.getRefAsList("charterInMarkets");
		if (charterInMarkets == null) {
			charterInMarkets = new LinkedList<>();
		} else {
			charterInMarkets = new LinkedList<>(charterInMarkets);

		}
		EObjectWrapper defaultNominalMarket = null;

		final EObjectWrapper adpModelResult = adpModel.getRef("result");
		if (adpModelResult != null) {

			final EClass cls_LoadSlot = MetamodelUtils.getEClass(pkg_CargoModel, "LoadSlot");
			final EClass cls_SpotSlot = MetamodelUtils.getEClass(pkg_CargoModel, "SpotSlot");

			final List<EObjectWrapper> extraSlots = adpModelResult.getRefAsList("extraSlots");

			List<EObjectWrapper> extraSpotSlots = new LinkedList<>();
			if (extraSlots != null) {
				for (final EObjectWrapper slot : extraSlots) {

					// Store spot slot
					if (cls_SpotSlot.isInstance(slot)) {
						extraSpotSlots.add(slot);
						continue;
					}
					if (cls_LoadSlot.isInstance(slot)) {
						loadSlots.add(slot);
					} else {
						dischargeSlots.add(slot);
					}
				}
			}

			final List<EObjectWrapper> extraMarkets = adpModelResult.getRefAsList("extraSpotCharterMarkets");
			if (extraMarkets != null) {
				for (EObjectWrapper market : extraMarkets) {
					if (Objects.equals(market.getAttrib("name"), "ADP Default Vessel") //
							&& Objects.equals(market.getRef("vessel"), fleetProfile.getRef("defaultVessel")) //
							&& Objects.equals(market.getAttrib("charterInRate"), fleetProfile.getAttrib("defaultVesselCharterInRate"))) {

						defaultNominalMarket = market;
					}
				}
				charterInMarkets.addAll(extraMarkets);
			}

			EPackage pkg_AnalyticsModel = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_AnalyticsModel);
			EClass cls_OptimisationResult = MetamodelUtils.getEClass(pkg_AnalyticsModel, "OptimisationResult");
			EClass cls_SolutionOption = MetamodelUtils.getEClass(pkg_AnalyticsModel, "SolutionOption");

			// We *could* retain the schedule *but* we have to generate the cargoes from the CargoAllocations
			final EObjectWrapper resultScheduleModel = adpModelResult.getRef("scheduleModel");
			if (resultScheduleModel != null) {

				EObjectWrapper solutionOption = (EObjectWrapper) pkg_AnalyticsModel.getEFactoryInstance().create(cls_SolutionOption);
				solutionOption.setRef("scheduleModel", resultScheduleModel);
				List<EObjectWrapper> options = new LinkedList<>();
				options.add(solutionOption);

				EObjectWrapper optimisationResult = (EObjectWrapper) pkg_AnalyticsModel.getEFactoryInstance().create(cls_OptimisationResult);
				optimisationResult.setRef("extraSlots", extraSpotSlots);
				optimisationResult.setRef("options", options);
				optimisationResult.setAttrib("name", "ADP Optimisation");

				// Blank base option
				{
					EObjectWrapper baseSolutionOption = (EObjectWrapper) pkg_AnalyticsModel.getEFactoryInstance().create(cls_SolutionOption);
					EObjectWrapper baseScheduleModel = (EObjectWrapper) pkg_ScheduleModel.getEFactoryInstance().create(cls_ScheduleModel);
					EObjectWrapper schedule = (EObjectWrapper) pkg_ScheduleModel.getEFactoryInstance().create(cls_Schedule);

					baseScheduleModel.setRef("schedule", schedule);

					baseSolutionOption.setRef("scheduleModel", baseScheduleModel);
					optimisationResult.setRef("baseOption", baseSolutionOption);

				}

				List<EObjectWrapper> optimisations = scenarioModel.getRef("analyticsModel").getRefAsList("optimisations");
				if (optimisations != null) {
					optimisations.add(optimisationResult);
				} else {
					optimisations = new LinkedList<>();
					optimisations.add(optimisationResult);
					scenarioModel.getRef("analyticsModel").setRef("optimisations", optimisations);
				}

			}
			adpModel.unsetFeature("result");
		}

		if (defaultNominalMarket == null) {
			EClass cls_CharterInMarket = MetamodelUtils.getEClass(pkg_MarketsModel, "CharterInMarket");
			defaultNominalMarket = (EObjectWrapper) pkg_MarketsModel.getEFactoryInstance().create(cls_CharterInMarket);

			defaultNominalMarket.setRef("vessel", fleetProfile.getRef("defaultVessel"));
			defaultNominalMarket.setAttrib("charterInRate", fleetProfile.getAttrib("defaultVesselCharterInRate"));
			defaultNominalMarket.setAttrib("nominal", Boolean.TRUE);
			defaultNominalMarket.setAttrib("enabled", Boolean.TRUE);
			defaultNominalMarket.setAttrib("name", "ADP Default Vessel");

			charterInMarkets.add(defaultNominalMarket);

		}
		spotMarketsModel.setRef("charterInMarkets", charterInMarkets);

		final Set<EObjectWrapper> cargoes = new LinkedHashSet<>();
		for (final EObjectWrapper slot : loadSlots) {
			final EObjectWrapper cargo = slot.getRef("cargo");
			if (cargo != null) {
				cargoes.add(cargo);
			}
		}
		for (final EObjectWrapper slot : dischargeSlots) {
			final EObjectWrapper cargo = slot.getRef("cargo");
			if (cargo != null) {
				cargoes.add(cargo);
			}
		}

		cargoModel.setRef("loadSlots", loadSlots);
		cargoModel.setRef("dischargeSlots", dischargeSlots);
		cargoModel.setRef("cargoes", new ArrayList<>(cargoes));
		{
			final List<EObjectWrapper> l = fleetProfile.getRefAsList("vesselAvailabilities");
			if (l != null) {
				cargoModel.setRef("vesselAvailabilities", l);
			}
		}
		{
			final List<EObjectWrapper> l = fleetProfile.getRefAsList("vesselEvents");
			if (l != null) {
				cargoModel.setRef("vesselEvents", l);
			}
		}

		fleetProfile.setRef("defaultNominalMarket", defaultNominalMarket);
		fleetProfile.unsetFeature("defaultVessel");
		fleetProfile.unsetFeature("defaultVesselCharterInRate");
		fleetProfile.unsetFeature("includeEnabledCharterMarkets");
		adpModel.unsetFeature("spotMarketsProfile");

		// Set blank scenario
		{
			EObjectWrapper schedule = (EObjectWrapper) pkg_ScheduleModel.getEFactoryInstance().create(cls_Schedule);
			scheduleModel.setRef("schedule", schedule);
		}

	}
}

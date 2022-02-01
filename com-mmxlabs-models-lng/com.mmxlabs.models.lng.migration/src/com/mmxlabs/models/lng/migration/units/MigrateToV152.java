/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV152 extends AbstractMigrationUnit {
	
	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}
	
	@Override
	public int getScenarioSourceVersion() {
		return 151;
	}
	
	@Override
	public int getScenarioDestinationVersion() {
		return 152;
	}
	
	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		
		final @NonNull MetamodelLoader loader = modelRecord.getMetamodelLoader();
		final @NonNull EObjectWrapper scenarioModel = modelRecord.getModelRoot();
		final @NonNull EObjectWrapper referenceModel = scenarioModel.getRef("referenceModel");

		final EObjectWrapper cargoModel = scenarioModel.getRef("cargoModel");
		final EObjectWrapper analyticsModel = scenarioModel.getRef("analyticsModel");
		final EObjectWrapper commercialModel = referenceModel.getRef("commercialModel");
		final EObjectWrapper scheduleModel = scenarioModel.getRef("scheduleModel");

		final EPackage commercialPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CommercialModel);
		final EPackage schedulePackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScheduleModel);
		final EPackage analyticsPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_AnalyticsModel);
		
		
		processCharterContracts(commercialModel, commercialPackage);
		processVesselAvailabilities(commercialPackage, cargoModel);
		clearUpScheduleModel(scheduleModel, schedulePackage);
		clearUpAnalyticsModel(analyticsModel, analyticsPackage, schedulePackage);
		processCharterInsEndPort(modelRecord);
	}

	private void processCharterContracts(final EObjectWrapper commercialModel, final EPackage commercialPackage) {
		final List<EObjectWrapper> charterContracts = commercialModel.getRefAsList("charterContracts");
		if (charterContracts != null && !charterContracts.isEmpty()) {
			for (final EObjectWrapper cc : charterContracts) {
				processCharterContract(commercialPackage, cc);
			}
		}
	}
	
	private void processVesselAvailabilities(final EPackage commercialPackage, final EObjectWrapper cargoModel) {
		final List<EObjectWrapper> vesselAvailabilities = cargoModel.getRefAsList("vesselAvailabilities");
		if (vesselAvailabilities != null) {
			for (final EObjectWrapper vesselAvailability : vesselAvailabilities) {
				final EObjectWrapper charterContract = vesselAvailability.getRef("containedCharterContract");
				if (charterContract != null) {
					processCharterContract(commercialPackage, charterContract);
				}
			}
		}
	}
	
	private void processCharterContract(final EPackage commercialPackage, final EObjectWrapper cc) {
		final EClass class_LumpSumRepositioningFeeTerm = MetamodelUtils.getEClass(commercialPackage, "LumpSumRepositioningFeeTerm");
		final EClass class_RepositioningFeeTerm = MetamodelUtils.getEClass(commercialPackage, "RepositioningFeeTerm");
		final EObjectWrapper rfContainer = cc.getRef("repositioningFeeTerms");
		if (rfContainer != null) {
			final List<EObjectWrapper> repositioningFeeTerms = rfContainer.getRefAsList("terms");
			if (repositioningFeeTerms != null && !repositioningFeeTerms.isEmpty()) {
				for (final EObjectWrapper rft : repositioningFeeTerms) {
					if (class_RepositioningFeeTerm.isInstance(rft) && class_LumpSumRepositioningFeeTerm.isInstance(rft)) {
						rft.unsetFeature("originPort");
					}
				}
			}
		}
	}
	
	private void processCharterInsEndPort(@NonNull final MigrationModelRecord modelRecord) {
		final MetamodelLoader modelLoader = modelRecord.getMetamodelLoader();

		final EPackage package_LNGTypes = modelLoader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_LNGTypes);

		final EEnum enum_PortCapability = MetamodelUtils.getEEnum(package_LNGTypes, "PortCapability");
		final EEnumLiteral enumLiteral_PortCapability_Discharge = MetamodelUtils.getEEnum_Literal(enum_PortCapability, "DISCHARGE");

		final EObjectWrapper scenarioModel = modelRecord.getModelRoot();
		final EObjectWrapper referenceModel = scenarioModel.getRef("referenceModel");
		final EObjectWrapper portModel = referenceModel.getRef("portModel");

		final List<EObjectWrapper> portCapabilityGroups = portModel.getRefAsList("specialPortGroups");
		if (portCapabilityGroups == null) {
			return;
		}
		EObjectWrapper dischargeGroup = null;
		for (final EObjectWrapper group : portCapabilityGroups) {
			if (group.getAttrib("capability") == enumLiteral_PortCapability_Discharge) {
				dischargeGroup = group;
				break;
			}
		}
		if (dischargeGroup == null) {
			return;
		}

		final EObjectWrapper spotMarketsModel = referenceModel.getRef("spotMarketsModel");
		final List<EObjectWrapper> charterInMarkets = spotMarketsModel.getRefAsList("charterInMarkets");

		if (charterInMarkets != null) {
			for (final EObjectWrapper charterInMarket : charterInMarkets) {

				if (charterInMarket.getRef("genericCharterContract") != null) {
					final List<EObjectWrapper> endAt = charterInMarket.getRefAsList("endAt");
					if (endAt == null || endAt.isEmpty()) {
						charterInMarket.setRef("endAt", Collections.singletonList(dischargeGroup));
					}
				}
			}
		}
	}
	
	private void clearUpScheduleModel(final EObjectWrapper scheduleModel, EPackage schedulePackage) {
		if (scheduleModel != null) {
			final EClass class_LumpSumRepositioningFeeTermDetails = MetamodelUtils.getEClass(schedulePackage, "LumpSumRepositioningFeeTermDetails");
			
			final EObjectWrapper schedule = scheduleModel.getRef("schedule");
			if (schedule != null) {
				final TreeIterator<EObject> itr = schedule.eAllContents();
				while (itr.hasNext()) {
					final EObject obj = itr.next();
					if (class_LumpSumRepositioningFeeTermDetails.isInstance(obj)) {
						((EObjectWrapper)obj).unsetFeature("originPort");
					}
				}
			}
		}
	}
	
	private void clearUpAnalyticsModel(final EObjectWrapper analyticsModel, final EPackage analyticsPackage, final EPackage schedulePackage) {
		if (analyticsModel != null) {
			final List<EObjectWrapper> optionModels = analyticsModel.getRefAsList("optionModels");
			if (optionModels != null) {
				for (final EObjectWrapper optionModel : optionModels) {
					clearUpAbstractSolutionSet(optionModel.getRef("results"), schedulePackage);
				}
			}
			final List<EObjectWrapper> optimisations = analyticsModel.getRefAsList("optimisations");
			if (optimisations != null) {
				for (final EObjectWrapper optimisation : optimisations) {
					clearUpAbstractSolutionSet(optimisation, schedulePackage);
				}
			}
		}
	}

	private void clearUpAbstractSolutionSet(final EObjectWrapper abstractSolutionSet, final EPackage schedulePackage) {
		if (abstractSolutionSet != null) {
			clearUpSolutionOption(abstractSolutionSet.getRef("baseOption"), schedulePackage);
			final List<EObjectWrapper> options = abstractSolutionSet.getRefAsList("options");
			if (options != null) {
				for (final EObjectWrapper option : options) {
					clearUpSolutionOption(option, schedulePackage);
				}
			}
		}
	}

	private void clearUpSolutionOption(final EObjectWrapper option, final EPackage schedulePackage) {
		if (option != null) {
			final EObjectWrapper scheduleModel = option.getRef("scheduleModel");
			clearUpScheduleModel(scheduleModel, schedulePackage);
		}
	}
}
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV141 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 140;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 141;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		final @NonNull MetamodelLoader loader = modelRecord.getMetamodelLoader();
		final @NonNull EObjectWrapper scenarioModel = modelRecord.getModelRoot();
		final @NonNull EObjectWrapper referenceModel = scenarioModel.getRef("referenceModel");
		
		final EObjectWrapper cargoModel = scenarioModel.getRef("cargoModel");
		final EObjectWrapper commercialModel = referenceModel.getRef("commercialModel");
		final EObjectWrapper spotMarketModel = referenceModel.getRef("spotMarketsModel");
		
		
		final EPackage commercialPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CommercialModel);
		
		// OLD model description STARTS
		/**
		 * Top logical class
		 * and contains:
		 * minDuration : EInt
		 * maxDuration : EInt
		 */
		final EClass charterContract = MetamodelUtils.getEClass(commercialPackage, "CharterContract");
		/**
		 * implements CharterContract 
		 * and contains:
		 * ballastBonusContract 	: BallastBonusContract
		 * repositioningFee 		: EString
		 */
		final EClass ballastBonusCharterContract = MetamodelUtils.getEClass(commercialPackage, "BallastBonusCharterContract");
		/**
		 * extends BallastBonusCharterContract via SimpleBallastBonustCharterContract
		 */
		final EClass monthlyBallastBonusCharterContract = MetamodelUtils.getEClass(commercialPackage, "MonthlyBallastBonusCharterContract");
		/**
		 * contained by BBCC and MBCCC. Only required as an indicator interface.
		 */
		final EClass ballastBonusContract = MetamodelUtils.getEClass(commercialPackage, "BallastBonusContract");
		/**
		 * is the ONLY implementation of the BallastBonusContract
		 * and contains:
		 * rules 	: List<BallastBonusContractLine>
		 */
		final EClass ruleBasedBallastBonusContract = MetamodelUtils.getEClass(commercialPackage, "RuleBasedBallastBonusContract");
		/**
		 * extends RuleBasedBallastBonusContract
		 * and contaings:
		 * hubs		: APortSet<Port>
		 */
		final EClass monthlyBallastBonusContract = MetamodelUtils.getEClass(commercialPackage, "MonthlyBallastBonusContract");
		/**
		 * indicator interface
		 */
		final EClass ballastBonusContractLine = MetamodelUtils.getEClass(commercialPackage, "BallastBonusContractLine");
		/**
		 * implements BBCL
		 * and contains:
		 * priceExpression	: EString
		 */
		final EClass lumpSumBallastBonusContractLine = MetamodelUtils.getEClass(commercialPackage, "LumpSumBallastBonusContractLine");
		/**
		 * implements BBCL
		 * and contains:
		 * speed					: EDouble
		 * fuelPriceExpression		: EString
		 * hirePriceExpression		: EString
		 * returnPorts				: APortSet<Port>
		 * includeCanal				: EBoolean
		 * includeCanalTime			: EBoolean
		 * lumpSumPriceExpression	: EString
		 */
		final EClass notionalJourneyBallastBonusContractLine = MetamodelUtils.getEClass(commercialPackage, "NotionalJourneyBallastBonusContractLine");
		/**
		 * extends NJBBCL
		 * and contains:
		 * month					: YearMonth
		 * ballastBonusTo			: NextPortType
		 * ballastBonusPctFuel		: EString
		 * ballastBonusPctCharter	: EString
		 */
		final EClass monthlyBallastBonusContractLine = MetamodelUtils.getEClass(commercialPackage, "MonthlyBallastBonusContractLine");
		// OLD model description ENDS
		
		// NEW model description STARTS
		/**
		 * Top logical class
		 * and contains:
		 * minDuration 				: EInt
		 * maxDuration 				: EInt
		 * repositioningFeeTerms 	: IRepositioningFee
		 * ballastBonusTerms 		: IBallastBonus
		 * startHeel 				: StartHeelOptions
		 * endHeel 					: EndHeelOptions
		 */
		final EClass genericCharterContract = MetamodelUtils.getEClass(commercialPackage, "GenericCharterContract");
		
		/**
		 * implements IRepositioningFee
		 * and contains:
		 * terms	: List<RepositioningFeeTerm>
		 */
		final EClass simpleRepositioningFeeContainer = MetamodelUtils.getEClass(commercialPackage, "SimpleRepositioningFeeContainer");
		/**
		 * implement IBallasBonus
		 * and contains:
		 * terms	: List<BallastBonusTerm>
		 */
		final EClass simpleBallastBonusContainer = MetamodelUtils.getEClass(commercialPackage, "SimpleBallastBonusContainer");
		/**
		 * implement IBallasBonus
		 * and contains:
		 * hubs		: APortSet<Port>
		 * terms	: List<MonthlyBallastBonusTerm>
		 */
		final EClass monthlyBallastBonusContainer = MetamodelUtils.getEClass(commercialPackage, "MonthlyBallastBonusContainer");
		
		/**
		 * implements RepositioningFeeTerm and LumpSumTerm
		 * contains:
		 * priceExpression	: EString
		 */
		final EClass lumpSumRepositioningFeeTerm = MetamodelUtils.getEClass(commercialPackage, "LumpSumRepositioningFeeTerm");
		/**
		 * implements BallastBonusTerm and LumpSumTerm
		 * contains:
		 * priceExpression	: EString
		 * redeliveryPorts	: APortSet<Port>
		 */
		final EClass lumpSumBallastBonusTerm = MetamodelUtils.getEClass(commercialPackage, "LumpSumBallastBonusTerm");
		/**
		 * implements BallastBonusTerm and NotionalJourneyTerm
		 * contains:
		 * redeliveryPorts			: APortSet<Port>
		 * speed					: EDouble
		 * fuelPriceExpression		: EString
		 * hirePriceExpression		: EString
		 * returnPorts				: APortSet<Port>
		 * includeCanal				: EBoolean
		 * includeCanalTime			: EBoolean
		 * lumpSumPriceExpression	: EString
		 */
		final EClass notionalJourneyBallastBonusTerm = MetamodelUtils.getEClass(commercialPackage, "NotionalJourneyBallastBonusTerm");
		// NEW model description ENDS
		
		
		final EPackage cargoPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);
		final EClass vesselAvailability = MetamodelUtils.getEClass(cargoPackage, "VesselAvailability");
		
		// go through all of the charter contracts and convert them to generic charter contracts - also, create a map of old - new cc-s
		
		// go through all the spot markets and unset the value in the charter contract(?)
		
		// go through all the vessel availabilities and convert existing bb into the contained cc-s
		
//		final @NonNull EObjectWrapper modelRoot = modelRecord.getModelRoot();
//		final EObjectWrapper referenceModel = modelRoot.getRef("referenceModel");
//		final EObjectWrapper fleetModel = referenceModel.getRef("fleetModel");
//		final List<EObjectWrapper> vessels = fleetModel.getRefAsList("vessels");
//
//		for (final EObjectWrapper vessel : vessels) {
//			vessel.setAttrib("mmxReference", Boolean.FALSE);
//			final EObjectWrapper referenceVessel = vessel.getRef("reference");
//			if (referenceVessel != null && referenceVessel.getRef("reference") == null) {
//				referenceVessel.setAttrib("referenceVessel", Boolean.TRUE);
//			}
//		}
	}
}
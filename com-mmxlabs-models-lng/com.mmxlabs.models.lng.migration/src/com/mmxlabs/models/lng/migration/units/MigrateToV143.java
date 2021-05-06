/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV143 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 142;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 143;
	}
	
	private EPackage schedulePackage;

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		final @NonNull MetamodelLoader loader = modelRecord.getMetamodelLoader();
		final @NonNull EObjectWrapper scenarioModel = modelRecord.getModelRoot();
		final @NonNull EObjectWrapper referenceModel = scenarioModel.getRef("referenceModel");
		
		final EObjectWrapper cargoModel = scenarioModel.getRef("cargoModel");
		final EObjectWrapper analyticsModel = scenarioModel.getRef("analyticsModel");
		final EObjectWrapper commercialModel = referenceModel.getRef("commercialModel");
		final EObjectWrapper spotMarketModel = referenceModel.getRef("spotMarketsModel");
		final EObjectWrapper scheduleModel = scenarioModel.getRef("scheduleModel");
		
		final EPackage commercialPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CommercialModel);
		schedulePackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScheduleModel);
		final EPackage analyticsPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_AnalyticsModel);
		
		final Map<EObjectWrapper, EObjectWrapper> charterContracts = migrateCharterContracts(commercialPackage, commercialModel);
		processCharterMarkets(spotMarketModel, charterContracts);
		processVesselAvailabilities(commercialPackage, cargoModel, charterContracts);
		clearUpCommercialModel(commercialPackage, commercialModel);
		clearUpScheduleModel(scheduleModel);
		clearUpAnalyticsModel(analyticsModel, analyticsPackage);
	}
	
	private Map<EObjectWrapper, EObjectWrapper> migrateCharterContracts(final EPackage commercialPackage, final EObjectWrapper commercialModel) {
		final Map<EObjectWrapper, EObjectWrapper> map = new HashMap<>();
		final EClass class_BallastBonusCharterContract = MetamodelUtils.getEClass(commercialPackage, "BallastBonusCharterContract");
		
		final List<EObjectWrapper> oldCharteringContracts = commercialModel.getRefAsList("charteringContracts");
		List<EObjectWrapper> newCharterContracts = new ArrayList<>();

		if (oldCharteringContracts != null) {
			for (final EObjectWrapper oldCharteringContract : oldCharteringContracts) {
				if (class_BallastBonusCharterContract.isInstance(oldCharteringContract)) {
					final EObjectWrapper oldCharterContract = createCharterContract(oldCharteringContract, commercialPackage);
					newCharterContracts.add(oldCharterContract);
					map.put(oldCharteringContract, oldCharterContract);
				}
			}
		}
		
		if (!newCharterContracts.isEmpty()) {
			commercialModel.setRef("charterContracts", newCharterContracts);
		}
		return map;
	}
	
	private void clearUpCommercialModel(final EPackage commercialPackage, final EObjectWrapper commercialModel) {
		final EClass class_BallastBonusCharterContract = MetamodelUtils.getEClass(commercialPackage, "BallastBonusCharterContract");
		final List<EObjectWrapper> oldCharteringContracts = commercialModel.getRefAsList("charteringContracts");

		if (oldCharteringContracts != null) {
			final Iterator iter = oldCharteringContracts.iterator();
			while(iter.hasNext()) {
				final Object eo = iter.next();
				if (eo instanceof EObjectWrapper) {
					iter.remove();
				}
			}
		}
		
		commercialModel.unsetFeature("charteringContracts");
	}
	
	private void clearUpScheduleModel(final EObjectWrapper scheduleModel) {
		if (scheduleModel != null) {
			final EClass class_ProfitAndLossContainer = MetamodelUtils.getEClass(schedulePackage, "ProfitAndLossContainer");
			final EClass class_BallastBonusFeeDetails = MetamodelUtils.getEClass(schedulePackage, "BallastBonusFeeDetails");

			final EObjectWrapper schedule = scheduleModel.getRef("schedule");
			if (schedule != null) {
				final List<EObjectWrapper> sequences = schedule.getRefAsList("sequences");
				if (sequences != null) {
					for (final EObjectWrapper sequence : sequences) {
						final List<EObjectWrapper> events = sequence.getRefAsList("events");
						if (events != null) {
							for (final EObjectWrapper event : events) {
								if(class_ProfitAndLossContainer.isInstance(event)) {
									final List<EObjectWrapper> generalPNLDetails = event.getRefAsList("generalPNLDetails");
									if (generalPNLDetails != null) {
										final Iterator iter = generalPNLDetails.iterator();
										while(iter.hasNext()) {
											final Object eo = iter.next();
											if (class_BallastBonusFeeDetails.isInstance(eo)) {
												iter.remove();
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void clearUpAnalyticsModel(final EObjectWrapper analyticsModel, final EPackage analyticsPackage) {
		if (analyticsModel != null) {
			final List<EObjectWrapper> optionModels = analyticsModel.getRefAsList("optionModels");
			if (optionModels != null) {
				for(final EObjectWrapper optionModel : optionModels) {
					clearUpAbstractSolutionSet(optionModel.getRef("results"));
				}
			}
			final List<EObjectWrapper> optimisations = analyticsModel.getRefAsList("optimisations");
			if (optimisations != null) {
				for(final EObjectWrapper optimisation : optimisations) {
					clearUpAbstractSolutionSet(optimisation);
				}
			}
			final EClass class_AbstractSolutionSet = MetamodelUtils.getEClass(analyticsPackage, "AbstractSolutionSet");
		}
	}
	
	private void clearUpAbstractSolutionSet(final EObjectWrapper abstractSolutionSet) {
		if (abstractSolutionSet != null) {
			clearUpSolutionOption(abstractSolutionSet.getRef("baseOption"));
			final List<EObjectWrapper> options = abstractSolutionSet.getRefAsList("options");
			if (options != null) {
				for(final EObjectWrapper option : options) {
					clearUpSolutionOption(option);
				}
			}
		}
	}
	
	private void clearUpSolutionOption(final EObjectWrapper option) {
		if (option != null) {
			final EObjectWrapper scheduleModel = option.getRef("scheduleModel");
			clearUpScheduleModel(scheduleModel);
		}
	}
	
	private EObjectWrapper createCharterContract(final EObjectWrapper oldCharterContract, final EPackage commercialPackage) {
		final EClass class_GenericCharterContract = MetamodelUtils.getEClass(commercialPackage, "GenericCharterContract");
		final EClass class_StartHeelOptions = MetamodelUtils.getEClass(commercialPackage, "StartHeelOptions");
		final EClass class_EndHeelOptions = MetamodelUtils.getEClass(commercialPackage, "EndHeelOptions");
		//
		final Integer minDuration = oldCharterContract.getAttrib("minDuration");
		final Integer maxDuration = oldCharterContract.getAttrib("maxDuration");
		
		final EObjectWrapper newGenericCharterContract = (EObjectWrapper) commercialPackage.getEFactoryInstance().create(class_GenericCharterContract);
		newGenericCharterContract.setAttrib("name", oldCharterContract.getAttrib("name"));
		if (minDuration != null) {
			newGenericCharterContract.setAttrib("minDuration", minDuration);
		}
		if (maxDuration != null) {
			newGenericCharterContract.setAttrib("maxDuration", maxDuration);
		}
		
		newGenericCharterContract.setRef("startHeel", commercialPackage.getEFactoryInstance().create(class_StartHeelOptions));
		newGenericCharterContract.setRef("endHeel", commercialPackage.getEFactoryInstance().create(class_EndHeelOptions));
		newGenericCharterContract.setRef("repositioningFeeTerms", createRepositioningFeeContainer(commercialPackage, oldCharterContract.getAttrib("repositioningFee")));
		newGenericCharterContract.setRef("ballastBonusTerms", createBallastBonusContainer(commercialPackage, oldCharterContract.getRef("ballastBonusContract"), oldCharterContract.getAttrib("name")));
		
		return newGenericCharterContract;
	}
	
	private EObjectWrapper createRepositioningFeeContainer(final EPackage commercialPackage, final String repositioningFeePriceExpression) {
		final EClass class_SimpleRepositioningFeeContainer = MetamodelUtils.getEClass(commercialPackage, "SimpleRepositioningFeeContainer");
		final EClass class_lumpSumRepositioningFeeTerm = MetamodelUtils.getEClass(commercialPackage, "LumpSumRepositioningFeeTerm");
		
		final EObjectWrapper simpleRepositioningFeeContrainer = (EObjectWrapper) commercialPackage.getEFactoryInstance().create(class_SimpleRepositioningFeeContainer);
		if (repositioningFeePriceExpression != null && !repositioningFeePriceExpression.isEmpty()) {
			final EObjectWrapper lumpSumRepositioningFeeTerm = (EObjectWrapper) commercialPackage.getEFactoryInstance().create(class_lumpSumRepositioningFeeTerm);
			lumpSumRepositioningFeeTerm.setAttrib("priceExpression", repositioningFeePriceExpression);
			final List<EObjectWrapper> terms = new ArrayList<>();
			terms.add(lumpSumRepositioningFeeTerm);
			simpleRepositioningFeeContrainer.setRef("terms", terms);
		}
		
		return simpleRepositioningFeeContrainer;
	}
	
	private EObjectWrapper createBallastBonusContainer(final EPackage commercialPackage, final EObjectWrapper oldBallastBonusContainer, final String name) {
		// OLD STUFF
		final EClass class_RuleBasedBallastBonusContract = MetamodelUtils.getEClass(commercialPackage, "RuleBasedBallastBonusContract");
		final EClass class_MonthlyBallastBonusContract = MetamodelUtils.getEClass(commercialPackage, "MonthlyBallastBonusContract");
		
		final EObjectWrapper ballastBonusContrainer;
		if (class_MonthlyBallastBonusContract.isInstance(oldBallastBonusContainer)) {
			ballastBonusContrainer = createMonthlyBallastBonusContainer(commercialPackage, oldBallastBonusContainer);
		} else if (class_RuleBasedBallastBonusContract.isInstance(oldBallastBonusContainer)) {
			ballastBonusContrainer = createSimpleBallastBonusContainer(commercialPackage, oldBallastBonusContainer);
		} else {
			throw new IllegalArgumentException(String.format("Chartering contract %s has unknown ballast bonus contract type!", name));
		}
		return ballastBonusContrainer;
	}
	
	private EObjectWrapper createSimpleBallastBonusContainer(final EPackage commercialPackage, final @NonNull EObjectWrapper oldBallastBonusContract) {
		final EClass class_SimpleBallastBonusContainer = MetamodelUtils.getEClass(commercialPackage, "SimpleBallastBonusContainer");
		// OLD
		final EClass class_lumpSumBallastBonusContractLine = MetamodelUtils.getEClass(commercialPackage, "LumpSumBallastBonusContractLine");
		final EClass class_notionalJourneyBallastBonusContractLine = MetamodelUtils.getEClass(commercialPackage, "NotionalJourneyBallastBonusContractLine");
		// NEW
		final EClass class_lumpSumBallastBonusTerm = MetamodelUtils.getEClass(commercialPackage, "LumpSumBallastBonusTerm");
		final EClass class_notionalJourneyBallastBonusTerm = MetamodelUtils.getEClass(commercialPackage, "NotionalJourneyBallastBonusTerm");
		
		final EObjectWrapper ballastBonusContrainer = (EObjectWrapper) commercialPackage.getEFactoryInstance().create(class_SimpleBallastBonusContainer);
		
		final List<EObjectWrapper> rules = oldBallastBonusContract.getRefAsList("rules");
		final List<EObjectWrapper> terms = new ArrayList<>();
		if (rules != null) {
			for (final EObjectWrapper rule : rules) {
				EObjectWrapper term = null;
				if (class_lumpSumBallastBonusContractLine.isInstance(rule)) {
					term = (EObjectWrapper) commercialPackage.getEFactoryInstance().create(class_lumpSumBallastBonusTerm);
					term.setRef("redeliveryPorts", rule.getRefAsList("redeliveryPorts"));
					term.setAttrib("priceExpression", rule.getAttrib("priceExpression"));
					
				} else if (class_notionalJourneyBallastBonusContractLine.isInstance(rule)) {
					term = (EObjectWrapper) commercialPackage.getEFactoryInstance().create(class_notionalJourneyBallastBonusTerm);
					term.setRef("redeliveryPorts", rule.getRefAsList("redeliveryPorts"));
					term.setAttrib("speed", rule.getAttrib("speed"));
					term.setAttrib("fuelPriceExpression", rule.getAttrib("fuelPriceExpression"));
					term.setAttrib("hirePriceExpression", rule.getAttrib("hirePriceExpression"));
					term.setRef("returnPorts", rule.getRefAsList("returnPorts"));
					term.setAttrib("includeCanal", rule.getAttrib("includeCanal"));
					term.setAttrib("includeCanalTime", rule.getAttrib("includeCanalTime"));
					term.setAttrib("lumpSumPriceExpression", rule.getAttrib("lumpSumPriceExpression"));
				}
				if (term != null) {
					terms.add(term);
				}
			}
		}
		if (!terms.isEmpty()) {
			ballastBonusContrainer.setRef("terms", terms);
		}
		return ballastBonusContrainer;
	}
	
	private EObjectWrapper createMonthlyBallastBonusContainer(final EPackage commercialPackage, final @NonNull EObjectWrapper oldBallastBonusContract) {
		final EClass class_MonthlyBallastBonusContainer = MetamodelUtils.getEClass(commercialPackage, "MonthlyBallastBonusContainer");
		// OLD
		final EClass class_monthlyBallastBonusContractLine = MetamodelUtils.getEClass(commercialPackage, "MonthlyBallastBonusContractLine");
		// NEW
		final EClass class_monthlyBallastBonusTerm = MetamodelUtils.getEClass(commercialPackage, "MonthlyBallastBonusTerm");
		
		final EObjectWrapper ballastBonusContainer = (EObjectWrapper) commercialPackage.getEFactoryInstance().create(class_MonthlyBallastBonusContainer);
		ballastBonusContainer.setRef("hubs", oldBallastBonusContract.getRefAsList("hubs"));
		
		final List<EObjectWrapper> rules = oldBallastBonusContract.getRefAsList("rules");
		final List<EObjectWrapper> terms = new ArrayList<>();
		if (rules != null) {
			for (final EObjectWrapper rule : rules) {
				EObjectWrapper term = null;
				if (class_monthlyBallastBonusContractLine.isInstance(rule)) {
					term = (EObjectWrapper) commercialPackage.getEFactoryInstance().create(class_monthlyBallastBonusTerm);
					term.setRef("redeliveryPorts", rule.getRefAsList("redeliveryPorts"));
					term.setAttrib("speed", rule.getAttrib("speed"));
					term.setAttrib("fuelPriceExpression", rule.getAttrib("fuelPriceExpression"));
					term.setAttrib("hirePriceExpression", rule.getAttrib("hirePriceExpression"));
					term.setRef("returnPorts", rule.getRefAsList("returnPorts"));
					term.setAttrib("includeCanal", rule.getAttrib("includeCanal"));
					term.setAttrib("includeCanalTime", rule.getAttrib("includeCanalTime"));
					term.setAttrib("lumpSumPriceExpression", rule.getAttrib("lumpSumPriceExpression"));
					term.setAttrib("month", rule.getAttrib("month"));
					//FIXME: we can't migrate ballastTo, since it is different enum in different ecores
					term.setAttrib("ballastBonusPctFuel", rule.getAttrib("ballastBonusPctFuel"));
					term.setAttrib("ballastBonusPctCharter", rule.getAttrib("ballastBonusPctCharter"));
				}
				if (term != null) {
					terms.add(term);
				}
			}
			
		}
		if (!terms.isEmpty()) {
			ballastBonusContainer.setRef("terms", terms);
		}
		return ballastBonusContainer;
	}
	
	private void processCharterMarkets(final EObjectWrapper spotMarketModel, final Map<EObjectWrapper, EObjectWrapper> charterContracts) {
		final List<EObjectWrapper> charterInMarkets = spotMarketModel.getRefAsList("charterInMarkets");
		if (charterInMarkets != null && !charterContracts.isEmpty()) {
			for (final EObjectWrapper charterInMarket : charterInMarkets) {
				final EObjectWrapper oldCharterContract = charterInMarket.getRef("charterContract");
				if (oldCharterContract != null) {
					final EObjectWrapper newCharterContract = charterContracts.get(oldCharterContract);
					if (newCharterContract != null) {
						charterInMarket.setRef("genericCharterContract", newCharterContract);
					} else {
						throw new IllegalArgumentException(String.format("Charter contract %s could not be migrated. Please report to Minimax!", oldCharterContract.getAttrib("name")));
					}
				}
				charterInMarket.unsetFeature("charterContract");
			}
		}
	}
	
	private void processVesselAvailabilities(final EPackage commercialPackage, final EObjectWrapper cargoModel, final Map<EObjectWrapper, EObjectWrapper> charterContracts) {
		final List<EObjectWrapper> vesselAvailabilities = cargoModel.getRefAsList("vesselAvailabilities");
		if (vesselAvailabilities != null) {
			for (final EObjectWrapper vesselAvailability : vesselAvailabilities) {
				final EObjectWrapper oldCharterContract = vesselAvailability.getRef("charterContract");
				if (oldCharterContract != null) {
					final EObjectWrapper newCharterContract = charterContracts.get(oldCharterContract);
					if (newCharterContract != null) {
						vesselAvailability.setRef("genericCharterContract", newCharterContract);
					} else {
						throw new IllegalArgumentException(String.format("Charter contract %s could not be migrated. Please report to Minimax!", oldCharterContract.getAttrib("uuid")));
					}
				}
				
				final String name = String.format("contained_charter_contract_%s_%d", vesselAvailability.getAttrib("uuid"), vesselAvailability.getAttrib("charterNumber"));
				final String repositioningFee = vesselAvailability.getAttrib("repositioningFee");
				final EObjectWrapper oldBallastBonusContainer = vesselAvailability.getRef("ballastBonusContract");
				if ((repositioningFee != null && !repositioningFee.isEmpty()) || (oldBallastBonusContainer != null)){
					vesselAvailability.setRef("containedCharterContract", createContainedCharterContract(commercialPackage, repositioningFee, oldBallastBonusContainer, name));
					vesselAvailability.setAttrib("charterContractOverride", Boolean.TRUE);
				}
				
				vesselAvailability.unsetFeature("ballastBonusContract");
				vesselAvailability.unsetFeature("repositioningFee");
				vesselAvailability.unsetFeature("charterContract");
			}
		}
	}
	
	private EObjectWrapper createContainedCharterContract(final EPackage commercialPackage, final String repositioningFee, final EObjectWrapper oldBallastBonusContainer, final String name) {
		final EClass class_GenericCharterContract = MetamodelUtils.getEClass(commercialPackage, "GenericCharterContract");
		final EClass class_StartHeelOptions = MetamodelUtils.getEClass(commercialPackage, "StartHeelOptions");
		final EClass class_EndHeelOptions = MetamodelUtils.getEClass(commercialPackage, "EndHeelOptions");
		
		final EObjectWrapper newGenericCharterContract = (EObjectWrapper) commercialPackage.getEFactoryInstance().create(class_GenericCharterContract);
		newGenericCharterContract.setAttrib("name", name);
		
		newGenericCharterContract.setRef("startHeel", commercialPackage.getEFactoryInstance().create(class_StartHeelOptions));
		newGenericCharterContract.setRef("endHeel", commercialPackage.getEFactoryInstance().create(class_EndHeelOptions));
		if (repositioningFee != null && !repositioningFee.isEmpty()) {
			newGenericCharterContract.setRef("repositioningFeeTerms", createRepositioningFeeContainer(commercialPackage, repositioningFee));
		}
		if (oldBallastBonusContainer != null) {
			newGenericCharterContract.setRef("ballastBonusTerms", createBallastBonusContainer(commercialPackage, oldBallastBonusContainer, name));
		}
		return newGenericCharterContract;
	}
	
	/**
	 * Does NOTHING
	 * @param commercialPackage
	 */
	private void modelDescription(final EPackage commercialPackage) {
		// OLD model description STARTS
		/**
		 * Top logical class
		 * and contains:
		 * minDuration : EInt
		 * maxDuration : EInt
		 */
		final EClass class_CharterContract = MetamodelUtils.getEClass(commercialPackage, "CharterContract");
		/**
		 * implements CharterContract 
		 * and contains:
		 * ballastBonusContract 	: BallastBonusContract
		 * repositioningFee 		: EString
		 */
		final EClass class_BallastBonusCharterContract = MetamodelUtils.getEClass(commercialPackage, "BallastBonusCharterContract");
		/**
		 * extends BallastBonusCharterContract via SimpleBallastBonustCharterContract
		 */
		final EClass class_MonthlyBallastBonusCharterContract = MetamodelUtils.getEClass(commercialPackage, "MonthlyBallastBonusCharterContract");
		/**
		 * contained by BBCC and MBCCC. Only required as an indicator interface.
		 */
		final EClass class_ballastBonusContract = MetamodelUtils.getEClass(commercialPackage, "BallastBonusContract");
		/**
		 * is the ONLY implementation of the BallastBonusContract
		 * and contains:
		 * rules 	: List<BallastBonusContractLine>
		 */
		final EClass class_RuleBasedBallastBonusContract = MetamodelUtils.getEClass(commercialPackage, "RuleBasedBallastBonusContract");
		/**
		 * extends RuleBasedBallastBonusContract
		 * and contaings:
		 * hubs		: APortSet<Port>
		 */
		final EClass class_MonthlyBallastBonusContract = MetamodelUtils.getEClass(commercialPackage, "MonthlyBallastBonusContract");
		/**
		 * indicator interface
		 * and contains:
		 * redeliveryPorts	: APortSet<Port>
		 */
		final EClass class_ballastBonusContractLine = MetamodelUtils.getEClass(commercialPackage, "BallastBonusContractLine");
		/**
		 * implements BBCL
		 * and contains:
		 * priceExpression	: EString
		 */
		final EClass class_lumpSumBallastBonusContractLine = MetamodelUtils.getEClass(commercialPackage, "LumpSumBallastBonusContractLine");
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
		final EClass class_notionalJourneyBallastBonusContractLine = MetamodelUtils.getEClass(commercialPackage, "NotionalJourneyBallastBonusContractLine");
		/**
		 * extends NJBBCL
		 * and contains:
		 * month					: YearMonth
		 * ballastBonusTo			: NextPortType
		 * ballastBonusPctFuel		: EString
		 * ballastBonusPctCharter	: EString
		 */
		final EClass class_monthlyBallastBonusContractLine = MetamodelUtils.getEClass(commercialPackage, "MonthlyBallastBonusContractLine");
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
		final EClass class_GenericCharterContract = MetamodelUtils.getEClass(commercialPackage, "GenericCharterContract");

		/**
		 * implements IRepositioningFee
		 * and contains:
		 * terms	: List<RepositioningFeeTerm>
		 */
		final EClass class_SimpleRepositioningFeeContainer = MetamodelUtils.getEClass(commercialPackage, "SimpleRepositioningFeeContainer");
		/**
		 * implement IBallasBonus
		 * and contains:
		 * terms	: List<BallastBonusTerm>
		 */
		final EClass class_SimpleBallastBonusContainer = MetamodelUtils.getEClass(commercialPackage, "SimpleBallastBonusContainer");
		/**
		 * implement IBallasBonus
		 * and contains:
		 * hubs		: APortSet<Port>
		 * terms	: List<MonthlyBallastBonusTerm>
		 */
		final EClass class_MonthlyBallastBonusContainer = MetamodelUtils.getEClass(commercialPackage, "MonthlyBallastBonusContainer");

		/**
		 * implements RepositioningFeeTerm and LumpSumTerm
		 * contains:
		 * priceExpression	: EString
		 */
		final EClass class_lumpSumRepositioningFeeTerm = MetamodelUtils.getEClass(commercialPackage, "LumpSumRepositioningFeeTerm");
		/**
		 * implements BallastBonusTerm and LumpSumTerm
		 * contains:
		 * priceExpression	: EString
		 * redeliveryPorts	: APortSet<Port>
		 */
		final EClass class_lumpSumBallastBonusTerm = MetamodelUtils.getEClass(commercialPackage, "LumpSumBallastBonusTerm");
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
		final EClass class_notionalJourneyBallastBonusTerm = MetamodelUtils.getEClass(commercialPackage, "NotionalJourneyBallastBonusTerm");
		/**
		 * extends NJBBCL
		 * and contains:
		 * month					: YearMonth
		 * ballastBonusTo			: NextPortType
		 * ballastBonusPctFuel		: EString
		 * ballastBonusPctCharter	: EString
		 */
		final EClass class_monthlyBallastBonusTerm = MetamodelUtils.getEClass(commercialPackage, "MonthlyBallastBonusTerm");
		// NEW model description ENDS
	}
	
}
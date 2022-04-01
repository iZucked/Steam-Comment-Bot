/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.lng.nominations.Side;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV109 extends AbstractMigrationUnit {

	private static final String[] nominationTypes = { "window", "volume", "vessel", "port", "portLoad" };

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 108;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 109;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {

		final Map<String, EObjectWrapper> contractName_NominationTypeToNominationSpec = new HashMap<>();

		final EObjectWrapper scenarioModel = modelRecord.getModelRoot();
		final EObjectWrapper referenceModel = scenarioModel.getRef("referenceModel");

		// Nominations model migration.
		final EPackage nominationsPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_NominationsModel);
		final EClass nominationsModelClass = MetamodelUtils.getEClass(nominationsPackage, "NominationsModel");
		final EClass slotNominationClass = MetamodelUtils.getEClass(nominationsPackage, "SlotNomination");
		final EFactory nominationsFactory = nominationsPackage.getEFactoryInstance();
		final EEnum sideEnum = MetamodelUtils.getEEnum(nominationsPackage, "Side");
		final EEnumLiteral buyLiteral = MetamodelUtils.getEEnum_Literal(sideEnum, "Buy");
		final EEnumLiteral sellLiteral = MetamodelUtils.getEEnum_Literal(sideEnum, "Sell");

		EObjectWrapper nominationsModel = scenarioModel.getRef("nominationsModel");

		if (nominationsModel == null) {
			nominationsModel = (EObjectWrapper) nominationsFactory.create(nominationsModelClass);
			scenarioModel.setRef("nominationsModel", nominationsModel);
		}

//		EObjectWrapper nominationParameters = nominationsModel.getRef("nominationParameters");
//		if (nominationParameters == null) {
//			final EClass nominationsParametersClass = MetamodelUtils.getEClass(nominationsPackage, "NominationsParameters");
//			nominationParameters = (EObjectWrapper) nominationsFactory.create(nominationsParametersClass);
//			nominationsModel.setRef("nominationParameters", nominationParameters);
//		}

		// Process contracts first of all.
		final EPackage commercialPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CommercialModel);
		final EClass commercialModelClass = MetamodelUtils.getEClass(commercialPackage, "CommercialModel");

		final EObjectWrapper commercialModel = referenceModel.getRef("commercialModel");
		final List<EObjectWrapper> purchaseContracts = commercialModel.getRefAsList("purchaseContracts");

		final EClass slotNominationSpecClass = MetamodelUtils.getEClass(nominationsPackage, "SlotNominationSpec");

		final List<EObjectWrapper> slotNominationSpecs = new LinkedList<>();
		processContracts(buyLiteral, purchaseContracts, nominationsFactory, slotNominationSpecClass, slotNominationSpecs, contractName_NominationTypeToNominationSpec,
				modelRecord.getMetamodelLoader());

		final List<EObjectWrapper> salesContracts = commercialModel.getRefAsList("salesContracts");

		processContracts(sellLiteral, salesContracts, nominationsFactory, slotNominationSpecClass, slotNominationSpecs, contractName_NominationTypeToNominationSpec, modelRecord.getMetamodelLoader());

		nominationsModel.setRef("nominationSpecs", slotNominationSpecs);

		// Copy across old nominations into nominations model.
		final EPackage cargoPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);
		final EClass cargoModelClass = MetamodelUtils.getEClass(cargoPackage, "CargoModel");
		final EObjectWrapper cargoModel = scenarioModel.getRef("cargoModel");
		final List<EObjectWrapper> slotNominations = new LinkedList<>();

		final List<EObjectWrapper> loadSlots = cargoModel.getRefAsList("loadSlots");
		processSlots(buyLiteral, loadSlots, nominationsFactory, slotNominationClass, slotNominations, contractName_NominationTypeToNominationSpec);

		final List<EObjectWrapper> dischargeSlots = cargoModel.getRefAsList("dischargeSlots");
		processSlots(sellLiteral, dischargeSlots, nominationsFactory, slotNominationClass, slotNominations, contractName_NominationTypeToNominationSpec);

		nominationsModel.setRef("nominations", slotNominations);

		// remove old nominations features on contracts.
		purchaseContracts.forEach(pc -> removeObsoleteNominationFeaturesFromContract(pc));
		salesContracts.forEach(sc -> removeObsoleteNominationFeaturesFromContract(sc));

		// remove old nominations features on slots.
		loadSlots.forEach(ls -> removeObsoleteNominationFeaturesFromSlot(ls));
		dischargeSlots.forEach(ds -> removeObsoleteNominationFeaturesFromSlot(ds));
	}

	private void removeObsoleteNominationFeaturesFromSlot(EObjectWrapper slot) {
		for (final String nominationType : nominationTypes) {
			slot.unsetFeature(nominationType + "NominationDate");
			if (nominationType.equals("window")) {
				slot.unsetFeature(nominationType + "NominationIsDone");
			} else {
				slot.unsetFeature(nominationType + "NominationDone");
			}
			slot.unsetFeature(nominationType + "NominationCounterparty");
			slot.unsetFeature(nominationType + "NominationComment");
		}
	}

	private void removeObsoleteNominationFeaturesFromContract(EObjectWrapper contract) {
		for (final String nominationType : nominationTypes) {
			contract.unsetFeature(nominationType + "NominationSize");
			contract.unsetFeature(nominationType + "NominationSizeUnits");
			contract.unsetFeature(nominationType + "NominationCounterparty");
		}
	}

	private void processContracts(final EEnumLiteral side, final List<EObjectWrapper> contracts, final EFactory nominationsFactory, final EClass nominationSpecClass,
			final List<EObjectWrapper> nominationSpecs, Map<String, EObjectWrapper> contractName_NominationTypeToNominationSpec, MetamodelLoader loader) {
		for (final EObjectWrapper contract : contracts) {
			for (final String nominationType : nominationTypes) {
				final EObjectWrapper nominationSpec = getNominationSpecFromContract(side, contract, nominationType, nominationsFactory, nominationSpecClass, loader);
				if (nominationSpec != null) {
					nominationSpecs.add(nominationSpec);
					final String name = contract.getAttrib("name");
					contractName_NominationTypeToNominationSpec.put(name + "_" + nominationType, nominationSpec);
				}
			}
		}
	}

	private void processSlots(final EEnumLiteral side, final List<EObjectWrapper> slots, final EFactory nominationsFactory, final EClass nominationClass, final List<EObjectWrapper> nominations,
			Map<String, EObjectWrapper> contractName_NominationTypeToNominationSpec) {
		for (final EObjectWrapper slot : slots) {
			for (final String nominationType : nominationTypes) {
				final EObjectWrapper nomination = getNominationFromSlot(side, slot, nominationType, nominationsFactory, nominationClass, contractName_NominationTypeToNominationSpec);
				if (nomination != null) {
					nominations.add(nomination);
				}
			}
		}
	}

	private EObjectWrapper getNominationFromSlot(final EEnumLiteral side, final EObjectWrapper slot, final String nominationType, final EFactory nominationsFactory, final EClass nominationClass,
			Map<String, EObjectWrapper> contractName_NominationTypeToNominationSpec) {
		EObjectWrapper nomination = null;

		// Check if slot has a contract associated with it.
		final EObjectWrapper contract = slot.getRef("contract");
		EObjectWrapper spec = null;
		if (contract != null) {
			// Get the spec.
			spec = contractName_NominationTypeToNominationSpec.get(contract.getAttrib("name") + "_" + nominationType);
		}

		// Get any nominations fields which have been set on the slot for this
		// nomination type.
		final LocalDate dueDate = (LocalDate) slot.getAttrib(nominationType + "NominationDate");
		Boolean done = null;
		if ("window".equals(nominationType)) {
			if (slot.isSetFeature(nominationType + "NominationIsDone")) {
				done = slot.getAttribAsBooleanObject(nominationType + "NominationIsDone");
			}
		} else {
			if (slot.isSetFeature(nominationType + "NominationDone")) {
				done = slot.getAttribAsBooleanObject(nominationType + "NominationDone");
			}
		}
		Boolean counterParty = null;
		if (slot.isSetFeature(nominationType + "NominationCounterparty")) {
			counterParty = slot.getAttribAsBooleanObject(nominationType + "NominationCounterparty");
		}
		final String comment = (String) slot.getAttrib(nominationType + "NominationComment");

		// Create a nomination with the fields above set, if any have been.
		if (dueDate != null || done != null || (comment != null && !comment.trim().isEmpty())) {
			nomination = (EObjectWrapper) nominationsFactory.create(nominationClass);
			nomination.setAttrib("uuid", EcoreUtil.generateUUID());
			final String name = slot.getAttrib("name");
			if (name != null) {
				nomination.setAttrib("nomineeId", name);
			}
			if (spec != null) {
				final String specUuid = spec.getAttrib("uuid");
				nomination.setAttrib("specUuid", specUuid);
			}
			nomination.setAttrib("side", side);
			nomination.setAttrib("type", getNominationTypeName(side.getValue(), nominationType));
			if (dueDate != null) {
				nomination.setAttrib("dueDate", dueDate);
			}
			nomination.setAttrib("done", done);
			nomination.setAttrib("counterparty", counterParty);
			nomination.setAttrib("remark", comment);

			if (dueDate != null) {
				nomination.setAttrib("alertDate", generateDefaultAlertDate(dueDate));
			}
			// [Derived from slot: from, to, cn, type, date].
		}
		return nomination;
	}

	private LocalDate generateDefaultAlertDate(final LocalDate date) {
		return date.minusDays(10);
	}

	private String getNominationTypeName(final int sideValue, final String nominationType) {
		// "window", "volume", "vessel", "port", "portLoad"
		if ("window".equals(nominationType)) {
			return Side.get(sideValue).getName() + " " + nominationType;
		} else if ("volume".equals(nominationType)) {
			switch (sideValue) {
			case Side.BUY_VALUE:
				return "Load volume";
			case Side.SELL_VALUE:
				return "Discharge volume";
			}
		} else if ("port".equals(nominationType)) {
			switch (sideValue) {
			case Side.BUY_VALUE:
				return "Load port";
			case Side.SELL_VALUE:
				return "Discharge port";
			}
		} else if ("portLoad".equals(nominationType)) {
			switch (sideValue) {
			case Side.BUY_VALUE:
				return "Discharge port";
			case Side.SELL_VALUE:
				return "Load port";
			}
		} else if ("vessel".equals(nominationType)) {
			return "Vessel";
		}
		return nominationType;
	}

	private EObjectWrapper getNominationSpecFromContract(final EEnumLiteral side, final EObjectWrapper contract, final String nominationType, final EFactory nominationsFactory,
			final EClass nominationSpecClass, MetamodelLoader loader) {
		final Object size = contract.getAttrib(nominationType + "NominationSize");
		if ((Integer) size == 0) {
			return null;
		}

		final EPackage typesPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_LNGTypes);
		final EPackage nominationsPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_NominationsModel);

		EEnum timePeriodEnum;
		EEnumLiteral timePeriodMonthLiteral;
		EEnumLiteral timePeriodDayLiteral;

		EEnum datePeriodPriorEnum;
		EEnumLiteral datePeriodPriorMonthLiteral;
		EEnumLiteral datePeriodPriorDayLiteral;
		timePeriodEnum = MetamodelUtils.getEEnum(typesPackage, "TimePeriod");
		timePeriodMonthLiteral = MetamodelUtils.getEEnum_Literal(timePeriodEnum, "MONTHS");
		timePeriodDayLiteral = MetamodelUtils.getEEnum_Literal(timePeriodEnum, "DAYS");

		datePeriodPriorEnum = MetamodelUtils.getEEnum(nominationsPackage, "DatePeriodPrior");
		datePeriodPriorMonthLiteral = MetamodelUtils.getEEnum_Literal(datePeriodPriorEnum, "MonthsPrior");
		datePeriodPriorDayLiteral = MetamodelUtils.getEEnum_Literal(datePeriodPriorEnum, "DaysPrior");

		final EObjectWrapper nominationSpec = (EObjectWrapper) nominationsFactory.create(nominationSpecClass);
		nominationSpec.setAttrib("uuid", EcoreUtil.generateUUID());
		nominationSpec.setAttrib("type", getNominationTypeName(side.getValue(), nominationType));
		final String name = contract.getAttrib("name");
		if (name != null) {
			nominationSpec.setAttrib("refererId", name);
		}
		nominationSpec.setAttrib("size", size);
		final EEnumLiteral tp = contract.getAttrib(nominationType + "NominationSizeUnits");
		switch (tp.getValue()) {
		case TimePeriod.MONTHS_VALUE:
			nominationSpec.setAttrib("sizeUnits", datePeriodPriorMonthLiteral);
			nominationSpec.setAttrib("alertSize", (Integer) size + 1);
			nominationSpec.setAttrib("alertSizeUnits", datePeriodPriorMonthLiteral);
			break;
		case TimePeriod.DAYS_VALUE:
		case TimePeriod.HOURS_VALUE:
			nominationSpec.setAttrib("sizeUnits", datePeriodPriorDayLiteral);
			nominationSpec.setAttrib("alertSize", (Integer) size + 10);
			nominationSpec.setAttrib("alertSizeUnits", datePeriodPriorDayLiteral);
			break;
		}
		nominationSpec.setAttrib("side", side);
		final Boolean counterParty = contract.getAttribAsBooleanObject(nominationType + "NominationCounterparty");
		nominationSpec.setAttrib("counterparty", counterParty);
		return nominationSpec;
	}
}

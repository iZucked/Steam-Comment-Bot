/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;
import java.util.function.Consumer;
import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;

public class MigrateToV50 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 49;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 50;
	}

	@Override
	protected void doMigration(final MigrationModelRecord modelRecord) {
		final EObjectWrapper model = modelRecord.getModelRoot();

		final EObjectWrapper referenceModel = model.getRef("referenceModel");
		if (referenceModel == null) {
			return;
		}

		// Consumer to update the cancellation fee as an expression
		final Consumer<EObjectWrapper> slotOrContractUpdater = (contract) -> {
			if (contract.isSetFeature("cancellationFee")) {
				final int fee = contract.getAttrib("cancellationFee");
				contract.setAttrib("cancellationExpression", Integer.toString(fee));
				contract.unsetFeature("cancellationFee");
			}
		};

		final EObjectWrapper commercialModel = referenceModel.getRef("commercialModel");
		if (commercialModel != null) {
			final List<EObjectWrapper> purchaseContracts = commercialModel.getRefAsList("purchaseContracts");
			if (purchaseContracts != null) {
				purchaseContracts.forEach(slotOrContractUpdater);
			}
			final List<EObjectWrapper> salesContracts = commercialModel.getRefAsList("salesContracts");
			if (salesContracts != null) {
				salesContracts.forEach(slotOrContractUpdater);
			}
		}

		final EObjectWrapper cargoModel = model.getRef("cargoModel");
		if (cargoModel != null) {
			final List<EObjectWrapper> loadSlots = cargoModel.getRefAsList("loadSlots");
			if (loadSlots != null) {
				loadSlots.forEach(slotOrContractUpdater);
			}
			final List<EObjectWrapper> dischargeSlots = cargoModel.getRefAsList("dischargeSlots");
			if (dischargeSlots != null) {
				dischargeSlots.forEach(slotOrContractUpdater);
			}
		}
	}
}

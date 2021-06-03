/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV142 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 141;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 142;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {

		final EObjectWrapper scenarioModel = modelRecord.getModelRoot();

		final EPackage cargoPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.PKG_DATA_CargoModel.getNsURI());
		final EFactory cargoFactory = cargoPackage.getEFactoryInstance();
		final EClass vesselGroupCanalParametersClass = MetamodelUtils.getEClass(cargoPackage, "VesselGroupCanalParameters");
		EObjectWrapper defaultVesselGroupParameters = (EObjectWrapper) cargoFactory.create(vesselGroupCanalParametersClass);

		final EObjectWrapper cargoModel = scenarioModel.getRef("cargoModel");
		final EObjectWrapper canalBookingParameters = cargoModel.getRef("canalBookings");
		if (canalBookingParameters != null) {
			// Get the old max idle times and create default vessel group waiting time parameters.
			int oldNorthboundMaxIdleDays = canalBookingParameters.getAttrib("northboundMaxIdleDays");
			int oldSouthboundMaxIdleDays = canalBookingParameters.getAttrib("southboundMaxIdleDays");
			defaultVesselGroupParameters.setAttrib("name", "default");
			defaultVesselGroupParameters.setAttrib("northboundWaitingDays", oldNorthboundMaxIdleDays);
			defaultVesselGroupParameters.setAttrib("southboundWaitingDays", oldSouthboundMaxIdleDays);
			canalBookingParameters.unsetFeature("northboundMaxIdleDays");
			canalBookingParameters.unsetFeature("southboundMaxIdleDays");

			final List<EObjectWrapper> vesselGroupCanalParameters = canalBookingParameters.getRefAsList("vesselGroupCanalParameters");
			vesselGroupCanalParameters.add(defaultVesselGroupParameters);

			// Get the slots on each booking - convert them to vessels.
			final List<EObjectWrapper> bookings = canalBookingParameters.getRefAsList("canalBookingSlots");

			for (EObjectWrapper booking : bookings) {
				EObjectWrapper slot = booking.getRef("slot");
				EObjectWrapper vessel = getVessel(cargoModel, slot);
				if (vessel != null) {
					booking.setRef("vessel", vessel);
				}
				booking.unsetFeature("slot");
				booking.setRef("bookingCode", defaultVesselGroupParameters);
			}
		}
	}

	EObjectWrapper getVessel(EObjectWrapper cargoModel, EObjectWrapper slot) {
		if (slot != null) {
			final List<EObjectWrapper> cargoes = cargoModel.getRefAsList("cargoes");
			for (EObjectWrapper c : cargoes) {
				if (c.getRefAsList("slots").contains(slot)) {
					EObjectWrapper vat = c.getRef("vesselAssignmentType");
					if (vat != null && vat.hasFeature("vessel")) {
						EObjectWrapper vessel = vat.getRef("vessel");
						return vessel;
					}
				}
			}
		}
		// Unpaired slot with no cargo or null slot.
		return null;
	}
}
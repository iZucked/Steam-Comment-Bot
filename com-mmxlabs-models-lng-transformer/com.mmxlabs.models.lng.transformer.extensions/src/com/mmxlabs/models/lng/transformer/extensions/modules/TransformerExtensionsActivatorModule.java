/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.modules;

import org.ops4j.peaberry.activation.util.PeaberryActivationModule;

import com.mmxlabs.models.lng.transformer.extensions.actuals.ActualsTransformerFactory;
import com.mmxlabs.models.lng.transformer.extensions.adp.ADPTransformerModule;
import com.mmxlabs.models.lng.transformer.extensions.contingencytime.ContingencyIdleTimeModule;
import com.mmxlabs.models.lng.transformer.extensions.contracts.ballastbonus.BallastBonusContractTransformerFactory;
import com.mmxlabs.models.lng.transformer.extensions.contracts.ballastbonus.BallastBonusExporterExtensionFactory;
import com.mmxlabs.models.lng.transformer.extensions.entities.EntityTransformerExtensionFactory;
import com.mmxlabs.models.lng.transformer.extensions.exposures.ExposureDataModule;
import com.mmxlabs.models.lng.transformer.extensions.exposures.ExposuresExporterExtensionFactory;
import com.mmxlabs.models.lng.transformer.extensions.fcl.FullCargoLotModule;
import com.mmxlabs.models.lng.transformer.extensions.inventory.InventoryLevelsOutputScheduleProcessorFactory;
import com.mmxlabs.models.lng.transformer.extensions.panamaslots.PanamaSlotsModule;
import com.mmxlabs.models.lng.transformer.extensions.paperdeals.PaperDealDataModule;
import com.mmxlabs.models.lng.transformer.extensions.paperdeals.PaperDealsExporterExtensionFactory;
import com.mmxlabs.models.lng.transformer.extensions.portshipsizeconstraint.PortShipSizeModule;
import com.mmxlabs.models.lng.transformer.extensions.restrictedelements.RestrictedElementsModule;
import com.mmxlabs.models.lng.transformer.extensions.restrictedslots.RestrictedSlotsModule;
import com.mmxlabs.models.lng.transformer.extensions.shippingtype.ShippingTypeRequirementModule;
import com.mmxlabs.models.lng.transformer.extensions.simplecontracts.SimpleContractTransformerFactory;
import com.mmxlabs.models.lng.transformer.extensions.tradingexporter.BasicSlotPNLExporterExtensionFactory;
import com.mmxlabs.models.lng.transformer.extensions.tradingexporter.TradingExporterExtensionFactory;
import com.mmxlabs.models.lng.transformer.extensions.vesselavailabilities.VesselAvailabilityEntityTransformerExtensionFactory;

/**
 * Module to register Transformer extension factories as a service
 * 
 * @author Simon Goodall
 * 
 */
public class TransformerExtensionsActivatorModule extends PeaberryActivationModule {

	@Override
	protected void configure() {
		install(new PortShipSizeModule());
		install(new RestrictedElementsModule());
		install(new RestrictedSlotsModule());
		install(new ContingencyIdleTimeModule());		
		install(new PanamaSlotsModule());
		install(new ADPTransformerModule());
		install(new ShippingTypeRequirementModule());
		install(new FullCargoLotModule());
		install(new ExposureDataModule());
		install(new PaperDealDataModule());

		bindService(SimpleContractTransformerFactory.class).export();
		bindService(BallastBonusContractTransformerFactory.class).export();

		bindService(EntityTransformerExtensionFactory.class).export();
		bindService(TradingExporterExtensionFactory.class).export();
		bindService(BasicSlotPNLExporterExtensionFactory.class).export();
		bindService(BallastBonusExporterExtensionFactory.class).export();
		
		bindService(ExposuresExporterExtensionFactory.class).export();
		bindService(PaperDealsExporterExtensionFactory.class).export();
		
		bindService(ActualsTransformerFactory.class).export();
		
		bindService(VesselAvailabilityEntityTransformerExtensionFactory.class).export();
		
		bindService(InventoryLevelsOutputScheduleProcessorFactory.class).export();
	}

}

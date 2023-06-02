/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.modules;

import org.ops4j.peaberry.activation.util.PeaberryActivationModule;

import com.mmxlabs.models.lng.transformer.extensions.actuals.ActualsTransformerFactory;
import com.mmxlabs.models.lng.transformer.extensions.adp.ADPTransformerModule;
import com.mmxlabs.models.lng.transformer.extensions.contingencytime.ContingencyIdleTimeModule;
import com.mmxlabs.models.lng.transformer.extensions.contracts.charter.CharterContractExporterExtensionFactory;
import com.mmxlabs.models.lng.transformer.extensions.contracts.charter.CharterContractTransformerFactory;
import com.mmxlabs.models.lng.transformer.extensions.contracts.regas.RegasContractTransformerFactory;
import com.mmxlabs.models.lng.transformer.extensions.counterpartyvolume.CounterPartyVolumeDataModule;
import com.mmxlabs.models.lng.transformer.extensions.entities.EntityTransformerExtensionFactory;
import com.mmxlabs.models.lng.transformer.extensions.exposures.ExposureDataModule;
import com.mmxlabs.models.lng.transformer.extensions.exposures.ExposuresExporterExtensionFactory;
import com.mmxlabs.models.lng.transformer.extensions.fcl.FullCargoLotModule;
import com.mmxlabs.models.lng.transformer.extensions.fobsalerotations.FobSaleRotationModule;
import com.mmxlabs.models.lng.transformer.extensions.groupedslots.GroupedSlotsTransformerModule;
import com.mmxlabs.models.lng.transformer.extensions.heelcarry.HeelCarryDataModule;
import com.mmxlabs.models.lng.transformer.extensions.inventory.InventoryLevelsOutputScheduleProcessorFactory;
import com.mmxlabs.models.lng.transformer.extensions.panamaslots.PanamaSlotsModule;
import com.mmxlabs.models.lng.transformer.extensions.paperdeals.PaperDealDataModule;
import com.mmxlabs.models.lng.transformer.extensions.paperdeals.PaperDealsExporterExtensionFactory;
import com.mmxlabs.models.lng.transformer.extensions.portshipsizeconstraint.PortShipSizeModule;
import com.mmxlabs.models.lng.transformer.extensions.restrictedelements.RestrictedElementsModule;
import com.mmxlabs.models.lng.transformer.extensions.restrictedslots.RestrictedSlotsModule;
import com.mmxlabs.models.lng.transformer.extensions.shippingtype.ShippingTypeRequirementModule;
import com.mmxlabs.models.lng.transformer.extensions.simplecontracts.SimpleContractTransformerFactory;
import com.mmxlabs.models.lng.transformer.extensions.simplecontracts.VolumeTierContractTransformerFactory;
import com.mmxlabs.models.lng.transformer.extensions.tradingexporter.BasicSlotPNLExporterExtensionFactory;
import com.mmxlabs.models.lng.transformer.extensions.tradingexporter.TradingExporterExtensionFactory;
import com.mmxlabs.models.lng.transformer.extensions.transfers.TransfersDataExporterExtensionFactory;
import com.mmxlabs.models.lng.transformer.extensions.transfers.TransfersDataModule;
import com.mmxlabs.models.lng.transformer.extensions.vesselcharters.VesselCharterEntityTransformerExtensionFactory;

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
		install(new GroupedSlotsTransformerModule());
		install(new ShippingTypeRequirementModule());
		install(new FullCargoLotModule());
		install(new ExposureDataModule());
		install(new PaperDealDataModule());
		install(new CounterPartyVolumeDataModule());
		install(new HeelCarryDataModule());
		install(new TransfersDataModule());
		install(new FobSaleRotationModule());

		bindService(SimpleContractTransformerFactory.class).export();
		bindService(VolumeTierContractTransformerFactory.class).export();
		bindService(RegasContractTransformerFactory.class).export();
		bindService(CharterContractTransformerFactory.class).export();

		bindService(EntityTransformerExtensionFactory.class).export();
		bindService(TradingExporterExtensionFactory.class).export();
		bindService(BasicSlotPNLExporterExtensionFactory.class).export();
		bindService(CharterContractExporterExtensionFactory.class).export();
		bindService(TransfersDataExporterExtensionFactory.class).export();

		bindService(ExposuresExporterExtensionFactory.class).export();
		bindService(PaperDealsExporterExtensionFactory.class).export();

		bindService(ActualsTransformerFactory.class).export();

		bindService(VesselCharterEntityTransformerExtensionFactory.class).export();

		bindService(InventoryLevelsOutputScheduleProcessorFactory.class).export();
	}

}

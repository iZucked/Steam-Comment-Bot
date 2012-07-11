/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.trading.integration;

import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.trading.optimiser.IEntity;
import com.mmxlabs.trading.optimiser.builder.TradingBuilderExtension;

/**
 * Trading transformer extension which sets up entity models.
 * 
 * @author hinton
 * 
 */
public class TradingTransformerExtension implements ITransformerExtension {

	private MMXRootObject rootObject;
	private ModelEntityMap entities;
	private TradingBuilderExtension tradingBuilder;

	@Override
	public void startTransforming(MMXRootObject rootObject, ModelEntityMap map, ISchedulerBuilder builder) {
		this.rootObject = rootObject;
		this.entities = map;
		tradingBuilder = new TradingBuilderExtension();
		builder.addBuilderExtension(tradingBuilder);
	}

	@Override
	public void finishTransforming() {
		CommercialModel commercialModel = rootObject.getSubModel(CommercialModel.class);
		for (final LegalEntity e : commercialModel.getEntities()) {
			final IEntity e2;
			// Tmp hack until model is updated
			if (e.getName().equalsIgnoreCase("Third-parties")) {
				e2 = tradingBuilder.createExternalEntity(e.getName());
			} else {
				e2 = tradingBuilder.createGroupEntity(e.getName(), Calculator.scaleToInt(1.0), new ConstantValueCurve(Calculator.scaleToInt(0.0)), // TODO fix tax rates.
						Calculator.scaleToInt(0));
			}

			// if (e instanceof GroupEntity) {
			// final GroupEntity ge = (GroupEntity) e;
			// e2 = tradingBuilder.createGroupEntity(ge.getName(), Calculator.scaleToInt(ge.getOwnership()), new ConstantValueCurve(Calculator.scaleToInt(ge.getTaxRate())), // TODO fix tax rates.
			// Calculator.scaleToInt(ge.getTransferOffset()));
			// } else {
			// e2 = tradingBuilder.createExternalEntity(e.getName());
			// }
			entities.addModelObject(e, e2);
		}

		final LegalEntity shipping = commercialModel.getShippingEntity();
		// final IEntity shipping2 = tradingBuilder.createGroupEntity(shipping.getName(), Calculator.scaleToInt(shipping.getOwnership()),
		// new ConstantValueCurve(Calculator.scaleToInt(shipping.getTaxRate())), // TODO fix tax rates.
		// Calculator.scaleToInt(shipping.getTransferOffset()));
		// entities.addModelObject(shipping, shipping2);

		tradingBuilder.setShippingEntity(entities.getOptimiserObject(shipping, IEntity.class));

		// set up contract association or slot association or whatever
		CargoModel cargoModel = rootObject.getSubModel(CargoModel.class);
		for (final Cargo c : cargoModel.getCargoes()) {
			final IPortSlot load = entities.getOptimiserObject(c.getLoadSlot(), IPortSlot.class);
			final IPortSlot dis = entities.getOptimiserObject(c.getDischargeSlot(), IPortSlot.class);
			final IEntity loadEntity = entities.getOptimiserObject(c.getLoadSlot().getContract().getEntity(), IEntity.class);
			final IEntity dischargeEntity = entities.getOptimiserObject(c.getDischargeSlot().getContract().getEntity(), IEntity.class);
			tradingBuilder.setEntityForSlot(loadEntity, load);
			tradingBuilder.setEntityForSlot(dischargeEntity, dis);
		}
	}
}

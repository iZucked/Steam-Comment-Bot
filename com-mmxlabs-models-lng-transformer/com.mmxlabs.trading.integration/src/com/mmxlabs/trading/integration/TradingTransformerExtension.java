/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.trading.integration;

import scenario.Scenario;
import scenario.cargo.Cargo;
import scenario.contract.Entity;
import scenario.contract.GroupEntity;

import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.lngscheduler.emf.extras.ITransformerExtension;
import com.mmxlabs.lngscheduler.emf.extras.ModelEntityMap;
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

	private TradingBuilderExtension tradingBuilder;
	private Scenario scenario;
	private ModelEntityMap entities;


	@Override
	public void startTransforming(Scenario scenario, ModelEntityMap map, ISchedulerBuilder builder) {
		this.scenario = scenario;
		this.entities = map;
		tradingBuilder = new TradingBuilderExtension();
		builder.addBuilderExtension(tradingBuilder);
	}

	@Override
	public void finishTransforming() {
		for (final Entity e : scenario.getContractModel().getEntities()) {
			final IEntity e2;
			if (e instanceof GroupEntity) {
				final GroupEntity ge = (GroupEntity) e;
				e2 = tradingBuilder.createGroupEntity(ge.getName(), Calculator.scaleToInt(ge.getOwnership()), 
						new ConstantValueCurve(Calculator.scaleToInt(ge.getTaxRate())), //TODO fix tax rates.
						Calculator.scaleToInt(ge.getTransferOffset()));
			} else {
				e2 = tradingBuilder.createExternalEntity(e.getName());
			}
			entities.addModelObject(e, e2);
		}
		
		final GroupEntity shipping = scenario.getContractModel().getShippingEntity();
		final IEntity shipping2 = tradingBuilder.createGroupEntity(shipping.getName(), Calculator.scaleToInt(shipping.getOwnership()),
				new ConstantValueCurve(Calculator.scaleToInt(shipping.getTaxRate())), // TODO fix tax rates.
				Calculator.scaleToInt(shipping.getTransferOffset()));
		entities.addModelObject(shipping, shipping2);

		tradingBuilder.setShippingEntity(shipping2);

		// set up contract association or slot association or whatever
		for (final Cargo c : scenario.getCargoModel().getCargoes()) {
			final IPortSlot load = entities.getOptimiserObject(c.getLoadSlot(), IPortSlot.class);
			final IPortSlot dis = entities.getOptimiserObject(c.getDischargeSlot(), IPortSlot.class);
			final IEntity loadEntity = entities.getOptimiserObject(c.getLoadSlot().getSlotOrPortContract(scenario).getEntity(), IEntity.class);
			final IEntity dischargeEntity = entities.getOptimiserObject(c.getDischargeSlot().getSlotOrPortContract(scenario).getEntity(), IEntity.class);
			tradingBuilder.setEntityForSlot(loadEntity, load);
			tradingBuilder.setEntityForSlot(dischargeEntity, dis);
		}
	}
}

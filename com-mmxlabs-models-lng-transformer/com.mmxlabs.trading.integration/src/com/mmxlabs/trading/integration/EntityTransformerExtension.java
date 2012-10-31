/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.trading.integration;

import java.util.Collection;

import com.google.inject.Inject;
import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.ResourcelessModelEntityMap;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.IEntity;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapEntityProviderEditor;
import com.mmxlabs.trading.optimiser.impl.SimpleEntity;

/**
 * Trading transformer extension which sets up entity models.
 * 
 * @author hinton
 * 
 * @since 2.0
 */
public class EntityTransformerExtension implements ITransformerExtension {

	private MMXRootObject rootObject;

	private ResourcelessModelEntityMap entities;

	@Inject
	private HashMapEntityProviderEditor entityProvider;

	@Override
	public void startTransforming(final MMXRootObject rootObject, final ResourcelessModelEntityMap map, final ISchedulerBuilder builder) {
		this.rootObject = rootObject;
		this.entities = map;
	}

	@Override
	public void finishTransforming() {
		final CommercialModel commercialModel = rootObject.getSubModel(CommercialModel.class);
		for (final LegalEntity e : commercialModel.getEntities()) {
			final IEntity e2 = createGroupEntity(e.getName(), OptimiserUnitConvertor.convertToInternalConversionFactor(1.0), new ConstantValueCurve(0), // TODO fix tax rates.
					OptimiserUnitConvertor.convertToInternalConversionFactor(0));

			entities.addModelObject(e, e2);
		}

		final LegalEntity shipping = commercialModel.getShippingEntity();

		setShippingEntity(entities.getOptimiserObject(shipping, IEntity.class));

		// set up contract association or slot association or whatever
		final Collection<Slot> slots = entities.getAllModelObjects(Slot.class);
		for (final Slot slot : slots) {
			final IPortSlot portSlot = entities.getOptimiserObject(slot, IPortSlot.class);
			if (portSlot != null) {
				final IEntity entity = entities.getOptimiserObject(slot.getContract().getEntity(), IEntity.class);
				setEntityForSlot(entity, portSlot);

			}
		}
	}

	/**
	 * @since 2.0
	 */
	public IEntity createGroupEntity(final String name, final int ownership, final ICurve taxCurve, final int offset) {
		return new SimpleEntity(name, ownership, taxCurve, offset);
	}

	/**
	 * @since 2.0
	 */
	public void setEntityForSlot(final IEntity entity, final IPortSlot slot) {
		entityProvider.setEntityForSlot(entity, slot);
	}

	/**
	 * @since 2.0
	 */
	public void setShippingEntity(final IEntity shipping) {
		entityProvider.setShippingEntity(shipping);
	}
}

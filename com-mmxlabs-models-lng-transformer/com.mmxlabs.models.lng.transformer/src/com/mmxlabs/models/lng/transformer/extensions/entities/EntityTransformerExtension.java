/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.entities;

import java.util.Collection;

import com.google.inject.Inject;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.StepwiseIntegerCurve;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.models.lng.transformer.util.EntityTransformerUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.entities.impl.SimpleEntity;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapEntityProviderEditor;

/**
 * Trading transformer extension which sets up entity models.
 * 
 * @author hinton
 * 
 * @since 3.0
 */
public class EntityTransformerExtension implements ITransformerExtension {

	private MMXRootObject rootObject;

	private ModelEntityMap entities;

	@Inject
	private HashMapEntityProviderEditor entityProvider;

	@Inject
	private DateAndCurveHelper dateAndCurveHelper;

	@Override
	public void startTransforming(final MMXRootObject rootObject, final ModelEntityMap map, final ISchedulerBuilder builder) {
		this.rootObject = rootObject;
		this.entities = map;

		final CommercialModel commercialModel = rootObject.getSubModel(CommercialModel.class);

		for (final LegalEntity e : commercialModel.getEntities()) {
			final StepwiseIntegerCurve taxCurve = EntityTransformerUtils.createTaxCurve(e, dateAndCurveHelper, map.getEarliestDate());

			final IEntity e2 = createGroupEntity(e.getName(), OptimiserUnitConvertor.convertToInternalConversionFactor(1.0), taxCurve);

			entities.addModelObject(e, e2);
		}
	}

	@Override
	public void finishTransforming() {

		final CommercialModel commercialModel = rootObject.getSubModel(CommercialModel.class);
		final LegalEntity shipping = commercialModel.getShippingEntity();

		setShippingEntity(entities.getOptimiserObject(shipping, IEntity.class));

		// set up contract association or slot association or whatever
		final Collection<Slot> slots = entities.getAllModelObjects(Slot.class);
		for (final Slot slot : slots) {
			final IPortSlot portSlot = entities.getOptimiserObject(slot, IPortSlot.class);
			if (portSlot != null) {
				LegalEntity slotEntity = null;
//				if (slot.isSetPriceInfo()) {
//					slotEntity = slot.getPriceInfo().getEntity();
//				} else 
					if (slot.isSetContract()) {
					slotEntity = slot.getContract().getEntity();
				} else {
					slotEntity = shipping;
				}

				final IEntity entity = entities.getOptimiserObject(slotEntity, IEntity.class);
				setEntityForSlot(entity, portSlot);

			}
		}
	}

	/**
	 * @since 3.0
	 */
	public IEntity createGroupEntity(final String name, final int ownership, final ICurve taxCurve) {
		return new SimpleEntity(name, ownership, taxCurve);
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

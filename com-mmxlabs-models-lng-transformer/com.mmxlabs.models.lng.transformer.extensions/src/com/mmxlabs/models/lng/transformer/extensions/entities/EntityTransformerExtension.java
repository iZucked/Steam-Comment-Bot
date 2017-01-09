/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.entities;

import java.util.Collection;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.common.curves.StepwiseIntegerCurve;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.SimpleEntityBook;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.models.lng.transformer.util.EntityTransformerUtils;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.entities.EntityBookType;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.entities.IEntityBook;
import com.mmxlabs.scheduler.optimiser.entities.impl.DefaultEntity;
import com.mmxlabs.scheduler.optimiser.entities.impl.DefaultEntityBook;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapEntityProviderEditor;

/**
 * Trading transformer extension which sets up entity models.
 * 
 * @author hinton
 * 
 */
public class EntityTransformerExtension implements ITransformerExtension {

	private LNGScenarioModel rootObject;

	private ModelEntityMap modelEntityMap;

	@Inject
	private HashMapEntityProviderEditor entityProvider;

	@Inject
	private DateAndCurveHelper dateHelper;

	@Inject
	private Injector injector;

	@Override
	public void startTransforming(final LNGScenarioModel rootObject, final ModelEntityMap modelEntityMap, final ISchedulerBuilder builder) {
		this.rootObject = rootObject;
		this.modelEntityMap = modelEntityMap;

		// Register entities first, then in finish transforming create the books.
		final CommercialModel commercialModel = rootObject.getReferenceModel().getCommercialModel();

		for (final BaseLegalEntity e : commercialModel.getEntities()) {
			if (e instanceof LegalEntity) {
				final IEntity defaultEntity = new DefaultEntity(e.getName());
				injector.injectMembers(defaultEntity);
				modelEntityMap.addModelObject(e, defaultEntity);
			}
		}
	}

	@Override
	public void finishTransforming() {

		final CommercialModel commercialModel = rootObject.getReferenceModel().getCommercialModel();
		for (final BaseLegalEntity e : commercialModel.getEntities()) {
			IEntity entity = modelEntityMap.getOptimiserObjectNullChecked(e, IEntity.class);
			if (e.getShippingBook() instanceof SimpleEntityBook) {
				final SimpleEntityBook simpleBook = (SimpleEntityBook) e.getShippingBook();
				final StepwiseIntegerCurve taxCurve = EntityTransformerUtils.createTaxCurve(simpleBook.getTaxRates(), dateHelper, dateHelper.getEarliestTime());
				final IEntityBook book = new DefaultEntityBook(entity, EntityBookType.Shipping, taxCurve);
				injector.injectMembers(book);
				modelEntityMap.addModelObject(simpleBook, book);
				entityProvider.setEntityBook(entity, EntityBookType.Shipping, book);
			}
			if (e.getTradingBook() instanceof SimpleEntityBook) {
				final SimpleEntityBook simpleBook = (SimpleEntityBook) e.getTradingBook();
				final StepwiseIntegerCurve taxCurve = EntityTransformerUtils.createTaxCurve(simpleBook.getTaxRates(), dateHelper, dateHelper.getEarliestTime());
				final IEntityBook book = new DefaultEntityBook(entity, EntityBookType.Trading, taxCurve);
				injector.injectMembers(book);
				modelEntityMap.addModelObject(simpleBook, book);
				entityProvider.setEntityBook(entity, EntityBookType.Trading, book);
			}
			if (e.getUpstreamBook() instanceof SimpleEntityBook) {
				final SimpleEntityBook simpleBook = (SimpleEntityBook) e.getUpstreamBook();
				final StepwiseIntegerCurve taxCurve = EntityTransformerUtils.createTaxCurve(simpleBook.getTaxRates(), dateHelper, dateHelper.getEarliestTime());
				final IEntityBook book = new DefaultEntityBook(entity, EntityBookType.Upstream, taxCurve);
				injector.injectMembers(book);
				modelEntityMap.addModelObject(simpleBook, book);
				entityProvider.setEntityBook(entity, EntityBookType.Upstream, book);
			}
		}

		// Generic stuff -> split into separate extension
		final CargoModel cargoModel = rootObject.getCargoModel();
		for (final VesselAvailability eVesselAvailability : cargoModel.getVesselAvailabilities()) {
			final IVesselAvailability vesselAvailability = modelEntityMap.getOptimiserObjectNullChecked(eVesselAvailability, IVesselAvailability.class);
			final IEntity entity = modelEntityMap.getOptimiserObjectNullChecked(eVesselAvailability.getEntity(), IEntity.class);
			entityProvider.setEntityForVesselAvailability(entity, vesselAvailability);
		}

		// set up contract association or slot association or whatever
		final Collection<Slot> slots = modelEntityMap.getAllModelObjects(Slot.class);
		for (final Slot slot : slots) {
			assert slot != null;
			final IPortSlot portSlot = modelEntityMap.getOptimiserObject(slot, IPortSlot.class);
			if (portSlot != null) {
				final BaseLegalEntity slotEntity = slot.getSlotOrDelegatedEntity();
				final IEntity entity = modelEntityMap.getOptimiserObjectNullChecked(slotEntity, IEntity.class);
				entityProvider.setEntityForSlot(entity, portSlot);
			}
		}
	}
}

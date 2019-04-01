/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.entities;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.common.curves.StepwiseIntegerCurve;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.SimpleEntityBook;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.contracts.ISlotTransformer;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.models.lng.transformer.util.EntityTransformerUtils;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
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
public class EntityTransformerExtension implements ITransformerExtension, ISlotTransformer {

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
				final IEntity defaultEntity = new DefaultEntity(e.getName(), e.isThirdParty());
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
	}

	@Override
	public void slotTransformed(@NonNull Slot<?> modelSlot, @NonNull IPortSlot optimiserSlot) {
		final BaseLegalEntity slotEntity = modelSlot.getSlotOrDelegateEntity();
		final IEntity entity = modelEntityMap.getOptimiserObjectNullChecked(slotEntity, IEntity.class);
		entityProvider.setEntityForSlot(entity, optimiserSlot);
	}
}

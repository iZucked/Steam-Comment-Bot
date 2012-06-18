/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.trading.optimiser.builder;

import java.util.Collection;
import java.util.Collections;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.builder.IBuilderExtension;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.trading.optimiser.IEntity;
import com.mmxlabs.trading.optimiser.TradingConstants;
import com.mmxlabs.trading.optimiser.impl.OtherEntity;
import com.mmxlabs.trading.optimiser.impl.SimpleEntity;
import com.mmxlabs.trading.optimiser.providers.impl.HashMapEntityProviderEditor;

/**
 * Creates the extra elements for the trading optimiser. Mainly these are just the various entities.
 * 
 * @author hinton
 * 
 */
public class TradingBuilderExtension implements IBuilderExtension {
	HashMapEntityProviderEditor entityProvider = new HashMapEntityProviderEditor(TradingConstants.DCP_entityProvider);

	public TradingBuilderExtension() {

	}

	public IEntity createExternalEntity(final String name) {
		return new OtherEntity(name);
	}

	public IEntity createGroupEntity(final String name, final int ownership, final ICurve taxCurve, final int offset) {
		return new SimpleEntity(name, ownership, taxCurve, offset);
	}

	public void setEntityForSlot(final IEntity entity, final IPortSlot slot) {
		entityProvider.setEntityForSlot(entity, slot);
	}

	public void setShippingEntity(final IEntity shipping) {
		entityProvider.setShippingEntity(shipping);
	}

	@Override
	public Collection<Pair<String, IDataComponentProvider>> createDataComponentProviders(final IOptimisationData optimisationData) {
		return Collections.singleton(new Pair<String, IDataComponentProvider>(entityProvider.getName(), entityProvider));
	}

	@Override
	public void dispose() {
		entityProvider = null;
	}

	@Override
	public void finishBuilding(final IOptimisationData optimisationData) {

	}
}

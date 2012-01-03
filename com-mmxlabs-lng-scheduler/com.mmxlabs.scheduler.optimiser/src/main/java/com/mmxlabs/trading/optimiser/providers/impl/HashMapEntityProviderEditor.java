/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.trading.optimiser.providers.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.trading.optimiser.IEntity;
import com.mmxlabs.trading.optimiser.providers.IEntityProvider;

/**
 * Hash map based entity provider / editor.
 * 
 * @author hinton
 * 
 */
public class HashMapEntityProviderEditor implements IEntityProvider {
	private final LinkedHashSet<IEntity> entities = new LinkedHashSet<IEntity>();
	private final HashMap<IPortSlot, IEntity> entitiesBySlot = new HashMap<IPortSlot, IEntity>();

	private final String name;
	private IEntity shippingEntity;

	public HashMapEntityProviderEditor(String name) {
		super();
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		shippingEntity = null;
		entities.clear();
		entitiesBySlot.clear();
	}

	@Override
	public IEntity getShippingEntity() {
		return shippingEntity;
	}

	@Override
	public IEntity getEntityForSlot(final IPortSlot slot) {
		return entitiesBySlot.get(slot);
	}

	public Collection<IEntity> getEntities() {
		return entities;
	}

	public void setShippingEntity(final IEntity shippingEntity) {
		this.shippingEntity = shippingEntity;
		entities.add(shippingEntity);
	}

	public void setEntityForSlot(final IEntity entity, final IPortSlot slot) {
		this.entitiesBySlot.put(slot, entity);
		entities.add(entity);
	}
}

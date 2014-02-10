/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.providers.IEntityProvider;

/**
 * Hash map based entity provider / editor.
 * 
 * @author hinton
 * @since 2.0
 * 
 */
public class HashMapEntityProviderEditor implements IEntityProvider {
	private final LinkedHashSet<IEntity> entities = new LinkedHashSet<IEntity>();
	private final HashMap<IPortSlot, IEntity> entitiesBySlot = new HashMap<IPortSlot, IEntity>();
	private final HashMap<IVessel, IEntity> entitiesByVessel = new HashMap<IVessel, IEntity>();

//	private IEntity shippingEntity;

	@Override
	public String getName() {
		return "";
	}

	@Override
	public void dispose() {
//		shippingEntity = null;
		entities.clear();
		entitiesBySlot.clear();
		entitiesByVessel.clear();
	}

//	@Override
//	public IEntity getShippingEntity() {
//		return shippingEntity;
//	}

	@Override
	public IEntity getEntityForSlot(final IPortSlot slot) {
		return entitiesBySlot.get(slot);
	}

	public Collection<IEntity> getEntities() {
		return entities;
	}

//	public void setShippingEntity(final IEntity shippingEntity) {
//		this.shippingEntity = shippingEntity;
//		entities.add(shippingEntity);
//	}

	public void setEntityForSlot(final IEntity entity, final IPortSlot slot) {
		this.entitiesBySlot.put(slot, entity);
		entities.add(entity);
	}
	public void setEntityForVessel(final IEntity entity, final IVessel vessel) {
		this.entitiesByVessel.put(vessel, entity);
		entities.add(entity);
	}

	@Override
	public IEntity getEntityForVessel(IVessel vessel) {
		// TODO Auto-generated method stub
		return entitiesByVessel.get(vessel);
	}
}

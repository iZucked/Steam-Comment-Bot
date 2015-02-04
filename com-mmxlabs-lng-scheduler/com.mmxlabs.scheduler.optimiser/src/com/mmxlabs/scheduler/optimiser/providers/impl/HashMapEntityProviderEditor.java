/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.entities.EntityBookType;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.entities.IEntityBook;
import com.mmxlabs.scheduler.optimiser.providers.IEntityProvider;

/**
 * Hash map based entity provider / editor.
 * 
 * @author hinton
 * 
 */
public class HashMapEntityProviderEditor implements IEntityProvider {

	private final LinkedHashSet<IEntity> entities = new LinkedHashSet<>();
	private final Map<IPortSlot, IEntity> entitiesBySlot = new HashMap<>();
	private final Map<IVesselAvailability, IEntity> entitiesByVesselAvailability = new HashMap<>();
	private final Map<IEntity, Map<EntityBookType, IEntityBook>> entitiesBooksMap = new HashMap<>();

	@Override
	public IEntity getEntityForSlot(final IPortSlot slot) {
		return entitiesBySlot.get(slot);
	}

	public Collection<IEntity> getEntities() {
		return entities;
	}

	public void setEntityForSlot(final IEntity entity, final IPortSlot slot) {
		this.entitiesBySlot.put(slot, entity);
		entities.add(entity);
	}

	public void setEntityForVesselAvailability(final IEntity entity, final IVesselAvailability vesselAvailability) {
		this.entitiesByVesselAvailability.put(vesselAvailability, entity);
		entities.add(entity);
	}

	@Override
	public IEntity getEntityForVesselAvailability(final IVesselAvailability vesselAvailability) {
		return entitiesByVesselAvailability.get(vesselAvailability);
	}

	@Override
	public IEntityBook getEntityBook(final IEntity entity, final EntityBookType bookType) {
		if (entitiesBooksMap.containsKey(entity)) {
			final Map<EntityBookType, IEntityBook> map = entitiesBooksMap.get(entity);
			return map.get(bookType);
		}
		return null;
	}

	public void setEntityBook(final IEntity entity, final EntityBookType bookType, final IEntityBook entityBook) {

		final Map<EntityBookType, IEntityBook> map;
		if (entitiesBooksMap.containsKey(entity)) {
			map = entitiesBooksMap.get(entity);
		} else {
			map = new EnumMap<EntityBookType, IEntityBook>(EntityBookType.class);
			entitiesBooksMap.put(entity, map);
		}
		map.put(bookType, entityBook);
	}
}

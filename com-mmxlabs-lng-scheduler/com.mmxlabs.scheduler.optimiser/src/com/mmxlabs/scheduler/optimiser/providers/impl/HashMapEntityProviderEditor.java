/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

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

	private final LinkedHashSet<@NonNull IEntity> entities = new LinkedHashSet<>();
	private final Map<IPortSlot, IEntity> entitiesBySlot = new HashMap<>();
	private final Map<@NonNull IVesselAvailability, @NonNull IEntity> entitiesByVesselAvailability = new HashMap<>();
	private final Map<@NonNull IEntity, Map<@NonNull EntityBookType, @NonNull IEntityBook>> entitiesBooksMap = new HashMap<>();

	@Override
	public IEntity getEntityForSlot(final @NonNull IPortSlot slot) {
		return entitiesBySlot.get(slot);
	}

	public Collection<@NonNull IEntity> getEntities() {
		return entities;
	}

	public void setEntityForSlot(final @NonNull IEntity entity, final @NonNull IPortSlot slot) {
		this.entitiesBySlot.put(slot, entity);
		entities.add(entity);
	}

	public void setEntityForVesselAvailability(final @NonNull IEntity entity, final @NonNull IVesselAvailability vesselAvailability) {
		this.entitiesByVesselAvailability.put(vesselAvailability, entity);
		entities.add(entity);
	}

	@Override
	public IEntity getEntityForVesselAvailability(final @NonNull IVesselAvailability vesselAvailability) {
		return entitiesByVesselAvailability.get(vesselAvailability);
	}

	@Override
	public IEntityBook getEntityBook(final IEntity entity, final @NonNull EntityBookType bookType) {
		if (entitiesBooksMap.containsKey(entity)) {
			final Map<@NonNull EntityBookType, @NonNull IEntityBook> map = entitiesBooksMap.get(entity);
			return map.get(bookType);
		}
		throw new IllegalArgumentException("Unknown entity");
	}

	public void setEntityBook(final @NonNull IEntity entity, final @NonNull EntityBookType bookType, final @NonNull IEntityBook entityBook) {

		final Map<@NonNull EntityBookType, @NonNull IEntityBook> map;
		if (entitiesBooksMap.containsKey(entity)) {
			map = entitiesBooksMap.get(entity);
		} else {
			map = new EnumMap<@NonNull EntityBookType, @NonNull IEntityBook>(EntityBookType.class);
			entitiesBooksMap.put(entity, map);
		}
		map.put(bookType, entityBook);
	}
}

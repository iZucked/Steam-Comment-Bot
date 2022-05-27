/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
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
	private final Map<@NonNull IVesselCharter, @NonNull IEntity> entitiesByVesselCharter = new HashMap<>();
	private final Map<@NonNull IEntity, Map<@NonNull EntityBookType, @NonNull IEntityBook>> entitiesBooksMap = new HashMap<>();

	@Override
	public IEntity getEntityForSlot(final @NonNull IPortSlot slot) {
		return entitiesBySlot.get(slot);
	}

	@Override
	public Collection<@NonNull IEntity> getEntities() {
		return entities;
	}

	public void setEntityForSlot(final @NonNull IEntity entity, final @NonNull IPortSlot slot) {
		this.entitiesBySlot.put(slot, entity);
		entities.add(entity);
	}

	public void setEntityForVesselCharter(final @NonNull IEntity entity, final @NonNull IVesselCharter vesselCharter) {
		this.entitiesByVesselCharter.put(vesselCharter, entity);
		entities.add(entity);
	}

	@Override
	public IEntity getEntityForVesselCharter(final @NonNull IVesselCharter vesselCharter) {
		return entitiesByVesselCharter.get(vesselCharter);
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
		entities.add(entity);
		final Map<@NonNull EntityBookType, @NonNull IEntityBook> map;
		if (entitiesBooksMap.containsKey(entity)) {
			map = entitiesBooksMap.get(entity);
		} else {
			map = new EnumMap<>(EntityBookType.class);
			entitiesBooksMap.put(entity, map);
		}
		map.put(bookType, entityBook);
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.IExternalDateProvider;

/**
 * Class which maps model entities to classes; the LNGScenarioTransformer should
 * populate one of these, which maps every PortSlot to the URI of a Slot in the
 * EMF, and similarly for vessels, etc etc.
 * 
 * @author hinton
 * 
 */
public class DefaultModelEntityMap implements ModelEntityMap, IExternalDateProvider {

	private final @NonNull DateAndCurveHelper dateHelper;

	private final @NonNull Map<Object, Object> modelToOptimiser = new LinkedHashMap<>();

	private final @NonNull Map<Object, Object> optimiserToModel = new LinkedHashMap<>();

	private final @NonNull Map<String, Object> nameToOptimiserObject = new LinkedHashMap<>();

	@Inject
	public DefaultModelEntityMap(@NonNull final DateAndCurveHelper dateHelper) {
		this.dateHelper = dateHelper;
	}

	@Nullable
	public <U> U getModelObject(@NonNull final Object internalObject, @NonNull final Class<? extends U> clz) {
		return clz.cast(optimiserToModel.get(internalObject));
	}

	@SuppressWarnings("null")
	@NonNull
	public <U> U getModelObjectNullChecked(@Nullable final Object internalObject, @NonNull final Class<? extends U> clz) {
		if (internalObject == null) {
			throw new IllegalArgumentException("Optimiser object is null");
		}
		final Object o = optimiserToModel.get(internalObject);
		if (o == null) {
			throw new IllegalArgumentException("No model object found");
		}
		return clz.cast(o);
	}

	public void addModelObject(@NonNull final EObject modelObject, @NonNull final Object internalObject) {
		modelToOptimiser.put(modelObject, internalObject);
		optimiserToModel.put(internalObject, modelObject);
	}

	@Override
	public void addOptimiserToModelOnlyMapping(@NonNull final Object internalObject, @NonNull final EObject modelObject) {
		optimiserToModel.put(internalObject, modelObject);
	}

	@Override
	public void addNamedOptimiserObject(String name, @NonNull final Object object) {
		nameToOptimiserObject.put(name, object);
	}

	@Override
	public @Nullable Object getNamedOptimiserObject(String name) {
		return nameToOptimiserObject.get(name);
	}

	@SuppressWarnings("null")
	@NonNull
	public <T> T getOptimiserObjectNullChecked(@Nullable final EObject modelObject, final Class<? extends T> clz) {
		if (modelObject == null) {
			throw new IllegalArgumentException("Model object is null");
		}
		final Object o = modelToOptimiser.get(modelObject);
		if (o == null) {
			throw new IllegalArgumentException("No optimiser object found");
		}
		return clz.cast(o);
	}

	@Nullable
	public <T> T getOptimiserObject(@NonNull final EObject modelObject, @NonNull final Class<? extends T> clz) {
		return clz.cast(modelToOptimiser.get(modelObject));
	}

	/**
	 */
	public <T extends EObject> Collection<@NonNull T> getAllModelObjects(@NonNull final Class<? extends T> clz) {

		final List<T> objects = new LinkedList<>();
		for (final Object obj : modelToOptimiser.keySet()) {
			if (clz.isInstance(obj)) {
				objects.add(clz.cast(obj));
			}
		}
		return objects;
	}

	@SuppressWarnings("null")
	@NonNull
	public ZonedDateTime getDateFromHours(final int hours, final String tz) {
		return dateHelper.getDateFromHours(hours, tz);
	}

	/**
	 * Convert from hours, relative to earliest time, to a Date object
	 * 
	 * @param hours
	 * @param port
	 * @return
	 */
	@NonNull
	public ZonedDateTime getDateFromHours(final int hours, @Nullable final IPort port) {
		if (port == null) {
			return getDateFromHours(hours, "UTC");
		}
		return getDateFromHours(hours, port.getTimeZoneId());
	}

	/**
	 * Convert from hours, relative to earliest time, to a Date object
	 * 
	 * @param hours
	 * @param port
	 * @return
	 */
	@NonNull
	public ZonedDateTime getDateFromHours(final int hours, @Nullable final Port port) {
		if (port == null) {
			return getDateFromHours(hours, "UTC");
		}
		return getDateFromHours(hours, port.getLocation().getTimeZone());
	}

	@Override
	public @NonNull ZonedDateTime getEarliestTime() {
		return dateHelper.getEarliestTime();
	}

	@Override
	public @NonNull ZonedDateTime getLatestTime() {
		return dateHelper.getLatestTime();
	}

}

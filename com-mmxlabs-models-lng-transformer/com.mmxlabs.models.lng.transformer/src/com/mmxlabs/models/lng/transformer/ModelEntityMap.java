/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer;

import java.time.ZonedDateTime;
import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.scheduler.optimiser.components.IPort;

/**
 * Class which maps model entities to classes; the LNGScenarioTransformer should populate one of these, which maps every PortSlot to the URI of a Slot in the EMF, and similarly for vessels, vessel
 * classes, etc etc.
 * 
 * @author hinton
 * 
 */
public interface ModelEntityMap {

	@Nullable
	<U> U getModelObject(@NonNull final Object internalObject, @NonNull final Class<? extends U> clz);

	@NonNull
	<U> U getModelObjectNullChecked(@Nullable final Object internalObject, @NonNull final Class<? extends U> clz);

	void addModelObject(@NonNull final EObject modelObject, @NonNull final Object internalObject);

	@NonNull
	<T> T getOptimiserObjectNullChecked(@Nullable final EObject modelObject, final Class<? extends T> clz);

	@Nullable
	<T> T getOptimiserObject(@NonNull final EObject modelObject, @NonNull final Class<? extends T> clz);

	/**
	 */
	<T extends EObject> Collection<@NonNull T> getAllModelObjects(@NonNull final Class<? extends T> clz);

	@NonNull
	ZonedDateTime getDateFromHours(final int hours, final String tz);

	/**
	 * Convert from hours, relative to earliest time, to a Date object
	 * 
	 * @param hours
	 * @param port
	 * @return
	 */
	@NonNull
	ZonedDateTime getDateFromHours(final int hours, @Nullable final IPort port);

	/**
	 * Convert from hours, relative to earliest time, to a Date object
	 * 
	 * @param hours
	 * @param port
	 * @return
	 */
	@NonNull
	ZonedDateTime getDateFromHours(final int hours, @Nullable final Port port);
}

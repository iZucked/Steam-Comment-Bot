/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.period;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;

import com.mmxlabs.models.lng.schedule.Schedule;

/**
 * The IScenarioEntityMapping stores the mapping between an original scenario and the copied scenario used in a period optimisation. This is also used to track which of the original elements have been
 * excluded (or removed) in the period scenario.
 * 
 * @author Simon Goodall
 * 
 */
public interface IScenarioEntityMapping {

	/**
	 * Take all the mappings from the input map. This could even be from a {@link Copier} instance.
	 * 
	 * @param originalToCopyMap
	 */
	void createMappings(Map<EObject, EObject> originalToCopyMap);

	/**
	 * Bind an original object to its period optimisation copy.
	 * 
	 * @param original
	 * @param copy
	 */
	void createMapping(EObject original, EObject copy);

	/**
	 * Register an original object not present in the period optimisation scenario. Note we assume {@link Schedule} elements are excluded and API is undefined for these elements.
	 * 
	 * @param original
	 */
	void registerRemovedOriginal(EObject original);

	/**
	 * Return the original scenario model from the copied instance, or null if not found.
	 * 
	 * @param copy
	 * @return
	 */
	<T extends EObject> T getOriginalFromCopy(T copy);

	/**
	 * Return the copied scenario model from the original instance, or null if not found.
	 * 
	 * @param original
	 * @return
	 */
	<T extends EObject> T getCopyFromOriginal(T original);

	/**
	 * Returns all the original scenario elements that have not been removed.
	 * 
	 * @return
	 */
	Collection<EObject> getUsedOriginalObjects();

	/**
	 * Returns the list of elements passed to {@link #registerRemovedOriginal(EObject)}.
	 * 
	 * @return
	 */
	Collection<EObject> getUnusedOriginalObjects();

}
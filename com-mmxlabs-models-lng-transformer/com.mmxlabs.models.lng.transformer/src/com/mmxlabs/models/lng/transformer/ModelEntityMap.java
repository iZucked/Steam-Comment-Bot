/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.management.timer.Timer;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * Class which maps model entities to classes; the LNGScenarioTransformer should populate one of these, which maps every PortSlot to the URI of a Slot in the EMF, and similarly for vessels, vessel
 * classes, etc etc.
 * 
 * @author hinton
 * 
 */
public class ModelEntityMap {
	private final Map<Object, Object> modelToOptimiser = new HashMap<Object, Object>();
	private final Map<Object, Object> optimiserToModel = new HashMap<Object, Object>();

	private Date earliestDate;

	public <U> U getModelObject(final Object internalObject, final Class<? extends U> clz) {
		return clz.cast(optimiserToModel.get(internalObject));
	}

	public void addModelObject(final EObject modelObject, final Object internalObject) {
		modelToOptimiser.put(modelObject, internalObject);
		optimiserToModel.put(internalObject, modelObject);
	}

	public <T> T getOptimiserObject(final EObject modelObject, final Class<? extends T> clz) {
		return clz.cast(modelToOptimiser.get(modelObject));
	}

	public void dispose() {
		this.earliestDate = null;
		modelToOptimiser.clear();
		optimiserToModel.clear();
	}

	/**
	 * @since 3.0
	 */
	public <T extends EObject> Collection<T> getAllModelObjects(final Class<? extends T> clz) {

		final List<T> objects = new LinkedList<T>();
		for (final Object obj : modelToOptimiser.keySet()) {
			if (clz.isInstance(obj)) {
				objects.add(clz.cast(obj));
			}
		}
		return objects;
	}

	/**
	 * @return the earliestDate
	 * @since 2.0
	 */
	public Date getEarliestDate() {
		return earliestDate;
	}

	/**
	 * @param earliestDate
	 *            the earliestDate to set
	 * @since 2.0
	 */
	public void setEarliestDate(Date earliestDate) {
		this.earliestDate = earliestDate;
	}

	public Date getDateFromHours(final long hours) {
		return new Date(earliestDate.getTime() + hours * Timer.ONE_HOUR);
	}

	/**
	 * @since 2.0
	 */
	public int getHoursFromDate(final Date date) {
		long diff = date.getTime() - earliestDate.getTime();
		long hours = diff / Timer.ONE_HOUR;
		return (int) Math.max(hours, 0);
	}

}

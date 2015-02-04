/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.scheduler.optimiser.components.IPort;

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
	private Date latestDate;

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
		this.latestDate = null;
		modelToOptimiser.clear();
		optimiserToModel.clear();
	}

	/**
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
	 */
	public Date getEarliestDate() {
		return earliestDate;
	}

	public Date getLatestDate() {
		return latestDate;
	}

	/**
	 * @param earliestDate
	 *            the earliestDate to set. The date is rounded down to the nearest hour (relative to UTC).
	 */
	public void setEarliestDate(Date earliestDate) {
		this.earliestDate = DateAndCurveHelper.roundTimeDown(earliestDate);
	}

	public void setLatestDate(Date latestTime) {
		this.latestDate = latestTime;
	}

	public Date getDateFromHours(final long hours, final String tz) {
		String timeZone = (tz == null) ? "UTC" : tz;
		int offsetMinutes = DateAndCurveHelper.getOffsetInMinutesFromTimeZone(timeZone);
		return new Date(earliestDate.getTime() + hours * Timer.ONE_HOUR + offsetMinutes * Timer.ONE_MINUTE);
	}

	/**
	 * Convert from hours, relative to earliest time, to a Date object
	 * 
	 * @param hours
	 * @param port
	 * @return
	 */
	public Date getDateFromHours(final long hours, @Nullable IPort port) {
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
	public Date getDateFromHours(final long hours, @Nullable Port port) {
		if (port == null) {
			return getDateFromHours(hours, "UTC");
		}
		return getDateFromHours(hours, port.getTimeZone());
	}

}

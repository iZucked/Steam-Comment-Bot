/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.management.timer.Timer;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * Class which maps model entities to classes; the LNGScenarioTransformer should
 * populate one of these, which maps every PortSlot to the URI of a Slot in the
 * EMF, and similarly for vessels, vessel classes, etc etc.
 * 
 * @author hinton
 * 
 */
public class ModelEntityMap {
	private ResourceSet resourceSet;
	private final Map<Object, URI> uriMap = new HashMap<Object, URI>();

	private final Map<URI, Object> reverseMap = new HashMap<URI, Object>();

	protected Date earliestDate;

	public ModelEntityMap() {
		
	}
	
	/**
	 * @param scenario
	 */
	public void setScenario(final MMXRootObject rootObject) {
		this.resourceSet = rootObject.eResource().getResourceSet();
	}
	
	/**
	 * @return the earliestDate
	 * @since 2.0
	 */
	public Date getEarliestDate() {
		return earliestDate;
	}

	/**
	 * @param earliestDate the earliestDate to set
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
		long diff =date.getTime()  - earliestDate.getTime();
		long hours = diff / Timer.ONE_HOUR;
		return (int) Math.max(hours, 0);
	}
	/**
	 * @param vessel
	 * @param class1
	 * @return
	 */
	public <U> U getModelObject(final Object internalObject,
			final Class<? extends U> clz) {

		final URI uri = uriMap.get(internalObject);
		if (uri == null)
			return null;
		return clz.cast(resourceSet.getEObject(uri, false));

	}

	public void addModelObject(final EObject modelObject,
			final Object internalObject) {
		final URI theURI = EcoreUtil.getURI(modelObject);
		uriMap.put(internalObject, theURI);
		reverseMap.put(theURI, internalObject);
	}

	public <T> T getOptimiserObject(final EObject modelObject,
			final Class<? extends T> clz) {
		final URI theURI = EcoreUtil.getURI(modelObject);
		return clz.cast(reverseMap.get(theURI));
	}

	public void dispose() {
		this.resourceSet = null;
		this.earliestDate = null;
		this.uriMap.clear();
		this.reverseMap.clear();
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.management.timer.Timer;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

import scenario.Scenario;

/**
 * Class which maps model entities to classes; the LNGScenarioTransformer
 * should populate one of these, which maps every PortSlot to the URI of a
 * Slot in the EMF, and similarly for vessels, vessel classes, etc etc.
 * @author hinton
 * 
 */
public class ModelEntityMap {
	private ResourceSet resourceSet;
	private final Map<Object, URI> uriMap = 
		new HashMap<Object, URI>();
	
	private final Map<URI, Object> reverseMap =
		new HashMap<URI, Object>();
	
	private Date earliestDate;

	/**
	 * @param scenario
	 */
	public void setScenario(final Scenario scenario) {
		this.resourceSet = scenario.eResource().getResourceSet();
		earliestDate = EMFUtils.findEarliestAndLatestEvents(scenario).getFirst();
	}
	
	public Date getDateFromHours(final long hours) {
		return new Date(earliestDate.getTime() + hours * Timer.ONE_HOUR);
	}

	/**
	 * @param vessel
	 * @param class1
	 * @return
	 */
	public <U> U getModelObject(final Object internalObject,
			final Class<? extends U> clz) {

		final URI uri = uriMap.get(internalObject);
		if (uri == null) return null;
		return clz.cast(
				resourceSet.getEObject(uri, false)
				);
		
	}
	
	public void addModelObject(final EObject modelObject, final Object internalObject) {
		final URI theURI = EcoreUtil.getURI(modelObject);
		uriMap.put(internalObject, theURI);
		reverseMap.put(theURI, internalObject);
	}
	
	public <T> T getOptimiserObject(final EObject modelObject, final Class<? extends T> clz) {
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

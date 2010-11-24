/**
 *
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 */
package com.mmxlabs.jobcontroller.emf;

import java.util.Date;
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
	private Map<Object, URI> uriMap;
	private Date earliestDate;

	/**
	 * @param scenario
	 */
	public void setScenario(final Scenario scenario) {
		this.resourceSet = scenario.eResource().getResourceSet();
		earliestDate = EMFUtils.findMinMaxDateAttributes(scenario).getFirst();
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

		
		return clz.cast(
				resourceSet.getEObject(uriMap.get(internalObject), false)
				);
		
	}
	
	public void addModelObject(final EObject modelObject, final Object internalObject) {
		final URI theURI = EcoreUtil.getURI(modelObject);
		uriMap.put(internalObject, theURI);
	}
}

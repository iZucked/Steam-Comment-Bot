/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.json;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class EMFJacksonModule extends SimpleModule {

	public EMFJacksonModule() {
		super();

		for (final Object obj : Registry.INSTANCE.values()) {
			if (obj instanceof EPackage) {
				final EPackage ePackage = (EPackage) obj;
				for (final EClassifier classifier : ePackage.getEClassifiers()) {
					if (classifier instanceof EClass) {
						EClass eClass = (EClass) classifier;
						addSerializer(new EMFSerializer(eClass, eClass.getInstanceClass()));
						addDeserializer(eClass.getInstanceClass(), new EMFDeserializer(eClass, eClass.getInstanceClass()));
					}
				}
			}
		}
	}
}

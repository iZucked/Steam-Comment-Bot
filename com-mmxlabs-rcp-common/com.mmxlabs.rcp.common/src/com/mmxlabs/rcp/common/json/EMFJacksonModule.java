/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.json;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.jdt.annotation.Nullable;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class EMFJacksonModule extends SimpleModule {

	public EMFJacksonModule() {
		this(null);
	}

	public EMFJacksonModule(@Nullable IJSONSerialisationConfigProvider serialisationConfig) {
		super();

		for (final Object obj : Registry.INSTANCE.values()) {
			if (obj instanceof EPackage ePackage) {
				for (final EClassifier classifier : ePackage.getEClassifiers()) {
					if (classifier instanceof EClass eClass) {
						addSerializer(new EMFSerializer(eClass, eClass.getInstanceClass(), serialisationConfig));
						addDeserializer(eClass.getInstanceClass(), new EMFDeserializer(eClass, eClass.getInstanceClass()));
					}
				}
			}
		}
	}
}

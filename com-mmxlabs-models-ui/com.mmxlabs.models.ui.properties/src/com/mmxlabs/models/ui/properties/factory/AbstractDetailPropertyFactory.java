/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.properties.factory;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ILabelProvider;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.properties.DetailProperty;

/**
 * Abstract impl of {@link IDetailPropertyFactory} with some helper methods.
 * 
 * @author Simon Goodall
 * 
 */
public abstract class AbstractDetailPropertyFactory implements IDetailPropertyFactory {

	@Override
	@Nullable
	public DetailProperty createProperties(@NonNull final EObject eObject, @Nullable final MMXRootObject rootObject) {

		return createProperties(eObject);
	}

	protected DetailProperty addDetailProperty(final String name, final String description, final String unitsPre, final String unitsPost, final Object propertyObject, final ILabelProvider lp,
			final DetailProperty details) {

		return DetailPropertyFactoryUtil.addDetailProperty(name, description, unitsPre, unitsPost, propertyObject, lp, details);
	}
}

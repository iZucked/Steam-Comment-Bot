/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.properties.factory;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ILabelProvider;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.properties.DetailProperty;
import com.mmxlabs.models.ui.properties.PropertiesFactory;

public abstract class DetailPropertyFactoryUtil {

	public static DetailProperty addDetailProperty(final String name, final String description, final String unitsPre, final String unitsPost, final Object propertyObject, final ILabelProvider lp,
			final DetailProperty details) {
		final DetailProperty dp = PropertiesFactory.eINSTANCE.createDetailProperty();
		dp.setName(name);
		dp.setDescription(description);
		dp.setUnitsPrefix(unitsPre);
		dp.setUnitsSuffix(unitsPost);
		dp.setObject(propertyObject);
		dp.setLabelProvider(lp);
		details.getChildren().add(dp);
		return dp;
	}

	public static @NonNull String getName(@Nullable final NamedObject namedObject) {
		if (namedObject == null) {
			return "";
		}
		final String name = namedObject.getName();
		if (name == null) {
			return "";

		}
		return name;
	}
}

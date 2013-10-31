package com.mmxlabs.models.ui.properties.factory;

import org.eclipse.jface.viewers.ILabelProvider;

import com.mmxlabs.models.ui.properties.DetailProperty;
import com.mmxlabs.models.ui.properties.PropertiesFactory;

public abstract class DetailPropertyFactoryUtil {

	public static DetailProperty addDetailProperty(String name, String description, String unitsPre, String unitsPost, Object propertyObject, ILabelProvider lp, final DetailProperty details) {
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
}

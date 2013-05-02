/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.lingo.models.migration;

import com.mmxlabs.models.lng.migration.MetamodelVersionsUtil;
import com.mmxlabs.models.migration.utils.MetamodelLoader;

/**
 * Utility class to load metamodels for each Models LNG Version
 * 
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @author Simon Goodall
 * 
 */
public class LingoMetamodelVersionsUtil {

	public static MetamodelVersionsUtil.ModelsLNGSet_v1 getTypeFromNS(final String nsURI) {
		return MetamodelVersionsUtil.getTypeFromNS(nsURI);
	}

	public static MetamodelLoader createV0Loader() {

		final MetamodelLoader loader = MetamodelVersionsUtil.createV0Loader(null);

		return loader;
	}

	public static MetamodelLoader createV1Loader() {

		final MetamodelLoader loader = MetamodelVersionsUtil.createV2Loader(null);

		return loader;
	}
}

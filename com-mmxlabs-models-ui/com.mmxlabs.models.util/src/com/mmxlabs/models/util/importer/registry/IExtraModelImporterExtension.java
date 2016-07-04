/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.registry;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.models.util.importer.IExtraModelImporter;

/**
 */
@ExtensionBean("com.mmxlabs.models.util.import.extramodelimporters")
public interface IExtraModelImporterExtension {

	@MapName("class")
	public IExtraModelImporter createInstance();
}

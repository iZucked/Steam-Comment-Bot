/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.registry;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.models.util.importer.ISubmodelImporter;

@ExtensionBean("com.mmxlabs.models.util.import.submodels")
public interface ISubmodelImporterExtension {
	@MapName("ID")
	public String getID();
	@MapName("subModelClassName")
	public String getSubModelClassName();
	@MapName("subModelImporterClass")
	public ISubmodelImporter createInstance();
}

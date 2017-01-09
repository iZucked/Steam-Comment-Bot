/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.registry;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.models.util.importer.IClassImporter;

@ExtensionBean("com.mmxlabs.models.util.import.classes")
public interface IClassImporterExtension {
	@MapName("ID")
	public String getID();
	@MapName("eClassName")
	public String getEClassName();
	@MapName("inheritable")
	public String isInheritable();
	@MapName("classImporterClass")
	public IClassImporter createImporter();
}

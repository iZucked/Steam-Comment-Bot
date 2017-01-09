/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.registry;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.models.util.importer.IAttributeImporter;

@ExtensionBean("com.mmxlabs.models.util.import.attributes")
public interface IAttributeImporterExtension {
	@MapName("ID")
	public String getID();
	@MapName("attributeImporterClass")
	public IAttributeImporter createImporter();
	
	@MapName("dataType")
	IEDataTypeMatch [] getDataTypes();
	
	public interface IEDataTypeMatch {
		@MapName("attributeTypeName")
		public String getAttributeTypeName();
		@MapName("attributeTypeURI")
		public String getAttributeTypeURI();
	}
}

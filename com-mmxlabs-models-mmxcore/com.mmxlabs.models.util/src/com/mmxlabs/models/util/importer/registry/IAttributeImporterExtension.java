package com.mmxlabs.models.util.importer.registry;

import org.ops4j.peaberry.eclipse.ExtensionBean;

@ExtensionBean("com.mmxlabs.models.util.import.attributes")
public interface IAttributeImporterExtension {
	public String getID();
}

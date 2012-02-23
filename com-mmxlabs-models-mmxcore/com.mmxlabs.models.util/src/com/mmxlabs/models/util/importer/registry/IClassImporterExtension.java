package com.mmxlabs.models.util.importer.registry;

import org.ops4j.peaberry.eclipse.ExtensionBean;

@ExtensionBean("com.mmxlabs.models.util.import.classes")
public interface IClassImporterExtension {
	public String getID();
	public String getEClassName();
	public String isInheritable();
}

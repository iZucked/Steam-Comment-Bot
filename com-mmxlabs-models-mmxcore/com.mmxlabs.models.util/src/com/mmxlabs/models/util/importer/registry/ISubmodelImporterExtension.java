package com.mmxlabs.models.util.importer.registry;

import org.ops4j.peaberry.eclipse.ExtensionBean;

import com.mmxlabs.models.util.importer.ISubmodelImporter;

@ExtensionBean("com.mmxlabs.models.util.import.submodels")
public interface ISubmodelImporterExtension {
	public String getID();
	public String getEClassName();
	public ISubmodelImporter createInstance();
}

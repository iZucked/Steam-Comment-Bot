package com.mmxlabs.models.util.importer;

import java.util.Map;

import com.mmxlabs.models.mmxcore.UUIDObject;

public interface ISubmodelImporter {
	public UUIDObject importModel(final Map<String, String> inputs, final IImportContext context);
	public void exportModel(final UUIDObject model, final Map<String, Map<String, String>> output);
}

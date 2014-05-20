/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.importer;

import java.util.Collection;
import java.util.Map;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IExportContext;
import com.mmxlabs.models.util.importer.IExtraModelImporter;
import com.mmxlabs.models.util.importer.IImportContext;

/**
 */
public class AssignmentModelImporter implements IExtraModelImporter {

	public static final String ASSIGNMENTS = "ASSIGNMENTS";

	private final AssignmentImporter importer = new AssignmentImporter();

	@Override
	public Map<String, String> getRequiredInputs() {
		return CollectionsUtil.makeHashMap(ASSIGNMENTS, "Assignments");
	}

	@Override
	public void importModel(final MMXRootObject rootObject, final Map<String, CSVReader> inputs, final IImportContext context) {
		if (inputs.containsKey(ASSIGNMENTS)) {
			importer.importAssignments(inputs.get(ASSIGNMENTS), context);
		}
	}

	@Override
	public void exportModel(final Map<String, Collection<Map<String, String>>> output, final IExportContext context) {

	}
}

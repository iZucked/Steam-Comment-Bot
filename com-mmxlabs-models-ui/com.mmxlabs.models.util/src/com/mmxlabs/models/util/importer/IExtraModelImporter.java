/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer;

import java.util.Collection;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * The {@link IExtraModelImporter} imports additional data models into a scenario *after* the core set has been imported. Such additional imports could be attached anywhere in the model tree, thus we
 * pass in the {@link MMXRootObject} with the main submodels attached into the API here.
 * 
 */
public interface IExtraModelImporter {

	/**
	 * Return a mapping from keys to friendly names, each being a required input file / database table / whatever
	 * 
	 * @return
	 */
	@NonNull
	Map<String, String> getRequiredInputs();

	/**
	 * 
	 * @param inputs
	 * @param context
	 * @return
	 */
	void importModel(@NonNull MMXRootObject rootObject, @NonNull Map<String, CSVReader> inputs, @NonNull IMMXImportContext context);

	/**
	 * Turn the given model instance into a bunch of output values, keyed like the inputs.
	 * 
	 * @param output
	 * @param context
	 */
	void exportModel(@NonNull Map<String, Collection<Map<String, String>>> output, @NonNull IMMXExportContext context);
}

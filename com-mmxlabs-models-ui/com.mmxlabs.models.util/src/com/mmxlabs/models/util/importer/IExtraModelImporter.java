/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer;

import java.util.Collection;
import java.util.Map;

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
	Map<String, String> getRequiredInputs();

	/**
	 * 
	 * @param inputs
	 * @param context
	 * @return
	 */
	void importModel(MMXRootObject rootObject, Map<String, CSVReader> inputs, IImportContext context);

	/**
	 * Turn the given model instance into a bunch of output values, keyed like the inputs.
	 * 
	 * @param root
	 *            TODO
	 * @param model
	 * @param output
	 */
	void exportModel(MMXRootObject root, Map<String, Collection<Map<String, String>>> output);
}

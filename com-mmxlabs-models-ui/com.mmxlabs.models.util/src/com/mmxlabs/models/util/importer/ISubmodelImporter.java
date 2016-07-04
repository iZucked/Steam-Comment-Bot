/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * Each submodel being imported into a joint model should have a corresponding instance of this class. It can look up IClassImporters or do any other thing it fancies to set up its submodel.
 * 
 * @author hinton
 * 
 */
public interface ISubmodelImporter {

	/**
	 * Return the {@link EClass} this {@link ISubmodelImporter} is for.
	 * 
	 * @return
	 */
	@NonNull
	EClass getEClass();

	/**
	 * Return a mapping from keys to friendly names, each being a required input file / database table / whatever
	 * 
	 * @return
	 */
	@NonNull
	Map<String, String> getRequiredInputs();

	/**
	 * Convert the given mapping from the keys of {@link #getRequiredInputs()} to readers into a {@link UUIDObject} of the appropriate sort
	 * 
	 * @param inputs
	 * @param context
	 * @return
	 */
	EObject importModel(@NonNull Map<String, CSVReader> inputs, @NonNull IMMXImportContext context);

	/**
	 * Turn the given model instance into a bunch of output values, keyed like the inputs.
	 * 
	 * @param root
	 * @param model
	 * @param output
	 */
	void exportModel(@NonNull EObject model, @NonNull Map<String, Collection<Map<String, String>>> output, @NonNull IMMXExportContext context);
}

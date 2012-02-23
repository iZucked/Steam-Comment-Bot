package com.mmxlabs.models.util.importer;

import java.util.Collection;
import java.util.Map;

import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * Each submodel being imported into a joint model should have a corresponding instance of this class.
 * It can look up IClassImporters or do any other thing it fancies to set up its submodel.
 * @author hinton
 *
 */
public interface ISubmodelImporter {
	/**
	 * Return a mapping from keys to friendly names, each being a required input file / database table / whatever
	 * @return
	 */
	public Map<String, String> getRequiredInputs();
	/**
	 * Convert the given mapping from the keys of {@link #getRequiredInputs()} to readers into a {@link UUIDObject} of the appropriate sort
	 * @param inputs
	 * @param context
	 * @return
	 */
	public UUIDObject importModel(final Map<String, CSVReader> inputs, final IImportContext context);
	/**
	 * Turn the given model instance into a bunch of output values, keyed like the inputs.
	 * @param model
	 * @param output
	 */
	public void exportModel(final UUIDObject model, final Map<String, Collection<Map<String, String>>> output);
}

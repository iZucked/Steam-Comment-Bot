package com.mmxlabs.models.util.importer;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * Instances of this do the heavy lifting for import jobs. Some model elements do not fit into this form and should be
 * handled by custom logic, of course.
 * 
 * @author hinton
 *
 */
public interface IClassImporter {
	/**
	 * Import objects from the given reader.
	 * @param targetClass
	 * @param filename
	 * @param context
	 * @return
	 */
	public Collection<EObject> importObjects(final EClass targetClass, final CSVReader reader, final IImportContext context);
	/**
	 * Import objects from the given row; the first object should correspond to the row. Any other objects are those which are not contained
	 * in the imported object but were imported anyway; users of this class will probably want to iterate over the list sorting results into
	 * container by their types.
	 * 
	 * @param targetClass
	 * @param row
	 * @param context
	 * @return
	 */
	public Collection<EObject> importObject(final EClass targetClass, final Map<String, String> row, final IImportContext context);
	/**
	 * Turn the given collection of objects into a bunch of key-value maps, for export to something like a CSV writer.
	 * @param objects
	 * @return
	 */
	public Collection<Map<String, String>> exportObjects(final Collection<? extends EObject> objects);
}

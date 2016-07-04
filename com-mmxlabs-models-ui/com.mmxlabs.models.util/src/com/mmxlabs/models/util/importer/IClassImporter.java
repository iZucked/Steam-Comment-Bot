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
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter.ImportResults;

/**
 * Instances of this do the heavy lifting for import jobs. Some model elements do not fit into this form and should be handled by custom logic, of course.
 * 
 * @author hinton
 * 
 */
public interface IClassImporter {
	/**
	 * Import objects from the given reader.
	 * 
	 * @param targetClass
	 * @param filename
	 * @param context
	 * @return
	 */
	@NonNull
	Collection<EObject> importObjects(@NonNull EClass targetClass, @NonNull CSVReader reader, @NonNull IMMXImportContext context);

	/**
	 * Import objects from the given row; the first object should correspond to the row. Any other objects are those which are not contained in the imported object but were imported anyway; users of
	 * this class will probably want to iterate over the list sorting results into container by their types.
	 * 
	 * @param targetClass
	 * @param row
	 * @param context
	 * @return
	 */
	@NonNull
	ImportResults importObject(@Nullable EObject parent, @NonNull EClass targetClass, @NonNull Map<String, String> row, @NonNull IMMXImportContext context);

	/**
	 * Turn the given collection of objects into a bunch of key-value maps, for export to something like a CSV writer.
	 * 
	 * @param objects
	 * @return
	 */
	@NonNull
	Collection<Map<String, String>> exportObjects(@NonNull Collection<? extends EObject> objects, @NonNull IMMXExportContext context);
}

package com.mmxlabs.models.util.importer.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.FieldMap;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IFieldMap;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;
import com.mmxlabs.models.util.importer.impl.SetReference;

/**
 *  Class to permit the generic import of multi-line data from a CSV file.
 *  Where a CSV importer is 
 *  
 * @author Simon McGregor
 *
 */
public class MultiLineImporter extends DefaultClassImporter {
	// cache for objects which might occur in the file multiple times 
	private Map<Pair<EObject, String>, EObject> objectMap = new HashMap<>();
	
	/*
	 * Every time a sub-object is imported, a separate importer object will be invoked. 
	 * It's crucial that nested sub-objects are added to the top-level object map so that
	 * when they are encountered again, they will be looked up rather than instantiated. 
	 * 
	 */

	/*
	 * Each data row will normally contain data for several levels of nested objects
	 * - Some of that data will be data which does not appear on any other row,
	 *   and needs to be attached to a particular object (usually one which has
	 *   to be created)
	 * - Some of that data will serve as an index indicating that the object(s)
	 *   for this row should be attached to a particular list-value field in a
	 *   higher level object with the given index
	 * (non-Javadoc)
	 * @see com.mmxlabs.models.util.importer.impl.DefaultClassImporter#importObjects(org.eclipse.emf.ecore.EClass, com.mmxlabs.models.util.importer.CSVReader, com.mmxlabs.models.util.importer.IImportContext)
	 */
	
	@Override
	public Collection<EObject> importObjects(final EClass importClass, final CSVReader reader, final IImportContext context) {
		objectMap.clear();
		return super.importObjects(importClass, reader, context);
	}
	
	/**
	 * Import a particular reference field into an object using the appropriate class importer for that reference type from the registry. 
	 * If the field is a multiple (list) type, the imported sub-object is added to any existing contents of the list.  
	 * @param reference
	 * @param instance
	 * @param map
	 * @param context
	 * @param results
	 */
	@SuppressWarnings("unchecked")
	private void importReference(final EReference reference, final EObject instance, final IFieldMap map, final IImportContext context, final LinkedList<EObject> results) {
		// get the appropriate sub-importer from the importer registry and invoke it
		final IClassImporter classImporter = importerRegistry.getClassImporter(reference.getEReferenceType());
		
		final Collection<EObject> values;
		
		values = classImporter.importObject(instance, reference.getEReferenceType(), map, context);
		

		final Iterator<EObject> iterator = values.iterator();
		if (iterator.hasNext()) {
			// first object in the collection is the direct sub-object for the specified field  
			final EObject importObject = iterator.next();

			// when attaching an object to a multiple reference list, we append it to the list
			if (reference.isMany()) {
				((List<EObject>) instance.eGet(reference)).add(importObject);
			}
			// otherwise, we simply set the field value with the new object  
			else {
				instance.eSet(reference, importObject);
			}

			// if the imported sub-object is not contained in the base object, we need to add it to the list of
			// additional objects created.
			if (reference.isContainment() == false) {
				results.add(importObject);
			}
			// add any other recursively created sub-objects to the list of additional objects created
			while (iterator.hasNext()) {
				results.add(iterator.next());
			}
		}
		
	}

	@Override
	protected void importReferences(final IFieldMap row, final IImportContext context, final EClass rowClass, final EObject instance, final LinkedList<EObject> results) {
		for (final EReference reference : rowClass.getEAllReferences()) {
			if (!shouldImportReference(reference)) {
				continue;
			}
			final String lcrn = reference.getName().toLowerCase();
			if (row.containsKey(lcrn)) {
				// The reference itself is present, so do a lookup later
				final String referentName = row.get(lcrn).trim();
				if (!referentName.isEmpty()) {
					context.doLater(new SetReference(instance, reference, getEReferenceLinkType(reference), row.get(lcrn), context));
				}
			} else {
				// The reference is missing entirely
				// Maybe it is a sub-object; find any sub-keys
				final IFieldMap subKeys = row.getSubMap(lcrn + DOT);
						
				// The "extension" field has a special import syntax which allows multiple ones to be imported from the same row.
				if (reference == MMXCorePackage.Literals.MMX_OBJECT__EXTENSIONS) {
					if (subKeys.containsKey("count")) {
						final int count = Integer.parseInt(subKeys.get("count"));
						for (int i = 0; i < count; ++i) {
							final IFieldMap childMap = subKeys.getSubMap(i + DOT);
							importReference(reference, instance, childMap, context, results);
						}
					}
				} 
				else {
					// Complain if there was no data for the reference (unless the reference is a multiple / list type)   
					if (subKeys.isEmpty() && reference.isMany()) {
						if (reference.isContainment()) {
							populateWithBlank(instance, reference);
							notifyMissingFields((EObject) instance.eGet(reference), context.createProblem("Field not present", true, false, true), context);
						}

						context.addProblem(context.createProblem(reference.getName() + " is missing from " + instance.eClass().getName(), true, false, true));
					} else {
						importReference(reference, instance, subKeys, context, results);
					}
				}
			}
		}
	}
	
	/**
	 * Return the fieldname (as used in the CSV file) of the specified class used as
	 * a per-object unique identifier for objects of that class which belong to a 
	 * particular parent object (possibly a null parent).
	 *  
	 * @param parent
	 * @param eClass
	 * @return
	 */
	public String getIndexField(final EObject parent, final EClass eClass) {
		return null;
	}
	
	/**
	 * Return an object of a particular class, corresponding to a sub-field of a 
	 * parent object. If the class and parent have an "index field" associated with
	 * them, then look for an existing object based on data in the CSV row, and 
	 * return that if one exists. Otherwise, create a new object. 
	 *    
	 * @param parent
	 * @param eClass
	 * @param row
	 * @return
	 */
	public EObject getObject(final EObject parent, final EClass eClass, final Map<String, String> row) {
		EObject result = null;
		Pair<EObject, String> cacheKey = null; 				

		final String indexField = getIndexField(parent, eClass);
				
		if (indexField != null) {
			cacheKey = new Pair<EObject, String>(parent, row.get(indexField));
			result = objectMap.get(cacheKey);  
		}
		
		if (result == null) {
			result = eClass.getEPackage().getEFactoryInstance().create(eClass);
			if (cacheKey != null) {
				objectMap.put(cacheKey, result);
			}
		}
		
		return result;
	}
	
	public Collection<EObject> importObject(final EObject parent, final EClass eClass, final Map<String, String> row, final IImportContext context) {
		final EClass rowClass = getTrueOutputClass(eClass, row.get(KIND_KEY));
		try {
			// make sure the instance is looked up instead of created if it already exists
			final EObject instance = getObject(parent, rowClass, row);
			final LinkedList<EObject> results = new LinkedList<EObject>();
			results.add(instance);
			importAttributes(row, context, rowClass, instance);
			if (row instanceof IFieldMap) {
				importReferences((IFieldMap) row, context, rowClass, instance, results);
			} else {
				importReferences(new FieldMap(row), context, rowClass, instance, results);
			}
			return results;
		} catch (final IllegalArgumentException illegal) {
			context.addProblem(context.createProblem(row.get(KIND_KEY) + " is not a valid kind of " + rowClass.getName(), true, true, true));
			return Collections.emptySet();
		}
	}
	
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.common.Pair;
import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.common.csv.FieldMap;
import com.mmxlabs.common.csv.IFieldMap;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;

/**
 * Class to permit the generic import of multi-line data from a CSV file.
 * 
 * Semantics work as follows: The first line of the CSV file is mapped to a list of field names. These names mostly correspond to fields in the EMF model, although some of them are magic fields used
 * by the importer (or by specialised importer sub-classes).
 * 
 * Following conventional object-oriented notation, the field names may be delimited by one or more dots to indicate that they are sub-fields of structured objects (e.g. slot.name denotes the "name"
 * field of the "slot" field of the base object).
 * 
 * This class permits the use of more than one convention for importing multiple objects into a single field: 1) A particular sub-field may be used as an index field to identify unique imported
 * objects within a hierarchical context. The index serves to identify unique children of a particular EMF object, and need not be globally unique. When a specific index string is re-encountered in
 * the same hierarchical context, this allows new children to be added to the existing object.
 * 
 * 2) Sub-fields which are designated as multiple value fields may take multiple numeric suffixes within the same line (e.g. car.door.1 and car.door.2 denote data for two door objects to be added to
 * the car).
 * 
 * Index fields for particular EMF classes must currently be specified by importer sub-classes, although there is an EKeys property in the EMF model which has a similar semantics.
 * 
 * N.B. Behaviour of this class is unspecified when a single object has more than one direct multi-valued field.
 * 
 * This class is intended as an eventual replacement for the DefaultClassImporter
 * 
 * @author Simon McGregor
 * 
 */
public class MultiLineImporter extends DefaultClassImporter {
	// cache for objects which might occur in the file multiple times
	private Map<Pair<EObject, String>, EObject> objectMap = new HashMap<>();

	/*
	 * @Override public Collection<Map<String, String>> exportObjects(final Collection<? extends EObject> objects, final MMXRootObject root) { final LinkedList<Map<String, String>> result = new
	 * LinkedList<Map<String, String>>();
	 * 
	 * if (objects.isEmpty()) { return result; }
	 * 
	 * for (final EObject object : objects) { final Map<String, String> flattened = exportObject(object, root); flattened.put(KIND_KEY, object.eClass().getName()); result.add(flattened); } return
	 * result; }
	 */

	/*
	 * When exporting an object, the entire branching tree of multiple-content sub-fields needs to be written as successive lines to the file.
	 * 
	 * Exporting a multi-value attribute should work like: for each value, copy the row fields for objects higher up the hierarchy
	 * 
	 * (Unfinished code)
	 */

	protected Collection<Map<String, String>> multiExportObject(final EObject object, final IMMXExportContext context) {

		final Collection<Map<String, String>> result = new LinkedList<Map<String, String>>();
		final Map<String, String> row = new LinkedHashMap<String, String>();

		for (final EAttribute attribute : object.eClass().getEAllAttributes()) {
			if (shouldExportFeature(attribute)) {
				exportAttribute(object, attribute, row, context);
			}
		}

		for (final EReference reference : object.eClass().getEAllReferences()) {
			if (shouldExportFeature(reference)) {
				final Collection<Map<String, String>> subResult;
				subResult = multiExportReference(object, reference, row, context);
			}
		}

		return result;

	}

	/*
	 * Every time a sub-object is imported, a separate importer object will be invoked. It's crucial that nested sub-objects are added to the top-level object map so that when they are encountered
	 * again, they will be looked up rather than instantiated.
	 */

	/*
	 * Each data row will normally contain data for several levels of nested objects - Some of that data will be data which does not appear on any other row, and needs to be attached to a particular
	 * object (usually one which has to be created) - Some of that data will serve as an index indicating that the object(s) for this row should be attached to a particular list-value field in a
	 * higher level object with the given index (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.util.importer.impl.DefaultClassImporter#importObjects(org.eclipse.emf.ecore.EClass, com.mmxlabs.models.util.importer.CSVReader,
	 * com.mmxlabs.models.util.importer.IImportContext)
	 */

	/*
	 * (Unfinished code)
	 */
	protected Collection<Map<String, String>> multiExportReference(final EObject object, final EReference reference, final Map<String, String> row, final IMMXExportContext context) {
		Collection<Map<String, String>> result = null;

		if (shouldFlattenReference(reference)) {
			final EObject value = (EObject) object.eGet(reference);
			if (value != null) {
				final IClassImporter importer = Activator.getDefault().getImporterRegistry().getClassImporter(value.eClass());
				if (importer != null) {
					final Map<String, String> subMap = importer.exportObjects(Collections.singleton(value), context).iterator().next();
					for (final Map.Entry<String, String> e : subMap.entrySet()) {
						row.put(reference.getName() + DOT + e.getKey(), e.getValue());
					}
				}
			}
		} else {
			if (reference.isMany()) {
				if (reference == MMXCorePackage.Literals.MMX_OBJECT__EXTENSIONS) {

					final List<EObject> extensions = ((MMXObject) object).getExtensions();
					if (extensions != null) {
						int count = 0;
						for (final EObject extension : extensions) {
							final IClassImporter importer = Activator.getDefault().getImporterRegistry().getClassImporter(extension.eClass());
							if (importer != null) {
								final Map<String, String> subMap = importer.exportObjects(Collections.singleton(extension), context).iterator().next();
								for (final Map.Entry<String, String> e : subMap.entrySet()) {
									row.put(reference.getName() + DOT + count + DOT + e.getKey(), e.getValue());
								}
								++count;
							}
						}
						if (count > 0) {
							row.put(reference.getName() + DOT + "count", Integer.toString(count));
						}
					}
				} else {
					@SuppressWarnings("unchecked")
					final List<? extends EObject> values = (List<? extends EObject>) object.eGet(reference);
					for (EObject o : values) {
						final LinkedHashMap<String, String> newRow = new LinkedHashMap<String, String>();
						newRow.putAll(row);

					}
				}
			} else {
				final Object o = object.eGet(reference);
				if (o instanceof NamedObject) {
					final NamedObject no = (NamedObject) o;
					row.put(reference.getName(), no.getName());
				}
			}
		}

		return result;
	}

	@Override
	public Collection<EObject> importObjects(final EClass importClass, final CSVReader reader, final IMMXImportContext context) {
		try {
			return super.importObjects(importClass, reader, context);
		} finally {
			objectMap.clear();
		}
	}

	/**
	 * Import a particular reference field into an object using the appropriate class importer for that reference type from the registry. If the field is a multiple (list) type, the imported
	 * sub-object is added to any existing contents of the list.
	 * 
	 * @param reference
	 * @param instance
	 * @param map
	 * @param context
	 * @param results
	 */
	@SuppressWarnings("unchecked")
	private void importReference(final EReference reference, final EObject instance, final IFieldMap map, final IMMXImportContext context) {
		// get the appropriate sub-importer from the importer registry and invoke it
		final IClassImporter classImporter = importerRegistry.getClassImporter(reference.getEReferenceType());

		final ImportResults importResults = classImporter.importObject(instance, reference.getEReferenceType(), map, context);

		final EObject importObject = importResults.importedObject;

		if (importObject != null) {

			// final Iterator<EObject> iterator = importResults.createdExtraObjects.iterator();
			// when attaching an object to a multiple reference list, we append it to the list
			if (reference.isMany()) {
				((List<EObject>) instance.eGet(reference)).add(importObject);
			}
			// otherwise, we simply set the field value with the new object
			else {
				instance.eSet(reference, importObject);
			}

		}

	}

	@Override
	protected void importReferences(final IFieldMap row, final IMMXImportContext context, final EClass rowClass, final EObject instance) {
		for (final EReference reference : rowClass.getEAllReferences()) {
			if (!shouldImportReference(reference)) {
				continue;
			}
			final String lcrn = reference.getName().toLowerCase();
			if (!reference.isContainment()) {
				if (row.containsKey(lcrn)) {
					// The reference itself is present, so do a lookup later
					final String referentName = row.get(lcrn).trim();
					if (!referentName.isEmpty()) {
						context.doLater(new SetReference(instance, reference, getEReferenceLinkType(reference), row.get(lcrn), context));
					}
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
							importReference(reference, instance, childMap, context);
						}
					}
				} else {
					// Complain if there was no data for the reference (unless the reference is a multiple / list type)
					if (subKeys.isEmpty() && reference.isMany()) {
						if (reference.isContainment()) {
							// SUSPECT CODE - This applies to the isMany == false case
							// populateWithBlank(instance, reference);
							// notifyMissingFields((EObject) instance.eGet(reference), context.createProblem("Field not present", true, false, true), context);
						}

						context.addProblem(context.createProblem(reference.getName() + " is missing from " + instance.eClass().getName(), true, false, true));
					} else {
						// multiple elements can be imported within a single row, if they are preceded by consecutive digits beginning with 0
						if (subKeys.containsPrefix("0" + DOT)) {
							Integer i = 0;
							while (subKeys.containsPrefix(i.toString() + DOT)) {
								importReference(reference, instance, subKeys.getSubMap(i.toString() + DOT), context);
								i++;
							}
						} else {
							importReference(reference, instance, subKeys, context);
						}
					}
				}
			}
		}
	}

	/**
	 * Return the fieldname (as used in the CSV file) of the specified class used as a per-object unique identifier for objects of that class which belong to a particular parent object (possibly a
	 * null parent).
	 * 
	 * @param parent
	 * @param eClass
	 * @return
	 */
	public String getIndexField(final EObject parent, final EClass eClass) {
		// TODO:
		return null;
	}

	/**
	 * Return an object of a particular class, corresponding to a sub-field of a parent object. If the class and parent have an "index field" associated with them, then look for an existing object
	 * based on data in the CSV row, and return that if one exists. Otherwise, create a new object.
	 * 
	 * @param parent
	 * @param eClass
	 * @param row
	 * @return
	 */
	public EObject getObject(final EObject parent, final EClass eClass, final Map<String, String> row) {
		EObject result = null;

		final String indexField = getIndexField(parent, eClass);

		if (indexField != null) {
			Pair<EObject, String> cacheKey = new Pair<EObject, String>(parent, row.get(indexField));
			result = objectMap.get(cacheKey);
		}

		return result;
	}

	public EObject makeObject(final EObject parent, final EClass eClass, final Map<String, String> row) {
		final EObject result = eClass.getEPackage().getEFactoryInstance().create(eClass);

		final String indexField = getIndexField(parent, eClass);

		if (indexField != null) {
			final Pair<EObject, String> cacheKey = new Pair<EObject, String>(parent, row.get(indexField));
			objectMap.put(cacheKey, result);
		}

		return result;
	}

	@Override
	public ImportResults importObject(final EObject parent, final EClass eClass, final Map<String, String> row, final IMMXImportContext context) {
		final EClass rowClass = getTrueOutputClass(eClass, row.get(KIND_KEY));
		try {
			boolean objectCreated = false;
			// look up to see if the instance already exists
			EObject instance = getObject(parent, rowClass, row);
			if (instance == null) {
				// otherwise, make a new one
				instance = makeObject(parent, rowClass, row);
				// and add it to the list of created objects
				objectCreated = true;
			}

			final ImportResults results = new ImportResults(instance, objectCreated);

			importAttributes(row, context, rowClass, instance);
			if (row instanceof IFieldMap) {
				importReferences((IFieldMap) row, context, rowClass, instance);
			} else {
				importReferences(new FieldMap(row), context, rowClass, instance);
			}
			return results;
		} catch (final IllegalArgumentException illegal) {
			context.addProblem(context.createProblem(row.get(KIND_KEY) + " is not a valid kind of " + rowClass.getName(), true, true, true));
			return new ImportResults(null);
		}
	}

}

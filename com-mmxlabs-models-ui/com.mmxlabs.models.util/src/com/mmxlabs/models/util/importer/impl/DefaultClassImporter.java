/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.common.Equality;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.FieldMap;
import com.mmxlabs.models.util.importer.IAttributeImporter;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IFieldMap;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.IImportContext.IImportProblem;
import com.mmxlabs.models.util.importer.registry.IImporterRegistry;

public class DefaultClassImporter extends AbstractClassImporter {
	protected static final String KIND_KEY = "kind";
	protected static final String DOT = ".";

	/**
	 * Simple record structure class to hold results of importing a row of CSV data.
	 * 
	 * @author Simon McGregor
	 * 
	 * @param <T>
	 * @since 8.0
	 */
	public static class ImportResults {
		final public EObject importedObject;
		private final LinkedList<EObject> createdExtraObjects = new LinkedList<EObject>();
		private final boolean wasObjectCreated;

		public ImportResults(EObject object, boolean created) {
			importedObject = object;
			wasObjectCreated = created;
			if (created) {
				createdExtraObjects.add(object);
			}
		}

		public ImportResults(EObject object) {
			this(object, true);
		}

		public void add(EObject object) {
			createdExtraObjects.add(object);
		}

		public List<EObject> getCreatedObjects() {
			return createdExtraObjects;

		}

	}

	/**
	 * @since 2.0
	 */
	@Inject
	protected IImporterRegistry importerRegistry;

	/**
	 * @since 2.0
	 */
	public DefaultClassImporter() {
		final Activator activator = Activator.getDefault();
		if (activator != null) {

			importerRegistry = activator.getImporterRegistry();
		}
	}

	@Override
	public Collection<EObject> importObjects(final EClass importClass, final CSVReader reader, final IImportContext context) {
		final List<EObject> results = new ArrayList<EObject>();
		try {
			try {
				context.pushReader(reader);
				Map<String, String> row;
				while ((row = reader.readRow(true)) != null) {
					results.addAll(importObject(null, importClass, row, context).createdExtraObjects);
				}
			} finally {
				reader.close();
				context.popReader();
			}
		} catch (final IOException e) {

		}
		for (final EObject o : results) {
			if (o instanceof NamedObject) {
				context.registerNamedObject((NamedObject) o);
			}
		}
		return results;
	}

	protected EClass getTrueOutputClass(final EClass outputEClass, final String kind) {
		if (kind == null) {
			return outputEClass;
		}

		final EPackage ePackage = outputEClass.getEPackage();
		// find a subclass with the right name
		for (final EClassifier classifier : ePackage.getEClassifiers()) {
			if (classifier instanceof EClass) {
				final EClass possibleSubClass = (EClass) classifier;
				if (outputEClass.isSuperTypeOf(possibleSubClass) && (possibleSubClass.isAbstract() == false) && possibleSubClass.getName().equalsIgnoreCase(kind)) {
					return (EClass) classifier;
				}
			}
		}
		// All registry packages...
		for (final Object obj : Registry.INSTANCE.values()) {
			if (obj instanceof EPackage) {
				final EPackage ePackage2 = (EPackage) obj;
				for (final EClassifier classifier : ePackage2.getEClassifiers()) {
					if (classifier instanceof EClass) {
						final EClass possibleSubClass = (EClass) classifier;
						// EObject is not listed in supertypes?
						final boolean superTypeOf = outputEClass == EcorePackage.Literals.EOBJECT || outputEClass.isSuperTypeOf(possibleSubClass);
						final boolean b = possibleSubClass.isAbstract() == false;
						final boolean equalsIgnoreCase = possibleSubClass.getName().equalsIgnoreCase(kind);
						if (superTypeOf && b && equalsIgnoreCase) {
							return (EClass) classifier;
						}
					}
				}
			}

		}

		return outputEClass;
	}

	/**
	 * @since 8.0
	 */
	@Override
	public ImportResults importObject(final EObject parent, final EClass eClass, final Map<String, String> row, final IImportContext context) {
		final EClass rowClass = getTrueOutputClass(eClass, row.get(KIND_KEY));
		try {
			final EObject instance = rowClass.getEPackage().getEFactoryInstance().create(rowClass);
			final ImportResults results = new ImportResults(instance);
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

	/**
	 * Opportunity to specialise a reference type which is too general in the model. Contracts, etc.
	 * 
	 * @param reference
	 * @return
	 */
	protected EClass getEReferenceLinkType(final EReference reference) {
		return reference.getEReferenceType();
	}

	/**
	 * @since 8.0
	 */
	protected void importReferences(final IFieldMap row, final IImportContext context, final EClass rowClass, final EObject instance) {
		for (final EReference reference : rowClass.getEAllReferences()) {
			if (!shouldImportReference(reference)) {
				continue;
			}

			final String lcrn = reference.getName().toLowerCase();

			// If the reference is marked in the EMF model as "not contained", we expect
			// a field for the reference directly in the data as a REF_NAME field, which we will use for lookup later
			// if (!reference.isContainment()) {
			if (row.containsKey(lcrn)) {
				if (reference.isContainment()) {
					// System.err.println("Got " + reference.getContainerClass().getName() + "." + reference.getName() + " as direct CSV data");
				}

				if (!row.containsKey(lcrn)) {
					notifyMissingFields((EObject) instance.eGet(reference), context.createProblem("Field not present", true, false, true), context);
					continue;
				}

				// The reference itself is present, so do a lookup later
				final String referentName = row.get(lcrn).trim();
				if (!referentName.isEmpty()) {
					context.doLater(new SetReference(instance, reference, getEReferenceLinkType(reference), row.get(lcrn), context));
				}
			}
			// If the reference is marked in the EMF model as "contained" by its parent, we expect the child object's fields in the CSV data
			// under hierarchical REF_NAME.FIELD fields
			else {
				// The CSV data should not contain REF_NAME as a direct field; it will be ignored if present

				final IFieldMap subKeys = row.getSubMap(lcrn + DOT);

				if (!reference.isContainment() && !subKeys.isEmpty()) {
					// System.err.println("Got " + lcrn + " as direct CSV data");
				}

				if (reference.isMany()) {
					if (reference == MMXCorePackage.Literals.MMX_OBJECT__EXTENSIONS) {
						if (subKeys.containsKey("count")) {
							String countStr = subKeys.get("count");
							if (countStr != null && !countStr.isEmpty()) {
								final int count;
								try {
									count = Integer.parseInt(countStr);
								} catch (NumberFormatException e) {
									context.addProblem(context.createProblem(String.format("Error parsing %s as an integer for %s field", countStr, reference.getName()), true, true, true));
									continue;
								}
								final List<EObject> references = new LinkedList<EObject>();
								for (int i = 0; i < count; ++i) {
									final IFieldMap childMap = subKeys.getSubMap(i + DOT);
									final IClassImporter classImporter = importerRegistry.getClassImporter(reference.getEReferenceType());
									final ImportResults importResults = classImporter.importObject(instance, reference.getEReferenceType(), childMap, context);
									final Collection<EObject> values = importResults.createdExtraObjects;
									final Iterator<EObject> iterator = values.iterator();
									final EObject importObject = iterator.next();
									references.add(importObject);
								}
								instance.eSet(reference, references);
							}
						}
					} else {
						continue;
					}
				} else {

					if (subKeys.isEmpty()) {
						if (reference.isContainment()) {

							populateWithBlank(instance, reference);

							notifyMissingFields((EObject) instance.eGet(reference), context.createProblem("Field not present", true, false, true), context);

						}

						context.addProblem(context.createProblem(reference.getName() + " is missing from " + instance.eClass().getName(), true, false, true));
					} else {
						final IClassImporter classImporter = importerRegistry.getClassImporter(reference.getEReferenceType());
						final Collection<EObject> values = classImporter.importObject(instance, reference.getEReferenceType(), subKeys, context).createdExtraObjects;
						final Iterator<EObject> iterator = values.iterator();
						if (iterator.hasNext()) {
							instance.eSet(reference, iterator.next());
						}
					}
				}
			}
		}
	}

	/**
	 * @since 8.0
	 */
	protected boolean shouldImportReference(final EReference reference) {
		return true;
	}

	/**
	 * @since 8.0
	 */
	protected void notifyMissingFields(final EObject blank, final IImportProblem delegate, final IImportContext context) {
		if (blank == null) {
			return;
		}
		for (final EAttribute attribute : blank.eClass().getEAllAttributes()) {
			context.addProblem(new IImportProblem() {
				@Override
				public String getProblemDescription() {
					return delegate.getProblemDescription();
				}

				@Override
				public Integer getLineNumber() {
					return delegate.getLineNumber();
				}

				@Override
				public String getFilename() {
					return delegate.getFilename();
				}

				@Override
				public String getField() {
					return delegate.getField() + "." + attribute.getName();
				}

				@Override
				public boolean equals(final Object obj) {
					if (obj instanceof IImportProblem) {
						final IImportProblem other = (IImportProblem) obj;
						return Equality.isEqual(getProblemDescription(), other.getProblemDescription()) && Equality.isEqual(getLineNumber(), other.getLineNumber())
								&& Equality.isEqual(getFilename(), other.getFilename()) && Equality.isEqual(getField(), other.getField());
					} else {
						return false;
					}
				}

				@Override
				public int hashCode() {
					return (getProblemDescription() == null ? 0 : getProblemDescription().hashCode()) + (getLineNumber() == null ? 0 : getLineNumber().hashCode())
							+ (getFilename() == null ? 0 : getFilename().hashCode()) + (getField() == null ? 0 : getField().hashCode());
				}

			});
		}

		for (final EObject content : blank.eContents()) {
			notifyMissingFields(content, new IImportProblem() {
				@Override
				public String getProblemDescription() {
					return delegate.getProblemDescription();
				}

				@Override
				public Integer getLineNumber() {
					return delegate.getLineNumber();
				}

				@Override
				public String getFilename() {
					return delegate.getFilename();
				}

				@Override
				public String getField() {
					return delegate.getField() + "." + content.eContainingFeature().getName();
				}
			}, context);
		}
	}

	protected void populateWithBlank(final EObject target, final EReference reference) {
		final EClass objectClass = reference.getEReferenceType();
		if (objectClass.isAbstract()) {
			return;
		}
		final EObject content = objectClass.getEPackage().getEFactoryInstance().create(objectClass);
		for (final EReference ref : content.eClass().getEAllReferences()) {
			if (ref.isContainment() && ref.isMany() == false) {
				populateWithBlank(content, ref);
			}
		}
		target.eSet(reference, content);
	}

	protected void importAttributes(final Map<String, String> row, final IImportContext context, final EClass rowClass, final EObject instance) {
		for (final EAttribute attribute : rowClass.getEAllAttributes()) {
			final String lowerCase = attribute.getName().toLowerCase();
			if (row.containsKey(lowerCase)) {
				final IAttributeImporter ai = importerRegistry.getAttributeImporter(attribute.getEAttributeType());
				if (ai != null) {
					ai.setAttribute(instance, attribute, row.get(lowerCase), context);
				}
			} else {
				boolean shouldWarn = shouldExportFeature(attribute);
				if (shouldWarn) {
					if (attribute == MMXCorePackage.eINSTANCE.getOtherNamesObject_OtherNames()) {
						// Annotation is not on the feature itself, but rather the sub-class
						final EAnnotation annotation = instance.eClass().getEAnnotation("http://www.mmxlabs.com/models/mmxcore/annotations/namedobject");
						boolean exportOtherNames = false;
						if (annotation != null) {
							final String showOtherNamesAnnotation = annotation.getDetails().get("showOtherNames");
							if (showOtherNamesAnnotation != null) {
								exportOtherNames = Boolean.parseBoolean(showOtherNamesAnnotation);
							}
						}
						if (!exportOtherNames) {
							shouldWarn = false;
						}
					}
				}

				if (shouldWarn) {
					context.addProblem(context.createProblem("Field not present", true, false, true));
				}
			}
		}
	}

	@Override
	public Collection<Map<String, String>> exportObjects(final Collection<? extends EObject> objects, final MMXRootObject root) {
		final LinkedList<Map<String, String>> result = new LinkedList<Map<String, String>>();

		if (objects.isEmpty()) {
			return result;
		}

		for (final EObject object : objects) {
			final Map<String, String> flattened = exportObject(object, root);
			flattened.put(KIND_KEY, object.eClass().getName());
			result.add(flattened);
		}
		return result;
	}

	protected Map<String, String> exportObject(final EObject object, final MMXRootObject root) {
		final Map<String, String> result = new LinkedHashMap<String, String>();

		for (final EAttribute attribute : object.eClass().getEAllAttributes()) {
			if (shouldExportFeature(attribute)) {
				exportAttribute(object, attribute, result);
			}
		}

		for (final EReference reference : object.eClass().getEAllReferences()) {
			if (shouldExportFeature(reference)) {
				exportReference(object, reference, result, root);
			}
		}

		return result;
	}

	protected void exportAttribute(final EObject object, final EAttribute attribute, final Map<String, String> result) {
		final IAttributeImporter ai = Activator.getDefault().getImporterRegistry().getAttributeImporter(attribute.getEAttributeType());
		if (ai != null) {

			// Determine whether or not to export the othernames feature
			if (attribute == MMXCorePackage.eINSTANCE.getOtherNamesObject_OtherNames()) {
				// Annotation is not on the feature itself, but rather the sub-class
				final EAnnotation annotation = object.eClass().getEAnnotation("http://www.mmxlabs.com/models/mmxcore/annotations/namedobject");
				boolean exportOtherNames = false;
				if (annotation != null) {
					final String showOtherNamesAnnotation = annotation.getDetails().get("showOtherNames");
					if (showOtherNamesAnnotation != null) {
						exportOtherNames = Boolean.parseBoolean(showOtherNamesAnnotation);
					}
				}
				if (!exportOtherNames) {
					return;
				}
			}

			result.put(attribute.getName(), ai.writeAttribute(object, attribute, object.eGet(attribute)));
		}
	}

	protected void exportReference(final EObject object, final EReference reference, final Map<String, String> result, final MMXRootObject root) {
		if (shouldFlattenReference(reference)) {
			final EObject value = (EObject) object.eGet(reference);
			if (value != null) {
				final IClassImporter importer = Activator.getDefault().getImporterRegistry().getClassImporter(value.eClass());
				if (importer != null) {
					final Map<String, String> subMap = importer.exportObjects(Collections.singleton(value), root).iterator().next();
					for (final Map.Entry<String, String> e : subMap.entrySet()) {
						result.put(reference.getName() + DOT + e.getKey(), e.getValue());
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
								final Map<String, String> subMap = importer.exportObjects(Collections.singleton(extension), root).iterator().next();
								for (final Map.Entry<String, String> e : subMap.entrySet()) {
									result.put(reference.getName() + DOT + count + DOT + e.getKey(), e.getValue());
								}
								++count;
							}
						}
						if (count > 0) {
							result.put(reference.getName() + DOT + "count", Integer.toString(count));
						}
					}
				} else {
					@SuppressWarnings("unchecked")
					final List<? extends Object> values = (List<? extends Object>) object.eGet(reference);
					final StringBuffer sb = new StringBuffer();
					boolean comma = false;
					for (final Object o : values) {
						if (o instanceof NamedObject) {
							final NamedObject no = (NamedObject) o;
							if (comma) {
								sb.append(",");
							}
							comma = true;
							sb.append(no.getName());
						}
					}

					result.put(reference.getName(), sb.toString());
				}
			} else {
				final Object o = object.eGet(reference);
				if (o instanceof NamedObject) {
					final NamedObject no = (NamedObject) o;
					result.put(reference.getName(), no.getName());
				}
			}
		}
	}

	protected boolean shouldExportFeature(final EStructuralFeature feature) {
		return !(feature == MMXCorePackage.eINSTANCE.getUUIDObject_Uuid());
	}

	protected boolean shouldFlattenReference(final EReference reference) {
		return reference.isMany() == false && reference.isContainment() == true;
	}

	/**
	 * @since 2.0
	 * @param importerRegistry
	 */
	public void setImporterRegistry(final IImporterRegistry importerRegistry) {
		this.importerRegistry = importerRegistry;
	}

}

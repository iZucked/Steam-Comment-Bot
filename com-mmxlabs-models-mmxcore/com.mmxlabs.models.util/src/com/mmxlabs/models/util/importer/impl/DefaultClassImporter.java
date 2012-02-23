package com.mmxlabs.models.util.importer.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IAttributeImporter;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IImportContext;

public class DefaultClassImporter implements IClassImporter {
	private static final String KIND_KEY = "kind";
	private static final String DOT = ".";

	@Override
	public Collection<EObject> importObjects(EClass importClass,
			String filename, IImportContext context) {
		final LinkedList<EObject> results = new LinkedList<EObject>();
		try {
			final CSVReader reader = new CSVReader(filename);
			try {
				Map<String, String> row;
				while ((row = reader.readRow()) != null) {
					results.addAll(importObject(importClass, row, context));
				}
			} finally {
				reader.close();
			}
		} catch (IOException e) {

		}
		for (final EObject o : results) {
			if (o instanceof NamedObject) {
				context.registerNamedObject((NamedObject) o);
			}
		}
		return results;
	}

	protected EClass getTrueOutputClass(final EClass outputEClass,
			final String kind) {
		if (kind == null)
			return outputEClass;

		final EPackage ePackage = outputEClass.getEPackage();
		// find a subclass with the right name
		for (final EClassifier classifier : ePackage.getEClassifiers()) {
			if (classifier instanceof EClass) {
				final EClass possibleSubClass = (EClass) classifier;
				if (outputEClass.isSuperTypeOf(possibleSubClass)
						&& (possibleSubClass.isAbstract() == false)
						&& possibleSubClass.getName().equalsIgnoreCase(kind)) {
					return (EClass) classifier;
				}
			}
		}

		return outputEClass;
	}

	@Override
	public Collection<EObject> importObject(final EClass eClass,
			final Map<String, String> row, IImportContext context) {
		final EClass rowClass = getTrueOutputClass(eClass, row.get(KIND_KEY));
		final EObject instance = rowClass.getEPackage().getEFactoryInstance()
				.create(rowClass);
		final LinkedList<EObject> results = new LinkedList<EObject>();
		results.add(instance);
		importAttributes(row, context, rowClass, instance);
		importReferences(row, context, rowClass, instance, results);
		return results;
	}

	/**
	 * Opportunity to specialise a reference type which is too general in the model. Contracts, etc.
	 * @param reference
	 * @return
	 */
	protected EClass getEReferenceLinkType(final EReference reference) {
		return reference.getEReferenceType();
	}
	
	protected void importReferences(final Map<String, String> row,
			IImportContext context, final EClass rowClass,
			final EObject instance, final LinkedList<EObject> results) {
		for (final EReference reference : rowClass.getEAllReferences()) {
			final String lcrn = reference.getName().toLowerCase();
			if (row.containsKey(lcrn)) {
				// defer lookup
				context.doLater(new SetReference(instance, reference, getEReferenceLinkType(reference), row
						.get(lcrn)));
			} else {
				if (reference.isMany())
					continue;
				final HashMap<String, String> subKeys = new HashMap<String, String>();
				for (final String key : row.keySet()) {
					if (key.startsWith(lcrn + DOT)) {
						subKeys.put(key.substring(lcrn.length() + 1),
								row.get(key));
					}
				}
				if (subKeys.isEmpty()) {
					//TODO WARNING
				} else {
					final IClassImporter classImporter = Activator.getDefault()
							.getImporterRegistry()
							.getClassImporter(reference.getEReferenceType());
					final Collection<EObject> values = classImporter
							.importObject(reference.getEReferenceType(),
									subKeys, context);
					final Iterator<EObject> iterator = values.iterator();
					instance.eSet(reference, iterator.next());
					if (reference.isContainment() == false)
						results.add((EObject) instance.eGet(reference));
					while (iterator.hasNext())
						results.add(iterator.next());
				}
			}
		}
	}

	protected void importAttributes(final Map<String, String> row,
			IImportContext context, final EClass rowClass,
			final EObject instance) {
		for (final EAttribute attribute : rowClass.getEAllAttributes()) {
			if (row.containsKey(attribute.getName().toLowerCase())) {
				final IAttributeImporter ai = Activator.getDefault()
						.getImporterRegistry()
						.getAttributeImporter(attribute.getEAttributeType());
				ai.setAttribute(instance, attribute,
						row.get(attribute.getName().toLowerCase()), context);
			} else {
				// TODO WARN
			}
		}
	}

	@Override
	public Collection<Map<String, String>> exportObjects(
			Collection<EObject> objects) {
		final LinkedList<Map<String, String>> result = new LinkedList<Map<String, String>>();
		for (final EObject object : objects) {
			result.add(exportObject(object));
		}
		return result;
	}

	protected Map<String, String> exportObject(final EObject object) {
		final Map<String, String> result = new HashMap<String, String>();
		for (final EReference reference : object.eClass().getEAllReferences()) {
			if (shouldExportFeature(reference))
				exportReference(object, reference, result);
		}
		for (final EAttribute attribute : object.eClass().getEAllAttributes()) {
			if (shouldExportFeature(attribute))
				exportAttribute(object, attribute, result);
		}
		return result;
	}

	protected void exportAttribute(EObject object, EAttribute attribute,
			Map<String, String> result) {
		final IAttributeImporter ai = Activator.getDefault()
				.getImporterRegistry()
				.getAttributeImporter(attribute.getEAttributeType());
		result.put(attribute.getName(),
				ai.writeAttribute(object, attribute, object.eGet(attribute)));
	}

	protected void exportReference(EObject object, EReference reference,
			Map<String, String> result) {
		if (shouldFlattenReference(reference)) {
			final EObject value = (EObject) object.eGet(reference);
			if (value != null) {
				final IClassImporter importer = Activator.getDefault()
						.getImporterRegistry().getClassImporter(value.eClass());
				if (importer != null) {
					final Map<String, String> subMap = importer
							.exportObjects(Collections.singleton(value))
							.iterator().next();
					for (final Map.Entry<String, String> e : subMap.entrySet()) {
						result.put(reference.getName() + DOT + e.getKey(),
								e.getValue());
					}
				}
			}
		} else {
			if (MMXCorePackage.eINSTANCE.getNamedObject().isSuperTypeOf(
					reference.getEReferenceType())) {
				if (reference.isMany()) {
					final EList<? extends NamedObject> values = (EList<? extends NamedObject>) object
							.eGet(reference);
					final StringBuffer sb = new StringBuffer();
					boolean comma = false;
					for (final NamedObject no : values) {
						if (comma)
							sb.append(",");
						comma = true;
						sb.append(no.getName());
					}
					result.put(reference.getName(), sb.toString());
				} else {
					final NamedObject no = (NamedObject) object.eGet(reference);
					if (no != null)
						result.put(reference.getName(), no.getName());
				}
			}
		}
	}

	protected boolean shouldExportFeature(final EStructuralFeature feature) {
		return true;
	}
	
	protected boolean shouldFlattenReference(final EReference reference) {
		return reference.isMany() == false && reference.isContainment() == true;
	}
}

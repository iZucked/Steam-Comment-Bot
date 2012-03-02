/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.importer.importers;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scenario.ScenarioPackage;
import scenario.port.Port;
import scenario.port.PortPackage;

import com.mmxlabs.lngscheduler.emf.extras.EMFUtils;

/**
 * Gadget for importing EObjects and their singly contained sub-objects. If you want to override the default import / export behaviour, you can subclass this.
 * 
 * @author Tom Hinton
 * 
 */
public class EObjectImporter {
	private static final Logger log = LoggerFactory.getLogger(EObjectImporter.class);
	protected static final String SEPARATOR = ".";
	protected EClass outputEClass;

	public EObjectImporter() {

	}

	public void setOutputEClass(final EClass outputEClass) {
		this.outputEClass = outputEClass;
	}

	protected EClass getTrueOutputClass(final Map<String, String> fields) {
		final EPackage ePackage = outputEClass.getEPackage();
		if (fields.containsKey("kind")) {
			// find a subclass with the right name
			for (final EClassifier classifier : ePackage.getEClassifiers()) {
				if (classifier instanceof EClass) {
					final EClass possibleSubClass = (EClass) classifier;
					if (outputEClass.isSuperTypeOf(possibleSubClass) && (possibleSubClass.isAbstract() == false) && possibleSubClass.getName().equalsIgnoreCase(fields.get("kind"))) {
						return (EClass) classifier;
					}
				}
			}
		}
		return outputEClass;
	}

	private final Map<String, Collection<Map<String, String>>> currentExportResults = new HashMap<String, Collection<Map<String, String>>>();
	private CSVReader currentReader;

	protected CSVReader getCurrentReader() {
		return currentReader;
	}

	/**
	 * Flatten a bunch of EObjects down into a lot of key-value maps.
	 * 
	 * This isn't quite symmetric with the import side API.
	 * 
	 * @param objects
	 *            some EObjects to export
	 * @return a map from group name to a collection of rows, in key-value form.
	 */
	public Map<String, Collection<Map<String, String>>> exportObjects(final Collection<? extends EObject> objects) {

		// default thing is to check whether the objects are variadic (for the
		// kind column)
		// and then export each object one at a time.

		// the caller can then decide how to serialize the data.

		currentExportResults.clear();

		final boolean sameTypes = EMFUtils.allSameEClass(objects) && !outputEClass.isAbstract();
		final Collection<Map<String, String>> results = addExportFile(outputEClass.getName());

		new LinkedList<Map<String, String>>();
		for (final EObject object : objects) {
			final Map<String, String> map = exportObject(object);
			if (sameTypes == false) {
				map.put("kind", object.eClass().getName());
			}
			results.add(map);
		}

		return currentExportResults;
	}

	/**
	 * Create another file to export to
	 * 
	 * @param filename
	 * @return
	 */
	protected Collection<Map<String, String>> addExportFile(final String filename) {
		final LinkedList<Map<String, String>> rows = new LinkedList<Map<String, String>>();
		currentExportResults.put(filename, rows);
		return rows;
	}

	/**
	 * Flatten the given object into its component parts
	 * 
	 * @param object
	 * @return a map which describes object
	 */
	protected Map<String, String> exportObject(final EObject object) {
		return exportObject(object, "");
	}

	/**
	 * @param eGet
	 * @param prefix2
	 * @return
	 */
	private Map<String, String> exportObject(final EObject object, final String prefix) {
		// First write out all the fields in the object, then write all the
		// fields in its contained objects
		final LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();
		flattenAttributesAndReferences(object, prefix, result);
		flattenContainments(object, prefix, result);
		return result;
	}

	/**
	 * Add all of the attributes of the given object to the given output map, prefixing field names with the prefix and SEPARATOR
	 * 
	 * @param object
	 * @param prefix
	 * @param output
	 */
	protected void flattenAttributesAndReferences(final EObject object, final String prefix, final Map<String, String> output) {
		String timezone = "UTC";
		for (final EReference ref : object.eClass().getEAllReferences()) {
			if (ref.getEReferenceType().equals(PortPackage.eINSTANCE.getPort()) && (ref.isContainment() == false) && (ref.isMany() == false)) {
				final Port p = (Port) object.eGet(ref);
				if (p != null) {
					timezone = p.getTimeZone();
					break;
				}
			}
		}

		for (final EAttribute attribute : object.eClass().getEAllAttributes()) {
			final Object value = (!attribute.isUnsettable() || object.eIsSet(attribute)) ? object.eGet(attribute) : null;
			if (attribute.isMany()) {
				final List<?> values = (List) value;

				final StringBuffer sb = new StringBuffer();
				boolean comma = false;
				for (final Object v : values) {
					final String svalue = attributeValueToString(timezone, attribute, v);
					sb.append((comma ? "," : "") + svalue);
					comma = true;
				}
				output.put(prefix + attribute.getName(), sb.toString());
			} else {
				final String svalue = attributeValueToString(timezone, attribute, value);
				output.put(prefix + attribute.getName(), svalue);
			}
			
		}

		for (final EReference reference : object.eClass().getEAllReferences()) {
			if (reference.isContainment()) {
				continue;
			}
			if (reference.isMany()) {
				final EList<EObject> values = (EList<EObject>) object.eGet(reference);

				final StringBuffer sb = new StringBuffer();
				boolean comma = false;
				for (final EObject v : values) {
					sb.append((comma ? "," : "") + NamedObjectRegistry.getName(v));
					comma = true;
				}
				output.put(prefix + reference.getName(), sb.toString());
			} else {
				final EObject value = (EObject) object.eGet(reference);
				output.put(prefix + reference.getName(), NamedObjectRegistry.getName(value));
			}
		}
	}

	public String attributeValueToString(final String timezone, final EAttribute attribute, final Object value) {
		String svalue = "";
		if (value instanceof Date) {
			svalue = DateTimeParser.getInstance().formatDate((Date) value, timezone);
		} else if (attribute.getEType() == ScenarioPackage.eINSTANCE.getPercentage()) {
			if ((value == null) || (((Number) value).doubleValue() == 0)) {
				svalue = "0";
			} else {
				svalue = String.format("%.1g", ((Number) value).doubleValue() * 100.0);
			}
		} else if ((value instanceof Float) || (value instanceof Double)) {
			svalue = String.format("%3g", ((Number) value).doubleValue());
		} else if (value != null) {
			svalue = value.toString();
		}
		return svalue;
	}

	/**
	 * Recursively flatten the fields of all singly-contained objects.
	 * 
	 * @param object
	 * @param prefix
	 * @param output
	 */
	protected void flattenContainments(final EObject object, final String prefix, final Map<String, String> output) {
		for (final EReference ref : object.eClass().getEAllContainments()) {
			if (ref.isMany()) {
				flattenMultiContainment(object, prefix, ref, output);
			} else {
				flattenSingleContainment(object, prefix, output, ref);
			}
		}
	}

	/**
	 * Flatten a singly contained object. Default behaviour is to add more fields.
	 * 
	 * @param object
	 * @param prefix
	 * @param output
	 * @param ref
	 */
	protected void flattenSingleContainment(final EObject object, final String prefix, final Map<String, String> output, final EReference ref) {
		// get exporter for contained data and do the business.
		final EObjectImporter exporter = EObjectImporterFactory.getInstance().getImporter(ref.getEReferenceType());

		final String prefix2 = prefix + ref.getName() + SEPARATOR;

		final Map<String, String> subObject = exporter.exportObject((EObject) object.eGet(ref), prefix2);

		for (final Map.Entry<String, String> entry : subObject.entrySet()) {
			output.put(entry.getKey(), entry.getValue());
		}
	}

	protected void flattenMultiContainment(final EObject object, final String prefix, final EReference reference, final Map<String, String> output) {
		// default behaviour is to create another file
		final EObjectImporter i2 = EObjectImporterFactory.getInstance().getImporter(reference.getEReferenceType());

		// i2.importObjects(reader, deferredReferences, registry)
		final Map<String, Collection<Map<String, String>>> subObjects = i2.exportObjects((Collection<EObject>) object.eGet(reference));

		String fieldValue = "";
		for (final Map.Entry<String, Collection<Map<String, String>>> e : subObjects.entrySet()) {
			final String adjustedName = NamedObjectRegistry.getName(object) + "-" + e.getKey();
			final Collection<Map<String, String>> extraFile = addExportFile(adjustedName);
			if (e.getKey().equals(reference.getEReferenceType().getName())) {
				fieldValue = adjustedName;
			}
			extraFile.addAll(e.getValue());
		}

		output.put(prefix + reference.getName(), fieldValue);
	}

	public Collection<EObject> importObjects(final CSVReader reader, final Collection<DeferredReference> deferredReferences, final NamedObjectRegistry registry) {
		currentReader = reader;
		final LinkedList<EObject> importedObjects = new LinkedList<EObject>();
		// open input file and retrieve fields

		try {
			Map<String, String> row = null;
			while ((row = reader.readRow()) != null) {
				importedObjects.add(importObject(row, deferredReferences, registry));
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}

		for (final String s : reader.getUnusedHeaders()) {
			warn("The field " + s + " was never used.", false, s);
		}

		return importedObjects;
	}

	private final LinkedList<IImportWarningListener> warningListeners = new LinkedList<IImportWarningListener>();

	public void addImportWarningListener(final IImportWarningListener listener) {
		warningListeners.add(listener);
	}

	public void removeImportWarningListener(final IImportWarningListener listener) {
		warningListeners.remove(listener);
	}

	protected void warn(final String message, final boolean includeLine, final String field) {
		final ImportWarning iw;
		if (getCurrentReader() == null) {
			iw = new ImportWarning(message, "", 0, field);
		} else {
			final String casedField = getCurrentReader().getCasedColumnName(field);
			iw = new ImportWarning(message, getCurrentReader().getFileName(), includeLine ? getCurrentReader().getLineNumber() : 0, casedField == null ? field : casedField);
		}
		for (final IImportWarningListener listener : warningListeners) {
			listener.importWarning(iw);
		}

		log.warn("Import warning:" + iw.toString());
	}

	/**
	 * @param referencePrefix
	 * @param subFields
	 * @param deferredReferences
	 * @param registry
	 * @return
	 */
	private EObject importObject(final String prefix, final Map<String, String> fields, final Collection<DeferredReference> deferredReferences, final NamedObjectRegistry registry) {
		final EPackage ePackage = outputEClass.getEPackage();
		final EFactory eFactory = ePackage.getEFactoryInstance();

		final EClass trueOutputClass = getTrueOutputClass(fields);
		final EObject result = eFactory.create(trueOutputClass);

		for (final EAttribute attribute : trueOutputClass.getEAllAttributes()) {
			populateAttribute(prefix, result, attribute, fields);
		}

		for (final EReference reference : trueOutputClass.getEAllReferences()) {
			if (reference.isContainment()) {
				populateContainment(prefix, result, reference, fields, deferredReferences, registry);
			} else {
				populateReference(prefix, result, reference, fields, deferredReferences);
			}
		}

		return result;
	}

	public EObject importObject(final Map<String, String> fields, final Collection<DeferredReference> deferredReferences, final NamedObjectRegistry registry) {
		return importObject("", fields, deferredReferences, registry);
	}

	/**
	 * @param prefix
	 * @param result
	 * @param reference
	 * @param fields
	 * @param deferredReferences
	 * @param registry
	 */
	protected void populateContainment(final String prefix, final EObject result, final EReference reference, final Map<String, String> fields, final Collection<DeferredReference> deferredReferences,
			final NamedObjectRegistry registry) {
		if (reference.isMany()) {
			// import multiple; default behaviour is to run another session
			final String referenceName = prefix + reference.getName().toLowerCase();
			if (fields.containsKey(referenceName)) {
				final String filePath = fields.get(referenceName);

				CSVReader reader = null;
				try {
					reader = currentReader.getAdjacentReader(filePath);
					final EObjectImporter importer = EObjectImporterFactory.getInstance().getImporter(reference.getEReferenceType());
					((EList<EObject>) result.eGet(reference)).addAll(importer.importObjects(reader, deferredReferences, registry));
				} catch (final IOException e) {

				} finally {
					if (reader != null) {
						try {
							reader.close();
						} catch (final IOException e) {
							log.error(e.getMessage(), e);
						}
					}
				}
			}
		} else {
			// get sub-fields
			final String referencePrefix = prefix + reference.getName().toLowerCase() + SEPARATOR;

			// perform import
			final EObjectImporter importer = EObjectImporterFactory.getInstance().getImporter(reference.getEReferenceType());
			importer.setCurrentReader(getCurrentReader());
			final EObject value = importer.importObject(referencePrefix, fields, deferredReferences, registry);
			result.eSet(reference, value);
		}
	}

	/**
	 * @param currentReader
	 */
	protected void setCurrentReader(final CSVReader currentReader) {
		this.currentReader = currentReader;
	}

	/**
	 * @param prefix
	 * @param result
	 * @param reference
	 * @param fields
	 * @param deferredReferences
	 */
	protected void populateReference(final String prefix, final EObject target, final EReference reference, final Map<String, String> fields, final Collection<DeferredReference> deferredReferences) {

		final String referenceName = prefix + reference.getName().toLowerCase();
		if (fields.containsKey(referenceName)) {
			final String value = fields.get(referenceName);

			if (reference.isMany()) {
				final String[] values = value.split(",");
				for (final String value2 : values) {
					deferredReferences.add(new DeferredReference(target, reference, value2.trim()));
				}
			} else {
				deferredReferences.add(new DeferredReference(target, reference, value.trim()));
			}
		}
	}

	/**
	 * @param prefix
	 * @param result
	 * @param attribute
	 * @param fields
	 */
	protected void populateAttribute(final String prefix, final EObject target, final EAttribute attribute, final Map<String, String> fields) {
		final String attributeName = prefix + attribute.getName().toLowerCase();
		if (fields.containsKey(attributeName)) {
			final String value = fields.get(attributeName);
			final EDataType dataType = attribute.getEAttributeType();
			// TODO better parsing here, especially for date values.
			// TODO local dates are especially tricky, maybe do a second pass to
			// fix them.
			Object obj;
			try {

				if (attribute.isMany() && value != null) {
					final List<Object> list = new LinkedList<Object>();
					final String[] values = value.split(",");
					for (final String value2 : values) {
						final String trim = value2.trim();
						if (!trim.isEmpty()) {
							list.add(stringToAttributeValue(attributeName, trim, dataType));
						}
					}
					obj = list;
				} else {
					obj = stringToAttributeValue(attributeName, value, dataType);
				}
				if (!attribute.isUnsettable() || (obj != null)) {
					target.eSet(attribute, obj);
				} else {
					target.eUnset(attribute);
				}
			} catch (final Exception ex) {
				if (!(attribute.isUnsettable() && value.isEmpty())) {
					warn("Error parsing value \"" + value + "\" - " + ex.getMessage(), true, attributeName);
				}
			}
		} else {
			// warn that the column is missing; ignore line number
			// because it will be omitted on all lines
			final String attributeNameCased = prefix + attribute.getName();
			warn("Column " + attributeNameCased + " omitted", false, attributeNameCased);
		}
	}

	public Object stringToAttributeValue(final String attributeName, final String value, final EDataType dataType) throws ParseException {
		Object obj;
		if (dataType.equals(EcorePackage.eINSTANCE.getEDate())) {
			obj = DateTimeParser.getInstance().parseDate(value);
		} else if (dataType.equals(ScenarioPackage.eINSTANCE.getDateAndOptionalTime())) {
			obj = DateTimeParser.getInstance().parseDateAndOptionalTime(value);
		} else if (dataType.equals(ScenarioPackage.eINSTANCE.getPercentage())) {
			obj = dataType.getEPackage().getEFactoryInstance().createFromString(dataType, value);
			double d = (Double) obj;
			d /= 100.0;
			if (d < 0) {
				d = 0d;
				warn("Percentage value " + value + " is negative. It has been clamped to zero.", true, attributeName);
			} else if (d > 1) {
				d = 1d;
				warn("Percentage value " + value + " is more than 100%. It has been clamped to 100%.", true, attributeName);
			}
			obj = d;
		} else {
			obj = dataType.getEPackage().getEFactoryInstance().createFromString(dataType, value);
		}
		return obj;
	}
}

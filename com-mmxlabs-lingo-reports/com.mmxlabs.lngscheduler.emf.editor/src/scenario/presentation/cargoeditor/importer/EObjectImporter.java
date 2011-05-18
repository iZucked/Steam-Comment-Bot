/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.importer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;

/**
 * Gadget for importing EObjects and their singly contained sub-objects. If you
 * want to override the default import / export behaviour, you can subclass
 * this.
 * 
 * @author Tom Hinton
 * 
 */
public class EObjectImporter {
	private static final String SEPARATOR = ".";
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
					if (outputEClass.isSuperTypeOf(possibleSubClass)
							&& possibleSubClass.isAbstract() == false
							&& possibleSubClass.getName().equalsIgnoreCase(
									fields.get("kind"))) {
						return (EClass) classifier;
					}
				}
			}
		}
		return outputEClass;
	}

	public EObject importObject(final Map<String, String> fields,
			final Collection<DeferredReference> deferredReferences,
			final NamedObjectRegistry registry) {
		final EPackage ePackage = outputEClass.getEPackage();
		final EFactory eFactory = ePackage.getEFactoryInstance();

		final EClass trueOutputClass = getTrueOutputClass(fields);
		final EObject result = eFactory.create(trueOutputClass);

		for (final EAttribute attribute : trueOutputClass.getEAllAttributes()) {
			populateAttribute(result, attribute, fields);
		}

		for (final EReference reference : trueOutputClass.getEAllReferences()) {
			if (reference.isContainment()) {
				populateContainment(result, reference, fields,
						deferredReferences, registry);
			} else {
				populateReference(result, reference, fields, deferredReferences);
			}
		}
		
		return result;
	}

	/**
	 * @param result
	 * @param reference
	 * @param fields
	 * @param deferredReferences
	 * @param registry
	 */
	protected void populateContainment(final EObject result,
			final EReference reference, final Map<String, String> fields,
			final Collection<DeferredReference> deferredReferences,
			final NamedObjectRegistry registry) {
		if (reference.isMany()) {
			// import multiple; default behaviour is to run another session
			final String referenceName = reference.getName().toLowerCase();
			if (fields.containsKey(referenceName)) {
				final String filePath = fields.get(referenceName);
				final ImportSession session = new ImportSession();
				session.setDeferredReferences(deferredReferences);
				session.setInputFileName(filePath);
				session.setOutputObjectClass(reference.getEReferenceType());
				session.setNamedObjectRegistry(registry);
				session.run();
				((EList<EObject>) result.eGet(reference)).addAll(session
						.getImportedObjects());
			}
		} else {
			// get sub-fields
			final Map<String, String> subFields = new HashMap<String, String>();
			final String referencePrefix = reference.getName().toLowerCase()
					+ SEPARATOR;
			for (final Map.Entry<String, String> column : fields.entrySet()) {
				if (column.getKey().startsWith(referencePrefix)) {
					subFields
							.put(column.getKey().substring(
									referencePrefix.length()),
									column.getValue());
				}
			}
			// perform import
			final EObjectImporter importer = EObjectImporterFactory
					.getInstance().getImporter(reference.getEReferenceType());
			final EObject value = importer.importObject(subFields,
					deferredReferences, registry);
			result.eSet(reference, value);
		}
	}

	/**
	 * @param result
	 * @param reference
	 * @param fields
	 * @param deferredReferences
	 */
	protected void populateReference(final EObject target,
			final EReference reference, final Map<String, String> fields,
			final Collection<DeferredReference> deferredReferences) {

		final String referenceName = reference.getName().toLowerCase();
		if (fields.containsKey(referenceName)) {
			final String value = fields.get(referenceName);

			if (reference.isMany()) {
				final String[] values = value.split(",");
				for (final String value2 : values) {
					deferredReferences.add(new DeferredReference(target,
							reference, value2.trim()));
				}
			} else {
				deferredReferences.add(new DeferredReference(target, reference,
						value.trim()));
			}
		}
	}

	/**
	 * @param result
	 * @param attribute
	 * @param fields
	 */
	protected void populateAttribute(final EObject target,
			final EAttribute attribute, final Map<String, String> fields) {
		final String attributeName = attribute.getName().toLowerCase();
		if (fields.containsKey(attributeName)) {
			final String value = fields.get(attributeName);
			final EDataType dataType = attribute.getEAttributeType();
			// TODO better parsing here, especially for date values.
			// TODO local dates are especially tricky, maybe do a second pass to
			// fix them.
			final Object obj;
			if (dataType.equals(EcorePackage.eINSTANCE.getEDate())) {
				obj = DateTimeParser.getInstance().parseDate(value);
			} else {
				obj = dataType.getEPackage().getEFactoryInstance()
						.createFromString(dataType, value);
			}
			target.eSet(attribute, obj);
		}
	}
}

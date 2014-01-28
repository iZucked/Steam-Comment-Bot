package com.mmxlabs.models.lng.actuals.importer;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.util.importer.IExportContext;
import com.mmxlabs.models.util.importer.impl.MultiLineImporter;

public class ActualsModelImporter extends MultiLineImporter {
	
	/**
	 * Returns the first multi-line reference in the class.
	 * 
	 * Note: this is assumed to be the one which is iterated over when the class
	 * is exported. 
	 * 
	 * @param eClass
	 * @return
	 */
	private EReference getMultipleSubReference(final EClass eClass) {
		for (EReference ref: eClass.getEAllReferences()) {
			if (ref.isMany() && ref != MMXCorePackage.Literals.MMX_OBJECT__EXTENSIONS) {
				return ref;
			}
		}
		return null;
	}	
	
	@Override
	public String getIndexField(final EObject parent, final EClass eClass) {
		if (eClass == ActualsPackage.Literals.CARGO_ACTUALS) {
			return "cargo";
		}

		if (eClass == ActualsPackage.Literals.SLOT_ACTUALS) {
			return "slot";
		}

		return null;
	}

	/**
	 * Convenience method to add all elements of one map to another map, prepending them
	 * with a string prefix.
	 * 
	 * @param source
	 * @param target
	 * @param prefix
	 */
	protected <V> void addAllWithPrefix(final Map<String, V> source, final Map<String, V> target, final String prefix) {
		for (final Map.Entry<String, V> e : source.entrySet()) {
			target.put(prefix + e.getKey(), e.getValue());
		}		
	}
	
	@Override
	/*
	 * This method is identical to DefaultClassImporter#exportObjects except for the use of multiple-line
	 * return results while iterating over each object.
	 * 
	 * (non-Javadoc)
	 * @see com.mmxlabs.models.util.importer.impl.DefaultClassImporter#exportObjects(java.util.Collection, com.mmxlabs.models.mmxcore.MMXRootObject)
	 */
	public Collection<Map<String, String>> exportObjects(final Collection<? extends EObject> objects, final IExportContext context) {
		final LinkedList<Map<String, String>> result = new LinkedList<Map<String, String>>();

		if (objects.isEmpty()) {
			return result;
		}

		for (final EObject object : objects) {
			final Collection<Map<String, String>> subResult = multiExportObject(object, context);
			for (Map<String, String> flattened: subResult) {
				flattened.put(KIND_KEY, object.eClass().getName());
				result.add(flattened);
			}
		}
		return result;
	}

	/**
	 * Returns a list of CSV rows for export based on the hierarchical contents of a single object
	 * (the object may contain multi-reference fields which are exported in the same file). 
	 */
	@Override
	protected List<Map<String, String>> multiExportObject(final EObject object, final IExportContext context) {
		final LinkedList<Map<String, String>> result = new LinkedList<Map<String, String>>();
		EReference multiSubReference = getMultipleSubReference(object.eClass());
				
		if (multiSubReference != null) {
			// get the default export info for the object (excluding multi-references)
			Map<String, String> row = exportObject(object, context);
			
			// add the multi-reference content to the list of results
			for (final EObject line: (List<EObject>) object.eGet(multiSubReference)) {
				List<Map<String, String>> subRows = multiExportObject(line, context);
				for (Map<String, String> subRow: subRows) {
					LinkedHashMap<String, String> newRow = new LinkedHashMap<String, String>(row);
					addAllWithPrefix(subRow, newRow, multiSubReference.getName() + DOT);
					result.add(newRow);
				}
			}
			
		}		
		else {
			result.add(exportObject(object, context));
		}
		
		return result;
	}

	@Override
	/*
	 * This method is overridden to inhibit the default export handling of multiple
	 * sub-references (they are treated specially in the multiExportObject method of 
	 * this class).
	 *  
	 * (non-Javadoc)
	 * @see com.mmxlabs.models.util.importer.impl.DefaultClassImporter#shouldExportFeature(org.eclipse.emf.ecore.EStructuralFeature)
	 */
	protected boolean shouldExportFeature(final EStructuralFeature feature) {
		// inhibit default export of multiple sub-references
		if (getMultipleSubReference(feature.getEContainingClass()) == feature) {
			return false;
		}

		return super.shouldExportFeature(feature);
	}

}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.importer;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import scenario.port.Canal;
import scenario.port.PortPackage;

/**
 * Canal cost importer. Uses linked files for distance model, I'm afraid.
 * 
 * @author Tom Hinton
 * 
 */
public class CanalImporter extends EObjectImporter {
	

	@Override
	protected void populateContainment(final String prefix,
			final EObject result, final EReference reference,
			final Map<String, String> fields,
			final Collection<DeferredReference> deferredReferences,
			final NamedObjectRegistry registry) {
		if (reference == PortPackage.eINSTANCE.getCanal_DistanceModel()) {
			// special canal processing
			final String referenceName = prefix
					+ reference.getName().toLowerCase();

			if (fields.containsKey(referenceName)) {
				CSVReader reader;
				try {
					reader = getCurrentReader().getAdjacentReader(fields.get(referenceName));
					final EObjectImporter importer = EObjectImporterFactory
							.getInstance().getImporter(
									reference.getEReferenceType());
					final Collection<EObject> matrix = importer.importObjects(
							reader, deferredReferences, registry);
					result.eSet(reference, matrix.iterator().next());
				} catch (IOException e) {
					System.err.println("Unable to import " + referenceName
							+ " from " + fields.get(referenceName) + ": "
							+ e.getMessage());
				}
			}
		} else {
			super.populateContainment(prefix, result, reference, fields,
					deferredReferences, registry);
		}
	}

	@Override
	protected void flattenSingleContainment(EObject object, String prefix,
			Map<String, String> output, EReference reference) {
		if (reference == PortPackage.eINSTANCE.getCanal_DistanceModel()) {
			final EObjectImporter distanceImporter = EObjectImporterFactory
					.getInstance().getImporter(
							PortPackage.eINSTANCE.getDistanceModel());

			Map<String, Collection<Map<String, String>>> export = distanceImporter
					.exportObjects(Collections.singleton((EObject) object
							.eGet(reference)));

			Collection<Map<String, String>> extraFile = addExportFile(((Canal) object)
					.getName() + "-distances");
			extraFile.addAll(export.values().iterator().next());
			output.put(prefix + reference.getName(),
					((Canal) object).getName() + "-distances.csv");
		} else {
			super.flattenSingleContainment(object, prefix, output, reference);
		}
	}
}

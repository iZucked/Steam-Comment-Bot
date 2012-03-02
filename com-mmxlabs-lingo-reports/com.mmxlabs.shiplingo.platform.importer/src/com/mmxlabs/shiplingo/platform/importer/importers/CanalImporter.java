/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.importer.importers;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scenario.port.Canal;
import scenario.port.PortPackage;

/**
 * Canal cost importer. Uses linked files for distance model, I'm afraid.
 * 
 * @author Tom Hinton
 * 
 */
public class CanalImporter extends EObjectImporter {
	private final Logger log = LoggerFactory.getLogger(CanalImporter.class);

	@Override
	protected void populateContainment(final String prefix, final EObject result, final EReference reference, final Map<String, String> fields, final Collection<DeferredReference> deferredReferences,
			final NamedObjectRegistry registry) {
		if (reference == PortPackage.eINSTANCE.getCanal_DistanceModel()) {
			// special canal processing
			final String referenceName = prefix + reference.getName().toLowerCase();

			if (fields.containsKey(referenceName)) {
				CSVReader reader = null;
				try {
					reader = getCurrentReader().getAdjacentReader(fields.get(referenceName));
					final EObjectImporter importer = EObjectImporterFactory.getInstance().getImporter(reference.getEReferenceType());
					final Collection<EObject> matrix = importer.importObjects(reader, deferredReferences, registry);
					result.eSet(reference, matrix.iterator().next());
				} catch (final IOException e) {
					log.error("Unable to import " + referenceName + " from " + fields.get(referenceName) + ": " + e.getMessage(), e);
					super.warn("Unable to import " + referenceName + " from " + fields.get(referenceName) + ": " + e.getMessage(), true, referenceName);
				} finally {
					if (reader != null) {
						try {
							reader.close();
						} catch (IOException e) {
							log.error(e.getMessage(), e);
						}
					}
				}
			}
		} else {
			super.populateContainment(prefix, result, reference, fields, deferredReferences, registry);
		}
	}

	@Override
	protected void flattenSingleContainment(final EObject object, final String prefix, final Map<String, String> output, final EReference reference) {
		if (reference == PortPackage.eINSTANCE.getCanal_DistanceModel()) {
			final EObjectImporter distanceImporter = EObjectImporterFactory.getInstance().getImporter(PortPackage.eINSTANCE.getDistanceModel());

			final Map<String, Collection<Map<String, String>>> export = distanceImporter.exportObjects(Collections.singleton((EObject) object.eGet(reference)));

			final Collection<Map<String, String>> extraFile = addExportFile(((Canal) object).getName() + "-distances");
			extraFile.addAll(export.values().iterator().next());
			output.put(prefix + reference.getName(), ((Canal) object).getName() + "-distances.csv");
		} else {
			super.flattenSingleContainment(object, prefix, output, reference);
		}
	}
}

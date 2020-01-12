/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.importer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.common.csv.FieldMap;
import com.mmxlabs.common.csv.IFieldMap;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;
import com.mmxlabs.models.util.importer.registry.IImporterRegistry;

/**
 * Vessel importer; adds support for canal time fields.
 * 
 * @author hinton
 * 
 */
public class VesselImporter extends DefaultClassImporter {

	@Inject
	private IImporterRegistry importerRegistry;

	private IClassImporter parameterImporter;

	/**
	 */
	public VesselImporter() {
		final Activator activator = Activator.getDefault();
		if (activator != null) {

			importerRegistry = activator.getImporterRegistry();
			registryInit();
		}
	}

	@Inject
	private void registryInit() {
		if (importerRegistry != null) {
			parameterImporter = importerRegistry.getClassImporter(FleetPackage.eINSTANCE.getVesselClassRouteParameters());
		}
	}

	@Override
	public ImportResults importObject(final EObject parent, final EClass eClass, final Map<String, String> row, final IMMXImportContext context) {
		final ImportResults result = super.importObject(parent, eClass, row, context);
		final Vessel vessel = (Vessel) result.importedObject;
		final HashSet<String> parameterisedCanals = new HashSet<>();
		for (final String key : row.keySet()) {
			final String[] parts = key.split("\\.");
			if (parts.length > 1) {
				if (parts[1].equals("parameters")) {
					final String original = context.peekReader().getCasedColumnName(key);
					final String canalName = original.split("\\.")[0];

					if (parameterisedCanals.contains(canalName)) {
						continue;
					}
					parameterisedCanals.add(canalName);

					if (parameterImporter != null) {
						final IFieldMap rowMap;
						if (row instanceof IFieldMap) {
							rowMap = (IFieldMap) row;
						} else {
							rowMap = new FieldMap(row);
						}
						final IFieldMap subMap = rowMap.getSubMap(parts[0] + "." + parts[1] + ".");

						if (canalName.toLowerCase().contains("suez")) {
							subMap.put("routeoption", "SUEZ");
						} else if (canalName.toLowerCase().contains("panama")) {
							subMap.put("routeoption", "PANAMA");
						} else {
							subMap.put("routeoption", canalName);
						}

						final VesselClassRouteParameters parameters = (VesselClassRouteParameters) parameterImporter.importObject(parent, FleetPackage.eINSTANCE.getVesselClassRouteParameters(),
								subMap, context).importedObject;
						if (parameters.getRouteOption() != null) {
							vessel.getVesselOrDelegateRouteParameters().add(parameters);
						}
					}
				}
			}
		}

		return result;
	}

	@Override
	protected Map<String, String> exportObject(final EObject object, final IMMXExportContext context) {
		final Vessel vessel = (Vessel) object;
		final Map<String, String> result = super.exportObject(object, context);

		for (final VesselClassRouteParameters routeParameters : vessel.getVesselOrDelegateRouteParameters()) {
			final Map<String, String> exportedParameters = parameterImporter.exportObjects(Collections.singleton(routeParameters), context).iterator().next();
			final String route = exportedParameters.get("routeOption");
			exportedParameters.remove("routeOption");
			final String prefix = route + ".parameters.";
			for (final Map.Entry<String, String> e : exportedParameters.entrySet()) {
				result.put(prefix + e.getKey(), e.getValue());
			}
		}

		return result;
	}
}

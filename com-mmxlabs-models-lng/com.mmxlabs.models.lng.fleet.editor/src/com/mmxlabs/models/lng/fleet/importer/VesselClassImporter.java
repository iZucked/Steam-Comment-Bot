/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.importer;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.FieldMap;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IFieldMap;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.IImportContext.IDeferment;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;
import com.mmxlabs.models.util.importer.registry.IImporterRegistry;

/**
 * Vessel class importer; adds support for canal time fields.
 * 
 * @author hinton
 * @since 2.0
 * 
 */
public class VesselClassImporter extends DefaultClassImporter {

	@Inject
	private IImporterRegistry importerRegistry;

	private IClassImporter routeCostImporter;
	private IClassImporter parameterImporter;

	/**
	 * @since 2.0
	 */
	public VesselClassImporter() {
		final Activator activator = Activator.getDefault();
		if (activator != null) {

			importerRegistry = activator.getImporterRegistry();
			registryInit();
		}
	}

	@Inject
	private void registryInit() {
		if (importerRegistry != null) {
			routeCostImporter = importerRegistry.getClassImporter(PricingPackage.eINSTANCE.getRouteCost());
			parameterImporter = importerRegistry.getClassImporter(FleetPackage.eINSTANCE.getVesselClassRouteParameters());
		}
	}

	@Override
	public Collection<EObject> importObject(EClass eClass, Map<String, String> row, IImportContext context) {
		final Collection<EObject> result = super.importObject(eClass, row, context);
		final VesselClass vc = (VesselClass) result.iterator().next();
		final HashSet<String> pricedCanals = new HashSet<String>();
		final HashSet<String> parameterisedCanals = new HashSet<String>();
		for (final String key : row.keySet()) {
			final String[] parts = key.split("\\.");
			if (parts.length > 1) {
				if (parts[1].equals("pricing")) {
					final String original = context.peekReader().getCasedColumnName(key);
					final String canalName = original.split("\\.")[0];
					if (pricedCanals.contains(canalName)) {
						continue;
					}
					pricedCanals.add(canalName);
					if (routeCostImporter != null) {
						final IFieldMap rowMap;
						if (row instanceof IFieldMap) {
							rowMap = (IFieldMap) row;
						} else {
							rowMap = new FieldMap(row);
						}
						final IFieldMap subMap = rowMap.getSubMap(parts[0] + "." + parts[1] + ".");

						subMap.put("route", canalName);
						if (row.containsKey("name"))
							subMap.put("vesselclass", row.get("name"));
						final RouteCost cost = (RouteCost) routeCostImporter.importObject(PricingPackage.eINSTANCE.getRouteCost(), subMap, context).iterator().next();

						context.doLater(new IDeferment() {
							@Override
							public void run(final IImportContext context) {
								if (cost.getRoute() != null && cost.getVesselClass() != null) {
									context.getRootObject().getSubModel(PricingModel.class).getRouteCosts().add(cost);
								}
							}

							@Override
							public int getStage() {
								return IImportContext.STAGE_REFERENCES_RESOLVED;
							}
						});
					}
				} else if (parts[1].equals("parameters")) {
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

						subMap.put("route", canalName);

						final VesselClassRouteParameters parameters = (VesselClassRouteParameters) parameterImporter
								.importObject(FleetPackage.eINSTANCE.getVesselClassRouteParameters(), subMap, context).iterator().next();

						context.doLater(new IDeferment() {
							@Override
							public void run(IImportContext context) {
								if (parameters.getRoute() != null) {
									vc.getRouteParameters().add(parameters);
								}
							}

							@Override
							public int getStage() {
								return IImportContext.STAGE_REFERENCES_RESOLVED;
							}
						});
					}
				}
			}
		}

		return result;
	}

	@Override
	protected Map<String, String> exportObject(EObject object, final MMXRootObject root) {
		final VesselClass vc = (VesselClass) object;
		final Map<String, String> result = super.exportObject(object, root);

		for (final VesselClassRouteParameters routeParameters : vc.getRouteParameters()) {
			final Map<String, String> exportedParameters = parameterImporter.exportObjects(Collections.singleton(routeParameters), root).iterator().next();
			final String route = exportedParameters.get("route");
			exportedParameters.remove("route");
			final String prefix = route + ".parameters.";
			for (final Map.Entry<String, String> e : exportedParameters.entrySet()) {
				result.put(prefix + e.getKey(), e.getValue());
			}
		}

		final PricingModel pm = root.getSubModel(PricingModel.class);
		if (pm != null) {
			for (final RouteCost rc : pm.getRouteCosts()) {
				if (rc.getVesselClass() == vc) {
					final Map<String, String> exportedCost = routeCostImporter.exportObjects(Collections.singleton(rc), root).iterator().next();
					exportedCost.remove("vesselclass");
					final String route = exportedCost.get("route");
					exportedCost.remove("route");
					final String prefix = route + ".pricing.";
					for (final Map.Entry<String, String> e : exportedCost.entrySet()) {
						result.put(prefix + e.getKey(), e.getValue());
					}
				}
			}
		}

		return result;
	}
}

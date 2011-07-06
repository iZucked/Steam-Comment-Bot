/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.importer;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import scenario.port.Canal;
import scenario.port.PortFactory;
import scenario.port.PortPackage;
import scenario.port.VesselClassCost;

/**
 * Canal cost importer. Uses linked files for distance model, I'm afraid.
 * 
 * @author Tom Hinton
 * 
 */
public class CanalImporter extends EObjectImporter {
	/**
	 * 
	 */
	private static final String TRANSIT_FUEL = "transitFuel";
	/**
	 * 
	 */
	private static final String TRANSIT_TIME = "transitTime";
	/**
	 * 
	 */
	private static final String UNLADEN_COST = "unladenCost";
	/**
	 * 
	 */
	private static final String LADEN_COST = "ladenCost";

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

		} else if (reference == PortPackage.eINSTANCE.getCanal_ClassCosts()) {
			// class costs are populated from columns like "DFDE-117.xxx
			final Map<String, VesselClassCost> costs = new HashMap<String, VesselClassCost>();
			for (final Map.Entry<String, String> entry : fields.entrySet()) {
				final String[] parts = entry.getKey().split(SEPARATOR);
				if (parts.length == 2) {
					if (parts[1].equalsIgnoreCase(LADEN_COST)) {
						final VesselClassCost vcc = getClassCost(costs,
								parts[1]);
						vcc.setLadenCost(Integer.parseInt(entry.getValue()));
					} else if (parts[1].equalsIgnoreCase(UNLADEN_COST)) {
						final VesselClassCost vcc = getClassCost(costs,
								parts[1]);
						vcc.setUnladenCost(Integer.parseInt(entry.getValue()));
					} else if (parts[1].equalsIgnoreCase(TRANSIT_TIME)) {
						final VesselClassCost vcc = getClassCost(costs,
								parts[1]);
						vcc.setTransitTime(Integer.parseInt(entry.getValue()));
					} else if (parts[1].equalsIgnoreCase(TRANSIT_FUEL)) {
						final VesselClassCost vcc = getClassCost(costs,
								parts[1]);
						vcc.setTransitFuel(Float.parseFloat(entry.getValue()));
					}
				}
			}
			final EList<EObject> container = (EList<EObject>) result
					.eGet(reference);
			for (final Map.Entry<String, VesselClassCost> entry : costs
					.entrySet()) {
				container.add(entry.getValue());
				deferredReferences.add(new DeferredReference(entry.getValue(),
						PortPackage.eINSTANCE.getVesselClassCost_VesselClass(),
						entry.getKey()));
			}
		} else {
			super.populateContainment(prefix, result, reference, fields,
					deferredReferences, registry);
		}
	}

	@Override
	protected void flattenMultiContainment(final EObject object,
			final String prefix, final EReference reference,
			Map<String, String> output) {
		if (reference == PortPackage.eINSTANCE.getCanal_ClassCosts()) {
			for (final VesselClassCost vcc : ((Canal) object).getClassCosts()) {
				final String s = vcc.getVesselClass().getName() + SEPARATOR;
				output.put(s + LADEN_COST, vcc.getLadenCost() + "");
				output.put(s + UNLADEN_COST, vcc.getUnladenCost() + "");
				output.put(s + TRANSIT_TIME, vcc.getTransitTime() + "");
				output.put(s + TRANSIT_FUEL,
						String.format("%3g", vcc.getTransitFuel()));
			}
		} else {
			super.flattenMultiContainment(object, prefix, reference, output);
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

	/**
	 * Get a vessel class cost for the given vessel class name, creating one if
	 * it's not already in the map.
	 * 
	 * @param costs
	 * @param className
	 * @return
	 */
	private static VesselClassCost getClassCost(Map<String, VesselClassCost> costs,
			String className) {
		if (costs.containsKey(className)) {
			return costs.get(className);
		} else {
			final VesselClassCost vcc = PortFactory.eINSTANCE
					.createVesselClassCost();
			costs.put(className, vcc);
			return vcc;
		}
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.importer;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import scenario.fleet.FleetPackage;
import scenario.fleet.FuelConsumptionLine;
import scenario.fleet.VesselClass;
import scenario.fleet.VesselClassCost;
import scenario.fleet.VesselState;
import scenario.fleet.VesselStateAttributes;

/**
 * A special-case for vessel class which handles the export behaviour.
 * 
 * @author Tom Hinton
 * 
 */
public class VesselClassImporter extends EObjectImporter {

	/**
	 * Used in canal column headers, like
	 * 
	 * canal.Suez.ladenCost
	 */
	private static final String CANAL = "canal";

	@Override
	public Map<String, Collection<Map<String, String>>> exportObjects(
			Collection<? extends EObject> objects) {
		final Map<String, Collection<Map<String, String>>> result = super
				.exportObjects(objects);

		final LinkedList<Map<String, String>> lines = new LinkedList<Map<String, String>>();
		// process fuel consumption curves
		for (final EObject o : objects) {
			if (o instanceof VesselClass) {
				final VesselClass vc = (VesselClass) o;
				vc.getBallastAttributes().setVesselState(VesselState.BALLAST);
				vc.getLadenAttributes().setVesselState(VesselState.LADEN);
				
				lines.add(convertLines(vc.getName(), vc.getBallastAttributes()));
				lines.add(convertLines(vc.getName(), vc.getLadenAttributes()));
			}
		}

		result.put("Fuel Curve", lines);

		return result;
	}

	/**
	 * @param vcName
	 * @param ballastAttributes
	 * @return
	 */
	private Map<String, String> convertLines(String vcName,
			VesselStateAttributes attributes) {
		final LinkedHashMap<String, String> out = new LinkedHashMap<String, String>();

		out.put("class", vcName);
		out.put("state", attributes.getVesselState().getName());

		for (final FuelConsumptionLine line : attributes
				.getFuelConsumptionCurve()) {
			out.put(line.getSpeed() + "", line.getConsumption() + "");
		}

		return out;
	}

	@Override
	protected void flattenMultiContainment(EObject object, String prefix,
			EReference reference, Map<String, String> output) {
		if (reference == FleetPackage.eINSTANCE.getVesselClass_CanalCosts()) {
			final EObjectImporter importer = EObjectImporterFactory
					.getInstance().getImporter(
							FleetPackage.eINSTANCE.getVesselClassCost());
			
			for (final VesselClassCost vcc : ((VesselClass) object)
					.getCanalCosts()) {
				final Map<String, String> map = importer.exportObject((EObject) vcc);
				final String p2 = prefix + CANAL + SEPARATOR + vcc.getCanal().getName() + SEPARATOR;
				for (final Map.Entry<String, String> e : map.entrySet()) {
					if (e.getKey().equalsIgnoreCase(CANAL)) continue;
					output.put(p2 + e.getKey(), e.getValue());
				}
			}
		} else {
			super.flattenMultiContainment(object, prefix, reference, output);
		}
	}

	@Override
	protected void populateContainment(String prefix, EObject result,
			EReference reference, Map<String, String> fields,
			Collection<DeferredReference> deferredReferences,
			NamedObjectRegistry registry) {
		if (reference == FleetPackage.eINSTANCE.getVesselClass_CanalCosts()) {
			final Map<String, Map<String, String>> canalCosts = new HashMap<String, Map<String, String>>();
			for (final String fieldName : fields.keySet()) {
				if (fieldName.startsWith(prefix + CANAL)) {
					final String fn2 = getCurrentReader().getCasedColumnName(
							fieldName).substring(
							(prefix + CANAL).length());
					final String canalName = fn2.substring(0,
							fn2.indexOf(SEPARATOR));
					final String rest = fn2
							.substring(fn2.indexOf(SEPARATOR) + 1);
					Map<String, String> vals = canalCosts.get(canalName);
					if (vals == null) {
						vals = new HashMap<String, String>();
						canalCosts.put(canalName, vals);
						vals.put(CANAL, canalName);
					}
					vals.put(rest.toLowerCase(), fields.get(fieldName));
				}
			}

			final EObjectImporter importer = EObjectImporterFactory
					.getInstance().getImporter(
							FleetPackage.eINSTANCE.getVesselClassCost());

			final VesselClass vc = (VesselClass) result;

			for (final Map<String, String> canalCost : canalCosts.values()) {
				vc.getCanalCosts().add(
						(VesselClassCost) importer.importObject(canalCost,
								deferredReferences, registry));
			}
		} else {
			super.populateContainment(prefix, result, reference, fields,
					deferredReferences, registry);
			// force vessel state to match up - why is there even a vessel state field, given that it's implied?
			if (reference == FleetPackage.eINSTANCE.getVesselClass_LadenAttributes() &&
					result.eGet(reference)!=null
					) {
				((VesselStateAttributes)result.eGet(reference)).setVesselState(VesselState.LADEN);
			}
			if (reference == FleetPackage.eINSTANCE.getVesselClass_BallastAttributes() &&
					result.eGet(reference)!=null
					) {
				((VesselStateAttributes)result.eGet(reference)).setVesselState(VesselState.BALLAST);
			}
		}
	}
	
	

}

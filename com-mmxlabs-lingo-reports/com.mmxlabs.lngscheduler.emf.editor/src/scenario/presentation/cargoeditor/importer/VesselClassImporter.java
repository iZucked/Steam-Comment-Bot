/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.importer;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;

import scenario.fleet.FuelConsumptionLine;
import scenario.fleet.VesselClass;
import scenario.fleet.VesselStateAttributes;

/**
 * A special-case for vessel class which handles the export behaviour.
 * 
 * @author Tom Hinton
 * 
 */
public class VesselClassImporter extends EObjectImporter {
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
}

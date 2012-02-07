/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.importer.importers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import scenario.fleet.FleetFactory;
import scenario.fleet.FleetPackage;
import scenario.fleet.FuelConsumptionLine;
import scenario.fleet.VesselClass;
import scenario.fleet.VesselStateAttributes;

import com.mmxlabs.common.Pair;

/**
 * This is a hack; it generates a bunch of fuel consumption lines and adds them where they need to go by direct lookups
 * 
 * @author Tom Hinton
 * 
 */
public class FuelConsumptionLineImporter extends EObjectImporter {
	@Override
	public EObject importObject(final Map<String, String> fields, final Collection<DeferredReference> deferredReferences, final NamedObjectRegistry registry) {
		final String className = fields.get("class");
		final String stateName = fields.get("state");

		final ArrayList<FuelConsumptionLine> lines = new ArrayList<FuelConsumptionLine>(fields.size() - 2);
		final FleetFactory factory = FleetFactory.eINSTANCE;

		fields.remove("class");
		fields.remove("state");

		for (final Map.Entry<String, String> column : fields.entrySet()) {
			try {
				final float speed = Float.parseFloat(column.getKey());
				final float consumption = Float.parseFloat(column.getValue());
				final FuelConsumptionLine fcl = factory.createFuelConsumptionLine();
				fcl.setSpeed(speed);
				fcl.setConsumption(consumption);
				lines.add(fcl);
			} catch (final NumberFormatException nfe) {

			}
		}

		Collections.sort(lines, new Comparator<FuelConsumptionLine>() {
			@Override
			public int compare(final FuelConsumptionLine o1, final FuelConsumptionLine o2) {
				return Float.compare(o1.getSpeed(), o2.getSpeed());
			}
		});

		final EReference ref;
		if (stateName.equalsIgnoreCase("laden")) {
			ref = FleetPackage.eINSTANCE.getVesselClass_LadenAttributes();
		} else {
			ref = FleetPackage.eINSTANCE.getVesselClass_BallastAttributes();
		}

		final VesselClass vc = (VesselClass) registry.getContents().get(new Pair<EClass, String>(FleetPackage.eINSTANCE.getVesselClass(), className));

		if (vc != null) {
			final VesselStateAttributes vsa = (VesselStateAttributes) vc.eGet(ref);
			vsa.getFuelConsumptionCurve().addAll(lines);
		}

		return null;
	}
}

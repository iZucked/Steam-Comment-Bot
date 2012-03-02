/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.importer.importers;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import scenario.cargo.Cargo;
import scenario.cargo.CargoPackage;
import scenario.cargo.LoadSlot;
import scenario.fleet.FleetPackage;
import scenario.fleet.VesselEvent;
import scenario.schedule.Schedule;
import scenario.schedule.ScheduleFactory;
import scenario.schedule.Sequence;
import scenario.schedule.events.EventsFactory;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.events.SlotVisit;
import scenario.schedule.events.VesselEventVisit;
import scenario.schedule.fleetallocation.AllocatedVessel;
import scenario.schedule.fleetallocation.FleetVessel;
import scenario.schedule.fleetallocation.FleetallocationFactory;
import scenario.schedule.fleetallocation.FleetallocationPackage;
import scenario.schedule.fleetallocation.SpotVessel;

import com.mmxlabs.common.Pair;

/**
 * An exporter which will transduce a simple reduced schedule model suitable for use as a start state but not much else.
 * 
 * Will only work when every pair of slotvisits matches a cargo, because we're trying to ignore slot IDs, and also presumes that the ID of a load / discharge slot match their cargo's ID.
 * 
 * The default exporter will of course export the full state, but in a less editable way.
 * 
 * @author Tom Hinton
 * 
 */
public class ReducedScheduleImporter extends EObjectImporter {
	@Override
	public Map<String, Collection<Map<String, String>>> exportObjects(final Collection<? extends EObject> objects) {
		final HashMap<String, Collection<Map<String, String>>> result = new HashMap<String, Collection<Map<String, String>>>();

		for (final EObject object : objects) {
			if (object instanceof Schedule) {
				final Schedule schedule = (Schedule) object;
				final Collection<Map<String, String>> answer = new LinkedList<Map<String, String>>();

				for (final Sequence seq : schedule.getSequences()) {
					final LinkedHashMap<String, String> output = new LinkedHashMap<String, String>();
					output.put("Vessel", seq.getVessel().getName());
					int seqExtent = 0;
					for (final ScheduledEvent event : seq.getEvents()) {
						if (event instanceof SlotVisit) {
							if (((SlotVisit) event).getSlot() instanceof LoadSlot) {
								seqExtent++;
								output.put("" + seqExtent, ((Cargo) ((SlotVisit) event).getSlot().eContainer()).getId());
							}
						}
					}
					answer.add(output);
				}
				result.put("Schedule-" + schedule.getName(), answer);
			}
		}

		return result;
	}

	@Override
	public Collection<EObject> importObjects(final CSVReader reader, final Collection<DeferredReference> deferredReferences, final NamedObjectRegistry registry) {
		final Schedule result = ScheduleFactory.eINSTANCE.createSchedule();
		final Pattern p = Pattern.compile("(.*) (\\d+)$");

		String[] line = null;
		try {
			while (null != (line = reader.readLine())) {

				final String vesselName = line[0];
				final Sequence answer = ScheduleFactory.eINSTANCE.createSequence();
				result.getSequences().add(answer);
				final Matcher m = p.matcher(vesselName);
				final AllocatedVessel av;
				if (m.matches()) {
					final int spotIndex = Integer.parseInt(m.group(2));
					final SpotVessel sv = FleetallocationFactory.eINSTANCE.createSpotVessel();
					av = sv;
					sv.setIndex(spotIndex);

					deferredReferences.add(new DeferredReference(sv, FleetallocationPackage.eINSTANCE.getSpotVessel_VesselClass(), m.group(1)));
				} else {
					final FleetVessel fv = FleetallocationFactory.eINSTANCE.createFleetVessel();
					av = fv;
					deferredReferences.add(new DeferredReference(av, FleetallocationPackage.eINSTANCE.getFleetVessel_Vessel(), vesselName));
				}

				result.getFleet().add(av);
				answer.setVessel(av);

				final EClass cargoClass = CargoPackage.eINSTANCE.getCargo();
				final EClass veClass = FleetPackage.eINSTANCE.getVesselEvent();

				final String[] fline = line.clone();

				deferredReferences.add(new DeferredReference() {
					@Override
					public void run() {
						for (int i = 1; i < fline.length; i++) {
							if (fline[i].isEmpty()) {
								continue;
							}

							final Cargo c = (Cargo) registry.get(new Pair<EClass, String>(cargoClass, fline[i]));
							if (c != null) {
								final SlotVisit loadVisit = EventsFactory.eINSTANCE.createSlotVisit();
								final SlotVisit dischargeVisit = EventsFactory.eINSTANCE.createSlotVisit();

								answer.getEvents().add(loadVisit);
								answer.getEvents().add(dischargeVisit);

								loadVisit.setSlot(c.getLoadSlot());
								dischargeVisit.setSlot(c.getDischargeSlot());

							} else {
								final VesselEvent ve = (VesselEvent) registry.get(new Pair<EClass, String>(veClass, fline[i]));
								if (ve != null) {
									final VesselEventVisit vev = EventsFactory.eINSTANCE.createVesselEventVisit();
									vev.setVesselEvent(ve);
									answer.getEvents().add(vev);
								} else {
									warn("Cannot find a cargo or event named " + fline[i] + " for vessel " + av.getName(), true, "");
								}
							}
						}
					}
				});

			}
		} catch (final IOException e) {

		}

		result.setName(reader.getFileName().replace(".csv", ""));

		return Collections.singleton((EObject) result);
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.importer;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.emf.ecore.EObject;

import scenario.cargo.Cargo;
import scenario.cargo.LoadSlot;
import scenario.schedule.Schedule;
import scenario.schedule.ScheduleFactory;
import scenario.schedule.Sequence;
import scenario.schedule.events.EventsFactory;
import scenario.schedule.events.EventsPackage;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.events.SlotVisit;
import scenario.schedule.fleetallocation.AllocatedVessel;
import scenario.schedule.fleetallocation.FleetVessel;
import scenario.schedule.fleetallocation.FleetallocationFactory;
import scenario.schedule.fleetallocation.FleetallocationPackage;
import scenario.schedule.fleetallocation.SpotVessel;

/**
 * An exporter which will transduce a simple reduced schedule model suitable for
 * use as a start state but not much else.
 * 
 * Will only work when every pair of slotvisits matches a cargo, because we're
 * trying to ignore slot IDs, and also presumes that the ID of a load /
 * discharge slot match their cargo's ID.
 * 
 * The default exporter will of course export the full state, but in a less
 * editable way.
 * 
 * @author Tom Hinton
 * 
 */
public class ReducedScheduleImporter extends EObjectImporter {
	@Override
	public Map<String, Collection<Map<String, String>>> exportObjects(
			Collection<? extends EObject> objects) {
		final HashMap<String, Collection<Map<String, String>>> result = new HashMap<String, Collection<Map<String, String>>>();

		for (final EObject object : objects) {
			if (object instanceof Schedule) {
				final Schedule schedule = (Schedule) object;
				final Collection<Map<String, String>> answer = new LinkedList<Map<String, String>>();

				result.put("Schedule-" + schedule.getName(), answer);

				final LinkedHashMap<String, String> output = new LinkedHashMap<String, String>();
				for (final Sequence seq : schedule.getSequences()) {
					output.put("Vessel", seq.getVessel().getName());
					int seqExtent = 0;
					for (int i = 0; i < seq.getEvents().size(); i++) {
						if (seq.getEvents().size() < i) {
							final ScheduledEvent event = seq.getEvents().get(i);
							if (event instanceof SlotVisit) {
								if (((SlotVisit) event).getSlot() instanceof LoadSlot) {
									seqExtent++;
									output.put("" + seqExtent,
											((Cargo) ((SlotVisit) event)
													.getSlot().eContainer())
													.getId());
								}
							}
						}
					}
				}
				answer.add(output);
			}
		}

		return result;
	}

	@Override
	public Collection<EObject> importObjects(final CSVReader reader,
			final Collection<DeferredReference> deferredReferences,
			final NamedObjectRegistry registry) {
		final Schedule result = ScheduleFactory.eINSTANCE.createSchedule();
		final Pattern p = Pattern.compile("(.*) \\d+$");

		String[] line = null;
		try {
			while (null != (line = reader.readLine())) {
				final Sequence answer = ScheduleFactory.eINSTANCE
						.createSequence();
				final Matcher m = p.matcher(line[0]);
				final AllocatedVessel av;
				if (m.matches()) {
					final int spotIndex = Integer.parseInt(m.group());
					final SpotVessel sv = FleetallocationFactory.eINSTANCE
							.createSpotVessel();
					av = sv;
					sv.setIndex(spotIndex);

					deferredReferences.add(new DeferredReference(sv,
							FleetallocationPackage.eINSTANCE
									.getSpotVessel_VesselClass(), m.group(0)));
				} else {
					final FleetVessel fv = FleetallocationFactory.eINSTANCE
							.createFleetVessel();
					av = fv;
					deferredReferences.add(new DeferredReference(av,
							FleetallocationPackage.eINSTANCE
									.getFleetVessel_Vessel(), line[0]));
				}

				answer.setVessel(av);

				for (int i = 1; i < line.length; i++) {
					final SlotVisit loadVisit = EventsFactory.eINSTANCE
							.createSlotVisit();
					final SlotVisit dischargeVisit = EventsFactory.eINSTANCE
							.createSlotVisit();

					deferredReferences.add(new DeferredReference(loadVisit,
							EventsPackage.eINSTANCE.getSlotVisit_Slot(),
							"load-" + line[i]));
					
					deferredReferences.add(new DeferredReference(loadVisit,
							EventsPackage.eINSTANCE.getSlotVisit_Slot(),
							"discharge-" + line[i]));
				}
			}
		} catch (IOException e) {

		}

		return Collections.singleton((EObject) result);
	}
}

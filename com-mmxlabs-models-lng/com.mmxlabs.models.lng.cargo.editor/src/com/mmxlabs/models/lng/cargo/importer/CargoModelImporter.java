/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.importer;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.common.csv.IDeferment;
import com.mmxlabs.common.csv.IImportContext;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoGroup;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.VesselGroupCanalParameters;
import com.mmxlabs.models.lng.cargo.VesselType;
import com.mmxlabs.models.lng.cargo.VesselTypeGroup;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.ISubmodelImporter;
import com.mmxlabs.models.util.importer.registry.IImporterRegistry;

/**
 */
public class CargoModelImporter implements ISubmodelImporter {
	public static final String CARGO_KEY = "CARGO";
	public static final String CARGO_GROUP_KEY = "CARGO-GROUP";
	private CargoImporter cargoImporter;

	@Inject
	private IImporterRegistry importerRegistry;

	private IClassImporter cargoGroupImporter;

	private IClassImporter vesselAvailabilityImporter;
	private IClassImporter vesselEventImporter;
	private IClassImporter paperDealsImporter;
	private final CanalBookingImporter canalBookingsImporter = new CanalBookingImporter();
	public static final @NonNull String EVENTS_KEY = "EVENTS";
	public static final @NonNull String CANAL_BOOKINGS_KEY = "CANAL_BOOKINGS";
	public static final @NonNull String VESSEL_AVAILABILITY_KEY = "VESSELSAVAILABILITIES";
	public static final @NonNull String PAPER_DEALS_KEY = "PAPER_DEALS";

	/**
	 */
	public CargoModelImporter() {
		final Activator activator = Activator.getDefault();
		if (activator != null) {

			importerRegistry = activator.getImporterRegistry();
			registryInit();
		}
	}

	@Inject
	private void registryInit() {
		if (importerRegistry != null) {
			cargoGroupImporter = importerRegistry.getClassImporter(CargoPackage.eINSTANCE.getCargoGroup());
			cargoImporter = new CargoImporter();
			cargoImporter.setImporterRegistry(importerRegistry);
			vesselAvailabilityImporter = importerRegistry.getClassImporter(CargoPackage.eINSTANCE.getVesselAvailability());
			vesselEventImporter = importerRegistry.getClassImporter(CargoPackage.eINSTANCE.getVesselEvent());
			paperDealsImporter = importerRegistry.getClassImporter(CargoPackage.eINSTANCE.getPaperDeal());
		}
	}

	@Override
	public Map<String, String> getRequiredInputs() {
		final @NonNull Map<String, String> inputs = new HashMap<>();

		inputs.put(CARGO_KEY, "Cargoes");
		inputs.put(CARGO_GROUP_KEY, "Cargo Groups");
		inputs.put(VESSEL_AVAILABILITY_KEY, "Vessel Availability");
		inputs.put(EVENTS_KEY, "Events");
		inputs.put(CANAL_BOOKINGS_KEY, "Canal Bookings");
		if (LicenseFeatures.isPermitted("features:paperdeals")) {
			inputs.put(PAPER_DEALS_KEY, "Paper Deals");
		}

		return inputs;
	}

	@Override
	public UUIDObject importModel(final Map<String, CSVReader> inputs, final IMMXImportContext context) {
		final CargoModel cargoModel = CargoFactory.eINSTANCE.createCargoModel();

		// Create Special groups
		if (cargoModel != null) {
			for (final VesselType type : VesselType.values()) {
				boolean found = false;
				for (final VesselTypeGroup g : cargoModel.getVesselTypeGroups()) {
					if (g.getVesselType().equals(type)) {
						found = true;
						break;
					}
				}
				if (found == false) {
					final VesselTypeGroup g = CargoFactory.eINSTANCE.createVesselTypeGroup();
					g.setName("All " + type.getName().replaceAll("_", " ") + " Vessels");
					g.setVesselType(type);
					cargoModel.getVesselTypeGroups().add(g);
					context.registerNamedObject(g);
				}
			}
		}

		if (inputs.containsKey(CARGO_KEY)) {
			final CSVReader reader = inputs.get(CARGO_KEY);
			if (reader == null) {
				throw new IllegalStateException("No CSV Reader");
			}
			final Collection<EObject> values = cargoImporter.importObjects(CargoPackage.eINSTANCE.getCargo(), reader, context);
			for (final EObject object : values) {
				if (object instanceof Cargo) {
					cargoModel.getCargoes().add((Cargo) object);
				} else if (object instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) object;
					cargoModel.getLoadSlots().add(loadSlot);
					// Set default pricing event if no delegate or previously set value
					context.doLater(new IDeferment() {

						@Override
						public void run(final IImportContext context) {
							if (!(loadSlot instanceof SpotSlot) && loadSlot.getContract() == null) {
								if (loadSlot.getPricingEvent() == null) {
									loadSlot.setPricingEvent(PricingEvent.START_LOAD);
								}
							}
						}

						@Override
						public int getStage() {
							// TODO Auto-generated method stub
							return 0;
						}
					});
				} else if (object instanceof DischargeSlot) {
					final DischargeSlot dischargeSlot = (DischargeSlot) object;
					cargoModel.getDischargeSlots().add(dischargeSlot);
					// Set default pricing event if no delegate or previously set value
					context.doLater(new IDeferment() {

						@Override
						public void run(final IImportContext context) {
							if (!(dischargeSlot instanceof SpotSlot) && dischargeSlot.getContract() == null) {
								if (dischargeSlot.getPricingEvent() == null) {
									dischargeSlot.setPricingEvent(PricingEvent.START_DISCHARGE);
								}
							}
						}

						@Override
						public int getStage() {
							return IMMXImportContext.STAGE_REFERENCES_RESOLVED;
						}
					});
				}
			}
		}

		if (inputs.containsKey(CARGO_GROUP_KEY)) {
			final CSVReader reader = inputs.get(CARGO_GROUP_KEY);
			final Collection<EObject> values = cargoGroupImporter.importObjects(CargoPackage.eINSTANCE.getCargoGroup(), reader, context);
			cargoModel.getCargoGroups().addAll((Collection<? extends CargoGroup>) values);
		}

		if (inputs.containsKey(VESSEL_AVAILABILITY_KEY)) {
			cargoModel.getVesselAvailabilities().addAll(
					(Collection<? extends VesselAvailability>) vesselAvailabilityImporter.importObjects(CargoPackage.eINSTANCE.getVesselAvailability(), inputs.get(VESSEL_AVAILABILITY_KEY), context));
		}
		if (inputs.containsKey(EVENTS_KEY)) {
			cargoModel.getVesselEvents().addAll((Collection<? extends VesselEvent>) vesselEventImporter.importObjects(CargoPackage.eINSTANCE.getVesselEvent(), inputs.get(EVENTS_KEY), context));
		}
		if (inputs.containsKey(PAPER_DEALS_KEY)) {
			cargoModel.getPaperDeals().addAll((Collection<? extends PaperDeal>) paperDealsImporter.importObjects(CargoPackage.eINSTANCE.getPaperDeal(), inputs.get(PAPER_DEALS_KEY), context));
		}
		CanalBookings canalBookings = CargoFactory.eINSTANCE.createCanalBookings();
		cargoModel.setCanalBookings(canalBookings);
		if (inputs.containsKey(CANAL_BOOKINGS_KEY)) {
			final CSVReader reader = inputs.get(CANAL_BOOKINGS_KEY);
			final Collection<EObject> importObjects = canalBookingsImporter.importObjects(CargoPackage.eINSTANCE.getCanalBookingSlot(), reader, context);
			
			// See if we have imported a replacement object with params.
			for (final Object o : importObjects) {
				if (o instanceof CanalBookings) {
					canalBookings = (CanalBookings) o;
					cargoModel.setCanalBookings(canalBookings);
				}
			}
			for (final Object o : importObjects) {
				if (o instanceof VesselGroupCanalParameters) {
					final VesselGroupCanalParameters vesselGroupCanalParameters = (VesselGroupCanalParameters) o;
					canalBookings.getVesselGroupCanalParameters().add(vesselGroupCanalParameters);
				}
			}
			for (final Object o : importObjects) {
				if (o instanceof CanalBookingSlot) {
					final CanalBookingSlot canalBookingSlot = (CanalBookingSlot) o;
					canalBookings.getCanalBookingSlots().add(canalBookingSlot);
				}
			}
		}
		return cargoModel;
	}

	@Override
	public void exportModel(final EObject model, final Map<String, Collection<Map<String, String>>> output, final IMMXExportContext context) {
		final CargoModel cargoModel = (CargoModel) model;
		output.put(CARGO_KEY, cargoImporter.exportObjects(cargoModel.getCargoes(), cargoModel.getLoadSlots(), cargoModel.getDischargeSlots(), context));
		output.put(CARGO_GROUP_KEY, cargoGroupImporter.exportObjects(cargoModel.getCargoGroups(), context));
		output.put(VESSEL_AVAILABILITY_KEY, vesselAvailabilityImporter.exportObjects(cargoModel.getVesselAvailabilities(), context));
		output.put(EVENTS_KEY, vesselEventImporter.exportObjects(cargoModel.getVesselEvents(), context));
		output.put(PAPER_DEALS_KEY, paperDealsImporter.exportObjects(cargoModel.getPaperDeals(), context));
		final CanalBookings canalBookings = cargoModel.getCanalBookings();
		if (canalBookings != null) {
			final List<EObject> l = new LinkedList<>();
			l.add(canalBookings);
			l.addAll(canalBookings.getVesselGroupCanalParameters());
			l.addAll(canalBookings.getCanalBookingSlots());
			output.put(CANAL_BOOKINGS_KEY, canalBookingsImporter.exportObjects(l, context));
		}
	}

	@Override
	public EClass getEClass() {
		return CargoPackage.eINSTANCE.getCargoModel();
	}
}

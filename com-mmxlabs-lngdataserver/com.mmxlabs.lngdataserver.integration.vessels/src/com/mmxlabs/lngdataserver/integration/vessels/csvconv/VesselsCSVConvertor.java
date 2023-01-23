/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.vessels.csvconv;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.mmxlabs.lngdataserver.integration.vessels.VesselsIO;
import com.mmxlabs.lngdataserver.integration.vessels.model.FuelConsumption;
import com.mmxlabs.lngdataserver.integration.vessels.model.Vessel;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselPortAttributes;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselRouteParameters;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselTravelAttributes;
import com.mmxlabs.lngdataserver.integration.vessels.model.VesselsVersion;

/**
 * Helper class to read in vessel CSV files and generate a .lingodata file.
 * 
 * TODO: Test notes (multi line)
 * TODO: Test escaped characters (, & " etc)
 * TODO: Test lists (e.g inaccessible ports list) 
 * 
 * @author Simon Goodall
 *
 */
public class VesselsCSVConvertor {

	public static void main(String[] args) throws Exception {

		List<CSVVessel> loadVessels = loadVessels("C://temp//Vessels.csv");
		List<CSVConsumptionCurves> loadConsumptionCurves = loadConsumptionCurves("C://temp//Consumption Curves.csv");

		List<Vessel> convert = convert(loadVessels, loadConsumptionCurves);

		VesselsVersion vv = new VesselsVersion();
		vv.setVessels(convert);

		vv.setIdentifier(UUID.randomUUID().toString());

		saveTo(vv, "c://temp//vessels.lingodata");

	}

	public static List<Vessel> convert(final List<CSVVessel> vessels, final List<CSVConsumptionCurves> consumptionCurves) {
		final List<Vessel> newVessels = new LinkedList<>();

		for (final CSVVessel v : vessels) {

			final boolean hasReference = v.reference != null && !v.reference.isEmpty();

			final Vessel newVessel = new Vessel();
			set(newVessel::setMmxId, v.mmxId);
			set(newVessel::setName, v.name);
			set(newVessel::setShortName, v.shortName);
			set(newVessel::setImo, v.IMO);
			setOptional(newVessel::setType, v.type);
			setOptional(newVessel::setReferenceVessel, v.reference);
			setOptional(newVessel::setCapacity, v.capacity);
			setOptional(newVessel::setFillCapacity, v.fillCapacity);
			setOptional(newVessel::setScnt, v.scnt);

			set(newVessel::setNotes, v.notes);

			if (!hasReference || v.inaccessiblePortsOverride) {
				setOptional(newVessel::setInaccessiblePorts, v.inaccessiblePorts);
			}
			if (!hasReference || v.inaccessibleRoutesOverride) {
				setOptional(newVessel::setInaccessibleRoutes, v.inaccessibleRoutes);
			}

			setOptional(newVessel::setTravelBaseFuel, v.baseFuel);
			setOptional(newVessel::setIdleBaseFuel, v.idleBaseFuel);
			setOptional(newVessel::setInPortBaseFuel, v.inPortBaseFuel);
			setOptional(newVessel::setPilotLightBaseFuel, v.pilotLightBaseFuel);

			setOptional(newVessel::setMinSpeed, v.minSpeed);
			setOptional(newVessel::setMaxSpeed, v.maxSpeed);

			setOptional(newVessel::setSafetyHeel, v.safetyHeel);
			setOptional(newVessel::setWarmingTime, v.warmingTime);

			setOptional(newVessel::setPurgeTime, v.purgeTime);
			setOptional(newVessel::setCoolingVolume, v.coolingTime);
			setOptional(newVessel::setPilotLightRate, v.pilotLightRate);
			if (!hasReference || v.hasReliqCapabilityOverride) {
				setOptional(newVessel::setHasReliqCapacity, v.hasReliqCapability);
			}

			final VesselTravelAttributes ladenAttributes = new VesselTravelAttributes();
			newVessel.setLadenAttributes(ladenAttributes);
			setOptional(ladenAttributes::setServiceSpeed, v.ladenAttributes_serviceSpeed);
			setOptional(ladenAttributes::setNboRate, v.ladenAttributes_nboRate);
			setOptional(ladenAttributes::setIdleNBORate, v.ladenAttributes_idleNBORate);
			setOptional(ladenAttributes::setIdleBaseRate, v.ladenAttributes_idleBaseRate);

			final VesselTravelAttributes ballastAttributes = new VesselTravelAttributes();
			newVessel.setBallastAttributes(ballastAttributes);
			setOptional(ballastAttributes::setServiceSpeed, v.ballastAttributes_serviceSpeed);
			setOptional(ballastAttributes::setNboRate, v.ballastAttributes_nboRate);
			setOptional(ballastAttributes::setIdleNBORate, v.ballastAttributes_idleNBORate);
			setOptional(ballastAttributes::setIdleBaseRate, v.ballastAttributes_idleBaseRate);

			final VesselPortAttributes loadAttributes = new VesselPortAttributes();
			newVessel.setLoadAttributes(loadAttributes);
			setOptional(loadAttributes::setInPortNBORate, v.ladenAttributes_inPortNBORate);
			setOptional(loadAttributes::setInPortBaseRate, v.ladenAttributes_inPortBaseRate);

			final VesselPortAttributes dischargeAttributes = new VesselPortAttributes();
			newVessel.setDischargeAttributes(dischargeAttributes);
			setOptional(dischargeAttributes::setInPortNBORate, v.ballastAttributes_inPortNBORate);
			setOptional(dischargeAttributes::setInPortBaseRate, v.ballastAttributes_inPortBaseRate);

			// Find any consumption curves
			if (!hasReference || v.ladenAttributes_fuelConsumptionOverride) {
				for (final CSVConsumptionCurves c : consumptionCurves) {
					if (v.name.equalsIgnoreCase(c.vesselName) && "laden".equalsIgnoreCase(c.state)) {

						final List<FuelConsumption> values = new LinkedList<>();
						for (final Map.Entry<Double, Double> e : c.conumptionValues.entrySet()) {
							final FuelConsumption fc = new FuelConsumption();
							fc.setSpeed(e.getKey());
							fc.setConsumption(e.getValue());
							values.add(fc);
						}
						ladenAttributes.setFuelConsumption(Optional.of(values));
					}
				}
			}
			if (!hasReference || v.ballastAttributes_fuelConsumptionOverride) {
				for (final CSVConsumptionCurves c : consumptionCurves) {
					if (v.name.equalsIgnoreCase(c.vesselName) && "ballast".equalsIgnoreCase(c.state)) {

						final List<FuelConsumption> values = new LinkedList<>();
						for (final Map.Entry<Double, Double> e : c.conumptionValues.entrySet()) {
							final FuelConsumption fc = new FuelConsumption();
							fc.setSpeed(e.getKey());
							fc.setConsumption(e.getValue());
							values.add(fc);
						}
						ballastAttributes.setFuelConsumption(Optional.of(values));
					}
				}
			}

			if (!hasReference || v.routeParametersOverride) {
				final VesselRouteParameters suezParameters = new VesselRouteParameters();
				suezParameters.setRoute("SUEZ");
				set(suezParameters::setExtraTransitTimeInHours, v.suez_extraTransitTime);
				set(suezParameters::setBallastBunkerRate, v.suez_ballastConsumptionRate);
				set(suezParameters::setBallastNBORate, v.suez_ballastNBORate);
				set(suezParameters::setLadenBunkerRate, v.suez_ladenConsumptionRate);
				set(suezParameters::setLadenNBORate, v.suez_ladenNBORate);

				final VesselRouteParameters panamaParameters = new VesselRouteParameters();
				panamaParameters.setRoute("PANAMA");
				set(panamaParameters::setExtraTransitTimeInHours, v.panama_extraTransitTime);
				set(panamaParameters::setBallastBunkerRate, v.panama_ballastConsumptionRate);
				set(panamaParameters::setBallastNBORate, v.panama_ballastNBORate);
				set(panamaParameters::setLadenBunkerRate, v.panama_ladenConsumptionRate);
				set(panamaParameters::setLadenNBORate, v.panama_ladenNBORate);

				final List<VesselRouteParameters> parameters = new ArrayList<>(2);
				parameters.add(suezParameters);
				parameters.add(panamaParameters);

				set(newVessel::setRouteParameters, Optional.of(parameters));
			}

			newVessels.add(newVessel);
		}
		return newVessels;
	}

	public static List<CSVConsumptionCurves> loadConsumptionCurves(String path) throws IOException {

		List<CSVConsumptionCurves> vessels = new LinkedList<>();

		final CsvMapper mapper = new CsvMapper();
		final CsvSchema schema = mapper.schema() //
				.withHeader() //
		;
		try (FileInputStream fileInputStream = new FileInputStream(path)) {
			// Use a mapping iterator
			final MappingIterator<Map<String, String>> itr = mapper.readerFor(Map.class).with(schema).readValues(fileInputStream);

			while (itr.hasNext()) {
				final Map<String, String> v = itr.next();
				final CSVConsumptionCurves consumptionCurve = new CSVConsumptionCurves();
				for (final Map.Entry<String, String> e : v.entrySet()) {
					if ("class".equalsIgnoreCase(e.getKey())) {
						consumptionCurve.vesselName = e.getValue();
					} else if ("state".equalsIgnoreCase(e.getKey())) {
						consumptionCurve.state = e.getValue();
					} else {
						if (!e.getValue().isEmpty()) {
							consumptionCurve.conumptionValues.put(Double.parseDouble(e.getKey()), Double.parseDouble(e.getValue()));
						}
					}

				}
				vessels.add(consumptionCurve);
			}

		}
		return vessels;
	}

	public static List<CSVVessel> loadVessels(String path) throws IOException {

		List<CSVVessel> vessels = new LinkedList<>();

		// TODO: We may need to tweak for the List<String> import/parsing and any escaped quotes etc. (e.g. in notes fields)

		final CsvMapper mapper = new CsvMapper();
		final CsvSchema schema = mapper.schema() //
				.withHeader() //
				.withColumnReordering(true);
		final ObjectReader r = mapper.readerFor(CSVVessel.class).with(schema);
		try (FileInputStream fileInputStream = new FileInputStream(path)) {
			final Iterator<CSVVessel> itr = r.readValues(fileInputStream);
			while (itr.hasNext()) {
				final CSVVessel v = itr.next();
				vessels.add(v);
			}
		}

		return vessels;
	}

	private static void saveTo(VesselsVersion vv, String path) throws IOException {

		// DataManifest
		String manifest = String.format("{ \"entries\" : [ { \"dataVersion\" : \"%s\", \"modelVersion\" : %d, \"type\" : \"vessels\", \"path\" : \"vessels.json\" } ] }", vv.getIdentifier(),
				VesselsIO.CURRENT_MODEL_VERSION);

		try (ZipOutputStream os = new ZipOutputStream(new FileOutputStream(path))) {

			os.putNextEntry(new ZipEntry("manifest.json"));
			os.write(manifest.getBytes());
			os.closeEntry();

			os.putNextEntry(new ZipEntry("vessels.json"));
			VesselsIO.writeWithoutHeader(vv, os);
			os.closeEntry();

		}

	}

	private static void set(final Consumer<String> setter, final String value) {
		if (value != null && !value.isEmpty()) {
			setter.accept(value);
		}
	}

	private static void set(final DoubleConsumer setter, final double value) {
		setter.accept(value);
	}

	private static void set(final IntConsumer setter, final int value) {
		setter.accept(value);
	}

	private static <T> void set(final Consumer<T> setter, final T value) {
		if (value != null) {
			setter.accept(value);
		}
	}

	private static void setOptional(final Consumer<Optional<String>> setter, final String value) {
		if (value != null && !value.isEmpty()) {
			setter.accept(Optional.of(value));
		}
	}

	private static void setOptional(final Consumer<Optional<Boolean>> setter, final Boolean value) {
		if (value != null) {
			setter.accept(Optional.of(value));
		}
	}

	private static <T> void setOptional(final Consumer<Optional<T>> setter, final T value) {
		if (value != null) {
			setter.accept(Optional.of(value));
		}
	}

	private static void setOptional(final Consumer<OptionalInt> setter, final Integer value) {
		if (value != null) {
			setter.accept(OptionalInt.of(value));
		}
	}

	private static void setOptional(final Consumer<OptionalDouble> setter, final Double value) {
		if (value != null) {
			setter.accept(OptionalDouble.of(value));
		}
	}
}

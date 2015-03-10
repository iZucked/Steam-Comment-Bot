/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.its.uat.suite.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lingo.its.tests.AbstractOptimisationResultTester;
import com.mmxlabs.lingo.its.uat.suite.testers.GlobalUATTestsConfig;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.transformer.its.tests.ScenarioRunner;

/**
 * Abstract class with methods to extract features from given EMF models for use in producing test files and comparisons for clients. Expected to be subclassed on a per client, per contract basis.
 * 
 * @author achurchill
 * 
 */
public abstract class FeatureBasedUAT extends AbstractOptimisationResultTester {

	private static final Logger LOG = LoggerFactory.getLogger(FeatureBasedUAT.class);

	protected List<EStructuralFeature> getFeatures(final EClass c) {
		final List<EStructuralFeature> list = new ArrayList<EStructuralFeature>();
		final List<EStructuralFeature> features = c.getEAllStructuralFeatures();
		for (final EStructuralFeature f : features) {
			if (f.getEType() == EcorePackage.Literals.EINT || f.getEType() == EcorePackage.Literals.EINTEGER_OBJECT || f.getEType() == EcorePackage.Literals.EDOUBLE
					|| f.getEType() == EcorePackage.Literals.EDOUBLE_OBJECT || f.getEType() == EcorePackage.Literals.EBOOLEAN || f.getEType() == EcorePackage.Literals.EBOOLEAN_OBJECT
					|| f.getEType() == EcorePackage.Literals.ELONG || f.getEType() == EcorePackage.Literals.ELONG_OBJECT) {
				list.add(f);
			}
		}
		return list;
	}

	public FeatureBasedUAT() {

	}

	protected ScenarioRunner getScenarioRunner(final String filePath, final boolean isLingoFile) throws Exception {
		final URL url = getClass().getResource(filePath);
		final ScenarioRunner runner;
		if (isLingoFile) {
			runner = evaluateScenario(url);
		} else {
			runner = null;
		}
		return runner;
	}

	protected ScenarioRunner getScenarioRunner(final String filePath) throws Exception {

		return getScenarioRunner(filePath, isLingoFile(filePath));
	}

	public Schedule getSchedule(final String lingoFileName) throws Exception {
		final ScenarioRunner runner = getScenarioRunner(lingoFileName);
		Assert.assertNotNull(runner);

		// Update the scenario with the Schedule links
		runner.updateScenario();

		final Schedule schedule = runner.getIntialSchedule();
		return schedule;
	}

	protected boolean isLingoFile(final String filePath) {
		String extension = "";
		final int i = filePath.lastIndexOf('.');
		if (i > 0) {
			extension = filePath.substring(i + 1);
		}
		boolean isLingo;
		if (extension.equals("lingo")) {
			isLingo = true;
		} else {
			isLingo = false;
		}
		return isLingo;
	}

	protected void fillFeatureMap(final EClass eClass, final IdMapContainer baseTable, final EObject containedObj, final String prefix) {
		if (eClass != null) {
			for (final EStructuralFeature e : getFeatures(eClass)) {
				baseTable.getIdMapList().add(new IdMap() {
					{
						this.setId(prefix.equals("") ? e.getName() : prefix + "-" + e.getName());
						setFeature(e);
						setContainer(containedObj);
					}
				});
			}
		}
	}

	protected void fillFeatureMap(final EClass eClass, final IdMapContainer baseTable, final EObject containedObj) {
		fillFeatureMap(eClass, baseTable, containedObj, "");
	}

	private double getLingoOutput(final EObject modelObject, final EStructuralFeature feature) {
		double value;
		if (modelObject.eGet(feature) instanceof Integer) {
			value = (double) ((Integer) modelObject.eGet(feature));
		} else if (modelObject.eGet(feature) instanceof Boolean) {
			final boolean boolValue = (Boolean) modelObject.eGet(feature);
			if (boolValue) {
				value = 1;
			} else {
				value = 0;
			}
		} else if (modelObject.eGet(feature) instanceof Long) {
			value = (double) ((Long) modelObject.eGet(feature));
		} else {
			value = (double) modelObject.eGet(feature);
		}
		final int multiplier = getMultiplier(feature);
		return value * (Math.pow(10, multiplier));
	}

	private int getMultiplier(final EStructuralFeature e) {
		final Integer multiplier = FeatureIdTools.getMultiplier(e);
		if (multiplier != null) {
			return FeatureIdTools.getMultiplier(e);
		}
		return 0;
	}

	protected IdMapContainer getGPLFeaturesMap(final GroupProfitAndLoss gPL) {
		final IdMapContainer gPLTable = new IdMapContainer("groupProfitAndLoss");
		if (gPL != null) {
			fillFeatureMap(gPL.eClass(), gPLTable, gPL);
		}
		return gPLTable;
	}

	protected IdMapContainer getLoadSlotFeaturesMap(final EList<SlotAllocation> slotAllocations) {
		final IdMapContainer slotTable = new IdMapContainer("loadSlot");
		LoadSlot loadSlot = null;
		SlotAllocation loadAllocation = null;
		for (final SlotAllocation slotAllocation : slotAllocations) {
			if (slotAllocation.getSlot() instanceof LoadSlot) {
				loadSlot = (LoadSlot) slotAllocation.getSlot();
				loadAllocation = slotAllocation;
			}
		}
		if (loadSlot != null && loadAllocation != null) {
			fillFeatureMap(loadSlot.eClass(), slotTable, loadSlot);
			fillFeatureMap(loadAllocation.eClass(), slotTable, loadAllocation, "allocation");
		}
		return slotTable;
	}

	protected IdMapContainer getDischargeSlotFeaturesMap(final EList<SlotAllocation> slotAllocations) {
		final IdMapContainer slotTable = new IdMapContainer("dischargeSlot");
		DischargeSlot dischargeSlot = null;
		SlotAllocation dischargeAllocation = null;
		for (final SlotAllocation slotAllocation : slotAllocations) {
			if (slotAllocation.getSlot() instanceof DischargeSlot) {
				dischargeSlot = (DischargeSlot) slotAllocation.getSlot();
				dischargeAllocation = slotAllocation;
			}
		}
		if (dischargeSlot != null && dischargeAllocation != null) {
			fillFeatureMap(dischargeSlot.eClass(), slotTable, dischargeSlot);
			fillFeatureMap(dischargeAllocation.eClass(), slotTable, dischargeAllocation, "allocation");
		}
		return slotTable;
	}

	protected IdMapContainer getEventsFeaturesMap(final EList<Event> events) {
		final IdMapContainer eventTable = new IdMapContainer("events");

		for (int i = 0; i < events.size(); i++) {
			final Event e = events.get(i);
			if (e instanceof FuelUsage) {
				final FuelUsage fu = (FuelUsage) e;
				for (int fqIndex = 0; fqIndex < fu.getFuels().size(); fqIndex++) {
					final FuelQuantity fq = fu.getFuels().get(fqIndex);
					for (int faIndex = 0; faIndex < fq.getAmounts().size(); faIndex++) {
						final FuelAmount fa = fq.getAmounts().get(faIndex);
						fillFeatureMap(SchedulePackage.Literals.FUEL_AMOUNT, eventTable, fa,
								String.format("%s(%s)-%s-fa(%s)-%s", i, e.eClass().getName(), fq.getFuel().getName(), faIndex, fa.getUnit().getName()));
					}
				}
			}
		}
		return eventTable;
	}

	protected void addDefaultDetails(final List<IdMapContainer> allDetails, final CargoAllocation cargoAllocation) {
		if (GlobalUATTestsConfig.INCLUDE_GROUP_PROFIT_LOSS_DETAILS) {
			final GroupProfitAndLoss gPL = cargoAllocation.getGroupProfitAndLoss();
			allDetails.add(getGPLFeaturesMap(gPL));
		}
		if (GlobalUATTestsConfig.INCLUDE_SLOT_DETAILS) {
			final EList<SlotAllocation> slotAllocations = cargoAllocation.getSlotAllocations();
			allDetails.add(getLoadSlotFeaturesMap(slotAllocations));
			allDetails.add(getDischargeSlotFeaturesMap(slotAllocations));
		}
		if (GlobalUATTestsConfig.INCLUDE_EVENT_FUEL_DETAILS) {
			allDetails.add(getEventsFeaturesMap(cargoAllocation.getEvents()));
		}
	}

	public void createPropertiesForCase(final String lingoFileName, final String cargoName) throws Exception {
		final Schedule schedule = getSchedule(lingoFileName);

		final Properties props = getNewProperties(lingoFileName);
		final URL propsURL = getPropertiesURL(lingoFileName, "");

		fillProperties(schedule, cargoName, props);

		writePropertiesFile(props, propsURL);
	}

	public void createPropertiesForTypedCase(final Schedule schedule, final String lingoFileName, final String cargoName) throws Exception {
		final Properties props = getNewProperties(lingoFileName);
		final URL propsURL = getPropertiesURL(lingoFileName, cargoName);

		final List<IdMapContainer> idMapContainers = getIdMapContainers(schedule, cargoName);
		for (final IdMapContainer d : idMapContainers) {
			addToProperties(props, d.getIdMapList(), d.name);
		}

		writePropertiesFile(props, propsURL);
	}

	private Properties getNewProperties(final String lingoFileName) throws MalformedURLException, IOException {
		/**
		 * Extend to save properties in a sorted order for ease of reading
		 */
		@SuppressWarnings("serial")
		final Properties props = new Properties() {
			@Override
			public Set<Object> keySet() {
				return Collections.unmodifiableSet(new TreeSet<Object>(super.keySet()));
			}

			@Override
			public synchronized Enumeration<Object> keys() {
				return Collections.enumeration(new TreeSet<Object>(super.keySet()));
			}
		};
		// store some meta data:
		props.setProperty("lingoFileURI", lingoFileName);
		return props;
	}

	private Properties getExistingProperties(final String lingoFileName, final String suffix) throws MalformedURLException, IOException {
		final Properties props = new Properties();
		final URL propsURL = getPropertiesURL(lingoFileName, suffix);
		props.load(propsURL.openStream());
		return props;
	}

	private void fillProperties(final Schedule schedule, final String cargoName, final Properties props) {
		final List<IdMapContainer> idMapContainers = getIdMapContainers(schedule, cargoName);
		for (final IdMapContainer d : idMapContainers) {
			addToProperties(props, d.getIdMapList(), d.name);
		}
	}

	private void writePropertiesFile(final Properties props, final URL propsURL) throws FileNotFoundException, IOException {
		// create properties file
		File file;
		try {
			file = new File(propsURL.toURI());
			props.store(new FileOutputStream(file), "Created by " + this.getClass().getName());
		} catch (final URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void checkPropertiesForCase(final String lingoFileName, final String cargoName, final boolean additionalChecks) throws Exception {
		final Properties props = getExistingProperties(lingoFileName, "");

		final Schedule schedule = getSchedule(lingoFileName);

		final List<IdMapContainer> idMapContainers = getIdMapContainers(schedule, cargoName);
		for (final IdMapContainer d : idMapContainers) {
			comparePropertiesToLingo(props, d.getIdMapList(), d.name, cargoName);
		}

		if (additionalChecks) {
			additionalChecks(schedule, cargoName);
		}
	}

	public void checkPropertiesForTypedCase(final Schedule schedule, final String lingoFileName, final String cargoName, final boolean additionalChecks) throws Exception {
		final Properties props = getExistingProperties(lingoFileName, cargoName);

		final List<IdMapContainer> idMapContainers = getIdMapContainers(schedule, cargoName);
		for (final IdMapContainer d : idMapContainers) {
			comparePropertiesToLingo(props, d.getIdMapList(), d.name, cargoName);
		}

		if (additionalChecks) {
			additionalChecks(schedule, cargoName);
		}
	}

	/**
	 * This method is used to add special contract specific checks
	 * 
	 * @param schedule
	 * @param cargoName
	 */
	protected void additionalChecks(final Schedule schedule, final String cargoName) {
	}

	protected URL getPropertiesURL(final String lingoFileName, final String suffix) throws MalformedURLException, IOException {
		final URL propsURL;
		final URL url = getClass().getResource(lingoFileName);
		final String urlString = FileLocator.toFileURL(new URL(url.toString())).toString();
		String propsPath;
		if (isLingoFile(urlString)) {
			propsPath = urlString.replaceAll("\\.lingo$", "");
			propsPath = propsPath + (suffix.equals("") ? "" : "-" + suffix) + ".properties";
			propsPath = propsPath.replaceAll(" ", "%20");
		} else {
			propsPath = urlString;
			int end = propsPath.length();
			for (int i = propsPath.length() - 1; i >= 0; i--) {
				if (i != propsPath.length() - 1 && propsPath.charAt(i) == '/') {
					end = i;
					break;
				}
			}
			propsPath = (propsPath.substring(0, end + 1) + propsPath.substring(end).replaceAll("/", "") + (suffix.equals("") ? "" : "-" + suffix) + ".properties").replaceAll(" ", "%20");
		}
		propsURL = new URL(propsPath);
		return propsURL;
	}

	private void addToProperties(final Properties prop, final List<IdMap> table, final String prefix) {
		for (final IdMap map : table) {
			prop.setProperty(createPropertyKey(map, prefix), String.format("%.7f", (double) getLingoOutput(map.getContainer(), map.getFeature())));
		}
	}

	private void comparePropertiesToLingo(final Properties prop, final List<IdMap> table, final String prefix, final String cargoName) {
		for (final IdMap map : table) {
			final String key = createPropertyKey(map, prefix);
			if (prop.containsKey(key)) {
				final double propertiesValue = Double.valueOf(prop.getProperty(key));
				final double lingoValue = getLingoOutput(map.getContainer(), map.getFeature());
				Assert.assertEquals(String.format("testing cargo (%s) \"%s:\"", cargoName, key), propertiesValue, lingoValue, 0.000001);
			} else {
				LOG.warn(key + " not in map");
			}
		}
	}

	private String createPropertyKey(final IdMap map, final String prefix) {
		return String.format("%s-%s", prefix, map.getId());
	}

	/**
	 * This abstract method must be implemented to produce the features used to create testing properties.
	 * 
	 * @param schedule
	 * @param cargoName
	 * @return
	 */
	protected abstract List<IdMapContainer> getIdMapContainers(Schedule schedule, String cargoName);

}

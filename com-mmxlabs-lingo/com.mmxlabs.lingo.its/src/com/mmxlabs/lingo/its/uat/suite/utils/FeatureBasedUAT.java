/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lingo.its.tests.AbstractOptimisationResultTester;
import com.mmxlabs.lingo.its.tests.LNGScenarioRunnerCreator;
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
import com.mmxlabs.models.lng.transformer.ui.LNGScenarioRunner;
import com.mmxlabs.models.mmxcore.NamedObject;

/**
 * Abstract class with methods to extract features from given EMF models for use in producing test files and comparisons for clients. Expected to be subclassed on a per client, per contract basis.
 * 
 * @author achurchill
 * 
 */
public abstract class FeatureBasedUAT extends AbstractOptimisationResultTester {

	private static final Logger LOG = LoggerFactory.getLogger(FeatureBasedUAT.class);

	protected List<EStructuralFeature> getFeatures(@NonNull final EClass c) {
		final List<EStructuralFeature> list = new ArrayList<EStructuralFeature>();
		final List<EStructuralFeature> features = c.getEAllStructuralFeatures();
		for (final EStructuralFeature f : features) {
			if (f.getName().equals("contract")) {
				int z = 0;
			}
			if (f instanceof EAttribute) {
				if (f.getEType() == EcorePackage.Literals.EINT || f.getEType() == EcorePackage.Literals.EINTEGER_OBJECT || f.getEType() == EcorePackage.Literals.EDOUBLE
						|| f.getEType() == EcorePackage.Literals.EDOUBLE_OBJECT || f.getEType() == EcorePackage.Literals.EBOOLEAN || f.getEType() == EcorePackage.Literals.EBOOLEAN_OBJECT
						|| f.getEType() == EcorePackage.Literals.ELONG || f.getEType() == EcorePackage.Literals.ELONG_OBJECT || f.getEType().getInstanceClass() == LocalDate.class || f.getEType() == EcorePackage.Literals.ESTRING) {
					list.add(f);
					}
			} else if (f instanceof EReference) {
				if (((EReference) f).getUpperBound() == 1 && NamedObject.class.isAssignableFrom(((EReference) f).getEReferenceType().getInstanceClass())) {
					list.add(f);
				}
			}
		}
		return list;
	}

	public FeatureBasedUAT() {

	}

	protected LNGScenarioRunner getScenarioRunner(@NonNull final String filePath, final boolean isLingoFile) throws Exception {
		final URL url = getClass().getResource(filePath);
		Assert.assertNotNull(url);
		final LNGScenarioRunner runner;
		if (isLingoFile) {
			runner = LNGScenarioRunnerCreator.createScenarioRunnerForEvaluation(url, false);
		} else {
			runner = null;
		}
		return runner;
	}

	protected LNGScenarioRunner getScenarioRunner(@NonNull final String filePath) throws Exception {

		return getScenarioRunner(filePath, isLingoFile(filePath));
	}

	public Schedule getSchedule(@NonNull final String lingoFileName) throws Exception {
		final LNGScenarioRunner runner = getScenarioRunner(lingoFileName);
		Assert.assertNotNull(runner);
		final Schedule schedule = runner.getSchedule();
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

	protected void fillFeatureMap(@NonNull final EClass eClass, @NonNull final IdMapContainer baseTable, @NonNull final EObject containedObj, @NonNull final String prefix) {
		if (eClass != null) {

			ArrayList<EObject> open = new ArrayList<EObject>();
			ArrayList<String> handles = new ArrayList<String>();
			open.add(containedObj);
			handles.add(prefix);

			while (open.size() > 0) {
				EObject current_container = open.get(0);

				if (current_container.eContainmentFeature().getName() != "groupProfitAndLoss") {
					EClass current_class = current_container.eClass();

					for (final EStructuralFeature e : getFeatures(current_class)) {

						baseTable.getIdMapList().add(new IdMap(handles.get(0).equals("") ? e.getName() : handles.get(0) + "-" + e.getName(), e, current_container));
					}

					EList<EObject> child_containers = current_container.eContents();

					for (EObject child_container : child_containers) {

						String child_handle = child_container.eContainmentFeature().getName();
						open.add(child_container);
						String handle = handles.get(0).concat(child_handle);
						handles.add(handle);

					}

					open.remove(0);
					handles.remove(0);

				} else {
					EClass current_class = current_container.eClass();

					for (final EStructuralFeature e : getFeatures(current_class)) {

						baseTable.getIdMapList().add(new IdMap(handles.get(0) + e.getName(), e, current_container));

					}

					EList<EObject> child_containers = current_container.eContents();
					Integer i = 0;
					for (EObject child_container : child_containers) {

						String child_handle = child_container.eContainmentFeature().getName().concat(Integer.toString(i));

						open.add(child_container);
						String handle = handles.get(0).concat(child_handle);
						handles.add(handle);
						i++;

					}
					open.remove(0);

					handles.remove(0);
				}
			}

		}
	}

	protected void fillFeatureMap(@NonNull final EClass eClass, @NonNull final IdMapContainer baseTable, @NonNull final EObject containedObj) {
		fillFeatureMap(eClass, baseTable, containedObj, "");
	}

	private double getLingoOutput(@NonNull final EObject modelObject, @NonNull final EStructuralFeature feature) {
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

	private String getLingoOutputString(@NonNull final EObject modelObject, @NonNull final EStructuralFeature feature) {
		if (modelObject.eGet(feature) == null) {
			return "null";
		} else if (feature.getEType().getInstanceClass() == LocalDate.class){
			return ((LocalDate) modelObject.eGet(feature)).toString();
		} else if (feature instanceof EReference) {
			if (NamedObject.class.isAssignableFrom(((EReference) feature).getEReferenceType().getInstanceClass())) {
				return ((NamedObject) modelObject.eGet(feature)).getName();
			}
		} else if (modelObject.eGet(feature) instanceof String) {
			return (String) modelObject.eGet(feature);
		}
		return "unsupported output";
	}
	
	private int getMultiplier(@NonNull final EStructuralFeature e) {
		final Integer multiplier = FeatureIdTools.getMultiplier(e);
		if (multiplier != null) {
			return FeatureIdTools.getMultiplier(e);
		}
		return 0;
	}

	protected IdMapContainer getGPLFeaturesMap(@Nullable final GroupProfitAndLoss gPL) {
		final IdMapContainer gPLTable = new IdMapContainer("groupProfitAndLoss");
		if (gPL != null) {
			fillFeatureMap(gPL.eClass(), gPLTable, gPL);
		}
		return gPLTable;
	}

	protected IdMapContainer getLoadSlotFeaturesMap(@NonNull final EList<SlotAllocation> slotAllocations) {
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

	protected IdMapContainer getDischargeSlotFeaturesMap(@NonNull final EList<SlotAllocation> slotAllocations) {
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

	protected IdMapContainer getEventsFeaturesMap(@NonNull final EList<Event> events) {
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

	protected void addDefaultDetails(@NonNull final List<IdMapContainer> allDetails, @NonNull final CargoAllocation cargoAllocation) {
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

	public void createPropertiesForCase(@NonNull final String lingoFileName, @NonNull final String cargoName) throws Exception {
		final Schedule schedule = getSchedule(lingoFileName);

		final Properties props = getNewProperties(lingoFileName);
		final URL propsURL = getPropertiesURL(lingoFileName, "");

		fillProperties(schedule, cargoName, props);

		writePropertiesFile(props, propsURL);
	}

	public void createPropertiesForTypedCase(@NonNull final Schedule schedule, @NonNull final String lingoFileName, @NonNull final String cargoName) throws Exception {
		final Properties props = getNewProperties(lingoFileName);
		final URL propsURL = getPropertiesURL(lingoFileName, cargoName);

		final List<IdMapContainer> idMapContainers = getIdMapContainers(schedule, cargoName);
		for (final IdMapContainer d : idMapContainers) {
			addToProperties(props, d.getIdMapList(), d.name);
		}

		writePropertiesFile(props, propsURL);
	}

	private Properties getNewProperties(@NonNull final String lingoFileName) throws MalformedURLException, IOException {
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

	private Properties getExistingProperties(@NonNull final String lingoFileName, @NonNull final String suffix) throws MalformedURLException, IOException {
		final Properties props = new Properties();
		final URL propsURL = getPropertiesURL(lingoFileName, suffix);
		props.load(propsURL.openStream());
		return props;
	}

	private void fillProperties(@NonNull final Schedule schedule, @NonNull final String cargoName, @NonNull final Properties props) {
		final List<IdMapContainer> idMapContainers = getIdMapContainers(schedule, cargoName);
		for (final IdMapContainer d : idMapContainers) {
			addToProperties(props, d.getIdMapList(), d.name);
		}
	}

	private void writePropertiesFile(@NonNull final Properties props, @NonNull final URL propsURL) throws FileNotFoundException, IOException {
		// create properties file
		File file;
		try {
			file = new File(propsURL.toURI());
			props.store(new FileOutputStream(file), "Created by " + this.getClass().getName());
		} catch (final URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void checkPropertiesForCase(@NonNull final String lingoFileName, @NonNull final String cargoName, final boolean additionalChecks) throws Exception {
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

	public void checkPropertiesForTypedCase(@NonNull final Schedule schedule, @NonNull final String lingoFileName, @NonNull final String cargoName, final boolean additionalChecks) throws Exception {
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
	protected void additionalChecks(@NonNull final Schedule schedule, @NonNull final String cargoName) {
	}

	@NonNull
	protected URL getPropertiesURL(@NonNull final String lingoFileName, @NonNull final String suffix) throws MalformedURLException, IOException {
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

	private void addToProperties(@NonNull final Properties prop, @NonNull final List<IdMap> table, @NonNull final String prefix) {
		for (final IdMap map : table) {
//			prop.setProperty(createPropertyKey(map, prefix), String.format("%.7f", (double) getLingoOutput(map.getContainer(), map.getFeature())));
			prop.setProperty(createPropertyKey(map, prefix), formatProperty(map.getContainer(), map.getFeature()));
		}
	}
	
	private String formatProperty(EObject eObj, EStructuralFeature f) {
		if (f.getEType() == EcorePackage.Literals.EINT || f.getEType() == EcorePackage.Literals.EINTEGER_OBJECT || f.getEType() == EcorePackage.Literals.EDOUBLE
				|| f.getEType() == EcorePackage.Literals.EDOUBLE_OBJECT || f.getEType() == EcorePackage.Literals.EBOOLEAN || f.getEType() == EcorePackage.Literals.EBOOLEAN_OBJECT
				|| f.getEType() == EcorePackage.Literals.ELONG || f.getEType() == EcorePackage.Literals.ELONG_OBJECT) {
			return String.format("%.7f", (double) getLingoOutput(eObj, f));
		} else {
			return getLingoOutputString(eObj, f);
		}
 
	}

	private void comparePropertiesToLingo(@NonNull final Properties prop, @NonNull final List<IdMap> table, @NonNull final String prefix, @NonNull final String cargoName) {
		for (final IdMap map : table) {
			final String key = createPropertyKey(map, prefix);
			if (prop.containsKey(key) && !key.contains("uuid")) {
				final String propertiesValue = prop.getProperty(key);
				final String lingoValue = formatProperty(map.getContainer(), map.getFeature());
				Assert.assertEquals(String.format("testing cargo (%s) \"%s:\"", cargoName, key), propertiesValue, lingoValue);
//				final double propertiesValue = Double.valueOf(prop.getProperty(key));
//				final double lingoValue = getLingoOutput(map.getContainer(), map.getFeature());
//				Assert.assertEquals(String.format("testing cargo (%s) \"%s:\"", cargoName, key), propertiesValue, lingoValue, 0.000001);
			} else {
				LOG.warn(key + " not in map");
			}
		}
	}

	private String createPropertyKey(@NonNull final IdMap map, @NonNull final String prefix) {
		return String.format("%s-%s", prefix, map.getId());
	}

	/**
	 * This abstract method must be implemented to produce the features used to create testing properties.
	 * 
	 * @param schedule
	 * @param cargoName
	 * @return
	 */
	protected abstract List<IdMapContainer> getIdMapContainers(@NonNull Schedule schedule, @NonNull String cargoName);

}

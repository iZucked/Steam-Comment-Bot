package com.mmxlabs.lingo.its.uat.utils;

import java.io.File;
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

import com.mmxlabs.lingo.its.tests.AbstractOptimisationResultTester;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.transformer.its.tests.ScenarioRunner;

public abstract class FeatureBasedUAT extends AbstractOptimisationResultTester {

	protected List<EStructuralFeature> getFeatures(EClass c) {
		List<EStructuralFeature> list = new ArrayList<EStructuralFeature>();
		List<EStructuralFeature> features = c.getEAllStructuralFeatures();
		for (EStructuralFeature f : features) {
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

	protected ScenarioRunner getScenarioRunner(String filePath, boolean isLingoFile) throws Exception {
		final URL url = getClass().getResource(filePath);
		final ScenarioRunner runner;
		if (isLingoFile) {
			runner = evaluateScenario(url);
		} else {
			runner = null;
		}
		return runner;
	}

	protected ScenarioRunner getScenarioRunner(String filePath) throws Exception {

		return getScenarioRunner(filePath, isLingoFile(filePath));
	}

	protected boolean isLingoFile(String filePath) {
		String extension = "";
		int i = filePath.lastIndexOf('.');
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

	private double getLingoOutput(EObject modelObject, EStructuralFeature feature) {
		double value;
		if (modelObject.eGet(feature) instanceof Integer) {
			value = (double) ((Integer) modelObject.eGet(feature));
		} else if (modelObject.eGet(feature) instanceof Boolean) {
			boolean boolValue = (Boolean) modelObject.eGet(feature);
			if (boolValue) {
				value = 0;
			} else {
				value = 1;
			}
		} else if (modelObject.eGet(feature) instanceof Long) {
			value = (double) ((Long) modelObject.eGet(feature));
		} else {
			value = (double) modelObject.eGet(feature);
		}
		int multiplier = getMultiplier(feature);
		return value * (Math.pow(10, multiplier));
	}

	private int getMultiplier(EStructuralFeature e) {
		Integer multiplier = FeatureIdTools.getMultiplier(e);
		if (multiplier != null) {
			return FeatureIdTools.getMultiplier(e);
		}
		return 0;
	}

	protected IdMapContainer getGPLFeaturesMap(GroupProfitAndLoss gPL) {
		IdMapContainer gPLTable = new IdMapContainer("groupProfitAndLoss");
		if (gPL != null) {
			fillFeatureMap(gPL.eClass(), gPLTable, gPL);
		}
		return gPLTable;
	}

	protected IdMapContainer getLoadSlotFeaturesMap(EList<SlotAllocation> slotAllocations) {
		IdMapContainer slotTable = new IdMapContainer("loadSlot");
		LoadSlot loadSlot = null;
		for (final SlotAllocation slotAllocation : slotAllocations) {
			if (slotAllocation.getSlot() instanceof LoadSlot) {
				loadSlot = (LoadSlot) slotAllocation.getSlot();
			}
		}
		if (loadSlot != null) {
			fillFeatureMap(loadSlot.eClass(), slotTable, loadSlot);
		}
		return slotTable;
	}

	protected IdMapContainer getDischargeSlotFeaturesMap(EList<SlotAllocation> slotAllocations) {
		IdMapContainer slotTable = new IdMapContainer("dischargeSlot");
		DischargeSlot dischargeSlot = null;
		for (final SlotAllocation slotAllocation : slotAllocations) {
			if (slotAllocation.getSlot() instanceof DischargeSlot) {
				dischargeSlot = (DischargeSlot) slotAllocation.getSlot();
			}
		}
		if (dischargeSlot != null) {
			fillFeatureMap(dischargeSlot.eClass(), slotTable, dischargeSlot);
		}
		return slotTable;
	}

	protected abstract List<IdMapContainer> getIdMapContainers(Schedule schedule, String cargoName);

	public void createPropertiesForCase(String lingoFileName, String cargoName) throws Exception {
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
		final URL propsURL = getPropertiesURL(lingoFileName);
		final ScenarioRunner runner = getScenarioRunner(lingoFileName);
		Assert.assertNotNull(runner);

		// Update the scenario with the Schedule links
		runner.updateScenario();

		final Schedule schedule = runner.getIntialSchedule();

		final List<IdMapContainer> idMapContainers = getIdMapContainers(schedule, cargoName);
		for (IdMapContainer d : idMapContainers) {
			addToProperties(props, d.getIdMapList(), d.name);
		}

		// create properties file
		File file;
		try {
			file = new File(propsURL.toURI());
			props.store(new FileOutputStream(file), "Created by " + this.getClass().getName());
		} catch (final URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void checkPropertiesForCase(String lingoFileName, String cargoName, boolean additionalChecks) throws Exception {
		final Properties props = new Properties();
		final URL propsURL = getPropertiesURL(lingoFileName);
		props.load(propsURL.openStream());

		final ScenarioRunner runner = getScenarioRunner(lingoFileName);
		Assert.assertNotNull(runner);

		// Update the scenario with the Schedule links
		runner.updateScenario();

		final Schedule schedule = runner.getIntialSchedule();

		final List<IdMapContainer> idMapContainers = getIdMapContainers(schedule, cargoName);
		for (IdMapContainer d : idMapContainers) {
			comparePropertiesToLingo(props, d.getIdMapList(), d.name);
		}

		if (additionalChecks) {
			additionalChecks(schedule, cargoName);
		}
	}

	protected void additionalChecks(Schedule schedule, String cargoName) {
	}

	protected URL getPropertiesURL(String lingoFileName) throws MalformedURLException, IOException {
		final URL propsURL;
		final URL url = getClass().getResource(lingoFileName);
		String urlString = FileLocator.toFileURL(new URL(url.toString())).toString();
		String propsPath;
		if (isLingoFile(urlString)) {
			propsPath = urlString.replaceAll("\\.lingo$", "");
			propsPath = propsPath + ".properties";
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
			propsPath = (propsPath.substring(0, end + 1) + propsPath.substring(end).replaceAll("/", "") + ".properties").replaceAll(" ", "%20");
		}
		propsURL = new URL(propsPath);
		return propsURL;
	}

	private void addToProperties(Properties prop, List<IdMap> table, String prefix) {
		for (IdMap map : table) {
			prop.setProperty(createPropertyKey(map, prefix), String.format("%.7f", (double) getLingoOutput(map.getContainer(), map.getFeature())));
		}
	}

	private void comparePropertiesToLingo(Properties prop, List<IdMap> table, String prefix) {
		for (IdMap map : table) {
			String key = createPropertyKey(map, prefix);
			Assert.assertTrue(prop.containsKey(key));
			double propertiesValue = Double.valueOf(prop.getProperty(key));
			double lingoValue = getLingoOutput(map.getContainer(), map.getFeature());
			Assert.assertEquals("testing \"" + key + "\":", propertiesValue, lingoValue, 0.000001);
		}
	}

	private String createPropertyKey(IdMap map, String prefix) {
		return String.format("%s-%s", prefix, map.getId());
	}

}

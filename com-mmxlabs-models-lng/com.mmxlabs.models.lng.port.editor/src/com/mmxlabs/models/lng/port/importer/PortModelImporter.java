/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.importer;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.port.CapabilityGroup;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.CSVReader;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.ISubmodelImporter;
import com.mmxlabs.models.util.importer.registry.IImporterRegistry;

/**
 * @since 2.0
 */
public class PortModelImporter implements ISubmodelImporter {
	/**
	 * 
	 */
	private static final String SUEZ_CANAL_NAME = "Suez canal";
	/**
	 * 
	 */
	private static final String DIRECT_NAME = "Direct";
	public static final String PORT_KEY = "PORT";
	public static final String PORT_GROUP_KEY = "PORTGROUP";
	public static final String DISTANCES_KEY = "DISTANCES";
	public static final String SUEZ_KEY = "SUEZ";
	public static final HashMap<String, String> inputs = new LinkedHashMap<String, String>();
	static {
		inputs.put(PORT_KEY, "Ports");
		inputs.put(PORT_GROUP_KEY, "Port Groups");
		inputs.put(DISTANCES_KEY, "Distance Matrix");
		inputs.put(SUEZ_KEY, "Suez Distance Matrix");
	}

	@Inject
	private IImporterRegistry importerRegistry;

	private IClassImporter portImporter;
	private IClassImporter portGroupImporter;
	private RouteImporter routeImporter = new RouteImporter();

	/**
	 * @since 2.0
	 */
	public PortModelImporter() {
		final Activator activator = Activator.getDefault();
		if (activator != null) {

			importerRegistry = activator.getImporterRegistry();
			registryInit();
		}
	}

	@Inject
	private void registryInit() {
		if (importerRegistry != null) {
			portImporter = importerRegistry.getClassImporter(PortPackage.eINSTANCE.getPort());
			portGroupImporter = importerRegistry.getClassImporter(PortPackage.eINSTANCE.getPortGroup());
		}
	}

	@Override
	public Map<String, String> getRequiredInputs() {
		return inputs;
	}

	@Override
	public UUIDObject importModel(Map<String, CSVReader> inputs, IImportContext context) {
		final PortModel result = PortFactory.eINSTANCE.createPortModel();

		final PortModel portModel = result;
		if (portModel != null) {
			for (final PortCapability capability : PortCapability.values()) {
				boolean found = false;
				for (final CapabilityGroup g : portModel.getSpecialPortGroups()) {
					if (g.getCapability().equals(capability)) {
						found = true;
						break;
					}
				}
				if (found == false) {
					final CapabilityGroup g = PortFactory.eINSTANCE.createCapabilityGroup();
					g.setName("All " + capability.getName() + " Ports");
					g.setCapability(capability);
					portModel.getSpecialPortGroups().add(g);
					context.registerNamedObject(g);
				}
			}
		}

		if (inputs.containsKey(PORT_KEY)) {
			final CSVReader reader = inputs.get(PORT_KEY);
			result.getPorts().addAll((Collection<? extends Port>) portImporter.importObjects(PortPackage.eINSTANCE.getPort(), reader, context));
		}
		if (inputs.containsKey(PORT_GROUP_KEY)) {
			final CSVReader reader = inputs.get(PORT_GROUP_KEY);
			result.getPortGroups().addAll((Collection<? extends PortGroup>) portGroupImporter.importObjects(PortPackage.eINSTANCE.getPortGroup(), reader, context));
		}
		if (inputs.containsKey(DISTANCES_KEY)) {
			final Route direct = routeImporter.importRoute(inputs.get(DISTANCES_KEY), context);
			if (direct != null) {
				direct.setName(DIRECT_NAME);
				result.getRoutes().add(direct);
				context.registerNamedObject(direct);
			}
		}
		if (inputs.containsKey(SUEZ_KEY)) {
			final Route suez = routeImporter.importRoute(inputs.get(SUEZ_KEY), context);
			if (suez != null) {
				suez.setName(SUEZ_CANAL_NAME);
				suez.setCanal(true);
				result.getRoutes().add(suez);
				context.registerNamedObject(suez);
			}
		}
		return result;
	}

	@Override
	public void exportModel(MMXRootObject root, UUIDObject model, Map<String, Collection<Map<String, String>>> output) {
		for (final Route r : ((PortModel) model).getRoutes()) {
			Collection<Map<String, String>> result = routeImporter.exportRoute(r);
			if (r.getName().equals(DIRECT_NAME)) {
				output.put(DISTANCES_KEY, result);
			} else if (r.getName().equals(SUEZ_CANAL_NAME)) {
				output.put(SUEZ_KEY, result);
			} else {
				inputs.put(r.getName(), r.getName());
				output.put(r.getName(), result);
			}
		}
		output.put(PORT_KEY, portImporter.exportObjects(((PortModel) model).getPorts(), root));
		output.put(PORT_GROUP_KEY, portGroupImporter.exportObjects(((PortModel) model).getPortGroups(), root));
	}

	@Override
	public EClass getEClass() {
		return PortPackage.eINSTANCE.getPortModel();
	}
}

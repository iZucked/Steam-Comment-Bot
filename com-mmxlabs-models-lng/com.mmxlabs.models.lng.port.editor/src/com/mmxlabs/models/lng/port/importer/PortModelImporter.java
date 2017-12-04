/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.importer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.port.CapabilityGroup;
import com.mmxlabs.models.lng.port.EntryPoint;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortCountryGroup;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.util.Activator;
import com.mmxlabs.models.util.importer.IClassImporter;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;
import com.mmxlabs.models.util.importer.ISubmodelImporter;
import com.mmxlabs.models.util.importer.impl.DefaultClassImporter;
import com.mmxlabs.models.util.importer.registry.IImporterRegistry;

/**
 */
public class PortModelImporter implements ISubmodelImporter {
	/**
	 * 
	 */
	private static final String SUEZ_CANAL_NAME = "Suez canal";
	private static final String PANAMA_CANAL_NAME = "Panama canal";
	/**
	 * 
	 */
	private static final String DIRECT_NAME = "Direct";
	public static final String PORT_KEY = "PORT";
	public static final String PORT_GROUP_KEY = "PORTGROUP";
	public static final String DISTANCES_KEY = "DISTANCES";
	public static final String SUEZ_KEY = "SUEZ";
	public static final String PANAMA_KEY = "PANAMA";
	public static final String CANAL_PORTS_KEY = "CANALPORTS";
	public static final HashMap<String, String> inputs = new LinkedHashMap<String, String>();

	static {
		inputs.put(PORT_KEY, "Ports");
		inputs.put(PORT_GROUP_KEY, "Port Groups");
		inputs.put(DISTANCES_KEY, "Distance Matrix");
		inputs.put(SUEZ_KEY, "Suez Distance Matrix");
		inputs.put(CANAL_PORTS_KEY, "Canal Ports");
		if (LicenseFeatures.isPermitted("features:panama-canal")) {
			inputs.put(PANAMA_KEY, "Panama Distance Matrix");
		}
	}

	@Inject
	private IImporterRegistry importerRegistry;

	private IClassImporter portImporter;
	private IClassImporter portGroupImporter;
	private final RouteImporter routeImporter = new RouteImporter();

	private final DefaultClassImporter canalPortsImporter = new DefaultClassImporter() {

		protected boolean shouldImportReference(final org.eclipse.emf.ecore.EReference reference) {
			return shouldExportFeature(reference);
		};

		protected boolean shouldExportFeature(final org.eclipse.emf.ecore.EStructuralFeature feature) {
			if (feature == PortPackage.Literals.ROUTE__ROUTE_OPTION) {
				return false;
			}
			if (feature == PortPackage.Literals.ROUTE__LINES) {
				return false;
			}			
			return super.shouldExportFeature(feature);
		}
	};

	/**
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
	public UUIDObject importModel(final Map<String, CSVReader> inputs, final IMMXImportContext context) {
		final PortModel result = PortFactory.eINSTANCE.createPortModel();

		final PortModel portModel = result;
		final @NonNull List<Route> importedRoutes = new ArrayList<Route>();

		if (inputs.containsKey(PORT_KEY)) {
			final CSVReader reader = inputs.get(PORT_KEY);
			result.getPorts().addAll((Collection<? extends Port>) portImporter.importObjects(PortPackage.eINSTANCE.getPort(), reader, context));

			// Register country groups before port groups for backward compatibility - untyped PortGroup names will replace newer country group names
			// Create country groups
			final Set<String> countries = portModel.getPorts().stream() //
					.map(Port::getLocation) //
					.filter(l -> l != null) //
					.map(Location::getCountry).collect(Collectors.toSet());

			final Set<String> groupNames = portModel.getPortCountryGroups().stream() //
					.map(PortCountryGroup::getName)//
					.collect(Collectors.toSet());

			countries.removeAll(groupNames);

			for (final String country : countries) {
				if (country == null || country.isEmpty()) {
					continue;
				}
				final PortCountryGroup g = PortFactory.eINSTANCE.createPortCountryGroup();
				g.setName(country);
				portModel.getPortCountryGroups().add(g);
				context.registerNamedObject(g);
			}

			// Set a default MMX ID based on port name
			for (final Port port : portModel.getPorts()) {
				Location location = port.getLocation();
				if (location == null) {
					location = PortFactory.eINSTANCE.createLocation();
					port.setLocation(location);
				}
//				if (location.getMmxId() == null || location.getMmxId().isEmpty()) {
//					location.setMmxId(port.getName());
//				}
			}
		}
		if (inputs.containsKey(PORT_GROUP_KEY)) {
			final CSVReader reader = inputs.get(PORT_GROUP_KEY);
			result.getPortGroups().addAll((Collection<? extends PortGroup>) portGroupImporter.importObjects(PortPackage.eINSTANCE.getPortGroup(), reader, context));
		}

		// Needs to be called before the canals are programmatically created because the DefaultClassImporter calls
		// context.registerNamedObject((NamedObject) o);
		if (inputs.containsKey(CANAL_PORTS_KEY)) {
			importedRoutes.addAll(canalPortsImporter.importObjects(PortPackage.Literals.ROUTE, inputs.get(CANAL_PORTS_KEY), context).stream().map(e -> (Route) e).collect(Collectors.toList()));
		}

		if (inputs.containsKey(DISTANCES_KEY)) {
			final Route direct = routeImporter.importRoute(inputs.get(DISTANCES_KEY), context);
			if (direct != null) {
				direct.setName(DIRECT_NAME);
				direct.setRouteOption(RouteOption.DIRECT);

				result.getRoutes().add(direct);
				context.registerNamedObject(direct);
			}
		}
		if (inputs.containsKey(SUEZ_KEY)) {
			final Route suez = routeImporter.importRoute(inputs.get(SUEZ_KEY), context);
			if (suez != null) {
				suez.setName(SUEZ_CANAL_NAME);
				suez.setRouteOption(RouteOption.SUEZ);

				result.getRoutes().add(suez);
				context.registerNamedObject(suez);
			}
		}
		if (LicenseFeatures.isPermitted("features:panama-canal")) {
			if (inputs.containsKey(PANAMA_KEY)) {
				final Route panama = routeImporter.importRoute(inputs.get(PANAMA_KEY), context);
				if (panama != null) {
					panama.setName(PANAMA_CANAL_NAME);
					panama.setRouteOption(RouteOption.PANAMA);

					result.getRoutes().add(panama);
					context.registerNamedObject(panama);
				}
			}
		}

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

			if (inputs.containsKey(CANAL_PORTS_KEY)) {
				result.getRoutes().forEach(route -> {
					final Optional<Route> potentialImported = importedRoutes.stream().filter(e -> e.getName().equals(route.getName())).findFirst();
					if (potentialImported.isPresent()) {
						final Route tmpRoute = potentialImported.get();
						if (!tmpRoute.getName().toLowerCase().contains("direct")) {

							// Move data to real route
							route.setNorthEntrance(tmpRoute.getNorthEntrance());
							route.setSouthEntrance(tmpRoute.getSouthEntrance());

							if (route.getNorthEntrance() != null) {
								context.registerNamedObject(route.getNorthEntrance());
							}
							if (route.getSouthEntrance() != null) {
								context.registerNamedObject(route.getSouthEntrance());
							}
						}
					}
				});	
			}

			for (final Route route : portModel.getRoutes()) {
				if (route.getRouteOption() == RouteOption.PANAMA) {
					if (route.getNorthEntrance() == null) {
						final EntryPoint entryPoint = PortFactory.eINSTANCE.createEntryPoint();
						entryPoint.setName("Panama Northside");
						route.setNorthEntrance(entryPoint);
					}
					if (route.getSouthEntrance() == null) {
						final EntryPoint entryPoint = PortFactory.eINSTANCE.createEntryPoint();
						entryPoint.setName("Panama Southside");
						route.setSouthEntrance(entryPoint);
					}
				}
				if (route.getRouteOption() == RouteOption.SUEZ) {
					if (route.getNorthEntrance() == null) {
						final EntryPoint entryPoint = PortFactory.eINSTANCE.createEntryPoint();
						entryPoint.setName("Suez Northside");
						route.setNorthEntrance(entryPoint);
					}
					if (route.getSouthEntrance() == null) {
						final EntryPoint entryPoint = PortFactory.eINSTANCE.createEntryPoint();
						entryPoint.setName("Suez Southside");
						route.setSouthEntrance(entryPoint);
					}
				}
			}
		}
		return result;
	}

	@Override
	public void exportModel(final EObject model, final Map<String, Collection<Map<String, String>>> output, final IMMXExportContext context) {
		for (final Route r : ((PortModel) model).getRoutes()) {
			final Collection<Map<String, String>> result = routeImporter.exportRoute(r, context);
			if (r.getRouteOption() == RouteOption.DIRECT) {
				output.put(DISTANCES_KEY, result);
			} else if (r.getRouteOption() == RouteOption.SUEZ) {
				output.put(SUEZ_KEY, result);
			} else if (r.getRouteOption() == RouteOption.PANAMA) {
				output.put(PANAMA_KEY, result);
			} else {
				inputs.put(r.getName(), r.getName());
				output.put(r.getName(), result);
			}
		}
		output.put(PORT_KEY, portImporter.exportObjects(((PortModel) model).getPorts(), context));
		output.put(PORT_GROUP_KEY, portGroupImporter.exportObjects(((PortModel) model).getPortGroups(), context));
		output.put(CANAL_PORTS_KEY, canalPortsImporter.exportObjects(((PortModel) model).getRoutes(), context));
	}

	@Override
	public EClass getEClass() {
		return PortPackage.eINSTANCE.getPortModel();
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.editor.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.BaseModelCommandProvider;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * Command provider which handles the addition and deletion of routes and vessel classes, and maintains the route cost table accordingly.
 * 
 * @author hinton
 * 
 */
public class RouteParametersCommandProvider extends BaseModelCommandProvider<Object> {
	@Override
	protected boolean shouldHandleAddition(final Object addedObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {
		return addedObject instanceof Route || addedObject instanceof VesselClass;
	}

	@Override
	protected boolean shouldHandleDeletion(final Object deletedObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {
		return shouldHandleAddition(deletedObject, overrides, editSet);
	}

	private VesselClassRouteParameters createRouteParameters(final Route route, final VesselClass vesselClass) {
		final VesselClassRouteParameters result = FleetFactory.eINSTANCE.createVesselClassRouteParameters();
		result.setRoute(route);
		return result;
	}

	@Override
	protected Command objectAdded(final EditingDomain domain, final MMXRootObject rootObject, final Object addedObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {

		if (!(rootObject instanceof LNGScenarioModel)) {
			return null;
		}
		final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;

		final CompoundCommand cmd = new CompoundCommand();

		final Set<Object> objects = getAddedObjects();
		objects.add(addedObject);

		if (addedObject instanceof Route) {
			final Route route = (Route) addedObject;
			if (route.getRouteOption() == RouteOption.DIRECT) {
				return null;
			}

			final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(scenarioModel);
			add_data_for_new_route: for (final VesselClass vesselClass : fleetModel.getVesselClasses()) {
				for (final VesselClassRouteParameters routeCost : vesselClass.getRouteParameters()) {
					if (routeCost.getRoute() == addedObject) {
						continue add_data_for_new_route;
					}
				}
				cmd.append(AddCommand.create(domain, vesselClass, FleetPackage.eINSTANCE.getVesselClass_RouteParameters(), createRouteParameters(route, vesselClass)));
			}
			add_data_for_new_route: for (final Object obj : objects) {
				if (obj instanceof VesselClass) {
					final VesselClass vesselClass = (VesselClass) obj;
					for (final VesselClassRouteParameters routeCost : vesselClass.getRouteParameters()) {
						if (routeCost.getRoute() == addedObject) {
							continue add_data_for_new_route;
						}
					}
					cmd.append(AddCommand.create(domain, vesselClass, FleetPackage.eINSTANCE.getVesselClass_RouteParameters(), createRouteParameters(route, vesselClass)));
				}
			}

		} else if (addedObject instanceof VesselClass) {
			final VesselClass vesselClass = (VesselClass) addedObject;

			final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);
			add_costs_for_new_vc: for (final Route route : portModel.getRoutes()) {
				for (final VesselClassRouteParameters routeCost : vesselClass.getRouteParameters()) {
					if (routeCost.getRoute() == route) {
						continue add_costs_for_new_vc;
					}
				}
				if (route.getRouteOption() != RouteOption.DIRECT) {
					cmd.append(AddCommand.create(domain, vesselClass, FleetPackage.eINSTANCE.getVesselClass_RouteParameters(), createRouteParameters(route, vesselClass)));
				}
			}

			// CHCEK
			add_costs_for_new_vc: for (final Object obj : objects) {
				if (obj instanceof Route) {
					final Route route = (Route) obj;
					for (final VesselClassRouteParameters routeCost : vesselClass.getRouteParameters()) {
						if (routeCost.getRoute() == route) {
							continue add_costs_for_new_vc;
						}
					}
					if (route.getRouteOption() != RouteOption.DIRECT) {
						cmd.append(AddCommand.create(domain, vesselClass, FleetPackage.eINSTANCE.getVesselClass_RouteParameters(), createRouteParameters(route, vesselClass)));
					}
				}
			}

		}
		if (cmd.isEmpty()) {
			return null;
		} else {
			return cmd.unwrap();
		}
	}

	@Override
	protected Command objectDeleted(final EditingDomain domain, final MMXRootObject rootObject, final Object deletedObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {
		if (!(rootObject instanceof LNGScenarioModel)) {
			return null;
		}
		final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;

		final List<VesselClassRouteParameters> deletedObjects = new ArrayList<VesselClassRouteParameters>();

		if (deletedObject instanceof Route) {
			final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(scenarioModel);
			for (final VesselClass vesselClass : fleetModel.getVesselClasses()) {
				for (final VesselClassRouteParameters params : vesselClass.getRouteParameters()) {
					if (params.getRoute() == deletedObject) {
						deletedObjects.add(params);

					}
				}
			}
		}

		if (deletedObjects.isEmpty())
			return null;
		else
			return DeleteCommand.create(domain, deletedObjects);
	}

	private Set<Object> getAddedObjects() {
		final Object obj = getContext();
		if (obj instanceof Set) {
			return (Set<Object>) obj;
		}
		final Set<Object> objects = new HashSet<Object>();
		setContext(objects);
		return objects;
	}
}

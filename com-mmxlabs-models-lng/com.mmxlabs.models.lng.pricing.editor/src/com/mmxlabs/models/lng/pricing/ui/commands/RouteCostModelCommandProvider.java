/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.BaseModelCommandProvider;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * Command provider which handles the addition and deletion of routes and vessel classes, and maintains the route cost table accordingly.
 * 
 * @author hinton
 * 
 */
public class RouteCostModelCommandProvider extends BaseModelCommandProvider<Object> {
	@Override
	protected boolean shouldHandleAddition(final Object addedObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {
		return addedObject instanceof Route || addedObject instanceof VesselClass;
	}

	@Override
	protected boolean shouldHandleDeletion(final Object deletedObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {
		return shouldHandleAddition(deletedObject, overrides, editSet);
	}

	private RouteCost createRouteCost(final Route route, final VesselClass vesselClass) {
		final RouteCost result = PricingFactory.eINSTANCE.createRouteCost();
		result.setRoute(route);
		result.setVesselClass(vesselClass);
		return result;
	}

	@Override
	protected Command objectAdded(final EditingDomain domain, final MMXRootObject rootObject, final Object addedObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {

		if (!(rootObject instanceof LNGScenarioModel)) {
			return null;
		}
		final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;

		final CostModel costModel = ScenarioModelUtil.getCostModel(scenarioModel);

		final Set<Object> objects = getAddedObjects();
		objects.add(addedObject);

		final List<RouteCost> extraCosts = new ArrayList<RouteCost>();
		if (addedObject instanceof Route) {
			final Route route = (Route) addedObject;
			// Only maintain Suez Canal Costs
			if (route.getRouteOption() != RouteOption.SUEZ) {
				return null;
			}
			final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(scenarioModel);
			add_costs_for_new_route: for (final VesselClass vesselClass : fleetModel.getVesselClasses()) {
				for (final RouteCost routeCost : costModel.getRouteCosts()) {
					if (routeCost.getVesselClass() == vesselClass && routeCost.getRoute() == addedObject)
						continue add_costs_for_new_route;
				}
				extraCosts.add(createRouteCost((Route) addedObject, vesselClass));
			}
			// CHCEK
			add_costs_for_new_route: for (final Object obj : objects) {
				if (obj instanceof VesselClass) {
					final VesselClass vesselClass = (VesselClass) obj;
					for (final RouteCost routeCost : costModel.getRouteCosts()) {
						if (routeCost.getVesselClass() == vesselClass && routeCost.getRoute() == addedObject)
							continue add_costs_for_new_route;
					}
					extraCosts.add(createRouteCost((Route) addedObject, vesselClass));
				}
			}

		} else if (addedObject instanceof VesselClass) {
			final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);
			add_costs_for_new_vc: for (final Route route : portModel.getRoutes()) {
				for (final RouteCost routeCost : costModel.getRouteCosts()) {
					if (routeCost.getVesselClass() == addedObject && routeCost.getRoute() == route)
						continue add_costs_for_new_vc;
				}
				if (route.getRouteOption() == RouteOption.SUEZ) {
					extraCosts.add(createRouteCost(route, (VesselClass) addedObject));
				}
			}

			// CHCEK
			add_costs_for_new_vc: for (final Object obj : objects) {
				if (obj instanceof Route) {
					final Route route = (Route) obj;
					for (final RouteCost routeCost : costModel.getRouteCosts()) {
						if (routeCost.getVesselClass() == addedObject && routeCost.getRoute() == route)
							continue add_costs_for_new_vc;
					}
					if (route.getRouteOption() == RouteOption.SUEZ) {
						extraCosts.add(createRouteCost(route, (VesselClass) addedObject));
					}
				}
			}

		}
		if (extraCosts.isEmpty()) {
			return null;
		} else {
			return AddCommand.create(domain, costModel, PricingPackage.eINSTANCE.getCostModel_RouteCosts(), extraCosts);
		}
	}

	@Override
	protected Command objectDeleted(final EditingDomain domain, final MMXRootObject rootObject, final Object deletedObject, final Map<EObject, EObject> overrides, final Set<EObject> editSet) {
		if (!(rootObject instanceof LNGScenarioModel)) {
			return null;
		}
		final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;

		final CostModel costModel = ScenarioModelUtil.getCostModel(scenarioModel);

		final List<RouteCost> deletedCosts = new ArrayList<RouteCost>();

		for (final RouteCost cost : costModel.getRouteCosts()) {
			if (cost.getRoute() == deletedObject || cost.getVesselClass() == deletedObject) {
				deletedCosts.add(cost);
			}
		}

		if (deletedCosts.isEmpty())
			return null;
		else
			return DeleteCommand.create(domain, deletedCosts);
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

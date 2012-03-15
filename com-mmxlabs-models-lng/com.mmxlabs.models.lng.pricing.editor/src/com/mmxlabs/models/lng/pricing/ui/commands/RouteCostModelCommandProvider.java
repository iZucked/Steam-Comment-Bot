/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.commandservice.BaseModelCommandProvider;

/**
 * Command provider which handles the addition and deletion of routes and vessel classes,
 * and maintains the route cost table accordingly.
 * 
 * @author hinton
 *
 */
public class RouteCostModelCommandProvider extends BaseModelCommandProvider {
	@Override
	protected boolean shouldHandleAddition(Object addedObject) {
		return addedObject instanceof Route || addedObject instanceof VesselClass;
	}

	@Override
	protected boolean shouldHandleDeletion(Object deletedObject) {
		return shouldHandleAddition(deletedObject);
	}

	private RouteCost createRouteCost(final Route route, final VesselClass vesselClass) {
		final RouteCost result = PricingFactory.eINSTANCE.createRouteCost();
		result.setRoute(route);
		result.setVesselClass(vesselClass);
		return result;
	}
	
	@Override
	protected Command objectAdded(EditingDomain domain, MMXRootObject root, Object addedObject) {
		final PricingModel pricing = root.getSubModel(PricingModel.class);
		if (pricing == null) return null;
		final List<RouteCost> extraCosts = new ArrayList<RouteCost>();
		if (addedObject instanceof Route) {
			if (((Route) addedObject).isCanal() == false) return null;
			final FleetModel fleetModel = root.getSubModel(FleetModel.class);
			add_costs_for_new_route:
			for (final VesselClass vesselClass : fleetModel.getVesselClasses()) {
				for (final RouteCost routeCost : pricing.getRouteCosts()) {
					if (routeCost.getVesselClass() == vesselClass && routeCost.getRoute() == addedObject)
						continue add_costs_for_new_route;
				}
				extraCosts.add(createRouteCost((Route) addedObject, vesselClass));
			}
		} else if (addedObject instanceof VesselClass) {
			final PortModel portModel = root.getSubModel(PortModel.class);
			add_costs_for_new_vc:
			for (final Route route : portModel.getRoutes()) {
				for (final RouteCost routeCost : pricing.getRouteCosts()) {
					if (routeCost.getVesselClass() == addedObject && routeCost.getRoute() == route)
						continue add_costs_for_new_vc;
				}
				if (route.isCanal()) extraCosts.add(createRouteCost(route, (VesselClass) addedObject));
			}
		}
		if (extraCosts.isEmpty()) {
			return null;
		} else {
			return AddCommand.create(domain, pricing, PricingPackage.eINSTANCE.getPricingModel_RouteCosts(),extraCosts);
		}
	}

	@Override
	protected Command objectDeleted(EditingDomain domain, MMXRootObject root, Object deletedObject) {
		final PricingModel pricing = root.getSubModel(PricingModel.class);
		if (pricing == null)
			return null;
		final List<RouteCost> deletedCosts = new ArrayList<RouteCost>();

		for (final RouteCost cost : pricing.getRouteCosts()) {
			if (cost.getRoute() == deletedObject || cost.getVesselClass() == deletedObject) {
				deletedCosts.add(cost);
			}
		}

		if (deletedCosts.isEmpty())
			return null;
		else
			return DeleteCommand.create(domain, deletedCosts);
	}
	
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.editorpart;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.NonEditableColumn;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;

/**
 * Quick hack for vessel route cost editing
 * 
 * @author hinton
 * 
 */
public class CanalCostsPane extends ScenarioTableViewerPane {
	public CanalCostsPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final CommandStack commandStack) {
		super.init(path, adapterFactory, commandStack);

		final EditingDomain ed = getEditingDomain();

		final NonEditableColumn vesselManipulator = new NonEditableColumn() {
			@Override
			public String render(final Object object) {
				if (object instanceof RouteCost) {
					final VesselClass vesselClass = ((RouteCost) object).getVesselClass();
					if (vesselClass != null) {
						return vesselClass.getName();
					}
				}
				return "Unknown";
			}

		};

		final NonEditableColumn routeManipulator = new NonEditableColumn() {
			@Override
			public String render(final Object object) {
				if (object instanceof RouteCost) {
					final Route route = ((RouteCost) object).getRoute();
					if (route != null) {
						return route.getName();
					}
				}
				return "Unknown";
			}

		};

		addTypicalColumn("Route", routeManipulator);
		addTypicalColumn("Vessel Class", vesselManipulator);
		addTypicalColumn("Laden Toll", new NumericAttributeManipulator(PricingPackage.Literals.ROUTE_COST__LADEN_COST, ed));
		addTypicalColumn("Ballast Toll", new NumericAttributeManipulator(PricingPackage.Literals.ROUTE_COST__BALLAST_COST, ed));

		defaultSetTitle("Canal Costs");

		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.lingo.doc.Editor_CanalCosts");

	}

	@Override
	protected Action createDeleteAction(@Nullable final Function<Collection<?>, Collection<Object>> callback) {
		return null;
	}
}

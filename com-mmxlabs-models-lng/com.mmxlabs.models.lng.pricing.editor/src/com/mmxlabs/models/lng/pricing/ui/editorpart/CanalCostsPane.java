/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.NonEditableColumn;
import com.mmxlabs.models.ui.tabular.NumericAttributeManipulator;

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
					return ((RouteCost) object).getVesselClass().getName();
				}
				return "Unknown";
			}

		};
		
		final NonEditableColumn routeManipulator = new NonEditableColumn() {
			@Override
			public String render(final Object object) {
				if (object instanceof RouteCost) {
					return ((RouteCost) object).getRoute().getName();
				}
				return "Unknown";
			}

		};
		
		addTypicalColumn("Route", routeManipulator);
		addTypicalColumn("Vessel Class", vesselManipulator);
		addTypicalColumn("Laden Toll", new NumericAttributeManipulator(PricingPackage.Literals.ROUTE_COST__LADEN_COST, ed));
		addTypicalColumn("Ballast Toll", new NumericAttributeManipulator(PricingPackage.Literals.ROUTE_COST__BALLAST_COST, ed));

		defaultSetTitle("Canal Costs");
	}

	@Override
	protected Action createDeleteAction() {
		return null;
	}
}

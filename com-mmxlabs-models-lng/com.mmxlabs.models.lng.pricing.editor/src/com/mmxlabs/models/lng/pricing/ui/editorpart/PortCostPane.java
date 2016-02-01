/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.editorpart;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.nebula.widgets.formattedtext.FormattedTextCellEditor;
import org.eclipse.nebula.widgets.formattedtext.IntegerFormatter;
import org.eclipse.nebula.widgets.formattedtext.NumberFormatter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PortCostEntry;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.manipulators.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;

/**
 * Pricing model pane.
 * 
 * @author hinton
 * 
 */
public class PortCostPane extends ScenarioTableViewerPane {
	public PortCostPane(IWorkbenchPage page, IWorkbenchPart part, IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final CommandStack commandStack) {
		super.init(path, adapterFactory, commandStack);

		addTypicalColumn("Ports",
				new MultipleReferenceManipulator(PricingPackage.eINSTANCE.getPortCost_Ports(), getReferenceValueProviderCache(), getEditingDomain(), MMXCorePackage.eINSTANCE.getNamedObject_Name()));

		addTypicalColumn("Reference Capacity", new NumericAttributeManipulator(PricingPackage.eINSTANCE.getPortCost_ReferenceCapacity(), getEditingDomain()));

		addTypicalColumn("Loading Fee", new PortCostManipulator(PortCapability.LOAD));
		addTypicalColumn("Discharging Fee", new PortCostManipulator(PortCapability.DISCHARGE));

		defaultSetTitle("Port Costs");

		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.lingo.doc.Editor_PortCosts");
	}

	private class PortCostManipulator implements ICellRenderer, ICellManipulator {
		private PortCapability activity;

		public PortCostManipulator(final PortCapability activity) {
			this.activity = activity;
		}

		@Override
		public void setValue(Object object, Object value) {
			final PortCost def = (PortCost) object;

			for (final PortCostEntry entry : def.getEntries()) {
				if (entry.getActivity() == activity) {
					getEditingDomain().getCommandStack().execute(SetCommand.create(getEditingDomain(), entry, PricingPackage.eINSTANCE.getPortCostEntry_Cost(), value));
					return;
				}
			}

			final PortCostEntry addedEntry = PricingFactory.eINSTANCE.createPortCostEntry();

			addedEntry.setActivity(activity);
			addedEntry.setCost((Integer) value);

			getEditingDomain().getCommandStack().execute(AddCommand.create(getEditingDomain(), def, PricingPackage.eINSTANCE.getPortCost_Entries(), addedEntry));
		}

		@Override
		public CellEditor getCellEditor(Composite parent, Object object) {
			final FormattedTextCellEditor result = new FormattedTextCellEditor(parent);
			final NumberFormatter formatter = new IntegerFormatter();

			formatter.setFixedLengths(false, false);

			result.setFormatter(formatter);
			return result;
		}

		@Override
		public Object getValue(Object object) {
			final PortCost def = (PortCost) object;

			for (final PortCostEntry entry : def.getEntries()) {
				if (entry.getActivity() == activity)
					return entry.getCost();
			}

			return 0;
		}

		@Override
		public boolean canEdit(Object object) {
			return true;
		}

		@Override
		public boolean isValueUnset(Object object) {
			return false;
		}
		
		@Override
		public String render(Object object) {
			return String.format("$%,d", getValue(object));
		}

		@Override
		public Comparable<?> getComparable(Object object) {
			return render(object);
		}

		@Override
		public Object getFilterValue(Object object) {
			return getComparable(object);
		}

		@Override
		public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(Object object) {
			return Collections.emptyList();
		}

	}
}

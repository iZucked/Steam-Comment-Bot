/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.parameters.ui.editorpart;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.parameters.ParametersModel;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;

/**
 * Simplistic editor for optimiser settings.
 * 
 * @author hinton
 * 
 */
public class OptimiserSettingsEditorPane extends ScenarioTableViewerPane {
	public OptimiserSettingsEditorPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane#init(java.util.List, org.eclipse.emf.common.notify.AdapterFactory)
	 */
	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final CommandStack commandStack) {
		super.init(path, adapterFactory, commandStack);
		addNameManipulator("Name");

		addTypicalColumn("Seed", new NumericAttributeManipulator(ParametersPackage.eINSTANCE.getOptimiserSettings_Seed(), getEditingDomain()));

		addTypicalColumn("Iterations", new NumericAttributeManipulator(ParametersPackage.eINSTANCE.getAnnealingSettings_Iterations(), getEditingDomain()),
				ParametersPackage.eINSTANCE.getOptimiserSettings_AnnealingSettings());

		addTypicalColumn("Epoch Length", new NumericAttributeManipulator(ParametersPackage.eINSTANCE.getAnnealingSettings_EpochLength(), getEditingDomain()),
				ParametersPackage.eINSTANCE.getOptimiserSettings_AnnealingSettings());

		addTypicalColumn("Cooling Parameter", new NumericAttributeManipulator(ParametersPackage.eINSTANCE.getAnnealingSettings_Cooling(), getEditingDomain()),
				ParametersPackage.eINSTANCE.getOptimiserSettings_AnnealingSettings());

		addTypicalColumn("Initial Temperature", new NumericAttributeManipulator(ParametersPackage.eINSTANCE.getAnnealingSettings_InitialTemperature(), getEditingDomain()),
				ParametersPackage.eINSTANCE.getOptimiserSettings_AnnealingSettings());

		addTypicalColumn("Is Default", new SelectedSettingsManipulator());

		defaultSetTitle("Optimiser Settings");
	}

	class SelectedSettingsManipulator implements ICellRenderer, ICellManipulator {
		@Override
		public void setValue(final Object object, final Object value) {
			if (object instanceof OptimiserSettings) {
				final OptimiserSettings settings = (OptimiserSettings) object;
				if (settings.eContainer() instanceof ParametersModel) {
					final ParametersModel om = (ParametersModel) settings.eContainer();
					getEditingDomain().getCommandStack().execute(SetCommand.create(getEditingDomain(), om, ParametersPackage.eINSTANCE.getParametersModel_ActiveSetting(), settings));
				}
			}
		}

		@Override
		public CellEditor getCellEditor(final Composite parent, final Object object) {
			return new ComboBoxCellEditor(parent, new String[] { "Y", "N" });
		}

		@Override
		public Object getValue(final Object object) {
			if (object instanceof OptimiserSettings) {
				final OptimiserSettings settings = (OptimiserSettings) object;
				final EObject eContainer = settings.eContainer();
				if (eContainer instanceof ParametersModel) {
					if (((ParametersModel) eContainer).getActiveSetting() == settings) {
						return 0;
					}
				}
			}
			return 1;
		}
		
		@Override
		public boolean isValueUnset(Object object) {
			return false;
		}

		@Override
		public boolean canEdit(final Object object) {
			return true;
		}

		@Override
		public String render(final Object object) {
			return ((Integer) getValue(object)) == 0 ? "Y" : "N";
		}

		@Override
		public Comparable getComparable(final Object object) {
			return render(object);
		}

		@Override
		public Object getFilterValue(final Object object) {
			return render(object);
		}

		@Override
		public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(final Object object) {
			return Collections.emptySet();
		}
	}
}

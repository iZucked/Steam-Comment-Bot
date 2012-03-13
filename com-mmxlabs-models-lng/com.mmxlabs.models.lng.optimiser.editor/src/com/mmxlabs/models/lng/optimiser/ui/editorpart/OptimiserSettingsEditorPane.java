/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.optimiser.ui.editorpart;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.optimiser.OptimiserModel;
import com.mmxlabs.models.lng.optimiser.OptimiserPackage;
import com.mmxlabs.models.lng.optimiser.OptimiserSettings;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.ValueListAttributeManipulator;

/**
 * Simplistic editor for optimiser settings.
 * 
 * @author hinton
 *
 */
public class OptimiserSettingsEditorPane extends ScenarioTableViewerPane {
	public OptimiserSettingsEditorPane(IWorkbenchPage page, JointModelEditorPart part) {
		super(page, part);
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane#init(java.util.List, org.eclipse.emf.common.notify.AdapterFactory)
	 */
	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		addNameManipulator("Name");

		addTypicalColumn("Seed", 
				new NumericAttributeManipulator(OptimiserPackage.eINSTANCE.getOptimiserSettings_Seed(),
						getEditingDomain()));
		
		addTypicalColumn("Iterations", 
				new NumericAttributeManipulator(
						OptimiserPackage.eINSTANCE.getAnnealingSettings_Iterations()
						,
						getEditingDomain()), OptimiserPackage.eINSTANCE.getOptimiserSettings_AnnealingSettings());
		
		addTypicalColumn("Epoch Length", 
				new NumericAttributeManipulator(
						OptimiserPackage.eINSTANCE.getAnnealingSettings_EpochLength()
						,
						getEditingDomain()), OptimiserPackage.eINSTANCE.getOptimiserSettings_AnnealingSettings());
		
		addTypicalColumn("Cooling Parameter", 
				new NumericAttributeManipulator(
						OptimiserPackage.eINSTANCE.getAnnealingSettings_Cooling()
						,
						getEditingDomain()), OptimiserPackage.eINSTANCE.getOptimiserSettings_AnnealingSettings());
		
		addTypicalColumn("Initial Temperature", 
				new NumericAttributeManipulator(
						OptimiserPackage.eINSTANCE.getAnnealingSettings_InitialTemperature()
						,
						getEditingDomain()), OptimiserPackage.eINSTANCE.getOptimiserSettings_AnnealingSettings());
		
		addTypicalColumn("Is Default", new SelectedSettingsManipulator());
		
		defaultSetTitle("Optimiser Settings");
	}
	
	class SelectedSettingsManipulator implements ICellRenderer, ICellManipulator {
		@Override
		public void setValue(Object object, Object value) {
			if (object instanceof OptimiserSettings) {
				final OptimiserSettings settings = (OptimiserSettings) object;
				if (settings.eContainer() instanceof OptimiserModel) {
					final OptimiserModel om = (OptimiserModel) settings.eContainer();
					om.setActiveSetting(settings);
				}
			}
		}

		@Override
		public CellEditor getCellEditor(Composite parent, Object object) {
			return new ComboBoxCellEditor(parent, new String[] { "Y", "N" });
		}

		@Override
		public Object getValue(Object object) {
			if (object instanceof OptimiserSettings) {
				final OptimiserSettings settings = (OptimiserSettings) object;
				if (((OptimiserModel) settings.eContainer()).getActiveSetting() == settings)
					return 0;
			}
			return 1;
		}

		@Override
		public boolean canEdit(Object object) {
			return true;
		}

		@Override
		public String render(Object object) {
			return ((Integer) getValue(object)) == 0 ? "Y" : "N";
		}

		@Override
		public Comparable getComparable(Object object) {
			return render(object);
		}

		@Override
		public Object getFilterValue(Object object) {
			return render(object);
		}

		@Override
		public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(Object object) {
			return Collections.emptySet();
		}
	}
}

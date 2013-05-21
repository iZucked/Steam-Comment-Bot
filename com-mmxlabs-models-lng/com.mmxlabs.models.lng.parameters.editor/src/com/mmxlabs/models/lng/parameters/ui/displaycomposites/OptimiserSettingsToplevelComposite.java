/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.parameters.ui.displaycomposites;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.mmxlabs.models.lng.parameters.Constraint;
import com.mmxlabs.models.lng.parameters.Objective;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.parameters.presentation.ParametersEditorPlugin;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;

public class OptimiserSettingsToplevelComposite extends DefaultTopLevelComposite {
	public OptimiserSettingsToplevelComposite(final Composite parent, final int style, final IScenarioEditingLocation location) {
		super(parent, style, location);
	}

	@Override
	public void display(final IScenarioEditingLocation location, final MMXRootObject root, final EObject object, final Collection<EObject> range) {
		super.display(location, root, object, range);
		// display sub-things
		final Composite lower = new Composite(this, SWT.NONE);
		final GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalSpan = 3;
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.FILL;

		lower.setLayoutData(gd);
		final GridLayout gl = new GridLayout(2, true);
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		lower.setLayout(gl);

		final Group constraints = new Group(lower, SWT.NONE);
		constraints.setText("Constraints");

		constraints.setLayoutData(new GridData(GridData.FILL_BOTH));

		final Group objectives = new Group(lower, SWT.NONE);
		objectives.setText("Objectives");

		objectives.setLayoutData(new GridData(GridData.FILL_BOTH));

		final IConstraintCheckerRegistry ccr = ParametersEditorPlugin.getPlugin().getConstraintCheckerRegistry();
		final IFitnessFunctionRegistry ffr = ParametersEditorPlugin.getPlugin().getFitnessFunctionRegistry();

		constraints.setLayout(new GridLayout(1, true));
		objectives.setLayout(new GridLayout(1, true));

		final CheckboxTableViewer ccrv = CheckboxTableViewer.newCheckList(constraints, SWT.BORDER);

		ccrv.setContentProvider(new IStructuredContentProvider() {
			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

			}

			@Override
			public void dispose() {

			}

			@Override
			public Object[] getElements(final Object inputElement) {
				return ((IConstraintCheckerRegistry) inputElement).getConstraintCheckerNames().toArray();
			}
		});

		ccrv.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(final Object element) {
				return EditorUtils.unmangle("" + element);
			}
		});

		final CheckboxTableViewer ffrv = CheckboxTableViewer.newCheckList(objectives, SWT.BORDER);

		ffrv.setContentProvider(new IStructuredContentProvider() {

			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

			}

			@Override
			public void dispose() {

			}

			@Override
			public Object[] getElements(final Object inputElement) {
				return ((IFitnessFunctionRegistry) inputElement).getFitnessComponentNames().toArray();
			}
		});

		ffrv.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(final Object element) {
				return EditorUtils.unmangle("" + element).replace("-", " ");
			}
		});

		ffrv.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(final CheckStateChangedEvent event) {
				final String name = (String) event.getElement();
				final OptimiserSettings settings = (OptimiserSettings) object;
				for (final Objective o : settings.getObjectives()) {
					if (o.getName().equals(name)) {
						final Command sc = SetCommand.create(getCommandHandler().getEditingDomain(), o, ParametersPackage.eINSTANCE.getObjective_Enabled(), event.getChecked());
						getCommandHandler().handleCommand(sc, o, ParametersPackage.eINSTANCE.getObjective_Enabled());
						return;
					}
				}

				final Objective o = ParametersFactory.eINSTANCE.createObjective();
				o.setName(name);
				o.setWeight(1.0);
				o.setEnabled(event.getChecked());

				final Command ac = AddCommand.create(getCommandHandler().getEditingDomain(), object, ParametersPackage.eINSTANCE.getOptimiserSettings_Objectives(), o);
				getCommandHandler().handleCommand(ac, object, ParametersPackage.eINSTANCE.getOptimiserSettings_Objectives());
			}
		});

		ccrv.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(final CheckStateChangedEvent event) {
				final String name = (String) event.getElement();
				final OptimiserSettings settings = (OptimiserSettings) object;
				for (final Constraint c : settings.getConstraints()) {
					if (c.getName().equals(name)) {
						final Command sc = SetCommand.create(getCommandHandler().getEditingDomain(), c, ParametersPackage.eINSTANCE.getConstraint_Enabled(), event.getChecked());
						getCommandHandler().handleCommand(sc, c, ParametersPackage.eINSTANCE.getConstraint_Enabled());
						return;
					}
				}

				final Constraint o = ParametersFactory.eINSTANCE.createConstraint();
				o.setName(name);
				o.setEnabled(event.getChecked());

				final Command ac = AddCommand.create(getCommandHandler().getEditingDomain(), object, ParametersPackage.eINSTANCE.getOptimiserSettings_Constraints(), o);
				getCommandHandler().handleCommand(ac, object, ParametersPackage.eINSTANCE.getOptimiserSettings_Constraints());
			}
		});

		ffrv.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		ccrv.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));

		ccrv.setInput(ccr);
		ffrv.setInput(ffr);

		final ArrayList<String> enabledObjectiveNames = new ArrayList<String>();
		for (final Objective o : ((OptimiserSettings) object).getObjectives()) {
			if (o.isEnabled())
				enabledObjectiveNames.add(o.getName());
		}
		ffrv.setCheckedElements(enabledObjectiveNames.toArray());

		enabledObjectiveNames.clear();
		for (final Constraint o : ((OptimiserSettings) object).getConstraints()) {
			if (o.isEnabled())
				enabledObjectiveNames.add(o.getName());
		}
		ccrv.setCheckedElements(enabledObjectiveNames.toArray());

	}
}

package com.mmxlabs.models.lng.optimiser.ui.displaycomposites;

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

import com.mmxlabs.models.lng.optimiser.Constraint;
import com.mmxlabs.models.lng.optimiser.Objective;
import com.mmxlabs.models.lng.optimiser.OptimiserFactory;
import com.mmxlabs.models.lng.optimiser.OptimiserPackage;
import com.mmxlabs.models.lng.optimiser.OptimiserSettings;
import com.mmxlabs.models.lng.optimiser.presentation.OptimiserEditorPlugin;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;

public class OptimiserSettingsToplevelComposite extends DefaultTopLevelComposite {
	public OptimiserSettingsToplevelComposite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public void display(final MMXRootObject root, final EObject object, final Collection<EObject> range) {
		super.display(root, object, range);
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
		
		final IConstraintCheckerRegistry ccr = OptimiserEditorPlugin.getPlugin().getConstraintCheckerRegistry();
		final IFitnessFunctionRegistry ffr = OptimiserEditorPlugin.getPlugin().getFitnessFunctionRegistry();
		
		constraints.setLayout(new GridLayout(1, true));
		objectives.setLayout(new GridLayout(1, true));
		
		final CheckboxTableViewer ccrv = CheckboxTableViewer.newCheckList(constraints, SWT.BORDER);
		
		ccrv.setContentProvider(new IStructuredContentProvider() {			
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				
			}
			
			@Override
			public void dispose() {
				
			}
			
			@Override
			public Object[] getElements(Object inputElement) {
				return ((IConstraintCheckerRegistry) inputElement).getConstraintCheckerNames().toArray();
			}
		});
		
		
		ccrv.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				return EditorUtils.unmangle(""+element);
			}
		});

		final CheckboxTableViewer ffrv = CheckboxTableViewer.newCheckList(objectives, SWT.BORDER);
		
		ffrv.setContentProvider(new IStructuredContentProvider() {
			
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				
			}
			
			@Override
			public void dispose() {
				
			}
			
			@Override
			public Object[] getElements(Object inputElement) {
				return ((IFitnessFunctionRegistry) inputElement).getFitnessComponentNames().toArray();
			}
		});
		
		ffrv.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
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
						final Command sc = SetCommand.create(getCommandHandler().getEditingDomain(), o, OptimiserPackage.eINSTANCE.getObjective_Enabled(), event.getChecked());
						getCommandHandler().handleCommand(sc, o, OptimiserPackage.eINSTANCE.getObjective_Enabled());
						return;
					}
				}
				
				final Objective o = OptimiserFactory.eINSTANCE.createObjective();
				o.setName(name);
				o.setWeight(1.0);
				o.setEnabled(event.getChecked());
				
				final Command ac = AddCommand.create(getCommandHandler().getEditingDomain(), object, OptimiserPackage.eINSTANCE.getOptimiserSettings_Objectives(), o);
				getCommandHandler().handleCommand(ac, object, OptimiserPackage.eINSTANCE.getOptimiserSettings_Objectives());
			}
		});
		
		ccrv.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				final String name = (String) event.getElement();
				final OptimiserSettings settings = (OptimiserSettings) object;
				for (final Constraint c : settings.getConstraints()) {
					if (c.getName().equals(name)) {
						final Command sc = SetCommand.create(getCommandHandler().getEditingDomain(), c, OptimiserPackage.eINSTANCE.getConstraint_Enabled(), event.getChecked());
						getCommandHandler().handleCommand(sc, c, OptimiserPackage.eINSTANCE.getConstraint_Enabled());
						return;
					}
				}
				
				final Constraint o = OptimiserFactory.eINSTANCE.createConstraint();
				o.setName(name);
				o.setEnabled(event.getChecked());
				
				final Command ac = AddCommand.create(getCommandHandler().getEditingDomain(), object, OptimiserPackage.eINSTANCE.getOptimiserSettings_Constraints(), o);
				getCommandHandler().handleCommand(ac, object, OptimiserPackage.eINSTANCE.getOptimiserSettings_Constraints());
			}
		});
		
		ffrv.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		ccrv.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		
		ccrv.setInput(ccr);
		ffrv.setInput(ffr);
	
		final ArrayList<String> enabledObjectiveNames = new ArrayList<String>();
		for (final Objective o : ((OptimiserSettings) object).getObjectives()) {
			if (o.isEnabled()) enabledObjectiveNames.add(o.getName());
		}
		ffrv.setCheckedElements(enabledObjectiveNames.toArray());
		
		enabledObjectiveNames.clear();
		for (final Constraint o : ((OptimiserSettings) object).getConstraints()) {
			if (o.isEnabled()) enabledObjectiveNames.add(o.getName());
		}
		ccrv.setCheckedElements(enabledObjectiveNames.toArray());
		
	}
}

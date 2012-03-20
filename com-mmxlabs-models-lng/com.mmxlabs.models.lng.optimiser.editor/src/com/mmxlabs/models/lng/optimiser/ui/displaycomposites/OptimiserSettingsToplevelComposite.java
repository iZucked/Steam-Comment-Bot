package com.mmxlabs.models.lng.optimiser.ui.displaycomposites;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.mmxlabs.models.lng.optimiser.presentation.OptimiserEditorPlugin;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;

public class OptimiserSettingsToplevelComposite extends DefaultTopLevelComposite {
	public OptimiserSettingsToplevelComposite(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public void display(final MMXRootObject root, final EObject object) {
		super.display(root, object);
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
		
		constraints.setLayout(new FillLayout());
		objectives.setLayout(new FillLayout());
		
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
				return "" + element;
			}
		});

		ccrv.setInput(ccr);
	}
}

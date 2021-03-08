/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.displaycomposites;

import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

/**
 * A display composite for the {@link GenericCharterContractTopLevelComposite} to add complex ballast bonus UI
 * @author achurchill
 * 
 */
public class GenericCharterContractTopLevelComposite extends DefaultTopLevelComposite {

	/**
	 * {@link Composite} to contain the heel editors
	 */
	private IDisplayComposite ballastBonusComposite;
	private IDisplayComposite repositioningFeeComposite;
	
	public GenericCharterContractTopLevelComposite(final Composite parent, final int style, final IDialogEditingContext dialogContext, FormToolkit toolkit) {
		super(parent, style, dialogContext, toolkit);
	}

	/**
	 * Override default implementation to pass in the "right" composite for heel options. Implementation should be more or less the same otherwise.
	 */
	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc) {

		final EClass eClass = object.eClass();
		Composite containerComposite = toolkit.createComposite(this, SWT.NONE);
		containerComposite.setLayout(new GridLayout());
		containerComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		final Group g = new Group(containerComposite, SWT.NONE);
		toolkit.adapt(g);

		g.setText("Charter terms");
		g.setLayout(new GridLayout());
		g.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		g.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		topLevel = new DefaultDetailComposite(g, SWT.NONE, toolkit);
		topLevel.setCommandHandler(commandHandler);
		topLevel.setEditorWrapper(editorWrapper);

		topLevel.display(dialogContext, root, object, range, dbc);

		Composite bottomComposite = toolkit.createComposite(this, SWT.NONE);
		bottomComposite.setLayout(new GridLayout());
		bottomComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		final Group g2 = new Group(bottomComposite, SWT.NONE);
		toolkit.adapt(g2);
		g2.setText("Ballast bonus");
		g2.setLayout(new GridLayout());
		g2.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		ballastBonusComposite = new BallastBonusDetailComposite(g2, SWT.NONE, toolkit, () -> {
			if (!GenericCharterContractTopLevelComposite.this.isDisposed()) {
				GenericCharterContractTopLevelComposite.this.layout(true, true);
			}
		});
		ballastBonusComposite.setCommandHandler(commandHandler);
		ballastBonusComposite.display(dialogContext, root, object, range, dbc);

		final Group g3 = new Group(bottomComposite, SWT.DOWN);
		toolkit.adapt(g3);
		g3.setText("Repositioning Fee");
		g3.setLayout(new GridLayout());
		g3.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		
		repositioningFeeComposite = new RepositioningFeeDetailComposite(g3, SWT.NONE, toolkit, () -> {
			if (!GenericCharterContractTopLevelComposite.this.isDisposed()) {
				GenericCharterContractTopLevelComposite.this.layout(true, true);
			}
		});
		repositioningFeeComposite.setCommandHandler(commandHandler);
		repositioningFeeComposite.display(dialogContext, root, object, range, dbc);
		
		this.setLayout(new GridLayout(1, true));
	}

	private Runnable defaultResizeMethod() {
		return () -> {
			if (!GenericCharterContractTopLevelComposite.this.isDisposed()) {
				GenericCharterContractTopLevelComposite.this.layout(true, true);
			}
		};
	}

	@Override
	public void displayValidationStatus(IStatus status) {
		super.displayValidationStatus(status);
		ballastBonusComposite.displayValidationStatus(status);
		repositioningFeeComposite.displayValidationStatus(status);
	}
	
	@Override
	protected boolean shouldDisplay(final EReference ref) {
		return true;
	}

}

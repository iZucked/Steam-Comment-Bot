/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

/**
 * A display composite for the {@link MonthlyBallastBonusContractTopLevelComposite} to add complex ballast bonus UI
 */
public class MonthlyBallastBonusContractTopLevelComposite extends DefaultTopLevelComposite {

	/**
	 * {@link Composite} to contain the heel editors
	 */
	private Composite right;
	private IDisplayComposite ballastBonusComposite;

	public MonthlyBallastBonusContractTopLevelComposite(final Composite parent, final int style, final IDialogEditingContext dialogContext, FormToolkit toolkit) {
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
		g.setLayout(new FillLayout());
		g.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		g.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		topLevel = new MonthlyBallastBonusContractDetailComposite(g, SWT.NONE, toolkit);
		topLevel.setCommandHandler(commandHandler);
		topLevel.setEditorWrapper(editorWrapper);

		topLevel.display(dialogContext, root, object, range, dbc);

//		final Iterator<IDisplayComposite> children = childComposites.iterator();
//		final Iterator<EObject> childObjectsItr = childObjects.iterator();
//
//		while (childObjectsItr.hasNext()) {
//			EObject next = childObjectsItr.next();
//			if (!(next instanceof BallastBonusContract)) {
//				children.next().display(dialogContext, root, next, range, dbc);
//			}
//		}

		Composite bottomComposite = toolkit.createComposite(this, SWT.NONE);
		bottomComposite.setLayout(new FillLayout());
		bottomComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		final Group g2 = new Group(bottomComposite, SWT.NONE);
		toolkit.adapt(g2);
		g2.setText("Ballast bonus");
		g2.setLayout(new GridLayout());
		g2.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		ballastBonusComposite = new MonthlyBallastBonusCharterContractDetailComposite(g2, SWT.NONE, toolkit, () -> {
			if (!MonthlyBallastBonusContractTopLevelComposite.this.isDisposed()) {
				MonthlyBallastBonusContractTopLevelComposite.this.layout(true, true);
			}
		});
		ballastBonusComposite.setCommandHandler(commandHandler);
		ballastBonusComposite.display(dialogContext, root, object, range, dbc);

		this.setLayout(new GridLayout(1, true));
	}

	@Override
	public void displayValidationStatus(IStatus status) {
		super.displayValidationStatus(status);
		ballastBonusComposite.displayValidationStatus(status);
	}
	
	@Override
	protected boolean shouldDisplay(final EReference ref) {
//		return ref.isContainment() && !ref.isMany() && ref != CargoPackage.eINSTANCE.getVesselAvailability_BallastBonusContract();
		return true;
	}

}

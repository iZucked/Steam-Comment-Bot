/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.displaycomposites;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeFactory;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

public class NetbackPurchaseContractCompositeFactory extends DefaultDisplayCompositeFactory {
	public NetbackPurchaseContractCompositeFactory() {

	}

	@Override
	public IDisplayComposite createToplevelComposite(final Composite composite, final EClass eClass, final IScenarioEditingLocation location) {
		return new DefaultTopLevelComposite(composite, SWT.NONE, location) {
			@Override
			protected void createChildComposites(final MMXRootObject root, final EObject object, final EClass eClass, final Composite parent) {
				super.createChildComposites(root, object, eClass, parent);

				final Button btn = new Button(parent, SWT.PUSH);
				btn.setText("Edit Notional Ballast Parameters");
				btn.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(final SelectionEvent e) {
						final DetailCompositeDialog dcd = new DetailCompositeDialog(parent.getShell(), getCommandHandler());
						dcd.open(location, root, object, CommercialPackage.eINSTANCE.getNetbackPurchaseContract_NotionalBallastParameters());
					}

					@Override
					public void widgetDefaultSelected(final SelectionEvent e) {

					}
				});
			}
		};
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.displaycomposites;

import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.ui.displaycomposites.ContractDetailComposite.ContractDetailGroup;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.DefaultStatusProvider;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

/**
 * 
 * @author Simon Goodall, achurchill
 * 
 */
public class ContractTopLevelComposite extends DefaultTopLevelComposite {

	/**
	 * {@link IDisplayComposite} to contain elements for the bottom of the editor
	 */
	protected IDisplayComposite restrictionsLevel = null;
	
	
	protected IDisplayComposite emissionsLevel = null;
	
	private Runnable resizeAction = () -> {
		if (!ContractTopLevelComposite.this.isDisposed()) {
			ContractTopLevelComposite.this.layout(true, true);
		}
	};
	private IStatus status;
	private DefaultStatusProvider statusProvider = new DefaultStatusProvider() {
		@Override
		public IStatus getStatus() {
			return status;
		}
	};
	/**
	 * {@link Composite} to contain the sub editors
	 */
	private Composite middle;

	public ContractTopLevelComposite(final Composite parent, final int style, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		super(parent, style, dialogContext, toolkit);
		setLayoutProvider(new DefaultDisplayCompositeLayoutProvider() {
			// used for children in "middle" composite
			@Override
			public Object createTopLayoutData(MMXRootObject root, EObject value, EObject detail) {
				return new GridData(GridData.FILL_BOTH);
			}
		});
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc) {

		final EClass eClass = object.eClass();
		final Group g = new Group(this, SWT.NONE);

		toolkit.adapt(g);

		g.setText("Contract");
		g.setLayout(new FillLayout());
		g.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL));
		g.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		// Create the directly rather than go through the registry. True indicates this is the top section. The bottom will be created later on
		topLevel = new ContractDetailComposite(g, SWT.NONE, ContractDetailGroup.GENERAL, toolkit);
		topLevel.setCommandHandler(commandHandler);
		topLevel.setEditorWrapper(editorWrapper);

		
		// Initialise middle composite
		middle = toolkit.createComposite(this);
		final int numChildren = createDefaultChildCompositeSection(dialogContext, root, object, range, dbc, eClass, middle);

		// We know there are n slots, so n columns
		middle.setLayout(new GridLayout(numChildren, true));
		middle.setLayoutData(new GridData(GridData.FILL_BOTH));
		middle.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		
		doDisplay(middle, object);

		final Composite myComposite = new Composite(this, SWT.NONE);
		toolkit.adapt(myComposite);
		myComposite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL));
		
		if(LicenseFeatures.isPermitted(KnownFeatures.FEATURE_EMISSIONS) && object instanceof PurchaseContract) {
			final Group g3 = new Group(middle, SWT.NONE);
			toolkit.adapt(g3);
			g3.setText("Emission rates");
			g3.setLayout(new FillLayout());
			g3.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL));
			g3.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

			emissionsLevel = new ContractDetailComposite(g3, SWT.NONE, ContractDetailGroup.EMISSIONS, toolkit);
			emissionsLevel.setCommandHandler(commandHandler);
			emissionsLevel.setEditorWrapper(editorWrapper);
			
			emissionsLevel.display(dialogContext, root, object, range, dbc);
		}
		
		final Group g2 = new Group(middle, SWT.NONE);
		toolkit.adapt(g2);
		
		g2.setText("Restrictions");
		g2.setLayout(new FillLayout());
		g2.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL));
		g2.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		restrictionsLevel = new ContractDetailComposite(g2, SWT.NONE, ContractDetailGroup.RESTRICTIONS, toolkit);
		restrictionsLevel.setCommandHandler(commandHandler);
		restrictionsLevel.setEditorWrapper(editorWrapper);
		
		
		//
		// // Overrides default layout factory so we get a single column rather than multiple columns and one row
		this.setLayout(new GridLayout(3, false));
		myComposite.setLayout(new GridLayout(1, false));
		
		topLevel.display(dialogContext, root, object, range, dbc);
		
		restrictionsLevel.display(dialogContext, root, object, range, dbc);
	}
	
	protected void doDisplay(final Composite parent, final EObject object) {
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_PREFERRED_FORMULAE)) {
			if (!this.dialogContext.isMultiEdit() && object instanceof final Contract contract && contract.getPriceInfo() instanceof final ExpressionPriceParameters epp) {
				PreferredFormulaeTableCreator.createPrefferedFormulaeTable(parent, toolkit, dialogContext, commandHandler, epp, //
						CommercialPackage.Literals.EXPRESSION_PRICE_PARAMETERS__PREFERRED_FORMULAE, //
						statusProvider, resizeAction);
				resizeAction.run();
			}
		}
	}

	@Override
	public void displayValidationStatus(final IStatus status) {
		super.displayValidationStatus(status);
		this.status = status;
		statusProvider.fireStatusChanged(status);
		restrictionsLevel.displayValidationStatus(status);
	}

	@Override
	public void setEditorWrapper(final IInlineEditorWrapper wrapper) {
		if (restrictionsLevel != null) {
			restrictionsLevel.setEditorWrapper(wrapper);
		}
		super.setEditorWrapper(wrapper);
	}
}

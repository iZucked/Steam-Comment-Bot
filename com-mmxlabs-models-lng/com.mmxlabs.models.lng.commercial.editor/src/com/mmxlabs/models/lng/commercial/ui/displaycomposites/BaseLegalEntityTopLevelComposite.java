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
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editorpart.DefaultStatusProvider;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.ui.impl.DefaultDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

public class BaseLegalEntityTopLevelComposite extends DefaultTopLevelComposite {

	/**
	 * {@link Composite} to contain the sub editors
	 */
	private Composite middle;
	
	private Runnable resizeAction = () -> {
		if (!BaseLegalEntityTopLevelComposite.this.isDisposed()) {
			BaseLegalEntityTopLevelComposite.this.layout(true, true);
		}
	};
	private IStatus status;
	private DefaultStatusProvider statusProvider = new DefaultStatusProvider() {
		@Override
		public IStatus getStatus() {
			return status;
		}
	};

	public BaseLegalEntityTopLevelComposite(final Composite parent, final int style, final IDialogEditingContext dialogContext, FormToolkit toolkit) {
		super(parent, style, dialogContext, toolkit);
		setLayoutProvider(new DefaultDisplayCompositeLayoutProvider() {
			// used for children in "middle" composite
			@Override
			public Object createTopLayoutData(MMXRootObject root, EObject value, EObject detail) {
				return new GridData(GridData.FILL_BOTH);
			}
		});
		setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc) {

		final EClass eClass = object.eClass();
		final Group g = new Group(this, SWT.NONE);
		toolkit.adapt(g);

		String groupName = EditorUtils.unmangle(eClass.getName());
		if (object instanceof Cargo) {
			final CargoType cargoType = ((Cargo) object).getCargoType();
			groupName += " Type: " + cargoType.getName();
		}

		g.setText(groupName);
		g.setLayout(new FillLayout());
		g.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		g.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		// Create the directly rather than go through the registry. True indicates this is the top section. The bottom will be created later on
		topLevel = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(object.eClass()).createSublevelComposite(g, object.eClass(), dialogContext, toolkit);
		topLevel.setCommandHandler(commandHandler);
		topLevel.setEditorWrapper(editorWrapper);
		
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_BUSINESS_UNITS) && shouldDisplayTable(object)) {
			final Group businessUnitsComposite = new Group(this, SWT.NONE);
			toolkit.adapt(businessUnitsComposite);
			businessUnitsComposite.setText("Business units");
			businessUnitsComposite.setLayout(GridLayoutFactory.fillDefaults()
					.numColumns(1)
					.equalWidth(false)
					.margins(0, 0)
					.extendedMargins(3, 0, 0, 0)
					.create());
			businessUnitsComposite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL));
			businessUnitsComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

			displayBusinessUnitsTable(businessUnitsComposite, object);
		}

		// Initialise middle composite
		middle = toolkit.createComposite(this);

		// We know there are n slots, so n columns
		middle.setLayoutData(new GridData(GridData.FILL_BOTH));
		middle.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		topLevel.display(dialogContext, root, object, range, dbc);
		
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_THIRD_PARTY_ENTITIES) && !dialogContext.isMultiEdit()) {
			final Label label = toolkit.createLabel(topLevel.getComposite(), "Third Party");
			label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
			
			final Button thirdPartyCheckBox = toolkit.createButton(topLevel.getComposite(), null, SWT.CHECK);
			thirdPartyCheckBox.setSelection((object instanceof final BaseLegalEntity entity && entity.isThirdParty()));
			thirdPartyCheckBox.addMouseListener(new ThirdPartyMouseListener(object));
		}

		int numChildren = createDefaultChildCompositeSection(dialogContext, root, object, range, dbc, eClass, middle);
		middle.setLayout(new GridLayout(numChildren, true));

		// Overrides default layout factory so we get a single column rather than multiple columns and one row
		this.setLayout(new GridLayout(1, true));
	}
	
	private boolean shouldDisplayTable(final EObject object) {
		return (object instanceof final BaseLegalEntity entity && (!entity.isThirdParty() || !entity.getBusinessUnits().isEmpty())) ;
	}
	
	private void displayBusinessUnitsTable(final Composite parent, final EObject object) {
		if (!this.dialogContext.isMultiEdit() && object instanceof final BaseLegalEntity entity) {
			BusinessUnitTableCreator.createBusinessUnitsTable(parent, toolkit, dialogContext, commandHandler, entity, //
					CommercialPackage.Literals.BASE_LEGAL_ENTITY__BUSINESS_UNITS, //
					statusProvider, resizeAction);
			resizeAction.run();
		}
	}

	@Override
	protected boolean shouldDisplay(final EReference ref) {
		return super.shouldDisplay(ref) || ref == CommercialPackage.eINSTANCE.getBaseLegalEntity_ShippingBook() || ref == CommercialPackage.eINSTANCE.getBaseLegalEntity_TradingBook();
	}

	protected IDisplayComposite createChildArea(ChildCompositeContainer childCompositeContainer, final MMXRootObject root, final EObject object, final Composite parent, final EReference ref,
			final EObject value) {
		final String label;
		if (ref == CommercialPackage.Literals.BASE_LEGAL_ENTITY__SHIPPING_BOOK) {
			label = "Shipping Book";
		} else if (ref == CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK) {
			label = "Trading Book";
		} else if (ref == CommercialPackage.Literals.BASE_LEGAL_ENTITY__UPSTREAM_BOOK) {
			label = "Upstream Book";
		} else {
			// Unmangle the feature name (to avoid forking this class for client specific books - See FB)
			label = EditorUtils.unmangle(ref.getName());
		}
		return createChildArea(childCompositeContainer, root, object, parent, ref, label, value);
	}
	
	private class ThirdPartyMouseListener implements MouseListener {
		private final EObject object;

		private ThirdPartyMouseListener(EObject object) {
			this.object = object;
		}

		@Override
		public void mouseUp(MouseEvent e) {
		}

		@Override
		public void mouseDown(MouseEvent e) {
			if (object instanceof final BaseLegalEntity entity) {
				boolean isThirdParty = entity.isThirdParty();
				commandHandler.handleCommand(SetCommand.create(commandHandler.getEditingDomain(), entity, //
						CommercialPackage.Literals.BASE_LEGAL_ENTITY__THIRD_PARTY, !isThirdParty), entity,
						CommercialPackage.Literals.BASE_LEGAL_ENTITY__THIRD_PARTY);

				dialogContext.getDialogController().rebuild(true);
			}
		}

		@Override
		public void mouseDoubleClick(MouseEvent e) {
		}
	}

}

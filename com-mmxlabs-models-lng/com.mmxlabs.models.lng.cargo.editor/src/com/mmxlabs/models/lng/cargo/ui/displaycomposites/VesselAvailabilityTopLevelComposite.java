/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.ui.displaycomposites.VesselAvailabilityDetailComposite.VesselAvailabilityDetailGroup;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

/**
 * A display composite for the {@link VesselAvailability} editor to keep the start and end heel options in one column. Note this assumes start and end heel are the only contained children.
 * 
 * @author Simon Goodall
 * 
 */
public class VesselAvailabilityTopLevelComposite extends DefaultTopLevelComposite {


	/**
	 * {@link Composite} to contain the heel editors
	 */
	private BallastBonusTermsDetailComposite ballastBonusComposite;
	private RepositioningFeeTermsDetailComposite repositioningFeeComposite;
	private Button gccButton;
	
	public VesselAvailabilityTopLevelComposite(final Composite parent, final int style, final IDialogEditingContext dialogContext, FormToolkit toolkit) {
		super(parent, style, dialogContext, toolkit);
	}

	/**
	 * Override default implementation to pass in the "right" composite for heel options. Implementation should be more or less the same otherwise.
	 */
	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc) {

		Composite containerComposite = toolkit.createComposite(this, SWT.NONE);
		containerComposite.setLayout(new GridLayout(1, true));
		
		final Group g = new Group(containerComposite, SWT.NONE);
		toolkit.adapt(g);

		g.setText(EditorUtils.unmangle(object));
		g.setLayout(new GridLayout(2, true));
		g.setLayoutData(layoutProvider.createTopLayoutData(root, object, object));
		g.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		
		topLevel = new VesselAvailabilityDetailComposite(g, SWT.NONE, toolkit, VesselAvailabilityDetailGroup.TOP_LEFT, dialogContext);
		topLevel.setCommandHandler(commandHandler);
		topLevel.setEditorWrapper(editorWrapper);
		topLevel.display(dialogContext, root, object, range, dbc);
		
		IDisplayComposite topRight = new VesselAvailabilityDetailComposite(g, SWT.NONE, toolkit, VesselAvailabilityDetailGroup.TOP_RIGHT, dialogContext);
		topRight.setCommandHandler(commandHandler);
		topRight.setEditorWrapper(editorWrapper);
		topRight.display(dialogContext, root, object, range, dbc);

		checkForNewAvailability(object);
		if (!dialogContext.isMultiEdit()) {
			gccButton = toolkit.createButton(topLevel.getComposite(), gccButtonLabel(object), SWT.CENTER);
			gccButton.addMouseListener(new CharterContractMouseListener(object));
		}

		Composite midComposite = toolkit.createComposite(this, SWT.NONE);
		midComposite.setLayout(new GridLayout(1, true));
		midComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		final Group g2 = new Group(midComposite, SWT.DOWN);
		toolkit.adapt(g2);
		g2.setText("Start conditions");
		g2.setLayout(new GridLayout());
		g2.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		
		IDisplayComposite startStuff = new VesselAvailabilityDetailComposite(g2, SWT.NONE, toolkit, VesselAvailabilityDetailGroup.START, dialogContext);
		startStuff.setCommandHandler(commandHandler);
		startStuff.setEditorWrapper(editorWrapper);
		startStuff.display(dialogContext, root, object, range, dbc);
		
		repositioningFeeComposite = new RepositioningFeeTermsDetailComposite(g2, SWT.NONE, dialogContext, toolkit, defaultResizeAction());
		repositioningFeeComposite.setCommandHandler(commandHandler);
		repositioningFeeComposite.setEditorWrapper(editorWrapper);
		repositioningFeeComposite.display(dialogContext, root, object, range, dbc);
		
		Composite bottomComposite = toolkit.createComposite(this, SWT.NONE);
		bottomComposite.setLayout(new GridLayout(1, true));
		bottomComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		final Group g3 = new Group(bottomComposite, SWT.NONE);
		toolkit.adapt(g3);
		g3.setText("End conditions");
		g3.setLayout(new GridLayout());
		g3.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		
		IDisplayComposite endStuff = new VesselAvailabilityDetailComposite(g3, SWT.NONE, toolkit, VesselAvailabilityDetailGroup.END, dialogContext);
		endStuff.setCommandHandler(commandHandler);
		endStuff.setEditorWrapper(editorWrapper);
		endStuff.display(dialogContext, root, object, range, dbc);
		
		ballastBonusComposite = new BallastBonusTermsDetailComposite(g3, SWT.NONE, dialogContext, toolkit, defaultResizeAction());
		ballastBonusComposite.setCommandHandler(commandHandler);
		ballastBonusComposite.setEditorWrapper(editorWrapper);
		ballastBonusComposite.display(dialogContext, root, object, range, dbc);

		this.setLayout(new GridLayout(1, true));
	}

	private Runnable defaultResizeAction() {
		return () -> {
			if (!VesselAvailabilityTopLevelComposite.this.isDisposed()) {
				VesselAvailabilityTopLevelComposite.this.layout(true, true);
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
		return ref.isContainment() && !ref.isMany() && ref != CargoPackage.eINSTANCE.getVesselAvailability_ContainedCharterContract()
				&& ref != CargoPackage.eINSTANCE.getVesselAvailability_StartAt();
	}

	private class CharterContractMouseListener implements MouseListener {
		private final EObject object;

		private CharterContractMouseListener(EObject object) {
			this.object = object;
		}

		@Override
		public void mouseUp(MouseEvent e) {
		}

		@Override
		public void mouseDown(MouseEvent e) {
			if (object instanceof VesselAvailability) {
				final VesselAvailability va = (VesselAvailability) object;
				if (va.getContainedCharterContract() != null) {
					commandHandler.handleCommand(SetCommand.create(commandHandler.getEditingDomain(), va, CargoPackage.Literals.VESSEL_AVAILABILITY__CONTAINED_CHARTER_CONTRACT, null), va,
							CargoPackage.Literals.VESSEL_AVAILABILITY__CONTAINED_CHARTER_CONTRACT);
					commandHandler.handleCommand(SetCommand.create(commandHandler.getEditingDomain(), va, CargoPackage.Literals.VESSEL_AVAILABILITY__CHARTER_CONTRACT_OVERRIDE, Boolean.FALSE), va,
							CargoPackage.Literals.VESSEL_AVAILABILITY__CHARTER_CONTRACT_OVERRIDE);
					gccButton.setText(gccButtonLabel(va));
				} else {
					GenericCharterContract gcc = CommercialFactory.eINSTANCE.createGenericCharterContract();
					gcc.setName("-");
					commandHandler.handleCommand(SetCommand.create(commandHandler.getEditingDomain(), va, CargoPackage.Literals.VESSEL_AVAILABILITY__CONTAINED_CHARTER_CONTRACT, gcc), va,
							CargoPackage.Literals.VESSEL_AVAILABILITY__CONTAINED_CHARTER_CONTRACT);
					commandHandler.handleCommand(SetCommand.create(commandHandler.getEditingDomain(), va, CargoPackage.Literals.VESSEL_AVAILABILITY__CHARTER_CONTRACT_OVERRIDE, Boolean.TRUE), va,
							CargoPackage.Literals.VESSEL_AVAILABILITY__CHARTER_CONTRACT_OVERRIDE);
					gccButton.setText(gccButtonLabel(va));
				}
				boolean needRebuilding = true;
				if (repositioningFeeComposite != null && needRebuilding) {
					needRebuilding = false;
					repositioningFeeComposite.getDialogContext().getDialogController().rebuild(true);
				}
				if (ballastBonusComposite != null && needRebuilding) {
					ballastBonusComposite.getDialogContext().getDialogController().rebuild(true);
				}
			}
		}

		@Override
		public void mouseDoubleClick(MouseEvent e) {
		}
	}
	
	private void checkForNewAvailability(EObject object) {
		if (object instanceof VesselAvailability) {
			final VesselAvailability va = (VesselAvailability) object;
			final GenericCharterContract gcc = va.getContainedCharterContract();
			if (gcc != null && gcc.getName() == null) {
				va.setContainedCharterContract(null);
			}
		}
	}
	
	private String gccButtonLabel(EObject object) {
		if (object instanceof VesselAvailability) {
			final VesselAvailability va = (VesselAvailability) object;
			final GenericCharterContract gcc = va.getContainedCharterContract();
			if (gcc != null) {
				return "Reset";
			} else {
				return "Override";
			}
		}
		return "Override";
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
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

import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.ui.displaycomposites.VesselCharterDetailComposite.VesselCharterDetailGroup;
import com.mmxlabs.models.lng.cargo.ui.displaycomposites.cii.CIIEndOptionsDetailComposite;
import com.mmxlabs.models.lng.cargo.ui.displaycomposites.cii.CIIStartOptionsDetailComposite;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.ui.impl.DefaultTopLevelComposite;

/**
 * A display composite for the {@link VesselCharter} editor to keep the start and end heel options in one column. Note this assumes start and end heel are the only contained children.
 * 
 * @author Simon Goodall, Andrey Popov
 * 
 */
public class VesselCharterTopLevelComposite extends DefaultTopLevelComposite {


	/**
	 * {@link Composite} to contain the heel editors
	 */
	private IDisplayComposite startHeelComposite;
	private IDisplayComposite endHeelComposite;
	private BallastBonusTermsDetailComposite ballastBonusComposite;
	private RepositioningFeeTermsDetailComposite repositioningFeeComposite;
	private Button gccButton;
	private IDisplayComposite ciiStartOptions;
	private IDisplayComposite ciiEndOptions;
	
	public VesselCharterTopLevelComposite(final Composite parent, final int style, final IDialogEditingContext dialogContext, FormToolkit toolkit) {
		super(parent, style, dialogContext, toolkit);
	}

	/**
	 * Override default implementation to pass in the "right" composite for heel options. Implementation should be more or less the same otherwise.
	 */
	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc) {
		Composite containerComposite = toolkit.createComposite(this, SWT.NONE);
		containerComposite.setLayout(new GridLayout(1, true));
		
		final Group vesselCharterTopGroup = new Group(containerComposite, SWT.NONE);
		toolkit.adapt(vesselCharterTopGroup);

		vesselCharterTopGroup.setText(EditorUtils.unmangle(object));
		vesselCharterTopGroup.setLayout(new GridLayout(2, true));
		vesselCharterTopGroup.setLayoutData(layoutProvider.createTopLayoutData(root, object, object));
		vesselCharterTopGroup.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		
		topLevel = new VesselCharterDetailComposite(vesselCharterTopGroup, SWT.NONE, toolkit, VesselCharterDetailGroup.TOP_LEFT, dialogContext);
		topLevel.setCommandHandler(commandHandler);
		topLevel.setEditorWrapper(editorWrapper);
		topLevel.display(dialogContext, root, object, range, dbc);
		
		IDisplayComposite topRight = new VesselCharterDetailComposite(vesselCharterTopGroup, SWT.NONE, toolkit, VesselCharterDetailGroup.TOP_RIGHT, dialogContext);
		topRight.setCommandHandler(commandHandler);
		topRight.setEditorWrapper(editorWrapper);
		topRight.display(dialogContext, root, object, range, dbc);

		checkForNewAvailability(object);
		if (!dialogContext.isMultiEdit()) {
			gccButton = toolkit.createButton(topLevel.getComposite(), gccButtonLabel(object), SWT.CENTER);
			gccButton.addMouseListener(new CharterContractMouseListener(object));
		}
		
		//
		// Layouts
		final GridDataFactory layoutDataFactory = GridDataFactory.fillDefaults().grab(true, true);
		final GridLayout fourColumnsLayout = createCustomGridLayout(4, false);
		final GridLayout twoColumnLayout = createCustomGridLayout(2, false);
		final GridLayout singleColumnLayout = createCustomGridLayout(1, false);
		final GridData twoColumnSpanningGridLayoutData = createSpanningLayoutData(layoutDataFactory, 2);
		final GridData fourColumnSpanningGridLayoutData = createSpanningLayoutData(layoutDataFactory, 4);
		
		//
		// Wrapper
		final Composite wrapperCompositeWithFourColumns = toolkit.createComposite(this, SWT.NONE);
		wrapperCompositeWithFourColumns.setLayout(fourColumnsLayout);	
		wrapperCompositeWithFourColumns.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		//
		// Start Stuff
		final Group groupForVesselStartStuff = createGroup(wrapperCompositeWithFourColumns, "Start", singleColumnLayout, layoutDataFactory.create());
		final IDisplayComposite startStuff = new VesselCharterDetailComposite(groupForVesselStartStuff, SWT.NONE, toolkit, VesselCharterDetailGroup.START, dialogContext);
		startStuff.setCommandHandler(commandHandler);
		startStuff.setEditorWrapper(editorWrapper);
		startStuff.display(dialogContext, root, object, range, dbc);
		
		//
		// Start Heel Stuff
		final Group groupForVesselStartHeelStuff = createGroup(wrapperCompositeWithFourColumns, "Heel", singleColumnLayout, layoutDataFactory.create());
		startHeelComposite = new StartHeelOptionsDetailComposite(groupForVesselStartHeelStuff, SWT.NONE, toolkit);
		startHeelComposite.setCommandHandler(commandHandler);
		startHeelComposite.display(dialogContext, root, getStartHeel(object), range, dbc);
		
		//
		// Vessel Charter End Stuff
		final Group groupForVesselEndStuff = createGroup(wrapperCompositeWithFourColumns, "End", singleColumnLayout, layoutDataFactory.create());
		IDisplayComposite endStuff = new VesselCharterDetailComposite(groupForVesselEndStuff, SWT.NONE, toolkit, VesselCharterDetailGroup.END, dialogContext);
		endStuff.setCommandHandler(commandHandler);
		endStuff.setEditorWrapper(editorWrapper);
		endStuff.display(dialogContext, root, object, range, dbc);
		
		//
		// End Heel Stuff
		final Group groupForEndHeelStuff = createGroup(wrapperCompositeWithFourColumns, "Heel", singleColumnLayout, layoutDataFactory.create());
		endHeelComposite = new EndHeelOptionsDetailComposite(groupForEndHeelStuff, SWT.NONE, toolkit);
		endHeelComposite.setCommandHandler(commandHandler);
		endHeelComposite.display(dialogContext, root, getEndHeel(object), range, dbc);

		//
		// Start CII Stuff
		final Group groupForCIIStartOptionsStuff = createGroup(wrapperCompositeWithFourColumns, "CII", twoColumnLayout, twoColumnSpanningGridLayoutData);
		ciiStartOptions = new CIIStartOptionsDetailComposite(groupForCIIStartOptionsStuff, SWT.NONE, toolkit);
		ciiStartOptions.setCommandHandler(commandHandler);
		ciiStartOptions.display(dialogContext, root, getCIIStartOptions(object), range, dbc);
		
		//
		// CII End Options Stuff
		final Group groupForCIIEndOptionsStuff = createGroup(wrapperCompositeWithFourColumns, "CII", singleColumnLayout, twoColumnSpanningGridLayoutData);
		ciiEndOptions = new CIIEndOptionsDetailComposite(groupForCIIEndOptionsStuff, SWT.NONE, toolkit);
		ciiEndOptions.setCommandHandler(commandHandler);
		ciiEndOptions.display(dialogContext, root, getCIIEndOptions(object), range, dbc);
		
		//
		// Repositioning Fee Stuff
		final Group groupForRepositioningFeeComposite = createGroup(wrapperCompositeWithFourColumns, null, singleColumnLayout, fourColumnSpanningGridLayoutData);
		groupForRepositioningFeeComposite.setVisible(hasContract(object));
		repositioningFeeComposite = new RepositioningFeeTermsDetailComposite(groupForRepositioningFeeComposite, SWT.NONE, dialogContext, toolkit, defaultResizeAction());
		repositioningFeeComposite.setCommandHandler(commandHandler);
		repositioningFeeComposite.setEditorWrapper(editorWrapper);
		repositioningFeeComposite.display(dialogContext, root, object, range, dbc);
		
		//
		// Ballast Bonus Stuff
		final Group groupForBallastBonusComposite = createGroup(wrapperCompositeWithFourColumns, null, singleColumnLayout, fourColumnSpanningGridLayoutData);
		groupForBallastBonusComposite.setVisible(hasContract(object));
		ballastBonusComposite = new BallastBonusTermsDetailComposite(groupForBallastBonusComposite, SWT.NONE, dialogContext, toolkit, defaultResizeAction(), object);
		ballastBonusComposite.setCommandHandler(commandHandler);
		ballastBonusComposite.setEditorWrapper(editorWrapper);
		ballastBonusComposite.display(dialogContext, root, object, range, dbc);

		this.setLayout(new GridLayout(1, true));
	}
	
	private Group createGroup(final Composite parentComposite, final String groupTitle, final GridLayout groupGridLayout, final GridData groupGridData) {
		final Group group = new Group(parentComposite, SWT.NONE);
		toolkit.adapt(group);
		if (groupTitle != null) {
			group.setText(groupTitle);
		}
		group.setLayout(groupGridLayout);
		group.setLayoutData(groupGridData);
		group.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		return group;
	}

	private GridData createSpanningLayoutData(final GridDataFactory layoutDataFactory, final int columnSpan) {
		final GridData spanningGridLayoutData = layoutDataFactory.create();
		spanningGridLayoutData.horizontalAlignment = GridData.FILL;
		spanningGridLayoutData.horizontalSpan = columnSpan;
		return spanningGridLayoutData;
	}


	private Runnable defaultResizeAction() {
		return () -> {
			if (!VesselCharterTopLevelComposite.this.isDisposed()) {
				VesselCharterTopLevelComposite.this.layout(true, true);
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
		return ref.isContainment() && !ref.isMany() //
				&& ref != CargoPackage.eINSTANCE.getVesselCharter_ContainedCharterContract()//
				&& ref != CargoPackage.eINSTANCE.getVesselCharter_StartAt();
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
			if (object instanceof VesselCharter) {
				final VesselCharter va = (VesselCharter) object;
				if (va.getContainedCharterContract() != null) {
					commandHandler.handleCommand(SetCommand.create(commandHandler.getEditingDomain(), va, CargoPackage.Literals.VESSEL_CHARTER__CONTAINED_CHARTER_CONTRACT, null), va,
							CargoPackage.Literals.VESSEL_CHARTER__CONTAINED_CHARTER_CONTRACT);
					commandHandler.handleCommand(SetCommand.create(commandHandler.getEditingDomain(), va, CargoPackage.Literals.VESSEL_CHARTER__CHARTER_CONTRACT_OVERRIDE, Boolean.FALSE), va,
							CargoPackage.Literals.VESSEL_CHARTER__CHARTER_CONTRACT_OVERRIDE);
					gccButton.setText(gccButtonLabel(va));
				} else {
					GenericCharterContract gcc = CommercialFactory.eINSTANCE.createGenericCharterContract();
					gcc.setName("-");
					commandHandler.handleCommand(SetCommand.create(commandHandler.getEditingDomain(), va, CargoPackage.Literals.VESSEL_CHARTER__CONTAINED_CHARTER_CONTRACT, gcc), va,
							CargoPackage.Literals.VESSEL_CHARTER__CONTAINED_CHARTER_CONTRACT);
					commandHandler.handleCommand(SetCommand.create(commandHandler.getEditingDomain(), va, CargoPackage.Literals.VESSEL_CHARTER__CHARTER_CONTRACT_OVERRIDE, Boolean.TRUE), va,
							CargoPackage.Literals.VESSEL_CHARTER__CHARTER_CONTRACT_OVERRIDE);
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
	
	private boolean hasContract(EObject object) {
		if (object instanceof VesselCharter va) {
			final GenericCharterContract gcc = va.getContainedCharterContract();
			if (gcc != null && gcc.getName() != null) {
				return true;
			}
		}
		return false;
	}
	
	private EObject getStartHeel(final EObject object) {
		return (EObject) object.eGet(CargoPackage.Literals.VESSEL_CHARTER__START_HEEL);
	}
	
	private EObject getCIIStartOptions(final EObject object) {
		if (object instanceof VesselCharter vc && vc.getCiiStartOptions() == null) {
			vc.setCiiStartOptions(CargoFactory.eINSTANCE.createCIIStartOptions());
		}
		return (EObject) object.eGet(CargoPackage.Literals.VESSEL_CHARTER__CII_START_OPTIONS);
	}
	
	private EObject getCIIEndOptions(EObject object) {
		if (object instanceof VesselCharter vc && vc.getCiiEndOptions() == null) {
			vc.setCiiEndOptions(CargoFactory.eINSTANCE.createCIIEndOptions());
		}
		return (EObject) object.eGet(CargoPackage.Literals.VESSEL_CHARTER__CII_END_OPTIONS);
	}

	private EObject getEndHeel(final EObject object) {
		return (EObject) object.eGet(CargoPackage.Literals.VESSEL_CHARTER__END_HEEL);
	}
	
	private void checkForNewAvailability(EObject object) {
		if (object instanceof VesselCharter) {
			final VesselCharter va = (VesselCharter) object;
			final GenericCharterContract gcc = va.getContainedCharterContract();
			if (gcc != null && gcc.getName() == null) {
				va.setContainedCharterContract(null);
			}
		}
	}
	
	private String gccButtonLabel(EObject object) {
		if (object instanceof VesselCharter) {
			final VesselCharter va = (VesselCharter) object;
			final GenericCharterContract gcc = va.getContainedCharterContract();
			if (gcc != null) {
				return "Reset";
			} else {
				return "Create";
			}
		}
		return "Create";
	}
	
	private GridLayout createCustomGridLayout(int numColumns, boolean makeColumnsEqualWidth) {
		return GridLayoutFactory.fillDefaults()
				.numColumns(numColumns)
				.equalWidth(makeColumnsEqualWidth)
				.margins(0, 0)
				.extendedMargins(0, 0, 0, 0)
				.create();
	}
}

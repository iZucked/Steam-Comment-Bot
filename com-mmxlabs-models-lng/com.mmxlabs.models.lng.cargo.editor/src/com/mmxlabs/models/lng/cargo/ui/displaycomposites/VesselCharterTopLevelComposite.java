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

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.ui.displaycomposites.VesselCharterDetailComposite.VesselCharterDetailGroup;
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
 * @author Simon Goodall
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
		
		final Group g = new Group(containerComposite, SWT.NONE);
		toolkit.adapt(g);

		g.setText(EditorUtils.unmangle(object));
		g.setLayout(new GridLayout(2, true));
		g.setLayoutData(layoutProvider.createTopLayoutData(root, object, object));
		g.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		
		topLevel = new VesselCharterDetailComposite(g, SWT.NONE, toolkit, VesselCharterDetailGroup.TOP_LEFT, dialogContext);
		topLevel.setCommandHandler(commandHandler);
		topLevel.setEditorWrapper(editorWrapper);
		topLevel.display(dialogContext, root, object, range, dbc);
		
		IDisplayComposite topRight = new VesselCharterDetailComposite(g, SWT.NONE, toolkit, VesselCharterDetailGroup.TOP_RIGHT, dialogContext);
		topRight.setCommandHandler(commandHandler);
		topRight.setEditorWrapper(editorWrapper);
		topRight.display(dialogContext, root, object, range, dbc);

		checkForNewAvailability(object);
		if (!dialogContext.isMultiEdit()) {
			gccButton = toolkit.createButton(topLevel.getComposite(), gccButtonLabel(object), SWT.CENTER);
			gccButton.addMouseListener(new CharterContractMouseListener(object));
		}
		
		final GridDataFactory layoutData = GridDataFactory.fillDefaults().grab(true, true);

		final GridLayout gl2C = createCustomGridLayout(2, false);
		
		Composite midComposite = toolkit.createComposite(this, SWT.NONE);
		midComposite.setLayout(gl2C);
		midComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		final GridLayout gl4C = createCustomGridLayout(4, false);
		
		final Composite g2 = new Composite(midComposite, SWT.NONE);
		toolkit.adapt(g2);
		g2.setLayout(gl4C);
		g2.setLayoutData(layoutData.create());
		g2.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		
		final GridLayout gl = createCustomGridLayout(1, false);
		
		final Group g21 = new Group(g2, SWT.NONE);
		toolkit.adapt(g21);
		g21.setText("Start");
		g21.setLayout(gl);
		g21.setLayoutData(layoutData.create());
		g21.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		
		IDisplayComposite startStuff = new VesselCharterDetailComposite(g21, SWT.NONE, toolkit, VesselCharterDetailGroup.START, dialogContext);
		startStuff.setCommandHandler(commandHandler);
		startStuff.setEditorWrapper(editorWrapper);
		startStuff.display(dialogContext, root, object, range, dbc);
		
		final Group g22 = new Group(g2, SWT.NONE);
		toolkit.adapt(g22);
		g22.setText("Heel");
		g22.setLayout(gl);
		g22.setLayoutData(layoutData.create());
		g22.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		
		startHeelComposite = new StartHeelOptionsDetailComposite(g22, SWT.NONE, toolkit);
		startHeelComposite.setCommandHandler(commandHandler);
		startHeelComposite.display(dialogContext, root, getStartHeel(object), range, dbc);
		
		
		final Composite g3 = new Composite(midComposite, SWT.NONE);
		toolkit.adapt(g3);
		g3.setLayout(gl4C);
		g3.setLayoutData(layoutData.create());
		g3.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		
		final Group g31 = new Group(g3, SWT.NONE);
		toolkit.adapt(g31);
		g31.setText("End");
		g31.setLayout(gl);
		g31.setLayoutData(layoutData.create());
		g31.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		
		IDisplayComposite endStuff = new VesselCharterDetailComposite(g31, SWT.NONE, toolkit, VesselCharterDetailGroup.END, dialogContext);
		endStuff.setCommandHandler(commandHandler);
		endStuff.setEditorWrapper(editorWrapper);
		endStuff.display(dialogContext, root, object, range, dbc);
		
		final Group g32 = new Group(g3, SWT.NONE);
		toolkit.adapt(g32);
		g32.setText("Heel");
		g32.setLayout(gl);
		g32.setLayoutData(layoutData.create());
		g32.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		
		endHeelComposite = new EndHeelOptionsDetailComposite(g32, SWT.NONE, toolkit);
		endHeelComposite.setCommandHandler(commandHandler);
		endHeelComposite.display(dialogContext, root, getEndHeel(object), range, dbc);
		
		
		final Group g4 = new Group(midComposite, SWT.NONE);
		toolkit.adapt(g4);
		g4.setLayout(gl);
		g4.setLayoutData(layoutData.create());
		g4.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		g4.setVisible(hasContract(object));
		
		repositioningFeeComposite = new RepositioningFeeTermsDetailComposite(g4, SWT.NONE, dialogContext, toolkit, defaultResizeAction());
		repositioningFeeComposite.setCommandHandler(commandHandler);
		repositioningFeeComposite.setEditorWrapper(editorWrapper);
		repositioningFeeComposite.display(dialogContext, root, object, range, dbc);
		
		final Group g5 = new Group(midComposite, SWT.NONE);
		toolkit.adapt(g5);
		g5.setLayout(gl);
		g5.setLayoutData(layoutData.create());
		g5.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		g5.setVisible(hasContract(object));
		
		ballastBonusComposite = new BallastBonusTermsDetailComposite(g5, SWT.NONE, dialogContext, toolkit, defaultResizeAction(), object);
		ballastBonusComposite.setCommandHandler(commandHandler);
		ballastBonusComposite.setEditorWrapper(editorWrapper);
		ballastBonusComposite.display(dialogContext, root, object, range, dbc);

		this.setLayout(new GridLayout(1, true));
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

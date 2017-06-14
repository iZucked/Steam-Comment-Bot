/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.editors.routeslots;

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.nebula.widgets.formattedtext.FormattedText;
import org.eclipse.nebula.widgets.formattedtext.IntegerFormatter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.impl.ReferenceInlineEditor;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;

public class RouteOptionEditorPane extends ScenarioTableViewerPane {

	private static final Logger log = LoggerFactory.getLogger(RouteOptionEditorPane.class);
	private FormattedText strictEditor;
	private FormattedText relaxedEditor;
	private FormattedText flexEditor;
	private CanalBookings canalBookingsModel;

	private AdapterImpl changeListener = new AdapterImpl() {
		public void notifyChanged(org.eclipse.emf.common.notify.Notification msg) {

			super.notifyChanged(msg);
			if (msg.getEventType() == Notification.REMOVING_ADAPTER) {
				return;
			}
			if (msg.getFeature() == CargoPackage.Literals.CANAL_BOOKINGS__FLEXIBLE_SLOT_AMOUNT) {
				flexEditor.setValue(msg.getNewValue());
				return;
			}
			if (msg.getFeature() == CargoPackage.Literals.CANAL_BOOKINGS__RELAXED_BOUNDARY_OFFSET_DAYS) {
				relaxedEditor.setValue(msg.getNewValue());
				return;
			}
			if (msg.getFeature() == CargoPackage.Literals.CANAL_BOOKINGS__STRICT_BOUNDARY_OFFSET_DAYS) {
				strictEditor.setValue(msg.getNewValue());
				return;
			}
		};
	};

	public RouteOptionEditorPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public ScenarioTableViewer createViewer(Composite parent) {

		{

			final Composite pparent = new Composite(parent, SWT.NONE);
			pparent.setLayout(GridLayoutFactory.fillDefaults().numColumns(8).equalWidth(false).spacing(3, 0).margins(0, 7).create());

			final Label lbl = new Label(pparent, SWT.NONE);
			lbl.setText("Panama booking period : Strict +");
			lbl.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).minSize(1000, -1).create());

			strictEditor = new FormattedText(pparent);
			strictEditor.setFormatter(new IntegerFormatter());
			strictEditor.getControl().setLayoutData(GridDataFactory.swtDefaults().hint(30, SWT.DEFAULT).create());

			strictEditor.getControl().addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					Object newValue = strictEditor.getValue();
					if (canalBookingsModel != null && newValue instanceof Integer && !Objects.equals(newValue, canalBookingsModel.getStrictBoundaryOffsetDays())) {
						final Command cmd = SetCommand.create(getEditingDomain(), canalBookingsModel, CargoPackage.eINSTANCE.getCanalBookings_StrictBoundaryOffsetDays(), newValue);
						getEditingDomain().getCommandStack().execute(cmd);
					}
				}

			});
			strictEditor.getControl().setToolTipText("Bookings must exist for the panama canal up to n days from the prompt start date.");

			final Label lbl2 = new Label(pparent, SWT.NONE);
			lbl2.setText("days, relaxed +");
			lbl2.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).create());

			relaxedEditor = new FormattedText(pparent);
			relaxedEditor.setFormatter(new IntegerFormatter());
			relaxedEditor.getControl().setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).hint(30, SWT.DEFAULT).create());

			relaxedEditor.getControl().addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					Object newValue = relaxedEditor.getValue();
					if (canalBookingsModel != null && newValue instanceof Integer && !Objects.equals(newValue, canalBookingsModel.getRelaxedBoundaryOffsetDays())) {
						final Command cmd = SetCommand.create(getEditingDomain(), canalBookingsModel, CargoPackage.eINSTANCE.getCanalBookings_RelaxedBoundaryOffsetDays(), newValue);
						getEditingDomain().getCommandStack().execute(cmd);
					}
				}

			});
			relaxedEditor.getControl().setToolTipText("Bookings should exist for the panama canal up to n days from the prompt start date.");

			
			final Label lbl3 = new Label(pparent, SWT.NONE);
			lbl3.setText("days from prompt. Flexible bookings ");
			lbl3.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).create());
			flexEditor = new FormattedText(pparent);
			flexEditor.setFormatter(new IntegerFormatter());
			// .setLayoutData(GridDataFactory.swtDefaults().minSize(1000, -1).create());
			flexEditor.getControl().setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).hint(30, SWT.DEFAULT).create());
			flexEditor.getControl().addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					Object newValue = flexEditor.getValue();
					if (canalBookingsModel != null && newValue instanceof Integer && !Objects.equals(newValue, canalBookingsModel.getFlexibleSlotAmount())) {
						final Command cmd = SetCommand.create(getEditingDomain(), canalBookingsModel, CargoPackage.eINSTANCE.getCanalBookings_FlexibleSlotAmount(), newValue);
						getEditingDomain().getCommandStack().execute(cmd);
					}
				}

			});
			flexEditor.getControl().setToolTipText("Number of permitted panama voyages without a booking in the relaxed period.");
		}

		return super.createViewer(parent);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final CommandStack commandStack) {
		super.init(path, adapterFactory, commandStack);

		addTypicalColumn("Entry Point",
				new SingleReferenceManipulator(CargoPackage.eINSTANCE.getCanalBookingSlot_EntryPoint(), scenarioEditingLocation.getReferenceValueProviderCache(), getEditingDomain()));
		addTypicalColumn("Canal", new SingleReferenceManipulator(CargoPackage.eINSTANCE.getCanalBookingSlot_Route(), scenarioEditingLocation.getReferenceValueProviderCache(), getEditingDomain()));
		addTypicalColumn("Date", new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getCanalBookingSlot_SlotDate(), getEditingDomain()));
		addTypicalColumn("Slot", new SingleReferenceManipulator(CargoPackage.eINSTANCE.getCanalBookingSlot_Slot(), scenarioEditingLocation.getReferenceValueProviderCache(), getEditingDomain()));

		defaultSetTitle("Canal Bookings");

		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.lingo.doc.Editor_CanalBookings");
	}

	public void setInput(CanalBookings canalBookingsModel) {

		if (this.canalBookingsModel != null) {
			this.canalBookingsModel.eAdapters().remove(changeListener);
		}

		this.canalBookingsModel = canalBookingsModel;
		getViewer().setInput(canalBookingsModel);
		if (canalBookingsModel != null) {
			strictEditor.setValue(canalBookingsModel.getStrictBoundaryOffsetDays());
			relaxedEditor.setValue(canalBookingsModel.getRelaxedBoundaryOffsetDays());
			flexEditor.setValue(canalBookingsModel.getFlexibleSlotAmount());
		} else {
			strictEditor.setValue(0);
			relaxedEditor.setValue(0);
			flexEditor.setValue(0);
		}

		if (this.canalBookingsModel != null) {
			this.canalBookingsModel.eAdapters().add(changeListener);
		}

	}

	@Override
	public void dispose() {

		if (this.canalBookingsModel != null) {
			this.canalBookingsModel.eAdapters().remove(changeListener);
		}
		this.canalBookingsModel = null;
		super.dispose();
	}
}

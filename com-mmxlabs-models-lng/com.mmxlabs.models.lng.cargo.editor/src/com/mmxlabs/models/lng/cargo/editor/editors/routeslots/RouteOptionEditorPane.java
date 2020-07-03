/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.editors.routeslots;

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.nebula.widgets.formattedtext.FormattedText;
import org.eclipse.nebula.widgets.formattedtext.IntegerFormatter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.license.features.NonLicenseFeatures;
import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.ui.editors.CanalEntryAttributeManipulator;
import com.mmxlabs.models.lng.port.ui.editors.PanamaOnlyRouteOptionAttributeManipulator;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.date.LocalDateTextFormatter;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.ReadOnlyManipulatorWrapper;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.rcp.common.ecore.SafeAdapterImpl;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class RouteOptionEditorPane extends ScenarioTableViewerPane {

	private static final Logger log = LoggerFactory.getLogger(RouteOptionEditorPane.class);
	private FormattedText strictEditor;
	private FormattedText relaxedEditor;
	// private FormattedText flexEditorNorthbound;
	private FormattedText flexEditorSouthbound;
	private FormattedText maxIdleNorthboundEditor;
	private FormattedText maxIdleSouthboundEditor;
	private FormattedText marginEditor;

	private Label todayLabel_Strict;
	private Label todayLabel_Relaxed;

	private LNGScenarioModel scenarioModel;
	private CanalBookings canalBookingsModel;

	private final @NonNull AdapterImpl changeListener = new SafeAdapterImpl() {
		
		@Override
		public void safeNotifyChanged(org.eclipse.emf.common.notify.Notification msg) {

			if (msg.getEventType() == Notification.REMOVING_ADAPTER) {
				return;
			}
			if (msg.getFeature() == CargoPackage.Literals.CANAL_BOOKINGS__NORTHBOUND_MAX_IDLE_DAYS) {
				maxIdleNorthboundEditor.setValue(msg.getNewValue());
				return;
			}
			if (msg.getFeature() == CargoPackage.Literals.CANAL_BOOKINGS__SOUTHBOUND_MAX_IDLE_DAYS) {
				maxIdleSouthboundEditor.setValue(msg.getNewValue());
				return;
			}
			if (!NonLicenseFeatures.isSouthboundIdleTimeRuleEnabled()) {
				if (msg.getFeature() == CargoPackage.Literals.CANAL_BOOKINGS__FLEXIBLE_BOOKING_AMOUNT_SOUTHBOUND) {
					flexEditorSouthbound.setValue(msg.getNewValue());
					return;
				}
			}
			if (msg.getFeature() == CargoPackage.Literals.CANAL_BOOKINGS__RELAXED_BOUNDARY_OFFSET_DAYS) {
				relaxedEditor.setValue(msg.getNewValue());
				return;
			}
			if (msg.getFeature() == CargoPackage.Literals.CANAL_BOOKINGS__STRICT_BOUNDARY_OFFSET_DAYS) {
				strictEditor.setValue(msg.getNewValue());
				return;
			}
			if (msg.getFeature() == CargoPackage.Literals.CANAL_BOOKINGS__ARRIVAL_MARGIN_HOURS) {
				marginEditor.setValue(msg.getNewValue());
				return;
			}
			if (msg.getFeature() == LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_PromptPeriodStart()) {
				final LocalDateTextFormatter formatter = new LocalDateTextFormatter();
				formatter.setValue(msg.getNewValue());
				String dateStr = formatter.getDisplayString();
				todayLabel_Relaxed.setText(dateStr);
				todayLabel_Strict.setText(dateStr);
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

			ExpandableComposite parametersExpandable = new ExpandableComposite(parent, ExpandableComposite.TWISTIE);
			parametersExpandable.setExpanded(false);
			parametersExpandable.setText("Panama bookings parameters");

			// final Group parametersParent = new Group(parent, SWT.NONE);
			final Composite parametersParent = new Composite(parametersExpandable, SWT.NONE);

			parametersExpandable.setClient(parametersParent);
			parametersExpandable.addExpansionListener(new ExpansionAdapter() {
				@Override
				public void expansionStateChanged(final ExpansionEvent e) {
					parametersParent.layout(true);
					parent.layout(true);
				}
			});
			parametersParent.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).spacing(0, 0).margins(0, 0).create());
			{
			}
			// Strict editor
			{

				final Label lbl = new Label(parametersParent, SWT.NONE);
				lbl.setText("Strict booking period ends ");
				lbl.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.CENTER).minSize(1000, -1).create());

				final Composite strictParent = new Composite(parametersParent, SWT.NONE);
				strictParent.setLayout(GridLayoutFactory.fillDefaults().numColumns(5).equalWidth(false).spacing(3, 0).margins(0, 7).create());

				strictEditor = new FormattedText(strictParent);
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
				final Label lbl2 = new Label(strictParent, SWT.NONE);
				lbl2.setText(" days after");
				lbl2.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).create());

				todayLabel_Strict = new Label(strictParent, SWT.NONE);
				todayLabel_Strict.setText("XX/XX/XXXX");
				todayLabel_Strict.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).minSize(1000, -1).create());
			}
			{

				final Label lbl = new Label(parametersParent, SWT.NONE);
				lbl.setText("Relaxed booking period ends ");
				lbl.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.CENTER).minSize(1000, -1).create());

				final Composite relaxParent = new Composite(parametersParent, SWT.NONE);
				relaxParent.setLayout(GridLayoutFactory.fillDefaults().numColumns(5).equalWidth(false).spacing(3, 0).margins(0, 7).create());

				relaxedEditor = new FormattedText(relaxParent);
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
				relaxedEditor.getControl().setToolTipText("Bookings should exist for the panama canal up to n days from the prompt start date and after the strict period.");

				final Label lbl3 = new Label(relaxParent, SWT.NONE);
				lbl3.setText(" days after");
				lbl3.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).create());

				todayLabel_Relaxed = new Label(relaxParent, SWT.NONE);
				todayLabel_Relaxed.setText("XX/XX/XXXX");
				todayLabel_Relaxed.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).minSize(1000, -1).create());
			}
			if (!NonLicenseFeatures.isSouthboundIdleTimeRuleEnabled())
			{
				//Only add it to the GUI if we are using old southbound rules.
				final Label lbl4 = new Label(parametersParent, SWT.NONE);
				lbl4.setText("Flexible bookings southbound ");
				lbl4.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).create());

				final Composite flexParent = new Composite(parametersParent, SWT.NONE);
				flexParent.setLayout(GridLayoutFactory.fillDefaults().numColumns(5).equalWidth(false).spacing(3, 0).margins(0, 7).create());

				flexEditorSouthbound = new FormattedText(flexParent);
				flexEditorSouthbound.setFormatter(new IntegerFormatter());
				// .setLayoutData(GridDataFactory.swtDefaults().minSize(1000, -1).create());
				flexEditorSouthbound.getControl().setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).hint(30, SWT.DEFAULT).create());
				flexEditorSouthbound.getControl().addModifyListener(new ModifyListener() {

					@Override
					public void modifyText(ModifyEvent e) {
						Object newValue = flexEditorSouthbound.getValue();
						if (canalBookingsModel != null && newValue instanceof Integer && !Objects.equals(newValue, canalBookingsModel.getFlexibleBookingAmountSouthbound())) {
							final Command cmd = SetCommand.create(getEditingDomain(), canalBookingsModel, CargoPackage.eINSTANCE.getCanalBookings_FlexibleBookingAmountSouthbound(), newValue);
							getEditingDomain().getCommandStack().execute(cmd);
						}
					}

				});
				flexEditorSouthbound.getControl().setToolTipText("Number of permitted panama voyages without a booking in the relaxed period.");
			}
			{

				// final Label lbl3 = new Label(parametersParent, SWT.NONE);
				// lbl3.setText("Flexible bookings northbound ");
				// lbl3.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.CENTER).create());
				//
				// final Composite flexParent = new Composite(parametersParent, SWT.NONE);
				// flexParent.setLayout(GridLayoutFactory.fillDefaults().numColumns(5).equalWidth(false).spacing(3, 0).margins(0, 7).create());
				//
				// flexEditorNorthbound = new FormattedText(flexParent);
				// flexEditorNorthbound.setFormatter(new IntegerFormatter());
				// // .setLayoutData(GridDataFactory.swtDefaults().minSize(1000, -1).create());
				// flexEditorNorthbound.getControl().setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).hint(30, SWT.DEFAULT).create());
				// flexEditorNorthbound.getControl().addModifyListener(new ModifyListener() {
				//
				// @Override
				// public void modifyText(ModifyEvent e) {
				// Object newValue = flexEditorNorthbound.getValue();
				// if (canalBookingsModel != null && newValue instanceof Integer && !Objects.equals(newValue, canalBookingsModel.getFlexibleBookingAmountNorthbound())) {
				// final Command cmd = SetCommand.create(getEditingDomain(), canalBookingsModel, CargoPackage.eINSTANCE.getCanalBookings_FlexibleBookingAmountNorthbound(), newValue);
				// getEditingDomain().getCommandStack().execute(cmd);
				// }
				// }
				//
				// });
				// flexEditorNorthbound.getControl().setToolTipText("Number of permitted panama voyages without a booking in the relaxed period.");

				final Label lbl3 = new Label(parametersParent, SWT.NONE);
				lbl3.setText("Northbound maximum idle");
				lbl3.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.CENTER).create());

				final Composite northParent = new Composite(parametersParent, SWT.NONE);
				northParent.setLayout(GridLayoutFactory.fillDefaults().numColumns(5).equalWidth(false).spacing(3, 0).margins(0, 7).create());

				maxIdleNorthboundEditor = new FormattedText(northParent);
				maxIdleNorthboundEditor.setFormatter(new IntegerFormatter());
				maxIdleNorthboundEditor.getControl().setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).hint(30, SWT.DEFAULT).create());
				maxIdleNorthboundEditor.getControl().addModifyListener(new ModifyListener() {

					@Override
					public void modifyText(ModifyEvent e) {
						Object newValue = maxIdleNorthboundEditor.getValue();
						if (canalBookingsModel != null && newValue instanceof Integer && !Objects.equals(newValue, canalBookingsModel.getNorthboundMaxIdleDays())) {
							final Command cmd = SetCommand.create(getEditingDomain(), canalBookingsModel, CargoPackage.eINSTANCE.getCanalBookings_NorthboundMaxIdleDays(), newValue);
							getEditingDomain().getCommandStack().execute(cmd);
						}
					}

				});
				maxIdleNorthboundEditor.getControl().setToolTipText("The amount of days a vessel will idle at the canal in order to try and get a spontanous booking.");

				final Label lbl3b = new Label(northParent, SWT.NONE);
				lbl3b.setText(" days");
				lbl3b.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).create());
				
				final Label lblS = new Label(parametersParent, SWT.NONE);
				lblS.setText("Southbound maximum idle");
				lblS.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.CENTER).create());

				final Composite southParent = new Composite(parametersParent, SWT.NONE);
				southParent.setLayout(GridLayoutFactory.fillDefaults().numColumns(5).equalWidth(false).spacing(3, 0).margins(0, 7).create());
			
				maxIdleSouthboundEditor = new FormattedText(southParent);
				maxIdleSouthboundEditor.setFormatter(new IntegerFormatter());
				maxIdleSouthboundEditor.getControl().setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).hint(30, SWT.DEFAULT).create());
				maxIdleSouthboundEditor.getControl().addModifyListener(new ModifyListener() {

					@Override
					public void modifyText(ModifyEvent e) {
						Object newValue = maxIdleSouthboundEditor.getValue();
						if (canalBookingsModel != null && newValue instanceof Integer && !Objects.equals(newValue, canalBookingsModel.getSouthboundMaxIdleDays())) {
							final Command cmd = SetCommand.create(getEditingDomain(), canalBookingsModel, CargoPackage.eINSTANCE.getCanalBookings_SouthboundMaxIdleDays(), newValue);
							getEditingDomain().getCommandStack().execute(cmd);
						}
					}

				});
				maxIdleSouthboundEditor.getControl().setToolTipText("The amount of days a vessel will idle at the canal in order to try and get a spontanous booking.");
	
				final Label lbl4b = new Label(southParent, SWT.NONE);
				lbl4b.setText(" days");
				lbl4b.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).create());
			}
			{

				final Label lbl5 = new Label(parametersParent, SWT.NONE);
				lbl5.setText("Arrival margin in hours ");
				lbl5.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.CENTER).create());

				final Composite marginParent = new Composite(parametersParent, SWT.NONE);
				marginParent.setLayout(GridLayoutFactory.fillDefaults().numColumns(5).equalWidth(false).spacing(3, 0).margins(0, 7).create());

				marginEditor = new FormattedText(marginParent);
				marginEditor.setFormatter(new IntegerFormatter());
				// .setLayoutData(GridDataFactory.swtDefaults().minSize(1000, -1).create());
				marginEditor.getControl().setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).hint(30, SWT.DEFAULT).create());
				marginEditor.getControl().addModifyListener(new ModifyListener() {

					@Override
					public void modifyText(ModifyEvent e) {
						Object newValue = marginEditor.getValue();
						if (canalBookingsModel != null && newValue instanceof Integer && !Objects.equals(newValue, canalBookingsModel.getArrivalMarginHours())) {
							final Command cmd = SetCommand.create(getEditingDomain(), canalBookingsModel, CargoPackage.eINSTANCE.getCanalBookings_ArrivalMarginHours(), newValue);
							getEditingDomain().getCommandStack().execute(cmd);
						}
					}

				});
				marginEditor.getControl().setToolTipText("The time in hours to arrive prior to 00:00 of the day of the slot.");
			}
		}

		return super.createViewer(parent);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);
		final LNGScenarioModel scenarioModel = (LNGScenarioModel) modelReference.getInstance();
		PortModel portModel = scenarioModel.getReferenceModel().getPortModel();
		addTypicalColumn("Entry Point", new CanalEntryAttributeManipulator(portModel, CargoPackage.eINSTANCE.getCanalBookingSlot_CanalEntrance(), getEditingDomain()));
		addTypicalColumn("Canal", new PanamaOnlyRouteOptionAttributeManipulator(CargoPackage.eINSTANCE.getCanalBookingSlot_RouteOption(), getEditingDomain()));
		addTypicalColumn("Date", new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getCanalBookingSlot_BookingDate(), getEditingDomain()));
		addTypicalColumn("Slot", new SingleReferenceManipulator(CargoPackage.eINSTANCE.getCanalBookingSlot_Slot(), scenarioEditingLocation.getReferenceValueProviderCache(), getEditingDomain()));
		addTypicalColumn("Notes", new ReadOnlyManipulatorWrapper<>(new BasicAttributeManipulator(CargoPackage.eINSTANCE.getCanalBookingSlot_Notes(), getEditingDomain())));

		defaultSetTitle("Canal Bookings");

		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.lingo.doc.Editor_CanalBookings");
	}

	public void setInput(LNGScenarioModel scenarioModel, CanalBookings canalBookingsModel) {

		if (this.scenarioModel != null) {
			this.scenarioModel.eAdapters().remove(changeListener);
		}
		if (this.canalBookingsModel != null) {
			this.canalBookingsModel.eAdapters().remove(changeListener);
		}

		this.scenarioModel = scenarioModel;
		this.canalBookingsModel = canalBookingsModel;
		getViewer().setInput(canalBookingsModel);
		if (canalBookingsModel != null) {
			strictEditor.setValue(canalBookingsModel.getStrictBoundaryOffsetDays());
			relaxedEditor.setValue(canalBookingsModel.getRelaxedBoundaryOffsetDays());
			maxIdleNorthboundEditor.setValue(canalBookingsModel.getNorthboundMaxIdleDays());
			maxIdleSouthboundEditor.setValue(canalBookingsModel.getSouthboundMaxIdleDays());
			if (!NonLicenseFeatures.isSouthboundIdleTimeRuleEnabled()) {
				flexEditorSouthbound.setValue(canalBookingsModel.getFlexibleBookingAmountSouthbound());
			}
			marginEditor.setValue(canalBookingsModel.getArrivalMarginHours());
			{
				final LocalDateTextFormatter formatter = new LocalDateTextFormatter();
				formatter.setValue(scenarioModel.getPromptPeriodStart());
				String dateStr = formatter.getDisplayString();
				todayLabel_Relaxed.setText(dateStr);
				todayLabel_Strict.setText(dateStr);
			}
		} else {
			strictEditor.setValue(0);
			relaxedEditor.setValue(0);
			maxIdleNorthboundEditor.setValue(0);
			maxIdleSouthboundEditor.setValue(0);
			if (!NonLicenseFeatures.isSouthboundIdleTimeRuleEnabled()) {
				flexEditorSouthbound.setValue(0);
			}
			marginEditor.setValue(0);
		}

		if (this.canalBookingsModel != null) {
			this.canalBookingsModel.eAdapters().add(changeListener);
		}
		if (this.scenarioModel != null) {
			this.scenarioModel.eAdapters().add(changeListener);
		}

	}

	@Override
	public void dispose() {

		if (this.canalBookingsModel != null) {
			this.canalBookingsModel.eAdapters().remove(changeListener);
		}
		this.canalBookingsModel = null;

		if (this.scenarioModel != null) {
			this.scenarioModel.eAdapters().add(changeListener);
		}
		this.scenarioModel = null;

		super.dispose();
	}
}

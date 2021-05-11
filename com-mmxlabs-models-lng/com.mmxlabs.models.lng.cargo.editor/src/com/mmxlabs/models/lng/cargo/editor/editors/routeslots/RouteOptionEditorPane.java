/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.editors.routeslots;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.ui.editors.CanalEntryAttributeManipulator;
import com.mmxlabs.models.lng.port.util.PortModelLabeller;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.date.LocalDateTextFormatter;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.EmbeddedDetailComposite;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.ReadOnlyManipulatorWrapper;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.models.ui.validation.IDetailConstraintStatus;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.models.ui.validation.IStatusProvider.IStatusChangedListener;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ecore.SafeAdapterImpl;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class RouteOptionEditorPane extends ScenarioTableViewerPane {

	private static final Logger log = LoggerFactory.getLogger(RouteOptionEditorPane.class);

	private Composite parametersParent;

	private EmbeddedDetailComposite bookingsParametersComposite;

	private LNGScenarioModel scenarioModel;
	private CanalBookings canalBookingsModel;

	private final @NonNull AdapterImpl changeListener = new SafeAdapterImpl() {

		@Override
		public void safeNotifyChanged(org.eclipse.emf.common.notify.Notification msg) {
			if (msg.getEventType() == Notification.REMOVING_ADAPTER) {
				return;
			}
		};
	};

	private IStatusChangedListener statusChangedListener;

	public RouteOptionEditorPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);

		statusChangedListener = (provider, status) -> {
			RunnerHelper.runNowOrAsync(() -> displayValidationStatus(status));
		};
	}

	private void displayValidationStatus(IStatus status) {

		/*
		 * this.validationStatus.setText("OK");
		 * this.validationStatus.setBackground(Display.getDefault().getSystemColor(SWT.
		 * COLOR_WHITE));
		 * 
		 * if (iStatus.isMultiStatus()) { final IStatus[] children =
		 * iStatus.getChildren(); for (final IStatus element : children) {
		 * displayValidationStatus(element); } }
		 * 
		 * if (iStatus instanceof IDetailConstraintStatus) { final
		 * IDetailConstraintStatus element = (IDetailConstraintStatus) iStatus;
		 * 
		 * final Collection<EObject> objects = element.getObjects();
		 * 
		 * if (objects.contains(this.canalBookingsModel)) {
		 * this.validationStatus.setText("Status: "+iStatus.getMessage()); int colour =
		 * SWT.COLOR_WHITE; if (iStatus.getSeverity() == IStatus.ERROR) { colour =
		 * SWT.COLOR_RED; } else if (iStatus.getSeverity() == IStatus.WARNING) { colour
		 * = SWT.COLOR_YELLOW; }
		 * 
		 * Color bgColor = Display.getDefault().getSystemColor(colour);
		 * this.bookingsParametersComposite.getComposite().setBackground(bgColor);
		 * this.validationStatus.setBackground(bgColor); } }
		 */
		int errorLevel = checkStatus(status, 0);
		int colour = SWT.COLOR_WHITE;
		if (errorLevel == IStatus.ERROR) {
			colour = SWT.COLOR_RED;
		} else if (errorLevel == IStatus.WARNING) {
			colour = SWT.COLOR_YELLOW;
		}
		Color bgColor = Display.getDefault().getSystemColor(colour);
		// this.bookingsParametersComposite.getComposite().setBackground(bgColor);
		// this.validationStatus.setBackground(bgColor);
	}

	protected int checkStatus(final IStatus status, int currentSeverity) {
		if (status.isMultiStatus()) {
			final IStatus[] children = status.getChildren();
			for (final IStatus element : children) {
				int severity = checkStatus(element, currentSeverity);
				currentSeverity = Math.max(severity, currentSeverity);
			}
		}

		if (status instanceof IDetailConstraintStatus) {
			final IDetailConstraintStatus element = (IDetailConstraintStatus) status;

			final Collection<EObject> objects = element.getObjects();

			if (objects.contains(this.canalBookingsModel)) {
				// Is severity worse, then note it
				if (element.getSeverity() > currentSeverity) {
					currentSeverity = Math.max(element.getSeverity(), currentSeverity);
				}
			}
		}

		return currentSeverity;
	}

	@Override
	public ScenarioTableViewer createViewer(Composite parent) {
		ExpandableComposite parametersExpandable = new ExpandableComposite(parent, ExpandableComposite.TWISTIE);
		parametersExpandable.setExpanded(false);
		parametersExpandable.setText("Panama bookings parameters");

		parametersParent = new Composite(parametersExpandable, SWT.NONE);
		parametersExpandable.setClient(parametersParent);
		parametersExpandable.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(final ExpansionEvent e) {
				parametersParent.layout(true);
				parent.layout(true);
			}
		});
		parametersParent.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(false).spacing(0, 0).margins(0, 0).create());

		// Required in case no default vessel group parameters set for example.
		// this.validationStatus = new Label(parametersParent, SWT.NONE);
		// this.validationStatus.setText("Init OK!");
		// this.validationStatus.setLayoutData(GridDataFactory.swtDefaults().align(SWT.LEFT,
		// SWT.CENTER).minSize(1000, 10).create());

		this.bookingsParametersComposite = new EmbeddedDetailComposite(parametersParent, scenarioEditingLocation);

		IStatusProvider statusProvider = scenarioEditingLocation.getStatusProvider();

		if (statusProvider != null) {
			statusProvider.addStatusChangedListener(statusChangedListener);
		}

		return super.createViewer(parent);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);
		final LNGScenarioModel scenarioModel = (LNGScenarioModel) modelReference.getInstance();
		PortModel portModel = scenarioModel.getReferenceModel().getPortModel();

		addTypicalColumn("Date", new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getCanalBookingSlot_BookingDate(), getCommandHandler()));
		addTypicalColumn("Direction", new CanalEntryAttributeManipulator(portModel, CargoPackage.eINSTANCE.getCanalBookingSlot_CanalEntrance(), getCommandHandler(), PortModelLabeller::getDirection));
		addTypicalColumn("Vessel", new SingleReferenceManipulator(CargoPackage.eINSTANCE.getCanalBookingSlot_Vessel(), scenarioEditingLocation.getReferenceValueProviderCache(), getCommandHandler()));
		addTypicalColumn("Booking Code",
				new SingleReferenceManipulator(CargoPackage.eINSTANCE.getCanalBookingSlot_BookingCode(), scenarioEditingLocation.getReferenceValueProviderCache(), getCommandHandler()));
		addTypicalColumn("Notes", new ReadOnlyManipulatorWrapper<>(new BasicAttributeManipulator(CargoPackage.eINSTANCE.getCanalBookingSlot_Notes(), getCommandHandler())));

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
			final LocalDateTextFormatter formatter = new LocalDateTextFormatter();
			formatter.setValue(scenarioModel.getPromptPeriodStart());
		}
		this.bookingsParametersComposite.setInput(canalBookingsModel);

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

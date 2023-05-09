/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.bindings.keys.IKeyLookup;
import org.eclipse.jface.bindings.keys.KeyLookupFactory;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.jface.gridviewer.GridViewerEditor;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.VesselUsageDistribution;
import com.mmxlabs.models.lng.adp.VesselUsageDistributionProfileConstraint;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselGroup;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.ColumnLabelProviderFactory;
import com.mmxlabs.models.ui.editorpart.DefaultStatusProvider;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerValidationSupport;
import com.mmxlabs.models.ui.tabular.GridViewerEditingSupport;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.manipulators.MultiAttributeInlineEditor;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

/**
 * Detail composite for vessel state attributes; adds an additional bit to the
 * bottom of the composite which contains a fuel curve table.
 * 
 * @author hinton
 * 
 */
public class VesselUsageProfileConstraintDetailComposite extends Composite implements IDisplayComposite {

	// private IDisplayComposite delegate;
	private ICommandHandler commandHandler;
	private GridTableViewer tableViewer;
	private EObjectTableViewerValidationSupport validationSupport;
	private MMXRootObject scenarioModel;

	private final Color errorColour = Display.getDefault().getSystemColor(SWT.COLOR_RED);
	private final Color warningColour = Display.getDefault().getSystemColor(SWT.COLOR_YELLOW);
	private final Color lockedColour = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);

	private DefaultStatusProvider statusProvider = new DefaultStatusProvider() {

		private IStatus status;

		@Override
		public void fireStatusChanged(final IStatus status) {
			this.status = status;
			super.fireStatusChanged(status);
		}

		@Override
		public IStatus getStatus() {
			return status;
		}
	};

	private Function<Object, Color> colourProvider = (element) -> {

		final IStatus s = validationSupport.getValidationErrors().get(element);
		if (s != null) {
			if (s.getSeverity() == IStatus.ERROR) {
				return errorColour;
			} else if (s.getSeverity() == IStatus.WARNING) {
				return warningColour;
			}
		}
		return null;

	};

	public VesselUsageProfileConstraintDetailComposite(final Composite parent, final int style, final FormToolkit toolkit) {
		super(parent, style);
		toolkit.adapt(this);
		setLayout(new GridLayout(1, false));
		setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		// delegate = new DefaultDetailComposite(this, style, toolkit);
		//
		// delegate.getComposite().setLayoutData(new
		// GridData(GridData.FILL_HORIZONTAL));

		tableViewer = new GridTableViewer(this, SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);

		final ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(tableViewer) {
			@Override
			protected boolean isEditorActivationEvent(final ColumnViewerEditorActivationEvent event) {
				final boolean activate = event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION //
						|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC //
						|| event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL;
				if (activate) {
					return true;
				}
				if (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED) {
					if (event.keyCode == KeyLookupFactory.getDefault().formalKeyLookup(IKeyLookup.ENTER_NAME)) {
						return true;
					}
				}
				return false;
			}
		};

		GridViewerEditor.create(tableViewer, actSupport, ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.KEYBOARD_ACTIVATION);

		final Grid table = tableViewer.getGrid();
		GridViewerHelper.configureLookAndFeel(tableViewer);

		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		validationSupport = new EObjectTableViewerValidationSupport(tableViewer);
		validationSupport.setStatusProvider(statusProvider);

		table.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).minSize(200, 200).create());
		final GridViewerColumn rangeColumn = new GridViewerColumn(tableViewer, SWT.NONE);
		rangeColumn.getColumn().setWidth(200);
		final GridViewerColumn slotsColumn = new GridViewerColumn(tableViewer, SWT.NONE);
		slotsColumn.getColumn().setWidth(50);

		GridViewerHelper.configureLookAndFeel(rangeColumn);
		GridViewerHelper.configureLookAndFeel(slotsColumn);

		rangeColumn.getColumn().setText("Vessels");
		slotsColumn.getColumn().setText("Slots");

		{
			rangeColumn.setEditingSupport(
					new GridViewerEditingSupport(tableViewer, table, () -> new MultiAttributeInlineEditor(ADPPackage.Literals.VESSEL_USAGE_DISTRIBUTION__VESSELS, commandHandler, a -> {
						return String.format(a instanceof VesselGroup ? "[G] %s" : "[V] %s", ((NamedObject) a).getName());
					}, () -> {
						final FleetModel fleetModel = ScenarioModelUtil.getFleetModel((LNGScenarioModel) scenarioModel);
						final List<Object> vesselsToAdd = new ArrayList<>();
						vesselsToAdd.addAll(fleetModel.getVessels());
						vesselsToAdd.addAll(fleetModel.getVesselGroups());
						return vesselsToAdd;
					})));
		}
		// Use hyphen for consecutive months, comma otherwise
		rangeColumn.setLabelProvider(ColumnLabelProviderFactory.make(VesselUsageDistribution.class, "", vesselDistribution -> {
			List<AVesselSet<Vessel>> vessels = vesselDistribution.getVessels();
			if(vesselDistribution.getVessels().isEmpty()) {
				return "";
			}
			StringBuilder builder = new StringBuilder();
			builder.append(vessels.get(0).getName());
			for (int i = 1; i < vessels.size(); i++) {
				builder.append(", " + vessels.get(i).getName());
			}
			return builder.toString();
		}, colourProvider));

		slotsColumn.setLabelProvider(ColumnLabelProviderFactory.make(VesselUsageDistribution.class, "", vesselDistribution -> {
			final int num = vesselDistribution.getCargoes();
			return String.format("%d", num);
		}, colourProvider));

		slotsColumn.setEditingSupport(new GridViewerEditingSupport(tableViewer, table, () -> new NumericAttributeManipulator(ADPPackage.Literals.VESSEL_USAGE_DISTRIBUTION__CARGOES, commandHandler)));

		tableViewer.setContentProvider(new IStructuredContentProvider() {

			@Override
			public Object[] getElements(final Object inputElement) {
				final VesselUsageDistributionProfileConstraint model = (VesselUsageDistributionProfileConstraint) inputElement;
				final List<?> list = (List<?>) model.eGet(ADPPackage.Literals.VESSEL_USAGE_DISTRIBUTION_PROFILE_CONSTRAINT__DISTRIBUTIONS);
				if (list == null) {
					return new Object[0];
				}
				return list.toArray();
			}
		});

		final Composite buttons = toolkit.createComposite(this);

		buttons.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false));
		final GridLayout buttonLayout = new GridLayout(7, false);
		buttons.setLayout(buttonLayout);
		buttonLayout.marginHeight = 0;
		buttonLayout.marginWidth = 0;

		final Button add = toolkit.createButton(buttons, null, SWT.NONE);
		{
			add.setImage(CommonImages.getImage(IconPaths.Plus, IconMode.Enabled));
		}
		add.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));

		add.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final VesselUsageDistribution newDistribution = createVesselUsageDistribution();

				commandHandler.handleCommand(
						AddCommand.create(commandHandler.getEditingDomain(), oldValue, ADPPackage.eINSTANCE.getVesselUsageDistributionProfileConstraint_Distributions(), newDistribution), oldValue,
						ADPPackage.eINSTANCE.getPeriodDistributionProfileConstraint_Distributions());
				tableViewer.refresh();
				tableViewer.setSelection(new StructuredSelection(newDistribution));
			}
		});

		final Button remove = toolkit.createButton(buttons, null, SWT.NONE);
		{
			remove.setImage(CommonImages.getImage(IconPaths.Delete, IconMode.Enabled));
		}
		remove.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
		remove.setEnabled(false);
		tableViewer.addSelectionChangedListener((event) -> remove.setEnabled(!event.getSelection().isEmpty()));

		remove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final ISelection sel = tableViewer.getSelection();
				if (sel.isEmpty())
					return;
				if (sel instanceof IStructuredSelection) {
					final VesselUsageDistribution fc = (VesselUsageDistribution) ((IStructuredSelection) sel).getFirstElement();
					commandHandler.handleCommand(RemoveCommand.create(commandHandler.getEditingDomain(), fc.eContainer(), fc.eContainingFeature(), fc), oldValue,
							ADPPackage.eINSTANCE.getPeriodDistributionProfileConstraint_Distributions());
					tableViewer.refresh();
				}
			}
		});

	}

	private VesselUsageDistribution createVesselUsageDistribution() {
		return ADPFactory.eINSTANCE.createVesselUsageDistribution();
	}

	@Override
	public Composite getComposite() {
		return this;
	}

	private VesselUsageDistributionProfileConstraint oldValue = null;
	private final @NonNull Adapter adapter = new EContentAdapter() {

		@Override
		public void notifyChanged(Notification notification) {
			super.notifyChanged(notification);
			if (!notification.isTouch()) {
				ViewerHelper.refresh(tableViewer, false);
			}

		};
	};

	void removeAdapter() {
		final VesselUsageDistributionProfileConstraint oldValue2 = oldValue;
		oldValue = null;
		if (oldValue2 != null) {
			oldValue2.eAdapters().remove(adapter);

			if (oldValue2.eContainer() != null) {
				oldValue2.eContainer().eAdapters().remove(adapter);
			}

		}
	}

	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject value, final Collection<EObject> range, final EMFDataBindingContext dbc) {
		removeAdapter();
		oldValue = (VesselUsageDistributionProfileConstraint) value;
		scenarioModel = root;
		tableViewer.setInput(value);

		value.eAdapters().add(adapter);
		if (value.eContainer() != null) {
			value.eContainer().eAdapters().add(adapter);
		}
	}

	@Override
	public void dispose() {
		removeAdapter();
		super.dispose();
	}

	@Override
	public void setCommandHandler(final ICommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}

	@Override
	public void displayValidationStatus(final IStatus status) {
		statusProvider.fireStatusChanged(status);
	}
	@Override
	public void setEditorWrapper(final IInlineEditorWrapper wrapper) {
	}

	@Override
	public boolean checkVisibility(final IDialogEditingContext context) {
		return true;
	}
}

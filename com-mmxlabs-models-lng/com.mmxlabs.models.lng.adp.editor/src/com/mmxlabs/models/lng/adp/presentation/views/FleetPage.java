/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.presentation.views;

import java.util.Iterator;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.EVesselTankState;
import com.mmxlabs.models.lng.cargo.EndHeelOptions;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.StartHeelOptions;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.ui.editorpart.AssignmentManipulator;
import com.mmxlabs.models.lng.cargo.ui.editorpart.VesselEventVesselsManipulator;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.port.ui.editorpart.MultiplePortReferenceManipulator;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editors.DetailToolbarManager;
import com.mmxlabs.models.ui.editors.EmbeddedDetailComposite;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.NonEditableColumn;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.BooleanAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateTimeAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.ReadOnlyManipulatorWrapper;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.actions.AbstractMenuLockableAction;

public class FleetPage extends ADPComposite {

	private ADPEditorData editorData;

	private SashForm mainComposite;
	private ScrolledComposite rhsScrolledComposite;
	private Composite rhsComposite;
	private EmbeddedDetailComposite detailComposite;
	private Runnable releaseAdaptersRunnable = null;

	private ScenarioTableViewer availabilityViewer;
	private ScenarioTableViewer eventsViewer;

	private Button updateFleetDatesButton;

	private Group availabilityGroup;
	private Group eventsGroup;

	private Action deleteAvailabilityAction;
	private Action deleteEventAction;

	private final AdapterImpl spotMarketsModelAdapter = new EContentAdapter() {
		@Override
		public void notifyChanged(final org.eclipse.emf.common.notify.Notification msg) {

			super.notifyChanged(msg);

			if (msg.isTouch()) {
				return;
			}

			if (msg.getFeature() == SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__CHARTER_IN_MARKETS
					|| msg.getNotifier() instanceof CharterInMarket) {
					RunnerHelper.asyncExec(() -> {
						updateDetailComposite();
				});
			} 
		}
	};
	
	
	public FleetPage(final Composite parent, final int style, final ADPEditorData editorData) {
		super(parent, style);
		this.editorData = editorData;
		this.setLayout(GridLayoutFactory.fillDefaults().numColumns(1).margins(0, 0).create());

		// Top Toolbar
		{
			final Composite toolbarComposite = new Composite(this, SWT.NONE);
			toolbarComposite.setLayout(GridLayoutFactory.fillDefaults() //
					.numColumns(5) //
					.equalWidth(false) //
					.create());

			{
				updateFleetDatesButton = new Button(toolbarComposite, SWT.PUSH);
				updateFleetDatesButton.setText("Update fleet dates");
				updateFleetDatesButton.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(final SelectionEvent e) {
						if (editorData.scenarioModel == null) {
							return;
						}
						final ADPModel adpModel = editorData.adpModel;
						if (adpModel == null) {
							return;
						}
						final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(editorData.scenarioModel);
						final CompoundCommand cmd = new CompoundCommand("Re-generate ADP fleet");
						{
							for (final VesselAvailability vesselAvailability : cargoModel.getVesselAvailabilities()) {
								final StartHeelOptions startOptions = CargoFactory.eINSTANCE.createStartHeelOptions();
								startOptions.setMinVolumeAvailable(0);
								startOptions.setMaxVolumeAvailable(vesselAvailability.getVessel().getSafetyHeel());
								startOptions.setCvValue(22.8);
								startOptions.setPriceExpression("0.0");
								cmd.append(SetCommand.create(editorData.getEditingDomain(), vesselAvailability, CargoPackage.Literals.VESSEL_AVAILABILITY__START_HEEL, startOptions));

								final EndHeelOptions endOptions = CargoFactory.eINSTANCE.createEndHeelOptions();
								endOptions.setMinimumEndHeel(vesselAvailability.getVessel().getSafetyHeel());
								endOptions.setMaximumEndHeel(vesselAvailability.getVessel().getSafetyHeel());
								endOptions.setTankState(EVesselTankState.EITHER);
								endOptions.setPriceExpression("0.0");
								cmd.append(SetCommand.create(editorData.getEditingDomain(), vesselAvailability, CargoPackage.Literals.VESSEL_AVAILABILITY__END_HEEL, endOptions));

								cmd.append(SetCommand.create(editorData.getEditingDomain(), vesselAvailability, CargoPackage.Literals.VESSEL_AVAILABILITY__START_AFTER,
										adpModel.getYearStart().atDay(1).atStartOfDay()));
								cmd.append(SetCommand.create(editorData.getEditingDomain(), vesselAvailability, CargoPackage.Literals.VESSEL_AVAILABILITY__START_BY,
										adpModel.getYearStart().atDay(1).atStartOfDay()));

								cmd.append(SetCommand.create(editorData.getEditingDomain(), vesselAvailability, CargoPackage.Literals.VESSEL_AVAILABILITY__END_AFTER,
										adpModel.getYearEnd().plusMonths(1).atDay(1).atStartOfDay()));
								cmd.append(SetCommand.create(editorData.getEditingDomain(), vesselAvailability, CargoPackage.Literals.VESSEL_AVAILABILITY__END_BY,
										adpModel.getYearEnd().plusMonths(1).atDay(1).atStartOfDay()));

							}

							editorData.getDefaultCommandHandler().handleCommand(cmd, cargoModel, null);
						}
						updateDetailPaneInput();
					}
				});
			}
		}

		mainComposite = new SashForm(this, SWT.HORIZONTAL);
		mainComposite.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_GRAY));
		mainComposite.setSashWidth(5);

		mainComposite.setLayoutData(GridDataFactory.fillDefaults()//
				.grab(true, true)//
				// .align(SWT.CENTER, SWT.TOP)//
				// .span(1, 1) //
				.create());
		mainComposite.setLayout(GridLayoutFactory.fillDefaults()//
				.equalWidth(true) //
				.numColumns(2) //
				.spacing(0, 0) //
				.create());

		{
			detailComposite = new EmbeddedDetailComposite(mainComposite, editorData);
		}
		{

			rhsScrolledComposite = new ScrolledComposite(mainComposite, SWT.V_SCROLL | SWT.V_SCROLL);
			rhsScrolledComposite.setLayoutData(GridDataFactory.fillDefaults()//
					.grab(false, true)//
					.hint(200, SWT.DEFAULT) //
					// .span(1, 1) //
					.align(SWT.FILL, SWT.FILL).create());

			rhsScrolledComposite.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));

			rhsScrolledComposite.setLayout(GridLayoutFactory.swtDefaults().margins(0, 0).create());
			rhsScrolledComposite.setExpandHorizontal(true);
			rhsScrolledComposite.setExpandVertical(true);
			// lhsScrolledComposite.setMinSize(400, 400);
			rhsComposite = new Composite(rhsScrolledComposite, SWT.NONE);
			// centralComposite.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));
			// centralComposite.setBackgroundMode(SWT.INHERIT_FORCE);
			rhsScrolledComposite.setBackgroundMode(SWT.INHERIT_FORCE);
			rhsScrolledComposite.setContent(rhsComposite);

			rhsComposite.setLayout(GridLayoutFactory.swtDefaults().margins(0, 0).create());
			{
				// Preview Table with generated options
				{

					availabilityGroup = new Group(rhsComposite, SWT.NONE);
					availabilityGroup.setLayout(new GridLayout(1, false));
					availabilityGroup.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_BOTH));

					availabilityGroup.setText("Fleet");
					// toolkit.adapt(previewGroup);

					if (editorData.getEditingDomain() != null) {
						// constructPreviewViewer(editorData, previewGroup);
					}

					final DetailToolbarManager buttonManager = new DetailToolbarManager(availabilityGroup, SWT.TOP);
					{
						final Action action = new Action("New") {

							@Override
							public void run() {
								final VesselAvailability availability = CargoFactory.eINSTANCE.createVesselAvailability();

								final StartHeelOptions startOptions = CargoFactory.eINSTANCE.createStartHeelOptions();
								startOptions.setMinVolumeAvailable(0);
								startOptions.setCvValue(22.8);
								startOptions.setPriceExpression("0.0");
								availability.setStartHeel(startOptions);

								final EndHeelOptions endOptions = CargoFactory.eINSTANCE.createEndHeelOptions();
								endOptions.setTankState(EVesselTankState.EITHER);
								endOptions.setPriceExpression("0.0");
								availability.setEndHeel(endOptions);

								availability.setStartAfter(editorData.adpModel.getYearStart().atDay(1).atStartOfDay());
								availability.setStartBy(editorData.adpModel.getYearStart().atDay(1).atStartOfDay());
								availability.setEndAfter(editorData.adpModel.getYearEnd().plusMonths(1).atDay(1).atStartOfDay());
								availability.setEndBy(editorData.adpModel.getYearEnd().plusMonths(1).atDay(1).atStartOfDay());

								final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(editorData.getScenarioDataProvider());
								if (commercialModel.getEntities().size() == 1) {
									availability.setEntity(commercialModel.getEntities().get(0));
								}
								final Command c = AddCommand.create(editorData.getEditingDomain(), editorData.getScenarioModel().getCargoModel(),
										CargoPackage.Literals.CARGO_MODEL__VESSEL_AVAILABILITIES, availability);
								editorData.getEditingDomain().getCommandStack().execute(c);
								DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(editorData, availability, editorData.getEditingDomain().getCommandStack().getMostRecentCommand());
								if (availabilityViewer != null) {
									availabilityViewer.refresh();
								}
							}
						};
						action.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
						buttonManager.getToolbarManager().add(action);
					}
					{

						deleteAvailabilityAction = new Action("Delete") {
							@Override
							public void run() {
								if (availabilityViewer != null) {
									final ISelection selection = availabilityViewer.getSelection();
									if (selection instanceof IStructuredSelection) {
										final IStructuredSelection iStructuredSelection = (IStructuredSelection) selection;
										final CompoundCommand c = new CompoundCommand();
										final Iterator<Object> itr = iStructuredSelection.iterator();
										while (itr.hasNext()) {
											final Object o = itr.next();
											c.append(RemoveCommand.create(editorData.getEditingDomain(), o));
										}
										editorData.getEditingDomain().getCommandStack().execute(c);
										availabilityViewer.refresh();
									}
								}
								;

							}
						};
						{
							Action packAction = new Action("Pack") {
								@Override
								public void run() {

									if (availabilityViewer != null && !availabilityViewer.getControl().isDisposed()) {
										final GridColumn[] columns = availabilityViewer.getGrid().getColumns();
										for (final GridColumn c : columns) {
											if (c.getResizeable()) {
												c.pack();
											}
										}
									}
								}
							};
							packAction.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.rcp.common", "/icons/pack.gif"));
							buttonManager.getToolbarManager().add(packAction);
						}
						deleteAvailabilityAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE));
						buttonManager.getToolbarManager().add(deleteAvailabilityAction);
					}
					buttonManager.getToolbarManager().update(true);
					// toolkit.adapt(removeButtonManager.getToolbarManager().getControl());
				}
				// Events
				{

					eventsGroup = new Group(rhsComposite, SWT.NONE);
					eventsGroup.setLayout(new GridLayout(1, false));
					eventsGroup.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_BOTH));

					eventsGroup.setText("Drydock events");
					// toolkit.adapt(previewGroup);

					if (editorData.getEditingDomain() != null) {
						// constructPreviewViewer(editorData, previewGroup);
					}

					final DetailToolbarManager buttonManager = new DetailToolbarManager(eventsGroup, SWT.TOP);

					if (false) {
						final AbstractMenuLockableAction menu = new AbstractMenuLockableAction("New") {
							@Override
							public void runWithEvent(Event e) {
								final IMenuCreator mc = getMenuCreator();
								if (mc != null) {
									final Menu m = mc.getMenu(buttonManager.getToolbarManager().getControl());
									if (m != null) {
										// position the menu below the drop down item
										final Point point = buttonManager.getToolbarManager().getControl().toDisplay(new Point(e.x, e.y));
										m.setLocation(point.x, point.y);
										m.setVisible(true);
										return;
									}
								}
							}

							@Override
							protected void populate(final Menu menu) {

								{
									final Action action = new Action("Charter out") {

										@Override
										public void run() {
											final CharterOutEvent event = CargoFactory.eINSTANCE.createCharterOutEvent();
											event.setAvailableHeel(CargoFactory.eINSTANCE.createStartHeelOptions());
											event.setRequiredHeel(CargoFactory.eINSTANCE.createEndHeelOptions());

											final Command c = AddCommand.create(editorData.getEditingDomain(), editorData.getScenarioModel().getCargoModel(),
													CargoPackage.Literals.CARGO_MODEL__VESSEL_EVENTS, event);
											editorData.getEditingDomain().getCommandStack().execute(c);
											DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(editorData, event, editorData.getEditingDomain().getCommandStack().getMostRecentCommand());
											if (eventsViewer != null) {
												eventsViewer.refresh();
											}
										}
									};
									final ActionContributionItem aci = new ActionContributionItem(action);
									aci.fill(menu, -1);
								}
								if (false) {
									final Action action = new Action("Drydock") {

										@Override
										public void run() {
											final DryDockEvent event = CargoFactory.eINSTANCE.createDryDockEvent();
											final Command c = AddCommand.create(editorData.getEditingDomain(), editorData.getScenarioModel().getCargoModel(),
													CargoPackage.Literals.CARGO_MODEL__VESSEL_EVENTS, event);
											editorData.getEditingDomain().getCommandStack().execute(c);
											DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(editorData, event, editorData.getEditingDomain().getCommandStack().getMostRecentCommand());
											if (eventsViewer != null) {
												eventsViewer.refresh();
											}
										}
									};
									final ActionContributionItem aci = new ActionContributionItem(action);
									aci.fill(menu, -1);
								}
								{
									final Action action = new Action("Maintenance") {

										@Override
										public void run() {
											final MaintenanceEvent event = CargoFactory.eINSTANCE.createMaintenanceEvent();
											final Command c = AddCommand.create(editorData.getEditingDomain(), editorData.getScenarioModel().getCargoModel(),
													CargoPackage.Literals.CARGO_MODEL__VESSEL_EVENTS, event);
											editorData.getEditingDomain().getCommandStack().execute(c);
											DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(editorData, event, editorData.getEditingDomain().getCommandStack().getMostRecentCommand());
											if (eventsViewer != null) {
												eventsViewer.refresh();
											}
										}
									};
									final ActionContributionItem aci = new ActionContributionItem(action);
									aci.fill(menu, -1);
								}

							}
						};
						menu.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
						buttonManager.getToolbarManager().add(menu);
					} else {
						final Action action = new Action("New") {

							@Override
							public void run() {
								final DryDockEvent event = CargoFactory.eINSTANCE.createDryDockEvent();
								final Command c = AddCommand.create(editorData.getEditingDomain(), editorData.getScenarioModel().getCargoModel(), CargoPackage.Literals.CARGO_MODEL__VESSEL_EVENTS,
										event);
								editorData.getEditingDomain().getCommandStack().execute(c);
								DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(editorData, event, editorData.getEditingDomain().getCommandStack().getMostRecentCommand());
								if (eventsViewer != null) {
									eventsViewer.refresh();
								}
							}
						};
						action.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
						buttonManager.getToolbarManager().add(action);
					}

					{

						deleteEventAction = new Action("Delete") {

							@Override
							public void run() {
								if (eventsViewer != null) {
									final ISelection selection = eventsViewer.getSelection();
									if (selection instanceof IStructuredSelection) {
										final IStructuredSelection iStructuredSelection = (IStructuredSelection) selection;
										final CompoundCommand c = new CompoundCommand();
										final Iterator<Object> itr = iStructuredSelection.iterator();
										while (itr.hasNext()) {
											final Object o = itr.next();
											c.append(RemoveCommand.create(editorData.getEditingDomain(), o));
										}
										editorData.getEditingDomain().getCommandStack().execute(c);
										eventsViewer.refresh();
									}
								}
								;

							}
						};
						{

							Action packAction = new Action("Pack") {
								@Override
								public void run() {

									if (eventsViewer != null && !eventsViewer.getControl().isDisposed()) {
										final GridColumn[] columns = eventsViewer.getGrid().getColumns();
										for (final GridColumn c : columns) {
											if (c.getResizeable()) {
												c.pack();
											}
										}
									}
								}
							};
							packAction.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.rcp.common", "/icons/pack.gif"));
							buttonManager.getToolbarManager().add(packAction);
						}
						deleteEventAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE));
						buttonManager.getToolbarManager().add(deleteEventAction);
					}
					buttonManager.getToolbarManager().update(true);
					// toolkit.adapt(removeButtonManager.getToolbarManager().getControl());
				}
			}
		}
	}

	private ScenarioTableViewer constructFleetViewer(final ADPEditorData editorData, final Group previewGroup) {

		final ScenarioTableViewer previewViewer = new ScenarioTableViewer(previewGroup, SWT.V_SCROLL | SWT.MULTI, editorData);
		previewViewer.init(editorData.getAdapterFactory(), editorData.getModelReference(), CargoPackage.Literals.CARGO_MODEL__VESSEL_AVAILABILITIES);
		GridViewerHelper.configureLookAndFeel(previewViewer);

		previewViewer.setStatusProvider(editorData.getStatusProvider());

		previewViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		// Enable tooltip support
		ColumnViewerToolTipSupport.enableFor(previewViewer);

		previewViewer.getGrid().setHeaderVisible(true);

		// previewViewer.setContentProvider(new ArrayContentProvider());
		final ReadOnlyManipulatorWrapper<BasicAttributeManipulator> nameManipulator = new ReadOnlyManipulatorWrapper<BasicAttributeManipulator>(
				new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), editorData.getEditingDomain()));

		previewViewer.addTypicalColumn("Name", nameManipulator, CargoPackage.eINSTANCE.getVesselAvailability_Vessel());

		previewViewer.addTypicalColumn("Fleet", new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_Fleet(), editorData.getEditingDomain()));

		previewViewer.addTypicalColumn("Optional", new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_Optional(), editorData.getEditingDomain()));

		previewViewer.addTypicalColumn("Charter", new BasicAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_TimeCharterRate(), editorData.getEditingDomain()));

		// addTypicalColumn("Repositioning Fee", new BasicAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_RepositioningFee(), editorData.getEditingDomain()) {
		// @Override
		// public boolean canEdit(Object object) {
		// if (object instanceof VesselAvailability) {
		// if (!((VesselAvailability) object).isFleet()) {
		// return true;
		// } else {
		// return false;
		// }
		// } else {
		// return super.canEdit(object);
		// }
		// }
		// });

		previewViewer.addTypicalColumn("Start Port",
				new SingleReferenceManipulator(CargoPackage.eINSTANCE.getVesselAvailability_StartAt(), editorData.getReferenceValueProviderCache(), editorData.getEditingDomain()));

		previewViewer.addTypicalColumn("Start After", new LocalDateTimeAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_StartAfter(), editorData.getEditingDomain()));

		previewViewer.addTypicalColumn("Start By", new LocalDateTimeAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_StartBy(), editorData.getEditingDomain()));

		previewViewer.addTypicalColumn("End Port", new MultiplePortReferenceManipulator(CargoPackage.eINSTANCE.getVesselAvailability_EndAt(), editorData.getReferenceValueProviderCache(),
				editorData.getEditingDomain(), MMXCorePackage.eINSTANCE.getNamedObject_Name()));

		previewViewer.addTypicalColumn("End After", new LocalDateTimeAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_EndAfter(), editorData.getEditingDomain()));

		previewViewer.addTypicalColumn("End By", new LocalDateTimeAttributeManipulator(CargoPackage.eINSTANCE.getVesselAvailability_EndBy(), editorData.getEditingDomain()));

		previewViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(final DoubleClickEvent event) {
				final ISelection selection = previewViewer.getSelection();
				if (selection instanceof IStructuredSelection) {
					final IStructuredSelection ss = (IStructuredSelection) selection;
					DetailCompositeDialogUtil.editSelection(editorData, ss);
					if (previewViewer != null) {
						previewViewer.refresh();
					}
				}
			}
		});
		previewViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(final SelectionChangedEvent event) {
				deleteAvailabilityAction.setEnabled(!event.getSelection().isEmpty());
			}
		});
		deleteAvailabilityAction.setEnabled(false);
		return previewViewer;
	}

	private ScenarioTableViewer constructEventsViewer(final ADPEditorData editorData, final Group previewGroup) {

		final ScenarioTableViewer previewViewer = new ScenarioTableViewer(previewGroup, SWT.V_SCROLL | SWT.MULTI, editorData);
		previewViewer.init(editorData.getAdapterFactory(), editorData.getModelReference(), CargoPackage.Literals.CARGO_MODEL__VESSEL_EVENTS);
		GridViewerHelper.configureLookAndFeel(previewViewer);

		previewViewer.setStatusProvider(editorData.getStatusProvider());

		previewViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		// Enable tooltip support
		ColumnViewerToolTipSupport.enableFor(previewViewer);

		previewViewer.getGrid().setHeaderVisible(true);

		// previewViewer.setContentProvider(new ArrayContentProvider());
		final ReadOnlyManipulatorWrapper<BasicAttributeManipulator> nameManipulator = new ReadOnlyManipulatorWrapper<BasicAttributeManipulator>(
				new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), editorData.getEditingDomain()));

		previewViewer.addTypicalColumn("Type", new NonEditableColumn() {
			@Override
			public String render(final Object object) {
				if (object instanceof CharterOutEvent) {
					return "Charter Out";
				} else if (object instanceof DryDockEvent) {
					return "Dry Dock";
				} else if (object instanceof MaintenanceEvent) {
					return "Maintenance Event";
				} else {
					return "Unknown Event";
				}
			}
		});

		previewViewer.addTypicalColumn("Name", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), editorData.getEditingDomain()));
		previewViewer.addTypicalColumn("Earliest Start", new LocalDateTimeAttributeManipulator(CargoPackage.eINSTANCE.getVesselEvent_StartAfter(), editorData.getEditingDomain()));
		previewViewer.addTypicalColumn("Latest Start", new LocalDateTimeAttributeManipulator(CargoPackage.eINSTANCE.getVesselEvent_StartBy(), editorData.getEditingDomain()));
		previewViewer.addTypicalColumn("Port",
				new SingleReferenceManipulator(CargoPackage.eINSTANCE.getVesselEvent_Port(), editorData.getReferenceValueProviderCache(), editorData.getEditingDomain()));
		previewViewer.addTypicalColumn("Duration", new NumericAttributeManipulator(CargoPackage.eINSTANCE.getVesselEvent_DurationInDays(), editorData.getEditingDomain()));
		previewViewer.addTypicalColumn("Vessel", new AssignmentManipulator(editorData));
		previewViewer.addTypicalColumn("Allowed Vessels", new VesselEventVesselsManipulator(CargoPackage.eINSTANCE.getVesselEvent_AllowedVessels(), editorData.getReferenceValueProviderCache(),
				editorData.getEditingDomain(), MMXCorePackage.eINSTANCE.getNamedObject_Name()));
		previewViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(final DoubleClickEvent event) {
				final ISelection selection = previewViewer.getSelection();
				if (selection instanceof IStructuredSelection) {
					final IStructuredSelection ss = (IStructuredSelection) selection;
					DetailCompositeDialogUtil.editSelection(editorData, ss);
					if (previewViewer != null) {
						previewViewer.refresh();
					}
				}
			}
		});
		previewViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(final SelectionChangedEvent event) {
				deleteEventAction.setEnabled(!event.getSelection().isEmpty());
			}
		});
		deleteEventAction.setEnabled(false);
		return previewViewer;
	}

	@Override
	public void refresh() {
		if (availabilityViewer != null) {
			availabilityViewer.refresh();
		}
		if (eventsViewer != null) {
			eventsViewer.refresh();
		}
	}

	@Override
	public synchronized void updateRootModel(@Nullable final LNGScenarioModel scenarioModel, @Nullable final ADPModel adpModel) {

		if (releaseAdaptersRunnable != null) {
			releaseAdaptersRunnable.run();
			releaseAdaptersRunnable = null;
		}
		if (availabilityViewer != null) {
			availabilityViewer.getControl().dispose();
			availabilityViewer.dispose();
			availabilityViewer = null;
		}
		if (eventsViewer != null) {
			eventsViewer.getControl().dispose();
			eventsViewer.dispose();
			eventsViewer = null;
		}
		if (scenarioModel != null) {
			availabilityViewer = constructFleetViewer(editorData, availabilityGroup);
			availabilityGroup.layout();

			eventsViewer = constructEventsViewer(editorData, eventsGroup);
			eventsGroup.layout();
			
			final SpotMarketsModel spotMarketsModel = ScenarioModelUtil.getSpotMarketsModel(scenarioModel);
			spotMarketsModel.eAdapters().add(spotMarketsModelAdapter);
			
			releaseAdaptersRunnable = () -> { 
				spotMarketsModel.eAdapters().remove(spotMarketsModelAdapter);
			};
		}

		// TODO: Attach adapter if needed.
		mainComposite.setVisible(adpModel != null);
		updateFleetDatesButton.setEnabled(adpModel != null);
		if (adpModel == null) {
			updateDetailPaneInput();
		} else {
			updateDetailPaneInput();
			detailComposite.getComposite().pack();
		}
	}

	@Override
	public void dispose() {

		if (releaseAdaptersRunnable != null) {
			releaseAdaptersRunnable.run();
			releaseAdaptersRunnable = null;
		}

		super.dispose();
	}

	private void updateDetailComposite() {
		EObject target = null;
		if (editorData.getAdpModel() != null) {
			target = editorData.getAdpModel().getFleetProfile();
		}
		detailComposite.setInput(target);
	}
	
	private void updateDetailPaneInput() {
		EObject target = null;
		if (editorData.getAdpModel() != null) {
			target = editorData.getAdpModel().getFleetProfile();
			availabilityViewer.setInput(editorData.getScenarioModel().getCargoModel());
			eventsViewer.setInput(editorData.getScenarioModel().getCargoModel());
		}

		detailComposite.setInput(target);
	}
}

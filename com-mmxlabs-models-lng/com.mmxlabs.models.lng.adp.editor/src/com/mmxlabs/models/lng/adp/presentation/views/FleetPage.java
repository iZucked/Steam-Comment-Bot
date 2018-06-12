package com.mmxlabs.models.lng.adp.presentation.views;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.FleetProfile;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.EVesselTankState;
import com.mmxlabs.models.lng.cargo.EndHeelOptions;
import com.mmxlabs.models.lng.cargo.StartHeelOptions;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.port.ui.editorpart.MultiplePortReferenceManipulator;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editors.DetailToolbarManager;
import com.mmxlabs.models.ui.editors.EmbeddedDetailComposite;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.BooleanAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateTimeAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.ReadOnlyManipulatorWrapper;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;

public class FleetPage extends ADPComposite {

	private ADPEditorData editorData;

	private SashForm mainComposite;
	private ScrolledComposite rhsScrolledComposite;
	private Composite rhsComposite;
	private EmbeddedDetailComposite detailComposite;
	private Runnable releaseAdaptersRunnable = null;

	private ScenarioTableViewer previewViewer;

	private Button generateButton;

	private Group previewGroup;

	private Action deleteSlotAction;

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
				generateButton = new Button(toolbarComposite, SWT.PUSH);
				generateButton.setText("Re-generate fleet");
				generateButton.addSelectionListener(new SelectionAdapter() {

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
						final EObject input = detailComposite.getInput();
						if (input instanceof FleetProfile) {
							final FleetProfile fleetProfile = (FleetProfile) input;
							final List<VesselAvailability> newAvailabilities = new LinkedList<>();
							for (final VesselAvailability vesselAvailability : cargoModel.getVesselAvailabilities()) {
								final VesselAvailability newAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
								newAvailability.setVessel(vesselAvailability.getVessel());
								newAvailability.setCharterNumber(vesselAvailability.getCharterNumber());
								newAvailability.setEntity(vesselAvailability.getEntity());
								if (vesselAvailability.isSetTimeCharterRate()) {
									newAvailability.setTimeCharterRate(vesselAvailability.getTimeCharterRate());
								}
								newAvailability.setFleet(vesselAvailability.isFleet());
								newAvailability.setOptional(vesselAvailability.isOptional());

								final StartHeelOptions startOptions = CargoFactory.eINSTANCE.createStartHeelOptions();
								startOptions.setMinVolumeAvailable(0);
								startOptions.setMaxVolumeAvailable(newAvailability.getVessel().getSafetyHeel());
								startOptions.setCvValue(22.8);
								startOptions.setPriceExpression("0.0");
								newAvailability.setStartHeel(startOptions);

								final EndHeelOptions endOptions = CargoFactory.eINSTANCE.createEndHeelOptions();
								endOptions.setMinimumEndHeel(newAvailability.getVessel().getSafetyHeel());
								endOptions.setMaximumEndHeel(newAvailability.getVessel().getSafetyHeel());
								endOptions.setTankState(EVesselTankState.EITHER);
								endOptions.setPriceExpression("0.0");
								newAvailability.setEndHeel(endOptions);

								newAvailability.setStartAfter(adpModel.getYearStart().atDay(1).atStartOfDay());
								newAvailability.setStartBy(adpModel.getYearStart().atDay(1).atStartOfDay());

								newAvailability.setEndAfter(adpModel.getYearEnd().atDay(1).atStartOfDay().plusMonths(3)); //ALEXTODO:undo
								newAvailability.setEndBy(adpModel.getYearEnd().atDay(1).atStartOfDay().plusMonths(3)); //ALEXTODO:undo

								newAvailabilities.add(newAvailability);
							}

							final CompoundCommand cmd = new CompoundCommand("Re-generate ADP fleet");
							if (!fleetProfile.getVesselAvailabilities().isEmpty()) {
								cmd.append(DeleteCommand.create(editorData.getEditingDomain(), fleetProfile.getVesselAvailabilities()));
							}
							if (!newAvailabilities.isEmpty()) {
								cmd.append(AddCommand.create(editorData.getEditingDomain(), fleetProfile, ADPPackage.Literals.FLEET_PROFILE__VESSEL_AVAILABILITIES, newAvailabilities));
							}
							editorData.getDefaultCommandHandler().handleCommand(cmd, fleetProfile, null);
						}
						updateDetailPaneInput(input);
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

					previewGroup = new Group(rhsComposite, SWT.NONE);
					previewGroup.setLayout(new GridLayout(1, false));
					previewGroup.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_BOTH));

					previewGroup.setText("Preview");
					// toolkit.adapt(previewGroup);

					if (editorData.getEditingDomain() != null) {
						// constructPreviewViewer(editorData, previewGroup);
					}

					final DetailToolbarManager buttonManager = new DetailToolbarManager(previewGroup, SWT.TOP);
					{
						final Action action = new Action("New") {

							@Override
							public void run() {
								VesselAvailability availability = CargoFactory.eINSTANCE.createVesselAvailability();
								availability.setStartHeel(CargoFactory.eINSTANCE.createStartHeelOptions());
								availability.setEndHeel(CargoFactory.eINSTANCE.createEndHeelOptions());
								CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(editorData.getScenarioDataProvider());
								if (commercialModel.getEntities().size() == 1) {
									availability.setEntity(commercialModel.getEntities().get(0));
								}
								Command c = AddCommand.create(editorData.getEditingDomain(), editorData.getAdpModel().getFleetProfile(), ADPPackage.Literals.FLEET_PROFILE__VESSEL_AVAILABILITIES,
										availability);
								editorData.getEditingDomain().getCommandStack().execute(c);
								DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(editorData, availability, editorData.getEditingDomain().getCommandStack().getMostRecentCommand());
								if (previewViewer != null) {
									previewViewer.refresh();
								}
							}
						};
						action.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
						buttonManager.getToolbarManager().add(action);
					}
					{

						deleteSlotAction = new Action("Delete") {
							@Override
							public void run() {
								if (previewViewer != null) {
									ISelection selection = previewViewer.getSelection();
									if (selection instanceof IStructuredSelection) {
										IStructuredSelection iStructuredSelection = (IStructuredSelection) selection;
										CompoundCommand c = new CompoundCommand();
										Iterator<Object> itr = iStructuredSelection.iterator();
										while (itr.hasNext()) {
											Object o = itr.next();
											c.append(RemoveCommand.create(editorData.getEditingDomain(), o));
										}
										editorData.getEditingDomain().getCommandStack().execute(c);
										previewViewer.refresh();
									}
								}
								;

							}
						};
						deleteSlotAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE));
						buttonManager.getToolbarManager().add(deleteSlotAction);
					}
					buttonManager.getToolbarManager().update(true);
					// toolkit.adapt(removeButtonManager.getToolbarManager().getControl());
				}
			}
		}
	}

	private ScenarioTableViewer constructPreviewViewer(final ADPEditorData editorData, final Group previewGroup) {

		final ScenarioTableViewer previewViewer = new ScenarioTableViewer(previewGroup, SWT.NONE, editorData);
		previewViewer.init(editorData.getAdapterFactory(), editorData.getModelReference(), ADPPackage.Literals.FLEET_PROFILE__VESSEL_AVAILABILITIES);
		GridViewerHelper.configureLookAndFeel(previewViewer);

		previewViewer.setStatusProvider(editorData.getStatusProvider());

		previewViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		// Enable tooltip support
		ColumnViewerToolTipSupport.enableFor(previewViewer);

		previewViewer.getGrid().setHeaderVisible(true);

		// previewViewer.setContentProvider(new ArrayContentProvider());
		ReadOnlyManipulatorWrapper<BasicAttributeManipulator> nameManipulator = new ReadOnlyManipulatorWrapper<BasicAttributeManipulator>(
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
				final ISelection selection = event.getSelection();
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
			public void selectionChanged(SelectionChangedEvent event) {
				deleteSlotAction.setEnabled(!event.getSelection().isEmpty());
			}
		});
		deleteSlotAction.setEnabled(false);
		return previewViewer;
	}

	@Override
	public void refresh() {
		if (previewViewer != null) {
			previewViewer.refresh();
		}
	}

	@Override
	public synchronized void updateRootModel(@Nullable final LNGScenarioModel scenarioModel, @Nullable final ADPModel adpModel) {

		if (releaseAdaptersRunnable != null) {
			releaseAdaptersRunnable.run();
			releaseAdaptersRunnable = null;
		}
		if (previewViewer != null) {
			previewViewer.getControl().dispose();
			previewViewer.dispose();
			previewViewer = null;
		}
		if (scenarioModel != null) {
			previewViewer = constructPreviewViewer(editorData, previewGroup);
			previewGroup.layout();
		}

		// TODO: Attach adapter if needed.
		mainComposite.setVisible(adpModel != null);
		generateButton.setEnabled(adpModel != null);
		if (adpModel == null) {
			updateDetailPaneInput(null);
		} else {
			updateDetailPaneInput(adpModel.getFleetProfile());
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

	private void updateDetailPaneInput(final EObject object) {
		EObject target = null;
		if (editorData.getAdpModel() != null) {
			target = editorData.getAdpModel().getFleetProfile();
			previewViewer.setInput(editorData.getAdpModel().getFleetProfile());
		}

		detailComposite.setInput(target);
	}
}

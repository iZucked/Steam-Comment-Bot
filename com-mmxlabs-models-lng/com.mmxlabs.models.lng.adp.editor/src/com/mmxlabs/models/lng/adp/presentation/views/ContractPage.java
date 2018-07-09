package com.mmxlabs.models.lng.adp.presentation.views;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.utils.ADPModelUtil;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.ui.editorpart.VolumeAttributeManipulator;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editors.DetailToolbarManager;
import com.mmxlabs.models.ui.editors.EmbeddedDetailComposite;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.TextualEnumAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.TextualSingleReferenceManipulator;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;

public class ContractPage extends ADPComposite {

	private ADPEditorData editorData;

	private SashForm mainComposite;
	private ScrolledComposite rhsScrolledComposite;
	private Composite rhsComposite;
	private EmbeddedDetailComposite detailComposite;
	private ComboViewer objectSelector;
	private Runnable releaseAdaptersRunnable = null;

	private ScenarioTableViewer previewViewer;

	public ContractPage(final Composite parent, final int style, final ADPEditorData editorData) {
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
				final Label lbl = new Label(toolbarComposite, SWT.NONE);
				lbl.setText("Contract");
				objectSelector = new ComboViewer(toolbarComposite, SWT.DROP_DOWN);
				objectSelector.getControl().setLayoutData(GridDataFactory.swtDefaults().hint(150, SWT.DEFAULT).create());
				objectSelector.setContentProvider(new ArrayContentProvider());
				objectSelector.setLabelProvider(new LabelProvider() {

					@Override
					public String getText(final Object element) {
						if (element instanceof PurchaseContract) {
							final PurchaseContract profile = (PurchaseContract) element;
							return String.format("%s (Purchase)", profile.getName());
						} else if (element instanceof SalesContract) {
							final SalesContract profile = (SalesContract) element;
							return String.format("%s (Sale)", profile.getName());
						}
						return super.getText(element);
					}
				});
				objectSelector.setInput(Collections.emptyList());
				objectSelector.addSelectionChangedListener(new ISelectionChangedListener() {

					@Override
					public void selectionChanged(final SelectionChangedEvent event) {
						final ISelection selection = event.getSelection();
						EObject target = null;
						if (selection instanceof IStructuredSelection) {
							final IStructuredSelection iStructuredSelection = (IStructuredSelection) selection;
							target = (EObject) iStructuredSelection.getFirstElement();
						}
						generateButton.setEnabled(target != null);
						updateDetailPaneInput(target);
					}
				});
			}
			{
				generateButton = new Button(toolbarComposite, SWT.PUSH);
				// btn.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).create());
				generateButton.setText("Re-generate slots");
				generateButton.setEnabled(false);
				generateButton.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(final SelectionEvent e) {

						EObject input = detailComposite.getInput();
						if (input instanceof PurchaseContractProfile) {
							final PurchaseContractProfile profile = (PurchaseContractProfile) input;
							final CompoundCommand cmd = new CompoundCommand("Re-generate ADP Slots");
							for (final SubContractProfile<?> sub : profile.getSubProfiles()) {
								if (!sub.getSlots().isEmpty()) {
									cmd.append(DeleteCommand.create(editorData.getEditingDomain(), sub.getSlots()));
								}
							}
							final Command populateModelCommand = ADPModelUtil.populateModel(editorData.getEditingDomain(), editorData.scenarioModel, editorData.adpModel, profile);
							if (populateModelCommand != null) {
								cmd.append(populateModelCommand);
								editorData.getDefaultCommandHandler().handleCommand(cmd, profile, null);
								input = profile.getContract();
							}
						} else if (input instanceof SalesContractProfile) {
							final SalesContractProfile profile = (SalesContractProfile) input;
							final CompoundCommand cmd = new CompoundCommand("Re-generate ADP Slots");

							for (final SubContractProfile<?> sub : profile.getSubProfiles()) {
								if (!sub.getSlots().isEmpty()) {
									cmd.append(DeleteCommand.create(editorData.getEditingDomain(), sub.getSlots()));
								}
							}
							final Command populateModelCommand = ADPModelUtil.populateModel(editorData.getEditingDomain(), editorData.scenarioModel, editorData.adpModel, profile);
							if (populateModelCommand != null) {
								cmd.append(populateModelCommand);
								editorData.getDefaultCommandHandler().handleCommand(cmd, profile, null);
								input = profile.getContract();
							}
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
					.align(SWT.FILL, SWT.FILL).create());

			rhsScrolledComposite.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));

			rhsScrolledComposite.setLayout(GridLayoutFactory.swtDefaults().margins(0, 0).create());
			rhsScrolledComposite.setExpandHorizontal(true);
			rhsScrolledComposite.setExpandVertical(true);
			rhsComposite = new Composite(rhsScrolledComposite, SWT.NONE);
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

					final DetailToolbarManager removeButtonManager = new DetailToolbarManager(previewGroup, SWT.TOP);

					deleteSlotAction = new Action("Delete") {
						@Override
						public void run() {
							if (previewViewer != null) {
								final ISelection selection = previewViewer.getSelection();
								if (selection instanceof IStructuredSelection) {
									final IStructuredSelection iStructuredSelection = (IStructuredSelection) selection;
									final CompoundCommand c = new CompoundCommand();
									final Iterator<?> itr = iStructuredSelection.iterator();
									while (itr.hasNext()) {
										final Object o = itr.next();
										c.append(DeleteCommand.create(editorData.getEditingDomain(), o));
									}
									editorData.getEditingDomain().getCommandStack().execute(c);
									updatePreviewPaneInput(detailComposite.getInput());
								}
							}
							;

						}
					};
					deleteSlotAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_DELETE));
					removeButtonManager.getToolbarManager().add(deleteSlotAction);
					removeButtonManager.getToolbarManager().update(true);
				}
			}
		}

	}

	private ScenarioTableViewer constructPreviewViewer(final ADPEditorData editorData, final Group previewGroup) {

		final ScenarioTableViewer previewViewer = new ScenarioTableViewer(previewGroup, SWT.V_SCROLL | SWT.MULTI, editorData);
		previewViewer.init(editorData.getAdapterFactory(), editorData.getModelReference(), new EReference[0]);
		previewViewer.setContentProvider(new ContentProvider());
		GridViewerHelper.configureLookAndFeel(previewViewer);

		previewViewer.setStatusProvider(editorData.getStatusProvider());

		previewViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		// Enable tooltip support
		ColumnViewerToolTipSupport.enableFor(previewViewer);

		previewViewer.getGrid().setHeaderVisible(true);

		previewViewer.addTypicalColumn("Name", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), editorData.getEditingDomain()));
		previewViewer.addTypicalColumn("Port",
				new TextualSingleReferenceManipulator(CargoPackage.eINSTANCE.getSlot_Port(), editorData.getReferenceValueProviderCache(), editorData.getEditingDomain()));

		// TODO: Groups
		final GridColumnGroup windowGroup = new GridColumnGroup(previewViewer.getGrid(), SWT.NONE);
		windowGroup.setText("Window");
		GridViewerHelper.configureLookAndFeel(windowGroup);
		previewViewer.addTypicalColumn("Date", windowGroup, new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowStart(), editorData.getEditingDomain()));
		previewViewer.addTypicalColumn("Time", windowGroup, new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowStartTime(), editorData.getEditingDomain()));
		previewViewer.addTypicalColumn("Size", windowGroup, new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowSize(), editorData.getEditingDomain()));
		previewViewer.addTypicalColumn("Units", windowGroup,
				new TextualEnumAttributeManipulator(CargoPackage.eINSTANCE.getSlot_WindowSizeUnits(), editorData.getEditingDomain(), (e) -> mapName((TimePeriod) e)));
		previewViewer.addTypicalColumn("Duration", windowGroup, new NumericAttributeManipulator(CargoPackage.eINSTANCE.getSlot_Duration(), editorData.getEditingDomain()));

		// TODO: Groups
		final GridColumnGroup quantityGroup = new GridColumnGroup(previewViewer.getGrid(), SWT.NONE);
		quantityGroup.setText("Quantity");
		GridViewerHelper.configureLookAndFeel(quantityGroup);
		previewViewer.addTypicalColumn("Min", quantityGroup, new VolumeAttributeManipulator(CargoPackage.eINSTANCE.getSlot_MinQuantity(), editorData.getEditingDomain()));
		previewViewer.addTypicalColumn("Max", quantityGroup, new VolumeAttributeManipulator(CargoPackage.eINSTANCE.getSlot_MaxQuantity(), editorData.getEditingDomain()));
		previewViewer.addTypicalColumn("Units", quantityGroup,
				new TextualEnumAttributeManipulator(CargoPackage.eINSTANCE.getSlot_VolumeLimitsUnit(), editorData.getEditingDomain(), (e) -> mapName((VolumeUnits) e)));

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
				deleteSlotAction.setEnabled(!event.getSelection().isEmpty());
			}
		});
		deleteSlotAction.setEnabled(false);
		return previewViewer;
	}

	@Override
	public void refresh() {
		ViewerHelper.refresh(previewViewer, true);
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
			rhsScrolledComposite.setMinSize(rhsComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		}
		final List<Object> objects = new LinkedList<>();
		if (scenarioModel != null && adpModel != null) {
			final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(scenarioModel);
			commercialModel.eAdapters().add(rootModelAdapter);

			releaseAdaptersRunnable = () -> commercialModel.eAdapters().remove(rootModelAdapter);

			objects.addAll(commercialModel.getPurchaseContracts());
			objects.addAll(commercialModel.getSalesContracts());
		}
		// Try to retain current selection
		final ISelection selection = new StructuredSelection(objectSelector.getSelection());
		objectSelector.setInput(objects);
		objectSelector.setSelection(selection);

		detailComposite.getComposite().setEnabled(adpModel != null);
		mainComposite.setVisible(adpModel != null);
		objectSelector.getControl().setEnabled(adpModel != null);
		if (adpModel == null) {
			updateDetailPaneInput(null);
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
		if (object instanceof PurchaseContract) {
			final PurchaseContract contract = (PurchaseContract) object;
			for (final PurchaseContractProfile profile : editorData.adpModel.getPurchaseContractProfiles()) {
				if (profile.getContract() == contract) {
					target = profile;
				}
			}
			if (target == null) {
				target = ADPModelUtil.createProfile(editorData.scenarioModel, editorData.adpModel, contract);
				if (target != null) {
					final CompoundCommand cmd = new CompoundCommand("Create ADP Profile");
					cmd.append(AddCommand.create(editorData.getEditingDomain(), editorData.adpModel, ADPPackage.eINSTANCE.getADPModel_PurchaseContractProfiles(), Collections.singletonList(target)));
					editorData.getEditingDomain().getCommandStack().execute(cmd);
				}
			}
		} else if (object instanceof SalesContract) {
			final SalesContract contract = (SalesContract) object;
			for (final SalesContractProfile profile : editorData.adpModel.getSalesContractProfiles()) {
				if (profile.getContract() == contract) {
					target = profile;
				}
			}
			if (target == null) {
				target = ADPModelUtil.createProfile(editorData.scenarioModel, editorData.adpModel, contract);
				if (target != null) {
					final CompoundCommand cmd = new CompoundCommand("Create ADP Profile");
					cmd.append(AddCommand.create(editorData.getEditingDomain(), editorData.adpModel, ADPPackage.eINSTANCE.getADPModel_SalesContractProfiles(), Collections.singletonList(target)));
					editorData.getEditingDomain().getCommandStack().execute(cmd);
				}
			}
		}

		detailComposite.setInput(target);
		updatePreviewPaneInput(target);
	}

	private void updatePreviewPaneInput(final EObject target) {
		if (previewViewer != null) {
			if (target instanceof PurchaseContractProfile) {
				final PurchaseContractProfile purchaseContractProfile = (PurchaseContractProfile) target;
				final List<Object> o = new LinkedList<>();
				for (final SubContractProfile<?> p : purchaseContractProfile.getSubProfiles()) {
					o.addAll(p.getSlots());
				}
				previewViewer.setInput(o);
			} else if (target instanceof SalesContractProfile) {
				final SalesContractProfile salesContractProfile = (SalesContractProfile) target;
				final List<Object> o = new LinkedList<>();
				for (final SubContractProfile<?> p : salesContractProfile.getSubProfiles()) {
					o.addAll(p.getSlots());
				}
				previewViewer.setInput(o);
			}
		}
	}

	private final AdapterImpl rootModelAdapter = new EContentAdapter() {
		@Override
		public void notifyChanged(final org.eclipse.emf.common.notify.Notification msg) {

			super.notifyChanged(msg);

			if (msg.isTouch()) {
				return;
			}

			if (msg.getFeature() == CommercialPackage.Literals.COMMERCIAL_MODEL__PURCHASE_CONTRACTS //
					|| msg.getFeature() == CommercialPackage.Literals.COMMERCIAL_MODEL__SALES_CONTRACTS) {
				final CommercialModel commercialModel = (CommercialModel) msg.getNotifier();
				final List<Object> objects = new LinkedList<>();
				objects.addAll(commercialModel.getPurchaseContracts());
				objects.addAll(commercialModel.getSalesContracts());

				RunnerHelper.runNowOrAsync(() -> objectSelector.setInput(objects));
			} else if (msg.getNotifier() instanceof Contract) {
				RunnerHelper.runNowOrAsync(() -> objectSelector.refresh(true));
			}
		}
	};

	private Button generateButton;

	private Group previewGroup;

	private Action deleteSlotAction;

	private static String mapName(final VolumeUnits units) {

		switch (units) {
		case M3:
			return "m³";
		case MMBTU:
			return "mmBtu";
		}
		return units.getName();
	}

	private static String mapName(final TimePeriod units) {

		switch (units) {
		case DAYS:
			return "Days";
		case HOURS:
			return "Hours";
		case MONTHS:
			return "Months";
		default:
			break;

		}
		return units.getName();
	}

	private class ContentProvider extends ArrayContentProvider implements ITreeContentProvider {

		@Override
		public Object[] getChildren(final Object parentElement) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getParent(final Object element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean hasChildren(final Object element) {
			// TODO Auto-generated method stub
			return false;
		}

	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.risk;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.formattedtext.LongFormatter;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.PropertySheet;
import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.eclipse.EclipseRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.common.Pair;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DealSet;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.DefaultMenuCreatorAction;
import com.mmxlabs.models.lng.cargo.util.IExposuresCustomiser;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.PaperDealAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.ui.actions.AddModelAction;
import com.mmxlabs.models.lng.ui.actions.ScenarioModifyingAction;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.date.LocalDateTextFormatter;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.manipulators.BooleanFlagAttributeManipulator;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.ScenarioLock;

public class DealSetsPane extends ScenarioTableViewerPane implements org.eclipse.e4.ui.workbench.modeling.ISelectionListener {

	private final IScenarioEditingLocation jointModelEditor;
	
	@Inject
	private Iterable<IExposuresCustomiser> exposuresCustomisers;

	public DealSetsPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
		this.jointModelEditor = location;

		final BundleContext bc = FrameworkUtil.getBundle(DealSetsPane.class).getBundleContext();
		final Injector injector = Guice.createInjector(Peaberry.osgiModule(bc, EclipseRegistry.eclipseRegistry()), new DealSetsEditorProviderModule());
		injector.injectMembers(this);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);
		
		final GridViewerColumn gtc = addNameManipulator("Name");
		gtc.getColumn().setTree(true);
		
		addColumn("Earliest Date", dealSetStartDateFormatter(), null);
		addColumn("Estimated P&L", dealSetPNLEstimateFormatter(), null);
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_INDIVIDUAL_EXPOSURES)) {
			addTypicalColumn("Exposure", new BooleanFlagAttributeManipulator(CargoPackage.eINSTANCE.getDealSet_AllowExposure(),//
					getCommandHandler()) {
				@Override
				public void doSetValue(final Object object, final Object value) {
					if (object instanceof DealSet) {
						super.doSetValue(object, value);
					}
				}
				
				@Override
				public Object getValue(final Object object) {
					if (object instanceof DealSet) {
						return super.getValue(object);
					}
					return false;
				}
			});
			addTypicalColumn("Hedging", new BooleanFlagAttributeManipulator(CargoPackage.eINSTANCE.getDealSet_AllowHedging(),//
					getCommandHandler()) {
				@Override
				public void doSetValue(final Object object, final Object value) {
					if (object instanceof DealSet) {
						super.doSetValue(object, value);
					}
				}
				@Override
				public Object getValue(final Object object) {
					if (object instanceof DealSet) {
						return super.getValue(object);
					}
					return false;
				}
			});
		}
		
		{
			final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
			final DealSetDropTargetListener listener = new DealSetDropTargetListener(scenarioEditingLocation, getScenarioViewer());

			final DropTarget dropTarget = new DropTarget(viewer.getControl(), DND.DROP_MOVE);
			dropTarget.setTransfer(types);
			dropTarget.addDropListener(listener);
		}

		setTitle("Deal Sets");
		if (getScenarioViewer() != null) {
			getScenarioViewer().setContentProvider(new SimpleTabularReportContentProvider<DealSet>());
		}
		
		final ESelectionService service = part.getSite().getService(ESelectionService.class);
		service.addPostSelectionListener(this);
	}
	
	private ICellRenderer dealSetStartDateFormatter() {
		return new ICellRenderer() {

			@Override
			public Comparable getComparable(Object object) {
				return render(object);
			}

			@Override
			public @Nullable String render(Object object) {
				if (object instanceof final DealSet ds) {
					LocalDate earliestSlot = LocalDate.MAX;
					if (!ds.getSlots().isEmpty()) {
						earliestSlot = ds.getSlots().stream().map(Slot::getWindowStart).min(LocalDate::compareTo).get();
					}
					LocalDate earliestPaper = LocalDate.MAX;
					if (!ds.getPaperDeals().isEmpty()) {
						earliestPaper = ds.getPaperDeals().stream().map(PaperDeal::getPricingPeriodStart).min(LocalDate::compareTo).get();
					}
					final int res = earliestSlot.compareTo(earliestPaper);
					final LocalDateTextFormatter formatter = new LocalDateTextFormatter();
					if ((res == 0 && earliestSlot != LocalDate.MAX) || res < 0) {
						formatter.setValue(earliestSlot);
						return formatter.getDisplayString();
					} else if (res > 0) {
						formatter.setValue(earliestPaper);
						return formatter.getDisplayString();
					}
				}
				return "";
			}

			@Override
			public boolean isValueUnset(Object object) {
				return false;
			}

			@Override
			public @Nullable Object getFilterValue(Object object) {
				return null;
			}

			@Override
			public @Nullable Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(Object object) {
				return null;
			}
		};
	}
	
	private ICellRenderer dealSetPNLEstimateFormatter() {
		return new ICellRenderer() {

			@Override
			public Comparable getComparable(Object object) {
				return render(object);
			}

			@Override
			public @Nullable String render(Object object) {
				if (jointModelEditor != null) {
					final IScenarioDataProvider sdp = jointModelEditor.getScenarioDataProvider();
					final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(sdp);
					if (scheduleModel != null) {
						final Schedule schedule = scheduleModel.getSchedule();
						if (schedule != null) {
							long totalPNL = 0;
							if (object instanceof final DealSet ds) {
								
								if (schedule.getSlotAllocations() != null && !ds.getSlots().isEmpty()) {
									final List<SlotAllocation> salist = schedule.getSlotAllocations()
											.stream().filter(sa -> ds.getSlots().contains(sa.getSlot())).collect(Collectors.toList());
									for (final SlotAllocation sa : salist) {
										if (sa.getSlot() instanceof LoadSlot) {
											totalPNL -= sa.getVolumeValue();
										}
										if (sa.getSlot() instanceof DischargeSlot) {
											totalPNL += sa.getVolumeValue();
										}
									}
								}
								if (schedule.getPaperDealAllocations() != null && !ds.getPaperDeals().isEmpty()) {
									final List<PaperDealAllocation> pdalist = schedule.getPaperDealAllocations()
											.stream().filter(pda -> ds.getPaperDeals().contains(pda.getPaperDeal())).collect(Collectors.toList());
									for(final PaperDealAllocation pda : pdalist) {
										totalPNL += pda.getGroupProfitAndLoss().getProfitAndLoss();
									}
								}
								
							} else if (object instanceof final Slot ls) {
								if (schedule.getSlotAllocations() != null) {
									final List<SlotAllocation> salist = schedule.getSlotAllocations()
											.stream().filter(sa -> ls == sa.getSlot()).collect(Collectors.toList());
									for (final SlotAllocation sa : salist) {
										if (sa.getSlot() instanceof LoadSlot) {
											totalPNL -= sa.getVolumeValue();
										}
										if (sa.getSlot() instanceof DischargeSlot) {
											totalPNL += sa.getVolumeValue();
										}
									}
								}
							} else if (object instanceof final PaperDeal pd) {
								if (schedule.getPaperDealAllocations() != null) {
									final List<PaperDealAllocation> pdalist = schedule.getPaperDealAllocations()
											.stream().filter(pda -> pd == pda.getPaperDeal()).collect(Collectors.toList());
									for(final PaperDealAllocation pda : pdalist) {
										totalPNL += pda.getGroupProfitAndLoss().getProfitAndLoss();
									}
								}
							}
							
							final LongFormatter inner = new LongFormatter();
							inner.setValue(totalPNL);
							return inner.getDisplayString();
						}
					}
				}
				return "";
			}

			@Override
			public boolean isValueUnset(Object object) {
				return false;
			}

			@Override
			public @Nullable Object getFilterValue(Object object) {
				return null;
			}

			@Override
			public @Nullable Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(Object object) {
				return null;
			}
		};
	}
	
	public class SimpleTabularReportContentProvider<T> implements ITreeContentProvider {

		@Override
		public Object[] getElements(final Object inputElement) {

			if (inputElement instanceof final Collection<?> collection) {
				return collection.toArray();
			}
			if (inputElement instanceof final LNGScenarioModel scenarioModel) {
				return getElements(ScenarioModelUtil.getCargoModel(scenarioModel));
			}
			if (inputElement instanceof final CargoModel cargoModel) {
				final List<DealSet> dealSets = cargoModel.getDealSets();
				if (dealSets != null) {
					return dealSets.toArray();
				}
			}
			return new Object[0];
		}

		@Override
		public void dispose() {
			
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof final Collection<?> collection) {
				return collection.toArray();
			}
			if (parentElement instanceof final DealSet ds) {
				boolean goAhead = false;
				final List<Object> objects = new ArrayList<Object>();
				if (ds.getSlots() != null) {
					objects.addAll(ds.getSlots());
					goAhead = true;
				}
				if (ds.getPaperDeals() != null) {
					objects.addAll(ds.getPaperDeals());
					goAhead = true;
				}
				if (goAhead) {
					return objects.toArray();
				}
			}
			return null;
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return element instanceof Collection<?> || (element instanceof DealSet && ((((DealSet) element).getSlots() != null) || (((DealSet) element).getPaperDeals() != null)));
		}
		
		@Override
		public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
			
			if (oldInput instanceof LNGScenarioModel) {
				final CargoModel cargoModel = ((LNGScenarioModel)oldInput).getCargoModel();
				((EObjectTableViewer)viewer).setCurrentContainerAndContainment(cargoModel, null);
			}
			if (newInput instanceof LNGScenarioModel) {
				final CargoModel cargoModel = ((LNGScenarioModel)newInput).getCargoModel();
				((EObjectTableViewer)viewer).setCurrentContainerAndContainment(cargoModel, null);
			}
		}
	}
	
	@Override
	protected Action createDeleteAction(@Nullable final Function<Collection<?>, Collection<Object>> callback) {
		return new ScenarioModifyingAction("Delete") {
			{
				setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
				setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
				viewer.addSelectionChangedListener(this);
			}

			@Override
			public void run() {

				// Delete commands can be slow, so show the busy indicator while deleting.
				final Runnable runnable = new Runnable() {

					@Override
					public void run() {

						final ScenarioLock editorLock = scenarioEditingLocation.getEditorLock();
						editorLock.lock();
						try {
							getScenarioEditingLocation().setDisableUpdates(true);
							
							if (getLastSelection() instanceof final TreeSelection ts) {
								final TreePath[] tp = ts.getPaths();
								if (tp.length == 0) return;
								final EditingDomain ed = scenarioEditingLocation.getEditingDomain();
								final List<DealSet> dealsToDelete = new ArrayList<>();
								final Map<DealSet, List<Object>> featuresToRemove = new HashMap<DealSet, List<Object>>();
								for (final TreePath tpo :tp) {
									final int cnt = tpo.getSegmentCount();
									if (cnt == 1 && tpo.getSegment(0) instanceof DealSet) {
										dealsToDelete.add((DealSet)tpo.getSegment(0));
									} else {
										List<Object> a = featuresToRemove.computeIfAbsent((DealSet)tpo.getSegment(0), k -> new ArrayList<Object>());
										a.add(tpo.getSegment(1));
									}
								}
								dealsToDelete.forEach(featuresToRemove::remove);
								final CompoundCommand cmd = new CompoundCommand();
								if (!dealsToDelete.isEmpty()) {
									cmd.append(DeleteCommand.create(ed, dealsToDelete));
								}
								if (!featuresToRemove.isEmpty()) {
									for(Map.Entry<DealSet, List<Object>> entry : featuresToRemove.entrySet()) {
										for (final Object value : entry.getValue()) {
											if (value instanceof Slot) {
												cmd.append(RemoveCommand.create(ed, entry.getKey(), CargoPackage.Literals.DEAL_SET__SLOTS, value));
											} else {
												cmd.append(RemoveCommand.create(ed, entry.getKey(), CargoPackage.Literals.DEAL_SET__PAPER_DEALS, value));
											}
										}
									}
								}
								if(!cmd.isEmpty()) {
									scenarioEditingLocation.getDefaultCommandHandler().handleCommand(cmd, null, null);
								}
							}
						} finally {
							editorLock.unlock();
							getScenarioEditingLocation().setDisableUpdates(false);
						}
					}

				};
				BusyIndicator.showWhile(null, runnable);
			}

			@Override
			protected boolean isApplicableToSelection(final ISelection selection) {
				return selection.isEmpty() == false && selection instanceof IStructuredSelection;
			}
		};
	}
	
	@Override
	protected Action createAddAction(final EReference containment) {
		final List<Action> actionList = new LinkedList<>();
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_DEAL_SETS_GENERATE_FROM_CARGOES)) {
			actionList.add(createDealSetsFromCargoesAction());
		}
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_DEAL_SETS_GENERATE_FROM_CONTRACTS)) {
			actionList.add(createDealSetsFromContractsAction());
		}
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_DEAL_SETS_GENERATE_FROM_CURVES)) {
			actionList.add(createDealSetsFromCurvesMenu());
		}
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_DEAL_SETS_GENERATE_FROM_INDICES)) {
			actionList.add(createDealSetsFromIndicesMenu());
		}
		final Action[] extraActions = actionList.toArray(new Action[0]);
		return AddModelAction.create(containment.getEReferenceType(), getAddContext(containment), extraActions);
	}
	
	private Action createDealSetsFromContractsAction() {
		return new DefaultMenuCreatorAction("From contracts") {
			
			@Override
			protected void populate(Menu menu) {
				final Action generateFromPurchaseContractsAction = DealSetActionUtils.createFromPurchaseContractsAction(jointModelEditor.getScenarioDataProvider(),
						scenarioEditingLocation.getEditingDomain(), scenarioEditingLocation.getDefaultCommandHandler());
				final Action generateFromSalesContractsAction = DealSetActionUtils.createFromSalesContractsAction(jointModelEditor.getScenarioDataProvider(),
						scenarioEditingLocation.getEditingDomain(), scenarioEditingLocation.getDefaultCommandHandler());
				addActionToMenu(generateFromPurchaseContractsAction, menu);
				addActionToMenu(generateFromSalesContractsAction, menu);
			}
		};
	}
	
	private Action createDealSetsFromCargoesAction() {
		return new DefaultMenuCreatorAction("From cargoes") {
			
			@Override
			protected void populate(Menu menu) {
				addActionToMenu(DealSetActionUtils.createFromCargoesWithoutPapersAction(jointModelEditor.getScenarioDataProvider(),
						scenarioEditingLocation.getEditingDomain(), scenarioEditingLocation.getDefaultCommandHandler()), menu);
				addActionToMenu(DealSetActionUtils.createFromCargoesAndUpdateWithPaperDealCommentsAction(jointModelEditor.getScenarioDataProvider(),
						scenarioEditingLocation.getEditingDomain(), scenarioEditingLocation.getDefaultCommandHandler()), menu);
			}
		};
	}
	
	private Action createDealSetsFromCurvesMenu() {
		return new DefaultMenuCreatorAction("From curves") {

			@Override
			protected void populate(Menu menu) {
				final Action generateByMonthAction = DealSetActionUtils.createFromCurvesByMonthAction(jointModelEditor.getScenarioDataProvider(),
						scenarioEditingLocation.getEditingDomain(), scenarioEditingLocation.getDefaultCommandHandler(), exposuresCustomisers);
				final Action generateByQuarterAction = DealSetActionUtils.createFromCurvesByQuarterAction(jointModelEditor.getScenarioDataProvider(),
						scenarioEditingLocation.getEditingDomain(), scenarioEditingLocation.getDefaultCommandHandler(), exposuresCustomisers);
				addActionToMenu(generateByMonthAction, menu);
				addActionToMenu(generateByQuarterAction, menu);
			}
		};
	}

	private Action createDealSetsFromIndicesMenu() {
		return new DefaultMenuCreatorAction("From indices") {

			@Override
			protected void populate(Menu menu) {
				final Action generateByMonthAction = DealSetActionUtils.createFromIndicesByMonthAction(jointModelEditor.getScenarioDataProvider(),
						scenarioEditingLocation.getEditingDomain(), scenarioEditingLocation.getDefaultCommandHandler(), exposuresCustomisers);
				final Action generateByQuarterAction = DealSetActionUtils.createFromIndicesByQuarterAction(jointModelEditor.getScenarioDataProvider(),
						scenarioEditingLocation.getEditingDomain(), scenarioEditingLocation.getDefaultCommandHandler(), exposuresCustomisers);
				addActionToMenu(generateByMonthAction, menu);
				addActionToMenu(generateByQuarterAction, menu);
			}
		};
	}
	
	@Override
	public void dispose() {
		final ESelectionService service = part.getSite().getService(ESelectionService.class);
		service.removePostSelectionListener(this);

		super.dispose();
	}

	@Override
	public void selectionChanged(MPart part, Object selection) {
		final IWorkbenchPart e3Part = SelectionHelper.getE3Part(part);
		if (e3Part != null) {
			if (e3Part == this) {
				return;
			}
			if (e3Part instanceof PropertySheet) {
				return;
			}
			if (e3Part instanceof JointModelEditorPart) {
				return;
			}
		}
		SelectionHelper.adaptSelection(selection);
	}
}

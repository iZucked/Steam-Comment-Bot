/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 * All rights reserved.
 */
package com.mmxlabs.models.lng.actuals.ui.editorpart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridCellRenderer;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.nebula.widgets.grid.internal.DefaultCellRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.common.Equality;
import com.mmxlabs.models.lng.actuals.ActualsModel;
import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.actuals.CargoActuals;
import com.mmxlabs.models.lng.actuals.DischargeActuals;
import com.mmxlabs.models.lng.actuals.LoadActuals;
import com.mmxlabs.models.lng.actuals.ReturnActuals;
import com.mmxlabs.models.lng.actuals.ui.editorpart.ActualsModelRowTransformer.RootData;
import com.mmxlabs.models.lng.actuals.ui.editorpart.ActualsModelRowTransformer.RowData;
import com.mmxlabs.models.lng.actuals.ui.editorpart.ActualsModelRowTransformer.RowDataEMFPath;
import com.mmxlabs.models.lng.actuals.ui.editorpart.ActualsModelRowTransformer.Type;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.editors.dialogs.MultiDetailDialog;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerValidationSupport;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateTimeAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.ReadOnlyManipulatorWrapper;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.models.util.emfpath.EMFPath;
import com.mmxlabs.rcp.common.actions.CopyToClipboardActionFactory;
import com.mmxlabs.rcp.common.actions.PackGridTreeColumnsAction;
import com.mmxlabs.scenario.service.model.ScenarioLock;

/**
 * Tabular editor displaying cargoes and slots with a custom wiring editor. This implementation is "stupid" in that any changes to the data cause a full update. This has the disadvantage of loosing
 * the current ordering of items. Each row is a cargo. Changing the wiring will re-order slots. The {@link CargoWiringComposite} based view only re-orders slots when requested permitting the original
 * (or at least the wiring at time of opening the editor) wiring to be seem via rows and the current wiring via the wire lines.
 * 
 * 
 * @author Simon Goodall
 * 
 */
public class ActualsTableViewerPane extends ScenarioTableViewerPane {

	protected RootData rootData;
	/**
	 * A reference {@link RootData} object. This is used by a {@link ActualsModelRowTransformer} to retain load/discharge row pairings but allow wires to cross rows. Initially null until the first
	 * rootData object is created. May be "nulled" again to reset state by an action.
	 * 
	 */
	protected RootData referenceRootData;

	private final Set<GridColumn> cargoColumns = new HashSet<GridColumn>();
	private final Set<GridColumn> loadColumns = new HashSet<GridColumn>();
	private final Set<GridColumn> dischargeColumns = new HashSet<GridColumn>();

	private boolean locked;

	private final ActualsEditingCommands cec;
	private final ActualsEditorMenuHelper menuHelper;

	private final TradesFilter tradesFilter = new TradesFilter();

	public ActualsTableViewerPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation scenarioEditingLocation, final IActionBars actionBars) {
		super(page, part, scenarioEditingLocation, actionBars);

		final LNGScenarioModel scenarioModel = (LNGScenarioModel) scenarioEditingLocation.getRootObject();
		this.cec = new ActualsEditingCommands(scenarioEditingLocation.getEditingDomain(), scenarioModel);
		this.menuHelper = new ActualsEditorMenuHelper(part.getSite().getShell(), scenarioEditingLocation, scenarioModel);
	}

	@Override
	public void dispose() {

		this.rootData = null;
		this.referenceRootData = null;

		super.dispose();
	}

	@Override
	protected ScenarioTableViewer constructViewer(final Composite parent) {

		final ScenarioTableViewer scenarioViewer = new ScenarioTableViewer(parent, SWT.FULL_SELECTION | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, scenarioEditingLocation) {

			@Override
			public EReference getCurrentContainment() {
				return super.getCurrentContainment();// ActualsModel.eINSTANCE.getCargoModel_Cargoes();
			}

			@Override
			public EObject getCurrentContainer() {
				return super.getCurrentContainer();// getPortfolioModel().getCargoModel();
			}

			/**
			 * Overridden method to convert internal RowData objects into a collection of EMF Objects
			 */
			@Override
			protected void updateSelection(final ISelection selection) {

				if (selection instanceof IStructuredSelection) {
					final IStructuredSelection originalSelection = (IStructuredSelection) selection;

					final List<Object> selectedObjects = new LinkedList<Object>();

					final Iterator<?> itr = originalSelection.iterator();
					while (itr.hasNext()) {
						final Object obj = itr.next();
						if (obj instanceof RowData) {
							final RowData rd = (RowData) obj;
							if (rd.cargo != null) {
								selectedObjects.add(rd.cargo);
							}
							if (rd.loadSlot != null) {
								selectedObjects.add(rd.loadSlot);
							}
							if (rd.dischargeSlot != null) {
								selectedObjects.add(rd.dischargeSlot);
							}
						}
					}

					super.updateSelection(new StructuredSelection(selectedObjects));
				} else {
					super.updateSelection(selection);
				}
			}

			@Override
			public void init(final AdapterFactory adapterFactory, final CommandStack commandStack, final EReference... path) {
				super.init(adapterFactory, commandStack, path);

				init(new ITreeContentProvider() {

					@Override
					public void dispose() {

					}

					@Override
					public Object[] getElements(final Object inputElement) {

						ActualsModel actualsModel = getActualsModel();
						final RootData root = setActualsData(actualsModel);

						ActualsTableViewerPane.this.rootData = root;

						if (ActualsTableViewerPane.this.referenceRootData == null) {
							ActualsTableViewerPane.this.referenceRootData = root;
						}

						return rootData.getRows().toArray();
					}

					@Override
					public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

					}

					@Override
					public Object[] getChildren(final Object parentElement) {
						return null;
					}

					@Override
					public Object getParent(final Object element) {
						return null;
					}

					@Override
					public boolean hasChildren(final Object element) {
						return false;
					}

				}, commandStack);

				addFilter(tradesFilter);
			}

			@Override
			protected EObjectTableViewerValidationSupport createValidationSupport() {
				return new EObjectTableViewerValidationSupport(this) {

					@Override
					public EObject getElementForValidationTarget(final EObject source) {
						return getElementForNotificationTarget(source);
					}

					@Override
					protected void processStatus(final IStatus status, final boolean update) {
						super.processStatus(status, update);
						if (!isBusy()) {
							refresh();
						}
					}

					@Override
					protected void updateObject(final EObject object, final IStatus status, final boolean update) {
						if (object == null) {
							return;
						}

						int idx = -1;
						if (rootData.getLoadActuals().contains(object)) {
							idx = rootData.getLoadActuals().indexOf(object);
						} else if (rootData.getDischargeActuals().contains(object)) {
							idx = rootData.getDischargeActuals().indexOf(object);
						} else if (rootData.getCargoActuals().contains(object)) {
							idx = rootData.getCargoActuals().indexOf(object);
						}

						RowData rd = null;
						if (idx >= 0) {
							rd = rootData.getRows().get(idx);
						}

						if (rd != null) {
							getValidationSupport().setStatus(rd, status);
						}
					}
				};
			}

			@Override
			public EObject getElementForNotificationTarget(final EObject source) {
				if (source instanceof LoadActuals) {
					return source;
				} else if (source instanceof DischargeActuals) {
					return source;
				} else if (source instanceof CargoActuals) {
					return source;
				} else if (source instanceof ReturnActuals) {
					return source.eContainer();
				}

				return super.getElementForNotificationTarget(source);
			}

			@Override
			@SuppressWarnings("restriction")
			protected GridCellRenderer createCellRenderer() {
				return new DefaultCellRenderer();
			}

		};

		// scenarioViewer.setToolTipProvider(new DefaultToolTipProvider() {
		//
		// @Override
		// public String getToolTipText(Object element) {
		// RowData rd = ((RowData) element);
		// LoadActuals ls = rd.getLoadSlot();
		// DischargeActuals ds = rd.getDischargeSlot();
		//
		// String lString = ls != null ? (ls.getNotes() != null ? ls.getNotes() : "") : "";
		// String dString = ds != null ? (ds.getNotes() != null ? ds.getNotes() : "") : "";
		// if (lString.length() + dString.length() == 0) {
		// return null;
		// } else {
		// if (lString.length() > 40)
		// lString = lString.substring(0, 40) + "...";
		// if (dString.length() > 40)
		// dString = dString.substring(0, 40) + "...";
		// return lString + "\n\n" + dString;
		// }
		// }
		//
		// @Override
		// public Point getToolTipShift(Object object) {
		// return new Point(10, 10);
		// }
		// });

		final MenuManager mgr = new MenuManager();

		scenarioViewer.getGrid().addMenuDetectListener(new MenuDetectListener() {

			private Menu menu;

			@Override
			public void menuDetected(final MenuDetectEvent e) {

				if (locked) {
					return;
				}
				final Point mousePoint = getScenarioViewer().getGrid().toControl(new Point(e.x, e.y));
				final GridColumn column = getScenarioViewer().getGrid().getColumn(mousePoint);

				final GridItem item = getScenarioViewer().getGrid().getItem(mousePoint);
				if (item == null) {
					return;
				}
				final Object data = item.getData();
				if (data instanceof RowData) {

					final RowData rowDataItem = (RowData) data;
					final int idx = rootData.getRows().indexOf(rowDataItem);

					if (menu == null) {
						menu = mgr.createContextMenu(scenarioViewer.getGrid());
					}
					mgr.removeAll();

					if (loadColumns.contains(column)) {
						if (rowDataItem.loadSlot != null) {
							final IMenuListener listener = menuHelper.createLoadSlotMenuListener(rootData.getLoadActuals(), idx, getActualsModel());
							listener.menuAboutToShow(mgr);
							// if (contextMenuExtensions != null) {
							// final Slot slot = rootData.getLoadSlots().get(idx);
							// for (final ITradesTableContextMenuExtension ext : contextMenuExtensions) {
							// ext.contributeToMenu(scenarioEditingLocation, slot, mgr);
							// }
							// }
						} else {
							final IMenuListener listener = menuHelper.createDischargeSlotMenuListener(rootData.getDischargeActuals(), idx, getActualsModel());
							listener.menuAboutToShow(mgr);
							// if (contextMenuExtensions != null) {
							// final Slot slot = rootData.getDischargeSlots().get(idx);
							// for (final ITradesTableContextMenuExtension ext : contextMenuExtensions) {
							// ext.contributeToMenu(scenarioEditingLocation, slot, mgr);
							// }
							// }
						}
					}
					if (dischargeColumns.contains(column)) {
						if (rowDataItem.dischargeSlot != null) {
							final IMenuListener listener = menuHelper.createDischargeSlotMenuListener(rootData.getDischargeActuals(), idx, getActualsModel());
							listener.menuAboutToShow(mgr);
							// if (contextMenuExtensions != null) {
							// final Slot slot = rootData.getDischargeSlots().get(idx);
							// for (final ITradesTableContextMenuExtension ext : contextMenuExtensions) {
							// ext.contributeToMenu(scenarioEditingLocation, slot, mgr);
							// }
							// }
						} else if (rowDataItem.loadSlot != null) {
							final IMenuListener listener = menuHelper.createLoadSlotMenuListener(rootData.getLoadActuals(), idx, getActualsModel());
							listener.menuAboutToShow(mgr);
							// if (contextMenuExtensions != null) {
							// final Slot slot = rootData.getLoadSlots().get(idx);
							// for (final ITradesTableContextMenuExtension ext : contextMenuExtensions) {
							// ext.contributeToMenu(scenarioEditingLocation, slot, mgr);
							// }
							// }
						}
					}

					menu.setVisible(true);
				}
			}
		});

		scenarioViewer.setComparer(new IElementComparer() {
			@Override
			public int hashCode(final Object element) {
				return element.hashCode();
			}

			@Override
			public boolean equals(final Object a, final Object b) {

				final Set<Object> aSet = getObjectSet(a);
				final Set<Object> bSet = getObjectSet(b);

				if (aSet == null || bSet == null) {
					return false;
				}

				aSet.retainAll(bSet);
				if (!aSet.isEmpty()) {
					return true;
				}

				return Equality.isEqual(a, b);
			}

			private Set<Object> getObjectSet(final Object a) {
				final Set<Object> aSet = new HashSet<Object>();
				if (a instanceof RowData) {
					final RowData rd = (RowData) a;
					aSet.add(rd);
					if (rd.cargo != null) {
						aSet.add(rd.cargo);
						aSet.add(rd.cargo.getCargo());
					}
					if (rd.loadSlot != null) {
						aSet.add(rd.loadSlot);
						aSet.add(rd.loadSlot.getSlot());
					}
					if (rd.dischargeSlot != null) {
						aSet.add(rd.dischargeSlot);
						aSet.add(rd.dischargeSlot.getSlot());
					}
					aSet.remove(null);
				} else {
					aSet.add(a);
				}
				return aSet;
			}
		});

		return scenarioViewer;
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final CommandStack commandStack) {
		getScenarioViewer().init(adapterFactory, commandStack, new EReference[0]);

		final IStatusProvider statusProvider = scenarioEditingLocation.getStatusProvider();
		getScenarioViewer().setStatusProvider(statusProvider);

		final Grid table = getScenarioViewer().getGrid();

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// set up toolbars
		final ToolBarManager toolbar = getToolBarManager();
		toolbar.add(new GroupMarker(EDIT_GROUP));
		toolbar.add(new GroupMarker(ADD_REMOVE_GROUP));
		toolbar.add(new GroupMarker(VIEW_GROUP));
		toolbar.appendToGroup(VIEW_GROUP, new PackGridTreeColumnsAction(scenarioViewer));

		/*
		 * final ActionContributionItem filter = filterField.getContribution();
		 * 
		 * toolbar.appendToGroup(VIEW_GROUP, filter);
		 */
		addAction = null;
		// final Action addAction = new AddAction("Add");
		// addAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		// toolbar.appendToGroup(ADD_REMOVE_GROUP, addAction);

		final Action filterAction = new FilterMenuAction("Filter");
		filterAction.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.ui.tabular", "/icons/filter.gif"));
		toolbar.appendToGroup(VIEW_GROUP, filterAction);

		// add extension points to toolbar
		{
			final String toolbarID = getToolbarID();
			final IMenuService menuService = (IMenuService) PlatformUI.getWorkbench().getService(IMenuService.class);
			if (menuService != null) {
				menuService.populateContributionManager(toolbar, toolbarID);

				toolbar.getControl().addDisposeListener(new DisposeListener() {
					@Override
					public void widgetDisposed(final DisposeEvent e) {
						menuService.releaseContributions(toolbar);
					}
				});
			}
		}
		//
		// if (addAction != null) {
		// // if we can't add one, we can't duplicate one either.
		// final Action dupAction = createDuplicateAction();
		//
		// if (dupAction != null) {
		// toolbar.appendToGroup(ADD_REMOVE_GROUP, dupAction);
		// }
		// }
		deleteAction = createDeleteAction(null);
		if (deleteAction != null) {
			toolbar.appendToGroup(ADD_REMOVE_GROUP, deleteAction);
		}
		if (actionBars != null) {
			actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);
		}

		final Action copyToClipboardAction = CopyToClipboardActionFactory.createCopyToClipboardAction(getScenarioViewer());

		if (copyToClipboardAction != null) {
			toolbar.add(copyToClipboardAction);
		}

		if (actionBars != null) {
			actionBars.updateActionBars();
		}

		toolbar.update(true);

		final ActualsPackage pkg = ActualsPackage.eINSTANCE;
		// final IReferenceValueProviderProvider provider = scenarioEditingLocation.getReferenceValueProviderCache();
		final EditingDomain editingDomain = scenarioEditingLocation.getEditingDomain();

		// addActualsColumn(cargoColumns, "C-ID", new BasicOperationRenderer(CargoPackage.Literals.CARGO___GET_LOAD_NAME, editingDomain),
		// new RowDataEMFPath(false, Type.CARGO, pkg.getCargoActuals_Cargo()));

		addActualsColumn(loadColumns, "L-ID", new ReadOnlyManipulatorWrapper<BasicAttributeManipulator>(new BasicAttributeManipulator(MMXCorePackage.Literals.NAMED_OBJECT__NAME, editingDomain)),
				new RowDataEMFPath(false, Type.LOAD, pkg.getSlotActuals_Slot()));
		addActualsColumn(loadColumns, "L-Port", new SingleReferenceManipulator(pkg.getSlotActuals_TitleTransferPoint(), getReferenceValueProviderCache(), editingDomain),
				new RowDataEMFPath(false, Type.LOAD));
		addActualsColumn(loadColumns, "L-Counter Party", new BasicAttributeManipulator(pkg.getSlotActuals_Counterparty(), editingDomain), new RowDataEMFPath(false, Type.LOAD));
		addActualsColumn(loadColumns, "L-Price", new NumericAttributeManipulator(pkg.getSlotActuals_PriceDOL(), editingDomain), new RowDataEMFPath(false, Type.LOAD));
		addActualsColumn(loadColumns, "L-Op. Start", new LocalDateTimeAttributeManipulator(pkg.getSlotActuals_OperationsStart(), editingDomain), new RowDataEMFPath(false, Type.LOAD));
		addActualsColumn(loadColumns, "L-Op. End", new LocalDateTimeAttributeManipulator(pkg.getSlotActuals_OperationsEnd(), editingDomain), new RowDataEMFPath(false, Type.LOAD));

		addActualsColumn(dischargeColumns, "D-ID", new ReadOnlyManipulatorWrapper<BasicAttributeManipulator>(new BasicAttributeManipulator(MMXCorePackage.Literals.NAMED_OBJECT__NAME, editingDomain)),
				new RowDataEMFPath(false, Type.DISCHARGE, pkg.getSlotActuals_Slot()));
		addActualsColumn(dischargeColumns, "D-Port", new SingleReferenceManipulator(pkg.getSlotActuals_TitleTransferPoint(), getReferenceValueProviderCache(), editingDomain),
				new RowDataEMFPath(false, Type.DISCHARGE));
		addActualsColumn(dischargeColumns, "D-Counter Party", new BasicAttributeManipulator(pkg.getSlotActuals_Counterparty(), editingDomain), new RowDataEMFPath(false, Type.DISCHARGE));
		addActualsColumn(dischargeColumns, "D-Price", new NumericAttributeManipulator(pkg.getSlotActuals_PriceDOL(), editingDomain), new RowDataEMFPath(false, Type.DISCHARGE));
		addActualsColumn(dischargeColumns, "D-Op. Start", new LocalDateTimeAttributeManipulator(pkg.getSlotActuals_OperationsStart(), editingDomain), new RowDataEMFPath(false, Type.DISCHARGE));
		addActualsColumn(dischargeColumns, "D-Op. End", new LocalDateTimeAttributeManipulator(pkg.getSlotActuals_OperationsEnd(), editingDomain), new RowDataEMFPath(false, Type.DISCHARGE));

		addActualsColumn(cargoColumns, "Base Fuel Price", new NumericAttributeManipulator(pkg.getCargoActuals_BaseFuelPrice(), editingDomain), new RowDataEMFPath(false, Type.CARGO));
		// addActualsColumn(cargoColumns, "Crew Bonus", new NumericAttributeManipulator(pkg.getCargoActuals_CrewBonus(), editingDomain), new RowDataEMFPath(false, Type.CARGO));
		addActualsColumn(cargoColumns, "Insurance Premium", new NumericAttributeManipulator(pkg.getCargoActuals_InsurancePremium(), editingDomain), new RowDataEMFPath(false, Type.CARGO));
		addActualsColumn(cargoColumns, "Vessel", new SingleReferenceManipulator(pkg.getCargoActuals_Vessel(), getReferenceValueProviderCache(), editingDomain), new RowDataEMFPath(false, Type.CARGO));
	}

	@Override
	protected Action createAddAction(EReference containment) {
		return null;
	}

	public RootData setActualsData(final ActualsModel actualModel) {
		final ActualsModelRowTransformer transformer = new ActualsModelRowTransformer();
		return transformer.transform(actualModel, getScenarioViewer().getValidationSupport().getValidationErrors());
	}

	public void init(final AdapterFactory adapterFactory, final CommandStack commandStack) {
		getScenarioViewer().init(adapterFactory, commandStack);

	}

	@Override
	protected void enableOpenListener() {
		scenarioViewer.addOpenListener(new IOpenListener() {

			@Override
			public void open(final OpenEvent event) {
				if (scenarioViewer.getSelection() instanceof IStructuredSelection) {
					final IStructuredSelection structuredSelection = (IStructuredSelection) scenarioViewer.getSelection();
					if (structuredSelection.isEmpty() == false) {

						final List<EObject> editorTargets = new ArrayList<EObject>();
						final Iterator<?> itr = structuredSelection.iterator();
						while (itr.hasNext()) {
							final Object obj = itr.next();
							EObject target = null;
							if (obj instanceof RowData) {
								final RowData rd = (RowData) obj;
								if (rd.cargo != null) {
									target = rd.cargo;
								} else if (rd.loadSlot != null) {
									target = rd.loadSlot;
								} else if (rd.dischargeSlot != null) {
									target = rd.dischargeSlot;
								}
							}
							if (target != null) {
								editorTargets.add(target);
							}
						}
						if (!editorTargets.isEmpty() && scenarioViewer.isLocked() == false) {
							final ScenarioLock editorLock = scenarioEditingLocation.getEditorLock();
							try {
								editorLock.claim();
								scenarioEditingLocation.setDisableUpdates(true);
								if (editorTargets.size() > 1) {
									final MultiDetailDialog mdd = new MultiDetailDialog(event.getViewer().getControl().getShell(), scenarioEditingLocation.getRootObject(),
											scenarioEditingLocation.getDefaultCommandHandler());
									mdd.open(scenarioEditingLocation, editorTargets);
								} else {
									final DetailCompositeDialog dcd = new DetailCompositeDialog(event.getViewer().getControl().getShell(), scenarioEditingLocation.getDefaultCommandHandler(),
											~SWT.MAX) {
										@Override
										protected void configureShell(final Shell newShell) {
											newShell.setMinimumSize(SWT.DEFAULT, 630);
											super.configureShell(newShell);
										}
									};
									dcd.open(scenarioEditingLocation, scenarioEditingLocation.getRootObject(), editorTargets, scenarioViewer.isLocked());
								}
							} finally {
								scenarioEditingLocation.setDisableUpdates(false);
								editorLock.release();
							}
						}
					}
				}
			}

		});
	}

	protected LNGScenarioModel getScenarioModel() {
		return ((LNGScenarioModel) scenarioEditingLocation.getRootObject());
	}

	// private <T extends ICellManipulator & ICellRenderer> GridViewerColumn addActualsColumn(final String columnName, final T manipulator, final EMFPath path) {
	// return this.addActualsColumn(null, columnName, manipulator, path);
	// }

	private <T extends ICellManipulator & ICellRenderer> GridViewerColumn addActualsColumn(final Set<GridColumn> group, final String columnName, final T manipulator, final EMFPath path) {
		final GridViewerColumn col = getScenarioViewer().addColumn(columnName, manipulator, manipulator, path);
		if (group != null) {
			group.add(col.getColumn());
		}
		return col;
	}

	@Override
	public void setLocked(final boolean locked) {
		if (this.locked == locked) {
			return;
		}
		this.locked = locked;
		super.setLocked(locked);
	}

	private abstract class DefaultMenuCreatorAction extends Action implements IMenuCreator {
		private Menu lastMenu;

		public DefaultMenuCreatorAction(final String label) {
			super(label, IAction.AS_DROP_DOWN_MENU);
		}

		@Override
		public void dispose() {
			if ((lastMenu != null) && (lastMenu.isDisposed() == false)) {
				lastMenu.dispose();
			}
			lastMenu = null;
		}

		@Override
		public IMenuCreator getMenuCreator() {
			return this;
		}

		@Override
		public Menu getMenu(final Control parent) {
			if (lastMenu != null) {
				lastMenu.dispose();
			}
			lastMenu = new Menu(parent);

			populate(lastMenu);

			return lastMenu;
		}

		abstract protected void populate(Menu menu);

		@Override
		public Menu getMenu(final Menu parent) {
			if (lastMenu != null) {
				lastMenu.dispose();
			}
			lastMenu = new Menu(parent);

			populate(lastMenu);

			return lastMenu;
		}

		protected void addActionToMenu(final Action a, final Menu m) {
			final ActionContributionItem aci = new ActionContributionItem(a);
			aci.fill(m, -1);
		}

	}

	private class TradesFilter extends ViewerFilter {
		final private Map<EMFPath, EObject> filterValues = new HashMap<EMFPath, EObject>();

		public void setFilterValue(final EMFPath path, final EObject value) {
			// adding a particular filter value resets all others
			filterValues.clear();
			if (value != null) {
				filterValues.put(path, value);
			} else {
				filterValues.remove(path);
			}
		}

		public EObject getFilterValue(final EMFPath path) {
			return filterValues.get(path);
		}

		@Override
		public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
			// TODO Auto-generated method stub
			if (element instanceof EObject) {
				final EObject object = (EObject) element;

				for (final Entry<EMFPath, EObject> entry : filterValues.entrySet()) {
					final Object value = entry.getKey().get(object);
					if (value != entry.getValue()) {
						return false;
					}
				}
			}

			return true;
		}

		public void clear() {
			filterValues.clear();
		}
	}

	private class FilterMenuAction extends DefaultMenuCreatorAction {
		/**
		 * A holder for a menu list of filter actions on different fields for the trades wiring table.
		 * 
		 * @param label
		 *            The label to show in the UI for this menu.
		 */
		public FilterMenuAction(final String label) {
			super(label);
		}

		/**
		 * Add the filterable fields to the menu for this item.
		 */
		@Override
		protected void populate(final Menu menu) {
			final LNGScenarioModel scenario = ((LNGScenarioModel) scenarioEditingLocation.getRootObject());
			// final CommercialModel commercialModel = scenario.getCommercialModel();
			// final FleetModel fleetModel = scenario.getFleetModel();

			// final EMFPath purchaseContractPath = new RowDataEMFPath(false, ActualsModelRowTransformer.Type.LOAD, CargoPackage.Literals.SLOT__CONTRACT);
			// final EMFPath salesContractPath = new RowDataEMFPath(false, ActualsModelRowTransformer.Type.DISCHARGE, CargoPackage.Literals.SLOT__CONTRACT);
			// final EMFPath vesselPath = new RowDataEMFPath(false, ActualsModelRowTransformer.Type.SLOT_OR_CARGO, FleetPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT);

			final Action clearAction = new Action("Clear Filter") {
				@Override
				public void run() {
					tradesFilter.clear();
					scenarioViewer.refresh(false);
				}
			};

			addActionToMenu(clearAction, menu);
			// addActionToMenu(new FilterAction("Purchase Contracts", commercialModel, CommercialPackage.Literals.COMMERCIAL_MODEL__PURCHASE_CONTRACTS, purchaseContractPath), menu);
			// addActionToMenu(new FilterAction("Sales Contracts", commercialModel, CommercialPackage.Literals.COMMERCIAL_MODEL__SALES_CONTRACTS, salesContractPath), menu);
			// addActionToMenu(new FilterAction("Vessels", fleetModel, FleetPackage.Literals.FLEET_MODEL__VESSELS, vesselPath), menu);

		}
	}

	//
	// private class FilterAction extends DefaultMenuCreatorAction {
	// final private EObject sourceObject;
	// final private EStructuralFeature sourceFeature;
	// final private EMFPath filterPath;
	//
	// /**
	// * An action which updates the filter on the trades wiring table and refreshes the table.
	// *
	// * @param label
	// * The label to associate with this action (the feature from the cargo row it represents).
	// * @param sourceObject
	// * The source object in the EMF model which holds the list of possible values for the filter.
	// * @param sourceFeature
	// * The EMF feature of the source object where the list of possible values resides.
	// * @param filterPath
	// * The path within a cargo row object of the field which the table is being filtered on.
	// */
	// public FilterAction(final String label, final EObject sourceObject, final EStructuralFeature sourceFeature, final EMFPath filterPath) {
	// super(label);
	// this.sourceObject = sourceObject;
	// this.sourceFeature = sourceFeature;
	// this.filterPath = filterPath;
	// }
	//
	// /**
	// * Add actions to the submenu associated with this action.
	// */
	// @Override
	// protected void populate(final Menu menu) {
	// // Get the labels to populate the menu from the source object in the EMF model
	// final EList<NamedObject> values = (EList<NamedObject>) sourceObject.eGet(sourceFeature);
	//
	// // Show the list of labels (one for each item in the source object feature)
	// for (final NamedObject value : values) {
	// // WEIRD: action returns wrong value from isChecked() so set the checked value here
	// // A label is checked if it is the value already selected in the trades filter
	// final boolean checked = tradesFilter.getFilterValue(filterPath) == value;
	// final Action action = new Action(value.getName(), IAction.AS_RADIO_BUTTON) {
	// @Override
	// public void run() {
	// // When we select a checked option, we want to turn the filter off
	// final EObject newValue = (checked) ? null : value;
	// tradesFilter.setFilterValue(filterPath, newValue);
	// scenarioViewer.refresh(false);
	// }
	// };
	// addActionToMenu(action, menu);
	// action.setChecked(checked);
	// }
	//
	// }
	//
	// }
	//
	// private class AddAction extends DefaultMenuCreatorAction {
	//
	// public AddAction(final String label) {
	// super(label);
	// }
	//
	// /**
	// * Subclasses should fill their menu with actions here.
	// *
	// * @param menu
	// * the menu which is about to be displayed
	// */
	// @Override
	// protected void populate(final Menu menu) {
	// final ActualsModel actualsModel = getActualsModel();
	//
	// RowData discoveredRowData = null;
	// final ISelection selection = getScenarioViewer().getSelection();
	// if (selection instanceof IStructuredSelection) {
	// final Object firstElement = ((IStructuredSelection) selection).getFirstElement();
	// if (firstElement instanceof RowData) {
	// discoveredRowData = (RowData) firstElement;
	// }
	// }
	// final RowData referenceRowData = discoveredRowData;
	// {
	// final Action newLoad = new Action("Cargo") {
	// @Override
	// public void run() {
	//
	// final CompoundCommand cmd = new CompoundCommand("Cargo Actuals");
	// List<Command> setCommands = new LinkedList<>();
	// final CargoActuals newCargo = cec.createNewCargoActuals(setCommands, actualsModel);
	// // for (final SlotActuals slot : newCargo.getActuals()) {
	// // if (slot instanceof LoadActuals) {
	// // final LoadActuals newLoad = (LoadActuals) slot;
	// // initialiseSlot(newLoad, true, referenceRowData);
	// // // newLoad.setDESPurchase(false);
	// // cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), actualsModel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), newLoad));
	// // } else if (slot instanceof DischargeActuals) {
	// // final DischargeActuals newDischarge = (DischargeActuals) slot;
	// // initialiseSlot(newDischarge, false, referenceRowData);
	// // // newDischarge.setFOBSale(false);
	// // cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), actualsModel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), newDischarge));
	// // } else {
	// // throw new IllegalStateException("Unexpected slot type");
	// // }
	// // }
	//
	// for (Command c : setCommands) {
	// cmd.append(c);
	// }
	// // cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), actualsModel, CargoPackage.eINSTANCE.getACargoModel_Cargoes(), newCargo));
	//
	// scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
	// }
	// };
	// addActionToMenu(newLoad, menu);
	// }
	// // {
	// // final Action newLoad = new Action("FOB Purchase") {
	// // public void run() {
	// //
	// // final CompoundCommand cmd = new CompoundCommand("FOB Purchase");
	// //
	// // final LoadActuals newLoad = cec.createObject(CargoPackage.eINSTANCE.getLoadSlot(), CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), cargoModel);
	// // // newLoad.setDESPurchase(false);
	// // initialiseSlot(newLoad, true, referenceRowData);
	// // cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), newLoad));
	// // scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
	// // }
	// // };
	// // addActionToMenu(newLoad, menu);
	// // }
	// // {
	// // final Action newDESPurchase = new Action("DES Purchase") {
	// // public void run() {
	// //
	// // final CompoundCommand cmd = new CompoundCommand("DES Purchase");
	// //
	// // final LoadActuals newLoad = cec.createObject(CargoPackage.eINSTANCE.getLoadSlot(), CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), cargoModel);
	// // newLoad.setDESPurchase(true);
	// // initialiseSlot(newLoad, true, referenceRowData);
	// // cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), newLoad));
	// // scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
	// // }
	// // };
	// // addActionToMenu(newDESPurchase, menu);
	// // }
	// // {
	// // final Action newDischarge = new Action("DES Sale") {
	// // public void run() {
	// //
	// // final CompoundCommand cmd = new CompoundCommand("DES Sale");
	// //
	// // final DischargeActuals newDischarge = cec.createObject(ActualsPackage.eINSTANCE.getDischargeActuals(), CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), cargoModel);
	// // // newDischarge.setFOBSale(false);
	// // initialiseSlot(newDischarge, false, referenceRowData);
	// // cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), actualsModel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), newDischarge));
	// // scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
	// // }
	// // };
	// //
	// // addActionToMenu(newDischarge, menu);
	// // }
	// // {
	// // final Action newFOBSale = new Action("FOB Sale") {
	// // public void run() {
	// //
	// // final CompoundCommand cmd = new CompoundCommand("FOB Sale");
	// //
	// // final DischargeActuals newDischarge = cec.createObject(CargoPackage.eINSTANCE.getDischargeSlot(), CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), cargoModel);
	// // // newDischarge.setFOBSale(true);
	// // initialiseSlot(newDischarge, false, referenceRowData);
	// // cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), actualsModel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), newDischarge));
	// // scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
	// // }
	// // };
	// // addActionToMenu(newFOBSale, menu);
	// // }
	// }
	//
	// private final void initialiseSlot(final SlotActuals newSlot, final boolean isLoad, final RowData referenceRowData) {
	// newSlot.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
	// // newSlot.setOptional(false);
	// // newSlot.setName("");
	// // Set window so that via default sorting inserts new slot at current table position
	// if (referenceRowData != null) {
	// final SlotActuals primarySortSlot = isLoad ? referenceRowData.loadSlot : referenceRowData.dischargeSlot;
	// final SlotActuals secondarySortSlot = isLoad ? referenceRowData.dischargeSlot : referenceRowData.loadSlot;
	// if (primarySortSlot != null) {
	// // newSlot.setWindowStart(primarySortSlot.getWindowStart());
	// // newSlot.setPort(primarySortSlot.getPort());
	// } else if (secondarySortSlot != null) {
	// // newSlot.setWindowStart(secondarySortSlot.getWindowStart());
	// }
	// }
	// }
	//
	// }

	//
	// private class CreateStripMenuAction extends Action implements IMenuCreator {
	//
	// private Menu lastMenu;
	//
	// public CreateStripMenuAction(final String label) {
	// super(label, IAction.AS_DROP_DOWN_MENU);
	// setImageDescriptor(LngUIActivator.getDefault().getImageRegistry().getDescriptor(ImageConstants.IMAGE_DUPLICATE));
	// setDisabledImageDescriptor(LngUIActivator.getDefault().getImageRegistry().getDescriptor(ImageConstants.IMAGE_DUPLICATE_DISABLED));
	// }
	//
	// @Override
	// public void dispose() {
	// if ((lastMenu != null) && (lastMenu.isDisposed() == false)) {
	// lastMenu.dispose();
	// }
	// lastMenu = null;
	// }
	//
	// @Override
	// public IMenuCreator getMenuCreator() {
	// return this;
	// }
	//
	// @Override
	// public Menu getMenu(final Control parent) {
	// if (lastMenu != null) {
	// lastMenu.dispose();
	// }
	// lastMenu = new Menu(parent);
	//
	// populate(lastMenu);
	//
	// return lastMenu;
	// }
	//
	// protected void addActionToMenu(final Action a, final Menu m) {
	// final ActionContributionItem aci = new ActionContributionItem(a);
	// aci.fill(m, -1);
	// }
	//
	// /**
	// * Subclasses should fill their menu with actions here.
	// *
	// * @param menu
	// * the menu which is about to be displayed
	// */
	// protected void populate(final Menu menu) {
	//
	// // final DuplicateAction result = new DuplicateAction(getJointModelEditorPart());
	// // // Translate into real objects, not just row object!
	// // final List<Object> selectedObjects = new LinkedList<Object>();
	// // if (scenarioViewer.getSelection() instanceof IStructuredSelection) {
	// // final IStructuredSelection structuredSelection = (IStructuredSelection) scenarioViewer.getSelection();
	// //
	// // final Iterator<?> itr = structuredSelection.iterator();
	// // while (itr.hasNext()) {
	// // final Object o = itr.next();
	// // if (o instanceof RowData) {
	// // final RowData rowData = (RowData) o;
	// // // TODO: Check logic, a row may contain two distinct items
	// // if (rowData.cargo != null) {
	// // selectedObjects.add(rowData.cargo);
	// // continue;
	// // }
	// // if (rowData.loadSlot != null) {
	// // selectedObjects.add(rowData.loadSlot);
	// // }
	// // if (rowData.dischargeSlot != null) {
	// // selectedObjects.add(rowData.dischargeSlot);
	// // }
	// // }
	// // }
	// // }
	// //
	// // result.selectionChanged(new SelectionChangedEvent(scenarioViewer, new StructuredSelection(selectedObjects)));
	// // addActionToMenu(result, menu);
	// //
	// // for (final CreateStripDialog.StripType stripType : CreateStripDialog.StripType.values()) {
	// // final Action stripAction = new CreateStripAction(stripType.toString(), stripType);
	// // addActionToMenu(stripAction, menu);
	// // }
	// }
	//
	// @Override
	// public Menu getMenu(final Menu parent) {
	// if (lastMenu != null) {
	// lastMenu.dispose();
	// }
	// lastMenu = new Menu(parent);
	//
	// populate(lastMenu);
	//
	// return lastMenu;
	// }
	//
	// }

	/**
	 * Return an action which duplicates the selection
	 * 
	 * @return
	 */
	@Override
	protected Action createDuplicateAction() {
		return null;// new CreateStripMenuAction("Duplicate");
	}

	private ActualsModel getActualsModel() {
		return getScenarioModel().getActualsModel();
	}

}

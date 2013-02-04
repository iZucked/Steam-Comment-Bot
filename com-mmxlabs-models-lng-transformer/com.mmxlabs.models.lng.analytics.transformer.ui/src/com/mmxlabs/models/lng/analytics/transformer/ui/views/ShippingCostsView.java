/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.transformer.ui.views;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.databinding.ObservablesManager;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ObservableList;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.databinding.edit.IEMFEditListProperty;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapCellLabelProvider;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.eclipse.ui.views.properties.PropertySheetPage;

import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.DestinationType;
import com.mmxlabs.models.lng.analytics.ShippingCostPlan;
import com.mmxlabs.models.lng.analytics.ShippingCostRow;
import com.mmxlabs.models.lng.analytics.UnitCostLine;
import com.mmxlabs.models.lng.analytics.transformer.IShippingCostTransformer;
import com.mmxlabs.models.lng.analytics.transformer.impl.ShippingCostTransformer;
import com.mmxlabs.models.lng.analytics.ui.properties.UnitCostLinePropertySource;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceView;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.modelfactories.IModelFactory;
import com.mmxlabs.models.ui.modelfactories.IModelFactory.ISetting;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;

public class ShippingCostsView extends ScenarioInstanceView {

	private final IShippingCostTransformer transformer = new ShippingCostTransformer();

	private ShippingCostRowViewerPane rows;

	private PropertySheetPage propertySheetPage;

	private Composite parent;

	private SashForm sidebarSash;
	private Composite sidebarComposite;

	private TableViewer selectionViewer;

	private Action addAction;

	private Action deleteAction;

	private EMFDataBindingContext dbc;
	private ObservablesManager manager;

	@Override
	protected void displayScenarioInstance(final ScenarioInstance instance) {
		if (instance != getScenarioInstance()) {
			disposeContents();
			getSite().setSelectionProvider(null);
			super.displayScenarioInstance(instance);

			manager = new ObservablesManager();
			dbc = new EMFDataBindingContext();

			if (instance != null) {
				manager.runAndCollect(new Runnable() {

					@Override
					public void run() {
						createContents();
					}
				});
			} else {
				final IObservableList list = new ObservableList(Collections.emptyList(), ShippingCostPlan.class) {

				};
				selectionViewer.setInput(list);
			}
		}
		if (addAction != null) {
			addAction.setEnabled(instance != null);
		}
		if (deleteAction != null && instance == null) {
			deleteAction.setEnabled(false);
		}
	}

	private void disposeContents() {

		if (rows != null) {
			final Control control = rows.getControl();
			// This can be null if the pane has already been disposed.
			if (control != null) {
				control.dispose();
			}
			rows.dispose();
			rows = null;
		}
		if (manager != null) {
			manager.dispose();
		}
		if (dbc != null) {
			dbc.dispose();
		}
	}

	private void createContents() {
		final IEMFEditListProperty prop = EMFEditProperties.list(getEditingDomain(), AnalyticsPackage.Literals.ANALYTICS_MODEL__SHIPPING_COST_PLANS);
		// TODO Auto-generated method stub
		final IObservableList modelObserver = prop.observe(getRootObject().getSubModel(AnalyticsModel.class));
		selectionViewer.setInput(modelObserver);

		// IViewerValueProperty targetObserver = ViewerProperties.input();
		// dbc.bindValue(targetObserver.observe(selectionViewer), modelObserver);

		// selectionViewer.setInput(getRootObject().getSubModel(AnalyticsModel.class).getShippingCostPlans());

		rows = new ShippingCostRowViewerPane(getSite().getPage(), this, this, this.getViewSite().getActionBars());
		// rows.setExternalToolBarManager((ToolBarManager) getViewSite().getActionBars().getToolBarManager());
		rows.createControl(sidebarSash);
		rows.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		rows.init(Collections.singletonList(AnalyticsPackage.eINSTANCE.getShippingCostPlan_Rows()), getAdapterFactory());

		createMenuManager(rows.getScenarioViewer().getGrid());
		getViewSite().setSelectionProvider(rows.getViewer());

		sidebarSash.setWeights(new int[] { 20, 80 });

		parent.layout(true);
	}

	@Override
	public void createPartControl(final Composite parent) {
		this.parent = parent;
		final GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginWidth = gridLayout.marginHeight = 0;
		parent.setLayout(gridLayout);

		// if (displaySidebarList) {
		sidebarSash = new SashForm(parent, SWT.HORIZONTAL | SWT.SMOOTH);
		{
			final GridLayout layout = new GridLayout(2, false);
			layout.marginHeight = layout.marginWidth = 0;
			sidebarSash.setLayout(layout);
		}
		sidebarSash.setLayoutData(new GridData(GridData.FILL_BOTH));
		sidebarComposite = new Composite(sidebarSash, SWT.NONE);
		sidebarComposite.setLayout(new GridLayout(1, true));
		sidebarComposite.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true));

		final ToolBarManager barManager = new ToolBarManager(SWT.BORDER | SWT.RIGHT);

		// need to populate add action - it could do a duplicate and then a
		// clear?
		// delete action can just kill the primary, and remove from inputs

		barManager.createControl(sidebarComposite).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		selectionViewer = new TableViewer(sidebarComposite, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);

		createToolbarActions(barManager);
		barManager.update(true);

		selectionViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));

		final ObservableListContentProvider contentProvider = new ObservableListContentProvider();
		selectionViewer.setContentProvider(contentProvider);

		// Bind the label provider to the name attribute
		selectionViewer.setLabelProvider(new ObservableMapCellLabelProvider(EMFProperties.value(MMXCorePackage.eINSTANCE.getNamedObject_Name()).observeDetail(contentProvider.getKnownElements())));

		selectionViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(final SelectionChangedEvent event) {
				final ISelection selection = event.getSelection();
				if (selection instanceof IStructuredSelection) {
					final Object element = ((IStructuredSelection) selection).getFirstElement();

					if (element instanceof ShippingCostPlan) {
						rows.getViewer().setInput(element);
					}
				}
			}
		});

		selectionViewer.addOpenListener(new IOpenListener() {

			@Override
			public void open(final OpenEvent event) {
				final ISelection selection = selectionViewer.getSelection();
				if (selection instanceof IStructuredSelection) {

					final IStructuredSelection ss = (IStructuredSelection) selection;
					final Object plan = ss.getFirstElement();
					if (plan instanceof ShippingCostPlan) {

						final DetailCompositeDialog dialog = new DetailCompositeDialog(getSite().getShell(), getDefaultCommandHandler());
						dialog.open(ShippingCostsView.this, getRootObject(), Collections.singletonList((EObject) plan));
					}
				}
				selectionViewer.refresh();
			}
		});

		listenToScenarioSelection();
	}

	@Override
	public void setFocus() {
		if (rows != null && rows.getControl() != null && !rows.getControl().isDisposed()) {
			rows.getControl().setFocus();
			// } else if (plans != null && plans.getControl() != null && !plans.getControl().isDisposed()) {
			// plans.getControl().setFocus();
			// } else if (top != null && !top.isDisposed()) {
			// top.setFocus();
			// }
		}
	}

	@Override
	public Object getAdapter(final Class adapter) {
		if (adapter.isAssignableFrom(IPropertySheetPage.class)) {
			if (propertySheetPage == null && selectionViewer != null) {
				propertySheetPage = new PropertySheetPage();
				propertySheetPage.setPropertySourceProvider(new IPropertySourceProvider() {
					@Override
					public IPropertySource getPropertySource(Object object) {

						if (object instanceof ShippingCostRow) {
							object = ((ShippingCostRow) object).eContainer();
						}

						if (object instanceof ShippingCostPlan) {
							final List<UnitCostLine> lines = transformer.evaulateShippingPlan(getRootObject(), (ShippingCostPlan) object, new NullProgressMonitor());
							try {
								if (lines != null && !lines.isEmpty()) {
									// TODO: Handle multiple cases
									final UnitCostLine line = (UnitCostLine) lines.get(0);
									return new UnitCostLinePropertySource(line);
								}
							} catch (final Throwable th) {
								return null;
							}
						} else if (object instanceof IPropertySource) {
							return (IPropertySource) object;
						}
						return null;
					}
				});
			}
			return propertySheetPage;
		}
		return null;
	}

	public void dispose() {
		disposeContents();
		getSite().setSelectionProvider(null);
		super.dispose();
	}

	@Override
	public void setLocked(final boolean locked) {
		// if (plans != null) {
		// plans.setLocked(locked);
		// }
		if (rows != null) {
			rows.setLocked(locked);
		}
		super.setLocked(locked);
	}

	IMenuManager createMenuManager(final Control parent) {

		final MenuManager mgr = new MenuManager();
		final Menu createContextMenu = mgr.createContextMenu(parent);
		parent.setMenu(createContextMenu);
		getSite().registerContextMenu(mgr, rows.getScenarioViewer());

		mgr.addMenuListener(new IMenuListener() {

			@Override
			public void menuAboutToShow(final IMenuManager manager) {

				manager.removeAll();
				final Object input = rows.getScenarioViewer().getInput();
				final ShippingCostPlan plan = (ShippingCostPlan) input;
				if (plan == null) {
					return;
				}

				final EList<ShippingCostRow> planRows = plan.getRows();

				final int selectionIndex = rows.getScenarioViewer().getGrid().getSelectionIndex();

				manager.add(new Action("Insert new row") {
					@Override
					public void run() {

						if (input instanceof ShippingCostPlan) {

							final ShippingCostRow row = createObject(AnalyticsPackage.eINSTANCE.getShippingCostRow(), AnalyticsPackage.eINSTANCE.getShippingCostPlan_Rows(), plan);
							row.setDestinationType(DestinationType.OTHER);
							// No insertion before first element!
							final int insertionIndex = Math.max(planRows.size() == 0 ? 0 : 1, selectionIndex);

							final Command addCmd = AddCommand.create(getEditingDomain(), plan, AnalyticsPackage.eINSTANCE.getShippingCostPlan_Rows(), row, insertionIndex);
							getEditingDomain().getCommandStack().execute(addCmd);
						}

						super.run();
					}
				});

				if (selectionIndex >= 0 && selectionIndex < plan.getRows().size() - 1) {
					manager.add(new Action("Move Down") {
						@Override
						public void run() {

							final Object input = rows.getScenarioViewer().getInput();
							if (input instanceof ShippingCostPlan) {

								final ShippingCostRow rowA = planRows.remove(selectionIndex);
								planRows.add(selectionIndex + 1, rowA);
								final CompoundCommand cmd = new CompoundCommand("Move row Down");
								cmd.append(SetCommand.create(getEditingDomain(), plan, AnalyticsPackage.eINSTANCE.getShippingCostPlan_Rows(), planRows));
								getEditingDomain().getCommandStack().execute(cmd);
							}

							super.run();
						}
					});
				}

				if (selectionIndex > 0) {
					manager.add(new Action("Move Up") {
						@Override
						public void run() {

							final Object input = rows.getScenarioViewer().getInput();
							if (input instanceof ShippingCostPlan) {

								final ShippingCostRow rowA = planRows.remove(selectionIndex);
								planRows.add(selectionIndex - 1, rowA);
								final CompoundCommand cmd = new CompoundCommand("Move row up");
								cmd.append(SetCommand.create(getEditingDomain(), plan, AnalyticsPackage.eINSTANCE.getShippingCostPlan_Rows(), planRows));
								getEditingDomain().getCommandStack().execute(cmd);
							}

							super.run();
						}
					});
				}
			}
		});

		return mgr;
	}

	/**
	 * When the sidebar is displayed, this method is invoked to add actions to the toolbar above it.
	 * 
	 * @param barManager
	 */
	private void createToolbarActions(final ToolBarManager barManager) {
		// create add actions
		final List<IModelFactory> factories = Activator.getDefault().getModelFactoryRegistry().getModelFactories(AnalyticsPackage.eINSTANCE.getShippingCostPlan());
		if (factories.isEmpty() == false) {
			if (factories.size() == 1) {
				addAction = createAddAction(factories.get(0));
				addAction.setEnabled(false);
				barManager.add(addAction);
			} else {
				// multi-adder //TODO
			}
		}

		deleteAction = new Action() {
			{
				setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
				selectionViewer.addSelectionChangedListener(new ISelectionChangedListener() {
					@Override
					public void selectionChanged(final SelectionChangedEvent event) {
						setEnabled(event.getSelection().isEmpty() == false);
					}
				});
			}

			@Override
			public void run() {
				final ISelection selection = selectionViewer.getSelection();
				if (selection instanceof IStructuredSelection) {
					final Object element = ((IStructuredSelection) selection).getFirstElement();
					getEditingDomain().getCommandStack().execute(DeleteCommand.create(getEditingDomain(), element));
					selectionViewer.refresh();
				}
			}
		};
		deleteAction.setEnabled(false);
		barManager.add(deleteAction);

		// create duplicate actions

	}

	private Action createAddAction(final IModelFactory factory) {
		return new Action("Create new " + factory.getLabel(), PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD)) {

			@Override
			public void run() {
				final MMXRootObject rootObject = getRootObject();
				final AnalyticsModel analyticsModel = rootObject.getSubModel(AnalyticsModel.class);

				final Collection<? extends ISetting> settings = factory.createInstance(rootObject, analyticsModel, AnalyticsPackage.eINSTANCE.getAnalyticsModel_ShippingCostPlans(), null);
				if (settings.isEmpty()) {
					return;
				}

				final ScenarioLock editorLock = getEditorLock();
				try {
					editorLock.claim();
					setDisableUpdates(true);

					// now create an add command, which will include adding any
					// other relevant objects
					final CompoundCommand add = new CompoundCommand();
					for (final ISetting setting : settings) {
						final EObject instance = setting.getInstance();
						final DetailCompositeDialog dialog = new DetailCompositeDialog(getSite().getShell(), getDefaultCommandHandler());
						if (dialog.open(ShippingCostsView.this, rootObject, Collections.singletonList(instance)) == Window.OK) {
							add.append(AddCommand.create(getEditingDomain(), setting.getContainer(), setting.getContainment(), setting.getInstance()));
						}
					}
					getEditingDomain().getCommandStack().execute(add);
					selectionViewer.refresh();
				} finally {
					setDisableUpdates(false);
					editorLock.release();
				}
			}
		};
	}

	private <T> T createObject(final EClass clz, final EReference reference, final EObject container) {
		final List<IModelFactory> factories = Activator.getDefault().getModelFactoryRegistry().getModelFactories(clz);

		// TODO: Pre-generate and link to UI
		// TODO: Add FOB/DES etc as explicit slot types.
		final IModelFactory factory = factories.get(0);

		final Collection<? extends ISetting> settings = factory.createInstance(getRootObject(), container, reference, StructuredSelection.EMPTY);
		if (settings.isEmpty() == false) {

			for (final ISetting setting : settings) {

				return (T) setting.getInstance();
			}
		}
		return null;
	}
}

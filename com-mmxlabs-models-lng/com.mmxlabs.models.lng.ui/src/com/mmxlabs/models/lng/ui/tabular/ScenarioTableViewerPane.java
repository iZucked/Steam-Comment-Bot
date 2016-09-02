/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.ui.tabular;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.jface.gridviewer.GridViewerEditor;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.ui.views.properties.PropertySheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.ui.actions.AddModelAction;
import com.mmxlabs.models.lng.ui.actions.AddModelAction.IAddContext;
import com.mmxlabs.models.lng.ui.actions.DuplicateAction;
import com.mmxlabs.models.lng.ui.actions.ScenarioModifyingAction;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.EMFViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.filter.FilterField;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.CopyTableToClipboardAction;
import com.mmxlabs.rcp.common.actions.CopyTreeToClipboardAction;
import com.mmxlabs.rcp.common.actions.LockableAction;
import com.mmxlabs.rcp.common.actions.PackGridTreeColumnsAction;
import com.mmxlabs.scenario.service.model.ScenarioLock;

public class ScenarioTableViewerPane extends EMFViewerPane {

	private static final Logger log = LoggerFactory.getLogger(ScenarioTableViewerPane.class);

	protected static final String VIEW_GROUP = "view";
	protected static final String ADD_REMOVE_GROUP = "addremove";
	protected static final String EDIT_GROUP = "edit";
	/**
	 */
	protected ScenarioTableViewer scenarioViewer;
	/**
	 */
	protected final IScenarioEditingLocation scenarioEditingLocation;

	/**
	 */
	protected FilterField filterField;
	/**
	 */
	protected ToolBarManager externalToolbarManager;
	protected MenuManager externalMenuManager;

	/**
	 */
	protected final IActionBars actionBars;
	/**
	 */
	protected Action deleteAction;

	/**
	 */
	protected Action addAction;

	private final ISelectionListener selectionListener = new ISelectionListener() {

		@Override
		public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {

			if (part == ScenarioTableViewerPane.this.part) {
				return;
			}
			if (part instanceof PropertySheet) {
				return;
			}
			// Avoid cyclic selection changes
			if (ScenarioTableViewerPane.this.page.getActivePart() == ScenarioTableViewerPane.this.part) {
				return;
			}
			if (scenarioViewer != null) {
				try {
					scenarioViewer.setSelection(selection, true);
				} catch (final Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		}
	};

	public ScenarioTableViewerPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part);
		this.scenarioEditingLocation = location;
		this.actionBars = actionBars;
		page.addPostSelectionListener(selectionListener);
	}

	public ScenarioTableViewer getScenarioViewer() {
		return scenarioViewer;
	}

	protected String getToolbarID() {
		return "toolbar:" + getClass().getCanonicalName();
	}

	protected String getMenuID() {
		return "menu:" + getClass().getCanonicalName();
	}

	@Override
	public void dispose() {
		if (page != null) {
			page.removePostSelectionListener(selectionListener);
		}

		if (externalToolbarManager != null) {
			externalToolbarManager.removeAll();
			externalToolbarManager.update(true);
		}
		externalToolbarManager = null;
		if (externalMenuManager != null) {
			externalMenuManager.removeAll();
			externalMenuManager.update(true);
		}
		externalMenuManager = null;

		scenarioViewer = null;

		super.dispose();
	}

	public void setExternalToolBarManager(final ToolBarManager manager) {
		this.externalToolbarManager = manager;
	}

	public void setExternalMenuManager(MenuManager menuManager) {
		this.externalMenuManager = menuManager;

	}

	@Override
	public ToolBarManager getToolBarManager() {
		if (externalToolbarManager != null) {
			return externalToolbarManager;
		} else {
			return super.getToolBarManager();
		}
	}

	@Override
	public MenuManager getMenuManager() {
		if (externalMenuManager != null) {
			return externalMenuManager;
		} else {
			return super.getMenuManager();
		}
	}

	@Override
	public void createControl(final Composite parent) {
		// interpose and create filter field
		if (getControl() == null) {
			container = parent;

			// Create view form.
			// control = new ViewForm(parent, getStyle());
			control = new ViewForm(parent, SWT.NONE);
			control.addDisposeListener(new DisposeListener() {
				@Override
				public void widgetDisposed(final DisposeEvent event) {
					dispose();
				}
			});

			control.marginWidth = 0;
			control.marginHeight = 0;

			// Create a title bar.
			if (externalToolbarManager == null)
				createTitleBar();

			final Composite inner = new Composite(control, SWT.NONE);
			filterField = new FilterField(inner);

			final GridLayout layout = new GridLayout(1, false);
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			inner.setLayout(layout);

			viewer = createViewer(inner);

			viewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));

			control.setContent(inner);

			control.setTabList(new Control[] { inner });

			// When the pane or any child gains focus, notify the workbench.
			control.addListener(SWT.Activate, this);
			hookFocus(control);
			hookFocus(viewer.getControl());
		}
	}

	@Override
	public ScenarioTableViewer createViewer(final Composite parent) {
		if (scenarioViewer == null) {
			scenarioViewer = constructViewer(parent);

			enableOpenListener();

			// scenarioViewer.getGrid().setCellSelectionEnabled(true);
			filterField.setFilterSupport(scenarioViewer.getFilterSupport());

			final ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(scenarioViewer) {
				@Override
				protected boolean isEditorActivationEvent(final ColumnViewerEditorActivationEvent event) {
					return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL || event.eventType == ColumnViewerEditorActivationEvent.MOUSE_CLICK_SELECTION
							|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
				}
			};

			GridViewerEditor.create(scenarioViewer, actSupport, ColumnViewerEditor.KEYBOARD_ACTIVATION | GridViewerEditor.SELECTION_FOLLOWS_EDITOR |
			// ColumnViewerEditor.KEEP_EDITOR_ON_DOUBLE_CLICK |
					ColumnViewerEditor.TABBING_HORIZONTAL | ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.TABBING_VERTICAL | ColumnViewerEditor.KEYBOARD_ACTIVATION);

			return scenarioViewer;
		} else {
			throw new RuntimeException("Did not expect two calls to createViewer()");
		}

	}

	/**
	 */
	protected void enableOpenListener() {
		scenarioViewer.addOpenListener(new IOpenListener() {

			@Override
			public void open(final OpenEvent event) {
				if (scenarioViewer.getSelection() instanceof IStructuredSelection) {
					final IStructuredSelection structuredSelection = (IStructuredSelection) scenarioViewer.getSelection();
					DetailCompositeDialogUtil.editSelection(scenarioEditingLocation, structuredSelection);
				}
			}
		});
	}

	protected ScenarioTableViewer constructViewer(final Composite parent) {
		return new ScenarioTableViewer(parent, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, scenarioEditingLocation);
	}

	public <T extends ICellManipulator & ICellRenderer> GridViewerColumn addTypicalColumn(final String columnName, final T manipulatorAndRenderer, final ETypedElement... path) {
		return this.addColumn(columnName, manipulatorAndRenderer, manipulatorAndRenderer, path);
	}

	public GridViewerColumn addColumn(final String columnName, final ICellRenderer renderer, final ICellManipulator manipulator, final ETypedElement... pathObjects) {
		return scenarioViewer.addColumn(columnName, renderer, manipulator, pathObjects);
	}

	public EditingDomain getEditingDomain() {
		return scenarioEditingLocation.getEditingDomain();
	}

	public AdapterFactory getAdapterFactory() {
		return scenarioEditingLocation.getAdapterFactory();
	}

	public IReferenceValueProviderProvider getReferenceValueProviderCache() {
		return scenarioEditingLocation.getReferenceValueProviderCache();
	}

	public IScenarioEditingLocation getJointModelEditorPart() {
		return scenarioEditingLocation;
	}

	/**
	 */
	protected GridViewerColumn addNameManipulator(final String nameName) {
		return addTypicalColumn(nameName, new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), getEditingDomain()));
	}

	/**
	 */
	protected void defaultSetTitle(final String string) {
		setTitle(string, PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));
	}

	/**
	 */
	protected Action createAddAction(final EReference containment) {
		return AddModelAction.create(containment.getEReferenceType(), getAddContext(containment), new Action[] { createDuplicateAction() });

	}

	/**
	 */
	protected IAddContext getAddContext(final EReference containment) {
		return new IAddContext() {
			@Override
			public MMXRootObject getRootObject() {
				return scenarioEditingLocation.getRootObject();
			}

			@Override
			public EReference getContainment() {
				return containment;
			}

			@Override
			public EObject getContainer() {
				return scenarioViewer.getCurrentContainer();
			}

			@Override
			public ICommandHandler getCommandHandler() {
				return scenarioEditingLocation.getDefaultCommandHandler();
			}

			@Override
			public IScenarioEditingLocation getEditorPart() {
				return scenarioEditingLocation;
			}

			@Override
			public @Nullable Collection<@NonNull EObject> getCurrentSelection() {
				return SelectionHelper.convertToList(viewer.getSelection(), EObject.class);
			}
		};
	}

	/**
	 */
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final CommandStack commandStack) {
		scenarioViewer.init(adapterFactory, commandStack, path.toArray(new EReference[path.size()]));

		scenarioViewer.setStatusProvider(getJointModelEditorPart().getStatusProvider());

		final Grid table = scenarioViewer.getGrid();

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// set up toolbars
		final ToolBarManager toolbar = getToolBarManager();
		toolbar.add(new GroupMarker(EDIT_GROUP));
		toolbar.add(new GroupMarker(ADD_REMOVE_GROUP));
		toolbar.add(new GroupMarker(VIEW_GROUP));
		toolbar.appendToGroup(VIEW_GROUP, new PackGridTreeColumnsAction(scenarioViewer));

		final ActionContributionItem filter = filterField.getContribution();

		toolbar.appendToGroup(VIEW_GROUP, filter);

		final EReference containment = path.get(path.size() - 1);

		addAction = createAddAction(containment);

		if (addAction != null) {
			/*
			 * // if we can't add one, we can't duplicate one either. final Action dupAction = createDuplicateAction();
			 * 
			 * if (dupAction != null) { toolbar.appendToGroup(ADD_REMOVE_GROUP, dupAction); }
			 */

			toolbar.appendToGroup(ADD_REMOVE_GROUP, addAction);
		}
		deleteAction = createDeleteAction(null);
		if (deleteAction != null) {
			toolbar.appendToGroup(ADD_REMOVE_GROUP, deleteAction);
		}
		if (actionBars != null) {
			actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);
		}

		// add extension points to toolbar
		{
			final IMenuService menuService = (IMenuService) PlatformUI.getWorkbench().getService(IMenuService.class);
			if (menuService != null) {
				{
					toolbar.getControl().addDisposeListener(new DisposeListener() {

						@Override
						public void widgetDisposed(DisposeEvent e) {
							menuService.releaseContributions(toolbar);

						}
					});
					menuService.populateContributionManager(toolbar, getToolbarID());
				}

			}
		}

		Action copyToClipboardAction = null;
		if (viewer instanceof TableViewer) {
			copyToClipboardAction = new CopyTableToClipboardAction(((TableViewer) viewer).getTable());
		} else if (viewer instanceof TreeViewer) {
			copyToClipboardAction = new CopyTreeToClipboardAction(((TreeViewer) viewer).getTree());
		} else if (viewer instanceof GridTableViewer) {
			copyToClipboardAction = new CopyGridToClipboardAction(((GridTableViewer) viewer).getGrid());
		} else if (viewer instanceof GridTreeViewer) {
			copyToClipboardAction = new CopyGridToClipboardAction(((GridTreeViewer) viewer).getGrid());
		}

		if (copyToClipboardAction != null) {
			toolbar.add(copyToClipboardAction);
		}

		{
			// Menu Manager
			{
				final String menuID = getMenuID();
				final IMenuService menuService = (IMenuService) PlatformUI.getWorkbench().getService(IMenuService.class);
				if (menuService != null) {
					final MenuManager mgr = getMenuManager();
					menuService.populateContributionManager(mgr, menuID);

					viewer.getControl().addDisposeListener(new DisposeListener() {
						@Override
						public void widgetDisposed(final DisposeEvent e) {
							menuService.releaseContributions(mgr);
						}
					});
				}
			}

		}

		if (actionBars != null) {
			actionBars.updateActionBars();
		}

		toolbar.update(true);
	}

	/**
	 * Return an action which duplicates the selection
	 * 
	 * @return
	 */
	protected Action createDuplicateAction() {
		final DuplicateAction result = new DuplicateAction(getJointModelEditorPart());
		scenarioViewer.addSelectionChangedListener(result);
		return result;
	}

	protected Action createDeleteAction(@Nullable Function<Collection<?>, Collection<Object>> callback) {
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
						editorLock.awaitClaim();
						getJointModelEditorPart().setDisableUpdates(true);
						try {
							final ISelection sel = getLastSelection();
							if (sel instanceof IStructuredSelection) {
								final EditingDomain ed = scenarioEditingLocation.getEditingDomain();
								// Copy selection
								final List<?> objects = new ArrayList<>(((IStructuredSelection) sel).toList());

								// Ensure a unique collection of objects - no duplicates
								final Set<Object> uniqueObjects = new HashSet<>(objects);

								// Pull in additional objects to delete.
								if (callback != null) {
									uniqueObjects.addAll(callback.apply(objects));
								}

								// Clear current selection
								selectionChanged(new SelectionChangedEvent(viewer, StructuredSelection.EMPTY));

								// Execute command
								final Command deleteCommand = DeleteCommand.create(ed, uniqueObjects);
								ed.getCommandStack().execute(deleteCommand);
							}
						} finally {
							editorLock.release();
							getJointModelEditorPart().setDisableUpdates(false);
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
	protected void requestActivation() {
		super.requestActivation();
		scenarioEditingLocation.setCurrentViewer(scenarioViewer);

		final IActionBars pActionBars = actionBars;
		if (pActionBars != null) {
			pActionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);
			pActionBars.updateActionBars();
		}
	}

	public void setLocked(final boolean locked) {
		final ScenarioTableViewer pScenarioViewer = scenarioViewer;
		if (pScenarioViewer != null) {
			pScenarioViewer.setLocked(locked);
		}

		for (final IContributionItem item : getToolBarManager().getItems()) {
			if (item instanceof ActionContributionItem) {
				final IAction action = ((ActionContributionItem) item).getAction();
				if (action instanceof LockableAction) {
					((LockableAction) action).setLocked(locked);
				}
			}
		}
	}
}

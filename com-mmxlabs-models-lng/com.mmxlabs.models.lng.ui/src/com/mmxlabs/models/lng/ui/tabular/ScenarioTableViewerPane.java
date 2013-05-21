/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.ui.tabular;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.ui.ViewerPane;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
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
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.editors.dialogs.MultiDetailDialog;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.filter.FilterField;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.CopyTableToClipboardAction;
import com.mmxlabs.rcp.common.actions.CopyTreeToClipboardAction;
import com.mmxlabs.rcp.common.actions.LockableAction;
import com.mmxlabs.rcp.common.actions.PackGridTableColumnsAction;
import com.mmxlabs.scenario.service.model.ScenarioLock;

public class ScenarioTableViewerPane extends ViewerPane {

	private static final Logger log = LoggerFactory.getLogger(ScenarioTableViewerPane.class);

	protected static final String VIEW_GROUP = "view";
	protected static final String ADD_REMOVE_GROUP = "addremove";
	protected static final String EDIT_GROUP = "edit";
	/**
	 * @since 2.0
	 */
	protected ScenarioTableViewer scenarioViewer;
	/**
	 * @since 4.0
	 */
	protected final IScenarioEditingLocation scenarioEditingLocation;

	/**
	 * @since 2.0
	 */
	protected FilterField filterField;
	/**
	 * @since 2.0
	 */
	protected ToolBarManager externalToolbarManager;

	/**
	 * @since 2.0
	 */
	protected final IActionBars actionBars;
	/**
	 * @since 2.0
	 */
	protected Action deleteAction;

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

	@Override
	public void dispose() {
		page.removePostSelectionListener(selectionListener);

		if (externalToolbarManager != null) {
			externalToolbarManager.removeAll();
			externalToolbarManager.update(true);
		}
		externalToolbarManager = null;

		scenarioViewer = null;

		super.dispose();
	}

	public void setExternalToolBarManager(final ToolBarManager manager) {
		this.externalToolbarManager = manager;
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
	 * @since 2.0
	 */
	protected void enableOpenListener() {
		scenarioViewer.addOpenListener(new IOpenListener() {

			@Override
			public void open(final OpenEvent event) {
				if (scenarioViewer.getSelection() instanceof IStructuredSelection) {
					final IStructuredSelection structuredSelection = (IStructuredSelection) scenarioViewer.getSelection();
					if (structuredSelection.isEmpty() == false) {
						if (structuredSelection.size() == 1) {
							final DetailCompositeDialog dcd = new DetailCompositeDialog(event.getViewer().getControl().getShell(), scenarioEditingLocation.getDefaultCommandHandler());
							try {
								scenarioEditingLocation.getEditorLock().claim();
								scenarioEditingLocation.setDisableUpdates(true);
								dcd.open(scenarioEditingLocation, scenarioEditingLocation.getRootObject(), structuredSelection.toList(), scenarioViewer.isLocked());
							} finally {
								scenarioEditingLocation.setDisableUpdates(false);
								scenarioEditingLocation.getEditorLock().release();
							}
						} else {
							try {
								scenarioEditingLocation.getEditorLock().claim();
								if (scenarioViewer.isLocked() == false) {
									final MultiDetailDialog mdd = new MultiDetailDialog(event.getViewer().getControl().getShell(), scenarioEditingLocation.getRootObject(), scenarioEditingLocation
											.getDefaultCommandHandler());
									mdd.open(scenarioEditingLocation, structuredSelection.toList());
								}
							} finally {
								scenarioEditingLocation.getEditorLock().release();
							}
						}
					}
				}
			}
		});
	}

	protected ScenarioTableViewer constructViewer(final Composite parent) {
		return new ScenarioTableViewer(parent, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, scenarioEditingLocation);
	}

	public <T extends ICellManipulator & ICellRenderer> GridViewerColumn addTypicalColumn(final String columnName, final T manipulatorAndRenderer, final Object... path) {
		return this.addColumn(columnName, manipulatorAndRenderer, manipulatorAndRenderer, path);
	}

	public GridViewerColumn addColumn(final String columnName, final ICellRenderer renderer, final ICellManipulator manipulator, final Object... pathObjects) {
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

	protected void addNameManipulator(final String nameName) {
		addTypicalColumn(nameName, new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), getEditingDomain()));
	}

	protected void defaultSetTitle(final String string) {
		setTitle(string, PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));
	}

	/**
	 * @since 3.1
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
		toolbar.appendToGroup(VIEW_GROUP, new PackGridTableColumnsAction(scenarioViewer));

		final ActionContributionItem filter = filterField.getContribution();

		toolbar.appendToGroup(VIEW_GROUP, filter);

		final EReference containment = path.get(path.size() - 1);

		final Action addAction = AddModelAction.create(containment.getEReferenceType(), new IAddContext() {
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
			public ISelection getCurrentSelection() {
				return viewer.getSelection();
			}
		});

		if (addAction != null) {
			// if we can't add one, we can't duplicate one either.
			final Action dupAction = createDuplicateAction();

			if (dupAction != null) {
				toolbar.appendToGroup(ADD_REMOVE_GROUP, dupAction);
			}

			toolbar.appendToGroup(ADD_REMOVE_GROUP, addAction);
		}
		deleteAction = createDeleteAction();
		if (deleteAction != null) {
			toolbar.appendToGroup(ADD_REMOVE_GROUP, deleteAction);
		}
		if (actionBars != null) {
			actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);
		}

		// add extension points to toolbar
		{
			final String toolbarID = getToolbarID();
			final IMenuService menuService = (IMenuService) PlatformUI.getWorkbench().getService(IMenuService.class);
			if (menuService != null) {
				menuService.populateContributionManager(toolbar, toolbarID);

				viewer.getControl().addDisposeListener(new DisposeListener() {
					@Override
					public void widgetDisposed(final DisposeEvent e) {
						menuService.releaseContributions(toolbar);
					}
				});
			}
		}

		Action copyToClipboardAction = null;
		if (viewer instanceof TableViewer) {
			copyToClipboardAction = new CopyTableToClipboardAction(((TableViewer) viewer).getTable());
		} else if (viewer instanceof TreeViewer) {
			copyToClipboardAction = new CopyTreeToClipboardAction(((TreeViewer) viewer).getTree());
		} else if (viewer instanceof GridTableViewer) {
			copyToClipboardAction = new CopyGridToClipboardAction(((GridTableViewer) viewer).getGrid());
		}

		if (copyToClipboardAction != null) {
			toolbar.add(copyToClipboardAction);
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

	protected Action createDeleteAction() {
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
								final List<?> objects = new ArrayList<Object>(((IStructuredSelection) sel).toList());

								// Clear current selection
								selectionChanged(new SelectionChangedEvent(viewer, StructuredSelection.EMPTY));

								// Execute command
								final Command deleteCommand = DeleteCommand.create(ed, objects);
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

		if (actionBars != null) {
			actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);
		}
		if (actionBars != null) {
			actionBars.updateActionBars();
		}

	}

	public void setLocked(final boolean locked) {
		scenarioViewer.setLocked(locked);

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

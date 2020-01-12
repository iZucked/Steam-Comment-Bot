/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.ui.tabular;

import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.modeling.ISelectionListener;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.ui.views.properties.PropertySheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.ui.EMFViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.filter.FilterField;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.actions.LockableAction;

public abstract class ScenarioViewerPane extends EMFViewerPane {

	private static final Logger log = LoggerFactory.getLogger(ScenarioViewerPane.class);

	protected static final String VIEW_GROUP = "view";
	protected static final String ADD_REMOVE_GROUP = "addremove";
	protected static final String EDIT_GROUP = "edit";

	protected final @NonNull IScenarioEditingLocation scenarioEditingLocation;

	protected ToolBarManager externalToolbarManager;
	protected MenuManager externalMenuManager;

	protected FilterField filterField;

	protected final IActionBars actionBars;

	private final ISelectionListener selectionListener = new ISelectionListener() {

		private AtomicBoolean inSelectionChanged = new AtomicBoolean(false);

		@Override
		public void selectionChanged(final MPart part, final Object selectedObject) {

			{
				final IWorkbenchPart view = SelectionHelper.getE3Part(part);

				if (view == ScenarioViewerPane.this.part) {
					return;
				}
				if (view instanceof PropertySheet) {
					return;
				}
			}

			// Convert selection
			final ISelection selection = SelectionHelper.adaptSelection(selectedObject);

			// Avoid re-entrant selection changes.
			if (inSelectionChanged.compareAndSet(false, true)) {
				try {
					// Avoid cyclic selection changes
					if (ScenarioViewerPane.this.page == null || ScenarioViewerPane.this.page.getActivePart() == ScenarioViewerPane.this.part) {
						return;
					}
					if (viewer != null) {
						try {
							viewer.setSelection(selection, true);
						} catch (final Exception e) {
							log.error(e.getMessage(), e);
						}
					}
				} finally {
					inSelectionChanged.set(false);
				}
			}
		}

	};

	public ScenarioViewerPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part);
		this.scenarioEditingLocation = location;
		this.actionBars = actionBars;
		final ESelectionService service = PlatformUI.getWorkbench().getService(ESelectionService.class);
		service.addPostSelectionListener(selectionListener);

	}

	protected String getToolbarID() {
		return "toolbar:" + getClass().getCanonicalName();
	}

	protected String getMenuID() {
		return "menu:" + getClass().getCanonicalName();
	}

	@Override
	public void dispose() {
		final ESelectionService service = PlatformUI.getWorkbench().getService(ESelectionService.class);
		service.removePostSelectionListener(selectionListener);

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
			control = new ViewForm(parent, SWT.NONE);
			control.addDisposeListener(event -> dispose());

			control.marginWidth = 0;
			control.marginHeight = 0;

			// Create a title bar.
			if (externalToolbarManager == null) {
				createTitleBar();
			}

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

	public EditingDomain getEditingDomain() {
		return scenarioEditingLocation.getEditingDomain();
	}

	public @NonNull AdapterFactory getAdapterFactory() {
		return scenarioEditingLocation.getAdapterFactory();
	}

	public @NonNull IReferenceValueProviderProvider getReferenceValueProviderCache() {
		return scenarioEditingLocation.getReferenceValueProviderCache();
	}

	public @NonNull IScenarioEditingLocation getJointModelEditorPart() {
		return scenarioEditingLocation;
	}

	/**
	 */
	protected void defaultSetTitle(final String string) {
		setTitle(string, PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));
	}

	/**
	 */
	public void initToolbars() {

		// set up toolbars
		final ToolBarManager toolbar = getToolBarManager();
		toolbar.add(new GroupMarker(EDIT_GROUP));
		toolbar.add(new GroupMarker(ADD_REMOVE_GROUP));
		toolbar.add(new GroupMarker(VIEW_GROUP));

		final ActionContributionItem filter = filterField.getContribution();

		toolbar.appendToGroup(VIEW_GROUP, filter);

		// add extension points to toolbar
		{
			final IMenuService menuService = PlatformUI.getWorkbench().getService(IMenuService.class);
			if (menuService != null) {
				toolbar.getControl().addDisposeListener(e -> menuService.releaseContributions(toolbar));
				menuService.populateContributionManager(toolbar, getToolbarID());
			}
		}

		// Disable menu contributions for embedded panes. This stops empty menus appearing
		if (externalMenuManager != null) {
			// Menu Manager
			final String menuID = getMenuID();
			final IMenuService menuService = PlatformUI.getWorkbench().getService(IMenuService.class);
			if (menuService != null) {
				final MenuManager mgr = getMenuManager();
				menuService.populateContributionManager(mgr, menuID);

				viewer.getControl().addDisposeListener(e -> menuService.releaseContributions(mgr));
			}
		}

		if (actionBars != null) {
			actionBars.updateActionBars();
		}

		toolbar.update(true);
	}

	public void setLocked(final boolean locked) {

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

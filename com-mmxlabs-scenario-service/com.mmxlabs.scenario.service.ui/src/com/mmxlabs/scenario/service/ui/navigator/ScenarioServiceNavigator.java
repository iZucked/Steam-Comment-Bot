/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.navigator;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.commands.Command;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.view.ExtendedPropertySheetPage;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IContributionManagerOverrides;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.internal.navigator.actions.CollapseAllAction;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.services.IServiceLocator;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManagerListener;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlListener;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.jobmanager.manager.IJobManager;
import com.mmxlabs.rcp.common.CommonImages;
import com.mmxlabs.rcp.common.CommonImages.IconMode;
import com.mmxlabs.rcp.common.CommonImages.IconPaths;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.scenario.service.IScenarioServiceSelectionChangedListener;
import com.mmxlabs.scenario.service.IScenarioServiceSelectionProvider;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.scenario.service.ScenarioServiceRegistry;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioModel;
import com.mmxlabs.scenario.service.ui.ScenarioResultImpl;
import com.mmxlabs.scenario.service.ui.internal.Activator;

public class ScenarioServiceNavigator extends CommonNavigator {

	public static final int COLUMN_NAME_IDX = 0;
	public static final int COLUMN_SHOW_IDX = 1;
	public static final int COLUMN_STATUS_IDX = 2;

	private static final Logger log = LoggerFactory.getLogger(ScenarioServiceNavigator.class);

	private final Image showColumnImage;
	private final Image statusColumnImage;

	protected AdapterFactoryEditingDomain editingDomain;

	protected ComposedAdapterFactory adapterFactory = ScenarioServiceComposedAdapterFactory.getAdapterFactory();

	private final ServiceTracker<ScenarioServiceRegistry, ScenarioServiceRegistry> tracker;

	private CommonViewer viewer = null;

	private ScenarioInstance lastAutoSelection = null;

	private ScenarioServiceNavigatorLinkHelper linkHelper;
	/**
	 * Part listener to track editor activation
	 */
	private IPartListener partListener = new IPartListener() {

		@Override
		public void partOpened(final IWorkbenchPart part) {

		}

		@Override
		public void partDeactivated(final IWorkbenchPart part) {

		}

		@Override
		public void partClosed(final IWorkbenchPart part) {
			// If the selection tracks editor, then get the scenario instance and make it
			// the only selection.
			if (part instanceof IEditorPart editorPart) {
				final IEditorInput editorInput = editorPart.getEditorInput();
				final ScenarioInstance scenarioInstance = editorInput.getAdapter(ScenarioInstance.class);
				if (scenarioInstance != null) {
					if (lastAutoSelection == scenarioInstance) {
						lastAutoSelection = null;
					}
				}
			}
		}

		@Override
		public void partBroughtToTop(final IWorkbenchPart part) {

		}

		@Override
		public void partActivated(final IWorkbenchPart part) {
			if (part instanceof IEditorPart editorPart) {
				final IEditorInput editorInput = editorPart.getEditorInput();
				final ScenarioInstance scenarioInstance = editorInput.getAdapter(ScenarioInstance.class);
				if (scenarioInstance != null) {
					lastAutoSelection = scenarioInstance;
				}
			}
		}
	};

	private final @NonNull IScenarioServiceSelectionChangedListener selectionChangedListener = new IScenarioServiceSelectionChangedListener() {

		@Override
		public void selectionChanged(ScenarioResult pinned, Collection<ScenarioResult> others) {
			RunnerHelper.syncExecDisplayOptional(() -> viewer.refresh());

		}
	};

	public ScenarioServiceNavigator() {
		super();

		tracker = new ServiceTracker<>(Activator.getDefault().getBundle().getBundleContext(), ScenarioServiceRegistry.class, null);
		tracker.open();

//		Activator.getDefault().getEclipseJobManager().addEclipseJobManagerListener(jobManagerListener);

		Activator.getDefault().getScenarioServiceSelectionProvider().addSelectionChangedListener(selectionChangedListener);
		showColumnImage = CommonImages.getImageDescriptor(IconPaths.Console, IconMode.Enabled).createImage();
		statusColumnImage = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/base-flag.png").createImage();
	}

	@Override
	public void dispose() {
		tracker.close();
		Activator.getDefault().getScenarioServiceSelectionProvider().removeSelectionChangedListener(selectionChangedListener);
//		Activator.getDefault().getEclipseJobManager().removeEclipseJobManagerListener(jobManagerListener);

		showColumnImage.dispose();
		statusColumnImage.dispose();

		linkHelper.dispose();

		getSite().getPage().removePartListener(partListener);

		super.dispose();
	}

	protected void tryRefresh() {
		ViewerHelper.refresh(viewer, false);
	}

	@Override
	protected CommonViewer createCommonViewer(final Composite aParent) {
		final CommonViewer viewer = super.createCommonViewer(aParent);

		this.viewer = viewer;
		viewer.setLabelProvider(new ScenarioServiceNavigatorDecoratingLabelProvider(viewer.getNavigatorContentService().createCommonLabelProvider()));

		viewer.getTree().setHeaderVisible(true);

		// Keep order in sync with COLUMN_XXX_IDX constants declared in this class

		final TreeColumn labelColumn = new TreeColumn(viewer.getTree(), SWT.NONE);
		labelColumn.setText("Name");
		labelColumn.setWidth(275);

		final TreeColumn checkColumn = new TreeColumn(viewer.getTree(), SWT.NONE);
		checkColumn.setImage(showColumnImage);
		checkColumn.setToolTipText("Display");
		checkColumn.setAlignment(SWT.CENTER);
		checkColumn.setWidth(30);
		checkColumn.setResizable(false);

		final TreeColumn statusColumn = new TreeColumn(viewer.getTree(), SWT.NONE);
		statusColumn.setImage(statusColumnImage);
		statusColumn.setToolTipText("In sync with base case");
		statusColumn.setAlignment(SWT.CENTER);
		statusColumn.setWidth(30);
		statusColumn.setResizable(false);

		final Tree tree = viewer.getTree();
		tree.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				TreeItem selected = null;
				for (final TreeItem i : tree.getItems()) {
					selected = processTreeItem(i, e);
					if (selected != null) {
						break;
					}
				}
				if (selected != null) {
					if (e.button == 1) {
						// if (!selectionModeTrackEditor) {
						final Rectangle imageBounds = selected.getImageBounds(COLUMN_SHOW_IDX);
						if ((e.x > imageBounds.x) && (e.x < (imageBounds.x + selected.getImage().getBounds().width))) {
							if ((e.y > imageBounds.y) && (e.y < (imageBounds.y + selected.getImage().getBounds().height))) {

								final Object data = selected.getData();
								if (data instanceof ScenarioInstance) {
									final ScenarioInstance instance = (ScenarioInstance) data;
									IScenarioServiceSelectionProvider scenarioServiceSelectionProvider = Activator.getDefault().getScenarioServiceSelectionProvider();
									if (scenarioServiceSelectionProvider.getPinned() == instance) {
										scenarioServiceSelectionProvider.setPinned((ScenarioResult) null, false);
									} else {
										scenarioServiceSelectionProvider.setPinned(new ScenarioResultImpl(instance), false);
									}
								}
							}
						}
					}
				} else {
					viewer.setSelection(StructuredSelection.EMPTY);
				}
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.MouseAdapter#mouseDown(org.eclipse.swt.events.
			 * MouseEvent)
			 */
			@Override
			public void mouseDown(final MouseEvent e) {

				TreeItem selected = null;
				for (final TreeItem i : tree.getItems()) {
					selected = processTreeItem(i, e);
					if (selected != null) {
						break;
					}
				}
				if (selected != null) {
					if (e.button == 1) {
						final Rectangle imageBounds = selected.getImageBounds(COLUMN_SHOW_IDX);
						if ((e.x > imageBounds.x) && (e.x < (imageBounds.x + selected.getImage().getBounds().width))) {
							if ((e.y > imageBounds.y) && (e.y < (imageBounds.y + selected.getImage().getBounds().height))) {

								final Object data = selected.getData();
								if (data instanceof ScenarioInstance) {
									final ScenarioInstance instance = (ScenarioInstance) data;
									IScenarioServiceSelectionProvider provider = Activator.getDefault().getScenarioServiceSelectionProvider();
									if (provider.isSelected(instance)) {
										provider.deselect(instance, false);
									} else {
										provider.select(new ScenarioResultImpl(instance), false);
									}
								}
							}
						}
					}
				} else {
					viewer.setSelection(StructuredSelection.EMPTY);
				}
			}

			private TreeItem processTreeItem(final TreeItem item, final MouseEvent e) {

				if (item.getImage() != null) {
					final Rectangle imageBounds = item.getBounds();
					if ((e.y > imageBounds.y) && (e.y < (imageBounds.y + item.getImage().getBounds().height))) {
						return item;
					}
				}
				for (final TreeItem i : item.getItems()) {
					final TreeItem selected = processTreeItem(i, e);
					if (selected != null) {
						return selected;
					}
				}
				return null;
			}
		});

		tree.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(final KeyEvent e) {
				if (e.character == SWT.SPACE) {
					// if (!selectionModeTrackEditor) {
					final TreeItem[] selection = tree.getSelection();
					for (final TreeItem item : selection) {
						final Object data = item.getData();
						if (data instanceof ScenarioInstance) {
							final ScenarioInstance instance = (ScenarioInstance) data;
							if (/* !selectionModeTrackEditor || */instance != lastAutoSelection) {
								IScenarioServiceSelectionProvider provider = Activator.getDefault().getScenarioServiceSelectionProvider();
								if (provider.isSelected(instance)) {
									provider.deselect(instance, false);
								} else {
									provider.select(new ScenarioResultImpl(instance), false);
								}
							}
						}
					}
					// }
				}
			}

			@Override
			public void keyPressed(final KeyEvent e) {

			}
		});
		/*
		 * final Action a = new Action("Toggle Track Editor", IAction.AS_CHECK_BOX) {
		 * 
		 * public void run() {
		 * 
		 * selectionModeTrackEditor = !selectionModeTrackEditor;
		 * 
		 * if (selectionModeTrackEditor) {
		 * partListener.partActivated(getSite().getPage().getActiveEditor()); } } };
		 * a.setImageDescriptor(Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
		 * "/icons/synced.gif")); a.setChecked(selectionModeTrackEditor);
		 * 
		 * getViewSite().getActionBars().getToolBarManager().add(a);
		 */
		// getViewSite().getActionBars().getToolBarManager().update(true);

		// We cannot set this programatically for a common navigator. Instead look in
		// the plugin.xml!
		// PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(),
		// "com.mmxlabs.lingo.doc.View_Navigator");

		return viewer;
	}

	@Override
	protected ActionGroup createCommonActionGroup() {
		// Initialise this here as normally the action in instantiated within th
		linkHelper = new ScenarioServiceNavigatorLinkHelper(this, viewer, getLinkHelperService());
		linkHelper.setChecked(true);
		ActionGroup actionGroup = super.createCommonActionGroup();
		setCollapseAllIcons(actionGroup);

		return actionGroup;
	}

	@Override
	public void init(final IViewSite aSite, final IMemento aMemento) throws PartInitException {
		super.init(aSite, aMemento);

		// Enable linking by default
		setLinkingEnabled(true);

		aSite.getPage().addPartListener(partListener);

		partListener.partActivated(getSite().getPage().getActiveEditor());

	}

	@Override
	protected Object getInitialInput() {

		final ScenarioModel scenarioModel = tracker.getService().getScenarioModel();

		return scenarioModel;
	}

	/**
	 * This accesses a cached version of the property sheet. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public IPropertySheetPage getPropertySheetPage() {
		final ExtendedPropertySheetPage propertySheetPage = new ExtendedPropertySheetPage(editingDomain) {
			@Override
			public void setSelectionToViewer(final List<?> selection) {
				ScenarioServiceNavigator.this.setFocus();
			}
		};
		propertySheetPage.setPropertySourceProvider(new AdapterFactoryContentProvider(adapterFactory));

		return propertySheetPage;
	}

	@Override
	public <T> T getAdapter(final Class<T> key) {
		if (key.equals(IPropertySheetPage.class)) {
			// Debugging only
			// return getPropertySheetPage();
			return key.cast(null);
		} else {
			return super.getAdapter(key);
		}
	}

	@Override
	protected void handleDoubleClick(final DoubleClickEvent anEvent) {
		final ICommandService commandService = (ICommandService) getSite().getService(ICommandService.class);

		final Command command = commandService.getCommand("com.mmxlabs.scenario.service.ui.open");
		if (command.isEnabled()) {
			final IHandlerService handlerService = (IHandlerService) getSite().getService(IHandlerService.class);
			try {
				handlerService.executeCommand("com.mmxlabs.scenario.service.ui.open", null);
			} catch (final Exception e) {
				log.error(e.getMessage(), e);
			}
		} else {
			super.handleDoubleClick(anEvent);
		}
	}

	/**
	 * Super hacky attempt to set the collapse all icon from it's default icon.
	 * Pretend to be an action bar and check the instanceof the action passed to one
	 * of the add method.
	 * 
	 * @param actionGroup
	 */
	private void setCollapseAllIcons(ActionGroup actionGroup) {
		actionGroup.fillActionBars(new IActionBars() {

			@Override
			public void clearGlobalActionHandlers() {

			}

			@Override
			public IAction getGlobalActionHandler(String actionId) {
				return null;
			}

			@Override
			public IMenuManager getMenuManager() {
				return new IMenuManager() {

					@Override
					public void update(String id) {

					}

					@Override
					public void update() {

					}

					@Override
					public void setVisible(boolean visible) {

					}

					@Override
					public void setParent(IContributionManager parent) {

					}

					@Override
					public void saveWidgetState() {
						// TODO Auto-generated method stub

					}

					@Override
					public boolean isVisible() {
						return false;
					}

					@Override
					public boolean isSeparator() {
						return false;
					}

					@Override
					public boolean isGroupMarker() {
						return false;
					}

					@Override
					public boolean isDynamic() {
						return false;
					}

					@Override
					public String getId() {
						return null;
					}

					@Override
					public void fill(CoolBar parent, int index) {

					}

					@Override
					public void fill(ToolBar parent, int index) {
						// TODO Auto-generated method stub

					}

					@Override
					public void fill(Menu parent, int index) {

					}

					@Override
					public void fill(Composite parent) {

					}

					@Override
					public void dispose() {

					}

					@Override
					public void update(boolean force) {

					}

					@Override
					public void removeAll() {

					}

					@Override
					public IContributionItem remove(IContributionItem item) {
						return null;
					}

					@Override
					public IContributionItem remove(String id) {
						return null;
					}

					@Override
					public void prependToGroup(String groupName, IContributionItem item) {

					}

					@Override
					public void prependToGroup(String groupName, IAction action) {

					}

					@Override
					public void markDirty() {

					}

					@Override
					public boolean isEmpty() {
						return false;
					}

					@Override
					public boolean isDirty() {
						return false;
					}

					@Override
					public void insertBefore(String id, IContributionItem item) {

					}

					@Override
					public void insertBefore(String id, IAction action) {

					}

					@Override
					public void insertAfter(String id, IContributionItem item) {

					}

					@Override
					public void insertAfter(String id, IAction action) {

					}

					@Override
					public IContributionManagerOverrides getOverrides() {
						return null;
					}

					@Override
					public IContributionItem[] getItems() {
						return null;
					}

					@Override
					public IContributionItem find(String id) {
						return null;
					}

					@Override
					public void appendToGroup(String groupName, IContributionItem item) {

					}

					@Override
					public void appendToGroup(String groupName, IAction action) {

					}

					@Override
					public void add(IContributionItem item) {

					}

					@Override
					public void add(IAction action) {

					}

					@Override
					public void updateAll(boolean force) {

					}

					@Override
					public void setRemoveAllWhenShown(boolean removeAll) {

					}

					@Override
					public void removeMenuListener(IMenuListener listener) {

					}

					@Override
					public boolean isEnabled() {
						return false;
					}

					@Override
					public boolean getRemoveAllWhenShown() {
						return false;
					}

					@Override
					public IContributionItem findUsingPath(String path) {
						return null;
					}

					@Override
					public IMenuManager findMenuUsingPath(String path) {
						return null;
					}

					@Override
					public void addMenuListener(IMenuListener listener) {

					}
				};
			}

			@Override
			public IServiceLocator getServiceLocator() {
				return null;
			}

			@Override
			public IStatusLineManager getStatusLineManager() {
				return null;
			}

			@Override
			public IToolBarManager getToolBarManager() {
				return new IToolBarManager() {

					@Override
					public void update(boolean force) {

					}

					@Override
					public void removeAll() {

					}

					@Override
					public IContributionItem remove(IContributionItem item) {
						return null;
					}

					@Override
					public IContributionItem remove(String id) {
						return null;
					}

					@Override
					public void prependToGroup(String groupName, IContributionItem item) {

					}

					@Override
					public void prependToGroup(String groupName, IAction action) {

					}

					@Override
					public void markDirty() {

					}

					@Override
					public boolean isEmpty() {
						return false;
					}

					@Override
					public boolean isDirty() {
						return false;
					}

					@Override
					public void insertBefore(String id, IContributionItem item) {

					}

					@Override
					public void insertBefore(String id, IAction action) {

					}

					@Override
					public void insertAfter(String id, IContributionItem item) {

					}

					@Override
					public void insertAfter(String id, IAction action) {

					}

					@Override
					public IContributionManagerOverrides getOverrides() {
						return null;
					}

					@Override
					public IContributionItem[] getItems() {
						return null;
					}

					@Override
					public IContributionItem find(String id) {
						return null;
					}

					@Override
					public void appendToGroup(String groupName, IContributionItem item) {

					}

					@Override
					public void appendToGroup(String groupName, IAction action) {

					}

					@Override
					public void add(IContributionItem item) {

					}

					@Override
					public void add(IAction action) {
						if (action instanceof CollapseAllAction ca) {
							CommonImages.setImageDescriptors(ca, IconPaths.CollapseAll, true);
							CommonImages.setImageDescriptors(ca, IconPaths.CollapseAll);
						}
					}
				};
			}

			@Override
			public void setGlobalActionHandler(String actionId, IAction handler) {

			}

			@Override
			public void updateActionBars() {

			}

		});
	}

	private final IEclipseJobManagerListener jobManagerListener = new IEclipseJobManagerListener() {

		private final IJobControlListener jobControlListener = new IJobControlListener() {
			@Override
			public boolean jobStateChanged(final IJobControl job, final EJobState oldState, final EJobState newState) {
				if (job.getJobDescriptor().getJobContext() instanceof ScenarioInstance instance) {
					ViewerHelper.refresh(viewer, instance, false);
				}
				return true;
			}

			@Override
			public boolean jobProgressUpdated(final IJobControl job, final int progressDelta) {
				if (job.getJobDescriptor().getJobContext() instanceof ScenarioInstance instance) {
					ViewerHelper.refresh(viewer, instance, false);
				}
				return true;
			}
		};

		@Override
		public void jobRemoved(final IEclipseJobManager eclipseJobManager, final IJobDescriptor job, final IJobControl control, final Object resource) {
			if (control != null) {
				control.removeListener(jobControlListener);
			}
			if (job.getJobContext() instanceof ScenarioInstance instance) {
				ViewerHelper.refresh(viewer, instance, false);
			}
		}

		@Override
		public void jobManagerRemoved(final IEclipseJobManager eclipseJobManager, final IJobManager jobManager) {

		}

		@Override
		public void jobManagerAdded(final IEclipseJobManager eclipseJobManager, final IJobManager jobManager) {

		}

		@Override
		public void jobAdded(final IEclipseJobManager eclipseJobManager, final IJobDescriptor job, final IJobControl control, final Object resource) {
			control.addListener(jobControlListener);
			if (job.getJobContext() instanceof ScenarioInstance instance) {
				ViewerHelper.refresh(viewer, instance, false);
			}
		}
	};
}

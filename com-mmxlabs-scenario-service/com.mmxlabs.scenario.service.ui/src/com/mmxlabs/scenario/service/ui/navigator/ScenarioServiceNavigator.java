/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
import org.eclipse.swt.widgets.Display;
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
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.plugin.AbstractUIPlugin;
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
import com.mmxlabs.scenario.service.ScenarioServiceRegistry;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioModel;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionChangedListener;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;
import com.mmxlabs.scenario.service.ui.internal.Activator;

public class ScenarioServiceNavigator extends CommonNavigator {

	public static final int COLUMN_NAME_IDX = 0;
	public static final int COLUMN_SHOW_IDX = 1;
	public static final int COLUMN_PROGRESS_IDX = 2;

	private static final Logger log = LoggerFactory.getLogger(ScenarioServiceNavigator.class);

	private final Image showColumnImage;
	private final Image optImage;

//	private boolean selectionModeTrackEditor = true;

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
//			if (selectionModeTrackEditor) {
				// If the selection tracks editor, then get the scenario instance and make it the only selection.
				if (part instanceof IEditorPart) {
					final IEditorPart editorPart = (IEditorPart) part;
					final IEditorInput editorInput = editorPart.getEditorInput();
					final ScenarioInstance scenarioInstance = (ScenarioInstance) editorInput.getAdapter(ScenarioInstance.class);
					if (scenarioInstance != null) {
//						ScenarioServiceSelectionProvider selectionProvider = Activator.getDefault().getScenarioServiceSelectionProvider();
						if (lastAutoSelection == scenarioInstance) {
							lastAutoSelection = null;
//							if (selectionProvider.isSelected(scenarioInstance) && scenarioInstance != selectionProvider.getPinnedInstance()) {
//								selectionProvider.deselect(scenarioInstance);
//							}
						}
					}
				}
//			}
		}

		@Override
		public void partBroughtToTop(final IWorkbenchPart part) {

		}

		@Override
		public void partActivated(final IWorkbenchPart part) {
//			if (selectionModeTrackEditor) {
				// If the selection tracks editor, then get the scenario instance and make it the only selection.
				if (part instanceof IEditorPart) {
					final IEditorPart editorPart = (IEditorPart) part;
					final IEditorInput editorInput = editorPart.getEditorInput();
					final ScenarioInstance scenarioInstance = (ScenarioInstance) editorInput.getAdapter(ScenarioInstance.class);
					if (scenarioInstance != null) {
//						ScenarioServiceSelectionProvider selectionProvider = Activator.getDefault().getScenarioServiceSelectionProvider();
//						if (lastAutoSelection != null && lastAutoSelection != scenarioInstance) {
//							if (selectionProvider.isSelected(lastAutoSelection) && selectionProvider.getPinnedInstance() != lastAutoSelection) {
//								selectionProvider.deselect(lastAutoSelection);
//							}
//							lastAutoSelection = null;
//						}
//						if (!selectionProvider.isSelected(scenarioInstance)) {
							lastAutoSelection = scenarioInstance;
//							selectionProvider.select(scenarioInstance);
//						}
					}
				}
			}
//		}
	};

	private final IScenarioServiceSelectionChangedListener selectionChangedListener = new IScenarioServiceSelectionChangedListener() {
		@Override
		public void selected(final IScenarioServiceSelectionProvider provider, final Collection<ScenarioInstance> deselected) {
			if (viewer != null) {
				for (final ScenarioInstance instance : deselected)
					viewer.refresh(instance, true);
			}
		}

		@Override
		public void deselected(final IScenarioServiceSelectionProvider provider, final Collection<ScenarioInstance> deselected) {
			if (viewer != null) {
				for (final ScenarioInstance instance : deselected)
					viewer.refresh(instance, true);
			}
		}

		@Override
		public void pinned(final IScenarioServiceSelectionProvider provider, final ScenarioInstance oldPin, final ScenarioInstance newPin) {
			if (oldPin != null)
				viewer.refresh(oldPin, true);
			if (newPin != null)
				viewer.refresh(newPin, true);
		}
	};

	private final IEclipseJobManagerListener jobManagerListener = new IEclipseJobManagerListener() {

		private final IJobControlListener jobControlListener = new IJobControlListener() {
			@Override
			public boolean jobStateChanged(final IJobControl job, final EJobState oldState, final EJobState newState) {
				tryRefresh();
				return true;
			}

			@Override
			public boolean jobProgressUpdated(final IJobControl job, final int progressDelta) {
				tryRefresh();
				return true;
			}
		};

		@Override
		public void jobRemoved(final IEclipseJobManager eclipseJobManager, final IJobDescriptor job, final IJobControl control, final Object resource) {
			control.removeListener(jobControlListener);
			tryRefresh();
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
			tryRefresh();
		}
	};

	public ScenarioServiceNavigator() {
		super();

		tracker = new ServiceTracker<ScenarioServiceRegistry, ScenarioServiceRegistry>(Activator.getDefault().getBundle().getBundleContext(), ScenarioServiceRegistry.class, null);
		tracker.open();

		Activator.getDefault().getScenarioServiceSelectionProvider().addSelectionChangedListener(selectionChangedListener);
		Activator.getDefault().getEclipseJobManager().addEclipseJobManagerListener(jobManagerListener);
		showColumnImage = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/console_view.gif").createImage();
		optImage = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/opt.gif").createImage();
	}

	@Override
	public void dispose() {
		tracker.close();
		Activator.getDefault().getScenarioServiceSelectionProvider().removeSelectionChangedListener(selectionChangedListener);
		Activator.getDefault().getEclipseJobManager().removeEclipseJobManagerListener(jobManagerListener);

		showColumnImage.dispose();
		optImage.dispose();

		linkHelper.dispose();

		getSite().getPage().removePartListener(partListener);

		super.dispose();
	}

	protected void tryRefresh() {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				final CommonViewer viewer1 = viewer;
				if (viewer1 != null && !viewer1.getControl().isDisposed()) {
					viewer1.refresh();
				}
			}

		});
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
		
		final TreeColumn progressColumn = new TreeColumn(viewer.getTree(), SWT.NONE);
		progressColumn.setImage(optImage);
		progressColumn.setWidth(30);
		progressColumn.setResizable(false);

		final Tree tree = viewer.getTree();
		tree.addMouseListener(new MouseAdapter() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.MouseAdapter#mouseDown(org.eclipse.swt.events.MouseEvent)
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
						// if (!selectionModeTrackEditor) {
						final Rectangle imageBounds = selected.getImageBounds(COLUMN_SHOW_IDX);
						if ((e.x > imageBounds.x) && (e.x < (imageBounds.x + selected.getImage().getBounds().width))) {
							if ((e.y > imageBounds.y) && (e.y < (imageBounds.y + selected.getImage().getBounds().height))) {

								final Object data = selected.getData();
								if (data instanceof ScenarioInstance) {
									final ScenarioInstance instance = (ScenarioInstance) data;
//									if (!selectionModeTrackEditor || instance != lastAutoSelection) {
										Activator.getDefault().getScenarioServiceSelectionProvider().toggleSelection(instance);
//									}
								}
							}
						}
						// }
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
							if (/*!selectionModeTrackEditor ||*/ instance != lastAutoSelection) {
								Activator.getDefault().getScenarioServiceSelectionProvider().toggleSelection(instance);
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
		final Action a = new Action("Toggle Track Editor", IAction.AS_CHECK_BOX) {

			public void run() {

				selectionModeTrackEditor = !selectionModeTrackEditor;

				if (selectionModeTrackEditor) {
					partListener.partActivated(getSite().getPage().getActiveEditor());
				}
			}
		};
		a.setImageDescriptor(Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/synced.gif"));
		a.setChecked(selectionModeTrackEditor);

		getViewSite().getActionBars().getToolBarManager().add(a);
		*/
		getViewSite().getActionBars().getToolBarManager().update(true);

		return viewer;
	}

	@Override
	protected ActionGroup createCommonActionGroup() {
		// Initialise this here as normally the action in instantiated within th
		linkHelper = new ScenarioServiceNavigatorLinkHelper(this, viewer, getLinkHelperService());
		linkHelper.setChecked(true);
		return super.createCommonActionGroup();
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
	 * This accesses a cached version of the property sheet. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public IPropertySheetPage getPropertySheetPage() {
		final ExtendedPropertySheetPage propertySheetPage = new ExtendedPropertySheetPage(editingDomain) {
			@Override
			public void setSelectionToViewer(final List<?> selection) {
				// ScenarioServiceNavigator.this.setSelectionToViewer(selection);
				ScenarioServiceNavigator.this.setFocus();
			}

			@Override
			public void setActionBars(final IActionBars actionBars) {
				super.setActionBars(actionBars);
				// getActionBarContributor().shareGlobalActions(this, actionBars);
			}
		};
		propertySheetPage.setPropertySourceProvider(new AdapterFactoryContentProvider(adapterFactory));

		return propertySheetPage;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(final Class key) {
		if (key.equals(IPropertySheetPage.class)) {
			return getPropertySheetPage();
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
	//
	// @Override
	// public void saveState(IMemento aMemento) {
	// // TODO Auto-generated method stub
	// super.saveState(aMemento);
	//
	// Object[] expandedElements = getCommonViewer().getExpandedElements();
	//
	// Properties props = new Properties();
	//
	// for (Object obj : expandedElements) {
	//
	// if (obj instanceof Container) {
	// Container container = (Container) obj;
	// IScenarioService scenarioService = container.getScenarioService();
	// String name = scenarioService.getName();
	// String uriFragment = container.eResource().getURIFragment(container);
	// TODO need to make this unique, but easy to parse.
	// Might be an issue with same path, but different services.
	// props.put(uriFragment, name);
	// }
	//
	// }
	//
	// StringWriter writer = new StringWriter();
	// try {
	// props.store(writer, null);
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// memento.putString("NAVIGATOR_STATE", writer.getBuffer().toString());
	// }
}

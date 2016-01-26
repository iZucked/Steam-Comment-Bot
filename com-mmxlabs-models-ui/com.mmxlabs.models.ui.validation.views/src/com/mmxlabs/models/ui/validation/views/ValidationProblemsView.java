/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation.views;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.ui.validation.gui.FlatValidationStatusContentProvider;
import com.mmxlabs.models.ui.validation.gui.IValidationStatusGoto;
import com.mmxlabs.models.ui.validation.gui.ValidationStatusColumnLabelProvider;
import com.mmxlabs.models.ui.validation.gui.ValidationStatusComparator;
import com.mmxlabs.models.ui.validation.gui.ValidationStatusLabelProvider;
import com.mmxlabs.rcp.common.actions.CopyTreeToClipboardAction;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;
import com.mmxlabs.scenario.service.ui.editing.ScenarioServiceEditorInput;

public class ValidationProblemsView extends ViewPart {

	private static final Logger log = LoggerFactory.getLogger(ValidationProblemsView.class);

	public static final String VIEW_ID = "com.mmxlabs.models.ui.validation.views.ValidationProblemsView";

	private TreeViewer viewer;

	private final Map<ScenarioInstance, IStatus> statusMap = new HashMap<ScenarioInstance, IStatus>();
	private final Set<ScenarioInstance> notifierInstances = new HashSet<>();
	private ScenarioInstance currentInstance = null;
	private IEditorPart editorPart;
	private final IPartListener partListener = new IPartListener() {

		@Override
		public void partOpened(final IWorkbenchPart part) {

		}

		@Override
		public void partDeactivated(final IWorkbenchPart part) {

		}

		@Override
		public void partClosed(final IWorkbenchPart part) {
			if (part == editorPart) {
				if (currentInstance != null) {
					releaseScenarioInstance(currentInstance);
					currentInstance = null;
				}
				editorPart = null;
				refreshViewer();
			}
		}

		@Override
		public void partBroughtToTop(final IWorkbenchPart part) {
			// If the selection tracks editor, then get the scenario instance and make it the only selection.
			if (part instanceof IEditorPart) {
				editorPart = (IEditorPart) part;
				final IEditorInput editorInput = editorPart.getEditorInput();
				final ScenarioInstance scenarioInstance = (ScenarioInstance) editorInput.getAdapter(ScenarioInstance.class);
				if (scenarioInstance != currentInstance) {
					if (currentInstance != null) {
						releaseScenarioInstance(currentInstance);
						currentInstance = null;
					}

					if (scenarioInstance != null) {
						currentInstance = scenarioInstance;
						hookScenarioInstance(scenarioInstance);

					}
				}
				refreshViewer();
			}
		}

		@Override
		public void partActivated(final IWorkbenchPart part) {

			// If the selection tracks editor, then get the scenario instance and make it the only selection.
			if (part instanceof IEditorPart) {
				editorPart = (IEditorPart) part;
				final IEditorInput editorInput = editorPart.getEditorInput();
				final ScenarioInstance scenarioInstance = (ScenarioInstance) editorInput.getAdapter(ScenarioInstance.class);
				if (scenarioInstance != currentInstance) {
					if (currentInstance != null) {
						releaseScenarioInstance(currentInstance);
						currentInstance = null;
					}

					if (scenarioInstance != null) {
						currentInstance = scenarioInstance;
						hookScenarioInstance(scenarioInstance);

					}
				}
				refreshViewer();
			}
		}
	};

	private final EContentAdapter scenarioInstanceChangedListener = new EContentAdapter() {
		@Override
		public void notifyChanged(final Notification notification) {

			if (notification.getFeature() == ScenarioServicePackage.eINSTANCE.getScenarioInstance_ValidationStatusCode()) {
				final ScenarioInstance instance = (ScenarioInstance) notification.getNotifier();
				if (instance.getValidationStatusCode() == IStatus.OK) {
					statusMap.remove(instance);
				} else {
					statusMap.put(instance, (IStatus) instance.getAdapters().get(IStatus.class));
				}
				refreshViewer();
			}
		}
	};

	@Override
	public void createPartControl(final Composite parent) {
		viewer = new TreeViewer(parent);

		viewer.getTree().setLinesVisible(true);
		// viewer.getTree().setHeaderVisible(true);
		viewer.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);

		final FlatValidationStatusContentProvider contentProvider = new FlatValidationStatusContentProvider();
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(new ValidationStatusLabelProvider());
		viewer.setComparator(new ValidationStatusComparator());

		{
			final TreeViewerColumn col = new TreeViewerColumn(viewer, SWT.NONE);
			col.setLabelProvider(new ValidationStatusColumnLabelProvider());
			col.getColumn().setWidth(1000);
			col.getColumn().setResizable(true);
		}

		getSite().getPage().addPartListener(partListener);
		partListener.partActivated(getSite().getPage().getActiveEditor());

		viewer.setInput(statusMap);

		viewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(final DoubleClickEvent event) {

				final ISelection selection = event.getSelection();
				if (selection.isEmpty()) {
					return;
				}

				if (selection instanceof IStructuredSelection) {
					final IStructuredSelection iStructuredSelection = (IStructuredSelection) selection;

					Object element = iStructuredSelection.getFirstElement();
					while (element != null && !(element instanceof ScenarioInstance)) {

						element = contentProvider.getParent(element);
						if (element instanceof Map.Entry) {
							element = ((Map.Entry<?, ?>) element).getKey();
						}
					}
					if (element instanceof ScenarioInstance) {
						try {
							final IStatus status = (IStatus) iStructuredSelection.getFirstElement();
							final ScenarioInstance instance = (ScenarioInstance) element;
							openEditor(instance, status);
							openViews(instance, status);
						} catch (final PartInitException e) {
							log.error(e.getMessage(), e);
						}
					}

				}
			}
		});

		CopyTreeToClipboardAction copyAction = new CopyTreeToClipboardAction(viewer.getTree());
		getViewSite().getActionBars().getToolBarManager().add(copyAction);
		getViewSite().getActionBars().getToolBarManager().update(true);
		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyAction);

		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.lingo.doc.View_Validation");
	}

	@Override
	public void dispose() {
		getSite().getPage().removePartListener(partListener);
		for (final ScenarioInstance instance : notifierInstances) {
			instance.eAdapters().remove(scenarioInstanceChangedListener);
		}

		super.dispose();
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	private void hookScenarioInstance(final ScenarioInstance instance) {
		instance.eAdapters().add(scenarioInstanceChangedListener);
		notifierInstances.add(instance);
		if (instance.getValidationStatusCode() != IStatus.OK) {
			final Map<Class<?>, Object> adapters = instance.getAdapters();
			if (adapters != null) {
				statusMap.put(instance, (IStatus) adapters.get(IStatus.class));
			}
		}
	}

	private void releaseScenarioInstance(final ScenarioInstance instance) {
		if (instance != null) {
			instance.eAdapters().remove(scenarioInstanceChangedListener);
			notifierInstances.add(instance);

			statusMap.remove(instance);
		}
	}

	public void openEditor(final ScenarioInstance scenarioInstance, final IStatus status) throws PartInitException {

		final ScenarioServiceEditorInput editorInput = new ScenarioServiceEditorInput(scenarioInstance);

		final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if (editorPart != null) {
			activePage.activate(editorPart);

			IValidationStatusGoto gotor = null;
			if (editorPart instanceof IValidationStatusGoto) {
				gotor = (IValidationStatusGoto) editorPart;

			}
			if (gotor == null) {
				gotor = (IValidationStatusGoto) editorPart.getAdapter(IValidationStatusGoto.class);
			}

			if (gotor != null) {
				gotor.openStatus(status);
			}
		} else {
			final IEditorRegistry registry = PlatformUI.getWorkbench().getEditorRegistry();
			final String contentTypeString = editorInput.getContentType();
			final IContentType contentType = contentTypeString == null ? null : Platform.getContentTypeManager().getContentType(contentTypeString);

			final IEditorDescriptor descriptor = registry.getDefaultEditor(editorInput.getName(), contentType);

			if (descriptor != null) {

				final ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getDefault().getActiveShell());
				try {
					dialog.run(false, false, new IRunnableWithProgress() {
						@Override
						public void run(final IProgressMonitor monitor) {
							monitor.beginTask("Opening editor", IProgressMonitor.UNKNOWN);
							try {
								final IEditorPart editor = activePage.openEditor(editorInput, descriptor.getId());

								IValidationStatusGoto gotor = null;
								if (editor instanceof IValidationStatusGoto) {
									gotor = (IValidationStatusGoto) editor;
								}
								if (gotor == null) {
									gotor = (IValidationStatusGoto) editor.getAdapter(IValidationStatusGoto.class);
								}

								if (gotor != null) {
									gotor.openStatus(status);
								}
								monitor.worked(1);
							} catch (final PartInitException e) {
								log.error(e.getMessage(), e);
							} finally {
								monitor.done();
							}
						}
					});
				} catch (final InvocationTargetException e) {
					log.error(e.getMessage(), e);
				} catch (final InterruptedException e) {
					log.error(e.getMessage(), e);
				}

			}
		}
	}

	public void openViews(final ScenarioInstance scenarioInstance, final IStatus status) {

		final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		final IViewReference[] viewReferences = activePage.getViewReferences();
		for (final IViewReference viewRef : viewReferences) {
			// Only try views which have been activated
			final IViewPart view = viewRef.getView(false);
			if (view == null) {
				continue;
			}
			IValidationStatusGoto gotor = null;
			if (view instanceof IValidationStatusGoto) {
				gotor = (IValidationStatusGoto) view;

			}
			if (gotor == null) {
				gotor = (IValidationStatusGoto) view.getAdapter(IValidationStatusGoto.class);
			}

			if (gotor != null) {
				// TODO: How to determine content i.e. which scenario instance?
				gotor.openStatus(status);
			}
		}
	}

	private void refreshViewer() {
		if (!viewer.getControl().isDisposed()) {
			// viewer.refresh();
			// viewer.expandAll();
			viewer.setInput(null);
			viewer.setInput(statusMap);
		}
	}
}

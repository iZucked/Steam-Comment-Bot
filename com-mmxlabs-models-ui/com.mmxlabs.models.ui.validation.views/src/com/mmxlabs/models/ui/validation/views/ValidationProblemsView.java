/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation.views;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
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
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.ui.validation.gui.IValidationStatusGoto;
import com.mmxlabs.models.ui.validation.gui.ValidationStatusColumnLabelProvider;
import com.mmxlabs.models.ui.validation.gui.ValidationStatusComparator;
import com.mmxlabs.models.ui.validation.gui.ValidationStatusContentProvider;
import com.mmxlabs.models.ui.validation.gui.ValidationStatusLabelProvider;
import com.mmxlabs.models.ui.validation.views.internal.Activator;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.IScenarioServiceListener;
import com.mmxlabs.scenario.service.impl.ScenarioServiceListener;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;
import com.mmxlabs.scenario.service.ui.editing.ScenarioServiceEditorInput;

public class ValidationProblemsView extends ViewPart {

	private static final Logger log = LoggerFactory.getLogger(ValidationProblemsView.class);

	public static final String VIEW_ID = "com.mmxlabs.models.ui.validation.views.ValidationProblemsView";

	private TreeViewer viewer;

	private BundleContext context;

	private final Map<ScenarioInstance, IStatus> statusMap = new HashMap<ScenarioInstance, IStatus>();

	private final EContentAdapter scenarioInstanceChangedListener = new EContentAdapter() {
		public void notifyChanged(final Notification notification) {

			if (notification.getFeature() == ScenarioServicePackage.eINSTANCE.getScenarioInstance_ValidationStatusCode()) {
				final ScenarioInstance instance = (ScenarioInstance) notification.getNotifier();
				if (instance.getValidationStatusCode() == IStatus.OK) {
					statusMap.remove(instance);
				} else {
					statusMap.put(instance, (IStatus) instance.getAdapters().get(IStatus.class));
				}
				viewer.refresh();
			}
		}
	};

	private final ServiceListener serviceListener = new ServiceListener() {

		@SuppressWarnings("unchecked")
		@Override
		public void serviceChanged(final ServiceEvent event) {
			if (event.getType() == ServiceEvent.REGISTERED) {
				final ServiceReference<IScenarioService> ref = (ServiceReference<IScenarioService>) event.getServiceReference();
				final IScenarioService service = context.getService(ref);
				addContentAdapters(service);
				service.addScenarioServiceListener(scenarioServiceListener);
			} else if (event.getType() == ServiceEvent.UNREGISTERING) {
				final ServiceReference<IScenarioService> ref = (ServiceReference<IScenarioService>) event.getServiceReference();
				final IScenarioService service = context.getService(ref);
				removeContentAdapters(service);
				service.removeScenarioServiceListener(scenarioServiceListener);
			}

		}
	};

	private final IScenarioServiceListener scenarioServiceListener = new ScenarioServiceListener() {
		public void onPostScenarioInstanceLoad(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {
			hookScenarioInstance(scenarioInstance);
		}

		public void onPreScenarioInstanceUnload(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {
			releaseScenarioInstance(scenarioInstance);
		}

		public void onPreScenarioInstanceDelete(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {
			releaseScenarioInstance(scenarioInstance);
		}
	};

	@Override
	public void createPartControl(final Composite parent) {
		viewer = new TreeViewer(parent);

		viewer.getTree().setLinesVisible(true);
		// viewer.getTree().setHeaderVisible(true);

		final ValidationStatusContentProvider contentProvider = new ValidationStatusContentProvider();
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(new ValidationStatusLabelProvider());
		viewer.setComparator(new ValidationStatusComparator());

		{
			final TreeViewerColumn col = new TreeViewerColumn(viewer, SWT.NONE);
			col.setLabelProvider(new ValidationStatusColumnLabelProvider());
			col.getColumn().setWidth(1000);
			col.getColumn().setResizable(true);
		}

		context = Activator.getDefault().getBundle().getBundleContext();

		try {
			context.addServiceListener(serviceListener, "(objectClass=" + IScenarioService.class + ")");
		} catch (final InvalidSyntaxException e) {
			log.error(e.getMessage(), e);
		}

		Collection<ServiceReference<IScenarioService>> serviceReferences;
		try {
			serviceReferences = context.getServiceReferences(IScenarioService.class, null);
			for (final ServiceReference<IScenarioService> ref : serviceReferences) {
				final IScenarioService service = context.getService(ref);
				addContentAdapters(service);
				service.addScenarioServiceListener(scenarioServiceListener);
			}
		} catch (final InvalidSyntaxException e) {
			log.error(e.getMessage(), e);

		}
		viewer.setInput(statusMap);
		viewer.expandAll();

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
	}

	@Override
	public void dispose() {
		context.removeServiceListener(serviceListener);

		Collection<ServiceReference<IScenarioService>> serviceReferences;
		try {
			serviceReferences = context.getServiceReferences(IScenarioService.class, null);
			for (final ServiceReference<IScenarioService> ref : serviceReferences) {
				final IScenarioService service = context.getService(ref);
				service.removeScenarioServiceListener(scenarioServiceListener);
				removeContentAdapters(service);

			}
		} catch (final InvalidSyntaxException e) {
			log.error(e.getMessage(), e);

		}

		super.dispose();
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	private void addContentAdapters(final IScenarioService service) {
		final ScenarioService model = service.getServiceModel();
		final TreeIterator<EObject> itr = model.eAllContents();
		while (itr.hasNext()) {
			final EObject eObj = itr.next();
			hookScenarioInstance(eObj);
		}
	}

	private void hookScenarioInstance(final EObject eObj) {
		if (eObj instanceof ScenarioInstance) {
			final ScenarioInstance instance = (ScenarioInstance) eObj;
			instance.eAdapters().add(scenarioInstanceChangedListener);
			if (instance.getValidationStatusCode() != IStatus.OK) {
				final Map<Class<?>, Object> adapters = instance.getAdapters();
				if (adapters != null) {
					statusMap.put(instance, (IStatus) adapters.get(IStatus.class));
				}
			}
		}
	}

	private void removeContentAdapters(final IScenarioService service) {
		final ScenarioService model = service.getServiceModel();
		final TreeIterator<EObject> itr = model.eAllContents();
		while (itr.hasNext()) {
			final EObject eObj = itr.next();
			releaseScenarioInstance(eObj);
		}
	}

	private void releaseScenarioInstance(final EObject eObj) {
		if (eObj instanceof ScenarioInstance) {
			final ScenarioInstance instance = (ScenarioInstance) eObj;
			instance.eAdapters().remove(scenarioInstanceChangedListener);
			statusMap.remove(instance);
		}
	}

	public void openEditor(final ScenarioInstance scenarioInstance, final IStatus status) throws PartInitException {

		final ScenarioServiceEditorInput editorInput = new ScenarioServiceEditorInput(scenarioInstance);

		final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		final IEditorPart editorPart = activePage.findEditor(editorInput);
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
}

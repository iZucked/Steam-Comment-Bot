package com.mmxlabs.models.ui.validation.gui;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.ui.validation.internal.Activator;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

public class ValidationProblemsView extends ViewPart {

	private static final Logger log = LoggerFactory.getLogger(ValidationProblemsView.class);

	public static final String VIEW_ID = "com.mmxlabs.models.ui.validation.gui.ValidationProblemsView";

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
			} else if (event.getType() == ServiceEvent.UNREGISTERING) {
				final ServiceReference<IScenarioService> ref = (ServiceReference<IScenarioService>) event.getServiceReference();
				final IScenarioService service = context.getService(ref);
				removeContentAdapters(service);
			}

		}
	};

	@Override
	public void createPartControl(final Composite parent) {
		viewer = new TreeViewer(parent);

		viewer.getTree().setLinesVisible(true);
		// viewer.getTree().setHeaderVisible(true);

		viewer.setContentProvider(new ValidationStatusContentProvider());
		viewer.setLabelProvider(new ValidationStatusLabelProvider());
		viewer.setComparator(new ValidationStatusComparator());

		{
			final TreeViewerColumn col = new TreeViewerColumn(viewer, SWT.NONE);
			col.setLabelProvider(new ValidationStatusColumnLabelProvider());
			col.getColumn().setWidth(500);
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
				addContentAdapters(context.getService(ref));

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

					// TODO Auto-generated method stub
					final IEditorPart activeEditor = getSite().getWorkbenchWindow().getActivePage().getActiveEditor();
					IValidationStatusGoto gotor = null;
					if (activeEditor instanceof IValidationStatusGoto) {
						gotor = (IValidationStatusGoto) activeEditor;

					}
					if (gotor == null) {
						gotor = (IValidationStatusGoto) activeEditor.getAdapter(IValidationStatusGoto.class);
					}

					if (gotor != null) {
						gotor.openStatus((IStatus) iStructuredSelection.getFirstElement());
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
				removeContentAdapters(context.getService(ref));

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
	}

	private void removeContentAdapters(final IScenarioService service) {
		final ScenarioService model = service.getServiceModel();
		final TreeIterator<EObject> itr = model.eAllContents();
		while (itr.hasNext()) {
			final EObject eObj = itr.next();
			if (eObj instanceof ScenarioInstance) {
				final ScenarioInstance instance = (ScenarioInstance) eObj;
				instance.eAdapters().remove(scenarioInstanceChangedListener);
				statusMap.remove(instance);
			}
		}
	}
}

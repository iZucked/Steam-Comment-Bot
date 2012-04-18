/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editorpart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManagerListener;
import com.mmxlabs.jobmanager.eclipse.manager.impl.EclipseJobManagerAdapter;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlListener;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.jobmanager.jobs.impl.JobControlAdapter;
import com.mmxlabs.models.mmxcore.IMMXAdapter;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.jointmodel.JointModel;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.commandservice.IModelCommandProvider;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.models.ui.valueproviders.ReferenceValueProviderCache;

/**
 * An editor part for editing MMX Root Objects.
 * 
 * TODO top level object may want to contribute functionality
 * 
 * @author hinton
 * 
 */
public class JointModelEditorPart extends MultiPageEditorPart implements IEditorPart, IEditingDomainProvider, ISelectionProvider {
	private static final Logger log = LoggerFactory.getLogger(JointModelEditorPart.class);
	/**
	 * This joint model instance should contain and provide the root object
	 */
	private JointModel jointModel;
	/**
	 * The root object from {@link #jointModel}
	 */
	private MMXRootObject rootObject;

	/**
	 * The editing domain for controls to use. This is pretty standard, but hooks command creation to allow extra things there
	 */
	private AdapterFactoryEditingDomain editingDomain;
	private ComposedAdapterFactory adapterFactory;
	private BasicCommandStack commandStack;

	/**
	 * This caches reference value provider providers.
	 */
	private IReferenceValueProviderProvider referenceValueProviderCache = null;
	private Collection<ISelectionChangedListener> selectionChangedListeners = new ArrayList<ISelectionChangedListener>();
	/**
	 * This holds the selection of the active viewer.
	 */
	private ISelection editorSelection = StructuredSelection.EMPTY;

	private List<IJointModelEditorContribution> contributions = new LinkedList<IJointModelEditorContribution>();
	private ICommandHandler defaultCommandHandler = new ICommandHandler() {
		@Override
		public void handleCommand(Command command, EObject target, EStructuralFeature feature) {
			getEditingDomain().getCommandStack().execute(command);
		}

		@Override
		public IReferenceValueProviderProvider getReferenceValueProviderProvider() {
			return JointModelEditorPart.this.getReferenceValueProviderCache();
		}

		@Override
		public EditingDomain getEditingDomain() {
			return JointModelEditorPart.this.getEditingDomain();
		}
	};
	private boolean saving = false;
	private Viewer currentViewer;
	private ISelectionChangedListener selectionChangedListener;

	/**
	 * Used to track jobs for the displayed joint model, and lock the UI when jobs are running.
	 */
	private IEclipseJobManagerListener jobManagerListener = new EclipseJobManagerAdapter() {
		@Override
		public void jobAdded(IEclipseJobManager eclipseJobManager, IJobDescriptor job, IJobControl control, Object resource) {
			if (job.getJobContext() == getRootObject() || job.getJobContext() == jointModel) {
				control.addListener(jobListener);
			}
		}

		@Override
		public void jobRemoved(IEclipseJobManager eclipseJobManager, IJobDescriptor job, IJobControl control, Object resource) {
			control.removeListener(jobListener); // no harm in removing even if it's not there
		}
	};
	
	private IJobControlListener jobListener = new JobControlAdapter() {
		@Override
		public boolean jobStateChanged(IJobControl control, EJobState oldState, EJobState newState) {
			switch (newState) {
			case INITIALISING:
			case INITIALISED:
			case PAUSED:
			case PAUSING:
			case RESUMING:
			case RUNNING:
				setLocked(true);
				break;
			default:
				setLocked(false);
			}
			return true;
		}
	};

	private boolean locked;
	private boolean commandProvidersDisabled;
	private PropertySheetPage propertySheetPage;

	public JointModelEditorPart() {

	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
		for (final IJointModelEditorContribution contribution : contributions) {
			contribution.setLocked(locked);
		}
	}

	public boolean isSaving() {
		return saving;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO use other invocation with scheduling rule
		try {
			ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
				@Override
				public void run(IProgressMonitor monitor) throws CoreException {
					try {
						saving = true;
						monitor.beginTask("Saving", 1);
						jointModel.save();
						monitor.worked(1);
					} catch (IOException e) {
						log.error("IO Error during save", e);
					} finally {
						monitor.done();
						saving = false;
						commandStack.saveIsDone();
						firePropertyChange(PROP_DIRTY);
					}
				}
			}, monitor);
		} catch (CoreException e) {
			log.error("Error during save", e);
		}
	}

	@Override
	public void doSaveAs() {

	}
	
	@Override
	public Object getAdapter(final Class adapter) {
		if (adapter.isAssignableFrom(IPropertySheetPage.class)) {
			if (propertySheetPage == null) {
				propertySheetPage = new PropertySheetPage();
				propertySheetPage.setPropertySourceProvider(
						new IPropertySourceProvider() {
							@Override
							public IPropertySource getPropertySource(Object object) {
								for (final IJointModelEditorContribution contribution : contributions) {
									if (contribution instanceof IPropertySourceProvider) {
										IPropertySource delegate = ((IPropertySourceProvider) contribution).getPropertySource(object);
										if (delegate != null) return delegate;
									}
								}
								return null;
							}
						}
						);
			}
			return propertySheetPage;
		}
		return super.getAdapter(adapter);
	}
	
	@Override
	public void init(final IEditorSite site, final IEditorInput input) throws PartInitException {
		super.init(site, input);
		setPartName(input.getName());
		JointModel jointModel = (JointModel) input.getAdapter(JointModel.class);

		if (jointModel == null)
			jointModel = (JointModel) Platform.getAdapterManager().loadAdapter(input, JointModel.class.getCanonicalName());
		if (jointModel == null) {
			throw new PartInitException("Could not adapt input to JointModel");
		} else {
			try {
				final MMXRootObject root = jointModel.load();
				if (root == null) {
					throw new PartInitException("JointModel has no root object");
				}
				this.jointModel = jointModel;
				this.rootObject = root;
				// set up adapter factory
				adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
				adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
				adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
			
				// synchronize command execution on the root object; this is a temporary
				// solution to allow other threads (evaluation thread for example) to ensure a consistent scenario 
				// state when they want to do something.
				// This probably belongs in the scenario service in some future version.
				commandStack = new BasicCommandStack() {
					@Override
					public void execute(Command command) {
						synchronized (rootObject) {
							super.execute(command);
						}
					}
				};
				
				commandStack.addCommandStackListener(new CommandStackListener() {
					@Override
					public void commandStackChanged(EventObject event) {
						if (commandStack.isSaveNeeded()) {
							firePropertyChange(IEditorPart.PROP_DIRTY);
						}
					}
				});

				final ServiceTracker<IModelCommandProvider, IModelCommandProvider> commandProviderTracker = Activator.getDefault().getCommandProviderTracker();
				// create editing domain
				editingDomain = new AdapterFactoryEditingDomain(adapterFactory, commandStack) {
					@Override
					public Command createCommand(Class<? extends Command> commandClass, CommandParameter commandParameter) {
						final Command normal = super.createCommand(commandClass, commandParameter);

						if (!commandProvidersDisabled) {
							final CompoundCommand wrapper = new CompoundCommand();
							wrapper.append(normal);
							for (final IModelCommandProvider provider : commandProviderTracker.getServices(new IModelCommandProvider[0])) {
								final Command addition = provider.provideAdditionalCommand(getEditingDomain(), getRootObject(), commandClass, commandParameter, normal);
								if (addition != null)
									wrapper.append(addition);
							}

							return wrapper.unwrap();
						} else {
							return normal;
						}
					}
				};

				// initialize extensions

				contributions = Activator.getDefault().getJointModelEditorContributionRegistry().initEditorContributions(this, root);

				referenceValueProviderCache = new ReferenceValueProviderCache(rootObject);

				Activator.getDefault().getJobManager().addEclipseJobManagerListener(jobManagerListener);
			} catch (MigrationException e) {
				throw new PartInitException("Error migrating joint model", e);
			} catch (IOException e) {
				throw new PartInitException("IO Exception loading joint model", e);
			}
		}

		site.setSelectionProvider(this);
	}

	@Override
	public void dispose() {
		Activator.getDefault().getJobManager().removeEclipseJobManagerListener(jobManagerListener);
		for (final IJointModelEditorContribution contribution : contributions) {
			contribution.dispose();
		}
		super.dispose();
	}

	@Override
	public boolean isDirty() {
		return commandStack.isSaveNeeded();
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void setFocus() {

	}

	@Override
	public EditingDomain getEditingDomain() {
		return editingDomain;
	}

	@Override
	protected void createPages() {
		for (final IJointModelEditorContribution contribution : contributions) {
			contribution.addPages(getContainer());
		}
	}

	@Override
	public void setPageImage(int pageIndex, Image image) {
		super.setPageImage(pageIndex, image);
	}

	@Override
	public void setPageText(int pageIndex, String text) {
		super.setPageText(pageIndex, text);
	}

	public AdapterFactory getAdapterFactory() {
		return adapterFactory;
	}

	public IReferenceValueProviderProvider getReferenceValueProviderCache() {
		return referenceValueProviderCache;
	}

	/**
	 * Get a reference value provider for the given reference on the given EClass. Delegates to the result of {@link #getReferenceValueProviderCache()}
	 * 
	 * @param owner
	 * @param reference
	 * @return
	 */
	public IReferenceValueProvider getReferenceValueProvider(EClass owner, EReference reference) {
		return referenceValueProviderCache.getReferenceValueProvider(owner, reference);
	}

	public ICommandHandler getDefaultCommandHandler() {
		return defaultCommandHandler;
	}

	public MMXRootObject getRootObject() {
		return rootObject;
	}

	@Override
	public ISelection getSelection() {
		return editorSelection;
	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		selectionChangedListeners.add(listener);
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		selectionChangedListeners.remove(listener);
	}

	@Override
	public void setSelection(ISelection selection) {
		this.editorSelection = selection;
		final SelectionChangedEvent event = new SelectionChangedEvent(this, getSelection());
		for (final ISelectionChangedListener l : selectionChangedListeners) {
			l.selectionChanged(event);
		}
	}

	public void setDisableCommandProviders(final boolean disable) {
		this.commandProvidersDisabled = disable;
	}

	public void setDisableUpdates(final boolean disable) {
		if (disable) {
			disableAdapters(getRootObject());
		} else {
			enableAdapters(getRootObject());
		}
	}

	private void disableAdapters(final EObject top) {
		for (final Adapter a : top.eAdapters()) {
			if (a instanceof IMMXAdapter) {
				((IMMXAdapter) a).disable();
			}
		}
		for (final EObject o : top.eContents())
			disableAdapters(o);
	}

	private void enableAdapters(final EObject top) {
		for (final Adapter a : top.eAdapters()) {
			if (a instanceof IMMXAdapter) {
				((IMMXAdapter) a).enable();
			}
		}
		for (final EObject o : top.eContents())
			enableAdapters(o);
	}

	/**
	 * This makes sure that one content viewer, either for the current page or the outline view, if it has focus, is the current one. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setCurrentViewer(final Viewer viewer) {
		// If it is changing...
		//
		if (currentViewer != viewer) {
			if (selectionChangedListener == null) {
				// Create the listener on demand.
				//
				selectionChangedListener = new ISelectionChangedListener() {
					// This just notifies those things that are affected by the
					// section.
					//
					@Override
					public void selectionChanged(final SelectionChangedEvent selectionChangedEvent) {
						setSelection(selectionChangedEvent.getSelection());
					}
				};
			}

			// Stop listening to the old one.
			//
			if (currentViewer != null) {
				currentViewer.removeSelectionChangedListener(selectionChangedListener);
			}

			// Start listening to the new one.
			//
			if (viewer != null) {
				viewer.addSelectionChangedListener(selectionChangedListener);
			}

			// Remember it.
			//
			currentViewer = viewer;

			// Set the editors selection based on the current viewer's
			// selection.
			//
			setSelection(currentViewer == null ? StructuredSelection.EMPTY : currentViewer.getSelection());
		}
	}
}

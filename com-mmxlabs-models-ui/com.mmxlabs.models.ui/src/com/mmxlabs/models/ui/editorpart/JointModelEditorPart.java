/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editorpart;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.ui.ViewerPane;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.celleditor.AdapterFactoryTreeEditor;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.view.ExtendedPropertySheetPage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.IMMXRootObjectProvider;
import com.mmxlabs.models.ui.IScenarioInstanceProvider;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.models.ui.validation.gui.IValidationStatusGoto;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.models.ui.valueproviders.ReferenceValueProviderCache;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.editors.IPartGotoTarget;
import com.mmxlabs.rcp.common.editors.IReasonProvider;
import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;
import com.mmxlabs.scenario.service.ui.editing.IDiffEditHandler;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceDiffingEditorInput;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;
import com.mmxlabs.scenario.service.util.ScenarioInstanceSchedulingRule;

/**
 * An editor part for editing MMX Root Objects.
 * 
 * 
 * @author hinton
 * 
 */
public class JointModelEditorPart extends MultiPageEditorPart implements ISelectionProvider, IScenarioEditingLocation, IMMXRootObjectProvider, IScenarioInstanceProvider, IValidationStatusGoto {

	/**
	 * Flag to enable the default EMF tree view editor
	 * 
	 * NEVER COMMIT AS TRUE
	 */
	private static final boolean DEBUG = false;

	private static final Logger log = LoggerFactory.getLogger(JointModelEditorPart.class);

	private Throwable initException = null;

	private final Stack<IExtraValidationContext> validationContextStack = new Stack<IExtraValidationContext>();

	private ModelReference modelReference;

	/**
	 * The root object from {@link #jointModel}
	 */
	private MMXRootObject rootObject;

	private ScenarioInstanceStatusProvider scenarioInstanceStatusProvider;

	/**
	 * The editing domain for controls to use. This is pretty standard, but hooks command creation to allow extra things there
	 */
	private CommandProviderAwareEditingDomain editingDomain;
	private AdapterFactory adapterFactory;
	private BasicCommandStack commandStack;

	/**
	 * This caches reference value provider providers.
	 */
	private IReferenceValueProviderProvider referenceValueProviderCache = null;
	private final Collection<ISelectionChangedListener> selectionChangedListeners = new ArrayList<ISelectionChangedListener>();
	/**
	 * This holds the selection of the active viewer.
	 */
	private ISelection editorSelection = StructuredSelection.EMPTY;

	private List<IJointModelEditorContribution> contributions = new LinkedList<IJointModelEditorContribution>();

	private final ICommandHandler defaultCommandHandler = new ICommandHandler() {
		@Override
		public void handleCommand(final Command command, final EObject target, final EStructuralFeature feature) {
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

	private final IPartGotoTarget partGotoTarget = new IPartGotoTarget() {

		@Override
		public void gotoTarget(final Object object) {
			for (final IJointModelEditorContribution contrib : contributions) {
				if (contrib instanceof IPartGotoTarget) {
					((IPartGotoTarget) contrib).gotoTarget(object);
				}
			}
		}
	};

	private boolean saving = false;
	private Viewer currentViewer;
	private ISelectionChangedListener selectionChangedListener;

	private boolean locked;
	private PropertySheetPage propertySheetPage;

	private ScenarioInstance scenarioInstance;

	private AdapterImpl lockedAdapter;
	private AdapterImpl scenarioAttributeAdapter;

	private TreeViewer selectionViewer;

	private Image editorTitleImage;

	private ScenarioLock editorLock;

	private ScenarioInstance referenceInstance;

	private ScenarioLock referenceLock;

	private IPartListener referencePinPartListener;

	private IScenarioServiceSelectionProvider scenarioSelectionProvider;

	private IDiffEditHandler diffEditHandler;

	public JointModelEditorPart() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation#isLocked()
	 */
	@Override
	public boolean isLocked() {
		return locked;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation#setLocked(boolean)
	 */
	@Override
	public void setLocked(final boolean locked) {
		RunnerHelper.asyncExec(new Runnable() {
			@Override
			public void run() {
				final ScenarioInstance scenarioInstance2 = getScenarioInstance();
				if (scenarioInstance2 == null) {
					return;
				}
				boolean validationLock = false;
				String title = getEditorInput().getName();
				for (final ScenarioLock lock : scenarioInstance2.getLocks()) {
					if (lock.isClaimed()) {
						if (lock.getKey() == ScenarioLock.VALIDATION) {
							// Validation locks should not really stop editing, so track that here
							validationLock = true;
						} else {
							title += " (locked for " + lock.getKey() + ")";
						}
					}
				}
				setPartName(title);
				updateTitleImage(getEditorInput());
				// It is possible (due to asyncExec) that the lock has since been cleared, so see whether it is still live now
				// Use the detected lock status, rather than provided to get the new lock status - ignore validation locks
				final boolean newLock = !validationLock && scenarioInstance2.isLocked();
				// Only update state if it has changed.
				if (JointModelEditorPart.this.locked == newLock) {
					return;
				}
				JointModelEditorPart.this.locked = newLock;
				for (final IJointModelEditorContribution contribution : contributions) {
					contribution.setLocked(locked);
				}

			}
		});
	}

	public boolean isSaving() {
		return saving;
	}

	@Override
	public void doSave(final IProgressMonitor monitor) {
		// TODO use other invocation with scheduling rule
		try {
			ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
				@Override
				public void run(final IProgressMonitor monitor) throws CoreException {
					try {
						saving = true;
						scenarioInstance.save();
						monitor.worked(1);
					} catch (final IOException e) {
						log.error("IO Error during save", e);
					} finally {
						monitor.done();
						saving = false;
						firePropertyChange(PROP_DIRTY);
					}
				}

			}, new ScenarioInstanceSchedulingRule(scenarioInstance), 0, monitor);
		} catch (final CoreException e) {
			log.error("Error during save", e);
		}
	}

	@Override
	public void doSaveAs() {

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <T> T getAdapter(final Class<T> adapter) {
		if (adapter.isAssignableFrom(IPropertySheetPage.class)) {
			if (propertySheetPage == null) {
				propertySheetPage = new PropertySheetPage();
				propertySheetPage.setPropertySourceProvider(new IPropertySourceProvider() {
					@Override
					public IPropertySource getPropertySource(final Object object) {
						for (final IJointModelEditorContribution contribution : contributions) {
							if (contribution instanceof IPropertySourceProvider) {
								final IPropertySource delegate = ((IPropertySourceProvider) contribution).getPropertySource(object);
								if (delegate != null) {
									return delegate;
								}
							}
						}
						return null;
					}
				});
				if (DEBUG && propertySheetPage == null)
					propertySheetPage = new ExtendedPropertySheetPage(editingDomain) {
						@Override
						public void setSelectionToViewer(final List<?> selection) {
							selectionViewer.setSelection(new StructuredSelection(selection));
							JointModelEditorPart.this.setFocus();
						}

						@Override
						public void setActionBars(final IActionBars actionBars) {
							super.setActionBars(actionBars);
						}
					};
				propertySheetPage.setPropertySourceProvider(new AdapterFactoryContentProvider(adapterFactory));
			}
			return adapter.cast(propertySheetPage);
		} else if (adapter.isAssignableFrom(IValidationStatusGoto.class)) {
			return adapter.cast(this);
		} else if (adapter.isAssignableFrom(IPartGotoTarget.class)) {
			return adapter.cast(partGotoTarget);
		}

		return super.getAdapter(adapter);
	}

	@Override
	public void init(final IEditorSite site, final IEditorInput input) throws PartInitException {

		try {

			super.init(site, input);
			setPartName(input.getName());
			final MMXRootObject root;
			final ScenarioInstance instance;
			if (input instanceof IScenarioServiceDiffingEditorInput) {
				final IScenarioServiceDiffingEditorInput ssInput = (IScenarioServiceDiffingEditorInput) input;

				updateTitleImage(ssInput);

				instance = ssInput.getCurrentScenarioInstance();

				diffEditHandler = ssInput.getDiffEditHandler();

				referenceInstance = ssInput.getReferenceScenarioInstance();
				referenceLock = referenceInstance.getLock(ScenarioLock.EDITORS);

				// TODO: Sensible?
				referenceLock.awaitClaim();

				final BundleContext bundleContext = Activator.getDefault().getBundle().getBundleContext();
				final ServiceReference<IScenarioServiceSelectionProvider> serviceReference = bundleContext.getServiceReference(IScenarioServiceSelectionProvider.class);
				scenarioSelectionProvider = bundleContext.getService(serviceReference);
				if (scenarioSelectionProvider != null) {
					referencePinPartListener = new IPartListener() {

						@Override
						public void partOpened(final IWorkbenchPart part) {
							if (part == JointModelEditorPart.this) {
								// Ensure pin is set.
								setPinMode();
							}

						}

						@Override
						public void partDeactivated(final IWorkbenchPart part) {
							// TODO Auto-generated method stub

						}

						@Override
						public void partClosed(final IWorkbenchPart part) {
							// TODO Auto-generated method stub

						}

						@Override
						public void partBroughtToTop(final IWorkbenchPart part) {
							if (part == JointModelEditorPart.this) {
								// Ensure pin is set.
								setPinMode();
							}

						}

						@Override
						public void partActivated(final IWorkbenchPart part) {
							if (part == JointModelEditorPart.this) {
								// Ensure pin is set.
								setPinMode();
							}

						}
					};
					getSite().getPage().addPartListener(referencePinPartListener);

				}
			} else if (input instanceof IScenarioServiceEditorInput) {

				final IScenarioServiceEditorInput ssInput = (IScenarioServiceEditorInput) input;

				updateTitleImage(ssInput);

				instance = ssInput.getScenarioInstance();
			} else {
				// Error!
				throw new IllegalArgumentException("Editor input should be instance of IScenarioServiceEditorInput");
			}

			editorLock = instance.getLock(ScenarioLock.EDITORS);

			modelReference = instance.getReference("JointModelEditorPart");
			final EObject ro = modelReference.getInstance();
			if (ro == null) {
				throw new RuntimeException("Instance was not loaded");
			}
			scenarioInstance = instance;
			scenarioInstanceStatusProvider = new ScenarioInstanceStatusProvider(scenarioInstance);

			commandStack = (BasicCommandStack) instance.getAdapters().get(BasicCommandStack.class);
			editingDomain = (CommandProviderAwareEditingDomain) instance.getAdapters().get(EditingDomain.class);

			adapterFactory = editingDomain.getAdapterFactory();

			lockedAdapter = new AdapterImpl() {
				@Override
				public void notifyChanged(final Notification msg) {
					if (msg.isTouch() == false && msg.getFeature() == ScenarioServicePackage.eINSTANCE.getScenarioLock_Available()) {
						setLocked(!msg.getNewBooleanValue());
					}
				}
			};
			editorLock.eAdapters().add(lockedAdapter);

			scenarioAttributeAdapter = new AdapterImpl() {
				@Override
				public void notifyChanged(final Notification msg) {
					if (msg.isTouch() == false && msg.getFeature() == ScenarioServicePackage.eINSTANCE.getContainer_Name()) {
						RunnerHelper.asyncExec(new Runnable() {
							@Override
							public void run() {
								setPartName(msg.getNewStringValue());
							}
						});
					}
					if (msg.isTouch() == false && msg.getFeature() == ScenarioServicePackage.eINSTANCE.getScenarioInstance_Dirty()) {
						RunnerHelper.asyncExec(new Runnable() {
							@Override
							public void run() {
								firePropertyChange(IEditorPart.PROP_DIRTY);
							}
						});
					}
				}
			};
			instance.eAdapters().add(scenarioAttributeAdapter);

			if (ro instanceof MMXRootObject) {
				root = (MMXRootObject) ro;
			} else {
				// Wrong type.
				throw new RuntimeException("Root object is of type " + ro.getClass().getName() + ", not MMXRootObject");
			}
			this.rootObject = root;

			{
				commandStack.addCommandStackListener(new CommandStackListener() {
					@Override
					public void commandStackChanged(final EventObject event) {
						RunnerHelper.asyncExec(new Runnable() {
							@Override
							public void run() {
								firePropertyChange(IEditorPart.PROP_DIRTY);
							}
						});
					}
				});

				// initialize extensions

				contributions = Activator.getDefault().getJointModelEditorContributionRegistry().initEditorContributions(this, rootObject);

				referenceValueProviderCache = new ReferenceValueProviderCache(rootObject);
			}

			site.setSelectionProvider(this);

			validationContextStack.clear();
			validationContextStack.push(new DefaultExtraValidationContext(getRootObject(), false));

			setPinMode();
			setLocked(!editorLock.isAvailable());
		} catch (final Throwable t) {
			// Clean up internal data structures etc
			cleanup();

			// Display some kind of better UI than default error view.
			for (int i = 0; i < getPageCount(); ++i) {
				removePage(i);
			}
			initException = t;

		}
	}

	protected void createErrorPage(final Throwable t) {
		getContainer().setLayout(new FillLayout());

		boolean expandTechnicalDetails = false;
		IReasonProvider reasonProvider = (IReasonProvider) Platform.getAdapterManager().loadAdapter(t, IReasonProvider.class.getCanonicalName());
		if (reasonProvider == null) {
			reasonProvider = new SimpleReasonProvider(t);
			expandTechnicalDetails = true;
		}
		final FormToolkit toolkit = new FormToolkit(getShell().getDisplay());
		final ScrolledForm scrolledForm = toolkit.createScrolledForm(getContainer());
		final Form form = scrolledForm.getForm();
		toolkit.decorateFormHeading(form);
		form.setText(reasonProvider.getTitle());

		form.getBody().setLayout(new GridLayout(1, true));

		{
			final Section sectionDescription = toolkit.createSection(form.getBody(), ExpandableComposite.TITLE_BAR | ExpandableComposite.EXPANDED);
			sectionDescription.setText("Description");
			sectionDescription.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));

			final Text descriptionText = toolkit.createText(sectionDescription, reasonProvider.getDescription(), SWT.READ_ONLY);
			sectionDescription.setClient(descriptionText);
		}
		{
			final Section sectionResolution = toolkit.createSection(form.getBody(), ExpandableComposite.TITLE_BAR | ExpandableComposite.EXPANDED);
			sectionResolution.setText("Resolution Steps");
			sectionResolution.setLayout(new FillLayout());
			sectionResolution.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));

			final Text resoutionText = toolkit.createText(sectionResolution, reasonProvider.getResolutionSteps(), SWT.READ_ONLY);
			resoutionText.setLayoutData(new GridData(SWT.DEFAULT, 40));
			sectionResolution.setClient(resoutionText);
		}
		{
			final Section technicalDetails = toolkit.createSection(form.getBody(), ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE);
			technicalDetails.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
			technicalDetails.setText("Technical Detail");
			technicalDetails.setLayout(new FillLayout());
			final Throwable throwable = reasonProvider.getThrowable();

			final StringWriter sw = new StringWriter();
			{
				final IEditorInput ei = getEditorInput();
				if (ei instanceof IScenarioServiceEditorInput) {
					final IScenarioServiceEditorInput ssInput = (IScenarioServiceEditorInput) ei;
					final ScenarioInstance instance = ssInput.getScenarioInstance();

					sw.append(String.format("Name: %s\n", instance.getName()));
					sw.append(String.format("UUID: %s\n", instance.getUuid()));
					sw.append(String.format("Scenario Version: %s - %d\n", instance.getVersionContext(), instance.getScenarioVersion()));
					sw.append(String.format("Client Version: %s - %d\n", instance.getClientVersionContext(), instance.getClientScenarioVersion()));
					sw.append("\n");
				}
			}
			sw.append(String.format("Message %s\n", throwable.getMessage()));
			sw.append("\nStackTrace:\n");
			throwable.printStackTrace(new PrintWriter(sw));
			final Text detailsText = toolkit.createText(technicalDetails, sw.toString(), SWT.MULTI | SWT.READ_ONLY);
			technicalDetails.setClient(detailsText);

			technicalDetails.setExpanded(expandTechnicalDetails);
		}

		final int pageIndex = addPage(scrolledForm);
		setPageText(pageIndex, "Error");
	}

	protected void setPinMode() {

		if (scenarioSelectionProvider != null) {
			if (scenarioSelectionProvider.getPinnedInstance() != referenceInstance) {
				scenarioSelectionProvider.deselectAll();
				scenarioSelectionProvider.select(scenarioInstance);
				scenarioSelectionProvider.setPinnedInstance(referenceInstance);
			}
		}
	}

	protected void unsetPinMode() {

		if (scenarioSelectionProvider != null) {
			if (scenarioSelectionProvider.getPinnedInstance() == referenceInstance) {
				scenarioSelectionProvider.deselect(scenarioInstance);
				scenarioSelectionProvider.setPinnedInstance(null);
			}
		}
	}

	private void updateTitleImage(final IEditorInput editorInput) {
		if (scenarioInstance == null) {
			return;
		}
		if (editorInput instanceof IScenarioServiceEditorInput) {
			final IScenarioServiceEditorInput ssInput = (IScenarioServiceEditorInput) getEditorInput();
			final ImageDescriptor imageDescriptor = ssInput.getImageDescriptor();
			if (imageDescriptor != null) {
				final Image img = editorTitleImage;
				editorTitleImage = imageDescriptor.createImage();
				setTitleImage(editorTitleImage);
				if (img != null) {
					img.dispose();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation#getExtraValidationContext()
	 */
	@Override
	public IExtraValidationContext getExtraValidationContext() {
		return validationContextStack.peek();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation#pushExtraValidationContext(com.mmxlabs.models.ui.validation.IExtraValidationContext)
	 */
	@Override
	public void pushExtraValidationContext(final IExtraValidationContext context) {
		validationContextStack.push(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation#popExtraValidationContext()
	 */
	@Override
	public void popExtraValidationContext() {
		validationContextStack.pop();
	}

	@Override
	public void dispose() {
		cleanup();

		super.dispose();
	}

	protected void cleanup() {
		if (referenceLock != null) {
			referenceLock.release();
			referenceLock = null;
		}

		unsetPinMode();

		if (diffEditHandler != null) {
			diffEditHandler.onEditorDisposed();
			diffEditHandler = null;
		}

		if (referencePinPartListener != null) {
			getSite().getPage().removePartListener(referencePinPartListener);
			referencePinPartListener = null;
		}

		if (editorLock != null) {
			editorLock.eAdapters().remove(lockedAdapter);
			this.editorLock = null;
		}
		if (scenarioAttributeAdapter != null) {
			scenarioInstance.eAdapters().remove(scenarioAttributeAdapter);
			this.scenarioAttributeAdapter = null;
		}

		for (final IJointModelEditorContribution contribution : contributions) {
			contribution.dispose();
		}
		contributions.clear();

		if (referenceValueProviderCache != null) {
			referenceValueProviderCache.dispose();
			referenceValueProviderCache = null;
		}

		if (editorTitleImage != null) {
			editorTitleImage.dispose();
			editorTitleImage = null;
		}
		if (scenarioInstanceStatusProvider != null) {
			this.scenarioInstanceStatusProvider.dispose();
			this.scenarioInstanceStatusProvider = null;
		}
		if (propertySheetPage != null) {
			propertySheetPage.dispose();
			propertySheetPage = null;
		}

		this.adapterFactory = null;
		this.commandStack = null;
		this.currentViewer = null;
		this.editingDomain = null;
		this.editorSelection = null;
		this.lockedAdapter = null;
		this.validationContextStack.clear();
		this.selectionViewer = null;
		this.scenarioInstance = null;
		this.selectionChangedListeners.clear();

		this.rootObject = null;
		if (this.modelReference != null) {
			this.modelReference.close();
			this.modelReference = null;
		}
	}

	@Override
	public boolean isDirty() {
		return commandStack != null && commandStack.isSaveNeeded();
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void setFocus() {
		getControl(getActivePage()).setFocus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation#getEditingDomain()
	 */
	@Override
	public EditingDomain getEditingDomain() {
		return editingDomain;
	}

	@Override
	protected void createPages() {

		if (initException != null) {
			createErrorPage(initException);
			return;
		}

		for (final IJointModelEditorContribution contribution : contributions) {
			contribution.addPages(getContainer());
			contribution.setLocked(isLocked());
		}
		if (DEBUG) {
			final ViewerPane viewerPane = new ViewerPane(getSite().getPage(), JointModelEditorPart.this) {
				@Override
				public Viewer createViewer(final Composite composite) {
					final Tree tree = new Tree(composite, SWT.MULTI);
					final TreeViewer newTreeViewer = new TreeViewer(tree);
					return newTreeViewer;
				}

				@Override
				public void requestActivation() {

					super.requestActivation();
					setCurrentViewer(viewer);
				}
			};
			viewerPane.createControl(getContainer());

			selectionViewer = (TreeViewer) viewerPane.getViewer();
			selectionViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));

			selectionViewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));
			selectionViewer.setInput(editingDomain.getResourceSet());
			selectionViewer.setSelection(new StructuredSelection(editingDomain.getResourceSet().getResources().get(0)), true);
			viewerPane.setTitle(editingDomain.getResourceSet());

			new AdapterFactoryTreeEditor(selectionViewer.getTree(), adapterFactory);

			final int pageIndex = addPage(viewerPane.getControl());
			setPageText(pageIndex, "DEBUG");
		}
		// TODO: Be more context sensitive?
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getContainer(), "com.mmxlabs.lingo.doc.Chapter_Editor");
	}

	@Override
	public void setPageImage(final int pageIndex, final Image image) {
		super.setPageImage(pageIndex, image);
	}

	@Override
	public void setPageText(final int pageIndex, final String text) {
		super.setPageText(pageIndex, text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation#getAdapterFactory()
	 */
	@Override
	public AdapterFactory getAdapterFactory() {
		return adapterFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation#getReferenceValueProviderCache()
	 */
	@Override
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
	public IReferenceValueProvider getReferenceValueProvider(final EClass owner, final EReference reference) {
		return referenceValueProviderCache.getReferenceValueProvider(owner, reference);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation#getDefaultCommandHandler()
	 */
	@Override
	public ICommandHandler getDefaultCommandHandler() {
		return defaultCommandHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation#getRootObject()
	 */
	@Override
	public MMXRootObject getRootObject() {
		return rootObject;
	}

	@Override
	public ISelection getSelection() {
		return editorSelection;
	}

	@Override
	public void addSelectionChangedListener(final ISelectionChangedListener listener) {
		selectionChangedListeners.add(listener);
	}

	@Override
	public void removeSelectionChangedListener(final ISelectionChangedListener listener) {
		selectionChangedListeners.remove(listener);
	}

	@Override
	public void setSelection(final ISelection selection) {
		this.editorSelection = selection;
		final SelectionChangedEvent event = new SelectionChangedEvent(this, getSelection());
		for (final ISelectionChangedListener l : selectionChangedListeners) {
			l.selectionChanged(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation#setDisableCommandProviders(boolean)
	 */
	@Override
	public void setDisableCommandProviders(final boolean disable) {
		editingDomain.setCommandProvidersDisabled(disable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation#setDisableUpdates(boolean)
	 */
	@Override
	public void setDisableUpdates(final boolean disable) {
		editingDomain.setAdaptersEnabled(!disable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation#setCurrentViewer(org.eclipse.jface.viewers.Viewer)
	 */
	@Override
	public void setCurrentViewer(final Viewer viewer) {
		// If it is changing...
		if (currentViewer != viewer) {
			if (selectionChangedListener == null) {
				// Create the listener on demand.
				//
				selectionChangedListener = new ISelectionChangedListener() {
					// This just notifies those things that are affected by the
					// selection.
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

			if (propertySheetPage != null) {
				if (viewer instanceof IPropertySourceProvider) {
					propertySheetPage.setPropertySourceProvider((IPropertySourceProvider) viewer);
				} else {
					propertySheetPage.setPropertySourceProvider(new AdapterFactoryContentProvider(adapterFactory));
				}
			}

			// Set the editors selection based on the current viewer's
			// selection.
			//
			setSelection(currentViewer == null ? StructuredSelection.EMPTY : currentViewer.getSelection());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation#getScenarioInstance()
	 */
	@Override
	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation#getShell()
	 */
	@Override
	public Shell getShell() {
		return getSite().getShell();
	}

	@Override
	public ScenarioLock getEditorLock() {
		return editorLock;
	}

	@Override
	public IStatusProvider getStatusProvider() {
		return scenarioInstanceStatusProvider;
	}

	@Override
	public void openStatus(final IStatus status) {

		if (status.isMultiStatus()) {
			// Try first element
			openStatus(status.getChildren()[0]);
		}

		for (final IJointModelEditorContribution contrib : contributions) {
			if (contrib.canHandle(status)) {
				contrib.handle(status);
				return;
			}
		}
	}

	@Override
	public void setActivePage(final int pageIndex) {
		super.setActivePage(pageIndex);
	}

	@Override
	public boolean isSaveOnCloseNeeded() {
		// Handled by ScenarioServiceSaveHook. Avoid double save prompt.
		return false;
	}
}

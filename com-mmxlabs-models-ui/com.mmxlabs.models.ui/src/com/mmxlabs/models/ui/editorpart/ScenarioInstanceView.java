/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editorpart;

import java.io.IOException;
import java.util.Map;
import java.util.Stack;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.IMMXRootObjectProvider;
import com.mmxlabs.models.ui.IScenarioInstanceProvider;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.models.ui.validation.gui.IValidationStatusGoto;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.models.ui.valueproviders.ReferenceValueProviderCache;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

public abstract class ScenarioInstanceView extends ViewPart implements IScenarioEditingLocation, ISelectionListener, IScenarioInstanceProvider, IMMXRootObjectProvider, IValidationStatusGoto {

	private final Logger log = LoggerFactory.getLogger(ScenarioInstanceView.class);

	private ScenarioInstance scenarioInstance;
	private ScenarioInstanceStatusProvider scenarioInstanceStatusProvider;
	private ReferenceValueProviderCache valueProviderCache;

	private ScenarioLock editorLock;

	private boolean locked;

	private final Adapter lockedAdapter = new AdapterImpl() {
		@Override
		public void notifyChanged(final Notification notification) {
			if (notification.getFeature() == ScenarioServicePackage.eINSTANCE.getScenarioLock_Available()) {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						setLocked(!notification.getNewBooleanValue());
					}
				});
			}
		}

	};
	private IPartListener partListener;

	protected void listenToScenarioSelection() {
		partListener = new IPartListener() {
			IWorkbenchPart lastPart = null;

			@Override
			public void partOpened(final IWorkbenchPart part) {

			}

			@Override
			public void partDeactivated(final IWorkbenchPart part) {

			}

			@Override
			public void partClosed(final IWorkbenchPart part) {
				if (part == lastPart) {
					selectionChanged(part, StructuredSelection.EMPTY);
					lastPart = null;
				}
			}

			@Override
			public void partBroughtToTop(final IWorkbenchPart part) {

			}

			@Override
			public void partActivated(final IWorkbenchPart part) {
				if (part instanceof IEditorPart) {
					final IEditorPart editorPart = (IEditorPart) part;
					final IEditorInput editorInput = editorPart.getEditorInput();
					final ScenarioInstance scenarioInstance = (ScenarioInstance) editorInput.getAdapter(ScenarioInstance.class);
					lastPart = part;
					if (scenarioInstance != null) {
						selectionChanged(part, new StructuredSelection(scenarioInstance));
					} else {
						selectionChanged(part, new StructuredSelection());
					}
				}
			}
		};
		getSite().getPage().addPartListener(partListener);
		// getSite().getPage().addSelectionListener(SCENARIO_NAVIGATOR_ID, this);
		partListener.partActivated(getSite().getPage().getActiveEditor());
		// selectionChanged(null, getSite().getPage().getSelection(SCENARIO_NAVIGATOR_ID));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object getAdapter(final Class adapter) {
		if (adapter.isAssignableFrom(IValidationStatusGoto.class)) {
			return this;
		}
		return super.getAdapter(adapter);
	}

	@Override
	public void dispose() {

		if (editorLock != null) {
			editorLock.eAdapters().remove(lockedAdapter);
		}
		if (valueProviderCache != null) {
			valueProviderCache.dispose();
		}
		if (scenarioInstanceStatusProvider != null) {
			scenarioInstanceStatusProvider.dispose();
		}

		// getSite().getPage().removeSelectionListener(SCENARIO_NAVIGATOR_ID, this);
		getSite().getPage().removePartListener(partListener);
		super.dispose();
	}

	@Override
	public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
		if (editorLock != null) {
			editorLock.eAdapters().remove(lockedAdapter);
		}
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection structured = (IStructuredSelection) selection;
			if (structured.size() == 1) {
				if (structured.getFirstElement() instanceof ScenarioInstance) {
					final ScenarioInstance instance = (ScenarioInstance) structured.getFirstElement();
					editorLock = instance.getLock(ScenarioLock.EDITORS);
					editorLock.eAdapters().add(lockedAdapter);
					setLocked(!editorLock.isAvailable());
					displayScenarioInstance(instance);
					return;
				}
			}
		}
		displayScenarioInstance(null);
	}

	protected void displayScenarioInstance(final ScenarioInstance instance) {
		extraValidationContext.clear();
		this.scenarioInstance = instance;
		if (this.valueProviderCache != null) {
			valueProviderCache.dispose();
		}

		if (scenarioInstanceStatusProvider != null) {
			scenarioInstanceStatusProvider.dispose();
		}

		if (instance != null) {
			scenarioInstanceStatusProvider = new ScenarioInstanceStatusProvider(instance);
			getRootObject();

			this.valueProviderCache = new ReferenceValueProviderCache(getRootObject());
			extraValidationContext.push(new DefaultExtraValidationContext(getRootObject(), false));
		} else {
			scenarioInstanceStatusProvider = null;
			valueProviderCache = null;
		}
	}

	@Override
	public boolean isLocked() {
		return locked;
	}

	@Override
	public void setLocked(final boolean locked) {
		this.locked = locked;
	}

	private final Stack<IExtraValidationContext> extraValidationContext = new Stack<IExtraValidationContext>();

	@Override
	public IExtraValidationContext getExtraValidationContext() {
		return extraValidationContext.peek();
	}

	@Override
	public void pushExtraValidationContext(final IExtraValidationContext context) {
		extraValidationContext.push(context);
	}

	@Override
	public void popExtraValidationContext() {
		extraValidationContext.pop();
	}

	@Override
	public EditingDomain getEditingDomain() {
		if (scenarioInstance == null) {
			return null;
		}
		
		final Map<Class<?>, Object> adapters = scenarioInstance.getAdapters();
		if (adapters != null) {
			return (EditingDomain) adapters.get(EditingDomain.class);
		}
		return null;
	}

	@Override
	public AdapterFactory getAdapterFactory() {

		final EditingDomain editingDomain = getEditingDomain();
		if (editingDomain instanceof CommandProviderAwareEditingDomain) {
			final CommandProviderAwareEditingDomain commandProviderAwareEditingDomain = (CommandProviderAwareEditingDomain) editingDomain;
			return commandProviderAwareEditingDomain.getAdapterFactory();
		}

		return null;
	}

	@Override
	public IReferenceValueProviderProvider getReferenceValueProviderCache() {
		return valueProviderCache;
	}

	final ICommandHandler commandHandler = new ICommandHandler() {

		@Override
		public void handleCommand(final Command command, final EObject target, final EStructuralFeature feature) {
			getEditingDomain().getCommandStack().execute(command);
		}

		@Override
		public IReferenceValueProviderProvider getReferenceValueProviderProvider() {
			return ScenarioInstanceView.this.getReferenceValueProviderCache();
		}

		@Override
		public EditingDomain getEditingDomain() {
			return ScenarioInstanceView.this.getEditingDomain();
		}
	};

	@Override
	public ICommandHandler getDefaultCommandHandler() {
		return commandHandler;
	}

	@Override
	public MMXRootObject getRootObject() {

		if (scenarioInstance == null) {
			return null;
		}
		final IScenarioService scenarioService = scenarioInstance.getScenarioService();
		if (scenarioService == null) {
			// This may or may not be null
			return (MMXRootObject) scenarioInstance.getInstance();
		}

		try {
			return (MMXRootObject) scenarioInstance.getScenarioService().load(scenarioInstance);

		} catch (final Exception e) {
			log.error("Error getting root object", e);
			return null;
		}
	}

	@Override
	public void setDisableCommandProviders(final boolean disable) {
		final EditingDomain editingDomain = getEditingDomain();
		if (editingDomain instanceof CommandProviderAwareEditingDomain) {
			((CommandProviderAwareEditingDomain) editingDomain).setCommandProvidersDisabled(disable);
		}
	}

	@Override
	public void setDisableUpdates(final boolean disable) {
		final EditingDomain editingDomain = getEditingDomain();
		if (editingDomain instanceof CommandProviderAwareEditingDomain) {
			((CommandProviderAwareEditingDomain) editingDomain).setAdaptersEnabled(!disable);
		}
	}

	@Override
	public void setCurrentViewer(final Viewer viewer) {
		getSite().setSelectionProvider(viewer);
	}

	@Override
	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}

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
		// Do nothing by default
	}

}

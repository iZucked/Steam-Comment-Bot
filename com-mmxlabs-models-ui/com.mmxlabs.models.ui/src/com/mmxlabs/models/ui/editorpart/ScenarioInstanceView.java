/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editorpart;

import java.io.IOException;
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

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.IMMXRootObjectProvider;
import com.mmxlabs.models.ui.IScenarioInstanceProvider;
import com.mmxlabs.models.ui.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.models.ui.validation.gui.IValidationStatusGoto;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.models.ui.valueproviders.ReferenceValueProviderCache;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

public abstract class ScenarioInstanceView extends ViewPart implements IScenarioEditingLocation, ISelectionListener, IScenarioInstanceProvider, IMMXRootObjectProvider, IValidationStatusGoto {

	private ScenarioInstance scenarioInstance;
	private ScenarioInstanceStatusProvider scenarioInstanceStatusProvider;
	private ReferenceValueProviderCache valueProviderCache;

	private ScenarioLock editorLock;

	private boolean locked;

	private Adapter lockedAdapter = new AdapterImpl() {
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
			public void partOpened(IWorkbenchPart part) {

			}

			@Override
			public void partDeactivated(IWorkbenchPart part) {

			}

			@Override
			public void partClosed(IWorkbenchPart part) {
				if (part == lastPart) {
					selectionChanged(part, StructuredSelection.EMPTY);
				}
			}

			@Override
			public void partBroughtToTop(IWorkbenchPart part) {

			}

			@Override
			public void partActivated(IWorkbenchPart part) {
				if (part instanceof IEditorPart) {
					final IEditorPart editorPart = (IEditorPart) part;
					final IEditorInput editorInput = editorPart.getEditorInput();
					final ScenarioInstance scenarioInstance = (ScenarioInstance) editorInput.getAdapter(ScenarioInstance.class);
					lastPart = part;
					selectionChanged(part, new StructuredSelection(scenarioInstance));
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
	public Object getAdapter(Class adapter) {
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
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (editorLock != null) {
			editorLock.eAdapters().remove(lockedAdapter);
		}
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection structured = (IStructuredSelection) selection;
			if (structured.size() == 1) {
				if (structured.getFirstElement() instanceof ScenarioInstance) {
					ScenarioInstance instance = (ScenarioInstance) structured.getFirstElement();
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
			extraValidationContext.push(new DefaultExtraValidationContext(getRootObject()));
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
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	private Stack<IExtraValidationContext> extraValidationContext = new Stack<IExtraValidationContext>();

	@Override
	public IExtraValidationContext getExtraValidationContext() {
		return extraValidationContext.peek();
	}

	@Override
	public void pushExtraValidationContext(IExtraValidationContext context) {
		extraValidationContext.push(context);
	}

	@Override
	public void popExtraValidationContext() {
		extraValidationContext.pop();
	}

	@Override
	public EditingDomain getEditingDomain() {
		return (EditingDomain) scenarioInstance.getAdapters().get(EditingDomain.class);
	}

	@Override
	public AdapterFactory getAdapterFactory() {
		return null;
	}

	@Override
	public IReferenceValueProviderProvider getReferenceValueProviderCache() {
		return valueProviderCache;
	}

	final ICommandHandler commandHandler = new ICommandHandler() {

		@Override
		public void handleCommand(Command command, EObject target, EStructuralFeature feature) {
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
		try {
			return (MMXRootObject) scenarioInstance.getScenarioService().load(scenarioInstance);
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public void setDisableCommandProviders(boolean disable) {
		final EditingDomain editingDomain = getEditingDomain();
		if (editingDomain instanceof CommandProviderAwareEditingDomain) {
			((CommandProviderAwareEditingDomain) editingDomain).setCommandProvidersDisabled(disable);
		}
	}

	@Override
	public void setDisableUpdates(boolean disable) {
		final EditingDomain editingDomain = getEditingDomain();
		if (editingDomain instanceof CommandProviderAwareEditingDomain) {
			((CommandProviderAwareEditingDomain) editingDomain).setAdaptersEnabled(!disable);
		}
	}

	@Override
	public void setCurrentViewer(Viewer viewer) {
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

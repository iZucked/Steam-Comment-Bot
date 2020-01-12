/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editorpart;

import java.util.Collections;
import java.util.Stack;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.google.common.base.Objects;
import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.IMMXRootObjectProvider;
import com.mmxlabs.models.ui.IScenarioInstanceProvider;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.models.ui.validation.gui.IValidationStatusGoto;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.models.ui.valueproviders.ReferenceValueProviderCache;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.IScenarioLockListener;
import com.mmxlabs.scenario.service.model.manager.ModelRecordScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioLock;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public abstract class ScenarioInstanceView extends ViewPart implements IScenarioEditingLocation, ISelectionListener, IScenarioInstanceProvider, IMMXRootObjectProvider, IValidationStatusGoto {

	protected ScenarioInstance scenarioInstance;
	private ScenarioModelRecord modelRecord;
	private IScenarioDataProvider scenarioDataProvider;
	protected MMXRootObject rootObject;
	protected Object targetObject;

	private ScenarioInstanceStatusProvider scenarioInstanceStatusProvider;
	private ReferenceValueProviderCache valueProviderCache;

	private final Stack<IExtraValidationContext> extraValidationContext = new Stack<>();

	private boolean locked;

	private final IScenarioLockListener lockListener = (pModelRecord, writeLocked) -> {
		RunnerHelper.runNowOrAsync(() -> setLocked(writeLocked || (scenarioInstance != null && scenarioInstance.isReadonly())));
	};
	private IPartListener partListener;

	protected void listenToScenarioSelection() {
		partListener = new IPartListener() {
			private IWorkbenchPart lastPart = null;

			@Override
			public void partClosed(final IWorkbenchPart part) {
				if (part == lastPart) {
					selectionChanged(part, StructuredSelection.EMPTY);
					lastPart = null;
				}
			}

			@Override
			public void partActivated(final IWorkbenchPart part) {
				if (part instanceof IEditorPart) {
					final IEditorPart editorPart = (IEditorPart) part;
					final IEditorInput editorInput = editorPart.getEditorInput();
					final ScenarioInstance editorEcenarioInstance = editorInput.getAdapter(ScenarioInstance.class);
					lastPart = part;
					if (editorEcenarioInstance != null) {
						activeEditorChange(editorEcenarioInstance);
					} else {
						activeEditorChange(null);
					}
				}
			}

			@Override
			public void partOpened(final IWorkbenchPart part) {
				// Nothing needed here
			}

			@Override
			public void partDeactivated(final IWorkbenchPart part) {
				// Nothing needed here
			}

			@Override
			public void partBroughtToTop(final IWorkbenchPart part) {
				// Nothing needed here
			}
		};
		getSite().getPage().addPartListener(partListener);
		partListener.partActivated(getSite().getPage().getActiveEditor());
	}

	@Override
	public <T> T getAdapter(final Class<T> adapter) {
		if (adapter.isAssignableFrom(IValidationStatusGoto.class)) {
			return adapter.cast(this);
		}
		return super.getAdapter(adapter);
	}

	@Override
	public void dispose() {
		resetState();

		if (partListener != null) {
			getSite().getPage().removePartListener(partListener);
		}
		super.dispose();
	}

	@Override
	public synchronized void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
		// Nothing to do now. Maybe remove interface
	}

	protected void activeEditorChange(final ScenarioInstance instance) {
		if (Objects.equal(instance, this.scenarioInstance)) {
			return;
		}

		if (instance == null) {
			displayScenarioInstance(null, null, null);
		} else {
			final ScenarioModelRecord mr = SSDataManager.Instance.getModelRecord(instance);
			try (IScenarioDataProvider sdp = mr.aquireScenarioDataProvider("ScenarioInstanceView:1")) {
				final MMXRootObject ro = sdp.getTypedScenario(MMXRootObject.class);
				displayScenarioInstance(instance, ro, null);
			}
		}
	}

	protected synchronized void displayScenarioInstance(final ScenarioInstance instance, @Nullable final MMXRootObject rootObject, @Nullable final Object targetObject) {

		// Has input changed?
		if (Objects.equal(instance, this.scenarioInstance) && Objects.equal(rootObject, this.rootObject) && Objects.equal(targetObject, this.targetObject)) {
			return;
		}

		resetState();

		if (instance != null) {
			this.scenarioInstance = instance;
			this.rootObject = rootObject;
			this.targetObject = targetObject;

			this.modelRecord = SSDataManager.Instance.getModelRecord(this.scenarioInstance);
			this.scenarioDataProvider = new ModelRecordScenarioDataProvider(this.modelRecord);
			this.scenarioDataProvider.getModelReference().getLock().addLockListener(lockListener);

			scenarioInstanceStatusProvider = createScenarioValidationProvider(instance);
			setLocked(this.scenarioDataProvider.getModelReference().isLocked() || instance.isReadonly());

			this.valueProviderCache = new ReferenceValueProviderCache(rootObject);

			boolean relaxedValidation = false;
			if (scenarioInstance != null) {
				relaxedValidation = "Period Scenario".equals(scenarioInstance.getName());
			}

			extraValidationContext.push(new DefaultExtraValidationContext(scenarioDataProvider, false, relaxedValidation));
			doDisplayScenarioInstance(scenarioInstance, rootObject, targetObject);
		} else {
			scenarioInstanceStatusProvider = null;
			valueProviderCache = null;
			doDisplayScenarioInstance(null, null, null);
		}
	}

	protected ScenarioInstanceStatusProvider createScenarioValidationProvider(final ScenarioInstance instance) {
		return new ScenarioInstanceStatusProvider(instance);
	}

	private void resetState() {
		targetObject = null;
		rootObject = null;

		extraValidationContext.clear();
		if (valueProviderCache != null) {
			valueProviderCache.dispose();
		}

		if (scenarioInstanceStatusProvider != null) {
			scenarioInstanceStatusProvider.dispose();
		}

		if (scenarioDataProvider != null) {
			scenarioDataProvider.getModelReference().getLock().removeLockListener(lockListener);
			scenarioDataProvider.close();
			scenarioDataProvider = null;
		}

		scenarioInstance = null;
		modelRecord = null;
	}

	protected synchronized void doDisplayScenarioInstance(@Nullable final ScenarioInstance scenarioInstance, @Nullable final MMXRootObject rootObject, @Nullable final Object target) {
		// Sublclasses can override to customise behaviour
	}

	@Override
	public boolean isLocked() {
		return locked;
	}

	public void setLocked(final boolean locked) {
		this.locked = locked;
	}

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
		if (scenarioDataProvider == null) {
			return null;
		}

		return scenarioDataProvider.getEditingDomain();
	}

	@Override
	public ModelReference getModelReference() {
		if (scenarioDataProvider == null) {
			throw new IllegalStateException();
		}
		return scenarioDataProvider.getModelReference();
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

		@Override
		public ModelReference getModelReference() {
			return ScenarioInstanceView.this.getModelReference();
		};
	};

	@Override
	public ICommandHandler getDefaultCommandHandler() {
		return commandHandler;
	}

	@Override
	public MMXRootObject getRootObject() {
		return rootObject;
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
	public @NonNull IScenarioDataProvider getScenarioDataProvider() {
		return scenarioDataProvider;
	}

	@Override
	public Shell getShell() {
		return getSite().getShell();
	}

	@Override
	public ScenarioLock getEditorLock() {
		return scenarioDataProvider.getModelReference().getLock();
	}

	@Override
	public IStatusProvider getStatusProvider() {
		return scenarioInstanceStatusProvider;
	}

	@Override
	public void openStatus(final IStatus status) {
		// Do nothing by default
	}

	public void editObject(@Nullable final EObject target) {
		if (target != null) {
			final DetailCompositeDialog dcd = new DetailCompositeDialog(this.getShell(), this.getDefaultCommandHandler());
			dcd.open(this, this.getRootObject(), Collections.singletonList((EObject) target));
		}
	}
}

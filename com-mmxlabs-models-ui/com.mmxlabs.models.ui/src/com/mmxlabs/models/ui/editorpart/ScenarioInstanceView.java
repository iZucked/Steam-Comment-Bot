/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
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
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

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
import com.mmxlabs.scenario.service.model.manager.ModelRecord;
import com.mmxlabs.scenario.service.model.manager.ModelRecordScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioLock;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public abstract class ScenarioInstanceView extends ViewPart implements IScenarioEditingLocation, ISelectionListener, IScenarioInstanceProvider, IMMXRootObjectProvider, IValidationStatusGoto {

	private ScenarioInstance scenarioInstance;
	private ScenarioModelRecord modelRecord;
	private IScenarioDataProvider scenarioDataProvider;
	private ModelReference modelReference;
	private ScenarioInstanceStatusProvider scenarioInstanceStatusProvider;
	private ReferenceValueProviderCache valueProviderCache;

	// private ScenarioLock editorLock;

	private boolean locked;

	private final IScenarioLockListener lockListener = new IScenarioLockListener() {

		@Override
		public void lockStateChanged(@NonNull ModelRecord modelRecord, boolean writeLocked) {
			RunnerHelper.runNowOrAsync(() -> setLocked(writeLocked || (scenarioInstance != null && scenarioInstance.isReadonly())));
		};
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
						try {
							selectionChanged(part, new StructuredSelection(scenarioInstance));
						} catch (final Exception e) {
							selectionChanged(part, new StructuredSelection());
						}
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

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (adapter.isAssignableFrom(IValidationStatusGoto.class)) {
			return adapter.cast(this);
		}
		return super.getAdapter(adapter);
	}

	@Override
	public void dispose() {
		resetState();

		// getSite().getPage().removeSelectionListener(SCENARIO_NAVIGATOR_ID, this);
		if (partListener != null) {
			getSite().getPage().removePartListener(partListener);
		}
		super.dispose();
	}

	@Override
	public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {

		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection structured = (IStructuredSelection) selection;
			if (structured.size() == 1) {
				if (structured.getFirstElement() instanceof ScenarioInstance) {
					final ScenarioInstance instance = (ScenarioInstance) structured.getFirstElement();
					displayScenarioInstance(instance);
					return;
				}
			}
		}
		displayScenarioInstance(null);
	}

	protected void displayScenarioInstance(final ScenarioInstance instance) {
		resetState();

		if (instance != null) {
			scenarioInstanceStatusProvider = createScenarioValidationProvider(instance);
			this.scenarioInstance = instance;
			this.modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
			this.modelReference = modelRecord.aquireReference("ScenarioInstanceView:1");
			this.scenarioDataProvider = new ModelRecordScenarioDataProvider(modelRecord);
			modelReference.getLock().addLockListener(lockListener);
			setLocked(modelReference.isLocked() || instance.isReadonly());
			MMXRootObject rootObject = getRootObject();

			this.valueProviderCache = new ReferenceValueProviderCache(rootObject);
		
			boolean relaxedValidation = false;
			final ScenarioInstance scenarioInstance = modelRecord.getScenarioInstance();
			if (scenarioInstance != null) {
				relaxedValidation = "Period Scenario".equals(scenarioInstance.getName());
			}
						
			extraValidationContext.push(new DefaultExtraValidationContext(scenarioDataProvider, false, relaxedValidation));
			doDisplayScenarioInstance(scenarioInstance, getRootObject());
		} else {
			scenarioInstanceStatusProvider = null;
			valueProviderCache = null;
			doDisplayScenarioInstance(null, null);
		}
	}

	protected ScenarioInstanceStatusProvider createScenarioValidationProvider(final ScenarioInstance instance) {
		return new ScenarioInstanceStatusProvider(instance);
	}

	private void resetState() {
		extraValidationContext.clear();
		if (valueProviderCache != null) {
			valueProviderCache.dispose();
		}

		if (scenarioInstanceStatusProvider != null) {
			scenarioInstanceStatusProvider.dispose();
		}

		if (scenarioDataProvider != null) {
			scenarioDataProvider.close();
			scenarioDataProvider = null;
		}

		if (modelReference != null) {
			modelReference.getLock().removeLockListener(lockListener);
			modelReference.close();
			modelReference = null;
		}
		modelRecord = null;

		scenarioInstance = null;
	}

	protected void doDisplayScenarioInstance(@Nullable ScenarioInstance scenarioInstance, @Nullable MMXRootObject rootObject) {

	}

	@Override
	public boolean isLocked() {
		return locked;
	}

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
		if (modelReference == null) {
			return null;
		}

		return modelReference.getEditingDomain();
	}

	@Override
	public ModelReference getModelReference() {
		return modelReference;
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

		if (modelReference == null) {
			return null;
		}
		return (MMXRootObject) modelReference.getInstance();
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
		return modelReference.getLock();
	}

	@Override
	public IStatusProvider getStatusProvider() {
		return scenarioInstanceStatusProvider;
	}

	@Override
	public void openStatus(final IStatus status) {
		// Do nothing by default
	}

	public void editObject(@Nullable EObject target) {
		if (target != null) {
			final DetailCompositeDialog dcd = new DetailCompositeDialog(this.getShell(), this.getDefaultCommandHandler());
			dcd.open(this, this.getRootObject(), Collections.singletonList((EObject) target));
		}
	}
}

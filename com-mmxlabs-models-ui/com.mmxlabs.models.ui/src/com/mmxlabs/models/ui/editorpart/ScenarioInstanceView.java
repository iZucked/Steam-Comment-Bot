package com.mmxlabs.models.ui.editorpart;

import java.io.IOException;
import java.util.Stack;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
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
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.models.ui.valueproviders.ReferenceValueProviderCache;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

public abstract class ScenarioInstanceView extends ViewPart implements IScenarioEditingLocation, ISelectionListener, IScenarioInstanceProvider, IMMXRootObjectProvider {
	private static final String SCENARIO_NAVIGATOR_ID = "com.mmxlabs.scenario.service.ui.navigator";
	private ScenarioInstance scenarioInstance;
	private ReferenceValueProviderCache valueProviderCache;
	private boolean locked;

	private EContentAdapter lockedAdapter = new EContentAdapter() {

		@Override
		public void notifyChanged(final Notification notification) {
			super.notifyChanged(notification);

			if (notification.getFeature() == ScenarioServicePackage.eINSTANCE.getScenarioInstance_Locked()) {
				Display.getDefault().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						setLocked(notification.getNewBooleanValue());
					}
				});
			}
		}

	};

	protected void listenToScenarioSelection() {
		getSite().getPage().addSelectionListener(SCENARIO_NAVIGATOR_ID, this);
		selectionChanged(null, getSite().getPage().getSelection(SCENARIO_NAVIGATOR_ID));
	}

	@Override
	public void dispose() {

		if (scenarioInstance != null) {
			scenarioInstance.eAdapters().remove(lockedAdapter);
		}

		getSite().getPage().removeSelectionListener(SCENARIO_NAVIGATOR_ID, this);
		super.dispose();
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (scenarioInstance != null) {
			scenarioInstance.eAdapters().remove(lockedAdapter);
		}
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection structured = (IStructuredSelection) selection;
			if (structured.size() == 1) {
				if (structured.getFirstElement() instanceof ScenarioInstance) {
					ScenarioInstance instance = (ScenarioInstance) structured.getFirstElement();
					displayScenarioInstance(instance);
					scenarioInstance.eAdapters().add(lockedAdapter);
					setLocked(instance.isLocked());
					return;
				}
			}
		}
		displayScenarioInstance(null);
	}

	protected void displayScenarioInstance(final ScenarioInstance instance) {
		extraValidationContext.clear();
		this.scenarioInstance = instance;
		if (instance != null) {
			getRootObject();
			this.valueProviderCache = new ReferenceValueProviderCache(getRootObject());
			extraValidationContext.push(new DefaultExtraValidationContext(getRootObject()));
		} else {
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

	}

	@Override
	public ScenarioInstance getScenarioInstance() {
		return scenarioInstance;
	}

	@Override
	public Shell getShell() {
		return getSite().getShell();
	}
}

package com.mmxlabs.scenario.service.ui.navigator;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.MMXSubModel;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.ValidationContentAdapter;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.IScenarioServiceListener;
import com.mmxlabs.scenario.service.impl.ScenarioServiceListener;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.internal.Activator;

/**
 * TODO: Handle new services being added.
 * TODO: Common navigator only updates with a single column!
 * TODO: Common navigator does not update until refreshed.
 * 
 * @author Simon Goodall
 * 
 */
public class ValidatingDecorator extends LabelProvider implements ILightweightLabelDecorator {

	private final Map<MMXRootObject, ValidationContentAdapter> listenerRefs = new WeakHashMap<MMXRootObject, ValidationContentAdapter>();

	private final IScenarioServiceListener listener = new ScenarioServiceListener() {

		@Override
		public void onPostScenarioInstanceLoad(final IScenarioService scenarioService, final ScenarioInstance scenarioInstance) {

			final MMXRootObject rootObject = (MMXRootObject) scenarioInstance.getInstance();
			if (rootObject == null) {
				return;
			}

			final DefaultExtraValidationContext extraValidationContext = new DefaultExtraValidationContext(rootObject);
			final ValidationContentAdapter validationAdapter = new ValidationContentAdapter(extraValidationContext) {

				@Override
				public void validationStatus(final IStatus status) {
					scenarioInstance.getAdapters().put(IStatus.class, status);

					final LabelProviderChangedEvent event = new LabelProviderChangedEvent(ValidatingDecorator.this, scenarioInstance);
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							fireLabelProviderChanged(event);
						}
					});
				}
			};

			// Add adapter to new input
			for (final MMXSubModel sub : rootObject.getSubModels()) {
				sub.getSubModelInstance().eAdapters().add(validationAdapter);
			}
			validationAdapter.performValidation();

			listenerRefs.put(rootObject, validationAdapter);
		}
	};

	public ValidatingDecorator() {
		for (final Map.Entry<String, WeakReference<IScenarioService>> entry : Activator.getDefault().getScenarioServices().entrySet()) {
			final IScenarioService service = entry.getValue().get();
			if (service != null) {
				service.addScenarioServiceListener(listener);
			}
		}
	}

	@Override
	public void dispose() {
		for (final Map.Entry<String, WeakReference<IScenarioService>> entry : Activator.getDefault().getScenarioServices().entrySet()) {
			final IScenarioService service = entry.getValue().get();
			if (service != null) {
				service.removeScenarioServiceListener(listener);
			}
		}

		for (final Map.Entry<MMXRootObject, ValidationContentAdapter> entry : listenerRefs.entrySet()) {
			// Remove adapter to new input
			for (final MMXSubModel sub : entry.getKey().getSubModels()) {
				sub.getSubModelInstance().eAdapters().remove(entry.getValue());
			}
		}

	}

	@Override
	public void decorate(final Object element, final IDecoration decoration) {

		if (element instanceof ScenarioInstance) {
			final ScenarioInstance scenarioInstance = (ScenarioInstance) element;
			final Map<Class<?>, Object> adapters = scenarioInstance.getAdapters();
			if (adapters != null) {
				final IStatus validationStatus = (IStatus) adapters.get(IStatus.class);
				if (validationStatus != null) {
					if (validationStatus.getSeverity() == IStatus.ERROR) {
						decoration.addOverlay(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/overlays/error.gif"), IDecoration.BOTTOM_RIGHT);
					} else if (validationStatus.getSeverity() == IStatus.WARNING) {
						decoration.addOverlay(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/overlays/warning.gif"), IDecoration.BOTTOM_RIGHT);
					}
					// else if (validationStatus.getSeverity() == IStatus.INFO) {
					// images.add(getResourceLocator().getImage("overlays/info.gif"));
					// }
				}
			}
		}
	}

}

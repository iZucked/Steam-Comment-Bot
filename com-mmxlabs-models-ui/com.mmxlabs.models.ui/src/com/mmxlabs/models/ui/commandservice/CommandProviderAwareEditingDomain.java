package com.mmxlabs.models.ui.commandservice;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.mmxcore.IMMXAdapter;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.MMXSubModel;

/**
 * EditingDomain implementation which is aware of {@link IModelCommandProvider}s and provides a mechanism to enable or disable their execution.
 * 
 */
public class CommandProviderAwareEditingDomain extends AdapterFactoryEditingDomain {
	private static final Logger log = LoggerFactory.getLogger(CommandProviderAwareEditingDomain.class);
	private final MMXRootObject rootObject;
	private final ServiceTracker<IModelCommandProvider, IModelCommandProvider> commandProviderTracker;

	private boolean commandProvidersDisabled = false;

	public CommandProviderAwareEditingDomain(final AdapterFactory adapterFactory, final CommandStack commandStack, final MMXRootObject rootObject,
			final ServiceTracker<IModelCommandProvider, IModelCommandProvider> commandProviderTracker, final ResourceSet resourceSet) {
		super(adapterFactory, commandStack, resourceSet);
		this.rootObject = rootObject;
		this.commandProviderTracker = commandProviderTracker;
	}

	public void setAdaptersEnabled(final boolean enabled) {
		for (final MMXSubModel subModel : rootObject.getSubModels()) {
			if (enabled)
				enableAdapters(subModel.getSubModelInstance());
			else
				disableAdapters(subModel.getSubModelInstance());
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

	@Override
	public Command createCommand(final Class<? extends Command> commandClass, final CommandParameter commandParameter) {
		final Command normal = super.createCommand(commandClass, commandParameter);

		if (!isCommandProvidersDisabled()) {
			final CompoundCommand wrapper = new CompoundCommand();
			wrapper.append(normal);
			final IModelCommandProvider[] providers = commandProviderTracker.getServices(new IModelCommandProvider[0]);
			for (final IModelCommandProvider provider : providers)
				provider.startCommandProvision();

			for (final IModelCommandProvider provider : providers) {
				final Command addition = provider.provideAdditionalCommand(this, (MMXRootObject) rootObject, commandClass, commandParameter, normal);
				if (addition != null) {
					log.debug(provider.getClass().getName() + " provided " + addition + " to " + normal);
					if (addition.canExecute() == false) {
						log.warn("Provided command was not executable, ignoring it");
					} else {
						wrapper.append(addition);
					}
				}
			}

			for (final IModelCommandProvider provider : providers)
				provider.endCommandProvision();

			return wrapper.unwrap();
		} else {
			return normal;
		}
	}

	public boolean isCommandProvidersDisabled() {
		return commandProvidersDisabled;
	}

	public void setCommandProvidersDisabled(final boolean commandProvidersDisabled) {
		this.commandProvidersDisabled = commandProvidersDisabled;
	}

}
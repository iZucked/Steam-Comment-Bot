package com.mmxlabs.models.ui.commandservice;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * EditingDomain implementation which is aware of {@link IModelCommandProvider}s and provides a mechanism to enable or disable their execution.
 * 
 */
public class CommandProviderAwareEditingDomain extends AdapterFactoryEditingDomain {
	private static final Logger log = LoggerFactory.getLogger(CommandProviderAwareEditingDomain.class);
	private final EObject rootObject;
	private final ServiceTracker<IModelCommandProvider, IModelCommandProvider> commandProviderTracker;

	private boolean commandProvidersDisabled = false;

	public CommandProviderAwareEditingDomain(final AdapterFactory adapterFactory, final CommandStack commandStack, final EObject rootObject,
			final ServiceTracker<IModelCommandProvider, IModelCommandProvider> commandProviderTracker) {
		super(adapterFactory, commandStack);
		this.rootObject = rootObject;
		this.commandProviderTracker = commandProviderTracker;
	}

	@Override
	public Command createCommand(final Class<? extends Command> commandClass, final CommandParameter commandParameter) {
		final Command normal = super.createCommand(commandClass, commandParameter);

		if (!isCommandProvidersDisabled()) {
			final CompoundCommand wrapper = new CompoundCommand();
			wrapper.append(normal);
			IModelCommandProvider[] providers = commandProviderTracker.getServices(new IModelCommandProvider[0]);
			for (final IModelCommandProvider provider : providers) provider.startCommandProvision();
			
			for (final IModelCommandProvider provider : providers){
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
			
			for (final IModelCommandProvider provider : providers) provider.endCommandProvision();
			
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
package com.mmxlabs.models.ui.commandservice;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.osgi.util.tracker.ServiceTracker;

import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * EditingDomain implementation which is aware of {@link IModelCommandProvider}s and provides a mechanism to enable or disable their execution.
 * 
 */
public class CommandProviderAwareEditingDomain extends AdapterFactoryEditingDomain {
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
			for (final IModelCommandProvider provider : commandProviderTracker.getServices(new IModelCommandProvider[0])) {
				final Command addition = provider.provideAdditionalCommand(this, (MMXRootObject) rootObject, commandClass, commandParameter, normal);
				if (addition != null)
					wrapper.append(addition);
			}

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
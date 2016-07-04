/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.common.commandservice;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.common.internal.Activator;
import com.mmxlabs.models.mmxcore.IMMXAdapter;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * EditingDomain implementation which is aware of {@link IModelCommandProvider}s and provides a mechanism to enable or disable their execution.
 * 
 */
public class CommandProviderAwareEditingDomain extends AdapterFactoryEditingDomain {
	private static final Logger log = LoggerFactory.getLogger(CommandProviderAwareEditingDomain.class);
	private final MMXRootObject rootObject;

	private boolean commandProvidersDisabled = false;

	private boolean enabled = true;

	/**
	 */
	public CommandProviderAwareEditingDomain(final AdapterFactory adapterFactory, final CommandStack commandStack, final MMXRootObject rootObject, final ResourceSet resourceSet) {
		super(adapterFactory, commandStack, resourceSet);
		this.rootObject = rootObject;
	}

	public void setAdaptersEnabled(final boolean enabled) {

		this.enabled = enabled;

		if (enabled) {
			enableAdapters(rootObject);
		} else {
			disableAdapters(rootObject);
		}
	}

	public void setAdaptersEnabled(final boolean enabled, final boolean skip) {
		this.enabled = enabled;
		if (enabled) {
			enableAdapters(rootObject, skip);
		} else {
			disableAdapters(rootObject);
		}
	}

	private void disableAdapters(final EObject top) {

		for (final Adapter a : top.eAdapters().toArray(new Adapter[top.eAdapters().size()])) {
			if (a instanceof IMMXAdapter) {
				((IMMXAdapter) a).disable();
			}
		}
		for (final EObject o : top.eContents()) {
			disableAdapters(o);
		}
	}

	private void enableAdapters(final EObject top) {

		for (final Adapter a : top.eAdapters().toArray(new Adapter[top.eAdapters().size()])) {
			if (a instanceof IMMXAdapter) {
				((IMMXAdapter) a).enable();
			}
		}
		for (final EObject o : top.eContents()) {
			enableAdapters(o);
		}
	}

	private void enableAdapters(final EObject top, final boolean skip) {
		for (final Adapter a : top.eAdapters().toArray(new Adapter[top.eAdapters().size()])) {
			if (a instanceof IMMXAdapter) {
				((IMMXAdapter) a).enable(skip);
			}
		}
		for (final EObject o : top.eContents()) {
			enableAdapters(o, skip);
		}
	}

	/**
	 * Notify command providers that a new set of related command is about to be executed. Normally this is invoked as part of a command creation call along with a matching call to
	 * {@link #endBatchCommand()}. External clients can manually invoked this pair of methods when multiple related commands are being created. The {@link BaseModelCommandProvider} keeps track of the
	 * depth of these calls and will clear caches when the depth returns to zero. Clients can call this pair of method to avoid early cache clearing.
	 * 
	 * 
	 */
	public void startBatchCommand() {
		final List<IModelCommandProvider> providers = Activator.getPlugin().getModelCommandProviders();
		for (final IModelCommandProvider provider : providers) {
			provider.startCommandProvision();
		}
	}

	/**
	 * To be called after command creation as part of a pair of calls with {@link #startBatchCommand()}
	 * 
	 */
	public void endBatchCommand() {
		final List<IModelCommandProvider> providers = Activator.getPlugin().getModelCommandProviders();
		for (final IModelCommandProvider provider : providers) {
			provider.endCommandProvision();
		}
	}

	@Override
	public Command createCommand(final Class<? extends Command> commandClass, final CommandParameter commandParameter) {
		final Command normal = super.createCommand(commandClass, commandParameter);

		if (!isCommandProvidersDisabled()) {
			final CompoundCommand wrapper = new CompoundCommand();

			final List<IModelCommandProvider> providers = Activator.getPlugin().getModelCommandProviders();
			
			// TODO: Maybe separate API here?
			startBatchCommand();

			for (final IModelCommandProvider provider : providers) {
				final Command addition = provider.provideAdditionalBeforeCommand(this, rootObject, overrides, editSet, commandClass, commandParameter, normal);
				if (addition != null) {
					log.debug(provider.getClass().getName() + " provided " + addition + " to " + normal);
					if (addition.canExecute() == false) {
						log.error("Provided command was not executable, ignoring it", new RuntimeException());
					} else {
						wrapper.append(addition);
					}
				}
			}

			endBatchCommand();

			wrapper.append(normal);

			startBatchCommand();

			for (final IModelCommandProvider provider : providers) {
				final Command addition = provider.provideAdditionalAfterCommand(this, rootObject, overrides, editSet, commandClass, commandParameter, normal);
				if (addition != null) {
					log.debug(provider.getClass().getName() + " provided " + addition + " to " + normal);
					if (addition.canExecute() == false) {
						log.error("Provided command was not executable, ignoring it", new RuntimeException());
					} else {
						wrapper.append(addition);
					}
				}
			}

			endBatchCommand();

			return wrapper.unwrap();
		} else {
			return normal;
		}
	}

	private final WeakHashMap<EObject, EObject> overrides = new WeakHashMap<EObject, EObject>();
	/**
	 * The {@link Set} of objects currently being edited.
	 */
	private final Set<EObject> editSet = new HashSet<EObject>();

	public void setOverride(final EObject real, final EObject override) {
		overrides.put(real, override);
	}

	public void clearOverride(final EObject real) {
		overrides.remove(real);
	}

	/**
	 * Add an {@link EObject} to the current {@link Set} of edited objects.
	 * 
	 * @param object
	 */
	public void addEditObject(final EObject object) {
		editSet.add(object);
	}

	/**
	 * Remove and {@link EObject} from the current {@link Set} of edited objects.
	 * 
	 * @param object
	 */
	public void removeEditObject(final EObject object) {
		editSet.remove(object);
	}

	public boolean isCommandProvidersDisabled() {
		return commandProvidersDisabled;
	}

	public void setCommandProvidersDisabled(final boolean commandProvidersDisabled) {
		this.commandProvidersDisabled = commandProvidersDisabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

}
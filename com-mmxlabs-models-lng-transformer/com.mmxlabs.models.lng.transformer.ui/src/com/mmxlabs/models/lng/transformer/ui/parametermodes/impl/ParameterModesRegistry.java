/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.parametermodes.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModeCustomiser;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModeExtender;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModesRegistry;

/**
 * An implementation of {@link IParameterModesRegistry} populated by extensions points.
 * 
 * @author Simon Goodall
 * 
 */
public class ParameterModesRegistry implements IParameterModesRegistry {

	@NonNull
	private final List<@NonNull IParameterModeCustomiser> customisers = new LinkedList<>();

	@NonNull
	private final List<@NonNull IParameterModeExtender> extenders = new LinkedList<>();

	@Inject
	public void init(@NonNull final Iterable<ParameterModeExtension> parameterModeExtensions, @NonNull final Iterable<ParameterModeExtenderExtension> parameterModeExtenderExtensions) {
		// Something needs to bind the parameters types to trigger the watchers in ParameterModesExtensionModule, and as we are the class that needs the watchers, we trigger the bind.
		// The watcher will trigger all the items in the list now and any future items also, so we do not need to process the iterables here.
	}

	/**
	 * Register an {@link ParameterModeExtension} with this registry
	 * 
	 * @param unit
	 */
	public void registerParameterMode(@NonNull final ParameterModeExtension ext) {
		final ParameterModeCustomiserProxy proxy = new ParameterModeCustomiserProxy(ext);
		registerParameterModeCustomiser(proxy);
	}

	/**
	 * Register an {@link ParameterModeExtenderExtension} with this registry
	 * 
	 * @param unit
	 */
	public void registerExtender(@NonNull final ParameterModeExtenderExtension ext) {
		final ParameterModeExtenderProxy proxy = new ParameterModeExtenderProxy(ext);
		registerExtender(proxy);
	}

	/**
	 * Register an {@link IParameterModeCustomiser} with this registry
	 * 
	 * @param unit
	 */
	public void registerParameterModeCustomiser(@NonNull final IParameterModeCustomiser customiser) {
		customisers.add(customiser);
	}

	/**
	 * Register an {@link IPModeCustomiser} with this registry
	 * 
	 * @param unit
	 */
	public void registerExtender(@NonNull final IParameterModeExtender extender) {
		extenders.add(extender);
	}

	@Override
	@NonNull
	public Collection<@NonNull IParameterModeCustomiser> getCustomisers() {
		return customisers;
	}

	@Override
	@NonNull
	public Collection<@NonNull IParameterModeExtender> getExtenders() {
		return extenders;
	}
}

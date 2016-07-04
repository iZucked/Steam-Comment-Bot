/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.parametermodes.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
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

	private static final Logger log = LoggerFactory.getLogger(ParameterModesRegistry.class);

	@NonNull
	private final Map<String, IParameterModeCustomiser> parameterModes = new HashMap<String, IParameterModeCustomiser>();

	@NonNull
	private final List<IParameterModeExtender> extenders = new LinkedList<IParameterModeExtender>();

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
		final String name = ext.getName();
		if (name != null) {
			registerParameterMode(name, proxy);
		}
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
	public void registerParameterMode(@NonNull final String name, @NonNull final IParameterModeCustomiser customiser) {
		if (parameterModes.containsKey(name)) {
			log.error(String.format("A parameter mode with name %s has already been registered", name), new RuntimeException());
		} else {
			parameterModes.put(name, customiser);
		}
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
	public Collection<String> getParameterModes() {

		return parameterModes.keySet();
	}

	@Override
	@Nullable
	public IParameterModeCustomiser getCustomiser(final String name) {
		return parameterModes.get(name);
	}

	@Override
	@NonNull
	public Collection<IParameterModeExtender> getExtenders() {
		return extenders;
	}
}

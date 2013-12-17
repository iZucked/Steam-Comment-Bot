/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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

	private final Map<String, IParameterModeCustomiser> parameterModes = new HashMap<String, IParameterModeCustomiser>();
	private final List<IParameterModeExtender> extenders = new LinkedList<IParameterModeExtender>();

	@Inject
	Iterable<ParameterModeExtension> parameterModeExtensions;

	@Inject
	public void init(final Iterable<ParameterModeExtension> parameterModeExtensions, final Iterable<ParameterModeExtenderExtension> parameterModeExtenderExtensions) {

		for (final ParameterModeExtension ext : parameterModeExtensions) {
			if (ext != null) {
				final ParameterModeCustomiserProxy proxy = new ParameterModeCustomiserProxy(ext);
				final String name = ext.getName();
				if (name != null) {
					registerParameterMode(name, proxy);
				}

			}
		}

		for (final ParameterModeExtenderExtension ext : parameterModeExtenderExtensions) {
			if (ext != null) {
				final ParameterModeExtenderProxy proxy = new ParameterModeExtenderProxy(ext);
				registerExtender(proxy);
			}
		}
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
	public Collection<IParameterModeExtender> getExtenders() {
		return extenders;
	}

}

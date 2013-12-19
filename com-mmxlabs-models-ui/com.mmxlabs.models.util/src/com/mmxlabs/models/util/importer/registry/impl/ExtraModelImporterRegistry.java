/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.registry.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import com.mmxlabs.models.util.importer.IExtraModelImporter;
import com.mmxlabs.models.util.importer.registry.IExtraModelImporterExtension;

/**
 * @since 8.0
 */
public class ExtraModelImporterRegistry {

	@Inject
	private Iterable<IExtraModelImporterExtension> extensions;

	public Collection<IExtraModelImporter> getExtraModelImporters() {
		final List<IExtraModelImporter> l = new ArrayList<>();
		for (final IExtraModelImporterExtension extension : extensions) {
			l.add(extension.createInstance());
		}

		return l;
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.registry.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import com.mmxlabs.models.util.importer.IPostModelImporter;
import com.mmxlabs.models.util.importer.registry.IPostModelImporterExtension;

/**
 */
public class PostModelImporterRegistry {

	@Inject
	private Iterable<IPostModelImporterExtension> extensions;

	public Collection<IPostModelImporter> getPostModelImporters() {
		final List<IPostModelImporter> l = new ArrayList<IPostModelImporter>();
		for (final IPostModelImporterExtension extension : extensions) {
			l.add(extension.createInstance());
		}

		return l;
	}
}

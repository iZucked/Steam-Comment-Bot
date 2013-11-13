/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.registry.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import com.mmxlabs.models.util.importer.IPostModelImporter;
import com.mmxlabs.models.util.importer.registry.PostModelImporterExtension;

public class PostModelImporterRegistry {

	@Inject
	private Iterable<PostModelImporterExtension> extensions;

	public Collection<IPostModelImporter> getPostModelImporters() {
		final List<IPostModelImporter> l = new ArrayList<IPostModelImporter>();
		for (final PostModelImporterExtension extension : extensions) {
			l.add(extension.createInstance());
		}

		return l;
	}
}

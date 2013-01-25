/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.ui.registries.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.MMXSubModel;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.editorpart.IJointModelEditorContribution;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;
import com.mmxlabs.models.ui.extensions.IJointModelEditorExtension;
import com.mmxlabs.models.ui.registries.IJointModelEditorContributionRegistry;

public class JointModelEditorContributionRegistry implements IJointModelEditorContributionRegistry {
	private static final Logger log = LoggerFactory.getLogger(JointModelEditorContributionRegistry.class);
	@Inject
	Iterable<IJointModelEditorExtension> extensions;

	@Override
	public List<IJointModelEditorContribution> initEditorContributions(final JointModelEditorPart part, final MMXRootObject root) {
		final List<Pair<IJointModelEditorExtension, UUIDObject>> matches = new ArrayList<Pair<IJointModelEditorExtension, UUIDObject>>(root.getSubModels().size());

		final HashMap<String, IJointModelEditorExtension> extensionsByModel = new HashMap<String, IJointModelEditorExtension>();
		for (final IJointModelEditorExtension ex : extensions) {
			extensionsByModel.put(ex.getSubModelClassName(), ex);
		}

		for (final MMXSubModel subModel : root.getSubModels()) {
			final IJointModelEditorExtension ex = extensionsByModel.get(subModel.getSubModelInstance().eClass().getInstanceClass().getCanonicalName());
			if (ex != null) {
				matches.add(new Pair<IJointModelEditorExtension, UUIDObject>(ex, subModel.getSubModelInstance()));
			}
		}

		Collections.sort(matches, new Comparator<Pair<IJointModelEditorExtension, UUIDObject>>() {

			@Override
			public int compare(Pair<IJointModelEditorExtension, UUIDObject> o1, Pair<IJointModelEditorExtension, UUIDObject> o2) {

				// Peaberry bug: Prority cannot be converted into an int directly;
				// @see http://code.google.com/p/peaberry/issues/detail?id=74
				int p1 = 100;
				try {
					p1 = Integer.parseInt(o1.getFirst().getPriority());
				} catch (NumberFormatException nfe) {
					// Ignore
				}
				int p2 = 100;
				try {
					p2 = Integer.parseInt(o2.getFirst().getPriority());
				} catch (NumberFormatException nfe) {
					// Ignore
				}

				return p1 - p2;
			}
		});

		final ArrayList<IJointModelEditorContribution> result = new ArrayList<IJointModelEditorContribution>(matches.size());
		for (final Pair<IJointModelEditorExtension, UUIDObject> extension : matches) {
			final IJointModelEditorContribution contribution = extension.getFirst().createInstance();
			result.add(contribution);
			contribution.init(part, root, extension.getSecond());
		}
		return result;
	}

}

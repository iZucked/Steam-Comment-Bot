/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.impl;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.csv.IDeferment;
import com.mmxlabs.common.csv.IImportContext;
import com.mmxlabs.common.csv.IImportProblem;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.util.importer.IMMXImportContext;

public class SetReference implements IDeferment {
	private final String name;
	private final EReference reference;
	private final EObject container;
	private final EClass linkType;
	private final IImportProblem problem;

	public SetReference(@NonNull final EObject container, @NonNull final EReference reference, @NonNull final String nameOrNames, @NonNull final IMMXImportContext context) {
		this(container, reference, reference.getEReferenceType(), nameOrNames, context);
	}

	public SetReference(@NonNull final EObject container, @NonNull final EReference reference, @NonNull final EClass eReferenceLinkType, @NonNull final String nameOrNames,
			@NonNull final IMMXImportContext context) {
		this.container = container;
		this.reference = reference;
		this.name = nameOrNames;
		this.linkType = eReferenceLinkType;
		this.problem = context.createProblem("Could not resolve " + linkType.getName() + " with name(s) " + nameOrNames, true, true, true);
	}

	@Override
	public void run(@NonNull final IImportContext iContext) {
		if (iContext instanceof IMMXImportContext) {
			final IMMXImportContext context = (IMMXImportContext) iContext;
			if (reference.isMany()) {

				final String[] names = name.split(",");
				for (final String rawName : names) {

					// Simple (de)encoding - should expand into something more robust
					final String name = EncoderUtil.decode(rawName);
					assert name != null;

					final NamedObject no = context.getNamedObject(name.trim(), linkType);
					if (no != null) {
						((EList) (container.eGet(reference))).add(no);
					} else {
						context.addProblem(problem);
					}
				}
			} else {
				final NamedObject no = context.getNamedObject(name.trim(), linkType);
				if (no != null)
					container.eSet(reference, no);
				else {
					context.addProblem(problem);
				}
			}
		}
	}

	@Override
	public int getStage() {
		return IMMXImportContext.STAGE_RESOLVE_REFERENCES;
	}
}

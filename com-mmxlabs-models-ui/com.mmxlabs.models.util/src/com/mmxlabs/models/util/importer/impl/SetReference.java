/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.impl;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.IImportContext.IDeferment;
import com.mmxlabs.models.util.importer.IImportContext.IImportProblem;

public class SetReference implements IDeferment {
	private String name;
	private EReference reference;
	private EObject container;
	private EClass linkType;
	private IImportProblem problem;
	public SetReference(final EObject container, final EReference reference, final String nameOrNames, final IImportContext context) {
		this(container, reference, reference.getEReferenceType(), nameOrNames, context);
	}
	public SetReference(EObject container, EReference reference,
			EClass eReferenceLinkType, String nameOrNames, final IImportContext context) {
		this.container = container;
		this.reference = reference;
		this.name = nameOrNames;
		this.linkType = eReferenceLinkType;
		this.problem = context.createProblem("Could not resolve " + linkType.getName() + " with name(s) " + nameOrNames, true, true, true);
	}
	@Override
	public void run(final IImportContext context) {
		if (reference.isMany()) {
			final String[] names = name.split(",");
			for (final String name : names) {
				final NamedObject no = context.getNamedObject(name.trim(), linkType);
				if (no != null) {
					((EList) (container.eGet(reference))).add(no);
				} else {
					context.addProblem(problem);
				}
			}
		} else {
			final NamedObject no = context.getNamedObject(name.trim(), linkType);
			if (no != null) container.eSet(reference, no);
			else {
				context.addProblem(problem);
			}
		}
	}
	@Override
	public int getStage() {
		return IImportContext.STAGE_RESOLVE_REFERENCES;
	}
}

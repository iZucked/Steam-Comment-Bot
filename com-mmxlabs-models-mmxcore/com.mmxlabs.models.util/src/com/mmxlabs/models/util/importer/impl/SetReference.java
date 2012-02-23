package com.mmxlabs.models.util.importer.impl;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.IImportContext.IDeferment;

public class SetReference implements IDeferment {
	private String name;
	private EReference reference;
	private EObject container;
	private EClass linkType;
	public SetReference(final EObject container, final EReference reference, final String nameOrNames) {
		this(container, reference, reference.getEReferenceType(), nameOrNames);
	}
	public SetReference(EObject container, EReference reference,
			EClass eReferenceLinkType, String nameOrNames) {
		this.container = container;
		this.reference = reference;
		this.name = nameOrNames;
		this.linkType = eReferenceLinkType;
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
					context.addProblem("Could not resolve " + linkType.eClass() + " named " + name, false, false);
				}
			}
		} else {
			final NamedObject no = context.getNamedObject(name.trim(), linkType);
			if (no != null) container.eSet(reference, no);
			else {
				context.addProblem("Could not resolve " + linkType.eClass() + " named " + name, false, false);
			}
		}
	}
	@Override
	public int getStage() {
		return 0;
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.impl;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.util.importer.IImportContext;
import com.mmxlabs.models.util.importer.IImportContext.IDeferment;

public class Join implements IDeferment {
	final String nameOne;
	final String nameTwo;
	final EClass typeOne;
	final EClass typeTwo;
	final EReference referenceOne;

	boolean secondAttempt = false;
	
	public Join(String nameOne, String nameTwo, EClass typeOne, EClass typeTwo,
			EReference referenceOne) {
		super();
		this.nameOne = nameOne;
		this.nameTwo = nameTwo;
		this.typeOne = typeOne;
		this.typeTwo = typeTwo;
		this.referenceOne = referenceOne;

	}

	@Override
	public void run(IImportContext context) {
		final NamedObject no1 = context.getNamedObject(nameOne, typeOne);
		final NamedObject no2 = context.getNamedObject(nameOne, typeTwo);
		if (no1 == null || no2 == null) {
			if (secondAttempt) {
				// TODO warn
			} else {
				secondAttempt = true;
				context.doLater(this);
			}
		} else {
			if (referenceOne.isMany()) {
				final EList list = (EList) no1.eGet(referenceOne);
				list.add(no2);
			} else {
				no1.eSet(referenceOne, no2);
			}
		}
	}

	@Override
	public int getStage() {
		return secondAttempt ? IImportContext.STAGE_RESOLVE_CROSSREFERENCES : IImportContext.STAGE_RESOLVE_REFERENCES;
	}

}

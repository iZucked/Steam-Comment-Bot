/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.ecore;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;

import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.UUIDObject;

public final class EMFCopier {

	private EMFCopier() {

	}

	/**
	 * This method calls {@link EcoreUtil#copy(EObject)} but then resets any uuid
	 * fields from cloned {@link UUIDObject}s to avoid potential clashes later
	 * 
	 * @param <T>
	 * @param source
	 * @return
	 */
	public static <T extends EObject> T copy(final T source) {

		final T copy = EcoreUtil.copy(source);
		resetUUID(copy);
		copy.eAllContents().forEachRemaining(EMFCopier::resetUUID);
		return copy;
	}

	/**
	 * Returns a collection of the self-contained copies of each {@link EObject} in
	 * eObjects.
	 * 
	 * @param eObjects the collection of objects to copy.
	 * @return the collection of copies.
	 * @see Copier
	 */
	public static <T extends EObject> Collection<T> copyAll(final Collection<? extends T> eObjects) {
		final Collection<T> result = EcoreUtil.copyAll(eObjects);
		result.forEach(copy -> {
			resetUUID(copy);
			copy.eAllContents().forEachRemaining(EMFCopier::resetUUID);
		});
		return result;
	}

	public static void resetUUID(final EObject o) {
		if (o instanceof UUIDObject) {
			o.eSet(MMXCorePackage.Literals.UUID_OBJECT__UUID, EcoreUtil.generateUUID());
		}
	}
}

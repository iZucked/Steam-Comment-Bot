/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.modelfactories;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.mmxcore.MMXRootObject;

public interface IModelFactory {
	/**
	 * After being instantiated from an extension point this method will be called
	 * 
	 * @param ID
	 *            the extension point ID
	 * @param outputEClassName
	 *            the name of the output EClass
	 * @param label
	 *            a nice label for this in the UI
	 * @param replacementEClass
	 * @param replacementEReference
	 */
	public void initFromExtension(final String ID, final String label, final String prototype, String replacementEReference, String replacementEClass);

	/**
	 * Get the display label for this factory.
	 * 
	 * @return
	 */
	public String getLabel();

	/**
	 * Add an instance to the scenario, and return the main part of the instance (this can add other objects as well, if it wants)
	 * 
	 * @param rootObject
	 *            the root object
	 * @param container
	 *            the object where the output should end up being contained
	 * @param containment
	 *            the reference in which the output should be contained
	 * @param iSelection
	 * 
	 * @return null if the operation did not happen, or the main part of whatever has been created (for example the cargo if a cargo has been created with some slots).
	 */
	public Collection<? extends ISetting> createInstance(final MMXRootObject rootObject, final EObject container, final EReference containment, @Nullable Collection<@NonNull EObject> selection);

	public interface ISetting {
		public EObject getInstance();

		public EObject getContainer();

		public EReference getContainment();

		public @Nullable Collection<@NonNull EObject> getSelection();
	}
}

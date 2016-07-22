/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.modelfactories;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.modelfactories.DefaultModelFactory;
import com.mmxlabs.models.ui.modelfactories.IModelFactory;

/**
 * {@link IModelFactory} implementation to create {@link PortGroup} instances. This differs from the {@link DefaultModelFactory} in that it processes the {@link ISelection} object for the initial set
 * of {@link Port}s.
 * 
 */
public class PortGroupFactory extends DefaultModelFactory {

	public PortGroupFactory() {
		super();
	}

	@Override
	public Collection<ISetting> createInstance(final MMXRootObject rootObject, final EObject container, final EReference containment, @Nullable Collection<@NonNull EObject> selection) {
		return super.createInstance(rootObject, container, PortPackage.eINSTANCE.getPortModel_PortGroups(), selection);
	}

	/**
	 * Process the selection and generate a list of objects. Search for a many valued reference in the instance and add all objects to it.
	 * 
	 * @param cls
	 * @param instance
	 * @param selection
	 */
	protected void addSelectionToInstance(final EClass cls, final EObject instance, final ISelection selection) {
		if (selection == null || selection.isEmpty()) {
			return;
		}
		if (instance instanceof PortGroup) {
			final PortGroup portGroup = (PortGroup) instance;

			if (selection instanceof IStructuredSelection) {

				final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
				final Iterator<?> itr = structuredSelection.iterator();
				while (itr.hasNext()) {
					final Object obj = itr.next();
					if (obj instanceof Port) {
						portGroup.getContents().add((Port) obj);
					}
				}
			}
		}
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.modelfactories;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoGroup;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.modelfactories.DefaultModelFactory;
import com.mmxlabs.models.ui.modelfactories.IModelFactory;

/**
 * {@link IModelFactory} implementation to create {@link CargoGroup} instances. This differs from the {@link DefaultModelFactory} in that it processes the {@link ISelection} object for the initial set
 * of cargoes.
 * 
 */
public class CargoGroupFactory extends DefaultModelFactory {

	public CargoGroupFactory() {
		super();
	}

	@Override
	public Collection<ISetting> createInstance(MMXRootObject rootObject, EObject container, EReference containment, ISelection selection) {
		return super.createInstance(rootObject, container, CargoPackage.eINSTANCE.getCargoModel_CargoGroups(), selection);
	}
	
	/**
	 * Process the selection and generate a list of objects. Search for a many valued reference in the instance and add all objects to it.
	 * 
	 * @param cls
	 * @param instance
	 * @param selection
	 */
	protected void addSelectionToInstance(EClass cls, EObject instance, ISelection selection) {
		if (selection == null || selection.isEmpty()) {
			return;
		}
		if (instance instanceof CargoGroup) {
			CargoGroup cargoGroup = (CargoGroup) instance;

			if (selection instanceof IStructuredSelection) {

				IStructuredSelection structuredSelection = (IStructuredSelection) selection;
				Iterator<?> itr = structuredSelection.iterator();
				while (itr.hasNext()) {
					Object obj = itr.next();
					if (obj instanceof Cargo) {
						cargoGroup.getCargoes().add((Cargo) obj);
					}
				}
			}
		}
	}
}

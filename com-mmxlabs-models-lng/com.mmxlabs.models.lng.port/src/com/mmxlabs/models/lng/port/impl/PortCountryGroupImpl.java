/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.port.impl;

import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortCountryGroup;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.impl.APortSetImpl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Country Group</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class PortCountryGroupImpl extends APortSetImpl<Port> implements PortCountryGroup {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PortCountryGroupImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PortPackage.Literals.PORT_COUNTRY_GROUP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public EList<Port> collect(EList<APortSet<Port>> marked) {
		if (marked.contains(this)) {
			return ECollections.emptyEList();
		}
		final String countryName = getName();
		if (countryName == null || countryName.isEmpty()) {
			return ECollections.emptyEList();
		}
		final UniqueEList<Port> result = new UniqueEList<Port>();
		marked.add(this);

		
		final EObject parent = eContainer();
		if (parent instanceof PortModel) {
			final PortModel portModel = (PortModel)parent;
			for (final Port p : portModel.getPorts()) {
				final Location location = p.getLocation();
				if (location != null) {
					if (countryName.equalsIgnoreCase(location.getCountry())) {
						result.add(p);
					}
				}
			}
		}
		return result;
	}
	
} //PortCountryGroupImpl

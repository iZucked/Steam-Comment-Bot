/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.mmxcore.impl.NamedObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Charter Index</b></em>'.
 * @since 5.0
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class CharterIndexImpl extends NamedIndexContainerImpl<Integer> implements CharterIndex {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CharterIndexImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.CHARTER_INDEX;
	}

} //CharterIndexImpl

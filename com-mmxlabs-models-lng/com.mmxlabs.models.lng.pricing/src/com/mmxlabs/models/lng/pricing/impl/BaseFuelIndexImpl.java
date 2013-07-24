/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing.impl;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.pricing.BaseFuelIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.PricingPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Base Fuel Index</b></em>'.
 * @since 5.0
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class BaseFuelIndexImpl extends NamedIndexContainerImpl<Double> implements BaseFuelIndex {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BaseFuelIndexImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.BASE_FUEL_INDEX;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public double getPrice() {
		final Index<Double> data = getData();
		return data.getValueForMonth(data.getDates().get(0));
	}

} //BaseFuelIndexImpl

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing;
import java.util.Date;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.types.AIndex;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Index</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getIndex()
 * @model
 * @generated
 */
public interface Index<Value> extends AIndex {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true" dateRequired="true"
	 * @generated
	 */
	Value getValueAfter(Date date);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EList<Date> getDates();
} // end of  Index

// finish type fixing

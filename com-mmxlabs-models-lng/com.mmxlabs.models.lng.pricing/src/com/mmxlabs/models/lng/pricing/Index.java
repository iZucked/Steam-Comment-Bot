/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing;
import java.time.YearMonth;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Index</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getIndex()
 * @model abstract="true"
 * @generated
 */
public interface Index<Value> extends EObject {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true" dateDataType="com.mmxlabs.models.datetime.YearMonth" dateRequired="true"
	 * @generated
	 */
	Value getValueForMonth(YearMonth date);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" dataType="com.mmxlabs.models.datetime.YearMonth"
	 * @generated
	 */
	EList<YearMonth> getDates();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true" dateDataType="com.mmxlabs.models.datetime.YearMonth" dateRequired="true"
	 * @generated
	 */
	Value getForwardValueForMonth(YearMonth date);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model required="true" dateDataType="com.mmxlabs.models.datetime.YearMonth" dateRequired="true"
	 * @generated
	 */
	Value getBackwardsValueForMonth(YearMonth date);
} // end of  Index

// finish type fixing

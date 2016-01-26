/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.pricing.DataIndex;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Spot Availability</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.SpotAvailability#getConstant <em>Constant</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.SpotAvailability#getCurve <em>Curve</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getSpotAvailability()
 * @model
 * @generated
 */
public interface SpotAvailability extends EObject {
	/**
	 * Returns the value of the '<em><b>Constant</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Constant</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Constant</em>' attribute.
	 * @see #isSetConstant()
	 * @see #unsetConstant()
	 * @see #setConstant(int)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getSpotAvailability_Constant()
	 * @model unsettable="true"
	 * @generated
	 */
	int getConstant();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.SpotAvailability#getConstant <em>Constant</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Constant</em>' attribute.
	 * @see #isSetConstant()
	 * @see #unsetConstant()
	 * @see #getConstant()
	 * @generated
	 */
	void setConstant(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.SpotAvailability#getConstant <em>Constant</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetConstant()
	 * @see #getConstant()
	 * @see #setConstant(int)
	 * @generated
	 */
	void unsetConstant();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.spotmarkets.SpotAvailability#getConstant <em>Constant</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Constant</em>' attribute is set.
	 * @see #unsetConstant()
	 * @see #getConstant()
	 * @see #setConstant(int)
	 * @generated
	 */
	boolean isSetConstant();

	/**
	 * Returns the value of the '<em><b>Curve</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Curve</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Curve</em>' containment reference.
	 * @see #isSetCurve()
	 * @see #unsetCurve()
	 * @see #setCurve(DataIndex)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getSpotAvailability_Curve()
	 * @model type="com.mmxlabs.models.lng.pricing.DataIndex<org.eclipse.emf.ecore.EIntegerObject>" containment="true" unsettable="true" required="true"
	 * @generated
	 */
	DataIndex<Integer> getCurve();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.SpotAvailability#getCurve <em>Curve</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Curve</em>' containment reference.
	 * @see #isSetCurve()
	 * @see #unsetCurve()
	 * @see #getCurve()
	 * @generated
	 */
	void setCurve(DataIndex<Integer> value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.SpotAvailability#getCurve <em>Curve</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetCurve()
	 * @see #getCurve()
	 * @see #setCurve(DataIndex)
	 * @generated
	 */
	void unsetCurve();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.spotmarkets.SpotAvailability#getCurve <em>Curve</em>}' containment reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Curve</em>' containment reference is set.
	 * @see #unsetCurve()
	 * @see #getCurve()
	 * @see #setCurve(DataIndex)
	 * @generated
	 */
	boolean isSetCurve();

} // end of  SpotAvailability

// finish type fixing

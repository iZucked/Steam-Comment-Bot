/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Base Fuel</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.BaseFuel#getEquivalenceFactor <em>Equivalence Factor</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.BaseFuel#getEmissionRate <em>Emission Rate</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getBaseFuel()
 * @model
 * @generated
 */
public interface BaseFuel extends UUIDObject, NamedObject {
 
	/**
	 * Returns the value of the '<em><b>Equivalence Factor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Equivalence Factor</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Equivalence Factor</em>' attribute.
	 * @see #setEquivalenceFactor(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getBaseFuel_EquivalenceFactor()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='mmBtu/mt' formatString='##.###'"
	 * @generated
	 */
	double getEquivalenceFactor();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.BaseFuel#getEquivalenceFactor <em>Equivalence Factor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Equivalence Factor</em>' attribute.
	 * @see #getEquivalenceFactor()
	 * @generated
	 */
	void setEquivalenceFactor(double value);

	/**
	 * Returns the value of the '<em><b>Emission Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Emission Rate</em>' attribute.
	 * @see #isSetEmissionRate()
	 * @see #unsetEmissionRate()
	 * @see #setEmissionRate(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getBaseFuel_EmissionRate()
	 * @model unsettable="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='t/t' formatString='####0.###'"
	 * @generated
	 */
	double getEmissionRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.BaseFuel#getEmissionRate <em>Emission Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Emission Rate</em>' attribute.
	 * @see #isSetEmissionRate()
	 * @see #unsetEmissionRate()
	 * @see #getEmissionRate()
	 * @generated
	 */
	void setEmissionRate(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.fleet.BaseFuel#getEmissionRate <em>Emission Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetEmissionRate()
	 * @see #getEmissionRate()
	 * @see #setEmissionRate(double)
	 * @generated
	 */
	void unsetEmissionRate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.fleet.BaseFuel#getEmissionRate <em>Emission Rate</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Emission Rate</em>' attribute is set.
	 * @see #unsetEmissionRate()
	 * @see #getEmissionRate()
	 * @see #setEmissionRate(double)
	 * @generated
	 */
	boolean isSetEmissionRate();
} // end of  BaseFuel

// finish type fixing

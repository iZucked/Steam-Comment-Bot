/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Distribution Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.DistributionModel#getVolumePerCargo <em>Volume Per Cargo</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.DistributionModel#getVolumeUnit <em>Volume Unit</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getDistributionModel()
 * @model abstract="true"
 * @generated
 */
public interface DistributionModel extends MMXObject {

	/**
	 * Returns the value of the '<em><b>Volume Per Cargo</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume Per Cargo</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Per Cargo</em>' attribute.
	 * @see #isSetVolumePerCargo()
	 * @see #unsetVolumePerCargo()
	 * @see #setVolumePerCargo(double)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getDistributionModel_VolumePerCargo()
	 * @model unsettable="true"
	 * @generated
	 */
	double getVolumePerCargo();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.DistributionModel#getVolumePerCargo <em>Volume Per Cargo</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Per Cargo</em>' attribute.
	 * @see #isSetVolumePerCargo()
	 * @see #unsetVolumePerCargo()
	 * @see #getVolumePerCargo()
	 * @generated
	 */
	void setVolumePerCargo(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.adp.DistributionModel#getVolumePerCargo <em>Volume Per Cargo</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetVolumePerCargo()
	 * @see #getVolumePerCargo()
	 * @see #setVolumePerCargo(double)
	 * @generated
	 */
	void unsetVolumePerCargo();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.adp.DistributionModel#getVolumePerCargo <em>Volume Per Cargo</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Volume Per Cargo</em>' attribute is set.
	 * @see #unsetVolumePerCargo()
	 * @see #getVolumePerCargo()
	 * @see #setVolumePerCargo(double)
	 * @generated
	 */
	boolean isSetVolumePerCargo();

	/**
	 * Returns the value of the '<em><b>Volume Unit</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.adp.LNGVolumeUnit}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume Unit</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Unit</em>' attribute.
	 * @see com.mmxlabs.models.lng.adp.LNGVolumeUnit
	 * @see #isSetVolumeUnit()
	 * @see #unsetVolumeUnit()
	 * @see #setVolumeUnit(LNGVolumeUnit)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getDistributionModel_VolumeUnit()
	 * @model unsettable="true"
	 * @generated
	 */
	LNGVolumeUnit getVolumeUnit();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.DistributionModel#getVolumeUnit <em>Volume Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Unit</em>' attribute.
	 * @see com.mmxlabs.models.lng.adp.LNGVolumeUnit
	 * @see #isSetVolumeUnit()
	 * @see #unsetVolumeUnit()
	 * @see #getVolumeUnit()
	 * @generated
	 */
	void setVolumeUnit(LNGVolumeUnit value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.adp.DistributionModel#getVolumeUnit <em>Volume Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetVolumeUnit()
	 * @see #getVolumeUnit()
	 * @see #setVolumeUnit(LNGVolumeUnit)
	 * @generated
	 */
	void unsetVolumeUnit();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.adp.DistributionModel#getVolumeUnit <em>Volume Unit</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Volume Unit</em>' attribute is set.
	 * @see #unsetVolumeUnit()
	 * @see #getVolumeUnit()
	 * @see #setVolumeUnit(LNGVolumeUnit)
	 * @generated
	 */
	boolean isSetVolumeUnit();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	double getModelOrContractVolumePerCargo();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	LNGVolumeUnit getModelOrContractVolumeUnit();

} // DistributionModel

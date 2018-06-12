/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp;

import org.eclipse.emf.ecore.EObject;

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
 * @model interface="true" abstract="true"
 * @generated
 */
public interface DistributionModel extends EObject {

	/**
	 * Returns the value of the '<em><b>Volume Per Cargo</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume Per Cargo</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Per Cargo</em>' attribute.
	 * @see #setVolumePerCargo(double)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getDistributionModel_VolumePerCargo()
	 * @model
	 * @generated
	 */
	double getVolumePerCargo();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.DistributionModel#getVolumePerCargo <em>Volume Per Cargo</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Per Cargo</em>' attribute.
	 * @see #getVolumePerCargo()
	 * @generated
	 */
	void setVolumePerCargo(double value);

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
	 * @see #setVolumeUnit(LNGVolumeUnit)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getDistributionModel_VolumeUnit()
	 * @model
	 * @generated
	 */
	LNGVolumeUnit getVolumeUnit();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.DistributionModel#getVolumeUnit <em>Volume Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Unit</em>' attribute.
	 * @see com.mmxlabs.models.lng.adp.LNGVolumeUnit
	 * @see #getVolumeUnit()
	 * @generated
	 */
	void setVolumeUnit(LNGVolumeUnit value);

} // DistributionModel

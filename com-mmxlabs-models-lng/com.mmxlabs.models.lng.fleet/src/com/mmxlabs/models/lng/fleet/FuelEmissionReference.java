/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.fleet;

import com.mmxlabs.models.mmxcore.NamedObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Fuel Emission Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.FuelEmissionReference#getIsoReference <em>Iso Reference</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.FuelEmissionReference#getCf <em>Cf</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getFuelEmissionReference()
 * @model
 * @generated
 */
public interface FuelEmissionReference extends NamedObject {
	/**
	 * Returns the value of the '<em><b>Iso Reference</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Iso Reference</em>' attribute.
	 * @see #setIsoReference(String)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getFuelEmissionReference_IsoReference()
	 * @model
	 * @generated
	 */
	String getIsoReference();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.FuelEmissionReference#getIsoReference <em>Iso Reference</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Iso Reference</em>' attribute.
	 * @see #getIsoReference()
	 * @generated
	 */
	void setIsoReference(String value);

	/**
	 * Returns the value of the '<em><b>Cf</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cf</em>' attribute.
	 * @see #setCf(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getFuelEmissionReference_Cf()
	 * @model
	 * @generated
	 */
	double getCf();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.FuelEmissionReference#getCf <em>Cf</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cf</em>' attribute.
	 * @see #getCf()
	 * @generated
	 */
	void setCf(double value);

} // FuelEmissionReference

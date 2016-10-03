/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Named Index Container</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.NamedIndexContainer#getData <em>Data</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.NamedIndexContainer#getCurrencyUnit <em>Currency Unit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.NamedIndexContainer#getVolumeUnit <em>Volume Unit</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getNamedIndexContainer()
 * @model
 * @generated
 */
public interface NamedIndexContainer<Value> extends UUIDObject, NamedObject {
	/**
	 * Returns the value of the '<em><b>Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Data</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Data</em>' containment reference.
	 * @see #setData(Index)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getNamedIndexContainer_Data()
	 * @model containment="true" required="true"
	 * @generated
	 */
	Index<Value> getData();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.NamedIndexContainer#getData <em>Data</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Data</em>' containment reference.
	 * @see #getData()
	 * @generated
	 */
	void setData(Index<Value> value);

	/**
	 * Returns the value of the '<em><b>Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Units</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Units</em>' attribute.
	 * @see #setUnits(String)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getNamedIndexContainer_Units()
	 * @model
	 * @generated
	 */
//	String getUnits();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.NamedIndexContainer#getUnits <em>Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Units</em>' attribute.
	 * @see #getUnits()
	 * @generated
	 */
//	void setUnits(String value);

	/**
	 * Returns the value of the '<em><b>Currency Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Currency Unit</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Currency Unit</em>' attribute.
	 * @see #setCurrencyUnit(String)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getNamedIndexContainer_CurrencyUnit()
	 * @model
	 * @generated
	 */
	String getCurrencyUnit();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.NamedIndexContainer#getCurrencyUnit <em>Currency Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Currency Unit</em>' attribute.
	 * @see #getCurrencyUnit()
	 * @generated
	 */
	void setCurrencyUnit(String value);

	/**
	 * Returns the value of the '<em><b>Volume Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume Unit</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Unit</em>' attribute.
	 * @see #setVolumeUnit(String)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getNamedIndexContainer_VolumeUnit()
	 * @model
	 * @generated
	 */
	String getVolumeUnit();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.NamedIndexContainer#getVolumeUnit <em>Volume Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Unit</em>' attribute.
	 * @see #getVolumeUnit()
	 * @generated
	 */
	void setVolumeUnit(String value);

} // NamedIndexContainer

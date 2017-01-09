/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing;

import java.time.LocalDate;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Panama Canal Tariff</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariff#getBands <em>Bands</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariff#getAvailableFrom <em>Available From</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariff#getMarkupRate <em>Markup Rate</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPanamaCanalTariff()
 * @model
 * @generated
 */
public interface PanamaCanalTariff extends EObject {
	/**
	 * Returns the value of the '<em><b>Bands</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bands</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bands</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPanamaCanalTariff_Bands()
	 * @model containment="true"
	 * @generated
	 */
	EList<PanamaCanalTariffBand> getBands();

	/**
	 * Returns the value of the '<em><b>Available From</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Available From</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Available From</em>' attribute.
	 * @see #isSetAvailableFrom()
	 * @see #unsetAvailableFrom()
	 * @see #setAvailableFrom(LocalDate)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPanamaCanalTariff_AvailableFrom()
	 * @model unsettable="true" dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getAvailableFrom();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariff#getAvailableFrom <em>Available From</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Available From</em>' attribute.
	 * @see #isSetAvailableFrom()
	 * @see #unsetAvailableFrom()
	 * @see #getAvailableFrom()
	 * @generated
	 */
	void setAvailableFrom(LocalDate value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariff#getAvailableFrom <em>Available From</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetAvailableFrom()
	 * @see #getAvailableFrom()
	 * @see #setAvailableFrom(LocalDate)
	 * @generated
	 */
	void unsetAvailableFrom();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariff#getAvailableFrom <em>Available From</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Available From</em>' attribute is set.
	 * @see #unsetAvailableFrom()
	 * @see #getAvailableFrom()
	 * @see #setAvailableFrom(LocalDate)
	 * @generated
	 */
	boolean isSetAvailableFrom();

	/**
	 * Returns the value of the '<em><b>Markup Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Markup Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Markup Rate</em>' attribute.
	 * @see #setMarkupRate(double)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPanamaCanalTariff_MarkupRate()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat scale='100' formatString='##0.#' unit='%'"
	 * @generated
	 */
	double getMarkupRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariff#getMarkupRate <em>Markup Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Markup Rate</em>' attribute.
	 * @see #getMarkupRate()
	 * @generated
	 */
	void setMarkupRate(double value);

} // PanamaCanalTariff

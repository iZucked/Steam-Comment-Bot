/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing;

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
 *   <li>{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariff#getMarkupRate <em>Markup Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PanamaCanalTariff#getAnnualTariffs <em>Annual Tariffs</em>}</li>
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

	/**
	 * Returns the value of the '<em><b>Annual Tariffs</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.PanamaTariffV2}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Annual Tariffs</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPanamaCanalTariff_AnnualTariffs()
	 * @model containment="true"
	 * @generated
	 */
	EList<PanamaTariffV2> getAnnualTariffs();

} // PanamaCanalTariff

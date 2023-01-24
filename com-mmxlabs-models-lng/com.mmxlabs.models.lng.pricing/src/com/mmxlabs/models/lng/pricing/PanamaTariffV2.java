/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Panama Tariff V2</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PanamaTariffV2#getFixedFee <em>Fixed Fee</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PanamaTariffV2#getCapacityTariff <em>Capacity Tariff</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PanamaTariffV2#getEffectiveFrom <em>Effective From</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPanamaTariffV2()
 * @model
 * @generated
 */
public interface PanamaTariffV2 extends EObject {
	/**
	 * Returns the value of the '<em><b>Fixed Fee</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fixed Fee</em>' attribute.
	 * @see #setFixedFee(double)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPanamaTariffV2_FixedFee()
	 * @model
	 * @generated
	 */
	double getFixedFee();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PanamaTariffV2#getFixedFee <em>Fixed Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fixed Fee</em>' attribute.
	 * @see #getFixedFee()
	 * @generated
	 */
	void setFixedFee(double value);

	/**
	 * Returns the value of the '<em><b>Capacity Tariff</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Capacity Tariff</em>' attribute.
	 * @see #setCapacityTariff(double)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPanamaTariffV2_CapacityTariff()
	 * @model
	 * @generated
	 */
	double getCapacityTariff();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PanamaTariffV2#getCapacityTariff <em>Capacity Tariff</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Capacity Tariff</em>' attribute.
	 * @see #getCapacityTariff()
	 * @generated
	 */
	void setCapacityTariff(double value);

	/**
	 * Returns the value of the '<em><b>Effective From</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Effective From</em>' attribute.
	 * @see #setEffectiveFrom(LocalDate)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPanamaTariffV2_EffectiveFrom()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getEffectiveFrom();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PanamaTariffV2#getEffectiveFrom <em>Effective From</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Effective From</em>' attribute.
	 * @see #getEffectiveFrom()
	 * @generated
	 */
	void setEffectiveFrom(LocalDate value);

} // PanamaTariffV2

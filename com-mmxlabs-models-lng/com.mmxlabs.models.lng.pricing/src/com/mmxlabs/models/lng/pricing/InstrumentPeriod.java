/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing;

import com.mmxlabs.models.lng.types.PricingPeriod;
import com.mmxlabs.models.lng.types.TimePeriod;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Instrument Period</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.InstrumentPeriod#getPeriodSize <em>Period Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.InstrumentPeriod#getPeriodSizeUnit <em>Period Size Unit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.InstrumentPeriod#getPeriodOffset <em>Period Offset</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getInstrumentPeriod()
 * @model
 * @generated
 */
public interface InstrumentPeriod extends EObject {
	/**
	 * Returns the value of the '<em><b>Period Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Period Size</em>' attribute.
	 * @see #setPeriodSize(int)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getInstrumentPeriod_PeriodSize()
	 * @model
	 * @generated
	 */
	int getPeriodSize();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.InstrumentPeriod#getPeriodSize <em>Period Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Period Size</em>' attribute.
	 * @see #getPeriodSize()
	 * @generated
	 */
	void setPeriodSize(int value);

	/**
	 * Returns the value of the '<em><b>Period Size Unit</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.types.PricingPeriod}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Period Size Unit</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.PricingPeriod
	 * @see #setPeriodSizeUnit(PricingPeriod)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getInstrumentPeriod_PeriodSizeUnit()
	 * @model
	 * @generated
	 */
	PricingPeriod getPeriodSizeUnit();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.InstrumentPeriod#getPeriodSizeUnit <em>Period Size Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Period Size Unit</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.PricingPeriod
	 * @see #getPeriodSizeUnit()
	 * @generated
	 */
	void setPeriodSizeUnit(PricingPeriod value);

	/**
	 * Returns the value of the '<em><b>Period Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Period Offset</em>' attribute.
	 * @see #setPeriodOffset(int)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getInstrumentPeriod_PeriodOffset()
	 * @model
	 * @generated
	 */
	int getPeriodOffset();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.InstrumentPeriod#getPeriodOffset <em>Period Offset</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Period Offset</em>' attribute.
	 * @see #getPeriodOffset()
	 * @generated
	 */
	void setPeriodOffset(int value);

} // InstrumentPeriod

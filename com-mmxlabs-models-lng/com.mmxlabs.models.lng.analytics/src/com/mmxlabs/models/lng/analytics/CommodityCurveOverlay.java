/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.YearMonthPointContainer;

import com.mmxlabs.models.mmxcore.UUIDObject;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Commodity Curve Overlay</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.CommodityCurveOverlay#getReferenceCurve <em>Reference Curve</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.CommodityCurveOverlay#getAlternativeCurves <em>Alternative Curves</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getCommodityCurveOverlay()
 * @model
 * @generated
 */
public interface CommodityCurveOverlay extends UUIDObject, CommodityCurveOption {
	/**
	 * Returns the value of the '<em><b>Reference Curve</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Reference Curve</em>' reference.
	 * @see #setReferenceCurve(CommodityCurve)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getCommodityCurveOverlay_ReferenceCurve()
	 * @model
	 * @generated
	 */
	CommodityCurve getReferenceCurve();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.CommodityCurveOverlay#getReferenceCurve <em>Reference Curve</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Reference Curve</em>' reference.
	 * @see #getReferenceCurve()
	 * @generated
	 */
	void setReferenceCurve(CommodityCurve value);

	/**
	 * Returns the value of the '<em><b>Alternative Curves</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.YearMonthPointContainer}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Alternative Curves</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getCommodityCurveOverlay_AlternativeCurves()
	 * @model containment="true"
	 * @generated
	 */
	EList<YearMonthPointContainer> getAlternativeCurves();

} // CommodityCurveOverlay

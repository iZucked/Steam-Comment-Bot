/**
 */
package com.mmxlabs.models.lng.pricing;

import com.mmxlabs.models.lng.port.Port;

import com.mmxlabs.models.lng.types.APortSet;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Suez Canal Route Rebate</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.SuezCanalRouteRebate#getFrom <em>From</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.SuezCanalRouteRebate#getTo <em>To</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.SuezCanalRouteRebate#getRebate <em>Rebate</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSuezCanalRouteRebate()
 * @model
 * @generated
 */
public interface SuezCanalRouteRebate extends EObject {
	/**
	 * Returns the value of the '<em><b>From</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}<code>&lt;com.mmxlabs.models.lng.port.Port&gt;</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>From</em>' reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSuezCanalRouteRebate_From()
	 * @model
	 * @generated
	 */
	EList<APortSet<Port>> getFrom();

	/**
	 * Returns the value of the '<em><b>To</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}<code>&lt;com.mmxlabs.models.lng.port.Port&gt;</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>To</em>' reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSuezCanalRouteRebate_To()
	 * @model
	 * @generated
	 */
	EList<APortSet<Port>> getTo();

	/**
	 * Returns the value of the '<em><b>Rebate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rebate</em>' attribute.
	 * @see #setRebate(double)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSuezCanalRouteRebate_Rebate()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat scale='100' formatString='##0.#' unit='%'"
	 * @generated
	 */
	double getRebate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.SuezCanalRouteRebate#getRebate <em>Rebate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rebate</em>' attribute.
	 * @see #getRebate()
	 * @generated
	 */
	void setRebate(double value);

} // SuezCanalRouteRebate

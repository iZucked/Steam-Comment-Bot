/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.fleet.Vessel;

import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Market Allocation Row</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.MarketAllocationRow#getMarket <em>Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.MarketAllocationRow#getWeight <em>Weight</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.MarketAllocationRow#getVessels <em>Vessels</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getMarketAllocationRow()
 * @model
 * @generated
 */
public interface MarketAllocationRow extends EObject {
	/**
	 * Returns the value of the '<em><b>Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Market</em>' reference.
	 * @see #setMarket(DESSalesMarket)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getMarketAllocationRow_Market()
	 * @model
	 * @generated
	 */
	DESSalesMarket getMarket();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.MarketAllocationRow#getMarket <em>Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Market</em>' reference.
	 * @see #getMarket()
	 * @generated
	 */
	void setMarket(DESSalesMarket value);

	/**
	 * Returns the value of the '<em><b>Weight</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Weight</em>' attribute.
	 * @see #setWeight(int)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getMarketAllocationRow_Weight()
	 * @model
	 * @generated
	 */
	int getWeight();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.MarketAllocationRow#getWeight <em>Weight</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Weight</em>' attribute.
	 * @see #getWeight()
	 * @generated
	 */
	void setWeight(int value);

	/**
	 * Returns the value of the '<em><b>Vessels</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.fleet.Vessel}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessels</em>' reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getMarketAllocationRow_Vessels()
	 * @model
	 * @generated
	 */
	EList<Vessel> getVessels();

} // MarketAllocationRow

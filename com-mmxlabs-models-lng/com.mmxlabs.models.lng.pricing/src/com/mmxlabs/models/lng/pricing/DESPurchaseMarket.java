/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing;
import com.mmxlabs.models.lng.types.APortSet;

import com.mmxlabs.models.lng.commercial.PurchaseContract;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>DES Purchase Market</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.DESPurchaseMarket#getCv <em>Cv</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.DESPurchaseMarket#getDestinationPorts <em>Destination Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.DESPurchaseMarket#getContract <em>Contract</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getDESPurchaseMarket()
 * @model
 * @generated
 */
public interface DESPurchaseMarket extends SpotMarket {
	/**
	 * Returns the value of the '<em><b>Cv</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cv</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cv</em>' attribute.
	 * @see #setCv(double)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getDESPurchaseMarket_Cv()
	 * @model
	 * @generated
	 */
	double getCv();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.DESPurchaseMarket#getCv <em>Cv</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cv</em>' attribute.
	 * @see #getCv()
	 * @generated
	 */
	void setCv(double value);

	/**
	 * Returns the value of the '<em><b>Destination Ports</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Destination Ports</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Destination Ports</em>' reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getDESPurchaseMarket_DestinationPorts()
	 * @model
	 * @generated
	 */
	EList<APortSet> getDestinationPorts();

	/**
	 * Returns the value of the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contract</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contract</em>' reference.
	 * @see #setContract(PurchaseContract)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getDESPurchaseMarket_Contract()
	 * @model required="true"
	 * @generated
	 */
	PurchaseContract getContract();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.DESPurchaseMarket#getContract <em>Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contract</em>' reference.
	 * @see #getContract()
	 * @generated
	 */
	void setContract(PurchaseContract value);

} // end of  DESPurchaseMarket

// finish type fixing

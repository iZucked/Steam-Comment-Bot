/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.ASpotMarket;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Spot Market</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.SpotMarket#getAvailability <em>Availability</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.SpotMarket#getPorts <em>Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.SpotMarket#getMinQuantity <em>Min Quantity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.SpotMarket#getMaxQuantity <em>Max Quantity</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSpotMarket()
 * @model
 * @generated
 */
public interface SpotMarket extends ASpotMarket {
	/**
	 * Returns the value of the '<em><b>Availability</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Availability</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Availability</em>' reference.
	 * @see #setAvailability(Index)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSpotMarket_Availability()
	 * @model type="com.mmxlabs.models.lng.pricing.Index<org.eclipse.emf.ecore.EIntegerObject>" required="true"
	 * @generated
	 */
	Index<Integer> getAvailability();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.SpotMarket#getAvailability <em>Availability</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Availability</em>' reference.
	 * @see #getAvailability()
	 * @generated
	 */
	void setAvailability(Index<Integer> value);

	/**
	 * Returns the value of the '<em><b>Ports</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ports</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ports</em>' reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSpotMarket_Ports()
	 * @model
	 * @generated
	 */
	EList<APortSet> getPorts();

	/**
	 * Returns the value of the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Quantity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Quantity</em>' attribute.
	 * @see #setMinQuantity(int)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSpotMarket_MinQuantity()
	 * @model required="true"
	 * @generated
	 */
	int getMinQuantity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.SpotMarket#getMinQuantity <em>Min Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Quantity</em>' attribute.
	 * @see #getMinQuantity()
	 * @generated
	 */
	void setMinQuantity(int value);

	/**
	 * Returns the value of the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Quantity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Quantity</em>' attribute.
	 * @see #setMaxQuantity(int)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSpotMarket_MaxQuantity()
	 * @model required="true"
	 * @generated
	 */
	int getMaxQuantity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.SpotMarket#getMaxQuantity <em>Max Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Quantity</em>' attribute.
	 * @see #getMaxQuantity()
	 * @generated
	 */
	void setMaxQuantity(int value);

} // end of  SpotMarket

// finish type fixing

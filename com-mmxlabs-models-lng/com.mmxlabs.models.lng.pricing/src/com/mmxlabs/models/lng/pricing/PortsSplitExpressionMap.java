/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ports Split Expression Map</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PortsSplitExpressionMap#getPorts <em>Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PortsSplitExpressionMap#getExpression1 <em>Expression1</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PortsSplitExpressionMap#getExpression2 <em>Expression2</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPortsSplitExpressionMap()
 * @model
 * @generated
 */
public interface PortsSplitExpressionMap extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Ports</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}&lt;com.mmxlabs.models.lng.port.Port>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ports</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ports</em>' reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPortsSplitExpressionMap_Ports()
	 * @model required="true"
	 * @generated
	 */
	EList<APortSet<Port>> getPorts();

	/**
	 * Returns the value of the '<em><b>Expression1</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Expression1</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Expression1</em>' attribute.
	 * @see #setExpression1(String)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPortsSplitExpressionMap_Expression1()
	 * @model required="true"
	 * @generated
	 */
	String getExpression1();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PortsSplitExpressionMap#getExpression1 <em>Expression1</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Expression1</em>' attribute.
	 * @see #getExpression1()
	 * @generated
	 */
	void setExpression1(String value);

	/**
	 * Returns the value of the '<em><b>Expression2</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Expression2</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Expression2</em>' attribute.
	 * @see #setExpression2(String)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPortsSplitExpressionMap_Expression2()
	 * @model required="true"
	 * @generated
	 */
	String getExpression2();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.PortsSplitExpressionMap#getExpression2 <em>Expression2</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Expression2</em>' attribute.
	 * @see #getExpression2()
	 * @generated
	 */
	void setExpression2(String value);

} // PortsSplitExpressionMap

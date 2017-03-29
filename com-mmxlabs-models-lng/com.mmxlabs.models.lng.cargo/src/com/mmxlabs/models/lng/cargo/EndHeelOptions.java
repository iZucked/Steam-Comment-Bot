/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>End Heel Options</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.EndHeelOptions#getTankState <em>Tank State</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.EndHeelOptions#getMinimumEndHeel <em>Minimum End Heel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.EndHeelOptions#getMaximumEndHeel <em>Maximum End Heel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.EndHeelOptions#getPriceExpression <em>Price Expression</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getEndHeelOptions()
 * @model
 * @generated
 */
public interface EndHeelOptions extends EObject {
	/**
	 * Returns the value of the '<em><b>Tank State</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.cargo.EVesselTankState}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tank State</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tank State</em>' attribute.
	 * @see com.mmxlabs.models.lng.cargo.EVesselTankState
	 * @see #setTankState(EVesselTankState)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getEndHeelOptions_TankState()
	 * @model
	 * @generated
	 */
	EVesselTankState getTankState();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.EndHeelOptions#getTankState <em>Tank State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Tank State</em>' attribute.
	 * @see com.mmxlabs.models.lng.cargo.EVesselTankState
	 * @see #getTankState()
	 * @generated
	 */
	void setTankState(EVesselTankState value);

	/**
	 * Returns the value of the '<em><b>Minimum End Heel</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Minimum End Heel</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Minimum End Heel</em>' attribute.
	 * @see #isSetMinimumEndHeel()
	 * @see #unsetMinimumEndHeel()
	 * @see #setMinimumEndHeel(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getEndHeelOptions_MinimumEndHeel()
	 * @model unsettable="true"
	 * @generated
	 */
	int getMinimumEndHeel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.EndHeelOptions#getMinimumEndHeel <em>Minimum End Heel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Minimum End Heel</em>' attribute.
	 * @see #isSetMinimumEndHeel()
	 * @see #unsetMinimumEndHeel()
	 * @see #getMinimumEndHeel()
	 * @generated
	 */
	void setMinimumEndHeel(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.EndHeelOptions#getMinimumEndHeel <em>Minimum End Heel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMinimumEndHeel()
	 * @see #getMinimumEndHeel()
	 * @see #setMinimumEndHeel(int)
	 * @generated
	 */
	void unsetMinimumEndHeel();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.EndHeelOptions#getMinimumEndHeel <em>Minimum End Heel</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Minimum End Heel</em>' attribute is set.
	 * @see #unsetMinimumEndHeel()
	 * @see #getMinimumEndHeel()
	 * @see #setMinimumEndHeel(int)
	 * @generated
	 */
	boolean isSetMinimumEndHeel();

	/**
	 * Returns the value of the '<em><b>Maximum End Heel</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Maximum End Heel</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Maximum End Heel</em>' attribute.
	 * @see #isSetMaximumEndHeel()
	 * @see #unsetMaximumEndHeel()
	 * @see #setMaximumEndHeel(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getEndHeelOptions_MaximumEndHeel()
	 * @model unsettable="true"
	 * @generated
	 */
	int getMaximumEndHeel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.EndHeelOptions#getMaximumEndHeel <em>Maximum End Heel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Maximum End Heel</em>' attribute.
	 * @see #isSetMaximumEndHeel()
	 * @see #unsetMaximumEndHeel()
	 * @see #getMaximumEndHeel()
	 * @generated
	 */
	void setMaximumEndHeel(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.EndHeelOptions#getMaximumEndHeel <em>Maximum End Heel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMaximumEndHeel()
	 * @see #getMaximumEndHeel()
	 * @see #setMaximumEndHeel(int)
	 * @generated
	 */
	void unsetMaximumEndHeel();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.EndHeelOptions#getMaximumEndHeel <em>Maximum End Heel</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Maximum End Heel</em>' attribute is set.
	 * @see #unsetMaximumEndHeel()
	 * @see #getMaximumEndHeel()
	 * @see #setMaximumEndHeel(int)
	 * @generated
	 */
	boolean isSetMaximumEndHeel();

	/**
	 * Returns the value of the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Price Expression</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Price Expression</em>' attribute.
	 * @see #setPriceExpression(String)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getEndHeelOptions_PriceExpression()
	 * @model annotation="http://www.mmxlabs.com/models/pricing/expressionType type='commodity'"
	 * @generated
	 */
	String getPriceExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.EndHeelOptions#getPriceExpression <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price Expression</em>' attribute.
	 * @see #getPriceExpression()
	 * @generated
	 */
	void setPriceExpression(String value);

} // EndHeelOptions

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.fleet;

import scenario.NamedObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Fuel</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.fleet.VesselFuel#getUnitPrice <em>Unit Price</em>}</li>
 *   <li>{@link scenario.fleet.VesselFuel#getEquivalenceFactor <em>Equivalence Factor</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.fleet.FleetPackage#getVesselFuel()
 * @model
 * @generated
 */
public interface VesselFuel extends NamedObject {
	/**
	 * Returns the value of the '<em><b>Unit Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Unit Price</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Unit Price</em>' attribute.
	 * @see #setUnitPrice(float)
	 * @see scenario.fleet.FleetPackage#getVesselFuel_UnitPrice()
	 * @model required="true"
	 * @generated
	 */
	float getUnitPrice();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselFuel#getUnitPrice <em>Unit Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Unit Price</em>' attribute.
	 * @see #getUnitPrice()
	 * @generated
	 */
	void setUnitPrice(float value);

	/**
	 * Returns the value of the '<em><b>Equivalence Factor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Equivalence Factor</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Equivalence Factor</em>' attribute.
	 * @see #setEquivalenceFactor(float)
	 * @see scenario.fleet.FleetPackage#getVesselFuel_EquivalenceFactor()
	 * @model required="true"
	 * @generated
	 */
	float getEquivalenceFactor();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselFuel#getEquivalenceFactor <em>Equivalence Factor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Equivalence Factor</em>' attribute.
	 * @see #getEquivalenceFactor()
	 * @generated
	 */
	void setEquivalenceFactor(float value);

} // VesselFuel

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.types;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Port Capability</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.types.TypesPackage#getPortCapability()
 * @model
 * @generated
 */
public enum PortCapability implements Enumerator {
	/**
	 * The '<em><b>LOAD</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #LOAD_VALUE
	 * @generated
	 * @ordered
	 */
	LOAD(0, "LOAD", "LOAD"),

	/**
	 * The '<em><b>DISCHARGE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DISCHARGE_VALUE
	 * @generated
	 * @ordered
	 */
	DISCHARGE(1, "DISCHARGE", "DISCHARGE"),

	/**
	 * The '<em><b>DRYDOCK</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DRYDOCK_VALUE
	 * @generated
	 * @ordered
	 */
	DRYDOCK(2, "DRYDOCK", "DRYDOCK"),

	/**
	 * The '<em><b>MAINTENANCE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MAINTENANCE_VALUE
	 * @generated
	 * @ordered
	 */
	MAINTENANCE(3, "MAINTENANCE", "MAINTENANCE"),
	/**
	* The '<em><b>TRANSFER</b></em>' literal object.
	* <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	* @see #TRANSFER_VALUE
	* @generated
	* @ordered
	*/
	TRANSFER(4, "TRANSFER", "TRANSFER");

	/**
	 * The '<em><b>LOAD</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>LOAD</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #LOAD
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int LOAD_VALUE = 0;

	/**
	 * The '<em><b>DISCHARGE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>DISCHARGE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DISCHARGE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int DISCHARGE_VALUE = 1;

	/**
	 * The '<em><b>DRYDOCK</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>DRYDOCK</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DRYDOCK
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int DRYDOCK_VALUE = 2;

	/**
	 * The '<em><b>MAINTENANCE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>MAINTENANCE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MAINTENANCE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int MAINTENANCE_VALUE = 3;

	/**
	 * The '<em><b>TRANSFER</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>TRANSFER</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #TRANSFER
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int TRANSFER_VALUE = 4;

	/**
	 * An array of all the '<em><b>Port Capability</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final PortCapability[] VALUES_ARRAY = new PortCapability[] { LOAD, DISCHARGE, DRYDOCK, MAINTENANCE, TRANSFER, };

	/**
	 * A public read-only list of all the '<em><b>Port Capability</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<PortCapability> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Port Capability</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static PortCapability get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			PortCapability result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Port Capability</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static PortCapability getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			PortCapability result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Port Capability</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static PortCapability get(int value) {
		switch (value) {
		case LOAD_VALUE:
			return LOAD;
		case DISCHARGE_VALUE:
			return DISCHARGE;
		case DRYDOCK_VALUE:
			return DRYDOCK;
		case MAINTENANCE_VALUE:
			return MAINTENANCE;
		case TRANSFER_VALUE:
			return TRANSFER;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private PortCapability(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLiteral() {
		return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string representation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}

} //PortCapability

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Fuel Choice</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getFuelChoice()
 * @model
 * @generated
 */
public enum FuelChoice implements Enumerator {
	/**
	 * The '<em><b>NBO FBO</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #NBO_FBO_VALUE
	 * @generated
	 * @ordered
	 */
	NBO_FBO(0, "NBO_FBO", "NBO_FBO"),

	/**
	 * The '<em><b>NBO BUNKERS</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #NBO_BUNKERS_VALUE
	 * @generated
	 * @ordered
	 */
	NBO_BUNKERS(1, "NBO_BUNKERS", "NBO_BUNKERS");

	/**
	 * The '<em><b>NBO FBO</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #NBO_FBO
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int NBO_FBO_VALUE = 0;

	/**
	 * The '<em><b>NBO BUNKERS</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #NBO_BUNKERS
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int NBO_BUNKERS_VALUE = 1;

	/**
	 * An array of all the '<em><b>Fuel Choice</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final FuelChoice[] VALUES_ARRAY =
		new FuelChoice[] {
			NBO_FBO,
			NBO_BUNKERS,
		};

	/**
	 * A public read-only list of all the '<em><b>Fuel Choice</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<FuelChoice> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Fuel Choice</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static FuelChoice get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			FuelChoice result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Fuel Choice</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static FuelChoice getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			FuelChoice result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Fuel Choice</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static FuelChoice get(int value) {
		switch (value) {
			case NBO_FBO_VALUE: return NBO_FBO;
			case NBO_BUNKERS_VALUE: return NBO_BUNKERS;
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
	private FuelChoice(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getValue() {
	  return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
	  return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	
} //FuelChoice

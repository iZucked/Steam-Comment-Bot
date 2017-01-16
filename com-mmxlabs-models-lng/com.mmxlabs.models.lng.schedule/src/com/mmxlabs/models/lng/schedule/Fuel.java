/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Fuel</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getFuel()
 * @model
 * @generated
 */
public enum Fuel implements Enumerator {
	/**
	 * The '<em><b>Base Fuel</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #BASE_FUEL_VALUE
	 * @generated
	 * @ordered
	 */
	BASE_FUEL(0, "BaseFuel", "BaseFuel"),

	/**
	 * The '<em><b>FBO</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #FBO_VALUE
	 * @generated
	 * @ordered
	 */
	FBO(1, "FBO", "FBO"),

	/**
	 * The '<em><b>NBO</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #NBO_VALUE
	 * @generated
	 * @ordered
	 */
	NBO(2, "NBO", "NBO"), /**
	 * The '<em><b>Pilot Light</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #PILOT_LIGHT_VALUE
	 * @generated
	 * @ordered
	 */
	PILOT_LIGHT(3, "PilotLight", "PilotLight");

	/**
	 * The '<em><b>Base Fuel</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Base Fuel</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #BASE_FUEL
	 * @model name="BaseFuel"
	 * @generated
	 * @ordered
	 */
	public static final int BASE_FUEL_VALUE = 0;

	/**
	 * The '<em><b>FBO</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>FBO</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #FBO
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int FBO_VALUE = 1;

	/**
	 * The '<em><b>NBO</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>NBO</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #NBO
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int NBO_VALUE = 2;

	/**
	 * The '<em><b>Pilot Light</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Pilot Light</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #PILOT_LIGHT
	 * @model name="PilotLight"
	 * @generated
	 * @ordered
	 */
	public static final int PILOT_LIGHT_VALUE = 3;

	/**
	 * An array of all the '<em><b>Fuel</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final Fuel[] VALUES_ARRAY =
		new Fuel[] {
			BASE_FUEL,
			FBO,
			NBO,
			PILOT_LIGHT,
		};

	/**
	 * A public read-only list of all the '<em><b>Fuel</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<Fuel> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Fuel</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static Fuel get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			Fuel result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Fuel</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static Fuel getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			Fuel result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Fuel</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static Fuel get(int value) {
		switch (value) {
			case BASE_FUEL_VALUE: return BASE_FUEL;
			case FBO_VALUE: return FBO;
			case NBO_VALUE: return NBO;
			case PILOT_LIGHT_VALUE: return PILOT_LIGHT;
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
	private Fuel(int value, String name, String literal) {
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
	
} //Fuel

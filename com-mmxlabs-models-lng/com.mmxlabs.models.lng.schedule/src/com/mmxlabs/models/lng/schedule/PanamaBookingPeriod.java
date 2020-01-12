/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Panama Booking Period</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getPanamaBookingPeriod()
 * @model
 * @generated
 */
public enum PanamaBookingPeriod implements Enumerator {
	/**
	 * The '<em><b>STRICT</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #STRICT_VALUE
	 * @generated
	 * @ordered
	 */
	STRICT(0, "STRICT", "STRICT"),

	/**
	 * The '<em><b>RELAXED</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #RELAXED_VALUE
	 * @generated
	 * @ordered
	 */
	RELAXED(1, "RELAXED", "RELAXED"),

	/**
	 * The '<em><b>BEYOND</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #BEYOND_VALUE
	 * @generated
	 * @ordered
	 */
	BEYOND(2, "BEYOND", "BEYOND"),

	/**
	 * The '<em><b>NOMINAL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #NOMINAL_VALUE
	 * @generated
	 * @ordered
	 */
	NOMINAL(3, "NOMINAL", "NOMINAL");

	/**
	 * The '<em><b>STRICT</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>STRICT</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #STRICT
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int STRICT_VALUE = 0;

	/**
	 * The '<em><b>RELAXED</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>RELAXED</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #RELAXED
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int RELAXED_VALUE = 1;

	/**
	 * The '<em><b>BEYOND</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>BEYOND</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #BEYOND
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int BEYOND_VALUE = 2;

	/**
	 * The '<em><b>NOMINAL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>NOMINAL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #NOMINAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int NOMINAL_VALUE = 3;

	/**
	 * An array of all the '<em><b>Panama Booking Period</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final PanamaBookingPeriod[] VALUES_ARRAY =
		new PanamaBookingPeriod[] {
			STRICT,
			RELAXED,
			BEYOND,
			NOMINAL,
		};

	/**
	 * A public read-only list of all the '<em><b>Panama Booking Period</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<PanamaBookingPeriod> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Panama Booking Period</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static PanamaBookingPeriod get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			PanamaBookingPeriod result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Panama Booking Period</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static PanamaBookingPeriod getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			PanamaBookingPeriod result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Panama Booking Period</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static PanamaBookingPeriod get(int value) {
		switch (value) {
			case STRICT_VALUE: return STRICT;
			case RELAXED_VALUE: return RELAXED;
			case BEYOND_VALUE: return BEYOND;
			case NOMINAL_VALUE: return NOMINAL;
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
	private PanamaBookingPeriod(int value, String name, String literal) {
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
	
} //PanamaBookingPeriod

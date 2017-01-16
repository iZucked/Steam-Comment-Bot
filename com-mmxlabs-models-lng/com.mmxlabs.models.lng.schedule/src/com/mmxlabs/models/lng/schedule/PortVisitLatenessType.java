/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
 * A representation of the literals of the enumeration '<em><b>Port Visit Lateness Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getPortVisitLatenessType()
 * @model
 * @generated
 */
public enum PortVisitLatenessType implements Enumerator {
	/**
	 * The '<em><b>PROMPT</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #PROMPT_VALUE
	 * @generated
	 * @ordered
	 */
	PROMPT(0, "PROMPT", "PROMPT"),

	/**
	 * The '<em><b>MID TERM</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MID_TERM_VALUE
	 * @generated
	 * @ordered
	 */
	MID_TERM(1, "MID_TERM", "MID_TERM"),

	/**
	 * The '<em><b>BEYOND</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #BEYOND_VALUE
	 * @generated
	 * @ordered
	 */
	BEYOND(2, "BEYOND", "BEYOND");

	/**
	 * The '<em><b>PROMPT</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>PROMPT</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #PROMPT
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int PROMPT_VALUE = 0;

	/**
	 * The '<em><b>MID TERM</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>MID TERM</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MID_TERM
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int MID_TERM_VALUE = 1;

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
	 * An array of all the '<em><b>Port Visit Lateness Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final PortVisitLatenessType[] VALUES_ARRAY =
		new PortVisitLatenessType[] {
			PROMPT,
			MID_TERM,
			BEYOND,
		};

	/**
	 * A public read-only list of all the '<em><b>Port Visit Lateness Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<PortVisitLatenessType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Port Visit Lateness Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static PortVisitLatenessType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			PortVisitLatenessType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Port Visit Lateness Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static PortVisitLatenessType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			PortVisitLatenessType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Port Visit Lateness Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static PortVisitLatenessType get(int value) {
		switch (value) {
			case PROMPT_VALUE: return PROMPT;
			case MID_TERM_VALUE: return MID_TERM;
			case BEYOND_VALUE: return BEYOND;
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
	private PortVisitLatenessType(int value, String name, String literal) {
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
	
} //PortVisitLatenessType

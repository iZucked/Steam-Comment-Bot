/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.schedule.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Change Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getChangeType()
 * @model
 * @generated
 */
public enum ChangeType implements Enumerator {
	/**
	 * The '<em><b>PNL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #PNL_VALUE
	 * @generated
	 * @ordered
	 */
	PNL(0, "PNL", "PNL"), /**
	 * The '<em><b>DURATION</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DURATION_VALUE
	 * @generated
	 * @ordered
	 */
	DURATION(1, "DURATION", "DURATION"),

	/**
	 * The '<em><b>CHARTERING</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CHARTERING_VALUE
	 * @generated
	 * @ordered
	 */
	CHARTERING(2, "CHARTERING", "CHARTERING"), /**
	 * The '<em><b>VESSEL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #VESSEL_VALUE
	 * @generated
	 * @ordered
	 */
	VESSEL(3, "VESSEL", "VESSEL"), /**
	 * The '<em><b>WIRING</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #WIRING_VALUE
	 * @generated
	 * @ordered
	 */
	WIRING(4, "WIRING", "WIRING");

	/**
	 * The '<em><b>PNL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>PNL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #PNL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int PNL_VALUE = 0;

	/**
	 * The '<em><b>DURATION</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>DURATION</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DURATION
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int DURATION_VALUE = 1;

	/**
	 * The '<em><b>CHARTERING</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>CHARTERING</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CHARTERING
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int CHARTERING_VALUE = 2;

	/**
	 * The '<em><b>VESSEL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>VESSEL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #VESSEL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int VESSEL_VALUE = 3;

	/**
	 * The '<em><b>WIRING</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>WIRING</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #WIRING
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int WIRING_VALUE = 4;

	/**
	 * An array of all the '<em><b>Change Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final ChangeType[] VALUES_ARRAY =
		new ChangeType[] {
			PNL,
			DURATION,
			CHARTERING,
			VESSEL,
			WIRING,
		};

	/**
	 * A public read-only list of all the '<em><b>Change Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<ChangeType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Change Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ChangeType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ChangeType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Change Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ChangeType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ChangeType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Change Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ChangeType get(int value) {
		switch (value) {
			case PNL_VALUE: return PNL;
			case DURATION_VALUE: return DURATION;
			case CHARTERING_VALUE: return CHARTERING;
			case VESSEL_VALUE: return VESSEL;
			case WIRING_VALUE: return WIRING;
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
	private ChangeType(int value, String name, String literal) {
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
	
} //ChangeType

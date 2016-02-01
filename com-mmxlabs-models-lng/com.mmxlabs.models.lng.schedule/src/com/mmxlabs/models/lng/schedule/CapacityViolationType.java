/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Capacity Violation Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getCapacityViolationType()
 * @model
 * @generated
 */
public enum CapacityViolationType implements Enumerator {
	/**
	 * The '<em><b>MAX LOAD</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MAX_LOAD_VALUE
	 * @generated
	 * @ordered
	 */
	MAX_LOAD(0, "MAX_LOAD", "MAX_LOAD"),

	/**
	 * The '<em><b>MAX DISCHARGE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MAX_DISCHARGE_VALUE
	 * @generated
	 * @ordered
	 */
	MAX_DISCHARGE(1, "MAX_DISCHARGE", "MAX_DISCHARGE"),

	/**
	 * The '<em><b>MIN LOAD</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MIN_LOAD_VALUE
	 * @generated
	 * @ordered
	 */
	MIN_LOAD(2, "MIN_LOAD", "MIN_LOAD"),

	/**
	 * The '<em><b>MIN DISCHARGE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MIN_DISCHARGE_VALUE
	 * @generated
	 * @ordered
	 */
	MIN_DISCHARGE(3, "MIN_DISCHARGE", "MIN_DISCHARGE"),

	/**
	 * The '<em><b>MAX HEEL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MAX_HEEL_VALUE
	 * @generated
	 * @ordered
	 */
	MAX_HEEL(4, "MAX_HEEL", "MAX_HEEL"),

	/**
	 * The '<em><b>FORCED COOLDOWN</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #FORCED_COOLDOWN_VALUE
	 * @generated
	 * @ordered
	 */
	FORCED_COOLDOWN(5, "FORCED_COOLDOWN", "FORCED_COOLDOWN"), /**
	 * The '<em><b>VESSEL CAPACITY</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #VESSEL_CAPACITY_VALUE
	 * @generated
	 * @ordered
	 */
	VESSEL_CAPACITY(6, "VESSEL_CAPACITY", "VESSEL_CAPACITY"), /**
	 * The '<em><b>LOST HEEL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #LOST_HEEL_VALUE
	 * @generated
	 * @ordered
	 */
	LOST_HEEL(7, "LOST_HEEL", "LOST_HEEL");

	/**
	 * The '<em><b>MAX LOAD</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>MAX LOAD</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MAX_LOAD
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int MAX_LOAD_VALUE = 0;

	/**
	 * The '<em><b>MAX DISCHARGE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>MAX DISCHARGE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MAX_DISCHARGE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int MAX_DISCHARGE_VALUE = 1;

	/**
	 * The '<em><b>MIN LOAD</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>MIN LOAD</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MIN_LOAD
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int MIN_LOAD_VALUE = 2;

	/**
	 * The '<em><b>MIN DISCHARGE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>MIN DISCHARGE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MIN_DISCHARGE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int MIN_DISCHARGE_VALUE = 3;

	/**
	 * The '<em><b>MAX HEEL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>MAX HEEL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MAX_HEEL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int MAX_HEEL_VALUE = 4;

	/**
	 * The '<em><b>FORCED COOLDOWN</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>FORCED COOLDOWN</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #FORCED_COOLDOWN
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int FORCED_COOLDOWN_VALUE = 5;

	/**
	 * The '<em><b>VESSEL CAPACITY</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>VESSEL CAPACITY</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #VESSEL_CAPACITY
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int VESSEL_CAPACITY_VALUE = 6;

	/**
	 * The '<em><b>LOST HEEL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>LOST HEEL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #LOST_HEEL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int LOST_HEEL_VALUE = 7;

	/**
	 * An array of all the '<em><b>Capacity Violation Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final CapacityViolationType[] VALUES_ARRAY =
		new CapacityViolationType[] {
			MAX_LOAD,
			MAX_DISCHARGE,
			MIN_LOAD,
			MIN_DISCHARGE,
			MAX_HEEL,
			FORCED_COOLDOWN,
			VESSEL_CAPACITY,
			LOST_HEEL,
		};

	/**
	 * A public read-only list of all the '<em><b>Capacity Violation Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<CapacityViolationType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Capacity Violation Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static CapacityViolationType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			CapacityViolationType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Capacity Violation Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static CapacityViolationType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			CapacityViolationType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Capacity Violation Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static CapacityViolationType get(int value) {
		switch (value) {
			case MAX_LOAD_VALUE: return MAX_LOAD;
			case MAX_DISCHARGE_VALUE: return MAX_DISCHARGE;
			case MIN_LOAD_VALUE: return MIN_LOAD;
			case MIN_DISCHARGE_VALUE: return MIN_DISCHARGE;
			case MAX_HEEL_VALUE: return MAX_HEEL;
			case FORCED_COOLDOWN_VALUE: return FORCED_COOLDOWN;
			case VESSEL_CAPACITY_VALUE: return VESSEL_CAPACITY;
			case LOST_HEEL_VALUE: return LOST_HEEL;
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
	private CapacityViolationType(int value, String name, String literal) {
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
	
} //CapacityViolationType

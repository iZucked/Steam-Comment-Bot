/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.commercial;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Pricing Event</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getPricingEvent()
 * @model
 * @generated
 */
public enum PricingEvent implements Enumerator {
	/**
	 * The '<em><b>START LOAD</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #START_LOAD_VALUE
	 * @generated
	 * @ordered
	 */
	START_LOAD(0, "START_LOAD", "START_LOAD"),

	/**
	 * The '<em><b>END LOAD</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #END_LOAD_VALUE
	 * @generated
	 * @ordered
	 */
	END_LOAD(1, "END_LOAD", "END_LOAD"),

	/**
	 * The '<em><b>START DISCHARGE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #START_DISCHARGE_VALUE
	 * @generated
	 * @ordered
	 */
	START_DISCHARGE(2, "START_DISCHARGE", "START_DISCHARGE"),

	/**
	 * The '<em><b>END DISCHARGE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #END_DISCHARGE_VALUE
	 * @generated
	 * @ordered
	 */
	END_DISCHARGE(3, "END_DISCHARGE", "END_DISCHARGE");

	/**
	 * The '<em><b>START LOAD</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>START LOAD</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #START_LOAD
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int START_LOAD_VALUE = 0;

	/**
	 * The '<em><b>END LOAD</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>END LOAD</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #END_LOAD
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int END_LOAD_VALUE = 1;

	/**
	 * The '<em><b>START DISCHARGE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>START DISCHARGE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #START_DISCHARGE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int START_DISCHARGE_VALUE = 2;

	/**
	 * The '<em><b>END DISCHARGE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>END DISCHARGE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #END_DISCHARGE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int END_DISCHARGE_VALUE = 3;

	/**
	 * An array of all the '<em><b>Pricing Event</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final PricingEvent[] VALUES_ARRAY =
		new PricingEvent[] {
			START_LOAD,
			END_LOAD,
			START_DISCHARGE,
			END_DISCHARGE,
		};

	/**
	 * A public read-only list of all the '<em><b>Pricing Event</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<PricingEvent> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Pricing Event</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static PricingEvent get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			PricingEvent result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Pricing Event</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static PricingEvent getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			PricingEvent result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Pricing Event</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static PricingEvent get(int value) {
		switch (value) {
			case START_LOAD_VALUE: return START_LOAD;
			case END_LOAD_VALUE: return END_LOAD;
			case START_DISCHARGE_VALUE: return START_DISCHARGE;
			case END_DISCHARGE_VALUE: return END_DISCHARGE;
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
	private PricingEvent(int value, String name, String literal) {
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
	
} //PricingEvent

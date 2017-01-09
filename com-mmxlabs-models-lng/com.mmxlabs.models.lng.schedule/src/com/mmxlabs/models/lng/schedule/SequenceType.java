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
 * A representation of the literals of the enumeration '<em><b>Sequence Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSequenceType()
 * @model
 * @generated
 */
public enum SequenceType implements Enumerator {
	/**
	 * The '<em><b>VESSEL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #VESSEL_VALUE
	 * @generated
	 * @ordered
	 */
	VESSEL(0, "VESSEL", "VESSEL"),

	/**
	 * The '<em><b>SPOT VESSEL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #SPOT_VESSEL_VALUE
	 * @generated
	 * @ordered
	 */
	SPOT_VESSEL(1, "SPOT_VESSEL", "SPOT_VESSEL"),

	/**
	 * The '<em><b>DES PURCHASE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DES_PURCHASE_VALUE
	 * @generated
	 * @ordered
	 */
	DES_PURCHASE(2, "DES_PURCHASE", "DES_PURCHASE"),

	/**
	 * The '<em><b>FOB SALE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #FOB_SALE_VALUE
	 * @generated
	 * @ordered
	 */
	FOB_SALE(3, "FOB_SALE", "FOB_SALE"),

	/**
	 * The '<em><b>ROUND TRIP</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ROUND_TRIP_VALUE
	 * @generated
	 * @ordered
	 */
	ROUND_TRIP(4, "ROUND_TRIP", "ROUND_TRIP");

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
	public static final int VESSEL_VALUE = 0;

	/**
	 * The '<em><b>SPOT VESSEL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>SPOT VESSEL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SPOT_VESSEL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int SPOT_VESSEL_VALUE = 1;

	/**
	 * The '<em><b>DES PURCHASE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>DES PURCHASE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DES_PURCHASE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int DES_PURCHASE_VALUE = 2;

	/**
	 * The '<em><b>FOB SALE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>FOB SALE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #FOB_SALE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int FOB_SALE_VALUE = 3;

	/**
	 * The '<em><b>ROUND TRIP</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ROUND TRIP</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ROUND_TRIP
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int ROUND_TRIP_VALUE = 4;

	/**
	 * An array of all the '<em><b>Sequence Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final SequenceType[] VALUES_ARRAY =
		new SequenceType[] {
			VESSEL,
			SPOT_VESSEL,
			DES_PURCHASE,
			FOB_SALE,
			ROUND_TRIP,
		};

	/**
	 * A public read-only list of all the '<em><b>Sequence Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<SequenceType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Sequence Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static SequenceType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			SequenceType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Sequence Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static SequenceType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			SequenceType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Sequence Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static SequenceType get(int value) {
		switch (value) {
			case VESSEL_VALUE: return VESSEL;
			case SPOT_VESSEL_VALUE: return SPOT_VESSEL;
			case DES_PURCHASE_VALUE: return DES_PURCHASE;
			case FOB_SALE_VALUE: return FOB_SALE;
			case ROUND_TRIP_VALUE: return ROUND_TRIP;
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
	private SequenceType(int value, String name, String literal) {
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
	
} //SequenceType

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Slot Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSlotType()
 * @model
 * @generated
 */
public enum SlotType implements Enumerator {
	/**
	 * The '<em><b>FOB PURCHASE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #FOB_PURCHASE_VALUE
	 * @generated
	 * @ordered
	 */
	FOB_PURCHASE(0, "FOB_PURCHASE", "FOB_PURCHASE"),

	/**
	 * The '<em><b>FOB SALE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #FOB_SALE_VALUE
	 * @generated
	 * @ordered
	 */
	FOB_SALE(1, "FOB_SALE", "FOB_SALE"),

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
	 * The '<em><b>DES SALE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DES_SALE_VALUE
	 * @generated
	 * @ordered
	 */
	DES_SALE(3, "DES_SALE", "DES_SALE");

	/**
	 * The '<em><b>FOB PURCHASE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>FOB PURCHASE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #FOB_PURCHASE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int FOB_PURCHASE_VALUE = 0;

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
	public static final int FOB_SALE_VALUE = 1;

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
	 * The '<em><b>DES SALE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>DES SALE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DES_SALE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int DES_SALE_VALUE = 3;

	/**
	 * An array of all the '<em><b>Slot Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final SlotType[] VALUES_ARRAY =
		new SlotType[] {
			FOB_PURCHASE,
			FOB_SALE,
			DES_PURCHASE,
			DES_SALE,
		};

	/**
	 * A public read-only list of all the '<em><b>Slot Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<SlotType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Slot Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static SlotType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			SlotType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Slot Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static SlotType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			SlotType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Slot Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static SlotType get(int value) {
		switch (value) {
			case FOB_PURCHASE_VALUE: return FOB_PURCHASE;
			case FOB_SALE_VALUE: return FOB_SALE;
			case DES_PURCHASE_VALUE: return DES_PURCHASE;
			case DES_SALE_VALUE: return DES_SALE;
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
	private SlotType(int value, String name, String literal) {
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
	
} //SlotType

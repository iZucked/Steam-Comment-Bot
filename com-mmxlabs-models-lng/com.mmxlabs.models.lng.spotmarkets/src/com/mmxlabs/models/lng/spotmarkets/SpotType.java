/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Spot Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getSpotType()
 * @model
 * @generated
 */
public enum SpotType implements Enumerator {
	/**
	 * The '<em><b>FOB Sale</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #FOB_SALE_VALUE
	 * @generated
	 * @ordered
	 */
	FOB_SALE(0, "FOBSale", "FOB Sale"),

	/**
	 * The '<em><b>DES Purchase</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DES_PURCHASE_VALUE
	 * @generated
	 * @ordered
	 */
	DES_PURCHASE(1, "DESPurchase", "DES Purchase"),

	/**
	 * The '<em><b>DES Sale</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DES_SALE_VALUE
	 * @generated
	 * @ordered
	 */
	DES_SALE(2, "DESSale", "DES Sale"),

	/**
	 * The '<em><b>FOB Purchase</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #FOB_PURCHASE_VALUE
	 * @generated
	 * @ordered
	 */
	FOB_PURCHASE(3, "FOBPurchase", "FOB Purchase");

	/**
	 * The '<em><b>FOB Sale</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>FOB Sale</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #FOB_SALE
	 * @model name="FOBSale" literal="FOB Sale"
	 * @generated
	 * @ordered
	 */
	public static final int FOB_SALE_VALUE = 0;

	/**
	 * The '<em><b>DES Purchase</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>DES Purchase</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DES_PURCHASE
	 * @model name="DESPurchase" literal="DES Purchase"
	 * @generated
	 * @ordered
	 */
	public static final int DES_PURCHASE_VALUE = 1;

	/**
	 * The '<em><b>DES Sale</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>DES Sale</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DES_SALE
	 * @model name="DESSale" literal="DES Sale"
	 * @generated
	 * @ordered
	 */
	public static final int DES_SALE_VALUE = 2;

	/**
	 * The '<em><b>FOB Purchase</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>FOB Purchase</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #FOB_PURCHASE
	 * @model name="FOBPurchase" literal="FOB Purchase"
	 * @generated
	 * @ordered
	 */
	public static final int FOB_PURCHASE_VALUE = 3;

	/**
	 * An array of all the '<em><b>Spot Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final SpotType[] VALUES_ARRAY =
		new SpotType[] {
			FOB_SALE,
			DES_PURCHASE,
			DES_SALE,
			FOB_PURCHASE,
		};

	/**
	 * A public read-only list of all the '<em><b>Spot Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<SpotType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Spot Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static SpotType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			SpotType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Spot Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static SpotType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			SpotType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Spot Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static SpotType get(int value) {
		switch (value) {
			case FOB_SALE_VALUE: return FOB_SALE;
			case DES_PURCHASE_VALUE: return DES_PURCHASE;
			case DES_SALE_VALUE: return DES_SALE;
			case FOB_PURCHASE_VALUE: return FOB_PURCHASE;
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
	private SpotType(int value, String name, String literal) {
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
	
} //SpotType

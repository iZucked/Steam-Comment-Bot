/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.types;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>DES Purchase Deal Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.types.TypesPackage#getDESPurchaseDealType()
 * @model
 * @generated
 */
public enum DESPurchaseDealType implements Enumerator {
	/**
	 * The '<em><b>DEST ONLY</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DEST_ONLY_VALUE
	 * @generated
	 * @ordered
	 */
	DEST_ONLY(0, "DEST_ONLY", "DEST_ONLY"),

	/**
	 * The '<em><b>DIVERTIBLE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	* <!-- end-user-doc -->
	 * @see #DIVERTIBLE_VALUE
	 * @generated
	 * @ordered
	 */
	DIVERTIBLE(3, "DIVERTIBLE", "DIVERTIBLE"),
	/**
	* The '<em><b>DEST WITH SOURCE</b></em>' literal object.
	* <!-- begin-user-doc -->
	* <!-- end-user-doc -->
	* @see #DEST_WITH_SOURCE_VALUE
	* @generated
	* @ordered
	*/
	DEST_WITH_SOURCE(1, "DEST_WITH_SOURCE", "DEST_WITH_SOURCE"),
	/**
	* The '<em><b>DIVERT FROM SOURCE</b></em>' literal object.
	* <!-- begin-user-doc -->
	* <!-- end-user-doc -->
	* @see #DIVERT_FROM_SOURCE_VALUE
	* @generated
	* @ordered
	*/
	DIVERT_FROM_SOURCE(2, "DIVERT_FROM_SOURCE", "DIVERT_FROM_SOURCE");

	/**
	 * The '<em><b>DEST ONLY</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>DEST ONLY</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DEST_ONLY
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int DEST_ONLY_VALUE = 0;

	/**
	 * The '<em><b>DIVERTIBLE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DIVERTIBLE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int DIVERTIBLE_VALUE = 3;

	/**
	 * The '<em><b>DEST WITH SOURCE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>DEST WITH SOURCE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DEST_WITH_SOURCE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int DEST_WITH_SOURCE_VALUE = 1;

	/**
	 * The '<em><b>DIVERT FROM SOURCE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>DIVERT FROM SOURCE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DIVERT_FROM_SOURCE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int DIVERT_FROM_SOURCE_VALUE = 2;

	/**
	 * An array of all the '<em><b>DES Purchase Deal Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final DESPurchaseDealType[] VALUES_ARRAY = new DESPurchaseDealType[] { DEST_ONLY, DIVERTIBLE, DEST_WITH_SOURCE, DIVERT_FROM_SOURCE, };

	/**
	 * A public read-only list of all the '<em><b>DES Purchase Deal Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<DESPurchaseDealType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>DES Purchase Deal Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static DESPurchaseDealType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			DESPurchaseDealType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>DES Purchase Deal Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static DESPurchaseDealType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			DESPurchaseDealType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>DES Purchase Deal Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static DESPurchaseDealType get(int value) {
		switch (value) {
		case DEST_ONLY_VALUE:
			return DEST_ONLY;
		case DIVERTIBLE_VALUE:
			return DIVERTIBLE;
		case DEST_WITH_SOURCE_VALUE:
			return DEST_WITH_SOURCE;
		case DIVERT_FROM_SOURCE_VALUE:
			return DIVERT_FROM_SOURCE;
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
	private DESPurchaseDealType(int value, String name, String literal) {
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

} //DESPurchaseDealType

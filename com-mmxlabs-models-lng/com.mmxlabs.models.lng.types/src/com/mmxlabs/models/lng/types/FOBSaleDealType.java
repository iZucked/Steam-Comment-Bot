/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.types;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>FOB Sale Deal Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.types.TypesPackage#getFOBSaleDealType()
 * @model
 * @generated
 */
public enum FOBSaleDealType implements Enumerator {
	/**
	 * The '<em><b>SOURCE ONLY</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #SOURCE_ONLY_VALUE
	 * @generated
	 * @ordered
	 */
	SOURCE_ONLY(0, "SOURCE_ONLY", "SOURCE_ONLY"),

	/**
	 * The '<em><b>SOURCE WITH DEST</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #SOURCE_WITH_DEST_VALUE
	 * @generated
	 * @ordered
	 */
	SOURCE_WITH_DEST(1, "SOURCE_WITH_DEST", "SOURCE_WITH_DEST"),

	/**
	 * The '<em><b>DIVERT TO DEST</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DIVERT_TO_DEST_VALUE
	 * @generated
	 * @ordered
	 */
	DIVERT_TO_DEST(2, "DIVERT_TO_DEST", "DIVERT_TO_DEST");

	/**
	 * The '<em><b>SOURCE ONLY</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>SOURCE ONLY</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SOURCE_ONLY
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int SOURCE_ONLY_VALUE = 0;

	/**
	 * The '<em><b>SOURCE WITH DEST</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>SOURCE WITH DEST</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SOURCE_WITH_DEST
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int SOURCE_WITH_DEST_VALUE = 1;

	/**
	 * The '<em><b>DIVERT TO DEST</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>DIVERT TO DEST</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DIVERT_TO_DEST
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int DIVERT_TO_DEST_VALUE = 2;

	/**
	 * An array of all the '<em><b>FOB Sale Deal Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final FOBSaleDealType[] VALUES_ARRAY = new FOBSaleDealType[] { SOURCE_ONLY, SOURCE_WITH_DEST, DIVERT_TO_DEST, };

	/**
	 * A public read-only list of all the '<em><b>FOB Sale Deal Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<FOBSaleDealType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>FOB Sale Deal Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static FOBSaleDealType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			FOBSaleDealType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>FOB Sale Deal Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static FOBSaleDealType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			FOBSaleDealType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>FOB Sale Deal Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static FOBSaleDealType get(int value) {
		switch (value) {
		case SOURCE_ONLY_VALUE:
			return SOURCE_ONLY;
		case SOURCE_WITH_DEST_VALUE:
			return SOURCE_WITH_DEST;
		case DIVERT_TO_DEST_VALUE:
			return DIVERT_TO_DEST;
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
	private FOBSaleDealType(int value, String name, String literal) {
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

} //FOBSaleDealType

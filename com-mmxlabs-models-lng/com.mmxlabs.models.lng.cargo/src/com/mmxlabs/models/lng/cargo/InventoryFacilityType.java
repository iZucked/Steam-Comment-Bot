/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Inventory Facility Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventoryFacilityType()
 * @model
 * @generated
 */
public enum InventoryFacilityType implements Enumerator {
	/**
	 * The '<em><b>Upstream</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #UPSTREAM_VALUE
	 * @generated
	 * @ordered
	 */
	UPSTREAM(0, "Upstream", "Upstream"),

	/**
	 * The '<em><b>Hub</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #HUB_VALUE
	 * @generated
	 * @ordered
	 */
	HUB(1, "Hub", "Hub"),

	/**
	 * The '<em><b>Downstream</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DOWNSTREAM_VALUE
	 * @generated
	 * @ordered
	 */
	DOWNSTREAM(2, "Downstream", "Downstream");

	/**
	 * The '<em><b>Upstream</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Upstream</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #UPSTREAM
	 * @model name="Upstream"
	 * @generated
	 * @ordered
	 */
	public static final int UPSTREAM_VALUE = 0;

	/**
	 * The '<em><b>Hub</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Hub</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #HUB
	 * @model name="Hub"
	 * @generated
	 * @ordered
	 */
	public static final int HUB_VALUE = 1;

	/**
	 * The '<em><b>Downstream</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Downstream</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DOWNSTREAM
	 * @model name="Downstream"
	 * @generated
	 * @ordered
	 */
	public static final int DOWNSTREAM_VALUE = 2;

	/**
	 * An array of all the '<em><b>Inventory Facility Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final InventoryFacilityType[] VALUES_ARRAY =
		new InventoryFacilityType[] {
			UPSTREAM,
			HUB,
			DOWNSTREAM,
		};

	/**
	 * A public read-only list of all the '<em><b>Inventory Facility Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<InventoryFacilityType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Inventory Facility Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static InventoryFacilityType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			InventoryFacilityType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Inventory Facility Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static InventoryFacilityType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			InventoryFacilityType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Inventory Facility Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static InventoryFacilityType get(int value) {
		switch (value) {
			case UPSTREAM_VALUE: return UPSTREAM;
			case HUB_VALUE: return HUB;
			case DOWNSTREAM_VALUE: return DOWNSTREAM;
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
	private InventoryFacilityType(int value, String name, String literal) {
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
	
} //InventoryFacilityType

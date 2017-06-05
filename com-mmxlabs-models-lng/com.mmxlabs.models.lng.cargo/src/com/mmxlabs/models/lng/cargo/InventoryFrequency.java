/**
 */
package com.mmxlabs.models.lng.cargo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Inventory Frequency</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventoryFrequency()
 * @model
 * @generated
 */
public enum InventoryFrequency implements Enumerator {
	/**
	 * The '<em><b>Cargo</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CARGO_VALUE
	 * @generated
	 * @ordered
	 */
	CARGO(0, "Cargo", "Cargo"),

	/**
	 * The '<em><b>Hourly</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #HOURLY_VALUE
	 * @generated
	 * @ordered
	 */
	HOURLY(1, "Hourly", "Hourly"),

	/**
	 * The '<em><b>Daily</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DAILY_VALUE
	 * @generated
	 * @ordered
	 */
	DAILY(2, "Daily", "Daily"),

	/**
	 * The '<em><b>Monthly</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MONTHLY_VALUE
	 * @generated
	 * @ordered
	 */
	MONTHLY(3, "Monthly", "Monthly"), /**
	 * The '<em><b>Level</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #LEVEL_VALUE
	 * @generated
	 * @ordered
	 */
	LEVEL(4, "Level", "Level");

	/**
	 * The '<em><b>Cargo</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Cargo</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CARGO
	 * @model name="Cargo"
	 * @generated
	 * @ordered
	 */
	public static final int CARGO_VALUE = 0;

	/**
	 * The '<em><b>Hourly</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Hourly</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #HOURLY
	 * @model name="Hourly"
	 * @generated
	 * @ordered
	 */
	public static final int HOURLY_VALUE = 1;

	/**
	 * The '<em><b>Daily</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Daily</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DAILY
	 * @model name="Daily"
	 * @generated
	 * @ordered
	 */
	public static final int DAILY_VALUE = 2;

	/**
	 * The '<em><b>Monthly</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Monthly</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MONTHLY
	 * @model name="Monthly"
	 * @generated
	 * @ordered
	 */
	public static final int MONTHLY_VALUE = 3;

	/**
	 * The '<em><b>Level</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Level</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #LEVEL
	 * @model name="Level"
	 * @generated
	 * @ordered
	 */
	public static final int LEVEL_VALUE = 4;

	/**
	 * An array of all the '<em><b>Inventory Frequency</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final InventoryFrequency[] VALUES_ARRAY =
		new InventoryFrequency[] {
			CARGO,
			HOURLY,
			DAILY,
			MONTHLY,
			LEVEL,
		};

	/**
	 * A public read-only list of all the '<em><b>Inventory Frequency</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<InventoryFrequency> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Inventory Frequency</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static InventoryFrequency get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			InventoryFrequency result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Inventory Frequency</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static InventoryFrequency getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			InventoryFrequency result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Inventory Frequency</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static InventoryFrequency get(int value) {
		switch (value) {
			case CARGO_VALUE: return CARGO;
			case HOURLY_VALUE: return HOURLY;
			case DAILY_VALUE: return DAILY;
			case MONTHLY_VALUE: return MONTHLY;
			case LEVEL_VALUE: return LEVEL;
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
	private InventoryFrequency(int value, String name, String literal) {
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
	
} //InventoryFrequency

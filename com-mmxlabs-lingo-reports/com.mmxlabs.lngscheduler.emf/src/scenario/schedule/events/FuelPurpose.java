/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.schedule.events;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Fuel Purpose</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see scenario.schedule.events.EventsPackage#getFuelPurpose()
 * @model
 * @generated
 */
public enum FuelPurpose implements Enumerator {
	/**
	 * The '<em><b>Travel</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #TRAVEL_VALUE
	 * @generated
	 * @ordered
	 */
	TRAVEL(0, "Travel", "Travel"),

	/**
	 * The '<em><b>Pilot Light</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #PILOT_LIGHT_VALUE
	 * @generated
	 * @ordered
	 */
	PILOT_LIGHT(1, "PilotLight", "PilotLight"),

	/**
	 * The '<em><b>Idle</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #IDLE_VALUE
	 * @generated
	 * @ordered
	 */
	IDLE(2, "Idle", "Idle"),

	/**
	 * The '<em><b>Cooldown</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #COOLDOWN_VALUE
	 * @generated
	 * @ordered
	 */
	COOLDOWN(3, "Cooldown", "Cooldown");

	/**
	 * The '<em><b>Travel</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Travel</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #TRAVEL
	 * @model name="Travel"
	 * @generated
	 * @ordered
	 */
	public static final int TRAVEL_VALUE = 0;

	/**
	 * The '<em><b>Pilot Light</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Pilot Light</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #PILOT_LIGHT
	 * @model name="PilotLight"
	 * @generated
	 * @ordered
	 */
	public static final int PILOT_LIGHT_VALUE = 1;

	/**
	 * The '<em><b>Idle</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Idle</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #IDLE
	 * @model name="Idle"
	 * @generated
	 * @ordered
	 */
	public static final int IDLE_VALUE = 2;

	/**
	 * The '<em><b>Cooldown</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Cooldown</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #COOLDOWN
	 * @model name="Cooldown"
	 * @generated
	 * @ordered
	 */
	public static final int COOLDOWN_VALUE = 3;

	/**
	 * An array of all the '<em><b>Fuel Purpose</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final FuelPurpose[] VALUES_ARRAY =
		new FuelPurpose[] {
			TRAVEL,
			PILOT_LIGHT,
			IDLE,
			COOLDOWN,
		};

	/**
	 * A public read-only list of all the '<em><b>Fuel Purpose</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<FuelPurpose> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Fuel Purpose</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static FuelPurpose get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			FuelPurpose result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Fuel Purpose</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static FuelPurpose getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			FuelPurpose result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Fuel Purpose</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static FuelPurpose get(int value) {
		switch (value) {
			case TRAVEL_VALUE: return TRAVEL;
			case PILOT_LIGHT_VALUE: return PILOT_LIGHT;
			case IDLE_VALUE: return IDLE;
			case COOLDOWN_VALUE: return COOLDOWN;
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
	private FuelPurpose(int value, String name, String literal) {
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
	
} //FuelPurpose

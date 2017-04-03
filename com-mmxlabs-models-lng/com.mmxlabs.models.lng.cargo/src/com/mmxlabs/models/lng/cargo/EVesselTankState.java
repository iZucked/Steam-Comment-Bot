/**
 */
package com.mmxlabs.models.lng.cargo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>EVessel Tank State</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getEVesselTankState()
 * @model
 * @generated
 */
public enum EVesselTankState implements Enumerator {
	/**
	 * The '<em><b>EITHER</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #EITHER_VALUE
	 * @generated
	 * @ordered
	 */
	EITHER(0, "EITHER", "EITHER"),

	/**
	 * The '<em><b>MUST BE COLD</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MUST_BE_COLD_VALUE
	 * @generated
	 * @ordered
	 */
	MUST_BE_COLD(1, "MUST_BE_COLD", "MUST_BE_COLD"),

	/**
	 * The '<em><b>MUST BE WARM</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MUST_BE_WARM_VALUE
	 * @generated
	 * @ordered
	 */
	MUST_BE_WARM(2, "MUST_BE_WARM", "MUST_BE_WARM");

	/**
	 * The '<em><b>EITHER</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>EITHER</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #EITHER
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int EITHER_VALUE = 0;

	/**
	 * The '<em><b>MUST BE COLD</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>MUST BE COLD</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MUST_BE_COLD
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int MUST_BE_COLD_VALUE = 1;

	/**
	 * The '<em><b>MUST BE WARM</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>MUST BE WARM</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MUST_BE_WARM
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int MUST_BE_WARM_VALUE = 2;

	/**
	 * An array of all the '<em><b>EVessel Tank State</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final EVesselTankState[] VALUES_ARRAY =
		new EVesselTankState[] {
			EITHER,
			MUST_BE_COLD,
			MUST_BE_WARM,
		};

	/**
	 * A public read-only list of all the '<em><b>EVessel Tank State</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<EVesselTankState> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>EVessel Tank State</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static EVesselTankState get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			EVesselTankState result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>EVessel Tank State</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static EVesselTankState getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			EVesselTankState result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>EVessel Tank State</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static EVesselTankState get(int value) {
		switch (value) {
			case EITHER_VALUE: return EITHER;
			case MUST_BE_COLD_VALUE: return MUST_BE_COLD;
			case MUST_BE_WARM_VALUE: return MUST_BE_WARM;
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
	private EVesselTankState(int value, String name, String literal) {
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
	
} //EVesselTankState

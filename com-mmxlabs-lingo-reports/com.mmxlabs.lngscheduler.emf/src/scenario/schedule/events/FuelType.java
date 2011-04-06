/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule.events;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Fuel Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see scenario.schedule.events.EventsPackage#getFuelType()
 * @model
 * @generated
 */
public enum FuelType implements Enumerator {
	/**
	 * The '<em><b>FBO</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #FBO_VALUE
	 * @generated
	 * @ordered
	 */
	FBO(0, "FBO", "FBO"),

	/**
	 * The '<em><b>NBO</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #NBO_VALUE
	 * @generated
	 * @ordered
	 */
	NBO(1, "NBO", "NBO"),

	/**
	 * The '<em><b>Base Fuel</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #BASE_FUEL_VALUE
	 * @generated
	 * @ordered
	 */
	BASE_FUEL(2, "BaseFuel", "BaseFuel");

	/**
	 * The '<em><b>FBO</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>FBO</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #FBO
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int FBO_VALUE = 0;

	/**
	 * The '<em><b>NBO</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>NBO</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #NBO
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int NBO_VALUE = 1;

	/**
	 * The '<em><b>Base Fuel</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Base Fuel</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #BASE_FUEL
	 * @model name="BaseFuel"
	 * @generated
	 * @ordered
	 */
	public static final int BASE_FUEL_VALUE = 2;

	/**
	 * An array of all the '<em><b>Fuel Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final FuelType[] VALUES_ARRAY =
		new FuelType[] {
			FBO,
			NBO,
			BASE_FUEL,
		};

	/**
	 * A public read-only list of all the '<em><b>Fuel Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<FuelType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Fuel Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static FuelType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			FuelType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Fuel Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static FuelType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			FuelType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Fuel Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static FuelType get(int value) {
		switch (value) {
			case FBO_VALUE: return FBO;
			case NBO_VALUE: return NBO;
			case BASE_FUEL_VALUE: return BASE_FUEL;
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
	private FuelType(int value, String name, String literal) {
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
	
} //FuelType

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
 * A representation of the literals of the enumeration '<em><b>Fuel Unit</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see scenario.schedule.events.EventsPackage#getFuelUnit()
 * @model
 * @generated
 */
public enum FuelUnit implements Enumerator {
	/**
	 * The '<em><b>MT</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MT_VALUE
	 * @generated
	 * @ordered
	 */
	MT(0, "MT", "MT"),

	/**
	 * The '<em><b>M3</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #M3_VALUE
	 * @generated
	 * @ordered
	 */
	M3(1, "M3", "M3"),

	/**
	 * The '<em><b>MMB Tu</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MMB_TU_VALUE
	 * @generated
	 * @ordered
	 */
	MMB_TU(2, "MMBTu", "MMBTu");

	/**
	 * The '<em><b>MT</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>MT</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MT
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int MT_VALUE = 0;

	/**
	 * The '<em><b>M3</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>M3</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #M3
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int M3_VALUE = 1;

	/**
	 * The '<em><b>MMB Tu</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>MMB Tu</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MMB_TU
	 * @model name="MMBTu"
	 * @generated
	 * @ordered
	 */
	public static final int MMB_TU_VALUE = 2;

	/**
	 * An array of all the '<em><b>Fuel Unit</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final FuelUnit[] VALUES_ARRAY =
		new FuelUnit[] {
			MT,
			M3,
			MMB_TU,
		};

	/**
	 * A public read-only list of all the '<em><b>Fuel Unit</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<FuelUnit> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Fuel Unit</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static FuelUnit get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			FuelUnit result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Fuel Unit</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static FuelUnit getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			FuelUnit result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Fuel Unit</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static FuelUnit get(int value) {
		switch (value) {
			case MT_VALUE: return MT;
			case M3_VALUE: return M3;
			case MMB_TU_VALUE: return MMB_TU;
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
	private FuelUnit(int value, String name, String literal) {
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
	
} //FuelUnit

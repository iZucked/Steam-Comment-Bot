/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo;
import com.mmxlabs.models.lng.types.ACargo;
import com.mmxlabs.models.lng.types.AVesselSet;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cargo</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Cargo#getLoadSlot <em>Load Slot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Cargo#getDischargeSlot <em>Discharge Slot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Cargo#isAllowRewiring <em>Allow Rewiring</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.Cargo#getAllowedVessels <em>Allowed Vessels</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCargo()
 * @model
 * @generated
 */
public interface Cargo extends ACargo {
	/**
	 * Returns the value of the '<em><b>Load Slot</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.lng.cargo.LoadSlot#getCargo <em>Cargo</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Load Slot</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Slot</em>' reference.
	 * @see #setLoadSlot(LoadSlot)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCargo_LoadSlot()
	 * @see com.mmxlabs.models.lng.cargo.LoadSlot#getCargo
	 * @model opposite="cargo" required="true"
	 * @generated
	 */
	LoadSlot getLoadSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Cargo#getLoadSlot <em>Load Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Load Slot</em>' reference.
	 * @see #getLoadSlot()
	 * @generated
	 */
	void setLoadSlot(LoadSlot value);

	/**
	 * Returns the value of the '<em><b>Discharge Slot</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getCargo <em>Cargo</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Discharge Slot</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge Slot</em>' reference.
	 * @see #setDischargeSlot(DischargeSlot)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCargo_DischargeSlot()
	 * @see com.mmxlabs.models.lng.cargo.DischargeSlot#getCargo
	 * @model opposite="cargo" required="true"
	 * @generated
	 */
	DischargeSlot getDischargeSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Cargo#getDischargeSlot <em>Discharge Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discharge Slot</em>' reference.
	 * @see #getDischargeSlot()
	 * @generated
	 */
	void setDischargeSlot(DischargeSlot value);

	/**
	 * Returns the value of the '<em><b>Allow Rewiring</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Allow Rewiring</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Allow Rewiring</em>' attribute.
	 * @see #isSetAllowRewiring()
	 * @see #unsetAllowRewiring()
	 * @see #setAllowRewiring(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCargo_AllowRewiring()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	boolean isAllowRewiring();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.Cargo#isAllowRewiring <em>Allow Rewiring</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Allow Rewiring</em>' attribute.
	 * @see #isSetAllowRewiring()
	 * @see #unsetAllowRewiring()
	 * @see #isAllowRewiring()
	 * @generated
	 */
	void setAllowRewiring(boolean value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.Cargo#isAllowRewiring <em>Allow Rewiring</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetAllowRewiring()
	 * @see #isAllowRewiring()
	 * @see #setAllowRewiring(boolean)
	 * @generated
	 */
	void unsetAllowRewiring();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.Cargo#isAllowRewiring <em>Allow Rewiring</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Allow Rewiring</em>' attribute is set.
	 * @see #unsetAllowRewiring()
	 * @see #isAllowRewiring()
	 * @see #setAllowRewiring(boolean)
	 * @generated
	 */
	boolean isSetAllowRewiring();

	/**
	 * Returns the value of the '<em><b>Allowed Vessels</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.AVesselSet}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Allowed Vessels</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Allowed Vessels</em>' reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCargo_AllowedVessels()
	 * @model
	 * @generated
	 */
	EList<AVesselSet> getAllowedVessels();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	CargoType getCargoType();

} // end of  Cargo

// finish type fixing

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.contract;

import java.util.Date;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import scenario.port.Port;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Total Volume Limit</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link scenario.contract.TotalVolumeLimit#getPorts <em>Ports</em>}</li>
 * <li>{@link scenario.contract.TotalVolumeLimit#getMaximumVolume <em>Maximum Volume</em>}</li>
 * <li>{@link scenario.contract.TotalVolumeLimit#getStartDate <em>Start Date</em>}</li>
 * <li>{@link scenario.contract.TotalVolumeLimit#getDuration <em>Duration</em>}</li>
 * <li>{@link scenario.contract.TotalVolumeLimit#isRepeating <em>Repeating</em>}</li>
 * </ul>
 * </p>
 * 
 * @see scenario.contract.ContractPackage#getTotalVolumeLimit()
 * @model
 * @generated
 */
public interface TotalVolumeLimit extends EObject {
	/**
	 * Returns the value of the '<em><b>Ports</b></em>' reference list. The list contents are of type {@link scenario.port.Port}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ports</em>' reference list isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Ports</em>' reference list.
	 * @see scenario.contract.ContractPackage#getTotalVolumeLimit_Ports()
	 * @model
	 * @generated
	 */
	EList<Port> getPorts();

	/**
	 * Returns the value of the '<em><b>Maximum Volume</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Maximum Volume</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Maximum Volume</em>' attribute.
	 * @see #setMaximumVolume(long)
	 * @see scenario.contract.ContractPackage#getTotalVolumeLimit_MaximumVolume()
	 * @model
	 * @generated
	 */
	long getMaximumVolume();

	/**
	 * Sets the value of the '{@link scenario.contract.TotalVolumeLimit#getMaximumVolume <em>Maximum Volume</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Maximum Volume</em>' attribute.
	 * @see #getMaximumVolume()
	 * @generated
	 */
	void setMaximumVolume(long value);

	/**
	 * Returns the value of the '<em><b>Start Date</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Date</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Start Date</em>' attribute.
	 * @see #setStartDate(Date)
	 * @see scenario.contract.ContractPackage#getTotalVolumeLimit_StartDate()
	 * @model required="true"
	 * @generated
	 */
	Date getStartDate();

	/**
	 * Sets the value of the '{@link scenario.contract.TotalVolumeLimit#getStartDate <em>Start Date</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Start Date</em>' attribute.
	 * @see #getStartDate()
	 * @generated
	 */
	void setStartDate(Date value);

	/**
	 * Returns the value of the '<em><b>Duration</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Duration</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Duration</em>' attribute.
	 * @see #setDuration(int)
	 * @see scenario.contract.ContractPackage#getTotalVolumeLimit_Duration()
	 * @model required="true"
	 * @generated
	 */
	int getDuration();

	/**
	 * Sets the value of the '{@link scenario.contract.TotalVolumeLimit#getDuration <em>Duration</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Duration</em>' attribute.
	 * @see #getDuration()
	 * @generated
	 */
	void setDuration(int value);

	/**
	 * Returns the value of the '<em><b>Repeating</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Repeating</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Repeating</em>' attribute.
	 * @see #setRepeating(boolean)
	 * @see scenario.contract.ContractPackage#getTotalVolumeLimit_Repeating()
	 * @model required="true"
	 * @generated
	 */
	boolean isRepeating();

	/**
	 * Sets the value of the '{@link scenario.contract.TotalVolumeLimit#isRepeating <em>Repeating</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Repeating</em>' attribute.
	 * @see #isRepeating()
	 * @generated
	 */
	void setRepeating(boolean value);

} // TotalVolumeLimit

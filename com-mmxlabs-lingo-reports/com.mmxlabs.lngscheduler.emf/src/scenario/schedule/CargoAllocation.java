/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule;

import java.util.Date;

import org.eclipse.emf.ecore.EObject;

import scenario.cargo.LoadSlot;
import scenario.cargo.Slot;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cargo Allocation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.schedule.CargoAllocation#getLoadSlot <em>Load Slot</em>}</li>
 *   <li>{@link scenario.schedule.CargoAllocation#getDischargeSlot <em>Discharge Slot</em>}</li>
 *   <li>{@link scenario.schedule.CargoAllocation#getFuelVolume <em>Fuel Volume</em>}</li>
 *   <li>{@link scenario.schedule.CargoAllocation#getDischargeVolume <em>Discharge Volume</em>}</li>
 *   <li>{@link scenario.schedule.CargoAllocation#getLoadDate <em>Load Date</em>}</li>
 *   <li>{@link scenario.schedule.CargoAllocation#getDischargeDate <em>Discharge Date</em>}</li>
 *   <li>{@link scenario.schedule.CargoAllocation#getLoadPriceM3 <em>Load Price M3</em>}</li>
 *   <li>{@link scenario.schedule.CargoAllocation#getDischargePriceM3 <em>Discharge Price M3</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.schedule.SchedulePackage#getCargoAllocation()
 * @model
 * @generated
 */
public interface CargoAllocation extends EObject {
	/**
	 * Returns the value of the '<em><b>Load Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Load Slot</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Slot</em>' reference.
	 * @see #setLoadSlot(LoadSlot)
	 * @see scenario.schedule.SchedulePackage#getCargoAllocation_LoadSlot()
	 * @model required="true"
	 * @generated
	 */
	LoadSlot getLoadSlot();

	/**
	 * Sets the value of the '{@link scenario.schedule.CargoAllocation#getLoadSlot <em>Load Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Load Slot</em>' reference.
	 * @see #getLoadSlot()
	 * @generated
	 */
	void setLoadSlot(LoadSlot value);

	/**
	 * Returns the value of the '<em><b>Discharge Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Discharge Slot</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge Slot</em>' reference.
	 * @see #setDischargeSlot(Slot)
	 * @see scenario.schedule.SchedulePackage#getCargoAllocation_DischargeSlot()
	 * @model required="true"
	 * @generated
	 */
	Slot getDischargeSlot();

	/**
	 * Sets the value of the '{@link scenario.schedule.CargoAllocation#getDischargeSlot <em>Discharge Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discharge Slot</em>' reference.
	 * @see #getDischargeSlot()
	 * @generated
	 */
	void setDischargeSlot(Slot value);

	/**
	 * Returns the value of the '<em><b>Fuel Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fuel Volume</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fuel Volume</em>' attribute.
	 * @see #setFuelVolume(long)
	 * @see scenario.schedule.SchedulePackage#getCargoAllocation_FuelVolume()
	 * @model required="true"
	 * @generated
	 */
	long getFuelVolume();

	/**
	 * Sets the value of the '{@link scenario.schedule.CargoAllocation#getFuelVolume <em>Fuel Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fuel Volume</em>' attribute.
	 * @see #getFuelVolume()
	 * @generated
	 */
	void setFuelVolume(long value);

	/**
	 * Returns the value of the '<em><b>Discharge Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Discharge Volume</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge Volume</em>' attribute.
	 * @see #setDischargeVolume(long)
	 * @see scenario.schedule.SchedulePackage#getCargoAllocation_DischargeVolume()
	 * @model required="true"
	 * @generated
	 */
	long getDischargeVolume();

	/**
	 * Sets the value of the '{@link scenario.schedule.CargoAllocation#getDischargeVolume <em>Discharge Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discharge Volume</em>' attribute.
	 * @see #getDischargeVolume()
	 * @generated
	 */
	void setDischargeVolume(long value);

	/**
	 * Returns the value of the '<em><b>Load Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Load Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Date</em>' attribute.
	 * @see #setLoadDate(Date)
	 * @see scenario.schedule.SchedulePackage#getCargoAllocation_LoadDate()
	 * @model required="true"
	 * @generated
	 */
	Date getLoadDate();

	/**
	 * Sets the value of the '{@link scenario.schedule.CargoAllocation#getLoadDate <em>Load Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Load Date</em>' attribute.
	 * @see #getLoadDate()
	 * @generated
	 */
	void setLoadDate(Date value);

	/**
	 * Returns the value of the '<em><b>Discharge Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Discharge Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge Date</em>' attribute.
	 * @see #setDischargeDate(Date)
	 * @see scenario.schedule.SchedulePackage#getCargoAllocation_DischargeDate()
	 * @model required="true"
	 * @generated
	 */
	Date getDischargeDate();

	/**
	 * Sets the value of the '{@link scenario.schedule.CargoAllocation#getDischargeDate <em>Discharge Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discharge Date</em>' attribute.
	 * @see #getDischargeDate()
	 * @generated
	 */
	void setDischargeDate(Date value);

	/**
	 * Returns the value of the '<em><b>Load Price M3</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Load Price M3</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Price M3</em>' attribute.
	 * @see #setLoadPriceM3(int)
	 * @see scenario.schedule.SchedulePackage#getCargoAllocation_LoadPriceM3()
	 * @model required="true"
	 * @generated
	 */
	int getLoadPriceM3();

	/**
	 * Sets the value of the '{@link scenario.schedule.CargoAllocation#getLoadPriceM3 <em>Load Price M3</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Load Price M3</em>' attribute.
	 * @see #getLoadPriceM3()
	 * @generated
	 */
	void setLoadPriceM3(int value);

	/**
	 * Returns the value of the '<em><b>Discharge Price M3</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Discharge Price M3</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge Price M3</em>' attribute.
	 * @see #setDischargePriceM3(int)
	 * @see scenario.schedule.SchedulePackage#getCargoAllocation_DischargePriceM3()
	 * @model required="true"
	 * @generated
	 */
	int getDischargePriceM3();

	/**
	 * Sets the value of the '{@link scenario.schedule.CargoAllocation#getDischargePriceM3 <em>Discharge Price M3</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discharge Price M3</em>' attribute.
	 * @see #getDischargePriceM3()
	 * @generated
	 */
	void setDischargePriceM3(int value);

} // CargoAllocation

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule;

import java.util.Date;

import org.eclipse.emf.ecore.EObject;

import scenario.cargo.CargoType;
import scenario.cargo.LoadSlot;
import scenario.cargo.Slot;
import scenario.schedule.events.Idle;
import scenario.schedule.events.Journey;
import scenario.schedule.fleetallocation.AllocatedVessel;

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
 *   <li>{@link scenario.schedule.CargoAllocation#getVessel <em>Vessel</em>}</li>
 *   <li>{@link scenario.schedule.CargoAllocation#getLadenLeg <em>Laden Leg</em>}</li>
 *   <li>{@link scenario.schedule.CargoAllocation#getBallastLeg <em>Ballast Leg</em>}</li>
 *   <li>{@link scenario.schedule.CargoAllocation#getLadenIdle <em>Laden Idle</em>}</li>
 *   <li>{@link scenario.schedule.CargoAllocation#getBallastIdle <em>Ballast Idle</em>}</li>
 *   <li>{@link scenario.schedule.CargoAllocation#getLoadRevenue <em>Load Revenue</em>}</li>
 *   <li>{@link scenario.schedule.CargoAllocation#getShippingRevenue <em>Shipping Revenue</em>}</li>
 *   <li>{@link scenario.schedule.CargoAllocation#getDischargeRevenue <em>Discharge Revenue</em>}</li>
 *   <li>{@link scenario.schedule.CargoAllocation#getCargoType <em>Cargo Type</em>}</li>
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

	/**
	 * Returns the value of the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel</em>' reference.
	 * @see #setVessel(AllocatedVessel)
	 * @see scenario.schedule.SchedulePackage#getCargoAllocation_Vessel()
	 * @model required="true"
	 * @generated
	 */
	AllocatedVessel getVessel();

	/**
	 * Sets the value of the '{@link scenario.schedule.CargoAllocation#getVessel <em>Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel</em>' reference.
	 * @see #getVessel()
	 * @generated
	 */
	void setVessel(AllocatedVessel value);

	/**
	 * Returns the value of the '<em><b>Laden Leg</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Laden Leg</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Laden Leg</em>' reference.
	 * @see #setLadenLeg(Journey)
	 * @see scenario.schedule.SchedulePackage#getCargoAllocation_LadenLeg()
	 * @model required="true"
	 * @generated
	 */
	Journey getLadenLeg();

	/**
	 * Sets the value of the '{@link scenario.schedule.CargoAllocation#getLadenLeg <em>Laden Leg</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Laden Leg</em>' reference.
	 * @see #getLadenLeg()
	 * @generated
	 */
	void setLadenLeg(Journey value);

	/**
	 * Returns the value of the '<em><b>Ballast Leg</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ballast Leg</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ballast Leg</em>' reference.
	 * @see #setBallastLeg(Journey)
	 * @see scenario.schedule.SchedulePackage#getCargoAllocation_BallastLeg()
	 * @model required="true"
	 * @generated
	 */
	Journey getBallastLeg();

	/**
	 * Sets the value of the '{@link scenario.schedule.CargoAllocation#getBallastLeg <em>Ballast Leg</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ballast Leg</em>' reference.
	 * @see #getBallastLeg()
	 * @generated
	 */
	void setBallastLeg(Journey value);

	/**
	 * Returns the value of the '<em><b>Laden Idle</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Laden Idle</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Laden Idle</em>' reference.
	 * @see #setLadenIdle(Idle)
	 * @see scenario.schedule.SchedulePackage#getCargoAllocation_LadenIdle()
	 * @model required="true"
	 * @generated
	 */
	Idle getLadenIdle();

	/**
	 * Sets the value of the '{@link scenario.schedule.CargoAllocation#getLadenIdle <em>Laden Idle</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Laden Idle</em>' reference.
	 * @see #getLadenIdle()
	 * @generated
	 */
	void setLadenIdle(Idle value);

	/**
	 * Returns the value of the '<em><b>Ballast Idle</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ballast Idle</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ballast Idle</em>' reference.
	 * @see #setBallastIdle(Idle)
	 * @see scenario.schedule.SchedulePackage#getCargoAllocation_BallastIdle()
	 * @model required="true"
	 * @generated
	 */
	Idle getBallastIdle();

	/**
	 * Sets the value of the '{@link scenario.schedule.CargoAllocation#getBallastIdle <em>Ballast Idle</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ballast Idle</em>' reference.
	 * @see #getBallastIdle()
	 * @generated
	 */
	void setBallastIdle(Idle value);

	/**
	 * Returns the value of the '<em><b>Load Revenue</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Load Revenue</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Revenue</em>' reference.
	 * @see #setLoadRevenue(BookedRevenue)
	 * @see scenario.schedule.SchedulePackage#getCargoAllocation_LoadRevenue()
	 * @model required="true"
	 * @generated
	 */
	BookedRevenue getLoadRevenue();

	/**
	 * Sets the value of the '{@link scenario.schedule.CargoAllocation#getLoadRevenue <em>Load Revenue</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Load Revenue</em>' reference.
	 * @see #getLoadRevenue()
	 * @generated
	 */
	void setLoadRevenue(BookedRevenue value);

	/**
	 * Returns the value of the '<em><b>Shipping Revenue</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Shipping Revenue</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shipping Revenue</em>' reference.
	 * @see #setShippingRevenue(BookedRevenue)
	 * @see scenario.schedule.SchedulePackage#getCargoAllocation_ShippingRevenue()
	 * @model required="true"
	 * @generated
	 */
	BookedRevenue getShippingRevenue();

	/**
	 * Sets the value of the '{@link scenario.schedule.CargoAllocation#getShippingRevenue <em>Shipping Revenue</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Shipping Revenue</em>' reference.
	 * @see #getShippingRevenue()
	 * @generated
	 */
	void setShippingRevenue(BookedRevenue value);

	/**
	 * Returns the value of the '<em><b>Discharge Revenue</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Discharge Revenue</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge Revenue</em>' reference.
	 * @see #setDischargeRevenue(BookedRevenue)
	 * @see scenario.schedule.SchedulePackage#getCargoAllocation_DischargeRevenue()
	 * @model required="true"
	 * @generated
	 */
	BookedRevenue getDischargeRevenue();

	/**
	 * Sets the value of the '{@link scenario.schedule.CargoAllocation#getDischargeRevenue <em>Discharge Revenue</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discharge Revenue</em>' reference.
	 * @see #getDischargeRevenue()
	 * @generated
	 */
	void setDischargeRevenue(BookedRevenue value);

	/**
	 * Returns the value of the '<em><b>Cargo Type</b></em>' attribute.
	 * The default value is <code>"Fleet"</code>.
	 * The literals are from the enumeration {@link scenario.cargo.CargoType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargo Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo Type</em>' attribute.
	 * @see scenario.cargo.CargoType
	 * @see #setCargoType(CargoType)
	 * @see scenario.schedule.SchedulePackage#getCargoAllocation_CargoType()
	 * @model default="Fleet" required="true"
	 * @generated
	 */
	CargoType getCargoType();

	/**
	 * Sets the value of the '{@link scenario.schedule.CargoAllocation#getCargoType <em>Cargo Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargo Type</em>' attribute.
	 * @see scenario.cargo.CargoType
	 * @see #getCargoType()
	 * @generated
	 */
	void setCargoType(CargoType value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='long totalCost = 0;\ntotalCost += (getLadenIdle() == null) ? 0 : getLadenIdle().getTotalCost();\ntotalCost += (getLadenLeg() == null) ? 0 : getLadenLeg().getTotalCost();\ntotalCost += (getBallastIdle() == null) ? 0 : getBallastIdle().getTotalCost();\ntotalCost += (getBallastLeg() == null) ? 0 : getBallastLeg().getTotalCost();\nreturn totalCost;'"
	 * @generated
	 */
	long getTotalCost();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='final java.util.Calendar calendar = java.util.Calendar.getInstance(\njava.util.TimeZone.getTimeZone(getLoadSlot().getPort().getTimeZone())\n);\ncalendar.setTime(getLoadDate());\nreturn calendar;'"
	 * @generated
	 */
	Object getLocalLoadDate();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='final java.util.Calendar calendar = java.util.Calendar.getInstance(\njava.util.TimeZone.getTimeZone(getDischargeSlot().getPort().getTimeZone())\n);\ncalendar.setTime(getDischargeDate());\nreturn calendar;'"
	 * @generated
	 */
	Object getLocalDischargeDate();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return getDischargeVolume() + getFuelVolume();'"
	 * @generated
	 */
	long getLoadVolume();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return ((scenario.cargo.Cargo) (getLoadSlot().eContainer())).getId();'"
	 * @generated
	 */
	String getName();

} // CargoAllocation

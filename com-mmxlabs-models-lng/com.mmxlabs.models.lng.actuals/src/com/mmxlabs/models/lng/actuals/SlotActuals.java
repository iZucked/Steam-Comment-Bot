/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.actuals;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.types.ITimezoneProvider;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Slot Actuals</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.actuals.SlotActuals#getSlot <em>Slot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.SlotActuals#getCounterparty <em>Counterparty</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.SlotActuals#getOperationsStart <em>Operations Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.SlotActuals#getOperationsEnd <em>Operations End</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.SlotActuals#getTitleTransferPoint <em>Title Transfer Point</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.SlotActuals#getVolumeInM3 <em>Volume In M3</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.SlotActuals#getVolumeInMMBtu <em>Volume In MM Btu</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.SlotActuals#getPriceDOL <em>Price DOL</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.SlotActuals#getPenalty <em>Penalty</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.SlotActuals#getNotes <em>Notes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.SlotActuals#getCV <em>CV</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.SlotActuals#getBaseFuelConsumption <em>Base Fuel Consumption</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.SlotActuals#getPortBaseFuelConsumption <em>Port Base Fuel Consumption</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.SlotActuals#getRoute <em>Route</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.SlotActuals#getDistance <em>Distance</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.SlotActuals#getRouteCosts <em>Route Costs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.SlotActuals#getCrewBonus <em>Crew Bonus</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.SlotActuals#getPortCharges <em>Port Charges</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.SlotActuals#getCapacityCharges <em>Capacity Charges</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals()
 * @model abstract="true"
 * @generated
 */
public interface SlotActuals extends ITimezoneProvider {
	/**
	 * Returns the value of the '<em><b>CV</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>CV</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>CV</em>' attribute.
	 * @see #setCV(double)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals_CV()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='mmBtu/m\263' formatString='#0.#####'"
	 * @generated
	 */
	double getCV();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getCV <em>CV</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>CV</em>' attribute.
	 * @see #getCV()
	 * @generated
	 */
	void setCV(double value);

	/**
	 * Returns the value of the '<em><b>Port Charges</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Charges</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Charges</em>' attribute.
	 * @see #setPortCharges(int)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals_PortCharges()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='$' formatString='###,###,##0'"
	 * @generated
	 */
	int getPortCharges();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getPortCharges <em>Port Charges</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port Charges</em>' attribute.
	 * @see #getPortCharges()
	 * @generated
	 */
	void setPortCharges(int value);

	/**
	 * Returns the value of the '<em><b>Capacity Charges</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Capacity Charges</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Capacity Charges</em>' attribute.
	 * @see #setCapacityCharges(int)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals_CapacityCharges()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='$' formatString='###,###,##0'"
	 * @generated
	 */
	int getCapacityCharges();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getCapacityCharges <em>Capacity Charges</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Capacity Charges</em>' attribute.
	 * @see #getCapacityCharges()
	 * @generated
	 */
	void setCapacityCharges(int value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" dataType="com.mmxlabs.models.datetime.DateTime" required="true"
	 * @generated
	 */
	ZonedDateTime getOperationsStartAsDateTime();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" dataType="com.mmxlabs.models.datetime.DateTime" required="true"
	 * @generated
	 */
	ZonedDateTime getOperationsEndAsDateTime();

	/**
	 * Returns the value of the '<em><b>Base Fuel Consumption</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Fuel Consumption</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Fuel Consumption</em>' attribute.
	 * @see #setBaseFuelConsumption(int)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals_BaseFuelConsumption()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='MT' formatString='###,##0'"
	 * @generated
	 */
	int getBaseFuelConsumption();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getBaseFuelConsumption <em>Base Fuel Consumption</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Fuel Consumption</em>' attribute.
	 * @see #getBaseFuelConsumption()
	 * @generated
	 */
	void setBaseFuelConsumption(int value);

	/**
	 * Returns the value of the '<em><b>Port Base Fuel Consumption</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Base Fuel Consumption</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Base Fuel Consumption</em>' attribute.
	 * @see #setPortBaseFuelConsumption(int)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals_PortBaseFuelConsumption()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='MT' formatString='###,##0'"
	 * @generated
	 */
	int getPortBaseFuelConsumption();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getPortBaseFuelConsumption <em>Port Base Fuel Consumption</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port Base Fuel Consumption</em>' attribute.
	 * @see #getPortBaseFuelConsumption()
	 * @generated
	 */
	void setPortBaseFuelConsumption(int value);

	/**
	 * Returns the value of the '<em><b>Route</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Route</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Route</em>' reference.
	 * @see #setRoute(Route)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals_Route()
	 * @model
	 * @generated
	 */
	Route getRoute();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getRoute <em>Route</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Route</em>' reference.
	 * @see #getRoute()
	 * @generated
	 */
	void setRoute(Route value);

	/**
	 * Returns the value of the '<em><b>Distance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Distance</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Distance</em>' attribute.
	 * @see #setDistance(int)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals_Distance()
	 * @model
	 * @generated
	 */
	int getDistance();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getDistance <em>Distance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Distance</em>' attribute.
	 * @see #getDistance()
	 * @generated
	 */
	void setDistance(int value);

	/**
	 * Returns the value of the '<em><b>Route Costs</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Route Costs</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Route Costs</em>' attribute.
	 * @see #setRouteCosts(int)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals_RouteCosts()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='$' formatString='###,###,##0'"
	 * @generated
	 */
	int getRouteCosts();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getRouteCosts <em>Route Costs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Route Costs</em>' attribute.
	 * @see #getRouteCosts()
	 * @generated
	 */
	void setRouteCosts(int value);

	/**
	 * Returns the value of the '<em><b>Crew Bonus</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Crew Bonus</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Crew Bonus</em>' attribute.
	 * @see #setCrewBonus(int)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals_CrewBonus()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='$' formatString='###,###,##0'"
	 * @generated
	 */
	int getCrewBonus();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getCrewBonus <em>Crew Bonus</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Crew Bonus</em>' attribute.
	 * @see #getCrewBonus()
	 * @generated
	 */
	void setCrewBonus(int value);

	/**
	 * Returns the value of the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot</em>' reference.
	 * @see #setSlot(Slot)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals_Slot()
	 * @model
	 * @generated
	 */
	Slot getSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getSlot <em>Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Slot</em>' reference.
	 * @see #getSlot()
	 * @generated
	 */
	void setSlot(Slot value);

	/**
	 * Returns the value of the '<em><b>Counterparty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Counterparty</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Counterparty</em>' attribute.
	 * @see #setCounterparty(String)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals_Counterparty()
	 * @model
	 * @generated
	 */
	String getCounterparty();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getCounterparty <em>Counterparty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Counterparty</em>' attribute.
	 * @see #getCounterparty()
	 * @generated
	 */
	void setCounterparty(String value);

	/**
	 * Returns the value of the '<em><b>Operations Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Operations Start</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Operations Start</em>' attribute.
	 * @see #setOperationsStart(LocalDateTime)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals_OperationsStart()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDateTime"
	 * @generated
	 */
	LocalDateTime getOperationsStart();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getOperationsStart <em>Operations Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Operations Start</em>' attribute.
	 * @see #getOperationsStart()
	 * @generated
	 */
	void setOperationsStart(LocalDateTime value);

	/**
	 * Returns the value of the '<em><b>Operations End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Operations End</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Operations End</em>' attribute.
	 * @see #setOperationsEnd(LocalDateTime)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals_OperationsEnd()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDateTime"
	 * @generated
	 */
	LocalDateTime getOperationsEnd();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getOperationsEnd <em>Operations End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Operations End</em>' attribute.
	 * @see #getOperationsEnd()
	 * @generated
	 */
	void setOperationsEnd(LocalDateTime value);

	/**
	 * Returns the value of the '<em><b>Title Transfer Point</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Title Transfer Point</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Title Transfer Point</em>' reference.
	 * @see #setTitleTransferPoint(Port)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals_TitleTransferPoint()
	 * @model
	 * @generated
	 */
	Port getTitleTransferPoint();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getTitleTransferPoint <em>Title Transfer Point</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Title Transfer Point</em>' reference.
	 * @see #getTitleTransferPoint()
	 * @generated
	 */
	void setTitleTransferPoint(Port value);

	/**
	 * Returns the value of the '<em><b>Volume In M3</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume In M3</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume In M3</em>' attribute.
	 * @see #setVolumeInM3(double)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals_VolumeInM3()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m\263' formatString='###,##0.###'"
	 * @generated
	 */
	double getVolumeInM3();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getVolumeInM3 <em>Volume In M3</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume In M3</em>' attribute.
	 * @see #getVolumeInM3()
	 * @generated
	 */
	void setVolumeInM3(double value);

	/**
	 * Returns the value of the '<em><b>Volume In MM Btu</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume In MM Btu</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume In MM Btu</em>' attribute.
	 * @see #setVolumeInMMBtu(double)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals_VolumeInMMBtu()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='mmBtu' formatString='##,###,##0.###'"
	 * @generated
	 */
	double getVolumeInMMBtu();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getVolumeInMMBtu <em>Volume In MM Btu</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume In MM Btu</em>' attribute.
	 * @see #getVolumeInMMBtu()
	 * @generated
	 */
	void setVolumeInMMBtu(double value);

	/**
	 * Returns the value of the '<em><b>Price DOL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Price DOL</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Price DOL</em>' attribute.
	 * @see #setPriceDOL(double)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals_PriceDOL()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='$/mmBtu' formatString='#0.###'"
	 * @generated
	 */
	double getPriceDOL();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getPriceDOL <em>Price DOL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price DOL</em>' attribute.
	 * @see #getPriceDOL()
	 * @generated
	 */
	void setPriceDOL(double value);

	/**
	 * Returns the value of the '<em><b>Penalty</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.actuals.PenaltyType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Penalty</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Penalty</em>' attribute.
	 * @see com.mmxlabs.models.lng.actuals.PenaltyType
	 * @see #setPenalty(PenaltyType)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals_Penalty()
	 * @model
	 * @generated
	 */
	PenaltyType getPenalty();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getPenalty <em>Penalty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Penalty</em>' attribute.
	 * @see com.mmxlabs.models.lng.actuals.PenaltyType
	 * @see #getPenalty()
	 * @generated
	 */
	void setPenalty(PenaltyType value);

	/**
	 * Returns the value of the '<em><b>Notes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Notes</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Notes</em>' attribute.
	 * @see #setNotes(String)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals_Notes()
	 * @model
	 * @generated
	 */
	String getNotes();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getNotes <em>Notes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Notes</em>' attribute.
	 * @see #getNotes()
	 * @generated
	 */
	void setNotes(String value);

} // SlotActuals

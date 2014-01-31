/**
 */
package com.mmxlabs.models.lng.actuals;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.ITimezoneProvider;
import java.util.Date;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Slot Actuals</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
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
 *   <li>{@link com.mmxlabs.models.lng.actuals.SlotActuals#getPortCharges <em>Port Charges</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.SlotActuals#getBaseFuelConsumption <em>Base Fuel Consumption</em>}</li>
 * </ul>
 * </p>
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
	 * @model
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
	 * @model
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
	 * @model
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
	 * @see #setOperationsStart(Date)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals_OperationsStart()
	 * @model
	 * @generated
	 */
	Date getOperationsStart();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getOperationsStart <em>Operations Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Operations Start</em>' attribute.
	 * @see #getOperationsStart()
	 * @generated
	 */
	void setOperationsStart(Date value);

	/**
	 * Returns the value of the '<em><b>Operations End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Operations End</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Operations End</em>' attribute.
	 * @see #setOperationsEnd(Date)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals_OperationsEnd()
	 * @model
	 * @generated
	 */
	Date getOperationsEnd();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getOperationsEnd <em>Operations End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Operations End</em>' attribute.
	 * @see #getOperationsEnd()
	 * @generated
	 */
	void setOperationsEnd(Date value);

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
	 * @see #setVolumeInM3(float)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals_VolumeInM3()
	 * @model
	 * @generated
	 */
	float getVolumeInM3();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getVolumeInM3 <em>Volume In M3</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume In M3</em>' attribute.
	 * @see #getVolumeInM3()
	 * @generated
	 */
	void setVolumeInM3(float value);

	/**
	 * Returns the value of the '<em><b>Volume In MM Btu</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume In MM Btu</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume In MM Btu</em>' attribute.
	 * @see #setVolumeInMMBtu(int)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals_VolumeInMMBtu()
	 * @model
	 * @generated
	 */
	int getVolumeInMMBtu();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getVolumeInMMBtu <em>Volume In MM Btu</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume In MM Btu</em>' attribute.
	 * @see #getVolumeInMMBtu()
	 * @generated
	 */
	void setVolumeInMMBtu(int value);

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
	 * @model
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
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Penalty</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Penalty</em>' attribute.
	 * @see #setPenalty(String)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getSlotActuals_Penalty()
	 * @model
	 * @generated
	 */
	String getPenalty();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getPenalty <em>Penalty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Penalty</em>' attribute.
	 * @see #getPenalty()
	 * @generated
	 */
	void setPenalty(String value);

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

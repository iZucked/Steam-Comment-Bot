/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.mmxcore.MMXObject;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Slot Allocation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getSlot <em>Slot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getSpotMarket <em>Spot Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getCargoAllocation <em>Cargo Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getMarketAllocation <em>Market Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getSlotVisit <em>Slot Visit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getPrice <em>Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getVolumeTransferred <em>Volume Transferred</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getEnergyTransferred <em>Energy Transferred</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getCv <em>Cv</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getVolumeValue <em>Volume Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getExposures <em>Exposures</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getPhysicalVolumeTransferred <em>Physical Volume Transferred</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getPhysicalEnergyTransferred <em>Physical Energy Transferred</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSlotAllocation()
 * @model
 * @generated
 */
public interface SlotAllocation extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot</em>' reference.
	 * @see #isSetSlot()
	 * @see #unsetSlot()
	 * @see #setSlot(Slot)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSlotAllocation_Slot()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Slot getSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getSlot <em>Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Slot</em>' reference.
	 * @see #isSetSlot()
	 * @see #unsetSlot()
	 * @see #getSlot()
	 * @generated
	 */
	void setSlot(Slot value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getSlot <em>Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetSlot()
	 * @see #getSlot()
	 * @see #setSlot(Slot)
	 * @generated
	 */
	void unsetSlot();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getSlot <em>Slot</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Slot</em>' reference is set.
	 * @see #unsetSlot()
	 * @see #getSlot()
	 * @see #setSlot(Slot)
	 * @generated
	 */
	boolean isSetSlot();

	/**
	 * Returns the value of the '<em><b>Spot Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Spot Market</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Spot Market</em>' reference.
	 * @see #isSetSpotMarket()
	 * @see #unsetSpotMarket()
	 * @see #setSpotMarket(SpotMarket)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSlotAllocation_SpotMarket()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	SpotMarket getSpotMarket();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getSpotMarket <em>Spot Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spot Market</em>' reference.
	 * @see #isSetSpotMarket()
	 * @see #unsetSpotMarket()
	 * @see #getSpotMarket()
	 * @generated
	 */
	void setSpotMarket(SpotMarket value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getSpotMarket <em>Spot Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetSpotMarket()
	 * @see #getSpotMarket()
	 * @see #setSpotMarket(SpotMarket)
	 * @generated
	 */
	void unsetSpotMarket();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getSpotMarket <em>Spot Market</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Spot Market</em>' reference is set.
	 * @see #unsetSpotMarket()
	 * @see #getSpotMarket()
	 * @see #setSpotMarket(SpotMarket)
	 * @generated
	 */
	boolean isSetSpotMarket();

	/**
	 * Returns the value of the '<em><b>Cargo Allocation</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.lng.schedule.CargoAllocation#getSlotAllocations <em>Slot Allocations</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargo Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo Allocation</em>' reference.
	 * @see #setCargoAllocation(CargoAllocation)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSlotAllocation_CargoAllocation()
	 * @see com.mmxlabs.models.lng.schedule.CargoAllocation#getSlotAllocations
	 * @model opposite="slotAllocations" required="true"
	 * @generated
	 */
	CargoAllocation getCargoAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getCargoAllocation <em>Cargo Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargo Allocation</em>' reference.
	 * @see #getCargoAllocation()
	 * @generated
	 */
	void setCargoAllocation(CargoAllocation value);

	/**
	 * Returns the value of the '<em><b>Market Allocation</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.lng.schedule.MarketAllocation#getSlotAllocation <em>Slot Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Market Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Market Allocation</em>' reference.
	 * @see #setMarketAllocation(MarketAllocation)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSlotAllocation_MarketAllocation()
	 * @see com.mmxlabs.models.lng.schedule.MarketAllocation#getSlotAllocation
	 * @model opposite="slotAllocation" required="true"
	 * @generated
	 */
	MarketAllocation getMarketAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getMarketAllocation <em>Market Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Market Allocation</em>' reference.
	 * @see #getMarketAllocation()
	 * @generated
	 */
	void setMarketAllocation(MarketAllocation value);

	/**
	 * Returns the value of the '<em><b>Slot Visit</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.lng.schedule.SlotVisit#getSlotAllocation <em>Slot Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot Visit</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot Visit</em>' reference.
	 * @see #isSetSlotVisit()
	 * @see #unsetSlotVisit()
	 * @see #setSlotVisit(SlotVisit)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSlotAllocation_SlotVisit()
	 * @see com.mmxlabs.models.lng.schedule.SlotVisit#getSlotAllocation
	 * @model opposite="slotAllocation" unsettable="true" required="true"
	 * @generated
	 */
	SlotVisit getSlotVisit();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getSlotVisit <em>Slot Visit</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Slot Visit</em>' reference.
	 * @see #isSetSlotVisit()
	 * @see #unsetSlotVisit()
	 * @see #getSlotVisit()
	 * @generated
	 */
	void setSlotVisit(SlotVisit value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getSlotVisit <em>Slot Visit</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetSlotVisit()
	 * @see #getSlotVisit()
	 * @see #setSlotVisit(SlotVisit)
	 * @generated
	 */
	void unsetSlotVisit();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getSlotVisit <em>Slot Visit</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Slot Visit</em>' reference is set.
	 * @see #unsetSlotVisit()
	 * @see #getSlotVisit()
	 * @see #setSlotVisit(SlotVisit)
	 * @generated
	 */
	boolean isSetSlotVisit();

	/**
	 * Returns the value of the '<em><b>Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Price</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Price</em>' attribute.
	 * @see #setPrice(double)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSlotAllocation_Price()
	 * @model
	 * @generated
	 */
	double getPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getPrice <em>Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price</em>' attribute.
	 * @see #getPrice()
	 * @generated
	 */
	void setPrice(double value);

	/**
	 * Returns the value of the '<em><b>Volume Transferred</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume Transferred</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Transferred</em>' attribute.
	 * @see #setVolumeTransferred(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSlotAllocation_VolumeTransferred()
	 * @model required="true"
	 * @generated
	 */
	int getVolumeTransferred();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getVolumeTransferred <em>Volume Transferred</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Transferred</em>' attribute.
	 * @see #getVolumeTransferred()
	 * @generated
	 */
	void setVolumeTransferred(int value);

	/**
	 * Returns the value of the '<em><b>Energy Transferred</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Energy Transferred</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Energy Transferred</em>' attribute.
	 * @see #setEnergyTransferred(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSlotAllocation_EnergyTransferred()
	 * @model required="true"
	 * @generated
	 */
	int getEnergyTransferred();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getEnergyTransferred <em>Energy Transferred</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Energy Transferred</em>' attribute.
	 * @see #getEnergyTransferred()
	 * @generated
	 */
	void setEnergyTransferred(int value);

	/**
	 * Returns the value of the '<em><b>Cv</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cv</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cv</em>' attribute.
	 * @see #setCv(double)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSlotAllocation_Cv()
	 * @model required="true"
	 * @generated
	 */
	double getCv();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getCv <em>Cv</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cv</em>' attribute.
	 * @see #getCv()
	 * @generated
	 */
	void setCv(double value);

	/**
	 * Returns the value of the '<em><b>Volume Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Value</em>' attribute.
	 * @see #setVolumeValue(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSlotAllocation_VolumeValue()
	 * @model
	 * @generated
	 */
	int getVolumeValue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getVolumeValue <em>Volume Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Value</em>' attribute.
	 * @see #getVolumeValue()
	 * @generated
	 */
	void setVolumeValue(int value);

	/**
	 * Returns the value of the '<em><b>Exposures</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.schedule.ExposureDetail}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Exposures</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Exposures</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSlotAllocation_Exposures()
	 * @model containment="true"
	 * @generated
	 */
	EList<ExposureDetail> getExposures();

	/**
	 * Returns the value of the '<em><b>Physical Volume Transferred</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Physical Volume Transferred</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Physical Volume Transferred</em>' attribute.
	 * @see #setPhysicalVolumeTransferred(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSlotAllocation_PhysicalVolumeTransferred()
	 * @model required="true"
	 * @generated
	 */
	int getPhysicalVolumeTransferred();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getPhysicalVolumeTransferred <em>Physical Volume Transferred</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Physical Volume Transferred</em>' attribute.
	 * @see #getPhysicalVolumeTransferred()
	 * @generated
	 */
	void setPhysicalVolumeTransferred(int value);

	/**
	 * Returns the value of the '<em><b>Physical Energy Transferred</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Physical Energy Transferred</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Physical Energy Transferred</em>' attribute.
	 * @see #setPhysicalEnergyTransferred(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getSlotAllocation_PhysicalEnergyTransferred()
	 * @model required="true"
	 * @generated
	 */
	int getPhysicalEnergyTransferred();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.SlotAllocation#getPhysicalEnergyTransferred <em>Physical Energy Transferred</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Physical Energy Transferred</em>' attribute.
	 * @see #getPhysicalEnergyTransferred()
	 * @generated
	 */
	void setPhysicalEnergyTransferred(int value);

	/**
	 * <!-- begin-user-doc -->
	 * @generated
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	Port getPort();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	Contract getContract();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	String getName();

} // end of  SlotAllocation

// finish type fixing

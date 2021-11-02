/**
 */
package com.mmxlabs.models.lng.cargo;

import java.time.LocalDate;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Panama Seasonality Record</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord#getVesselGroupCanalParameter <em>Vessel Group Canal Parameter</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord#getStartDay <em>Start Day</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord#getStartMonth <em>Start Month</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord#getStartYear <em>Start Year</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord#getNorthboundWaitingDays <em>Northbound Waiting Days</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord#getSouthboundWaitingDays <em>Southbound Waiting Days</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getPanamaSeasonalityRecord()
 * @model
 * @generated
 */
public interface PanamaSeasonalityRecord extends EObject {
	/**
	 * Returns the value of the '<em><b>Vessel Group Canal Parameter</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Group Canal Parameter</em>' reference.
	 * @see #setVesselGroupCanalParameter(VesselGroupCanalParameters)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getPanamaSeasonalityRecord_VesselGroupCanalParameter()
	 * @model
	 * @generated
	 */
	VesselGroupCanalParameters getVesselGroupCanalParameter();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord#getVesselGroupCanalParameter <em>Vessel Group Canal Parameter</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Group Canal Parameter</em>' reference.
	 * @see #getVesselGroupCanalParameter()
	 * @generated
	 */
	void setVesselGroupCanalParameter(VesselGroupCanalParameters value);

	/**
	 * Returns the value of the '<em><b>Start Day</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Day</em>' attribute.
	 * @see #setStartDay(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getPanamaSeasonalityRecord_StartDay()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat numberFormat='#0'"
	 * @generated
	 */
	int getStartDay();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord#getStartDay <em>Start Day</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Day</em>' attribute.
	 * @see #getStartDay()
	 * @generated
	 */
	void setStartDay(int value);

	/**
	 * Returns the value of the '<em><b>Start Month</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Month</em>' attribute.
	 * @see #setStartMonth(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getPanamaSeasonalityRecord_StartMonth()
	 * @model
	 * @generated
	 */
	int getStartMonth();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord#getStartMonth <em>Start Month</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Month</em>' attribute.
	 * @see #getStartMonth()
	 * @generated
	 */
	void setStartMonth(int value);

	/**
	 * Returns the value of the '<em><b>Start Year</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Year</em>' attribute.
	 * @see #setStartYear(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getPanamaSeasonalityRecord_StartYear()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat numberFormat='#0'"
	 * @generated
	 */
	int getStartYear();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord#getStartYear <em>Start Year</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Year</em>' attribute.
	 * @see #getStartYear()
	 * @generated
	 */
	void setStartYear(int value);

	/**
	 * Returns the value of the '<em><b>Northbound Waiting Days</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Northbound Waiting Days</em>' attribute.
	 * @see #setNorthboundWaitingDays(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getPanamaSeasonalityRecord_NorthboundWaitingDays()
	 * @model
	 * @generated
	 */
	int getNorthboundWaitingDays();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord#getNorthboundWaitingDays <em>Northbound Waiting Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Northbound Waiting Days</em>' attribute.
	 * @see #getNorthboundWaitingDays()
	 * @generated
	 */
	void setNorthboundWaitingDays(int value);

	/**
	 * Returns the value of the '<em><b>Southbound Waiting Days</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Southbound Waiting Days</em>' attribute.
	 * @see #setSouthboundWaitingDays(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getPanamaSeasonalityRecord_SouthboundWaitingDays()
	 * @model
	 * @generated
	 */
	int getSouthboundWaitingDays();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord#getSouthboundWaitingDays <em>Southbound Waiting Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Southbound Waiting Days</em>' attribute.
	 * @see #getSouthboundWaitingDays()
	 * @generated
	 */
	void setSouthboundWaitingDays(int value);

} // PanamaSeasonalityRecord

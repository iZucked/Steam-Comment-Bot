

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.analytics;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.fleet.Vessel;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Unit Cost Matrix</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getPorts <em>Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getVessel <em>Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getNotionalDayRate <em>Notional Day Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getSpeed <em>Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#isRoundTrip <em>Round Trip</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getMinimumLoad <em>Minimum Load</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getMaximumLoad <em>Maximum Load</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getMinimumDischarge <em>Minimum Discharge</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getMaximumDischarge <em>Maximum Discharge</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getCargoPrice <em>Cargo Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getBaseFuelPrice <em>Base Fuel Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getCvValue <em>Cv Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getDischargeIdleTime <em>Discharge Idle Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getReturnIdleTime <em>Return Idle Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getCostLines <em>Cost Lines</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getAllowedRoutes <em>Allowed Routes</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostMatrix()
 * @model
 * @generated
 */
public interface UnitCostMatrix extends UUIDObject, NamedObject {
	/**
	 * Returns the value of the '<em><b>Ports</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ports</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ports</em>' reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostMatrix_Ports()
	 * @model
	 * @generated
	 */
	EList<APortSet> getPorts();

	/**
	 * Returns the value of the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel</em>' reference.
	 * @see #setVessel(Vessel)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostMatrix_Vessel()
	 * @model required="true"
	 * @generated
	 */
	Vessel getVessel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getVessel <em>Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel</em>' reference.
	 * @see #getVessel()
	 * @generated
	 */
	void setVessel(Vessel value);

	/**
	 * Returns the value of the '<em><b>Notional Day Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Notional Day Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Notional Day Rate</em>' attribute.
	 * @see #setNotionalDayRate(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostMatrix_NotionalDayRate()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='$/day'"
	 * @generated
	 */
	int getNotionalDayRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getNotionalDayRate <em>Notional Day Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Notional Day Rate</em>' attribute.
	 * @see #getNotionalDayRate()
	 * @generated
	 */
	void setNotionalDayRate(int value);

	/**
	 * Returns the value of the '<em><b>Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Speed</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Speed</em>' attribute.
	 * @see #isSetSpeed()
	 * @see #unsetSpeed()
	 * @see #setSpeed(double)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostMatrix_Speed()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='kts'"
	 * @generated
	 */
	double getSpeed();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getSpeed <em>Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Speed</em>' attribute.
	 * @see #isSetSpeed()
	 * @see #unsetSpeed()
	 * @see #getSpeed()
	 * @generated
	 */
	void setSpeed(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getSpeed <em>Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetSpeed()
	 * @see #getSpeed()
	 * @see #setSpeed(double)
	 * @generated
	 */
	void unsetSpeed();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getSpeed <em>Speed</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Speed</em>' attribute is set.
	 * @see #unsetSpeed()
	 * @see #getSpeed()
	 * @see #setSpeed(double)
	 * @generated
	 */
	boolean isSetSpeed();

	/**
	 * Returns the value of the '<em><b>Round Trip</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Round Trip</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Round Trip</em>' attribute.
	 * @see #setRoundTrip(boolean)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostMatrix_RoundTrip()
	 * @model default="true" required="true"
	 * @generated
	 */
	boolean isRoundTrip();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#isRoundTrip <em>Round Trip</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Round Trip</em>' attribute.
	 * @see #isRoundTrip()
	 * @generated
	 */
	void setRoundTrip(boolean value);

	/**
	 * Returns the value of the '<em><b>Minimum Load</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Minimum Load</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Minimum Load</em>' attribute.
	 * @see #setMinimumLoad(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostMatrix_MinimumLoad()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m3'"
	 * @generated
	 */
	int getMinimumLoad();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getMinimumLoad <em>Minimum Load</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Minimum Load</em>' attribute.
	 * @see #getMinimumLoad()
	 * @generated
	 */
	void setMinimumLoad(int value);

	/**
	 * Returns the value of the '<em><b>Maximum Load</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Maximum Load</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Maximum Load</em>' attribute.
	 * @see #setMaximumLoad(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostMatrix_MaximumLoad()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m3'"
	 * @generated
	 */
	int getMaximumLoad();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getMaximumLoad <em>Maximum Load</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Maximum Load</em>' attribute.
	 * @see #getMaximumLoad()
	 * @generated
	 */
	void setMaximumLoad(int value);

	/**
	 * Returns the value of the '<em><b>Minimum Discharge</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Minimum Discharge</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Minimum Discharge</em>' attribute.
	 * @see #setMinimumDischarge(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostMatrix_MinimumDischarge()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m3'"
	 * @generated
	 */
	int getMinimumDischarge();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getMinimumDischarge <em>Minimum Discharge</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Minimum Discharge</em>' attribute.
	 * @see #getMinimumDischarge()
	 * @generated
	 */
	void setMinimumDischarge(int value);

	/**
	 * Returns the value of the '<em><b>Maximum Discharge</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Maximum Discharge</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Maximum Discharge</em>' attribute.
	 * @see #setMaximumDischarge(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostMatrix_MaximumDischarge()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m3'"
	 * @generated
	 */
	int getMaximumDischarge();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getMaximumDischarge <em>Maximum Discharge</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Maximum Discharge</em>' attribute.
	 * @see #getMaximumDischarge()
	 * @generated
	 */
	void setMaximumDischarge(int value);

	/**
	 * Returns the value of the '<em><b>Cargo Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargo Price</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo Price</em>' attribute.
	 * @see #setCargoPrice(double)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostMatrix_CargoPrice()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='$/mmbtu'"
	 * @generated
	 */
	double getCargoPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getCargoPrice <em>Cargo Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargo Price</em>' attribute.
	 * @see #getCargoPrice()
	 * @generated
	 */
	void setCargoPrice(double value);

	/**
	 * Returns the value of the '<em><b>Base Fuel Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Fuel Price</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Fuel Price</em>' attribute.
	 * @see #setBaseFuelPrice(double)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostMatrix_BaseFuelPrice()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='$/MT'"
	 * @generated
	 */
	double getBaseFuelPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getBaseFuelPrice <em>Base Fuel Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Fuel Price</em>' attribute.
	 * @see #getBaseFuelPrice()
	 * @generated
	 */
	void setBaseFuelPrice(double value);

	/**
	 * Returns the value of the '<em><b>Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cv Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cv Value</em>' attribute.
	 * @see #isSetCvValue()
	 * @see #unsetCvValue()
	 * @see #setCvValue(double)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostMatrix_CvValue()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	double getCvValue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getCvValue <em>Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cv Value</em>' attribute.
	 * @see #isSetCvValue()
	 * @see #unsetCvValue()
	 * @see #getCvValue()
	 * @generated
	 */
	void setCvValue(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getCvValue <em>Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetCvValue()
	 * @see #getCvValue()
	 * @see #setCvValue(double)
	 * @generated
	 */
	void unsetCvValue();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getCvValue <em>Cv Value</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Cv Value</em>' attribute is set.
	 * @see #unsetCvValue()
	 * @see #getCvValue()
	 * @see #setCvValue(double)
	 * @generated
	 */
	boolean isSetCvValue();

	/**
	 * Returns the value of the '<em><b>Discharge Idle Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Discharge Idle Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge Idle Time</em>' attribute.
	 * @see #setDischargeIdleTime(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostMatrix_DischargeIdleTime()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='hrs'"
	 * @generated
	 */
	int getDischargeIdleTime();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getDischargeIdleTime <em>Discharge Idle Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discharge Idle Time</em>' attribute.
	 * @see #getDischargeIdleTime()
	 * @generated
	 */
	void setDischargeIdleTime(int value);

	/**
	 * Returns the value of the '<em><b>Return Idle Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Return Idle Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Return Idle Time</em>' attribute.
	 * @see #setReturnIdleTime(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostMatrix_ReturnIdleTime()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='hrs'"
	 * @generated
	 */
	int getReturnIdleTime();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.UnitCostMatrix#getReturnIdleTime <em>Return Idle Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Return Idle Time</em>' attribute.
	 * @see #getReturnIdleTime()
	 * @generated
	 */
	void setReturnIdleTime(int value);

	/**
	 * Returns the value of the '<em><b>Cost Lines</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.UnitCostLine}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cost Lines</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cost Lines</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostMatrix_CostLines()
	 * @model containment="true"
	 * @generated
	 */
	EList<UnitCostLine> getCostLines();

	/**
	 * Returns the value of the '<em><b>Allowed Routes</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.Route}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Allowed Routes</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Allowed Routes</em>' reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getUnitCostMatrix_AllowedRoutes()
	 * @model
	 * @generated
	 */
	EList<Route> getAllowedRoutes();

} // end of  UnitCostMatrix

// finish type fixing

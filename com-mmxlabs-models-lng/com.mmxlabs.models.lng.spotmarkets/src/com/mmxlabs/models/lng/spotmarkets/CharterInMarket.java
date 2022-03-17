/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.spotmarkets;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.mmxcore.NamedObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Charter In Market</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getVessel <em>Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getCharterInRate <em>Charter In Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getSpotCharterCount <em>Spot Charter Count</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#isOverrideInaccessibleRoutes <em>Override Inaccessible Routes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getInaccessibleRoutes <em>Inaccessible Routes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getGenericCharterContract <em>Generic Charter Contract</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#isNominal <em>Nominal</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getMinDuration <em>Min Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getMaxDuration <em>Max Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#isMtm <em>Mtm</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getEntity <em>Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getStartAt <em>Start At</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getEndAt <em>End At</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getStartHeelCV <em>Start Heel CV</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterInMarket()
 * @model
 * @generated
 */
public interface CharterInMarket extends SpotCharterMarket, VesselAssignmentType, NamedObject {
	/**
	 * Returns the value of the '<em><b>Charter In Price</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter In Price</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter In Price</em>' reference.
	 * @see #setCharterInPrice(CharterIndex)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterInMarket_CharterInPrice()
	 * @model required="true"
	 * @generated
	 */
//	CharterIndex getCharterInPrice();

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
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterInMarket_Vessel()
	 * @model
	 * @generated
	 */
	Vessel getVessel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getVessel <em>Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel</em>' reference.
	 * @see #getVessel()
	 * @generated
	 */
	void setVessel(Vessel value);

	/**
	 * Returns the value of the '<em><b>Nominal</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Nominal</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Nominal</em>' attribute.
	 * @see #setNominal(boolean)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterInMarket_Nominal()
	 * @model default="true"
	 * @generated
	 */
	boolean isNominal();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#isNominal <em>Nominal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Nominal</em>' attribute.
	 * @see #isNominal()
	 * @generated
	 */
	void setNominal(boolean value);

	/**
	 * Returns the value of the '<em><b>Min Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Duration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Duration</em>' attribute.
	 * @see #isSetMinDuration()
	 * @see #unsetMinDuration()
	 * @see #setMinDuration(int)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterInMarket_MinDuration()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='days' formatString='##0'"
	 * @generated
	 */
	int getMinDuration();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getMinDuration <em>Min Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Duration</em>' attribute.
	 * @see #isSetMinDuration()
	 * @see #unsetMinDuration()
	 * @see #getMinDuration()
	 * @generated
	 */
	void setMinDuration(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getMinDuration <em>Min Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMinDuration()
	 * @see #getMinDuration()
	 * @see #setMinDuration(int)
	 * @generated
	 */
	void unsetMinDuration();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getMinDuration <em>Min Duration</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Min Duration</em>' attribute is set.
	 * @see #unsetMinDuration()
	 * @see #getMinDuration()
	 * @see #setMinDuration(int)
	 * @generated
	 */
	boolean isSetMinDuration();

	/**
	 * Returns the value of the '<em><b>Max Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Duration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Duration</em>' attribute.
	 * @see #isSetMaxDuration()
	 * @see #unsetMaxDuration()
	 * @see #setMaxDuration(int)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterInMarket_MaxDuration()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='days' formatString='##0'"
	 * @generated
	 */
	int getMaxDuration();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getMaxDuration <em>Max Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Duration</em>' attribute.
	 * @see #isSetMaxDuration()
	 * @see #unsetMaxDuration()
	 * @see #getMaxDuration()
	 * @generated
	 */
	void setMaxDuration(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getMaxDuration <em>Max Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMaxDuration()
	 * @see #getMaxDuration()
	 * @see #setMaxDuration(int)
	 * @generated
	 */
	void unsetMaxDuration();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getMaxDuration <em>Max Duration</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Max Duration</em>' attribute is set.
	 * @see #unsetMaxDuration()
	 * @see #getMaxDuration()
	 * @see #setMaxDuration(int)
	 * @generated
	 */
	boolean isSetMaxDuration();

	/**
	 * Returns the value of the '<em><b>Mtm</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mtm</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mtm</em>' attribute.
	 * @see #setMtm(boolean)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterInMarket_Mtm()
	 * @model
	 * @generated
	 */
	boolean isMtm();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#isMtm <em>Mtm</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mtm</em>' attribute.
	 * @see #isMtm()
	 * @generated
	 */
	void setMtm(boolean value);

	/**
	 * Returns the value of the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entity</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entity</em>' reference.
	 * @see #setEntity(BaseLegalEntity)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterInMarket_Entity()
	 * @model
	 * @generated
	 */
	BaseLegalEntity getEntity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getEntity <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entity</em>' reference.
	 * @see #getEntity()
	 * @generated
	 */
	void setEntity(BaseLegalEntity value);

	/**
	 * Returns the value of the '<em><b>Start At</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start At</em>' reference.
	 * @see #isSetStartAt()
	 * @see #unsetStartAt()
	 * @see #setStartAt(Port)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterInMarket_StartAt()
	 * @model unsettable="true"
	 * @generated
	 */
	Port getStartAt();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getStartAt <em>Start At</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start At</em>' reference.
	 * @see #isSetStartAt()
	 * @see #unsetStartAt()
	 * @see #getStartAt()
	 * @generated
	 */
	void setStartAt(Port value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getStartAt <em>Start At</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetStartAt()
	 * @see #getStartAt()
	 * @see #setStartAt(Port)
	 * @generated
	 */
	void unsetStartAt();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getStartAt <em>Start At</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Start At</em>' reference is set.
	 * @see #unsetStartAt()
	 * @see #getStartAt()
	 * @see #setStartAt(Port)
	 * @generated
	 */
	boolean isSetStartAt();

	/**
	 * Returns the value of the '<em><b>End At</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}<code>&lt;com.mmxlabs.models.lng.port.Port&gt;</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End At</em>' reference list.
	 * @see #isSetEndAt()
	 * @see #unsetEndAt()
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterInMarket_EndAt()
	 * @model unsettable="true"
	 * @generated
	 */
	EList<APortSet<Port>> getEndAt();

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getEndAt <em>End At</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetEndAt()
	 * @see #getEndAt()
	 * @generated
	 */
	void unsetEndAt();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getEndAt <em>End At</em>}' reference list is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>End At</em>' reference list is set.
	 * @see #unsetEndAt()
	 * @see #getEndAt()
	 * @generated
	 */
	boolean isSetEndAt();

	/**
	 * Returns the value of the '<em><b>Start Heel CV</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Heel CV</em>' attribute.
	 * @see #isSetStartHeelCV()
	 * @see #unsetStartHeelCV()
	 * @see #setStartHeelCV(double)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterInMarket_StartHeelCV()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='#0.###'"
	 * @generated
	 */
	double getStartHeelCV();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getStartHeelCV <em>Start Heel CV</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Heel CV</em>' attribute.
	 * @see #isSetStartHeelCV()
	 * @see #unsetStartHeelCV()
	 * @see #getStartHeelCV()
	 * @generated
	 */
	void setStartHeelCV(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getStartHeelCV <em>Start Heel CV</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetStartHeelCV()
	 * @see #getStartHeelCV()
	 * @see #setStartHeelCV(double)
	 * @generated
	 */
	void unsetStartHeelCV();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getStartHeelCV <em>Start Heel CV</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Start Heel CV</em>' attribute is set.
	 * @see #unsetStartHeelCV()
	 * @see #getStartHeelCV()
	 * @see #setStartHeelCV(double)
	 * @generated
	 */
	boolean isSetStartHeelCV();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	int getMarketOrContractMinDuration();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	int getMarketOrContractMaxDuration();

	/**
	 * Returns the value of the '<em><b>Spot Charter Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Spot Charter Count</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Spot Charter Count</em>' attribute.
	 * @see #setSpotCharterCount(int)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterInMarket_SpotCharterCount()
	 * @model required="true"
	 * @generated
	 */
	int getSpotCharterCount();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getSpotCharterCount <em>Spot Charter Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spot Charter Count</em>' attribute.
	 * @see #getSpotCharterCount()
	 * @generated
	 */
	void setSpotCharterCount(int value);

	/**
	 * Returns the value of the '<em><b>Override Inaccessible Routes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Override Inaccessible Routes</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Override Inaccessible Routes</em>' attribute.
	 * @see #setOverrideInaccessibleRoutes(boolean)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterInMarket_OverrideInaccessibleRoutes()
	 * @model
	 * @generated
	 */
	boolean isOverrideInaccessibleRoutes();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#isOverrideInaccessibleRoutes <em>Override Inaccessible Routes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Override Inaccessible Routes</em>' attribute.
	 * @see #isOverrideInaccessibleRoutes()
	 * @generated
	 */
	void setOverrideInaccessibleRoutes(boolean value);

	/**
	 * Returns the value of the '<em><b>Inaccessible Routes</b></em>' attribute list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.port.RouteOption}.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.port.RouteOption}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Inaccessible Routes</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Inaccessible Routes</em>' attribute list.
	 * @see com.mmxlabs.models.lng.port.RouteOption
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterInMarket_InaccessibleRoutes()
	 * @model
	 * @generated
	 */
	EList<RouteOption> getInaccessibleRoutes();

	/**
	 * Returns the value of the '<em><b>Generic Charter Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter Contract</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Generic Charter Contract</em>' reference.
	 * @see #isSetGenericCharterContract()
	 * @see #unsetGenericCharterContract()
	 * @see #setGenericCharterContract(GenericCharterContract)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterInMarket_GenericCharterContract()
	 * @model unsettable="true"
	 * @generated
	 */
	GenericCharterContract getGenericCharterContract();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getGenericCharterContract <em>Generic Charter Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Generic Charter Contract</em>' reference.
	 * @see #isSetGenericCharterContract()
	 * @see #unsetGenericCharterContract()
	 * @see #getGenericCharterContract()
	 * @generated
	 */
	void setGenericCharterContract(GenericCharterContract value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getGenericCharterContract <em>Generic Charter Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetGenericCharterContract()
	 * @see #getGenericCharterContract()
	 * @see #setGenericCharterContract(GenericCharterContract)
	 * @generated
	 */
	void unsetGenericCharterContract();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getGenericCharterContract <em>Generic Charter Contract</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Generic Charter Contract</em>' reference is set.
	 * @see #unsetGenericCharterContract()
	 * @see #getGenericCharterContract()
	 * @see #setGenericCharterContract(GenericCharterContract)
	 * @generated
	 */
	boolean isSetGenericCharterContract();

	/**
	 * Returns the value of the '<em><b>Charter In Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter In Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter In Rate</em>' attribute.
	 * @see #setCharterInRate(String)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterInMarket_CharterInRate()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='$/day'"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='charter'"
	 * @generated
	 */
	String getCharterInRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getCharterInRate <em>Charter In Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Charter In Rate</em>' attribute.
	 * @see #getCharterInRate()
	 * @generated
	 */
	void setCharterInRate(String value);

} // CharterInMarket

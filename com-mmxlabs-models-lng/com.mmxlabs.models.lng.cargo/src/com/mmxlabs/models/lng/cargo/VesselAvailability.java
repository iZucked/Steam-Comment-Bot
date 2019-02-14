/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo;

import com.mmxlabs.models.lng.commercial.BallastBonusContract;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CharterContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Availability</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#isFleet <em>Fleet</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#isOptional <em>Optional</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getVessel <em>Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getCharterNumber <em>Charter Number</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEntity <em>Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getTimeCharterRate <em>Time Charter Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartAt <em>Start At</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartAfter <em>Start After</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartBy <em>Start By</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEndAt <em>End At</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEndAfter <em>End After</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEndBy <em>End By</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartHeel <em>Start Heel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEndHeel <em>End Heel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#isForceHireCostOnlyEndRule <em>Force Hire Cost Only End Rule</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getRepositioningFee <em>Repositioning Fee</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getBallastBonusContract <em>Ballast Bonus Contract</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getCharterContract <em>Charter Contract</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getMinDuration <em>Min Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getMaxDuration <em>Max Duration</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability()
 * @model
 * @generated
 */
public interface VesselAvailability extends UUIDObject, VesselAssignmentType {
	/**
	 * Returns the value of the '<em><b>Fleet</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fleet</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fleet</em>' attribute.
	 * @see #setFleet(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_Fleet()
	 * @model
	 * @generated
	 */
	boolean isFleet();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#isFleet <em>Fleet</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fleet</em>' attribute.
	 * @see #isFleet()
	 * @generated
	 */
	void setFleet(boolean value);

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
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_Vessel()
	 * @model
	 * @generated
	 */
	Vessel getVessel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getVessel <em>Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel</em>' reference.
	 * @see #getVessel()
	 * @generated
	 */
	void setVessel(Vessel value);

	/**
	 * Returns the value of the '<em><b>Time Charter Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Time Charter Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Time Charter Rate</em>' attribute.
	 * @see #isSetTimeCharterRate()
	 * @see #unsetTimeCharterRate()
	 * @see #setTimeCharterRate(String)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_TimeCharterRate()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='$/day'"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='charter'"
	 * @generated
	 */
	String getTimeCharterRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getTimeCharterRate <em>Time Charter Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Time Charter Rate</em>' attribute.
	 * @see #isSetTimeCharterRate()
	 * @see #unsetTimeCharterRate()
	 * @see #getTimeCharterRate()
	 * @generated
	 */
	void setTimeCharterRate(String value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getTimeCharterRate <em>Time Charter Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetTimeCharterRate()
	 * @see #getTimeCharterRate()
	 * @see #setTimeCharterRate(String)
	 * @generated
	 */
	void unsetTimeCharterRate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getTimeCharterRate <em>Time Charter Rate</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Time Charter Rate</em>' attribute is set.
	 * @see #unsetTimeCharterRate()
	 * @see #getTimeCharterRate()
	 * @see #setTimeCharterRate(String)
	 * @generated
	 */
	boolean isSetTimeCharterRate();

	/**
	 * Returns the value of the '<em><b>Start At</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start At</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start At</em>' reference.
	 * @see #setStartAt(Port)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_StartAt()
	 * @model
	 * @generated
	 */
	Port getStartAt();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartAt <em>Start At</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start At</em>' reference.
	 * @see #getStartAt()
	 * @generated
	 */
	void setStartAt(Port value);

	/**
	 * Returns the value of the '<em><b>Start After</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start After</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start After</em>' attribute.
	 * @see #isSetStartAfter()
	 * @see #unsetStartAfter()
	 * @see #setStartAfter(LocalDateTime)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_StartAfter()
	 * @model unsettable="true" dataType="com.mmxlabs.models.datetime.LocalDateTime"
	 * @generated
	 */
	LocalDateTime getStartAfter();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartAfter <em>Start After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start After</em>' attribute.
	 * @see #isSetStartAfter()
	 * @see #unsetStartAfter()
	 * @see #getStartAfter()
	 * @generated
	 */
	void setStartAfter(LocalDateTime value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartAfter <em>Start After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetStartAfter()
	 * @see #getStartAfter()
	 * @see #setStartAfter(LocalDateTime)
	 * @generated
	 */
	void unsetStartAfter();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartAfter <em>Start After</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Start After</em>' attribute is set.
	 * @see #unsetStartAfter()
	 * @see #getStartAfter()
	 * @see #setStartAfter(LocalDateTime)
	 * @generated
	 */
	boolean isSetStartAfter();

	/**
	 * Returns the value of the '<em><b>Start By</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start By</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start By</em>' attribute.
	 * @see #isSetStartBy()
	 * @see #unsetStartBy()
	 * @see #setStartBy(LocalDateTime)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_StartBy()
	 * @model unsettable="true" dataType="com.mmxlabs.models.datetime.LocalDateTime"
	 * @generated
	 */
	LocalDateTime getStartBy();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartBy <em>Start By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start By</em>' attribute.
	 * @see #isSetStartBy()
	 * @see #unsetStartBy()
	 * @see #getStartBy()
	 * @generated
	 */
	void setStartBy(LocalDateTime value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartBy <em>Start By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetStartBy()
	 * @see #getStartBy()
	 * @see #setStartBy(LocalDateTime)
	 * @generated
	 */
	void unsetStartBy();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartBy <em>Start By</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Start By</em>' attribute is set.
	 * @see #unsetStartBy()
	 * @see #getStartBy()
	 * @see #setStartBy(LocalDateTime)
	 * @generated
	 */
	boolean isSetStartBy();

	/**
	 * Returns the value of the '<em><b>End At</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}<code>&lt;com.mmxlabs.models.lng.port.Port&gt;</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End At</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End At</em>' reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_EndAt()
	 * @model
	 * @generated
	 */
	EList<APortSet<Port>> getEndAt();

	/**
	 * Returns the value of the '<em><b>End After</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End After</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End After</em>' attribute.
	 * @see #isSetEndAfter()
	 * @see #unsetEndAfter()
	 * @see #setEndAfter(LocalDateTime)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_EndAfter()
	 * @model unsettable="true" dataType="com.mmxlabs.models.datetime.LocalDateTime"
	 * @generated
	 */
	LocalDateTime getEndAfter();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEndAfter <em>End After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End After</em>' attribute.
	 * @see #isSetEndAfter()
	 * @see #unsetEndAfter()
	 * @see #getEndAfter()
	 * @generated
	 */
	void setEndAfter(LocalDateTime value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEndAfter <em>End After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetEndAfter()
	 * @see #getEndAfter()
	 * @see #setEndAfter(LocalDateTime)
	 * @generated
	 */
	void unsetEndAfter();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEndAfter <em>End After</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>End After</em>' attribute is set.
	 * @see #unsetEndAfter()
	 * @see #getEndAfter()
	 * @see #setEndAfter(LocalDateTime)
	 * @generated
	 */
	boolean isSetEndAfter();

	/**
	 * Returns the value of the '<em><b>End By</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End By</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End By</em>' attribute.
	 * @see #isSetEndBy()
	 * @see #unsetEndBy()
	 * @see #setEndBy(LocalDateTime)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_EndBy()
	 * @model unsettable="true" dataType="com.mmxlabs.models.datetime.LocalDateTime"
	 * @generated
	 */
	LocalDateTime getEndBy();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEndBy <em>End By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End By</em>' attribute.
	 * @see #isSetEndBy()
	 * @see #unsetEndBy()
	 * @see #getEndBy()
	 * @generated
	 */
	void setEndBy(LocalDateTime value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEndBy <em>End By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetEndBy()
	 * @see #getEndBy()
	 * @see #setEndBy(LocalDateTime)
	 * @generated
	 */
	void unsetEndBy();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEndBy <em>End By</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>End By</em>' attribute is set.
	 * @see #unsetEndBy()
	 * @see #getEndBy()
	 * @see #setEndBy(LocalDateTime)
	 * @generated
	 */
	boolean isSetEndBy();

	/**
	 * Returns the value of the '<em><b>Start Heel</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Heel</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Heel</em>' containment reference.
	 * @see #setStartHeel(StartHeelOptions)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_StartHeel()
	 * @model containment="true" resolveProxies="true" required="true"
	 * @generated
	 */
	StartHeelOptions getStartHeel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getStartHeel <em>Start Heel</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Heel</em>' containment reference.
	 * @see #getStartHeel()
	 * @generated
	 */
	void setStartHeel(StartHeelOptions value);

	/**
	 * Returns the value of the '<em><b>End Heel</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Heel</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Heel</em>' containment reference.
	 * @see #setEndHeel(EndHeelOptions)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_EndHeel()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EndHeelOptions getEndHeel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEndHeel <em>End Heel</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Heel</em>' containment reference.
	 * @see #getEndHeel()
	 * @generated
	 */
	void setEndHeel(EndHeelOptions value);

	/**
	 * Returns the value of the '<em><b>Force Hire Cost Only End Rule</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Force Hire Cost Only End Rule</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Force Hire Cost Only End Rule</em>' attribute.
	 * @see #setForceHireCostOnlyEndRule(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_ForceHireCostOnlyEndRule()
	 * @model
	 * @generated
	 */
	boolean isForceHireCostOnlyEndRule();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#isForceHireCostOnlyEndRule <em>Force Hire Cost Only End Rule</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Force Hire Cost Only End Rule</em>' attribute.
	 * @see #isForceHireCostOnlyEndRule()
	 * @generated
	 */
	void setForceHireCostOnlyEndRule(boolean value);

	/**
	 * Returns the value of the '<em><b>Optional</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Optional</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Optional</em>' attribute.
	 * @see #setOptional(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_Optional()
	 * @model
	 * @generated
	 */
	boolean isOptional();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#isOptional <em>Optional</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Optional</em>' attribute.
	 * @see #isOptional()
	 * @generated
	 */
	void setOptional(boolean value);

	/**
	 * Returns the value of the '<em><b>Repositioning Fee</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Repositioning Fee</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Repositioning Fee</em>' attribute.
	 * @see #setRepositioningFee(String)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_RepositioningFee()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='$/day'"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='charter'"
	 * @generated
	 */
	String getRepositioningFee();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getRepositioningFee <em>Repositioning Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Repositioning Fee</em>' attribute.
	 * @see #getRepositioningFee()
	 * @generated
	 */
	void setRepositioningFee(String value);

	/**
	 * Returns the value of the '<em><b>Ballast Bonus Contract</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ballast Bonus Contract</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ballast Bonus Contract</em>' containment reference.
	 * @see #setBallastBonusContract(BallastBonusContract)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_BallastBonusContract()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	BallastBonusContract getBallastBonusContract();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getBallastBonusContract <em>Ballast Bonus Contract</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ballast Bonus Contract</em>' containment reference.
	 * @see #getBallastBonusContract()
	 * @generated
	 */
	void setBallastBonusContract(BallastBonusContract value);

	/**
	 * Returns the value of the '<em><b>Charter Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter Contract</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter Contract</em>' reference.
	 * @see #isSetCharterContract()
	 * @see #unsetCharterContract()
	 * @see #setCharterContract(CharterContract)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_CharterContract()
	 * @model unsettable="true"
	 * @generated
	 */
	CharterContract getCharterContract();

	/**
	 * Returns the value of the '<em><b>Charter Number</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter Number</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter Number</em>' attribute.
	 * @see #setCharterNumber(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_CharterNumber()
	 * @model default="1"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='#0'"
	 * @generated
	 */
	int getCharterNumber();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getCharterContract <em>Charter Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Charter Contract</em>' reference.
	 * @see #isSetCharterContract()
	 * @see #unsetCharterContract()
	 * @see #getCharterContract()
	 * @generated
	 */
	void setCharterContract(CharterContract value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getCharterContract <em>Charter Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetCharterContract()
	 * @see #getCharterContract()
	 * @see #setCharterContract(CharterContract)
	 * @generated
	 */
	void unsetCharterContract();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getCharterContract <em>Charter Contract</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Charter Contract</em>' reference is set.
	 * @see #unsetCharterContract()
	 * @see #getCharterContract()
	 * @see #setCharterContract(CharterContract)
	 * @generated
	 */
	boolean isSetCharterContract();

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
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_MinDuration()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='days' formatString='##0'"
	 * @generated
	 */
	int getMinDuration();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getMinDuration <em>Min Duration</em>}' attribute.
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
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getMinDuration <em>Min Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMinDuration()
	 * @see #getMinDuration()
	 * @see #setMinDuration(int)
	 * @generated
	 */
	void unsetMinDuration();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getMinDuration <em>Min Duration</em>}' attribute is set.
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
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_MaxDuration()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='days' formatString='##0'"
	 * @generated
	 */
	int getMaxDuration();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getMaxDuration <em>Max Duration</em>}' attribute.
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
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getMaxDuration <em>Max Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMaxDuration()
	 * @see #getMaxDuration()
	 * @see #setMaxDuration(int)
	 * @generated
	 */
	void unsetMaxDuration();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getMaxDuration <em>Max Duration</em>}' attribute is set.
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
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getCharterNumber <em>Charter Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Charter Number</em>' attribute.
	 * @see #getCharterNumber()
	 * @generated
	 */
	void setCharterNumber(int value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" dataType="com.mmxlabs.models.datetime.DateTime"
	 * @generated
	 */
	ZonedDateTime getStartByAsDateTime();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" dataType="com.mmxlabs.models.datetime.DateTime"
	 * @generated
	 */
	ZonedDateTime getStartAfterAsDateTime();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" dataType="com.mmxlabs.models.datetime.DateTime"
	 * @generated
	 */
	ZonedDateTime getEndByAsDateTime();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" dataType="com.mmxlabs.models.datetime.DateTime"
	 * @generated
	 */
	ZonedDateTime getEndAfterAsDateTime();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	BallastBonusContract getAvailabilityOrCharterContractBallastBonusContract();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	int getAvailabilityOrContractMinDuration();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	int getAvailabilityOrContractMaxDuration();

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
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getVesselAvailability_Entity()
	 * @model
	 * @generated
	 */
	BaseLegalEntity getEntity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.VesselAvailability#getEntity <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entity</em>' reference.
	 * @see #getEntity()
	 * @generated
	 */
	void setEntity(BaseLegalEntity value);

} // VesselAvailability

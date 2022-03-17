/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.commercial;

import com.mmxlabs.models.lng.port.Port;

import com.mmxlabs.models.lng.types.APortSet;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Generic Charter Contract</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.GenericCharterContract#getMinDuration <em>Min Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.GenericCharterContract#getMaxDuration <em>Max Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.GenericCharterContract#getRepositioningFeeTerms <em>Repositioning Fee Terms</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.GenericCharterContract#getBallastBonusTerms <em>Ballast Bonus Terms</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.GenericCharterContract#getStartHeel <em>Start Heel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.GenericCharterContract#getEndHeel <em>End Heel</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getGenericCharterContract()
 * @model
 * @generated
 */
public interface GenericCharterContract extends NamedObject, UUIDObject {
	/**
	 * Returns the value of the '<em><b>Min Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Duration</em>' attribute.
	 * @see #isSetMinDuration()
	 * @see #unsetMinDuration()
	 * @see #setMinDuration(int)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getGenericCharterContract_MinDuration()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='days' formatString='##0'"
	 * @generated
	 */
	int getMinDuration();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.GenericCharterContract#getMinDuration <em>Min Duration</em>}' attribute.
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
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.commercial.GenericCharterContract#getMinDuration <em>Min Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMinDuration()
	 * @see #getMinDuration()
	 * @see #setMinDuration(int)
	 * @generated
	 */
	void unsetMinDuration();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.commercial.GenericCharterContract#getMinDuration <em>Min Duration</em>}' attribute is set.
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
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Duration</em>' attribute.
	 * @see #isSetMaxDuration()
	 * @see #unsetMaxDuration()
	 * @see #setMaxDuration(int)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getGenericCharterContract_MaxDuration()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='days' formatString='##0'"
	 * @generated
	 */
	int getMaxDuration();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.GenericCharterContract#getMaxDuration <em>Max Duration</em>}' attribute.
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
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.commercial.GenericCharterContract#getMaxDuration <em>Max Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMaxDuration()
	 * @see #getMaxDuration()
	 * @see #setMaxDuration(int)
	 * @generated
	 */
	void unsetMaxDuration();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.commercial.GenericCharterContract#getMaxDuration <em>Max Duration</em>}' attribute is set.
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
	 * Returns the value of the '<em><b>Repositioning Fee Terms</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Repositioning Fee Terms</em>' containment reference.
	 * @see #setRepositioningFeeTerms(IRepositioningFee)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getGenericCharterContract_RepositioningFeeTerms()
	 * @model containment="true"
	 * @generated
	 */
	IRepositioningFee getRepositioningFeeTerms();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.GenericCharterContract#getRepositioningFeeTerms <em>Repositioning Fee Terms</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Repositioning Fee Terms</em>' containment reference.
	 * @see #getRepositioningFeeTerms()
	 * @generated
	 */
	void setRepositioningFeeTerms(IRepositioningFee value);

	/**
	 * Returns the value of the '<em><b>Ballast Bonus Terms</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ballast Bonus Terms</em>' containment reference.
	 * @see #setBallastBonusTerms(IBallastBonus)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getGenericCharterContract_BallastBonusTerms()
	 * @model containment="true"
	 * @generated
	 */
	IBallastBonus getBallastBonusTerms();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.GenericCharterContract#getBallastBonusTerms <em>Ballast Bonus Terms</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ballast Bonus Terms</em>' containment reference.
	 * @see #getBallastBonusTerms()
	 * @generated
	 */
	void setBallastBonusTerms(IBallastBonus value);

	/**
	 * Returns the value of the '<em><b>Start Heel</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Heel</em>' containment reference.
	 * @see #setStartHeel(StartHeelOptions)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getGenericCharterContract_StartHeel()
	 * @model containment="true" required="true"
	 * @generated
	 */
	StartHeelOptions getStartHeel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.GenericCharterContract#getStartHeel <em>Start Heel</em>}' containment reference.
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
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Heel</em>' containment reference.
	 * @see #setEndHeel(EndHeelOptions)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getGenericCharterContract_EndHeel()
	 * @model containment="true"
	 * @generated
	 */
	EndHeelOptions getEndHeel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.GenericCharterContract#getEndHeel <em>End Heel</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Heel</em>' containment reference.
	 * @see #getEndHeel()
	 * @generated
	 */
	void setEndHeel(EndHeelOptions value);

} // GenericCharterContract

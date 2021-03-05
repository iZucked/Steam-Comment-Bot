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
 *   <li>{@link com.mmxlabs.models.lng.commercial.GenericCharterContract#getTerms <em>Terms</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.GenericCharterContract#getHubs <em>Hubs</em>}</li>
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
	 * Returns the value of the '<em><b>Terms</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.commercial.CharterContractTerm}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Terms</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getGenericCharterContract_Terms()
	 * @model containment="true"
	 * @generated
	 */
	EList<CharterContractTerm> getTerms();

	/**
	 * Returns the value of the '<em><b>Hubs</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}<code>&lt;com.mmxlabs.models.lng.port.Port&gt;</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hubs</em>' reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getGenericCharterContract_Hubs()
	 * @model
	 * @generated
	 */
	EList<APortSet<Port>> getHubs();

} // GenericCharterContract

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.actuals;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.fleet.Vessel;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cargo Actuals</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.actuals.CargoActuals#getActuals <em>Actuals</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.CargoActuals#getReturnActuals <em>Return Actuals</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.CargoActuals#getContractYear <em>Contract Year</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.CargoActuals#getOperationNumber <em>Operation Number</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.CargoActuals#getSubOperationNumber <em>Sub Operation Number</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.CargoActuals#getSellerID <em>Seller ID</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.CargoActuals#getCargoReference <em>Cargo Reference</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.CargoActuals#getCargoReferenceSeller <em>Cargo Reference Seller</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.CargoActuals#getVessel <em>Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.CargoActuals#getCharterRatePerDay <em>Charter Rate Per Day</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.CargoActuals#getCargo <em>Cargo</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.CargoActuals#getBaseFuelPrice <em>Base Fuel Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.CargoActuals#getInsurancePremium <em>Insurance Premium</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getCargoActuals()
 * @model
 * @generated
 */
public interface CargoActuals extends EObject {
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
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getCargoActuals_BaseFuelPrice()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='MT/d' formatString='#,##0.###'"
	 * @generated
	 */
	double getBaseFuelPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getBaseFuelPrice <em>Base Fuel Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Fuel Price</em>' attribute.
	 * @see #getBaseFuelPrice()
	 * @generated
	 */
	void setBaseFuelPrice(double value);

	/**
	 * Returns the value of the '<em><b>Insurance Premium</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Insurance Premium</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Insurance Premium</em>' attribute.
	 * @see #setInsurancePremium(int)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getCargoActuals_InsurancePremium()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='$' formatString='###,###,##0'"
	 * @generated
	 */
	int getInsurancePremium();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getInsurancePremium <em>Insurance Premium</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Insurance Premium</em>' attribute.
	 * @see #getInsurancePremium()
	 * @generated
	 */
	void setInsurancePremium(int value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EList<EObject> getSortedActuals();

	/**
	 * Returns the value of the '<em><b>Actuals</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.actuals.SlotActuals}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Actuals</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Actuals</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getCargoActuals_Actuals()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<SlotActuals> getActuals();

	/**
	 * Returns the value of the '<em><b>Return Actuals</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Return Actuals</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Return Actuals</em>' containment reference.
	 * @see #setReturnActuals(ReturnActuals)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getCargoActuals_ReturnActuals()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	ReturnActuals getReturnActuals();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getReturnActuals <em>Return Actuals</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Return Actuals</em>' containment reference.
	 * @see #getReturnActuals()
	 * @generated
	 */
	void setReturnActuals(ReturnActuals value);

	/**
	 * Returns the value of the '<em><b>Contract Year</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contract Year</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contract Year</em>' attribute.
	 * @see #setContractYear(int)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getCargoActuals_ContractYear()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='####'"
	 * @generated
	 */
	int getContractYear();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getContractYear <em>Contract Year</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contract Year</em>' attribute.
	 * @see #getContractYear()
	 * @generated
	 */
	void setContractYear(int value);

	/**
	 * Returns the value of the '<em><b>Operation Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Operation Number</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Operation Number</em>' attribute.
	 * @see #setOperationNumber(int)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getCargoActuals_OperationNumber()
	 * @model
	 * @generated
	 */
	int getOperationNumber();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getOperationNumber <em>Operation Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Operation Number</em>' attribute.
	 * @see #getOperationNumber()
	 * @generated
	 */
	void setOperationNumber(int value);

	/**
	 * Returns the value of the '<em><b>Sub Operation Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sub Operation Number</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sub Operation Number</em>' attribute.
	 * @see #setSubOperationNumber(int)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getCargoActuals_SubOperationNumber()
	 * @model
	 * @generated
	 */
	int getSubOperationNumber();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getSubOperationNumber <em>Sub Operation Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sub Operation Number</em>' attribute.
	 * @see #getSubOperationNumber()
	 * @generated
	 */
	void setSubOperationNumber(int value);

	/**
	 * Returns the value of the '<em><b>Seller ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Seller ID</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Seller ID</em>' attribute.
	 * @see #setSellerID(String)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getCargoActuals_SellerID()
	 * @model
	 * @generated
	 */
	String getSellerID();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getSellerID <em>Seller ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Seller ID</em>' attribute.
	 * @see #getSellerID()
	 * @generated
	 */
	void setSellerID(String value);

	/**
	 * Returns the value of the '<em><b>Cargo Reference</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargo Reference</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo Reference</em>' attribute.
	 * @see #setCargoReference(String)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getCargoActuals_CargoReference()
	 * @model
	 * @generated
	 */
	String getCargoReference();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getCargoReference <em>Cargo Reference</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargo Reference</em>' attribute.
	 * @see #getCargoReference()
	 * @generated
	 */
	void setCargoReference(String value);

	/**
	 * Returns the value of the '<em><b>Cargo Reference Seller</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargo Reference Seller</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo Reference Seller</em>' attribute.
	 * @see #setCargoReferenceSeller(String)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getCargoActuals_CargoReferenceSeller()
	 * @model
	 * @generated
	 */
	String getCargoReferenceSeller();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getCargoReferenceSeller <em>Cargo Reference Seller</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargo Reference Seller</em>' attribute.
	 * @see #getCargoReferenceSeller()
	 * @generated
	 */
	void setCargoReferenceSeller(String value);

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
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getCargoActuals_Vessel()
	 * @model
	 * @generated
	 */
	Vessel getVessel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getVessel <em>Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel</em>' reference.
	 * @see #getVessel()
	 * @generated
	 */
	void setVessel(Vessel value);

	/**
	 * Returns the value of the '<em><b>Charter Rate Per Day</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter Rate Per Day</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter Rate Per Day</em>' attribute.
	 * @see #setCharterRatePerDay(double)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getCargoActuals_CharterRatePerDay()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='$/day' formatString='###,##0.##'"
	 * @generated
	 */
	double getCharterRatePerDay();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getCharterRatePerDay <em>Charter Rate Per Day</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Charter Rate Per Day</em>' attribute.
	 * @see #getCharterRatePerDay()
	 * @generated
	 */
	void setCharterRatePerDay(double value);

	/**
	 * Returns the value of the '<em><b>Cargo</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargo</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo</em>' reference.
	 * @see #setCargo(Cargo)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getCargoActuals_Cargo()
	 * @model
	 * @generated
	 */
	Cargo getCargo();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getCargo <em>Cargo</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargo</em>' reference.
	 * @see #getCargo()
	 * @generated
	 */
	void setCargo(Cargo value);

} // CargoActuals

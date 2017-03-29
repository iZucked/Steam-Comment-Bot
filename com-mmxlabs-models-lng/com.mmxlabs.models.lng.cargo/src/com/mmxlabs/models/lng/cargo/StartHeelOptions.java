/**
 */
package com.mmxlabs.models.lng.cargo;

import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Start Heel Options</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.StartHeelOptions#getCvValue <em>Cv Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.StartHeelOptions#getMinVolumeAvailable <em>Min Volume Available</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.StartHeelOptions#getMaxVolumeAvailable <em>Max Volume Available</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.StartHeelOptions#getPriceExpression <em>Price Expression</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getStartHeelOptions()
 * @model
 * @generated
 */
public interface StartHeelOptions extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cv Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cv Value</em>' attribute.
	 * @see #setCvValue(double)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getStartHeelOptions_CvValue()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='mmBtu/m\263' formatString='#0.######'"
	 * @generated
	 */
	double getCvValue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.StartHeelOptions#getCvValue <em>Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cv Value</em>' attribute.
	 * @see #getCvValue()
	 * @generated
	 */
	void setCvValue(double value);

	/**
	 * Returns the value of the '<em><b>Min Volume Available</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Volume Available</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Volume Available</em>' attribute.
	 * @see #isSetMinVolumeAvailable()
	 * @see #unsetMinVolumeAvailable()
	 * @see #setMinVolumeAvailable(double)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getStartHeelOptions_MinVolumeAvailable()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m\263' formatString='###,##0.###'"
	 * @generated
	 */
	double getMinVolumeAvailable();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.StartHeelOptions#getMinVolumeAvailable <em>Min Volume Available</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Volume Available</em>' attribute.
	 * @see #isSetMinVolumeAvailable()
	 * @see #unsetMinVolumeAvailable()
	 * @see #getMinVolumeAvailable()
	 * @generated
	 */
	void setMinVolumeAvailable(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.StartHeelOptions#getMinVolumeAvailable <em>Min Volume Available</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMinVolumeAvailable()
	 * @see #getMinVolumeAvailable()
	 * @see #setMinVolumeAvailable(double)
	 * @generated
	 */
	void unsetMinVolumeAvailable();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.StartHeelOptions#getMinVolumeAvailable <em>Min Volume Available</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Min Volume Available</em>' attribute is set.
	 * @see #unsetMinVolumeAvailable()
	 * @see #getMinVolumeAvailable()
	 * @see #setMinVolumeAvailable(double)
	 * @generated
	 */
	boolean isSetMinVolumeAvailable();

	/**
	 * Returns the value of the '<em><b>Max Volume Available</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Volume Available</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Volume Available</em>' attribute.
	 * @see #isSetMaxVolumeAvailable()
	 * @see #unsetMaxVolumeAvailable()
	 * @see #setMaxVolumeAvailable(double)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getStartHeelOptions_MaxVolumeAvailable()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m\263' formatString='###,##0.###'"
	 * @generated
	 */
	double getMaxVolumeAvailable();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.StartHeelOptions#getMaxVolumeAvailable <em>Max Volume Available</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Volume Available</em>' attribute.
	 * @see #isSetMaxVolumeAvailable()
	 * @see #unsetMaxVolumeAvailable()
	 * @see #getMaxVolumeAvailable()
	 * @generated
	 */
	void setMaxVolumeAvailable(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.StartHeelOptions#getMaxVolumeAvailable <em>Max Volume Available</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMaxVolumeAvailable()
	 * @see #getMaxVolumeAvailable()
	 * @see #setMaxVolumeAvailable(double)
	 * @generated
	 */
	void unsetMaxVolumeAvailable();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.StartHeelOptions#getMaxVolumeAvailable <em>Max Volume Available</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Max Volume Available</em>' attribute is set.
	 * @see #unsetMaxVolumeAvailable()
	 * @see #getMaxVolumeAvailable()
	 * @see #setMaxVolumeAvailable(double)
	 * @generated
	 */
	boolean isSetMaxVolumeAvailable();

	/**
	 * Returns the value of the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Price Expression</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Price Expression</em>' attribute.
	 * @see #setPriceExpression(String)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getStartHeelOptions_PriceExpression()
	 * @model annotation="http://www.mmxlabs.com/models/pricing/expressionType type='commodity'"
	 * @generated
	 */
	String getPriceExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.StartHeelOptions#getPriceExpression <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price Expression</em>' attribute.
	 * @see #getPriceExpression()
	 * @generated
	 */
	void setPriceExpression(String value);

} // StartHeelOptions

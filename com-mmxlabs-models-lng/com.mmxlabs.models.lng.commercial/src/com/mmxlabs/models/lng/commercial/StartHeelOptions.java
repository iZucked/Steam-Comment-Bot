/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.commercial;

import com.mmxlabs.models.mmxcore.MMXObject;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Start Heel Options</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.StartHeelOptions#getCvValue <em>Cv Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.StartHeelOptions#getMinVolumeAvailable <em>Min Volume Available</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.StartHeelOptions#getMaxVolumeAvailable <em>Max Volume Available</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.StartHeelOptions#getPriceExpression <em>Price Expression</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getStartHeelOptions()
 * @model
 * @generated
 */
public interface StartHeelOptions extends EObject {
	/**
	 * Returns the value of the '<em><b>Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cv Value</em>' attribute.
	 * @see #setCvValue(double)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getStartHeelOptions_CvValue()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='mmBtu/m\263' formatString='#0.######'"
	 * @generated
	 */
	double getCvValue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.StartHeelOptions#getCvValue <em>Cv Value</em>}' attribute.
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
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Volume Available</em>' attribute.
	 * @see #setMinVolumeAvailable(double)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getStartHeelOptions_MinVolumeAvailable()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m\263' formatString='###,##0.###'"
	 * @generated
	 */
	double getMinVolumeAvailable();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.StartHeelOptions#getMinVolumeAvailable <em>Min Volume Available</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Volume Available</em>' attribute.
	 * @see #getMinVolumeAvailable()
	 * @generated
	 */
	void setMinVolumeAvailable(double value);

	/**
	 * Returns the value of the '<em><b>Max Volume Available</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Volume Available</em>' attribute.
	 * @see #setMaxVolumeAvailable(double)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getStartHeelOptions_MaxVolumeAvailable()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m\263' formatString='###,##0.###'"
	 * @generated
	 */
	double getMaxVolumeAvailable();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.StartHeelOptions#getMaxVolumeAvailable <em>Max Volume Available</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Volume Available</em>' attribute.
	 * @see #getMaxVolumeAvailable()
	 * @generated
	 */
	void setMaxVolumeAvailable(double value);

	/**
	 * Returns the value of the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Price Expression</em>' attribute.
	 * @see #setPriceExpression(String)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getStartHeelOptions_PriceExpression()
	 * @model annotation="http://www.mmxlabs.com/models/pricing/expressionType type='commodity'"
	 * @generated
	 */
	String getPriceExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.StartHeelOptions#getPriceExpression <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price Expression</em>' attribute.
	 * @see #getPriceExpression()
	 * @generated
	 */
	void setPriceExpression(String value);

} // StartHeelOptions

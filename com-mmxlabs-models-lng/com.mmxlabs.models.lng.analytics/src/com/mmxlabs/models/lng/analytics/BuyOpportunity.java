/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import java.time.LocalDate;

import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Buy Opportunity</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#isDesPurchase <em>Des Purchase</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getPort <em>Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getContract <em>Contract</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getDate <em>Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getPriceExpression <em>Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getEntity <em>Entity</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getCv <em>Cv</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getCancellationExpression <em>Cancellation Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getMiscCosts <em>Misc Costs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getVolumeMode <em>Volume Mode</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getVolumeUnits <em>Volume Units</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getMinVolume <em>Min Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getMaxVolume <em>Max Volume</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBuyOpportunity()
 * @model
 * @generated
 */
public interface BuyOpportunity extends MMXObject, BuyOption {
	/**
	 * Returns the value of the '<em><b>Des Purchase</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Des Purchase</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Des Purchase</em>' attribute.
	 * @see #setDesPurchase(boolean)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBuyOpportunity_DesPurchase()
	 * @model
	 * @generated
	 */
	boolean isDesPurchase();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#isDesPurchase <em>Des Purchase</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Des Purchase</em>' attribute.
	 * @see #isDesPurchase()
	 * @generated
	 */
	void setDesPurchase(boolean value);

	/**
	 * Returns the value of the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port</em>' reference.
	 * @see #setPort(Port)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBuyOpportunity_Port()
	 * @model
	 * @generated
	 */
	Port getPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getPort <em>Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port</em>' reference.
	 * @see #getPort()
	 * @generated
	 */
	void setPort(Port value);

	/**
	 * Returns the value of the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contract</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contract</em>' reference.
	 * @see #setContract(PurchaseContract)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBuyOpportunity_Contract()
	 * @model
	 * @generated
	 */
	PurchaseContract getContract();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getContract <em>Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contract</em>' reference.
	 * @see #getContract()
	 * @generated
	 */
	void setContract(PurchaseContract value);

	/**
	 * Returns the value of the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Date</em>' attribute.
	 * @see #setDate(LocalDate)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBuyOpportunity_Date()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getDate <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Date</em>' attribute.
	 * @see #getDate()
	 * @generated
	 */
	void setDate(LocalDate value);

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
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBuyOpportunity_PriceExpression()
	 * @model annotation="http://www.mmxlabs.com/models/pricing/expressionType type='commodity'"
	 * @generated
	 */
	String getPriceExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getPriceExpression <em>Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price Expression</em>' attribute.
	 * @see #getPriceExpression()
	 * @generated
	 */
	void setPriceExpression(String value);

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
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBuyOpportunity_Entity()
	 * @model
	 * @generated
	 */
	BaseLegalEntity getEntity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getEntity <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entity</em>' reference.
	 * @see #getEntity()
	 * @generated
	 */
	void setEntity(BaseLegalEntity value);

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
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBuyOpportunity_Cv()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='#0.###'"
	 * @generated
	 */
	double getCv();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getCv <em>Cv</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cv</em>' attribute.
	 * @see #getCv()
	 * @generated
	 */
	void setCv(double value);

	/**
	 * Returns the value of the '<em><b>Cancellation Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cancellation Expression</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cancellation Expression</em>' attribute.
	 * @see #setCancellationExpression(String)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBuyOpportunity_CancellationExpression()
	 * @model annotation="http://www.mmxlabs.com/models/pricing/expressionType type='commodity'"
	 * @generated
	 */
	String getCancellationExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getCancellationExpression <em>Cancellation Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cancellation Expression</em>' attribute.
	 * @see #getCancellationExpression()
	 * @generated
	 */
	void setCancellationExpression(String value);

	/**
	 * Returns the value of the '<em><b>Misc Costs</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Misc Costs</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Misc Costs</em>' attribute.
	 * @see #setMiscCosts(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBuyOpportunity_MiscCosts()
	 * @model annotation="http://www.mmxlabs.com/models/pricing/expressionType type='commodity'"
	 * @generated
	 */
	int getMiscCosts();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getMiscCosts <em>Misc Costs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Misc Costs</em>' attribute.
	 * @see #getMiscCosts()
	 * @generated
	 */
	void setMiscCosts(int value);

	/**
	 * Returns the value of the '<em><b>Volume Mode</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.analytics.VolumeMode}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume Mode</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Mode</em>' attribute.
	 * @see com.mmxlabs.models.lng.analytics.VolumeMode
	 * @see #setVolumeMode(VolumeMode)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBuyOpportunity_VolumeMode()
	 * @model
	 * @generated
	 */
	VolumeMode getVolumeMode();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getVolumeMode <em>Volume Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Mode</em>' attribute.
	 * @see com.mmxlabs.models.lng.analytics.VolumeMode
	 * @see #getVolumeMode()
	 * @generated
	 */
	void setVolumeMode(VolumeMode value);

	/**
	 * Returns the value of the '<em><b>Volume Units</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.types.VolumeUnits}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume Units</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Units</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.VolumeUnits
	 * @see #setVolumeUnits(VolumeUnits)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBuyOpportunity_VolumeUnits()
	 * @model
	 * @generated
	 */
	VolumeUnits getVolumeUnits();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getVolumeUnits <em>Volume Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Units</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.VolumeUnits
	 * @see #getVolumeUnits()
	 * @generated
	 */
	void setVolumeUnits(VolumeUnits value);

	/**
	 * Returns the value of the '<em><b>Min Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Volume</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Volume</em>' attribute.
	 * @see #setMinVolume(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBuyOpportunity_MinVolume()
	 * @model
	 * @generated
	 */
	int getMinVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getMinVolume <em>Min Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Volume</em>' attribute.
	 * @see #getMinVolume()
	 * @generated
	 */
	void setMinVolume(int value);

	/**
	 * Returns the value of the '<em><b>Max Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Volume</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Volume</em>' attribute.
	 * @see #setMaxVolume(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBuyOpportunity_MaxVolume()
	 * @model
	 * @generated
	 */
	int getMaxVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BuyOpportunity#getMaxVolume <em>Max Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Volume</em>' attribute.
	 * @see #getMaxVolume()
	 * @generated
	 */
	void setMaxVolume(int value);

} // end of  BuyOpportunity

// finish type fixing

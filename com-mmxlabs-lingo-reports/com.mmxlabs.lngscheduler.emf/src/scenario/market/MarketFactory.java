/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.market;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see scenario.market.MarketPackage
 * @generated
 */
public interface MarketFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MarketFactory eINSTANCE = scenario.market.impl.MarketFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	MarketModel createMarketModel();

	/**
	 * Returns a new object of class '<em>Stepwise Price Curve</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Stepwise Price Curve</em>'.
	 * @generated
	 */
	StepwisePriceCurve createStepwisePriceCurve();

	/**
	 * Returns a new object of class '<em>Stepwise Price</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Stepwise Price</em>'.
	 * @generated
	 */
	StepwisePrice createStepwisePrice();

	/**
	 * Returns a new object of class '<em>Market</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Market</em>'.
	 * @generated
	 */
	Market createMarket();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	MarketPackage getMarketPackage();

} //MarketFactory

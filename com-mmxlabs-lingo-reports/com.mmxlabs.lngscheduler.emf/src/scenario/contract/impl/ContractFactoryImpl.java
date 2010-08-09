/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.contract.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import scenario.contract.ContractFactory;
import scenario.contract.ContractModel;
import scenario.contract.ContractPackage;
import scenario.contract.PurchaseContract;
import scenario.contract.SalesContract;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ContractFactoryImpl extends EFactoryImpl implements ContractFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ContractFactory init() {
		try {
			ContractFactory theContractFactory = (ContractFactory)EPackage.Registry.INSTANCE.getEFactory("http://com.mmxlabs.lng.emf/contract"); 
			if (theContractFactory != null) {
				return theContractFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ContractFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ContractFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case ContractPackage.CONTRACT_MODEL: return createContractModel();
			case ContractPackage.PURCHASE_CONTRACT: return createPurchaseContract();
			case ContractPackage.SALES_CONTRACT: return createSalesContract();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ContractModel createContractModel() {
		ContractModelImpl contractModel = new ContractModelImpl();
		return contractModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PurchaseContract createPurchaseContract() {
		PurchaseContractImpl purchaseContract = new PurchaseContractImpl();
		return purchaseContract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SalesContract createSalesContract() {
		SalesContractImpl salesContract = new SalesContractImpl();
		return salesContract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ContractPackage getContractPackage() {
		return (ContractPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ContractPackage getPackage() {
		return ContractPackage.eINSTANCE;
	}

} //ContractFactoryImpl

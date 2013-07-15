/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ChildCreationExtenderManager;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IChildCreationExtender;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.util.CommercialSwitch;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.spotmarkets.util.SpotMarketsAdapterFactory;

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers.
 * The adapters generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged fireNotifyChanged}.
 * The adapters also support Eclipse property sheets.
 * Note that most of the adapters are shared among multiple instances.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SpotMarketsItemProviderAdapterFactory extends SpotMarketsAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable, IChildCreationExtender {
	/**
	 * This keeps track of the root adapter factory that delegates to this adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ComposedAdapterFactory parentAdapterFactory;

	/**
	 * This is used to implement {@link org.eclipse.emf.edit.provider.IChangeNotifier}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IChangeNotifier changeNotifier = new ChangeNotifier();

	/**
	 * This helps manage the child creation extenders.
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ChildCreationExtenderManager childCreationExtenderManager = new ChildCreationExtenderManager(SpotMarketsEditPlugin.INSTANCE, SpotMarketsPackage.eNS_URI);

	/**
	 * This keeps track of all the supported types checked by {@link #isFactoryForType isFactoryForType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Collection<Object> supportedTypes = new ArrayList<Object>();

	/**
	 * This constructs an instance.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotMarketsItemProviderAdapterFactory() {
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
		supportedTypes.add(IItemPropertySource.class);
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SpotMarketsModelItemProvider spotMarketsModelItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSpotMarketsModelAdapter() {
		if (spotMarketsModelItemProvider == null) {
			spotMarketsModelItemProvider = new SpotMarketsModelItemProvider(this);
		}

		return spotMarketsModelItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.spotmarkets.CharterCostModel} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CharterCostModelItemProvider charterCostModelItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.spotmarkets.CharterCostModel}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCharterCostModelAdapter() {
		if (charterCostModelItemProvider == null) {
			charterCostModelItemProvider = new CharterCostModelItemProvider(this);
		}

		return charterCostModelItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SpotMarketGroupItemProvider spotMarketGroupItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSpotMarketGroupAdapter() {
		if (spotMarketGroupItemProvider == null) {
			spotMarketGroupItemProvider = new SpotMarketGroupItemProvider(this);
		}

		return spotMarketGroupItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DESPurchaseMarketItemProvider desPurchaseMarketItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createDESPurchaseMarketAdapter() {
		if (desPurchaseMarketItemProvider == null) {
			desPurchaseMarketItemProvider = new DESPurchaseMarketItemProvider(this);
		}

		return desPurchaseMarketItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.spotmarkets.DESSalesMarket} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DESSalesMarketItemProvider desSalesMarketItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.spotmarkets.DESSalesMarket}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createDESSalesMarketAdapter() {
		if (desSalesMarketItemProvider == null) {
			desSalesMarketItemProvider = new DESSalesMarketItemProvider(this);
		}

		return desSalesMarketItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FOBPurchasesMarketItemProvider fobPurchasesMarketItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createFOBPurchasesMarketAdapter() {
		if (fobPurchasesMarketItemProvider == null) {
			fobPurchasesMarketItemProvider = new FOBPurchasesMarketItemProvider(this);
		}

		return fobPurchasesMarketItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FOBSalesMarketItemProvider fobSalesMarketItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createFOBSalesMarketAdapter() {
		if (fobSalesMarketItemProvider == null) {
			fobSalesMarketItemProvider = new FOBSalesMarketItemProvider(this);
		}

		return fobSalesMarketItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.spotmarkets.SpotAvailability} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SpotAvailabilityItemProvider spotAvailabilityItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.spotmarkets.SpotAvailability}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSpotAvailabilityAdapter() {
		if (spotAvailabilityItemProvider == null) {
			spotAvailabilityItemProvider = new SpotAvailabilityItemProvider(this);
		}

		return spotAvailabilityItemProvider;
	}

	/**
	 * This returns the root adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ComposeableAdapterFactory getRootAdapterFactory() {
		return parentAdapterFactory == null ? this : parentAdapterFactory.getRootAdapterFactory();
	}

	/**
	 * This sets the composed adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory) {
		this.parentAdapterFactory = parentAdapterFactory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object type) {
		return supportedTypes.contains(type) || super.isFactoryForType(type);
	}

	/**
	 * This implementation substitutes the factory itself as the key for the adapter.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter adapt(Notifier notifier, Object type) {
		return super.adapt(notifier, this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object adapt(Object object, Object type) {
		if (isFactoryForType(type)) {
			Object adapter = super.adapt(object, type);
			if (!(type instanceof Class<?>) || (((Class<?>)type).isInstance(adapter))) {
				return adapter;
			}
		}

		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List<IChildCreationExtender> getChildCreationExtenders() {
		return childCreationExtenderManager.getChildCreationExtenders();
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<?> getNewChildDescriptors(Object object, EditingDomain editingDomain) {
		return childCreationExtenderManager.getNewChildDescriptors(object, editingDomain);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return childCreationExtenderManager;
	}

	/**
	 * This adds a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void addListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.addListener(notifyChangedListener);
	}

	/**
	 * This removes a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void removeListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.removeListener(notifyChangedListener);
	}

	/**
	 * This delegates to {@link #changeNotifier} and to {@link #parentAdapterFactory}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void fireNotifyChanged(Notification notification) {
		changeNotifier.fireNotifyChanged(notification);

		if (parentAdapterFactory != null) {
			parentAdapterFactory.fireNotifyChanged(notification);
		}
	}

	/**
	 * This disposes all of the item providers created by this factory. 
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void dispose() {
		if (spotMarketsModelItemProvider != null) spotMarketsModelItemProvider.dispose();
		if (charterCostModelItemProvider != null) charterCostModelItemProvider.dispose();
		if (spotMarketGroupItemProvider != null) spotMarketGroupItemProvider.dispose();
		if (desPurchaseMarketItemProvider != null) desPurchaseMarketItemProvider.dispose();
		if (desSalesMarketItemProvider != null) desSalesMarketItemProvider.dispose();
		if (fobPurchasesMarketItemProvider != null) fobPurchasesMarketItemProvider.dispose();
		if (fobSalesMarketItemProvider != null) fobSalesMarketItemProvider.dispose();
		if (spotAvailabilityItemProvider != null) spotAvailabilityItemProvider.dispose();
	}

	/**
	 * A child creation extender for the {@link CommercialPackage}.
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static class CommercialChildCreationExtender implements IChildCreationExtender {
		/**
		 * The switch for creating child descriptors specific to each extended class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		protected static class CreationSwitch extends CommercialSwitch<Object> {
			/**
			 * The child descriptors being populated.
			 * <!-- begin-user-doc -->
			 * <!-- end-user-doc -->
			 * @generated
			 */
			protected List<Object> newChildDescriptors;

			/**
			 * The domain in which to create the children.
			 * <!-- begin-user-doc -->
			 * <!-- end-user-doc -->
			 * @generated
			 */
			protected EditingDomain editingDomain;

			/**
			 * Creates the a switch for populating child descriptors in the given domain.
			 * <!-- begin-user-doc -->
			 * <!-- end-user-doc -->
			 * @generated
			 */
			CreationSwitch(List<Object> newChildDescriptors, EditingDomain editingDomain) {
				this.newChildDescriptors = newChildDescriptors;
				this.editingDomain = editingDomain;
			}
			/**
			 * <!-- begin-user-doc -->
			 * <!-- end-user-doc -->
			 * @generated
			 */
			@Override
			public Object caseCommercialModel(CommercialModel object) {
				newChildDescriptors.add
					(createChildParameter
						(CommercialPackage.Literals.COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS,
						 SpotMarketsFactory.eINSTANCE.createSpotMarketsModel()));

				newChildDescriptors.add
					(createChildParameter
						(CommercialPackage.Literals.COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS,
						 SpotMarketsFactory.eINSTANCE.createDESPurchaseMarket()));

				newChildDescriptors.add
					(createChildParameter
						(CommercialPackage.Literals.COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS,
						 SpotMarketsFactory.eINSTANCE.createDESSalesMarket()));

				newChildDescriptors.add
					(createChildParameter
						(CommercialPackage.Literals.COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS,
						 SpotMarketsFactory.eINSTANCE.createFOBPurchasesMarket()));

				newChildDescriptors.add
					(createChildParameter
						(CommercialPackage.Literals.COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS,
						 SpotMarketsFactory.eINSTANCE.createFOBSalesMarket()));

				return null;
			}
 
			/**
			 * <!-- begin-user-doc -->
			 * <!-- end-user-doc -->
			 * @generated
			 */
			protected CommandParameter createChildParameter(Object feature, Object child) {
				return new CommandParameter(null, feature, child);
			}

		}

		/**
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		@Override
		public Collection<Object> getNewChildDescriptors(Object object, EditingDomain editingDomain) {
			ArrayList<Object> result = new ArrayList<Object>();
			new CreationSwitch(result, editingDomain).doSwitch((EObject)object);
			return result;
		}

		/**
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		@Override
		public ResourceLocator getResourceLocator() {
			return SpotMarketsEditPlugin.INSTANCE;
		}
	}

}

/**
 */
package com.mmxlabs.models.lng.analytics.provider;


import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;

import com.mmxlabs.models.mmxcore.provider.NamedObjectItemProvider;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IChildCreationExtender;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class OptionAnalysisModelItemProvider 
	extends NamedObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OptionAnalysisModelItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addUseTargetPNLPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Use Target PNL feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addUseTargetPNLPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_OptionAnalysisModel_useTargetPNL_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_OptionAnalysisModel_useTargetPNL_feature", "_UI_OptionAnalysisModel_type"),
				 AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__USE_TARGET_PNL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BUYS);
			childrenFeatures.add(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SELLS);
			childrenFeatures.add(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BASE_CASE);
			childrenFeatures.add(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES);
			childrenFeatures.add(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__RULES);
			childrenFeatures.add(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
			childrenFeatures.add(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__RESULT_SETS);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns OptionAnalysisModel.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/OptionAnalysisModel"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((OptionAnalysisModel)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_OptionAnalysisModel_type") :
			getString("_UI_OptionAnalysisModel_type") + " " + label;
	}
	

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(OptionAnalysisModel.class)) {
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__USE_TARGET_PNL:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__BUYS:
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__SELLS:
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__BASE_CASE:
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES:
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__RULES:
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__PARTIAL_CASE:
			case AnalyticsPackage.OPTION_ANALYSIS_MODEL__RESULT_SETS:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BUYS,
				 AnalyticsFactory.eINSTANCE.createBuyOpportunity()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BUYS,
				 AnalyticsFactory.eINSTANCE.createBuyMarket()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BUYS,
				 AnalyticsFactory.eINSTANCE.createBuyReference()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SELLS,
				 AnalyticsFactory.eINSTANCE.createSellOpportunity()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SELLS,
				 AnalyticsFactory.eINSTANCE.createSellMarket()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SELLS,
				 AnalyticsFactory.eINSTANCE.createSellReference()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__BASE_CASE,
				 AnalyticsFactory.eINSTANCE.createBaseCase()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES,
				 AnalyticsFactory.eINSTANCE.createShippingOption()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES,
				 AnalyticsFactory.eINSTANCE.createFleetShippingOption()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES,
				 AnalyticsFactory.eINSTANCE.createRoundTripShippingOption()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__SHIPPING_TEMPLATES,
				 AnalyticsFactory.eINSTANCE.createNominatedShippingOption()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__RULES,
				 AnalyticsFactory.eINSTANCE.createModeOptionRule()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE,
				 AnalyticsFactory.eINSTANCE.createPartialCase()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__RESULT_SETS,
				 AnalyticsFactory.eINSTANCE.createResultSet()));
	}

}

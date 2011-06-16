/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.optimiser.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import scenario.optimiser.OptimisationSettings;
import scenario.optimiser.OptimiserFactory;
import scenario.optimiser.OptimiserPackage;

import scenario.provider.LngEditPlugin;
import scenario.provider.NamedObjectItemProvider;

/**
 * This is the item provider adapter for a {@link scenario.optimiser.OptimisationSettings} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class OptimisationSettingsItemProvider
	extends NamedObjectItemProvider
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OptimisationSettingsItemProvider(AdapterFactory adapterFactory) {
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

			addRandomSeedPropertyDescriptor(object);
			addInitialSchedulePropertyDescriptor(object);
			addDefaultDiscountCurvePropertyDescriptor(object);
			addFreezeDaysFromStartPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Random Seed feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRandomSeedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_OptimisationSettings_randomSeed_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_OptimisationSettings_randomSeed_feature", "_UI_OptimisationSettings_type"),
				 OptimiserPackage.Literals.OPTIMISATION_SETTINGS__RANDOM_SEED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Initial Schedule feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addInitialSchedulePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_OptimisationSettings_initialSchedule_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_OptimisationSettings_initialSchedule_feature", "_UI_OptimisationSettings_type"),
				 OptimiserPackage.Literals.OPTIMISATION_SETTINGS__INITIAL_SCHEDULE,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Default Discount Curve feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDefaultDiscountCurvePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_OptimisationSettings_defaultDiscountCurve_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_OptimisationSettings_defaultDiscountCurve_feature", "_UI_OptimisationSettings_type"),
				 OptimiserPackage.Literals.OPTIMISATION_SETTINGS__DEFAULT_DISCOUNT_CURVE,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Freeze Days From Start feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addFreezeDaysFromStartPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_OptimisationSettings_freezeDaysFromStart_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_OptimisationSettings_freezeDaysFromStart_feature", "_UI_OptimisationSettings_type"),
				 OptimiserPackage.Literals.OPTIMISATION_SETTINGS__FREEZE_DAYS_FROM_START,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
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
			childrenFeatures.add(OptimiserPackage.Literals.OPTIMISATION_SETTINGS__CONSTRAINTS);
			childrenFeatures.add(OptimiserPackage.Literals.OPTIMISATION_SETTINGS__OBJECTIVES);
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
	 * This returns OptimisationSettings.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/OptimisationSettings"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((OptimisationSettings)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_OptimisationSettings_type") :
			getString("_UI_OptimisationSettings_type") + " " + label;
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

		switch (notification.getFeatureID(OptimisationSettings.class)) {
			case OptimiserPackage.OPTIMISATION_SETTINGS__RANDOM_SEED:
			case OptimiserPackage.OPTIMISATION_SETTINGS__FREEZE_DAYS_FROM_START:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case OptimiserPackage.OPTIMISATION_SETTINGS__CONSTRAINTS:
			case OptimiserPackage.OPTIMISATION_SETTINGS__OBJECTIVES:
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
				(OptimiserPackage.Literals.OPTIMISATION_SETTINGS__CONSTRAINTS,
				 OptimiserFactory.eINSTANCE.createConstraint()));

		newChildDescriptors.add
			(createChildParameter
				(OptimiserPackage.Literals.OPTIMISATION_SETTINGS__OBJECTIVES,
				 OptimiserFactory.eINSTANCE.createObjective()));
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return LngEditPlugin.INSTANCE;
	}

}

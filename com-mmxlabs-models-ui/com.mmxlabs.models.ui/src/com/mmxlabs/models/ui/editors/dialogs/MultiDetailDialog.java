/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.databinding.ObservablesManager;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import com.google.common.collect.Lists;
import com.mmxlabs.common.Equality;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeFactory;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorExternalNotificationListener;
import com.mmxlabs.models.ui.forms.AbstractDataBindingFormDialog;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.models.util.emfpath.EMFUtils;
import com.mmxlabs.rcp.common.actions.AbstractMenuAction;

enum SetMode {
	IGNORE, REPLACE, UNION, INTERSECTION
}

/**
 * A dialog which lets you apply the same edit to multiple target objects simultaneously.
 * 
 * TODO sort out fake multi-value editors like the enum editor.
 * 
 * @author hinton
 * 
 */
public class MultiDetailDialog extends AbstractDataBindingFormDialog {
	private IDisplayComposite displayComposite;
	/**
	 * The top composite in which we store our detail views
	 */
	private Composite dialogArea = null;

	private final MMXRootObject rootObject;
	private final IInlineEditorWrapper wrapper = new EditorWrapper();
	private final ICommandHandler commandHandler;

	private DefaultExtraValidationContext validationContext;

	/**
	 * Track all the controls that have been created, so we can disable them after setInput(), which will re-enable them otherwise.
	 */
	private final List<IInlineEditor> controlsToDisable = new LinkedList<IInlineEditor>();

	/**
	 * This list of EObjects contains the proxy structure being edited in place of the real input
	 */
	private final List<EObject> proxies = new ArrayList<EObject>();

	/**
	 * This maps from proxy objects and features to set modes
	 */
	private final Map<Pair<EObject, EStructuralFeature>, SetMode> featuresToSet = new HashMap<Pair<EObject, EStructuralFeature>, SetMode>();
	private EClass editingClass;
	private List<EObject> editedObjects;
	private final Map<EObject, List<EObject>> proxyCounterparts = new HashMap<EObject, List<EObject>>();
	private IDisplayCompositeFactory displayCompositeFactory;
	// private IScenarioEditingLocation scenarioEditingLocation;
	private IDialogEditingContext dialogContext;

	public MultiDetailDialog(final Shell parentShell, final MMXRootObject root, final ICommandHandler commandHandler) {
		super(parentShell);
		this.commandHandler = commandHandler;
		this.rootObject = root;
	}

	private void disableControls() {
		for (final IInlineEditor c : controlsToDisable) {
			c.setEditorEnabled(false);
		}
		controlsToDisable.clear();
	}

	@Override
	public void create() {
		super.create();
		updateEditor();
	}

	private void updateEditor() {

		if (observablesManager != null) {
			observablesManager.dispose();
		}
		if (dbc != null) {
			dbc.dispose();
		}

		this.dbc = new EMFDataBindingContext();
		this.observablesManager = new ObservablesManager();

		// This call means we do not need to manually manage our databinding objects lifecycle manually.
		observablesManager.runAndCollect(new Runnable() {

			@Override
			public void run() {
				doCreateFormContent();
			}
		});

	}

	/**
	 * Create an editor view for the selected object and display it.
	 * 
	 */
	@Override
	protected void doCreateFormContent() {

		if (displayComposite != null) {
			displayComposite.getComposite().dispose();
			displayComposite = null;
		}

		displayCompositeFactory = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(editingClass);
		createProxies();

		displayComposite = displayCompositeFactory.createToplevelComposite(dialogArea, editingClass, dialogContext, toolkit);
		displayComposite.getComposite().setLayoutData(new GridData(GridData.FILL_BOTH));

		displayComposite.setEditorWrapper(wrapper);
		final ICommandHandler immediate = new ICommandHandler() {
			@Override
			public void handleCommand(final Command command, final EObject target, final EStructuralFeature feature) {
				command.execute();
			}

			@Override
			public IReferenceValueProviderProvider getReferenceValueProviderProvider() {
				return commandHandler.getReferenceValueProviderProvider();
			}

			@Override
			public EditingDomain getEditingDomain() {
				return commandHandler.getEditingDomain();
			}
		};
		displayComposite.setCommandHandler(immediate);
		displayComposite.display(dialogContext, rootObject, proxies.get(proxies.size() - 1), proxies, dbc);
		disableControls();
		resizeAndCenter(false);
	}

	/**
	 */
	@Override
	protected void createFormContent(final IManagedForm managedForm) {

		this.managedForm = managedForm;
		this.toolkit = managedForm.getToolkit();

		final ScrolledForm form = managedForm.getForm();
		form.setLayoutData(new GridData(GridData.FILL_BOTH));
		form.setText("");
		toolkit.decorateFormHeading(form.getForm());
		{
			final GridLayout layout = new GridLayout(1, true);
			form.getBody().setLayout(layout);
		}
		final Composite c = managedForm.getForm().getBody();

		dialogArea = c;
	}

	private void createProxies() {
		final List<List<EObject>> ranges = new ArrayList<List<EObject>>(editedObjects.size());

		List<Class<?>> classes = null;

		for (final EObject object : editedObjects) {
			// NOTE: it is important that each object produces the same type of array. That is the same number of elements and each element at a given index is of the same type in all ranges.
			final List<IComponentHelper> componentHelpers = Activator.getDefault().getComponentHelperRegistry().getComponentHelpers(object.eClass());
			final List<EObject> range = new LinkedList<>();
			for (final IComponentHelper h : componentHelpers) {
				range.addAll(h.getExternalEditingRange(rootObject, object));
			}

			// final List<EObject> range = displayCompositeFactory.getExternalEditingRange(rootObject, object);
			// Remove existing copies of the object
			while (range.contains(object)) {
				range.remove(object);
			}
			range.add(object);
			ranges.add(range);

			// Find the common classes in the ranges
			if (classes == null) {
				// First time - add all
				classes = new LinkedList<>();
				for (final Object o : range) {
					classes.add(o.getClass());
				}
			} else {
				// Get the list of classes in this object
				final List<Class<?>> rangeClasses = new LinkedList<>();
				for (final Object o : range) {
					rangeClasses.add(o.getClass());
				}

				// Next determine the intersection. Note we could have the same class instance appear twice in the list, thus we cannot use retainAll()
				final Set<Class<?>> missingClasses = new HashSet<>();
				for (final Class<?> c : classes) {
					if (rangeClasses.contains(c)) {
						// Found match, remove from range
						rangeClasses.remove(c);
					} else {
						// Not found, remove from common class list
						missingClasses.add(c);
					}
				}

				// Reduce classes list to the common classes
				for (final Class<?> c : missingClasses) {
					classes.remove(c);
				}

				// Break out early
				if (classes.isEmpty()) {
					throw new IllegalArgumentException("Unable to multi-edit - data has nothing common");
				}
			}
		}

		if (classes == null) {
			throw new IllegalArgumentException("Unable to multi-edit - no data");
		}

		final List<EObject> range0 = ranges.get(0);
		// Extract common object references in order of first element
		final EObject[] commonRange = new EObject[classes.size()];
		{
			// Take a copy of this array so we can remove elements to get correct indices in case of duplicate classes
			final List<Class<?>> classesCpy = new ArrayList<>(classes);
			for (final EObject o : range0) {
				final int idx = classesCpy.indexOf(o.getClass());
				if (idx != -1) {
					commonRange[idx] = o;
					classesCpy.set(idx, null);
				}
			}
		}

		proxies.addAll(EcoreUtil.copyAll(Lists.newArrayList(commonRange)));

		// now set equal attributes.
		for (int i = 0; i < proxies.size(); i++) {
			final EObject proxy = proxies.get(i);
			final ArrayList<EObject> originals = new ArrayList<EObject>(editedObjects.size());
			for (final List<EObject> range : ranges) {

				// Ensure originals array is in the same order as all other arrays.
				for (final EObject obj : range) {
					if (obj.getClass().equals(classes.get(i))) {
						originals.add(obj);
						// Remove so as to avoid picking it again if we have duplicate classes
						range.remove(obj);
						break;
					}
				}
			}
			setSameValues(proxy, originals);
			setCounterparts(proxy, originals);
		}
	}

	private void setCounterparts(final EObject proxy, final List<EObject> originals) {
		if (proxy == null) {
			return;
		}
		proxyCounterparts.put(proxy, originals);
		for (final EReference reference : proxy.eClass().getEAllContainments()) {
			if (!reference.isMany()) {
				final ArrayList<EObject> subOriginals = new ArrayList<EObject>(originals.size());
				for (final EObject original : originals) {
					subOriginals.add((EObject) original.eGet(reference));
				}
				setCounterparts((EObject) proxy.eGet(reference), subOriginals);
			}
		}
	}

	private void applyProxies() {
		final CompoundCommand command = new CompoundCommand();
		command.append(IdentityCommand.INSTANCE); // add the identity command so that even if we set no features the command is executable

		for (final Map.Entry<Pair<EObject, EStructuralFeature>, SetMode> featureToSet : featuresToSet.entrySet()) {
			final EObject proxy = featureToSet.getKey().getFirst();
			final EStructuralFeature feature = featureToSet.getKey().getSecond();
			final SetMode mode = feature.isMany() ? featureToSet.getValue() : SetMode.REPLACE;
			switch (mode) {
			case REPLACE:
				final Object newValue = (!feature.isUnsettable() || proxy.eIsSet(feature)) ? proxy.eGet(feature) : SetCommand.UNSET_VALUE;
				for (final EObject original : proxyCounterparts.get(proxy)) {
					command.append(SetCommand.create(commandHandler.getEditingDomain(), original, feature, newValue));
				}
				break;
			case UNION:
			case INTERSECTION:
				final List<Object> newValues = (List) proxy.eGet(feature);
				for (final EObject original : proxyCounterparts.get(proxy)) {
					final List<Object> oldValues = (List) original.eGet(feature);

					final LinkedHashSet<Object> lhs = new LinkedHashSet<Object>();
					lhs.addAll(oldValues);
					if (mode == SetMode.UNION) {
						lhs.addAll(newValues);
					} else {
						lhs.retainAll(newValues);
					}
					final ArrayList<Object> combination = new ArrayList<Object>(lhs);
					command.append(SetCommand.create(commandHandler.getEditingDomain(), original, feature, combination));
				}
				break;
			}
		}

		commandHandler.getEditingDomain().getCommandStack().execute(command);
	}

	/**
	 * Opens a multiple detail editor dialog allowing the user to edit multiple
	 * existing objects in place. 
	 *  
	 * @param location
	 * @param objects
	 * @return
	 */
	public int open(final IScenarioEditingLocation location, final List<EObject> objects) {
		this.dialogContext = new DefaultDialogEditingContext(new NullDialogController(), location, true, false);

		this.editedObjects = objects;
		editingClass = EMFUtils.findCommonSuperclass(objects);

		final int result;
		if ((result = open()) == OK) {
			applyProxies();
		}

		// undo change from createProxies above
		// ValidationSupport.getInstance().clearContainers(proxies);

		return result;
	}

	/**
	 * Set the fields which are the same on all objects in multiples onto target.
	 * 
	 * @param target
	 * @param multiples
	 */
	void setSameValues(final EObject target, final List<EObject> multiples) {
		if (target == null) {
			return;
		}
		attribute_loop: for (final EStructuralFeature feature : target.eClass().getEAllStructuralFeatures()) {
			boolean gotValue = false;
			Object value = null;
			if (feature instanceof EReference) {
				if (((EReference) feature).isContainment()) {
					continue attribute_loop;
				}
			}

			for (final EObject m : multiples) {
				if (m == null) {
					return;
				}
				final Object mValue = m.eGet(feature);
				if (!gotValue) {
					gotValue = true;
					value = mValue;
				} else {
					if (Equality.isEqual(value, mValue) == false) {
						continue attribute_loop;
					}
				}
			}

			target.eSet(feature, value);
		}

		// now do contained objects
		final List<EObject> containedObjects = new ArrayList<EObject>(multiples.size());
		for (final EReference c : target.eClass().getEAllContainments()) {
			if (c.isMany()) {
				continue;
			}
			containedObjects.clear();
			for (final EObject m : multiples) {
				containedObjects.add((EObject) m.eGet(c));
			}
			setSameValues((EObject) target.eGet(c), containedObjects);
		}
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	private class EditorWrapper implements IInlineEditorWrapper {
		@Override
		public IInlineEditor wrap(final IInlineEditor proxy) {
			if (proxy == null)
				return null;

			if (proxy.getFeature() == MMXCorePackage.eINSTANCE.getNamedObject_Name()) {
				return null;
			}

			if (proxy.getFeature() == null) {
				return null;// hack that filters out assignment editor
			}

			return new IInlineEditor() {
				private Pair<EObject, EStructuralFeature> key;

				@Override
				public void processValidation(final IStatus status) {
					proxy.processValidation(status);

				};

				@Override
				public Control createControl(final Composite parent, final EMFDataBindingContext dbc, final FormToolkit toolkit) {
					final Composite composite = toolkit.createComposite(parent);

					final GridLayout layout = new GridLayout(2, false);
					layout.marginHeight = 0;
					layout.marginWidth = 0;
					composite.setLayout(layout);

					final Composite c2 = toolkit.createComposite(composite, SWT.NONE);
					final GridLayout layout2 = new GridLayout(1, false);
					layout2.marginHeight = 0;
					layout2.marginWidth = 0;

					c2.setLayoutData(new GridData(GridData.FILL_BOTH));

					c2.setLayout(layout2);

					final Control sub = proxy.createControl(c2, dbc, toolkit);
					sub.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

					// we can't disable here any more, as the later setInput() call will re-enable
					controlsToDisable.add(proxy);

					final ToolBarManager manager = new ToolBarManager(SWT.NONE);
					final Pair<EObject, EStructuralFeature> pair = new Pair<EObject, EStructuralFeature>(null, getFeature());
					this.key = pair;
					manager.add(new Action("Set", IAction.AS_CHECK_BOX) {
						@Override
						public void run() {
							// ControlUtils.setControlEnabled(sub, isChecked());
							proxy.setEditorEnabled(isChecked());
							if (isChecked()) {
								featuresToSet.put(pair, SetMode.REPLACE);
							} else {
								featuresToSet.remove(pair);
							}
						}
					});
					if (proxy.getFeature() != null) {
						if (proxy.getFeature().isMany() && ((proxy.getFeature() instanceof EAttribute) || (((EReference) proxy.getFeature()).isContainment() == false))) {
							manager.add(new MultiFeatureAction(pair, featuresToSet));
						}
					}

					final ToolBar tb = manager.createControl(composite);
					toolkit.adapt(tb, true, true);
					final GridData gd = new GridData();
					// TODO fix magic number - measure width properly and set everywhere
					// gd.minimumWidth = gd.widthHint = ;
					tb.setLayoutData(gd);

					return composite;
				}

				@Override
				public EStructuralFeature getFeature() {
					return proxy.getFeature();
				}

				@Override
				public void setCommandHandler(final ICommandHandler handler) {
					proxy.setCommandHandler(handler);
				}

				@Override
				public void display(final IDialogEditingContext dialogContext, final MMXRootObject scenario, final EObject object, final Collection<EObject> range) {
					proxy.display(dialogContext, scenario, object, range);
					key.setFirst(proxy.getEditorTarget());
				}

				@Override
				public void setLabel(final Label label) {
					proxy.setLabel(label);
				}

				@Override
				public void setEditorEnabled(final boolean enabled) {
					proxy.setEditorEnabled(enabled);
				}

				@Override
				public EObject getEditorTarget() {
					return proxy.getEditorTarget();
				}

				@Override
				public Label getLabel() {
					return proxy.getLabel();
				}

				@Override
				public void setEditorLocked(final boolean locked) {
					proxy.setEditorLocked(locked);

				}

				@Override
				public boolean isEditorLocked() {
					return proxy.isEditorLocked();
				}

				@Override
				public boolean isEditorEnabled() {
					return proxy.isEditorEnabled();
				}

				@Override
				public void setEditorVisible(final boolean visible) {
					proxy.setEditorVisible(visible);
				}

				@Override
				public boolean isEditorVisible() {
					return proxy.isEditorVisible();
				}

				@Override
				public void addNotificationChangedListener(final IInlineEditorExternalNotificationListener listener) {
					// TODO Auto-generated method stub

				}

				@Override
				public void removeNotificationChangedListener(final IInlineEditorExternalNotificationListener listener) {
					// TODO Auto-generated method stub

				}
				
				@Override
				public boolean hasLabel() {
					return true;
				}
			};
		}
	}
}

class MultiFeatureAction extends AbstractMenuAction {
	public static final SetMode[] MODES = new SetMode[] { SetMode.REPLACE, SetMode.INTERSECTION, SetMode.UNION };

	private SetMode mode = SetMode.REPLACE;
	private final Pair<EObject, EStructuralFeature> path;
	private final Map<Pair<EObject, EStructuralFeature>, SetMode> map;

	public MultiFeatureAction(final Pair<EObject, EStructuralFeature> p, final Map<Pair<EObject, EStructuralFeature>, SetMode> m) {
		super("");
		this.map = m;
		this.path = p;
	}

	/**
	 * @param lastMenu2
	 */
	@Override
	protected void populate(final Menu menu) {
		for (final SetMode s : MODES) {
			final Action a = new Action(s.toString(), IAction.AS_RADIO_BUTTON) {
				@Override
				public void run() {
					mode = s;
					map.put(path, s);
				}
			};

			// a.setActionDefinitionId(mode.toString());
			final ActionContributionItem actionContributionItem = new ActionContributionItem(a);
			actionContributionItem.fill(menu, -1);

			// Set initially checked item.
			if (s.equals(mode)) {
				a.setChecked(true);
			}
		}
	}

}
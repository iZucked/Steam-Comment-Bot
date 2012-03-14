/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.dialogs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
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
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;
import com.mmxlabs.models.ui.editors.util.ControlUtils;
import com.mmxlabs.models.ui.validation.ValidationSupport;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.models.util.emfpath.EMFUtils;
import com.mmxlabs.rcp.common.actions.AbstractMenuAction;
enum SetMode {
	IGNORE,
	REPLACE,
	UNION,
	INTERSECTION
}
/**
 * A dialog which lets you apply the same edit to multiple target objects simultaneously.
 * 
 * TODO sort out fake multi-value editors like the enum editor. 
 * 
 * @author hinton
 *
 */
public class MultiDetailDialog extends Dialog {	
	private IDisplayComposite displayComposite;
	private final MMXRootObject rootObject;
	private final IInlineEditorWrapper wrapper = new EditorWrapper();
	private final ICommandHandler commandHandler;
	
	/**
	 * Track all the controls that have been created, so we can disable them after setInput(), which will re-enable them otherwise.
	 */
	private final List<Control> controlsToDisable = new LinkedList<Control>();
	
	/**
	 * This list of EObjects contains the proxy structure being edited in place of the real input
	 */
	private List<EObject> proxies = new ArrayList<EObject>();

	/**
	 * This maps from proxy objects and features to set modes
	 */
	private Map<Pair<EObject, EStructuralFeature>, SetMode> featuresToSet = new HashMap<Pair<EObject, EStructuralFeature>, SetMode>();
	private EClass editingClass;
	private List<EObject> editedObjects;
	private Map<EObject, List<EObject>> proxyCounterparts = new HashMap<EObject, List<EObject>>();
	


	public MultiDetailDialog(final Shell parentShell, final MMXRootObject root, final ICommandHandler commandHandler) {
		super(parentShell);
		this.commandHandler = commandHandler;
		this.rootObject = root;
	}

	private void disableControls() {
		for (final Control c : controlsToDisable) {
			ControlUtils.setControlEnabled(c, false);
		}
		controlsToDisable.clear();
	}

	@Override
	public void create() {
		super.create();
		createProxies();
		
		displayComposite.setEditorWrapper(wrapper);
		final ICommandHandler immediate = new ICommandHandler() {
			@Override
			public void handleCommand(Command command, EObject target, EStructuralFeature feature) {
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
		displayComposite.display(rootObject, proxies.get(0));
		disableControls();
		resizeAndCenter();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite c = (Composite) super.createDialogArea(parent);
		displayComposite = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(editingClass).
				createToplevelComposite(c, editingClass);
		displayComposite.getComposite().setLayoutData(new GridData(GridData.FILL_BOTH));
		return c;
	}

	private void createProxies() {
		final List<List<EObject>> ranges = new ArrayList<List<EObject>>(editedObjects.size());
		
		for (final EObject object : editedObjects) {
			ranges.add(displayComposite.getEditingRange(rootObject, object));
		}
		
		final List<EObject> range0 = ranges.get(0);
		proxies.addAll(EcoreUtil.copyAll(range0));
		
		// clear attributes on proxies
		
		// now set equal attributes.
		for (int i = 0; i<proxies.size(); i++) {
			final EObject proxy = proxies.get(i);
			final ArrayList<EObject> originals = new ArrayList<EObject>(editedObjects.size());
			for (final List<EObject> range : ranges) {
				originals.add(range.get(i));
			}
			final Pair<EObject, EReference> c = ValidationSupport.getInstance().getContainer(range0.get(i));
			ValidationSupport.getInstance().setContainers(Collections.singleton(proxies.get(i)), c.getFirst(), c.getSecond());
			setSameValues(proxy, originals);
			setCounterparts(proxy, originals);
		}
	}
	
	private void setCounterparts(final EObject proxy, final List<EObject> originals) {
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
				final Object newValue = 
				(!feature.isUnsettable() ||
						proxy.eIsSet(feature)) ?
								proxy.eGet(feature) : SetCommand.UNSET_VALUE;
				for (final EObject original : proxyCounterparts.get(proxy)) {
					command.append(
							SetCommand.create(commandHandler.getEditingDomain(),
									original, feature, newValue));
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
	
	public int open(final List<EObject> objects) {
		this.editedObjects = objects;
		editingClass = EMFUtils.findCommonSuperclass(objects);
		
		final int result;
		if ((result = open()) == OK) {
			applyProxies();
		}
		
		// undo change from createProxies above
		ValidationSupport.getInstance().clearContainers(proxies);
		
		return result;
	}

	/**
	 * Set the fields which are the same on all objects in multiples onto target.
	 * 
	 * @param target
	 * @param multiples
	 */
	void setSameValues(final EObject target, final List<EObject> multiples) {
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

	private void resizeAndCenter() {
		final Shell shell = getShell();
		if (shell != null) {
			shell.layout(true);

			shell.setSize(shell.computeSize(SWT.DEFAULT, SWT.DEFAULT));

			final Rectangle shellBounds = getParentShell().getBounds();
			final Point dialogSize = shell.getSize();

			shell.setLocation(shellBounds.x + ((shellBounds.width - dialogSize.x) / 2), shellBounds.y + ((shellBounds.height - dialogSize.y) / 2));
		}
	}
	
	private class EditorWrapper implements IInlineEditorWrapper {
		@Override
		public IInlineEditor wrap(final IInlineEditor proxy) {
			if (proxy.getFeature() == MMXCorePackage.eINSTANCE.getNamedObject_Name()) {
				return null;
			}

			return new IInlineEditor() {
				private Pair<EObject, EStructuralFeature> key;

				@Override
				public void processValidation(final IStatus status) {
					proxy.processValidation(status);

				};

				@Override
				public Control createControl(final Composite parent) {
					final Composite composite = new Composite(parent, SWT.NONE);

					final GridLayout layout = new GridLayout(2, false);
					layout.marginHeight = 0;
					layout.marginWidth = 0;
					composite.setLayout(layout);

					final Composite c2 = new Composite(composite, SWT.NONE);
					final GridLayout layout2 = new GridLayout(1, false);
					layout2.marginHeight = 0;
					layout2.marginWidth = 0;

					c2.setLayoutData(new GridData(GridData.FILL_BOTH));

					c2.setLayout(layout2);

					final Control sub = proxy.createControl(c2);
					sub.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

					// we can't disable here any more, as the later setInput() call will re-enable
					controlsToDisable.add(sub);

					final ToolBarManager manager = new ToolBarManager(SWT.NONE);
					final Pair<EObject, EStructuralFeature> pair = 
							new Pair<EObject, EStructuralFeature>(null, getFeature());
					this.key = pair;
					manager.add(new Action("Set", IAction.AS_CHECK_BOX) {
						@Override
						public void run() {
							ControlUtils.setControlEnabled(sub, isChecked());
							if (isChecked()) {
								featuresToSet.put(pair, SetMode.REPLACE);
							} else {
								featuresToSet.remove(pair);
							}
						}
					});

					if (proxy.getFeature().isMany() && ((proxy.getFeature() instanceof EAttribute) || (((EReference) proxy.getFeature()).isContainment() == false))) {
						manager.add(new MultiFeatureAction(pair, featuresToSet));
					}

					final ToolBar tb = manager.createControl(composite);
					final GridData gd = new GridData();
					// TODO fix magic number - measure width properly and set everywhere
					gd.minimumWidth = gd.widthHint = 64;
					tb.setLayoutData(gd);

					return composite;
				}

				@Override
				public EStructuralFeature getFeature() {
					return proxy.getFeature();
				}

				@Override
				public void setCommandHandler(ICommandHandler handler) {
					proxy.setCommandHandler(handler);
				}
				
				@Override
				public void display(MMXRootObject scenario, EObject object) {
					key.setFirst(object);
					proxy.display(scenario, object);
				}

				@Override
				public void setLabel(Label label) {
					proxy.setLabel(label);
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
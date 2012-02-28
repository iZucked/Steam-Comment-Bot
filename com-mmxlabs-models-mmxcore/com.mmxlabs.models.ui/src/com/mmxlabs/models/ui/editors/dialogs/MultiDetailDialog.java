/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.IShellProvider;

/**
 * A dialog for editing the same field on several objects simultaneously.
 * 
 * @author hinton
 * 
 */
public abstract class MultiDetailDialog extends Dialog {

	protected MultiDetailDialog(IShellProvider parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}
	
//	private final IDisplayComposite displayComposite;
//	
//	/**
//	 * Track all the controls that have been created, so we can disable them after setInput(), which will re-enable them otherwise.
//	 */
//	private final List<Control> controlsToDisable = new LinkedList<Control>();
//	private EObject proxy;
//
//	/**
//	 * A set which describes which features need to be set in the output command
//	 */
//	private final Set<Pair<EMFPath, EStructuralFeature>> featuresToSet = new HashSet<Pair<EMFPath, EStructuralFeature>>();
//	private final Map<Pair<EMFPath, EStructuralFeature>, String> setMode = new HashMap<Pair<EMFPath, EStructuralFeature>, String>();
//
//	private EditingDomain editingDomain;
//
//	public MultiDetailDialog(final Shell parentShell, final IValueProviderProvider valueProviderProvider, final EditingDomain editingDomain) {
//		super(parentShell);
//		dcc = new DetailCompositeContainer(valueProviderProvider, editingDomain, ICommandProcessor.EXECUTE, new IInlineEditorWrapper() {
//			@Override
//			public IInlineEditor wrap(final IInlineEditor proxy) {
//				if (proxy.getFeature() == ScenarioPackage.eINSTANCE.getNamedObject_Name()) {
//					return null;
//				}
//				if (proxy.getFeature() == CargoPackage.eINSTANCE.getSlot_Id()) {
//					return null;
//				}
//				if (proxy.getFeature() == CargoPackage.eINSTANCE.getCargo_Id()) {
//					return null;
//				}
//				if (proxy.getFeature() == FleetPackage.eINSTANCE.getVesselEvent_Id()) {
//					return null;
//				}
//
//				return new IInlineEditor() {
//
//					@Override
//					public void setInput(final EObject object) {
//						proxy.setInput(object);
//					}
//
//					@Override
//					public void processValidation(final IStatus status) {
//						proxy.processValidation(status);
//
//					};
//
//					@Override
//					public Control createControl(final Composite parent) {
//						final Composite composite = new Composite(parent, SWT.NONE);
//
//						final GridLayout layout = new GridLayout(2, false);
//						layout.marginHeight = 0;
//						layout.marginWidth = 0;
//						composite.setLayout(layout);
//
//						final Composite c2 = new Composite(composite, SWT.NONE);
//						final GridLayout layout2 = new GridLayout(1, false);
//						layout2.marginHeight = 0;
//						layout2.marginWidth = 0;
//
//						c2.setLayoutData(new GridData(GridData.FILL_BOTH));
//
//						c2.setLayout(layout2);
//
//						final Control sub = proxy.createControl(c2);
//						sub.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//
//						// we can't disable here any more, as the later setInput() call will re-enable
//						controlsToDisable.add(sub);
//
//						final ToolBarManager manager = new ToolBarManager(SWT.NONE);
//						final Pair<EMFPath, EStructuralFeature> pair = new Pair<EMFPath, EStructuralFeature>(getPath(), getFeature());
//						manager.add(new Action("Set", IAction.AS_CHECK_BOX) {
//							@Override
//							public void run() {
//								ControlUtils.setControlEnabled(sub, isChecked());
//								if (isChecked()) {
//									featuresToSet.add(pair);
//								} else {
//									featuresToSet.remove(pair);
//								}
//							}
//						});
//
//						if (proxy.getFeature().isMany() && ((proxy.getFeature() instanceof EAttribute) || (((EReference) proxy.getFeature()).isContainment() == false))) {
//							manager.add(new MultiFeatureAction(pair, setMode));
//						}
//
//						final ToolBar tb = manager.createControl(composite);
//						final GridData gd = new GridData();
//						// TODO fix magic number - measure width properly and set everywhere
//						gd.minimumWidth = gd.widthHint = 64;
//						tb.setLayoutData(gd);
//
//						return composite;
//					}
//
//					@Override
//					public EStructuralFeature getFeature() {
//						return proxy.getFeature();
//					}
//
//					@Override
//					public EMFPath getPath() {
//						return proxy.getPath();
//					}
//				};
//			}
//		});
//		this.editingDomain = editingDomain;
//	}
//
//	@Override
//	protected Control createDialogArea(final Composite parent) {
//		final Composite area = (Composite) super.createDialogArea(parent);
//		final GridLayout layout = new GridLayout();
//		layout.marginHeight = 0;
//		layout.marginWidth = 0;
//		area.setLayout(layout);
//		return area;
//	}
//
//	private void resizeAndCenter() {
//		final Shell shell = getShell();
//		if (shell != null) {
//			shell.layout(true);
//
//			shell.setSize(shell.computeSize(SWT.DEFAULT, SWT.DEFAULT));
//
//			final Rectangle shellBounds = getParentShell().getBounds();
//			final Point dialogSize = shell.getSize();
//
//			shell.setLocation(shellBounds.x + ((shellBounds.width - dialogSize.x) / 2), shellBounds.y + ((shellBounds.height - dialogSize.y) / 2));
//		}
//	}
//
//	private void displayProxy() {
//		final AbstractDetailComposite editor = dcc.getDetailView(proxy.eClass(), (Composite) getDialogArea());
//		editor.setInput(proxy);
//		disableControls();
//	}
//
//	private void disableControls() {
//		for (final Control c : controlsToDisable) {
//			ControlUtils.setControlEnabled(c, false);
//		}
//		controlsToDisable.clear();
//	}
//
//	@Override
//	public void create() {
//		super.create();
//		displayProxy();
//		resizeAndCenter();
//	}
//
//	public int open(final List<EObject> objects) {
//		final EClass editingClass = EMFUtils.findCommonSuperclass(objects);
//		proxy = EMFUtils.createEObject(editingClass);
//		final Pair<EObject, EReference> c = ValidationSupport.getInstance().getContainer(objects.get(0));
//		try {
//			ValidationSupport.getInstance().setContainers(Collections.singleton(proxy), c.getFirst(), c.getSecond());
//
//			final HashSet<AutoCorrector> correctors = new HashSet<AutoCorrector>();
//			for (final EObject object : objects) {
//				// add autocorrector adapters.
//				for (final Object adapter : object.eAdapters()) {
//					if (adapter instanceof AutoCorrector) {
//						correctors.add((AutoCorrector) adapter);
//					}
//				}
//			}
//
//			proxy.eAdapters().addAll(correctors);
//
//			setSameValues(proxy, objects);
//
//			final int result = open();
//			if (result == OK) {
//				final CompoundCommand cc = new CompoundCommand();
//				for (final Pair<EMFPath, EStructuralFeature> p : featuresToSet) {
//					final Object value;
//					if (p.getSecond().isUnsettable() && (((EObject) p.getFirst().get(proxy)).eIsSet(p.getSecond()) == false)) {
//						value = SetCommand.UNSET_VALUE;
//					} else {
//						value = ((EObject) p.getFirst().get(proxy)).eGet(p.getSecond());
//					}
//
//					final String mode = setMode.get(p);
//					if ((p.getSecond().isMany() == false) || (mode == null) || mode.equals(MultiFeatureAction.REPLACE)) {
//						for (final EObject object : objects) {
//							cc.append(SetCommand.create(editingDomain, p.getFirst().get(object), p.getSecond(), value));
//						}
//					} else {
//						if (mode.equals(MultiFeatureAction.UNION)) {
//							final List unionWith = (List) value;
//							for (final EObject object : objects) {
//								final ArrayList<Object> add = new ArrayList<Object>();
//								final List<Object> values = (List<Object>) ((EObject) p.getFirst().get(object)).eGet(p.getSecond());
//								add.addAll(values);
//								for (final Object o : unionWith) {
//									if (add.contains(o) == false) {
//										add.add(o);
//									}
//								}
//								cc.append(SetCommand.create(editingDomain, p.getFirst().get(object), p.getSecond(), add));
//							}
//						} else {
//							final List intersectWith = (List) value;
//							for (final EObject object : objects) {
//								final ArrayList<Object> drop = new ArrayList<Object>();
//								final List<Object> values = (List<Object>) ((EObject) p.getFirst().get(object)).eGet(p.getSecond());
//								for (final Object o : intersectWith) {
//									if (values.contains(o)) {
//										drop.add(o);
//									}
//								}
//
//								cc.append(SetCommand.create(editingDomain, p.getFirst().get(object), p.getSecond(), drop));
//							}
//						}
//					}
//				}
//				cc.canExecute();
//				editingDomain.getCommandStack().execute(cc);
//			}
//			return result;
//		} finally {
//			ValidationSupport.getInstance().clearContainers(Collections.singleton(proxy));
//		}
//	}
//
//	/**
//	 * Set the fields which are the same on all objects in multiples onto target.
//	 * 
//	 * @param target
//	 * @param multiples
//	 */
//	void setSameValues(final EObject target, final List<EObject> multiples) {
//		attribute_loop: for (final EStructuralFeature feature : target.eClass().getEAllStructuralFeatures()) {
//			boolean gotValue = false;
//			Object value = null;
//			if (feature instanceof EReference) {
//				if (((EReference) feature).isContainment()) {
//					continue attribute_loop;
//				}
//			}
//
//			for (final EObject m : multiples) {
//				if (m == null) {
//					return;
//				}
//				final Object mValue = m.eGet(feature);
//				if (!gotValue) {
//					gotValue = true;
//					value = mValue;
//				} else {
//					if (Equality.isEqual(value, mValue) == false) {
//						continue attribute_loop;
//					}
//				}
//			}
//
//			target.eSet(feature, value);
//		}
//
//		// now do contained objects
//		final List<EObject> containedObjects = new ArrayList<EObject>(multiples.size());
//		for (final EReference c : target.eClass().getEAllContainments()) {
//			if (c.isMany()) {
//				continue;
//			}
//			containedObjects.clear();
//			for (final EObject m : multiples) {
//				containedObjects.add((EObject) m.eGet(c));
//			}
//			setSameValues((EObject) target.eGet(c), containedObjects);
//		}
//	}
//
//	@Override
//	protected boolean isResizable() {
//		return true;
//	}
//
//}
//
//class MultiFeatureAction extends AbstractMenuAction {
//	public static final String REPLACE = "Replace";
//	public static final String UNION = "Union";
//	public static final String INTERSECTION = "Intersection";
//	public static final String[] MODES = new String[] { REPLACE, UNION, INTERSECTION };
//
//	private String mode = REPLACE;
//	private final Pair<EMFPath, EStructuralFeature> path;
//	private final Map<Pair<EMFPath, EStructuralFeature>, String> map;
//
//	public MultiFeatureAction(final Pair<EMFPath, EStructuralFeature> p, final Map<Pair<EMFPath, EStructuralFeature>, String> m) {
//		super("");
//		this.map = m;
//		this.path = p;
//	}
//
//	/**
//	 * @param lastMenu2
//	 */
//	@Override
//	protected void populate(final Menu menu) {
//		for (final String s : MODES) {
//			final Action a = new Action(s, IAction.AS_RADIO_BUTTON) {
//				@Override
//				public void run() {
//					mode = s;
//					map.put(path, s);
//				}
//			};
//
//			// a.setActionDefinitionId(mode.toString());
//			final ActionContributionItem actionContributionItem = new ActionContributionItem(a);
//			actionContributionItem.fill(menu, -1);
//
//			// Set initially checked item.
//			if (s.equals(mode)) {
//				a.setChecked(true);
//			}
//		}
//	}
}
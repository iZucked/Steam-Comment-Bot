/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 */
package com.mmxlabs.shiplingo.ui.detailview.containers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import scenario.ScenarioPackage;
import scenario.cargo.CargoPackage;
import scenario.fleet.FleetPackage;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lngscheduler.emf.extras.EMFPath;
import com.mmxlabs.lngscheduler.emf.extras.EMFUtils;
import com.mmxlabs.shiplingo.ui.autocorrector.AutoCorrector;
import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.shiplingo.ui.detailview.base.IInlineEditorWrapper;
import com.mmxlabs.shiplingo.ui.detailview.base.IValueProviderProvider;
import com.mmxlabs.shiplingo.ui.detailview.editors.ICommandProcessor;
import com.mmxlabs.shiplingo.ui.detailview.editors.IInlineEditor;
import com.mmxlabs.shiplingo.ui.detailview.utils.ControlUtils;

/**
 * A dialog for editing the same field on several objects simultaneously.
 * 
 * @author hinton
 * 
 */
public class MultiDetailDialog extends Dialog {
	private final DetailCompositeContainer dcc;
	private EObject proxy;

	/**
	 * A set which describes which features need to be set in the output command
	 */
	private final Set<Pair<EMFPath, EStructuralFeature>> featuresToSet = new HashSet<Pair<EMFPath, EStructuralFeature>>();
	private EditingDomain editingDomain;

	public MultiDetailDialog(final Shell parentShell,
			final IValueProviderProvider valueProviderProvider,
			final EditingDomain editingDomain) {
		super(parentShell);
		dcc = new DetailCompositeContainer(valueProviderProvider,
				editingDomain, ICommandProcessor.EXECUTE,
				new IInlineEditorWrapper() {
					@Override
					public IInlineEditor wrap(final IInlineEditor proxy) {
						if (proxy.getFeature() == ScenarioPackage.eINSTANCE
								.getNamedObject_Name())
							return null;
						if (proxy.getFeature() == CargoPackage.eINSTANCE
								.getSlot_Id())
							return null;
						if (proxy.getFeature() == CargoPackage.eINSTANCE
								.getCargo_Id())
							return null;
						if (proxy.getFeature() == FleetPackage.eINSTANCE
								.getVesselEvent_Id())
							return null;

						return new IInlineEditor() {
							@Override
							public void setInput(final EObject object) {
								proxy.setInput(object);
							}

							@Override
							public void processValidation(IStatus status) {
								proxy.processValidation(status);

							};

							@Override
							public Control createControl(final Composite parent) {
								final Composite composite = new Composite(
										parent, SWT.NONE);

								final GridLayout layout = new GridLayout(2,
										false);
								layout.marginHeight = 0;
								layout.marginWidth = 0;
								composite.setLayout(layout);

								final Composite c2 = new Composite(composite,
										SWT.NONE);
								final GridLayout layout2 = new GridLayout(1,
										false);
								layout2.marginHeight = 0;
								layout2.marginWidth = 0;

								c2.setLayoutData(new GridData(
										GridData.FILL_BOTH));

								c2.setLayout(layout2);

								final Control sub = proxy.createControl(c2);
								sub.setLayoutData(new GridData(
										GridData.FILL_HORIZONTAL));

								ControlUtils.setControlEnabled(sub, false);
								final Button check = new Button(composite,
										SWT.TOGGLE);
								check.setText("Set");

								check.addSelectionListener(new SelectionAdapter() {
									@Override
									public void widgetSelected(
											final SelectionEvent e) {
										if (check.getSelection()) {
											featuresToSet
													.add(new Pair<EMFPath, EStructuralFeature>(
															getPath(),
															getFeature()));
											ControlUtils.setControlEnabled(sub,
													true);
											// sub.setEnabled(true);
										} else {
											featuresToSet
													.remove(new Pair<EMFPath, EStructuralFeature>(
															getPath(),
															getFeature()));
											// sub.setEnabled(false);
											ControlUtils.setControlEnabled(sub,
													false);
										}
									}
								});

								return composite;
							}

							@Override
							public EStructuralFeature getFeature() {
								return proxy.getFeature();
							}

							@Override
							public EMFPath getPath() {
								return proxy.getPath();
							}
						};
					}
				});
		this.editingDomain = editingDomain;
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite area = (Composite) super.createDialogArea(parent);
		final GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		area.setLayout(layout);
		return area;
	}

	private void resizeAndCenter() {
		final Shell shell = getShell();
		if (shell != null) {
			shell.layout(true);

			shell.setSize(shell.computeSize(SWT.DEFAULT, SWT.DEFAULT));

			final Rectangle shellBounds = getParentShell().getBounds();
			final Point dialogSize = shell.getSize();

			shell.setLocation(shellBounds.x
					+ (shellBounds.width - dialogSize.x) / 2, shellBounds.y
					+ (shellBounds.height - dialogSize.y) / 2);
		}
	}

	private void displayProxy() {
		final AbstractDetailComposite editor = dcc.getDetailView(
				proxy.eClass(), (Composite) getDialogArea());
		editor.setInput(proxy);
	}

	@Override
	public void create() {
		super.create();
		displayProxy();
		resizeAndCenter();
	}

	public int open(final List<EObject> objects) {
		final EClass editingClass = EMFUtils.findCommonSuperclass(objects);
		proxy = EMFUtils.createEObject(editingClass);

		final HashSet<AutoCorrector> correctors = new HashSet<AutoCorrector>();
		for (final EObject object : objects) {
			// add autocorrector adapters.
			for (final Object adapter : object.eAdapters()) {
				if (adapter instanceof AutoCorrector) {
					correctors.add((AutoCorrector) adapter);
				}
			}
		}
		
		proxy.eAdapters().addAll(correctors);

		setSameValues(proxy, objects);

		final int result = open();
		if (result == OK) {
			final CompoundCommand cc = new CompoundCommand();
			for (final Pair<EMFPath, EStructuralFeature> p : featuresToSet) {
				final Object value;
				if (p.getSecond().isUnsettable() && ((EObject) p.getFirst().get(proxy)).eIsSet(p.getSecond()) == false) {
					value = SetCommand.UNSET_VALUE;
				} else {
					value = ((EObject) p.getFirst().get(proxy)).eGet(p.getSecond());
				}

				for (final EObject object : objects) {
					cc.append(SetCommand.create(editingDomain, p.getFirst()
							.get(object), p.getSecond(), value));
				}
				editingDomain.getCommandStack().execute(cc);
			}

		}
		return result;
	}

	/**
	 * Set the fields which are the same on all objects in multiples onto
	 * target.
	 * 
	 * @param target
	 * @param multiples
	 */
	void setSameValues(final EObject target, final List<EObject> multiples) {
		attribute_loop: for (final EStructuralFeature feature : target.eClass()
				.getEAllStructuralFeatures()) {
			boolean gotValue = false;
			Object value = null;
			if (feature instanceof EReference) {
				if (((EReference) feature).isContainment())
					continue attribute_loop;
			}

			for (final EObject m : multiples) {
				if (m == null)
					return;
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
		final List<EObject> containedObjects = new ArrayList<EObject>(
				multiples.size());
		for (final EReference c : target.eClass().getEAllContainments()) {
			if (c.isMany())
				continue;
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
}

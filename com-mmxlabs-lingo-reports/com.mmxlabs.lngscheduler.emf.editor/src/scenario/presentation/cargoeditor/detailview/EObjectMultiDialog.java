/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor.detailview;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import scenario.presentation.cargoeditor.detailview.EObjectDetailView.ICommandProcessor;
import scenario.presentation.cargoeditor.detailview.EObjectDetailView.IInlineEditor;
import scenario.presentation.cargoeditor.detailview.EObjectDetailView.IInlineEditorFactory;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lngscheduler.emf.extras.EMFPath;
import com.mmxlabs.lngscheduler.emf.extras.EMFUtils;

/**
 * Like EObjectEditorDialog, but with multi-value editing support
 * 
 * @author Tom Hinton
 * 
 */
public class EObjectMultiDialog extends Dialog implements IDetailViewContainer {
	@Override
	public void addDefaultEditorFactories() {
		containerDelegate.addDefaultEditorFactories();
		System.err.println("Add default factories");
	}

	@Override
	public void setNameForFeature(EStructuralFeature feature, String string) {
		containerDelegate.setNameForFeature(feature, string);
	}

	@Override
	public void setEditorFactoryForClassifier(EClassifier classifier,
			IInlineEditorFactory factory) {
		containerDelegate.setEditorFactoryForClassifier(classifier, factory);
	}

	@Override
	public void setEditorFactoryForFeature(EStructuralFeature feature,
			IInlineEditorFactory iInlineEditorFactory) {
		containerDelegate.setEditorFactoryForFeature(feature,
				iInlineEditorFactory);
	}

	/**
	 * A set which describes which features need to be set in the output command
	 */
	private final Set<Pair<EMFPath, EStructuralFeature>> featuresToSet = new HashSet<Pair<EMFPath, EStructuralFeature>>();

	private EObjectDetailViewContainer containerDelegate = new EObjectDetailViewContainer() {
		@Override
		protected ICommandProcessor getProcessor() {
			return processor;
		}

		@Override
		protected EditingDomain getEditingDomain() {
			return editingDomain;
		}

		@Override
		protected IInlineEditorFactory wrapEditorFactory(
				final IInlineEditorFactory factory) {
			if (factory == null)
				return null;
			return new IInlineEditorFactory() {
				@Override
				public IInlineEditor createEditor(final EMFPath path,
						final EStructuralFeature feature,
						final ICommandProcessor commandProcessor) {
					final IInlineEditor proxy = factory.createEditor(path,
							feature, commandProcessor);
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
							final Composite composite = new Composite(parent,
									SWT.NONE);
							
							final GridLayout layout = new GridLayout(2, false);
							layout.marginHeight = 0;
							layout.marginWidth = 0;
							composite.setLayout(layout);
							
							final Composite c2 = new Composite(composite, SWT.NONE);
							final GridLayout layout2 = new GridLayout(1, false);
							layout2.marginHeight = 0;
							layout2.marginWidth = 0;
							c2.setLayout(layout2);
							
							final Control sub = proxy.createControl(c2);
							sub.setLayoutData(new GridData(
									GridData.FILL_HORIZONTAL));

//							sub.setEnabled(false);
							setControlEnabled(sub, false);
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
														path, feature));
										setControlEnabled(sub, true);
//										sub.setEnabled(true);
									} else {
										featuresToSet
												.remove(new Pair<EMFPath, EStructuralFeature>(
														path, feature));
//										sub.setEnabled(false);
										setControlEnabled(sub, false);
									}
								}
							});
							
							return composite;
						}
					};
				}
			};
		}
	};
	
	private void setControlEnabled(final Control sub, final boolean enabled) {
		sub.setEnabled(enabled);
		if (sub instanceof Composite) {
			for (final Control c : ((Composite) sub).getChildren()) {
				setControlEnabled(c, enabled);
			}
		}
	}
	
	private List<EObject> objects = null;
	private EditingDomain editingDomain;
	private ICommandProcessor processor = new ICommandProcessor() {
		@Override
		public void processCommand(Command command, EObject target,
				EStructuralFeature feature) {
			command.execute();
		}
	};

	public EObjectMultiDialog(final IShellProvider parentShell) {
		super(parentShell);
	}

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

	public Command createCommand() {
		final CompoundCommand cc = new CompoundCommand();

		for (final Pair<EMFPath, EStructuralFeature> p : featuresToSet) {
			final Object value = ((EObject) p.getFirst().get(proxy)).eGet(p
					.getSecond());
			for (final EObject object : objects) {
				cc.append(SetCommand.create(editingDomain, p.getFirst().get(object),p.getSecond(), value));
			}
		}

		return cc;
	}

	private EObject proxy = null;

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Control container = super.createDialogArea(parent);

		// determine what class we are editing
		final EClass editingClass = EMFUtils.findCommonSuperclass(objects);
		
		proxy = EMFUtils.createEObject(editingClass);
		final GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		parent.setLayout(layout);
		
		final EObjectDetailView view = containerDelegate.getDetailView(
				editingClass, parent);

		setSameValues(proxy, objects);

		view.setInput(proxy);
		view.layout(true);

		getShell().setText("Batch editing " + objects.size() + " " + editingClass.getName() + "s");
		
		return container;
	}

	/**
	 * Open a dialog and edit the objects in it
	 * 
	 * @param l
	 * @return
	 */
	public int open(final List<EObject> objects,
			final EditingDomain editingDomain) {
		setObjects(objects);
		setEditingDomain(editingDomain);
		return super.open();
	}

	/**
	 * @param editingDomain
	 */
	private void setEditingDomain(final EditingDomain editingDomain) {
		this.editingDomain = editingDomain;
	}

	/**
	 * Set the objects to edit
	 * 
	 * @param objects
	 */
	private void setObjects(List<EObject> objects) {
		this.objects = objects;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}
}

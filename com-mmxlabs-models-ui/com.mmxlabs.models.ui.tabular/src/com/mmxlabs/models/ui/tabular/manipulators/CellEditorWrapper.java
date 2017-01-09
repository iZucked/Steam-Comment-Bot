/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.manipulators;

import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;


/**
 */
public class CellEditorWrapper extends CellEditor {
		private CheckedControl<Control> inner;
		private CellEditor delegate;

		public CellEditorWrapper(final Composite composite) {
			super(composite);
		}
		@Override
		protected Control createControl(Composite parent) {
			inner = new CheckedControl<Control>(parent, SWT.NONE);
			inner.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			inner.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					Display.getCurrent().asyncExec(new Runnable() {
						@Override
						public void run() {
							if (inner.isFocusControl() == false) {
								CellEditorWrapper.this.fireApplyEditorValue();
							}
						}
					});
					
				}
			});
			return inner;
		}

		public CheckedControl<Control> getInnerComposite() {
			return inner;
		}
		
		public void setDelegate(final CellEditor delegate) {
			this.delegate = delegate;
			inner.setValueControl( delegate.getControl());
			if (isActivated()) {
				delegate.activate();
			} else if (delegate.isActivated()) {
				delegate.deactivate();
			}
			
			delegate.addListener(new ICellEditorListener() {
				@Override
				public void editorValueChanged(boolean oldValidState, boolean newValidState) {
					if (!inner.isChecked() && newValidState) {
						inner.setChecked(true);
					}
				}
				
				@Override
				public void cancelEditor() {
					CellEditorWrapper.this.fireCancelEditor();
				}
				
				@Override
				public void applyEditorValue() {
					// Handle this later. Because the applyEditorValue() is typically triggered during some kind of
					// event handler, testing the focus does not always work reliably during this block. Instead, we
					// have to wait for things to settle down and then see where the focus is.
					Display.getCurrent().asyncExec(new Runnable() {
						@Override
						public void run() {
							if (inner.isFocusControl()) {
								// I am not sure whether this is necessary or correct for all kinds of cell editor.
								delegate.activate();
								delegate.getControl().setVisible(true);
							} else {
								CellEditorWrapper.this.fireApplyEditorValue();
							}
						}
					});
				}
			});
		}
		
		@Override
		protected Object doGetValue() {
			if (inner.isChecked()) {
				if (delegate != null) {
					return delegate.getValue();
				}
				return null;
			} else {
				return SetCommand.UNSET_VALUE;
			}
		}

		@Override
		protected void doSetFocus() {
			inner.setFocus();
		}
		
		@Override
		protected void doSetValue(Object value) {
			if (value == SetCommand.UNSET_VALUE) {
				inner.setChecked(false);
			} else {
				inner.setChecked(true);
				if (delegate != null) {
					delegate.setValue(value);
				}
			}
		}
		@Override
		public void activate() {
			super.activate();
			if (delegate != null) {
				delegate.activate();
				delegate.getControl().setVisible(true);
			}
		}
		@Override
		public void addPropertyChangeListener(IPropertyChangeListener listener) {
			if (delegate != null) {
				delegate.addPropertyChangeListener(listener);
			}
		}
		@Override
		public void deactivate() {
			super.deactivate();
			if (delegate != null) {
				delegate.deactivate();
			}
		}
		@Override
		public void dispose() {
			if (delegate != null) {
				delegate.dispose();
			}
			super.dispose();
		}
		@Override
		public String getErrorMessage() {
			if (delegate != null) {
				return delegate.getErrorMessage();
			}
			return super.getErrorMessage();
		}
		@Override
		public ICellEditorValidator getValidator() {
			if (delegate != null) {
				return delegate.getValidator();
			}
			return super.getValidator();
		}
		@Override
		public boolean isActivated() {
			return super.isActivated();
		}
		@Override
		public boolean isCopyEnabled() {
			if (delegate != null) {
				return delegate.isCopyEnabled();
			}
			return super.isCopyEnabled();
		}
		@Override
		public boolean isCutEnabled() {
			if (delegate != null) {
				return delegate.isCutEnabled();
			}
			return super.isCutEnabled();
		}
		@Override
		public boolean isDeleteEnabled() {
			if (delegate != null) {
				return delegate.isDeleteEnabled();
			}
			return super.isDeleteEnabled();
		}
		@Override
		public boolean isDirty() {
			if (delegate != null) {
				return delegate.isDirty();
			}
			return super.isDirty();
		}
		@Override
		public boolean isFindEnabled() {
			if (delegate != null) {
				return delegate.isFindEnabled();
			}
			return super.isFindEnabled();
		}
		@Override
		public boolean isPasteEnabled() {
			if (delegate != null) {
				return delegate.isPasteEnabled();
			}
			return super.isPasteEnabled();
		}
		@Override
		public boolean isRedoEnabled() {
			if (delegate != null) {
				return delegate.isRedoEnabled();
			}
			return super.isRedoEnabled();
		}
		@Override
		public boolean isSelectAllEnabled() {
			if (delegate != null) {
				return delegate.isSelectAllEnabled();
			}
			return super.isSelectAllEnabled();
		}
		@Override
		public boolean isUndoEnabled() {
			if (delegate != null) {
				return delegate.isUndoEnabled();
			}
			return super.isUndoEnabled();
		}
		@Override
		public boolean isValueValid() {
			if (delegate != null) {
				return delegate.isValueValid();
			}
			return super.isValueValid();
		}
		@Override
		public void performCopy() {
			if (delegate != null) {
				delegate.performCopy();
			}
		}
		@Override
		public void performCut() {
			if (delegate != null) {
				delegate.performCut();
			}
		}
		@Override
		public void performDelete() {
			if (delegate != null) {
				delegate.performDelete();
			}
		}
		@Override
		public void performFind() {
			if (delegate != null) {
				delegate.performFind();
			}
		}
		@Override
		public void performPaste() {
			if (delegate != null) {
				delegate.performPaste();
			}
		}
		@Override
		public void performRedo() {
			if (delegate != null) {
				delegate.performRedo();
			}
		}
		@Override
		public void performSelectAll() {
			if (delegate != null) {
				delegate.performSelectAll();
			}
		}
		@Override
		public void performUndo() {
			if (delegate != null) {
				delegate.performUndo();
			}
		}
		@Override
		public void removeListener(ICellEditorListener listener) {
			if (delegate != null) {
				delegate.removeListener(listener);
			}
		}
		@Override
		public void removePropertyChangeListener(
				IPropertyChangeListener listener) {
			if (delegate != null) {
				delegate.removePropertyChangeListener(listener);
			}
		}
		@Override
		public void setValidator(ICellEditorValidator validator) {
			if (delegate != null) {
				delegate.setValidator(validator);
			}
		}
		@Override
		protected void focusLost() {
			if (inner.isFocusControl() == false) {
				super.focusLost();
			}
		}
		@Override
		protected boolean dependsOnExternalFocusListener() {
			return false;
		}
	}
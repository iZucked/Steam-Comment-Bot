/*******************************************************************************
 * Copyright (c) Emil Crumhorn - Hexapixel.com - emil.crumhorn@gmail.com
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    emil.crumhorn@gmail.com - initial API and implementation
 *******************************************************************************/

package org.eclipse.nebula.widgets.ganttchart.undoredo;

import org.eclipse.nebula.widgets.ganttchart.undoredo.commands.IUndoRedoCommand;

/**
 * Basic implementation of {@link IUndoRedoListener}.
 * 
 * @author Emil
 *
 */
public class UndoRedoListenerAdapter implements IUndoRedoListener {

    public void canRedoChanged(boolean canRedo) {
    }

    public void canUndoChanged(boolean canUndo) {
    }

    public void commandRedone(IUndoRedoCommand command) {
    }

    public void commandUndone(IUndoRedoCommand command) {
    }

    public void undoableCommandAdded(IUndoRedoCommand command) {
    }

}

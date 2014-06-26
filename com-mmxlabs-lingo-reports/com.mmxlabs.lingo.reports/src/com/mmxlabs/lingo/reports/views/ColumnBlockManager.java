package com.mmxlabs.lingo.reports.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.ui.IMemento;

/**
 * A class which manages custom column blocks for a Nebula Grid widget. The blocks have columns assigned to them, can be made visible or invisible, and can be moved en masse on the grid.
 * 
 * @author Simon McGregor
 * 
 */
public class ColumnBlockManager {
	private static final String COLUMN_BLOCK_CONFIG_MEMENTO = "COLUMN_BLOCK_CONFIG_MEMENTO";
	private static final String BLOCK_VISIBLE_MEMENTO = "VISIBLE";

	private List<ColumnBlock> blocks = new ArrayList<>();
	private Grid grid;

	protected ColumnBlock findColumnBlock(final GridColumn column) {
		for (final ColumnBlock block : blocks) {
			if (block.findHandler(column) != null) {
				return block;
			}
		}

		return null;
	}

	protected ColumnBlock getBlockByName(final String name) {
		for (final ColumnBlock block : blocks) {
			if (block.name.equals(name)) {
				return block;
			}
		}

		return null;
	}

	/**
	 * Associates the specified column handler with a column block specified by the given name, removing it from any block it is already attached to. If no block exists with that name, one is created
	 * unless the name is null. In the case of a null name, this method merely removes the handler from all currently known blocks.
	 * 
	 * @param handler
	 * @param blockName
	 */
	public void setHandlerBlockName(final ColumnHandler handler, final String blockName, final ColumnType columnType) {
		ColumnBlock namedBlock = null;
		final List<ColumnBlock> blocksToPurge = new ArrayList<>();

		for (final ColumnBlock block : blocks) {
			if (block.name.equals(blockName)) {
				namedBlock = block;
			} else {
				block.blockHandlers.remove(handler);
				if (block.blockHandlers.isEmpty()) {
					blocksToPurge.add(block);
				}
			}
		}

		if (namedBlock == null && blockName != null) {
			namedBlock = new ColumnBlock(blockName, columnType);
			blocks.add(namedBlock);
		}

		if (namedBlock != null) {
			namedBlock.setColumnType(columnType);
		}

		if (namedBlock != null && namedBlock.blockHandlers.contains(handler) == false) {
			namedBlock.addColumn(handler);
		}

		if (blocksToPurge.isEmpty() == false) {
			blocks.removeAll(blocksToPurge);
		}

	}

	/**
	 * Returns the block order for the grid widget. Assumes that the column display order on the widget respects the managed column blocks (i.e. all columns in a block are displayed contiguously).
	 * Returns null and prints an error if there is an inconsistency.
	 * 
	 * TODO: throw an exception if there is any inconsistency.
	 * 
	 * @return
	 */
	public List<ColumnBlock> getBlocksInVisibleOrder() {
		final List<ColumnBlock> result = new ArrayList<>();
		ColumnBlock current = null;

		final int[] colOrder = grid.getColumnOrder();
		for (int i = 0; i < colOrder.length; i++) {
			final GridColumn column = grid.getColumn(colOrder[i]);
			final ColumnBlock block = findColumnBlock(column);
			// the next column in the grid display should belong to a managed block
			if (block == null) {
				System.err.println(String.format("Grid contains an un-managed column: %s.", column.toString()));
				return null;
			}
			// and the block should not be one which has been seen before unless it contained the last column as well
			if (block != current && result.contains(block)) {
				System.err.println(String.format("Grid contains an out-of-block column: %s.", column.toString()));
				return null;
			}
			// otherwise everything should be fine
			if (result.contains(block) == false) {
				result.add(block);
			}
			current = block;
		}

		return result;
	}

	public void setVisibleBlockOrder(final List<ColumnBlock> order) {
		int index = 0;
		final int[] colOrder = grid.getColumnOrder();

		int maxIndex = 0;
		for (final ColumnBlock block : order) {
			for (final ColumnHandler handler : block.blockHandlers) {
				final GridColumn column = handler.column.getColumn();
				if (column.isDisposed()) {
					colOrder[index] = -1;// grid.indexOf(column);
				} else {
					colOrder[index] = grid.indexOf(column);
				}
				maxIndex = Math.max(maxIndex, colOrder[index]);
				index += 1;
			}
		}

		// The incoming column block order may have come from a memto. There may be different columns thus a mismatch on old and new column blocks. Here we make sure any new columns not covered by
		// the column order have a unique column index.
		for (; index < colOrder.length; ++index) {
			colOrder[index] = ++maxIndex;
		}

		// Renumber the col order to have consecutive numbering from zero. Removed columns may cause holes in sequence.
		index = 0;
		for (int i = 0; i <= maxIndex; ++i) {
			for (int j = 0; j < colOrder.length; ++j) {
				if (colOrder[j] == i) {
					colOrder[j] = index++;
					break;
				}
			}
		}

		// // Replace -1's with valid index
		// for (int i = 0; i < colOrder.length; ++i) {
		// if (colOrder[i] == -1) {
		// colOrder[i] = usedCount++;
		// }
		// }

		grid.setColumnOrder(colOrder);
	}

	public void swapBlockOrder(final ColumnBlock block1, final ColumnBlock block2) {
		final List<ColumnBlock> order = getBlocksInVisibleOrder();
		final int index1 = order.indexOf(block1);
		final int index2 = order.indexOf(block2);
		order.set(index1, block2);
		order.set(index2, block1);
		setVisibleBlockOrder(order);
	}

	public int getBlockIndex(final ColumnBlock block) {
		return getBlocksInVisibleOrder().indexOf(block);
	}

	@SuppressWarnings("null")
	public boolean getBlockVisible(final ColumnBlock block) {
		Boolean result = null;
		for (final ColumnHandler handler : block.blockHandlers) {
			final GridColumn column = handler.column.getColumn();
			if (result != null && !column.isDisposed() && column.getVisible() != result) {
				System.err.println(String.format("Column block has inconsistent visibility: %s.", column.toString()));
				return false;
			}
			result = !column.isDisposed() && column.getVisible();
		}
		return (result == null ? false : result.booleanValue());
	}

	public void saveToMemento(final String uniqueConfigKey, final IMemento memento) {
		final IMemento blocksInfo = memento.createChild(uniqueConfigKey);
		for (final ColumnBlock block : this.getBlocksInVisibleOrder()) {
			final IMemento blockInfo = blocksInfo.createChild(COLUMN_BLOCK_CONFIG_MEMENTO, block.name);
			blockInfo.putBoolean(BLOCK_VISIBLE_MEMENTO, getBlockVisible(block));
		}
	}

	public void initFromMemento(final String uniqueConfigKey, final IMemento memento) {
		final IMemento blocksInfo = memento.getChild(uniqueConfigKey);
		final List<ColumnBlock> order = new ArrayList<>();

		if (blocksInfo != null) {

			for (final IMemento blockInfo : blocksInfo.getChildren(COLUMN_BLOCK_CONFIG_MEMENTO)) {
				final String blockName = blockInfo.getID();
				ColumnBlock block = getBlockByName(blockName);
				if (block == null) {
					block = new ColumnBlock(blockName, ColumnType.NORMAL);
					blocks.add(block);
				}
				final Boolean visible = blockInfo.getBoolean(BLOCK_VISIBLE_MEMENTO);
				if (visible != null) {
					block.setUserVisible(visible);
				}
				order.add(block);
			}

			setVisibleBlockOrder(order);
		}
	}

	public void setGrid(Grid grid) {
		this.grid = grid;

	}
}
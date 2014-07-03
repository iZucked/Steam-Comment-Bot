package com.mmxlabs.lingo.reports.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.ui.IMemento;

/**
 * A class which manages custom column blocks for a Nebula Grid widget. The blocks have columns assigned to them, can be made visible or invisible, and can be moved en masse on the grid.
 * 
 * The column block manager requires that every single column in the Grid widget, without exception, is attached to some named column block.
 * 
 * @author Simon McGregor
 * 
 */
public class ColumnBlockManager {
	private static final String COLUMN_BLOCK_CONFIG_MEMENTO = "COLUMN_BLOCK_CONFIG_MEMENTO";
	private static final String BLOCK_VISIBLE_MEMENTO = "VISIBLE";

	private final List<ColumnBlock> blocks = new ArrayList<>();
	private Grid grid;
	private List<String> blockOrderByName = new ArrayList<>();

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
			namedBlock = createBlock(blockName, columnType);
		}

		if (namedBlock != null) {
			namedBlock.setColumnType(columnType);
		}

		if (namedBlock != null && namedBlock.blockHandlers.contains(handler) == false) {
			namedBlock.addColumn(handler);
		}

		if (blocksToPurge.isEmpty() == false) {
			for (final ColumnBlock block : blocksToPurge) {
				// Do not purge placeholder columns
				if (!block.isPlaceholder()) {
					blocks.remove(block);
				}
			}
		}

	}

	public ColumnBlock createBlock(final String blockName, final ColumnType columnType) {
		ColumnBlock namedBlock;
		namedBlock = new ColumnBlock(blockName, columnType);
		blocks.add(namedBlock);
		return namedBlock;
	}

	public void setNameOrder(final List<String> order) {
		final List<ColumnBlock> blockOrder = new ArrayList<>();
		for (String name: order) {
			ColumnBlock block = getBlockByName(name);
			blockOrder.add(block);
		}
		setVisibleBlockOrder(blockOrder);
		blockOrderByName.clear();
		blockOrderByName.addAll(order);
	}	
	
	/**
	 * Returns the block order for the grid widget. Assumes that the column display order on the widget respects the managed column blocks (i.e. all columns in a block are displayed contiguously).
	 * Returns null and prints an error if there is an inconsistency.
	 * 
	 * TODO: throw an exception if there is any inconsistency.
	 * 
	 * @return
	 */
	public List<ColumnBlock> getBlockOrderFromGrid() {
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
	
	/**
	 * Returns a list of block names in currently specified order. This will automatically
	 * append any block names which were not listed in the last setNameOrder() call.
	 * @return
	 */
	public List<String> getNameOrder() {		
		final List<String> result = new ArrayList<String>(blockOrderByName);
		for (ColumnBlock block: blocks) {
			if (result.contains(block.name) == false) {
				result.add(block.name);
			}
		}
		return result;
	}
	
	/**
	 * Returns a list of all blocks in the order they should appear in. 
	 * @return
	 */
	public List<ColumnBlock> getBlocksInVisibleOrder() {
		final ArrayList<ColumnBlock> result = new ArrayList<ColumnBlock>();
		for (String name: getNameOrder()) {
			ColumnBlock block = getBlockByName(name);
			result.add(getBlockByName(name));
		}
		return result;
	}
	
	/**
	 * Sets the column ordering on a nebula grid widget to respect the desired ordering of column blocks.
	 * 
	 * @param order
	 */
	public void setVisibleBlockOrder(final List<ColumnBlock> order) {
		int index = 0;
		final int[] colOrder = grid.getColumnOrder();

		final List<ColumnBlock> finalOrder = new ArrayList<ColumnBlock>(order);
		
		// detect any visible blocks missing from the specified order and append them  
		for (final ColumnBlock block: blocks) {
			if (getBlockVisible(block) && finalOrder.contains(block) == false) {
				finalOrder.add(block);
			}
		}
		
		// Java won't allow initialising a List<Integer> directly from an int []
		final List<Integer> missingIndices = new ArrayList<>();
		for (int i: colOrder) {
			missingIndices.add(i);
		}
		
		// go through blocks in order
		for (final ColumnBlock block : finalOrder) {
			// adding the columns in each block to the grid in the correct order
			for (final ColumnHandler handler : block.blockHandlers) {
				final GridColumn column = handler.column.getColumn();
				// sanity check to make sure nothing is bad
				if (column != null && column.isDisposed() == false) {
					// the next column in the grid display will be the given column
					colOrder[index] = grid.indexOf(column);
					missingIndices.remove((Object) colOrder[index]); 
					index += 1;
				}
			}
		}
		
		// if there are any columns missing from the specified order, something is wrong
		if (missingIndices.isEmpty() == false) {
			System.err.println(String.format("Available blocks only account for %d out of %d columns.", index, colOrder.length));
			for (int i: missingIndices) {
				colOrder[index] = i;
				index += 1;
			}
		}

		grid.setColumnOrder(colOrder);
	}

	public void swapBlockOrder(final ColumnBlock block1, final ColumnBlock block2) {
		List<String> order = getNameOrder();
		final int index1 = order.indexOf(block1.name);
		final int index2 = order.indexOf(block2.name);
		order.set(index1, block2.name);
		order.set(index2, block1.name);
		setNameOrder(order);
	}

	public int getBlockIndex(final ColumnBlock block) {
		return getBlocksInVisibleOrder().indexOf(block);
	}
	
	public boolean getBlockVisible(final ColumnBlock block) {
		return block.getVisible();
	}

	@SuppressWarnings("null")
	public boolean getBlockReallyVisible(final ColumnBlock block) {
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

	/**
	 * Saves the state of the block manager to an IMemento object.
	 * 
	 * @param uniqueConfigKey
	 * @param memento
	 */
	public void saveToMemento(final String uniqueConfigKey, final IMemento memento) {
		final IMemento blocksInfo = memento.createChild(uniqueConfigKey);
		for (final String name: getNameOrder()) {
			ColumnBlock block = getBlockByName(name);
			final IMemento blockInfo = blocksInfo.createChild(COLUMN_BLOCK_CONFIG_MEMENTO, name);
			blockInfo.putBoolean(BLOCK_VISIBLE_MEMENTO, getBlockVisible(block));
		}
	}

	/**
	 * Loads the state of the block manager from an IMemento object.
	 * 
	 * @param uniqueConfigKey
	 * @param memento
	 */
	public void initFromMemento(final String uniqueConfigKey, final IMemento memento) {
		final IMemento blocksInfo = memento.getChild(uniqueConfigKey);
		final List<String> order = new ArrayList<>();

		if (blocksInfo != null) {

			for (final IMemento blockInfo : blocksInfo.getChildren(COLUMN_BLOCK_CONFIG_MEMENTO)) {
				final String blockName = blockInfo.getID();
				ColumnBlock block = getBlockByName(blockName);
				if (block == null) {
					block = createBlock(blockName, ColumnType.NORMAL);
				}
				final Boolean visible = blockInfo.getBoolean(BLOCK_VISIBLE_MEMENTO);
				if (visible != null) {
					block.setUserVisible(visible);
				}
				order.add(blockName);
			}

			setNameOrder(order);
		}
	}

	public void setGrid(final Grid grid) {
		this.grid = grid;

	}
}
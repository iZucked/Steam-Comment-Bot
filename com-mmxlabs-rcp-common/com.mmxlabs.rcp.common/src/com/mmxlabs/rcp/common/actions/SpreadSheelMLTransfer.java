package com.mmxlabs.rcp.common.actions;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;

/**
 * The class <code>TextTransfer</code> provides a platform specific mechanism for converting plain text represented as a java <code>String</code> to a platform specific representation of the data and
 * vice versa.
 * 
 * <p>
 * An example of a java <code>String</code> containing plain text is shown below:
 * </p>
 * 
 * <code><pre>
 *     String textData = "Hello World";
 * </code>
 * </pre>
 * 
 * <p>
 * Note the <code>TextTransfer</code> does not change the content of the text data. For a better integration with the platform, the application should convert the line delimiters used in the text data
 * to the standard line delimiter used by the platform.
 * </p>
 * 
 * @see Transfer
 */
public class SpreadSheelMLTransfer extends ByteArrayTransfer {
	private final String[] typeNames = new String[] { "XML Spreadsheet", "XML Spreadsheet" };
	private final int[] typeIds = new int[] { registerType(typeNames[0]), registerType(typeNames[1]) };

	@Override
	protected String[] getTypeNames() {
		return typeNames;
	}

	@Override
	protected int[] getTypeIds() {
		return typeIds;
	}

	// @Override
	protected Object __nativeToJava(TransferData transferData) {
		if (!isSupportedType(transferData))
			return null;

		final byte[] buffer = (byte[]) super.nativeToJava(transferData);
		if (buffer == null)
			return null;

		try {
			final DataInputStream in = new DataInputStream(new ByteArrayInputStream(buffer));

			long count = 0;
			for (int i = 0; i < 4; i++) {
				count += in.readUnsignedByte() << i;
			}

			for (int i = 0; i < count; i++) {
				final byte[] filenameBytes = new byte[260 * 2];
				// in.skipBytes(72); // probable architecture assumption(s) - may be wrong outside standard 32-bit Win XP
				in.read(filenameBytes);
				final String fileNameIncludingTrailingNulls = new String(filenameBytes, "UTF-16LE");
				int stringLength = fileNameIncludingTrailingNulls.indexOf('\0');
				if (stringLength == -1)
					stringLength = 260;
				final String fileName = fileNameIncludingTrailingNulls.substring(0, stringLength);
				System.out.println("File " + i + ": " + fileName);
			}

			in.close();

			return buffer;
		} catch (final Exception e) {
			return null;
		}
	}
}

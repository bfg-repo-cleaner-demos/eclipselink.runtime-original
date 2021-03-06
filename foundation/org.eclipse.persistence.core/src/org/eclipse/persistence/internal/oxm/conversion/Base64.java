/*******************************************************************************
 * Copyright (c) 1998, 2013 Oracle and/or its affiliates. All rights reserved.
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0 
 * which accompanies this distribution. 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Oracle - initial API and implementation from Oracle TopLink
 ******************************************************************************/  
package org.eclipse.persistence.internal.oxm.conversion;

import java.util.BitSet;

/**
 * INTERNAL: 
 * <p><b>Purpose</b>: Convert to/from XML base64Binary.</p>
 */

public class Base64 {
    private static BitSet BoundChar;
    private static BitSet EBCDICUnsafeChar;
    private static byte[] Base64EncMap;
    private static byte[] Base64DecMap;
    private static char[] UUEncMap;
    private static byte[] UUDecMap;

    // Class Initializer
    static {
        // rfc-2046 & rfc-2045: (bcharsnospace & token)
        // used for multipart codings
        BoundChar = new BitSet(256);
        for (int ch = '0'; ch <= '9'; ch++) {
            BoundChar.set(ch);
        }
        for (int ch = 'A'; ch <= 'Z'; ch++) {
            BoundChar.set(ch);
        }
        for (int ch = 'a'; ch <= 'z'; ch++) {
            BoundChar.set(ch);
        }
        BoundChar.set('+');
        BoundChar.set('_');
        BoundChar.set('-');
        BoundChar.set('.');

        // EBCDIC unsafe characters to be quoted in quoted-printable
        // See first NOTE in section 6.7 of rfc-2045
        EBCDICUnsafeChar = new BitSet(256);
        EBCDICUnsafeChar.set('!');
        EBCDICUnsafeChar.set('"');
        EBCDICUnsafeChar.set('#');
        EBCDICUnsafeChar.set('$');
        EBCDICUnsafeChar.set('@');
        EBCDICUnsafeChar.set('[');
        EBCDICUnsafeChar.set('\\');
        EBCDICUnsafeChar.set(']');
        EBCDICUnsafeChar.set('^');
        EBCDICUnsafeChar.set('`');
        EBCDICUnsafeChar.set('{');
        EBCDICUnsafeChar.set('|');
        EBCDICUnsafeChar.set('}');
        EBCDICUnsafeChar.set('~');

        // rfc-2045: Base64 Alphabet
        byte[] map = { (byte)'A', (byte)'B', (byte)'C', (byte)'D', (byte)'E', (byte)'F', (byte)'G', (byte)'H', (byte)'I', (byte)'J', (byte)'K', (byte)'L', (byte)'M', (byte)'N', (byte)'O', (byte)'P', (byte)'Q', (byte)'R', (byte)'S', (byte)'T', (byte)'U', (byte)'V', (byte)'W', (byte)'X', (byte)'Y', (byte)'Z', (byte)'a', (byte)'b', (byte)'c', (byte)'d', (byte)'e', (byte)'f', (byte)'g', (byte)'h', (byte)'i', (byte)'j', (byte)'k', (byte)'l', (byte)'m', (byte)'n', (byte)'o', (byte)'p', (byte)'q', (byte)'r', (byte)'s', (byte)'t', (byte)'u', (byte)'v', (byte)'w', (byte)'x', (byte)'y', (byte)'z', (byte)'0', (byte)'1', (byte)'2', (byte)'3', (byte)'4', (byte)'5', (byte)'6', (byte)'7', (byte)'8', (byte)'9', (byte)'+', (byte)'/' };
        Base64EncMap = map;
        Base64DecMap = new byte[128];
        for (int idx = 0; idx < Base64EncMap.length; idx++) {
            Base64DecMap[Base64EncMap[idx]] = (byte)idx;
        }

        // uuencode'ing maps
        UUEncMap = new char[64];
        for (int idx = 0; idx < UUEncMap.length; idx++) {
            UUEncMap[idx] = (char)(idx + 0x20);
        }
        UUDecMap = new byte[128];
        for (int idx = 0; idx < UUEncMap.length; idx++) {
            UUDecMap[UUEncMap[idx]] = (byte)idx;
        }
    }

    /**
     * Base64 constructor comment.
     */
    public Base64() {
        super();
    }

    /**
     * This method decodes the given byte[] using the base64-encoding
     * specified in RFC-2045 (Section 6.8).
     *
     * @param  data the base64-encoded data.
     * @return the decoded <var>data</var>.
     */
    public final static byte[] base64Decode(byte[] data) {
        if (data == null) {
            return null;
        }
        
        if (data.length == 0) {
            return new byte[0];
        }

        int tail = data.length;
        while (data[tail - 1] == '=') {
            tail--;
        }

        byte[] dest = new byte[tail - (data.length / 4)];

        // ascii printable to 0-63 conversion
        for (int idx = 0; idx < data.length; idx++) {
            data[idx] = Base64DecMap[data[idx]];
        }

        // 4-byte to 3-byte conversion
        int sidx;

        // 4-byte to 3-byte conversion
        int didx;
        for (sidx = 0, didx = 0; didx < (dest.length - 2); sidx += 4, didx += 3) {
            dest[didx] = (byte)(((data[sidx] << 2) & 255) | ((data[sidx + 1] >>> 4) & 003));
            dest[didx + 1] = (byte)(((data[sidx + 1] << 4) & 255) | ((data[sidx + 2] >>> 2) & 017));
            dest[didx + 2] = (byte)(((data[sidx + 2] << 6) & 255) | (data[sidx + 3] & 077));
        }
        if (didx < dest.length) {
            dest[didx] = (byte)(((data[sidx] << 2) & 255) | ((data[sidx + 1] >>> 4) & 003));
        }
        if (++didx < dest.length) {
            dest[didx] = (byte)(((data[sidx + 1] << 4) & 255) | ((data[sidx + 2] >>> 2) & 017));
        }

        return dest;
    }

    /**
     * This method encodes the given byte[] using the base64-encoding
     * specified in RFC-2045 (Section 6.8).
     *
     * @param  data the data
     * @return the base64-encoded <var>data</var>
     */
    public final static byte[] base64Encode(byte[] data) {
        if (data == null) {
            return null;
        }

        int sidx;
        int didx;
        byte[] dest = new byte[((data.length + 2) / 3) * 4];

        // 3-byte to 4-byte conversion + 0-63 to ascii printable conversion
        for (sidx = 0, didx = 0; sidx < (data.length - 2); sidx += 3) {
            dest[didx++] = Base64EncMap[(data[sidx] >>> 2) & 077];
            dest[didx++] = Base64EncMap[((data[sidx + 1] >>> 4) & 017) | ((data[sidx] << 4) & 077)];
            dest[didx++] = Base64EncMap[((data[sidx + 2] >>> 6) & 003) | ((data[sidx + 1] << 2) & 077)];
            dest[didx++] = Base64EncMap[data[sidx + 2] & 077];
        }
        if (sidx < data.length) {
            dest[didx++] = Base64EncMap[(data[sidx] >>> 2) & 077];
            if (sidx < (data.length - 1)) {
                dest[didx++] = Base64EncMap[((data[sidx + 1] >>> 4) & 017) | ((data[sidx] << 4) & 077)];
                dest[didx++] = Base64EncMap[(data[sidx + 1] << 2) & 077];
            } else {
                dest[didx++] = Base64EncMap[(data[sidx] << 4) & 077];
            }
        }

        // add padding
        for (; didx < dest.length; didx++) {
            dest[didx] = (byte)'=';
        }

        return dest;
    }
}

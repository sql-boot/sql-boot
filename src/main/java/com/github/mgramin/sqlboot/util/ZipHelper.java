/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 Maksim Gramin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mgramin.sqlboot.util;

import com.github.mgramin.sqlboot.exceptions.SqlBootException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Zip helper.
 *
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class ZipHelper {

    /**
     * Ctor.
     */
    private ZipHelper() {
    }

    /**
     * Compress list of files.
     *
     * @param files List of files
     * @return Zipped files in byte array
     * @throws SqlBootException If fail
     */
    public static byte[] compress(final Map<String,
            byte[]> files) throws SqlBootException {
        try (ByteArrayOutputStream byteArrayOutputStream =
                     new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream =
                     new ZipOutputStream(byteArrayOutputStream)) {
            for (final Map.Entry<String, byte[]> strEntry : files.entrySet()) {
                final ZipEntry entry = new ZipEntry(strEntry.getKey());
                zipOutputStream.putNextEntry(entry);
                zipOutputStream.write(strEntry.getValue());
                zipOutputStream.closeEntry();
            }
            zipOutputStream.close();
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (final IOException exception) {
            throw new SqlBootException(exception);
        }
    }

}

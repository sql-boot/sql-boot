/*
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016-2019 Maksim Gramin
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mgramin.sqlboot.sql.select

import com.github.mgramin.sqlboot.exceptions.BootException
import java.util.stream.Stream

/**
 * Simple select SQL-query
 *
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id: 3a4e282eda365f55a3031fef68fec51109ca784d $
 * @since 0.1
 */
interface SelectQuery {

    /**
     * Execute select query
     *
     * @return query result
     * @throws BootException SQL exception
     */
    @Deprecated("")
    @Throws(BootException::class)
    fun select(): Stream<Map<String, Any>>

    /**
     * Execute select query with parameters
     *
     * @return query result
     * @throws BootException SQL exception
     */
    @Throws(BootException::class)
    open fun select(variables: Map<String, Any>): Stream<Map<String, Any>> {
        return select()
    }

    /**
     *
     * @return
     */
    fun columns(): Map<String, String>

    /**
     * Check db health
     *
     * @throws BootException SQL exception
     */
    @Deprecated("")
    @Throws(BootException::class)
    fun dbHealth()

}
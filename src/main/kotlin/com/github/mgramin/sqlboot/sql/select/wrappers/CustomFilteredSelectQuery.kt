/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2019, CROC Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.mgramin.sqlboot.sql.select.wrappers

import com.github.mgramin.sqlboot.sql.select.SelectQuery
import com.google.gson.JsonObject


class CustomFilteredSelectQuery(
        private val origin: SelectQuery,
        private val filter: JsonObject
) : SelectQuery {

    override fun query(): String {
        return if (filter.entrySet().size > 0) {
            val whereCondition = filter.entrySet()
                    .joinToString(separator = " and ")
                    {
                        when (origin.columns().first { column -> column.name() == it.key }.datatype()) {
                            "numeric" -> """${it.key} = ${it.value.asNumber}"""
                            "float8" -> """${it.key} = ${it.value.asFloat}"""
                            "int8" -> """${it.key} = ${it.value.asInt}"""
                            "varchar","text","name" -> """${it.key} like '${it.value.asString.replace("'", "''")}'"""
                            "timestamptz" -> """${it.key} between '${it.value.asJsonObject["start"].asString}'::timestamptz and '${it.value.asJsonObject["end"].asString}'::timestamptz"""
                            else -> """${it.key} = '${it.value.asString.replace("'", "''")}'"""
                        }
                    }
            """select *
              |  from (${origin.query()}) q
              | where $whereCondition""".trimMargin()
        } else {
            origin.query()
        }
    }

    override fun properties() = origin.properties()

    override fun columns() = origin.columns()

    override fun execute(variables: Map<String, Any>) = origin.execute(variables)
}
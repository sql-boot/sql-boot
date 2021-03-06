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
import com.github.mgramin.sqlboot.template.generator.impl.JinjaTemplateGenerator
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toFlux
import reactor.core.scheduler.Schedulers
import javax.sql.DataSource

/**
 * Execute SQL-query through plain old Jdbc.
 *
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id: f38638fde3d38f83edd4b8a03c570f845c856752 $
 * @since 0.1
 */
class ExecutableSelectQuery(
        private val origin: SelectQuery,
        private val dataSource: DataSource,
        private val nullAlias: String
) : SelectQuery {

    constructor(origin: SelectQuery, dataSource: DataSource) : this(origin, dataSource, "")

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun query() = origin.query()

    override fun properties() = origin.properties()

    override fun columns() = origin.columns()

    override fun execute(variables: Map<String, Any>): Flux<Map<String, Any>> {
        val sqlText = JinjaTemplateGenerator(origin.query()).generate(variables)
        logger.info("\n$sqlText")

        return Mono.fromCallable {
            logger.info(Thread.currentThread().toString())
            val rowSet = JdbcTemplate(dataSource).queryForRowSet(sqlText)
            return@fromCallable object : Iterator<Map<String, Any>> {
                override fun hasNext(): Boolean {
                    return rowSet.next()
                }

                override fun next(): Map<String, Any> {
                    return rowSet
                            .metaData
                            .columnNames
                            .map { it.toLowerCase() to (rowSet.getObject(it) ?: nullAlias) }
                            .toMap()
                }
            }
        }
                .publishOn(Schedulers.parallel())
                .map { it.toFlux() }
                .toFlux()
                .doOnError { logger.warn(it.message) }
                .onErrorReturn(Flux.empty())
                .flatMap { it }

    }

}

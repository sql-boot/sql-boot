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

package com.github.mgramin.sqlboot.rest.controllers

import com.github.mgramin.sqlboot.model.connection.CheckedConnection
import com.github.mgramin.sqlboot.model.connection.DbConnection
import com.github.mgramin.sqlboot.model.connection.DbConnectionList
import com.github.mgramin.sqlboot.model.connection.SimpleDbConnection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.toFlux
import reactor.core.scheduler.Schedulers

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id: a6d2d498c7e5c3b03e04f40cf775dc5248064da8 $
 * @since 0.1
 */
@RestController
@ComponentScan(basePackages = ["com.github.mgramin.sqlboot.model.resource_type"])
@EnableAutoConfiguration
@CrossOrigin
class DbConnectionsController @Autowired constructor(private val dbConnectionList: DbConnectionList) {

    val allDbConnections: List<SimpleDbConnection>
        @RequestMapping(value = ["/connections"])
        get() = dbConnectionList.connections

    /*val allDbConnectionsByMask: List<SimpleDbConnection>
        @RequestMapping(value = ["/connections"])
        get() = dbConnectionList.connections*/

    @GetMapping(value = ["/connections/health"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    @ResponseBody
    internal fun health(): Flux<DbConnection> {
        return dbConnectionList
                .connections
                .toFlux()
                .parallel()
                .runOn(Schedulers.elastic())
                .map { CheckedConnection(it) as DbConnection }
                .sequential()
    }

}

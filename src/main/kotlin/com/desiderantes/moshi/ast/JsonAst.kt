/*
 * Copyright (C) 2024 Mario Daniel Ruiz Saavedra <desiderantes93@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.desiderantes.moshi.ast

import java.math.BigDecimal
import java.math.BigInteger

public sealed class JValue<out T> {
  public abstract val value: T
}

/**
 * Represents an invalid JSON value or an empty document
 */
public data object JNothing : JValue<Nothing>() {
  override val value: Nothing get() = throw UnsupportedOperationException("JNothing has no value")
  override fun toString(): String = ""
}

/**
 * Represents a JSON `null` value.
 */
public data object JNull : JValue<Nothing?>() {
  override val value: Nothing? get() = null
  override fun toString(): String = "null"
}

/**
 * Represents a JSON string value.
 */
public data class JString(override val value: String) : JValue<String>() {
  override fun toString(): String = "\"$value\""
}

public sealed class JNumber<out T: Number> : JValue<T>() {
}

/**
 * Represents a JSON number value, if the value is a decimal.
 */
public data class JDouble(override val value: BigDecimal) : JNumber<BigDecimal>() {
  override fun toString(): String = value.toString()
}

/**
 * Represents a JSON number value, if the value is an integer.
 */
public data class JInt(override val value: BigInteger) : JNumber<BigInteger>() {
  override fun toString(): String = value.toString()
}

/**
 * Represents a JSON boolean value.
 */
public sealed class JBoolean : JValue<Boolean>() {
  override fun toString(): String = value.toString()
}

public data object JTrue : JBoolean() {
  override val value: Boolean get() = true
}

public data object JFalse : JBoolean() {
  override val value: Boolean get() = false
}

/**
 * Represents a JSON array value.
 */
public data class JArray(val values: List<JValue<*>>) : JValue<List<*>>() {
  public constructor(vararg fs: JValue<*>) : this(fs.toList())

  override val value: List<*> get() = values.map { it.value }
  override fun toString(): String = values.joinToString(prefix = "[", postfix = "]")
}

/**
 * Represents a JSON object value.
 */
public data class JObject(val fields: List<JField>) : JValue<Map<String, *>>() {

  public constructor(vararg fs: JField) : this(fs.toList())

  override val value: Map<String, *> get() = fields.associate { it.name to it.value.value }
  override fun toString(): String = fields.joinToString(prefix = "{", postfix = "}")
}

/**
 * Represents a JSON object field.
 */
public data class JField(val name: String, val value: JValue<*>) {
  override fun toString(): String = "$name: $value"
}

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
package com.desiderantes.moshi.ast;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;


@SuppressWarnings("rawtypes")
public class AstAdapterTests {

  @Test
  public void toAndFromJson() throws IOException {
    Moshi moshi = new Moshi.Builder().add(JValue.class, new AstAdapter()).build();
    JsonAdapter<JValue> astAdapter = moshi.adapter(JValue.class);

    JObject subject = new JObject(
       new JField("alpha", new JString("apple")),
       new JField("beta",
         new JObject(
           new JField("alpha", new JString("bacon")),
           new JField("charlie", new JString("i'm a masseuse"))
         )
       )
    );

    // language=JSON
    String jsonValue =
        "{\"alpha\":\"apple\",\"beta\":{\"alpha\":\"bacon\",\"charlie\":\"i'm a masseuse\"}}";

    assertEquals(jsonValue, astAdapter.toJson(subject));

    JValue<?> fromJson = astAdapter.fromJson(jsonValue);
    assertEquals(subject, fromJson);
  }

  @Test
  public void invalidDocumentTests() throws IOException {
    Moshi moshi = new Moshi.Builder().add(JValue.class, new AstAdapter()).build();
    JsonAdapter<JValue> astAdapter = moshi.adapter(JValue.class).lenient();

    assertEquals(JNothing.INSTANCE, astAdapter.fromJson(",."));
    assertEquals(JNull.INSTANCE, astAdapter.fromJson("null"));
    assertInstanceOf(JBoolean.class, astAdapter.fromJson("true"));
    assertInstanceOf(JBoolean.class, astAdapter.fromJson("false"));
    assertInstanceOf(JInt.class, astAdapter.fromJson("0"));
    assertInstanceOf(JInt.class, astAdapter.fromJson("1"));
    assertInstanceOf(JDouble.class, astAdapter.fromJson("1.0"));
    assertInstanceOf(JString.class, astAdapter.fromJson("\"\""));
    assertInstanceOf(JArray.class, astAdapter.fromJson("[]"));
    assertInstanceOf(JObject.class, astAdapter.fromJson("{}"));
  }
}

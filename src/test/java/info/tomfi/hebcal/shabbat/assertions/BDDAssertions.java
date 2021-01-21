/**
 * Copyright Tomer Figenblat.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.tomfi.hebcal.shabbat.assertions;

import info.tomfi.hebcal.shabbat.response.Response;
import info.tomfi.hebcal.shabbat.response.ResponseItem;
import info.tomfi.hebcal.shabbat.response.ResponseLocation;

/** Custom assertions starting point for the project. */
public final class BDDAssertions {
  private BDDAssertions() {
    //
  }

  public static ResponseAssert then(final Response actual) {
    return new ResponseAssert(actual);
  }

  public static ResponseItemAssert then(final ResponseItem actual) {
    return new ResponseItemAssert(actual);
  }

  public static ResponseLocationAssert then(final ResponseLocation actual) {
    return new ResponseLocationAssert(actual);
  }
}

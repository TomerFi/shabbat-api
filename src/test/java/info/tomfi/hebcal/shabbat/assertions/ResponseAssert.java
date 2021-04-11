/*
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
import org.assertj.core.api.AbstractAssert;

/** Custom assert for the response pojo. */
public final class ResponseAssert extends AbstractAssert<ResponseAssert, Response> {
  protected ResponseAssert(final Response actual) {
    super(actual, ResponseAssert.class);
  }

  public ResponseAssert dateIs(final String testDate) {
    isNotNull();
    if (!actual.date().equals(testDate)) {
      failWithMessage("Expected date to be <%s> but was <%s>", testDate, actual.date());
    }
    return this;
  }

  public ResponseAssert linkPresent() {
    isNotNull();
    if (actual.link().isEmpty()) {
      failWithMessage("Expected link to be present but was not");
    }
    return this;
  }

  public ResponseAssert linkEmpty() {
    isNotNull();
    if (actual.link().isPresent()) {
      failWithMessage("Expected link to be empty but was not");
    }
    return this;
  }

  public ResponseAssert linkIs(final String testLink) {
    isNotNull();
    if (!actual.link().get().equals(testLink)) {
      failWithMessage("Expected link to be <%s> but was <%s>", testLink, actual.link().get());
    }
    return this;
  }

  public ResponseAssert titleIs(final String testTitle) {
    isNotNull();
    if (!actual.title().equals(testTitle)) {
      failWithMessage("Expected title to be <%s> but was <%s>", testTitle, actual.title());
    }
    return this;
  }

  public ResponseAssert hasItems() {
    isNotNull();
    if (!actual.items().isPresent() && actual.items().get().isEmpty()) {
      failWithMessage("Expected items to be present but were not");
    }
    return this;
  }

  public ResponseAssert hasNoItems() {
    isNotNull();
    if (actual.items().isPresent() && !actual.items().get().isEmpty()) {
      failWithMessage(
          "Expected no items to be present but found <%s>", actual.items().get().size());
    }
    return this;
  }
}

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

import info.tomfi.hebcal.shabbat.response.ResponseItem;
import org.assertj.core.api.AbstractAssert;

/** Custom assert for the response item pojo. */
public final class ResponseItemAssert extends AbstractAssert<ResponseItemAssert, ResponseItem> {
  protected ResponseItemAssert(final ResponseItem actual) {
    super(actual, ResponseItemAssert.class);
  }

  public ResponseItemAssert hebrewPresent() {
    isNotNull();
    if (actual.hebrew().isEmpty()) {
      failWithMessage("Expected hebrew to be present but was not");
    }
    return this;
  }

  public ResponseItemAssert hebrewEmpty() {
    isNotNull();
    if (actual.hebrew().isPresent()) {
      failWithMessage("Expected hebrew to be empty but was not");
    }
    return this;
  }

  public ResponseItemAssert hebrewIs(final String testHebrew) {
    isNotNull();
    if (!actual.hebrew().get().equals(testHebrew)) {
      failWithMessage("Expected cc to hebrew <%s> but was <%s>", testHebrew, actual.hebrew().get());
    }
    return this;
  }

  public ResponseItemAssert dateIs(final String testDate) {
    isNotNull();
    if (!actual.date().equals(testDate)) {
      failWithMessage("Expected date to be <%s> but was <%s>", testDate, actual.date());
    }
    return this;
  }

  public ResponseItemAssert titleIs(final String testTitle) {
    isNotNull();
    if (!actual.title().equals(testTitle)) {
      failWithMessage("Expected title to be <%s> but was <%s>", testTitle, actual.title());
    }
    return this;
  }

  public ResponseItemAssert categoryIs(final String testCategory) {
    isNotNull();
    if (!actual.category().equals(testCategory)) {
      failWithMessage("Expected category to be <%s> but was <%s>", testCategory, actual.category());
    }
    return this;
  }

  public ResponseItemAssert titleOrigPresent() {
    isNotNull();
    if (actual.titleOrig().isEmpty()) {
      failWithMessage("Expected titleOrig to be present but was not");
    }
    return this;
  }

  public ResponseItemAssert titleOrigEmpty() {
    isNotNull();
    if (actual.titleOrig().isPresent()) {
      failWithMessage("Expected titleOrig to be empty but was not");
    }
    return this;
  }

  public ResponseItemAssert titleOrigIs(final String testTitleOrig) {
    isNotNull();
    if (!actual.titleOrig().get().equals(testTitleOrig)) {
      failWithMessage(
          "Expected titleOrig to be <%s> but was <%s>", testTitleOrig, actual.titleOrig().get());
    }
    return this;
  }

  public ResponseItemAssert linkPresent() {
    isNotNull();
    if (actual.link().isEmpty()) {
      failWithMessage("Expected link to be present but was not");
    }
    return this;
  }

  public ResponseItemAssert linkEmpty() {
    isNotNull();
    if (actual.link().isPresent()) {
      failWithMessage("Expected link to be empty but was not");
    }
    return this;
  }

  public ResponseItemAssert linkIs(final String testLink) {
    isNotNull();
    if (!actual.link().get().equals(testLink)) {
      failWithMessage("Expected link to be <%s> but was <%s>", testLink, actual.link().get());
    }
    return this;
  }

  public ResponseItemAssert memoPresent() {
    isNotNull();
    if (actual.memo().isEmpty()) {
      failWithMessage("Expected memo to be present but was not");
    }
    return this;
  }

  public ResponseItemAssert memoEmpty() {
    isNotNull();
    if (actual.memo().isPresent()) {
      failWithMessage("Expected memo to be empty but was not");
    }
    return this;
  }

  public ResponseItemAssert memoIs(final String testMemo) {
    isNotNull();
    if (!actual.memo().get().equals(testMemo)) {
      failWithMessage("Expected memo to be <%s> but was <%s>", testMemo, actual.memo().get());
    }
    return this;
  }

  public ResponseItemAssert subcatPresent() {
    isNotNull();
    if (actual.subcat().isEmpty()) {
      failWithMessage("Expected subcat to be present but was not");
    }
    return this;
  }

  public ResponseItemAssert subcatEmpty() {
    isNotNull();
    if (actual.subcat().isPresent()) {
      failWithMessage("Expected subcat to be empty but was not");
    }
    return this;
  }

  public ResponseItemAssert subcatIs(final String testSubcat) {
    isNotNull();
    if (!actual.subcat().get().equals(testSubcat)) {
      failWithMessage("Expected subcat to be <%s> but was <%s>", testSubcat, actual.subcat().get());
    }
    return this;
  }

  public ResponseItemAssert isYomtov() {
    isNotNull();
    if (!actual.yomtov().orElseGet(() -> false)) {
      failWithMessage("Expected to be yomtov");
    }
    return this;
  }

  public ResponseItemAssert isNotYomtov() {
    isNotNull();
    if (actual.yomtov().orElseGet(() -> false)) {
      failWithMessage("Expected not to be yomtov");
    }
    return this;
  }
}

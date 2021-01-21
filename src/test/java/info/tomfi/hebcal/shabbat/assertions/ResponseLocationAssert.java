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

import info.tomfi.hebcal.shabbat.response.ResponseLocation;
import org.assertj.core.api.AbstractAssert;

/** Custom assert for the response location pojo. */
public final class ResponseLocationAssert
    extends AbstractAssert<ResponseLocationAssert, ResponseLocation> {
  protected ResponseLocationAssert(final ResponseLocation actual) {
    super(actual, ResponseLocationAssert.class);
  }

  public ResponseLocationAssert admin1Is(final String testAdmin1) {
    isNotNull();
    if (!actual.admin1().equals(testAdmin1)) {
      failWithMessage("Expected admin1 to be <%s> but was <%s>", testAdmin1, actual.admin1());
    }
    return this;
  }

  public ResponseLocationAssert asciinameIs(final String testAsciiname) {
    isNotNull();
    if (!actual.asciiname().equals(testAsciiname)) {
      failWithMessage(
          "Expected asciiname to be <%s> but was <%s>", testAsciiname, actual.asciiname());
    }
    return this;
  }

  public ResponseLocationAssert cityIs(final String testCity) {
    isNotNull();
    if (!actual.city().equals(testCity)) {
      failWithMessage("Expected city to be <%s> but was <%s>", testCity, actual.city());
    }
    return this;
  }

  public ResponseLocationAssert countryIs(final String testCountry) {
    isNotNull();
    if (!actual.country().equals(testCountry)) {
      failWithMessage("Expected country to be <%s> but was <%s>", testCountry, actual.country());
    }
    return this;
  }

  public ResponseLocationAssert geoIs(final String testGeo) {
    isNotNull();
    if (!actual.geo().equals(testGeo)) {
      failWithMessage("Expected geo to be <%s> but was <%s>", testGeo, actual.geo());
    }
    return this;
  }

  public ResponseLocationAssert geonameidIs(final int testGeonameid) {
    isNotNull();
    if (actual.geonameid() != testGeonameid) {
      failWithMessage(
          "Expected geonameid to be <%s> but was <%s>", testGeonameid, actual.geonameid());
    }
    return this;
  }

  public ResponseLocationAssert titleIs(final String testTitle) {
    isNotNull();
    if (!actual.title().equals(testTitle)) {
      failWithMessage("Expected title to be <%s> but was <%s>", testTitle, actual.title());
    }
    return this;
  }

  public ResponseLocationAssert tzidIs(final String testTzid) {
    isNotNull();
    if (!actual.tzid().equals(testTzid)) {
      failWithMessage("Expected tzid to be <%s> but was <%s>", testTzid, actual.tzid());
    }
    return this;
  }

  public ResponseLocationAssert ccPresent() {
    isNotNull();
    if (actual.cc().isEmpty()) {
      failWithMessage("Expected cc to be present but was not");
    }
    return this;
  }

  public ResponseLocationAssert ccEmpty() {
    isNotNull();
    if (actual.cc().isPresent()) {
      failWithMessage("Expected cc to be empty but was not");
    }
    return this;
  }

  public ResponseLocationAssert ccIs(final String testCc) {
    isNotNull();
    if (!actual.cc().get().equals(testCc)) {
      failWithMessage("Expected cc to be <%s> but was <%s>", testCc, actual.cc().get());
    }
    return this;
  }

  public ResponseLocationAssert latitudeIs(final Double testLatitude) {
    isNotNull();
    if (!actual.latitude().equals(testLatitude)) {
      failWithMessage("Expected latitude to be <%s> but was <%s>", testLatitude, actual.latitude());
    }
    return this;
  }

  public ResponseLocationAssert longitudeIs(final Double testLongitude) {
    isNotNull();
    if (!actual.longitude().equals(testLongitude)) {
      failWithMessage(
          "Expected longitude to be <%s> but was <%s>", testLongitude, actual.longitude());
    }
    return this;
  }
}

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
package info.tomfi.hebcal.shabbat.request;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.time.LocalDate;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** Test cases for the verification processes of the request builder. */
@Tag("unit-tests")
final class RequestBuilderTest {
  @Test
  void building_a_request_without_setting_the_geoid_throws_an_IllegalStateException() {
    assertThatExceptionOfType(IllegalStateException.class)
        .isThrownBy(() -> Request.builder().build())
        .withMessage("geo id is mandatory for this request");
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 0})
  void building_a_request_with_a_non_positive_geoid_throws_an_IllegalArgumentException(
      final int geoId) {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> Request.builder().forGeoId(geoId))
        .withMessage("geo id should be a positive integer");
  }

  @Test
  void build_a_request_with_a_negative_minutes_before_sunset_throws_an_IllegalArgumentException() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> Request.builder().withMinutesBeforeSunset(-1))
        .withMessage("minutes before sunset should be a non negative integer");
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 0})
  void build_a_request_with_a_non_positive_minutes_after_sundown_throws_an_IllegalArgumentException(
      final int minutes) {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> Request.builder().withMinutesAfterSundown(minutes))
        .withMessage("minutes after sundown should be a positive integer");
  }

  @Test
  void building_a_request_with_a_null_date_throws_a_NullPointerException() {
    assertThatNullPointerException()
        .isThrownBy(() -> Request.builder().withDate(null).build())
        .withMessage("Null dateTime");
  }

  @Test
  void building_a_request_with_legal_arguments_does_not_throw_exceptions() {
    assertThatNoException()
        .isThrownBy(
            () ->
                Request.builder()
                    .forGeoId(12345)
                    .withDate(LocalDate.now())
                    .withMinutesBeforeSunset(0)
                    .withMinutesAfterSundown(10)
                    .build());
  }
}

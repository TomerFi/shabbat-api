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
package info.tomfi.hebcal.shabbat.request;

import static com.google.common.base.Strings.isNullOrEmpty;
import static nl.jqno.equalsverifier.Warning.INHERITED_DIRECTLY_FROM_OBJECT;
import static nl.jqno.equalsverifier.Warning.NULL_FIELDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.function.Predicate;
import java.util.stream.Stream;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/** Verify proper implementation of the equals and hashcode methods for the request value. */
@Tag("unit-tests")
final class RequestTest {
  @Test
  void verify_request_object_equals_and_hashcode_implementations() {
    // verify abstraction, not implementing equals and hashcode
    EqualsVerifier.forClass(Request.class)
        .withRedefinedSubclass(AutoValue_Request.class)
        .usingGetClass()
        .suppress(INHERITED_DIRECTLY_FROM_OBJECT)
        .verify();
    // verify implementation, not verifying nulls
    EqualsVerifier.forClass(AutoValue_Request.class)
        .withRedefinedSuperclass()
        .suppress(NULL_FIELDS)
        .verify();
  }

  @Test
  void value_request_to_string_should_yield_non_empty_string() {
    var request = Request.builder().forGeoId(111).build();
    assertThat(request.toString()).isNotEmpty();
  }

  @ParameterizedTest
  @MethodSource
  <T> void verify_enum_members_are_non_empty_strings(final T[] values, Predicate<T> matcher) {
    assertThat(values).hasSizeGreaterThan(0).allMatch(matcher::test);
  }

  static Stream<Arguments> verify_enum_members_are_non_empty_strings() {
    Predicate<ParamKeys> paramKeyPredicate = (ParamKeys k) -> !isNullOrEmpty(k.key());
    Predicate<OutputTypes> outputTypesPredicate = (OutputTypes t) -> !isNullOrEmpty(t.type());
    Predicate<GeoTypes> geoTypesPredicate = (GeoTypes t) -> !isNullOrEmpty(t.type());
    Predicate<FlagStates> flagStatePredicate = (FlagStates s) -> !isNullOrEmpty(s.state());
    return Stream.of(
        arguments(ParamKeys.values(), paramKeyPredicate),
        arguments(OutputTypes.values(), outputTypesPredicate),
        arguments(GeoTypes.values(), geoTypesPredicate),
        arguments(FlagStates.values(), flagStatePredicate));
  }
}

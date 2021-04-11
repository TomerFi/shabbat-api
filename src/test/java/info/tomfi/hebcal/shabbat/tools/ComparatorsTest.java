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
package info.tomfi.hebcal.shabbat.tools;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.BDDMockito.given;

import info.tomfi.hebcal.shabbat.response.ResponseItem;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Comparators test cases. */
@Tag("unit-tests")
@ExtendWith(MockitoExtension.class)
final class ComparatorsTest {
  @TestFactory
  Collection<DynamicTest> sort_two_items_by_item_date_and_verify_order(
      @Mock final ResponseItem firstItem, @Mock final ResponseItem secondItem) {
    // stub dates
    given(firstItem.date()).willReturn("2021-01-02T16:00:00+02:00");
    given(secondItem.date()).willReturn("2021-01-02T17:00:00+02:00");
    // sort using byItemDate and verify order
    return List.of(
        dynamicTest(
            "when the first item is before the second one, the first one will remain first",
            () ->
                assertThat(List.of(firstItem, secondItem).stream().sorted(Comparators.byItemDate()))
                    .containsExactly(firstItem, secondItem)),
        dynamicTest(
            "when the second item is before the first one, the first should become first",
            () ->
                assertThat(List.of(secondItem, firstItem).stream().sorted(Comparators.byItemDate()))
                    .containsExactly(firstItem, secondItem)));
  }

  @Test
  void instantiating_the_utility_class_with_the_default_ctor_throws_an_illegal_access_exception() {
    assertThatExceptionOfType(IllegalAccessException.class)
        .isThrownBy(() -> ItemExtractors.class.getDeclaredConstructor().newInstance());
  }
}

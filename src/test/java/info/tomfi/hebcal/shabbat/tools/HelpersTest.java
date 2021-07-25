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

import static info.tomfi.hebcal.shabbat.response.ItemCategory.CANDLES;
import static info.tomfi.hebcal.shabbat.response.ItemCategory.HAVDALAH;
import static info.tomfi.hebcal.shabbat.response.ItemCategory.HOLIDAY;
import static info.tomfi.hebcal.shabbat.tools.Comparators.byItemDate;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.when;
import static org.mockito.quality.Strictness.LENIENT;

import info.tomfi.hebcal.shabbat.response.ItemCategory;
import info.tomfi.hebcal.shabbat.response.Response;
import info.tomfi.hebcal.shabbat.response.ResponseItem;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junitpioneer.jupiter.CartesianProductTest;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

/** Various tools and helper methods to aid working with responses test cases. */
@Tag("unit-tests")
@MockitoSettings(strictness = LENIENT)
final class HelpersTest {
  @Mock private ResponseItem holidayItem;
  @Mock private ResponseItem candlesHolidayItem;
  @Mock private ResponseItem havdalaHolidayItem;
  @Mock private ResponseItem candlesShabbatItem;
  @Mock private ResponseItem havdalaShabbatItem;

  private List<ResponseItem> allItems;
  private List<ResponseItem> holidayOnlyItems;

  private LocalDate fridayDate;
  private LocalDate saturdayDate;

  @BeforeEach
  void initialize() {
    // populate item lists
    allItems =
        List.of(candlesHolidayItem, havdalaHolidayItem, candlesShabbatItem, havdalaShabbatItem);
    holidayOnlyItems = List.of(candlesHolidayItem, havdalaHolidayItem);
    // create local date objects for invoking the methods
    fridayDate = LocalDate.parse("2019-10-18", ISO_LOCAL_DATE);
    saturdayDate = LocalDate.parse("2019-10-19", ISO_LOCAL_DATE);
    // stub holiday item with type
    when(holidayItem.category()).thenReturn(HOLIDAY.toString());
    // stub holiday candles itesm with type, date and time
    when(candlesHolidayItem.category()).thenReturn(CANDLES.toString());
    when(candlesHolidayItem.date()).thenReturn("2019-10-13T17:53:00+03:00");
    // stub holiday havdalah itesm with type, date and time
    when(havdalaHolidayItem.category()).thenReturn(HAVDALAH.toString());
    when(havdalaHolidayItem.date()).thenReturn("2019-10-14T19:00:00+03:00");
    // stub shabbat candles itesm with type, date and time
    when(candlesShabbatItem.category()).thenReturn(CANDLES.toString());
    when(candlesShabbatItem.date()).thenReturn("2019-10-18T17:47:00+03:00");
    // stub shabbat havdalah itesm with type, date and time
    when(havdalaShabbatItem.category()).thenReturn(HAVDALAH.toString());
    when(havdalaShabbatItem.date()).thenReturn("2019-10-19T18:54:00+03:00");
  }

  @TestFactory
  Collection<DynamicTest> retrieving_shabbat_candles_and_havdalah_items_from_predefined_lists() {
    return List.of(
        dynamicTest(
            "the nearest shabbat candles item for friday is friday and not holiday sunday",
            () ->
                assertThat(Helpers.getFirstByDate(allItems, fridayDate, CANDLES))
                    .hasValue(candlesShabbatItem)),
        dynamicTest(
            "when no shabbat candles item found, returning optional and not failing",
            () ->
                assertThat(Helpers.getFirstByDate(holidayOnlyItems, fridayDate, CANDLES))
                    .isEmpty()),
        dynamicTest(
            "the nearest shabbat havdalah item for saturday is saturday and not holiday monday",
            () ->
                assertThat(Helpers.getFirstByDate(allItems, saturdayDate, HAVDALAH))
                    .hasValue(havdalaShabbatItem)),
        dynamicTest(
            "when no shabbat havdalah item found, returning optional and not failing",
            () ->
                assertThat(Helpers.getFirstByDate(holidayOnlyItems, saturdayDate, HAVDALAH))
                    .isEmpty()));
  }

  @Test
  void matching_and_sorting_a_full_items_list_returns_only_sorted_candles_and_havdalah_items() {
    // when invoking the getItemFromResponse for the CANDLES and HAVDALAH categories
    var items = Helpers.matchAndSort(allItems, byItemDate(), CANDLES, HAVDALAH);
    // then the items returned doesn't include the holiday item, its not a candles nor havdala item
    // and the items in the list is sorted by the date of the item
    assertThat(items)
        .containsExactly(
            candlesHolidayItem, havdalaHolidayItem, candlesShabbatItem, havdalaShabbatItem)
        .isSortedAccordingTo(byItemDate());
  }

  @Test
  void matching_with_no_categories_selected_throws_an_illegal_argument_exception() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> Helpers.matchAndSort(allItems, byItemDate()))
        .withMessage("at least one item category is required");
  }

  @Test
  void matching_with_null_as_a_one_of_the_categories_selected_throws_a_null_pointer_exception() {
    assertThatNullPointerException()
        .isThrownBy(() -> Helpers.matchAndSort(allItems, byItemDate(), ItemCategory.CANDLES, null));
  }

  @Test
  void instantiating_the_utility_class_with_the_default_ctor_throws_an_illegal_access_exception() {
    assertThatExceptionOfType(IllegalAccessException.class)
        .isThrownBy(() -> Helpers.class.getDeclaredConstructor().newInstance());
  }

  @TestFactory
  Stream<DynamicTest> getting_the_first_item_by_category_should_return_the_first_occurence_of_the_matching_item(
      @Mock Response response) {
    given(response.items()).willReturn(Optional.of(allItems));
    return Stream.of(
      dynamicTest(
        "return the candles item for the CANDLES category using the undelining method",
        () -> assertThat(Helpers.getFirstItemOf(response, CANDLES)).hasValue(candlesHolidayItem)),
      dynamicTest(
        "return the havdalah item for the HAVDALAH  using the undelining method",
        () -> assertThat(Helpers.getFirstItemOf(response, HAVDALAH)).hasValue(havdalaHolidayItem)),
      dynamicTest(
        "return the candles item for the CANDLES category using the helper method",
        () -> assertThat(Helpers.getCandlesItem(response)).hasValue(candlesHolidayItem)),
      dynamicTest(
        "return the havdalah item for the HAVDALAH category using the helper method",
        () -> assertThat(Helpers.getHavdalaItem(response)).hasValue(havdalaHolidayItem))
    );
  }

  @CartesianProductTest(factory = "categoryItemsAndEmptyItemListsMatrix")
  void attempting_to_get_the_first_item_of_a_category_from_a_response_with_no_items_should_throw_an_exception(
      final ItemCategory category, Optional<List<ResponseItem>> returnItems, @Mock final Response response) {
    given(response.items()).willReturn(returnItems);
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> Helpers.getFirstItemOf(response, category))
        .withMessage("response has no items");
  }

  static CartesianProductTest.Sets categoryItemsAndEmptyItemListsMatrix() {
    return new CartesianProductTest.Sets()
        .addAll(Arrays.stream(ItemCategory.values()))
        .add(
          Optional.<ResponseItem> empty(),
          Optional.of(Collections.<ResponseItem> emptyList()));
  }

  @TestFactory
  Stream<DynamicTest> verify_retrieval_of_the_shabbat_start_and_end_times_from_the_response(
      @Mock final Response response) {
    given(response.items()).willReturn(Optional.of(allItems));
    return Stream.of(
      dynamicTest(
        "verify the shabbat start time is created from the candles holiday item",
        () -> assertThat(Helpers.getShabbatStart(response))
            .isEqualTo(OffsetDateTime.parse("2019-10-13T17:53:00+03:00", ISO_OFFSET_DATE_TIME))),
      dynamicTest(
        "verify the shabbat end time is created from the havdalah holiday item",
        () -> assertThat(Helpers.getShabbatEnd(response))
            .isEqualTo(OffsetDateTime.parse("2019-10-14T19:00:00+03:00", ISO_OFFSET_DATE_TIME)))
    );
  }

  @Test
  void attempting_to_get_the_shabbat_start_time_with_no_candles_item_should_throw_an_exception(
      @Mock final Response response) {
    given(response.items()).willReturn(Optional.of(List.of(holidayItem, havdalaShabbatItem)));
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> Helpers.getShabbatStart(response))
        .withMessage("no candles item found");
  }

  @Test
  void attempting_to_get_the_shabbat_end_time_with_no_havdalah_item_should_throw_an_exception(
      @Mock final Response response) {
    given(response.items()).willReturn(Optional.of(List.of(holidayItem, candlesShabbatItem)));
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> Helpers.getShabbatEnd(response))
        .withMessage("no havdala item found");
  }
}

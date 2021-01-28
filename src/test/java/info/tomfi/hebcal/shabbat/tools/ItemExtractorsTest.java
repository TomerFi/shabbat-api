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
package info.tomfi.hebcal.shabbat.tools;

import static info.tomfi.hebcal.shabbat.response.ItemCategory.CANDLES;
import static info.tomfi.hebcal.shabbat.response.ItemCategory.HAVDALAH;
import static info.tomfi.hebcal.shabbat.response.ItemCategory.HOLIDAY;
import static info.tomfi.hebcal.shabbat.tools.Comparators.byItemDate;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static org.assertj.core.api.BDDAssertions.assertThat;
import static org.assertj.core.api.BDDAssertions.assertThatExceptionOfType;
import static org.assertj.core.api.BDDAssertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.BDDMockito.when;
import static org.mockito.quality.Strictness.LENIENT;

import info.tomfi.hebcal.shabbat.response.ItemCategory;
import info.tomfi.hebcal.shabbat.response.ResponseItem;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

/** Extracting tools test cases. */
@Tag("unit-tests")
@MockitoSettings(strictness = LENIENT)
final class ItemExtractorsTest {
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
                assertThat(ItemExtractors.extractByDate(allItems, fridayDate, CANDLES))
                    .hasValue(candlesShabbatItem)),
        dynamicTest(
            "when no shabbat candles item found, returning optional and not failing",
            () ->
                assertThat(ItemExtractors.extractByDate(holidayOnlyItems, fridayDate, CANDLES))
                    .isEmpty()),
        dynamicTest(
            "the nearest shabbat havdalah item for saturday is saturday and not holiday monday",
            () ->
                assertThat(ItemExtractors.extractByDate(allItems, saturdayDate, HAVDALAH))
                    .hasValue(havdalaShabbatItem)),
        dynamicTest(
            "when no shabbat havdalah item found, returning optional and not failing",
            () ->
                assertThat(ItemExtractors.extractByDate(holidayOnlyItems, saturdayDate, HAVDALAH))
                    .isEmpty()));
  }

  @Test
  void extracting_and_sorting_a_full_items_list_returns_only_sorted_candles_and_havdalah_items() {
    // when invoking the getItemFromResponse for the CANDLES and HAVDALAH categories
    var items = ItemExtractors.extractAndSort(allItems, byItemDate(), CANDLES, HAVDALAH);
    // then the items returned doesn't include the holiday item, its not a candles nor havdala item
    // and the items in the list is sorted by the date of the item
    then(items)
        .containsExactly(
            candlesHolidayItem, havdalaHolidayItem, candlesShabbatItem, havdalaShabbatItem)
        .isSortedAccordingTo(byItemDate());
  }

  @Test
  void extracting_with_no_categories_selected_throws_an_illegal_argument_exception() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> ItemExtractors.extractAndSort(allItems, byItemDate()));
  }

  @Test
  void extracting_with_null_as_a_one_of_the_categories_selected_throws_a_null_pointer_exception() {
    assertThatNullPointerException()
        .isThrownBy(
            () ->
                ItemExtractors.extractAndSort(allItems, byItemDate(), ItemCategory.CANDLES, null));
  }

  @Test
  void instantiating_the_utility_class_with_the_default_ctor_throws_an_illegal_access_exception() {
    assertThatExceptionOfType(IllegalAccessException.class)
        .isThrownBy(() -> ItemExtractors.class.getDeclaredConstructor().newInstance());
  }
}

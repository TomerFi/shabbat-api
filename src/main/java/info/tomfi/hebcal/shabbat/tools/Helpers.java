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
import static java.time.ZonedDateTime.parse;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import info.tomfi.hebcal.shabbat.response.ItemCategory;
import info.tomfi.hebcal.shabbat.response.Response;
import info.tomfi.hebcal.shabbat.response.ResponseItem;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/** Various tools and helper methods to aid working with responses. */
public final class Helpers {
  private Helpers() {
    //
  }

  /**
   * Sort using the given comparator, all {@link ResponseItem} objects in the given {@link Response}
   * matching the given {@link ItemCategory}.
   *
   * @param response the response to get the items from.
   * @param comparator the response item comprator used for sorting.
   * @param categories the item categories to match list item to.
   * @return a sorted list of the matched response item.
   */
  public static List<ResponseItem> matchAndSort(
      final Response response,
      final Comparator<? super ResponseItem> comparator,
      final ItemCategory... categories) {
    var itemsOpt = response.items();
    if (itemsOpt.isEmpty() || itemsOpt.get().isEmpty()) {
      throw new IllegalArgumentException("response has no items");
    }
    if (categories.length <= 0) {
      throw new IllegalArgumentException("at least one item category is required");
    }
    for (var cat : categories) {
      requireNonNull(cat, () -> "a category can not be null");
    }
    var catList = Stream.of(categories).map(ItemCategory::toString).collect(toSet());
    return itemsOpt.get().stream()
        .filter(it -> catList.contains(it.category()))
        .sorted(comparator)
        .collect(toList());
  }

  /**
   * Get the first occurrence of an item matching the given {@link ItemCategory} and
   * {@link LocalDate}.
   *
   * @param items list of the items to iterate over.
   * @param localDate the local date to match the items to.
   * @param category the category to match the items to.
   * @return an optional response item.
   */
  public static Optional<ResponseItem> getFirstByDate(
      final List<ResponseItem> items, final LocalDate localDate, final ItemCategory category) {
    return items.stream()
        .filter(item -> item.category().equals(category.toString()))
        .filter(item -> parse(item.date(), ISO_OFFSET_DATE_TIME).toLocalDate().equals(localDate))
        .findFirst();
  }

  /**
   * Get the first occurrence of an item from a given response.
   *
   * @param response the response to get the item from.
   * @param category the category to match the item to.
   * @return an optional response item.
   */
  public static Optional<ResponseItem> getFirstItemOf(
      final Response response, final ItemCategory category) {
    var items = response.items();
    if (items.isEmpty() || items.get().isEmpty()) {
      throw new IllegalArgumentException("response has no items");
    }
    return items.get().stream()
        .filter(item -> item.category().equals(category.toString())).findFirst();
  }

  /**
   * Get the first occurrence of the Candles item from a given response.
   *
   * @param response the response to get the candles item from.
   * @return an optional response item.
   */
  public static Optional<ResponseItem> getCandlesItem(final Response response) {
    return getFirstItemOf(response, CANDLES);
  }

  /**
   * Get the first occurrence of the Havdalah item from a given response.
   *
   * @param response the response to get the havdalah item from.
   * @return an optional response item.
   */
  public static Optional<ResponseItem> getHavdalaItem(final Response response) {
    return getFirstItemOf(response, HAVDALAH);
  }

  /**
   * Get the shabbat start time from a given response.
   *
   * @param response the response to get the start time from.
   * @return an OffsetDateTime instance represnting the start time of the shabbat.
   */
  public static OffsetDateTime getShabbatStart(final Response response) {
    var item = getCandlesItem(response);
    if (item.isPresent()) {
      return OffsetDateTime.parse(item.get().date(), ISO_OFFSET_DATE_TIME);
    }
    throw new IllegalArgumentException("no candles item found");
  }

  /**
   * Get the shabbat end time from a given response.
   *
   * @param response the response to get the end time from.
   * @return an OffsetDateTime instance represnting the end time of the shabbat.
   */
  public static OffsetDateTime getShabbatEnd(final Response response) {
    var item = getHavdalaItem(response);
    if (item.isPresent()) {
      return OffsetDateTime.parse(item.get().date(), ISO_OFFSET_DATE_TIME);
    }
    throw new IllegalArgumentException("no havdala item found");
  }
}

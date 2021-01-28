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

import static java.time.ZonedDateTime.parse;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import info.tomfi.hebcal.shabbat.response.ItemCategory;
import info.tomfi.hebcal.shabbat.response.ResponseItem;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/** Tools used for extracting data from the responses. */
public final class ItemExtractors {
  private ItemExtractors() {
    //
  }

  /**
   * Extract items by a given category from a given list and sort by a given comparator.
   *
   * @param items the list items to iterate over.
   * @param sorter the response item comprator used for sorting.
   * @param categories the item categories to be extracted.
   * @return a list of the extracted response item.
   */
  public static List<ResponseItem> extractAndSort(
      final List<ResponseItem> items,
      final Comparator<? super ResponseItem> sorter,
      final ItemCategory... categories) {
    if (categories.length <= 0) {
      throw new IllegalArgumentException("at least one item category is required");
    }
    for (var cat : categories) {
      requireNonNull(cat, () -> "a category can not be null");
    }
    var catList = Stream.of(categories).map(ItemCategory::toString).collect(toSet());
    return items.stream()
        .filter(it -> catList.contains(it.category()))
        .sorted(sorter)
        .collect(toList());
  }

  /**
   * Extract an item from a given list matching a given date and category.
   *
   * @param items list of the items to iterate over.
   * @param shabbatDate the date to match the items to.
   * @param category the category to match the item to.
   * @return an optional response item.
   */
  public static Optional<ResponseItem> extractByDate(
      final List<ResponseItem> items, final LocalDate shabbatDate, final ItemCategory category) {
    return items.stream()
        .filter(item -> item.category().equals(category.toString()))
        .filter(item -> parse(item.date(), ISO_OFFSET_DATE_TIME).toLocalDate().equals(shabbatDate))
        .findFirst();
  }
}

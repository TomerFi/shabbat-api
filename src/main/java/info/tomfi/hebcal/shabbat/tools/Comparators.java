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

import info.tomfi.hebcal.shabbat.response.ResponseItem;
import java.util.Comparator;

/** Useful comparators. */
public final class Comparators {
  private Comparators() {
    //
  }

  public static Comparator<? super ResponseItem> byItemDate() {
    return (p, c) ->
        parse(p.date(), ISO_OFFSET_DATE_TIME).compareTo(parse(c.date(), ISO_OFFSET_DATE_TIME));
  }
}

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

import static java.util.Objects.requireNonNull;

import com.google.auto.value.AutoValue;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/** Value abstraction used to represent the API request. */
@AutoValue
public abstract class Request {
  public abstract Map<String, String> queryParams();

  public static Builder builder() {
    return new Builder();
  }

  /** Builder used to construct API requests. */
  public static final class Builder {
    private static final String DEFAULT_HAVDALAH = "50";
    private static final String DEFAULT_CANDLE_LIGHTING = "18";

    private Map<String, String> queryParams;

    /** The builder constructor sets the default query parameters needed for the request. */
    public Builder() {
      queryParams = new HashMap<>();
      queryParams.put(ParamKey.OUTPUT_FORMAT.toString(), OutputType.JSON.toString());
      queryParams.put(ParamKey.INCLUDE_TURAH_HAFTARAH.toString(), FlagState.OFF.toString());
      queryParams.put(ParamKey.ASHKENAZIS_TRANSLITERATIONS.toString(), FlagState.OFF.toString());
      queryParams.put(ParamKey.GEO_TYPE.toString(), GeoType.GEO_NAME.toString());
      queryParams.put(ParamKey.HAVDALAH.toString(), DEFAULT_HAVDALAH);
      queryParams.put(ParamKey.CANDLE_LIGHTING.toString(), DEFAULT_CANDLE_LIGHTING);
    }

    /**
     * Build the request.
     *
     * @return the constructed request value.
     */
    public Request build() {
      if (queryParams.containsKey(ParamKey.GEO_ID.toString())) {
        return new AutoValue_Request(queryParams);
      }
      throw new IllegalStateException("geo id is mandatory for this request");
    }

    /**
     * Set the havdalah minutes (after sundown) to be calculated. If not set, the default {@value
     * DEFAULT_HAVDALAH} will be used.
     *
     * @param minutes the minutes to calculate.
     * @return the fluent builder instance.
     */
    public Builder withMinutesAfterSundown(final int minutes) {
      if (minutes <= 0) {
        throw new IllegalArgumentException("minutes after sundown should be a positive integer");
      }
      queryParams.put(ParamKey.HAVDALAH.toString(), String.valueOf(minutes));
      return this;
    }

    /**
     * Set the candle light minutes (before sunset) to be calculated. If not set, the default
     * {@value DEFAULT_CANDLE_LIGHTING} will be used.
     *
     * @param minutes the minutes to calculate.
     * @return the fluent builder instance.
     */
    public Builder withMinutesBeforeSunset(final int minutes) {
      if (minutes < 0) {
        throw new IllegalArgumentException(
            "minutes before sunset should be a non negative integer");
      }
      queryParams.put(ParamKey.CANDLE_LIGHTING.toString(), String.valueOf(minutes));
      return this;
    }

    /**
     * Set the geoid for the request, any geoid supported by HebCall will work. The various geoids
     * can be found in https://www.geonames.org/.
     *
     * @param geoId the geoid to incorporate into the request.
     * @return the fluent builder instance.
     */
    public Builder forGeoId(final int geoId) {
      if (geoId <= 0) {
        throw new IllegalArgumentException("geo id should be a positive integer");
      }
      queryParams.put(ParamKey.GEO_ID.toString(), String.valueOf(geoId));
      return this;
    }

    /**
     * Set a specific date for the request.
     *
     * @param dateTime the date to incorporate into the request.
     * @return the fluent builder instance.
     */
    public Builder withDate(final LocalDate dateTime) {
      requireNonNull(dateTime, "Null dateTime");
      var year = String.valueOf(dateTime.getYear());
      var month = String.format("0%s", String.valueOf(dateTime.getMonthValue()));
      var day = String.format("0%s", String.valueOf(dateTime.getDayOfMonth()));

      queryParams.put(ParamKey.GREGORIAN_YEAR.toString(), year);
      queryParams.put(ParamKey.GREGORIAN_MONTH.toString(), month.substring(month.length() - 2));
      queryParams.put(ParamKey.GREGORIAN_DAY.toString(), day.substring(month.length() - 2));
      return this;
    }
  }
}

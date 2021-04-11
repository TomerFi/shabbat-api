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
package info.tomfi.hebcal.shabbat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.auto.value.AutoValue;
import info.tomfi.hebcal.shabbat.internal.Nullable;
import java.util.Optional;

/** Value abstraction used to represent the location json object from the API response. */
@AutoValue
@JsonDeserialize(builder = AutoValue_ResponseLocation.Builder.class)
public abstract class ResponseLocation {
  public abstract Optional<String> admin1();

  public abstract Optional<String> asciiname();

  public abstract String city();

  public abstract String country();

  public abstract String geo();

  public abstract int geonameid();

  public abstract Double latitude();

  public abstract Double longitude();

  public abstract Optional<String> cc();

  public abstract String title();

  public abstract Optional<String> tzid();

  /** Builder abstraction used to deserialize the location json object from the API response. */
  @AutoValue.Builder
  public abstract static class Builder {
    @JsonProperty("admin1")
    public abstract Builder admin1(@Nullable String admin1);

    @JsonProperty("asciiname")
    public abstract Builder asciiname(@Nullable String asciiname);

    @JsonProperty("city")
    public abstract Builder city(String city);

    @JsonProperty("country")
    public abstract Builder country(String country);

    @JsonProperty("geo")
    public abstract Builder geo(String geo);

    @JsonProperty("geonameid")
    public abstract Builder geonameid(int geonameid);

    @JsonProperty("latitude")
    public abstract Builder latitude(Double latitude);

    @JsonProperty("longitude")
    public abstract Builder longitude(Double longitude);

    @JsonProperty("cc")
    public abstract Builder cc(@Nullable String cc);

    @JsonProperty("title")
    public abstract Builder title(String title);

    @JsonProperty("tzid")
    public abstract Builder tzid(@Nullable String tzid);

    public abstract ResponseLocation build();
  }
}

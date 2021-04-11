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
import java.util.List;
import java.util.Optional;

/** Value abstraction used to represent the json API response. */
@AutoValue
@JsonDeserialize(builder = AutoValue_Response.Builder.class)
public abstract class Response {
  public abstract String date();

  public abstract Optional<List<ResponseItem>> items();

  public abstract Optional<String> link();

  public abstract ResponseLocation location();

  public abstract String title();

  /** Builder abstraction used to deserialize the json API response. */
  @AutoValue.Builder
  public abstract static class Builder {
    @JsonProperty("date")
    public abstract Builder date(String date);

    @JsonProperty("items")
    public abstract Builder items(@Nullable List<ResponseItem> items);

    @JsonProperty("link")
    public abstract Builder link(@Nullable String link);

    @JsonProperty("location")
    public abstract Builder location(ResponseLocation location);

    @JsonProperty("title")
    public abstract Builder title(String title);

    public abstract Response build();
  }
}

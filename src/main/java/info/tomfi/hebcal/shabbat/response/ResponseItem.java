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

/** Value abstraction used to represent an item json object from the API response. */
@AutoValue
@JsonDeserialize(builder = AutoValue_ResponseItem.Builder.class)
public abstract class ResponseItem {
  public abstract Optional<String> hebrew();

  public abstract String date();

  public abstract String title();

  public abstract String category();

  public abstract Optional<String> titleOrig();

  public abstract Optional<String> link();

  public abstract Optional<String> memo();

  public abstract Optional<String> subcat();

  public abstract Optional<Boolean> yomtov();

  /** Builder abstraction used to deserialize an item json object from the API response. */
  @AutoValue.Builder
  public abstract static class Builder {
    @JsonProperty("hebrew")
    public abstract Builder hebrew(@Nullable String hebrew);

    @JsonProperty("date")
    public abstract Builder date(String date);

    @JsonProperty("title")
    public abstract Builder title(String title);

    @JsonProperty("category")
    public abstract Builder category(String category);

    @JsonProperty("title_orig")
    public abstract Builder titleOrig(@Nullable String titleOrig);

    @JsonProperty("link")
    public abstract Builder link(@Nullable String link);

    @JsonProperty("memo")
    public abstract Builder memo(@Nullable String memo);

    @JsonProperty("subcat")
    public abstract Builder subcat(@Nullable String subcat);

    @JsonProperty("yomtov")
    public abstract Builder yomtov(@Nullable Boolean yomtov);

    public abstract ResponseItem build();
  }
}

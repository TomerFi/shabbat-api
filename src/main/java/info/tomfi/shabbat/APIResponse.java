package info.tomfi.shabbat;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Value class used to represent an API response. */
@JsonDeserialize(builder = APIResponse.Builder.class)
public final class APIResponse {
  public final String date;
  public final Optional<List<Item>> items;
  public final Optional<String> link;
  public final Location location;
  public final String title;
  public final Optional<DateRange> range;

  private Optional<APIResponse.Item> candlesItem;
  private Optional<APIResponse.Item> havdalahItem;
  private Optional<APIResponse.Item> parashaItem;
  private Optional<APIResponse.Item> roshChodeshItem;

  /** Enum for selecting a Shabbat item. */
  public enum ShabbatItem {
    SHABBAT_START,
    SHABBAT_END,
    PARASHAT_SHABBAT,
    ROSH_CHODESH
  }

  private APIResponse(
      final String date,
      final Optional<List<Item>> items,
      final Optional<String> link,
      final Location location,
      final String title,
      final Optional<DateRange> range) {
    this.date = date;
    this.items = items;
    this.link = link;
    this.location = location;
    this.title = title;
    this.range = range;
  }

  @JsonIgnore
  @Override
  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof APIResponse)) {
      return false;
    }
    var apiResponse = (APIResponse) o;
    return Objects.equals(date, apiResponse.date)
        && Objects.equals(this.items, apiResponse.items)
        && Objects.equals(this.link, apiResponse.link)
        && Objects.equals(this.location, apiResponse.location)
        && Objects.equals(this.title, apiResponse.title)
        && Objects.equals(this.range, apiResponse.range);
  }

  @JsonIgnore
  @Override
  public int hashCode() {
    return Objects.hash(this.date, this.items, this.link, this.location, this.title, this.range);
  }

  @JsonIgnore
  private Optional<Item> getFirstItemOf(final Item.Category category) {
    if (this.items.isEmpty() || this.items.get().isEmpty()) {
      throw new IllegalArgumentException("response has no items");
    }
    return this.items.get().stream().filter(item -> item.category.equals(category)).findFirst();
  }

  @JsonIgnore
  private Optional<Item> getParashaItem(final String name) {
    if (this.items.isEmpty() || this.items.get().isEmpty()) {
      throw new IllegalArgumentException("response has no items");
    }
    return this.items.get().stream()
        .filter(item -> item.category.equals(Item.Category.PARASHAT))
        .filter(item -> item.title.equals(name))
        .findFirst();
  }

  /**
   * Get a singleton of the response's first {@link Item} based on {@link ShabbatItem}.
   *
   * @param shabbatItem the type of item to retrieve
   * @return an Optional Item.
   */
  @JsonIgnore
  public Optional<Item> getShabbatItem(final ShabbatItem shabbatItem) {
    switch (shabbatItem) {
      case SHABBAT_START:
        {
          if (Objects.isNull(candlesItem)) {
            candlesItem = getFirstItemOf(Item.Category.CANDLES);
          }
          return candlesItem;
        }
      case SHABBAT_END:
        {
          if (Objects.isNull(havdalahItem)) {
            havdalahItem = getFirstItemOf(Item.Category.HAVDALAH);
          }
          return havdalahItem;
        }
      case PARASHAT_SHABBAT:
        {
          if (Objects.isNull(parashaItem)) {
            parashaItem = getParashaItem(getShabbatParasha());
          }
          return parashaItem;
        }
      case ROSH_CHODESH:
        {
          if (Objects.isNull(roshChodeshItem)) {
            roshChodeshItem = getFirstItemOf(Item.Category.ROSH_CHODESH);
          }
          return roshChodeshItem;
        }
      default:
        return Optional.empty();
    }
  }

  /**
   * Get the shabbat start time.
   *
   * @return an OffsetDateTime instance representing the start time of the shabbat.
   */
  @JsonIgnore
  public OffsetDateTime getShabbatStart() {
    var item = getShabbatItem(ShabbatItem.SHABBAT_START);
    if (item.isPresent()) {
      return OffsetDateTime.parse(item.get().date, ISO_OFFSET_DATE_TIME);
    }
    throw new IllegalArgumentException("no candles item found");
  }

  /**
   * Get the shabbat end time.
   *
   * @return an OffsetDateTime instance representing the end time of the shabbat.
   */
  @JsonIgnore
  public OffsetDateTime getShabbatEnd() {
    var item = getShabbatItem(ShabbatItem.SHABBAT_END);
    if (item.isPresent()) {
      return OffsetDateTime.parse(item.get().date, ISO_OFFSET_DATE_TIME);
    }
    throw new IllegalArgumentException("no havdala item found");
  }

  /**
   * Get the shabbat parasha name.
   *
   * @return a String representing the parasha name.
   */
  @JsonIgnore
  public String getShabbatParasha() {
    var item = getShabbatItem(ShabbatItem.SHABBAT_START);
    if (item.isPresent()) {
      if (item.get().memo.isPresent()) {
        return item.get().memo.get();
      }
      throw new IllegalArgumentException("candles item has doesn't states parasha");
    }
    throw new IllegalArgumentException("no candles item found");
  }

  @JsonIgnore
  public Boolean isRoshChodesh() {
    return getShabbatItem(ShabbatItem.ROSH_CHODESH).isPresent();
  }

  @JsonPOJOBuilder
  static class Builder {
    private String date;
    private Optional<List<Item>> items;
    private Optional<String> link;
    private Location location;
    private String title;
    private Optional<DateRange> range;

    Builder withDate(final String date) {
      this.date = date;
      return this;
    }

    Builder withItems(final List<Item> items) {
      this.items = Optional.of(items);
      return this;
    }

    Builder withLink(final String link) {
      this.link = Optional.of(link);
      return this;
    }

    Builder withLocation(final Location location) {
      this.location = location;
      return this;
    }

    Builder withTitle(final String title) {
      this.title = title;
      return this;
    }

    Builder withRange(final DateRange range) {
      this.range = Optional.of(range);
      return this;
    }

    APIResponse build() {
      return new APIResponse(
          Objects.requireNonNull(this.date),
          Objects.isNull(this.items) ? Optional.empty() : this.items,
          Objects.isNull(this.link) ? Optional.empty() : this.link,
          Objects.requireNonNull(this.location),
          Objects.requireNonNull(this.title),
          Objects.isNull(this.range) ? Optional.empty() : this.range);
    }
  }

  /** Value class used to represent the range object of the API response. */
  @JsonDeserialize(builder = DateRange.Builder.class)
  public static final class DateRange {
    public final LocalDate start;
    public final LocalDate end;

    private DateRange(final LocalDate start, final LocalDate end) {
      this.start = start;
      this.end = end;
    }

    @JsonIgnore
    @Override
    public boolean equals(final Object o) {
      if (o == this) {
        return true;
      }
      if (!(o instanceof DateRange)) {
        return false;
      }
      var dateRange = (DateRange) o;
      return Objects.equals(this.start, dateRange.start) && Objects.equals(this.end, dateRange.end);
    }

    @JsonIgnore
    @Override
    public int hashCode() {
      return Objects.hash(this.start, this.end);
    }

    static class Builder {
      public LocalDate start;
      public LocalDate end;

      Builder withStart(final String start) {
        this.start = LocalDate.parse(start);
        return this;
      }

      Builder withEnd(final String end) {
        this.end = LocalDate.parse(end);
        return this;
      }

      DateRange build() {
        return new DateRange(Objects.requireNonNull(start), Objects.requireNonNull(end));
      }
    }
  }

  /** Value class used to represent the location object of the API response. */
  @JsonDeserialize(builder = Location.Builder.class)
  public static final class Location {
    public final Optional<String> admin1;
    public final Optional<String> asciiname;
    public final String city;
    public final String country;
    public final String geo;
    public final int geonameid;
    public final Double latitude;
    public final Double longitude;
    public final Optional<String> cc;
    public final String title;
    public final Optional<String> tzid;

    private Location(
        final Optional<String> admin1,
        final Optional<String> asciiname,
        final String city,
        final String country,
        final String geo,
        final int geonameid,
        final Double latitude,
        final Double longitude,
        final Optional<String> cc,
        final String title,
        final Optional<String> tzid) {
      this.admin1 = admin1;
      this.asciiname = asciiname;
      this.city = city;
      this.country = country;
      this.geo = geo;
      this.geonameid = geonameid;
      this.latitude = latitude;
      this.longitude = longitude;
      this.cc = cc;
      this.title = title;
      this.tzid = tzid;
    }

    @JsonIgnore
    @Override
    public boolean equals(final Object o) {
      if (o == this) {
        return true;
      }
      if (!(o instanceof Location)) {
        return false;
      }
      var location = (Location) o;
      return Objects.equals(this.admin1, location.admin1)
          && Objects.equals(this.asciiname, location.asciiname)
          && Objects.equals(this.city, location.city)
          && Objects.equals(this.country, location.country)
          && Objects.equals(this.geo, location.geo)
          && Objects.equals(this.geonameid, location.geonameid)
          && Objects.equals(this.latitude, location.latitude)
          && Objects.equals(this.longitude, location.longitude)
          && Objects.equals(this.cc, location.cc)
          && Objects.equals(this.title, location.title)
          && Objects.equals(this.tzid, location.tzid);
    }

    @JsonIgnore
    @Override
    public int hashCode() {
      return Objects.hash(
          this.admin1,
          this.asciiname,
          this.city,
          this.country,
          this.geo,
          this.geonameid,
          this.latitude,
          this.longitude,
          this.cc,
          this.title,
          this.tzid);
    }

    @JsonPOJOBuilder
    static class Builder {
      private Optional<String> admin1;
      private Optional<String> asciiname;
      private String city;
      private String country;
      private String geo;
      private int geonameid;
      private Double latitude;
      private Double longitude;
      private Optional<String> cc;
      private String title;
      private Optional<String> tzid;

      Builder withAdmin1(final String admin1) {
        this.admin1 = Optional.of(admin1);
        return this;
      }

      Builder withAsciiname(final String asciiname) {
        this.asciiname = Optional.of(asciiname);
        return this;
      }

      Builder withCity(final String city) {
        this.city = city;
        return this;
      }

      Builder withCountry(final String country) {
        this.country = country;
        return this;
      }

      Builder withGeo(final String geo) {
        this.geo = geo;
        return this;
      }

      Builder withGeonameid(final int geonameid) {
        this.geonameid = geonameid;
        return this;
      }

      Builder withLatitude(final Double latitude) {
        this.latitude = latitude;
        return this;
      }

      Builder withLongitude(final Double longitude) {
        this.longitude = longitude;
        return this;
      }

      Builder withCc(final String cc) {
        this.cc = Optional.of(cc);
        return this;
      }

      Builder withTitle(final String title) {
        this.title = title;
        return this;
      }

      Builder withTzid(final String tzid) {
        this.tzid = Optional.of(tzid);
        return this;
      }

      Location build() {
        return new Location(
            Objects.isNull(this.admin1) ? Optional.empty() : this.admin1,
            Objects.isNull(this.asciiname) ? Optional.empty() : this.asciiname,
            Objects.requireNonNull(this.city),
            Objects.requireNonNull(this.country),
            Objects.requireNonNull(this.geo),
            Objects.requireNonNull(this.geonameid),
            Objects.requireNonNull(this.latitude),
            Objects.requireNonNull(this.longitude),
            Objects.isNull(this.cc) ? Optional.empty() : this.cc,
            Objects.requireNonNull(this.title),
            Objects.isNull(this.tzid) ? Optional.empty() : this.tzid);
      }
    }
  }

  /** Value class used to represent an item object of the API response. */
  @JsonDeserialize(builder = Item.Builder.class)
  public static final class Item {
    public final Optional<String> hebrew;
    public final String date;
    public final String title;
    public final Category category;
    public final Optional<String> hdate;
    public final Optional<String> titleOrig;
    public final Optional<String> link;
    public final Optional<String> memo;
    public final Optional<String> subcat;
    public final Boolean yomtov;

    /** Enum used to categorize the various available items in the API response. */
    public enum Category {
      CANDLES("candles"),
      HAVDALAH("havdalah"),
      HOLIDAY("holiday"),
      PARASHAT("parashat"),
      ROSH_CHODESH("roshchodesh");

      private final String value;

      Category(final String value) {
        this.value = value;
      }

      @Override
      public String toString() {
        return this.value;
      }

      public static Category parse(final String category) {
        switch (category) {
          case "candles":
            return CANDLES;
          case "havdalah":
            return HAVDALAH;
          case "holiday":
            return HOLIDAY;
          case "parashat":
            return PARASHAT;
          case "roshchodesh":
            return ROSH_CHODESH;
          default:
            throw new IllegalArgumentException("unknown category");
        }
      }
    }

    private Item(
        final Optional<String> hebrew,
        final String date,
        final String title,
        final Category category,
        final Optional<String> hdate,
        final Optional<String> titleOrig,
        final Optional<String> link,
        final Optional<String> memo,
        final Optional<String> subcat,
        final Boolean yomtov) {
      this.hebrew = hebrew;
      this.date = date;
      this.title = title;
      this.category = category;
      this.hdate = hdate;
      this.titleOrig = titleOrig;
      this.link = link;
      this.memo = memo;
      this.subcat = subcat;
      this.yomtov = yomtov;
    }

    @JsonIgnore
    @Override
    public boolean equals(final Object o) {
      if (o == this) {
        return true;
      }
      if (!(o instanceof Item)) {
        return false;
      }
      var item = (Item) o;
      return Objects.equals(this.hebrew, item.hebrew)
          && Objects.equals(this.date, item.date)
          && Objects.equals(this.title, item.title)
          && Objects.equals(this.category, item.category)
          && Objects.equals(this.hdate, item.hdate)
          && Objects.equals(this.titleOrig, item.titleOrig)
          && Objects.equals(this.link, item.link)
          && Objects.equals(this.memo, item.memo)
          && Objects.equals(this.subcat, item.subcat)
          && Objects.equals(this.yomtov, item.yomtov);
    }

    @JsonIgnore
    @Override
    public int hashCode() {
      return Objects.hash(
          this.hebrew,
          this.date,
          this.title,
          this.category,
          this.hdate,
          this.titleOrig,
          this.link,
          this.memo,
          this.subcat,
          this.yomtov);
    }

    @JsonPOJOBuilder
    static class Builder {
      private Optional<String> hebrew;
      private String date;
      private String title;
      private Category category;
      private Optional<String> hdate;
      private Optional<String> titleOrig;
      private Optional<String> link;
      private Optional<String> memo;
      private Optional<String> subcat;
      private Boolean yomtov;

      Builder withHebrew(final String hebrew) {
        this.hebrew = Optional.of(hebrew);
        return this;
      }

      Builder withDate(final String date) {
        this.date = date;
        return this;
      }

      Builder withTitle(final String title) {
        this.title = title;
        return this;
      }

      Builder withCategory(final String category) {
        this.category = Category.parse(category);
        return this;
      }

      Builder withHdate(final String hdate) {
        this.hdate = Optional.of(hdate);
        return this;
      }

      @JsonProperty("title_orig")
      Builder withTitleOrig(final String titleOrig) {
        this.titleOrig = Optional.of(titleOrig);
        return this;
      }

      Builder withLink(final String link) {
        this.link = Optional.of(link);
        return this;
      }

      Builder withMemo(final String memo) {
        this.memo = Optional.of(memo);
        return this;
      }

      Builder withSubcat(final String subcat) {
        this.subcat = Optional.of(subcat);
        return this;
      }

      Builder withYomtov(final Boolean yomtov) {
        this.yomtov = yomtov;
        return this;
      }

      Item build() {
        return new Item(
            Objects.isNull(this.hebrew) ? Optional.empty() : this.hebrew,
            Objects.requireNonNull(this.date),
            Objects.requireNonNull(this.title),
            Objects.requireNonNull(this.category),
            Objects.isNull(this.hdate) ? Optional.empty() : this.hdate,
            Objects.isNull(this.titleOrig) ? Optional.empty() : this.titleOrig,
            Objects.isNull(this.link) ? Optional.empty() : this.link,
            Objects.isNull(this.memo) ? Optional.empty() : this.memo,
            Objects.isNull(this.subcat) ? Optional.empty() : this.subcat,
            this.yomtov);
      }
    }
  }
}

package info.tomfi.shabbat;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/** Value class used to represent an API request. */
public final class APIRequest {
  private static final IntWrapper DEFAULT_HAVDALAH = IntWrapper.of(50);
  private static final IntWrapper DEFAULT_CANDLE_LIGHTING = IntWrapper.of(18);

  private final Map<ParamKey, ParamValue> queryParams;

  /** Enum for setting the various available query parameter keys */
  public enum ParamKey {
    ASHKENAZIS_TRANSLITERATIONS("a"),
    CANDLE_LIGHTING("b"),
    CITY("city"), // used with GeoType.CITY
    GEO_ID("geonameid"), // used with GeoType.GEO_NAME
    GEO_TYPE("geo"),
    GREGORIAN_DAY("gd"),
    GREGORIAN_MONTH("gm"),
    GREGORIAN_YEAR("gy"),
    HAVDALAH("m"),
    INCLUDE_TURAH_HAFTARAH("leyning"),
    LATITUDE("latitude"), // used with GeoType.POSITIONAL
    LONGITUDE("longitude"), // used with GeoType.POSITIONAL
    OUTPUT_FORMAT("cfg"),
    TZID("tzid"), // used with GeoType.POSITIONAL
    ZIP("zip"); // used with GeoType.ZIP

    private final String value;

    ParamKey(final String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return this.value;
    }
  }

  /** Interface for contracting the various available query parameter values */
  public interface ParamValue {
    String toString();
  }

  /** Enum used to encapsulate values for the {@link ParamKey#OUTPUT_FORMAT} query parameter. */
  public enum OutputType implements ParamValue {
    JSON("json"),
    RSS("r");

    private final String value;

    OutputType(final String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return this.value;
    }
  }

  /** Enum used to encapsulate values for {@link ParamKey} query parameters. */
  public enum FlagState implements ParamValue {
    OFF("off"),
    ON("on");

    private final String value;

    FlagState(final String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return this.value;
    }
  }

  /** Enum used to encapsulate the values of the {@link ParamKey#GEO_TYPE} query parameter. */
  public enum GeoType implements ParamValue {
    CITY("city"),
    GEO_NAME("geoname"),
    POSITIONAL("pos"),
    ZIP("zip");

    private final String value;

    GeoType(final String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return this.value;
    }
  }

  /** Wrapper for int values used for {@link ParamKey} query parameters. */
  public static final class IntWrapper implements ParamValue {
    private final String originalInt;

    public static IntWrapper of(final int originalInt) {
      return new IntWrapper(originalInt);
    }

    private IntWrapper(final int originalInt) {
      this.originalInt = String.valueOf(originalInt);
    }

    @Override
    public String toString() {
      return this.originalInt;
    }

    @Override
    public boolean equals(final Object o) {
      if (o == this) {
        return true;
      }
      if (!(o instanceof IntWrapper)) {
        return false;
      }
      var wrapper = (IntWrapper) o;
      return Objects.equals(this.originalInt, wrapper.originalInt);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(this.originalInt);
    }
  }

  /**
   * Wrapper for int values used for {@link ParamKey} two-chars padded query parameters, i.e. '02'
   * instead of '2'.
   */
  public static final class PaddedIntWrapper implements ParamValue {
    private final String paddedInt;

    public static PaddedIntWrapper of(final int originalInt) {
      return new PaddedIntWrapper(originalInt);
    }

    private PaddedIntWrapper(final int originalInt) {
      var asStr = String.format("0%s", String.valueOf(originalInt));
      this.paddedInt = asStr.substring(asStr.length() - 2);
    }

    @Override
    public String toString() {
      return this.paddedInt;
    }

    @Override
    public boolean equals(final Object o) {
      if (o == this) {
        return true;
      }
      if (!(o instanceof PaddedIntWrapper)) {
        return false;
      }
      var wrapper = (PaddedIntWrapper) o;
      return Objects.equals(this.paddedInt, wrapper.paddedInt);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(this.paddedInt);
    }
  }

  /** Wrapper for string values used for {@link ParamKey} query parameters. */
  public static final class StringWrapper implements ParamValue {
    private final String originalString;

    public static StringWrapper of(final String originalString) {
      return new StringWrapper(originalString);
    }

    private StringWrapper(final String originalString) {
      this.originalString = String.valueOf(originalString);
    }

    @Override
    public String toString() {
      return this.originalString;
    }

    @Override
    public boolean equals(final Object o) {
      if (o == this) {
        return true;
      }
      if (!(o instanceof StringWrapper)) {
        return false;
      }
      var wrapper = (StringWrapper) o;
      return Objects.equals(this.originalString, wrapper.originalString);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(this.originalString);
    }
  }

  private APIRequest(final Map<ParamKey, ParamValue> queryParams) {
    this.queryParams = queryParams;
  }

  @Override
  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof APIRequest)) {
      return false;
    }
    var apiRequest = (APIRequest) o;
    return Objects.equals(queryParams, apiRequest.queryParams);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(queryParams);
  }

  public Map<ParamKey, ParamValue> queryParams() {
    return this.queryParams;
  }

  public static Builder builder() {
    return new Builder();
  }

  /** Builder class for {@link APIRequest}. */
  public static final class Builder {
    private Map<ParamKey, ParamValue> queryParams;

    /** The builder's constructor sets the default query parameters needed for the request. */
    public Builder() {
      queryParams = new EnumMap<>(ParamKey.class);
      queryParams.put(ParamKey.OUTPUT_FORMAT, OutputType.JSON);
      queryParams.put(ParamKey.INCLUDE_TURAH_HAFTARAH, FlagState.OFF);
      queryParams.put(ParamKey.ASHKENAZIS_TRANSLITERATIONS, FlagState.OFF);
      queryParams.put(ParamKey.GEO_TYPE, GeoType.GEO_NAME);
      queryParams.put(ParamKey.HAVDALAH, DEFAULT_HAVDALAH);
      queryParams.put(ParamKey.CANDLE_LIGHTING, DEFAULT_CANDLE_LIGHTING);
    }

    public APIRequest build() {
      if (queryParams.containsKey(ParamKey.GEO_ID)) {
        return new APIRequest(queryParams);
      }
      throw new IllegalArgumentException("geo id is mandatory for sending a request");
    }

    /**
     * Set the havdalah minutes (after sundown) to be calculated. If not set, the default 50 will be
     * used.
     *
     * @param minutes the minutes to calculate.
     * @return the fluent builder instance.
     */
    public Builder withMinutesAfterSundown(final int minutes) {
      if (minutes <= 0) {
        throw new IllegalArgumentException("minutes after sundown should be a positive integer");
      }
      queryParams.put(ParamKey.HAVDALAH, IntWrapper.of(minutes));
      return this;
    }

    /**
     * Set the candle light minutes (before sunset) to be calculated. If not set, the default 18
     * will be used.
     *
     * @param minutes the minutes to calculate.
     * @return the fluent builder instance.
     */
    public Builder withMinutesBeforeSunset(final int minutes) {
      if (minutes < 0) {
        throw new IllegalArgumentException(
            "minutes before sunset should be a non negative integer");
      }
      queryParams.put(ParamKey.CANDLE_LIGHTING, IntWrapper.of(minutes));
      return this;
    }

    /**
     * Set the geoid for the request, any geoid supported should work. The various geoids can be
     * found in https://www.geonames.org/.
     *
     * @param geoId the geoid to incorporate into the request.
     * @return the fluent builder instance.
     */
    public Builder forGeoId(final int geoId) {
      if (geoId <= 0) {
        throw new IllegalArgumentException("geo id should be a positive integer");
      }
      queryParams.put(ParamKey.GEO_ID, IntWrapper.of(geoId));
      return this;
    }

    /**
     * Set a specific date for the request.
     *
     * @param localDate the date to incorporate into the request.
     * @return the fluent builder instance.
     */
    public Builder withDate(final LocalDate localDate) {
      Objects.requireNonNull(localDate, "Null localDate");
      var year = IntWrapper.of(localDate.getYear());
      var month = PaddedIntWrapper.of(localDate.getMonthValue());
      var day = PaddedIntWrapper.of(localDate.getDayOfMonth());

      queryParams.put(ParamKey.GREGORIAN_YEAR, year);
      queryParams.put(ParamKey.GREGORIAN_MONTH, month);
      queryParams.put(ParamKey.GREGORIAN_DAY, day);
      return this;
    }

    public ParamBuilder addParam(final ParamKey paramKey) {
      return new ParamBuilder(this, paramKey);
    }

    /**
     * Sub-builder class for adding {@link APIRequest#queryParams} key-value pairs. Based on
     * HebCal's Shabbat Times API docs https://www.hebcal.com/home/197/shabbat-times-rest-api.
     */
    public static final class ParamBuilder {
      private Builder builder;
      private ParamKey paramKey;

      ParamBuilder(final Builder builder, final ParamKey paramKey) {
        this.builder = builder;
        this.paramKey = paramKey;
      }

      public Builder withValue(final ParamValue paramValue) {
        builder.queryParams.put(paramKey, paramValue);
        return builder;
      }

      public Builder withValue(final String paramStrValue) {
        builder.queryParams.put(paramKey, StringWrapper.of(paramStrValue));
        return builder;
      }

      public Builder withValue(final int paramIntValue) {
        builder.queryParams.put(paramKey, IntWrapper.of(paramIntValue));
        return builder;
      }

      public Builder withPaddedValue(final int paramIntValue) {
        builder.queryParams.put(paramKey, PaddedIntWrapper.of(paramIntValue));
        return builder;
      }
    }
  }
}

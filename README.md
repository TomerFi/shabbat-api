<!-- markdownlint-disable MD013 -->
# A Java API encapsulating HebCal's REST API</br>[![maven-central-version]][7] [![javadoc-io-badge]][9]</br>[![gh-build-status]][0] [![codecov-coverage]][1] [![conventional-commits]][2]
<!-- markdownlint-enable MD013 -->

The artifact offers a Java service and provider encapsulating [HebCal][3]'s REST API.</br>
For the time being, only the [Shabbat times][4] was implemented using the publicized
[REST API](https://www.hebcal.com/home/197/shabbat-times-rest-api).

- Requires Java >= 11.
- Use the [Wiki section][8] for more information.

```xml
<!-- declare the dependency -->
<dependency>
  <groupId>info.tomfi.hebcal</groupId>
  <artifactId>hebcal-api</artifactId>
  <version>VERSION</version>
</dependency>
```

```java
// get the provider from the service loader
var shabbatApi = ServiceLoader.load(ShabbatAPI.class).findFirst().get();
// build a request (omit the withDate step to fetch the closest next shabbat)
var request = Request.builder().forGeoId(281184).withDate(LocalDate.of(2021, 1, 1)).build();
// get the future for the reponse
var completableFuture = shabbatApi.sendAsync(request);
// do what ever you need to do
// ...
// or just wait for the resoponse
var response = completableFuture.get();

// 281184 is the geoid for jerusalem israel
assertThat(response.location()).titleIs("Jerusalem, Israel");

// get the various items collection
var itemsList = response.items().get();

// find the candles item
var candlesItem = itemsList.stream()
    .filter(item -> item.category().equals(CANDLES.category()))
    .findFirst()
    .get();
// the shabbat started on the 2021-01-01 at 16:05
assertThat(candlesItem).dateIs("2021-01-01T16:05:00+02:00");

// find the havdalah item
var havdalahItem = itemsList.stream()
    .filter(item -> item.category().equals(HAVDALAH.category()))
    .findFirst()
    .get();
// the shabbat ended on the 2021-01-02 at 17:36
assertThat(havdalahItem).dateIs("2021-01-02T17:36:00+02:00");
```

## Links

- [HebCal site][3].
- [HebCal API docs][5].
- [GeoNames][10].

## Disclaimer

This repository has no relations with [HebCal][3].</br>
[HebCal][3] were nice enough to allow public **free access** to their [API via REST services][5].</br>
The artifact constructed with this repository merely wraps the publicly open
REST API with a Java API.

<!-- Real Links -->
[0]: https://github.com/TomerFi/hebcal-api/actions?query=workflow%3APre-release
[1]: https://codecov.io/gh/TomerFi/hebcal-api
[2]: https://conventionalcommits.org
[3]: https://www.hebcal.com/
[4]: https://www.hebcal.com/shabbat
[5]: https://www.hebcal.com/home/197/shabbat-times-rest-api
[6]: https://www.geonames.org/
[7]: https://search.maven.org/artifact/info.tomfi.hebcal/hebcal-api
[8]: https://github.com/TomerFi/hebcal-api/wiki
[9]: https://javadoc.io/doc/info.tomfi.hebcal/hebcal-api
[10]: https://www.geonames.org/
<!-- Badges Links -->
[codecov-coverage]: https://codecov.io/gh/TomerFi/hebcal-api/branch/master/graph/badge.svg
[conventional-commits]: https://img.shields.io/badge/Conventional%20Commits-1.0.0-yellow.svg
[gh-build-status]: https://github.com/TomerFi/hebcal-api/workflows/Release/badge.svg
[maven-central-version]: https://badgen.net/maven/v/maven-central/info.tomfi.hebcal/hebcal-api?icon=maven&label=Maven%20Central
[javadoc-io-badge]: https://javadoc.io/badge2/info.tomfi.hebcal/hebcal-api/Javadoc.svg

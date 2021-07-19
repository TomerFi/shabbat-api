<!-- markdownlint-disable MD013 -->
# A Java API encapsulating HebCal's REST API</br>[![maven-central-version]][7] [![java-min-version]][11] [![javadoc-io-badge]][9]</br>[![gh-build-status]][0] [![codecov-coverage]][1]
<!-- markdownlint-enable MD013 -->

The library offers a Java service and provider encapsulating [HebCal][3]'s REST API.

Head on over to the [Wiki section][8] for more information.</br>
Take a look at the [Javadoc][9] for API documentation.</br>

A brief usage example:

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
    .filter(item -> item.category().equals(CANDLES.toString()))
    .findFirst()
    .get();
// the shabbat started on the 2021-01-01 at 16:05
assertThat(candlesItem).dateIs("2021-01-01T16:05:00+02:00");

// find the havdalah item
var havdalahItem = itemsList.stream()
    .filter(item -> item.category().equals(HAVDALAH.toString()))
    .findFirst()
    .get();
// the shabbat ended on the 2021-01-02 at 17:36
assertThat(havdalahItem).dateIs("2021-01-02T17:36:00+02:00");
```

## Links

- [HebCal site][3]
- [HebCal online tool][4]
- [HebCal API docs][5]
- [GeoNames][10]

## Disclaimer

This repository has no direct relations with [HebCal][3].</br>
[HebCal][3] were nice enough to allow public **free access** to their
[API via REST services][5].</br>
The artifact constructed with this repository merely wraps the publicly open REST API with a Java
API.

<!-- Real Links -->
[0]: https://github.com/TomerFi/hebcal-api/actions?query=workflow%3APre-release
[1]: https://codecov.io/gh/TomerFi/hebcal-api
[3]: https://www.hebcal.com/
[4]: https://www.hebcal.com/shabbat
[5]: https://www.hebcal.com/home/197/shabbat-times-rest-api
[6]: https://www.geonames.org/
[7]: https://search.maven.org/artifact/info.tomfi.hebcal/hebcal-api
[8]: https://github.com/TomerFi/hebcal-api/wiki
[9]: https://javadoc.io/doc/info.tomfi.hebcal/hebcal-api
[10]: https://www.geonames.org/
[11]: https://openjdk.java.net/projects/jdk/11/
<!-- Badges Links -->
[codecov-coverage]: https://codecov.io/gh/TomerFi/hebcal-api/branch/master/graph/badge.svg
[gh-build-status]: https://github.com/TomerFi/hebcal-api/workflows/Release/badge.svg
[maven-central-version]: https://badgen.net/maven/v/maven-central/info.tomfi.hebcal/hebcal-api?icon=maven&label=Maven%20Central
[javadoc-io-badge]: https://javadoc.io/badge2/info.tomfi.hebcal/hebcal-api/Javadoc.io.svg
[java-min-version]: https://badgen.net/badge/Java%20Version/11/5382a1

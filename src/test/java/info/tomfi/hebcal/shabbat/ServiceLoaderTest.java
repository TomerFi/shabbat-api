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
package info.tomfi.hebcal.shabbat;

import static org.assertj.core.api.Assertions.assertThat;

import info.tomfi.hebcal.shabbat.impl.ShabbatAPIProvider;
import java.lang.reflect.InvocationTargetException;
import java.util.ServiceLoader;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/** Test cases for consuming the service provider with the service loader api. */
@Tag("unit-tests")
final class ServiceLoaderTest {
  @Test
  void load_service_with_default_empty_constructor()
      throws NoSuchFieldException, SecurityException, IllegalArgumentException,
          IllegalAccessException {
    var impl = ServiceLoader.load(ShabbatAPI.class).findFirst().get();
    assertThat(impl).isInstanceOf(ShabbatAPIProvider.class);

    var endpointField = impl.getClass().getDeclaredField("endpoint");
    endpointField.setAccessible(true);
    assertThat((String) endpointField.get(impl)).isEqualTo("https://www.hebcal.com/shabbat/");
  }

  @Test
  void load_service_via_reflection_with_custom_constructor()
      throws InstantiationException, IllegalAccessException, IllegalArgumentException,
          InvocationTargetException, NoSuchMethodException, SecurityException,
          NoSuchFieldException {
    var type = ServiceLoader.load(ShabbatAPI.class).stream().findFirst().get().type();
    var impl = type.getDeclaredConstructor(String.class).newInstance("fake.url.com:1234/blabla");
    assertThat(impl).isInstanceOf(ShabbatAPIProvider.class);

    var endpointField = type.getDeclaredField("endpoint");
    endpointField.setAccessible(true);
    assertThat((String) endpointField.get(impl)).isEqualTo("fake.url.com:1234/blabla");
  }
}

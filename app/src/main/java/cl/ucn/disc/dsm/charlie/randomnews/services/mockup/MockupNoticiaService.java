/*
 * Copyright (c) 2020 Charlie Condorcet
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cl.ucn.disc.dsm.charlie.randomnews.services.mockup;

import cl.ucn.disc.dsm.charlie.randomnews.model.Noticia;
import cl.ucn.disc.dsm.charlie.randomnews.services.NoticiaService;
import java.util.ArrayList;
import java.util.List;
import org.threeten.bp.ZonedDateTime;

/**
 * Interim class to do tests by implementing {@link NoticiaService}.
 *
 * @author Charlie Condorcet.
 */
public class MockupNoticiaService implements NoticiaService {

  /**
   * Default Constructor.
   */
  public MockupNoticiaService() {

  }

  /**
   * Get the Noticias from the backend.
   *
   * @param pageSize how many.
   * @return the {@link List} of {@link Noticia}.
   */
  @Override
  public List<Noticia> getNoticias(int pageSize) {

    final List<Noticia> noticias = new ArrayList<>();

    noticias.add(new Noticia(
        1L,
        "Primer Titulo",
        "Primera Fuente",
        "Primer Autor",
        "http://primero.cl",
        "http://primero.cl/primero.jpg",
        "Primer Resumen",
        "Primer Contenido",
        ZonedDateTime.now())
    );

    noticias.add(new Noticia(
        2L,
        "Segundo Titulo",
        "Segunda Fuente",
        "Segundo Autor",
        "http://segundo.cl",
        "http://segundo.cl/segundo.jpg",
        "Segundo Resumen",
        "Segundo Contenido",
        ZonedDateTime.now())
    );

    return noticias;
  }

}

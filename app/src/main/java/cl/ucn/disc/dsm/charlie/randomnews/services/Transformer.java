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

package cl.ucn.disc.dsm.charlie.randomnews.services;

import cl.ucn.disc.dsm.charlie.randomnews.model.Noticia;
import cl.ucn.disc.dsm.charlie.randomnews.services.newsapi.Article;
import cl.ucn.disc.dsm.charlie.randomnews.services.newsapi.NewsApiNoticiaService;
import cl.ucn.disc.dsm.charlie.randomnews.services.newsapi.Source;
import java.net.URI;
import java.net.URISyntaxException;
import net.openhft.hashing.LongHashFunction;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Transformer class.
 *
 * @author Charlie Condorcet.
 */
public class Transformer {


  /**
   * The logger.
   */
  private static final Logger log = LoggerFactory.getLogger(Transformer.class);

  /**
   * Article to Noticia.
   *
   * @param Article to transform.
   * @return the Noticia.
   */
  public static Noticia transform(final Article article) {

    // Nullity.
    if (article == null) {
      throw new NewsApiNoticiaService.NewsAPIException("Article was null");
    }

    // The host.
    final String host = getHost(article.url);

    // If the Article is null ...
    if (article.title == null) {

      log.warn("Article without title: {}", toString(article));

      // ... and the content is null, throw exception !.
      if (article.description == null) {
        throw new NewsApiNoticiaService.NewsAPIException("Article without title and description");
      }

      // FIXME: Cambiar el titulo por alguna informacion disponible
      article.title = "No Title*";
    }

    // FIXED: In case there is no source.
    if (article.source == null) {
      article.source = new Source();

      if (host != null) {
        article.source.name = host;
      } else {
        article.source.name = "No Source*";
        log.warn("Article without source: {}", toString(article));
      }
    }

    // FIXED: If the article has no author.
    if (article.author == null) {

      if (host != null) {
        article.author = host;
      } else {
        article.author = "No Author*";
        log.warn("Article without author: {}", toString(article));
      }
    }

    // The date.
    final ZonedDateTime publishedAt = parseZonedDateTime(article.publishedAt)
        .withZoneSameInstant(Noticia.ZONE_ID);

    // The unique id (computed from hash).
    final Long theId = LongHashFunction.xx()
        .hashChars(article.title + article.source.name);

    // The Noticia.
    return new Noticia(
        theId,
        article.title,
        article.source.name,
        article.author,
        article.url,
        article.urlToImage,
        article.description,
        article.content,
        publishedAt
    );

  }


  /**
   * Parse the date of {@link String} to {@link ZonedDateTime}.
   *
   * @param fecha to parse.
   * @return the fecha.
   * @throws cl.ucn.disc.dsm.thenews.services.newsapi.NewsApiNoticiaService.NewsAPIException
   * In case you can't convert the date.
   */
  private static ZonedDateTime parseZonedDateTime(final String fecha) {

    // It is imperative that the date exists.
    if (fecha == null) {
      throw new NewsApiNoticiaService.NewsAPIException("Can't parse null fecha");
    }

    try {
      // Try to parse the date.
      return ZonedDateTime.parse(fecha);
    } catch (DateTimeParseException ex) {

      // Debug message.
      log.error("Can't parse date: ->{}<-. Error: ", fecha, ex);

      // Nest the DateTimeParseException to NoticiaTransformerException.
      throw new NewsApiNoticiaService.NewsAPIException("Can't parse date: " + fecha, ex);
    }
  }

  /**
   * Get the host part of one url.
   *
   * @param url to use.
   * @return the host part (without the www)
   */
  private static String getHost(final String url) {

    try {

      final URI uri = new URI(url);
      final String hostname = uri.getHost();

      // to provide faultproof result, check if not null then return only hostname, without www.
      if (hostname != null) {
        return hostname.startsWith("www.") ? hostname.substring(4) : hostname;
      }

      return null;

    } catch (final URISyntaxException | NullPointerException ex) {
      return null;
    }
  }

  /**
   * Transform a t object in a String showing his attributes.
   *
   * @param t   to convert.
   * @param <T> type of t.
   * @return the object in string format.
   */
  public static <T> String toString(final T t) {
    return ReflectionToStringBuilder.toString(t, ToStringStyle.MULTI_LINE_STYLE);
  }

}

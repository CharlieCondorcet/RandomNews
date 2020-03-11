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

package cl.ucn.disc.dsm.charlie.randomnewsapp.services.newsapi;

import cl.ucn.disc.dsm.charlie.randomnews.services.newsapi.NewsApi;
import cl.ucn.disc.dsm.charlie.randomnews.services.newsapi.NewsApiResult;
import cl.ucn.disc.dsm.charlie.randomnews.model.Noticia;
import cl.ucn.disc.dsm.charlie.randomnews.services.NoticiaService;
import cl.ucn.disc.dsm.charlie.randomnews.services.Transformer;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.TimeUnit;
import net.openhft.hashing.LongHashFunction;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.threeten.bp.ZonedDateTime;
import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Get a CALL and process the result implementing {@link NoticiaService}.
 *
 * @author Charlie Condorcet.
 */
public final class NewsApiNoticiaService implements NoticiaService {

  /**
   * The NewsAPI.
   */
  private final NewsApi newsApi;

  /**
   * The Constructor.
   */
  public NewsApiNoticiaService() {
    // https://futurestud.io/tutorials/retrofit-getting-started-and-android-client
    this.newsApi = new Retrofit.Builder()
        // The main URL
        .baseUrl(NewsApi.BASE_URL)
        // JSON to POJO
        .addConverterFactory(GsonConverterFactory.create())
        // Validate the interface
        .validateEagerly(true)
        // Build the Retrofit ..
        .build()
        // .. get the NewsApi.
        .create(NewsApi.class);
  }


  /**
   * Get the Noticias from the Call.
   *
   * @param theCall to use.
   * @return the {@link List} of {@link Noticia}.
   */
  private static List<Noticia> getNoticiasFromCall(final Call<NewsApiResult> theCall) {

    try {

      // Get the result from the call.
      final Response<NewsApiResult> response = theCall.execute();

      // UnSuccessful!.
      if (!response.isSuccessful()) {

        // Error!.
        throw new NewsAPIException(
            "Can't get the NewsResult, code: " + response.code(),
            new HttpException(response)
        );

      }

      final NewsApiResult theResult = response.body();

      // No body.
      if (theResult == null) {
        throw new NewsAPIException("NewsResult was null");
      }

      // No articles.
      if (theResult.articles == null) {
        throw new NewsAPIException("Articles in NewsResult was null");
      }


      // Article to Noticia (transformer) via Stream.
      return theResult.articles.stream()
          .map(Transformer::transform)
          .collect(Collectors.toList());

    } catch (final IOException ex) {
      throw new NewsAPIException("Can't get the NewsResult", ex);
    }

  }


  /**
   * Get the Noticias from the Call.
   *
   * @param pageSize how many.
   * @return the {@link List} of {@link Noticia}.
   */
  @Override
  public List<Noticia> getNoticias(int pageSize) {

    // the Call
    final Call<NewsApiResult> theCall = this.newsApi.getEverything(pageSize);

    // Process the Call.
    return getNoticiasFromCall(theCall);
  }


  /**
   * The Exception.
   */
  public static final class NewsAPIException extends RuntimeException {

    public NewsAPIException(final String message) {
      super(message);
    }

    public NewsAPIException(final String message, final Throwable cause) {
      super(message, cause);
    }
  }


}

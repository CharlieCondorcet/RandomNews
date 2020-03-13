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

package cl.ucn.disc.dsm.charlie.randomnews.activities.adapters;

import androidx.recyclerview.widget.RecyclerView;
//import cl.ucn.disc.dsm.charlie.randomnews.R;
import cl.ucn.disc.dsm.charlie.randomnews.R;
import cl.ucn.disc.dsm.charlie.randomnews.model.Noticia;
import cl.ucn.disc.dsm.charlie.randomnews.databinding.RowNoticiaBinding;
import java.util.Date;
import org.ocpsoft.prettytime.PrettyTime;
import org.threeten.bp.DateTimeUtils;


/**
 * ViewHolder Pattern.
 *
 * @author Charlie Condorcet.
 */
public class NoticiaViewHolder extends RecyclerView.ViewHolder {

  /**
   * The Bindings.
   */
  private final RowNoticiaBinding binding;

  /**
   * The Date formatter.
   */
  private static final PrettyTime prettyTyme = new PrettyTime();


  /**
   * The Constructor.
   *
   * @param rowNoticiaBinding to use.
   */
  public NoticiaViewHolder(RowNoticiaBinding rowNoticiaBinding) {
    super(rowNoticiaBinding.getRoot());
    this.binding = rowNoticiaBinding;
  }

  /**
   * Bind the Noticia to the ViewHolder.
   *
   * @param noticia to bind.
   */
  public void bind(final Noticia noticia) {

    this.binding.tvTitulo.setText(noticia.getTitulo());
    this.binding.tvResumen.setText(noticia.getResumen());
    this.binding.tvAutor.setText(noticia.getAutor());
    this.binding.tvFuente.setText(noticia.getFuente());

    // FIXED: The format of the date. ZonedDateTime to Date.
    final Date date = DateTimeUtils.toDate(noticia.getFecha().toInstant());
    this.binding.tvFecha.setText(this.prettyTyme.format(date));

    // If exist the url ...
    if (noticia.getUrlFoto() != null) {
      // ... set the uri.
      this.binding.sdvFoto.setImageURI(noticia.getUrlFoto());
    } else {
      // .. set a default image.
      this.binding.sdvFoto.setImageResource(R.drawable.newsicon);
    }
  }

}

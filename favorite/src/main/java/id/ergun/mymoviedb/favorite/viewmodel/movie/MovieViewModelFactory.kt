package id.ergun.mymoviedb.favorite.viewmodel.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.ergun.mymoviedb.ui.datasource.movie.MovieDataSourceFactory
import id.ergun.mymoviedb.ui.datasource.tvshow.TvShowDataSourceFactory
import javax.inject.Inject

/**
 * Created by alfacart on 17/01/21.
 */
class MovieViewModelFactory @Inject constructor(
    private val movieDataSourceFactory: MovieDataSourceFactory,
    private val tvShowDataSourceFactory: TvShowDataSourceFactory
) :
    ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T =
      when {
        modelClass.isAssignableFrom(MovieViewModel::class.java) -> {
          MovieViewModel(movieDataSourceFactory, tvShowDataSourceFactory) as T
        }
        else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
      }
}
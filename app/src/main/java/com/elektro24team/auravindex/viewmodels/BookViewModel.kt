package com.elektro24team.auravindex.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elektro24team.auravindex.model.Book
import com.elektro24team.auravindex.retrofit.BookClient
import com.elektro24team.auravindex.utils.normalize
import kotlinx.coroutines.launch

class BookViewModel: ViewModel(){
    var posts = mutableStateOf<List<Book>>(emptyList())
        private set
    var latestReleases = mutableStateOf<List<Book>>(emptyList())
        private set
    private val _book = MutableLiveData<Book?>()
    val book: LiveData<Book?> = _book
    var filteredBooks = mutableStateOf<List<Book>>(emptyList())
        private set
    init {
        fetchBooks(showDuplicates = false, showLents = false, page = 1, limit = 20)
        fetchLatestReleases()
        fetchFilteredBooks(filter = "", value = "")
    }


    private fun fetchBooks(showDuplicates: Boolean = true, showLents: Boolean = true, page: Int = 1, limit: Int = 100){
        viewModelScope.launch {
            try {
                val response = BookClient.apiService.getBooks(showDuplicates = showDuplicates, showLents = showLents, page, limit)
                posts.value = response.data
                Log.d("DEBUG", "Se obtuvieron ${response.data.size} libros")

            }catch (e: Exception){
                e.printStackTrace()
            }

        }
    }
    private fun fetchLatestReleases(limit: Int = 10){
        viewModelScope.launch {
            try {
                val response = BookClient.apiService.getLatestReleases(limit)
                latestReleases.value = response.data
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
    //duplicados desactivados temporalmente para la search screen
    fun fetchFilteredBooks(showDuplicates: Boolean = false, showLents: Boolean = true, filter: String, value: String){
        viewModelScope.launch {
            try {
                val response = BookClient.apiService.getFilteredBooks(showDuplicates,showLents,filter,value)
                filteredBooks.value = response.data.distinctBy { it._id }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    fun fetchBookById(id: String) {
        viewModelScope.launch {
            try {
                val result = BookClient.apiService.getBookById(id)
                _book.postValue(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun applyLocalFilter(filter: String, search: String) {
        val result = getFirstFiveFilteredBooks(posts.value, search, filter)
        filteredBooks.value = result
        Log.d("DEBUG", "Filtrando con '$search' usando '$filter': ${result.size} resultados")

    }

    fun getFirstFiveFilteredBooks(books: List<Book>, search: String, filter: String = "título"): List<Book> {
        val cleanedQuery = search.trim()

        val filtered = books.filter { book ->
                when (filter.lowercase()) {
                    "título" , "title" -> book.title.normalize().contains(cleanedQuery, ignoreCase = true)
                    "autor" , "author" -> book.authors.any { author -> author.name.contains(cleanedQuery, ignoreCase = true) }
                    "género" , "genre"-> book.genres.any { genre -> genre.contains(cleanedQuery, ignoreCase = true) }
                    else -> false
                }
            }
        val distinct = filtered.distinctBy { it._id }
        return distinct.take(5)


    }

    fun getAllFilteredBooks(books: List<Book>, search: String, filter: String = "título"): List<Book> {
        val cleanedQuery = search.trim()

        val filtered = books.filter { book ->
            when (filter.lowercase()) {
                "título", "title" -> book.title.normalize().contains(cleanedQuery, ignoreCase = true)
                "autor", "author" -> book.authors.any { author -> author.name.contains(cleanedQuery, ignoreCase = true) }
                "género", "genre" -> book.genres.any { genre -> genre.contains(cleanedQuery, ignoreCase = true) }
                else -> false
            }
        }
        val distinct = filtered.distinctBy { it._id }
        return distinct.take(10)
    }

    fun filterAllBooksLocally(books: List<Book>, filter: String, search: String): List<Book> {
        return getAllFilteredBooks(books, search, filter)
    }

    fun filterBooksLocally(books: List<Book>, filter: String, search: String): List<Book> {
        return getFirstFiveFilteredBooks(books, search, filter)
    }

    fun getRecommendations(filter: String, value: String): List<Book> {
        val allBooks = posts.value

        return allBooks.filter {
            when (filter.lowercase()) {
                "título", "title"  -> !it.title.contains(value, ignoreCase = true)
                "autor", "author" -> !it.authors.any { a -> a.name.contains(value, ignoreCase = true) }
                "género", "genre" -> !it.genres.any { g -> g.contains(value, ignoreCase = true) }
                else -> false
            }
        }.take(10)
    }

}
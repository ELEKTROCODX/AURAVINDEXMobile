package com.elektro24team.auravindex.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elektro24team.auravindex.data.repository.BookRepository
import com.elektro24team.auravindex.model.Book
import com.elektro24team.auravindex.model.local.BookEntity
import com.elektro24team.auravindex.retrofit.BookClient
import com.elektro24team.auravindex.viewmodels.base.BaseViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import kotlin.collections.filter
import kotlin.text.contains

class BookViewModel(
    private val repository: BookRepository
) : BaseViewModel() {

    private val _books = MutableLiveData<List<Book>?>()
    private val _book = MutableLiveData<Book?>()
    private val _filteredBooks = MutableLiveData<List<Book>?>()
    private val _latestReleases = MutableLiveData<List<Book>?>()
    val books: MutableLiveData<List<Book>?> = _books
    val book: MutableLiveData<Book?> = _book
    val filteredBooks: MutableLiveData<List<Book>?> = _filteredBooks
    val latestReleases: MutableLiveData<List<Book>?> = _latestReleases

    suspend fun loadBooks(showDuplicates: Boolean, showLents: Boolean) {
        val result = repository.getAllBooks(showDuplicates, showLents)
        _books.postValue(result)
    }

    suspend fun loadBooksAndFilter(
        showDuplicates: Boolean,
        showLents: Boolean,
        filterField: String,
        filterValue: String
    ) {
        val result = repository.getAllBooks(showDuplicates, showLents)
        _books.postValue(result)
        filterBook(filterField, filterValue, showDuplicates, showLents)
    }

    fun filterBook(filterField: String, filterValue: String, showDuplicates: Boolean, showLents: Boolean) {
        var filteredValue = filterValue.trim()
        val filtered = _books.value?.filter { book ->
            when (filterField) {
                "title" -> book.title.contains(filteredValue, ignoreCase = true)
                "author" -> book.authors.any {
                    it._id.contains(filteredValue, ignoreCase = true) ||
                            it.name.contains(filteredValue, ignoreCase = true) ||
                            it.last_name.contains(filteredValue, ignoreCase = true) ||
                            it.birthdate.contains(filteredValue, ignoreCase = true) ||
                            it.gender.contains(filteredValue, ignoreCase = true)
                }
                "genre" -> book.genres.any { it.contains(filteredValue, ignoreCase = true) }
                "book_collection" -> book.book_collection._id.contains(filteredValue, ignoreCase = true) ||
                        book.book_collection.name.contains(filteredValue, ignoreCase = true)
                "book_status" -> book.book_status._id.contains(filteredValue, ignoreCase = true) ||
                        book.book_status.book_status.contains(filteredValue, ignoreCase = true)
                "editorial" -> book.editorial._id.contains(filteredValue, ignoreCase = true) ||
                        book.editorial.name.contains(filteredValue, ignoreCase = true) ||
                        book.editorial.email.contains(filteredValue, ignoreCase = true) ||
                        book.editorial.address.contains(filteredValue, ignoreCase = true)
                "language" -> book.language.contains(filteredValue, ignoreCase = true)
                "summary" -> book.summary.contains(filteredValue, ignoreCase = true)
                "classification" -> book.classification.contains(filteredValue, ignoreCase = true)
                "isbn" -> book.isbn.contains(filteredValue, ignoreCase = true)
                else -> false
            }
        }
        _filteredBooks.postValue(filtered ?: emptyList())
    }

     fun fetchLatestReleases(limit: String = "10"){
        viewModelScope.launch {
            try {
                val response = BookClient.apiService.getLatestReleases(limit)
                _latestReleases.value = response.data
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun searchBook(filterValue: String) {
        var filteredValue = filterValue.trim()
        val filtered = _books.value?.mapNotNull { book ->
            val nameMatch = book.title.contains(filteredValue, ignoreCase = true)
            val authorNameMatch = book.authors.any { (it.name + " " + it.last_name).contains(filteredValue, ignoreCase = true) }
            val genreMatch = book.genres.any { it.contains(filteredValue, ignoreCase = true) }
            val bookCollectionMatch = book.book_collection.name.contains(filteredValue, ignoreCase = true)
            val editorialMatch = book.editorial.name.contains(filteredValue, ignoreCase = true)
            val languageMatch = book.language.contains(filteredValue, ignoreCase = true)
            val summaryMatch = book.summary.contains(filteredValue, ignoreCase = true)
            val classificationMatch = book.classification.contains(filteredValue, ignoreCase = true)
            val isbnMatch = book.isbn.contains(filteredValue, ignoreCase = true)

            val score = when {
                nameMatch -> 9
                authorNameMatch -> 8
                genreMatch -> 7
                bookCollectionMatch -> 6
                editorialMatch -> 5
                languageMatch -> 4
                summaryMatch -> 3
                classificationMatch -> 2
                isbnMatch -> 1
                else -> 0
            }

            if (score > 0) book to score else null
        }?.sortedByDescending { it.second }?.map { it.first }
        _filteredBooks.postValue(filtered ?: emptyList())
    }
    fun loadBook(bookId: String, forceApiRequest: Boolean = false) {
        viewModelScope.launch {
            if( _books.value?.find{ it._id == bookId } == null || forceApiRequest) {
                val result = repository.getBookById(bookId)
                _books.postValue(listOf(result))
                _book.postValue(result)
            } else {
                _book.postValue(_books.value?.find{ it._id == bookId })
            }
        }
    }
    fun fetchBookWithAuth(token: String, bookId: String) {
        viewModelScope.launch {
            val result = repository.getBookByIdWithAuth(token, bookId)
            if (result.isSuccess) {
                _book.value = result.getOrNull()
            } else {
                val error = result.exceptionOrNull()
                if (error is HttpException) {
                    when (error.code()) {
                        401 -> notifyTokenExpired()
                        403 -> notifyInsufficentPermissions()
                        else -> notifyError("HTTP error: ${error.code()}")
                    }
                } else {
                    notifyError("Network error: ${error?.message}")
                }
            }
        }
    }
    override fun clearViewModelData() {
        _books.value = null
        _book.value = null
        _filteredBooks.value = null
        _latestReleases.value = null

    }
}
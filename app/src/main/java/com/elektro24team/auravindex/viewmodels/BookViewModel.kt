package com.elektro24team.auravindex.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elektro24team.auravindex.data.repository.BookRepository
import com.elektro24team.auravindex.model.Book
import com.elektro24team.auravindex.model.local.BookEntity
import com.elektro24team.auravindex.retrofit.BookClient
import com.elektro24team.auravindex.utils.normalize
import kotlinx.coroutines.launch
import kotlin.collections.filter
import kotlin.text.contains

class BookViewModel(
    private val repository: BookRepository
) : ViewModel() {

    private val _books = MutableLiveData<List<Book>>()
    private val _filteredBooks = MutableLiveData<List<Book>>()
    private val _latestReleases = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> = _books
    val filteredBooks: LiveData<List<Book>> = _filteredBooks
    val latestReleases: LiveData<List<Book>> = _latestReleases

    suspend fun loadBooks(showDuplicates: Boolean, showLents: Boolean) {
        val result = repository.getAllBooks(showDuplicates, showLents)
        _books.postValue(result)
    }

    fun filterBook(filterField: String, filterValue: String, showDuplicates: Boolean, showLents: Boolean) {
        val filtered = _books.value?.filter { book ->
            when (filterField) {
                "title" -> book.title.contains(filterValue, ignoreCase = true)
                "author" -> book.authors.any {
                    it._id.contains(filterValue, ignoreCase = true) ||
                            it.name.contains(filterValue, ignoreCase = true) ||
                            it.last_name.contains(filterValue, ignoreCase = true) ||
                            it.birthdate.contains(filterValue, ignoreCase = true) ||
                            it.gender.contains(filterValue, ignoreCase = true)
                }
                "genre" -> book.genres.any { it.contains(filterValue, ignoreCase = true) }
                "book_collection" -> book.book_collection._id.contains(filterValue, ignoreCase = true) ||
                        book.book_collection.name.contains(filterValue, ignoreCase = true)
                "book_status" -> book.book_status._id.contains(filterValue, ignoreCase = true) ||
                        book.book_status.book_status.contains(filterValue, ignoreCase = true)
                "editorial" -> book.editorial._id.contains(filterValue, ignoreCase = true) ||
                        book.editorial.name.contains(filterValue, ignoreCase = true) ||
                        book.editorial.email.contains(filterValue, ignoreCase = true) ||
                        book.editorial.address.contains(filterValue, ignoreCase = true)
                "language" -> book.language.contains(filterValue, ignoreCase = true)
                "summary" -> book.summary.contains(filterValue, ignoreCase = true)
                "classification" -> book.classification.contains(filterValue, ignoreCase = true)
                "isbn" -> book.isbn.contains(filterValue, ignoreCase = true)
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
        val filteredBooks = _books.value?.mapNotNull { book ->
            val nameMatch = book.title.contains(filterValue, ignoreCase = true)
            val authorNameMatch = book.authors.any { it.name.contains(filterValue, ignoreCase = true) }
            val authorLastNameMatch = book.authors.any { it.last_name.contains(filterValue, ignoreCase = true) }
            val genreMatch = book.genres.any { it.contains(filterValue, ignoreCase = true) }
            val bookCollectionMatch = book.book_collection.name.contains(filterValue, ignoreCase = true)
            val editorialMatch = book.editorial.name.contains(filterValue, ignoreCase = true)
            val languageMatch = book.language.contains(filterValue, ignoreCase = true)
            val summaryMatch = book.summary.contains(filterValue, ignoreCase = true)
            val classificationMatch = book.classification.contains(filterValue, ignoreCase = true)
            val isbnMatch = book.isbn.contains(filterValue, ignoreCase = true)

            val score = when {
                nameMatch -> 10
                authorNameMatch -> 9
                authorLastNameMatch -> 8
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
        _books.postValue(filteredBooks ?: emptyList())
    }

    fun loadBook(bookId: String) {
        viewModelScope.launch {
            if( _books.value?.find{ it._id == bookId } == null) {
                val result = repository.getBookById(bookId)
                _books.postValue(listOf(result))
            }
        }

    }
    
}
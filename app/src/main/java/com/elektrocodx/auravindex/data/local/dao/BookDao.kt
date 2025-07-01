package com.elektrocodx.auravindex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.elektrocodx.auravindex.model.local.AuthorEntity
import com.elektrocodx.auravindex.model.local.BookAuthorCrossRef
import com.elektrocodx.auravindex.model.local.BookCollectionEntity
import com.elektrocodx.auravindex.model.local.BookEntity
import com.elektrocodx.auravindex.model.local.BookStatusEntity
import com.elektrocodx.auravindex.model.local.EditorialEntity
import com.elektrocodx.auravindex.model.local.relations.BookWithRelations

@Dao
interface BookDao {

    @Transaction
    suspend fun insertBookWithRelations(
        book: BookEntity,
        authors: List<AuthorEntity>,
        editorial: EditorialEntity,
        bookStatus: BookStatusEntity,
        bookCollection: BookCollectionEntity,
        crossRefs: List<BookAuthorCrossRef>
    ) {
        insertBook(book)
        insertEditorial(editorial)
        insertBookStatus(bookStatus)
        insertBookCollection(bookCollection)
        insertAuthors(authors)
        insertBookAuthorCrossRefs(crossRefs)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEditorial(editorial: EditorialEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookStatus(status: BookStatusEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookCollection(collection: BookCollectionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuthors(authors: List<AuthorEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookAuthorCrossRefs(crossRefs: List<BookAuthorCrossRef>)

    @Transaction
    @Query("SELECT * FROM books WHERE _id = :bookId")
    suspend fun getBookWithRelations(bookId: String): BookWithRelations

    @Transaction
    @Query("SELECT * FROM books")
    suspend fun getAllBooksWithRelations(): List<BookWithRelations>

}

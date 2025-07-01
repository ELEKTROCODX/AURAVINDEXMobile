package com.elektrocodx.auravindex.model.local.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.elektrocodx.auravindex.model.local.AuthorEntity
import com.elektrocodx.auravindex.model.local.BookAuthorCrossRef
import com.elektrocodx.auravindex.model.local.BookCollectionEntity
import com.elektrocodx.auravindex.model.local.BookEntity
import com.elektrocodx.auravindex.model.local.BookStatusEntity
import com.elektrocodx.auravindex.model.local.EditorialEntity

data class BookWithRelations(
    @Embedded val book: BookEntity,

    @Relation(
        parentColumn = "editorial_id",
        entityColumn = "_id"
    )
    val editorial: EditorialEntity,

    @Relation(
        parentColumn = "book_status_id",
        entityColumn = "_id"
    )
    val bookStatus: BookStatusEntity,

    @Relation(
        parentColumn = "book_collection_id",
        entityColumn = "_id"
    )
    val bookCollection: BookCollectionEntity,

    @Relation(
        parentColumn = "_id",
        entity = AuthorEntity::class,
        entityColumn = "_id",
        associateBy = Junction(
            value = BookAuthorCrossRef::class,
            parentColumn = "book_id",
            entityColumn = "author_id"
        )
    )
    val authors: List<AuthorEntity>
)
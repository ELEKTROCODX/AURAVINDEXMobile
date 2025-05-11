package com.elektro24team.auravindex.model.local

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "bookauthors",
    primaryKeys = ["book_id", "author_id"],
    foreignKeys = [
        ForeignKey(entity = BookEntity::class, parentColumns = ["_id"], childColumns = ["book_id"]),
        ForeignKey(
            entity = AuthorEntity::class,
            parentColumns = ["_id"],
            childColumns = ["author_id"]
        )
    ]
)
data class BookAuthorCrossRef(
    val book_id: String,
    val author_id: String
)
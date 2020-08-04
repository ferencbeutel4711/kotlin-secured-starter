package de.fbeutel.lifeataglance.note.repository

import de.fbeutel.lifeataglance.note.model.Note
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface NoteRepository: MongoRepository<Note, String> {
    fun findAllByCreatorId(creatorId: String): List<Note>
}
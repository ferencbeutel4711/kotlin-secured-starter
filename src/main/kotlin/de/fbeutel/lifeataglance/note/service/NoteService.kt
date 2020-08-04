package de.fbeutel.lifeataglance.note.service

import de.fbeutel.lifeataglance.note.model.Note
import de.fbeutel.lifeataglance.note.model.RawNote
import de.fbeutel.lifeataglance.note.repository.NoteRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class NoteService(private val noteRepository: NoteRepository) {
    fun save(note: RawNote, userId: String): Note {
        return noteRepository.save(Note(
                note.text,
                ObjectId.get().toString(),
                userId,
                note.targetDate,
                note.previousDates,
                note.cancellationDate,
                note.isImportant
        ))
    }

    fun findAllForUser(userId: String): List<Note> {
        return noteRepository.findAllByCreatorId(userId)
    }
}

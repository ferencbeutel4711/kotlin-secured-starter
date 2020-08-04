package de.fbeutel.lifeataglance.note.web

import de.fbeutel.lifeataglance.common.exception.BadRequestException
import de.fbeutel.lifeataglance.note.model.Note
import de.fbeutel.lifeataglance.note.model.RawNote
import de.fbeutel.lifeataglance.note.service.NoteService
import de.fbeutel.lifeataglance.security.oauth2.model.UserPrincipal
import de.fbeutel.lifeataglance.user.annotation.CurrentUser
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/note")
class NoteRestController(private val noteService: NoteService) {

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    fun getNotesForUser(@CurrentUser userPrincipal: UserPrincipal): ResponseEntity<List<Note>> {
        return ResponseEntity.ok(noteService.findAllForUser(userPrincipal.id ?: throw BadRequestException()))
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    fun saveNote(@CurrentUser userPrincipal: UserPrincipal, @Valid @RequestBody note: RawNote): ResponseEntity<Note> {
        return ResponseEntity.ok(noteService.save(note, userPrincipal.id ?: throw BadRequestException()))
    }
}

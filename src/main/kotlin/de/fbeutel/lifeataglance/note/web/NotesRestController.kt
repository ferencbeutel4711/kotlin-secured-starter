package de.fbeutel.lifeataglance.note.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/notes")
class NotesRestController {

    @GetMapping
    fun hello() = "hello there"
}

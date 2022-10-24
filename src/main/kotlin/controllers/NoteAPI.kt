package controllers
import models.Note

class NoteAPI {
    private var notes = ArrayList<Note>()

    fun add(note: Note): Boolean {
        return notes.add(note)
    }

    fun listAllNotes(): String {
        return if (notes.isEmpty()) {
            "No notes stored"
        } else {
            var listOfNotes = ""
            for (i in notes.indices) {
                listOfNotes += "${i}: ${notes[i]} \n"
            }
            listOfNotes
        }
    }

    fun numberOfNotes(): Int {
        return notes.size
    }

    fun findNote(index: Int): Note? {
        return if (isValidListIndex(index, notes)) {
            notes[index]
        } else null
    }
    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }

    fun listActiveNotes(): String {
        return if (numberOfActiveNotes() == 0) {
            "No active notes stored"
        } else {
            var listOfActiveNotes = ""
            for (note in notes) {
                if (!note.isNoteArchived) {
                    listOfActiveNotes += "${notes.indexOf(note)}: $note \n"
                }
                }
            listOfActiveNotes
        }
    }

    fun listArchivedNotes(): String {
        return if (numberOfArchivedNotes() == 0) {
            "No archived notes stored"
        } else {
            var listOfArchivedNotes = ""
            for (note in notes) {
                if (note.isNoteArchived) {
                    listOfArchivedNotes += "${notes.indexOf(note)}: $note \n"
                }
            }
            listOfArchivedNotes
        }
}

    fun numberOfArchivedNotes(): Int {
        var counter = 0
        for (note in notes) {
            if (note.isNoteArchived){
                counter++
            }
        }
        return counter
    }

    fun numberOfActiveNotes(): Int {
        var counter = 0
        for (note in notes) {
            if (!note.isNoteArchived){
                counter++
            }
        }
        return counter
    }

    fun listNotesBySelectedPriority(priority: Int): String {
        return if (notes.isEmpty()){
            "No notes stored"
        } else {
            var listOfNotes = ""
            for (i in notes.indices) {
                if (notes[i].notePriority == priority) {
                    listOfNotes +=
                        """$i: ${notes[i]}
                        """.trimIndent()
                }
            }
            if (listOfNotes.equals("")) {
                "No notes with priority: $priority"
            } else {
                "${numberOfNotesByPriority(priority)} notes with priority $priority: $listOfNotes"
            }
        }
    }

    fun numberOfNotesByPriority(priority: Int): Int {
        var counter = 0
        for (note in notes) {
            if (note.notePriority == priority) {
                counter++
            }
        }
        return counter
    }

    /*fun listNotesByCategory(): String {
        return if (notes.isEmpty()) {
            "No notes stored"
        } else {
            var listOfCategoriedNotes = ""
            for (i in notes.indices) {
                listOfCategoriedNotes += "${i}: ${notes[i]} \n"
            }
            listOfCategoriedNotes
        }
    }*/

    fun deleteNote(indexToDelete : Int): Note? {
        return if (isValidListIndex(indexToDelete, notes)) {
            notes.removeAt(indexToDelete)
        } else null
    }

    fun updateNote(indexToUpdate: Int, note: Note?): Boolean {
        val foundNote = findNote(indexToUpdate)

        if ((foundNote !=null) && (note !=null)) {
            foundNote.noteTitle = note.noteTitle
            foundNote.notePriority = note.notePriority
            foundNote.noteCategory = note.noteCategory
            return true
        }
        return false
    }

    fun isValidIndex(index: Int): Boolean{
        return isValidListIndex(index, notes);
    }
}



package controllers
import models.Note
import persistance.Serializer

class NoteAPI(serializerType: Serializer) {
    private var notes = ArrayList<Note>()
    private var serializer: Serializer = serializerType
    private fun formatListString(notesToFormat : List<Note>): String =
        notesToFormat
            .joinToString (separator = "\n") { note ->
                notes.indexOf(note).toString() + ": "+ note.toString()
            }


    // ADD UPDATE AND DELETE FUNCTIONS

    fun add(note: Note): Boolean {
        return notes.add(note)
    }

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


    // FIND NOTES

    fun numberOfNotes(): Int {
        return notes.size
    }

    fun findNote(index: Int): Note? {
        return if (isValidListIndex(index, notes)) {
            notes[index]
        } else null
    }


    // COUNT NOTE FUNCTIONS

    fun numberOfArchivedNotes(): Int =
        notes.count { note: Note -> note.isNoteArchived }


    fun numberOfActiveNotes(): Int =
        notes.count {note: Note -> !note.isNoteArchived}


    fun numberOfNotesByPriority(priority: Int): Int =
        notes.count {note: Note -> note.notePriority == priority}

    fun numberOfNotesToDo(): Int =
        notes.count { note: Note -> !note.noteStatus}

    fun numberOfNotesDone(): Int =
        notes.count { note: Note -> note.noteStatus}


    // LIST NOTES FUNCTIONS

    fun listAllNotes(): String =
        if (notes.isEmpty())
            "No notes stored"
        else
            formatListString(notes)

    fun listActiveNotes(): String =
        if (numberOfActiveNotes() == 0 )
            "No active notes stored"
        else
            formatListString(notes.filter {note -> note.isNoteArchived == false})


    fun listArchivedNotes(): String =
        if (numberOfArchivedNotes() == 0)
            "No archived notes stored"
        else
            formatListString(notes.filter {note -> note.isNoteArchived == true})

    fun listNotesToDo(): String =
        if (numberOfNotesToDo() == 0 )
            "Your all caught up!"
        else
            formatListString(notes.filter {note -> note.noteStatus == false})
    fun listNotesDone(): String =
        if (numberOfNotesDone() == 0 )
            "Check To-Do"
        else
            formatListString(notes.filter {note -> note.noteStatus == true})

    fun listNotesBySelectedPriority(priority: Int): String =
        if (notes.isEmpty())
            "No notes stored"
        else
        {
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

    fun listNotesByCategory(category : String): String {
        return if (notes.isEmpty()) {
            "No notes stored"
        } else {
            var listOfCategoriedNotes = ""
            for (i in notes.indices) {
                if(notes[i].noteCategory == category){
                    listOfCategoriedNotes += "${i}: ${notes[i]} \n"
                }
            }
            ///
            listOfCategoriedNotes
        }
    }

    // ARCHIVE AND MARK NOTES FUNCTIONS


    fun archiveNote(indexToArchive: Int): Boolean {
        val foundNote = findNote(indexToArchive)

        if ((foundNote !=null) && !foundNote.isNoteArchived) {
            foundNote.isNoteArchived = true
            return true
        }
        return false
    }

    fun markANoteDone(indexToStatus: Int): Boolean {
        val foundNote = findNote(indexToStatus)

        if ((foundNote !=null) && !foundNote.noteStatus) {
            foundNote.noteStatus = true
            return true
        }
        return false
    }

    fun markANoteToDo(indexToStatus: Int): Boolean {
        val foundNote = findNote(indexToStatus)

        if ((foundNote !=null) && !foundNote.noteStatus) {
            foundNote.noteStatus = false
            return false
        }
        return true
    }


    // SEARCH BY FUNCTIONS


    fun searchByTitle(searchString: String) =
        formatListString(
            notes.filter {note -> note.noteTitle.contains(searchString, ignoreCase = true)} )


    fun searchByCategory(searchString: String) =
        formatListString(
            notes.filter {note -> note.noteCategory.contains(searchString, ignoreCase = true)})

    fun searchByPriority(priority: Int): String =
        formatListString(
            notes.filter {note -> note.notePriority == priority})


    // SAVE AND LOAD


    @Throws(Exception::class)
    fun load() {
        notes = serializer.read() as ArrayList<Note>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(notes)
    }

    // VALIDATION


    fun isValidIndex(index: Int): Boolean{
        return isValidListIndex(index, notes);
    }

    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }

    /*fun isValidPriority(priority: Int):Boolean{
        return(priority in 1..5)
    }*/

   /* fun isValidCategory(category: String):Boolean{
        return (category )
    }*/
}



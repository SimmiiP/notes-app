package controllers
import models.Note
import persistance.Serializer
class NoteAPI(serializerType: Serializer) {
    private var notes = ArrayList<Note>()
    private var serializer: Serializer = serializerType
    fun add(note: Note): Boolean {
        return notes.add(note)
    }



    fun numberOfNotes(): Int {
        return notes.size
    }

    fun findNote(index: Int): Note? {
        return if (isValidListIndex(index, notes)) {
            notes[index]
        } else null
    }

    fun isValidIndex(index: Int): Boolean{
        return isValidListIndex(index, notes);
    }

    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }

    fun searchByTitle(searchString: String) =
        notes.filter {note -> note.noteTitle.contains(searchString, ignoreCase = true)}
            .joinToString( separator = "/n"){note ->
                notes.indexOf(note).toString()+":"+note.toString()
            }

    /*fun searchByPriority(index: Int): Note?{
        return
    }*/

    fun listAllNotes(): String =
          if (notes.isEmpty())
             "No notes stored"
         else
            notes.joinToString (separator = "\n"){ note ->
                notes.indexOf(note).toString() + ":" + note.toString()

        }

    fun listActiveNotes(): String =
        if (numberOfActiveNotes() == 0 )
            "No active notes stored"
         else
            notes.filter {note -> note.isNoteArchived == false}
            .joinToString (separator = "\n"){ note ->
                notes.indexOf(note).toString() + ":" + note.toString()
    }

    fun listArchivedNotes(): String =
         if (numberOfArchivedNotes() == 0)
            "No archived notes stored"
         else
             notes.filter {note -> note.isNoteArchived == true}
                 .joinToString (separator = "\n"){ note ->
                     notes.indexOf(note).toString() + ":" + note.toString()
    }

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
    fun numberOfArchivedNotes(): Int {
        return notes.stream()
            .filter { note: Note -> note.isNoteArchived }
            .count()
            .toInt()
    }

    fun numberOfActiveNotes(): Int {
        return notes.stream()
            .filter{note: Note -> !note.isNoteArchived}
            .count()
            .toInt()
    }

    fun numberOfNotesByPriority(priority: Int): Int {
        return notes.stream()
            .filter{note: Note -> note.notePriority == priority}
            .count()
            .toInt()
    }


    /*fun listNotesByCategory(category : String): String {
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

    fun archiveNote(indexToArchive: Int): Boolean {
        val foundNote = findNote(indexToArchive)

        if ((foundNote !=null) && !foundNote.isNoteArchived) {
            foundNote.isNoteArchived = true
            return true
        }
        return false
    }

    @Throws(Exception::class)
    fun load() {
        notes = serializer.read() as ArrayList<Note>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(notes)
    }
}



import controllers.NoteAPI
import models.Note
import mu.KotlinLogging
import persistance.JSONSerializer
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import java.lang.System.exit

private val logger = KotlinLogging.logger {}
private val noteAPI = NoteAPI(JSONSerializer(File("notes.json")))
fun main(args: Array<String>) {

    runMenu()
}


fun mainMenu() : Int {
    return ScannerInput.readNextInt("""
       ------------------------------
       |        NOTE KEEPER APP     |
       ------------------------------
       | NOTE MENU                  |
       |   1) Add a note            |
       |   2) List notes            |
       |   3) Update a note         |
       |   4) Delete a note         |
       |   5) Archive a note        |
       |   20) Save Notes           |
       |   21) Load Notes           |
       -----------------------------
       |   0) Exit                 |
       -----------------------------
       ==>> """.trimIndent())

}

fun subMenu(): Int {
    return ScannerInput.readNextInt("""
       ------------------------------
       |        NOTE KEEPER APP     |
       ------------------------------
       | LIST NOTE SUBMENU               |
       |   1) List all Notes        |
       |   2) List Active Notes     |
       |   3) List Archived Notes   |
       |                            |
       ------------------------------
       |   0) Exit SubMenu                  |
       ------------------------------  
    ==>> """.trimIndent())
}

fun runMenu(): Int {
    do {
        val option =mainMenu()
        when (option){
            1 -> addNote()
            2 -> runSubMenu()
            3 -> updateNote()
            4 -> deleteNote()
            5 -> archiveNote()
            20 -> saveNotes()
            21 -> loadNotes()
            0 -> exitApp()
            else -> println("Invalid option entered: ${option}")
        }
     } while (true)
}

fun runSubMenu(){
    do {
        val option =subMenu()
        when (option){
            1 -> listNotes()
            2 -> listActiveNotes()
            3 -> listArchivedNotes()
            0 -> exitSubMenu()
            else -> println("Invalid option entered: ${option}")
        }
    } while (true)
}

fun saveNotes(){
    try {
        noteAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun loadNotes(){
    try {
        noteAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading to file: $e")
    }
}
fun addNote(){
    logger.info {"addNote() function invoked"}
    val noteTitle = readNextLine("Enter a title for the note: ")
    val notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
    val noteCategory = readNextLine("Enter a category for the note: ")
    val isAdded = noteAPI.add(Note(noteTitle, notePriority, noteCategory, false))

    if (isAdded) {
        println("Added Successfully")
    }else{
        println("Add Failed")
    }
}

fun listNotes(){
    logger.info {"listNotes() function invoked"}
    println(noteAPI.listAllNotes())
}

fun listActiveNotes(){
    logger.info {"listActiveNotes() function invoked"}
    println(noteAPI.listActiveNotes())
}

fun listArchivedNotes(){
    logger.info {"listArchivedNotes() function invoked"}
    println(noteAPI.listArchivedNotes())
}

fun updateNote(){
    logger.info {"updateNote() function invoked"}
    listNotes()
    if (noteAPI.numberOfNotes()>0){
        val indexToUpdate = readNextInt("Enter the index of the note to update: ")
        if (noteAPI.isValidIndex(indexToUpdate)) {
            val noteTitle = readNextLine("Enter a title for the note: ")
            val notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
            val noteCategory = readNextLine("Enter a category for the note: ")

            //pass the index of the note and the new note details to NoteAPI for updating and check for success.
            if (noteAPI.updateNote(indexToUpdate, Note(noteTitle, notePriority, noteCategory, false))){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no notes for this index number")
        }
    }
}

fun archiveNote(){
    logger.info {"archiveNote() function invoked" }
    println(noteAPI.listActiveNotes())
    if (noteAPI.numberOfActiveNotes() > 0) {
        val indexToArchive = readNextInt("Enter the index of the note to archive: ")
        if (noteAPI.isValidIndex(indexToArchive)) {
            val ans: String = readNextLine("Archive this note? ")
            if (ans == "Yes")
                noteAPI.archiveNote(indexToArchive)
            if (ans == "No")
                println("Nevermind")
        } else {
            println("There are no notes for this index number")
        }
    }
}

fun deleteNote(){
    logger.info {"deleteNote() function invoked"}
    listNotes()
    if (noteAPI.numberOfNotes() > 0){
        val indexToDelete = readNextInt("Enter the index of the note to delete: ")
        val noteToDelete = noteAPI.deleteNote(indexToDelete)
        if(noteToDelete != null) {
            print("Delete Successful! Deleted note ${noteToDelete.noteTitle}")
        } else {
            println("Delete NOT Successful")
        }
    }
}

fun exitApp(){
    println("Exiting...bye")
    exit(0)
}

fun exitSubMenu(){
    println("Exiting SubMenu byeeeeeee")
    exit(runMenu())
}
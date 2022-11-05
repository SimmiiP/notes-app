
import controllers.NoteAPI
import models.Note
import mu.KotlinLogging
import persistance.JSONSerializer
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import java.lang.System.exit

const val ANSI_RESET = "\u001B[0m"
const val ANSI_BLACK = "\u001B[30m"
const val ANSI_RED = "\u001B[31m"
const val ANSI_GREEN = "\u001B[32m"
const val ANSI_YELLOW = "\u001B[33m"
const val ANSI_BLUE = "\u001B[34m"
const val ANSI_PURPLE = "\u001B[35m"
const val ANSI_CYAN = "\u001B[36m"
const val ANSI_WHITE = "\u001B[37m"

private val logger = KotlinLogging.logger {}
private val noteAPI = NoteAPI(JSONSerializer(File("notes.json")))
fun main(args: Array<String>) {

    runMenu()
}


fun mainMenu() : Int {
    return ScannerInput.readNextInt("""${ANSI_BLUE}
       ------------------------------
       |        NOTE KEEPER APP     |
       ------------------------------
       | NOTE MENU                  |
       |   1) Add a note            |
       ${ANSI_RED}|   2) List notes            |${ANSI_RESET}
       ${ANSI_BLUE}|   3) Update a note         |
       |   4) Delete a note         |
       |   5) Archive a note        |
       ------------------------------
       ${ANSI_RED}|   6) Search Notes          |${ANSI_RESET}
       ${ANSI_BLUE}------------------------------
       |   7) Mark a note done      |
       |   8) Mark a note to-do     |
       ------------------------------
       |   20) Save Notes           |
       |   21) Load Notes           |
       ------------------------------
       ${ANSI_RED}|   0) Exit                  |
       ------------------------------
       ==>> ${ANSI_RESET}""".trimIndent())

}

fun subMenu(): Int {
    return ScannerInput.readNextInt("""${ANSI_CYAN}
       ------------------------------
       |        NOTE KEEPER APP     |
       ------------------------------
       | LIST NOTE SUBMENU          |
       |   1) List all Notes        |
       |   2) List Active Notes     |
       |   3) List Archived Notes   |
       |   4) List Notes To Do      |
       |   5) List Notes Done       |
       |                            |
       ------------------------------
       ${ANSI_RED}|   0) Exit SubMenu          |
       ------------------------------  
    ==>> ${ANSI_RESET}""".trimIndent())
}

fun subMenuTwo(): Int {
    return ScannerInput.readNextInt(
        """${ANSI_PURPLE}
           ------------------------------
           |        NOTE KEEPER APP     |
           ------------------------------
           | SEARCH NOTE SUBMENU        |
           |   1) Search by Title       |
           |   2) Search by Category    |
           |   3) Search by Priority    |
           |                            |
           ------------------------------
           ${ANSI_RED}|   0) Exit SubMenu          |
           ------------------------------
        ==>> ${ANSI_RESET}""".trimIndent()
    )
}

 // MENUS
fun runMenu(): Int {
    do {
        val option =mainMenu()
        when (option){
            1 -> addNote()
            2 -> runSubMenu()
            3 -> updateNote()
            4 -> deleteNote()
            5 -> archiveNote()
            6 -> runSubMenuTwo()
            7 -> markANoteDone()
            8 -> markANoteToDo()
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
            4 -> listNotesToDo()
            5 -> listNotesDone()
            0 -> exitSubMenu()
            else -> println("Invalid option entered: ${option}")
        }
    } while (true)
}

fun runSubMenuTwo(){
    do {
        val option =subMenuTwo()
        when (option){
            1 -> searchNotesByTitle()
            2 -> searchNotesByCategory()
            3 -> searchNotesByPriority()
            0 -> exitSubMenu()
            else -> println("Invalid option entered: ${option}")
        }
    } while (true)
}

 // SAVE AND LOAD
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

// ADD, UPDATE AND DELETE
fun addNote(){
    logger.info {"addNote() function invoked"}
    val noteTitle = readNextLine("Enter a title for the note: ")
    val notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
    val noteCategory = readNextLine("Enter a category for the note: ")
    val noteContent = readNextLine("Enter Message: ")
    val isAdded = noteAPI.add(Note(noteTitle, notePriority, noteCategory, false, noteContent,  false))


    if (isAdded) {
        println("Added Successfully")
    }else{
        println("Add Failed")
    }
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
            val noteContent = readNextLine("Enter Message: ")

            //pass the index of the note and the new note details to NoteAPI for updating and check for success.
            if (noteAPI.updateNote(indexToUpdate, Note(noteTitle, notePriority, noteCategory, false, noteContent, false))){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
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

 // ARCHIVE AND MARK NOTES
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

fun markANoteDone(){
    logger.info {"markANoteDone() function invoked" }
    println(noteAPI.listNotesToDo())
    if (noteAPI.numberOfNotesToDo() > 0) {
        val indexToStatus = readNextInt("Enter the index of the note to mark done: ")
        if (noteAPI.isValidIndex(indexToStatus)) {
            val ans: String = readNextLine("Mark this note done? ")
            if (ans == "Yes")
                noteAPI.markANoteDone(indexToStatus)
            if (ans == "No")
                println("Nevermind")
        } else {
            println("There are no notes for this index number")
        }
    }
}

fun markANoteToDo(){
    logger.info {"markANoteToDo() function invoked" }
    println(noteAPI.listNotesDone())
    if (noteAPI.numberOfNotesDone() > 0) {
        val indexToStatus = readNextInt("Enter the index of the note to mark to-do: ")
        if (noteAPI.isValidIndex(indexToStatus)) {
            val ans: String = readNextLine("Mark this note to-do? ")
            if (ans == "Yes")
                noteAPI.markANoteToDo(indexToStatus)
            if (ans == "No")
                println("Nevermind")
        } else {
            println("There are no notes for this index number")
        }
    }
}

 // LIST NOTES
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

fun listNotesToDo(){
    logger.info {"listNotesToDo() function invoked"}
    println(noteAPI.listNotesToDo())
}

fun listNotesDone(){
    logger.info {"listNotesDone() function invoked"}
    println(noteAPI.listNotesDone())
}

 // SEARCH NOTES
fun searchNotesByTitle(){
    val searchTitle = readNextLine( "Enter the title to search by: ")
    val searchResults = noteAPI.searchByTitle(searchTitle)
    if (searchResults.isEmpty()){
        println("No notes of that title found")
    } else {
        println(searchResults)
    }
}

fun searchNotesByCategory(){
    val searchCategory = readNextLine( "Enter the category to search by: ")
    val searchResults = noteAPI.searchByCategory(searchCategory)
    if (searchResults.isEmpty()){
        println("No notes of that category found")
    } else {
        println(searchResults)
    }
}

fun searchNotesByPriority(){
    val searchPriority = readNextInt( "Enter the priority to search by: ")
    val searchResults = noteAPI.searchByPriority(searchPriority)
    if (searchResults.isEmpty()){
        println("No notes of that priority found")
    } else {
        println(searchResults)
    }
}

 // EXIT MENU'S

fun exitApp(){
    println("Exiting...bye")
    exit(0)
}

fun exitSubMenu(){
    println("Exiting SubMenu byeeeeeee")
    exit(runMenu())
}
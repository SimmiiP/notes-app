package controllers

import models.Note
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue
import persistance.JSONSerializer
import persistance.XMLSerializer
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertFalse
class NoteAPITest {

    private var learnKotlin: Note? = null
    private var summerHoliday: Note? = null
    private var codeApp: Note? = null
    private var testApp: Note? = null
    private var swim: Note? = null
    private var populatedNotes: NoteAPI? = NoteAPI(XMLSerializer(File("notes.xml")))
    private var emptyNotes: NoteAPI? = NoteAPI(XMLSerializer(File("notes.xml")))

    @BeforeEach
    fun setup() {
        learnKotlin = Note("Learning Kotlin", 5, "College", false, "Exam tomorrow", false)
        summerHoliday = Note("Summer Holiday to France", 1, "Holiday", false, "Book tickets", false)
        codeApp = Note("Code App", 4, "Work", true, "9am meeting", true)
        testApp = Note("Test App", 4, "Work", false, "7pm meeting", false)
        swim = Note("Swim - Pool", 3, "Hobby", true, "Fix roller skates", true)

        //adding 5 Note to the notes api
        populatedNotes!!.add(learnKotlin!!)
        populatedNotes!!.add(summerHoliday!!)
        populatedNotes!!.add(codeApp!!)
        populatedNotes!!.add(testApp!!)
        populatedNotes!!.add(swim!!)
    }

    @AfterEach
    fun tearDown() {
        learnKotlin = null
        summerHoliday = null
        codeApp = null
        testApp = null
        swim = null
        populatedNotes = null
        emptyNotes = null
    }

    @Nested
    inner class AddNotes {
        @Test
        fun `adding a Note to a populated list adds to ArrayList`() {
            val newNote = Note("Study Lambdas", 1, "College", false, "Exam tomorrow", false)
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertTrue(populatedNotes!!.add(newNote))
            assertEquals(6, populatedNotes!!.numberOfNotes())
            assertEquals(newNote, populatedNotes!!.findNote(populatedNotes!!.numberOfNotes() - 1))
        }

        @Test
        fun `adding a Note to an empty list adds to ArrayList`() {
            val newNote = Note("Study Lambdas", 1, "College", false,"Exam tomorrow", false)
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.add(newNote))
            assertEquals(1, emptyNotes!!.numberOfNotes())
            assertEquals(newNote, emptyNotes!!.findNote(emptyNotes!!.numberOfNotes() - 1))
        }
    }

    @Nested
    inner class ListNotes {

        @Test
        fun `listAllNotes returns No Notes Stored message when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.listAllNotes().lowercase().contains("no notes"))
        }

        @Test
        fun `listAllNotes returns Notes when ArrayList has notes stored`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val notesString = populatedNotes!!.listAllNotes().lowercase()
            assertTrue(notesString.contains("learning kotlin"))
            assertTrue(notesString.contains("code app"))
            assertTrue(notesString.contains("test app"))
            assertTrue(notesString.contains("swim"))
            assertTrue(notesString.contains("summer holiday"))
        }

        @Test
        fun `listNotesByCategory returns No Notes Stored message when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.listNotesByCategory("").lowercase().contains("no notes"))
        }

        @Test
        fun `listNotesByCategory returns Notes when ArrayList has notes stored`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val notesString = populatedNotes!!.listNotesByCategory("").lowercase()
            assertTrue(notesString.contains(""))

        }
        @Test
        fun `listActiveNotes returns no active notes stored when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfActiveNotes())
            assertTrue(
                emptyNotes!!.listActiveNotes().lowercase().contains("no active notes")
            )
        }

        @Test
        fun `listActiveNotes returns active notes when ArrayList has active notes stored`() {
            assertEquals(3, populatedNotes!!.numberOfActiveNotes())
            val activeNotesString = populatedNotes!!.listActiveNotes().lowercase()
            assertTrue(activeNotesString.contains("learning kotlin"))
            Assertions.assertFalse(activeNotesString.contains("code app"))
            assertTrue(activeNotesString.contains("summer holiday"))
            assertTrue(activeNotesString.contains("test app"))
            Assertions.assertFalse(activeNotesString.contains("swim"))
        }

        @Test
        fun `listArchivedNotes returns no archived notes when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfArchivedNotes())
            assertTrue(
                emptyNotes!!.listArchivedNotes().lowercase().contains("no archived notes")
            )
        }

        @Test
        fun `listNotesBySelectedPriority returns No Notes when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(
                emptyNotes!!.listNotesBySelectedPriority(1).lowercase().contains("no notes")
            )
        }

        @Test
        fun `listNotesBySelectedPriority returns no notes when no notes of that priority exist`() {
            //Priority 1 (1 note), 2 (none), 3 (1 note). 4 (2 notes), 5 (1 note)
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val priority2String = populatedNotes!!.listNotesBySelectedPriority(2).lowercase()
            assertTrue(priority2String.contains("no notes"))
            assertTrue(priority2String.contains("2"))
        }

        @Test
        fun `listNotesBySelectedPriority returns all notes that match that priority when notes of that priority exist`() {
            //Priority 1 (1 note), 2 (none), 3 (1 note). 4 (2 notes), 5 (1 note)
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val priority1String = populatedNotes!!.listNotesBySelectedPriority(1).lowercase()
            assertTrue(priority1String.contains("1 note"))
            assertTrue(priority1String.contains("priority 1"))
            assertTrue(priority1String.contains("summer holiday"))
            Assertions.assertFalse(priority1String.contains("swim"))
            Assertions.assertFalse(priority1String.contains("learning kotlin"))
            Assertions.assertFalse(priority1String.contains("code app"))
            Assertions.assertFalse(priority1String.contains("test app"))

            val priority4String = populatedNotes!!.listNotesBySelectedPriority(4).lowercase()
            assertTrue(priority4String.contains("2 note"))
            assertTrue(priority4String.contains("priority 4"))
            Assertions.assertFalse(priority4String.contains("swim"))
            assertTrue(priority4String.contains("code app"))
            assertTrue(priority4String.contains("test app"))
            Assertions.assertFalse(priority4String.contains("learning kotlin"))
            Assertions.assertFalse(priority4String.contains("summer holiday"))
        }

    }

    @Nested
    inner class DeleteNotes {
        @Test
        fun `deleting a Note that does not exist, returns null`() {
            assertNull(emptyNotes!!.deleteNote(0))
            assertNull(populatedNotes!!.deleteNote(-1))
            assertNull(populatedNotes!!.deleteNote(5))
        }

        @Test
        fun `deleting a note that exists delete and returns deleted object`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertEquals(swim, populatedNotes!!.deleteNote(4))
            assertEquals(4, populatedNotes!!.numberOfNotes())
            assertEquals(learnKotlin, populatedNotes!!.deleteNote(0))
            assertEquals(3, populatedNotes!!.numberOfNotes())
        }
    }

    @Nested
    inner class UpdateNotes {
        @Test
        fun `updating a note that does not exist returns false`() {
            assertFalse(populatedNotes!!.updateNote(6, Note("Updating Note", 2, "Work", false,"7pm meeting", false)))
            assertFalse(populatedNotes!!.updateNote(-1, Note("Updating Note", 2, "7pm meeting", false,"7pm meeting", false)))
            assertFalse(emptyNotes!!.updateNote(0, Note("Updating Note", 2, "Work", false,"7pm meeting", false)))
        }

        @Test
        fun `updating a note that exists returns true and updates`() {
            //check note 5 exists and check the contents
            assertEquals(swim, populatedNotes!!.findNote(4))
            assertEquals("Swim - Pool", populatedNotes!!.findNote(4)!!.noteTitle)
            assertEquals(3, populatedNotes!!.findNote(4)!!.notePriority)
            assertEquals("Hobby", populatedNotes!!.findNote(4)!!.noteCategory)

            //update note 5 with new information and ensure contents updated successfully
            assertTrue(populatedNotes!!.updateNote(4, Note("Updating Note", 2, "College", false,"Exam tomorrow", false)))
            assertEquals("Updating Note", populatedNotes!!.findNote(4)!!.noteTitle)
            assertEquals(2, populatedNotes!!.findNote(4)!!.notePriority)
            assertEquals("College", populatedNotes!!.findNote(4)!!.noteCategory)
        }
    }

    @Nested
    inner class PersistenceTests {

        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty notes.XML file.
            val storingNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            storingNotes.store()

            //Loading the empty notes.xml file into a new object
            val loadedNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            loadedNotes.load()

            //Comparing the source of the notes (storingNotes) with the XML loaded notes (loadedNotes)
            assertEquals(0, storingNotes.numberOfNotes())
            assertEquals(0, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
        }

        @Test
        fun `saving and loading an loaded collection in XML doesn't loose data`() {
            // Storing 3 notes to the notes.XML file.
            val storingNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            storingNotes.add(testApp!!)
            storingNotes.add(swim!!)
            storingNotes.add(summerHoliday!!)
            storingNotes.store()

            //Loading notes.xml into a different collection
            val loadedNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            loadedNotes.load()

            //Comparing the source of the notes (storingNotes) with the XML loaded notes (loadedNotes)
            assertEquals(3, storingNotes.numberOfNotes())
            assertEquals(3, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
            assertEquals(storingNotes.findNote(0), loadedNotes.findNote(0))
            assertEquals(storingNotes.findNote(1), loadedNotes.findNote(1))
            assertEquals(storingNotes.findNote(2), loadedNotes.findNote(2))
        }

        @Test
        fun `saving and loading an empty collection in JSON doesn't crash app`() {
            // Saving an empty notes.json file.
            val storingNotes = NoteAPI(JSONSerializer(File("notes.json")))
            storingNotes.store()

            //Loading the empty notes.json file into a new object
            val loadedNotes = NoteAPI(JSONSerializer(File("notes.json")))
            loadedNotes.load()

            //Comparing the source of the notes (storingNotes) with the json loaded notes (loadedNotes)
            assertEquals(0, storingNotes.numberOfNotes())
            assertEquals(0, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
        }

        @Test
        fun `saving and loading an loaded collection in JSON doesn't loose data`() {
            // Storing 3 notes to the notes.json file.
            val storingNotes = NoteAPI(JSONSerializer(File("notes.json")))
            storingNotes.add(testApp!!)
            storingNotes.add(swim!!)
            storingNotes.add(summerHoliday!!)
            storingNotes.store()

            //Loading notes.json into a different collection
            val loadedNotes = NoteAPI(JSONSerializer(File("notes.json")))
            loadedNotes.load()

            //Comparing the source of the notes (storingNotes) with the json loaded notes (loadedNotes)
            assertEquals(3, storingNotes.numberOfNotes())
            assertEquals(3, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
            assertEquals(storingNotes.findNote(0), loadedNotes.findNote(0))
            assertEquals(storingNotes.findNote(1), loadedNotes.findNote(1))
            assertEquals(storingNotes.findNote(2), loadedNotes.findNote(2))
        }
    }

    @Nested
    inner class ArchiveNotes {
        @Test
        fun `archiving a note that does not exist returns false`() {

            assertFalse(populatedNotes!!.archiveNote(6))
            assertFalse(populatedNotes!!.archiveNote(-1))
            assertFalse(emptyNotes!!.archiveNote(0))
        }

        @Test
        fun `Archiving a note that exists returns true and archives the note`() {
            //check note 4 exists and check the contents
            assertEquals(testApp, populatedNotes!!.findNote(3))
            assertEquals("Test App", populatedNotes!!.findNote(3)!!.noteTitle)
            assertEquals(4, populatedNotes!!.findNote(3)!!.notePriority)
            assertEquals("Work", populatedNotes!!.findNote(3)!!.noteCategory)
            assertEquals(false, populatedNotes!!.findNote(3)!!.isNoteArchived)
            assertTrue(populatedNotes!!.archiveNote(3))
            assertEquals("Test App", populatedNotes!!.findNote(3)!!.noteTitle)
            assertEquals(4, populatedNotes!!.findNote(3)!!.notePriority)
            assertEquals("Work", populatedNotes!!.findNote(3)!!.noteCategory)
            assertEquals(true, populatedNotes!!.findNote(3)!!.isNoteArchived)
        }
    }

    @Nested
    inner class MarkNotesDone {
        @Test
        fun `Marking a note that does not exist returns false`() {

            assertFalse(populatedNotes!!.markANoteDone(6))
            assertFalse(populatedNotes!!.markANoteDone(-1))
            assertFalse(emptyNotes!!.markANoteDone(0))
        }

        @Test
        fun `Marking a note that exists returns true and marks the note`() {
            //check note 4 exists and check the contents
            assertEquals(testApp, populatedNotes!!.findNote(3))
            assertEquals("Test App", populatedNotes!!.findNote(3)!!.noteTitle)
            assertEquals(4, populatedNotes!!.findNote(3)!!.notePriority)
            assertEquals("Work", populatedNotes!!.findNote(3)!!.noteCategory)
            assertEquals(false, populatedNotes!!.findNote(3)!!.isNoteArchived)
            assertTrue(populatedNotes!!.archiveNote(3))
            assertEquals("7pm meeting", populatedNotes!!.findNote(3)!!.noteContent)
            assertEquals(false, populatedNotes!!.findNote(3)!!.noteStatus)
            assertTrue(populatedNotes!!.markANoteDone(3))
            assertEquals("Test App", populatedNotes!!.findNote(3)!!.noteTitle)
            assertEquals(4, populatedNotes!!.findNote(3)!!.notePriority)
            assertEquals("Work", populatedNotes!!.findNote(3)!!.noteCategory)
            assertEquals(true, populatedNotes!!.findNote(3)!!.isNoteArchived)
            assertEquals("7pm meeting", populatedNotes!!.findNote(3)!!.noteContent)
            assertEquals(true, populatedNotes!!.findNote(3)!!.noteStatus)
        }
    }

    @Nested
    inner class MarkNotesToDo {
        @Test
        fun `Marking a note that does not exist returns false`() {

            assertTrue(populatedNotes!!.markANoteToDo(6))
            assertTrue(populatedNotes!!.markANoteToDo(-1))
            assertTrue(emptyNotes!!.markANoteToDo(0))
        }

        @Test
        fun `Marking a note that exists returns false and marks the note`() {
            //check note 4 exists and check the contents
            assertEquals(codeApp, populatedNotes!!.findNote(2))
            assertEquals("Code App", populatedNotes!!.findNote(2)!!.noteTitle)
            assertEquals(4, populatedNotes!!.findNote(2)!!.notePriority)
            assertEquals("Work", populatedNotes!!.findNote(2)!!.noteCategory)
            assertEquals(true, populatedNotes!!.findNote(2)!!.isNoteArchived)
            assertFalse(populatedNotes!!.archiveNote(2))
            assertEquals("9am meeting", populatedNotes!!.findNote(2)!!.noteContent)
            assertEquals(true, populatedNotes!!.findNote(2)!!.noteStatus)
            assertFalse(populatedNotes!!.markANoteDone(2))
            assertEquals("Code App", populatedNotes!!.findNote(2)!!.noteTitle)
            assertEquals(4, populatedNotes!!.findNote(2)!!.notePriority)
            assertEquals("Work", populatedNotes!!.findNote(2)!!.noteCategory)
            assertEquals(true, populatedNotes!!.findNote(2)!!.isNoteArchived)
            assertEquals("9am meeting", populatedNotes!!.findNote(2)!!.noteContent)
            assertEquals(true, populatedNotes!!.findNote(2)!!.noteStatus)
        }
    }


    @Nested
    inner class CountingMethods {

        @Test
        fun numberOfNotesCalculatedCorrectly() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertEquals(0, emptyNotes!!.numberOfNotes())
        }

        @Test
        fun numberOfArchivedNotesCalculatedCorrectly() {
            assertEquals(2, populatedNotes!!.numberOfArchivedNotes())
            assertEquals(0, emptyNotes!!.numberOfArchivedNotes())
        }

        @Test
        fun numberOfActiveNotesCalculatedCorrectly() {
            assertEquals(3, populatedNotes!!.numberOfActiveNotes())
            assertEquals(0, emptyNotes!!.numberOfActiveNotes())
        }

        @Test
        fun numberOfNotesByPriorityCalculatedCorrectly() {
            assertEquals(1, populatedNotes!!.numberOfNotesByPriority(1))
            assertEquals(0, populatedNotes!!.numberOfNotesByPriority(2))
            assertEquals(1, populatedNotes!!.numberOfNotesByPriority(3))
            assertEquals(2, populatedNotes!!.numberOfNotesByPriority(4))
            assertEquals(1, populatedNotes!!.numberOfNotesByPriority(5))
            assertEquals(0, emptyNotes!!.numberOfNotesByPriority(1))
        }

        @Test
        fun numberOfNotesToDoCalculatedCorrectly() {
            assertEquals(3, populatedNotes!!.numberOfNotesToDo())
            assertEquals(0, emptyNotes!!.numberOfNotesToDo())
        }

        @Test
        fun numberOfNotesDoneCalculatedCorrectly() {
            assertEquals(2, populatedNotes!!.numberOfNotesDone())
            assertEquals(0, emptyNotes!!.numberOfNotesDone())
        }

    }

    @Nested
    inner class SearchMethods {

        @Test
        fun `search notes by title returns no notes when no notes with that title exists`(){
            assertEquals(5,populatedNotes!!.numberOfNotes())
            val searchResults = populatedNotes!!.searchByTitle("no results expected")
            assertTrue(searchResults.isEmpty())

            assertEquals(0,emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.searchByTitle("").isEmpty())
        }

        @Test
        fun `search notes by title returns notes when notes with that title exist`(){
            assertEquals(5, populatedNotes!!.numberOfNotes())

            var searchResults = populatedNotes!!.searchByTitle("Code App")
            assertTrue(searchResults.contains("Code App"))
            assertFalse(searchResults.contains("Test App"))

            searchResults = populatedNotes!!.searchByTitle("App")
            assertTrue(searchResults.contains("Code App"))
            assertTrue(searchResults.contains("Test App"))
            assertFalse(searchResults.contains("Swim-Pool"))

            searchResults = populatedNotes!!.searchByTitle(("aPp"))
            assertTrue(searchResults.contains("Code App"))
            assertTrue(searchResults.contains("Test App"))
            assertFalse(searchResults.contains("Swim - Pool"))
        }

        @Test
        fun `search notes by category returns no notes when no notes with that category exists`(){
            assertEquals(5,populatedNotes!!.numberOfNotes())
            val searchResults = populatedNotes!!.searchByCategory("no results expected")
            assertTrue(searchResults.isEmpty())

            assertEquals(0,emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.searchByCategory("").isEmpty())
        }

        @Test
        fun `search notes by category returns notes when notes with that category exist`(){
            assertEquals(5, populatedNotes!!.numberOfNotes())

            var searchResults = populatedNotes!!.searchByCategory("Hobby")
            assertTrue(searchResults.contains("Hobby"))
            assertFalse(searchResults.contains("Work"))

            searchResults = populatedNotes!!.searchByCategory("Hob")
            assertTrue(searchResults.contains("Hobby"))
            assertFalse(searchResults.contains("College"))

            searchResults = populatedNotes!!.searchByCategory(("HoBbY"))
            assertTrue(searchResults.contains("Hobby"))
            assertFalse(searchResults.contains("College"))
        }

        @Test
        fun `search notes by priority returns no notes when no notes with that priority exists`(){
            assertEquals(5,populatedNotes!!.numberOfNotes())
            val searchResults = populatedNotes!!.searchByPriority(0)
            assertTrue(searchResults.isEmpty())

            assertEquals(0,emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.searchByPriority(0).isEmpty())
        }

        @Test
        fun `search notes by priority returns notes when notes with that priority exist`(){
            assertEquals(5, populatedNotes!!.numberOfNotes())

            var searchResults = populatedNotes!!.searchByPriority(1)
            assertTrue(searchResults.contains("Summer Holiday to France"))
            assertFalse(searchResults.contains("Work"))

        }
    }
}

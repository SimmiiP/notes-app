# notes-app

++ Start Menu 

+++ I added a start menu that includes the menu options:
 Add a note, List notes, Update a note, Delete a note,
Archive a note, Search Notes, Mark a note done, Mark a note to-do,
Save Notes, Load Notes and Exit.
Two of these options lead to sub-menus.

++ SubMenu 

+++ The first sub-menu includes the options:
List all Notes, List Active Notes, List Archived Notes,
List Notes To Do, List Notes Done and Exit SubMenu.
The exit submenu option returns the user to the main menu

++ SubMenuTwo

+++ The second sub-menu includes the options:
Search by Title, Search by Category, Search by Priority and
Exit SubMenu. 

++ Add Notes

+++ The add notes function in the Main.kt reads the information into the 
new notes object that the user wants to add.
The add notes function in the API then adds a new note.

++ Update Notes

+++ The update notes function in the main.kt reads the new information 
that the user wants to update the note with.
In the API the function then updates the note with the new info.

++ Delete Notes

+++  In the main.kt, the delete notes function reads index number of the 
note the user wants to delete, the function in the API then deletes it.


++ Archive Notes 

+++ The user has to input the index number of the note they would like to archive
and confirm that they want to archive it in the main which reads these responses.
The answers to the questions are yes and no for the function to read, and the first letter 
of the users response hase to be to uppercase. 
The api function then marks the isNoteArchived field to true instead of its default false.
The listing function for active notes is also used here to list the notes that can be archived.


++ Mark a note done 

+++ The user has to input the index number of the note they would like to mark done,
and confirm that they would like to mark it done with a yes or no answer same as the
archive notes function. The api function then marks the note status field as true.
The listing function for notes to-do is also used here to list the notes that can actually 
be marked done 


++ Mark a note to-do 

+++ Similar to the previous entry except that the functions now do the opposite, 
the listing function for notes done is used to list the notes that can be marked to-do.


++ List Notes

+++ Prints all notes. the api function fetches all notes.


++ List Active Notes

+++ Prints active notes. The API function fetches notes where the isNoteArchived
field is set to false.


++ List Archived Notes 

+++ Prints archived notes. The Api function fetches notes where the isNoteArchived 
field is set to true.

++ List Notes To-Do

+++ Prints notes to-do. The Api function fetches notes where the noteStatus
field is set to false.

++ List Notes Done

+++ Prints notes done. The Api function fetches notes where the noteStatus
field is set to true.

++ List Notes by Selected Priority

+++ Prints notes by selected priority. The Api function fetches notes that contain the same 
number between 1 and 5 as imputed by the user in the priority field.


++ List Notes by Category

+++ Prints notes by category. The Api function fetches notes that contain the same words as
imputed by the user in the category field. This accounts for half imputed categories and when words 
are not fully spelled. 


++ Search notes by title

+++ A string is imputed by the user which is matched by the api to one of the notes 
with that title. If it doesn't match a result, the console returns "No notes of that title found". It ignores
case and returns half spelled values as well.

++ Search notes by priority

+++ A number is imputed by the user which is matched by the api to one of the notes
with that priority. If it doesn't match a result, the console returns "No notes of that priority found"

++ Search notes by category

+++ A string is imputed by the user which is matched by the api to one of the notes
with that title. If it doesn't match a result, the console returns "No notes of that category found". It ignores 
case and returns half spelled values as well.

++ Exit App

+++ End the instance of the app running.

++ Exit Sub Menu 

+++ Returns to the main menu instead of ending the instance.

++ Save Notes

+++ Serializer is used to save notes.

++ Load Notes 

+++ Serializer is used to load up saved notes.  
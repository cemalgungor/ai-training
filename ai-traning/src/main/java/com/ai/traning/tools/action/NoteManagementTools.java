package com.ai.traning.tools.action;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class NoteManagementTools {

    public record NoteResult(
            Long noteId,
            String title,
            String message,
            String timeStamp
    ){}

    private record Note(Long id, String title,String content, LocalDateTime createdAt){}

    private final Map<Long, Note> notes = new ConcurrentHashMap<>();
    private final AtomicLong noteIdGenerator = new AtomicLong();

    @Tool(description = "Create a new note with title and content")
    public NoteResult createNote(String title, String content){
        Long noteId = noteIdGenerator.incrementAndGet();
        Note note = new Note(noteId, title, content, LocalDateTime.now());
        notes.put(noteId, note);

        return new NoteResult(noteId, title, "Note created successfully", note.createdAt.toString());
    }

    @Tool(description = "Update the content of an existing note")
    public NoteResult updateNote(Long noteId, String newContent){
        Note existingNote = notes.get(noteId);
        if(existingNote == null){
            return new NoteResult(noteId, "Note not found", newContent, "");
        }

        Note updatedNote = new Note(noteId , existingNote.title(), newContent, existingNote.createdAt());
        notes.put(noteId, updatedNote);

        return new NoteResult(noteId, existingNote.title(), "Note updated successfully" , updatedNote.createdAt.toString());
    }

    @Tool(description = "Delete a note by Id")
    public NoteResult deleteNote(Long noteId){
        Note removedNote = notes.get(noteId);
        if(removedNote == null){
            return new NoteResult(noteId, "" , "Note not found" , "");
        }

        return new NoteResult(noteId, removedNote.title(), "Note deleted successfully", removedNote.createdAt.toString());
    }

    @Tool(description = "Get note details by ID")
    public NoteResult getNote(Long noteId){
        Note note = notes.get(noteId);
        if(note == null){
            return new NoteResult(noteId, "" , "Note not found" , "");
        }
        return new NoteResult(noteId, note.title(), note.content, note.createdAt.toString());
    }

}

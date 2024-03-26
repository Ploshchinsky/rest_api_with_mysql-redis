package ploton.main.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String head;
    private String content;
    private boolean isCompleted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return id == note.id && isCompleted == note.isCompleted && Objects.equals(head, note.head) && Objects.equals(content, note.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, head, content, isCompleted);
    }
}

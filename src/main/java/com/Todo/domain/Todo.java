package com.Todo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Todo {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long id;

    @Setter
    private String title;

    @Setter
    private String description;

    @Setter
    private LocalDateTime dueDate;

    @Setter
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @OneToMany(mappedBy = "todo")
    private List<Tag> tags = new ArrayList<>();

    @Setter
    private Boolean completed = false;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Todo(String title, String description, LocalDateTime dueDate, Priority priority, User user){
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.user = user;
    }


    public static Todo of(String title, String description, LocalDateTime dueDate, Priority priority, User user) {
        return new Todo(title, description, dueDate, priority, user);
    }


}

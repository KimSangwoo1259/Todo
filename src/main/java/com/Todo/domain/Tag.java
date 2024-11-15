package com.Todo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @Setter
    private String name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "todo_id")
    private Todo todo;

    private Tag(String name, Todo todo) {
        this.name = name;
        this.todo = todo;
    }

    public static Tag of(String name, Todo todo) {
        return new Tag(name, todo);
    }
}

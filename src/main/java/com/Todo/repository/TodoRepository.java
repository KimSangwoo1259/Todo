package com.Todo.repository;

import com.Todo.domain.Todo;
import com.Todo.repository.querydsl.TodoRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo,Long>, TodoRepositoryCustom {

    List<Todo> findAllByUser_Username(String username);

}

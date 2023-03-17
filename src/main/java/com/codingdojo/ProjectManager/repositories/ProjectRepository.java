package com.codingdojo.ProjectManager.repositories;

import com.codingdojo.ProjectManager.models.Project;
import com.codingdojo.ProjectManager.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

    List<Project> findByUsersJoinedNotContains(User user); //Proyectos en los que el usuario NO pertenece

    List<Project> findAllByUsersJoined(User user); //Proyectos a lso que el usuario pertenece
}

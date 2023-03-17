package com.codingdojo.ProjectManager.services;

import com.codingdojo.ProjectManager.models.LoginUser;
import com.codingdojo.ProjectManager.models.Project;
import com.codingdojo.ProjectManager.models.User;
import com.codingdojo.ProjectManager.repositories.ProjectRepository;
import com.codingdojo.ProjectManager.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

@Service
public class AppService {
    @Autowired
    private UserRepository repoUser;

    @Autowired
    private ProjectRepository repoProject;

    public User register(User nuevoUsuario, BindingResult result) {
        if(!nuevoUsuario.getPassword().equals(nuevoUsuario.getConfirmPassword()))
            result.rejectValue("password","Matches", "Las contraseñas no coinciden");

        if(repoUser.findByEmail(nuevoUsuario.getEmail()).isPresent())
            result.rejectValue("email", "Unique", "El correo eléctronico ya existe");

        if(result.hasErrors()) {
            return null;
        } else {
            String encryptedPassword = BCrypt.hashpw(nuevoUsuario.getPassword(), BCrypt.gensalt());
            nuevoUsuario.setPassword(encryptedPassword);
            return repoUser.save(nuevoUsuario);
        }
    }

    public User login(LoginUser nuevoLogin, BindingResult result) {
        Optional<User> posibleUsuario = repoUser.findByEmail(nuevoLogin.getEmail());

        if(!posibleUsuario.isPresent()) {
            result.rejectValue("email", "Unique", "Correo no registrado");
            return null;
        }

        User userLogin = posibleUsuario.get();
        if(!BCrypt.checkpw(nuevoLogin.getPassword(), userLogin.getPassword())) {
            result.rejectValue("password", "Matches", "Contraseña incorrecta");
        }

        return result.hasErrors() ? null : userLogin;
    }

    public Project saveProject(Project nuevoProyecto) {
        return repoProject.save(nuevoProyecto);
    }

    public Project findProject(Long id) {
        return repoProject.findById(id).orElse(null);
    }

    public User findUser(Long id) {
        return repoUser.findById(id).orElse(null);
    }

    public User saveUser(User nuevoUser) {
        return repoUser.save(nuevoUser);
    }

    public List<Project> findOtherProjects(User userInSession) {
        return repoProject.findByUsersJoinedNotContains(userInSession);
    }

    public List<Project>  findMyProjects(User userInSession) {
        return repoProject.findAllByUsersJoined(userInSession);
    }

    public void saveProjectUser(Long projectId, Long userId) {
        User myUser = findUser(userId);
        Project myProject = findProject(projectId);

        myUser.getProjectsJoined().add(myProject);
        repoUser.save(myUser);
    }

    public void removeProjectUser(Long projectId, Long userId) {
        User myUser = findUser(userId);
        Project myProject = findProject(projectId);

        myUser.getProjectsJoined().remove(myProject);
        repoUser.save(myUser);
    }
}

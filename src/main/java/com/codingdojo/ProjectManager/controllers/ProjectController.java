package com.codingdojo.ProjectManager.controllers;

import com.codingdojo.ProjectManager.models.Project;
import com.codingdojo.ProjectManager.models.User;
import com.codingdojo.ProjectManager.services.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private AppService service;

    @GetMapping("/new")
    public String newProject(@ModelAttribute("project") Project project, HttpSession session) {
        User currentUser = (User) session.getAttribute("userSession");

        if(currentUser == null) {
            return "redirect:/";
        }

        return "new.jsp";
    }

    @PostMapping("/create")
    public String createProject(@Valid @ModelAttribute("project") Project project, BindingResult result, HttpSession session) {
        User currentUser = (User) session.getAttribute("userSession");

        if(currentUser == null) {
            return "redirect:/";
        }

        if(result.hasErrors()) {
            return "new.jsp";
        } else {
            Project nuevoProyecto = service.saveProject(project);
            User myUser = service.findUser(currentUser.getId());
            myUser.getProjectsJoined().add(nuevoProyecto);
            service.saveUser(myUser);

            return "redirect:/dashboard";
        }
    }

    @GetMapping("/join/{projectId}")
    public String join(@PathVariable("projectId") Long projectId, HttpSession session) {
        User currentUser = (User) session.getAttribute("userSession");

        if(currentUser == null) {
            return "redirect:/";
        }

        service.saveProjectUser(projectId, currentUser.getId());

        return "redirect:/dashboard";
    }

    @GetMapping("/leave/{projectId}")
    public String leave(@PathVariable("projectId") Long projectId, HttpSession session) {
        User currentUser = (User) session.getAttribute("userSession");

        if(currentUser == null) {
            return "redirect:/";
        }

        service.removeProjectUser(projectId, currentUser.getId());

        return "redirect:/dashboard";
    }

    @GetMapping("/edit/{projectId}")
    public String edit(@PathVariable("projectId") Long projectId, @ModelAttribute("project") Project project, Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("userSession");

        if(currentUser == null) {
            return "redirect:/";
        }

        Project projectEdit = service.findProject(projectId);
        if(currentUser.getId() != projectEdit.getLead().getId()) {
            return "redirect:/dashboard";
        }

        model.addAttribute("project", service.findProject(projectId));

        return "edit.jsp";
    }

    @PutMapping("/update")
    public String update(@Valid @ModelAttribute("project") Project project, BindingResult result, HttpSession session) {
        User currentUser = (User) session.getAttribute("userSession");

        if(currentUser == null) {
            return "redirect:/";
        }

        if(result.hasErrors()) {
            return "edit.jsp";
        } else {
            Project thisProject = service.findProject(project.getId());
            List<User> usersJoinedProject = thisProject.getUsersJoined();
            project.setUsersJoined(usersJoinedProject);
            service.saveProject(project);

            return "redirect:/dashboard";
        }
    }
}

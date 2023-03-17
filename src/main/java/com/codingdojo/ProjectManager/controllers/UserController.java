package com.codingdojo.ProjectManager.controllers;

import com.codingdojo.ProjectManager.models.LoginUser;
import com.codingdojo.ProjectManager.models.Project;
import com.codingdojo.ProjectManager.models.User;
import com.codingdojo.ProjectManager.services.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private AppService service;

    @GetMapping("/")
    public String index(@ModelAttribute("nuevoUsuario") User nuevoUsuario, @ModelAttribute("nuevoLogin") LoginUser nuevoLogin) {
        return "index.jsp";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("nuevoUsuario") User nuevoUsuario, BindingResult result, Model model, HttpSession session) {
        service.register(nuevoUsuario, result);

        if(result.hasErrors()) {
            model.addAttribute("nuevoLogin", new LoginUser());
            return "index.jsp";
        } else {
            session.setAttribute("userSession", nuevoUsuario);
            return "redirect:/dashboard";
        }
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("nuevoLogin") LoginUser nuevoLogin, BindingResult result, Model model, HttpSession session) {
        User user = service.login(nuevoLogin, result);

        if(result.hasErrors()) {
            model.addAttribute("nuevoUsuario", new User());
            return "index.jsp";
        }

        session.setAttribute("userSession", user);

        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("userSession");

        if(currentUser == null) {
            return "redirect:/";
        }

        List<Project> myProjects = service.findMyProjects(currentUser);
        List<Project> otherProjects = service.findOtherProjects(currentUser);

        model.addAttribute("myProjects", myProjects);
        model.addAttribute("otherProjects", otherProjects);

        return "dashboard.jsp";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }


}

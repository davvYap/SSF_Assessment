package ibf2022.batch2.ssf.frontcontroller.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import ibf2022.batch2.ssf.frontcontroller.model.Profile;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(path = "/protected/view1.html")
public class ProtectedController {

	// TODO Task 5
	// Write a controller to protect resources rooted under /protected
	@GetMapping
	public String getProtectedResource(HttpSession session, Model model, @ModelAttribute Profile profile) {
		Profile p = (Profile) session.getAttribute("profile");
		if (p == null || !p.isAuthenticated()) {
			model.addAttribute("profile", profile);
			return "view0";
		}
		return "view1";
	}

}

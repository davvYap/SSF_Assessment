package ibf2022.batch2.ssf.frontcontroller.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ibf2022.batch2.ssf.frontcontroller.model.Attemp;
import ibf2022.batch2.ssf.frontcontroller.model.Profile;
import ibf2022.batch2.ssf.frontcontroller.services.AuthenticationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping(path = "/")
public class FrontController {

	// TODO: Task 2, Task 3, Task 4, Task 6
	@Autowired
	private AuthenticationService aService;

	@GetMapping
	public String getLandingPage(Model model, @ModelAttribute Profile profile) {
		model.addAttribute("profile", profile);
		return "view0";
	}

	@PostMapping(path = "/login", consumes = "application/x-www-form-urlencoded", produces = "text/html")
	public String login(Model model, @Valid Profile profile, BindingResult binding, HttpSession session) {

		if (binding.hasErrors()) {
			model.addAttribute("profile", profile);
			return "view0";
		}

		if (aService.isLocked(profile.getUsername())) {
			model.addAttribute("loginName", profile.getUsername());
			return "View2";
		}

		Attemp attemps = (Attemp) session.getAttribute("attemps");
		if (attemps == null) {
			attemps = new Attemp();
			attemps.setAttemps(0);
			session.setAttribute("attemps", attemps);
		}
		System.out.println("\nattempss >>>>>>>>>>>>>" + attemps.getAttemps());
		if (attemps.getAttemps() < 3) {
			// check if the session contains profile
			Profile p = (Profile) session.getAttribute("profile");
			// IMPORTANT 1. initial case, when user first try to login
			if (p == null) {
				String username = profile.getUsername();
				String password = profile.getPassword();
				Boolean authenticated = false;
				try {
					authenticated = aService.authenticate(username, password);
					if (authenticated) {
						// IMPORTANT SUCCESS
						profile.setAuthenticated(true);
						session.setAttribute("profile", profile);
					}
				} catch (Exception e) {
					// IMPORTANT FAILED -> release captcha and set profile to fail
					List<ObjectError> errors = aService.validateAuthentication(authenticated);
					for (ObjectError objectError : errors) {
						binding.addError(objectError);
					}
					model.addAttribute("notAuthenticated", !authenticated);
					profile.setAuthenticated(false);
					session.setAttribute("profile", profile);

					int num1 = aService.getRandNum();
					int num2 = aService.getRandNum();
					String operator = aService.getOperator();
					System.out.println(aService.debug(num1, num2, operator));
					String phrase = aService.getPhrase(num1, num2, operator);
					int answer = aService.getCaptchaAnswer(num1, num2, operator);
					// IMPORTANT set the answer to session
					session.setAttribute("answer", answer);
					System.out.println("Answer >>>>>>>>>" + answer);
					model.addAttribute("phrase", phrase);
					model.addAttribute("profile", profile);
					attemps.addAttemps();

					return "view0";
				}
			}

			// IMPORTANT after 1st attempt to login
			if (p != null && !p.isAuthenticated()) {
				String username = profile.getUsername();
				String password = profile.getPassword();
				Integer captchaAnswer = profile.getCaptchaAnswer();
				Boolean authenticated = false;
				// IMPORTANT if captcha correct
				if (captchaAnswer == (Integer) session.getAttribute("answer")) {
					try {
						authenticated = aService.authenticate(username, password);
						if (authenticated) {
							profile.setAuthenticated(true);
							session.setAttribute("profile", profile);
						}
					} catch (Exception e) {
						List<ObjectError> errors = aService.validateAuthentication(authenticated);
						for (ObjectError objectError : errors) {
							binding.addError(objectError);
						}
						model.addAttribute("notAuthenticated", !authenticated);
						profile.setAuthenticated(false);
						session.setAttribute("profile", profile);
						int num1 = aService.getRandNum();
						int num2 = aService.getRandNum();
						String operator = aService.getOperator();
						System.out.println(aService.debug(num1, num2, operator));
						String phrase = aService.getPhrase(num1, num2, operator);
						int answer = aService.getCaptchaAnswer(num1, num2, operator);
						session.setAttribute("answer", answer);
						System.out.println("Answer >>>>>>>>>" + answer);
						model.addAttribute("phrase", phrase);
						model.addAttribute("profile", profile);
						attemps.addAttemps();

						return "view0";
					}
				} else {
					// IMPORTANT if captcha incorrect
					// display captcha error message and regenerate captcha
					List<ObjectError> cErrors = aService
							.validateAnswer(captchaAnswer == (Integer) session.getAttribute("answer"));
					for (ObjectError objectError : cErrors) {
						binding.addError(objectError);
					}
					model.addAttribute("notAuthenticated", !authenticated);
					profile.setAuthenticated(false);
					session.setAttribute("profile", profile);
					int num1 = aService.getRandNum();
					int num2 = aService.getRandNum();
					String operator = aService.getOperator();
					System.out.println(aService.debug(num1, num2, operator));
					String phrase = aService.getPhrase(num1, num2, operator);
					int answer = aService.getCaptchaAnswer(num1, num2, operator);
					session.setAttribute("answer", answer);
					System.out.println("Answer >>>>>>>>>" + answer);
					model.addAttribute("phrase", phrase);
					model.addAttribute("profile", profile);
					attemps.addAttemps();

					return "view0";
				}
			}
			return "view1";
		}
		aService.disableUser(profile.getUsername());
		model.addAttribute("loginName", profile.getUsername());
		return "view2";
	}

	@GetMapping(path = "/logout")
	public String logout(Model model, @ModelAttribute Profile profile, HttpSession session) {
		session.invalidate();
		model.addAttribute("profile", profile);
		return "view0";
	}
}

package ibf2022.batch2.ssf.frontcontroller.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import ibf2022.batch2.ssf.frontcontroller.model.Captcha;
import ibf2022.batch2.ssf.frontcontroller.respositories.AuthenticationRepository;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@Service
public class AuthenticationService {

	@Autowired
	private AuthenticationRepository aRepo;

	@Value("${chuck.auth.server.url}")
	private String CHUCK_SERVER_URL;

	// TODO: Task 2
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write the authentication method in here
	public boolean authenticate(String username, String password) throws Exception {
		// String url = "https://auth.chuklee.com/api/authenticate";
		String url = UriComponentsBuilder.fromUriString(CHUCK_SERVER_URL)
				.path("/api/authenticate")
				.toUriString();

		JsonObject profile = Json.createObjectBuilder()
				.add("username", username)
				.add("password", password)
				.build();

		RequestEntity req = RequestEntity.post(url)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.body(profile.toString(), String.class);

		RestTemplate template = new RestTemplate();

		ResponseEntity<String> res = template.exchange(req, String.class);

		String body = res.getBody();
		System.out.println("Body >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + body);

		System.out.println("HTTP STATUS CODE >>>>>>>>>>>" + res.getStatusCode());
		if (res.getStatusCode() == HttpStatus.CREATED) {
			return true;
		}
		return false;
	}

	// TODO: Task 3
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write an implementation to disable a user account for 30 mins
	public void disableUser(String username) {
		aRepo.disableUser(username);
	}

	// TODO: Task 5
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write an implementation to check if a given user's login has been disabled
	public boolean isLocked(String username) {
		System.out.println("User %s is locked".formatted(username));
		return aRepo.isLocked(username);
	}

	public List<ObjectError> validateAuthentication(Boolean bool) {
		List<ObjectError> errors = new LinkedList<>();
		FieldError fe;
		if (!bool) {
			fe = new FieldError("profile", "password", "Incorrect username or password");
			errors.add(fe);
		}
		return errors;
	}

	// IMPORTANT All method moved to Captcha class
	// public int getRandNum() {
	// Random rand = new Random();
	// int randInt = rand.nextInt(1, 51);
	// return randInt;
	// }

	// public String getOperator() {
	// String[] mathOperators = { "+", "-", "*", "/" };
	// Random rand = new Random();
	// int randIndex = rand.nextInt(0, 4);
	// return mathOperators[randIndex];

	// }

	// public String getPhrase(int num1, int num2, String operator) {
	// String phrase = "";
	// if (num1 > num2) {
	// phrase = "What is " + num1 + " " + operator + " " + num2 + "?";
	// } else {
	// phrase = "What is " + num2 + " " + operator + " " + num1 + "?";
	// }
	// return phrase;
	// }

	// public String getPhrase(Captcha c) {
	// String phrase = "";
	// if (c.getFirstNum() > c.getSecNum()) {
	// phrase = "What is %d %s %d?".formatted(c.getFirstNum(), c.getOperator(),
	// c.getSecNum());
	// } else {
	// phrase = "What is %d %s %d?".formatted(c.getSecNum(), c.getOperator(),
	// c.getFirstNum());
	// }
	// return phrase;
	// }

	// public int getCaptchaAnswer(int num1, int num2, String operator) {
	// int answer = 0;
	// if (num1 > num2) {
	// switch (operator) {
	// case "+":
	// answer = num1 + num2;
	// break;
	// case "-":
	// answer = num1 - num2;
	// break;
	// case "*":
	// answer = num1 * num2;
	// break;
	// case "/":
	// answer = num1 / num2;
	// break;
	// default:
	// answer = 0;
	// }
	// } else {
	// switch (operator) {
	// case "+":
	// answer = num2 + num1;
	// break;
	// case "-":
	// answer = num2 - num1;
	// break;
	// case "*":
	// answer = num2 * num1;
	// break;
	// case "/":
	// answer = num2 / num1;
	// break;
	// default:
	// answer = 0;
	// }
	// }
	// return answer;
	// }

	public List<ObjectError> validateAnswer(Boolean bool) {
		List<ObjectError> errors = new LinkedList<>();
		FieldError fe;
		if (!bool) {
			fe = new FieldError("profile", "captchaAnswer", "Incorrect captcha asnwer");
			errors.add(fe);
		}
		return errors;
	}

	public String debug(int num1, int num2, String operator) {
		return "num1 >>> %d\nnum2 >>> %d\noperator >>> %s".formatted(num1, num2, operator);
	}
}

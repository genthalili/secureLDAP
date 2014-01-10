package co.ntier.secureLDAP.controller;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.ldap.NamingException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import co.ntier.secureLDAP.models.Group;
import co.ntier.secureLDAP.models.User;
import co.ntier.secureLDAP.services.OdmGroupService;
import co.ntier.secureLDAP.services.OdmUserService;

@Controller
public class HomeController {

	@Autowired
	private OdmUserService userService;

	@Autowired
	private OdmGroupService groupService;

	private String defaultView = "users";
	private String errorMsg;

	// Source :
	// http://www.java2s.com/Code/Java/Security/GeneratearandomStringsuitableforuseasatemporarypassword.htm
	private static final Random RANDOM = new SecureRandom();
	/** Length of password. @see #generateRandomPassword() */
	public static final int PASSWORD_LENGTH = 8;

	/**
	 * Generate a random String suitable for use as a temporary password.
	 * 
	 * @return String suitable for use as a temporary password
	 */
	public static String generateRandomPassword() {
		// Pick from some letters that won't be easily mistaken for each
		// other. So, for example, omit o O and 0, 1 l and L.
		String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789+@";

		String pw = "";
		for (int i = 0; i < PASSWORD_LENGTH; i++) {
			int index = (int) (RANDOM.nextDouble() * letters.length());
			pw += letters.substring(index, index + 1);
		}
		return pw;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView test(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		ModelAndView view = new ModelAndView();

		if (request.isUserInRole("ROLE_ADMINISTRATOR")
				|| request.isUserInRole("ROLE_HELPDESK")
				|| request.isUserInRole("ROLE_MANAGER")) {
			view.setViewName("securite/home");

			List<User> users = new ArrayList<User>();
			if (request.getParameter("fullname") == null
					|| request.getParameter("fullname") == "") {
				users = userService.findAll();
			} else {
				User u = userService.findByCn(request.getParameter("fullname"));
				users.add(u);
			}
			view.addObject("users", users);

			List<Group> groups = groupService.findAll();
			view.addObject("groups", groups);

			view.addObject("defaultView", defaultView);
			view.addObject("errorMsg", errorMsg);

			errorMsg = "";
		} else {
			view.setViewName("login");
		}
		return view;
	}

	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public String testaddUser(HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			InvalidNameException {
		if (request.isUserInRole("ROLE_ADMINISTRATOR")
				|| request.isUserInRole("ROLE_HELPDESK")) {
			errorMsg = "";
			defaultView = "users";

			User u = new User(request.getParameter("fullname"),
					request.getParameter("surname"),
					request.getParameter("password"));
			try {
				userService.create(u);
			} catch (NamingException e) {
				errorMsg = this.getErrorLDAP(e);
			}

		}

		return "redirect:/";
	}

	@RequestMapping(value = "/addGroup", method = RequestMethod.POST)
	public String addGroup(HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			InvalidNameException {

		if (request.isUserInRole("ROLE_ADMINISTRATOR")
				|| request.isUserInRole("ROLE_MANAGER")) {

			errorMsg = "";
			defaultView = "groups";

			try {
				User u = userService.findByCn(request.getParameter("fullname"));

				Group g = new Group(request.getParameter("groupname"));
				g.setMembers(new HashSet<Name>());
				g.addMember(u.getDn());

				groupService.create(g);
			} catch (NamingException e) {
				errorMsg = this.getErrorLDAP(e);
			}catch (EmptyResultDataAccessException e) {
				errorMsg = "Could not find fullname.";
			}
		}

		return "redirect:/";
	}

	@RequestMapping(value = "/addUserToGroup", method = RequestMethod.POST)
	public String addUserToGroup(HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			InvalidNameException {
		if (request.isUserInRole("ROLE_ADMINISTRATOR")
				|| request.isUserInRole("ROLE_HELPDESK")
				|| request.isUserInRole("ROLE_MANAGER")) {
			errorMsg = "";
			defaultView = "groups";

			try {
				User u = userService.findByCn(request.getParameter("fullname"));

				Group g = groupService.findByCn(request
						.getParameter("groupname"));

				g.addMember(u.getDn());
				System.out.println(u.getDn());

				groupService.update(g);
			} catch (NamingException e) {
				errorMsg = this.getErrorLDAP(e);
			}catch (EmptyResultDataAccessException e) {
				errorMsg = "Could not find fullname/groupname.";
			}
		}
		return "redirect:/";
	}

	@RequestMapping(value = "/removeUserFromGroup", method = RequestMethod.POST)
	public String removeUserFromGroup(HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			InvalidNameException {
		if (request.isUserInRole("ROLE_ADMINISTRATOR")
				|| request.isUserInRole("ROLE_HELPDESK")
				|| request.isUserInRole("ROLE_MANAGER")) {
			errorMsg = "";
			defaultView = "groups";

			try {
				
				User u = userService.findByCn(request.getParameter("fullname"));

				Group g = groupService.findByCn(request
						.getParameter("groupname"));

				g.removeMember(u.getDn().addAll(0, groupService.getBasePath()));

				groupService.update(g);

			} catch (NamingException e) {
				errorMsg = this.getErrorLDAP(e);
				if(errorMsg.contains("ERR_279")){
					errorMsg = "List of members could not be empty.";
				}
			} catch (EmptyResultDataAccessException e) {
				errorMsg = "Could not find fullname/groupname.";
			}
		}
		return "redirect:/";
	}

	@RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
	public String deleteUser(HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			InvalidNameException {
		if (request.isUserInRole("ROLE_ADMINISTRATOR")) {
			errorMsg = "";
			defaultView = "users";

			try {
				User u = userService.findByCn(request.getParameter("fullname"));

				
				List<Group> groups = groupService.findAll();

				for (Group group : groups) {
					group.removeMember(u.getDn());
					groupService.update(group);
				}

				userService.delete(u);
			} catch (NamingException e) {
				errorMsg = this.getErrorLDAP(e);
			} catch (EmptyResultDataAccessException e) {
				errorMsg = "Could not find fullname.";
			}
		}
		return "redirect:/";

	}

	@RequestMapping(value = "/deleteGroup", method = RequestMethod.GET)
	public String deleteGroup(HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			InvalidNameException {
		if (request.isUserInRole("ROLE_ADMINISTRATOR")
				|| request.isUserInRole("ROLE_MANAGER")) {
			errorMsg = "";
			defaultView = "groups";

			try {
				Group g = groupService.findByCn(request
						.getParameter("groupname"));

				groupService.delete(g);
			} catch (NamingException e) {
				errorMsg = this.getErrorLDAP(e);
			}
		}
		return "redirect:/";

	}

	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public String updateUser(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (request.isUserInRole("ROLE_ADMINISTRATOR")
				|| request.isUserInRole("ROLE_HELPDESK")) {
			errorMsg = "";
			defaultView = "users";

			try {
				User u = userService.findByCn(request.getParameter("cn"));

				u.setFullName(request.getParameter("fullname"));
				u.setSurName(request.getParameter("surname"));

				if (request.getParameter("password") != "") {
					u.setPassword(request.getParameter("password"));
				}

				userService.update(u);
			} catch (NamingException e) {
				errorMsg = this.getErrorLDAP(e);
			}
		}

		return "redirect:/";

	}

	@RequestMapping(value = "/updateGroup", method = RequestMethod.POST)
	public String updateGroup(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (request.isUserInRole("ROLE_ADMINISTRATOR")
				|| request.isUserInRole("ROLE_MANAGER")) {
			errorMsg = "";
			defaultView = "groups";

			try {
				Group g = groupService.findByCn(request.getParameter("cn"));
				System.out.println(g.toString());
				
				g.setName(request.getParameter("groupname"));
				
				groupService.update(g);
			} catch (NamingException e) {
				errorMsg = this.getErrorLDAP(e);
			} catch (EmptyResultDataAccessException e) {
				errorMsg = "Could not find groupname.";
			}
		}
		return "redirect:/";

	}

	@RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
	public String resetPassword(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (request.isUserInRole("ROLE_ADMINISTRATOR")
				|| request.isUserInRole("ROLE_HELPDESK")) {
			errorMsg = "";
			defaultView = "users";

			try {
				User u = userService.findByCn(request.getParameter("fullname"));
				String password = generateRandomPassword();
				u.setPassword(password);
				userService.update(u);
				errorMsg = "New password is : " + password;
			} catch (NamingException e) {
				errorMsg = this.getErrorLDAP(e);
			}
		}
		return "redirect:/";

	}

	private String getErrorLDAP(NamingException e) {
		String message = e.getRootCause().getMessage();
		String[] words = message.split("\n");

		String error_msg = words[words.length - 1];
		error_msg = error_msg.substring(2, error_msg.length() - 1);
		error_msg.trim();

		return error_msg;

	}

}

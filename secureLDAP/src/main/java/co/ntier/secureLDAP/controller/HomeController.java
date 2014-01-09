package co.ntier.secureLDAP.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
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
	private String errorMsg = "";
	private String defaultPassword = "abc123456";

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView test(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (request.isUserInRole("ROLE_ADMINISTRATOR")) {
			ModelAndView view = new ModelAndView("securite/admin");

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

			return view;
		}
		return new ModelAndView("login");

	}

	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public String testaddUser(HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			InvalidNameException {

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

		return "redirect:/";
	}

	@RequestMapping(value = "/addGroup", method = RequestMethod.POST)
	public String addGroup(HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			InvalidNameException {

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
		}

		return "redirect:/";
	}

	@RequestMapping(value = "/addUserToGroup", method = RequestMethod.POST)
	public String addUserToGroup(HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			InvalidNameException {

		errorMsg = "";
		defaultView = "groups";

		try {
			User u = userService.findByCn(request.getParameter("fullname"));

			Group g = groupService.findByCn(request.getParameter("groupname"));

			g.addMember(u.getDn());

			groupService.update(g);
		} catch (NamingException e) {
			errorMsg = this.getErrorLDAP(e);
		}

		return "redirect:/";
	}

	@RequestMapping(value = "/removeUserFromGroup", method = RequestMethod.POST)
	public String removeUserFromGroup(HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			InvalidNameException {

		errorMsg = "";
		defaultView = "groups";

		try {
			User u = userService.findByCn(request.getParameter("fullname"));

			Group g = groupService.findByCn(request.getParameter("groupname"));

			g.removeMember(u.getDn());

			groupService.update(g);
		} catch (NamingException e) {
			errorMsg = this.getErrorLDAP(e);
		}

		return "redirect:/";
	}

	@RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
	public String deleteUser(HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			InvalidNameException {

		errorMsg = "";
		defaultView = "users";

		try {
			User u = userService.findByCn(request.getParameter("fullname"));

			userService.delete(u);
		} catch (NamingException e) {
			errorMsg = this.getErrorLDAP(e);
		}

		return "redirect:/";

	}

	@RequestMapping(value = "/deleteGroup", method = RequestMethod.GET)
	public String deleteGroup(HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			InvalidNameException {

		errorMsg = "";
		defaultView = "groups";

		try {
			Group g = groupService.findByCn(request.getParameter("groupname"));

			groupService.delete(g);
		} catch (NamingException e) {
			errorMsg = this.getErrorLDAP(e);
		}

		return "redirect:/";

	}

	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public String updateUser(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		errorMsg = "";
		defaultView = "users";

		try {
			User u = userService.findByCn(request.getParameter("cn"));

			// TODO dn string to name
			// u.setDn(request.getParameter("dn"));
			u.setFullName(request.getParameter("fullname"));
			u.setSurName(request.getParameter("surname"));

			if (request.getParameter("password") != "") {
				u.setPassword(request.getParameter("password"));
			}

			userService.update(u);
		} catch (NamingException e) {
			errorMsg = this.getErrorLDAP(e);
		}

		return "redirect:/";

	}

	@RequestMapping(value = "/updateGroup", method = RequestMethod.POST)
	public String updateGroup(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		errorMsg = "";
		defaultView = "groups";

		try {
			Group g = groupService.findByCn(request.getParameter("cn"));

			// TODO dn string to name
			// g.setDn(request.getParameter("dn"));
			// g.setName(request.getParameter("groupname"));

			groupService.update(g);
		} catch (NamingException e) {
			errorMsg = this.getErrorLDAP(e);
		}

		return "redirect:/";

	}

	@RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
	public String resetPassword(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		errorMsg = "";
		defaultView = "users";

		try {
			User u = userService.findByCn(request.getParameter("fullname"));

			u.setPassword(defaultPassword);
			userService.update(u);
		} catch (NamingException e) {
			errorMsg = this.getErrorLDAP(e);
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

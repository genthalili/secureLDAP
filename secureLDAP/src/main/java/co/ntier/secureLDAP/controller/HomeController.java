package co.ntier.secureLDAP.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.NamingException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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

	@RequestMapping(value = "/")
	public ModelAndView test(HttpServletResponse response) throws IOException {
		return new ModelAndView("home");
	}

	@RequestMapping(value = "/securite")
	public ModelAndView testSecurite(HttpServletResponse response)
			throws IOException {

		return new ModelAndView("securite/secure_page");

	}

	@RequestMapping(value = "/test")
	public ModelAndView testLDAP(HttpServletResponse response)
			throws IOException {

		List<User> a = userService.findAll();
		
		for (User user : a) {
			System.out.println(user);
		}
		
		List<Group> g = groupService.findAll();
		
		for (Group group : g) {
			Set<Name> s= group.getMembers();
			
			for (Name user2 : s) {
				//System.out.println(userService.fi);
			}
			
		}

		return null;

	}
	

	@RequestMapping(value = "/addUser")
	public ModelAndView testaddUser(HttpServletResponse response)
			throws IOException, InvalidNameException {

		User u = new User( "etrit.halili", "etrit", "hahahaha");
		try {
			userService.create(u);
		} catch (NamingException e) {
//			String message = e.getExplanation();
//			String[] words = message.split(" ");
//			for (String string : words) {
//				System.out.println(string.trim());
//			}
			System.out.println(e.getExplanation());
		}
		
		return null;
		

	}
	
	@RequestMapping(value = "/addGroup")
	public ModelAndView testaddGroup(HttpServletResponse response)
			throws IOException, InvalidNameException {

		User u = userService.findByCn("etrit.halili");
		
		Group g = new Group("test02");
		g.setMembers(new HashSet<Name>());
		g.addMember(u.getDn());
		
		groupService.create(g);
		
		return null;
		

	}
	
	@RequestMapping(value = "/addMemberToAdmin")
	public ModelAndView testaddMemberToAdmin(HttpServletResponse response)
			throws IOException, InvalidNameException {

		User u = userService.findByCn("etrit.halili");
		
		Group g = groupService.findByCn("administrator");
		
		g.addMember(u.getDn());
		
		groupService.update(g);
		
		return null;
		

	}
	
	@RequestMapping(value = "/find")
	public String testfind(HttpServletResponse response)
			throws IOException {
		User u = userService.findByCn("etrit.halili");
		System.out.println(u);
		
		
		return "login";
		

	}
	
	@RequestMapping(value = "/delete")
	public ModelAndView testdelete(HttpServletResponse response)
			throws IOException, InvalidNameException {
		
		User u = userService.findByCn("etrit.halili");

		userService.delete(u);
		
		
		return null;
	
	}
	
	@RequestMapping(value = "/update")
	public ModelAndView testUpdate(HttpServletResponse response)
			throws IOException {
		
		User u = userService.findByCn("etrit.halili");
		
		u.setSurName("Etriti");
		u.setPassword("bali");
		try{
		userService.update(u);
			} catch (NamingException e) {
//				String message = e.getExplanation();
//				String[] words = message.split(" ");
//				for (String string : words) {
//					System.out.println(string.trim());
//				}
				System.out.println(e.getRootCause().getMessage());
			}
			
			return null;
		

	
	}
	
	
	
	
	
}

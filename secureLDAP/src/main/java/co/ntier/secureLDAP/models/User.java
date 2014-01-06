package co.ntier.secureLDAP.models;

import java.util.List;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.ldap.Rdn;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.odm.annotations.Transient;
import org.springframework.ldap.support.LdapNameBuilder;

@Entry(objectClasses = { "organizationalPerson", "person", "top" }, base = "ou=users")
public class User {

	@Id
	private Name dn;

	@Attribute(name = "cn")
	@DnAttribute(value = "cn", index = 1)
	private String fullName;

	@Attribute(name = "sn")
	private String surName;

	@Attribute(name = "userPassword")
	private String password;

	@Transient
	private List<Rdn> fulldn;

	public User() {
	};

	public User(String s) {
		super();
		User u = new User();
		
		this.dn = u.getDn();
		this.fullName = u.getFullName();
		this.surName = u.getSurName();
		this.password = u.getPassword();

	};

	public User(Name dn, String fullName, String surName, String password) {
		super();
		this.dn = dn;
		this.fullName = fullName;
		this.surName = surName;
		this.password = password;
	}

	public User(String fullName, String surName, String password)
			throws InvalidNameException {

		String base = this.getClass().getAnnotation(Entry.class).base();

		this.fullName = fullName;
		this.surName = surName;
		this.password = password;
		
		this.dn = LdapNameBuilder.newLdapName("cn="+fullName+","+base).build();
		
		
	}

	public Name getDn() {
		return dn;
	}

	public void setDn(Name dn) {
		this.dn = dn;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getSurName() {
		return surName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [dn=" + dn + ", fullName=" + fullName + ", surName="
				+ surName + ", password=" + password + "]";
	}


	

}

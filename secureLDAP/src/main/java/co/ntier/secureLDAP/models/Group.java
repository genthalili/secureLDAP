package co.ntier.secureLDAP.models;

import java.util.Set;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.ldap.LdapName;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;


@Entry(objectClasses = {"top", "groupOfNames"}, base = "ou=groups")
public class Group {

    @Id
    private Name dn;

    @Attribute(name="cn")
    @DnAttribute("cn")
    private String name;

    @Attribute(name="member")
    private Set<Name> members;
    
    
    
    public Group(){}
    
    public Group(String name) throws InvalidNameException {
		super();
		String base = this.getClass().getAnnotation(Entry.class).base();
		this.dn = new LdapName("cn=" + name + "," + base);
		
		this.name = name;
	}

	public Name getDn() {
        return dn;
    }

    public void setDn(Name dn) {
        this.dn = dn;
    }

    public Set<Name> getMembers() {
        return members;
    }

    public void setMembers(Set<Name> members) {
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addMember(Name member) {
        members.add(member);
    }

    public void removeMember(Name member) {
        members.remove(member);
    }

	@Override
	public String toString() {
		return "Group [dn=" + dn + ", name=" + name + ", members=" + members
				+ "]";
	}
    
    
}
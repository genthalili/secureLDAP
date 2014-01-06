package co.ntier.secureLDAP.DAO;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Repository;

import co.ntier.secureLDAP.models.Group;

@Repository
public class OdmGroupDaoImpl implements OdmGroupDao {

	@Autowired
	private LdapTemplate ldapTemplate;
	
	
	

	@Override
	public Group create(Group group) {
		
		ldapTemplate.create(group);

		return group;
	}

	@Override
	public Group findByCn(String cn) {
		return ldapTemplate.findOne( query().where("cn").is(cn) , Group.class);
	}

	@Override
	public void update(Group group) {
		ldapTemplate.update(group);
		

	}

	@Override
	public void delete(Group group) {
		
		ldapTemplate.delete(group);

	}

	@Override
	public List<Group> findAll() {
		return ldapTemplate.findAll(Group.class);
	}


}

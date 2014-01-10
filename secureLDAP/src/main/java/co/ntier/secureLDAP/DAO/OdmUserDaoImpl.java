package co.ntier.secureLDAP.DAO;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Repository;

import co.ntier.secureLDAP.models.User;

@Repository
public class OdmUserDaoImpl implements OdmUserDao {

	@Autowired
	private LdapTemplate ldapTemplate;

	@Override
	public User create(User user) {
		
		ldapTemplate.create(user);

		return user;
	}

	@Override
	public User findByCn(String cn) {
		return ldapTemplate.findOne( query().where("cn").is(cn) , User.class);
	}

	@Override
	public void update(User user) {
		ldapTemplate.update(user);
		

	}

	@Override
	public void delete(User user) {
		
		ldapTemplate.delete(user);

	}

	@Override
	public List<User> findAll() {
		return ldapTemplate.findAll(User.class);
	}

	@Override
	public List<User> findByLastName(String lastName) {
		return ldapTemplate.find(query().where("sn").is(lastName), User.class);
	}

	@Override
	public List<User> findLikeCn(String cn) {
		return ldapTemplate.find(query().where("cn").like(cn), User.class);
	}

}

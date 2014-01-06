package co.ntier.secureLDAP.DAO;

import java.util.List;

import co.ntier.secureLDAP.models.User;

public interface OdmUserDao {

	public User create(User user);
	
	public User findByCn(String cn);

	public void update(User user);

	public void delete(User user);

	public List<User> findAll();

	public List<User> findByLastName(String lastName);

}

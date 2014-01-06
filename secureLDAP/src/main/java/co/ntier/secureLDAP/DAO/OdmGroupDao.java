package co.ntier.secureLDAP.DAO;

import java.util.List;

import co.ntier.secureLDAP.models.Group;

public interface OdmGroupDao {

	public Group create(Group group);
	
	public Group findByCn(String cn);

	public void update(Group group);

	public void delete(Group group);

	public List<Group> findAll();

}

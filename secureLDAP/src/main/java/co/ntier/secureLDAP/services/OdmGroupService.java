package co.ntier.secureLDAP.services;

import java.util.List;

import co.ntier.secureLDAP.models.Group;

public interface OdmGroupService {

	public Group create(Group group);
	
	public Group findByCn(String cn);

	public void update(Group group);

	public void delete(Group group);

	public List<Group> findAll();

}

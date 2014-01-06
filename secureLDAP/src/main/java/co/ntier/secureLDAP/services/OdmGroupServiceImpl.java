package co.ntier.secureLDAP.services;

import java.util.List;
import java.util.Set;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.ldap.LdapName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.support.BaseLdapNameAware;
import org.springframework.stereotype.Service;

import co.ntier.secureLDAP.DAO.OdmGroupDao;
import co.ntier.secureLDAP.models.Group;

@Service
public class OdmGroupServiceImpl implements OdmGroupService, BaseLdapNameAware {

	@Autowired
	private OdmGroupDao odmGroupDao;

	private LdapName basePath;

	@Override
	public Group create(Group group) {
		// add baseLdapPath
		Set<Name> mb = group.getMembers();

		for (Name name : mb) {
			try {
				name.addAll(0, basePath);
			} catch (InvalidNameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return odmGroupDao.create(group);
	}

	@Override
	public Group findByCn(String cn) {
		return odmGroupDao.findByCn(cn);
	}

	@Override
	public void update(Group group) {

		// add baseLdapPath
		Set<Name> mb = group.getMembers();

		for (Name name : mb) {

			try {
				if (!name.toString().contains(basePath.toString())) {
					name.addAll(0, basePath);
				}
			} catch (InvalidNameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		odmGroupDao.update(group);
	}

	@Override
	public void delete(Group group) {
		odmGroupDao.delete(group);

	}

	@Override
	public List<Group> findAll() {
		return odmGroupDao.findAll();
	}

	@Override
	public void setBaseLdapPath(LdapName baseLdapPath) {
		this.basePath = baseLdapPath;

	}

}

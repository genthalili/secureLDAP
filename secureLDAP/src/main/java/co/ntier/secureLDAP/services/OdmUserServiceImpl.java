package co.ntier.secureLDAP.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.ntier.secureLDAP.DAO.OdmUserDao;
import co.ntier.secureLDAP.models.User;

@Service
public class OdmUserServiceImpl implements OdmUserService {

	@Autowired
	private OdmUserDao odmUserDao;


	@Override
	public User create(User user) {
		return odmUserDao.create(user);
	}

	@Override
	public User findByCn(String cn) {
		return odmUserDao.findByCn(cn);
	}

	@Override
	public void update(User user) {
		odmUserDao.update(user);
	}

	@Override
	public void delete(User user) {
		odmUserDao.delete(user);

	}

	@Override
	public List<User> findAll() {
		return odmUserDao.findAll();
	}

	@Override
	public List<User> findByLastName(String lastName) {
		return odmUserDao.findByLastName(lastName);
	}

	
	

}

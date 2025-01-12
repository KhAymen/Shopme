package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;

	
	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userWithOneRole = new User("aymen@khay.com", "ay2025", "Aymen", "Khelifi");
		userWithOneRole.addRole(roleAdmin);
		
		User savedUser = repo.save(userWithOneRole);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewUserWithTwoRoles() {
		User userWithTwoRole = new User("a@b.com", "ab2025", "ABCDE", "EFGHIJKL");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		
		userWithTwoRole.addRole(roleEditor);
		userWithTwoRole.addRole(roleAssistant);
		
		User savedUser = repo.save(userWithTwoRole);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));
	}
	
	@Test
	public void testGetUserById() {
		User userById = repo.findById(1).get();
		System.out.println(userById);
		assertThat(userById).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		User updatedUser = repo.findById(1).get();
		updatedUser.setEnabled(true);
		updatedUser.setEmail("aykhelifi@khay.com");
		
		repo.save(updatedUser);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User updatedUserRoles = repo.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(2);
		
		updatedUserRoles.getRoles().remove(roleEditor);
		updatedUserRoles.addRole(roleSalesperson);
		
		repo.save(updatedUserRoles);
	}
	
	@Test
	public void testDeleteUser() {
		Integer deletedUser = 2;
		repo.deleteById(deletedUser);
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "a@b.com";
		User userByEmail = repo.getUserByEmail(email);
		
		assertThat(userByEmail).isNotNull();
	}
	
	@Test
	public void testCountById() {
		Integer id = 1;
		Long countById = repo.countById(id);
		
		assertThat(countById).isNotNull().isGreaterThan(0);
	}
	
	@Test
	public void testDisableUser() {
		Integer id = 1;
		repo.updateEnabledStatus(id, false);
	}
	
	@Test
	public void testEnableUser() {
		Integer id = 3;
		repo.updateEnabledStatus(id, true);
	}


}

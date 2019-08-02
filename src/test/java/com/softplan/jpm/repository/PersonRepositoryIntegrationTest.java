package com.softplan.jpm.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.softplan.jpm.entities.Person;
import com.softplan.jpm.jpa.repository.CustomPersonRepository;
import com.softplan.jpm.main.GestorProcessosJudiciaisApplication;

//@RunWith(SpringRunner.class)
//@ContextConfiguration(classes = GestorProcessosJudiciaisApplication.class)
//@DataJpaTest
//@SpringBootTest
public class PersonRepositoryIntegrationTest {

	//@Autowired
    private TestEntityManager entityManager;
 
    //@Autowired
    private CustomPersonRepository customPersonRepository;
 
    //@Test
	public void whenFindByName_thenReturnEmployee() {
		// given
		Person person = new Person("alex", "alex@gmail.com","222222222");
		entityManager.persist(person);
		entityManager.flush();
		
		Person personToFind = new Person();
		personToFind.setName("alex");

		// when
		Page<Person> foundPerson = customPersonRepository.findPerson(personToFind,0,null);		
		
		// then
		//AssertTrue(foundPerson.getContent().get(0).getName().equals(person.getName()));
	}
}

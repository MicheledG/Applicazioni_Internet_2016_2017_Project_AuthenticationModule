package it.polito.ai.authenticationmodule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.polito.ai.authenticationmodule.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

	public Account findOneByUsername(String username);

}

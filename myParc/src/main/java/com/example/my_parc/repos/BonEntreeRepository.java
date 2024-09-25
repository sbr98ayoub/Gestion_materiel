package com.example.my_parc.repos;

import com.example.my_parc.domain.BonEntree;
import com.example.my_parc.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BonEntreeRepository extends JpaRepository<BonEntree, Long> {

    BonEntree findFirstByUserId(User user);

}

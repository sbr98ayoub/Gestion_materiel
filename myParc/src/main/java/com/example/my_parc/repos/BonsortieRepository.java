package com.example.my_parc.repos;

import com.example.my_parc.domain.Bonsortie;
import com.example.my_parc.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BonsortieRepository extends JpaRepository<Bonsortie, Long> {

    Bonsortie findFirstByUserId(User user);

}

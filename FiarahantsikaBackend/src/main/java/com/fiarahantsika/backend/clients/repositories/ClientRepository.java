package com.fiarahantsika.backend.clients.repositories;

import com.fiarahantsika.backend.clients.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository   extends JpaRepository<Client,Long> {

}

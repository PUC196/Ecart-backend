package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.pojo.Address;

public interface AddressRepository extends JpaRepository<Address,Long>{

}

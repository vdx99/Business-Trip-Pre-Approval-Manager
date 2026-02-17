package com.vdx.BTR.repository;

import com.vdx.BTR.model.BusinessTripRequest;
import com.vdx.BTR.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusinessTripRequestRepository extends JpaRepository<BusinessTripRequest, Long> {

    List<BusinessTripRequest> findByUser(User user);
}
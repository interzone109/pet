package ua.squirrel.user.service.store.consignment;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.user.entity.store.consignment.Consignment;

public interface ConsignmentRepository extends JpaRepository<Consignment, Long> {

}

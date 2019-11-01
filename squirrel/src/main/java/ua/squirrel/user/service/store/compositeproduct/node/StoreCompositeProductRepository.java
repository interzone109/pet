package ua.squirrel.user.service.store.compositeproduct.node;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.user.entity.store.compositeproduct.node.StoreCompositeProductNode;

public interface StoreCompositeProductRepository extends JpaRepository<StoreCompositeProductNode, Long> {

}

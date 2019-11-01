package ua.squirrel.user.service.product.node;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.user.entity.product.composite.CompositeProduct;
import ua.squirrel.user.entity.product.node.ProductMap;

public interface ProductMapRepository extends JpaRepository<ProductMap, Long> {
	ProductMap findOneByIdAndCompositeProduct(Long id, CompositeProduct compositeProduct);
}

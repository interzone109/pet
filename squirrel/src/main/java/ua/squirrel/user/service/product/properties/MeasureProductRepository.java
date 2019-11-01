package ua.squirrel.user.service.product.properties;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.user.entity.product.MeasureProduct;

public interface MeasureProductRepository extends JpaRepository<MeasureProduct, Long> {
	public MeasureProduct findOneByMeasure(String measure);
}

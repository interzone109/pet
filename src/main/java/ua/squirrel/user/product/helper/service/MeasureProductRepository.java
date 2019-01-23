package ua.squirrel.user.product.helper.service;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.squirrel.user.product.entity.MeasureProduct;

public interface MeasureProductRepository extends JpaRepository<MeasureProduct, Long> {
	public MeasureProduct findOneByMeasure(String measure);
}

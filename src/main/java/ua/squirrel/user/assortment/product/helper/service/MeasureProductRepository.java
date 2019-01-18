package ua.squirrel.user.assortment.product.helper.service;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.squirrel.user.assortment.product.helper.MeasureProduct;

public interface MeasureProductRepository extends JpaRepository<MeasureProduct, Long> {
	public MeasureProduct findOneByMeasure(String measure);
}

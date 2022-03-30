package cn.edu.bit.secondclass.repository;

import cn.edu.bit.secondclass.domain.Product;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    @Query(nativeQuery = true, value = "SELECT * FROM `product`")
    List<Product> findAllProducts();

    @Query(nativeQuery = true, value = "SELECT * FROM `product` WHERE `name` = :name")
    Product findProductByName(@Param("name") String name);

    @Query(nativeQuery = true, value = "SELECT * FROM `product` WHERE `stock` > :stock")
    List<Product> findProductByStock(@Param("stock") int stock);

    @Query(nativeQuery = true, value = "SELECT * FROM `product` WHERE `stock` > :stock AND `point` <= :point")
    List<Product> findProductByStockAndPoint(@Param("stock") int stock, @Param("point") Integer point);

    Product findProductById(int id);
}

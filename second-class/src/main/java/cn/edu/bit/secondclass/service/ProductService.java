package cn.edu.bit.secondclass.service;

import cn.edu.bit.secondclass.domain.PointExchangedRecord;
import cn.edu.bit.secondclass.domain.Product;
import cn.edu.bit.secondclass.repository.ProductRepository;
import cn.edu.bit.secondclass.vo.PointExchangedRecordInfoVo;
import cn.edu.bit.secondclass.vo.ProductInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import java.util.List;

public interface ProductService {

    List<ProductInfoVo> getAllProducts();

    Product getProductByName(String name);

    List<ProductInfoVo> getProductsInStock();

    List<ProductInfoVo> getConvertibleProduct(int userId);

    void exchangeProduct(PointExchangedRecordInfoVo record);

    void cancel(int id);
}

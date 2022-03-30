package cn.edu.bit.secondclass.controller;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.secondclass.domain.Product;
import cn.edu.bit.secondclass.service.ProductService;
import cn.edu.bit.secondclass.vo.PointExchangedRecordInfoVo;
import cn.edu.bit.secondclass.vo.ProductInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/all")
    public CommonResult queryAllProduct() {
        List<ProductInfoVo> products = productService.getAllProducts();
        return CommonResult.ok(ResultCode.SUCCESS)
                .data("products", products);
    }

    @GetMapping("/name/{name}")
    public CommonResult queryProductByName(@PathVariable(name = "name") String name) {
        Product product = productService.getProductByName(name);
        return CommonResult.ok(ResultCode.SUCCESS)
                .data("product", product);
    }

    @GetMapping("/stocked")
    public CommonResult queryProductInStock() {
        List<ProductInfoVo> products =  productService.getProductsInStock();
        return CommonResult.ok(ResultCode.SUCCESS)
                .data("products", products);
    }


    @GetMapping("/convertible/{id}")
    public CommonResult queryConvertibleProduct(@PathVariable(name = "id") int id) {
        List<ProductInfoVo> products = productService.getConvertibleProduct(id);
        return CommonResult.ok(ResultCode.SUCCESS)
                .data("products", products);
    }


    @PostMapping("/exchange")
    public CommonResult exchangeProduct(@RequestBody() PointExchangedRecordInfoVo record) {
        productService.exchangeProduct(record);
        return CommonResult.ok(ResultCode.SUCCESS);
    }

    @PostMapping("cancel/{id}")
    public CommonResult cancel(@PathVariable(name = "id") int id) {
        productService.cancel(id);
        return CommonResult.ok(ResultCode.SUCCESS);
    }
}

package cn.edu.bit.secondclass.service.impl;

import cn.edu.bit.secondclass.domain.PointExchangedRecord;
import cn.edu.bit.secondclass.domain.Product;
import cn.edu.bit.secondclass.domain.User;
import cn.edu.bit.secondclass.exception.PointExchangedRecordDaoException;
import cn.edu.bit.secondclass.myenum.PointExchangedRecordStatus;
import cn.edu.bit.secondclass.repository.PointExchangedRecordRepository;
import cn.edu.bit.secondclass.repository.ProductRepository;
import cn.edu.bit.secondclass.repository.UserRepository;
import cn.edu.bit.secondclass.service.ProductService;
import cn.edu.bit.secondclass.vo.PointExchangedRecordInfoVo;
import cn.edu.bit.secondclass.vo.ProductInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PointExchangedRecordRepository pointExchangedRecordRepository;

    private final ReadWriteLock readWriteLock;
    private final Lock readLock;
    private final Lock writeLock;

    @Autowired
    public ProductServiceImpl(ReadWriteLock readWriteLock) {
        this.readWriteLock = readWriteLock;
        this.readLock = this.readWriteLock.readLock();
        this.writeLock = this.readWriteLock.writeLock();
    }


    @Transactional(readOnly = true)
    @Override
    public List<ProductInfoVo> getAllProducts() {
        return productRepository
                .findAllProducts()
                .stream()
                .map(ProductServiceImpl::convertProductToVo)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Product getProductByName(String name) {
        return productRepository.findProductByName(name);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductInfoVo> getProductsInStock() {
        return productRepository
                .findProductByStock(0)
                .stream()
                .map(ProductServiceImpl::convertProductToVo)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductInfoVo> getConvertibleProduct(int userId) {
        Integer point = userRepository.findPointById(userId);
        return productRepository
                .findProductByStockAndPoint(0, point)
                .stream()
                .map(ProductServiceImpl::convertProductToVo)
                .collect(Collectors.toList());
    }


    @Override
    public void exchangeProduct(PointExchangedRecordInfoVo record) {
        String schoolId = record.getSchoolId();
        writeLock.lock();
        try {
            User user = userRepository.findUserBySchoolId(schoolId);
            Product product = productRepository.findProductById(record.getProduct());
            // 库存为0
            if (product.getStock() <= 0) {
                throw new PointExchangedRecordDaoException("该产品当前无库存");
            }

            // 积分不足以兑换文创产品
            if (user.getPoint() < product.getPoint()) {
                throw new PointExchangedRecordDaoException("您的积分不足以兑换当前产品");
            }

            PointExchangedRecord currentRecord = pointExchangedRecordRepository
                    .findPointExchangedRecordBySchoolIdAndStatus(schoolId
                            , PointExchangedRecordStatus.NEW.getStatus());
            if (currentRecord != null) {
                throw new PointExchangedRecordDaoException("您当前有未审批的兑换记录，无法再次兑换");
            }

            // 生成兑换文创产品的记录
            // 积分变动记录在管理员审核后生成
            PointExchangedRecord exchangedRecord = convertVoToPointExchangedRecord(record);
            exchangedRecord.setStatus(PointExchangedRecordStatus.NEW.getStatus());
            exchangedRecord.setTime(new Date());
            pointExchangedRecordRepository.save(exchangedRecord);

        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void cancel(int id) {
        PointExchangedRecord record = pointExchangedRecordRepository.findPointExchangedRecordById(id);
        if (record == null) {
            throw new PointExchangedRecordDaoException("不存在该兑换记录");
        }

        if (record.getStatus().equals(PointExchangedRecordStatus.FINISH.getStatus())
                || record.getStatus().equals(PointExchangedRecordStatus.REJECT.getStatus())) {
            throw new PointExchangedRecordDaoException("该兑换不可撤销");
        }

        if (record.getStatus().equals(PointExchangedRecordStatus.CANCEL.getStatus())) {
            throw new PointExchangedRecordDaoException("该兑换已被撤销，无法重复撤销！");
        }

        record.setStatus(PointExchangedRecordStatus.CANCEL.getStatus());
        pointExchangedRecordRepository.save(record);
    }


    private static ProductInfoVo convertProductToVo(Product product) {
        ProductInfoVo infoVo = new ProductInfoVo();
        infoVo.setId(product.getId());
        infoVo.setName((product.getName()));
        infoVo.setPoint(product.getPoint());
        infoVo.setStock(product.getStock());
        return infoVo;
    }

    private static PointExchangedRecord convertVoToPointExchangedRecord(PointExchangedRecordInfoVo infoVo) {
        PointExchangedRecord record = new PointExchangedRecord();
        record.setProduct(infoVo.getProduct());
        record.setReason(infoVo.getReason());
        record.setQuantity(infoVo.getQuantity());
        record.setSchoolId(infoVo.getSchoolId());
        return record;
    }
}

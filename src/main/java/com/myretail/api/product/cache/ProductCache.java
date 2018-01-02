package com.myretail.api.product.cache;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.myretail.api.product.dao.ProductDAO;
import com.myretail.api.product.domain.Product;
import com.myretail.api.product.exception.MyRetailFatalException;

@Component
public class ProductCache {
    private static Logger log = LoggerFactory.getLogger(ProductCache.class);
    LoadingCache<Long, Optional<Product>> prodCache = null;
    @Autowired
    ProductDAO prodDAO;

    public void init() {
        log.info("Initializing Product cache ...");
        if (prodCache != null)
            return;
        prodCache = CacheBuilder.newBuilder().maximumSize(10000).expireAfterAccess(30, TimeUnit.MINUTES)
                // .recordStats()
                .build(new CacheLoader<Long, Optional<Product>>() {
                    @Override
                    public Optional<Product> load(Long id) throws Exception {
                        Optional<Product> prod = Optional.empty();
                        prod = prodDAO.findProductDetails(id);
                        return prod;
                    }
                });
    }

    public Optional<Product> getProductDetails(Long id) throws MyRetailFatalException {
        try {
            log.info("ProductCache --> reading prod info, {} ", prodCache.stats().toString());
            return prodCache.get(id);
        } catch (ExecutionException ee) {
            log.error("ProductCache --> Error calling ProductCache");
            throw new MyRetailFatalException("Product Cache Exception", ee);
        }
    }
}

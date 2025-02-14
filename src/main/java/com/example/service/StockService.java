package com.example.service;

import com.example.mapper.StockMapper;
import com.example.pojo.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void deduct() {
        // 加锁setnx
        Boolean lock = this.redisTemplate.opsForValue().setIfAbsent("lock", "111");
        // 重试：递归调用
        if (!lock){
            try {
                Thread.sleep(50);
                this.deduct();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            try {
                // 1. 查询库存信息
                String stock = redisTemplate.opsForValue().get("stock").toString();

                // 2. 判断库存是否充足
                if (stock != null && stock.length() != 0) {
                    Integer st = Integer.valueOf(stock);
                    if (st > 0) {
                        // 3.扣减库存
                        redisTemplate.opsForValue().set("stock", String.valueOf(--st));
                    }
                }
//                checkAndLock();
            } finally {
                // 解锁
                this.redisTemplate.delete("lock");
            }
        }
    }



    public void checkAndLock() {
        // 先查询库存是否充足
        Stock stock = this.stockMapper.selectById(1L);

        // 再减库存
        if (stock != null && stock.getCount() > 0){
            stock.setCount(stock.getCount() - 1);
            this.stockMapper.updateById(stock);
        }
    }
}
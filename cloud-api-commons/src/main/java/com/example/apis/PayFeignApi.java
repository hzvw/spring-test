package com.example.apis;

import com.example.entities.PayDTO;
import com.example.resp.ResultData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @auther zzyy
 * @create 2023-11-09 15:29
 */
@FeignClient(value = "cloud-payment-service")
public interface PayFeignApi
{
    /**
     * 新增一条支付相关流水记录
     * @param payDTO
     * @return
     */
    @PostMapping("/pay/add")
    public ResultData addPay(@RequestBody PayDTO payDTO);

    /**
     * 按照主键记录查询支付流水信息
     * @param id
     * @return
     */
    @GetMapping("/pay/get/{id}")
    public ResultData getPayInfo(@PathVariable("id") Integer id);

    /**
     * openfeign天然支持负载均衡演示
     * @return
     */
    @GetMapping(value = "/pay/get/info")
    public String mylb();
}

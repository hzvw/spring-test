package com.example.controller;

import com.alibaba.fastjson2.JSON;
import com.example.apis.PayFeignApi;
import com.example.entities.Order;
import com.example.entities.PayDTO;
import com.example.resp.ResultData;
import com.example.service.OrderService;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * @auther zzyy
 * @create 2023-11-04 16:00
 */
@RestController
public class OrderController{
//    public static final String PaymentSrv_URL = "http://localhost:8001";//先写死，硬编码

    public static final String PaymentSrv_URL
            = "http://cloud-payment-service";//服务注册中心上的微服务名称

    @Resource
    private PayFeignApi payFeignApi;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 一般情况下，通过浏览器的地址栏输入url，发送的只能是get请求
     * 我们底层调用的是post方法，模拟消费者发送get请求，客户端消费者
     * 参数可以不添加@RequestBody
     * @param payDTO
     * @return
     */
//    @PostMapping("/consumer/pay/add")
//    public ResultData addOrder(@RequestBody PayDTO payDTO){
//        //return restTemplate.postForObject(PaymentSrv_URL + "/pay/add",payDTO,ResultData.class);
//
//        System.out.println("第一步：模拟本地addOrder新增订单成功(省略sql操作)，第二步：再开启addPay支付微服务远程调用");
//        ResultData resultData = payFeignApi.addPay(payDTO);
//        System.out.println(JSON.toJSONString(payDTO));
//        return resultData;
//    }

    @PostMapping("/consumer/pay/add")
    @GlobalTransactional
    public ResultData addOrder(@RequestBody PayDTO payDTO)
    {
        System.out.println("第一步：模拟本地addOrder新增订单成功(省略sql操作)，" +
                "第二步：再开启addPay支付微服务远程调用");
        Order order = new Order(1L,1L,10,1L,1);
        orderService.add(order);
        ResultData resultData = payFeignApi.addPay(payDTO);
        return resultData;
    }

    // 删除+修改操作作为家庭作业，O(∩_∩)O。。。。。。。
    @GetMapping("/consumer/pay/get/{id}")
    public ResultData getPayInfo(@PathVariable("id") Integer id){
        return restTemplate.getForObject(PaymentSrv_URL + "/pay/get/"+id, ResultData.class, id);
    }


}
 
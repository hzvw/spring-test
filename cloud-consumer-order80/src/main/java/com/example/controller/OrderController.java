package com.example.controller;

import com.example.anno.Idempotent;
import com.example.apis.PayFeignApi;
import com.example.dto.ApiResponse;
import com.example.dto.Order;
import com.example.dto.OrderModificationRequest;
import com.example.dto.OrderModifyLog;
import com.example.entities.PayDTO;
import com.example.exp.BusinessException;
import com.example.resp.ResultData;
import com.example.service.OrderService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @auther zzyy
 * @create 2023-11-04 16:00
 */
@RestController
@Slf4j
@EnableJpaAuditing//在启动类或配置类上添加 @EnableJpaAuditing注解，以使 @CreatedDate和 @LastModifiedDate生效。
public class OrderController{
    public static final String PaymentSrv_URL
            = "http://cloud-payment-service";//服务注册中心上的微服务名称

    @Resource
    private PayFeignApi payFeignApi;

    @Autowired
    private OrderService orderService;

//    @Autowired
//    private OrderService orderService;

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
    public ResultData addOrder(@RequestBody PayDTO payDTO)    {
        System.out.println("第一步：模拟本地addOrder新增订单成功(省略sql操作)，" +
                "第二步：再开启addPay支付微服务远程调用");
        ResultData resultData = payFeignApi.addPay(payDTO);
        return resultData;
    }

    // 删除+修改操作作为家庭作业，O(∩_∩)O。。。。。。。
    @GetMapping("/consumer/pay/get/{id}")
    public ResultData getPayInfo(@PathVariable("id") Integer id){
        //return restTemplate.getForObject(PaymentSrv_URL + "/pay/get/"+id, ResultData.class, id);
        return payFeignApi.getPayInfo(id);
    }

    /**
     * 修改订单信息
     */
    @PutMapping("/modify_order/{orderId}")
    @Idempotent(key = "'orderModify:' + #orderId", timeout = 30) // key由订单ID确定
    public ResultData modifyOrder(
            @PathVariable("orderId") Long orderId,
            @RequestBody @Valid OrderModificationRequest request,
            @RequestHeader("X-Operator-Id") Long operatorId,
            @RequestHeader("X-Operator-Type") Integer operatorType) {

        try {
            Order updatedOrder = orderService.modifyOrder(orderId, request, operatorId, operatorType);
            return ResultData.success(updatedOrder);
        } catch (BusinessException e) {
            //return ApiResponse.error("MODIFY_FAILED", e.getMessage());
            log.error(e.getMessage());
            return ResultData.fail("1001", "出错了");
        }
    }

    /**
     * 获取订单修改历史
     */
    @GetMapping("/{orderId}/modification-history")
    public ApiResponse<List<OrderModifyLog>> getModificationHistory(
            @PathVariable Long orderId) {

        List<OrderModifyLog> history = orderService.getModificationHistory(orderId);
        return ApiResponse.success(history);
    }

    /**
     * 获取订单可修改字段信息
     */
    @GetMapping("/{orderId}/modifiable-fields")
    public ApiResponse<Set<String>> getModifiableFields(
            @PathVariable Long orderId) {

        Set<String> fields = orderService.getModifiableFields(orderId);
        return ApiResponse.success(fields);
    }


}
 
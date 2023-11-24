package com.example.userservice.client;

import com.example.userservice.VO.ResponseOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "order-service")
public interface OrderServiceClient {
    @GetMapping("/oerder-servic/{userId}/orders")
    List<ResponseOrder> getOrders(@PathVariable String userId);
}

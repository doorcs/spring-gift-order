package gift.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.resolver.LoginMemberId;
import gift.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
        @LoginMemberId Long memberId,
        @Valid @RequestBody OrderRequest orderRequest
    ) throws JsonProcessingException {
        OrderResponse orderResponse = orderService.order(memberId, orderRequest);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponse);
    }
}

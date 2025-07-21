package com.app.rest.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.app.dao.OrderDAO;
import com.app.dao.OrderDetailDAO;
import com.app.entity.Order;
import com.app.entity.OrderDetail;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/counters")
public class CounterRestController {

    @Autowired
    OrderDAO orderDAO;

    @Autowired
    OrderDetailDAO orderDetailDAO;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * Tạo đơn hàng mới từ JsonNode (gửi từ AngularJS)
     */
    @PostMapping
    public Order create(@RequestBody JsonNode orderData) {
        try {
            // Chuyển JSON sang Order
            Order order = objectMapper.convertValue(orderData.get("order"), Order.class);
            order.setCreateDate(new Date());
            Order savedOrder = orderDAO.save(order);

            // Lấy danh sách các OrderDetail từ JSON
            JsonNode detailsNode = orderData.get("orderDetails");
            for (JsonNode d : detailsNode) {
                OrderDetail detail = objectMapper.convertValue(d, OrderDetail.class);
                detail.setOrder(savedOrder);
                orderDetailDAO.save(detail);
            }

            return savedOrder;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi tạo đơn hàng!");
        }
    }

    /**
     * Lấy toàn bộ đơn hàng
     */
    @GetMapping
    public List<Order> getAll() {
        return orderDAO.findAll();
    }

    /**
     * Lấy đơn hàng theo ID
     */
    @GetMapping("/{id}")
    public Order getOne(@PathVariable("id") Integer id) {
        return orderDAO.findById(id).orElse(null);
    }

    /**
     * Cập nhật trạng thái đơn hàng
     */
    @PutMapping("/{id}/status")
    public Order updateStatus(@PathVariable("id") Integer id, @RequestParam("status") String status) {
        Order order = orderDAO.findById(id).orElse(null);
        if (order != null) {
            order.setStatus(status);
            return orderDAO.save(order);
        }
        throw new RuntimeException("Không tìm thấy đơn hàng!");
    }

    /**
     * Lọc đơn hàng theo ngày
     */
    @GetMapping("/by-date")
    public List<Order> getByDate(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to) {
        return orderDAO.findByCreateDateBetween(from, to);
    }
}

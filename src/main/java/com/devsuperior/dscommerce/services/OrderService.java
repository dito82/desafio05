package com.devsuperior.dscommerce.services;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscommerce.dto.CategoryDTO;
import com.devsuperior.dscommerce.dto.OrderDTO;
import com.devsuperior.dscommerce.dto.OrderItemDTO;
import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.dto.ProductMinDTO;
import com.devsuperior.dscommerce.entities.Category;
import com.devsuperior.dscommerce.entities.Order;
import com.devsuperior.dscommerce.entities.OrderItem;
import com.devsuperior.dscommerce.entities.OrderStatus;
import com.devsuperior.dscommerce.entities.Product;
import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.repositories.OrderItemRepository;
import com.devsuperior.dscommerce.repositories.OrderRepository;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.DatabaseException;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private UserService userService;

	@Transactional(readOnly = true)
	public OrderDTO findById(Long id) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));
		return new OrderDTO(order);
	}

	@Transactional
	public OrderDTO insert(OrderDTO dto) {
		
		Order order = new Order();

		order.setMoment(Instant.now());
		order.setStatus(OrderStatus.WAITING_PAYMENT);

		User user = userService.authenticated();
		order.setClient(user);

		for (OrderItemDTO itemDTO : dto.getItems()) {
			Product product = productRepository.getReferenceById(itemDTO.getProductId());
			OrderItem item = new OrderItem(order, product, itemDTO.getQuantity(), product.getPrice());
			order.getItems().add(item);
		}
		orderRepository.save(order);
		orderItemRepository.saveAll(order.getItems());
		return new OrderDTO(order);
	}

}

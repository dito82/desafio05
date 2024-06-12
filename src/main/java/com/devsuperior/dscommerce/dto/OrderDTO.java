package com.devsuperior.dscommerce.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.devsuperior.dscommerce.entities.Order;
import com.devsuperior.dscommerce.entities.OrderItem;
import com.devsuperior.dscommerce.entities.OrderStatus;

import jakarta.validation.constraints.NotEmpty;

public class OrderDTO {

	private Long id;
	private Instant moment;
	private OrderStatus status;

	private ClientDTO clientDto;

	private PaymentDTO paymentDto;

	@NotEmpty(message = "Deve haver ao menos um item.")
	private List<OrderItemDTO> items = new ArrayList<>();

	public OrderDTO(Long id, Instant moment, OrderStatus status, ClientDTO clientDto, PaymentDTO paymentDto) {
		this.id = id;
		this.moment = moment;
		this.status = status;
		this.clientDto = clientDto;
		this.paymentDto = paymentDto;
	}

	public OrderDTO(Order entity) {
		id = entity.getId();
		moment = entity.getMoment();
		status = entity.getStatus();
		clientDto = new ClientDTO(entity.getClient());
		paymentDto = (entity.getPayment() == null) ? null : new PaymentDTO(entity.getPayment());
		for (OrderItem item : entity.getItems()) {
			OrderItemDTO itemDTO = new OrderItemDTO(item);
			items.add(itemDTO);
		}
	}

	public Long getId() {
		return id;
	}

	public Instant getMoment() {
		return moment;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public ClientDTO getClientDto() {
		return clientDto;
	}

	public PaymentDTO getPaymentDto() {
		return paymentDto;
	}

	public List<OrderItemDTO> getItems() {
		return items;
	}

	public Double getTotal() {
		double sum = 0.0;
		for (OrderItemDTO item : items) {
			sum += item.getSubTotal();
		}
		return sum;
	}

}

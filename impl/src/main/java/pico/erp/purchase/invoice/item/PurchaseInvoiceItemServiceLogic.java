package pico.erp.purchase.invoice.item;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kkojaeh.spring.boot.component.Give;
import kkojaeh.spring.boot.component.Take;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.purchase.invoice.PurchaseInvoiceId;
import pico.erp.purchase.invoice.PurchaseInvoiceService;
import pico.erp.purchase.invoice.item.PurchaseInvoiceItemRequests.DeleteRequest;
import pico.erp.purchase.order.item.PurchaseOrderItemService;
import pico.erp.shared.event.EventPublisher;

@SuppressWarnings("Duplicates")
@Service
@Give
@Transactional
@Validated
public class PurchaseInvoiceItemServiceLogic implements PurchaseInvoiceItemService {

  @Autowired
  private PurchaseInvoiceItemRepository itemRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private PurchaseInvoiceItemMapper mapper;

  @Autowired
  private PurchaseInvoiceService invoiceService;

  @Take
  private PurchaseOrderItemService purchaseOrderItemService;


  @Override
  public PurchaseInvoiceItemData create(PurchaseInvoiceItemRequests.CreateRequest request) {
    val item = new PurchaseInvoiceItem();
    val response = item.apply(mapper.map(request));
    if (itemRepository.exists(item.getId())) {
      throw new PurchaseInvoiceItemExceptions.AlreadyExistsException();
    }
    val created = itemRepository.create(item);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(DeleteRequest request) {
    val item = itemRepository.findBy(request.getId())
      .orElseThrow(PurchaseInvoiceItemExceptions.NotFoundException::new);
    val response = item.apply(mapper.map(request));
    itemRepository.deleteBy(item.getId());
    eventPublisher.publishEvents(response.getEvents());
  }


  @Override
  public boolean exists(PurchaseInvoiceItemId id) {
    return itemRepository.exists(id);
  }

  @Override
  public void generate(PurchaseInvoiceItemRequests.GenerateRequest request) {
    val invoice = invoiceService.get(request.getInvoiceId());
    val orderItems = purchaseOrderItemService.getAll(invoice.getOrderId());
    val createRequests = orderItems.stream()
      .map(item -> PurchaseInvoiceItemRequests.CreateRequest.builder()
        .id(PurchaseInvoiceItemId.generate())
        .invoiceId(invoice.getId())
        .orderItemId(item.getId())
        .quantity(BigDecimal.ZERO)
        .remark(item.getRemark())
        .build()
      ).collect(Collectors.toList());
    createRequests.forEach(this::create);
    eventPublisher.publishEvent(
      new PurchaseInvoiceItemEvents.GeneratedEvent(
        createRequests.stream()
          .map(PurchaseInvoiceItemRequests.CreateRequest::getId)
          .collect(Collectors.toList())
      )
    );
  }

  @Override
  public PurchaseInvoiceItemData get(PurchaseInvoiceItemId id) {
    return itemRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(PurchaseInvoiceItemExceptions.NotFoundException::new);
  }

  @Override
  public List<PurchaseInvoiceItemData> getAll(PurchaseInvoiceId invoiceId) {
    return itemRepository.findAllBy(invoiceId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void invoice(PurchaseInvoiceItemRequests.InvoiceRequest request) {
    val item = itemRepository.findBy(request.getId())
      .orElseThrow(PurchaseInvoiceItemExceptions.NotFoundException::new);
    val response = item.apply(mapper.map(request));
    itemRepository.update(item);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void update(PurchaseInvoiceItemRequests.UpdateRequest request) {
    val item = itemRepository.findBy(request.getId())
      .orElseThrow(PurchaseInvoiceItemExceptions.NotFoundException::new);
    val response = item.apply(mapper.map(request));
    itemRepository.update(item);
    eventPublisher.publishEvents(response.getEvents());
  }

}

package pico.erp.purchase.invoice;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kkojaeh.spring.boot.component.ComponentAutowired;
import kkojaeh.spring.boot.component.ComponentBean;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.invoice.InvoiceId;
import pico.erp.purchase.invoice.PurchaseInvoiceRequests.CancelRequest;
import pico.erp.purchase.invoice.PurchaseInvoiceRequests.DetermineRequest;
import pico.erp.purchase.invoice.PurchaseInvoiceRequests.GenerateRequest;
import pico.erp.purchase.invoice.PurchaseInvoiceRequests.InvoiceRequest;
import pico.erp.purchase.invoice.PurchaseInvoiceRequests.ReceiveRequest;
import pico.erp.purchase.order.PurchaseOrderId;
import pico.erp.purchase.order.PurchaseOrderService;
import pico.erp.shared.event.EventPublisher;

@SuppressWarnings("Duplicates")
@ComponentBean
@Service
@Transactional
@Validated
public class PurchaseInvoiceServiceLogic implements PurchaseInvoiceService {

  @Autowired
  private PurchaseInvoiceRepository purchaseInvoiceRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private PurchaseInvoiceMapper mapper;

  @ComponentAutowired
  private PurchaseOrderService purchaseOrderService;

  @Override
  public void cancel(CancelRequest request) {
    val purchaseInvoice = purchaseInvoiceRepository.findBy(request.getId())
      .orElseThrow(PurchaseInvoiceExceptions.NotFoundException::new);
    val response = purchaseInvoice.apply(mapper.map(request));
    purchaseInvoiceRepository.update(purchaseInvoice);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public PurchaseInvoiceData create(PurchaseInvoiceRequests.CreateRequest request) {
    val hasDraft = purchaseInvoiceRepository.findAllBy(request.getOrderId())
      .anyMatch(invoice -> invoice.getStatus() == PurchaseInvoiceStatusKind.DRAFT);
    if (hasDraft) {
      throw new PurchaseInvoiceExceptions.DraftAlreadyExistsException();
    }
    val purchaseInvoice = new PurchaseInvoice();
    val response = purchaseInvoice.apply(mapper.map(request));
    if (purchaseInvoiceRepository.exists(purchaseInvoice.getId())) {
      throw new PurchaseInvoiceExceptions.AlreadyExistsException();
    }
    val created = purchaseInvoiceRepository.create(purchaseInvoice);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void determine(DetermineRequest request) {
    val purchaseInvoice = purchaseInvoiceRepository.findBy(request.getId())
      .orElseThrow(PurchaseInvoiceExceptions.NotFoundException::new);
    val response = purchaseInvoice.apply(mapper.map(request));
    purchaseInvoiceRepository.update(purchaseInvoice);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(PurchaseInvoiceId id) {
    return purchaseInvoiceRepository.exists(id);
  }

  @Override
  public boolean exists(InvoiceId invoiceId) {
    return purchaseInvoiceRepository.exists(invoiceId);
  }

  @Override
  public PurchaseInvoiceData generate(GenerateRequest request) {
    val order = purchaseOrderService.get(request.getOrderId());
    val id = request.getId();
    val createRequest = PurchaseInvoiceRequests.CreateRequest.builder()
      .id(id)
      .orderId(order.getId())
      .dueDate(OffsetDateTime.now().plusDays(1))
      .build();
    val created = create(createRequest);
    eventPublisher.publishEvent(
      new PurchaseInvoiceEvents.GeneratedEvent(created.getId())
    );
    return created;
  }

  @Override
  public PurchaseInvoiceData get(PurchaseInvoiceId id) {
    return purchaseInvoiceRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(PurchaseInvoiceExceptions.NotFoundException::new);
  }

  @Override
  public PurchaseInvoiceData get(InvoiceId invoiceId) {
    return purchaseInvoiceRepository.findBy(invoiceId)
      .map(mapper::map)
      .orElseThrow(PurchaseInvoiceExceptions.NotFoundException::new);
  }

  @Override
  public List<PurchaseInvoiceData> getAll(PurchaseOrderId orderId) {
    return purchaseInvoiceRepository.findAllBy(orderId)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void invoice(InvoiceRequest request) {
    val purchaseInvoice = purchaseInvoiceRepository.findBy(request.getId())
      .orElseThrow(PurchaseInvoiceExceptions.NotFoundException::new);
    val response = purchaseInvoice.apply(mapper.map(request));
    purchaseInvoiceRepository.update(purchaseInvoice);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void receive(ReceiveRequest request) {
    val purchaseInvoice = purchaseInvoiceRepository.findBy(request.getId())
      .orElseThrow(PurchaseInvoiceExceptions.NotFoundException::new);
    val response = purchaseInvoice.apply(mapper.map(request));
    purchaseInvoiceRepository.update(purchaseInvoice);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void update(PurchaseInvoiceRequests.UpdateRequest request) {
    val purchaseInvoice = purchaseInvoiceRepository.findBy(request.getId())
      .orElseThrow(PurchaseInvoiceExceptions.NotFoundException::new);
    val response = purchaseInvoice.apply(mapper.map(request));
    purchaseInvoiceRepository.update(purchaseInvoice);
    eventPublisher.publishEvents(response.getEvents());
  }

}

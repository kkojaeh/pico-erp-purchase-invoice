package pico.erp.purchase.invoice;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.invoice.InvoiceEvents;
import pico.erp.invoice.InvoiceId;
import pico.erp.invoice.InvoiceRequests;
import pico.erp.invoice.InvoiceService;
import pico.erp.purchase.invoice.item.PurchaseInvoiceItemService;
import pico.erp.purchase.order.PurchaseOrderService;

@SuppressWarnings("unused")
@Component
@Transactional
public class PurchaseInvoiceEventListener {

  private static final String LISTENER_NAME = "listener.purchase-invoice-event-listener";

  @Lazy
  @Autowired
  private InvoiceService invoiceService;

  @Lazy
  @Autowired
  private PurchaseInvoiceService purchaseInvoiceService;

  @Lazy
  @Autowired
  private PurchaseInvoiceItemService purchaseInvoiceItemService;

  @Lazy
  @Autowired
  private PurchaseOrderService purchaseOrderService;


  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + PurchaseInvoiceEvents.DeterminedEvent.CHANNEL)
  public void onPurchaseInvoiceDetermined(PurchaseInvoiceEvents.DeterminedEvent event) {
    val purchaseInvoice = purchaseInvoiceService.get(event.getPurchaseInvoiceId());
    val purchaseOrder = purchaseOrderService.get(purchaseInvoice.getOrderId());
    val invoiceId = InvoiceId.generate();
    invoiceService.create(
      InvoiceRequests.CreateRequest.builder()
        .id(invoiceId)
        .dueDate(purchaseInvoice.getDueDate())
        .receiverId(purchaseOrder.getReceiverId())
        .senderId(purchaseOrder.getSupplierId())
        .receiveAddress(purchaseOrder.getReceiveAddress())
        .remark(purchaseInvoice.getRemark())
        .build()
    );
    purchaseInvoiceService.invoice(
      PurchaseInvoiceRequests.InvoiceRequest.builder()
        .id(purchaseInvoice.getId())
        .invoiceId(invoiceId)
        .build()
    );
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + PurchaseInvoiceEvents.UpdatedEvent.CHANNEL)
  public void onPurchaseInvoiceUpdated(PurchaseInvoiceEvents.UpdatedEvent event) {
    val purchaseInvoice = purchaseInvoiceService.get(event.getPurchaseInvoiceId());
    val purchaseOrder = purchaseOrderService.get(purchaseInvoice.getOrderId());
    val invoiceId = purchaseInvoice.getInvoiceId();
    if (invoiceId != null) {
      invoiceService.update(
        InvoiceRequests.UpdateRequest.builder()
          .id(invoiceId)
          .dueDate(purchaseInvoice.getDueDate())
          .receiverId(purchaseOrder.getReceiverId())
          .senderId(purchaseOrder.getSupplierId())
          .receiveAddress(purchaseOrder.getReceiveAddress())
          .remark(purchaseInvoice.getRemark())
          .build()
      );
    }
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + PurchaseInvoiceEvents.CanceledEvent.CHANNEL)
  public void onPurchaseInvoiceCanceled(PurchaseInvoiceEvents.CanceledEvent event) {
    val purchaseInvoice = purchaseInvoiceService.get(event.getPurchaseInvoiceId());
    val purchaseOrder = purchaseOrderService.get(purchaseInvoice.getOrderId());
    val invoiceId = purchaseInvoice.getInvoiceId();
    if (invoiceId != null) {
      invoiceService.cancel(
        InvoiceRequests.CancelRequest.builder()
          .id(invoiceId)
          .build()
      );
    }
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + InvoiceEvents.ReceivedEvent.CHANNEL)
  public void onInvoiceReceived(InvoiceEvents.ReceivedEvent event) {
    val invoiceId = event.getInvoiceId();
    val exists = purchaseInvoiceService.exists(invoiceId);
    if (exists) {
      val purchaseInvoice = purchaseInvoiceService.get(invoiceId);
      purchaseInvoiceService.receive(
        PurchaseInvoiceRequests.ReceiveRequest.builder()
          .id(purchaseInvoice.getId())
          .build()
      );
    }
  }


}

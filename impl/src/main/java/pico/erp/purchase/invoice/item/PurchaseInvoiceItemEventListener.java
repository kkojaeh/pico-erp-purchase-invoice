package pico.erp.purchase.invoice.item;

import kkojaeh.spring.boot.component.ComponentAutowired;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pico.erp.invoice.item.InvoiceItemId;
import pico.erp.invoice.item.InvoiceItemRequests;
import pico.erp.invoice.item.InvoiceItemService;
import pico.erp.item.lot.ItemLotService;
import pico.erp.item.spec.ItemSpecService;
import pico.erp.purchase.invoice.PurchaseInvoiceEvents;
import pico.erp.purchase.invoice.PurchaseInvoiceService;
import pico.erp.purchase.order.item.PurchaseOrderItemRequests;
import pico.erp.purchase.order.item.PurchaseOrderItemService;

@SuppressWarnings("unused")
@Component
public class PurchaseInvoiceItemEventListener {

  private static final String LISTENER_NAME = "listener.purchase-invoice-item-event-listener";

  @Autowired
  private PurchaseInvoiceItemService purchaseInvoiceItemService;

  @ComponentAutowired
  private PurchaseOrderItemService purchaseOrderItemService;

  @ComponentAutowired
  private InvoiceItemService invoiceItemService;

  @Autowired
  private PurchaseInvoiceService purchaseInvoiceService;

  @ComponentAutowired
  private ItemLotService itemLotService;

  @ComponentAutowired
  private ItemSpecService itemSpecService;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + PurchaseInvoiceEvents.GeneratedEvent.CHANNEL)
  public void onPurchaseInvoiceGenerated(PurchaseInvoiceEvents.GeneratedEvent event) {

    purchaseInvoiceItemService.generate(
      new PurchaseInvoiceItemRequests.GenerateRequest(event.getId())
    );
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + PurchaseInvoiceEvents.InvoicedEvent.CHANNEL)
  public void onPurchaseInvoiceInvoiced(PurchaseInvoiceEvents.InvoicedEvent event) {
    val purchaseInvoice = purchaseInvoiceService.get(event.getId());

    purchaseInvoiceItemService.getAll(event.getId())
      .forEach(item -> {
        val orderItem = purchaseOrderItemService.get(item.getOrderItemId());
        val itemId = orderItem.getItemId();
        val itemSpecCode = orderItem.getItemSpecCode();
        val invoiceItemId = InvoiceItemId.generate();
        invoiceItemService.create(
          InvoiceItemRequests.CreateRequest.builder()
            .id(invoiceItemId)
            .invoiceId(purchaseInvoice.getInvoiceId())
            .itemId(orderItem.getItemId())
            .itemSpecCode(itemSpecCode)
            .quantity(item.getQuantity())
            .unit(orderItem.getUnit())
            .remark(item.getRemark())
            .build()
        );
        purchaseInvoiceItemService.invoice(
          PurchaseInvoiceItemRequests.InvoiceRequest.builder()
            .id(item.getId())
            .invoiceItemId(invoiceItemId)
            .build()
        );
      });
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + PurchaseInvoiceItemEvents.UpdatedEvent.CHANNEL)
  public void onPurchaseInvoiceItemUpdated(PurchaseInvoiceItemEvents.UpdatedEvent event) {
    val purchaseInvoiceItem = purchaseInvoiceItemService.get(event.getId());
    val invoiceItemId = purchaseInvoiceItem.getInvoiceItemId();
    if (invoiceItemId != null) {
      invoiceItemService.update(
        InvoiceItemRequests.UpdateRequest.builder()
          .id(invoiceItemId)
          .quantity(purchaseInvoiceItem.getQuantity())
          .remark(purchaseInvoiceItem.getRemark())
          .build()
      );
    }
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + PurchaseInvoiceEvents.ReceivedEvent.CHANNEL)
  public void onPurchaseInvoiceReceived(PurchaseInvoiceEvents.ReceivedEvent event) {

    purchaseInvoiceItemService.getAll(event.getId())
      .forEach(invoiceItem -> {
        purchaseOrderItemService.receive(
          PurchaseOrderItemRequests.ReceiveRequest.builder()
            .id(invoiceItem.getOrderItemId())
            .quantity(invoiceItem.getQuantity())
            .build()
        );
      });
  }


}

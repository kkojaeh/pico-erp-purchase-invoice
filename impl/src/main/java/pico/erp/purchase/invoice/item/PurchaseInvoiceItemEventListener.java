package pico.erp.purchase.invoice.item;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pico.erp.invoice.item.InvoiceItemId;
import pico.erp.invoice.item.InvoiceItemRequests;
import pico.erp.invoice.item.InvoiceItemService;
import pico.erp.item.lot.ItemLotId;
import pico.erp.item.lot.ItemLotRequests;
import pico.erp.item.lot.ItemLotService;
import pico.erp.item.spec.ItemSpecService;
import pico.erp.purchase.invoice.PurchaseInvoiceEvents;
import pico.erp.purchase.invoice.PurchaseInvoiceService;
import pico.erp.purchase.order.item.PurchaseOrderItemService;

@SuppressWarnings("unused")
@Component
public class PurchaseInvoiceItemEventListener {

  private static final String LISTENER_NAME = "listener.purchase-invoice-item-event-listener";

  @Autowired
  private PurchaseInvoiceItemService purchaseInvoiceItemService;

  @Lazy
  @Autowired
  private PurchaseOrderItemService purchaseOrderItemService;

  @Lazy
  @Autowired
  private InvoiceItemService invoiceItemService;

  @Lazy
  @Autowired
  private PurchaseInvoiceService purchaseInvoiceService;

  @Lazy
  @Autowired
  private ItemLotService itemLotService;

  @Lazy
  @Autowired
  private ItemSpecService itemSpecService;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + PurchaseInvoiceEvents.GeneratedEvent.CHANNEL)
  public void onPurchaseInvoiceGenerated(PurchaseInvoiceEvents.GeneratedEvent event) {

    purchaseInvoiceItemService.generate(
      new PurchaseInvoiceItemRequests.GenerateRequest(event.getPurchaseInvoiceId())
    );
  }


  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + PurchaseInvoiceEvents.InvoicedEvent.CHANNEL)
  public void onPurchaseInvoiceInvoiced(PurchaseInvoiceEvents.InvoicedEvent event) {
    val purchaseInvoice = purchaseInvoiceService.get(event.getPurchaseInvoiceId());

    purchaseInvoiceItemService.getAll(event.getPurchaseInvoiceId()).stream()
      .forEach(item -> {
        val orderItem = purchaseOrderItemService.get(item.getOrderItemId());
        val itemId = orderItem.getItemId();
        ItemLotId itemLotId = null;
        val itemSpecId = orderItem.getItemSpecId();
        if (itemSpecId != null) {
          val itemSpec = itemSpecService.get(orderItem.getItemSpecId());
          val lotCode = itemSpec.getLotCode();
          val exists = itemLotService.exists(itemId, lotCode);

          if (exists) {
            itemLotId = itemLotService.get(itemId, lotCode).getId();
          } else {
            itemLotId = ItemLotId.generate();
            itemLotService.create(
              ItemLotRequests.CreateRequest.builder()
                .id(itemLotId)
                .itemId(itemId)
                .code(lotCode)
                .build()
            );

          }
        }
        val invoiceItemId = InvoiceItemId.generate();
        invoiceItemService.create(
          InvoiceItemRequests.CreateRequest.builder()
            .id(invoiceItemId)
            .invoiceId(purchaseInvoice.getInvoiceId())
            .itemId(orderItem.getItemId())
            .itemLotId(itemLotId)
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
    val purchaseInvoiceItem = purchaseInvoiceItemService.get(event.getPurchaseInvoiceItemId());
    val invoiceItemId = purchaseInvoiceItem.getInvoiceItemId();
    if(invoiceItemId != null) {
      invoiceItemService.update(
        InvoiceItemRequests.UpdateRequest.builder()
          .id(invoiceItemId)
          .quantity(purchaseInvoiceItem.getQuantity())
          .remark(purchaseInvoiceItem.getRemark())
          .build()
      );
    }
  }


}

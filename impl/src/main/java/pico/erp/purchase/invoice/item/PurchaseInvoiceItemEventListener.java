package pico.erp.purchase.invoice.item;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pico.erp.invoice.item.InvoiceItemId;
import pico.erp.invoice.item.InvoiceItemRequests;
import pico.erp.invoice.item.InvoiceItemService;
import pico.erp.item.lot.ItemLotCode;
import pico.erp.item.lot.ItemLotId;
import pico.erp.item.lot.ItemLotKey;
import pico.erp.item.lot.ItemLotRequests;
import pico.erp.item.lot.ItemLotService;
import pico.erp.item.spec.ItemSpecCode;
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
      new PurchaseInvoiceItemRequests.GenerateRequest(event.getId())
    );
  }

  private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + PurchaseInvoiceEvents.InvoicedEvent.CHANNEL)
  public void onPurchaseInvoiceInvoiced(PurchaseInvoiceEvents.InvoicedEvent event) {
    val purchaseInvoice = purchaseInvoiceService.get(event.getId());

    purchaseInvoiceItemService.getAll(event.getId())
      .forEach(item -> {
        val orderItem = purchaseOrderItemService.get(item.getOrderItemId());
        val itemId = orderItem.getItemId();
        ItemLotId lotId = null;
        val itemSpecId = orderItem.getItemSpecId();
        val lotCode = ItemLotCode.from(dateFormatter.format(OffsetDateTime.now()));
        val specCode = Optional.ofNullable(itemSpecId)
          .map(specId -> itemSpecService.get(specId).getCode())
          .orElse(ItemSpecCode.NOT_APPLICABLE);
        val lotKey = ItemLotKey.from(itemId, specCode, lotCode);
        val exists = itemLotService.exists(lotKey);
        if (exists) {
          lotId = itemLotService.get(lotKey).getId();
        } else {
          lotId = ItemLotId.generate();
          itemLotService.create(
            ItemLotRequests.CreateRequest.builder()
              .id(lotId)
              .itemId(itemId)
              .specCode(specCode)
              .lotCode(lotCode)
              .build()
          );
        }
        val invoiceItemId = InvoiceItemId.generate();
        invoiceItemService.create(
          InvoiceItemRequests.CreateRequest.builder()
            .id(invoiceItemId)
            .invoiceId(purchaseInvoice.getInvoiceId())
            .itemId(orderItem.getItemId())
            .itemLotId(lotId)
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

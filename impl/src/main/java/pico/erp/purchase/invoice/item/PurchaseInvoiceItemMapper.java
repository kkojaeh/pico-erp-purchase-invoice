package pico.erp.purchase.invoice.item;

import java.util.Optional;
import kkojaeh.spring.boot.component.Take;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import pico.erp.invoice.item.InvoiceItemData;
import pico.erp.invoice.item.InvoiceItemId;
import pico.erp.invoice.item.InvoiceItemService;
import pico.erp.item.ItemData;
import pico.erp.item.ItemId;
import pico.erp.item.ItemService;
import pico.erp.item.lot.ItemLotData;
import pico.erp.item.lot.ItemLotId;
import pico.erp.item.lot.ItemLotService;
import pico.erp.item.spec.ItemSpecData;
import pico.erp.item.spec.ItemSpecId;
import pico.erp.item.spec.ItemSpecService;
import pico.erp.purchase.invoice.PurchaseInvoice;
import pico.erp.purchase.invoice.PurchaseInvoiceExceptions;
import pico.erp.purchase.invoice.PurchaseInvoiceId;
import pico.erp.purchase.invoice.PurchaseInvoiceMapper;
import pico.erp.purchase.order.item.PurchaseOrderItemData;
import pico.erp.purchase.order.item.PurchaseOrderItemId;
import pico.erp.purchase.order.item.PurchaseOrderItemService;
import pico.erp.shared.data.Auditor;

@Mapper
public abstract class PurchaseInvoiceItemMapper {

  @Autowired
  protected AuditorAware<Auditor> auditorAware;

  @Take
  protected ItemService itemService;

  @Take
  protected ItemLotService itemLotService;

  @Take
  protected ItemSpecService itemSpecService;

  @Lazy
  @Autowired
  private PurchaseInvoiceItemRepository purchaseRequestItemRepository;

  @Autowired
  private PurchaseInvoiceMapper requestMapper;

  @Take
  private PurchaseOrderItemService purchaseOrderItemService;

  @Take
  private InvoiceItemService invoiceItemService;

  protected PurchaseInvoiceItemId id(PurchaseInvoiceItem purchaseRequestItem) {
    return purchaseRequestItem != null ? purchaseRequestItem.getId() : null;
  }

  @Mappings({
    @Mapping(target = "invoiceId", source = "invoice.id"),
    @Mapping(target = "orderItemId", source = "orderItem.id"),
    @Mapping(target = "invoiceItemId", source = "invoiceItem.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract PurchaseInvoiceItemEntity jpa(PurchaseInvoiceItem data);

  public PurchaseInvoiceItem jpa(PurchaseInvoiceItemEntity entity) {
    return PurchaseInvoiceItem.builder()
      .id(entity.getId())
      .invoice(map(entity.getInvoiceId()))
      .orderItem(map(entity.getOrderItemId()))
      .invoiceItem(map(entity.getInvoiceItemId()))
      .quantity(entity.getQuantity())
      .remark(entity.getRemark())
      .build();
  }

  public PurchaseInvoiceItem map(PurchaseInvoiceItemId purchaseRequestItemId) {
    return Optional.ofNullable(purchaseRequestItemId)
      .map(id -> purchaseRequestItemRepository.findBy(id)
        .orElseThrow(PurchaseInvoiceExceptions.NotFoundException::new)
      )
      .orElse(null);
  }

  protected ItemData map(ItemId itemId) {
    return Optional.ofNullable(itemId)
      .map(itemService::get)
      .orElse(null);
  }

  protected ItemLotData map(ItemLotId itemLotId) {
    return Optional.ofNullable(itemLotId)
      .map(itemLotService::get)
      .orElse(null);
  }

  protected ItemSpecData map(ItemSpecId itemSpecId) {
    return Optional.ofNullable(itemSpecId)
      .map(itemSpecService::get)
      .orElse(null);
  }

  protected PurchaseInvoice map(PurchaseInvoiceId purchaseInvoiceId) {
    return requestMapper.map(purchaseInvoiceId);
  }

  protected PurchaseOrderItemData map(PurchaseOrderItemId orderItemId) {
    return Optional.ofNullable(orderItemId)
      .map(purchaseOrderItemService::get)
      .orElse(null);
  }

  protected InvoiceItemData map(InvoiceItemId invoiceItemId) {
    return Optional.ofNullable(invoiceItemId)
      .map(invoiceItemService::get)
      .orElse(null);
  }

  @Mappings({
    @Mapping(target = "invoiceId", source = "invoice.id"),
    @Mapping(target = "orderItemId", source = "orderItem.id"),
    @Mapping(target = "invoiceItemId", source = "invoiceItem.id")
  })
  public abstract PurchaseInvoiceItemData map(PurchaseInvoiceItem item);

  @Mappings({
    @Mapping(target = "invoice", source = "invoiceId"),
    @Mapping(target = "orderItem", source = "orderItemId")
  })
  public abstract PurchaseInvoiceItemMessages.Create.Request map(
    PurchaseInvoiceItemRequests.CreateRequest request);

  @Mappings({
    @Mapping(target = "invoiceItem", source = "invoiceItemId")
  })
  public abstract PurchaseInvoiceItemMessages.Invoice.Request map(
    PurchaseInvoiceItemRequests.InvoiceRequest request);

  public abstract PurchaseInvoiceItemMessages.Update.Request map(
    PurchaseInvoiceItemRequests.UpdateRequest request);

  public abstract PurchaseInvoiceItemMessages.Delete.Request map(
    PurchaseInvoiceItemRequests.DeleteRequest request);


  public abstract void pass(
    PurchaseInvoiceItemEntity from, @MappingTarget PurchaseInvoiceItemEntity to);


}




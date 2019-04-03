package pico.erp.purchase.invoice;

import java.util.Optional;
import kkojaeh.spring.boot.component.ComponentAutowired;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import pico.erp.company.CompanyData;
import pico.erp.company.CompanyId;
import pico.erp.company.CompanyService;
import pico.erp.invoice.InvoiceData;
import pico.erp.invoice.InvoiceId;
import pico.erp.invoice.InvoiceService;
import pico.erp.item.ItemData;
import pico.erp.item.ItemId;
import pico.erp.item.ItemService;
import pico.erp.item.spec.ItemSpecData;
import pico.erp.item.spec.ItemSpecId;
import pico.erp.item.spec.ItemSpecService;
import pico.erp.project.ProjectService;
import pico.erp.purchase.invoice.PurchaseInvoiceRequests.DetermineRequest;
import pico.erp.purchase.invoice.PurchaseInvoiceRequests.ReceiveRequest;
import pico.erp.purchase.order.PurchaseOrderData;
import pico.erp.purchase.order.PurchaseOrderId;
import pico.erp.purchase.order.PurchaseOrderService;
import pico.erp.shared.data.Auditor;
import pico.erp.user.UserData;
import pico.erp.user.UserId;
import pico.erp.user.UserService;

@Mapper
public abstract class PurchaseInvoiceMapper {

  @Autowired
  protected AuditorAware<Auditor> auditorAware;

  @ComponentAutowired
  protected ItemService itemService;

  @ComponentAutowired
  protected ItemSpecService itemSpecService;

  @ComponentAutowired
  private CompanyService companyService;

  @ComponentAutowired
  private UserService userService;

  @Lazy
  @Autowired
  private PurchaseInvoiceRepository purchaseInvoiceRepository;

  @ComponentAutowired
  private ProjectService projectService;

  @ComponentAutowired
  private PurchaseOrderService purchaseOrderService;

  @ComponentAutowired
  private InvoiceService invoiceService;

  protected Auditor auditor(UserId userId) {
    return Optional.ofNullable(userId)
      .map(userService::getAuditor)
      .orElse(null);
  }

  @Mappings({
    @Mapping(target = "orderId", source = "order.id"),
    @Mapping(target = "invoiceId", source = "invoice.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract PurchaseInvoiceEntity jpa(PurchaseInvoice data);

  public PurchaseInvoice jpa(PurchaseInvoiceEntity entity) {
    return PurchaseInvoice.builder()
      .id(entity.getId())
      .dueDate(entity.getDueDate())
      .order(map(entity.getOrderId()))
      .invoice(map(entity.getInvoiceId()))
      .remark(entity.getRemark())
      .status(entity.getStatus())
      .build();
  }

  protected UserData map(UserId userId) {
    return Optional.ofNullable(userId)
      .map(userService::get)
      .orElse(null);
  }

  protected CompanyData map(CompanyId companyId) {
    return Optional.ofNullable(companyId)
      .map(companyService::get)
      .orElse(null);
  }

  public PurchaseInvoice map(PurchaseInvoiceId purchaseInvoiceId) {
    return Optional.ofNullable(purchaseInvoiceId)
      .map(id -> purchaseInvoiceRepository.findBy(id)
        .orElseThrow(PurchaseInvoiceExceptions.NotFoundException::new)
      )
      .orElse(null);
  }

  protected ItemData map(ItemId itemId) {
    return Optional.ofNullable(itemId)
      .map(itemService::get)
      .orElse(null);
  }

  protected ItemSpecData map(ItemSpecId itemSpecId) {
    return Optional.ofNullable(itemSpecId)
      .map(itemSpecService::get)
      .orElse(null);
  }

  protected PurchaseOrderData map(PurchaseOrderId orderId) {
    return Optional.ofNullable(orderId)
      .map(purchaseOrderService::get)
      .orElse(null);
  }

  protected InvoiceData map(InvoiceId invoiceId) {
    return Optional.ofNullable(invoiceId)
      .map(invoiceService::get)
      .orElse(null);
  }

  @Mappings({
    @Mapping(target = "orderId", source = "order.id"),
    @Mapping(target = "invoiceId", source = "invoice.id")

  })
  public abstract PurchaseInvoiceData map(PurchaseInvoice purchaseInvoice);

  @Mappings({
    @Mapping(target = "order", source = "orderId")
  })
  public abstract PurchaseInvoiceMessages.Create.Request map(
    PurchaseInvoiceRequests.CreateRequest request);

  public abstract PurchaseInvoiceMessages.Update.Request map(
    PurchaseInvoiceRequests.UpdateRequest request);

  @Mappings({
    @Mapping(target = "invoice", source = "invoiceId")
  })
  public abstract PurchaseInvoiceMessages.Invoice.Request map(
    PurchaseInvoiceRequests.InvoiceRequest request);

  public abstract PurchaseInvoiceMessages.Determine.Request map(
    DetermineRequest request);

  public abstract PurchaseInvoiceMessages.Receive.Request map(
    ReceiveRequest request);

  public abstract PurchaseInvoiceMessages.Cancel.Request map(
    PurchaseInvoiceRequests.CancelRequest request);

  public abstract void pass(PurchaseInvoiceEntity from, @MappingTarget PurchaseInvoiceEntity to);


}



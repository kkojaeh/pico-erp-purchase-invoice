package pico.erp.purchase.invoice.item;

import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.invoice.item.InvoiceItemId;
import pico.erp.item.ItemId;
import pico.erp.item.spec.ItemSpecId;
import pico.erp.project.ProjectId;
import pico.erp.purchase.invoice.PurchaseInvoiceId;
import pico.erp.purchase.order.item.PurchaseOrderItemId;
import pico.erp.shared.TypeDefinitions;

public interface PurchaseInvoiceItemRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    PurchaseInvoiceItemId id;

    @Valid
    @NotNull
    PurchaseInvoiceId invoiceId;

    @Valid
    @NotNull
    PurchaseOrderItemId orderItemId;

    @NotNull
    @Min(0)
    BigDecimal quantity;

    @Size(max = TypeDefinitions.REMARK_LENGTH)
    String remark;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class UpdateRequest {

    @Valid
    @NotNull
    PurchaseInvoiceItemId id;

    @NotNull
    @Min(0)
    BigDecimal quantity;

    @Size(max = TypeDefinitions.REMARK_LENGTH)
    String remark;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class DeleteRequest {

    @Valid
    @NotNull
    PurchaseInvoiceItemId id;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class GenerateRequest {

    @Valid
    @NotNull
    PurchaseInvoiceId invoiceId;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class InvoiceRequest {

    @Valid
    @NotNull
    PurchaseInvoiceItemId id;

    @Valid
    @NotNull
    InvoiceItemId invoiceItemId;

  }
}
